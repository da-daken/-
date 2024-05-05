package com.ruoyi.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotVo {

    /**
     * 日常保洁的数量
     */
    private Long sum1;

    /**
     * 擦玻璃的数量
     */
    private Long sum2;
}
