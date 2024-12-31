package com.aqiu.domain.award.model.aggregate;

import com.aqiu.domain.award.model.entity.UserAwardRecordEntity;
import com.aqiu.domain.award.model.entity.UserCreditAwardEntity;
import com.aqiu.domain.award.model.valobj.AwardStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 发放奖品聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveOutPrizesAggregate {
    private String userId;
    private UserAwardRecordEntity userAwardRecordEntity;
    private UserCreditAwardEntity userCreditAwardEntity;

    public static UserAwardRecordEntity buildUserAwardRecordEntity(String userId, String orderId, Integer awardId, AwardStateVO awardStateVO){
        return UserAwardRecordEntity.builder()
                .userId(userId)
                .orderId(orderId)
                .awardId(awardId)
                .awardState(awardStateVO)
                .build();
    }

    public static UserCreditAwardEntity buildUserCreditAwardEntity(String userId, BigDecimal creditAmount){
        return UserCreditAwardEntity.builder()
                .creditAmount(creditAmount)
                .userId(userId)
                .build();
    }
}


