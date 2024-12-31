package com.aqiu.domain.credit.model.entity;

import com.aqiu.domain.credit.model.valobj.TradeNameVO;
import com.aqiu.domain.credit.model.valobj.TradeTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditOrderEntity {
    //    用户ID
    private String userId;
    //    订单ID
    private String orderId;
    //    交易名称
    private TradeNameVO tradeName;
    //    交易类型
    private TradeTypeVO tradeType;
    //    交易金额
    private BigDecimal tradeAmount;
    //    业务防重ID
    private String outBusinessNo;
}
