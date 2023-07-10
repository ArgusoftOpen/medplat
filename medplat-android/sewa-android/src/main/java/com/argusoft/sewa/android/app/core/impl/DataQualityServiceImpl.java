package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.util.Log;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class DataQualityServiceImpl {
    @OrmLiteDao(helper = DBConnection.class)
    Dao<DataQualityBean, Integer> dataQualityBeanDao;

    public List<DataQualityBean> getAllDataQualityBeans() {
        try {
            return dataQualityBeanDao.queryForAll();
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
            return new ArrayList<>();
        }
    }
}
