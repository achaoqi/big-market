package com.aqiu.trigger.http;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.service.IRaffleAward;
import com.aqiu.domain.strategy.service.IRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyArmory;
import com.aqiu.trigger.api.IRaffleService;
import com.aqiu.trigger.api.dto.RaffleAwardListRequestDTO;
import com.aqiu.trigger.api.dto.RaffleAwardListResponseDTO;
import com.aqiu.trigger.api.dto.RaffleRequestDTO;
import com.aqiu.trigger.api.dto.RaffleResponseDTO;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle")
public class IRaffleController implements IRaffleService {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleAward raffleAward;

    @Resource
    private IRaffleStrategy raffleStrategy;

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
        log.info("查询奖品列表开始 strategyId:{}", requestDTO.getStrategyId());
        List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
        log.info("查询奖品列表 strategyId:{} strategyAwardEntities:{}", requestDTO.getStrategyId(), strategyAwardEntities);
        List<RaffleAwardListResponseDTO> response = strategyAwardEntities.stream().map(obj -> RaffleAwardListResponseDTO.builder()
                .awardId(obj.getAwardId())
                .awardTitle(obj.getAwardTitle())
                .awardSubtitle(obj.getAwardSubtitle())
                .sort(obj.getSort())
                .build()).collect(Collectors.toList());
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
    public Response<RaffleResponseDTO> randomRaffle(@RequestBody RaffleRequestDTO requestDTO) {
        log.info("抽奖开始 strategyId:{}", requestDTO.getStrategyId());
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                .userId("system")
                .strategyId(requestDTO.getStrategyId())
                .build());
        log.info("抽奖完成 strategyId:{} response:{}", requestDTO.getStrategyId(), JSON.toJSONString(raffleAwardEntity));
        RaffleResponseDTO response = RaffleResponseDTO.builder()
                .awardId(raffleAwardEntity.getAwardId())
                .awardIndex(raffleAwardEntity.getSort())
                .build();
        return Response.<RaffleResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(response)
                .build();
    }
}
