package com.aqiu.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserCreditAccount {
//    自增ID
    private Long id;
//    用户ID
    private String userId;
//    总积分
    private BigDecimal totalAmount;
//    可用积分
    private BigDecimal availableAmount;
//    状态
    private String accountStatus;
    private Date createTime;
    private Date updateTime;
}
