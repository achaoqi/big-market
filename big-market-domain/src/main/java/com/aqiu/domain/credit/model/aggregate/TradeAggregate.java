package com.aqiu.domain.credit.model.aggregate;

import com.aqiu.domain.award.model.valobj.TaskStateVO;
import com.aqiu.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.aqiu.domain.credit.model.entity.CreditAccountEntity;
import com.aqiu.domain.credit.model.entity.CreditOrderEntity;
import com.aqiu.domain.credit.model.entity.TaskEntity;
import com.aqiu.domain.credit.model.valobj.TradeNameVO;
import com.aqiu.domain.credit.model.valobj.TradeTypeVO;
import com.aqiu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeAggregate {
    private String userId;
    private CreditAccountEntity creditAccountEntity;
    private CreditOrderEntity creditOrderEntity;
    private TaskEntity taskEntity;

    public static CreditAccountEntity createCreditAccountEntity(String userId, BigDecimal adjustAmount){
        return CreditAccountEntity.builder()
                .userId(userId)
                .adjustAmount(adjustAmount)
                .build();
    }

    public static CreditOrderEntity createCreditOrderEntity(String userId,TradeNameVO tradeName, TradeTypeVO tradeType,BigDecimal tradeAmount,String outBusinessNo){
        return CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeName(tradeName)
                .tradeType(tradeType)
                .tradeAmount(tradeAmount)
                .outBusinessNo(outBusinessNo)
                .build();
    }

    public static TaskEntity createTaskEntity(String userId, String topic, String messageId, BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message){
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userId);
        taskEntity.setTopic(topic);
        taskEntity.setMessageId(messageId);
        taskEntity.setMessage(message);
        taskEntity.setState(TaskStateVO.create);
        return taskEntity;
    }
}
