package com.aqiu.domain.strategy.service.rule;

import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.entity.RuleMatterEntity;

public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
