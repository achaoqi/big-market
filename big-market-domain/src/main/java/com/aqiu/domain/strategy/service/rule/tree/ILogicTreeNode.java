package com.aqiu.domain.strategy.service.rule.tree;

import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * 规则树接口
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Integer awardId, Integer strategyId, String ruleValue, Date endDateTime);
}
