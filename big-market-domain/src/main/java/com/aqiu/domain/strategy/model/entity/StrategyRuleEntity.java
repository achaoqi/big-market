package com.aqiu.domain.strategy.model.entity;

import com.aqiu.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyRuleEntity {
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

    public Map<String, List<Integer>> getRuleWeightValue(){
        if (!"rule_weight".equals(ruleModel)){ return null; }
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<String,List<Integer>> ruleWeightValue = new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            if (StringUtils.isBlank(ruleValueGroup)) return ruleWeightValue;
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if (parts.length!=2){
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format "+ruleValueGroup);
            }
            List<Integer> awardIds=new ArrayList<>();
            for (String awardId : parts[1].split(Constants.SPLIT)) {
                awardIds.add(Integer.parseInt(awardId));
            }
            ruleWeightValue.put(parts[0], awardIds);
        }
        return ruleWeightValue;
    }
}
