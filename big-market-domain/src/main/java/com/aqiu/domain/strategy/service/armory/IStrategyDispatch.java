package com.aqiu.domain.strategy.service.armory;

import java.util.Date;

public interface IStrategyDispatch {
    public Integer getRandomAwardId(Integer strategyId);

    public Integer getRandomAwardId(Integer strategyId,String ruleWeightValue);

    Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endDateTime);
}
