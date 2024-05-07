package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.Order;


public class OrderDto extends Order {
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
