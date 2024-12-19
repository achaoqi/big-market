package com.aqiu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.aqiu.domain.rebate.event.SendRebateMessageEvent;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 行为返利消息接收
 */
@Slf4j
@Component
public class SendRebateCustomer {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @RabbitListener(queuesToDeclare = @Queue(value = "send_rebate"))
    public void listener(String message){
        try{
            log.info("监听行为返利消息 topic:{} message:{}",topic,message);
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());
            log.info("监听行为返利消息，消费成功，topic:{} message:{}",topic,eventMessage);
        }catch (Exception e){
            log.error("监听行为返利消息消息，消费失败，topic:{} message:{}",topic,message);
            throw e;
        }
    }
}
