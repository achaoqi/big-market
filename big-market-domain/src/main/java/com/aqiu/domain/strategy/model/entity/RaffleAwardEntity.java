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
    private String awardConfig;
    private Integer sort;
    /** 奖品标题（名称） */
    private String awardTitle;
}
