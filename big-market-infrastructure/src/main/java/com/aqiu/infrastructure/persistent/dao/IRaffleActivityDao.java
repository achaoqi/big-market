package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动表Dao
 * @create 2024-03-09 10:04
 */
@Mapper
public interface IRaffleActivityDao {

    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

    long queryStrategyIdByActivityId(Long activityId);

    long queryActivityIdByStrategyId(Long strategyId);
}
