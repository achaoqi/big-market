package com.aqiu.domain.strategy.service.rule.chain.impl;

import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import com.aqiu.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.aqiu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 兜底
 */
@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyDispatch dispatch;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Integer strategyId) {
        Integer awardId = dispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId:{},strategyId:{},awardId:{}", userId, strategyId,awardId);
        return DefaultChainFactory.StrategyAwardVO.builder()
                .logicModel(ruleModel())
                .awardId(awardId)
                .build();
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode();
    }
}
