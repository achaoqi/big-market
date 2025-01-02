package com.aqiu.domain.activity.service.quota.policy;

import com.aqiu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);

}
