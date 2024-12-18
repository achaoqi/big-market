package com.aqiu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {
//    奖品ID
    private Integer awardId;
//    奖品标题
    private String awardTitle;
//    奖品副标题【抽奖一次后解锁】
    private String awardSubtitle;
//    排序编号
    private Integer sort;
//    抽奖N次后解释，未配置则为空
    private Integer awardRuleLockCount;
//    是否已解锁
    private Boolean isAwardUnLock;
//    用户等待抽奖解锁次数 规定解锁次数-用户已经抽奖次数
    private Integer waitUnLockCount;
}
