package com.aqiu.infrastructure.persistent.repository;

import com.aqiu.domain.task.model.entity.TaskEntity;
import com.aqiu.domain.task.repository.ITaskRepository;
import com.aqiu.infrastructure.event.EventPublisher;
import com.aqiu.infrastructure.persistent.dao.ITaskDao;
import com.aqiu.infrastructure.persistent.po.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> tasks = taskDao.queryNoSendMessageTaskList();
        return tasks.stream().map(k -> {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setMessage(k.getMessage());
            taskEntity.setTopic(k.getTopic());
            taskEntity.setMessageId(k.getMessageId());
            taskEntity.setUserId(k.getUserId());
            return taskEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskDao.updateTaskSendMessageCompleted(Task.builder()
                .userId(userId)
                .messageId(messageId)
                .build());
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskDao.updateTaskSendMessageFail(Task.builder()
                .userId(userId)
                .messageId(messageId)
                .build());
    }

    @Override
    public void sendMessage(TaskEntity task) {
        eventPublisher.publish(task.getTopic(),task.getMessage());
    }
}
