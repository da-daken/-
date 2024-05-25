package com.ruoyi.web.controller.order;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanCopyUtils;
import com.ruoyi.system.domain.Product;
import com.ruoyi.system.domain.ProductType;
import com.ruoyi.system.domain.dto.OrderDto;
import com.ruoyi.system.domain.request.OrderRequest;
import com.ruoyi.system.domain.vo.OrderVo;
import com.ruoyi.system.mapper.OrderMapper;
import com.ruoyi.system.mapper.ProductMapper;
import com.ruoyi.system.mapper.ProductTypeMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Order;
import com.ruoyi.system.service.IOrderService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户、家政员Controller
 * 
 * @author daken
 * @date 2024-03-17
 */
@RestController
@RequestMapping("/order/order")
public class OrderController extends BaseController
{
    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     * 获取角色id
     * @param userId
     * @return
     */
    @GetMapping("/getRoleInfo")
    public AjaxResult getRoleInfo(Long userId) {
        return orderService.getRoleInfo(userId);
    }

    /**
     * 查询订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderDto order1) {
        startPage();
        // 管理员显示所有订单
        if (!order1.getRoleId().equals("admin")){
            // 普通用户显示自己下的订单
            if (order1.getRoleId().equals("normal")){
                order1.setcId(SecurityUtils.getUserId());
            } else if (order1.getRoleId().equals("service")){
                // 家政员显示收到的订单
                order1.setbId(SecurityUtils.getUserId());
            }
        }
        Order order = BeanCopyUtils.copyBean(order1, Order.class);
        List<Order> orderList = orderMapper.selectOrderList(order);
        List<OrderVo> orderVoList = orderList.stream().map(order2 -> {
            PageUtils.startPage();
            Product product = productMapper.selectProductById(order2.getpId());
            ProductType productType = productTypeMapper.selectProductTypeById(product.getTypeId());
            SysUser bUser = sysUserMapper.selectUserById(order2.getbId());
            SysUser cUser = sysUserMapper.selectUserById(order2.getcId());
            OrderVo orderVo = BeanCopyUtils.copyBean(order2, OrderVo.class);
            orderVo.setProductName(productType.getName());
            orderVo.setbName(bUser.getNickName());
            orderVo.setcName(cUser.getNickName());
            orderVo.setTotalPrice(String.valueOf(order2.getCounts() * product.getSingelPrice()));
            // 家政员端显示客户的电话号码
            if (order1.getRoleId().equals("service")) {
                orderVo.setPhoneNumber(cUser.getPhonenumber());
            }
            // 普通用户显示家政员的电话号码
            else if (order1.getRoleId().equals("normal")) {
                orderVo.setPhoneNumber(bUser.getPhonenumber());
            }
            // 管理员不显示电话号码
            return orderVo;
        }).collect(Collectors.toList());

        // 查询家政员名称
        if (order1.getUsername() != null && !order1.getUsername().isEmpty()){
            orderVoList = orderVoList.stream().filter(orderVo -> orderVo.getbName().equals(order1.getUsername())).collect(Collectors.toList());
        }

        // 查询服务类型名称
        if (order1.getProductTypeName() != null && !order1.getProductTypeName().isEmpty()){
            orderVoList = orderVoList.stream().filter(orderVo -> orderVo.getProductName().equals(order1.getProductTypeName())).collect(Collectors.toList());
        }

        return getDataTable(orderVoList, orderList);
    }

    /**
     * 获取订单详情
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(orderService.selectOrderById(id));
    }

    @Log(title = "创建订单", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public AjaxResult createOrder(@RequestBody Order order, HttpServletRequest request){
        return AjaxResult.success(orderService.insertOrder(order, request));
    }

    /**
     * 取消、支付、核销、评价 (core方法)
     *
     * @param orderRequest
     * @return
     */
    @Log(title = "用户、家政员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderRequest orderRequest) {
        Order order = BeanCopyUtils.copyBean(orderRequest, Order.class);
        return toAjax(orderService.updateOrder(order, orderRequest.getOperate()));
    }


    @Log(title = "生成防重令牌", businessType = BusinessType.INSERT)
    @GetMapping("/generateToken")
    public AjaxResult generateToken(Long id, HttpServletResponse response){
        return AjaxResult.success(orderService.generateToken(id, response));
    }

//    @Log(title = "支付订单")
//    @PostMapping("/parOrder")
//    public AjaxResult payOrder(@RequestBody Long id){
//        return AjaxResult.success(orderService.payOrder(id));
//    }
//
//    @Log(title = "取消订单")
//    @PostMapping("/cancelOrder")
//    public AjaxResult cancelOrder(@RequestBody Long id){
//        orderService.cancelOrder(id);
//        return AjaxResult.success();
//    }
//
//    @Log(title = "评价订单")
//    @PostMapping("/commit")
//    public AjaxResult commitOrder(@RequestBody Order order){
//        orderService.commitOrder(order);
//        return AjaxResult.success();
//    }
//
//    @Log(title = "订单核销")
//    @PostMapping("/check")
//    public AjaxResult checkOrder(@RequestBody Order order){
//        orderService.checkOrder(order);
//        return AjaxResult.success();
//    }

    /**
     * 测试创建订单
     */
    @Log(title = "用户、家政员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Order order,  HttpServletRequest request)
    {
        return AjaxResult.success(orderService.insertOrder(order, request));
    }





//    @PreAuthorize("@ss.hasPermi('order:order:remove')")
//    @Log(title = "用户、家政员", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(orderService.deleteOrderByIds(ids));
//    }
}
