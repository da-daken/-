package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.Order;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderVo extends Order implements Serializable {

    private String totalPrice;

}
