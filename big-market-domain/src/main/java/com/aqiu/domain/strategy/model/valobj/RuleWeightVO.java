package com.aqiu.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleWeightVO {
    private Integer weight;
    private String ruleValue;
    private List<Integer> awardIds;
    private List<Award> awardList;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Award{
        private Integer awardId;
        private String awardTitle;
    }
}
