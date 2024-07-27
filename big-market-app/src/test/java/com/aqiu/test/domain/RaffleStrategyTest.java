package com.aqiu.test.domain;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.service.IRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.domain.strategy.service.rule.impl.RuleLockLogicFilter;
import com.aqiu.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
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
public class RaffleStrategyTest {
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;
    @Resource
    private RuleLockLogicFilter ruleLockLogicFilter;
    @Resource
    private IStrategyArmory strategyArmory;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(ruleWeightLogicFilter,"userScore",1010);
        ReflectionTestUtils.setField(ruleLockLogicFilter,"userRaffleCount",2);

        boolean flag = strategyArmory.assembleLotteryStrategy(10002);
        log.info("测试结果：{}",flag);
    }

    @Test
    public void test_raffleStrategy(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("chaoqi")
                .strategyId(10001)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数:{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果:{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle_blackList(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user001")
                .strategyId(10001)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数:{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果:{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_raffle_center_rule_lock(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user001")
                .strategyId(10002)
                .build();
        for (int i = 0; i < 10; i++) {
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
            log.info("请求参数:{}", JSON.toJSONString(raffleFactorEntity));
            log.info("测试结果:{}", JSON.toJSONString(raffleAwardEntity));
        }

    }
}
