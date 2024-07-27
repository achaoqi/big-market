package com.aqiu.domain.strategy.service.rule.impl;

import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.entity.RuleMatterEntity;
import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.annotation.LogicStrategy;
import com.aqiu.domain.strategy.service.rule.ILogicFilter;
import com.aqiu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.aqiu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-黑名单 userId:{},strategyId:{},ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());
        String ruleValue=repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        String awardId=splitRuleValue[0];
//        100:user001,user002,user003过滤其他规则
        String[] userIds = splitRuleValue[1].split(Constants.SPLIT);
        String userId = ruleMatterEntity.getUserId();
        for (String id : userIds) {
            if (userId.equals(id)){
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(ruleMatterEntity.getRuleModel())
                        .data(
                                RuleActionEntity.RaffleBeforeEntity.builder()
                                        .awardId(Integer.parseInt(awardId))
                                        .strategyId(ruleMatterEntity.getStrategyId())
                                        .build()
                        )
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
