package com.aqiu.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行为返利订单实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateOrderEntity {
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
    //    业务仿重ID
    private String outBusinessNo;
}
