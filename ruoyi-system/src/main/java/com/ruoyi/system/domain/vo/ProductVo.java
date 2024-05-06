package com.ruoyi.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVo implements Serializable {

    private Long id;

    private String username;

    private String productName;
    private String img;
    private String content;
    private Long singelPrice;
    private Long score;
    private String unit;
}
