package com.aqiu.domain.strategy.service.rule.tree.factory.engine;

import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * 规则树组合接口
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Integer strategyId, Integer awardId, Date endDateTime);
}
