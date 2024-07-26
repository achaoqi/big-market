package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDao {

    public List<Strategy> queryStrategyList();

    Strategy queryStrategyByStrategyId(Integer strategyId);
}
