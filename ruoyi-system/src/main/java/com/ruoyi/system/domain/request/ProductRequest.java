package com.ruoyi.system.domain.request;


import com.ruoyi.system.domain.Product;

public class ProductRequest extends Product {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
