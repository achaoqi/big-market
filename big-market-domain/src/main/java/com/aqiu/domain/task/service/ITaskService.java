package com.aqiu.domain.task.service;

import com.aqiu.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * 消息任务服务接口
 */
public interface ITaskService {

    List<TaskEntity> queryNoSendMessageTaskList();

    void updateTaskSendMessageCompleted(String userId,String messageId);

    void updateTaskSendMessageFail(String userId,String messageId);

    public void sendMessage(TaskEntity task);
}
