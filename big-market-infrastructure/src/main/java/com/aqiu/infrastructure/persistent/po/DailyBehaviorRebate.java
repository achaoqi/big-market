package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 日常行为返利配置
 */
@Data
public class DailyBehaviorRebate {
//    自增ID
    private Long id;
//    行为类型 sign-签到 openai_pay-支付
    private String behaviorType;
//    返利描述
    private String rebateDesc;
//    返利类型
    private String rebateType;
//    返利配置
    private String rebateConfig;
//    状态 open开启 close关闭
    private String state;
    private Date createTime;
    private Date updateTime;
}
