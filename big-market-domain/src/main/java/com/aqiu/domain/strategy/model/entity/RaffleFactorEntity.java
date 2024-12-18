package com.aqiu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 抽奖因子实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleFactorEntity {
    private String userId;
    private Integer strategyId;
    /**
     * 结束时间
     */
    private Date endDateTime;
}
