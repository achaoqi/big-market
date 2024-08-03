package com.aqiu.domain.activity.service;

import com.aqiu.domain.activity.model.valobj.ActivitySkuStockKeyVO;

/**
 * 活动库存处理操作
 */
public interface ISkuStock {
    /**
     * 获取活动sku库存消耗队列
     * @return
     * @throws InterruptedException
     */
    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 延迟队列+任务趋势更新活动sku库存
     * @param sku
     */
    void updateActivitySkuStock(Long sku);

    /**
     * 缓存库存以消耗完毕，清楚数据库库存
     * @param sku
     */
    void clearActivitySkuStock(Long sku);
}
