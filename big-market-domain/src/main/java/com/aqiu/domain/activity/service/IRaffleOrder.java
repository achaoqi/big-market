package com.aqiu.domain.activity.service;

import com.aqiu.domain.activity.model.entity.ActivityOrderEntity;
import com.aqiu.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * 抽奖活动订单接口
 */
public interface IRaffleOrder {

    /**
     * 以sku创建活动订单，获得参与抽奖资格
     * @param activityShopCartEntity
     * @return
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);

}
