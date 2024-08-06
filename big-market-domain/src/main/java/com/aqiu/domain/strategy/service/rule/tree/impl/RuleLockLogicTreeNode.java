package com.aqiu.domain.strategy.service.rule.tree.impl;

import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 次数锁节点
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Integer awardId, Integer strategyId,String ruleValue) {
        log.info("规则过滤-次数锁 userId:{} awardId:{} strategyId:{} ruleValue:{}", userId, awardId, strategyId, ruleValue);
        Integer raffleCount=0;
        try{
            raffleCount=Integer.parseInt(ruleValue);
        }catch (Exception e){
            throw new RuntimeException("规则过滤-次数锁 ruleValue:"+ruleValue+"配置不正确");
        }
//查询用户抽奖次数 当天的策略ID:活动ID 1:1的配置 可以直接使用strategyId查询
        Integer userRaffleCount=strategyRepository.queryTodayUserRaffleCount(userId,strategyId);

        if (userRaffleCount>=raffleCount){
            log.info("规则过滤-次数锁[放行] userId:{} strategyId:{} awardId:{} raffleCount:{} userRaffleCount:{}", userId, strategyId, awardId, raffleCount, userRaffleCount);
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }
        log.info("规则过滤-次数锁[拦截] userId:{} strategyId:{} awardId:{} raffleCount:{} userRaffleCount:{}", userId, strategyId, awardId, raffleCount, userRaffleCount);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
