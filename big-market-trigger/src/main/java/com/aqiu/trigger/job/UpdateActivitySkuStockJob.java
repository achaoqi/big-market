package com.aqiu.trigger.job;

import com.aqiu.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.aqiu.domain.activity.service.ISkuStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class UpdateActivitySkuStockJob {
    @Resource
    private ISkuStock skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try {
            log.info("定时任务，更新活动sku缓存，【延迟队列获取】，降低对数据库的更新频次，不产生竞争");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (activitySkuStockKeyVO==null) return;
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}",activitySkuStockKeyVO.getSku(),activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (InterruptedException e) {
            log.error("定时任务，更新sku库存失败",e);
        }
    }
}
