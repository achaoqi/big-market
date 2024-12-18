package com.aqiu.domain.strategy.service;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * 策略奖品接口
 */
public interface IRaffleAward {

    /**
     * 根据策略查询抽奖策略奖品列表
     * @param strategyId
     * @return
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Integer strategyId);

    /**
     * 根据活动ID查询抽奖策略奖品列表
     * @param activityId
     * @return
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Integer activityId);

}
