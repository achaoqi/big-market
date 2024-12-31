package com.aqiu.domain.award.service.distribute;

import com.aqiu.domain.award.model.entity.DistributeAwardEntity;

/**
 * 分发奖品接口
 */
public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);

}
