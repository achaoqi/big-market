package com.aqiu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖奖品请求对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListRequestDTO {
    private Integer strategyId;
}
