package com.aqiu.domain.strategy.service.armory;

public interface IStrategyArmory {
    public boolean assembleLotteryStrategy(Integer strategyId);

    public Integer getRandomAwardId(Integer strategyId);
}
