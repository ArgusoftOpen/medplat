/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.core.impl;

import android.content.Context;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.core.SewaFhsService;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.LocationDetailDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MobileNumberUpdationBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.databean.RchVillageProfileDataBean;
import com.argusoft.sewa.android.app.databean.WorkerDetailDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LibraryBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberPregnancyStatusBean;
import com.argusoft.sewa.android.app.model.MergedFamiliesBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.QuestionBean;
import com.argusoft.sewa.android.app.model.RchVillageProfileBean;
import com.argusoft.sewa.android.app.model.ReadNotificationsBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author kunjan
 */
@EBean(scope = EBean.Scope.Singleton)
public class SewaFhsServiceImpl implements SewaFhsService {

    private static final String TAG = "SewaFhsServiceImpl";

    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    @Bean
    SewaServiceImpl sewaService;

    @RootContext
    Context context;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationBean, Integer> locationBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FhwServiceDetailBean, Integer> fhwServiceDetailBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MergedFamiliesBean, Integer> mergedFamiliesBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LoggerBean, Integer> loggerBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<NotificationBean, Integer> notificationBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<QuestionBean, Integer> questionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnswerBean, Integer> answerBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LabelBean, Integer> labelBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LoginBean, Integer> loginBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<ListValueBean, Integer> listValueBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnnouncementBean, Integer> announcementBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<RchVillageProfileBean, Integer> rchVillageProfileBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationMasterBean, Integer> locationMasterBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LibraryBean, Integer> libraryBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MigratedMembersBean, Integer> migratedMembersBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<ReadNotificationsBean, Integer> readNotificationsBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberPregnancyStatusBean, Integer> memberPregnancyStatusBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<CovidTravellersInfoBean, Integer> covidTravellersInfoBeanDao;

    @Override
    public List<FamilyDataBean> retrieveFamilyDataBeansForCFHCByVillage(String locationId, boolean isReverification, long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> verifiedFamilyStates = new ArrayList<>();
        verifiedFamilyStates.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_ORPHAN);
        verifiedFamilyStates.removeAll(FhsConstants.CFHC_FAMILY_STATES);

        try {
            List<FamilyBean> familyBeans = new ArrayList<>();
            if (isReverification) {
                familyBeans.addAll(familyBeanDao.queryBuilder().limit(limit).offset(offset)
                        .selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE)
                        .where().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.CFHC_IN_REVERIFICATION_FAMILY_STATES)
                        .query());
            } else {
                QueryBuilder<MemberBean, Integer> memberQB = memberBeanDao.queryBuilder();
                memberQB.where().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
                familyBeans.addAll(familyBeanDao.queryBuilder()
                        .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQB)
                        .limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE).distinct()
                        .orderBy(FieldNameConstants.UPDATED_ON, true)
                        .where().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, verifiedFamilyStates).query());
            }

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }
    @Override
    public List<MemberDataBean> retrieveMemberDataBeansForDnhddNcdByFamily(String familyId) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -30);
        Date dateBefore30Years = instance.getTime();

        try {
            List<MemberBean> memberBeans = new ArrayList<>(memberBeanDao.queryBuilder()
                    .orderBy(FieldNameConstants.FAMILY_HEAD_FLAG, Boolean.FALSE)
                    .where().eq(FieldNameConstants.FAMILY_ID, familyId)
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().le(FieldNameConstants.DOB, dateBefore30Years).query());
            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        return memberDataBeans;
    }

    @Override
    public List<FamilyDataBean> searchFamilyDataBeansForCFHCByVillage(String search, String locationId, boolean isReverification,
                                                                      LinkedHashMap<String, String> qrData) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> verifiedFamilyStates = new ArrayList<>();
        verifiedFamilyStates.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_ORPHAN);
        if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
            search = qrData.get(FieldNameConstants.FAMILY_ID);   //Search By FamilyId
        }
        try {
            Set<FamilyBean> familyBeans = new HashSet<>();

            //Fetching families based on search string (familyId)
            if (isReverification) {
                familyBeans.addAll(new ArrayList<>(familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE)
                        .where().like(FieldNameConstants.FAMILY_ID, "%" + search + "%")
                        .and().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.CFHC_IN_REVERIFICATION_FAMILY_STATES)
                        .query()));
            } else {
                List<String> familyIds = new ArrayList<>();
                for (FamilyBean familyBean : familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, verifiedFamilyStates).query()) {
                    familyIds.add(familyBean.getFamilyId());
                }

                // Search By familyId
                List<FamilyBean> familyBeansBySearch = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().like(FieldNameConstants.FAMILY_ID, "%" + search + "%")
                        .and().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, verifiedFamilyStates).query();

                // Search By Member uniqueHealthId
                Set<MemberBean> memberBeansBySearch = new HashSet<>();
                memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + search + "%")
                        .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                        .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

                // Search By Member firstName
                memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().like(FieldNameConstants.FIRST_NAME, "%" + search + "%")
                        .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                        .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

                // Search By Member mobileNumber
                memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().like(FieldNameConstants.MOBILE_NUMBER, "%" + search + "%")
                        .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                        .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

                List<String> familyIdsBySearch = new ArrayList<>();
                for (FamilyBean familyBean : familyBeansBySearch) {
                    familyIdsBySearch.add(familyBean.getFamilyId());
                }

                for (MemberBean memberBean : memberBeansBySearch) {
                    if (!familyIdsBySearch.contains(memberBean.getFamilyId())) {
                        familyIdsBySearch.add(memberBean.getFamilyId());
                    }
                }

                familyBeans.addAll(familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE)
                        .where().in(FieldNameConstants.FAMILY_ID, familyIdsBySearch)
                        .and().eq(FieldNameConstants.AREA_ID, locationId).query());
            }

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean aFamily : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(aFamily, memberDataBeans));
            }

        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    public List<FamilyDataBean> retrieveFamilyDataBeansForMergeFamily(String locationId, String search, long limit, long offset) {
        List<String> verifiedFamilyStates = new ArrayList<>();
        verifiedFamilyStates.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_ORPHAN);
        verifiedFamilyStates.addAll(FhsConstants.CFHC_IN_REVERIFICATION_FAMILY_STATES);

        try {
            QueryBuilder<MemberBean, Integer> memberQB = memberBeanDao.queryBuilder();
            memberQB.where().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
            Where<FamilyBean, Integer> where = familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQB)
                    .limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE).distinct()
                    .orderBy(FieldNameConstants.UPDATED_ON, true)
                    .where().eq(FieldNameConstants.LOCATION_ID, locationId)
                    .and().in(FieldNameConstants.STATE, verifiedFamilyStates);

            if (search != null && !search.isEmpty()) {
                where = where.and().like(FieldNameConstants.FAMILY_ID, "%" + search + "%");
            }

            List<FamilyBean> query = where.query();
            List<FamilyDataBean> familyDataBeans = new ArrayList<>();
            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : query) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
            return familyDataBeans;
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<FamilyDataBean> retrieveFamilyDataBeansForFHSByArea(Long areaId, long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> verifiedFamilyStates = new ArrayList<>(FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.remove(FhsConstants.FHS_FAMILY_STATE_TEMPORARY);

        try {
            QueryBuilder<MemberBean, Integer> memberQB = memberBeanDao.queryBuilder();
            memberQB.where().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQB)
                    .limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE).distinct()
                    .orderBy(FieldNameConstants.UPDATED_ON, true)
                    .where().eq(FieldNameConstants.AREA_ID, areaId)
                    .and().in(FieldNameConstants.STATE, verifiedFamilyStates).query();

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    @Override
    public List<FamilyDataBean> searchFamilyDataBeansForFHSByArea(Long areaId, String search) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> validFamilyStates = new ArrayList<>(FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES);
        validFamilyStates.remove(FhsConstants.FHS_FAMILY_STATE_TEMPORARY);

        try {
            //Fetching families based on search string (familyId)
            List<String> familyIds = new ArrayList<>();
            for (FamilyBean familyBean : familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().eq(FieldNameConstants.AREA_ID, areaId)
                    .and().in(FieldNameConstants.STATE, validFamilyStates).query()) {
                familyIds.add(familyBean.getFamilyId());
            }

            // Search By familyId
            List<FamilyBean> familyBeansBySearch = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.FAMILY_ID, "%" + search + "%")
                    .and().eq(FieldNameConstants.AREA_ID, areaId)
                    .and().in(FieldNameConstants.STATE, validFamilyStates).query();

            // Search By Member uniqueHealthId
            Set<MemberBean> memberBeansBySearch = new HashSet<>();
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            // Search By Member firstName
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.FIRST_NAME, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            // Search By Member mobileNumber
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.MOBILE_NUMBER, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            List<String> familyIdsBySearch = new ArrayList<>();
            for (FamilyBean familyBean : familyBeansBySearch) {
                familyIdsBySearch.add(familyBean.getFamilyId());
            }

            for (MemberBean memberBean : memberBeansBySearch) {
                if (!familyIdsBySearch.contains(memberBean.getFamilyId())) {
                    familyIdsBySearch.add(memberBean.getFamilyId());
                }
            }

            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE)
                    .where().in(FieldNameConstants.FAMILY_ID, familyIdsBySearch)
                    .and().eq(FieldNameConstants.AREA_ID, areaId).query();

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean aFamily : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(aFamily, memberDataBeans));
            }

        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMemberDataBeansByFamily(String familyId) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();
        try {
            List<MemberBean> memberBeans = new ArrayList<>(memberBeanDao.queryBuilder()
                    .orderBy(FieldNameConstants.FAMILY_HEAD_FLAG, Boolean.FALSE)
                    .where().eq(FieldNameConstants.FAMILY_ID, familyId).query());
            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        return memberDataBeans;
    }

    @Override
    public FamilyDataBean retrieveFamilyDataBeanByFamilyId(String familyId) {
        try {
            FamilyBean familyBean = familyBeanDao.queryBuilder().where().eq(FieldNameConstants.FAMILY_ID, familyId).queryForFirst();
            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            return new FamilyDataBean(familyBean, memberDataBeans);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public void markFamilyAsVerified(String familyId) {
        try {
            List<FamilyBean> family = familyBeanDao.queryForEq(FieldNameConstants.FAMILY_ID, familyId);
            FamilyBean familyBean = family.get(0);
            familyBean.setIsVerifiedFlag(Boolean.TRUE);
            familyBean.setReverificationFlag(Boolean.FALSE);
            familyBean.setState(FhsConstants.FHS_FAMILY_STATE_VERIFIED);
            familyBeanDao.update(familyBean);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }

    }

    @Override
    public void markFamilyAsCFHCVerified(String familyId) {
        try {
            List<FamilyBean> family = familyBeanDao.queryForEq(FieldNameConstants.FAMILY_ID, familyId);
            FamilyBean familyBean = family.get(0);
            familyBean.setIsVerifiedFlag(Boolean.TRUE);
            familyBean.setReverificationFlag(Boolean.FALSE);
            familyBean.setState(FhsConstants.CFHC_FAMILY_STATE_VERIFIED_LOCAL);
            familyBeanDao.update(familyBean);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
    }

    @Override
    public void markFamilyAsMigrated(String familyId) {
        try {
            List<FamilyBean> family = familyBeanDao.queryForEq(FieldNameConstants.FAMILY_ID, familyId);
            FamilyBean familyBean = family.get(0);
            familyBean.setIsVerifiedFlag(Boolean.TRUE);
            familyBean.setReverificationFlag(Boolean.FALSE);
            familyBean.setState(FhsConstants.FHS_FAMILY_STATE_MIGRATED);
            familyBeanDao.update(familyBean);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }

    }

    @Override
    public void markFamilyAsArchived(String familyId) {
        try {
            List<FamilyBean> family = familyBeanDao.queryForEq(FieldNameConstants.FAMILY_ID, familyId);
            FamilyBean familyBean = family.get(0);
            familyBean.setIsVerifiedFlag(Boolean.TRUE);
            familyBean.setReverificationFlag(Boolean.FALSE);
            familyBean.setState(FhsConstants.FHS_FAMILY_STATE_ARCHIVED);
            familyBeanDao.update(familyBean);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }

    }

    @Override
    public List<LocationBean> getDistinctLocationsAssignedToUser() {
        if (SewaTransformer.loginBean.getVillageCode() == null || SewaTransformer.loginBean.getVillageCode().isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> locationIdsAssignedToUser = new ArrayList<>();
        for (String id : SewaTransformer.loginBean.getVillageCode().split(":")) {
            locationIdsAssignedToUser.add(Integer.valueOf(id));
        }
        List<LocationBean> locations = new ArrayList<>();
        try {
            locations = locationBeanDao.queryBuilder().where().in(FieldNameConstants.ACTUAL_I_D, locationIdsAssignedToUser).query(); //all villages
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        return locations;
    }

    @Override
    public Map<String, MemberDataBean> retrieveHeadMemberDataBeansByFamilyId(List<String> familyIds) {
        Map<String, MemberDataBean> headMembersMap = new HashMap<>();
        Map<String, List<MemberBean>> mapOfMembersWithFamilyWithoutHeadsIdAsKey = new HashMap<>();
        List<MemberBean> memberBeans;
        List<MemberBean> membersOfFamiliesWithoutHeads;
        Set<String> familyIdsWithoutHead = new HashSet<>();
        try {
            memberBeans = new ArrayList<>(memberBeanDao.queryBuilder()
                    .selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.FIRST_NAME,
                            FieldNameConstants.MIDDLE_NAME, FieldNameConstants.LAST_NAME,
                            FieldNameConstants.GRAND_FATHER_NAME, FieldNameConstants.MARITAL_STATUS, FieldNameConstants.GENDER)
                    .where().in(FieldNameConstants.FAMILY_ID, familyIds)
                    .and().eq(FieldNameConstants.FAMILY_HEAD_FLAG, Boolean.TRUE).query());
            for (MemberBean bean : memberBeans) {
                headMembersMap.put(bean.getFamilyId(), new MemberDataBean(bean));
            }

            for (String familyId : familyIds) {
                MemberDataBean get = headMembersMap.get(familyId);
                if (get == null) {
                    familyIdsWithoutHead.add(familyId);
                }
            }
            membersOfFamiliesWithoutHeads = memberBeanDao.queryBuilder()
                    .selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.FIRST_NAME,
                            FieldNameConstants.MIDDLE_NAME, FieldNameConstants.LAST_NAME,
                            FieldNameConstants.GRAND_FATHER_NAME, FieldNameConstants.MARITAL_STATUS, FieldNameConstants.GENDER)
                    .where().in(FieldNameConstants.FAMILY_ID, familyIdsWithoutHead).query();
            for (MemberBean memberBean : membersOfFamiliesWithoutHeads) {
                List<MemberBean> members = mapOfMembersWithFamilyWithoutHeadsIdAsKey.get(memberBean.getFamilyId());
                if (members == null) {
                    members = new ArrayList<>();
                    mapOfMembersWithFamilyWithoutHeadsIdAsKey.put(memberBean.getFamilyId(), members);
                }
                members.add(memberBean);
            }

            for (String familyId : familyIds) {
                MemberDataBean head = null;

                if (mapOfMembersWithFamilyWithoutHeadsIdAsKey.get(familyId) == null) {
                    continue;
                }
                //search for any male married member
                List<MemberBean> tmpMemberBeans = mapOfMembersWithFamilyWithoutHeadsIdAsKey.get(familyId);
                if (tmpMemberBeans != null) {
                    for (MemberBean memberBean : tmpMemberBeans) {
                        if (memberBean.getMaritalStatus() != null && memberBean.getMaritalStatus().equals("629")
                                && memberBean.getGender() != null && memberBean.getGender().equals("M")) {
                            head = new MemberDataBean(memberBean);
                        }
                    }

                    //search for any male member
                    if (head == null) {
                        for (MemberBean memberBean : tmpMemberBeans) {
                            if (memberBean.getGender() != null && memberBean.getGender().equals("M")) {
                                head = new MemberDataBean(memberBean);
                            }
                        }
                    }

                    //search for any female member
                    if (head == null) {
                        for (MemberBean memberBean : tmpMemberBeans) {
                            if (memberBean.getGender() != null && memberBean.getGender().equals("F")) {
                                head = new MemberDataBean(memberBean);
                            }
                        }
                    }
                }

                if (head != null) {
                    headMembersMap.put(familyId, head);
                }
            }

        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }

        return headMembersMap;
    }

    @Override
    public List<FhwServiceDetailBean> retrieveFhwServiceDetailBeansByVillageId(Integer locationId) {
        if (locationId != null) {
            try {
                return fhwServiceDetailBeanDao.queryBuilder().where().eq(FieldNameConstants.LOCATION_ID, locationId).query();
            } catch (SQLException e) {
                Log.e(TAG, null, e);
            }
        } else {
            try {
                return fhwServiceDetailBeanDao.queryForAll();
            } catch (SQLException e) {
                Log.e(TAG, null, e);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<MemberDataBean> retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(String familyId) {
        List<MemberBean> memberBeans = new ArrayList<>();
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            memberBeans = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.FAMILY_ID, familyId)
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES).query();
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        for (MemberBean bean : memberBeans) {
            memberDataBeans.add(new MemberDataBean(bean));
        }
        return memberDataBeans;
    }

    @Override
    public void mergeFamilies(FamilyDataBean familyToBeExpanded, FamilyDataBean familyToBeMerged) {
        try {
            MergedFamiliesBean mergedFamiliesBean = new MergedFamiliesBean(familyToBeMerged.getFamilyId(), familyToBeExpanded.getFamilyId(), false);
            mergedFamiliesBeanDao.create(mergedFamiliesBean);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public Map<Long, MemberBean> retrieveMemberBeansMapByActualIds(List<Long> actualIds) {
        Map<Long, MemberBean> mapOfMemberBeans = new HashMap<>();
        try {
            List<String> stringActualIds = new ArrayList<>();
            for (Long actualId : actualIds) {
                stringActualIds.add(actualId.toString());
            }

            List<MemberBean> memberBeans = memberBeanDao.queryBuilder().where().in(FieldNameConstants.ACTUAL_ID, stringActualIds).query();

            for (MemberBean memberBean : memberBeans) {
                mapOfMemberBeans.put(Long.valueOf(memberBean.getActualId()), memberBean);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return mapOfMemberBeans;
    }

    @Override
    public String assignFamilyToUser(String locationId, FamilyDataBean familyDataBean) {
        try {
            FamilyDataBean familyDataBean1 = sewaServiceRestClient.assignFamilyToUser(locationId, familyDataBean.getFamilyId());
            List<MemberBean> memberBeansToCreate = new ArrayList<>();

            if (familyDataBean1 != null) {
                DeleteBuilder<FamilyBean, Integer> familyBeanDeleteBuilder = familyBeanDao.deleteBuilder();
                familyBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyDataBean1.getFamilyId());
                familyBeanDeleteBuilder.delete();

                DeleteBuilder<MemberBean, Integer> memberBeanDeleteBuilder = memberBeanDao.deleteBuilder();
                memberBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyDataBean1.getFamilyId());
                memberBeanDeleteBuilder.delete();

                List<MemberDataBean> memberDataBeans = familyDataBean1.getMembers();
                if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
                    for (MemberDataBean memberDataBean : memberDataBeans) {
                        memberBeansToCreate.add(new MemberBean(memberDataBean));
                    }
                }
                memberBeanDao.create(memberBeansToCreate);
                familyBeanDao.create(new FamilyBean(familyDataBean1));
                return UtilBean.getMyLabel(LabelConstants.FAMILY_ASSIGNED_SUCCESSFULLY);
            }
            return UtilBean.getMyLabel(LabelConstants.NETWORK_ERROR_WHILE_ASSIGNING_FAMILY);
        } catch (RestHttpException e) {
            Log.e(TAG, null, e);
            return UtilBean.getMyLabel(LabelConstants.THERE_WAS_ERROR_ASSIGNING_FAMILY);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
            return UtilBean.getMyLabel(LabelConstants.ERROR_WHILE_STORING_ASSIGNED_FAMILY);
        }
    }

    @Override
    public List<FieldValueMobDataBean> retrieveAnganwadiListFromSubcentreId(Integer subcentreId) {
        List<FieldValueMobDataBean> anganwadisUnderSubcenter = new ArrayList<>();
        List<Integer> listOfVillageIds = new ArrayList<>();
        try {
            List<LocationBean> villageList = locationBeanDao.queryBuilder().where().eq(FieldNameConstants.PARENT, subcentreId).query();
            for (LocationBean village : villageList) {
                listOfVillageIds.add(village.getActualID());
            }
            List<LocationBean> anganwadiList = locationBeanDao.queryBuilder().where().in(FieldNameConstants.PARENT, listOfVillageIds)
                    .and().eq("level", 8).query();
            for (LocationBean locationBean2 : anganwadiList) {
                FieldValueMobDataBean fieldValueMobDataBean = new FieldValueMobDataBean();
                fieldValueMobDataBean.setIdOfValue(locationBean2.getActualID());
                fieldValueMobDataBean.setValue(locationBean2.getName());
                anganwadisUnderSubcenter.add(fieldValueMobDataBean);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return anganwadisUnderSubcenter;
    }

    @Override
    public Integer retrieveSubcenterIdFromAnganwadiId(Integer anganwadiId) {
        try {
            if (anganwadiId != null) {
                LocationBean anganwadi = locationBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, anganwadiId).queryForFirst();
                if (anganwadi != null) {
                    LocationBean village = locationBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, anganwadi.getParent()).queryForFirst();
                    if (village != null) {
                        return village.getParent();
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public RchVillageProfileDataBean getRchVillageProfileDataBean(String locationId) {
        String aadharNumber = "123456789879";
        String mobileNumber = "9876543210";
        RchVillageProfileDataBean rchVillageProfileDataBean = new RchVillageProfileDataBean();
        LocationDetailDataBean village = new LocationDetailDataBean();
        village.setId(Integer.valueOf(locationId));
        village.setName("ઉનાવા");
        village.setLocType("V");

        WorkerDetailDataBean asha1 = new WorkerDetailDataBean();
        asha1.setName("Anjanaben Patel");
        asha1.setAadharNumber(aadharNumber);
        asha1.setMobileNumber(mobileNumber);
        asha1.setUserId(12345L);

        WorkerDetailDataBean asha2 = new WorkerDetailDataBean();
        asha2.setName("Gitaben Raval");
        asha2.setAadharNumber(aadharNumber);
        asha2.setMobileNumber(mobileNumber);
        asha2.setUserId(12346L);

        WorkerDetailDataBean anm = new WorkerDetailDataBean();
        anm.setName("Lata Solanki");
        anm.setAadharNumber(aadharNumber);
        anm.setMobileNumber(mobileNumber);
        anm.setUserId(12347L);

        WorkerDetailDataBean fhw = new WorkerDetailDataBean();
        fhw.setName("Pritiben Shah");
        fhw.setAadharNumber(aadharNumber);
        fhw.setMobileNumber(mobileNumber);
        fhw.setUserId(12348L);

        WorkerDetailDataBean mphw = new WorkerDetailDataBean();
        mphw.setName("Alpaben Patel");
        mphw.setAadharNumber(aadharNumber);
        mphw.setMobileNumber(mobileNumber);
        mphw.setUserId(12349L);

        rchVillageProfileDataBean.setState(village);
        rchVillageProfileDataBean.setDistrict(village);
        rchVillageProfileDataBean.setBlock(village);
        rchVillageProfileDataBean.setChc(village);
        rchVillageProfileDataBean.setPhc(village);
        rchVillageProfileDataBean.setSubCentre(village);
        rchVillageProfileDataBean.setVillage(village);

        rchVillageProfileDataBean.setTotalPopulation(15000);
        rchVillageProfileDataBean.setTotalEligibleCouple(1000);
        rchVillageProfileDataBean.setPregnantWomenCount(300);
        rchVillageProfileDataBean.setInfantChildCount(500);

        List<WorkerDetailDataBean> ashaList = new ArrayList<>();
        ashaList.add(asha1);
        ashaList.add(asha2);

        rchVillageProfileDataBean.setAshaDetail(ashaList);
        rchVillageProfileDataBean.setAnmDetail(anm);
        rchVillageProfileDataBean.setFhwDetail(fhw);
        rchVillageProfileDataBean.setMphwDetail(mphw);

        rchVillageProfileDataBean.setNearbyHospitalDetail("Civil Hospital, GH Road, Sector-12, Gandhinagar 7923221931");
        rchVillageProfileDataBean.setFruDetail("FRU Details 7878564234");
        rchVillageProfileDataBean.setAmbulanceNumber("8965465465");
        rchVillageProfileDataBean.setTransportationNumber("9594852316");
        rchVillageProfileDataBean.setNationalCallCentreNumber("9876854612");
        rchVillageProfileDataBean.setHelplineNumber("7965481236");

        return rchVillageProfileDataBean;

    }

    @Override
    public List<LocationBean> retrieveAshaAreaAssignedToUser(Integer villageId) {
        List<LocationBean> ashaAreaList = new ArrayList<>();
        try {
            ashaAreaList = locationBeanDao.queryBuilder().where().eq(FieldNameConstants.LEVEL, 7)
                    .and().eq(FieldNameConstants.PARENT, villageId).query();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return ashaAreaList;
    }

    @Override
    public List<MemberDataBean> retrieveEligibleCouplesByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset,
                                                                  LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .eq(FieldNameConstants.LOCATION_ID, villageId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -15);
                Date dateBefore18Years = calendar.getTime();
                calendar.add(Calendar.YEAR, -34);
                Date dateBefore45Years = calendar.getTime();
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();
                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.GENDER, "F"),
                            where.eq(FieldNameConstants.MARITAL_STATUS, "629"),
                            where.and(
                                    where.isNotNull(FieldNameConstants.DOB),
                                    where.le(FieldNameConstants.DOB, dateBefore18Years),
                                    where.ge(FieldNameConstants.DOB, dateBefore45Years)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.IS_PREGNANT_FLAG),
                                    where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.FALSE)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.MENOPAUSE_ARRIVED),
                                    where.eq(FieldNameConstants.MENOPAUSE_ARRIVED, Boolean.FALSE)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.HYSTERECTOMY_DONE),
                                    where.eq(FieldNameConstants.HYSTERECTOMY_DONE, Boolean.FALSE)
                            ),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),      //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")      //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.GENDER, "F"),
                            where.eq(FieldNameConstants.MARITAL_STATUS, "629"),
                            where.and(
                                    where.isNotNull(FieldNameConstants.DOB),
                                    where.le(FieldNameConstants.DOB, dateBefore18Years),
                                    where.ge(FieldNameConstants.DOB, dateBefore45Years)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.IS_PREGNANT_FLAG),
                                    where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.FALSE)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.MENOPAUSE_ARRIVED),
                                    where.eq(FieldNameConstants.MENOPAUSE_ARRIVED, Boolean.FALSE)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.HYSTERECTOMY_DONE),
                                    where.eq(FieldNameConstants.HYSTERECTOMY_DONE, Boolean.FALSE)
                            ),
                            where.or(
                                    where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),   //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),        //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),         //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),      //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")      //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.GENDER, "F"),
                            where.eq(FieldNameConstants.MARITAL_STATUS, "629"),
                            where.and(
                                    where.isNotNull(FieldNameConstants.DOB),
                                    where.le(FieldNameConstants.DOB, dateBefore18Years),
                                    where.ge(FieldNameConstants.DOB, dateBefore45Years)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.IS_PREGNANT_FLAG),
                                    where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.FALSE)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.MENOPAUSE_ARRIVED),
                                    where.eq(FieldNameConstants.MENOPAUSE_ARRIVED, Boolean.FALSE)
                            ),
                            where.or(
                                    where.isNull(FieldNameConstants.HYSTERECTOMY_DONE),
                                    where.eq(FieldNameConstants.HYSTERECTOMY_DONE, Boolean.FALSE)
                            )
                    ).query();
                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -3);

                Calendar cal1 = Calendar.getInstance();
                cal1.add(Calendar.YEAR, -3);

                List<MemberDataBean> membersWithChildCountMoreThan2 = new ArrayList<>();
                List<MemberDataBean> membersWithLastDeliveryDateLessThan3Years = new ArrayList<>();
                List<MemberDataBean> eligibleCouples = new ArrayList<>();

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getGender() != null && memberBean.getGender().equals(GlobalTypes.FEMALE)
                            && memberBean.getMaritalStatus() != null && memberBean.getMaritalStatus().equals("629")
                            && memberBean.getDob() != null && memberBean.getDob().after(dateBefore45Years) && memberBean.getDob().before(dateBefore18Years)
                            && (memberBean.getIsPregnantFlag() == null || !memberBean.getIsPregnantFlag())
                            && (memberBean.getMenopauseArrived() == null || !memberBean.getMenopauseArrived())
                            && (memberBean.getHysterectomyDone() == null || !memberBean.getHysterectomyDone())
                            && (memberBean.getLastMethodOfContraception() == null
                            || (!memberBean.getLastMethodOfContraception().equals(RchConstants.FEMALE_STERILIZATION)
                            && !memberBean.getLastMethodOfContraception().equals(RchConstants.MALE_STERILIZATION))
                            || (memberBean.getFpInsertOperateDate() != null && memberBean.getFpInsertOperateDate().after(cal.getTime())))) {
                        long childCount = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId()).countOf();
                        if (childCount > 2) {
                            membersWithChildCountMoreThan2.add(new MemberDataBean(memberBean));
                        } else if (memberBean.getLastDeliveryDate() != null && memberBean.getLastDeliveryDate().after(cal1.getTime())) {
                            membersWithLastDeliveryDateLessThan3Years.add(new MemberDataBean(memberBean));
                        } else {
                            eligibleCouples.add(new MemberDataBean(memberBean));
                        }
                    }
                }

                memberDataBeans.addAll(membersWithChildCountMoreThan2);
                memberDataBeans.addAll(membersWithLastDeliveryDateLessThan3Years);
                memberDataBeans.addAll(eligibleCouples);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMothersWithLastDeliveryLessThan6MonthsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset,
                                                                                         LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.EDD, true).where();

                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.MONTH, -6);

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.le(FieldNameConstants.LAST_DELIVERY_DATE, instance.getTime()),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),     //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.le(FieldNameConstants.LAST_DELIVERY_DATE, instance.getTime()),
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
                            where.le(FieldNameConstants.LAST_DELIVERY_DATE, instance.getTime())
                    ).query();
                }


                List<MemberDataBean> membersWithEddNotNull = new ArrayList<>();
                List<MemberDataBean> membersWithEddNull = new ArrayList<>();

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getGender() != null && memberBean.getGender().equals(GlobalTypes.FEMALE)
                            && Boolean.TRUE.equals(memberBean.getIsPregnantFlag())) {
                        if (memberBean.getEdd() != null) {
                            membersWithEddNotNull.add(new MemberDataBean(memberBean));
                        } else {
                            membersWithEddNull.add(new MemberDataBean(memberBean));
                        }
                    }
                }

                memberDataBeans.addAll(membersWithEddNotNull);
                memberDataBeans.addAll(membersWithEddNull);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrievePregnantWomenByAshaArea(List<Integer> locationIds, boolean isHighRisk, List<Integer> villageIds,
                                                                CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.LOCATION_ID, villageIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.EDD, true).where();
                if (isHighRisk) {
                    if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                                where.eq(FieldNameConstants.IS_HIGH_RISK_CASE, Boolean.TRUE),
                                where.or(
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),  //Search By UniqueHealthId
                                        where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthId
                                )
                        ).query();
                    } else if (searchString != null) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                                where.eq(FieldNameConstants.IS_HIGH_RISK_CASE, Boolean.TRUE),
                                where.or(
                                        where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),  //Search By UniqueHealthId
                                        where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),       //Search By FirstName
                                        where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),        //Search By FamilyId
                                        where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),    //Search By MobileNumber
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")     //Search By HealthId
                                )
                        ).query();
                    } else {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.eq(FieldNameConstants.IS_HIGH_RISK_CASE, Boolean.TRUE),
                                where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE)
                        ).query();
                    }
                } else {
                    if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                                where.or(
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),  //Search By UniqueHealthId
                                        where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthId
                                )
                        ).query();
                    } else if (searchString != null) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
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
                                where.eq(FieldNameConstants.GENDER, GlobalTypes.FEMALE),
                                where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE)
                        ).query();
                    }
                }

                List<MemberDataBean> membersWithEddNotNull = new ArrayList<>();
                List<MemberDataBean> membersWithEddNull = new ArrayList<>();

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getGender() != null && memberBean.getGender().equals(GlobalTypes.FEMALE)
                            && Boolean.TRUE.equals(memberBean.getIsPregnantFlag())) {
                        if (memberBean.getEdd() != null) {
                            membersWithEddNotNull.add(new MemberDataBean(memberBean));
                        } else {
                            membersWithEddNull.add(new MemberDataBean(memberBean));
                        }
                    }
                }

                memberDataBeans.addAll(membersWithEddNotNull);
                memberDataBeans.addAll(membersWithEddNull);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveWPDWomenByAshaArea(List<Integer> locationIds, List<Integer> villageIds, CharSequence searchString, long limit, long offset,
                                                           LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.LOCATION_ID, villageIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.EDD, true).where();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 15);

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                            where.le(FieldNameConstants.EDD, cal.getTime()),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),     //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                            where.le(FieldNameConstants.EDD, cal.getTime()),
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
                            where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                            where.le(FieldNameConstants.EDD, cal.getTime())
                    ).query();
                }

                List<MemberDataBean> membersWithEddNotNull = new ArrayList<>();
                List<MemberDataBean> membersWithEddNull = new ArrayList<>();

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getGender() != null && memberBean.getGender().equals(GlobalTypes.FEMALE)
                            && Boolean.TRUE.equals(memberBean.getIsPregnantFlag())) {
                        if (memberBean.getEdd() != null) {
                            membersWithEddNotNull.add(new MemberDataBean(memberBean));
                        } else {
                            membersWithEddNull.add(new MemberDataBean(memberBean));
                        }
                    }
                }

                memberDataBeans.addAll(membersWithEddNotNull);
                memberDataBeans.addAll(membersWithEddNull);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrievePncMothersByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset,
                                                             LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .eq(FieldNameConstants.LOCATION_ID, villageId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.DATE, -60);

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.ge(FieldNameConstants.LAST_DELIVERY_DATE, instance.getTime()),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),     //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.ge(FieldNameConstants.LAST_DELIVERY_DATE, instance.getTime()),
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
                            where.ge(FieldNameConstants.LAST_DELIVERY_DATE, instance.getTime())
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getLastDeliveryDate() != null && memberBean.getLastDeliveryDate().after(instance.getTime())) {
                        memberDataBeans.add(new MemberDataBean(memberBean));
                    }
                }

            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveChildsBelow5YearsByAshaArea(List<Integer> locationIds, Boolean isHighRisk, List<Integer> villageIds,
                                                                    CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.LOCATION_ID, villageIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }
            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -5);
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.DOB, false).where();
                if (isHighRisk != null && isHighRisk) {
                    if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.ge(FieldNameConstants.DOB, calendar.getTime()),
                                where.eq(FieldNameConstants.IS_HIGH_RISK_CASE, Boolean.TRUE),
                                where.or(
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),       //Search By MobileNumber
                                        where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")       //Search By HealthIdNumber
                                )
                        ).query();
                    } else if (searchString != null) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.ge(FieldNameConstants.DOB, calendar.getTime()),
                                where.eq(FieldNameConstants.IS_HIGH_RISK_CASE, Boolean.TRUE),
                                where.or(where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),   //Search By UniqueHealthId
                                        where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),         //Search By FirstName
                                        where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),          //Search By FamilyId
                                        where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),       //Search By MobileNumber
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")       //Search By HealthId
                                )
                        ).query();
                    } else {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.ge(FieldNameConstants.DOB, calendar.getTime()),
                                where.eq(FieldNameConstants.IS_HIGH_RISK_CASE, Boolean.TRUE)
                        ).query();
                    }
                } else {
                    if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.ge(FieldNameConstants.DOB, calendar.getTime()),
                                where.or(
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),       //Search By HealthId
                                        where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")       //Search By HealthIdNumber
                                )
                        ).query();
                    } else if (searchString != null) {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.ge(FieldNameConstants.DOB, calendar.getTime()),
                                where.or(where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),   //Search By UniqueHealthId
                                        where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),         //Search By FirstName
                                        where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),          //Search By FamilyId
                                        where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),       //Search By MobileNumber
                                        where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")       //Search By HealthId
                                )
                        ).query();
                    } else {
                        memberBeans = where.and(
                                where.in(FieldNameConstants.FAMILY_ID, familyIds),
                                where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                                where.ge(FieldNameConstants.DOB, calendar.getTime())
                        ).query();
                    }
                }

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getDob() != null && memberBean.getDob().after(calendar.getTime())) {
                        memberDataBeans.add(new MemberDataBean(memberBean));
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveChildsBelow6YearsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset,
                                                                    LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }
            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -6);
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.DOB, false).where();

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.ge(FieldNameConstants.DOB, calendar.getTime()),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),       //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")       //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.ge(FieldNameConstants.DOB, calendar.getTime()),
                            where.or(where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),   //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),         //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),          //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),       //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")       //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.ge(FieldNameConstants.DOB, calendar.getTime())
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getDob() != null && memberBean.getDob().after(calendar.getTime())) {
                        memberDataBeans.add(new MemberDataBean(memberBean));
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }

        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveChildsBetween6MonthsTo3YearsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset,
                                                                               LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }
            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -6);
                Date month6instace = calendar.getTime();
                calendar.add(Calendar.MONTH, 6);
                calendar.add(Calendar.YEAR, -3);
                Date year3instace = calendar.getTime();
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.DOB, false).where();

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.between(FieldNameConstants.DOB, year3instace, month6instace),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),       //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")       //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.between(FieldNameConstants.DOB, year3instace, month6instace),
                            where.or(where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),   //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),         //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),          //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),       //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")       //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.between(FieldNameConstants.DOB, year3instace, month6instace)
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    memberDataBeans.add(new MemberDataBean(memberBean));
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveAdolescentGirlsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset,
                                                                  LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;
            familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }
            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Calendar dateForAge14Years = Calendar.getInstance();
                dateForAge14Years.add(Calendar.YEAR, -14);
                Calendar dateForAge10Years = Calendar.getInstance();
                dateForAge10Years.add(Calendar.YEAR, -10);
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.DOB, false).where();

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.GENDER, "F"),
                            where.between(FieldNameConstants.DOB, dateForAge14Years.getTime(), dateForAge10Years.getTime()),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),          //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")       //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.GENDER, "F"),
                            where.between(FieldNameConstants.DOB, dateForAge14Years.getTime(), dateForAge10Years.getTime()),
                            where.or(where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),   //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),         //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),          //Search By FamilyId
                                    where.like(FieldNameConstants.MIDDLE_NAME, "%" + searchString + "%"),       //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")       //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.GENDER, "F"),
                            where.between(FieldNameConstants.DOB, dateForAge14Years.getTime(), dateForAge10Years.getTime())
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getDob() != null && memberBean.getDob().after(dateForAge10Years.getTime()) && memberBean.getDob().before(dateForAge14Years.getTime())) {
                        memberDataBeans.add(new MemberDataBean(memberBean));
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMembersByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset,
                                                          LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .eq(FieldNameConstants.LOCATION_ID, villageId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();
                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),      //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")        //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.or(where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),    //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),          //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),           //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%"),      //Search By MobileNumber
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + searchString + "%")        //Search By HealthId
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    ).query();
                }
                for (MemberBean memberBean : memberBeans) {
                    memberDataBeans.add(new MemberDataBean(memberBean));
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<FamilyDataBean> retrieveFamilyDataBeansByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        try {
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().limit(limit).offset(offset).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES)
                        .and().like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%").query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().limit(limit).offset(offset).where()
                        .eq(FieldNameConstants.LOCATION_ID, villageId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES)
                        .and().like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%").query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(familyBean, null));
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return familyDataBeans;
    }

    @Override
    public MemberBean retrieveMemberBeanByActualId(Long id) {
        try {
            return memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, id).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public MemberBean retrieveMemberBeanByHealthId(String healthId) {
        try {
            return memberBeanDao.queryBuilder().where().eq(FieldNameConstants.UNIQUE_HEALTH_ID, healthId).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public FamilyBean retrieveFamilyBeanByActualId(Long id) {
        try {
            return familyBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, id).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public void markMemberAsMigrated(Long memberActualId) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            memberBean.setState(FhsConstants.FHS_MEMBER_STATE_MIGRATED);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void markMemberAsDead(Long memberActualId) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            memberBean.setState(FhsConstants.FHS_MEMBER_STATE_DEAD);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void updateMemberPregnantFlag(Long memberActualId, boolean pregnantFlag) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            memberBean.setIsPregnantFlag(pregnantFlag);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void updateMemberLmpDate(Long memberActualId, Date lmpDate) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            memberBean.setLmpDate(lmpDate);
            memberBeanDao.update(memberBean);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void updateFamily(FamilyBean familyBean) {
        try {
            familyBeanDao.update(familyBean);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void deleteNotificationByMemberIdAndNotificationType(Long memberActualId, List<String> notificationTypes) {
        try {
            DeleteBuilder<NotificationBean, Integer> notificationBeanDeleteBuilder = notificationBeanDao.deleteBuilder();
            if (notificationTypes != null && !notificationTypes.isEmpty()) {
                notificationBeanDeleteBuilder.where().eq(FieldNameConstants.MEMBER_ID, memberActualId)
                        .and().in(FieldNameConstants.NOTIFICATION_CODE, notificationTypes);
            } else {
                notificationBeanDeleteBuilder.where().eq(FieldNameConstants.MEMBER_ID, memberActualId);
            }
            notificationBeanDeleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void updateVaccinationGivenForChild(Long memberActualId, String vaccinationGiven) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
            MemberBean child = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberActualId).queryForFirst();
            StringBuilder stringBuilder = new StringBuilder();
            String immunisationGiven = child.getImmunisationGiven();
            String[] split = vaccinationGiven.split("-");
            if (immunisationGiven != null && immunisationGiven.length() > 0) {
                stringBuilder.append(immunisationGiven);
            }
            for (String string : split) {
                if (string != null && string.contains("/")) {
                    String[] split1 = string.split("/");
                    if (split1[1].equals("T")) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(split1[0].trim());
                        stringBuilder.append("#");
                        stringBuilder.append(sdf.format(new Date(Long.parseLong(split1[2]))));
                    }
                }
            }
            child.setImmunisationGiven(stringBuilder.toString());
            memberBeanDao.update(child);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void updateVaccinationGivenForChild(String uniqueHealthId, String vaccinationGiven) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
            MemberBean child = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.UNIQUE_HEALTH_ID, uniqueHealthId).queryForFirst();
            StringBuilder stringBuilder = new StringBuilder();
            String immunisationGiven = child.getImmunisationGiven();
            String[] split = vaccinationGiven.split("-");
            if (immunisationGiven != null && immunisationGiven.length() > 0) {
                stringBuilder.append(immunisationGiven);
            }
            for (String string : split) {
                if (string != null && string.contains("/")) {
                    String[] split1 = string.split("/");
                    if (split1[1].equals("T")) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(split1[0].trim());
                        stringBuilder.append("#");
                        stringBuilder.append(sdf.format(new Date(Long.parseLong(split1[2]))));
                    }
                }
            }
            child.setImmunisationGiven(stringBuilder.toString());
            memberBeanDao.update(child);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public void markNotificationAsRead(NotificationMobDataBean notificationMobDataBean) {
        try {
            ReadNotificationsBean readNotificationsBean = new ReadNotificationsBean();
            readNotificationsBean.setNotificationId(notificationMobDataBean.getId());
            readNotificationsBeanDao.create(readNotificationsBean);
            sewaService.deleteNotificationByNotificationId(notificationMobDataBean.getId());
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public String getChildrenCount(Long motherId, boolean total, boolean male, boolean female) {
        List<String> invalidStates = new ArrayList<>();
        invalidStates.addAll(FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES);
        invalidStates.addAll(FhsConstants.FHS_DEAD_CRITERIA_MEMBER_STATES);

        try {
            if (total) {
                return String.valueOf(memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, motherId)
                        .and().ne(FieldNameConstants.ACTUAL_ID, motherId)
                        .and().notIn(FieldNameConstants.STATE, invalidStates).countOf());
            } else if (male) {
                return String.valueOf(memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, motherId)
                        .and().ne(FieldNameConstants.ACTUAL_ID, motherId)
                        .and().eq(FieldNameConstants.GENDER, "M")
                        .and().notIn(FieldNameConstants.STATE, invalidStates).countOf());
            } else if (female) {
                return String.valueOf(memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, motherId)
                        .and().ne(FieldNameConstants.ACTUAL_ID, motherId)
                        .and().eq(FieldNameConstants.GENDER, "F")
                        .and().notIn(FieldNameConstants.STATE, invalidStates).countOf());
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public MemberDataBean getLatestChildByMotherId(Long motherId) {
        List<String> invalidStates = new ArrayList<>();
        invalidStates.addAll(FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES);
        invalidStates.addAll(FhsConstants.FHS_DEAD_CRITERIA_MEMBER_STATES);

        MemberDataBean memberDataBean = null;
        try {
            MemberBean latestChild = memberBeanDao.queryBuilder().orderBy(FieldNameConstants.DOB, false)
                    .where().eq(FieldNameConstants.MOTHER_ID, motherId)
                    .and().ne(FieldNameConstants.ACTUAL_ID, motherId)
                    .and().notIn(FieldNameConstants.STATE, invalidStates).queryForFirst();

            if (latestChild != null) {
                memberDataBean = new MemberDataBean(latestChild);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBean;
    }

    @Override
    public String getValueOfListValuesById(String id) {
        if (id != null) {
            try {
                ListValueBean bean = listValueBeanDao.queryBuilder()
                        .where().eq(FieldNameConstants.ID_OF_VALUE, id).queryForFirst();
                if (bean != null) {
                    return bean.getValue();
                }
            } catch (SQLException e) {
                Log.e(TAG, null, e);
            }
        }
        return null;
    }

    @Override
    public Map<String, String> retrieveAshaInfoByAreaId(String areaId) {
        if (areaId != null) {
            try {
                LocationMasterBean locationMasterBean = locationMasterBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, areaId).queryForFirst();
                if (locationMasterBean != null) {
                    String fhwDetailString = locationMasterBean.getFhwDetailString();
                    if (fhwDetailString != null) {
                        Type type = new TypeToken<List<Map<String, String>>>() {
                        }.getType();
                        List<Map<String, String>> fhwDetailMapList = new Gson().fromJson(fhwDetailString, type);
                        return fhwDetailMapList.get(0);
                    }
                }
            } catch (SQLException e) {
                Log.e(TAG, null, e);
            }
        }
        return null;
    }

    @Override
    public List<MemberDataBean> retrieveMemberListForRchRegister(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset,
                                                                 LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans;

            if (locationIds != null && !locationIds.isEmpty()) {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .in(FieldNameConstants.AREA_ID, locationIds)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            } else {
                familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                        .eq(FieldNameConstants.LOCATION_ID, villageId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            }

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).where();
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.YEAR, -5);
                Date before5Years = instance.getTime();
                instance = Calendar.getInstance();
                instance.add(Calendar.YEAR, -15);
                Date before15Years = instance.getTime();
                instance = Calendar.getInstance();
                instance.add(Calendar.YEAR, -49);
                Date before49Years = instance.getTime();

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.or(
                                    where.ge(FieldNameConstants.DOB, before5Years),
                                    where.and(
                                            where.le(FieldNameConstants.DOB, before15Years),
                                            where.ge(FieldNameConstants.DOB, before49Years),
                                            where.eq(FieldNameConstants.GENDER, "F"),
                                            where.eq(FieldNameConstants.MARITAL_STATUS, "629")
                                    )
                            ),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),     //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.or(
                                    where.ge(FieldNameConstants.DOB, before5Years),
                                    where.and(
                                            where.le(FieldNameConstants.DOB, before15Years),
                                            where.ge(FieldNameConstants.DOB, before49Years),
                                            where.eq(FieldNameConstants.GENDER, "F"),
                                            where.eq(FieldNameConstants.MARITAL_STATUS, "629")
                                    )
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
                            where.or(
                                    where.ge(FieldNameConstants.DOB, before5Years),
                                    where.and(
                                            where.le(FieldNameConstants.DOB, before15Years),
                                            where.ge(FieldNameConstants.DOB, before49Years),
                                            where.eq(FieldNameConstants.GENDER, "F"),
                                            where.eq(FieldNameConstants.MARITAL_STATUS, "629")
                                    )
                            )
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    memberDataBeans.add(new MemberDataBean(memberBean));
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    public List<MemberDataBean> retrieveMembersForPhoneVerificationByFhsr(Long locationId, String searchString, long limit, long offset) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .in(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            List<String> familyIds = new ArrayList<>();
            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().limit(limit).offset(offset).orderBy(FieldNameConstants.EDD, true).where();

                if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                            where.or(
                                    where.isNull(FieldNameConstants.FHSR_PHONE_VERIFIED),
                                    where.eq(FieldNameConstants.FHSR_PHONE_VERIFIED, Boolean.FALSE)
                            ),
                            where.or(
                                    where.like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + searchString + "%"),  //Search By UniqueHealthId
                                    where.like(FieldNameConstants.FIRST_NAME, "%" + searchString + "%"),       //Search By FirstName
                                    where.like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%"),        //Search By FamilyId
                                    where.like(FieldNameConstants.MOBILE_NUMBER, "%" + searchString + "%")     //Search By MobileNumber
                            )
                    ).query();
                } else {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.eq(FieldNameConstants.IS_PREGNANT_FLAG, Boolean.TRUE),
                            where.or(
                                    where.isNull(FieldNameConstants.FHSR_PHONE_VERIFIED),
                                    where.eq(FieldNameConstants.FHSR_PHONE_VERIFIED, Boolean.FALSE)
                            )
                    ).query();
                }

                List<MemberDataBean> membersWithEddNotNull = new ArrayList<>();
                List<MemberDataBean> membersWithEddNull = new ArrayList<>();

                for (MemberBean memberBean : memberBeans) {
                    if (memberBean.getGender() != null && memberBean.getGender().equals(GlobalTypes.FEMALE)
                            && Boolean.TRUE.equals(memberBean.getIsPregnantFlag())) {
                        if (memberBean.getEdd() != null) {
                            membersWithEddNotNull.add(new MemberDataBean(memberBean));
                        } else {
                            membersWithEddNull.add(new MemberDataBean(memberBean));
                        }
                    }
                }

                memberDataBeans.addAll(membersWithEddNotNull);
                memberDataBeans.addAll(membersWithEddNull);
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public void storeFhsrPhoneVerificationForm(Long memberId, String phoneNumber, String answerString) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, memberId).queryForFirst();
            memberBean.setMobileNumber(phoneNumber);
            memberBean.setFhsrPhoneVerified(true);
            memberBeanDao.update(memberBean);

            SharedStructureData.gps.getLocation();

            //Preparing Checksum
            StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
            checkSum.append(Calendar.getInstance().getTimeInMillis());

            StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
            storeAnswerBean.setAnswerEntity(FormConstants.FHSR_PHONE_UPDATE);
            storeAnswerBean.setAnswer(answerString);
            storeAnswerBean.setChecksum(checkSum.toString());
            storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
            storeAnswerBean.setFormFilledUpTime(0L);
            storeAnswerBean.setMorbidityAnswer("-1");
            storeAnswerBean.setNotificationId(-1L);
            storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
            storeAnswerBean.setRelatedInstance("-1");
            if (SewaTransformer.loginBean != null) {
                storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
                storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
            }
            sewaService.createStoreAnswerBean(storeAnswerBean);

            LoggerBean loggerBean = new LoggerBean();
            loggerBean.setBeneficiaryName(memberBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberBean));
            loggerBean.setCheckSum(checkSum.toString());
            loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
            loggerBean.setFormType(FormConstants.FHSR_PHONE_UPDATE);
            loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.FHSR_PHONE_UPDATE));
            loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
            loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
            loggerBean.setNoOfAttempt(0);
            sewaService.createLoggerBean(loggerBean);

        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
    }

    @Override
    public long getMobileNumberCount(String mobileNumber) {
        long count = 0;
        try {
            Set<String> hashSet = new HashSet<>();
            List<MemberBean> members = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOBILE_NUMBER, mobileNumber).query();
            for (MemberBean member : members) {
                hashSet.add(member.getFamilyId());
            }
            count = hashSet.size();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return count;
    }

    public MemberPregnancyStatusBean retrievePregnancyStatusBeanByMemberId(Long memberId) {
        try {
            return memberPregnancyStatusBeanDao.queryBuilder().where().eq(FieldNameConstants.MEMBER_ID, memberId).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public List<MemberDataBean> retrieveMemberListForNPCB(List<Integer> areaIds, CharSequence searchString,
                                                          LinkedHashMap<String, String> qrData) {
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .in(FieldNameConstants.AREA_ID, areaIds)
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            if (!familyIds.isEmpty()) {
                List<MemberBean> memberBeans;
                Where<MemberBean, Integer> where = memberBeanDao.queryBuilder().where();

                if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.isNotNull(FieldNameConstants.NPCB_STATUS),
                            where.or(
                                    where.like(FieldNameConstants.HEALTH_ID, "%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%"),     //Search By HealthId
                                    where.like(FieldNameConstants.HEALTH_ID_NUMBER, "%" + qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%")     //Search By HealthIdNumber
                            )
                    ).query();
                } else if (searchString != null) {
                    memberBeans = where.and(
                            where.in(FieldNameConstants.FAMILY_ID, familyIds),
                            where.notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES),
                            where.isNotNull(FieldNameConstants.NPCB_STATUS),
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
                            where.isNotNull(FieldNameConstants.NPCB_STATUS)
                    ).query();
                }

                for (MemberBean memberBean : memberBeans) {
                    memberDataBeans.add(new MemberDataBean(memberBean));
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return memberDataBeans;
    }

    @Override
    public List<FamilyDataBean> retrieveAssignedFamilyDataBeansToUserForMobileNumberUpdationAndVerification(CharSequence searchString, String locationId,
                                                                                                            long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new LinkedList<>();

        try {
            Set<FamilyBean> familyBeans;
            if (searchString == null) {
                familyBeans = new HashSet<>(familyBeanDao.queryBuilder().limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.CFHC_FAMILY_STATES).query());
            } else {
                familyBeans = new HashSet<>(familyBeanDao.queryBuilder().limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID)
                        .where().eq(FieldNameConstants.AREA_ID, locationId)
                        .and().in(FieldNameConstants.STATE, FhsConstants.CFHC_FAMILY_STATES)
                        .and().like(FieldNameConstants.FAMILY_ID, "%" + searchString + "%").query());
            }

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean familyBean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(familyBean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveMemberDataBeanForMobileNumberUpdation(String familyId) {
        List<MemberBean> memberBeans = new ArrayList<>();
        List<MemberDataBean> memberDataBeans = new ArrayList<>();

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -17);
            memberBeans = memberBeanDao.queryBuilder().orderBy(FieldNameConstants.IS_MOBILE_NUMBER_VERIFIED, false).
                    orderBy(FieldNameConstants.FAMILY_HEAD_FLAG, false)
                    .where().eq(FieldNameConstants.FAMILY_ID, familyId)
                    .and().le(FieldNameConstants.DOB, calendar.getTime())
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES).query();

        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        for (MemberBean bean : memberBeans) {
            memberDataBeans.add(new MemberDataBean(bean));
        }
        return memberDataBeans;
    }

    @Override
    public void mobileNumberVerificationAndUpdationOfFamilyMember(MobileNumberUpdationBean mobileNumberUpdationBean, Long memberId) {
        try {
            MemberBean memberBean = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, mobileNumberUpdationBean.getMemberId()).queryForFirst();
            memberBean.setMobileNumber(mobileNumberUpdationBean.getMobileNumber());
            memberBean.setMobileNumberVerified(Boolean.TRUE);
            memberBeanDao.update(memberBean);

            //Preparing Checksum
            StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
            checkSum.append(Calendar.getInstance().getTimeInMillis());

            StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
            storeAnswerBean.setAnswerEntity(FormConstants.MOBILE_NUMBER_VERIFICATION);
            storeAnswerBean.setAnswer(new Gson().toJson(mobileNumberUpdationBean));
            storeAnswerBean.setChecksum(checkSum.toString());
            storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
            storeAnswerBean.setFormFilledUpTime(0L);
            storeAnswerBean.setMorbidityAnswer("-1");
            storeAnswerBean.setNotificationId(-1L);
            storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
            storeAnswerBean.setRelatedInstance("-1");
            storeAnswerBean.setMemberId(memberId);
            if (SewaTransformer.loginBean != null) {
                storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
                storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
            }
            sewaService.createStoreAnswerBean(storeAnswerBean);

            LoggerBean loggerBean = new LoggerBean();
            loggerBean.setBeneficiaryName(memberBean.getUniqueHealthId() + " - " + memberBean.getFirstName() + " " + memberBean.getMiddleName() + " " + memberBean.getLastName());
            loggerBean.setCheckSum(checkSum.toString());
            loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
            loggerBean.setFormType(FormConstants.MOBILE_NUMBER_VERIFICATION);
            loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.MOBILE_NUMBER_VERIFICATION));
            loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
            loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
            loggerBean.setNoOfAttempt(0);
            sewaService.createLoggerBean(loggerBean);

        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
    }

    @Override
    public List<String[]> retrieveGeriatricMembers(List<String> areaIds, CharSequence searchString,
                                                   LinkedHashMap<String, String> qrData) {
        String areaId = UtilBean.stringListJoin(areaIds, ",", false);
        String query = "select memberbean.actualId," +
                "memberbean.familyId, memberbean.firstName, memberbean.middleName, " +
                "memberbean.lastName, memberbean.uniqueHealthId from familybean " +
                "inner join memberbean on familybean.familyId = memberbean.familyId " +
                "where familybean.areaId in (" + areaId + ") " +
                " and memberbean.dob < date('now','-60 year') and memberbean.state not in ( " +
                FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES.toString().replaceAll("(^.|.$)", "'").replace(", ", "','") + " )";

        if (Objects.equals(qrData.get("isQrCodeScan"), "true")) {
            query += " and (memberbean.healthId like '%" + qrData.get(FieldNameConstants.HEALTH_ID) + "%' or memberbean.healthIdNumber like '%" +
                    qrData.get(FieldNameConstants.HEALTH_ID_NUMBER) + "%');";
        } else if (searchString != null) {
            query += " and (memberbean.firstName like '%" + searchString + "%' or  memberbean.middleName like '%" + searchString +
                    "%' or memberbean.uniqueHealthId like '%" + searchString + "%' or memberbean.healthId like '%" + searchString + "%') ;";
        }

        try {
            return memberBeanDao.queryRaw(query).getResults();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<MemberBean> retrieveFamilyMembersContactListByMember(String familyId, String memberId) {
        try {
            return memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FIRST_NAME, FieldNameConstants.MIDDLE_NAME,
                    FieldNameConstants.LAST_NAME, FieldNameConstants.MOBILE_NUMBER)
                    .where().eq(FieldNameConstants.FAMILY_ID, familyId)
                    .and().isNotNull(FieldNameConstants.MOBILE_NUMBER)
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().ne(FieldNameConstants.ACTUAL_ID, memberId).query();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CovidTravellersInfoBean> retrieveTravellersList(List<Integer> areaIds, String search, long limit, long offset) {
        try {
            if (search != null) {
                Where<CovidTravellersInfoBean, Integer> where = covidTravellersInfoBeanDao.queryBuilder().limit(limit).offset(offset).where();

                return where.and(
                        where.in(FieldNameConstants.LOCATION_ID, areaIds),
                        where.eq(FieldNameConstants.IS_ACTIVE, true),
                        where.or(
                                where.like(FieldNameConstants.NAME, "%" + search + "%"),
                                where.like(FieldNameConstants.MOBILE_NUMBER, "%" + search + "%"),
                                where.like(FieldNameConstants.MEMBER_ID, "%" + search + "%")
                        )
                ).query();

            } else {
                return covidTravellersInfoBeanDao.queryBuilder().limit(limit).offset(offset).where()
                        .in(FieldNameConstants.LOCATION_ID, areaIds)
                        .and().eq(FieldNameConstants.IS_ACTIVE, true).query();
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return new ArrayList<>();
    }

    public CovidTravellersInfoBean retrieveTravellersInfoBeanById(Integer actualId) {
        try {
            return covidTravellersInfoBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    @Override
    public List<FamilyDataBean> retrieveFamilyDataBeansForIDSPByVillage(String locationId, List<Integer> ashaAreas, long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> activeFamilyStates = new ArrayList<>();
        activeFamilyStates.addAll(FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES);
        activeFamilyStates.addAll(FhsConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES);
        activeFamilyStates.add(FhsConstants.IDSP_FAMILY_STATE_IDSP_TEMP);

        List<String> inactiveMembers = new ArrayList<>(FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
        inactiveMembers.remove(FhsConstants.IDSP_MEMBER_STATE_IDSP_TEMP);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();

        try {
            List<FamilyBean> familyBeans = new ArrayList<>();

            Where<FamilyBean, Integer> where = familyBeanDao.queryBuilder().limit(limit).offset(offset)
                    .selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE, FieldNameConstants.LAST_IDSP_SCREENING_DATE, FieldNameConstants.LAST_IDSP_2_SCREENING_DATE)
                    .orderBy(FieldNameConstants.UPDATED_ON, true)
                    .orderBy(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, true)
                    .where();

            if (ashaAreas != null) {
                familyBeans.addAll(where.and(
                        where.in(FieldNameConstants.STATE, activeFamilyStates),
                        where.in(FieldNameConstants.AREA_ID, ashaAreas),
                        where.or(
                                where.isNull(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE),
                                where.lt(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, today)
                        )
                ).query());
            } else {
                familyBeans.addAll(where.and(
                        where.in(FieldNameConstants.STATE, activeFamilyStates),
                        where.eq(FieldNameConstants.LOCATION_ID, locationId),
                        where.or(
                                where.isNull(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE),
                                where.lt(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, today)
                        )
                ).query());
            }

            List<FamilyBean> toBeRemovedFamilies = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                long l = memberBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.FAMILY_ID, bean.getFamilyId())
                        .and().notIn(FieldNameConstants.STATE, inactiveMembers).countOf();
                if (l == 0) {
                    toBeRemovedFamilies.add(bean);
                }
            }
            familyBeans.removeAll(toBeRemovedFamilies);

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    @Override
    public List<FamilyDataBean> searchFamilyDataBeansForIDSPByVillage(String search, String locationId, List<Integer> ashaAreas) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> activeFamilyStates = new ArrayList<>();
        activeFamilyStates.addAll(FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES);
        activeFamilyStates.addAll(FhsConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES);
        activeFamilyStates.add(FhsConstants.IDSP_FAMILY_STATE_IDSP_TEMP);

        List<String> inactiveMembers = new ArrayList<>(FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
        inactiveMembers.remove(FhsConstants.IDSP_MEMBER_STATE_IDSP_TEMP);

        try {
            Set<FamilyBean> familyBeans = new HashSet<>();

            List<String> familyIds = new ArrayList<>();
            List<FamilyBean> familyBeansBySearch;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long today = calendar.getTimeInMillis();

            Where<FamilyBean, Integer> where = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where();

            if (ashaAreas != null && !ashaAreas.isEmpty()) {
                //Fetching families based on search string (familyId)
                for (FamilyBean familyBean :
                        where.and(
                                where.in(FieldNameConstants.AREA_ID, ashaAreas),
                                where.in(FieldNameConstants.STATE, activeFamilyStates),
                                where.or(
                                        where.isNull(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE),
                                        where.lt(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, today)
                                )
                        ).query()
                ) {
                    familyIds.add(familyBean.getFamilyId());
                }
            } else {
                //Fetching families based on search string (familyId)
                for (FamilyBean familyBean :
                        where.and(
                                where.eq(FieldNameConstants.LOCATION_ID, locationId),
                                where.in(FieldNameConstants.STATE, activeFamilyStates),
                                where.or(
                                        where.isNull(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE),
                                        where.lt(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, today)
                                )
                        ).query()
                ) {
                    familyIds.add(familyBean.getFamilyId());
                }
            }

            // Search By familyId
            familyBeansBySearch = where.and().like(FieldNameConstants.FAMILY_ID, "%" + search + "%").query();

            // Search By Member uniqueHealthId
            Set<MemberBean> memberBeansBySearch = new HashSet<>();
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, inactiveMembers)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            // Search By Member firstName
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.FIRST_NAME, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, inactiveMembers)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            // Search By Member mobileNumber
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.MOBILE_NUMBER, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, inactiveMembers)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            List<String> familyIdsBySearch = new ArrayList<>();
            for (FamilyBean familyBean : familyBeansBySearch) {
                familyIdsBySearch.add(familyBean.getFamilyId());
            }

            for (MemberBean memberBean : memberBeansBySearch) {
                if (!familyIdsBySearch.contains(memberBean.getFamilyId())) {
                    familyIdsBySearch.add(memberBean.getFamilyId());
                }
            }

            where = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE,
                    FieldNameConstants.LAST_IDSP_SCREENING_DATE, FieldNameConstants.LAST_IDSP_2_SCREENING_DATE)
                    .orderBy(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, true)
                    .where();

            if (ashaAreas != null && !ashaAreas.isEmpty()) {
                familyBeans.addAll(where.and(
                        where.in(FieldNameConstants.FAMILY_ID, familyIdsBySearch),
                        where.in(FieldNameConstants.AREA_ID, ashaAreas),
                        where.or(
                                where.isNull(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE),
                                where.lt(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, today)
                        )
                ).query());
            } else {
                familyBeans.addAll(where.and(
                        where.in(FieldNameConstants.FAMILY_ID, familyIdsBySearch),
                        where.eq(FieldNameConstants.LOCATION_ID, locationId),
                        where.or(
                                where.isNull(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE),
                                where.lt(FieldNameConstants.LAST_IDSP_2_SCREENING_DATE, today)
                        )
                ).query());
            }

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean aFamily : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(aFamily, memberDataBeans));
            }

        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    @Override
    public List<MemberDataBean> retrieveActiveMemberByFamilyIdForIdsp(String familyId) {
        List<String> inactiveMembers = new ArrayList<>(FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
        inactiveMembers.remove(FhsConstants.IDSP_MEMBER_STATE_IDSP_TEMP);

        List<MemberDataBean> memberDataBeans = new ArrayList<>();
        try {
            List<MemberBean> memberBeans = new ArrayList<>(memberBeanDao.queryBuilder().orderBy(FieldNameConstants.FAMILY_HEAD_FLAG, Boolean.FALSE)
                    .where().eq(FieldNameConstants.FAMILY_ID, familyId).and().notIn(FieldNameConstants.STATE, inactiveMembers).query());
            for (MemberBean bean : memberBeans) {
                memberDataBeans.add(new MemberDataBean(bean));
            }
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        return memberDataBeans;
    }

    @Override
    public List<FamilyDataBean> retrieveFamilyDataBeansForAbhaNumberByVillage(String locationId, long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> verifiedFamilyStates = new ArrayList<>();
        verifiedFamilyStates.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_ORPHAN);
        verifiedFamilyStates.removeAll(FhsConstants.CFHC_FAMILY_STATES);

        try {
            QueryBuilder<MemberBean, Integer> memberQB = memberBeanDao.queryBuilder();
            memberQB.where().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES);
            List<FamilyBean> familyBeans = new ArrayList<>(familyBeanDao.queryBuilder()
                    .join(FieldNameConstants.FAMILY_ID, FieldNameConstants.FAMILY_ID, memberQB)
                    .limit(limit).offset(offset).selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE, FieldNameConstants.PENDING_ABHA_COUNT).distinct()
                    .orderBy(FieldNameConstants.UPDATED_ON, true)
                    .where().eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, verifiedFamilyStates)
                    .and().gt(FieldNameConstants.PENDING_ABHA_COUNT, 0)
                    .query());

            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean bean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(bean, memberDataBeans));
            }
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }

    @Override
    public List<FamilyDataBean> searchFamilyDataBeansForAbhaNumberByVillage(String search, String locationId) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();

        List<String> verifiedFamilyStates = new ArrayList<>();
        verifiedFamilyStates.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED);
        verifiedFamilyStates.add(FhsConstants.FHS_FAMILY_STATE_ORPHAN);

        try {

            //Fetching families based on search string (familyId)

            List<String> familyIds = new ArrayList<>();
            for (FamilyBean familyBean : familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, verifiedFamilyStates).query()) {
                familyIds.add(familyBean.getFamilyId());
            }

            // Search By familyId
            List<FamilyBean> familyBeansBySearch = familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.FAMILY_ID, "%" + search + "%")
                    .and().eq(FieldNameConstants.AREA_ID, locationId)
                    .and().in(FieldNameConstants.STATE, verifiedFamilyStates).query();

            // Search By Member uniqueHealthId
            Set<MemberBean> memberBeansBySearch = new HashSet<>();
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.UNIQUE_HEALTH_ID, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            // Search By Member firstName
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.FIRST_NAME, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            // Search By Member mobileNumber
            memberBeansBySearch.addAll(memberBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID)
                    .where().like(FieldNameConstants.MOBILE_NUMBER, "%" + search + "%")
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .and().in(FieldNameConstants.FAMILY_ID, familyIds).query());

            List<String> familyIdsBySearch = new ArrayList<>();
            for (FamilyBean familyBean : familyBeansBySearch) {
                familyIdsBySearch.add(familyBean.getFamilyId());
            }

            for (MemberBean memberBean : memberBeansBySearch) {
                if (!familyIdsBySearch.contains(memberBean.getFamilyId())) {
                    familyIdsBySearch.add(memberBean.getFamilyId());
                }
            }

            Set<FamilyBean> familyBeans = new HashSet<>(familyBeanDao.queryBuilder().selectColumns(FieldNameConstants.FAMILY_ID, FieldNameConstants.STATE, FieldNameConstants.PENDING_ABHA_COUNT)
                    .where().in(FieldNameConstants.FAMILY_ID, familyIdsBySearch)
                    .and().eq(FieldNameConstants.AREA_ID, locationId)
                    .and().gt(FieldNameConstants.PENDING_ABHA_COUNT, 0)
                    .query());


            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean aFamily : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(aFamily, memberDataBeans));
            }

        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        return familyDataBeans;
    }
}
