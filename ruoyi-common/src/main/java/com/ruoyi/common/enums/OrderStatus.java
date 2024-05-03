package com.ruoyi.common.enums;

/**
 * @author daken
 *
 * 订单状态
 */
public enum OrderStatus {

    NO_PAY(0L, "待支付"),
    PAY(1L, "已支付"),
    CANCEL(2L, "订单取消"),
    NO_COMMIT(3L, "待评价"),
    COMMIT(4L, "已评价");


    private final Long code;
    private final String info;

    OrderStatus(Long code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Long getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}

