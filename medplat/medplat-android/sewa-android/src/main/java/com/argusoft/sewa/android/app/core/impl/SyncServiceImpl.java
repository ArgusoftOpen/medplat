package com.argusoft.sewa.android.app.core.impl;

import static com.argusoft.sewa.android.app.datastructure.SharedStructureData.NETWORK_MESSAGE;
import static org.androidannotations.annotations.EBean.Scope.Singleton;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.SyncService;
import com.argusoft.sewa.android.app.databean.AnnouncementMobDataBean;
import com.argusoft.sewa.android.app.databean.ComponentTagBean;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.LibraryDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserLessonMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LoggedInUserPrincipleDataBean;
import com.argusoft.sewa.android.app.databean.LoginRequestParamDetailDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.argusoft.sewa.android.app.databean.MigratedFamilyDataBean;
import com.argusoft.sewa.android.app.databean.MigratedMembersDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.databean.RchVillageProfileDataBean;
import com.argusoft.sewa.android.app.databean.SurveyLocationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LibraryBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationCoordinatesBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LocationTypeBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.model.MemberMoConfirmedDetailBean;
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
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.impl.ApiManager;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;

@EBean(scope = Singleton)
public class SyncServiceImpl implements SyncService {

    private SewaTransformer sewaTransformer = SewaTransformer.getInstance();
    private static final String TAG = "SyncServiceImpl";
    private static final String MAX_UPDATED_ON = "MAX(updatedOn)";
    private static final String MAX_MODIFIED_ON = "MAX(modifiedOn)";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

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
    LmsDownloadServiceImpl lmsDownloadService;
    @Bean
    LmsServiceImpl lmsService;
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
    Dao<MigratedFamilyBean, Integer> migratedFamilyBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationTypeBean, Integer> locationTypeBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MenuBean, Integer> menuBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsCourseBean, Integer> courseBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsUserMetaDataBean, Integer> lmsUserMetadataBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsViewedMediaBean, Integer> lmsViewedMediaBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberMoConfirmedDetailBean, Integer> moConfirmedBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<DrugInventoryBean, Integer> drugInventoryBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<UserHealthInfraBean, Integer> userHealthInfraBeanDao;

    private void setImeiAndPhoneNumberInLoginParam(LoginRequestParamDetailDataBean param) {
        try {
            param.setImeiNumber(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void getDataFromServer() {
        try {
            Map<String, Boolean> metadata = sewaServiceRestClient.getMetadata();
            LoginRequestParamDetailDataBean loginRequestParamDetailDataBean = generateSyncRequestParams(metadata);

            //MAIN API CALL TO SERVER FOR BRINGING ALL THE DATA
            Date apiStartDate = new Date();
            Log.i(TAG, LabelConstants.CALLING_MAIN_API_FOR_BRINGING_DATA);
            LoggedInUserPrincipleDataBean loggedInUserPrincipleDataBean = sewaServiceRestClient.syncData(loginRequestParamDetailDataBean);
            Log.i(TAG, LabelConstants.DATA_FROM_MAIN_API_RETRIEVED + UtilBean.getDifferenceBetweenTwoDates(apiStartDate, new Date()));

            if (loggedInUserPrincipleDataBean == null) {
                return;
            }

            storeSyncData(loginRequestParamDetailDataBean, loggedInUserPrincipleDataBean);
        } catch (Exception e) {
            NETWORK_MESSAGE = SewaConstants.EXCEPTION_FETCHING_DATA_FOR_USER;
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public LoginRequestParamDetailDataBean generateSyncRequestParams(Map<String, Boolean> metadata) {
        LoginRequestParamDetailDataBean loginRequestParamDetailDataBean = new LoginRequestParamDetailDataBean();
        try {
            loginRequestParamDetailDataBean.setToken(SewaTransformer.loginBean.getUserToken());
            loginRequestParamDetailDataBean.setUserId(SewaTransformer.loginBean.getUserID());
            loginRequestParamDetailDataBean.setVillageCode(SewaTransformer.loginBean.getVillageCode());
            loginRequestParamDetailDataBean.setRoleType(SewaTransformer.loginBean.getUserRoleCode());
            loginRequestParamDetailDataBean.setSdkVersion(Build.VERSION.SDK_INT);
            setImeiAndPhoneNumberInLoginParam(loginRequestParamDetailDataBean);

            //For getting Free Space
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            long megAvailable = bytesAvailable / (1024 * 1024);
            Log.i(TAG, LabelConstants.AVAILABLE_SPACE_IN_MB + megAvailable);
            loginRequestParamDetailDataBean.setFreeSpaceMB(megAvailable);

            for (Map.Entry<String, Boolean> entry : metadata.entrySet()) {
                if (!Boolean.TRUE.equals(entry.getValue())) {
                    continue;
                }
                switch (entry.getKey()) {
                    case SyncConstant.LOCATION_MASTER_BEAN:
                        if (locationMasterBeanDao.countOf() > 0) {
                            String string = locationMasterBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = locationMasterBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForLocation(String.valueOf(sdf.parse(result[0]).getTime()));
                            }
                        }
                        break;

                    case SyncConstant.LOCATION_TYPE_BEAN:
                        if (locationTypeBeanDao.countOf() > 0) {
                            String string = locationTypeBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = locationTypeBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForLocationTypeMaster(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;


                    case SyncConstant.MENU_BEAN:
                        if (menuBeanDao.countOf() > 0) {
                            Date modifiedOn = menuBeanDao.queryBuilder().queryForFirst().getModifiedOn();
                            if (modifiedOn != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForMenuMaster(modifiedOn.getTime());
                            }
                        }
                        break;

                    case SyncConstant.HEALTH_INFRASTRUCTURE_BEAN:
                        if (healthInfrastructureBeanDao.countOf() > 0) {
                            List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LAST_UPDATED_HEALTH_INFRASTRUCTURE);
                            if (versionBeans != null && !versionBeans.isEmpty()) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForHealthInfrastructure(Long.valueOf(versionBeans.get(0).getValue()));
                            }
                        }
                        break;

                    case SyncConstant.SCHOOL_BEAN:
                        if (schoolBeanDao.countOf() > 0) {
                            String string = schoolBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = schoolBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForSchoolMaster(String.valueOf(sdf.parse(result[0]).getTime()));
                            }
                        }
                        break;

                    case SyncConstant.FAMILY_BEAN:
                        if (familyBeanDao.countOf() > 0 && memberBeanDao.countOf() > 0) {
                            String string = familyBeanDao.queryBuilder().selectRaw(MAX_UPDATED_ON).prepareStatementString();
                            String[] result = familyBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null)
                                loginRequestParamDetailDataBean.setLastUpdateDateForFamily(String.valueOf(sdf.parse(result[0]).getTime()));
                        }
                        break;

                    case SyncConstant.NOTIFICATION_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateDateForNotifications(0L);
                        if (notificationBeanDao.countOf() > 0) {
                            String string = notificationBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = notificationBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForNotifications(sdf.parse(result[0]).getTime());
                            }
                        }

                        if (readNotificationsBeanDao.countOf() > 0) {
                            List<Long> list = new ArrayList<>();
                            List<ReadNotificationsBean> readNotificationsBeans = readNotificationsBeanDao.queryForAll();
                            for (ReadNotificationsBean readNotificationsBean : readNotificationsBeans) {
                                list.add(readNotificationsBean.getNotificationId());
                            }
                            loginRequestParamDetailDataBean.setReadNotifications(list);
                        }
                        break;

                    case SyncConstant.MIGRATED_MEMBERS_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateDateForMigrationDetails(0L);
                        if (migratedMembersBeanDao.countOf() > 0) {
                            String string = migratedMembersBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = migratedMembersBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForMigrationDetails(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.LIST_VALUE_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateDateForListValue(0L);
                        if (listValueBeanDao.countOf() > 0) {
                            String string = listValueBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = listValueBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForListValue(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.LIBRARY_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateDateForLibrary(0L);
                        if (libraryBeanDao.countOf() > 0) {
                            String string = libraryBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = libraryBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForLibrary(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.ANNOUNCEMENT_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateOfAnnouncements(0L);
                        if (announcementBeanDao.countOf() > 0) {
                            String string = announcementBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = announcementBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateOfAnnouncements(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.LABEL_BEAN:
                        loginRequestParamDetailDataBean.setCreatedOnDateForLabel(0L);
                        if (labelBeanDao.countOf() > 0) {
                            String string = labelBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = labelBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setCreatedOnDateForLabel(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.PREGNANCY_STATUS_BEAN:
                        if (pregnancyStatusBeanDao.countOf() > 0) {
                            List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LAST_UPDATED_PREGNANCY_STATUS);
                            if (versionBeans != null && !versionBeans.isEmpty()) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForPregnancyStatus(Long.valueOf(versionBeans.get(0).getValue()));
                            }
                        }
                        break;

                    case SyncConstant.DATA_QUALITY_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateDateForDataQualityValue(0L);
                        if (dataQualityBeanDao.countOf() > 0) {
                            String string = dataQualityBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = dataQualityBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForDataQualityValue(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.MEMBER_CBAC_DETAIL_BEAN:
                        loginRequestParamDetailDataBean.setLastUpdateDateForCbacDetails(0L);
                        if (memberCbacDetailBeanDao.countOf() > 0) {
                            String string = memberCbacDetailBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = memberCbacDetailBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForCbacDetails(sdf.parse(result[0]).getTime());
                            }
                        }
                        break;

                    case SyncConstant.COVID_TRAVELLERS_INFO_BEAN:
                        if (covidTravellersInfoBeanDao.countOf() > 0) {
                            String string = covidTravellersInfoBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = covidTravellersInfoBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForSchoolMaster(String.valueOf(sdf.parse(result[0]).getTime()));
                            }
                        }
                        break;

                    case SyncConstant.USER_HEALTH_INFRA_BEAN:
                        if (userHealthInfraBeanDao.countOf() > 0) {
                            String string = userHealthInfraBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                            String[] result = userHealthInfraBeanDao.queryRaw(string).getFirstResult();
                            if (result != null && result[0] != null) {
                                loginRequestParamDetailDataBean.setLastUpdateDateForUserHealthInfra(Objects.requireNonNull(sdf.parse(result[0])).getTime());
                            }
                        }
                        break;
                    default:
                }
            }

            int value = 0;
            List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.MOBILE_FORM_VERSION);
            if (versionBeans != null && !versionBeans.isEmpty()) {
                value = Integer.parseInt(versionBeans.get(0).getVersion());
            }
            loginRequestParamDetailDataBean.setMobileFormVersion(value);

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
            NETWORK_MESSAGE = SewaConstants.SQL_EXCEPTION;
            sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_SQL);
        } catch (Exception e) {
            NETWORK_MESSAGE = SewaConstants.EXCEPTION_FETCHING_DATA_FOR_USER;
            Log.e(TAG, e.getMessage(), e);
        }
        return loginRequestParamDetailDataBean;
    }

    private void storeSyncData(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean, LoggedInUserPrincipleDataBean loggedInUserPrincipleDataBean) {
        try {
            //Delete Families
            if (loggedInUserPrincipleDataBean.getDeletedFamilies() != null && !loggedInUserPrincipleDataBean.getDeletedFamilies().isEmpty()) {
                Log.i(TAG, LabelConstants.DELETE_FAMILIES_COUNT + loggedInUserPrincipleDataBean.getDeletedFamilies().size());
                this.deleteFamiliesFromDatabaseFHW(loggedInUserPrincipleDataBean.getDeletedFamilies());
            }

            //For storeAllAssignedFamilies
            if (loggedInUserPrincipleDataBean.getAssignedFamilies() != null && !loggedInUserPrincipleDataBean.getAssignedFamilies().isEmpty()) {
                Log.i(TAG, LabelConstants.ASSIGNED_FAMILIES_COUNT + loggedInUserPrincipleDataBean.getAssignedFamilies().size());
                this.storeAllAssignedFamilies(loggedInUserPrincipleDataBean.getAssignedFamilies());
            }

            //For storeUpdatedFamilyData
            boolean updateVersionBeanForFamilyData = true;
            if (loggedInUserPrincipleDataBean.getUpdatedFamilyByDate() != null && !loggedInUserPrincipleDataBean.getUpdatedFamilyByDate().isEmpty()) {
                Log.i(TAG, LabelConstants.UPDATED_FAMILIES_COUNT + loggedInUserPrincipleDataBean.getUpdatedFamilyByDate().size());
                updateVersionBeanForFamilyData = this.storeUpdatedFamilyData(loggedInUserPrincipleDataBean.getUpdatedFamilyByDate());
            }

            if (loggedInUserPrincipleDataBean.getLocationsForFamilyDataDeletion() != null && !loggedInUserPrincipleDataBean.getLocationsForFamilyDataDeletion().isEmpty()
                    && updateVersionBeanForFamilyData) {
                Log.i(TAG, LabelConstants.DELETE_FAMILIES_BY_LOCATION_COUNT + loggedInUserPrincipleDataBean.getLocationsForFamilyDataDeletion().size());
                this.deleteFamiliesByLocation(loggedInUserPrincipleDataBean.getLocationsForFamilyDataDeletion());
            }

            //For storeFhwServiceDetailBeans
            if (loggedInUserPrincipleDataBean.getFhwServiceStatusDtos() != null && !loggedInUserPrincipleDataBean.getFhwServiceStatusDtos().isEmpty()) {
                Log.i(TAG, LabelConstants.FHW_SERVICE_STATUS_COUNT + loggedInUserPrincipleDataBean.getFhwServiceStatusDtos().size());
                this.storeFhwServiceDetailBeans(loggedInUserPrincipleDataBean.getFhwServiceStatusDtos());
            } else {
                TableUtils.clearTable(fhwServiceDetailBeanDao.getConnectionSource(), FhwServiceDetailBean.class);
            }

            //For retrieveOrphanedOrReverificationFamiliesForFHS
            if (loggedInUserPrincipleDataBean.getOrphanedAndReverificationFamilies() != null && !loggedInUserPrincipleDataBean.getOrphanedAndReverificationFamilies().isEmpty()) {
                Log.i(TAG, LabelConstants.ORPHANED_AND_REVERIFICATION_FAMILIES_COUNT + loggedInUserPrincipleDataBean.getOrphanedAndReverificationFamilies().size());
                this.storeOrphanedAndReverificationFamiliesForFHW(loggedInUserPrincipleDataBean.getOrphanedAndReverificationFamilies());
            }

            //For storeLocationBeansAssignedToUser
            if (loggedInUserPrincipleDataBean.getRetrievedVillageAndChildLocations() != null && !loggedInUserPrincipleDataBean.getRetrievedVillageAndChildLocations().isEmpty()) {
                Log.i(TAG, LabelConstants.ASSIGNED_LOCATIONS_AND_CHILD_LOCATIONS_COUNT + loggedInUserPrincipleDataBean.getRetrievedVillageAndChildLocations().size());
                this.storeLocationBeansAssignedToUser(loggedInUserPrincipleDataBean.getRetrievedVillageAndChildLocations());
            }

            if (loggedInUserPrincipleDataBean.getCsvCoordinates() != null) {
                this.storeLocationCoordinates(loggedInUserPrincipleDataBean.getCsvCoordinates());
            }

            //For storeNotificationsForUser
            if (loginRequestParamDetailDataBean.getLastUpdateDateForNotifications() != null) {
                if (loggedInUserPrincipleDataBean.getDeletedNotifications() != null && !loggedInUserPrincipleDataBean.getDeletedNotifications().isEmpty()) {
                    Log.i(TAG, LabelConstants.DELETED_NOTIFICATIONS_COUNT + loggedInUserPrincipleDataBean.getDeletedNotifications().size());
                    this.deleteNotificationsForUser(loggedInUserPrincipleDataBean.getDeletedNotifications());
                }
                if (loggedInUserPrincipleDataBean.getNotifications() != null && !loggedInUserPrincipleDataBean.getNotifications().isEmpty()) {
                    Log.i(TAG, LabelConstants.NOTIFICATIONS_COUNT + loggedInUserPrincipleDataBean.getNotifications().size());
                    this.storeNotificationsForUser(loggedInUserPrincipleDataBean.getNotifications(), new Date(loginRequestParamDetailDataBean.getLastUpdateDateForNotifications()));
                }
            } else {
                if (loggedInUserPrincipleDataBean.getNotifications() != null && !loggedInUserPrincipleDataBean.getNotifications().isEmpty()) {
                    Log.i(TAG, LabelConstants.NOTIFICATIONS_COUNT + loggedInUserPrincipleDataBean.getNotifications().size());
                    this.storeNotificationsForUser(loggedInUserPrincipleDataBean.getNotifications(), null);
                } else {
                    TableUtils.clearTable(notificationBeanDao.getConnectionSource(), NotificationBean.class);
                }
            }

            //For retrieving Village Profile
            if (loggedInUserPrincipleDataBean.getRchVillageProfileDataBeans() != null && !loggedInUserPrincipleDataBean.getRchVillageProfileDataBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.RCH_VILLAGE_PROFILES_COUNT + loggedInUserPrincipleDataBean.getRchVillageProfileDataBeans().size());
                this.storeRchVillageProfileBeanFromServer(loggedInUserPrincipleDataBean.getRchVillageProfileDataBeans());
            }

            //For storing MigratedMemberData
            if (loggedInUserPrincipleDataBean.getMigratedMembersDataBeans() != null && !loggedInUserPrincipleDataBean.getMigratedMembersDataBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.MIGRATED_MEMBERS_COUNT + loggedInUserPrincipleDataBean.getMigratedMembersDataBeans().size());
                this.storeMigrationDetails(loggedInUserPrincipleDataBean.getMigratedMembersDataBeans());
            }

            //For storing MigratedFamilyData
            if (loggedInUserPrincipleDataBean.getMigratedFamilyDataBeans() != null && !loggedInUserPrincipleDataBean.getMigratedFamilyDataBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.MIGRATED_FAMILY_COUNT + loggedInUserPrincipleDataBean.getMigratedFamilyDataBeans().size());
                this.storeMigrationFamilyDetails(loggedInUserPrincipleDataBean.getMigratedFamilyDataBeans());
            }

            //For storeLabelsDetailsToDatabase
            if (loggedInUserPrincipleDataBean.getRetrievedLabels() != null) {
                Log.i(TAG, LabelConstants.LABELS_COUNT + loggedInUserPrincipleDataBean.getRetrievedLabels().getResult().size());
                this.storeLabelsDetailsToDatabase(loggedInUserPrincipleDataBean.getRetrievedLabels());
            }

            //For storeXlsDataToDatabaseForFHW
            if (loggedInUserPrincipleDataBean.getRetrievedXlsData() != null && !loggedInUserPrincipleDataBean.getRetrievedXlsData().isEmpty()) {
                Log.i(TAG, LabelConstants.XLS_DATA_COUNT + loggedInUserPrincipleDataBean.getRetrievedXlsData().size());
                this.storeXlsDataToDatabase(loggedInUserPrincipleDataBean.getRetrievedXlsData(), loggedInUserPrincipleDataBean.getCurrentMobileFormVersion());
            }

            //Storing List Values
            if (loggedInUserPrincipleDataBean.getRetrievedListValues() != null && !loggedInUserPrincipleDataBean.getRetrievedListValues().isEmpty()) {
                Log.i(TAG, LabelConstants.LIST_VALUES_COUNT + loggedInUserPrincipleDataBean.getRetrievedListValues().size());
                this.storeAllListValues(loggedInUserPrincipleDataBean.getRetrievedListValues());
            }

            //Storing Data Quality Values
            if (loggedInUserPrincipleDataBean.getDataQualityBeans() != null && !loggedInUserPrincipleDataBean.getDataQualityBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.DATA_QUALITY_VALUES_COUNT + loggedInUserPrincipleDataBean.getDataQualityBeans().size());
                this.storeAllDataQualityValues(loggedInUserPrincipleDataBean.getDataQualityBeans());
            }

            //Storing Pregnancy Status
            this.storePregnancyStatusBeans(loggedInUserPrincipleDataBean);

            //For retrieving Announcements
            if (loggedInUserPrincipleDataBean.getRetrievedAnnouncements() != null && !loggedInUserPrincipleDataBean.getRetrievedAnnouncements().isEmpty()) {
                Log.i(TAG, LabelConstants.ANNOUNCEMENTS_COUNT + loggedInUserPrincipleDataBean.getRetrievedAnnouncements().size());
                this.storeAllAnnouncement(loggedInUserPrincipleDataBean.getRetrievedAnnouncements());
            } else {
                downloadAnnouncementFileFromServer();
            }

            boolean value = false;
            if(loggedInUserPrincipleDataBean.getFeatures().contains("IS_NON_GOV"))
                value = true;
            try {
                List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, "IS_NON_GOV_VAR");
                if (versionBeans != null && !versionBeans.isEmpty()) {
                    versionBeans.get(0).setValue(value+"");
                    versionBeanDao.update(versionBeans.get(0));
                }else {
                    VersionBean versionBean = new VersionBean();
                    versionBean.setKey("IS_NON_GOV_VAR");
                    versionBean.setValue(value+"");
                    versionBeanDao.create(versionBean);
                }
            }catch (SQLException e){

            }

            if (loggedInUserPrincipleDataBean.getFeatures() != null && !loggedInUserPrincipleDataBean.getFeatures().isEmpty()) {
                List<VersionBean> featureVersionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_FEATURES_LIST);
                VersionBean versionBean;
                if (featureVersionBeans == null || featureVersionBeans.isEmpty()) {
                    VersionBean versionBean1 = new VersionBean();
                    versionBean1.setKey(GlobalTypes.VERSION_FEATURES_LIST);
                    versionBean1.setValue(UtilBean.stringListJoin(loggedInUserPrincipleDataBean.getFeatures(), ","));
                    versionBeanDao.create(versionBean1);
                } else {
                    versionBean = featureVersionBeans.get(0);
                    versionBean.setValue(UtilBean.stringListJoin(loggedInUserPrincipleDataBean.getFeatures(), ","));
                    versionBeanDao.update(versionBean);
                }
            } else {
                DeleteBuilder<VersionBean, Integer> deleteBuilder = versionBeanDao.deleteBuilder();
                deleteBuilder.where().eq(FieldNameConstants.KEY, GlobalTypes.VERSION_FEATURES_LIST);
                deleteBuilder.delete();
            }

            // Storing CBAC Details
            if (loggedInUserPrincipleDataBean.getMemberCbacDetails() != null && !loggedInUserPrincipleDataBean.getMemberCbacDetails().isEmpty()) {
                Log.i(TAG, LabelConstants.CBAC_DETAILS_COUNT + loggedInUserPrincipleDataBean.getMemberCbacDetails().size());
                this.storeCbacDetailsToDatabaseForASHA(loggedInUserPrincipleDataBean.getMemberCbacDetails());
            }

            //For Storing Library Beans
            if (loggedInUserPrincipleDataBean.getMobileLibraryDataBeans() != null && !loggedInUserPrincipleDataBean.getMobileLibraryDataBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.LIBRARY_COUNT + loggedInUserPrincipleDataBean.getMobileLibraryDataBeans().size());
                this.storeLibraryDataBeans(loggedInUserPrincipleDataBean.getMobileLibraryDataBeans());
            }

            //For storing location master information (Whole state!)
            if (loginRequestParamDetailDataBean.getLastUpdateDateForLocation() == null) {
                Log.i(TAG, LabelConstants.ALL_LOCATION_MASTER_COUNT + loggedInUserPrincipleDataBean.getLocationMasterBeans().size());
                TableUtils.clearTable(locationMasterBeanDao.getConnectionSource(), LocationMasterBean.class);
                this.storeLocationMasterBeans(loggedInUserPrincipleDataBean.getLocationMasterBeans(), true);
            } else {
                this.storeLocationMasterBeans(loggedInUserPrincipleDataBean.getLocationMasterBeans(), false);
            }

            //For storing health infrastructure information (Whole state!)
            if (loginRequestParamDetailDataBean.getLastUpdateDateForHealthInfrastructure() == null) {
                TableUtils.clearTable(healthInfrastructureBeanDao.getConnectionSource(), HealthInfrastructureBean.class);
                Log.i(TAG, LabelConstants.ALL_HEALTH_INFRASTRUCTURES_COUNT + loggedInUserPrincipleDataBean.getHealthInfrastructures().size());
                this.storeHealthInfrastructureDetails(loggedInUserPrincipleDataBean.getHealthInfrastructures(), true);
            } else {
                Log.i(TAG, LabelConstants.HEALTH_INFRASTRUCTURES_COUNT + loggedInUserPrincipleDataBean.getHealthInfrastructures().size());
                this.storeHealthInfrastructureDetails(loggedInUserPrincipleDataBean.getHealthInfrastructures(), false);
            }

            //For storing Schools information (Whole state!)
            if (loginRequestParamDetailDataBean.getLastUpdateDateForSchoolMaster() == null) {
                TableUtils.clearTable(schoolBeanDao.getConnectionSource(), SchoolBean.class);
                Log.i(TAG, LabelConstants.ALL_SCHOOLS_COUNT + loggedInUserPrincipleDataBean.getSchools().size());
                this.storeSchoolDetails(loggedInUserPrincipleDataBean.getSchools(), true);
            } else {
                Log.i(TAG, LabelConstants.SCHOOLS_COUNT + loggedInUserPrincipleDataBean.getSchools().size());
                this.storeSchoolDetails(loggedInUserPrincipleDataBean.getSchools(), false);
            }

            //Storing covid travellers info
            if (loggedInUserPrincipleDataBean.getCovidTravellersInfos() != null && !loggedInUserPrincipleDataBean.getCovidTravellersInfos().isEmpty()) {
                Log.i(TAG, LabelConstants.COVID_TRAVELLERS_INFO_COUNT + loggedInUserPrincipleDataBean.getCovidTravellersInfos().size());
                this.storeCovidTravellersInfo(loggedInUserPrincipleDataBean.getCovidTravellersInfos(), loginRequestParamDetailDataBean.getLastUpdateDateForCovidTravellersInfo());
            }

            //For storing location types
            this.storeLocationTypeBeans(loggedInUserPrincipleDataBean.getLocationTypeMasters(), loginRequestParamDetailDataBean.getLastUpdateDateForLocationTypeMaster());

            //For storing menu information
            this.storeMenuDetails(loggedInUserPrincipleDataBean.getMobileMenus());

            //For Storing LMS Courses
            if (loggedInUserPrincipleDataBean.getCourseDataBeans() != null && !loggedInUserPrincipleDataBean.getCourseDataBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.LMS_COURSES_COUNT + loggedInUserPrincipleDataBean.getCourseDataBeans().size());
                this.storeLmsCourses(loggedInUserPrincipleDataBean.getCourseDataBeans());
            }

            //For Storing LMS User Meta Data
            if (loggedInUserPrincipleDataBean.getLmsUserMetaData() != null && !loggedInUserPrincipleDataBean.getLmsUserMetaData().isEmpty()) {
                Log.i(TAG, LabelConstants.LMS_USER_META_DATA_COUNT + loggedInUserPrincipleDataBean.getLmsUserMetaData().size());
                this.storeLmsUserMetaData(loggedInUserPrincipleDataBean.getLmsUserMetaData());
            }

            //For Storing Mo Confirmed Members
            if (loggedInUserPrincipleDataBean.getMoConfirmedDataBeans() != null && !loggedInUserPrincipleDataBean.getMoConfirmedDataBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.MO_CONFIRMED_COUNT + loggedInUserPrincipleDataBean.getMoConfirmedDataBeans().size());
                this.storeMoConfirmedDetails(loggedInUserPrincipleDataBean.getMoConfirmedDataBeans());
            }

            //For Storing drug inventory
            if (loggedInUserPrincipleDataBean.getDrugInventoryBeans() != null && !loggedInUserPrincipleDataBean.getDrugInventoryBeans().isEmpty()) {
                Log.i(TAG, LabelConstants.DRUG_INVENTORY_COUNT + loggedInUserPrincipleDataBean.getDrugInventoryBeans().size());
                this.storeDrugInventoryDetails(loggedInUserPrincipleDataBean.getDrugInventoryBeans());
            }

            //Storing user Health Infra
            if (loggedInUserPrincipleDataBean.getUserHealthInfrastructures() != null && !loggedInUserPrincipleDataBean.getUserHealthInfrastructures().isEmpty()) {
                Log.i(TAG, LabelConstants.USER_HEALTH_INFRA_COUNT + loggedInUserPrincipleDataBean.getUserHealthInfrastructures().size());
                this.storeUserInfraInfo(loggedInUserPrincipleDataBean.getUserHealthInfrastructures(), loginRequestParamDetailDataBean.getLastUpdateDateForUserHealthInfra());
            }

            //LMS File Download Url
            if (loggedInUserPrincipleDataBean.getLmsFileDownloadUrl() != null && !loggedInUserPrincipleDataBean.getLmsFileDownloadUrl().isEmpty()) {
                List<VersionBean> lmsFileDownloadUrlVersionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LMS_FILE_DOWNLOAD_URL);
                VersionBean versionBean;
                if (lmsFileDownloadUrlVersionBeans == null || lmsFileDownloadUrlVersionBeans.isEmpty()) {
                    VersionBean lmsFileDownloadUrlVersionBean = new VersionBean();
                    lmsFileDownloadUrlVersionBean.setKey(GlobalTypes.VERSION_LMS_FILE_DOWNLOAD_URL);
                    lmsFileDownloadUrlVersionBean.setValue(loggedInUserPrincipleDataBean.getLmsFileDownloadUrl());
                    versionBeanDao.create(lmsFileDownloadUrlVersionBean);
                } else {
                    versionBean = lmsFileDownloadUrlVersionBeans.get(0);
                    versionBean.setValue(loggedInUserPrincipleDataBean.getLmsFileDownloadUrl());
                    versionBeanDao.update(versionBean);
                }
            } else {
                DeleteBuilder<VersionBean, Integer> deleteBuilder = versionBeanDao.deleteBuilder();
                deleteBuilder.where().eq(FieldNameConstants.KEY, GlobalTypes.VERSION_LMS_FILE_DOWNLOAD_URL);
                deleteBuilder.delete();
            }


            if (loggedInUserPrincipleDataBean.getSystemConfigurations() != null && !loggedInUserPrincipleDataBean.getSystemConfigurations().isEmpty()) {
                List<VersionBean> screenStatusVersionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_SCREENING_STATUS_RED);
                VersionBean versionBean;
                if (screenStatusVersionBeans == null || screenStatusVersionBeans.isEmpty()) {
                    VersionBean screenStatusVersionBean = new VersionBean();
                    screenStatusVersionBean.setKey(GlobalTypes.VERSION_SCREENING_STATUS_RED);
                    screenStatusVersionBean.setValue(loggedInUserPrincipleDataBean.getSystemConfigurations().get(GlobalTypes.VERSION_SCREENING_STATUS_RED));
                    versionBeanDao.create(screenStatusVersionBean);
                } else {
                    versionBean = screenStatusVersionBeans.get(0);
                    versionBean.setValue(loggedInUserPrincipleDataBean.getSystemConfigurations().get(GlobalTypes.VERSION_SCREENING_STATUS_RED));
                    versionBeanDao.update(versionBean);
                }
            } else {
                DeleteBuilder<VersionBean, Integer> deleteBuilder = versionBeanDao.deleteBuilder();
                deleteBuilder.where().eq(FieldNameConstants.KEY, GlobalTypes.VERSION_SCREENING_STATUS_RED);
                deleteBuilder.delete();
            }

            if (loggedInUserPrincipleDataBean.getSystemConfigurations() != null && !loggedInUserPrincipleDataBean.getSystemConfigurations().isEmpty()) {
                List<VersionBean> screenStatusVersionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_SCREENING_STATUS_YELLOW);
                VersionBean versionBean;
                if (screenStatusVersionBeans == null || screenStatusVersionBeans.isEmpty()) {
                    VersionBean screenStatusVersionBean = new VersionBean();
                    screenStatusVersionBean.setKey(GlobalTypes.VERSION_SCREENING_STATUS_YELLOW);
                    screenStatusVersionBean.setValue(loggedInUserPrincipleDataBean.getSystemConfigurations().get(GlobalTypes.VERSION_SCREENING_STATUS_YELLOW));
                    versionBeanDao.create(screenStatusVersionBean);
                } else {
                    versionBean = screenStatusVersionBeans.get(0);
                    versionBean.setValue(loggedInUserPrincipleDataBean.getSystemConfigurations().get(GlobalTypes.VERSION_SCREENING_STATUS_YELLOW));
                    versionBeanDao.update(versionBean);
                }
            } else {
                DeleteBuilder<VersionBean, Integer> deleteBuilder = versionBeanDao.deleteBuilder();
                deleteBuilder.where().eq(FieldNameConstants.KEY, GlobalTypes.VERSION_SCREENING_STATUS_YELLOW);
                deleteBuilder.delete();
            }

            if (loggedInUserPrincipleDataBean.getSystemConfigurations() != null && !loggedInUserPrincipleDataBean.getSystemConfigurations().isEmpty()) {
                List<VersionBean> screenStatusVersionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_SCREENING_STATUS_GREEN);
                VersionBean versionBean;
                if (screenStatusVersionBeans == null || screenStatusVersionBeans.isEmpty()) {
                    VersionBean screenStatusVersionBean = new VersionBean();
                    screenStatusVersionBean.setKey(GlobalTypes.VERSION_SCREENING_STATUS_GREEN);
                    screenStatusVersionBean.setValue(loggedInUserPrincipleDataBean.getSystemConfigurations().get(GlobalTypes.VERSION_SCREENING_STATUS_GREEN));
                    versionBeanDao.create(screenStatusVersionBean);
                } else {
                    versionBean = screenStatusVersionBeans.get(0);
                    versionBean.setValue(loggedInUserPrincipleDataBean.getSystemConfigurations().get(GlobalTypes.VERSION_SCREENING_STATUS_GREEN));
                    versionBeanDao.update(versionBean);
                }
            } else {
                DeleteBuilder<VersionBean, Integer> deleteBuilder = versionBeanDao.deleteBuilder();
                deleteBuilder.where().eq(FieldNameConstants.KEY, GlobalTypes.VERSION_SCREENING_STATUS_GREEN);
                deleteBuilder.delete();
            }

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.SQL_EXCEPTION;
            SharedStructureData.sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_SQL);
        } catch (Exception e) {
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_FETCHING_DATA_FOR_USER;
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void deleteFamiliesFromDatabaseFHW(List<String> deletedFamilyIds) {
        for (String familyId : deletedFamilyIds) {
            DeleteBuilder<FamilyBean, Integer> familyBeanDeleteBuilder = familyBeanDao.deleteBuilder();
            DeleteBuilder<MemberBean, Integer> memberBeanDeleteBuilder = memberBeanDao.deleteBuilder();

            try {
                familyBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyId);
                familyBeanDeleteBuilder.delete();
                memberBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyId);
                memberBeanDeleteBuilder.delete();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void storeAllAssignedFamilies(List<FamilyDataBean> familyDataBeans) {
        List<FamilyBean> familyBeansToCreate = new ArrayList<>();
        List<MemberBean> memberBeansToCreate = new ArrayList<>();
        int pendingAbhaCount = 0;
        try {
            TableUtils.clearTable(memberBeanDao.getConnectionSource(), MemberBean.class);
            TableUtils.clearTable(familyBeanDao.getConnectionSource(), FamilyBean.class);
            for (FamilyDataBean familyDataBean : familyDataBeans) {
                List<MemberDataBean> memberDataBeans = familyDataBean.getMembers();
                if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
                    for (MemberDataBean memberDataBean : memberDataBeans) {
                        memberBeansToCreate.add(new MemberBean(memberDataBean));
                        if (memberDataBean.getHealthIdNumber() == null && !FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES.contains(memberDataBean.getState())) {
                            pendingAbhaCount++;
                        }
                    }
                }
                familyDataBean.setPendingAbhaCount(pendingAbhaCount);
                pendingAbhaCount = 0;
                FamilyBean familyBean = new FamilyBean(familyDataBean);
                familyBean.setAssignedTo(SewaTransformer.loginBean.getUserID());
                familyBeansToCreate.add(familyBean);
            }
            memberBeanDao.create(memberBeansToCreate);
            familyBeanDao.create(familyBeansToCreate);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
    }

    private void deleteFamiliesByLocation(List<Long> locationIds) {
        try {
            List<String> familyIds = new LinkedList<>();
            List<FamilyBean> familyBeanList = familyBeanDao.queryBuilder()
                    .selectColumns(FieldNameConstants.FAMILY_ID).where()
                    .in(FieldNameConstants.LOCATION_ID, locationIds).query();

            for (FamilyBean familyBean : familyBeanList) {
                familyIds.add(familyBean.getFamilyId());
            }

            DeleteBuilder<MemberBean, Integer> memberBeanIntegerDeleteBuilder = memberBeanDao.deleteBuilder();
            memberBeanIntegerDeleteBuilder.where().in(FieldNameConstants.FAMILY_ID, familyIds);
            memberBeanIntegerDeleteBuilder.delete();

            DeleteBuilder<FamilyBean, Integer> familyBeanIntegerDeleteBuilder = familyBeanDao.deleteBuilder();
            familyBeanIntegerDeleteBuilder.where().in(FieldNameConstants.FAMILY_ID, familyIds);
            familyBeanIntegerDeleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private boolean storeUpdatedFamilyData(List<FamilyDataBean> familyDataBeans) {
        List<FamilyBean> familyBeansToCreate = new ArrayList<>();
        List<MemberBean> memberBeansToCreate = new ArrayList<>();
        int pendingAbhaCount = 0;
        try {
            for (FamilyDataBean familyDataBean : familyDataBeans) {
                DeleteBuilder<FamilyBean, Integer> familyBeanDeleteBuilder = familyBeanDao.deleteBuilder();
                DeleteBuilder<MemberBean, Integer> memberBeanDeleteBuilder = memberBeanDao.deleteBuilder();

                familyBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyDataBean.getFamilyId());
                familyBeanDeleteBuilder.delete();
                memberBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyDataBean.getFamilyId());
                memberBeanDeleteBuilder.delete();

                List<MemberDataBean> memberDataBeans = familyDataBean.getMembers();

                if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
                    for (MemberDataBean memberDataBean : memberDataBeans) {
                        memberBeansToCreate.add(new MemberBean(memberDataBean));
                        if (memberDataBean.getHealthIdNumber() == null && !FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES.contains(memberDataBean.getState())) {
                            pendingAbhaCount++;
                        }
                    }
                }
                familyDataBean.setPendingAbhaCount(pendingAbhaCount);
                pendingAbhaCount = 0;

                FamilyBean familyBean = new FamilyBean(familyDataBean);
                familyBean.setAssignedTo(SewaTransformer.loginBean.getUserID());
                familyBeansToCreate.add(familyBean);
            }
            memberBeanDao.create(memberBeansToCreate);
            familyBeanDao.create(familyBeansToCreate);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }

    private void storeFhwServiceDetailBeans(List<FhwServiceDetailBean> fhwServiceDetailBeans) {
        try {
            TableUtils.clearTable(fhwServiceDetailBeanDao.getConnectionSource(), FhwServiceDetailBean.class);
            for (FhwServiceDetailBean fhwServiceDetailBean : fhwServiceDetailBeans) {
                fhwServiceDetailBeanDao.create(fhwServiceDetailBean);
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQL Exception While Inserting FHW Service Details", e);
        }
    }

    private void storeOrphanedAndReverificationFamiliesForFHW(List<FamilyDataBean> orphanedOrReverificationFamilies) {
        List<FamilyDataBean> orphanedFamilies = new ArrayList<>();
        List<FamilyDataBean> reverificationFamilies = new ArrayList<>();
        if (orphanedOrReverificationFamilies == null || orphanedOrReverificationFamilies.isEmpty()) {
            return;
        }

        for (FamilyDataBean familyDataBean : orphanedOrReverificationFamilies) {
            if (familyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_ORPHAN)) {
                orphanedFamilies.add(familyDataBean);
            } else {
                reverificationFamilies.add(familyDataBean);
            }
        }
        try {
            for (FamilyDataBean familyDataBean : orphanedFamilies) {
                DeleteBuilder<FamilyBean, Integer> familyBeanDeleteBuilder = familyBeanDao.deleteBuilder();
                DeleteBuilder<MemberBean, Integer> memberBeanDeleteBuilder = memberBeanDao.deleteBuilder();
                familyBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyDataBean.getFamilyId());
                familyBeanDeleteBuilder.delete();
                memberBeanDeleteBuilder.where().eq(FieldNameConstants.FAMILY_ID, familyDataBean.getFamilyId());
                memberBeanDeleteBuilder.delete();

                List<MemberDataBean> memberDataBeans = familyDataBean.getMembers();
                if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
                    for (MemberDataBean memberDataBean : memberDataBeans) {
                        memberBeanDao.create(new MemberBean(memberDataBean));
                    }
                }
                FamilyBean familyBean = new FamilyBean(familyDataBean);
                familyBean.setAssignedTo(SewaTransformer.loginBean.getUserID());
                familyBeanDao.create(familyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            SharedStructureData.NETWORK_MESSAGE = "Issue During Inserting Orphaned Family FHS Data";
        }

        try {
            for (FamilyDataBean familyDataBean : reverificationFamilies) {
                List<FamilyBean> query = familyBeanDao.queryBuilder().where().eq(FieldNameConstants.FAMILY_ID, familyDataBean.getFamilyId()).query();
                if (query != null && !query.isEmpty()) {
                    for (FamilyBean familyBean : query) {
                        familyBeanDao.delete(familyBean);
                        List<MemberBean> members = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.FAMILY_ID, familyBean.getFamilyId()).query();
                        if (members != null && !members.isEmpty()) {
                            for (MemberBean memberBean : members) {
                                memberBeanDao.delete(memberBean);
                            }
                        }

                    }
                }

                List<MemberDataBean> memberDataBeans = familyDataBean.getMembers();
                if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
                    for (MemberDataBean memberDataBean : memberDataBeans) {
                        memberBeanDao.create(new MemberBean(memberDataBean));
                    }
                }
                FamilyBean familyBean = new FamilyBean(familyDataBean);
                familyBean.setReverificationFlag(Boolean.TRUE);
                familyBean.setAssignedTo(SewaTransformer.loginBean.getUserID());
                familyBeanDao.create(familyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            SharedStructureData.NETWORK_MESSAGE = "Issue During Inserting Orphaned Family FHS Data";
        }
    }

    private void storeLocationBeansAssignedToUser(Map<Long, List<SurveyLocationMobDataBean>> allSurveyLocation) {
        try {
            TableUtils.clearTable(locationBeanDao.getConnectionSource(), LocationBean.class);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }
        try {
            if (allSurveyLocation != null) {
                for (Map.Entry<Long, List<SurveyLocationMobDataBean>> entrySet : allSurveyLocation.entrySet()) {
                    List<SurveyLocationMobDataBean> locMobDataBeans = entrySet.getValue();
                    List<LocationBean> locationModels = sewaTransformer.convertLocationDataBeanToLocationModel(locMobDataBeans, null);
                    for (LocationBean loc : locationModels) {
                        locationBeanDao.createOrUpdate(loc);
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeLocationCoordinates(QueryMobDataBean queryDataFromServer) throws SQLException {
        TableUtils.clearTable(locationCoordinatesBeansDao.getConnectionSource(), LocationCoordinatesBean.class);
        List<LinkedHashMap<String, Object>> result = queryDataFromServer.getResult();

        if (result.isEmpty()) {
            return;
        }

        Object lgdCode;
        Object coordinates;
        List<LocationCoordinatesBean> beansToBeCreated = new LinkedList<>();
        for (LinkedHashMap<String, Object> aRow : result) {
            lgdCode = aRow.get("lgd_code");
            coordinates = aRow.get("coordinates_csv");
            if (lgdCode != null && coordinates != null) {
                LocationCoordinatesBean locationCoordinatesBean = new LocationCoordinatesBean();
                locationCoordinatesBean.setLgdCode(lgdCode.toString());
                locationCoordinatesBean.setCoordinates(coordinates.toString());
                beansToBeCreated.add(locationCoordinatesBean);
            }
        }
        locationCoordinatesBeansDao.create(beansToBeCreated);
    }

    private void storeNotificationsForUser(List<NotificationMobDataBean> notificationMobDataBeans, Date lastModifiedOn) throws SQLException {
        if (lastModifiedOn != null) {
            if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
                List<Long> notificationIds = new ArrayList<>();
                for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
                    notificationIds.add(notificationMobDataBean.getId());
                }

                DeleteBuilder<NotificationBean, Integer> notificationBeanDeleteBuilder = notificationBeanDao.deleteBuilder();
                notificationBeanDeleteBuilder.where().in(FieldNameConstants.NOTIFICATION_ID, notificationIds);
                notificationBeanDeleteBuilder.delete();

                List<NotificationBean> notificationBeans = new LinkedList<>();
                notificationBeans = SewaTransformer.getInstance().convertNotificationDataBeanToNotificationBeanModel(notificationBeans, notificationMobDataBeans);
                notificationBeanDao.create(notificationBeans);
            }
        } else {
            TableUtils.clearTable(notificationBeanDao.getConnectionSource(), NotificationBean.class);

            List<NotificationBean> notificationBeans = new LinkedList<>();
            if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
                notificationBeans = SewaTransformer.getInstance().convertNotificationDataBeanToNotificationBeanModel(notificationBeans, notificationMobDataBeans);
                notificationBeanDao.create(notificationBeans);
            }
        }
    }

    private void deleteNotificationsForUser(List<Long> notificationIds) throws SQLException {
        if (notificationIds != null && !notificationIds.isEmpty()) {
            DeleteBuilder<NotificationBean, Integer> notificationBeanDeleteBuilder = notificationBeanDao.deleteBuilder();
            notificationBeanDeleteBuilder.where().in(FieldNameConstants.NOTIFICATION_ID, notificationIds);
            notificationBeanDeleteBuilder.delete();
        }
    }

    private void storeRchVillageProfileBeanFromServer(List<RchVillageProfileDataBean> rchVillageProfileDataBeans) {
        try {
            List<RchVillageProfileBean> rchVillageProfileBeans = new ArrayList<>();
            for (RchVillageProfileDataBean rchVillageProfileDataBean : rchVillageProfileDataBeans) {
                RchVillageProfileBean rchVillageProfileBean = new RchVillageProfileBean();
                rchVillageProfileBean.setVillageId(rchVillageProfileDataBean.getVillage().getId());
                rchVillageProfileBean.setRchVillageProfileDto(new Gson().toJson(rchVillageProfileDataBean));
                rchVillageProfileBeans.add(rchVillageProfileBean);
            }
            if (!rchVillageProfileBeans.isEmpty()) {
                TableUtils.clearTable(rchVillageProfileBeanDao.getConnectionSource(), RchVillageProfileBean.class);
                rchVillageProfileBeanDao.create(rchVillageProfileBeans);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeMigrationDetails(List<MigratedMembersDataBean> migratedMembersDataBeans) {
        List<MigratedMembersBean> migratedMembersBeans = new ArrayList<>();

        for (MigratedMembersDataBean migratedMembersDataBean : migratedMembersDataBeans) {
            MigratedMembersBean migratedMembersBean = sewaTransformer.convertMigratedMembersDataBeanToBean(migratedMembersDataBean);
            migratedMembersBeans.add(migratedMembersBean);
        }

        try {
            TableUtils.clearTable(migratedMembersBeanDao.getConnectionSource(), MigratedMembersBean.class);
            migratedMembersBeanDao.create(migratedMembersBeans);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeMigrationFamilyDetails(List<MigratedFamilyDataBean> migratedFamilyDataBeans) {
        List<MigratedFamilyBean> migratedFamilyBeans = new ArrayList<>();

        for (MigratedFamilyDataBean migratedFamilyDataBean : migratedFamilyDataBeans) {
            MigratedFamilyBean migratedFamilyBean = sewaTransformer.convertMigratedFamilyDataBeanToBean(migratedFamilyDataBean);
            migratedFamilyBeans.add(migratedFamilyBean);
        }

        try {
            TableUtils.clearTable(migratedFamilyBeanDao.getConnectionSource(), MigratedFamilyBean.class);
            migratedFamilyBeanDao.create(migratedFamilyBeans);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeLabelsDetailsToDatabase(QueryMobDataBean queryMobDataBean) {

        List<LabelBean> labelBeans = sewaTransformer.convertQueryMobDataBeanToLabelModel(queryMobDataBean);
        for (LabelBean labelBean : labelBeans) {
            try {
                if (SharedStructureData.listLabelBeans != null) {
                    LabelBean get = SharedStructureData.listLabelBeans.get(labelBean.getMapIndex());
                    if (get != null) {
                        labelBean.setId(get.getId());
                        labelBeanDao.createOrUpdate(labelBean);
                    } else {
                        labelBeanDao.create(labelBean);
                    }
                } else {
                    labelBeanDao.create(labelBean);
                }
            } catch (SQLException ex) {
                Log.e(TAG, null, ex);
            }
        }
    }

    private void storeXlsDataToDatabase(Map<String, List<ComponentTagBean>> xlsDataMap, Integer currentMobileFormVersion) {
        boolean isFormUpdate = Boolean.TRUE;
        for (Map.Entry<String, List<ComponentTagBean>> entry : xlsDataMap.entrySet()) {
            String sheetName = entry.getKey();
            List<ComponentTagBean> componentTagBeans = entry.getValue();

            if (componentTagBeans != null && !componentTagBeans.isEmpty()) {
                List<AnswerBean> answerBeans;
                List<QuestionBean> questionBeans;
                questionBeans = sewaTransformer.convertComponentDataBeanToQuestionBeanModel(null, componentTagBeans, sheetName);
                answerBeans = sewaTransformer.convertComponentDataBeanToAnswerBeanModel(null, componentTagBeans, sheetName);

                try {
                    DeleteBuilder<QuestionBean, Integer> deleteQueBuilder = questionBeanDao.deleteBuilder();
                    deleteQueBuilder.where().eq(SewaConstants.QUESTION_BEAN_ENTITY, sheetName);
                    PreparedDelete<QuestionBean> prepare = deleteQueBuilder.prepare();
                    if (prepare != null) {
                        questionBeanDao.delete(prepare);
                    }
                    for (QuestionBean questionBean : questionBeans) {
                        questionBeanDao.createOrUpdate(questionBean);
                    }

                    DeleteBuilder<AnswerBean, Integer> deleteAnsBuilder = answerBeanDao.deleteBuilder();
                    deleteAnsBuilder.where().eq(SewaConstants.QUESTION_BEAN_ENTITY, sheetName);
                    PreparedDelete<AnswerBean> prepare1 = deleteAnsBuilder.prepare();
                    if (prepare1 != null) {
                        answerBeanDao.delete(prepare1);
                    }
                    for (AnswerBean answerBean : answerBeans) {
                        answerBeanDao.createOrUpdate(answerBean);
                    }
                } catch (SQLException ex) {
                    Log.d(TAG, "Error in download sheet : " + sheetName);
                    Log.e(TAG, null, ex);
                    isFormUpdate = Boolean.FALSE;
                }
            }
        }
        if (isFormUpdate) {
            changeVersionBeanVersionValueForGivenKey(GlobalTypes.MOBILE_FORM_VERSION, null, String.valueOf(currentMobileFormVersion));
        }
    }

    private void storeAllListValues(List<FieldValueMobDataBean> fieldValueMobDataBeans) {
        if (SewaTransformer.loginBean == null) {
            return;
        }

        if (fieldValueMobDataBeans != null && !fieldValueMobDataBeans.isEmpty()) {
            // remove state list first
            try {
                List<ListValueBean> listValueBeans = listValueBeanDao.queryForEq(SewaConstants.LIST_VALUE_BEAN_FIELD, "stateList");
                listValueBeanDao.delete(listValueBeans);
            } catch (SQLException ex) {
                Log.e(TAG, null, ex);
            }

            for (FieldValueMobDataBean fieldValueMobDataBean : fieldValueMobDataBeans) {
                ListValueBean listValueBean = null;
                try {
                    List<ListValueBean> listValueBeans = listValueBeanDao.queryForEq(SewaConstants.LIST_VALUE_BEAN_ID_OF_VALUE, fieldValueMobDataBean.getIdOfValue());
                    if (listValueBeans != null && !listValueBeans.isEmpty()) {
                        listValueBean = listValueBeans.get(0);

                        if (Boolean.FALSE.equals(fieldValueMobDataBean.getActive())) {
                            listValueBeanDao.delete(listValueBean);
                        }
                    }
                } catch (SQLException ex) {
                    Log.e(TAG, null, ex);
                }

                if (Boolean.TRUE.equals(fieldValueMobDataBean.getActive())) {
                    if (listValueBean == null) {
                        listValueBean = new ListValueBean();
                        listValueBean.setIdOfValue(fieldValueMobDataBean.getIdOfValue());
                        listValueBean.setFormCode(fieldValueMobDataBean.getFormCode());
                        listValueBean.setField(fieldValueMobDataBean.getField());
                        listValueBean.setFieldType(fieldValueMobDataBean.getFieldType());
                        listValueBean.setValue(fieldValueMobDataBean.getValue());
                        listValueBean.setModifiedOn(new Date(fieldValueMobDataBean.getLastUpdateOfFieldValue()));

                        if (listValueBean.getFieldType().equals(GlobalTypes.TEXTUAL_LIST_VALUE)) { // T for Text and M for Multimedia
                            listValueBean.setIsDownloaded(GlobalTypes.TRUE); // text is direct available
                        } else {
                            listValueBean.setIsDownloaded(GlobalTypes.FALSE); // is multimedia than is should be download
                        }
                        try {
                            listValueBeanDao.create(listValueBean);
                        } catch (SQLException ex) {
                            Log.e(TAG, null, ex);
                        }
                    } else {
                        listValueBean.setFormCode(fieldValueMobDataBean.getFormCode());
                        listValueBean.setField(fieldValueMobDataBean.getField());
                        listValueBean.setFieldType(fieldValueMobDataBean.getFieldType());
                        listValueBean.setValue(fieldValueMobDataBean.getValue());
                        listValueBean.setModifiedOn(new Date(fieldValueMobDataBean.getLastUpdateOfFieldValue()));
                        if (listValueBean.getFieldType().equalsIgnoreCase(GlobalTypes.MULTIMEDIA_LIST_VALUE)) {
                            String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + listValueBean.getValue();
                            if (!UtilBean.isFileExists(path)) {
                                listValueBean.setIsDownloaded(GlobalTypes.FALSE);
                            } else {
                                listValueBean.setIsDownloaded(GlobalTypes.TRUE);
                            }
                        } else {
                            listValueBean.setIsDownloaded(GlobalTypes.TRUE);
                        }

                        // do update if File not available
                        try {
                            listValueBeanDao.update(listValueBean);
                        } catch (SQLException ex) {
                            Log.e(TAG, null, ex);
                        }
                    }
                }
            }
        }// end of if constraints

        // generate parameter for query
        Map<String, Object> mapQueryFields = new HashMap<>();
        mapQueryFields.put(SewaConstants.LIST_VALUE_BEAN_FIELD_TYPE, GlobalTypes.MULTIMEDIA_LIST_VALUE);
        List<ListValueBean> listValueBeans = null;

        try {
            listValueBeans = listValueBeanDao.queryForFieldValues(mapQueryFields);
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        } // fire query and find not downloaded list

        if (listValueBeans != null && !listValueBeans.isEmpty()) {
            for (ListValueBean listValueBean : listValueBeans) {
                String filename = listValueBean.getValue();
                if (filename != null && filename.trim().length() > 0) {
                    String filePath = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + filename;
                    if (!UtilBean.isFileExists(filePath)) {
                        Log.i(TAG, "File Not Exists for List Value : " + filePath + " :: id :: " + listValueBean.getIdOfValue());
                        try {
                            if (this.downloadFileFromServer(listValueBean.getValue())) {
                                listValueBean.setIsDownloaded(GlobalTypes.TRUE);
                            } else {
                                listValueBean.setIsDownloaded(GlobalTypes.FALSE);
                            }
                            listValueBeanDao.update(listValueBean);
                        } catch (Exception ex) {
                            Log.e(TAG, null, ex);
                            break;
                        }
                    } else {
                        Log.i(TAG, "File Exists for List value : " + filePath);
                    }
                }
            }
        }
    }

    private void storeAllDataQualityValues(List<DataQualityBean> dataQualityDataBeans) {
        if (dataQualityDataBeans != null && !dataQualityDataBeans.isEmpty()) {
            try {
                List<Long> memberIds = new ArrayList<>();

                for (DataQualityBean dataQualityDataBean : dataQualityDataBeans) {
                    memberIds.add(dataQualityDataBean.getMemberId());
                }

                DeleteBuilder<DataQualityBean, Integer> dataQualityBeanDeleteBuilder = dataQualityBeanDao.deleteBuilder();
                dataQualityBeanDeleteBuilder.where().in(FieldNameConstants.MEMBER_ID, memberIds);
                dataQualityBeanDeleteBuilder.delete();

                dataQualityBeanDao.create(dataQualityDataBeans);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void storePregnancyStatusBeans(LoggedInUserPrincipleDataBean data) {
        try {
            if (data.getPregnancyStatus() != null && !data.getPregnancyStatus().isEmpty()) {
                TableUtils.clearTable(pregnancyStatusBeanDao.getConnectionSource(), MemberPregnancyStatusBean.class);
                pregnancyStatusBeanDao.create(data.getPregnancyStatus());

                VersionBean versionBean;
                List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LAST_UPDATED_PREGNANCY_STATUS);

                if (versionBeans == null || versionBeans.isEmpty()) {
                    VersionBean versionBean1 = new VersionBean();
                    versionBean1.setKey(GlobalTypes.VERSION_LAST_UPDATED_PREGNANCY_STATUS);
                    versionBean1.setValue(data.getLastPregnancyStatusDate().toString());
                    versionBeanDao.create(versionBean1);
                } else {
                    versionBean = versionBeans.get(0);
                    versionBean.setValue(data.getLastPregnancyStatusDate().toString());
                    versionBeanDao.update(versionBean);
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    private boolean downloadFileFromServer(String fileNameToDownLoad) {
        return true;
//        try {
//            if (fileNameToDownLoad != null) {
//                String token = SewaTransformer.loginBean.getUserToken();
//                try {
//                    token = URLEncoder.encode(token, "UTF-8");
//                    fileNameToDownLoad = URLEncoder.encode(fileNameToDownLoad, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                long startTime = System.currentTimeMillis();
//                String url = WSConstants.REST_TECHO_SERVICE_URL + "getFile?token=" + token + "&fileName=" + fileNameToDownLoad;
//                 ResponseBody responseBody = apiManager.execute(apiManager.getInstance().getFile(url));
//                if (responseBody == null)
//                    return false;
//                String filePath = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + fileNameToDownLoad;
//                try (FileOutputStream fos = new FileOutputStream(filePath)) {
//                    fos.write(responseBody.bytes());
//                }
//                long secOfDownload = ((System.currentTimeMillis() - startTime) / 1000);
//                Log.i(TAG, "Downloaded file : " + filePath + "\n in " + secOfDownload + "sec");
//                return true;
//            }
//            return false;
//        } catch (Exception ex) {
//            Log.e(TAG, null, ex);
//            return false;
//        }
    }

    private void storeAllAnnouncement(List<AnnouncementMobDataBean> announcementMobDataBeans) {
        for (AnnouncementMobDataBean announcementMobDataBean : announcementMobDataBeans) {
            AnnouncementBean announcementBean = null;
            try {
                List<AnnouncementBean> announcementBeans = announcementBeanDao.queryForEq(SewaConstants.ANNOUNCEMENT_BEAN_ANNOUNCEMENT_ID, announcementMobDataBean.getAnnouncementId());
                if (announcementBeans != null && !announcementBeans.isEmpty()) {
                    announcementBean = announcementBeans.get(0);
                }
            } catch (SQLException ex) {
                Log.e(TAG, null, ex);
            }

            if (announcementBean == null) {
                if (announcementMobDataBean.isIsActive()) {
                    announcementBean = new AnnouncementBean();
                    announcementBean.setAnnouncementId(announcementMobDataBean.getAnnouncementId());
                    announcementBean.setDefaultLanguage(announcementMobDataBean.getDefaultLanguage());
                    if (announcementMobDataBean.getSubject() != null) {
                        announcementBean.setSubject(announcementMobDataBean.getSubject().trim());
                    }
                    announcementBean.setPublishedOn(announcementMobDataBean.getPublishedOn());
                    announcementBean.setIsDownloaded(GlobalTypes.FALSE);
                    announcementBean.setIsPlayedAnnouncement(0);
                    announcementBean.setFileName(announcementMobDataBean.getFileName());
                    if (announcementMobDataBean.getModifiedOn() != null) {
                        announcementBean.setModifiedOn(announcementMobDataBean.getModifiedOn());
                    }
                    try {
                        announcementBeanDao.create(announcementBean);
                    } catch (SQLException ex) {
                        Log.e(TAG, null, ex);
                    }
                }
            } else {
                if (announcementMobDataBean.isIsActive()) {
                    announcementBean.setDefaultLanguage(announcementMobDataBean.getDefaultLanguage());
                    if (announcementMobDataBean.getSubject() != null) {
                        byte[] bytes = announcementMobDataBean.getSubject().getBytes();
                        String string = new String(bytes, StandardCharsets.UTF_8);
                        announcementBean.setSubject(string.trim());

                    }
                    announcementBean.setPublishedOn(announcementMobDataBean.getPublishedOn());
                    announcementBean.setIsDownloaded(GlobalTypes.FALSE);
                    announcementBean.setFileName(announcementMobDataBean.getFileName());
                    if (announcementMobDataBean.getModifiedOn() != null) {
                        announcementBean.setModifiedOn(announcementMobDataBean.getModifiedOn());
                    }
                    try {
                        announcementBeanDao.update(announcementBean);
                    } catch (SQLException ex) {
                        Log.e(TAG, null, ex);
                    }
                } else {
                    File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + announcementBean.getFileName());
                    if (file.exists()) {
                        file.delete();
                    }

                    try {
                        announcementBeanDao.delete(announcementBean);

                    } catch (SQLException ex) {
                        Log.e(TAG, null, ex);
                    }
                }
            }
        } // done all announcement
        downloadAnnouncementFileFromServer();
    }

    private void downloadAnnouncementFileFromServer() {
        List<AnnouncementBean> announcementBeans = null;
        try {
            announcementBeans = announcementBeanDao.queryForAll();
        } catch (SQLException ex) {
            Log.e(TAG, null, ex);
        }

        if (announcementBeans != null && !announcementBeans.isEmpty()) {
            for (AnnouncementBean announcementBean : announcementBeans) {
                String filename = announcementBean.getFileName();
                if (filename != null && filename.trim().length() > 0) {
                    try {
                        String file = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + "/" + filename;
                        if (!UtilBean.isFileExists(file)) {
                            this.downloadFileFromServer(filename);
                            announcementBean.setIsPlayedAnnouncement(0);
                        }
                        announcementBean.setIsDownloaded(GlobalTypes.TRUE);
                        announcementBeanDao.update(announcementBean);
                    } catch (Exception ex) {
                        Log.e(TAG, null, ex);
                    }
                }
            }
        }
    }

    private void storeCbacDetailsToDatabaseForASHA(List<MemberCbacDetailBean> memberCbacDetailBeans) {
        if (memberCbacDetailBeans != null && !memberCbacDetailBeans.isEmpty()) {
            try {
                List<Long> cbacDetailsIds = new ArrayList<>();
                for (MemberCbacDetailBean memberCbacDetailBean : memberCbacDetailBeans) {
                    cbacDetailsIds.add(memberCbacDetailBean.getActualId());
                }

                DeleteBuilder<MemberCbacDetailBean, Integer> memberCbacDetailBeanDeleteBuilder = memberCbacDetailBeanDao.deleteBuilder();
                memberCbacDetailBeanDeleteBuilder.where().in(FieldNameConstants.ACTUAL_I_D, cbacDetailsIds);
                memberCbacDetailBeanDeleteBuilder.delete();

                memberCbacDetailBeanDao.create(memberCbacDetailBeans);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void storeLibraryDataBeans(List<LibraryDataBean> libraryDataBeans) {
        try {
            for (LibraryDataBean libraryDataBean : libraryDataBeans) {
                LibraryBean bean = libraryBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.ACTUAL_ID, libraryDataBean.getActualId())
                        .queryForFirst();

                if (libraryDataBean.getState() != null && libraryDataBean.getState().equals("INACTIVE")) {
                    File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY), libraryDataBean.getFileName());
                    if (file.exists()) {
                        file.delete();
                    }
                    if (bean != null) {
                        libraryBeanDao.delete(bean);
                    }
                    continue;
                }

                if (bean == null) {
                    LibraryBean libraryBean = new LibraryBean(libraryDataBean);
                    libraryBeanDao.create(libraryBean);
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        libraryDownloadService.retrieveNotDownloadedAndStartDownload();
    }

    private void storeLocationMasterBeans(List<LocationMasterBean> locationMasterBeans, boolean allClear) {
        if (locationMasterBeans != null && !locationMasterBeans.isEmpty()) {
            Log.i(TAG, LabelConstants.LOCATION_MASTER_COUNT + locationMasterBeans.size());
            try {
                if (!allClear) {
                    for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                        DeleteBuilder<LocationMasterBean, Integer> deleteBuilder = locationMasterBeanDao.deleteBuilder();
                        deleteBuilder.where().eq(FieldNameConstants.ACTUAL_I_D, locationMasterBean.getActualID());
                        deleteBuilder.delete();
                    }
                }
                locationMasterBeanDao.create(locationMasterBeans);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
                return;
            }
        }

        try {
            VersionBean versionBean;
            List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LAST_UPDATED_LOCATION_MASTER);
            if (versionBeans == null || versionBeans.isEmpty()) {
                VersionBean versionBean1 = new VersionBean();
                versionBean1.setKey(GlobalTypes.VERSION_LAST_UPDATED_LOCATION_MASTER);
                versionBean1.setValue(Long.toString(new Date().getTime()));
                versionBeanDao.create(versionBean1);
            } else {
                versionBean = versionBeans.get(0);
                versionBean.setValue(Long.toString(new Date().getTime()));
                versionBeanDao.update(versionBean);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeHealthInfrastructureDetails(List<HealthInfrastructureBean> healthInfrastructureBeans, boolean allClear) {
        if (healthInfrastructureBeans != null && !healthInfrastructureBeans.isEmpty()) {
            try {
                if (!allClear) {
                    for (HealthInfrastructureBean bean : healthInfrastructureBeans) {
                        DeleteBuilder<HealthInfrastructureBean, Integer> deleteBuilder = healthInfrastructureBeanDao.deleteBuilder();
                        deleteBuilder.where().eq(FieldNameConstants.ACTUAL_ID, bean.getActualId());
                        deleteBuilder.delete();
                    }
                }
                healthInfrastructureBeanDao.create(healthInfrastructureBeans);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
                return;
            }
        }

        try {
            VersionBean versionBean;
            List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LAST_UPDATED_HEALTH_INFRASTRUCTURE);

            String maxDate = null;
            if (healthInfrastructureBeanDao.countOf() > 0) {
                String string = healthInfrastructureBeanDao.queryBuilder().selectRaw(MAX_MODIFIED_ON).prepareStatementString();
                String[] result = healthInfrastructureBeanDao.queryRaw(string).getFirstResult();
                if (result != null && result[0] != null) {
                    maxDate = result[0];
                }
            }

            if (versionBeans == null || versionBeans.isEmpty()) {
                VersionBean versionBean1 = new VersionBean();
                versionBean1.setKey(GlobalTypes.VERSION_LAST_UPDATED_HEALTH_INFRASTRUCTURE);
                versionBean1.setValue(maxDate);
                versionBeanDao.create(versionBean1);
            } else {
                versionBean = versionBeans.get(0);
                versionBean.setValue(maxDate);
                versionBeanDao.update(versionBean);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeSchoolDetails(List<SchoolBean> schoolBeans, boolean allClear) {
        if (schoolBeans != null && !schoolBeans.isEmpty()) {
            try {
                if (!allClear) {
                    for (SchoolBean bean : schoolBeans) {
                        DeleteBuilder<SchoolBean, Integer> deleteBuilder = schoolBeanDao.deleteBuilder();
                        deleteBuilder.where().eq(FieldNameConstants.ACTUAL_ID, bean.getActualId());
                        deleteBuilder.delete();
                    }
                }
                schoolBeanDao.create(schoolBeans);
            } catch (SQLException e) {
                Log.e(TAG, null, e);
            }
        }
    }

    private void storeLocationTypeBeans(List<LocationTypeBean> locationTypeBeans, Long lastUpdateDate) {
        try {
            if (lastUpdateDate == null) {
                TableUtils.clearTable(locationTypeBeanDao.getConnectionSource(), LocationTypeBean.class);
            }
            if (locationTypeBeans == null || locationTypeBeans.isEmpty()) {
                return;
            }

            if (lastUpdateDate != null) {
                for (LocationTypeBean bean : locationTypeBeans) {
                    DeleteBuilder<LocationTypeBean, Integer> deleteBuilder = locationTypeBeanDao.deleteBuilder();
                    deleteBuilder.where().eq(SewaConstants.LOCATION_TYPE_BEAN_TYPE, bean.getType());
                    deleteBuilder.delete();
                }
            }
            locationTypeBeanDao.create(locationTypeBeans);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    private void storeMenuDetails(List<MenuBean> menuBeans) {
        if (menuBeans != null && !menuBeans.isEmpty()) {
            System.out.println("#### All menus : " + menuBeans.size());
            try {
                TableUtils.clearTable(menuBeanDao.getConnectionSource(), MenuBean.class);
                menuBeanDao.create(menuBeans);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    public void changeVersionBeanVersionValueForGivenKey(String key, String value, String version) {
        try {
            VersionBean versionBean = versionBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.KEY, key).queryForFirst();
            if (versionBean == null) {
                versionBean = new VersionBean();
                versionBean.setKey(key);
            }
            versionBean.setValue(value);
            versionBean.setVersion(version);
            versionBeanDao.createOrUpdate(versionBean);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeCovidTravellersInfo(List<CovidTravellersInfoBean> covidTravellersInfoBeans, Long lastUpdateDate) {
        try {
            if (lastUpdateDate == null) {
                TableUtils.clearTable(covidTravellersInfoBeanDao.getConnectionSource(), CovidTravellersInfoBean.class);
            } else {
                for (CovidTravellersInfoBean bean : covidTravellersInfoBeans) {
                    DeleteBuilder<CovidTravellersInfoBean, Integer> deleteBuilder = covidTravellersInfoBeanDao.deleteBuilder();
                    deleteBuilder.where().eq(FieldNameConstants.ACTUAL_ID, bean.getActualId());
                    deleteBuilder.delete();
                }
            }
            covidTravellersInfoBeanDao.create(covidTravellersInfoBeans);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    private void storeUserInfraInfo(List<UserHealthInfraBean> userHealthInfraBeans, Long lastUpdateDate) {
        try {
            if (lastUpdateDate == null) {
                TableUtils.clearTable(userHealthInfraBeanDao.getConnectionSource(), UserHealthInfraBean.class);
            } else {
                for (UserHealthInfraBean bean : userHealthInfraBeans) {
                    DeleteBuilder<UserHealthInfraBean, Integer> deleteBuilder = userHealthInfraBeanDao.deleteBuilder();
                    deleteBuilder.where().eq(FieldNameConstants.HEALTH_INFRASTRUCTURE_ID, bean.getHealthInfrastructureId());
                    deleteBuilder.delete();
                }
            }
            userHealthInfraBeanDao.create(userHealthInfraBeans);
        } catch (SQLException e) {
            Log.e(TAG, null, e);
        }
    }

    private void storeLmsCourses(List<LmsCourseDataBean> courseDataBeans) {
        if (courseDataBeans != null && !courseDataBeans.isEmpty()) {
            System.out.println("#### All Courses : " + courseDataBeans.size());
            try {
                List<LmsCourseBean> courseBeans = courseBeanDao.queryForAll();
                for (LmsCourseDataBean courseDataBean : courseDataBeans) {
                    for (LmsCourseBean lmsCourseBean : courseBeans) {
                        if (courseDataBean.getCourseId().equals(lmsCourseBean.getCourseId()) &&
                                lmsCourseBean.getArchived() != null) {
                            courseDataBean.setArchived(lmsCourseBean.getArchived());
                        }
                    }
                }
                TableUtils.clearTable(courseBeanDao.getConnectionSource(), LmsCourseBean.class);
                for (LmsCourseDataBean lmsCourseDataBean : courseDataBeans) {
                    courseBeanDao.create(new LmsCourseBean(lmsCourseDataBean));
                }
                lmsDownloadService.addLmsMediaFilesToDownloadList();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            try {
                TableUtils.clearTable(courseBeanDao.getConnectionSource(), LmsCourseBean.class);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void storeLmsUserMetaData(List<LmsUserMetaDataBean> lmsUserMetaDataBeans) {
        try {
            TableUtils.clearTable(lmsUserMetadataBeanDao.getConnectionSource(), LmsUserMetaDataBean.class);
            if (lmsUserMetaDataBeans != null && !lmsUserMetaDataBeans.isEmpty()) {
                lmsUserMetadataBeanDao.create(lmsUserMetaDataBeans);
                storeLmsViewedMediaBean(lmsUserMetaDataBeans);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private void storeLmsViewedMediaBean(List<LmsUserMetaDataBean> lmsUserMetaDataBeans) {
        for (LmsUserMetaDataBean metaData : lmsUserMetaDataBeans) {
            LmsUserMetaData userMetaData = new LmsUserMetaData(metaData);
            if (userMetaData.getLessonMetaData() != null && !userMetaData.getLessonMetaData().isEmpty()) {
                for (LmsUserLessonMetaData lessonMetaData : userMetaData.getLessonMetaData()) {
                    LmsViewedMediaBean viewedMediaBean = lmsService.getViewedLessonById(lessonMetaData.getLessonId());
                    if (viewedMediaBean == null) {
                        LmsViewedMediaBean lmsViewedMediaBean = new LmsViewedMediaBean();
                        lmsViewedMediaBean.setCourseId(userMetaData.getCourseId());
                        lmsViewedMediaBean.setModuleId(lessonMetaData.getModuleId());
                        lmsViewedMediaBean.setLessonId(lessonMetaData.getLessonId());
                        if (lessonMetaData.getStartDate() != null) {
                            lmsViewedMediaBean.setStartDate(new Date(lessonMetaData.getStartDate()));
                        }
                        if (lessonMetaData.getEndDate() != null) {
                            lmsViewedMediaBean.setEndDate(new Date(lessonMetaData.getEndDate()));
                        }
                        lmsViewedMediaBean.setUserFeedback(lessonMetaData.getUserFeedback());
                        if (lessonMetaData.getSessions() != null && !lessonMetaData.getSessions().isEmpty()) {
                            lmsViewedMediaBean.setSessions(new Gson().toJson(lessonMetaData.getSessions()));
                        }
                        lmsViewedMediaBean.setLastPausedOn(lessonMetaData.getLastPausedOn());
                        lmsViewedMediaBean.setCompleted(lessonMetaData.getCompleted());
                        try {
                            lmsViewedMediaBeanDao.create(lmsViewedMediaBean);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void storeMoConfirmedDetails(List<MemberMoConfirmedDataBean> memberMoConfirmedDataBeans) {
        if (memberMoConfirmedDataBeans != null && !memberMoConfirmedDataBeans.isEmpty()) {
            System.out.println("#### MO Confirmed Member Count : " + memberMoConfirmedDataBeans.size());
            try {
                TableUtils.clearTable(moConfirmedBeanDao.getConnectionSource(), MemberMoConfirmedDetailBean.class);
                for (MemberMoConfirmedDataBean moConfirmedDataBean : memberMoConfirmedDataBeans) {
                    moConfirmedBeanDao.create(new MemberMoConfirmedDetailBean(moConfirmedDataBean));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            try {
                TableUtils.clearTable(moConfirmedBeanDao.getConnectionSource(), MemberMoConfirmedDetailBean.class);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void storeDrugInventoryDetails(List<DrugInventoryBean> drugInventoryBeans) {
        if (drugInventoryBeans != null && !drugInventoryBeans.isEmpty()) {
            System.out.println("#### Drug Inventory Count : " + drugInventoryBeans.size());
            try {
                TableUtils.clearTable(drugInventoryBeanDao.getConnectionSource(), DrugInventoryBean.class);
                drugInventoryBeanDao.create(drugInventoryBeans);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            try {
                TableUtils.clearTable(drugInventoryBeanDao.getConnectionSource(), DrugInventoryBean.class);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
