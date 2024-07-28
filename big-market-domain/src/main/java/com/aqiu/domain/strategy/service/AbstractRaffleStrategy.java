package com.aqiu.domain.strategy.service;

import com.aqiu.domain.strategy.model.entity.RaffleAwardEntity;
import com.aqiu.domain.strategy.model.entity.RaffleFactorEntity;
import com.aqiu.domain.strategy.model.entity.RuleActionEntity;
import com.aqiu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.aqiu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.aqiu.domain.strategy.repository.IStrategyRepository;
import com.aqiu.domain.strategy.service.armory.IStrategyDispatch;
import com.aqiu.domain.strategy.service.rule.chain.ILogicChain;
import com.aqiu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
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

    private final DefaultChainFactory chainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory chainFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.chainFactory = chainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Integer strategyId = raffleFactorEntity.getStrategyId();
        if (strategyId==null|| StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }
//责任链处理抽奖
        ILogicChain logicChain = chainFactory.getLogicChain(strategyId);
        Integer awardId = logicChain.logic(userId, strategyId);

//        查询奖品规则 抽奖中(拿到奖品ID时，过滤规则)，抽奖后(扣减完库存后过滤，抽奖中拦截和无库存走兜底)
        StrategyAwardRuleModelVO strategyAwardRuleModelVO=repository.queryStrategyAwardRuleModel(strategyId,awardId);

//        抽奖中-规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .strategyId(strategyId)
                .awardId(awardId)
                .userId(userId)
                .build(), strategyAwardRuleModelVO.raffleRuleCenterModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())) {
            log.info("【临时日志】中将中规则拦截，通过规则后rule_luck_award走兜底奖励");
            return RaffleAwardEntity.builder()
                    .awardDesc("中将中规则拦截，通过抽奖后规则 rule_luck_award走兜底奖励")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    public abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity build,String... logics);

    public abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity build,String... logics);
}
