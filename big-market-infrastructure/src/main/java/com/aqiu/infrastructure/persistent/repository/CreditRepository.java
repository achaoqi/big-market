package com.aqiu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.aqiu.domain.award.model.valobj.AccountStatusVO;
import com.aqiu.domain.credit.model.aggregate.TradeAggregate;
import com.aqiu.domain.credit.model.entity.CreditAccountEntity;
import com.aqiu.domain.credit.model.entity.CreditOrderEntity;
import com.aqiu.domain.credit.model.entity.TaskEntity;
import com.aqiu.domain.credit.repository.ICreditRepository;
import com.aqiu.infrastructure.event.EventPublisher;
import com.aqiu.infrastructure.persistent.dao.ITaskDao;
import com.aqiu.infrastructure.persistent.dao.IUserCreditAccountDao;
import com.aqiu.infrastructure.persistent.dao.IUserCreditOrderDao;
import com.aqiu.infrastructure.persistent.po.Task;
import com.aqiu.infrastructure.persistent.po.UserCreditAccount;
import com.aqiu.infrastructure.persistent.po.UserCreditOrder;
import com.aqiu.infrastructure.persistent.redis.IRedisService;
import com.aqiu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class CreditRepository implements ICreditRepository {

    @Resource
    private IRedisService redisService;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IUserCreditOrderDao userCreditOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(userId);
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        RLock lock = redisService.getLock(Constants.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo());
        try{
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute((status -> {
                try{
                    UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (userCreditAccount==null){
                        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());
                        userCreditAccountDao.insert(userCreditAccountReq);
                    }else{
                        userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                    }
                    userCreditOrderDao.insert(userCreditOrderReq);
//                    写入任务
                    taskDao.insert(task);
                    return 1;
                }catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;
            }));
        }finally {
            lock.unlock();
            dbRouter.clear();
        }

        try {
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库记录，task 任务表
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("调整账户积分记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());
        } catch (Exception e) {
            log.error("调整账户积分记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }
}
