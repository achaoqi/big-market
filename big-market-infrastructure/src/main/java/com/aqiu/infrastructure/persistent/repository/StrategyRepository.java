package com.aqiu.infrastructure.persistent.repository;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.model.entity.StrategyRuleEntity;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.infrastructure.persistent.dao.IStrategyAwardDao;
import com.aqiu.infrastructure.persistent.dao.IStrategyDao;
import com.aqiu.infrastructure.persistent.dao.IStrategyRuleDao;
import com.aqiu.infrastructure.persistent.po.Strategy;
import com.aqiu.infrastructure.persistent.po.StrategyAward;
import com.aqiu.infrastructure.persistent.po.StrategyRule;
import com.aqiu.infrastructure.persistent.redis.IRedisService;
import com.aqiu.types.common.Constants;
import org.redisson.api.RMap;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Integer strategyId) {
        String cacheKey= Constants.STRATEGY_AWARD_KEY+strategyId;
        List<StrategyAwardEntity> entities= redisService.getValue(cacheKey);
        if (entities!=null&& !entities.isEmpty()){
            return entities;
        }
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        entities = strategyAwards.stream().map(obj -> StrategyAwardEntity.builder()
                .strategyId(obj.getStrategyId())
                .awardId(obj.getAwardId())
                .awardCount(obj.getAwardCount())
                .awardCountSurplus(obj.getAwardCountSurplus())
                .awardRate(obj.getAwardRate())
                .build()).collect(Collectors.toList());
        redisService.setValue(cacheKey,entities);
        return entities;
    }

    @Override
    public void storeStrategyAwardSearchRateTables(String key, Integer rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables) {
        redisService.setValue(Constants.STRATEGY_RATE_RANGE_KEY+key, rateRange);
        RMap<Integer, Integer> map = redisService.getMap(Constants.STRATEGY_RATE_TABLE_KEY + key);
        map.putAll(shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public int getRateRange(Integer strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.STRATEGY_RATE_RANGE_KEY+key);
    }

    @Override
    public Integer getStrategyAwardAssemble(String strategyId, int choice) {
        return redisService.getFromMap(Constants.STRATEGY_RATE_TABLE_KEY+strategyId,choice);
    }

    @Override
    public StrategyEntity getStrategyEntityByStrategyId(Integer strategyId) {
        String key=Constants.STRATEGY_KEY+strategyId;
        StrategyEntity strategyEntity = redisService.getValue(key);
        if (strategyEntity!=null){
            return strategyEntity;
        }
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(key,strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Integer strategyId, String ruleModel) {
        StrategyRule req = new StrategyRule();
        req.setStrategyId(strategyId);
        req.setRuleModel(ruleModel);
        StrategyRule strategyRule=strategyRuleDao.queryStrategyRule(req);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRule.getStrategyId())
                .awardId(strategyRule.getAwardId())
                .ruleType(strategyRule.getRuleType())
                .ruleModel(strategyRule.getRuleModel())
                .ruleValue(strategyRule.getRuleValue())
                .ruleDesc(strategyRule.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Integer strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule=new StrategyRule();
        strategyRule.setRuleModel(ruleModel);
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        return strategyRuleDao.queryStrategyRuleValue(strategyRule);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Integer strategyId) {
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        return StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .ruleModels(strategy.getRuleModels())
                .strategyDesc(strategy.getStrategyDesc())
                .build();
    }
}
