package com.aqiu.domain.rebate.service;

import com.aqiu.domain.rebate.event.SendRebateMessageEvent;
import com.aqiu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.aqiu.domain.rebate.model.entity.BehaviorEntity;
import com.aqiu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.aqiu.domain.rebate.model.entity.TaskEntity;
import com.aqiu.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.aqiu.domain.rebate.model.valobj.TaskStateVO;
import com.aqiu.domain.rebate.repository.IBehaviorRebateRepository;
import com.aqiu.types.common.Constants;
import com.aqiu.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BehaviorRebateService implements IBehaviorRebateService {
    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
//        查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());
        if (dailyBehaviorRebateVOS == null || dailyBehaviorRebateVOS.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> orderIds=new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
//构建聚合对象
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
//            拼装业务ID
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessId();
//            构建订单对象
            BehaviorRebateOrderEntity behaviorRebateOrderEntity=BehaviorRebateOrderEntity.builder()
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .bizId(bizId)
                    .outBusinessNo(behaviorEntity.getOutBusinessId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .userId(behaviorEntity.getUserId())
                    .build();
//            MQ消息对象
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .bizId(bizId)
                    .build();
//            构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);
            TaskEntity task = TaskEntity.builder()
                    .message(eventMessage)
                    .topic(sendRebateMessageEvent.topic())
                    .userId(behaviorEntity.getUserId())
                    .state(TaskStateVO.create)
                    .messageId(eventMessage.getId())
                    .build();
//            构建聚合对象
            BehaviorRebateAggregate behaviorRebateAggregate=new BehaviorRebateAggregate();
            behaviorRebateAggregate.setBehaviorRebateOrderEntity(behaviorRebateOrderEntity);
            behaviorRebateAggregate.setUserId(behaviorEntity.getUserId());
            behaviorRebateAggregate.setTaskEntity(task);
//填充集合，后续统一处理
            behaviorRebateAggregates.add(behaviorRebateAggregate);
            orderIds.add(behaviorRebateOrderEntity.getOrderId());
        }
//存储聚合对象数据
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(),behaviorRebateAggregates);


        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId,outBusinessNo);
    }
}
