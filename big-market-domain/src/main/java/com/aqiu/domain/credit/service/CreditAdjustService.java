package com.aqiu.domain.credit.service;

import com.aqiu.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.aqiu.domain.credit.model.aggregate.TradeAggregate;
import com.aqiu.domain.credit.model.entity.CreditAccountEntity;
import com.aqiu.domain.credit.model.entity.CreditOrderEntity;
import com.aqiu.domain.credit.model.entity.TaskEntity;
import com.aqiu.domain.credit.model.entity.TradeEntity;
import com.aqiu.domain.credit.repository.ICreditRepository;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class CreditAdjustService implements ICreditAdjustService{

    @Resource
    private ICreditRepository repository;

    @Resource
    private CreditAdjustSuccessMessageEvent creditAdjustSuccessMessageEvent;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(tradeEntity.getUserId(), tradeEntity.getAmount());

        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getTradeType(), tradeEntity.getAmount(), tradeEntity.getOutBusinessNo());

        CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = new CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage();
        creditAdjustSuccessMessage.setUserId(tradeEntity.getUserId());
        creditAdjustSuccessMessage.setOrderId(creditOrderEntity.getOrderId());
        creditAdjustSuccessMessage.setAmount(tradeEntity.getAmount());
        creditAdjustSuccessMessage.setOutBusinessNo(tradeEntity.getOutBusinessNo());
        BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> creditAdjustSuccessMessageEventMessage = creditAdjustSuccessMessageEvent.buildEventMessage(creditAdjustSuccessMessage);

        TaskEntity taskEntity = TradeAggregate.createTaskEntity(tradeEntity.getUserId(), creditAdjustSuccessMessageEvent.topic(), creditAdjustSuccessMessageEventMessage.getId(), creditAdjustSuccessMessageEventMessage);

        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(tradeEntity.getUserId())
                .creditOrderEntity(creditOrderEntity)
                .creditAccountEntity(creditAccountEntity)
                .taskEntity(taskEntity)
                .build();

        repository.saveUserCreditTradeOrder(tradeAggregate);
        return creditOrderEntity.getOrderId();
    }
}
