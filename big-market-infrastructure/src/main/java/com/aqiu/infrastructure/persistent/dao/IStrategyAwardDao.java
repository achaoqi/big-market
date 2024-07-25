package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {

    public List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Integer strategyId);
}
