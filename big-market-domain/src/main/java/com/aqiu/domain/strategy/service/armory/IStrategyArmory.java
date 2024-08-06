package com.aqiu.domain.strategy.service.armory;

public interface IStrategyArmory {
    public boolean assembleLotteryStrategy(Integer strategyId);

    public boolean assembleLotteryStrategyByActivityId(Long activityId);
}
