package com.aqiu.domain.activity.service.armory;

import java.util.Date;

public interface IActivityDispatch {
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
