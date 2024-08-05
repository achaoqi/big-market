package com.aqiu.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.aqiu.domain.task.model.entity.TaskEntity;
import com.aqiu.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 发送mq消息队列任务
 */
@Slf4j
@Component
public class SendMessageTaskJob {

    @Resource
    private ITaskService taskService;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private ThreadPoolExecutor executor;


    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            int dbCount = dbRouter.dbCount();
            for (int dbIdx = 1; dbIdx <= dbCount; dbIdx++) {
                int finalDbIdx = dbIdx;
                executor.execute(() -> {
                    try{
                        dbRouter.setDBKey(finalDbIdx);
                        dbRouter.setTBKey(0);
                        List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
                        if (taskEntities.isEmpty()) return;
                        for (TaskEntity taskEntity : taskEntities) {
                            executor.execute(()->{
                                try{
                                    taskService.sendMessage(taskEntity);
                                    taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                                }catch (Exception e){
                                    log.error("定时任务,发送MQ消息失败 userId:{} messageId:{} topic:{}", taskEntity.getUserId(), taskEntity.getMessageId(), taskEntity.getTopic(),e);
                                    taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
                                }
                            });
                        }
                    }finally {
                        dbRouter.clear();
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务扫描mq任务表发送失败", e);
        }finally {
            dbRouter.clear();
        }
    }
}

