package com.aqiu.domain.activity.service;

import com.aqiu.domain.activity.model.aggregate.CreateOrderAggregate;
import com.aqiu.domain.activity.model.entity.*;
import com.aqiu.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.aqiu.domain.activity.model.valobj.OrderStateVO;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class RaffleActivityService extends AbstractRaffleActivity implements ISkuStock{

    public RaffleActivityService(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate orderAggregate) {
        activityRepository.doSaveOrder(orderAggregate);
    }

    @Override
    protected CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity) {
//        订单实体对象
        ActivityOrderEntity activityOrderEntity=new ActivityOrderEntity();
        activityOrderEntity.setUserId(skuRechargeEntity.getUserId());
        activityOrderEntity.setActivityId(activitySkuEntity.getActivityId());
        activityOrderEntity.setSku(skuRechargeEntity.getSku());
        activityOrderEntity.setActivityName(activity.getActivityName());
        activityOrderEntity.setStrategyId(activity.getStrategyId());
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setState(OrderStateVO.completed);
        activityOrderEntity.setOutBusinessNo(skuRechargeEntity.getOutBusinessNo());
        return CreateOrderAggregate.builder()
                .activityOrderEntity(activityOrderEntity)
                .userId(skuRechargeEntity.getUserId())
                .activityId(activity.getActivityId())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .totalCount(activityCountEntity.getTotalCount())
                .build();
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException {
        return activityRepository.takeQueueValue();
    }

    @Override
    public void clearQueueValue() {
        activityRepository.clearQueueValue();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        activityRepository.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        activityRepository.clearActivitySkuStock(sku);
    }
}
