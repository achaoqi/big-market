package com.aqiu.domain.strategy.service.rule.chain.impl;

import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import com.aqiu.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.aqiu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.aqiu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权重责任链
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyRepository repository;
    @Resource
    private IStrategyDispatch strategyDispatch;

    private Integer userScore=0;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Integer strategyId) {
        log.info("规则过滤-权重过滤 userId:{},strategyId:{},ruleModel:{}", userId, strategyId,ruleModel());
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        //        4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108
        String[] ruleValues = ruleValue.split(Constants.SPACE);
        List<String> fraction = Arrays.stream(ruleValues).map(value -> value.split(Constants.COLON)[0].trim()).sorted(String::compareTo).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(fraction)){
            return next().logic(userId, strategyId);
        }

        for (int i = fraction.size()-1; i >= 0; i--) {
            int ruleWeightValueKey = Integer.parseInt(fraction.get(i));
            if (userScore>= ruleWeightValueKey){
                String awardId = String.valueOf(ruleWeightValueKey);
                log.info("抽奖责任链-权重接管 userId:{},strategyId:{},awardId:{}", userId, strategyId,awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .logicModel(ruleModel())
                        .awardId(strategyDispatch.getRandomAwardId(strategyId, awardId))
                        .build();
            }
        }
        log.info("抽奖责任链-权重放行 userId:{},strategyId:{}", userId, strategyId);
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }
}
