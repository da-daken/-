package com.ruoyi;

import com.ruoyi.system.domain.Order;
import com.ruoyi.system.mapper.OrderMapper;
import com.ruoyi.system.publisher.RocketMqPublisher;
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
