package com.aqiu.domain.award.service.distribute.impl;

import com.aqiu.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.aqiu.domain.award.model.entity.DistributeAwardEntity;
import com.aqiu.domain.award.model.entity.UserAwardRecordEntity;
import com.aqiu.domain.award.model.entity.UserCreditAwardEntity;
import com.aqiu.domain.award.model.valobj.AwardStateVO;
import com.aqiu.domain.award.repository.IAwardRepository;
import com.aqiu.domain.award.service.distribute.IDistributeAward;
import com.aqiu.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository awardRepository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        String awardConfig = distributeAwardEntity.getAwardConfig();
        Integer awardId = distributeAwardEntity.getAwardId();
        if (StringUtils.isBlank(awardConfig)){
            awardConfig = awardRepository.queryAwardConfig(awardId);
        }
        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length!=2){
            throw new RuntimeException("awardConfig :"+awardConfig +" 配置出错");
        }
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete
        );
        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(userAwardRecordEntity.getUserId(), creditAmount);

        GiveOutPrizesAggregate giveOutPrizesAggregate = GiveOutPrizesAggregate.builder()
                .userCreditAwardEntity(userCreditAwardEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .userId(distributeAwardEntity.getUserId())
                .build();

        awardRepository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min,BigDecimal max){
        if(min.equals(max)) return min;
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
