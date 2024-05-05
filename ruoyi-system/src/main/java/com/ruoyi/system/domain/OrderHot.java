package com.ruoyi.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OrderHot implements Serializable {
    private Long productId;

    private Integer month;

    private Long sum;
}
