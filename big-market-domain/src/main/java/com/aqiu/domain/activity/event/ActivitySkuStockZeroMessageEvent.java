package com.aqiu.domain.activity.event;

import com.aqiu.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ActivitySkuStockZeroMessageEvent extends BaseEvent<Long> {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .data(sku)
                .timestamp(new Date())
                .id(RandomStringUtils.random(11))
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }
}
