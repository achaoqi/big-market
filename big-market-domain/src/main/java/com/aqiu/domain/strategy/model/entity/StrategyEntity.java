package com.aqiu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {
    /**
     * 策略id
     */
    private Integer strategyId;
    /**
     * 策略描述
     */
    private String strategyDesc ;
    /**
     * 策略模型
     */
    private String ruleModels;

    public String getRuleWeight(){
        String[] models = ruleModel();
        for (String model : models) {
            if ("rule_weight".equals(model)) {
                return model;
            }
        }
        return null;
    }

    public String[] ruleModel(){
        if (StringUtils.isBlank(ruleModels)){
            return new String[0];
        }
        return ruleModels.split(",");
    }
}
