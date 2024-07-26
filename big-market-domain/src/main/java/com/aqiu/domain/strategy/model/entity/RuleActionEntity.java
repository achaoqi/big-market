package com.aqiu.domain.strategy.model.entity;

import com.aqiu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity <T extends RuleActionEntity.RaffleEntity> {
    private String code=RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info=RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity{

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity{
        //        策略ID
        private Integer strategyId;
        //        权重值Key,抽奖时可以选择权重抽奖
        private String ruleWeightValueKey;
        //        奖品ID
        private Integer awardId;
    }

    static public class RaffleCenterEntity extends RaffleEntity{

    }

    static public class RaffleAfterEntity extends RaffleEntity{

    }
}
