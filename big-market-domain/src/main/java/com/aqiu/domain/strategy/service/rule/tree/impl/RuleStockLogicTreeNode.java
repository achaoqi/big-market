package com.aqiu.domain.strategy.service.rule.tree.impl;

import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import com.aqiu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 库存节点
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {
    @Resource
    private IStrategyDispatch strategyDispatch;
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Integer awardId, Integer strategyId,String ruleValue, Date endDateTime) {
        log.info("规则过滤-库存扣减 userId:{} awardId:{} strategyId:{}", userId, awardId, strategyId);
        Boolean status = strategyDispatch.subtractionAwardStock(Long.valueOf(strategyId), awardId, endDateTime);
        if (status){
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                            .awardId(awardId)
                            .strategyId(strategyId)
                    .build());
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue("")
                            .build())
                    .build();
        }
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
