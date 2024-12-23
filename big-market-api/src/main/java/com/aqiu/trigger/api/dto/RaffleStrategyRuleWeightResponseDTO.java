package com.aqiu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleStrategyRuleWeightResponseDTO {
//    特殊奖品权重
    private Integer ruleWeightCount;
//    用户抽奖次数
    private Integer userActivityAccountTotalUseCount;
//    特殊奖品集合
    private List<StrategyAward> strategyAwards;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAward{
        private Integer awardId;
        private String awardTitle;
    }
}
