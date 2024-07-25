package com.aqiu.infrastructure.persistent.repository;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.infrastructure.persistent.dao.IStrategyAwardDao;
import com.aqiu.infrastructure.persistent.po.StrategyAward;
import com.aqiu.infrastructure.persistent.redis.IRedisService;
import com.aqiu.types.common.Constants;
import org.redisson.api.RMap;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

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
    public void storeStrategyAwardSearchRateTables(Integer strategyId, Integer rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables) {
        redisService.setValue(Constants.STRATEGY_RATE_RANGE_KEY+strategyId,rateRange.intValue());
        RMap<Integer, Integer> map = redisService.getMap(Constants.STRATEGY_RATE_TABLE_KEY + strategyId);
        map.putAll(shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public int getRateRange(Integer strategyId) {
        return redisService.getValue(Constants.STRATEGY_RATE_RANGE_KEY+strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Integer strategyId, int choice) {
        return redisService.getFromMap(Constants.STRATEGY_RATE_TABLE_KEY+strategyId,choice);
    }
}
