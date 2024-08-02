package com.aqiu.domain.activity.service;

import com.aqiu.domain.activity.model.entity.ActivityCountEntity;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.ActivitySkuEntity;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.rule.factory.DefaultActivityChainFactory;

/**
 * 抽奖活动支撑类
 */
public class RaffleActivitySupport {
    protected DefaultActivityChainFactory defaultActivityChainFactory;
    protected IActivityRepository activityRepository;

    public RaffleActivitySupport(IActivityRepository activityRepository,DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku){
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId){
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId){
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
