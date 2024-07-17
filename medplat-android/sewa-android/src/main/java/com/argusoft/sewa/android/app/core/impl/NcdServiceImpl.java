package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.model.MemberAvailableEveningBean;
import com.argusoft.sewa.android.app.model.MemberMoConfirmedDetailBean;
import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.core.NcdService;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by prateek on 16/1/19.
 */

@EBean(scope = EBean.Scope.Singleton)
public class NcdServiceImpl implements NcdService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberCbacDetailBean, Integer> memberCbacDetailBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberMoConfirmedDetailBean, Integer> moConfirmedDetailBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<DrugInventoryBean, Integer> drugInventoryBeans;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberAvailableEveningBean, Integer> eveningAvailableBeanDao;

    @Override
    public List<FamilyDataBean> retrieveFamiliesForCbacEntry(String searchString, Integer areaId, long limit, long offset,
                                                             LinkedHashMap<String, String> qrData) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();
        try {
            List<FamilyBean> familyBeans;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -30);
            if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                searchString = qrData.get(FieldNameConstants.FAMILY_ID);   //Search By FamilyId
            }
            QueryBuilder<MemberBean, Integer> memberQb = memberBeanDao.queryBuilder();
            Where<MemberBean, Integer> where = memberQb.where();

            where.and(
                    where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES),
                    where.le(FieldNameConstants.DOB, calendar.getTime()),
                    where.or(
                            where.isNull(FieldNameConstants.CBAC_DATE),
                            where.le(FieldNameConstants.CBAC_DATE, UtilBean.getStartOfFinancialYear(null))
                    )
            );

            if (searchString != null) {
                where.and().or(
                        where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),
                        where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),
                        where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),
                        where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%")
                );
            }

            familyBeans = familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQb)
                    .limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID).distinct()
                    .where()
                    .eq(FieldNameConstants.AREA_ID, areaId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), null, ex);
            return new ArrayList<>();
        }
        return familyDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMembersListForCbacEntry(String familyId) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -30);

            List<MemberBean> memberBeans = new ArrayList<>(
                    memberBeanDao.queryBuilder().where()
                            .eq(FieldNameConstants.FAMILY_ID, familyId)
                            .and().le(FieldNameConstants.DOB, calendar.getTime())
                            .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query()
            );

            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
            return new ArrayList<>();
        }
        return memberDataBeans;
    }
    public List<FamilyDataBean> retrieveFamiliesForDnhddNcdScreening(String searchString, List<Integer> areaIds, long limit, long offset,
                                                                     LinkedHashMap<String, String> qrData) {
        List<FamilyDataBean> familyDataBeans = new LinkedList<>();
        try {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.YEAR, -30);

            if (Objects.equals(qrData.get(FieldNameConstants.IS_QR_SCAN), "true")) {
                searchString = qrData.get(FieldNameConstants.FAMILY_ID);   //Search By FamilyId
            }
            QueryBuilder<MemberBean, Integer> memberQB = memberBeanDao.queryBuilder();
            Where<MemberBean, Integer> memberWhere = memberQB.where();
            memberWhere.and(
                    memberWhere.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES),
                    memberWhere.le(FieldNameConstants.DOB, instance.getTime())
            );

            if (searchString != null && !searchString.isEmpty()) {
                memberWhere.and().or(
                        memberWhere.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.LAST_NAME, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%")
                );
            }

            Where<FamilyBean, Integer> where = familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQB)
                    .limit(limit).offset(offset).distinct()
                    .orderBy(FieldNameConstants.UPDATED_ON, true)
                    .where();

            if (searchString == null) {
                where.and(
                        where.and(
                                where.in(FieldNameConstants.AREA_ID, areaIds),
                                where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES)
                        ),
                        where.or(
                                where.isNull(FieldNameConstants.LAST_MEMBER_NCD_SCREENING_DATE),
                                where.le(FieldNameConstants.LAST_MEMBER_NCD_SCREENING_DATE, UtilBean.getStartOfFinancialYear(null).getTime())
                        )
                );
            } else {
                where.and(
                        where.in(FieldNameConstants.AREA_ID, areaIds),
                        where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES)
                );
            }

            List<FamilyBean> familyBeans = where.query();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, null));
            }

        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        return familyDataBeans;
    }
    public List<MemberDataBean> retrieveMembersListForDnhddNcdScreening(String familyId) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -30);


            Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().where();

            List<MemberBean> memberBeans = where.and(
                    where.eq(FieldNameConstants.FAMILY_ID, familyId),
                    where.le(FieldNameConstants.DOB, calendar.getTime()),
                    where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES)
            ).query();

            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }

        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
            return new ArrayList<>();
        }
        return memberDataBeans;
    }
    @Override
    public List<FamilyDataBean> retrieveFamiliesForNcdScreening(String searchString, List<Integer> areaIds, long limit, long offset,
                                                                LinkedHashMap<String, String> qrData) {
        List<FamilyDataBean> familyDataBeans = new LinkedList<>();
        try {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.YEAR, -15);
            if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                searchString = qrData.get(FieldNameConstants.FAMILY_ID);   //Search By FamilyId
            }
            QueryBuilder<MemberBean, Integer> memberQB = memberBeanDao.queryBuilder();
            Where<MemberBean, Integer> memberWhere = memberQB.where();
            memberWhere.and(
                    memberWhere.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES),
                    memberWhere.or(
                            memberWhere.le(FieldNameConstants.DOB, instance.getTime()),
                            memberWhere.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE)
                    )
            );

            if (searchString != null && !searchString.isEmpty()) {
                memberWhere.and().or(
                        memberWhere.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),
                        memberWhere.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%")
                );
            }

            Where<FamilyBean, Integer> where = familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQB)
                    .limit(limit).offset(offset).distinct()
                    .orderBy(FieldNameConstants.UPDATED_ON, true)
                    .where();

            if (searchString == null) {
                where.and(
                        where.and(
                                where.in(FieldNameConstants.AREA_ID, areaIds),
                                where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES)
                        ),
                        where.or(
                                where.isNull(FieldNameConstants.LAST_MEMBER_NCD_SCREENING_DATE),
                                where.le(FieldNameConstants.LAST_MEMBER_NCD_SCREENING_DATE, UtilBean.getStartOfFinancialYear(null).getTime())
                        )
                );
            } else {
                where.and(
                        where.in(FieldNameConstants.AREA_ID, areaIds),
                        where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES)
                );
            }

            List<FamilyBean> familyBeans = where.query();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, null));
            }

            for (FamilyDataBean familyDataBean : familyDataBeans) {
                MemberAvailableEveningBean evening = eveningAvailableBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.FAMILY_ID, familyDataBean.getFamilyId())
                        .and().eq(FieldNameConstants.EVENING_AVAILABILITY, Boolean.TRUE).queryForFirst();
                if (evening != null) {
                    familyDataBean.setEveningAvailability(evening.getEveningAvailability());
                }
            }

        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        return familyDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMembersListForNcdScreening(String familyId) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -15);

            Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().where();

            List<MemberBean> memberBeans = where.and(
                    where.eq(FieldNameConstants.FAMILY_ID, familyId),
                    where.or(
                            where.le(FieldNameConstants.DOB, calendar.getTime()),
                            where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE)
                    ),
                    where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES)
            ).query();

            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }

            for (MemberDataBean memberDataBean : memberDataBeans) {
                MemberAvailableEveningBean evening = eveningAvailableBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.MEMBER_ID, memberDataBean.getId())
                        .and().eq(FieldNameConstants.EVENING_AVAILABILITY, Boolean.TRUE).queryForFirst();
                if (evening != null) {
                    memberDataBean.setEveningAvailability(evening.getEveningAvailability());
                }
            }

        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
            return new ArrayList<>();
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMembersListForNcdConfirmation(String searchString, List<Integer> areaIds, long limit, long offset) {

        List<MemberDataBean> memberDataBeans = new ArrayList<>();
        List<MemberBean> memberBeans = new ArrayList<>();
        try {

            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .in(FieldNameConstants.AREA_ID, areaIds)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();

                if (searchString != null && !searchString.isEmpty()) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.SUSPECTED_FOR_DIABETES, Boolean.TRUE),
                            where.or(
                                    where.isNull(FieldNameConstants.CONFIRMED_DIABETES),
                                    where.eq(FieldNameConstants.CONFIRMED_DIABETES, Boolean.FALSE)
                            ),
                            where.or(
                                    where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),  //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),       //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),        //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),     //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")     //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.SUSPECTED_FOR_DIABETES, Boolean.TRUE),
                            where.or(
                                    where.isNull(FieldNameConstants.CONFIRMED_DIABETES),
                                    where.eq(FieldNameConstants.CONFIRMED_DIABETES, Boolean.FALSE)
                            )
                    ).query();
                }
            }
            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }

        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }

        return memberDataBeans;
    }

    @Override
    public List<MemberBean> retrieveMembersForNcdRegisterFhw(Integer selectedVillage, List<Integer> selectedAshaAreas, String selectedScreening) {
        List<MemberBean> memberBeans = new ArrayList<>();

        try {
            QueryBuilder<FamilyBean, Integer> familyQB = familyBeanDao.queryBuilder();
            if (selectedAshaAreas != null && !selectedAshaAreas.isEmpty()) {
                familyQB.where()
                        .in(FieldNameConstants.AREA_ID, selectedAshaAreas)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES);
            } else {
                familyQB.where()
                        .eq(FieldNameConstants.LOCATION_ID, selectedVillage)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES);
            }

            switch (selectedScreening) {
                case "hypertension":
                    memberBeans = memberBeanDao.queryBuilder().join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, familyQB).where()
                            .like(FieldNameConstants.HYP_YEAR, "%"+ UtilBean.getFinancialYearFromDate(null) +"%")
                            .and().isNull("sufferingHypertension").and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query();
                    break;
                case "diabetes":
                    memberBeans = memberBeanDao.queryBuilder().join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, familyQB).where()
                            .like(FieldNameConstants.DIABETES_YEAR, "%"+ UtilBean.getFinancialYearFromDate(null) +"%")
                            .and().isNull("sufferingDiabetes").and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query();
                    break;
                case "oral":
                    memberBeans = memberBeanDao.queryBuilder().join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, familyQB).where()
                            .like(FieldNameConstants.ORAL_YEAR, "%"+ UtilBean.getFinancialYearFromDate(null) +"%")
                            .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query();
                    break;
                case "breast":
                    memberBeans = memberBeanDao.queryBuilder().join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, familyQB).where()
                            .like(FieldNameConstants.BREAST_YEAR, "%"+ UtilBean.getFinancialYearFromDate(null) +"%")
                            .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query();
                    break;
                case "cervical":
                    memberBeans = memberBeanDao.queryBuilder().join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, familyQB).where()
                            .like(FieldNameConstants.CERVICAL_YEAR, "%"+ UtilBean.getFinancialYearFromDate(null) +"%")
                            .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query();
                    break;
                case "mentalHealth":
                    memberBeans = memberBeanDao.queryBuilder().join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, familyQB).where()
                            .like(FieldNameConstants.MENTAL_HEALTH_YEAR, "%"+ UtilBean.getFinancialYearFromDate(null) +"%")
                            .and().isNull("sufferingMentalHealth").and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query();
                    break;
                default:
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        return memberBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMembersForNcdRegisterAsha(List<Integer> areaIds, String search, long limit, long offset,
                                                                  LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();
        List<MemberBean> memberBeans;

        List<String> familyStates = new ArrayList<>();
        familyStates.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        familyStates.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        familyStates.add(FhsConstants.FHS_FAMILY_STATE_TEMPORARY);
        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (areaIds != null && !areaIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, areaIds)
                        .and().in(FieldNameConstants.STATE, familyStates).query();

                for (FamilyBean familyBean : familyBeans) {
                    familyIds.add(familyBean.getFamilyId());
                }
            }

            if (!familyIds.isEmpty()) {
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();
                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.ge(FieldNameConstants.CBAC_DATE, UtilBean.getStartOfFinancialYear(null)),
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")
                            )
                    ).query();
                } else if (search != null && !search.isEmpty()) {
                    memberBeans = where.and(
                            where.ge(FieldNameConstants.CBAC_DATE, UtilBean.getStartOfFinancialYear(null)),
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.or(
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + search + "%"),
                                    where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + search + "%"),
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + search + "%"),
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + search + "%")
                            )
                    ).query();
                } else {
                    memberBeans = where.in(FieldNameConstants.FAMILY_ID, familyIds)
                            .and().ge(FieldNameConstants.CBAC_DATE, UtilBean.getStartOfFinancialYear(null)).query();
                }

                if (!memberBeans.isEmpty()) {
                    for (MemberBean bean : memberBeans) {
                        memberDataBeans.add(new MemberDataBean(bean));
                    }
                }
            }
        } catch (SQLException ex) {
            Log.e(getClass().getName(), null, ex);
        }
        return memberDataBeans;
    }


    @Override
    public MemberCbacDetailBean retrieveMemberCbacDetailBean(Long memberId) {
        try {
            return memberCbacDetailBeanDao.queryBuilder().where().eq(FieldNameConstants.MEMBER_ID, memberId).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
            return null;
        }
    }

    @Override
    public void markMemberAsCbacDone(Long actualId, Integer score) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
            memberBean.setCbacDate(new Date());
            if (score != null) {
                memberBean.setCbacScore(score);
            }
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void markMemberAsPersonalHistoryDone(Long actualId) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
            memberBean.setPersonalHistoryDone(true);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            android.util.Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void markMemberAsDiabetesConfirmed(Long actualId) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
            memberBean.setConfirmedDiabetes(true);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            android.util.Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void markFamilyAsCbacDoneForAnyMember(Long actualId) {
        try {
            FamilyBean familyBean = familyBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
            familyBean.setAnyMemberCbacDone(Boolean.TRUE);
            familyBeanDao.update(familyBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void markMemberAsNCDScreeningDone(Long memberActualId, String formType, Date screeningDate) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            String financialYear = UtilBean.getFinancialYearFromDate(screeningDate);
            switch (formType) {
                case FormConstants.NCD_FHW_HYPERTENSION:
                    memberBean.setHypYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getHypYear(), financialYear));
                    break;
                case FormConstants.NCD_FHW_ORAL:
                    memberBean.setOralYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getOralYear(), financialYear));
                    break;
                case FormConstants.NCD_FHW_DIABETES:
                    memberBean.setDiabetesYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getDiabetesYear(), financialYear));
                    break;
                case FormConstants.NCD_FHW_BREAST:
                    memberBean.setBreastYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getBreastYear(), financialYear));
                    break;
                case FormConstants.NCD_FHW_CERVICAL:
                    memberBean.setCervicalYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getCervicalYear(), financialYear));
                    break;
                case FormConstants.NCD_FHW_MENTAL_HEALTH:
                    memberBean.setMentalHealthYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getMentalHealthYear(), financialYear));
                    break;
                case FormConstants.NCD_FHW_HEALTH_SCREENING:
                    memberBean.setHealthYear(UtilBean.addCommaSeparatedStringIfNotExists(memberBean.getHealthYear(), financialYear));
                    break;
                default:
            }
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @Override
    public List<MemberDataBean> retrieveMembersListForWeeklyClinic(String searchString, boolean referenceDue, List<Integer> areaIds, long limit, long offset) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();
        List<MemberBean> memberBeans = new ArrayList<>();
        List<MemberMoConfirmedDetailBean> memberMoConfirmedBeans = null;
        try {
            List<String> memberIds = new ArrayList<>();
            Where<MemberMoConfirmedDetailBean, Integer> confirmedWhere = moConfirmedDetailBeanDao.queryBuilder().selectColumns(FieldNameConstants.MEMBER_ID)
                    .where();

            if (referenceDue) {
                memberMoConfirmedBeans =
                        confirmedWhere.and(
                                confirmedWhere.in(FieldNameConstants.LOCATION_ID, areaIds),
                                confirmedWhere.and(
                                        confirmedWhere.isNotNull("referenceDue"),
                                        confirmedWhere.eq("referenceDue", Boolean.TRUE)
                                )
                        ).query();
            } else {
                memberMoConfirmedBeans =
                        confirmedWhere.and(
                                confirmedWhere.in(FieldNameConstants.LOCATION_ID, areaIds),
                                confirmedWhere.or(
                                        confirmedWhere.eq("confirmedForDiabetes", Boolean.TRUE),
                                        confirmedWhere.eq("confirmedForHypertension", Boolean.TRUE),
                                        confirmedWhere.eq("confirmedForMentalHealth", Boolean.TRUE)
                                )
                        ).query();
            }


            for (MemberMoConfirmedDetailBean bean : memberMoConfirmedBeans) {
                memberIds.add(String.valueOf(bean.getMemberId()));
            }

            if (!memberIds.isEmpty()) {
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();

                if (searchString != null && !searchString.isEmpty()) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.ACTUAL_ID, memberIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.or(
                                    where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),  //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),       //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),        //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),     //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")     //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.ACTUAL_ID, memberIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    ).query();
                }
            }
            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }

        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }

        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMembersListForDnhddCbacAndNutritionEntry(String familyId) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            Calendar calendar = Calendar.getInstance();

            List<MemberBean> memberBeans = new ArrayList<>(
                    memberBeanDao.queryBuilder().where()
                            .eq(FieldNameConstants.FAMILY_ID, familyId)
                            .and().le(FieldNameConstants.DOB, calendar.getTime())
                            .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES).query()
            );

            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
            return new ArrayList<>();
        }
        return memberDataBeans;
    }
    @Override
    public List<FamilyDataBean> retrieveFamiliesForDnhddCbacAndNutritionEntry(String searchString, Integer areaId, long limit, long offset,
                                                                              LinkedHashMap<String, String> qrData) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();
        try {
            List<FamilyBean> familyBeans;
            Calendar calendar = Calendar.getInstance();

            if (Objects.equals(qrData.get(FieldNameConstants.IS_QR_SCAN), "true")) {
                searchString = qrData.get(FieldNameConstants.FAMILY_ID);   //Search By FamilyId
            }

            QueryBuilder<MemberBean, Integer> memberQb = memberBeanDao.queryBuilder();
            Where<MemberBean, Integer> where = memberQb.where();

            where.and(
                    where.in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES),
                    where.le(FieldNameConstants.DOB, calendar.getTime())
            );

            if (searchString != null) {
                where.and().or(
                        where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),
                        where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),
                        where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),
                        where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%")
                );
            }

            familyBeans = familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQb)
                    .limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID).distinct()
                    .where()
                    .eq(FieldNameConstants.AREA_ID, areaId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), null, ex);
            return new ArrayList<>();
        }
        return familyDataBeans;
    }

    @Override
    public MemberMoConfirmedDataBean retrieveMoConfirmedBeanByMemberId(Long memberId) {
        try {
            MemberMoConfirmedDetailBean bean = moConfirmedDetailBeanDao.queryBuilder().where().eq(FieldNameConstants.MEMBER_ID, memberId).queryForFirst();
            return new MemberMoConfirmedDataBean(bean);
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
            return null;
        }
    }

    @Override
    public DrugInventoryBean retrieveDrugByMedicineId(Integer medicineId) {
        try {
            return drugInventoryBeans.queryBuilder().where().eq(FieldNameConstants.MEDICINE_ID, medicineId).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
            return null;
        }
    }

    @Override
    public void markMemberEveningAvailability(Long memberId, String familyId, boolean evening) {
        try {
            MemberAvailableEveningBean eveningBean = eveningAvailableBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.MEMBER_ID, memberId).queryForFirst();
            if (eveningBean != null) {
                eveningBean.setEveningAvailability(evening);
                eveningAvailableBeanDao.update(eveningBean);
            } else {
                MemberAvailableEveningBean availableEveningBean = new MemberAvailableEveningBean();
                availableEveningBean.setFamilyId(familyId);
                availableEveningBean.setMemberId(String.valueOf(memberId));
                availableEveningBean.setEveningAvailability(evening);
                eveningAvailableBeanDao.create(availableEveningBean);
            }

        } catch (SQLException e) {
            android.util.Log.e(getClass().getSimpleName(), null, e);
        }
    }
}
