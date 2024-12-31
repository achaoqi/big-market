package com.aqiu.domain.credit.repository;

import com.aqiu.domain.credit.model.aggregate.TradeAggregate;

public interface ICreditRepository {
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);
}
