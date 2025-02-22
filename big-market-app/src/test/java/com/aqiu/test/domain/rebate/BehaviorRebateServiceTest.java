package com.aqiu.test.domain.rebate;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.rebate.model.entity.BehaviorEntity;
import com.aqiu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.aqiu.domain.rebate.service.BehaviorRebateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BehaviorRebateServiceTest {

    @Resource
    private BehaviorRebateService behaviorRebateService;

    @Test
    public void test_createOrder() throws InterruptedException {
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId("chaoqi");
        behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SING);
        behaviorEntity.setOutBusinessId("20241231");
        List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
        log.info("请求参数:{}", JSON.toJSONString(behaviorEntity));
        log.info("返回结果:{}", JSON.toJSONString(orderIds));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

}
