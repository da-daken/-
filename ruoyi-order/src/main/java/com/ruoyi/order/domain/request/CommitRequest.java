package com.ruoyi.order.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author daken
 *
 */
@Data
@AllArgsConstructor
public class CommitRequest implements Serializable {

    /**
     * 订单号
     */
    private Long id;

    /**
     * 订单得分
     */
    private Long score;
}
