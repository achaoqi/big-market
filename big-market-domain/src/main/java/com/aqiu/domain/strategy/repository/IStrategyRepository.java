package com.aqiu.domain.strategy.repository;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Integer strategyId);

    void storeStrategyAwardSearchRateTables(Integer strategyId, Integer rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables);

    int getRateRange(Integer strategyId);

    Integer getStrategyAwardAssemble(Integer strategyId, int choice);
}
