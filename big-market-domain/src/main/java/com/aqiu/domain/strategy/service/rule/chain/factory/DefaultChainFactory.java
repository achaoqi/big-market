package com.aqiu.domain.strategy.service.rule.chain.factory;

import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultChainFactory {
    private final Map<String, ILogicChain> logicChainGroups;

    private final IStrategyRepository strategyRepository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroups, IStrategyRepository strategyRepository) {
        this.logicChainGroups = logicChainGroups;
        this.strategyRepository = strategyRepository;
    }

    public ILogicChain getLogicChain(Integer strategyId) {
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.ruleModel();
        if (ruleModels==null || ruleModels.length==0) {
            return logicChainGroups.get("default");
        }
        return buildChain(ruleModels,0);
    }

    private ILogicChain buildChain(String[] ruleModels, int current) {
        if (current==ruleModels.length) {
            return logicChainGroups.get("default");
        }
        ILogicChain logicChain = logicChainGroups.get(ruleModels[current]);
        logicChain.appendNext(buildChain(ruleModels, current+1));
        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO{
//        抽奖奖品ID
        private Integer awardId;

        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel{
        RULE_DEFAULT("rule_default","默认抽奖"),
        RULE_BLACKLIST("rule_blacklist","黑名单抽奖"),
        RULE_WEIGHT("rule_weight","权重抽奖")
        ;

        private final String code;
        private final String info;
    }
}
