package com.aqiu.domain.award.model.entity;

import com.aqiu.domain.award.event.SendAwardMessageEvent;
import com.aqiu.domain.award.model.valobj.TaskStateVO;
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
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    private TaskStateVO state;

}
