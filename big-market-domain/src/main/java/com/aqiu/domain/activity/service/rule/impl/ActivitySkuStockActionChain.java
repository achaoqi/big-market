package com.aqiu.domain.activity.service.rule.impl;

import com.aqiu.domain.activity.model.entity.ActivityCountEntity;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.ActivitySkuEntity;
import com.aqiu.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品基础信息校验开始");
        return true;
    }
}
