package com.aqiu.domain.strategy.service.rule.chain.impl;

import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.aqiu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 黑名单方法
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Override
    public Integer logic(String userId,Integer strategyId) {
        log.info("抽奖责任链-黑名单开始 userId:{},strategyId:{},ruleModel:{}", userId, strategyId,ruleModel());
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        String awardId=splitRuleValue[0];
//        100:user001,user002,user003过滤其他规则
        String[] userIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String id : userIds) {
            if (userId.equals(id)){
                log.info("抽奖责任链-黑名单接管 userId:{},strategyId:{},ruleModel:{},awardId:{}", id, strategyId,ruleModel(),awardId);
                return Integer.parseInt(awardId);
            }
        }
        log.info("抽奖责任链-黑名单放行 userId:{},strategyId:{},ruleModel:{}", userId, strategyId,ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
