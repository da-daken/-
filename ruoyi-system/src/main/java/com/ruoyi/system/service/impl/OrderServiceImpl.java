package com.ruoyi.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.OrderStatus;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.ErrorCode;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanCopyUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.OrderVo;
import com.ruoyi.system.mapper.OrderMapper;
import com.ruoyi.system.publisher.RocketMqPublisher;
import com.ruoyi.system.mapper.ProductMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户、家政员Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RocketMqPublisher rocketMqPublisher;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ProductMapper productMapper;

    private final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy:MM:dd");
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");


    private static final Random random = new Random();

    /**
     * 查询订单
     * 
     * @param id 用户、家政员主键
     * @return 用户、家政员
     */
    @Override
    public Order selectOrderById(Long id)
    {
        return orderMapper.selectOrderById(id);
    }

    /**
     * 查询用户、家政员列表
     * 
     * @param order 用户、家政员
     * @return 用户、家政员
     */
    @Override
    public List<OrderVo> selectOrderList(Order order) {

        List<Order> orderList = orderMapper.selectOrderList(order);
        List<OrderVo> orderVoList = orderList.stream().map(order1 -> {
            PageUtils.startPage();
            Product product = productMapper.selectProductById(order1.getpId());
            OrderVo orderVo = BeanCopyUtils.copyBean(order1, OrderVo.class);
            orderVo.setTotalPrice(String.valueOf(order1.getCount() * product.getSingelPrice()));
            return orderVo;
        }).collect(Collectors.toList());

        return orderVoList;
    }


    /**
     * 创建订单
     * 
     * @param order 用户、家政员
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertOrder(Order order, HttpServletRequest request) {
        // 1. 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null == loginUser){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 2. 健壮性校验
        Long userId = order.getcId();
        Long productId = order.getProductId();
        Long count = order.getCount();
        Long bId = order.getbId();
        if (null == userId || null == productId || null == count || null == bId){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUserRole sysUserRole = sysUserRoleMapper.selectUserRoleById(userId);
        if (sysUserRole.getRoleId() == 1L || sysUserRole.getRoleId() == 101L){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "只有普通用户可以预约下单，请切换身份");
        }
        // 3. 防重令牌【令牌的对比和删除必须保证原子性】
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies) {
            if ("order-token".equals(cookie.getName())){
                token = cookie.getValue();
            }
        }
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList("order:token" + userId),
                token);
//        if (result == 0L){
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"提交太快了，请重新提交");
//        }

        // 4. 判断是否可以创建订单，防止并发创建两个重叠时间的订单
        if (!checkTimeLock(order)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该时间区间已经被占用，请刷新查看！");
        }

        // 5. 生成订单（在支付完订单后再生成核销码）
        order.setId(generateOrderSn(userId));
        order.setStatus(OrderStatus.NO_PAY.getCode());
        order.setCreateTime(DateUtils.getNowDate());
        try {
             // 发送一个定时任务，5分钟自动取消订单
             rocketMqPublisher.sendOrderSnInfo(order);
             return orderMapper.insertOrder(order);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单保存失败");
        }

    }

    /**
     * 判断是否可以创建订单，防止并发创建两个重叠时间的订单
     *
     * @param order
     * @return
     */
    private boolean checkTimeLock(Order order) {

        Date startTime = order.getStartTime();
        Date endTime = order.getEndTime();
        String dateTime = simpleDateFormat1.format(startTime);
        String start = simpleDateFormat2.format(startTime);
        String end = simpleDateFormat2.format(endTime);

        // 锁格式: timeBlockLock = bId : dateTime : time
        String timeBlockLock = order.getbId() + dateTime;
        RLock lock = redissonClient.getLock(timeBlockLock);

        // 获取锁，只允许同一时间一个用户能判断阿姨时间
        try {
            if (lock.tryLock(80, -1, TimeUnit.MILLISECONDS)){
                // 获取订单时间
                List<AYTime> orderTimes = getOrderTime1(order.getStartTime());
                int startNo = transformToInt(start);
                int endNo = transformToInt(end);
                for (int i = startNo; i <= endNo; i += 30){
                    if (checkInside(transformToString(i), orderTimes)){
                        return false;
                    }
                }
            }
        } catch (InterruptedException e){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        } finally {
            lock.unlock();
        }
        return true;
    }

    private int transformToInt(String time){
        String[] split = time.split(":");
        return Integer.parseInt(split[0] + split[1]);
    }

    private String transformToString(int i){
        int first = i / 100;
        int second = i % 100;
        return first + ":" + second;
    }


    /**
     * 判断时间是否在阿姨工作时间内 / 订单时间内
     * true -> 在
     * false -> 不在
     */
    @Override
    public boolean checkInside(String time, List<AYTime> ayTime) {
        String[] split1 = time.split(":");
        int i1 = Integer.parseInt(split1[0] + split1[1]);
        for (AYTime ayTime1 : ayTime) {
            String[] split2 = ayTime1.getStartTime().split(":");
            int i2 = Integer.parseInt(split2[0] + split2[1]);
            String[] split3 = ayTime1.getEndTime().split(":");
            int i3 = Integer.parseInt(split3[0] + split3[1]);
            if (i1 >= i2 && i1 <= i3){
                return true;
            }
        }
        return false;
    }

    @Override
    public AjaxResult getRoleInfo(Long userId) {
        return AjaxResult.success(sysUserRoleMapper.selectUserRoleById(userId));
    }

    /**
     * 获取阿姨当天订单的时间列表
     */
    @Override
    public List<AYTime> getOrderTime1(Date calDate) {
        List<OrderTime> orderTime = getOrderTime(calDate);
        return orderTime.stream().map(orderTime1 -> {
            String startTime = simpleDateFormat1.format(orderTime1.getStartTime());
            String endTime = simpleDateFormat1.format(orderTime1.getEndTime());
            return new AYTime(startTime, endTime);
        }).collect(Collectors.toList());
    }



    /**
     * 支付订单
     * @param id
     * @return
     */
    @Override
    public int payOrder(Long id) {
        if (null == id){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 幂等性保证：判断该订单是否被处理过
        Object o = redisTemplate.opsForValue().get("order:pay:" + id);
        if (null == o){

            // 和定时消息抢锁，防止在支付过程中被定时消息取消
            RLock lock = redissonClient.getLock("pay:lock:" + id);
            try {
                if (lock.tryLock(80, -1, TimeUnit.MILLISECONDS)){
                    // 抢到锁
                    // 是待支付状态才能支付
                    Order order = orderMapper.selectOrderById(id);
                    if (Objects.equals(OrderStatus.NO_PAY.getCode(), order.getStatus())){
                        redisTemplate.opsForValue().set("order:pay" + id, id);
                        order.setStatus(OrderStatus.PAY.getCode());
                        // 在支付完订单后 生成核验码
                        order.setCode(generateVfCode());
                        return orderMapper.updateOrder(order);
                    } else {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态异常，请刷新");
                    }

                }
            } catch (InterruptedException e){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        return 0;
    }


    /**
     * 取消订单
     * 在订单开始1小时前不可取消
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {
        Order order = orderMapper.selectOrderById(id);
        if (Objects.equals(OrderStatus.NO_PAY.getCode(), order.getStatus()) || Objects.equals(OrderStatus.PAY.getCode(), order.getStatus())){
            if (!checkOverTime(new Date(), order.getStartTime())){
                order.setStatus(OrderStatus.CANCEL.getCode());
                orderMapper.updateOrder(order);
            } else {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单还剩一小时开始，不可取消");
            }
        }
    }



    /**
     * 生成防重令牌：保证创建订单的接口幂等性
     * @param id
     * @param response
     * @return
     */
    @Override
    public AjaxResult generateToken(Long id, HttpServletResponse response) {
        if (null == id){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        //防重令牌
        String token = IdUtil.simpleUUID();
        redisTemplate.opsForValue().set("order:token" + id,token,30, TimeUnit.MINUTES);
        Cookie cookie = new Cookie("order-token",token);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        response.addCookie(cookie);
        return AjaxResult.success("生成防重令牌成功");
    }

    /**
     * 获取当天已支付、待支付订单的时间
     *
     * todo 待验证sql
     * @param calDate
     * @return
     */
    @Override
    public List<OrderTime> getOrderTime(Date calDate) {
        return orderMapper.getOrderTime(calDate);
    }

    /**
     * 修改订单（校验，打分）
     *
     * @param order 用户、家政员
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateOrder(Order order) {
        if (OrderStatus.PAY.getCode().equals(order.getStatus())){
            order = checkOrder(order);
        }
        else if (OrderStatus.NO_COMMIT.getCode().equals(order.getStatus())){
            order = commitOrder(order);
        }
        return orderMapper.updateOrder(order);
    }

    /**
     * 评价订单，给订单打分
     * 只有待评价状态才能评价打分
     *
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order commitOrder(Order order) {
        // 和定时消息抢锁，防止在评价过程中被定时消息覆盖
        RLock lock = redissonClient.getLock("commit:lock" + order.getId());
        try {
            if (lock.tryLock(80, -1, TimeUnit.MILLISECONDS)){
                // 抢到锁
                // 是待评价状态才能评价
                if (Objects.equals(OrderStatus.NO_COMMIT.getCode(), order.getStatus())){
                    order.setStatus(OrderStatus.COMMIT.getCode());
                    orderMapper.updateOrder(order);
                } else {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态异常，请刷新");
                }

            }
        } catch (InterruptedException e){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return order;
    }

    /**
     * 核销订单
     * 只有已支付状态才能核销
     *
     * @param newOrder
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order checkOrder(Order newOrder) {
        Order oldOrder = orderMapper.selectOrderById(newOrder.getId());
        // 只有已支付状态才能核销
        if (Objects.equals(OrderStatus.PAY.getCode(), oldOrder.getStatus())){
            if (oldOrder.getCode().equals(newOrder.getCode())){
                oldOrder.setStatus(OrderStatus.NO_COMMIT.getCode());
                orderMapper.updateOrder(oldOrder);

                // 发送一个2天内未评价自动评价为满分的定时消息

                rocketMqPublisher.sendOrderTenInfo(oldOrder);
                log.info("发送自动评价消息 订单 {}", oldOrder.getId());
            } else {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单核销码错误");
            }
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态异常，请刷新");
        }
        return oldOrder;
    }

    /**
     * 生成6位数的订单核销码
     */
    private String generateVfCode(){
        int randomNumber = random.nextInt(999999) + 100000; // 生成一个[100000, 999999]范围内的随机数
        String vfCode = String.format("%06d", randomNumber);
        return vfCode;
    }

    /**
     * 生成订单号, 雪花算法
     * @return
     */
    private Long generateOrderSn(Long userId){
        String timeId = IdWorker.getTimeId();
        String substring = timeId.substring(0, 12);
        return Long.valueOf(substring + RandomUtil.randomNumbers(2) + userId);

    }


    /**
     *
     * 判断当前时间是否在1小时内
     * @param nowTime
     * @param startTime
     * @return
     */
    private boolean checkOverTime(Date nowTime, Date startTime){
        return startTime.getTime() - nowTime.getTime() < 3600*1000;
    }



//    /**
//     * 批量删除用户、家政员
//     *
//     * @param ids 需要删除的用户、家政员主键
//     * @return 结果
//     */
//    @Override
//    public int deleteOrderByIds(Long[] ids)
//    {
//        return orderMapper.deleteOrderByIds(ids);
//    }

//    /**
//     * 删除用户、家政员信息
//     *
//     * @param id 用户、家政员主键
//     * @return 结果
//     */
//    @Override
//    public int deleteOrderById(Long id)
//    {
//        return orderMapper.deleteOrderById(id);
//    }


}
