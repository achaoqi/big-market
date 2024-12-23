package com.aqiu.domain.rebate.service;

import com.aqiu.domain.rebate.model.entity.BehaviorEntity;
import com.aqiu.domain.rebate.model.entity.BehaviorRebateOrderEntity;

import java.util.List;

/**
 * 行为返利服务接口
 */
public interface IBehaviorRebateService {

    List<String> createOrder(BehaviorEntity behaviorEntity);

    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId,String outBusinessNo);

}
