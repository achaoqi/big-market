package com.aqiu.domain.rebate.model.entity;

import com.aqiu.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行为实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    private String userId;
    private BehaviorTypeVO behaviorTypeVO;
    private String outBusinessId;

}
