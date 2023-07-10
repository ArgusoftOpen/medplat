package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.model.DailyNutritionLogBean;

public interface DailyNutritionLogService {

    DailyNutritionLogBean retrieveDailyNutritionLogByLocationId(Integer locationId);

    void createOrUpdateDailyNutritionLogByLocationId(Integer locationId);
}
