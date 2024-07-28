package com.aqiu.domain.strategy.service.rule.filter.impl;

import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.entity.RuleMatterEntity;
import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.annotation.LogicStrategy;
import com.aqiu.domain.strategy.service.rule.filter.ILogicFilter;
import com.aqiu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户抽奖n次后,对应奖品可解锁抽奖
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    private Integer userRaffleCount=0;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userId:{},strategyId:{},ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        int raffleCount=Integer.parseInt(ruleValue);
        if (userRaffleCount>=raffleCount){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .build();
        }
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
