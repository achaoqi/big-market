package com.aqiu.domain.strategy.service.rule.tree.impl;

import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 次数锁节点
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {
//    用户抽奖次数
    private Integer userRaffleCount=10;
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Integer awardId, Integer strategyId,String ruleValue) {
        log.info("规则过滤-次数锁 userId:{} awardId:{} strategyId:{} ruleValue:{}", userId, awardId, strategyId, ruleValue);
        Integer raffleCount=0;
        try{
            raffleCount=Integer.parseInt(ruleValue);
        }catch (Exception e){
            throw new RuntimeException("规则过滤-次数锁 ruleValue:"+ruleValue+"配置不正确");
        }

        if (userRaffleCount>=raffleCount){
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
