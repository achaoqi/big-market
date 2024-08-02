package com.aqiu.domain.activity.service.rule.impl;

import com.aqiu.domain.activity.model.entity.ActivityCountEntity;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.ActivitySkuEntity;
import com.aqiu.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存校验开始");
        return next().action(activitySkuEntity,activity,activityCountEntity);
    }
}
