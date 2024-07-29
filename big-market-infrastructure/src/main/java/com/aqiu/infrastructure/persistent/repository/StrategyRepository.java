package com.aqiu.infrastructure.persistent.repository;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.model.entity.StrategyRuleEntity;
import com.aqiu.domain.strategy.model.valobj.*;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.infrastructure.persistent.dao.*;
import com.aqiu.infrastructure.persistent.po.*;
import com.aqiu.infrastructure.persistent.redis.IRedisService;
import com.aqiu.types.common.Constants;
import org.redisson.api.RMap;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Resource
    private IRuleTreeDao ruleTreeDao;
    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

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
    public String queryStrategyRuleValue(Integer strategyId, String ruleModel) {
        StrategyRule strategyRule=new StrategyRule();
        strategyRule.setRuleModel(ruleModel);
        strategyRule.setStrategyId(strategyId);
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

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Integer strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
        return StrategyAwardRuleModelVO.builder().roleModels(ruleModels).build();
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
//        优先从缓存中取
        String cacheKey=Constants.RULE_TREE_VO_KEY+treeId;
        RuleTreeVO value = redisService.getValue(cacheKey);
        if (value!=null){
            return value;
        }
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        RuleTreeVO result = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeRootRuleKey())
                .build();
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);
        Map<String, RuleTreeNodeVO> treeNodeMap=new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            treeNodeMap.put(ruleTreeNode.getRuleKey(), RuleTreeNodeVO.builder()
                            .treeId(treeId)
                            .ruleKey(ruleTreeNode.getRuleKey())
                            .ruleDesc(ruleTreeNode.getRuleDesc())
                            .ruleValue(ruleTreeNode.getRuleDesc())
                            .treeNodeLineVOList(new ArrayList<>())
                    .build());
        }

        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            String from = ruleTreeNodeLine.getRuleNodeFrom(),to=ruleTreeNodeLine.getRuleNodeTo();
            RuleTreeNodeVO ruleFromVO = treeNodeMap.get(from);
            ruleFromVO.getTreeNodeLineVOList().add(RuleTreeNodeLineVO.builder()
                            .treeId(ruleTreeNodeLine.getTreeId())
                            .ruleNodeFrom(from)
                            .ruleNodeTo(to)
                            .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                            .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build());
        }
        result.setTreeNodeMap(treeNodeMap);
        redisService.setValue(cacheKey,result);
        return result;
    }
}
