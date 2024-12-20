package com.aqiu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.aqiu.domain.activity.service.IRaffleActivitySkuStockService;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 活动库存耗尽mq消息
 */
@Slf4j
@Component
public class ActivitySkuStockZeroCustomer {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Resource
    private IRaffleActivitySkuStockService skuStock;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.activity_sku_stock_zero}"))
    public void listener(String message){
        try{
            log.info("监听活动sku库存消耗为0消息 topic:{} message:{}",topic,message);
            BaseEvent.EventMessage<Long> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<Long>>() {
            }.getType());
            Long sku = eventMessage.getData();
            skuStock.clearActivitySkuStock(sku);
        }catch (Exception e){
            log.error("监听活动sku库存消耗为0消息，消费失败，topic:{} message:{}",topic,message);
            throw e;
        }
    }

}
