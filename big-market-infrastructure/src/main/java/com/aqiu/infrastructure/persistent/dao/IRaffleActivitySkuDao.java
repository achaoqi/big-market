package com.aqiu.infrastructure.persistent.dao;

import com.aqiu.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
