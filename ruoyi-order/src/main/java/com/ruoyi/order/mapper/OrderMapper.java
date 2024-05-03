package com.ruoyi.order.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.domain.OrderTime;

/**
 * 用户、家政员Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
public interface OrderMapper 
{
    /**
     * 查询用户、家政员
     * 
     * @param id 用户、家政员主键
     * @return 用户、家政员
     */
    public Order selectOrderById(Long id);

    /**
     * 查询用户、家政员列表
     * 
     * @param order 用户、家政员
     * @return 用户、家政员集合
     */
    public List<Order> selectOrderList(Order order);

    /**
     * 新增用户、家政员
     * 
     * @param order 用户、家政员
     * @return 结果
     */
    public int insertOrder(Order order);

    /**
     * 修改用户、家政员
     * 
     * @param order 用户、家政员
     * @return 结果
     */
    public int updateOrder(Order order);

    /**
     * 删除用户、家政员
     * 
     * @param id 用户、家政员主键
     * @return 结果
     */
    public int deleteOrderById(Long id);

    /**
     * 批量删除用户、家政员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderByIds(Long[] ids);

    /**
     * 获取当天已支付订单的时间
     * @param calDate
     * @return
     */
    List<OrderTime> getOrderTime(Date calDate);
}
