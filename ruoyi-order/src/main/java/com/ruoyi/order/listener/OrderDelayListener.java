package com.ruoyi.order.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.enums.OrderStatus;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.ErrorCode;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.mapper.OrderMapper;
import com.ruoyi.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RocketMQMessageListener(topic = "daken-order",
        consumerGroup = "my-group1", consumeMode = ConsumeMode.CONCURRENTLY)
public class OrderDelayListener implements RocketMQListener<String> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedissonClient redissonClient;


    /**
     * 收到定时取消订单的消息
     *
     * @param s
     */
    @Override
    public void onMessage(String s) {
        if (StrUtil.isBlank(s)) {
            return ;
        }
        Order order = JSON.parseObject(s, Order.class);
        log.info("收到信息{}", order.getId());
        // 1. 检查订单状态
        if (Objects.equals(OrderStatus.NO_PAY.getCode(), order.getStatus())) {
            // 2. 和支付动作抢锁
            RLock lock = redissonClient.getLock("pay:lock:" + order.getId());
            try {
                if (lock.tryLock(80, -1, TimeUnit.MILLISECONDS)) {
                    // 改变订单状态为取消
                    order.setStatus(OrderStatus.CANCEL.getCode());
                    orderMapper.updateOrder(order);
                    log.info("收到未支付订单 {} , 现将其取消！", order.getId());
                }
            } catch (InterruptedException e){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            } finally {
                lock.unlock();
            }
        }


    }
}
