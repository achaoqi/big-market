package com.aqiu.domain.strategy.repository;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.model.entity.StrategyRuleEntity;
import com.aqiu.domain.strategy.model.valobj.RuleTreeVO;
import com.aqiu.domain.strategy.model.valobj.RuleWeightVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Boolean subtractionAwardStock(String cacheKey, Date endDateTime);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Integer strategyId, Integer awardId);

    StrategyAwardEntity queryStrategyAwardEntity(Integer strategyId, Integer awardId);

    long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserRaffleCount(String userId, Integer strategyId);

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    Integer queryActivityAccountTotalUseCount(String userId, Integer strategyId);

    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);
}
