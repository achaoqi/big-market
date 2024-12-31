package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IAwardDao {

    public List<Award> queryAwardList();

    String queryAwardConfigByAwardId(@Param("awardId") Integer awardId);

    String queryAwardKeyByAwardId(@Param("awardId") Integer awardId);
}
