package com.ruoyi.order.domain;

import java.util.Date;

/**
 * @author daken
 *
 * 订单的开始时间和结束时间
 */
public class OrderTime {
    private Date startTime;
    private Date endTime;

    public OrderTime(Date startTime, Date endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
