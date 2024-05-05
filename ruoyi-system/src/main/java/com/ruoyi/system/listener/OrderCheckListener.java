package com.ruoyi.system.listener;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.enums.OrderStatus;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.ErrorCode;
import com.ruoyi.system.domain.Order;
import com.ruoyi.system.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 自动评价满分 监听器
 */
@Component
@RocketMQMessageListener(topic = "${daken.api.order.check.topic}",
        consumerGroup = "my-group")
@Slf4j
public class OrderCheckListener implements RocketMQListener<String> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onMessage(String s) {
        if (StrUtil.isBlank(s)) {
            return ;
        }
        Order order = JSON.parseObject(s, Order.class);
        log.info("收到定时评价信息 {}", order.getId());
        // 1. 检查订单状态
        if (Objects.equals(OrderStatus.NO_COMMIT.getCode(), order.getStatus())) {
            // 2. 和评价动作抢锁
            RLock lock = redissonClient.getLock("commit:lock" + order.getId());
            try {
                if (lock.tryLock(80, -1, TimeUnit.MILLISECONDS)) {
                    // 3. 改订单状态 && 自动评价满分
                    order.setStatus(OrderStatus.COMMIT.getCode());
                    order.setScore(5L);
                    orderMapper.updateOrder(order);
                }
            } catch (InterruptedException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } else {
            log.info("该订单已经被评价，不用自动评价 {}", order.getId());
        }

    }
}
