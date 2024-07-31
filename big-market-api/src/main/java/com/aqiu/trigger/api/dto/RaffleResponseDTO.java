package com.aqiu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleResponseDTO {
    private Integer awardId;
    private Integer awardIndex;
}
