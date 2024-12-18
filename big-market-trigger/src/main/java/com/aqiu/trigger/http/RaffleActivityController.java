package com.aqiu.trigger.http;

import com.aqiu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.aqiu.domain.activity.model.entity.UserRaffleOrderEntity;
import com.aqiu.domain.activity.service.IRaffleActivityPartakeService;
import com.aqiu.domain.activity.service.armory.IActivityArmory;
import com.aqiu.domain.award.model.entity.UserAwardRecordEntity;
import com.aqiu.domain.award.model.valobj.AwardStateVO;
import com.aqiu.domain.award.service.IAwardService;
import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.service.IRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.trigger.api.IRaffleActivityService;
import com.aqiu.trigger.api.dto.ActivityDrawRequestDTO;
import com.aqiu.trigger.api.dto.ActivityDrawResponseDTO;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import com.aqiu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 抽奖活动服务
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {
    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IAwardService awardService;

    /**
     * 活动装配-数据预热 | 把活动配置的对应sku一起装配
     * @param activityId
     * @return
     */
    @RequestMapping(value = "armory",method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(@RequestParam Long activityId) {
        try{
            log.info("活动装配,数据预热,开始 activityId:{}", activityId);
            activityArmory.assembleSkuByActivityId(activityId);
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
            log.info("活动装配,数据预热,完成 activityId:{}", activityId);
            return response;
        }catch (Exception e){
            log.error("活动装配,数据预热,失败 activityId:{}", activityId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "draw",method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try{
            log.info("活动抽奖 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
//            校验参数
            if (StringUtils.isBlank(request.getUserId())||null==request.getActivityId()){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
            }
//            参与活动-创建参与活动记录订单
            UserRaffleOrderEntity order = raffleActivityPartakeService.createOrder(request.getUserId(),request.getActivityId());
            log.info("活动抽奖,创建订单 userId:{} activityId:{} orderId:{}",request.getUserId(),request.getActivityId(),order.getOrderId());
//          抽奖策略-执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(request.getUserId())
                    .strategyId(order.getStrategyId().intValue())
                    .endDateTime(order.getEndDateTime())
                    .build());
//            存放结果-写入中奖记录
            awardService.saveUserAwardRecord(UserAwardRecordEntity.builder()
                            .activityId(request.getActivityId())
                            .userId(request.getUserId())
                            .orderId(order.getOrderId())
                            .awardId(raffleAwardEntity.getAwardId())
                            .strategyId(Long.valueOf(raffleAwardEntity.getAwardId()))
                            .awardState(AwardStateVO.create)
                            .awardTime(new Date())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                    .build());
//            返回结果
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .build())
                    .build();
        }catch (AppException e){
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }catch (Exception e){
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
