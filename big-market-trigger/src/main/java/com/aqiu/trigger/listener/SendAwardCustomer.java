package com.aqiu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.aqiu.domain.award.event.SendAwardMessageEvent;
import com.aqiu.domain.award.model.entity.DistributeAwardEntity;
import com.aqiu.domain.award.service.IAwardService;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SendAwardCustomer {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @Resource
    private IAwardService awardService;

    @RabbitListener(queuesToDeclare =@Queue(value = "${spring.rabbitmq.topic.send_award}"))
    public void listener(String message){
        try{
            log.info("监听用户奖品发送消息topic:{} message:{}",topic,message);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());
            SendAwardMessageEvent.SendAwardMessage sendAwardMessage = eventMessage.getData();
            DistributeAwardEntity distributeAwardEntity = new DistributeAwardEntity();
            distributeAwardEntity.setAwardConfig(sendAwardMessage.getAwardConfig());
            distributeAwardEntity.setAwardId(sendAwardMessage.getAwardId());
            distributeAwardEntity.setOrderId(sendAwardMessage.getOrderId());
            distributeAwardEntity.setUserId(sendAwardMessage.getUserId());
            awardService.distributeAward(distributeAwardEntity);
        }catch (Exception e){
            log.error("监听用户奖品发送消息,消费失败topic:{},message:{}",topic,message,e);
        }
    }

}
