package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 用户行为返利订单
 */
@Data
public class UserBehaviorRebateOrder {
//    自增ID
    private Long id;
//    用户ID
    private String userId;
//    订单ID
    private String orderId;
//    行为类型 sign-签到 openai_pay-支付
    private String behaviorType;
//    返利描述
    private String rebateDesc;
//    返利类型
    private String rebateType;
//    返利配置
    private String rebateConfig;
//    业务ID -拼接的唯一值
    private String bizId;
    private Date createTime;
    private Date updateTime;
}
