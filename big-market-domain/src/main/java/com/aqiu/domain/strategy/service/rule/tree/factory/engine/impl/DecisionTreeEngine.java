package com.aqiu.domain.strategy.service.rule.tree.factory.engine.impl;

import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.aqiu.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.aqiu.domain.strategy.model.valobj.RuleTreeVO;
import com.aqiu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.aqiu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.aqiu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 决策树引擎
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;
    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Integer strategyId, Integer awardId,Date endDateTime) {
        DefaultTreeFactory.StrategyAwardVO strategyAwardData=null;
        String nextNode= ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(nextNode);
        while (nextNode!=null){
            ILogicTreeNode iLogicTreeNode = logicTreeNodeGroup.get(nextNode);
            String ruleValue = ruleTreeNode.getRuleValue();
            DefaultTreeFactory.TreeActionEntity logicEntity = iLogicTreeNode.logic(userId, awardId, strategyId,ruleValue,endDateTime);
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckTypeVO();
            strategyAwardData = logicEntity.getStrategyAwardVO();
            log.info("决策树引擎 【{}】 treeId:{},node:{},code:{}",ruleTreeVO.getTreeName(),ruleTreeVO.getTreeId(),nextNode,ruleLogicCheckTypeVO.getCode());
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(),ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode=treeNodeMap.get(nextNode);
        }
//        返回最终结果
        return strategyAwardData;
    }

    private String nextNode(String matterValue, List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList) {
        if (ruleTreeNodeLineVOList==null || ruleTreeNodeLineVOList.isEmpty()){
            return null;
        }
        for (RuleTreeNodeLineVO ruleTreeNodeLineVO : ruleTreeNodeLineVOList) {
            if (decisionLogic(matterValue,ruleTreeNodeLineVO)){
                return ruleTreeNodeLineVO.getRuleNodeTo();
            }
        }
        return null;
    }

    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine){
        switch (nodeLine.getRuleLimitType()){
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }
}
