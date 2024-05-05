package com.ruoyi.system.domain.request;

import java.io.Serializable;
import java.util.Date;

public class EnableTimeRequest implements Serializable {
    private Date calDate;
    private Long bId;

    public EnableTimeRequest(Date calDate, Long bId) {
        this.calDate = calDate;
        this.bId = bId;
    }

    public Date getCalDate() {
        return calDate;
    }

    public void setCalDate(Date calDate) {
        this.calDate = calDate;
    }

    public Long getbId() {
        return bId;
    }

    public void setbId(Long bId) {
        this.bId = bId;
    }
}
