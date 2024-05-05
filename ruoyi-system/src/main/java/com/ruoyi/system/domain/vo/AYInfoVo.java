package com.ruoyi.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AYInfoVo implements Serializable {

    /**
     * 阿姨名称
     */
    private String nickName;

    /**
     * 服务名称
     */
    private String typeName;

    /**
     * 服务详情
     */
    private String content;

    /**
     * 该阿姨的得分
     */
    private Double score;

    /**
     * 阿姨展示图片
     */
    private String imgUrl;
}
