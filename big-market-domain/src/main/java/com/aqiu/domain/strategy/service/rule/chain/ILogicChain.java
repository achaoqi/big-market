package com.aqiu.domain.strategy.service.rule.chain;

import com.aqiu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * 责任链接口
 */
public interface ILogicChain extends ILogicChainArmory{
    /**
     * 责任链接口
     * @param userId 用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Integer strategyId);
}
