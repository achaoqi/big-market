package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAwardDao {

    public List<Award> queryAwardList();

}
