package com.aqiu.domain.strategy.service.rule.tree.impl;

import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.aqiu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 兜底奖励
 */
@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Integer awardId, Integer strategyId,String ruleValue) {
        log.info("规则过滤-兜底奖励 userId:{} awardId:{} strategyId:{} ruleValue:{}", userId, awardId, strategyId, ruleValue);
        String[] ruleValueSplit = ruleValue.split(Constants.COLON);
        if (ruleValueSplit.length==0){
            log.error("规则过滤-兜底奖励，兜底奖励未配置 userId:{} strategyId:{} awardId:{}",userId,strategyId,awardId);
            throw new RuntimeException("兜底奖励未配置"+ruleValue);
        }
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(
                        DefaultTreeFactory.StrategyAwardVO.builder()
                                .awardId(Integer.parseInt(ruleValueSplit[0]))
                                .awardRuleValue(ruleValueSplit.length>1 ? ruleValueSplit[1]:"")
                                .build()
                )
                .build();
    }
}
