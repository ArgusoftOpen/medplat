package com.argusoft.sewa.android.app.core.impl;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.VersionService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;


@EBean(scope = Singleton)
public class VersionServiceImpl implements VersionService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;

    @Override
    public void createOrUpdateVersionBean(String key, String value) {
        List<VersionBean> versionBeans;
        try {
            versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, key);
            VersionBean versionBean;
            if (versionBeans == null || versionBeans.isEmpty()) {
                VersionBean screenStatusVersionBean = new VersionBean();
                screenStatusVersionBean.setKey(key);
                screenStatusVersionBean.setValue(value);
                versionBeanDao.create(screenStatusVersionBean);
            } else {
                versionBean = versionBeans.get(0);
                versionBean.setValue(value);
                versionBeanDao.update(versionBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
