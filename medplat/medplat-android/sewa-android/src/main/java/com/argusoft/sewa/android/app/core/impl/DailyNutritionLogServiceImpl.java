package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.core.DailyNutritionLogService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.DailyNutritionLogBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.Date;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class DailyNutritionLogServiceImpl implements DailyNutritionLogService {

    private static final String TAG = "DNLService";

    @OrmLiteDao(helper = DBConnection.class)
    Dao<DailyNutritionLogBean, Integer> dailyNutritionLogBeanDao;

    @Override
    public DailyNutritionLogBean retrieveDailyNutritionLogByLocationId(Integer locationId) {
        try {
            return dailyNutritionLogBeanDao.queryBuilder().where().eq("locationId", locationId).queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void createOrUpdateDailyNutritionLogByLocationId(Integer locationId) {
        try {
            DailyNutritionLogBean dailyNutritionLogBean = dailyNutritionLogBeanDao.queryBuilder().where().eq("locationId", locationId).queryForFirst();
            if (dailyNutritionLogBean == null) {
                dailyNutritionLogBean = new DailyNutritionLogBean();
                dailyNutritionLogBean.setLocationId(locationId);
            }
            dailyNutritionLogBean.setSeviceDate(new Date().getTime());
            dailyNutritionLogBeanDao.createOrUpdate(dailyNutritionLogBean);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }
}
