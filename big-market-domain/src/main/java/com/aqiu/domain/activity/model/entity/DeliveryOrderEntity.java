package com.aqiu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 业务防重ID
     */
    private String outBusinessId;
}
