package com.ruoyi.order.listener;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.mapper.OrderMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${daken.api.order.check.topic}",
        consumerGroup = "my-group")
public class OrderCheckListener implements RocketMQListener<String> {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void onMessage(String s) {
        if (StrUtil.isBlank(s)) {
            return ;
        }
        Order order = JSON.parseObject(s, Order.class);
        order.setScore(5L);
        orderMapper.updateOrder(order);
    }
}
