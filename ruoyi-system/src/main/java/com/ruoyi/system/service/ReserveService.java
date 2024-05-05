package com.ruoyi.system.service;

import com.ruoyi.system.domain.EnableTime;

import java.util.Date;
import java.util.List;

/**
 * @author daken
 *
 * 预约模块的服务接口
 */
public interface ReserveService {

    /**
     * 查找家政员可预约的时间数组
     */
    List<EnableTime> checkReserveTime(Date calDate, Long bId);
}
