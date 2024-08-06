package com.aqiu.domain.activity.service.partake;

import com.alibaba.fastjson.JSON;
import com.aqiu.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.aqiu.domain.activity.model.entity.ActivityEntity;
import com.aqiu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.aqiu.domain.activity.model.entity.UserRaffleOrderEntity;
import com.aqiu.domain.activity.model.valobj.ActivityStateVO;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.domain.activity.service.IRaffleActivityPartakeService;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public abstract class AbstractRaffleActivityPartakeService implements IRaffleActivityPartakeService {
    protected final IActivityRepository activityRepository;

    public AbstractRaffleActivityPartakeService(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
//        基础信息
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        String userId = partakeRaffleActivityEntity.getUserId();
        Date currentDate = new Date();

//        活动查询
        ActivityEntity activity = activityRepository.queryRaffleActivityByActivityId(activityId);
//        校验活动
        if (ActivityStateVO.open!=activity.getState()){
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR);
        }
        if (activity.getBeginDateTime().after(currentDate)||activity.getEndDateTime().before(currentDate)){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR);
        }

//        查询未使用订单
        UserRaffleOrderEntity userRaffleOrderEntity=activityRepository.queryNoUsedRaffleOrder(partakeRaffleActivityEntity);
        if (userRaffleOrderEntity!=null){
            log.info("存在参与未使用订单：userId:{},activityId:{},userRaffleOrderEntity:{}", userId, activityId, JSON.toJSONString(userRaffleOrderEntity));
            return userRaffleOrderEntity;
        }

//        账户额度过滤&返回账户构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate=this.doFilterAccount(userId,activityId,currentDate);

//        创建订单
        userRaffleOrderEntity = this.buildUserRaffleOrder(userId,activityId,currentDate);
//        填充对象
        createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrderEntity);
//        保存聚合对象，一个领域内的聚合就是一个事务
        activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);
        return userRaffleOrderEntity;
    }

    @Override
    public UserRaffleOrderEntity createOrder(String userId, Long activityId) {
        return createOrder(PartakeRaffleActivityEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .build());
    }

    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);
}
