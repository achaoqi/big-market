package com.aqiu.domain.strategy.service.armory;

public interface IStrategyDispatch {
    public Integer getRandomAwardId(Integer strategyId);

    public Integer getRandomAwardId(Integer strategyId,String ruleWeightValue);
}
