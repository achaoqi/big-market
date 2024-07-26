package com.aqiu.domain.strategy.service.armory;

import com.aqiu.domain.strategy.model.entity.StrategyAwardEntity;
import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.model.entity.StrategyRuleEntity;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class StrategyArmoryDispatch implements IStrategyArmory,IStrategyDispatch{

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Integer strategyId) {
        //查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        if (CollectionUtils.isEmpty(strategyAwardEntities)) return false;
        assembleLotteryStrategy(String.valueOf(strategyId),strategyAwardEntities);

//        权重配置策略，适用于rule_weight
        StrategyEntity strategyEntity=repository.getStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (StringUtils.isBlank(ruleWeight)) return true;

        StrategyRuleEntity strategyRuleEntity=repository.queryStrategyRule(strategyId, ruleWeight);
        if (null==strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL);
        }

        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValue();
        ruleWeightValueMap.forEach((k, v) -> {
            List<StrategyAwardEntity> strategyAwardEntitiesClone=new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity->!v.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(k),strategyAwardEntitiesClone);
        });
        return true;
    }

    private void assembleLotteryStrategy(String key,List<StrategyAwardEntity> strategyAwardEntities){
        //求最小概率
        BigDecimal minRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        BigDecimal minRateRound = roundToSingleSignificantDigit(minRate);
        //概率值总和 这里设置的是1
        BigDecimal totalRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //求取概率范围
        BigDecimal rateRange = totalRate.divide(minRateRound, 0, RoundingMode.CEILING);
        ArrayList<Integer> strategyAwardSearchRateTables=new ArrayList<>(rateRange.intValue());
//        获取抽奖奖励对应AwardId
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i=0;i<awardRate.multiply(rateRange).setScale(0,RoundingMode.CEILING).intValue();i++){
                strategyAwardSearchRateTables.add(strategyAwardEntity.getAwardId());
            }
        }
//乱序并存储到Map
        Collections.shuffle(strategyAwardSearchRateTables);

        HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables = new HashMap<>();
        for(int i=0;i<strategyAwardSearchRateTables.size();i++){
            shuffleStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }
//缓存到redis
        repository.storeStrategyAwardSearchRateTables(key,shuffleStrategyAwardSearchRateTables.size(),shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public Integer getRandomAwardId(Integer strategyId) {
        int rateRange = repository.getRateRange(strategyId);
        int choice = new SecureRandom().nextInt(rateRange);
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId),choice);
    }

    @Override
    public Integer getRandomAwardId(Integer strategyId, String ruleWeightValue) {
        String key=String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        int rateRange = repository.getRateRange(key);
        int choice = new SecureRandom().nextInt(rateRange);
        return repository.getStrategyAwardAssemble(key,choice);
    }

    /**
     * 将 BigDecimal 值舍入到最小有效非零位数为 1 的形式，其余位数为 0。
     *
     * @param value 要处理的 BigDecimal 值
     * @return 舍入后的 BigDecimal 值
     */
    public static BigDecimal roundToSingleSignificantDigit(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        int scale = value.scale();
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < scale-1; i++) {
            sb.append("0");
        }
        sb.append("1");
        return new BigDecimal(sb.toString());
    }
}
