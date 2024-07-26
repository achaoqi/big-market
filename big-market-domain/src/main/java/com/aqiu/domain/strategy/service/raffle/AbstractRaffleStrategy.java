package com.aqiu.domain.strategy.service.raffle;

import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.entity.StrategyEntity;
import com.aqiu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.IRaffleStrategy;
import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import com.aqiu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.aqiu.types.common.Constants;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 抽奖策略抽象类
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {
    protected IStrategyRepository repository;

    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Integer strategyId = raffleFactorEntity.getStrategyId();
        if (strategyId==null|| StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }
//        策略查询
        StrategyEntity strategy=repository.queryStrategyEntityByStrategyId(strategyId);
//        抽奖前-规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity=this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder()
                .strategyId(strategyId)
                .userId(userId)
                .build()
        ,strategy.getRuleModels().split(Constants.SPLIT));

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
                //黑名单
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            }else if(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())){
//                权重根据返回的信息进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

//        默认抽奖流程
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    public abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity build,String... logics);
}