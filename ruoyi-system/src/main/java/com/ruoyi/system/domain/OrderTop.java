package com.ruoyi.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OrderTop implements Serializable {
    private Long pId;
    private Long bId;
    private Double avgScore;
}
