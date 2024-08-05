package com.aqiu.domain.award.service;

import com.aqiu.domain.award.event.SendAwardMessageEvent;
import com.aqiu.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.aqiu.domain.award.model.entity.TaskEntity;
import com.aqiu.domain.award.model.entity.UserAwardRecordEntity;
import com.aqiu.domain.award.model.valobj.TaskStateVO;
import com.aqiu.domain.award.repository.IAwardRepository;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AwardService implements IAwardService {
    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
//        构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);
//构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setState(TaskStateVO.create);
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
//构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();
//        一个事务下存储聚合对象
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);

    }
}
