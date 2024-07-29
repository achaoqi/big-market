package com.aqiu.test.infrastucture;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.strategy.model.valobj.RuleTreeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.infrastructure.persistent.repository.StrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 仓储数据测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StrategyRepositoryTest {

    @Resource
    private IStrategyRepository repository;

    @Test
    public void queryRuleTreeByTreeId(){
        RuleTreeVO ruleTreeVO=repository.queryRuleTreeVOByTreeId("tree_lock");
        log.info("测试结果:{}", JSON.toJSONString(ruleTreeVO));
    }
}
