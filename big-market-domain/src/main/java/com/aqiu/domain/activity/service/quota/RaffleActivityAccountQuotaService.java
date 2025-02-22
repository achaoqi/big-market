package com.aqiu.domain.activity.service.quota;

import com.aqiu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.aqiu.domain.activity.model.entity.*;
import com.aqiu.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.aqiu.domain.activity.model.valobj.OrderStateVO;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.IRaffleActivitySkuStockService;
import com.aqiu.domain.activity.service.quota.policy.ITradePolicy;
import com.aqiu.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class RaffleActivityAccountQuotaService extends AbstractRaffleActivityAccountQuota implements IRaffleActivitySkuStockService {


    public RaffleActivityAccountQuotaService(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, Map<String, ITradePolicy> tradePolicyMap) {
        super(activityRepository, defaultActivityChainFactory, tradePolicyMap);
    }

    @Override
    protected CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activity, ActivityCountEntity activityCountEntity) {
//        订单实体对象
        ActivityOrderEntity activityOrderEntity=new ActivityOrderEntity();
        activityOrderEntity.setUserId(skuRechargeEntity.getUserId());
        activityOrderEntity.setActivityId(activitySkuEntity.getActivityId());
        activityOrderEntity.setPayAmount(activitySkuEntity.getProductAmount());
        activityOrderEntity.setSku(skuRechargeEntity.getSku());
        activityOrderEntity.setActivityName(activity.getActivityName());
        activityOrderEntity.setStrategyId(activity.getStrategyId());
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setOutBusinessNo(skuRechargeEntity.getOutBusinessNo());
        return CreateQuotaOrderAggregate.builder()
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

    @Override
    public Integer queryActivitySkuStock(Long sku) {
        return activityRepository.queryActivitySkuStock(sku);
    }

    @Override
    public void updateOrder(DeliveryOrderEntity deliveryOrderEntity) {
        activityRepository.updateOrder(deliveryOrderEntity);
    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(String userId, Integer activityId) {
        return activityRepository.queryRaffleActivityAccountDayPartakeCount(userId, activityId);
    }

    @Override
    public ActivityAccountEntity queryRaffleActivityAccount(String userId, Long activityId) {
        return activityRepository.queryRaffleActivityAccount(userId,activityId);
    }

    @Override
    public Integer queryRaffleActivityAccountPartakeCount(String userId, Long activityId) {
        return activityRepository.queryRaffleActivityAccountPartakeCount(userId,activityId);
    }
}
