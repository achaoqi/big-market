package com.aqiu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.aqiu.domain.activity.model.entity.SkuRechargeEntity;
import com.aqiu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.aqiu.domain.rebate.event.SendRebateMessageEvent;
import com.aqiu.domain.rebate.model.valobj.RebateTypeVO;
import com.aqiu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 行为返利消息接收
 */
@Slf4j
@Component
public class RebateMessageCustomer {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message){
        try{
            log.info("监听行为返利消息 topic:{} message:{}",topic,message);
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.RebateMessage eventMessageData = eventMessage.getData();
            if (!RebateTypeVO.SKU.getCode().equals(eventMessageData.getRebateType())){
                log.info("监听用户行为返利消息 非sku，展示不予处理,topic:{},message:{}",topic,message);
                return;
            }
//            sku入账奖励
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setSku(Long.valueOf(eventMessageData.getRebateConfig()));
            skuRechargeEntity.setUserId(eventMessageData.getUserId());
            skuRechargeEntity.setOutBusinessNo(eventMessageData.getBizId());
            raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);

            log.info("监听行为返利消息，消费成功，topic:{} message:{}",topic,eventMessage);
        }catch (Exception e){
            log.error("监听行为返利消息消息，消费失败，topic:{} message:{}",topic,message);
            throw e;
        }
    }
}
