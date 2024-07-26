package com.aqiu.domain.strategy.service;

import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * 抽奖策略接口
 */
public interface IRaffleStrategy {
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
