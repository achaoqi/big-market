package com.aqiu.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.aggregate.CreateOrderAggregate;
import com.aqiu.domain.activity.model.entity.*;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.rule.IActionChain;
import com.aqiu.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder{


    public AbstractRaffleActivity(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
//        通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
//        查询活动信息
        ActivityEntity activity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
//        查询次数信息(用户在活动上可参与的次数)
        ActivityCountEntity activityCountEntity=activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果:{} {} {}", JSON.toJSONString(activitySkuEntity),JSON.toJSONString(activity),JSON.toJSONString(JSON.toJSONString(activityCountEntity)));
        return ActivityOrderEntity.builder().build();
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
//参数校验
        Long sku = skuRechargeEntity.getSku();
        String userId = skuRechargeEntity.getUserId();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (sku==null|| StringUtils.isBlank(userId)||StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }
//        查询基础信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        ActivityEntity activity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
//        活动动作规则校验
        IActionChain iActionChain = defaultActivityChainFactory.openActionChain();
        iActionChain.action(activitySkuEntity, activity, activityCountEntity);
//        构建订单聚合对象
        CreateOrderAggregate orderAggregate = buildOrderAggregate(skuRechargeEntity,activitySkuEntity,activity,activityCountEntity);
//        保存订单
        doSaveOrder(orderAggregate);
//        返回单号
        return orderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract void doSaveOrder(CreateOrderAggregate orderAggregate);

    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity);
}
