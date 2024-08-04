package com.aqiu.domain.activity.service.quota.rule.impl;

import com.aqiu.domain.activity.model.entity.ActivityCountEntity;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.ActivitySkuEntity;
import com.aqiu.domain.activity.model.valobj.ActivityStateVO;
import com.aqiu.domain.activity.service.quota.rule.AbstractActionChain;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-基础信息【有效期，状态、库存（sku）校验开始 sku:{},activityId:{}】",activitySkuEntity.getSku(),activity.getActivityId());
        Date begin = activity.getBeginDateTime(),end=activity.getEndDateTime();
        Date now = new Date();
        if (now.before(begin)||now.after(end)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR);
        }
        ActivityStateVO state = activity.getState();
        if (state!=ActivityStateVO.open){
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR);
        }
        if (activitySkuEntity.getStockCountSurplus()<=0){
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR);
        }

        return next().action(activitySkuEntity,activity,activityCountEntity);
    }
}
