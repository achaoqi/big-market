package com.aqiu.test.domain;

import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StrategyArmoryDispatchTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Before
    public void test_strategyArmory(){
        boolean flag = strategyArmory.assembleLotteryStrategy(10001);
        log.info("测试结果：{}",flag);
    }

    @Test
    public void test_getAssembleRandomVal(){
        for (int i = 0; i < 1000; i++) {
            log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001));
        }
    }

    @Test
    public void test_getAssembleRandomVal_ruleWeightValue(){
//        4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(4000)));
//        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(5000)));
//        log.info("测试结果:{} -奖品ID值",strategyDispatch.getRandomAwardId(10001,String.valueOf(6000)));
    }
}
