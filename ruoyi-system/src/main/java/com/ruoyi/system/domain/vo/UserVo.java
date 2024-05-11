package com.ruoyi.system.domain.vo;

import com.ruoyi.common.core.domain.entity.SysUser;

public class UserVo extends SysUser {
    private Long role;

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }
}
