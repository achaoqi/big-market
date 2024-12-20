package com.aqiu.trigger.api;

import com.aqiu.trigger.api.dto.ActivityDrawRequestDTO;
import com.aqiu.trigger.api.dto.ActivityDrawResponseDTO;
import com.aqiu.types.model.Response;

/**
 * 抽奖活动
 */
public interface IRaffleActivityService {
    /**
     * 数据预热缓存
     * @param activityId
     * @return
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request
     * @return
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 日历签到返利接口
     * @param userId
     * @return
     */
    Response<Boolean> calendarSignRebate(String userId);
}
