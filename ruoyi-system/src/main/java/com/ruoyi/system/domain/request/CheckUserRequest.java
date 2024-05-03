package com.ruoyi.system.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CheckUserRequest implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 审核操作
     */
    private String operate;

    /**
     * 审核批注
     */
    private String remark;
}
