package com.aqiu.domain.rebate.model.entity;

import com.aqiu.domain.rebate.event.SendRebateMessageEvent;
import com.aqiu.domain.rebate.model.valobj.TaskStateVO;
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
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    private TaskStateVO state;
}
