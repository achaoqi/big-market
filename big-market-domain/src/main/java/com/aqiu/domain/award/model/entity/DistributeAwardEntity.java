package com.aqiu.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributeAwardEntity {
    private String userId;
    private String orderId;
    private Integer awardId;
    private String awardConfig;
}
