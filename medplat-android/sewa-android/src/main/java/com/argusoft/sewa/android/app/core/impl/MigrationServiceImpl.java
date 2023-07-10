package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.core.MigrationService;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationInConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationOutDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MigratedFamilyDataBean;
import com.argusoft.sewa.android.app.databean.MigratedMembersDataBean;
import com.argusoft.sewa.android.app.databean.MigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.MigrationInConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.MigrationInDataBean;
import com.argusoft.sewa.android.app.databean.MigrationOutConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.MigrationOutDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 18/3/19.
 */

@EBean(scope = EBean.Scope.Singleton)
public class MigrationServiceImpl implements MigrationService {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationMasterBean, Integer> locationMasterBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MigratedMembersBean, Integer> migratedMembersBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MigratedFamilyBean, Integer> migratedFamilyBeanDao;

    @Override
    public List<LocationMasterBean> retrieveLocationMasterBeansBySearch(CharSequence s, Integer level) {
        List<LocationMasterBean> locationMasterBeans = null;
        try {
            Where<LocationMasterBean, Integer> name = locationMasterBeanDao.queryBuilder().where()
                    .like("name", s + "%");
            if (level != null) {
                name = name.and().eq("level", level);
            }
            locationMasterBeans = name.query();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return locationMasterBeans;
    }

    @Override
    public Map<Integer, String> retrieveHierarchyOfVillage(LocationMasterBean locationMasterBean) {
        Map<Integer, String> hierarchyMapWithLevelAndName = new HashMap<>();
        hierarchyMapWithLevelAndName.put(6, locationMasterBean.getName());
        try {
            LocationMasterBean subCenter = locationMasterBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_ID, locationMasterBean.getParent()).queryForFirst();
            hierarchyMapWithLevelAndName.put(5, subCenter.getName());

            LocationMasterBean phc = locationMasterBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_ID, subCenter.getParent()).queryForFirst();
            hierarchyMapWithLevelAndName.put(4, phc.getName());

            LocationMasterBean block = locationMasterBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_ID, phc.getParent()).queryForFirst();
            hierarchyMapWithLevelAndName.put(3, block.getName());

            LocationMasterBean district = locationMasterBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_ID, block.getParent()).queryForFirst();
            hierarchyMapWithLevelAndName.put(2, district.getName());

            LocationMasterBean region = locationMasterBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_ID, district.getParent()).queryForFirst();
            hierarchyMapWithLevelAndName.put(1, region.getName());
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return hierarchyMapWithLevelAndName;
    }


    @Override
    public List<MemberBean> retrieveChildrenUnder5YearsByMotherId(Long motherId) {
        List<MemberBean> childrenUnder5Years = new ArrayList<>();
        try {
            List<MemberBean> allChildrenOfMother = memberBeanDao.queryBuilder()
                    .where().eq(FieldNameConstants.MOTHER_ID, motherId)
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES).query();

            if (allChildrenOfMother != null && !allChildrenOfMother.isEmpty()) {
                for (MemberBean child : allChildrenOfMother) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, -5);
                    if (child.getDob().after(cal.getTime())) {
                        childrenUnder5Years.add(child);
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return childrenUnder5Years;
    }

    @Override
    public List<FamilyDataBean> retrieveFamilyListBySearchForMigration(String search) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();
        try {
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().where()
                    .like(FieldNameConstants.FAMILY_ID, "%" + search + "%")
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean familyBean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(familyBean, memberDataBeans));
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return familyDataBeans;
    }

    @Override
    public List<FamilyDataBean> searchFamilyByLocation(String search, Long locationId, long limit, long offset) {
        List<FamilyDataBean> familyDataBeans = new ArrayList<>();
        try {
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().limit(limit).offset(offset).where()
                    .eq(FieldNameConstants.LOCATION_ID, locationId)
                    .and().like(FieldNameConstants.FAMILY_ID, "%" + search + "%")
                    .and().in(FieldNameConstants.STATE, FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();
            ArrayList<MemberDataBean> memberDataBeans = new ArrayList<>();
            for (FamilyBean familyBean : familyBeans) {
                familyDataBeans.add(new FamilyDataBean(familyBean, memberDataBeans));
            }

        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return familyDataBeans;
    }

    @Override
    public void createMigrationOut(MigrationOutDataBean migration, MemberDataBean memberDataBean, NotificationBean notificationBean) {
        Log.i(getClass().getSimpleName(), "#### Generated Migration Out : " + new Gson().toJson(migration));

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.TECHO_MIGRATION_OUT);
        storeAnswerBean.setAnswer(new Gson().toJson(migration));
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        if (notificationBean != null) {
            storeAnswerBean.setNotificationId(notificationBean.getNotificationId());
        } else {
            storeAnswerBean.setNotificationId(-1L);
        }
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        storeAnswerBean.setMemberId(Long.valueOf(memberDataBean.getId()));
        if (SewaTransformer.loginBean != null) {
            storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
            storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        }
        sewaService.createStoreAnswerBean(storeAnswerBean);

        LoggerBean loggerBean = new LoggerBean();
        loggerBean.setBeneficiaryName(memberDataBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberDataBean));
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.TECHO_MIGRATION_OUT);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_MIGRATION_OUT));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    public void createMigrationIn(MigrationInDataBean migration) {
        Log.i(getClass().getSimpleName(), "#### Generated Migration In : " + new Gson().toJson(migration));

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.TECHO_MIGRATION_IN);
        storeAnswerBean.setAnswer(new Gson().toJson(migration));
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
        loggerBean.setBeneficiaryName(migration.getFirstname() + " " + migration.getMiddleName() + " " + migration.getLastName() + " (" + migration.getPhoneNumber() + ")");
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.TECHO_MIGRATION_IN);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_MIGRATION_IN));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    public void createMigrationInConfirmation(MigrationInConfirmationDataBean migrationInConfirmation, MigrationDetailsDataBean migrationDetailsDataBean) {
        Log.i(getClass().getSimpleName(), "#### Generated Migration In Confirmation: " + new Gson().toJson(migrationInConfirmation));

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.TECHO_MIGRATION_IN_CONFIRMATION);
        storeAnswerBean.setAnswer(new Gson().toJson(migrationInConfirmation));
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(migrationInConfirmation.getNotificationId());
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        storeAnswerBean.setMemberId(migrationInConfirmation.getMemberId());
        if (SewaTransformer.loginBean != null) {
            storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
            storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        }
        sewaService.createStoreAnswerBean(storeAnswerBean);


        LoggerBean loggerBean = new LoggerBean();
        String beneficiaryName = migrationDetailsDataBean.getFirstName() + " " + migrationDetailsDataBean.getMiddleName() + " " + migrationDetailsDataBean.getLastName();
        loggerBean.setBeneficiaryName(beneficiaryName);

        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.TECHO_MIGRATION_IN_CONFIRMATION);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_MIGRATION_IN_CONFIRMATION));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    public void createMigrationOutConfirmation(MigrationOutConfirmationDataBean migrationOutConfirmation, MigrationDetailsDataBean migrationDetailsDataBean) {
        Log.i(getClass().getSimpleName(), "#### Generated Migration Out Confirmation: " + new Gson().toJson(migrationOutConfirmation));

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.TECHO_MIGRATION_OUT_CONFIRMATION);
        storeAnswerBean.setAnswer(new Gson().toJson(migrationOutConfirmation));
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(-1L);
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        storeAnswerBean.setMemberId(migrationOutConfirmation.getMemberId());
        if (SewaTransformer.loginBean != null) {
            storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
            storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        }
        sewaService.createStoreAnswerBean(storeAnswerBean);


        LoggerBean loggerBean = new LoggerBean();
        String beneficiaryName = migrationDetailsDataBean.getFirstName() + " " + migrationDetailsDataBean.getMiddleName() + " " + migrationDetailsDataBean.getLastName();
        loggerBean.setBeneficiaryName(beneficiaryName);

        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.TECHO_MIGRATION_OUT_CONFIRMATION);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_MIGRATION_OUT_CONFIRMATION));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    public List<MigratedMembersBean> retrieveMigrationDetailsForMigratedMembers(Integer locationId, List<Integer> selectedAshaAreas, boolean isOut, long limit, long offset) {
        List<MigratedMembersBean> migratedMembers = new ArrayList<>();
        try {
            List<Long> allLoc = new ArrayList<>();
            if (locationId != null) {
                allLoc.add(locationId.longValue());
            }
            for (Integer area : selectedAshaAreas) {
                allLoc.add(area.longValue());
            }

            List<MigratedMembersBean> migratedMembersBeans;
            if (isOut) {
                migratedMembersBeans = migratedMembersBeanDao.queryBuilder().limit(limit).offset(offset).where().in("fromLocationId", allLoc).query();
            } else {
                migratedMembersBeans = migratedMembersBeanDao.queryBuilder().limit(limit).offset(offset).where().in("toLocationId", allLoc).query();
            }

            List<MigratedMembersBean> lfu = new ArrayList<>();
            List<MigratedMembersBean> confirmationPending = new ArrayList<>();
            List<MigratedMembersBean> outOfState = new ArrayList<>();
            List<MigratedMembersBean> confirmed = new ArrayList<>();

            for (MigratedMembersBean bean : migratedMembersBeans) {
                if (bean.getLfu() != null && bean.getLfu()) {
                    lfu.add(bean);
                } else if (bean.getOutOfState() != null && bean.getOutOfState()) {
                    outOfState.add(bean);
                } else if (bean.getConfirmed() == null || !bean.getConfirmed()) {
                    confirmationPending.add(bean);
                } else {
                    confirmed.add(bean);
                }
            }

            migratedMembers.addAll(lfu);
            migratedMembers.addAll(confirmationPending);
            migratedMembers.addAll(outOfState);
            migratedMembers.addAll(confirmed);

        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        return migratedMembers;
    }


    @Override
    public void storeRevertedMigrationBean(MigratedMembersDataBean migratedMembersBean) {
        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.TECHO_MIGRATION_REVERTED);
        storeAnswerBean.setAnswer(new GsonBuilder().registerTypeAdapter(Date.class, UtilBean.JSON_DATE_SERIALIZER).create().toJson(migratedMembersBean));
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
        String beneficiaryName = migratedMembersBean.getHealthId() + " - " + migratedMembersBean.getName();
        loggerBean.setBeneficiaryName(beneficiaryName);

        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.TECHO_MIGRATION_REVERTED);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_MIGRATION_REVERTED));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);

        try {
            DeleteBuilder<MigratedMembersBean, Integer> deleteBuilder = migratedMembersBeanDao.deleteBuilder();
            deleteBuilder.where().eq("memberId", migratedMembersBean.getMemberId());
            deleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void createFamilyMigrationOut(FamilyMigrationOutDataBean migration, FamilyDataBean family, NotificationMobDataBean notificationMobDataBean) {
        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.FAMILY_MIGRATION_OUT);
        storeAnswerBean.setAnswer(new Gson().toJson(migration));
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        if (notificationMobDataBean != null) {
            storeAnswerBean.setNotificationId(notificationMobDataBean.getId());
        } else {
            storeAnswerBean.setNotificationId(-1L);
        }
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        if (SewaTransformer.loginBean != null) {
            storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
            storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        }
        sewaService.createStoreAnswerBean(storeAnswerBean);

        LoggerBean loggerBean = new LoggerBean();
        String beneficiaryName = family.getFamilyId();
        loggerBean.setBeneficiaryName(beneficiaryName);

        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.FAMILY_MIGRATION_OUT);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.FAMILY_MIGRATION_OUT));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);

        fhsService.markFamilyAsMigrated(family.getFamilyId());
        if (notificationMobDataBean != null) {
            sewaService.deleteNotificationByNotificationId(notificationMobDataBean.getId());
        }
    }

    public void createFamilyMigrationInConfirmation(FamilyMigrationInConfirmationDataBean confirmationDataBean, FamilyMigrationDetailsDataBean detailsDataBean) {
        Log.i(getClass().getSimpleName(), "#### Generated Family Migration In Confirmation: " + new Gson().toJson(confirmationDataBean));

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.FAMILY_MIGRATION_IN_CONFIRMATION);
        storeAnswerBean.setAnswer(new Gson().toJson(confirmationDataBean));
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(confirmationDataBean.getNotificationId());
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        if (SewaTransformer.loginBean != null) {
            storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
            storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        }
        sewaService.createStoreAnswerBean(storeAnswerBean);

        LoggerBean loggerBean = new LoggerBean();
        StringBuilder beneficiaryName = new StringBuilder();
        if (detailsDataBean.getFamilyIdString() != null) {
            beneficiaryName.append(detailsDataBean.getFamilyIdString());
        }
        for (String string : detailsDataBean.getMemberDetails()) {
            beneficiaryName.append("\n");
            beneficiaryName.append(string);
        }

        loggerBean.setBeneficiaryName(beneficiaryName.toString());
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.FAMILY_MIGRATION_IN_CONFIRMATION);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.FAMILY_MIGRATION_IN_CONFIRMATION));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    public void deleteMigratedMembersBean(MigratedMembersBean migratedMembersBean) {
        try {
            migratedMembersBeanDao.delete(migratedMembersBeanDao.queryForMatching(migratedMembersBean));
        } catch (Exception e) {
            Log.e(getClass().getName(), "Exception" + e);
        }
    }

    @Override
    public List<MigratedFamilyBean> retrieveMigrationDetailsForMigratedFamily(Integer locationId, List<Integer> selectedAshaAreas, boolean isOut, long limit, long offset) {
        List<MigratedFamilyBean> migratedFamily = new ArrayList<>();
        try {
            List<Long> allLoc = new ArrayList<>();
            if (locationId != null) {
                allLoc.add(locationId.longValue());
            }
            for (Integer area : selectedAshaAreas) {
                allLoc.add(area.longValue());
            }

            List<MigratedFamilyBean> migratedFamilyBeans;
            if (isOut) {
                migratedFamilyBeans = migratedFamilyBeanDao.queryBuilder().limit(limit).offset(offset).where().in("fromLocationId", allLoc).query();
            } else {
                migratedFamilyBeans = migratedFamilyBeanDao.queryBuilder().limit(limit).offset(offset).where().in("toLocationId", allLoc).query();
            }

            List<MigratedFamilyBean> lfu = new ArrayList<>();
            List<MigratedFamilyBean> confirmationPending = new ArrayList<>();
            List<MigratedFamilyBean> outOfState = new ArrayList<>();
            List<MigratedFamilyBean> confirmed = new ArrayList<>();

            for (MigratedFamilyBean bean : migratedFamilyBeans) {
                if (bean.getLfu() != null && bean.getLfu()) {
                    lfu.add(bean);
                } else if (bean.getOutOfState() != null && bean.getOutOfState()) {
                    outOfState.add(bean);
                } else if (bean.getConfirmed() == null || !bean.getConfirmed()) {
                    confirmationPending.add(bean);
                } else {
                    confirmed.add(bean);
                }
            }

            migratedFamily.addAll(lfu);
            migratedFamily.addAll(confirmationPending);
            migratedFamily.addAll(outOfState);
            migratedFamily.addAll(confirmed);

        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        return migratedFamily;
    }

    @Override
    public void storeRevertedFamilyMigrationBean(MigratedFamilyDataBean migratedFamilyDataBean) {
        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.TECHO_FAMILY_MIGRATION_REVERTED);
        storeAnswerBean.setAnswer(new GsonBuilder().registerTypeAdapter(Date.class, UtilBean.JSON_DATE_SERIALIZER).create().toJson(migratedFamilyDataBean));
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
        String beneficiaryName = migratedFamilyDataBean.getFamilyIdString();
        loggerBean.setBeneficiaryName(beneficiaryName);

        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.TECHO_FAMILY_MIGRATION_REVERTED);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_FAMILY_MIGRATION_REVERTED));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);

        try {
            DeleteBuilder<MigratedFamilyBean, Integer> deleteBuilder = migratedFamilyBeanDao.deleteBuilder();
            deleteBuilder.where().eq("familyId", migratedFamilyDataBean.getFamilyId());
            deleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void deleteMigratedFamilyBean(MigratedFamilyBean migratedFamilyBean) {
        try {
            migratedFamilyBeanDao.delete(migratedFamilyBeanDao.queryForMatching(migratedFamilyBean));
        } catch (Exception e) {
            Log.e(getClass().getName(), "Exception" + e);
        }
    }
}
