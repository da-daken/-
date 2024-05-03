package com.ruoyi.order.domain;

/**
 * @author daken
 *
 * 阿姨工作时间
 */
public class AYTime {

    private String startTime;
    private String endTime;

    public AYTime(String startTime, String endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
