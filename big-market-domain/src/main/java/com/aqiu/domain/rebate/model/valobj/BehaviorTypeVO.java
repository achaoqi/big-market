package com.aqiu.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 行为类型
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {
    SING("sign","签到(日历)"),
    OPENAI_PAY("openai_pay","openai 外部支付完成"),
    ;

    private final String code;
    private final String info;

}
