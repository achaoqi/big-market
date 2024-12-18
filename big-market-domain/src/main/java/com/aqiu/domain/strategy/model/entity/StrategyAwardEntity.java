package com.aqiu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardEntity {
    /**
     * 抽奖策略ID
     */
    private Integer strategyId;
    /**
     * 奖品ID
     */
    private Integer awardId;
    /**
     * 奖品父标题
     */
    private String awardTitle;
    /**
     * 副标题
     */
    private String awardSubtitle;
    /**
     * 奖品数量
     */
    private Integer awardCount;
    /**
     * 奖品剩余数量
     */
    private Integer awardCountSurplus;
    /**
     * 奖品中将概率
     */
    private BigDecimal awardRate;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 奖品规则模型
     */
    private String ruleModels;
}
