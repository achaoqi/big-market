package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class Strategy {
    /**
     * 自增id
     */
    private Integer id ;
    /**
     * 策略id
     */
    private Integer strategyId;
    /**
     * 策略描述
     */
    private String strategyDesc ;
    /**
     * 策略模型
     */
    private String ruleModels;
    /**
     * 创建时间
     */
    private Date createTime ;
    /**
     * 更新时间
     */
    private Date updateTime;
}
