package com.aqiu.test.trigger;

import com.alibaba.fastjson.JSON;
import com.aqiu.trigger.api.IRaffleActivityService;
import com.aqiu.trigger.api.dto.ActivityDrawRequestDTO;
import com.aqiu.trigger.api.dto.ActivityDrawResponseDTO;
import com.aqiu.trigger.api.dto.UserActivityAccountRequestDTO;
import com.aqiu.trigger.api.dto.UserActivityAccountResponseDTO;
import com.aqiu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityControllerTest {

    @Resource
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_armory() {
        Response<Boolean> armory = raffleActivityService.armory(100301L);
        log.info("测试结果:{}", JSON.toJSONString(armory));
    }

    @Test
    public void test_draw() {
        ActivityDrawRequestDTO dto = ActivityDrawRequestDTO.builder()
                .activityId(100301L)
                .userId("xiaofuge")
                .build();
        Response<ActivityDrawResponseDTO> response = raffleActivityService.draw(dto);
        log.info("请求参数:{}", JSON.toJSONString(dto));
        log.info("返回结果:{}", JSON.toJSONString(response));
    }

    @Test
    public void test_calendarSignRebate() {
        Response<Boolean> response = raffleActivityService.calendarSignRebate("chaoqi");
        log.info("测试结果:{}", JSON.toJSONString(response));
    }

    @Test
    public void test_isCalendarSignRebate() {
        Response<Boolean> response = raffleActivityService.isCalendarSignRebate("xiaofuge");
        log.info("测试结果:{}", JSON.toJSONString(response));
    }

    @Test
    public void test_queryUserActivityAccount() {
        Response<UserActivityAccountResponseDTO> response = raffleActivityService.queryUserActivityAccount(UserActivityAccountRequestDTO.builder()
                .userId("chaoqi")
                .activityId(100301L)
                .build());
        log.info("测试结果:{}", JSON.toJSONString(response));
    }

}
