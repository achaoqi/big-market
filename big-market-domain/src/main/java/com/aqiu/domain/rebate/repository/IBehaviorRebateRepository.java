package com.aqiu.domain.rebate.repository;

import com.aqiu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.aqiu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.aqiu.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

public interface IBehaviorRebateRepository {

    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);

}
