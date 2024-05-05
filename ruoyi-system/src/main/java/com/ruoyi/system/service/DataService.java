package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.AYInfoVo;
import com.ruoyi.system.domain.vo.HotVo;

import java.util.Date;
import java.util.List;

public interface DataService {

    /**
     * 所有订单的数据，返回每个服务的最高分的家政员
     *
     */
    List<AYInfoVo> goldenService();

    /**
     * 所有订单数据，返回每个服务得分前几（或者全部）的家政员
     *
     * @param
     */
    List<AYInfoVo> topGoldenService();

    /**
     * 所有订单数据，返回当前平台每个月的不同服务需求的热度(数量)
     *
     * @param year 以哪一年为参数，
     * 返回一个数组，1 - 12月的服务需求热度
     */
    List<HotVo> hotService(Date year);
}
