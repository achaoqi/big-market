package com.aqiu.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.entity.*;
import com.aqiu.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRaffleActivity implements IRaffleOrder{
    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
//        通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
//        查询活动信息
        ActivityEntity activity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
//        查询次数信息(用户在活动上可参与的次数)
        ActivityCountEntity activityCountEntity=activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果:{} {} {}", JSON.toJSONString(activitySkuEntity),JSON.toJSONString(activity),JSON.toJSONString(JSON.toJSONString(activityCountEntity)));
        return ActivityOrderEntity.builder().build();
    }
}
