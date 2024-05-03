package com.ruoyi.order.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户、家政员对象 order
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
public class Order extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 家政员id */
    @Excel(name = "家政员id")
    private Long bId;

    /** 普通用户id */
    @Excel(name = "普通用户id")
    private Long cId;

    /** 服务id */
    @Excel(name = "服务id")
    private Long productId;

    /** 当天的日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "当天的日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateTime;

    /** 开始时间（上门时间） */
    @Excel(name = "开始时间", readConverterExp = "上=门时间")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 数量 */
    @Excel(name = "数量")
    private Long counts;

    /** 订单状态：0 订单创建，1 支付成功，2 订单取消，3 订单完成，4 待评价，5 已评价 */
    @Excel(name = "订单状态：0 订单创建，1 支付成功，2 订单取消，3 订单完成，4 待评价，5 已评价")
    private Long status;

    /** 6位数的订单核销码 */
    @Excel(name = "6位数的订单核销码")
    private String code;

    /** 该订单服务的得分 */
    @Excel(name = "该订单服务的得分")
    private Long score;

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
    public void setcId(Long cId) 
    {
        this.cId = cId;
    }

    public Long getcId() 
    {
        return cId;
    }
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setDateTime(Date dateTime) 
    {
        this.dateTime = dateTime;
    }

    public Date getDateTime() 
    {
        return dateTime;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setCount(Long count) 
    {
        this.counts = count;
    }

    public Long getCount() 
    {
        return counts;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bId", getbId())
            .append("cId", getcId())
            .append("productId", getProductId())
            .append("dateTime", getDateTime())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("count", getCount())
            .append("createTime", getCreateTime())
            .append("status", getStatus())
            .append("code", getCode())
            .append("score", getScore())
            .toString();
    }
}
