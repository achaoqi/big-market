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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository repository;

    private Integer userScore= 4500;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-权重过滤 userId:{},strategyId:{},ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());
        String userId= ruleMatterEntity.getUserId();
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
//        4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108
        String[] ruleValues = ruleValue.split(Constants.SPACE);
        List<String> fraction = Arrays.stream(ruleValues).map(value -> value.split(Constants.COLON)[0].trim()).sorted(String::compareTo).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(fraction)){
            RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        for (int i = fraction.size()-1; i >= 0; i--) {
            if (userScore>=Integer.parseInt(fraction.get(i))){
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(ruleMatterEntity.getRuleModel())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .ruleWeightValueKey(fraction.get(i))
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .build())
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
