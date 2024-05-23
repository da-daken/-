package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.Order;
import lombok.Data;

import java.io.Serializable;


public class OrderVo extends Order implements Serializable {

    private String totalPrice;

    private String productName;

    private String bName;
    private String cName;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}
