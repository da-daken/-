package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderVVo  implements Serializable {
    /** 主键id */
    private Long id;

    private Long pId;

    /** 家政员id */
    private Long bId;

    /** 普通用户id */
    private Long cId;

    /** 服务id */
    private Long productId;

    /** 当天的日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    /** 开始时间（上门时间） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 数量 */
    private double counts;

    /** 订单状态：0 订单创建，1 支付成功，2 订单取消，3 订单完成，4 待评价，5 已评价 */
    private Long status;

    /** 该订单服务的得分 */
    private Long score;
}
