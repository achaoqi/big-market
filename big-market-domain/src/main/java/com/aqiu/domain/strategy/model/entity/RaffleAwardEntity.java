package com.aqiu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {
    private Integer awardId;
    private Integer strategyId;
    private String awardKey;
    private String awardConfig;
    private String awardDesc;
}
