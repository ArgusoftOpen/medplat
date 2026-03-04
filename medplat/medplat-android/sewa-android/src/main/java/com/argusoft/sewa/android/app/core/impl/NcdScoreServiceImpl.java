package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.core.NcdScoreService;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

@EBean(scope = EBean.Scope.Singleton)
public class NcdScoreServiceImpl implements NcdScoreService {

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<MemberBean, Integer> memberBeanDao;

    private static final Integer SMOKE_CONSUME_PRODUCT_QUE_ID = 12;
    private static final Integer CONSUME_ALCOHOL_QUE_ID = 13;
    private static final Integer WAIST_MALE_QUE_ID = 1502;
    private static final Integer WAIST_FEMALE_QUE_ID = 1501;
    private static final Integer PHYSICAL_ACTIVITY_QUE_ID = 16;
    private static final Integer BP_DIABETES_HISTORY_QUE_ID = 17;

    @Override
    public Integer calculateCbacScoreForMember() {
        int score = 0;
        String actualId = SharedStructureData.relatedPropertyHashTable.get("memberActualId");
        if (actualId == null) {
            return  null;
        }
        MemberBean memberBean;
        try {
            memberBean = memberBeanDao.queryBuilder().where()
                    .eq("actualId", Long.valueOf(actualId))
                    .queryForFirst();

            //CALCULATING SCORE
            Date dob = memberBean.getDob();
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.setTime(dob);
            int birthYear = calendar.get(Calendar.YEAR);
            int age = currentYear - birthYear;

            if (age >= 40 && age < 50) {
                score += 1;
            } else if (age >= 50) {
                score += 2;
            }

            QueFormBean tmpQueFormBean = SharedStructureData.mapIndexQuestion.get(SMOKE_CONSUME_PRODUCT_QUE_ID);
            if ( tmpQueFormBean!= null
                    && tmpQueFormBean.getAnswer() != null) {
                String smokeConsumeSmokelessProduct = tmpQueFormBean.getAnswer().toString();
                if (smokeConsumeSmokelessProduct.equals("IN_PAST")) {
                    score += 1;
                } else if (smokeConsumeSmokelessProduct.equals("DAILY")) {
                    score += 2;
                }
            }

            tmpQueFormBean = SharedStructureData.mapIndexQuestion.get(CONSUME_ALCOHOL_QUE_ID);
            if (tmpQueFormBean != null && tmpQueFormBean.getAnswer() != null) {
                String consumeAlcohol = tmpQueFormBean.getAnswer().toString();
                if (consumeAlcohol.equals("1")) {
                    score += 1;
                }
            }

            String waist;
            if (memberBean.getGender().equals("M")) {
                tmpQueFormBean = SharedStructureData.mapIndexQuestion.get(WAIST_MALE_QUE_ID);
                if (tmpQueFormBean != null && tmpQueFormBean.getAnswer() != null) {
                    waist = tmpQueFormBean.getAnswer().toString();
                    if (waist.equals("91TO100")) {
                        score += 1;
                    } else if (waist.equals("GT100")) {
                        score += 2;
                    }
                }
            } else {
                tmpQueFormBean = SharedStructureData.mapIndexQuestion.get(WAIST_FEMALE_QUE_ID);
                if (tmpQueFormBean != null && tmpQueFormBean.getAnswer() != null) {
                    waist = tmpQueFormBean.getAnswer().toString();
                    if (waist.equals("81TO90")) {
                        score += 1;
                    } else if (waist.equals("GT90")) {
                        score += 2;
                    }
                }
            }

            tmpQueFormBean = SharedStructureData.mapIndexQuestion.get(PHYSICAL_ACTIVITY_QUE_ID);
            if (tmpQueFormBean != null && tmpQueFormBean.getAnswer() != null) {
                String physicalActivity = tmpQueFormBean.getAnswer().toString();
                if (physicalActivity.equals("LESS_THAN_150")) {
                    score += 1;
                }
            }

            tmpQueFormBean = SharedStructureData.mapIndexQuestion.get(BP_DIABETES_HISTORY_QUE_ID);
            if (tmpQueFormBean != null && tmpQueFormBean.getAnswer() != null) {
                String bpDiabetesHeartHistory = tmpQueFormBean.getAnswer().toString();
                if (bpDiabetesHeartHistory.equals("1")) {
                    score += 2;
                }
            }

            return score;
        } catch (SQLException e) {
            Log.e(getClass().getName(), "SQL Exception", e);
        }
        return null;
    }
}
