package com.ruoyi.order.publisher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruoyi.order.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * RocketMq 发送消息 向延时队列中发送消息
 *
 * @author daken
 */
@Component
@Slf4j
public class RocketMqPublisher {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketMqPublisher(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }


    @Value("${daken.api.order.topic}")
    private String topic;

    @Value("${daken.api.order.check.topic}")
    private String checkTopic;


    /**
     * 5分钟延时
     * @param order
     */
    public void sendOrderSnInfo(Order order){
        String message = JSON.toJSONString(order, new SerializerFeature[]{SerializerFeature.WriteClassName});
        // 延时队列
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build(), 3000, 9);
        log.info("收到订单：{}, 发送延时消息", order.getId());
    }


    /**
     * 10分钟延时
     * @param order
     */
    public void sendOrderTenInfo(Order order){
        String message = JSON.toJSONString(order, new SerializerFeature[]{SerializerFeature.WriteClassName});
        // 延时队列
        rocketMQTemplate.syncSend(checkTopic, MessageBuilder.withPayload(message).build(), 3000, 14);
    }

}
