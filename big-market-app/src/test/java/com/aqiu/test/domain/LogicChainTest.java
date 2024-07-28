package com.aqiu.test.domain;

import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.domain.strategy.service.rule.chain.ILogicChain;
import com.aqiu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.aqiu.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LogicChainTest {
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private DefaultChainFactory defaultChainFactory;

    @Before
    public void setUp(){
        log.info("测试结果:{}",strategyArmory.assembleLotteryStrategy(10001));
        log.info("测试结果:{}",strategyArmory.assembleLotteryStrategy(10002));
    }

    @Test
    public void test_LogicChain_rule_blackList(){
        ILogicChain logicChain = defaultChainFactory.getLogicChain(10001);
        Integer awardId = logicChain.logic("user001", 10001);
        log.info("测试结果：{}",awardId);
    }

    @Test
    public void test_LogicChain_rule_weight(){
        ReflectionTestUtils.setField(ruleWeightLogicChain,"userScore",4900);
        ILogicChain logicChain = defaultChainFactory.getLogicChain(10001);
        Integer awardId = logicChain.logic("chaoqi", 10001);
        log.info("测试结果：{}",awardId);
    }

    @Test
    public void test_LogicChain_rule_default(){
        ILogicChain logicChain = defaultChainFactory.getLogicChain(10001);
        Integer awardId = logicChain.logic("chaoqi", 10001);
        log.info("测试结果：{}",awardId);
    }
}
