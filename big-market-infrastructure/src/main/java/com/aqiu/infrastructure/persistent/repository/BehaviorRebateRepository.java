package com.aqiu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.aqiu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.aqiu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.aqiu.domain.rebate.model.entity.TaskEntity;
import com.aqiu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.aqiu.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.aqiu.domain.rebate.repository.IBehaviorRebateRepository;
import com.aqiu.infrastructure.event.EventPublisher;
import com.aqiu.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import com.aqiu.infrastructure.persistent.dao.ITaskDao;
import com.aqiu.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import com.aqiu.infrastructure.persistent.po.DailyBehaviorRebate;
import com.aqiu.infrastructure.persistent.po.Task;
import com.aqiu.infrastructure.persistent.po.UserBehaviorRebateOrder;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;

    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;

    @Resource
    private ITaskDao taskDao;

    @Resource
    private IDBRouterStrategy dbRouter;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private EventPublisher eventPublisher;

    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorTypeVO.getCode());
        return dailyBehaviorRebates.stream().map(dailyBehaviorRebate -> DailyBehaviorRebateVO.builder()
                .behaviorType(dailyBehaviorRebate.getBehaviorType())
                .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                .rebateType(dailyBehaviorRebate.getRebateType())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try{
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try{
                    for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
//                        返利订单构建
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                        userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                        userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                        userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                        userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                        userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                        userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
                        userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

//                        任务对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                        Task task = new Task();
                        task.setState(taskEntity.getState().getCode());
                        task.setUserId(taskEntity.getUserId());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setTopic(taskEntity.getTopic());
                        taskDao.insert(task);
                    }
                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入返利记录，唯一索引冲突 userId:{}" , userId,e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(),e);
                }
            });
        }finally {
            dbRouter.clear();
        }

//        异步发送mq消息
        for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
            executor.execute(()->{
                TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                Task task=new Task();
                task.setUserId(taskEntity.getUserId());
                task.setMessageId(taskEntity.getMessageId());
                try{
                    eventPublisher.publish(taskEntity.getTopic(),taskEntity.getMessage());
                    taskDao.updateTaskSendMessageCompleted(task);
                }catch (Exception e){
                    log.error("发送MQ消息失败 userId:{} messageId:{} topic:{}", behaviorRebateAggregate.getTaskEntity().getUserId(), behaviorRebateAggregate.getTaskEntity().getMessageId(), behaviorRebateAggregate.getTaskEntity().getTopic(),e);
                    taskDao.updateTaskSendMessageFail(task);
                }
            });
        }
    }
}
