package com.aqiu.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBehaviorRebateVO {
    //    行为类型 sign-签到 openai_pay-支付
    private String behaviorType;
    //    返利描述
    private String rebateDesc;
    //    返利类型
    private String rebateType;
    //    返利配置
    private String rebateConfig;
}
