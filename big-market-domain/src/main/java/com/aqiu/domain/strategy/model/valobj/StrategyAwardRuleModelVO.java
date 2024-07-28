package com.aqiu.domain.strategy.model.valobj;

import com.aqiu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.aqiu.types.common.Constants;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Builder
public class StrategyAwardRuleModelVO {
    /**
     * 规则模型
     */
    private String roleModels;

    public String[] raffleRuleCenterModelList(){
        String[] ruleModelValues= roleModels.split(Constants.SPLIT);
        return Arrays.stream(ruleModelValues).filter(DefaultLogicFactory.LogicModel::isCenter).toArray(String[]::new);
    }

    public String[] raffleRuleAfterModeList(){
        String[] ruleModelValues= roleModels.split(Constants.SPLIT);
        return Arrays.stream(ruleModelValues).filter(DefaultLogicFactory.LogicModel::isAfter).toArray(String[]::new);
    }

}
