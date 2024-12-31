package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserCreditOrder {
//    自增ID
    private Long id;
//    用户ID
    private String userId;
//    订单ID
    private String orderId;
//    交易名称
    private String tradeName;
//    交易类型
    private String tradeType;
//    交易金额
    private BigDecimal tradeAmount;
//    业务防重ID
    private String outBusinessNo;
//    创建时间
    private Date createTime;
//    更新时间
    private Date updateTime;
}
