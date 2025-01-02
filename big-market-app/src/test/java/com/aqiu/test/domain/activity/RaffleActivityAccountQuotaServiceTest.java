package com.aqiu.test.domain.activity;

import com.aqiu.domain.activity.model.entity.SkuRechargeEntity;
import com.aqiu.domain.activity.model.valobj.OrderTradeTypeVO;
import com.aqiu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.aqiu.domain.activity.service.armory.IActivityArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RaffleActivityAccountQuotaServiceTest {
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private IActivityArmory activityArmory;

    @Test
    public void test_credit_pay_trade() {
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("chaoqi");
        skuRechargeEntity.setSku(9011L);
        // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
        skuRechargeEntity.setOutBusinessNo("70009250102002");
        skuRechargeEntity.setOrderTradeType(OrderTradeTypeVO.credit_pay_trade);
        String orderId = raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }

}
