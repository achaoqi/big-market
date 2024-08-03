package com.aqiu.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.entity.ActivityOrderEntity;
import com.aqiu.domain.activity.model.entity.ActivityShopCartEntity;
import com.aqiu.domain.activity.model.entity.SkuRechargeEntity;
import com.aqiu.domain.activity.service.IRaffleOrder;
import com.aqiu.domain.activity.service.armory.IActivityArmory;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RaffleOrderTest {
    @Resource
    private IRaffleOrder raffleOrder;
    @Resource
    private IActivityArmory activityArmory;

    @Before
    public void setUp(){
        log.info("装配活动:{}",activityArmory.assembleActivitySku(9011L));
    }

    @Test
    public void test_createSkuRechargeOrder() throws InterruptedException{
        for (int i = 0; i < 20; i++) {
            try{
                SkuRechargeEntity skuRechargeEntity=new SkuRechargeEntity();
                skuRechargeEntity.setSku(9011L);
                skuRechargeEntity.setUserId("chaoqi");
                skuRechargeEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
                String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
                log.info("测试结果:{}",orderId);
            }catch (AppException e){
                log.warn(e.getInfo());
            }
        }
        new CountDownLatch(1).await();
    }

    @Test
    public void test_createRaffleActivityOrder(){
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("chaoqi");
        activityShopCartEntity.setSku(9011L);
        ActivityOrderEntity raffleActivityOrder = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("测试结果:{}", JSON.toJSONString(raffleActivityOrder));
    }

    @Test
    public void test_createSkuRechargeOrder_duplicate(){
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("chaoqi");
        skuRechargeEntity.setSku(9011L);
        skuRechargeEntity.setOutBusinessNo("7000910091113");
        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
        log.info("测试结果:{}", orderId);
    }
}
