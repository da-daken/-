package com.ruoyi.common.enums;

public enum CheckStatus {

    WAIT_CHECK(2L, "待审核"),
    CHECK_FAILURE(0L, "不通过"),
    CHECK_SUCCESS(1L, "通过");

    private final Long code;
    private final String info;

    CheckStatus(Long code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Long getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
