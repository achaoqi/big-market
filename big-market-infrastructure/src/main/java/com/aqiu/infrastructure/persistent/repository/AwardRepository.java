package com.aqiu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.aqiu.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.aqiu.domain.award.model.entity.TaskEntity;
import com.aqiu.domain.award.model.entity.UserAwardRecordEntity;
import com.aqiu.domain.award.repository.IAwardRepository;
import com.aqiu.infrastructure.event.EventPublisher;
import com.aqiu.infrastructure.persistent.dao.ITaskDao;
import com.aqiu.infrastructure.persistent.dao.IUserAwardRecordDao;
import com.aqiu.infrastructure.persistent.dao.IUserRaffleOrderDao;
import com.aqiu.infrastructure.persistent.po.Task;
import com.aqiu.infrastructure.persistent.po.UserAwardRecord;
import com.aqiu.infrastructure.persistent.po.UserRaffleOrder;
import com.aqiu.types.enums.ResponseCode;
import com.aqiu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task=new Task();
        task.setUserId(userId);
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());


        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(userAwardRecordEntity.getUserId());
        userRaffleOrderReq.setOrderId(userAwardRecordEntity.getOrderId());

        try{
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try{
//                    写入记录
                    userAwardRecordDao.insert(userAwardRecord);
//                    写入任务
                    taskDao.insert(task);
//                    更新抽奖订单
                    int count = userRaffleOrderDao.updateRaffleOrderStateUsed(userRaffleOrderReq);
                    if (count!=1){
                        status.setRollbackOnly();
                        log.error("写入中奖记录,用户抽奖单已被使用 userId:{} awardId:{} activityId:{}", userId, awardId, activityId);
                        throw new AppException(ResponseCode.ACTIVITY_ORDER_ERROR);
                    }
                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入中奖记录,唯一索引冲突 userId:{} awardId:{} activityId:{}", userId, awardId, activityId,e);
                    throw new AppException(ResponseCode.INDEX_DUP);
                }
            });
        }finally {
            dbRouter.clear();
        }

//        使用线程池发送mq
        executor.execute(()->{
            try{
                eventPublisher.publish(task.getTopic(),task.getMessage());
//            更新任务
                taskDao.updateTaskSendMessageCompleted(task);
            }catch (Exception e){
                log.error("写入中奖记录,写入mq消息失败 topic:{} userId:{}", task.getTopic(), userId, e);
                taskDao.updateTaskSendMessageFail(task);
            }
        });

    }
}
