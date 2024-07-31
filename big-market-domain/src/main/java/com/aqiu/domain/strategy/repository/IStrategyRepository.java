package com.aqiu.domain.strategy.repository;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.model.entity.StrategyRuleEntity;
import com.aqiu.domain.strategy.model.valobj.RuleTreeVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.HashMap;
import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Integer strategyId);

    void storeStrategyAwardSearchRateTables(String key, Integer rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables);

    int getRateRange(Integer strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(String key, int choice);

    StrategyEntity getStrategyEntityByStrategyId(Integer strategyId);

    StrategyRuleEntity queryStrategyRule(Integer strategyId, String ruleModel);

    String queryStrategyRuleValue(Integer strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Integer strategyId, String ruleModel);

    StrategyEntity queryStrategyEntityByStrategyId(Integer strategyId);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Integer strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    Boolean subtractionAwardStock(String cacheKey);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Integer strategyId, Integer awardId);

    StrategyAwardEntity queryStrategyAwardEntity(Integer strategyId, Integer awardId);
}
