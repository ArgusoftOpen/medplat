package com.argusoft.sewa.android.app.core.impl;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

import android.content.Context;
import android.provider.Settings;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.TechoService;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LibraryBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationCoordinatesBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LocationTypeBean;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.model.MemberPregnancyStatusBean;
import com.argusoft.sewa.android.app.model.MenuBean;
import com.argusoft.sewa.android.app.model.MergedFamiliesBean;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.QuestionBean;
import com.argusoft.sewa.android.app.model.RchVillageProfileBean;
import com.argusoft.sewa.android.app.model.ReadNotificationsBean;
import com.argusoft.sewa.android.app.model.SchoolBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.restclient.impl.ApiManager;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EBean(scope = Singleton)
public class TechoServiceImpl implements TechoService {

    private SewaTransformer sewaTransformer = SewaTransformer.getInstance();
    private static final String TAG = "TechoServiceImpl";

    @RootContext
    Context context;

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    @Bean
    LibraryDownloadServiceImpl libraryDownloadService;
    @Bean
    LocationMasterServiceImpl locationService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    ApiManager apiManager;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationBean, Integer> locationBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FhwServiceDetailBean, Integer> fhwServiceDetailBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MergedFamiliesBean, Integer> mergedFamiliesBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FormAccessibilityBean, Integer> formAccessibilityBeanDao;
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
    Dao<DataQualityBean, Integer> dataQualityBeanDao;
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
    Dao<MemberCbacDetailBean, Integer> memberCbacDetailBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<HealthInfrastructureBean, Integer> healthInfrastructureBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<SchoolBean, Integer> schoolBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationCoordinatesBean, Integer> locationCoordinatesBeansDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<StoreAnswerBean, Integer> storeAnswerBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberPregnancyStatusBean, Integer> pregnancyStatusBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<CovidTravellersInfoBean, Integer> covidTravellersInfoBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationTypeBean, Integer> locationTypeBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MenuBean, Integer> menuBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MigratedFamilyBean, Integer> migratedFamilyBeanDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private static final String MAX_UPDATED_ON = "MAX(updatedOn)";
    private static final String MAX_MODIFIED_ON = "MAX(modifiedOn)";

    @Override
    public Map<String, Boolean> checkIfDeviceIsBlockedOrDeleteDatabase(Context context) {
        String isBlocked = "isBlocked";
        String isDatabaseDeleted = "isDatabaseDeleted";
        Map<String, Boolean> deviceState = new HashMap<>();
        deviceState.put(isBlocked, false);
        deviceState.put(isDatabaseDeleted, false);
        try {
            String imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (imei == null || imei.isEmpty()) {
                return deviceState;
            }

            if (sewaService.isOnline()) {
                Map<String, Object> blockedDevice = sewaServiceRestClient.checkIfDeviceIsBlockedOrDeleteDatabase(imei);

                if (blockedDevice != null && !blockedDevice.isEmpty()) {
                    Object blockDevice = blockedDevice.get(GlobalTypes.BLOCKED_DEVICE_IS_BLOCK_DEVICE);
                    Object deleteDataBase = blockedDevice.get(GlobalTypes.BLOCKED_DEVICE_IS_DELETE_DATABASE);
                    if (blockDevice != null
                            && Boolean.TRUE.equals(Boolean.valueOf(blockDevice.toString()))) {
                        deviceState.put(isBlocked, true);
                        storeVersionBeanForBlockedImei(true);
                        deleteDatabaseFileFromLocal(context);
                    } else if (deleteDataBase != null
                            && Boolean.TRUE.equals(Boolean.valueOf(deleteDataBase.toString()))) {
                        deviceState.put(isDatabaseDeleted, true);
                        deleteDatabaseFileFromLocal(context);
                        sewaServiceRestClient.removeEntryForDeviceOfIMEI(imei);
                    } else {
                        storeVersionBeanForBlockedImei(false);
                    }
                } else {
                    storeVersionBeanForBlockedImei(false);
                }
            } else {
                List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_IMEI_BLOCKED);
                if (versionBeans != null && !versionBeans.isEmpty() && versionBeans.get(0).getValue().equals("1")) {
                    deviceState.put(isBlocked, true);
                }
            }
        } catch (SQLException | SecurityException | RestHttpException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return deviceState;
    }

    private void storeVersionBeanForBlockedImei(boolean isBlocked) {
        try {
            List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_IMEI_BLOCKED);

            if (versionBeans != null && !versionBeans.isEmpty()) {
                VersionBean versionBean = versionBeans.get(0);
                if (isBlocked) {
                    versionBean.setValue("1");
                } else {
                    versionBean.setValue("0");
                }
                versionBeanDao.update(versionBean);
            } else {
                VersionBean versionBean = new VersionBean();
                versionBean.setKey(GlobalTypes.VERSION_IMEI_BLOCKED);
                if (isBlocked) {
                    versionBean.setValue("1");
                } else {
                    versionBean.setValue("0");
                }
                versionBeanDao.create(versionBean);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void deleteDatabaseFileFromLocal(Context context) {
        try {
            File dir = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DATABASE));
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    new File(dir, child).delete();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, null, e);
        }
    }

    @Override
    public Boolean syncMergedFamiliesInformationWithServer() {
        String isSyncedWithServer = "isSyncedWithServer";
        try {
            List<MergedFamiliesBean> mergedFamiliesList = mergedFamiliesBeanDao.queryBuilder().where().eq(isSyncedWithServer, false).query();
            if (mergedFamiliesList != null && !mergedFamiliesList.isEmpty()) {
                Boolean status = sewaServiceRestClient.syncMergedFamiliesInformationWithServer(mergedFamiliesList);
                if (Boolean.TRUE.equals(status)) {
                    UpdateBuilder<MergedFamiliesBean, Integer> mergedFamiliesBeanIntegerUpdateBuilder = mergedFamiliesBeanDao.updateBuilder();
                    mergedFamiliesBeanIntegerUpdateBuilder.where().eq(isSyncedWithServer, false);
                    mergedFamiliesBeanIntegerUpdateBuilder.updateColumnValue(isSyncedWithServer, true);
                    mergedFamiliesBeanIntegerUpdateBuilder.update();
                }
            }
            return true;
        } catch (SQLException | RestHttpException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }



    @Override
    public void getLgdCodeWiseCoordinates() {
        Map<String, Map<String, List<Double>>> map = new HashMap<>();
        try {
            Gson gson = new Gson();
            List<LocationCoordinatesBean> locationCoordinatesBeans = locationCoordinatesBeansDao.queryForAll();
            for (LocationCoordinatesBean locationCoordinatesBean : locationCoordinatesBeans) {
                Map<String, List<Double>> mapOfLatLongArray = new HashMap<>();
                List<Double> latArray = new ArrayList<>();
                List<Double> longArray = new ArrayList<>();
                List<Double[]> coordinates = gson.fromJson(locationCoordinatesBean.getCoordinates(), new TypeToken<List<Double[]>>() {
                }.getType());

                for (Double[] aCoordinate : coordinates) {
                    longArray.add(aCoordinate[0]);
                    latArray.add(aCoordinate[1]);
                }
                mapOfLatLongArray.put("longArray", longArray);
                mapOfLatLongArray.put("latArray", latArray);
                map.put(locationCoordinatesBean.getLgdCode(), mapOfLatLongArray);
            }
            SharedStructureData.mapOfLatLongWithLGDCode = map;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            SharedStructureData.mapOfLatLongWithLGDCode = null;
        }
    }

    @Override
    public boolean checkIfFeatureIsReleased() {
        try {
            List<VersionBean> featureVersionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_FEATURES_LIST);
            if (featureVersionBeans != null && !featureVersionBeans.isEmpty()) {
                VersionBean versionBean = featureVersionBeans.get(0);
                return versionBean.getValue().contains(GlobalTypes.MOB_FEATURE_GEO_FENCING);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean checkIfOfflineAnyFormFilledForMember(Long memberId) {
        if (memberId == null) {
            return false;
        }

        try {
            List<StoreAnswerBean> storeAnswerBeans = storeAnswerBeanDao.queryForAll();
            for (StoreAnswerBean bean : storeAnswerBeans) {
                if (memberId.equals(bean.getMemberId())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return false;
    }

    public void deleteQuestionAndAnswersByFormCode(String formCode) {
        try {
            DeleteBuilder<QuestionBean, Integer> deleteQueBuilder = questionBeanDao.deleteBuilder();
            deleteQueBuilder.where().eq(SewaConstants.QUESTION_BEAN_ENTITY, formCode);
            PreparedDelete<QuestionBean> prepare = deleteQueBuilder.prepare();
            if (prepare != null) {
                questionBeanDao.delete(prepare);
            }

            DeleteBuilder<AnswerBean, Integer> deleteAnsBuilder = answerBeanDao.deleteBuilder();
            deleteAnsBuilder.where().eq(SewaConstants.QUESTION_BEAN_ENTITY, formCode);
            PreparedDelete<AnswerBean> prepare1 = deleteAnsBuilder.prepare();
            if (prepare1 != null) {
                answerBeanDao.delete(prepare1);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public boolean isNewNotification() {
        try {
            List<AnnouncementBean> announcementBeans = announcementBeanDao.queryBuilder().where().eq("isPlayedAnnouncement", 0).query();
            return announcementBeans.size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
