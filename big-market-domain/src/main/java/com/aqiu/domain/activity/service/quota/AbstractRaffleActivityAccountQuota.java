package com.aqiu.domain.activity.service.quota;

import com.aqiu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.aqiu.domain.activity.model.entity.*;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.aqiu.domain.activity.service.quota.policy.ITradePolicy;
import com.aqiu.domain.activity.service.quota.rule.IActionChain;
import com.aqiu.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {

    private final Map<String, ITradePolicy> tradePolicyMap;

    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, Map<String, ITradePolicy> tradePolicyMap) {
        super(activityRepository, defaultActivityChainFactory);
        this.tradePolicyMap = tradePolicyMap;
    }

    @Override
    public String createOrder(SkuRechargeEntity skuRechargeEntity) {
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
        CreateQuotaOrderAggregate orderAggregate = buildOrderAggregate(skuRechargeEntity,activitySkuEntity,activity,activityCountEntity);
//        保存订单
        ITradePolicy iTradePolicy = tradePolicyMap.get(skuRechargeEntity.getOrderTradeType().getCode());
        iTradePolicy.trade(orderAggregate);
//        返回单号
        return orderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity);
}
