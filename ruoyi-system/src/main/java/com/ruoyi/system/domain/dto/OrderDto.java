package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.Order;


public class OrderDto extends Order {
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
