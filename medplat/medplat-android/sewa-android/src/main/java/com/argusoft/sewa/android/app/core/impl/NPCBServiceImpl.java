package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.NPCBService;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by prateek on 3/6/19.
 */
@EBean(scope = EBean.Scope.Singleton)
public class NPCBServiceImpl implements NPCBService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;

    @Override
    public List<MemberDataBean> retrieveMembersForNPCBScreening(Integer ashaArea, String search, long limit, long offset,
                                                                LinkedHashMap<String, String> qrData) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -5);
        Date date5YearsAgo = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -20);
        Date date20YearsAgo = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -40);
        Date date40YearsAgo = calendar.getTime();

        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .eq(FieldNameConstants.AREA_ID, ashaArea)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            List<String> familyIds = new ArrayList<>();
            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;

                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset)
                        .orderBy(FieldNameConstants.FAMILY_ID, false).where();
                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.isNull(FieldNameConstants.NPCB_SCREENING_DATE),
                            where.or(
                                    where.and(
                                            where.isNotNull(FieldNameConstants.DOB),
                                            where.le(FieldNameConstants.DOB, date5YearsAgo),
                                            where.ge(FieldNameConstants.DOB, date20YearsAgo)
                                    ),
                                    where.and(
                                            where.isNotNull(FieldNameConstants.DOB),
                                            where.le(FieldNameConstants.DOB, date40YearsAgo)
                                    )
                            ),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),         //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")      //Search By HealthIdNumber
                            )
                    ).query();
                } else if (search != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.isNull(FieldNameConstants.NPCB_SCREENING_DATE),
                            where.or(
                                    where.and(
                                            where.isNotNull(FieldNameConstants.DOB),
                                            where.le(FieldNameConstants.DOB, date5YearsAgo),
                                            where.ge(FieldNameConstants.DOB, date20YearsAgo)
                                    ),
                                    where.and(
                                            where.isNotNull(FieldNameConstants.DOB),
                                            where.le(FieldNameConstants.DOB, date40YearsAgo)
                                    )
                            ),
                            where.or(
                                    where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + search + "%"),   //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + search + "%"),        //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + search + "%"),         //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + search + "%"),      //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + search + "%")      //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.isNull(FieldNameConstants.NPCB_SCREENING_DATE),
                            where.or(
                                    where.and(
                                            where.isNotNull(FieldNameConstants.DOB),
                                            where.le(FieldNameConstants.DOB, date5YearsAgo),
                                            where.ge(FieldNameConstants.DOB, date20YearsAgo)
                                    ),
                                    where.and(
                                            where.isNotNull(FieldNameConstants.DOB),
                                            where.le(FieldNameConstants.DOB, date40YearsAgo)
                                    )
                            )
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    memberDataBeans.add(new MemberDataBean(memberBean));
                }
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return memberDataBeans;
    }

    @Override
    public void updateMemberNpcbScreeningDate(Long memberActualId, Date screeningDate) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            memberBean.setNpcbScreeningDate(screeningDate);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }
}
