package com.aqiu.domain.activity.service.rule.impl;

import com.aqiu.domain.activity.model.entity.ActivityCountEntity;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.ActivitySkuEntity;
import com.aqiu.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.armory.IActivityDispatch;
import com.aqiu.domain.activity.service.rule.AbstractActionChain;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【有效期，状态、库存（sku）校验开始 sku:{},activityId:{}】",activitySkuEntity.getSku(),activity.getActivityId());
//        扣减库存
        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activity.getEndDateTime());
        if (status){
            log.info("活动责任链-商品库存处理【有效期，状态、库存（sku）成功 sku:{},activityId:{}】",activitySkuEntity.getSku(),activity.getActivityId());
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                            .sku(activitySkuEntity.getSku())
                            .activityId(activity.getActivityId())
                    .build());
            return true;
        }
        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR);
    }
}
