package com.aqiu.trigger.http;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.entity.ActivityAccountEntity;
import com.aqiu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.model.valobj.RuleWeightVO;
import com.aqiu.domain.strategy.service.IRaffleAward;
import com.aqiu.domain.strategy.service.IRaffleRule;
import com.aqiu.domain.strategy.service.IRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.trigger.api.IRaffleStrategyService;
import com.aqiu.trigger.api.dto.*;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import com.aqiu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/strategy")
public class RaffleStrategyController implements IRaffleStrategyService {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleAward raffleAward;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IRaffleRule raffleRule;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    /**
     * 策略装配，将策略信息装配到缓存中
     *
     * @param strategyId 策略ID
     * @return
     */
    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Integer strategyId) {
        try {
            log.info("抽奖策略装配开始 strategyId:{}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("抽奖策略装配完成 strategyId:{} response:{}", strategyId, response);
            return response;
        } catch (Exception e) {
            log.error("抽奖策略装配失败 ", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 查询奖品列表
     *
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        log.info("查询奖品列表开始 userId:{},activityId:{}", requestDTO.getUserId(),requestDTO.getActivityId());
//        参数校验
        if (StringUtils.isBlank(requestDTO.getUserId())||null==requestDTO.getActivityId()){
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                    .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                    .build();
        }
//        查询奖品列表
        List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardListByActivityId(requestDTO.getActivityId());
        log.info("查询奖品列表 activityId:{} strategyAwardEntities:{}", requestDTO.getActivityId(), strategyAwardEntities);
        String[] treeIds = strategyAwardEntities.stream().map(StrategyAwardEntity::getRuleModels).filter(StringUtils::isNotBlank).toArray(String[]::new);
//查询规则配置
        Map<String, Integer> ruleLockCountMap = raffleRule.queryAwardRuleLockCount(treeIds);
//        查询用户抽奖次数
        Integer userRaffleCount = raffleActivityAccountQuotaService.queryRaffleActivityAccountDayPartakeCount(requestDTO.getUserId(), requestDTO.getActivityId());
//        数据填充
        List<RaffleAwardListResponseDTO> response = strategyAwardEntities.stream().map(obj -> RaffleAwardListResponseDTO.builder()
                .awardId(obj.getAwardId())
                .awardTitle(obj.getAwardTitle())
                .awardSubtitle(obj.getAwardSubtitle())
                .sort(obj.getSort())
                .awardRuleLockCount(ruleLockCountMap.getOrDefault(obj.getRuleModels(),0))
                .isAwardUnLock(ruleLockCountMap.getOrDefault(obj.getRuleModels(),0) <= userRaffleCount)
                .waitUnLockCount(Math.max(0,ruleLockCountMap.getOrDefault(obj.getRuleModels(),0) - userRaffleCount))
                .build()).collect(Collectors.toList());
        log.info("查询奖品列表完成 userId:{} activityId:{} response:{})", requestDTO.getUserId(), requestDTO.getActivityId(), JSON.toJSONString(response));
        return Response.<List<RaffleAwardListResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(response)
                .build();
    }

    /**
     * 抽奖接口
     *
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "random_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleStrategyResponseDTO> randomRaffle(@RequestBody RaffleStrategyRequestDTO requestDTO) {
        log.info("抽奖开始 strategyId:{}", requestDTO.getStrategyId());
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                .userId("system")
                .strategyId(requestDTO.getStrategyId())
                .build());
        log.info("抽奖完成 strategyId:{} response:{}", requestDTO.getStrategyId(), JSON.toJSONString(raffleAwardEntity));
        RaffleStrategyResponseDTO response = RaffleStrategyResponseDTO.builder()
                .awardId(raffleAwardEntity.getAwardId())
                .awardIndex(raffleAwardEntity.getSort())
                .build();
        return Response.<RaffleStrategyResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(response)
                .build();
    }

    @Override
    public Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(RaffleStrategyRuleWeightRequestDTO requestDTO) {
        try{
            log.info("查询特殊奖品权重相关配置信息开始 userId:{},activityId:{}", requestDTO.getUserId(),requestDTO.getActivityId());
            if (StringUtils.isBlank(requestDTO.getUserId())||null==requestDTO.getActivityId()){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
            }
//            查询用户抽奖次数
            Integer userActivityAccountTotalUseCount = raffleActivityAccountQuotaService.queryRaffleActivityAccountPartakeCount(requestDTO.getUserId(), requestDTO.getActivityId());
//            查询权重奖品信息
            List<RuleWeightVO> ruleWeightVOS = raffleRule.queryAwardRuleWeightByActivityId(requestDTO.getActivityId());
            List<RaffleStrategyRuleWeightResponseDTO> response = ruleWeightVOS.stream().map(ruleWeightVO -> RaffleStrategyRuleWeightResponseDTO.builder()
                    .userActivityAccountTotalUseCount(userActivityAccountTotalUseCount)
                    .ruleWeightCount(ruleWeightVO.getWeight())
                    .strategyAwards(ruleWeightVO.getAwardList().stream().map(vo -> RaffleStrategyRuleWeightResponseDTO.StrategyAward.builder()
                            .awardId(vo.getAwardId())
                            .awardTitle(vo.getAwardTitle())
                            .build()).collect(Collectors.toList()))
                    .build()).collect(Collectors.toList());
            log.info("查询特殊奖品权重相关配置信息结束 userId:{},activityId:{},response:{}", requestDTO.getUserId(),requestDTO.getActivityId(),JSON.toJSONString(response));
            return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(response)
                    .build();
        }catch (Exception e){
            log.error("查询特殊奖品权重相关配置信息失败 userId:{},activityId:{}", requestDTO.getUserId(),requestDTO.getActivityId(),e);
            return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
