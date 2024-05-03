package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 work_time
 * 
 * @author ruoyi
 * @date 2024-04-02
 */
public class WorkTime extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一主键 */
    private Long id;

    /** 家政员id */
    @Excel(name = "家政员id")
    private Long bId;

    /** 服务类型id */
    @Excel(name = "服务类型id")
    private Long pId;

    /** 周一时间段JSON */
    @Excel(name = "周一时间段JSON")
    private String mon;

    /** 周二时间段JSON */
    @Excel(name = "周二时间段JSON")
    private String tues;

    /** 周三时间段JSON */
    @Excel(name = "周三时间段JSON")
    private String wed;

    /** 周四时间段JSON */
    @Excel(name = "周四时间段JSON")
    private String thur;

    /** 周五时间段JSON */
    @Excel(name = "周五时间段JSON")
    private String fri;

    /** 周六时间段JSON */
    @Excel(name = "周六时间段JSON")
    private String sat;

    /** 周天时间段JSON */
    @Excel(name = "周天时间段JSON")
    private String sun;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setbId(Long bId) 
    {
        this.bId = bId;
    }

    public Long getbId() 
    {
        return bId;
    }
    public void setpId(Long pId) 
    {
        this.pId = pId;
    }

    public Long getpId() 
    {
        return pId;
    }
    public void setMon(String mon) 
    {
        this.mon = mon;
    }

    public String getMon() 
    {
        return mon;
    }
    public void setTues(String tues) 
    {
        this.tues = tues;
    }

    public String getTues() 
    {
        return tues;
    }
    public void setWed(String wed) 
    {
        this.wed = wed;
    }

    public String getWed() 
    {
        return wed;
    }
    public void setThur(String thur) 
    {
        this.thur = thur;
    }

    public String getThur() 
    {
        return thur;
    }
    public void setFri(String fri) 
    {
        this.fri = fri;
    }

    public String getFri() 
    {
        return fri;
    }
    public void setSat(String sat) 
    {
        this.sat = sat;
    }

    public String getSat() 
    {
        return sat;
    }
    public void setSun(String sun) 
    {
        this.sun = sun;
    }

    public String getSun() 
    {
        return sun;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bId", getbId())
            .append("pId", getpId())
            .append("mon", getMon())
            .append("tues", getTues())
            .append("wed", getWed())
            .append("thur", getThur())
            .append("fri", getFri())
            .append("sat", getSat())
            .append("sun", getSun())
            .toString();
    }
}
