package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 product
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
public class Product extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 提供该服务的家政员id */
    @Excel(name = "提供该服务的家政员id")
    private Long userId;

    /** 服务类型id */
    @Excel(name = "服务类型id")
    private Long typeId;

    /** 阿姨展示图片 */
    @Excel(name = "阿姨展示图片")
    private String img;

    /** 服务详情（阿姨再说一次） */
    @Excel(name = "服务详情", readConverterExp = "阿姨再说一次")
    private String content;

    /** 单价（50 / hour, 15 / 平米） */
    @Excel(name = "单价", readConverterExp = "5=0,/=,h=our,,1=5,/=,平=米")
    private Long singelPrice;


    /** 该家政员在此服务的得分 */
    @Excel(name = "该家政员在此服务的得分")
    private Long score;

    /** 是否被删除 */
    @Excel(name = "是否被删除")
    private String isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTypeId(Long typeId) 
    {
        this.typeId = typeId;
    }

    public Long getTypeId() 
    {
        return typeId;
    }
    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setSingelPrice(Long singelPrice) 
    {
        this.singelPrice = singelPrice;
    }

    public Long getSingelPrice() 
    {
        return singelPrice;
    }

    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }
    public void setIsDeleted(String isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public String getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("typeId", getTypeId())
            .append("img", getImg())
            .append("content", getContent())
            .append("singelPrice", getSingelPrice())
            .append("score", getScore())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .toString();
    }
}
