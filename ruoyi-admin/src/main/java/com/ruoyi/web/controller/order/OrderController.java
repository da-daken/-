package com.ruoyi.web.controller.order;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanCopyUtils;
import com.ruoyi.system.domain.Product;
import com.ruoyi.system.domain.vo.OrderVo;
import com.ruoyi.system.mapper.OrderMapper;
import com.ruoyi.system.mapper.ProductMapper;
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
    @PreAuthorize("@ss.hasPermi('order:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(Order order) {
        startPage();
        if (SecurityUtils.getUserId() != 1L){
            order.setcId(SecurityUtils.getUserId());
        }
        List<Order> orderList = orderMapper.selectOrderList(order);
        List<OrderVo> orderVoList = orderList.stream().map(order1 -> {
            PageUtils.startPage();
            Product product = productMapper.selectProductById(order1.getpId());
            OrderVo orderVo = BeanCopyUtils.copyBean(order1, OrderVo.class);
            orderVo.setTotalPrice(String.valueOf(order1.getCount() * product.getSingelPrice()));
            return orderVo;
        }).collect(Collectors.toList());

        return getDataTable(orderVoList, orderList);
    }

    /**
     * 获取订单详情
     */
    @PreAuthorize("@ss.hasPermi('order:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(orderService.selectOrderById(id));
    }

    @PreAuthorize("@ss.hasPermi('order:order:add')")
    @Log(title = "创建订单", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public AjaxResult createOrder(@RequestBody Order order, HttpServletRequest request){
        return AjaxResult.success(orderService.insertOrder(order, request));
    }

    @Log(title = "生成防重令牌", businessType = BusinessType.INSERT)
    @GetMapping("/generateToken")
    public AjaxResult generateToken(Long id, HttpServletResponse response){
        return AjaxResult.success(orderService.generateToken(id, response));
    }

    @PreAuthorize("@ss.hasPermi('order:order:pay')")
    @Log(title = "支付订单")
    @PostMapping("/parOrder")
    public AjaxResult payOrder(@RequestBody Long id){
        return AjaxResult.success(orderService.payOrder(id));
    }

    @PreAuthorize("@ss.hasPermi('order:order:cancel')")
    @Log(title = "取消订单")
    @PostMapping("/cancelOrder")
    public AjaxResult cancelOrder(@RequestBody Long id){
        orderService.cancelOrder(id);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('order:order:add')")
    @Log(title = "评价订单")
    @PostMapping("/commit")
    public AjaxResult commitOrder(@RequestBody Order order){
        orderService.commitOrder(order);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('order:order:add')")
    @Log(title = "订单核销")
    @PostMapping("/check")
    public AjaxResult checkOrder(@RequestBody Order order){
        orderService.checkOrder(order);
        return AjaxResult.success();
    }

    /**
     * 测试创建订单
     */
    @PreAuthorize("@ss.hasPermi('order:order:add')")
    @Log(title = "用户、家政员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Order order,  HttpServletRequest request)
    {
        return AjaxResult.success(orderService.insertOrder(order, request));
    }


    @PreAuthorize("@ss.hasPermi('order:order:edit')")
    @Log(title = "用户、家政员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Order order)
    {
        return toAjax(orderService.updateOrder(order));
    }


//    @PreAuthorize("@ss.hasPermi('order:order:remove')")
//    @Log(title = "用户、家政员", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(orderService.deleteOrderByIds(ids));
//    }
}
