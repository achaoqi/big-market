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
//    策略ID
    @Deprecated
    private Integer strategyId;
//    活动ID
    private Integer activityId;
//    用户ID
    private String userId;
}
