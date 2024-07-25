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
}
