package com.aqiu.domain.activity.service.quota.rule;

import com.aqiu.domain.activity.model.entity.ActivityCountEntity;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.ActivitySkuEntity;

/**
 * 下单规则责任链
 */
public interface IActionChain extends IActionChainArmory{
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity);
}
