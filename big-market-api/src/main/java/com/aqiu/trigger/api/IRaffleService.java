package com.aqiu.trigger.api;

import com.aqiu.trigger.api.dto.RaffleAwardListRequestDTO;
import com.aqiu.trigger.api.dto.RaffleAwardListResponseDTO;
import com.aqiu.trigger.api.dto.RaffleRequestDTO;
import com.aqiu.trigger.api.dto.RaffleResponseDTO;
import com.aqiu.types.model.Response;

import java.util.List;

/**
 * 抽奖服务接口
 */
public interface IRaffleService {
    /**
     * 策略装配接口
     * @param strategyId 策略ID
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Integer strategyId);

    /**
     * 查询策略奖品列表
     * @param requestDTO
     * @return
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     * 抽奖接口
     * @param requestDTO
     * @return
     */
    Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO);
}