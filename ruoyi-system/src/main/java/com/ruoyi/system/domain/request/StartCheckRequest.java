package com.ruoyi.system.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class StartCheckRequest implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户审核资料
     */
    private String checkInfo;
}
