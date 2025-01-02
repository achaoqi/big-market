package com.aqiu.domain.credit.model.entity;

import com.aqiu.domain.award.model.valobj.TaskStateVO;
import com.aqiu.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.aqiu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    private String userId;
    private String topic;
    private String messageId;
    private BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message;
    private TaskStateVO state;
}
