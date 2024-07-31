package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StrategyAward {
    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 抽奖策略ID
     */
    private Integer strategyId;
    /**
     * 奖品ID
     */
    private Integer awardId;
    /**
     * 奖品父标题
     */
    private String awardTitle;
    /**
     * 副标题
     */
    private String awardSubtitle;
    /**
     * 奖品数量
     */
    private Integer awardCount;
    /**
     * 奖品剩余数量
     */
    private Integer awardCountSurplus;
    /**
     * 奖品中将概率
     */
    private BigDecimal awardRate;
    /**
     * 规则模型
     */
    private String ruleModels;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
