package com.ruoyi.order.domain;

import java.util.Date;

/**
 * @author daken
 *
 * 返回给前端的预约时间对象
 */
public class EnableTime {

    /**
     * 时间
     */
    private String time;

    /**
     * 是否可预约 true -> 可预约, false -> 不可预约
     */
    private boolean enable;


    public EnableTime(String time, boolean enable) {
        this.time = time;
        this.enable = enable;
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
