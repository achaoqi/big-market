package com.aqiu.domain.credit.service;

import com.aqiu.domain.credit.model.aggregate.TradeAggregate;
import com.aqiu.domain.credit.model.entity.CreditAccountEntity;
import com.aqiu.domain.credit.model.entity.CreditOrderEntity;
import com.aqiu.domain.credit.model.entity.TradeEntity;
import com.aqiu.domain.credit.repository.ICreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class CreditAdjustService implements ICreditAdjustService{

    @Resource
    private ICreditRepository repository;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(tradeEntity.getUserId(), tradeEntity.getAmount());

        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getTradeType(), tradeEntity.getAmount(), tradeEntity.getOutBusinessNo());

        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(tradeEntity.getUserId())
                .creditOrderEntity(creditOrderEntity)
                .creditAccountEntity(creditAccountEntity)
                .build();

        repository.saveUserCreditTradeOrder(tradeAggregate);
        return creditOrderEntity.getOrderId();
    }
}
