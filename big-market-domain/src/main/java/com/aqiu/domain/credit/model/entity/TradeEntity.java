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
public class TradeEntity {
    private String userId;
    private TradeNameVO tradeName;
    private TradeTypeVO tradeType;
    private BigDecimal amount;
    private String outBusinessNo;
}
