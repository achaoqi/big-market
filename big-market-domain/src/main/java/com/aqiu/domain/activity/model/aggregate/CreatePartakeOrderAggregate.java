package com.aqiu.domain.activity.model.aggregate;

import com.aqiu.domain.activity.model.entity.ActivityAccountDayEntity;
import com.aqiu.domain.activity.model.entity.ActivityAccountEntity;
import com.aqiu.domain.activity.model.entity.ActivityAccountMonthEntity;
import com.aqiu.domain.activity.model.entity.UserRaffleOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参与活动订单聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {
    private String userId;
    private Long activityId;
    private ActivityAccountEntity activityAccountEntity;
    private ActivityAccountMonthEntity activityAccountMonthEntity;
    private ActivityAccountDayEntity activityAccountDayEntity;
    private boolean isExistAccountMonth=true;
    private boolean isExistAccountDay=true;
    private UserRaffleOrderEntity userRaffleOrderEntity;
}
