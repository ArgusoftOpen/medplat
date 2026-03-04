package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.core.MoveToProductionService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class MoveToProductionServiceImpl implements MoveToProductionService {

    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<FormAccessibilityBean, Integer> formAccessibilityBeanDao;

    @Override
    public FormAccessibilityBean retrieveFormAccessibilityBeanByFormType(String formCode) {
        FormAccessibilityBean formAccessibilityBean = null;
        try {
            formAccessibilityBean = formAccessibilityBeanDao.queryBuilder().where().eq("formCode", formCode).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return formAccessibilityBean;
    }

    @Override
    public List<FormAccessibilityBean> isAnyFormTrainingCompleted() {
        List<FormAccessibilityBean> formAccessibilityBeans = new ArrayList<>();
        try {
            formAccessibilityBeans = formAccessibilityBeanDao.queryForEq("state", GlobalTypes.MOVE_TO_PRODUCTION_RESPONSE_PENDING);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return formAccessibilityBeans;
    }

    @Override
    public void saveFormsToMoveToProduction(List<FormAccessibilityBean> formAccessibilityBeans) {
        for (FormAccessibilityBean formAccessibilityBean : formAccessibilityBeans) {
            try {
                FormAccessibilityBean formAccessibilityBean1 = formAccessibilityBeanDao.queryBuilder()
                        .where().eq("formCode", formAccessibilityBean.getFormCode()).queryForFirst();
                formAccessibilityBean1.setReadyToMoveProduction(Boolean.TRUE);
                formAccessibilityBean1.setState(GlobalTypes.MOVE_TO_PRODUCTION);
                formAccessibilityBeanDao.update(formAccessibilityBean1);
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }
    }

    @Override
    public void postUserReadyToMoveProduction() {
        try {
            List<FormAccessibilityBean> formAccessibilityBeans = formAccessibilityBeanDao.queryForEq("readyToMoveProduction", Boolean.TRUE);
            if (formAccessibilityBeans != null && !formAccessibilityBeans.isEmpty()) {
                for (FormAccessibilityBean formAccessibilityBean : formAccessibilityBeans) {
                    sewaServiceRestClient.postUserReadyToMoveProduction(formAccessibilityBean.getFormCode());
                }
            }
        } catch (SQLException | RestHttpException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void saveFormAccessibilityBeansFromServer() {
        try {
            List<FormAccessibilityBean> formAccessibilityBeans = sewaServiceRestClient.getUserFormAccessDetailFromServer();

            TableUtils.clearTable(formAccessibilityBeanDao.getConnectionSource(), FormAccessibilityBean.class);
            formAccessibilityBeanDao.create(formAccessibilityBeans);
        } catch (RestHttpException | SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

}
