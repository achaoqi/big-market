package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyRule {
    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 抽奖策略ID
     */
    private Integer strategyId;
    /**
     * 抽奖商品ID
     */
    private Integer awardId;
    /**
     * 规则类型 1-策略 2-商品
     */
    private Integer ruleType;
    /**
     * 抽奖规则类型【rule_lock】
     */
    private String ruleModel;
    /**
     * 抽奖规则比值
     */
    private String ruleValue;
    /**
     * 抽奖规则描述
     */
    private String ruleDesc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
