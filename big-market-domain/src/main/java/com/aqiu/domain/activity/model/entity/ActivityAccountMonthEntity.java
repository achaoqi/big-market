package com.aqiu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountMonthEntity {
    private String userId;
    private Long activityId;
    private String month;
    private Integer monthCount;
    private Integer monthCountSurplus;
}
