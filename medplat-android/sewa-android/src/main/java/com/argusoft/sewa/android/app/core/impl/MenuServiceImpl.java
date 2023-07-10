package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.core.MenuService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.MenuBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class MenuServiceImpl implements MenuService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MenuBean, Integer> menuBeanDao;

    @Override
    public List<MenuBean> retrieveMenus() {
        try {
            return menuBeanDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
