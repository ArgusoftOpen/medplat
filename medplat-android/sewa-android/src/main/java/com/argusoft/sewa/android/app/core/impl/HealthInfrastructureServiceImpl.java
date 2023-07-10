package com.argusoft.sewa.android.app.core.impl;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.HealthInfrastructureService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EBean(scope = Singleton)
public class HealthInfrastructureServiceImpl implements HealthInfrastructureService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<HealthInfrastructureBean, Integer> infrastructureBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<UserHealthInfraBean, Integer> userHealthInfraBeanDao;

    public Map<Long, String> retrieveDistinctHealthInfraType() {
        Map<Long, String> map = new HashMap<>();
        try {
            List<HealthInfrastructureBean> distinctType = infrastructureBeanDao.queryBuilder().distinct()
                    .selectColumns(FieldNameConstants.TYPE_ID, FieldNameConstants.TYPE_NAME).query();
            for (HealthInfrastructureBean bean : distinctType) {
                map.put(bean.getTypeId(), bean.getTypeName());
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        return map;
    }

    @Override
    public Map<Long, String> retrieveDistinctHealthInfraTypeForChardham() {
        Map<Long, String> map = new HashMap<>();
        try {
            List<HealthInfrastructureBean> distinctType;
            if (SewaTransformer.loginBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_KIOSK)) {
                distinctType = infrastructureBeanDao.queryBuilder().where().eq(FieldNameConstants.TYPE_NAME, FieldNameConstants.TYPE_NAME_KIOSK).query();
            } else {
                distinctType = infrastructureBeanDao.queryBuilder().distinct()
                        .selectColumns(FieldNameConstants.TYPE_ID, FieldNameConstants.TYPE_NAME).query();
            }
            for (HealthInfrastructureBean bean : distinctType) {
                map.put(bean.getTypeId(), bean.getTypeName());
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        return map;
    }

    @Override
    public List<HealthInfrastructureBean> retrieveHealthInfraBySearch(String search, Long typeId, String attribute) {
        List<HealthInfrastructureBean> infrastructureBeans = new ArrayList<>();

        Where<HealthInfrastructureBean, Integer> where = infrastructureBeanDao.queryBuilder().where();
        try {
            if (attribute == null || attribute.trim().isEmpty()) {
                infrastructureBeans = where.and(
                        where.eq(FieldNameConstants.TYPE_ID, typeId),
                        where.or(
                                where.like(FieldNameConstants.NAME, "%" + search + "%"),
                                where.like(FieldNameConstants.ENGLISH_NAME, "%" + search + "%")
                        )
                ).query();
            } else {
                String[] split = attribute.split(",");
                StringBuilder stmt = new StringBuilder();
                stmt.append("(");
                int count = 0;
                for (String string : split) {
                    if (count == 0) {
                        stmt.append("`").append(string).append("`").append(" = 1");
                    } else {
                        stmt.append(" OR ").append("`").append(string).append("`").append(" =1");
                    }
                    count++;
                }
                stmt.append(")");

                Where<HealthInfrastructureBean, Integer> and = where.and(
                        where.eq(FieldNameConstants.TYPE_ID, typeId),
                        where.or(
                                where.like(FieldNameConstants.NAME, "%" + search + "%"),
                                where.like(FieldNameConstants.ENGLISH_NAME, "%" + search + "%")
                        ),
                        where.raw(stmt.toString())
                );

                infrastructureBeans = and.query();
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }

        return infrastructureBeans;
    }

    @Override
    public List<HealthInfrastructureBean> retrieveHealthInfraByLocationId(Long locationId, Long typeId, String attribute) {
        List<HealthInfrastructureBean> infrastructureBeans = new ArrayList<>();

        try {
            if (attribute == null || attribute.trim().isEmpty()) {
                infrastructureBeans = infrastructureBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.LOCATION_ID, locationId)
                        .and().eq(FieldNameConstants.TYPE_ID, typeId).query();

            } else {
                String[] split = attribute.split(",");
                StringBuilder stmt = new StringBuilder();
                stmt.append("(");
                int count = 0;
                for (String string : split) {
                    if (count == 0) {
                        stmt.append("`").append(string).append("`").append(" =1");
                    } else {
                        stmt.append(" OR ").append("`").append(string).append("`").append(" =1");
                    }
                    count++;
                }
                stmt.append(")");

                infrastructureBeans = infrastructureBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.LOCATION_ID, locationId)
                        .and().eq(FieldNameConstants.TYPE_ID, typeId)
                        .and().raw(stmt.toString()).query();
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }

        return infrastructureBeans;
    }

    @Override
    public List<HealthInfrastructureBean> retrieveHealthInfrastructures(Long typeId, String attribute) {
        List<HealthInfrastructureBean> infrastructureBeans = new ArrayList<>();

        Where<HealthInfrastructureBean, Integer> where = infrastructureBeanDao.queryBuilder().where();
        try {
            if (attribute == null || attribute.trim().isEmpty()) {
                infrastructureBeans = where.eq(FieldNameConstants.TYPE_ID, typeId).query();
            } else {
                String[] split = attribute.split(",");
                StringBuilder stmt = new StringBuilder();
                stmt.append("(");
                int count = 0;
                for (String string : split) {
                    if (count == 0) {
                        stmt.append("`").append(string).append("`").append(" = 1");
                    } else {
                        stmt.append(" OR ").append("`").append(string).append("`").append(" =1");
                    }
                    count++;
                }
                stmt.append(")");

                Where<HealthInfrastructureBean, Integer> and = where.and(
                        where.eq(FieldNameConstants.TYPE_ID, typeId),
                        where.raw(stmt.toString())
                );

                infrastructureBeans = and.query();
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }

        return infrastructureBeans;
    }

    @Override
    public HealthInfrastructureBean retrieveHealthInfrastructureAssignedToUser(Long userId) {
        UserHealthInfraBean userHealthInfraBean;
        HealthInfrastructureBean infrastructureBean = new HealthInfrastructureBean();
        Where<UserHealthInfraBean, Integer> whereUserHealthInfraBean = userHealthInfraBeanDao.queryBuilder().where();
        Where<HealthInfrastructureBean, Integer> whereHealthInfrastructureBean = infrastructureBeanDao.queryBuilder().where();
        try {
            userHealthInfraBean = whereUserHealthInfraBean.and(
                    whereUserHealthInfraBean.eq(FieldNameConstants.USER_ID, userId),
                    whereUserHealthInfraBean.eq(FieldNameConstants.IS_DEFAULT, true)
            ).queryForFirst();

            if (userHealthInfraBean != null) {
                infrastructureBean = whereHealthInfrastructureBean.eq(FieldNameConstants.ACTUAL_I_D, userHealthInfraBean.getHealthInfrastructureId()).queryForFirst();
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        return infrastructureBean;
    }

    @Override
    public UserHealthInfraBean retrieveUserHealthInfraBean() {
        UserHealthInfraBean userHealthInfraBean = null;
        try {
            userHealthInfraBean = userHealthInfraBeanDao.queryBuilder().where().eq(FieldNameConstants.IS_DEFAULT, true).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userHealthInfraBean;
    }
}
