package com.aqiu.domain.activity.service.quota.policy.impl;

import com.aqiu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.aqiu.domain.activity.model.valobj.OrderStateVO;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
