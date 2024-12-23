package com.aqiu.domain.activity.service;

import com.aqiu.domain.activity.model.entity.ActivityAccountEntity;
import com.aqiu.domain.activity.model.entity.SkuRechargeEntity;

/**
 * 抽奖活动订单接口
 */
public interface IRaffleActivityAccountQuotaService {

    /**
     * 创建sku充值订单
     * @param skuRechargeEntity
     */
    String createOrder(SkuRechargeEntity skuRechargeEntity);

    /**
     * 查询用户当天参与抽奖活动次数
     * @param userId
     * @param activityId
     * @return
     */
    Integer queryRaffleActivityAccountDayPartakeCount(String userId, Integer activityId);

    /**
     * 查询用户账户抽奖额度信息
     * @param userId
     * @param activityId
     * @return
     */
    ActivityAccountEntity queryRaffleActivityAccount(String userId, Long activityId);

    Integer queryRaffleActivityAccountPartakeCount(String userId, Long activityId);
}
