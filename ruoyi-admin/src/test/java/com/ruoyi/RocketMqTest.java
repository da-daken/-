package com.ruoyi;

import com.ruoyi.order.domain.Order;
import com.ruoyi.order.mapper.OrderMapper;
import com.ruoyi.order.publisher.RocketMqPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RocketMqTest {

    @Autowired
    private RocketMqPublisher rocketMqPublisher;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void sendMsg() {
        Order order = orderMapper.selectOrderById(1L);
        rocketMqPublisher.sendOrderSnInfo(order);

    }
}
