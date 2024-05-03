package com.ruoyi.order.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckRequest {

    /**
     * 订单号
     */
    private Long id;

    /**
     * 订单核销码
     */
    private String code;
}
