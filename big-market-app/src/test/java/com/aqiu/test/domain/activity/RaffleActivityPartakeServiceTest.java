package com.aqiu.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.aqiu.domain.activity.model.entity.UserRaffleOrderEntity;
import com.aqiu.domain.activity.service.partake.RaffleActivityPartakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RaffleActivityPartakeServiceTest {
    @Resource
    private RaffleActivityPartakeService raffleActivityPartakeService;

    @Test
    public void test_createOrder(){
        PartakeRaffleActivityEntity req = PartakeRaffleActivityEntity.builder()
                .activityId(100301L)
                .userId("xiaofuge")
                .build();
        UserRaffleOrderEntity res = raffleActivityPartakeService.createOrder(req);
        log.info("请求参数:{}",JSON.toJSONString(req));
        log.info("测试结果:{}",JSON.toJSONString(res));
    }
}
