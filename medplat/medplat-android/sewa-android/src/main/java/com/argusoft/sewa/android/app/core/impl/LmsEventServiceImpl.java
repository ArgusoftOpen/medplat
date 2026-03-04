package com.argusoft.sewa.android.app.core.impl;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

import com.argusoft.sewa.android.app.databean.RecordStatusBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LmsEventBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;

@EBean(scope = Singleton)
public class LmsEventServiceImpl {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsEventBean, Integer> eventBeanDao;

    @Bean
    SewaServiceRestClientImpl restClient;

    public void uploadLmsEventToServer() {
        try {
            LmsEventBean lmsEventBean = new LmsEventBean();
            lmsEventBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
            lmsEventBean.setUserId(SewaTransformer.loginBean.getUserID().intValue());
            List<LmsEventBean> lmsEventBeansToSubmit = eventBeanDao.queryForMatchingArgs(lmsEventBean);
            if (lmsEventBeansToSubmit == null || lmsEventBeansToSubmit.isEmpty()) {
                return;
            }
            List<RecordStatusBean> recordStatusBeans = restClient.lmsEventEntryFromMobileToDBeans(lmsEventBeansToSubmit);
            evaluateResponse(recordStatusBeans);
        } catch (SQLException | RestHttpException throwables) {
            throwables.printStackTrace();
        }
    }

    private void evaluateResponse(List<RecordStatusBean> recordStatusBeans) {
        if (recordStatusBeans == null || recordStatusBeans.isEmpty()) {
            return;
        }

        for (int i = 0; i < recordStatusBeans.size(); i++) {
            RecordStatusBean recordStatusBean = recordStatusBeans.get(i);
            if (recordStatusBean.getStatus() != null && recordStatusBean.getStatus().equals(GlobalTypes.STATUS_SUCCESS)) {
                LmsEventBean lmsEventBean = new LmsEventBean();
                lmsEventBean.setChecksum(recordStatusBean.getChecksum());
                try {
                    List<LmsEventBean> lmsEventBeans = eventBeanDao.queryForMatchingArgs(lmsEventBean);
                    eventBeanDao.delete(lmsEventBeans);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
