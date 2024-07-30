package com.aqiu.trigger.job;

import com.aqiu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.aqiu.domain.strategy.service.IRaffleStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class UpdateAwardStockJob {

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try{
            log.info("定时任务，更新奖品消耗库存【延迟队列获取，防止产生竞争】");
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
            if (strategyAwardStockKeyVO==null){
                return;
            }
            log.info("定时任务，更新奖品消耗库存 strategyId:{} awardId:{}",strategyAwardStockKeyVO.getStrategyId(),strategyAwardStockKeyVO.getAwardId());
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(),strategyAwardStockKeyVO.getAwardId());
        } catch (InterruptedException e) {
            log.error("定时任务,更新库存失败",e);
        }
    }

}
