package com.aqiu.domain.activity.service.partake;

import com.aqiu.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.aqiu.domain.activity.model.entity.*;
import com.aqiu.domain.activity.model.valobj.UserRaffleOrderStateVO;
import com.aqiu.domain.activity.repository.IActivityRepository;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartakeService{
    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    public RaffleActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {
        ActivityEntity activity = activityRepository.queryRaffleActivityByActivityId(activityId);
//        构建订单
        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userId);
        userRaffleOrderEntity.setActivityId(activityId);
        userRaffleOrderEntity.setActivityName(activity.getActivityName());
        userRaffleOrderEntity.setOrderState(UserRaffleOrderStateVO.create);
        userRaffleOrderEntity.setOrderTime(currentDate);
        userRaffleOrderEntity.setStrategyId(activity.getStrategyId());
        userRaffleOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        return userRaffleOrderEntity;
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
//        查询账户额度
        ActivityAccountEntity activityAccountEntity=activityRepository.queryActivityAccountByUserId(userId,activityId);
//        判断账户总额度
        if (activityAccountEntity==null||activityAccountEntity.getTotalCountSurplus()<=0){
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR);
        }

        String month=dateFormatMonth.format(currentDate),day=dateFormatDay.format(currentDate);
        ActivityAccountMonthEntity activityAccountMonthEntity=activityRepository.queryActivityAccountMonthByUserId(userId,activityId,month);
//        判断月账户额度是否存在
        if (activityAccountMonthEntity!=null&&activityAccountMonthEntity.getMonthCountSurplus()<=0){
            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR);
        }
//        创建月账户额度
        boolean isExistAccountMonth=activityAccountMonthEntity!=null;
        if (activityAccountMonthEntity==null){
            activityAccountMonthEntity=new ActivityAccountMonthEntity();
            activityAccountMonthEntity.setUserId(userId);
            activityAccountMonthEntity.setMonth(month);
            activityAccountMonthEntity.setActivityId(activityId);
            activityAccountMonthEntity.setMonthCount(activityAccountEntity.getMonthCount());
            activityAccountMonthEntity.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
        }
//查询日账户
        ActivityAccountDayEntity activityAccountDayEntity=activityRepository.queryActivityAccountDayByUserId(userId,activityId,day);
//        判断日账户额度是否存在
        if (activityAccountDayEntity!=null&&activityAccountDayEntity.getDayCountSurplus()<=0){
            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR);
        }
        //        创建日账户额度
        boolean isExistAccountDay=activityAccountDayEntity!=null;
        if (activityAccountDayEntity==null){
            activityAccountDayEntity=new ActivityAccountDayEntity();
            activityAccountDayEntity.setUserId(userId);
            activityAccountDayEntity.setDay(day);
            activityAccountDayEntity.setActivityId(activityId);
            activityAccountDayEntity.setDayCount(activityAccountEntity.getMonthCount());
            activityAccountDayEntity.setDayCountSurplus(activityAccountEntity.getMonthCountSurplus());
        }

//        构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = new CreatePartakeOrderAggregate();
        createPartakeOrderAggregate.setActivityId(activityId);
        createPartakeOrderAggregate.setUserId(userId);
        createPartakeOrderAggregate.setActivityAccountEntity(activityAccountEntity);
        createPartakeOrderAggregate.setActivityAccountMonthEntity(activityAccountMonthEntity);
        createPartakeOrderAggregate.setActivityAccountDayEntity(activityAccountDayEntity);
        createPartakeOrderAggregate.setExistAccountDay(isExistAccountDay);
        createPartakeOrderAggregate.setExistAccountMonth(isExistAccountMonth);
        return createPartakeOrderAggregate;
    }
}
