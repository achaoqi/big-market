package com.aqiu.domain.activity.service.rule.factory;

import com.aqiu.domain.activity.service.rule.IActionChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 责任链工厂
 */
@Service
public class DefaultActivityChainFactory {
    private final IActionChain actionChain;

    public DefaultActivityChainFactory(Map<String,IActionChain> actionChainGroups) {
        actionChain=actionChainGroups.get(ActionModel.activity_base_action.code);
        actionChain.appendNext(actionChainGroups.get(ActionModel.activity_sku_stock_action.code));
    }

    public IActionChain openActionChain() {
        return this.actionChain;
    }

    @Getter
    @AllArgsConstructor
    public enum ActionModel{
        activity_base_action("activity_base_action","活动库存，状态校验"),
        activity_sku_stock_action("activity_sku_stock_action","活动sku库存")
        ;

        private final String code;
        private final String info;
    }

}
