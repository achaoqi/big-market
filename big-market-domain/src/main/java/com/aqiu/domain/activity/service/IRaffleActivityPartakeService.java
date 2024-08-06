package com.aqiu.domain.activity.service;

import com.aqiu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.aqiu.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * 抽奖活动参与接口
 */
public interface IRaffleActivityPartakeService {
    /**
     * 创建抽奖单：用户参与抽奖活动，扣减活动账户库存，产生抽奖单，如存在未被使用的抽奖单则直接返回以存在的抽奖单
     * @param partakeRaffleActivityEntity 参与活动实体对象
     * @return 用户抽奖订单实体对象
     */
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);

    /**
     * 创建抽奖单：用户参与抽奖活动，扣减活动账户库存，产生抽奖单，如存在未被使用的抽奖单则直接返回以存在的抽奖单
     * @param userId 用户ID
     * @param activityId 活动ID
     * @return 用户抽奖订单实体对象
     */
    UserRaffleOrderEntity createOrder(String userId, Long activityId);
}
