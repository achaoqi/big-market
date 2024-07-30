package com.aqiu.domain.strategy.service.raffle;

import com.aqiu.domain.strategy.model.valobj.RuleTreeVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.AbstractRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import com.aqiu.domain.strategy.service.rule.chain.ILogicChain;
import com.aqiu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.aqiu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {


    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory) {
        super(repository, strategyDispatch, chainFactory, treeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Integer strategyId) {
        ILogicChain logicChain = chainFactory.getLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Integer strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModel(strategyId, awardId);
        if (strategyAwardRuleModelVO==null|| StringUtils.isBlank(strategyAwardRuleModelVO.getRoleModels())){
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRoleModels());
        if (null==ruleTreeVO){
            throw new RuntimeException("存在抽奖策略配置的规则模型key未在库表rule_tree,rule_tree_node,rule_tree_line 配置正确");
        }
        IDecisionTreeEngine iDecisionTreeEngine = treeFactory.openLogicTree(ruleTreeVO);
        return iDecisionTreeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Integer strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId,awardId);
    }
}
