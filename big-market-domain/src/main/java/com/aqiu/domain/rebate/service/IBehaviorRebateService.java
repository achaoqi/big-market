package com.aqiu.domain.rebate.service;

import com.aqiu.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * 行为返利服务接口
 */
public interface IBehaviorRebateService {

    List<String> createOrder(BehaviorEntity behaviorEntity);

}
