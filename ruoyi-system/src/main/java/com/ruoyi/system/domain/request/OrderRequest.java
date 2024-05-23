package com.ruoyi.system.domain.request;

import com.ruoyi.system.domain.Order;


public class OrderRequest extends Order {

    /**
     * 订单操作
     */
    private String operate;

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
