package com.aqiu.test.domain;

import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StrategyArmoryTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Test
    public void test_strategyArmory(){
        strategyArmory.assembleLotteryStrategy(10001);
    }

    @Test
    public void test_getAssembleRandomVal(){
        for (int i = 0; i < 1000; i++) {
            log.info("测试结果:{} -奖品ID值",strategyArmory.getRandomAwardId(10001));
        }
    }

}
