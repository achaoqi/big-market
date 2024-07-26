package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {

    public List<StrategyRule> queryStrategyRuleList();

    StrategyRule queryStrategyRule(StrategyRule req);
}
