package com.aqiu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleMatterEntity {
//    用户ID
    private String userId;
//    策略ID
    private Integer strategyId;
//    抽奖奖品ID 规则类型为策略，不需要奖品
    private Integer awardId;
//    抽奖规则类型 rule_random 随机值计算 rule_lock抽奖几次后解锁 rule_luck_award 幸运奖
    private String ruleModel;
}
