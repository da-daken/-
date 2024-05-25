package com.ruoyi.system.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.enums.OrderStatus;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.ErrorCode;
import com.ruoyi.system.domain.Order;
import com.ruoyi.system.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
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
        Order newOrder = orderMapper.selectOrderById(order.getId());
        log.info("收到定时取消信息 {}", newOrder.getId());
        // 1. 检查订单状态
        if (Objects.equals(OrderStatus.NO_PAY.getCode(), newOrder.getStatus())) {
            // 2. 和支付动作抢锁
            RLock lock = redissonClient.getLock("pay:lock:" + newOrder.getId());
            try {
                if (lock.tryLock(80, -1, TimeUnit.MILLISECONDS)) {
                    // 改变订单状态为取消
                    newOrder.setStatus(OrderStatus.CANCEL.getCode());
                    orderMapper.updateOrder(newOrder);
                    log.info("收到未支付订单 {} , 现将其取消！", newOrder.getId());
                }
            } catch (InterruptedException e){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } else {
            log.info("该订单状态已改变，不用取消订单 {}", newOrder.getId());
        }

    }
}
