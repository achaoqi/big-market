package com.aqiu.domain.strategy.service;

import com.aqiu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * 抽奖库存相关服务，获取库存消耗队列
 */
public interface IRaffleStock {
    /**
     * 获取库存消耗队列
     * @return
     * @throws InterruptedException
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新商品库存
     * @param strategyId
     * @param awardId
     */
    void updateStrategyAwardStock(Integer strategyId,Integer awardId);
}
