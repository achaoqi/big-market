package com.aqiu.domain.strategy.service;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * 策略奖品接口
 */
public interface IRaffleAward {

    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Integer strategyId);

}
