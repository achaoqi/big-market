package com.aqiu.trigger.http;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.entity.ActivityAccountEntity;
import com.aqiu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.aqiu.domain.activity.model.entity.UserRaffleOrderEntity;
import com.aqiu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.aqiu.domain.activity.service.IRaffleActivityPartakeService;
import com.aqiu.domain.activity.service.armory.IActivityArmory;
import com.aqiu.domain.award.model.entity.UserAwardRecordEntity;
import com.aqiu.domain.award.model.valobj.AwardStateVO;
import com.aqiu.domain.award.service.IAwardService;
import com.aqiu.domain.rebate.model.entity.BehaviorEntity;
import com.aqiu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.aqiu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.aqiu.domain.rebate.service.IBehaviorRebateService;
import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.service.IRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.trigger.api.IRaffleActivityService;
import com.aqiu.trigger.api.dto.ActivityDrawRequestDTO;
import com.aqiu.trigger.api.dto.ActivityDrawResponseDTO;
import com.aqiu.trigger.api.dto.UserActivityAccountRequestDTO;
import com.aqiu.trigger.api.dto.UserActivityAccountResponseDTO;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import com.aqiu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @Resource
    private IBehaviorRebateService behaviorRebateService;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

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
                            .strategyId(order.getStrategyId())
                            .awardState(AwardStateVO.create)
                            .awardTime(new Date())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .awardConfig(raffleAwardEntity.getAwardConfig())
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

    /**
     * 日历签到返利接口
     * @param userId
     * @return
     */
    @RequestMapping(value = "/calendar_sign_rebate",method = RequestMethod.POST)
    @Override
    public Response<Boolean> calendarSignRebate(String userId) {
        try{
            log.info("日历签到返利开始:userId:{}",userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SING);
            behaviorEntity.setOutBusinessId(dateFormat.format(new Date()));
            List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
            log.info("日历签到返利完成:orderIds:{}", JSON.toJSONString(orderIds));
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        }catch (AppException e){
            log.error("日历签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .data(false)
                    .build();
        }catch (Exception e){
            log.error("日历签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    /**
     * 判断是否完成日历签到返利
     * @param userId
     * @return
     */
    @RequestMapping(value = "/is_calendar_sign_rebate",method = RequestMethod.POST)
    @Override
    public Response<Boolean> isCalendarSignRebate(String userId) {
        try{
            log.info("查询是否完成日历签到返利开始 userId:{}",userId);
            String outBusinessNo = dateFormat.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = behaviorRebateService.queryOrderByOutBusinessNo(userId, outBusinessNo);
            log.info("查询是否完成日历签到返利完成 userId:{} orders.size:{}",userId,behaviorRebateOrderEntities.size());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(!behaviorRebateOrderEntities.isEmpty())
                    .build();
        }catch (Exception e){
            log.error("查询是否完成日历签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    /**
     * 查询用户活动抽奖次数
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "/query_user_activity_account",method = RequestMethod.POST)
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO requestDTO) {
        try{
            log.info("查询用户活动抽奖次数开始:userId:{} activityId:{}",requestDTO.getUserId(),requestDTO.getActivityId());
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryRaffleActivityAccount(requestDTO.getUserId(), requestDTO.getActivityId());
            UserActivityAccountResponseDTO responseDTO = UserActivityAccountResponseDTO.builder()
                    .totalCount(activityAccountEntity.getTotalCount())
                    .totalCountSurplus(activityAccountEntity.getTotalCountSurplus())
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                    .build();
            log.info("查询用户活动抽奖次数结束:userId:{} activityId:{} response:{}",requestDTO.getUserId(),requestDTO.getActivityId(),JSON.toJSONString(responseDTO));
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        }catch (Exception e){
            log.error("查询用户活动抽奖次数失败:userId:{} activityId:{}",requestDTO.getUserId(),requestDTO.getActivityId(),e);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
