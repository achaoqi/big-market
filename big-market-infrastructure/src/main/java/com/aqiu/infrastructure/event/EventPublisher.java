package com.aqiu.infrastructure.event;

import com.alibaba.fastjson.JSON;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class EventPublisher {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage){
        try{
            String messageJson= JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic, messageJson);
            log.info("发送MQ消息 topic:{},message:{}",topic,messageJson);
        }catch (Exception e){
            log.error("发送MQ消息失败 topic:{},message:{}",topic,eventMessage);
            throw e;
        }
    }

    public void publish(String topic, String jsonMessage){
        try{
            rabbitTemplate.convertAndSend(topic, jsonMessage);
            log.info("发送MQ消息 topic:{},message:{}",topic,jsonMessage);
        }catch (Exception e){
            log.error("发送MQ消息失败 topic:{},message:{}",topic,jsonMessage,e);
            throw e;
        }
    }
}
