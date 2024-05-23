package com.ruoyi.system.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.AYTime;
import com.ruoyi.system.domain.Order;
import com.ruoyi.system.domain.OrderTime;
import com.ruoyi.system.domain.vo.OrderVVo;
import com.ruoyi.system.domain.vo.OrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户、家政员Service接口
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
public interface IOrderService 
{
    /**
     * 查询用户、家政员
     * 
     * @param id 用户、家政员主键
     * @return 用户、家政员
     */
    public OrderVVo selectOrderById(Long id);

    /**
     * 查询用户、家政员列表
     * 
     * @param order 用户、家政员
     * @return 用户、家政员集合
     */
    public List<OrderVo> selectOrderList(Order order);

    /**
     * 创建订单
     * 
     * @param order 用户、家政员
     * @return 结果
     */
    public int insertOrder(Order order, HttpServletRequest request);

    /**
     * 生成防重令牌
     * @param id
     * @param response
     * @return
     */
    public AjaxResult generateToken(Long id, HttpServletResponse response);

    public Order cancelOrder(Long id);

    public Order payOrder(Long id);

    /**
     * 修改用户、家政员
     *
     * @param order 用户、家政员
     * @return 结果
     */
    public int updateOrder(Order order, String operate);

//    /**
//     * 批量删除用户、家政员
//     *
//     * @param ids 需要删除的用户、家政员主键集合
//     * @return 结果
//     */
//    public int deleteOrderByIds(Long[] ids);

//    /**
//     * 删除用户、家政员信息
//     *
//     * @param id 用户、家政员主键
//     * @return 结果
//     */
//    public int deleteOrderById(Long id);

    /**
     * 获取当天已支付订单的时间
     * @param calDate
     * @return
     */
    List<OrderTime> getOrderTime(Date calDate);

    /**
     * 评价订单，给订单打分
     * @param order
     */
    Order commitOrder(Order order);

    /**
     * 核销订单
     * @param checkRequest
     */
    Order checkOrder(Order order);

    List<AYTime> getOrderTime1(Date calDate);

    boolean checkInside(String time, List<AYTime> ayTime);

    AjaxResult getRoleInfo(Long userId);
}
