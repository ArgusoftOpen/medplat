package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.core.SchoolService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.SchoolBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class SchoolServiceImpl implements SchoolService {

    private static final String TAG = "SchoolServiceImpl";

    @OrmLiteDao(helper = DBConnection.class)
    Dao<SchoolBean, Integer> schoolBeanDao;

    @Override
    public List<SchoolBean> retrieveSchoolsListBySearch(String search, Integer standard) {
        List<SchoolBean> list = new ArrayList<>();
        try {
            Where<SchoolBean, Integer> where = schoolBeanDao.queryBuilder().where();
            if (standard == null) {
                list = where.like("name", "%" + search + "%").query();
            } else {
                list = where.and(
                        where.like("name", "%" + search + "%"),
                        getWhereClauseAsPerSchoolType(standard, where)
                ).query();
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return list;
    }

    @Override
    public List<SchoolBean> retrieveSchoolsListByLocationId(Long locationId, Integer standard) {
        List<SchoolBean> list = new ArrayList<>();

        try {
            Where<SchoolBean, Integer> where = schoolBeanDao.queryBuilder().where();
            if (standard == null) {
                list = where.eq("locationId", locationId).query();
            } else {
                list = where.and(
                        where.eq("locationId", locationId),
                        getWhereClauseAsPerSchoolType(standard, where)
                ).query();
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return list;
    }

    private Where<SchoolBean, Integer> getWhereClauseAsPerSchoolType(Integer standard, Where<SchoolBean, Integer> where) throws SQLException {
        if (standard > 8) {
            return where
                    .or(
                            where.eq(GlobalTypes.SCHOOL_TYPE_GURUKUL, Boolean.TRUE),
                            where.eq(GlobalTypes.SCHOOL_TYPE_MADRESA, Boolean.TRUE),
                            where.eq(GlobalTypes.SCHOOL_TYPE_HIGHER_SECONDARY, Boolean.TRUE)
                    );
        } else {
            return where
                    .or(
                            where.eq(GlobalTypes.SCHOOL_TYPE_GURUKUL, Boolean.TRUE),
                            where.eq(GlobalTypes.SCHOOL_TYPE_MADRESA, Boolean.TRUE),
                            where.eq(GlobalTypes.SCHOOL_TYPE_PRIMARY, Boolean.TRUE)
                    );
        }
    }
}
