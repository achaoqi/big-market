package com.aqiu.test.domain.credit;

import com.aqiu.domain.credit.model.entity.TradeEntity;
import com.aqiu.domain.credit.model.valobj.TradeNameVO;
import com.aqiu.domain.credit.model.valobj.TradeTypeVO;
import com.aqiu.domain.credit.service.ICreditAdjustService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditAdjustServiceTest {
    @Resource
    private ICreditAdjustService creditAdjustService;

    @Test
    public void test_createOrder_forward() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("chaoqi");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.FORWARD);
        tradeEntity.setAmount(new BigDecimal("10.19"));
        tradeEntity.setOutBusinessNo("100009909911");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_reverse() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("chaoqi");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-10.19"));
        tradeEntity.setOutBusinessNo("20000990993");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_pay() throws InterruptedException {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("chaoqi");
        tradeEntity.setTradeName(TradeNameVO.CONVERT_SKU);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-1.68"));
        tradeEntity.setOutBusinessNo("70009250102002");
        creditAdjustService.createOrder(tradeEntity);

        new CountDownLatch(1).await();
    }


}
