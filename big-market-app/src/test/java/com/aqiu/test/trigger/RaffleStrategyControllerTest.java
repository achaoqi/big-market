package com.aqiu.test.trigger;

import com.aqiu.trigger.api.IRaffleStrategyService;
import com.aqiu.trigger.api.dto.RaffleAwardListRequestDTO;
import com.aqiu.trigger.api.dto.RaffleAwardListResponseDTO;
import com.aqiu.trigger.api.dto.RaffleStrategyRuleWeightRequestDTO;
import com.aqiu.trigger.api.dto.RaffleStrategyRuleWeightResponseDTO;
import com.aqiu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyControllerTest {

    @Resource
    private IRaffleStrategyService raffleStrategyService;

    @Test
    public void test_queryRaffleAwardList(){
        RaffleAwardListRequestDTO dto = new RaffleAwardListRequestDTO();
        dto.setUserId("xiaofuge");
        dto.setActivityId(100301);
        Response<List<RaffleAwardListResponseDTO>> response = raffleStrategyService.queryRaffleAwardList(dto);
        log.info("请求参数:{}", dto);
        log.info("返回结果:{}", response);
    }

    @Test
    public void test_queryRaffleStrategyRuleWeight(){
        RaffleStrategyRuleWeightRequestDTO dto = new RaffleStrategyRuleWeightRequestDTO();
        dto.setUserId("xiaofuge");
        dto.setActivityId(100301L);
        Response<List<RaffleStrategyRuleWeightResponseDTO>> response = raffleStrategyService.queryRaffleStrategyRuleWeight(dto);
        log.info("请求参数:{}", dto);
        log.info("返回结果:{}", response);
    }

}
