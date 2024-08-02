package com.aqiu.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.entity.ActivityOrderEntity;
import com.aqiu.domain.activity.model.entity.ActivityShopCartEntity;
import com.aqiu.domain.activity.model.entity.SkuRechargeEntity;
import com.aqiu.domain.activity.service.IRaffleOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RaffleOrderTest {
    @Resource
    private IRaffleOrder raffleOrder;

    @Test
    public void test_createRaffleActivityOrder(){
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("chaoqi");
        activityShopCartEntity.setSku(9011L);
        ActivityOrderEntity raffleActivityOrder = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("测试结果:{}", JSON.toJSONString(raffleActivityOrder));
    }

    @Test
    public void test_createSkuRechargeOrder(){
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("chaoqi");
        skuRechargeEntity.setSku(9011L);
        skuRechargeEntity.setOutBusinessNo("7000910091113");
        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
        log.info("测试结果:{}", orderId);
    }
}
