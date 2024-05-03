package com.ruoyi.web.controller.order;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.order.domain.request.CommitRequest;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.service.IOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.order.domain.request.CheckRequest;

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
        List<Order> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 获取用户、家政员详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
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

    @Log(title = "支付订单")
    @PostMapping("/parOrder")
    public AjaxResult payOrder(@RequestBody Long id){
        return AjaxResult.success(orderService.payOrder(id));
    }

    @Log(title = "取消订单")
    @PostMapping("/cancelOrder")
    public AjaxResult cancelOrder(@RequestBody Long id){
        orderService.cancelOrder(id);
        return AjaxResult.success();
    }

    @Log(title = "评价订单")
    @PostMapping("/commit")
    public AjaxResult commitOrder(@RequestBody CommitRequest commitRequest){
        orderService.commitOrder(commitRequest);
        return AjaxResult.success();
    }

    @Log(title = "订单核销")
    @PostMapping("/check")
    public AjaxResult checkOrder(@RequestBody CheckRequest checkRequest){
        orderService.checkOrder(checkRequest);
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


//    @PreAuthorize("@ss.hasPermi('order:order:edit')")
//    @Log(title = "用户、家政员", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Order order)
//    {
//        return toAjax(orderService.updateOrder(order));
//    }


//    @PreAuthorize("@ss.hasPermi('order:order:remove')")
//    @Log(title = "用户、家政员", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(orderService.deleteOrderByIds(ids));
//    }
}
