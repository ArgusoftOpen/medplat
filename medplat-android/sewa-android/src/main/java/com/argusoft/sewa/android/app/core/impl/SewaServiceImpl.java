package com.argusoft.sewa.android.app.core.impl;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.core.SewaService;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.LoggedInUserPrincipleDataBean;
import com.argusoft.sewa.android.app.databean.MobileRequestParamDto;
import com.argusoft.sewa.android.app.databean.RecordStatusBean;
import com.argusoft.sewa.android.app.databean.UserInfoDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.DailyNutritionLogBean;
import com.argusoft.sewa.android.app.model.DailyPresenceReport;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.FileDownloadBean;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LibraryBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationCoordinatesBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
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
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.model.UncaughtExceptionBean;
import com.argusoft.sewa.android.app.model.UploadFileDataBean;
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.restclient.impl.ApiManager;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.Encryptor;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author alpesh
 */
@EBean(scope = Singleton)
public class SewaServiceImpl implements SewaService {

    private final SewaTransformer sewaTransformer = SewaTransformer.getInstance();
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    @Bean
    TechoServiceImpl techoService;
    @Bean
    MoveToProductionServiceImpl moveToProductionService;
    @Bean
    SyncServiceImpl syncService;
    @Bean
    LmsEventServiceImpl lmsEventService;
    @Bean
    ApiManager apiManager;

    @RootContext
    Context context;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LoginBean, Integer> loginBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<ListValueBean, Integer> listValueBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LabelBean, Integer> labelBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<NotificationBean, Integer> notificationBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LoggerBean, Integer> loggerBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnnouncementBean, Integer> announcementBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<StoreAnswerBean, Integer> storeAnswerBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<UploadFileDataBean, Integer> uploadFileDataBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<QuestionBean, Integer> questionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnswerBean, Integer> answerBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> syncBeanDao;
    //in phase 2
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationBean, Integer> locationBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<UncaughtExceptionBean, Integer> uncaughtExceptionBeanDao;

    private void resetData(UserInfoDataBean userInfoDataBean, LoginBean loginBean) {
        if (loginBean != null) {
            if (!loginBean.getUsername().equals(userInfoDataBean.getUsername())) {
                clearTable(AnnouncementBean.class);
                clearTable(CovidTravellersInfoBean.class);
                clearTable(DailyNutritionLogBean.class);
                clearTable(DailyPresenceReport.class);
                clearTable(DataQualityBean.class);
                clearTable(FamilyBean.class);
                clearTable(FhwServiceDetailBean.class);
                clearTable(FileDownloadBean.class);
                clearTable(FormAccessibilityBean.class);
                clearTable(LibraryBean.class);
                clearTable(ListValueBean.class);
                clearTable(LmsBookMarkBean.class);
                clearTable(LmsCourseBean.class);
                clearTable(LmsUserMetaDataBean.class);
                clearTable(LmsViewedMediaBean.class);
                clearTable(LocationBean.class);
                clearTable(LocationCoordinatesBean.class);
                clearTable(LoggerBean.class);
                clearTable(MemberBean.class);
                clearTable(MemberCbacDetailBean.class);
                clearTable(MemberPregnancyStatusBean.class);
                clearTable(MenuBean.class);
                clearTable(MergedFamiliesBean.class);
                clearTable(MigratedFamilyBean.class);
                clearTable(MigratedMembersBean.class);
                clearTable(NotificationBean.class);
                clearTable(RchVillageProfileBean.class);
                clearTable(ReadNotificationsBean.class);
                clearTable(UserHealthInfraBean.class);

                loginBean.setLastUpdateOfListValues(0L);
                loginBean.setFirstLogin(true);
                //load labels again if language is changed from previous login user language
                if (loginBean.getLanguageCode() != null && userInfoDataBean.getLanguageCode() != null && !loginBean.getLanguageCode().equalsIgnoreCase(userInfoDataBean.getLanguageCode())) {
                    loginBean.setLastUpdateOfLabels(0L);
                    clearTable(LabelBean.class);
                }
                if (!loginBean.getUserRole().equalsIgnoreCase(userInfoDataBean.getUserRole())) {
                    clearTable(MenuBean.class);
                    clearTable(QuestionBean.class);
                    clearTable(AnswerBean.class);
                    syncService.changeVersionBeanVersionValueForGivenKey(GlobalTypes.MOBILE_FORM_VERSION, null, "0");
                }
                loginBean.setLastUpdateOfInventory(0L);
                loginBean.setLastUpdateOfAnnouncements(0L);
                // also clear the otp
                VersionBean bean = new VersionBean();
                bean.setKey(loginBean.getUsername());
                try {
                    syncBeanDao.delete(syncBeanDao.queryForMatching(bean));
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }
            } else {
                //load labels again if language is changed from previous login
                if (loginBean.getLanguageCode() != null && userInfoDataBean.getLanguageCode() != null && !loginBean.getLanguageCode().equalsIgnoreCase(userInfoDataBean.getLanguageCode())) {
                    loginBean.setLastUpdateOfLabels(0L);
                    clearTable(LabelBean.class);
                }
            }
        }
    }

    @Override
    public List<UploadFileDataBean> getFileWorkLog() {
        List<UploadFileDataBean> uploadFileDataBeans = null;
        try {
            uploadFileDataBeans = uploadFileDataBeanDao.queryForAll();
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return uploadFileDataBeans;
    }

    @Override
    public String validateUser(String username, String password, int contextUrlInfo, boolean isRemember) {
        username = username.trim().toLowerCase();
        password = password.trim();
        UserInfoDataBean userInfoDataBean = null;
        LoginBean loginBean = SewaTransformer.loginBean;

        if (isOnline()) {
            try {
                //  Network Available
                userInfoDataBean = sewaServiceRestClient.getUser(username, password, null, Boolean.TRUE);
            } catch (Exception ex) {
                SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_USER_DOWNLOAD;
            }
        }

        if (userInfoDataBean != null) {
            //means you are online and get Detail
            if (userInfoDataBean.isAuthenticated()) {
                return doOnlineLogin(userInfoDataBean, loginBean, password, contextUrlInfo, isRemember);
            } else {
                return userInfoDataBean.getLoginFailureMsg();
            }
        } else {
            return doOfflineLogin(loginBean, username, password, isRemember);
        }
    }

    private String doOnlineLogin(UserInfoDataBean userInfoDataBean, LoginBean loginBean, String password, int contextUrlInfo, boolean isRemember) {
        resetData(userInfoDataBean, loginBean);

        if (loginBean != null && !loginBean.getUsername().equals(userInfoDataBean.getUsername())) {
            this.updateUserTokenInStoreAnswerBeanForExistingUser(loginBean);
        }

        loginBean = sewaTransformer.convertUserInfoDataBeanToLoginBeanModel(userInfoDataBean, loginBean, Encryptor.encrypt(password));
        loginBean.setPasswordPlain(password);
        loginBean.setRemember(isRemember);
        if (contextUrlInfo == 1) {
            loginBean.setTrainingUser(false);
            SewaUtil.isUserInTraining = Boolean.FALSE;
        } else if (contextUrlInfo == 2) {
            loginBean.setTrainingUser(true);
            SewaUtil.isUserInTraining = Boolean.TRUE;
        }

        try {
            clearTable(LoginBean.class);
            loginBean.setHidden(false);

            if (SewaTransformer.loginBean == null) {
                int id = loginBeanDao.create(loginBean);
                loginBean.setId(id);
            } else {
                loginBeanDao.createOrUpdate(loginBean);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        SewaTransformer.loginBean = loginBean;
        this.updateLatestTokenInStoreAnswerBean();

        if (!isMobileDateSameAsServerDate(loginBean.getServerDate())) {
            return GlobalTypes.MOBILE_DATE_NOT_SAME_SERVER + UtilBean.convertDateToString(loginBean.getServerDate(), false, true, true);
        }
        return SewaConstants.LOGIN_SUCCESS_WEB;
    }

    private String doOfflineLogin(LoginBean loginBean, String username, String password, boolean isRemember) {
        if (loginBean != null) {
            if (loginBean.getUsername().trim().equalsIgnoreCase(username)) {
                if (loginBean.getPassword().equals(Encryptor.encrypt(password))) {
                    loginBean.setLoggedIn(true);
                    try {
                        loginBean.setFirstLogin(false);
                        loginBean.setHidden(false);
                        loginBean.setPasswordPlain(password);
                        loginBean.setRemember(isRemember);
                        loginBeanDao.update(loginBean);
                    } catch (SQLException ex) {
                        Log.e(getClass().getSimpleName(), null, ex);
                    }
                    SewaTransformer.loginBean = loginBean;
                    return SewaConstants.LOGIN_SUCCESS_LOCAL;
                } else {
                    return SewaConstants.LOGIN_FAILURE;
                }
            }
        }
        return SewaConstants.NO_INTERNET_CONNECTION;
    }

    private boolean isMobileDateSameAsServerDate(Long serverDate) {
        boolean result = true;
        if (serverDate != null) {
            java.util.Calendar serverCal = java.util.Calendar.getInstance();
            serverCal.setTimeInMillis(serverDate);
            UtilBean.clearTimeFromDate(serverCal);

            java.util.Calendar mobCal = java.util.Calendar.getInstance();
            UtilBean.clearTimeFromDate(mobCal);

            if (serverCal.getTimeInMillis() != mobCal.getTimeInMillis()) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public String getAndroidVersion() {
        if (isOnline()) {
            try {
                return sewaServiceRestClient.retrieveAndroidVersionFromServer();
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), null, ex);
            }
        }
        return null;
    }

    @Override
    public boolean getServerIsAlive() {
        try {
            return sewaServiceRestClient.retrieveServerIsAlive();
        } catch (RestHttpException e) {
            Log.e(getClass().getSimpleName(), null, e);
            return false;
        }
    }

    @Override
    public void doAfterSuccessfulLogin(boolean isLogin) {
        if (isOnline() && SewaTransformer.loginBean != null && getServerIsAlive()) {
            try {
                fetchingInitializingData();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Successful login exception" + e);
                SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_INITIALIZING_DOWNLOAD;
            }
        }
    }

    @Override
    public boolean isOnline() {
        boolean networkAvailFlag = false;
        SharedStructureData.NETWORK_MESSAGE = SewaConstants.NETWORK_FAILURE;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                networkAvailFlag = true;
                SharedStructureData.NETWORK_MESSAGE = SewaConstants.NETWORK_AVAILABLE;
            }
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return networkAvailFlag;
    }

    @Override
    public void initLabelBeanMap() {
        if (SharedStructureData.listLabelBeans == null) {
            SharedStructureData.listLabelBeans = new HashMap<>();
        }
        for (LabelBean labelBean : getAllLabelBean()) {
            SharedStructureData.listLabelBeans.put(labelBean.getMapIndex(), labelBean);
        }
    }

    private void fetchingInitializingData() {
        boolean isMergedFamiliesInformationUpdated = true;
        try {
            if (SewaTransformer.loginBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_FHW)
                    || SewaTransformer.loginBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_MPHW)) {
                isMergedFamiliesInformationUpdated = techoService.syncMergedFamiliesInformationWithServer();
            }
        } catch (Exception e) {
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_UPLOAD_TO_SERVER;
            Log.e(getClass().getSimpleName(), null, e);
        }

        try {
            this.updateAnswerBeanTokensAndUserIdsOnce();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_TOKEN_UPDATION;
            return;
        }

        try {
            if (isMergedFamiliesInformationUpdated) {
                uploadDataToServer();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error Occurs while uploadDataToServer : " + e.getMessage());
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_UPLOAD_TO_SERVER;
            return;
        }


        try {
            lmsEventService.uploadLmsEventToServer();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error Occurs while Upload LMS Event To Serverf : " + e.getMessage());
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_UPLOAD_TO_SERVER;
            return;
        }

        //for uploading uncaught exceptions (if any)
        uploadUncaughtExceptionDetailToServer();

        try {
            moveToProductionService.postUserReadyToMoveProduction();
            moveToProductionService.saveFormAccessibilityBeansFromServer();
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), "Error Occurs while saving form accessibility beans.", ex);
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_FETCHING_USER_FORM_ACCESS;
        }

        try {
            if (SewaTransformer.loginBean.getUserRole() != null && !SewaTransformer.loginBean.getUserRole().trim().isEmpty()) {
                syncService.getDataFromServer();
            }
            initLabelBeanMap();
        } catch (Exception e) {
            SharedStructureData.NETWORK_MESSAGE = SewaConstants.EXCEPTION_FETCHING_DATA_FOR_USER;
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    private List<LoggerBean> getLoggerBeansByFilter(LoggerBean bean) {
        try {
            return loggerBeanDao.queryForMatchingArgs(bean);
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return new ArrayList<>();
    }

    private LoggerBean getLoggerBeanByFilter(LoggerBean bean) {
        List<LoggerBean> loggerBeansByFilter = getLoggerBeansByFilter(bean);
        if (loggerBeansByFilter != null && !loggerBeansByFilter.isEmpty()) {
            return loggerBeansByFilter.get(0);
        }
        return null;
    }

    private List<StoreAnswerBean> getStoreAnswerBeansByFilter(StoreAnswerBean bean) {
        try {
            return storeAnswerBeanDao.queryForMatchingArgs(bean);
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return new ArrayList<>();
    }

    private void uploadDataToServer() {
        try {
            StoreAnswerBean record = new StoreAnswerBean(true);
            record.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
            List<StoreAnswerBean> recordsToSubmit = getStoreAnswerBeansByFilter(record);
            if (recordsToSubmit != null && !recordsToSubmit.isEmpty()) {
                Map<String, List<StoreAnswerBean>> userWiseRecords = new HashMap<>();
                for (StoreAnswerBean storeAnswerBean : recordsToSubmit) {
                    if (storeAnswerBean.getToken() == null || storeAnswerBean.getToken().trim().length() == 0 || storeAnswerBean.getToken().trim().equalsIgnoreCase("null")) {
                        storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
                    }
                    List<StoreAnswerBean> records = userWiseRecords.get(storeAnswerBean.getToken());
                    if (records == null) {
                        records = new ArrayList<>();
                        userWiseRecords.put(storeAnswerBean.getToken(), records);
                    }
                    records.add(storeAnswerBean);
                }
                if (!userWiseRecords.isEmpty()) {

                    for (Map.Entry<String, List<StoreAnswerBean>> entry : userWiseRecords.entrySet()) {
                        String token = entry.getKey();
                        List<StoreAnswerBean> records = entry.getValue();
                        String[] recordsString = new String[records.size()];
                        for (int i = 0; i < recordsString.length; i++) {
                            recordsString[i] = records.get(i).pack();
                        }
                        RecordStatusBean[] recordStatusBeans = sewaServiceRestClient.recordEntryFromMobileToServer(token, recordsString);
                        evaluateResponse(recordStatusBeans, records);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    private void evaluateResponse(RecordStatusBean[] recordStatusBeans, List<StoreAnswerBean> records) {
        if (recordStatusBeans != null && recordStatusBeans.length > 0) {
            for (int i = 0; i < recordStatusBeans.length; i++) {

                RecordStatusBean recordStatusBean = recordStatusBeans[i];

                LoggerBean loggerBean = new LoggerBean();
                loggerBean.setCheckSum(recordStatusBean.getChecksum());
                loggerBean = getLoggerBeanByFilter(loggerBean);

                if (recordStatusBean.getStatus() != null
                        && recordStatusBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_SUCCESS)) {
                    try {
                        storeAnswerBeanDao.delete(records.get(i));
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                    if (loggerBean != null) {
                        loggerBean.setNoOfAttempt(loggerBean.getNoOfAttempt() + 1);
                        loggerBean.setStatus(GlobalTypes.STATUS_SUCCESS);
                        loggerBean.setModifiedOn(Calendar.getInstance().getTime());
                        if (recordStatusBean.getGeneratedId() != null &&
                                (loggerBean.getFormType().equals(FormConstants.FAMILY_HEALTH_SURVEY) ||
                                        loggerBean.getFormType().equals(FormConstants.FHS_MEMBER_UPDATE) ||
                                        loggerBean.getFormType().equals(FormConstants.CFHC) ||
                                        loggerBean.getFormType().equalsIgnoreCase(FormConstants.IDSP_NEW_FAMILY))) {
                            loggerBean.setBeneficiaryName(recordStatusBean.getGeneratedId());
                        }
                        if (recordStatusBean.getMessage() != null) {
                            loggerBean.setMessage(recordStatusBean.getMessage());
                        }
                        try {
                            loggerBeanDao.update(loggerBean);
                        } catch (SQLException ex) {
                            Log.e(getClass().getSimpleName(), null, ex);
                        }
                    }
                } else if (recordStatusBean.getStatus() != null
                        && recordStatusBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_ERROR)) {
                    try {
                        storeAnswerBeanDao.delete(records.get(i));
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                    if (loggerBean != null) {
                        loggerBean.setNoOfAttempt(loggerBean.getNoOfAttempt() + 1);
                        loggerBean.setStatus(GlobalTypes.STATUS_ERROR);
                        loggerBean.setModifiedOn(Calendar.getInstance().getTime());
                        try {
                            loggerBeanDao.update(loggerBean);
                        } catch (SQLException ex) {
                            Log.e(getClass().getSimpleName(), null, ex);
                        }
                    }
                } else if (recordStatusBean.getStatus() != null
                        && recordStatusBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_PENDING)) {

                    if (loggerBean != null) {
                        loggerBean.setNoOfAttempt(loggerBean.getNoOfAttempt() + 1);
                        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
                        loggerBean.setModifiedOn(Calendar.getInstance().getTime());
                        try {
                            loggerBeanDao.update(loggerBean);
                        } catch (SQLException ex) {
                            Log.e(getClass().getSimpleName(), null, ex);
                        }
                    }
                } else if (recordStatusBean.getStatus() != null
                        && recordStatusBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_HANDLED_ERROR)) {
                    try {
                        storeAnswerBeanDao.delete(records.get(i));
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                    if (recordStatusBean.getGeneratedId() != null &&
                            loggerBean.getFormType().equalsIgnoreCase(FormConstants.CFHC)) {
                        loggerBean.setBeneficiaryName(recordStatusBean.getGeneratedId());
                    }
                    if (loggerBean != null) {
                        loggerBean.setNoOfAttempt(loggerBean.getNoOfAttempt() + 1);
                        loggerBean.setStatus(GlobalTypes.STATUS_HANDLED_ERROR);
                        loggerBean.setModifiedOn(Calendar.getInstance().getTime());
                        if (recordStatusBean.getMessage() != null) {
                            loggerBean.setMessage(recordStatusBean.getMessage());
                        }
                        try {
                            loggerBeanDao.update(loggerBean);
                        } catch (SQLException ex) {
                            Log.e(getClass().getSimpleName(), null, ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void loadQuestionsBySheet(String entity) {
        try {
            if (SharedStructureData.mapDataMapLabelBean != null) {
                SharedStructureData.mapDataMapLabelBean.clear();
            } else {
                SharedStructureData.mapDataMapLabelBean = new HashMap<>();
            }
            // load all sheet related data map in map
            List<ListValueBean> listValueBeans = listValueBeanDao.queryBuilder().orderBy(SewaConstants.LIST_VALUE_BEAN_DATAMAP, true).where().eq(SewaConstants.LIST_VALUE_BEAN_ENTITY, entity).query();
            if (listValueBeans != null && !listValueBeans.isEmpty()) {
                for (ListValueBean listValueBean : listValueBeans) {
                    List<FieldValueMobDataBean> values = SharedStructureData.mapDataMapLabelBean.get(listValueBean.getField());
                    if (values == null) {
                        values = new ArrayList<>();
                        SharedStructureData.mapDataMapLabelBean.put(listValueBean.getField(), values);
                    }
                    values.add(sewaTransformer.convertListValueBeansToFieldValueDataBean(listValueBean));
                }
            }
            // default load to village
            String[] villageCodes = null;
            String[] villageNames = null;
            if (SewaTransformer.loginBean != null) {
                if (SewaTransformer.loginBean.getVillageCode() != null) {
                    villageCodes = UtilBean.split(SewaTransformer.loginBean.getVillageCode(), GlobalTypes.STRING_LIST_SEPARATOR);
                }
                if (SewaTransformer.loginBean.getVillageName() != null) {
                    villageNames = UtilBean.split(SewaTransformer.loginBean.getVillageName(), GlobalTypes.STRING_LIST_SEPARATOR);
                }
            }
            if (villageCodes != null && villageCodes.length > 0) {
                List<FieldValueMobDataBean> fieldValueMobDataBeans = new ArrayList<>();
                for (int i = 0; i < villageCodes.length; i++) {
                    FieldValueMobDataBean fieldValueMobDataBean = new FieldValueMobDataBean();
                    fieldValueMobDataBean.setIdOfValue(Integer.parseInt(villageCodes[i]));
                    if (villageNames != null) {
                        fieldValueMobDataBean.setValue(villageNames[i]);
                    }
                    fieldValueMobDataBeans.add(fieldValueMobDataBean);
                }
                Collections.sort(fieldValueMobDataBeans, (t, t1) -> t.getValue().compareTo(t1.getValue()));
                SharedStructureData.mapDataMapLabelBean.put(GlobalTypes.ASHA_VILLAGES, fieldValueMobDataBeans);
            }
            // load questions

            List<QuestionBean> questionBeans = questionBeanDao.queryForEq(SewaConstants.QUESTION_BEAN_ENTITY, entity);
            List<AnswerBean> answerBeans = answerBeanDao.queryForEq(SewaConstants.QUESTION_BEAN_ENTITY, entity);
            sewaTransformer.convertQuestionBeansToQueFormBeans(questionBeans, answerBeans);
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public List<FieldValueMobDataBean> getFieldValueMobDataBeanByDataMap(String datamap) {
        try {
            List<ListValueBean> listValueBeans = listValueBeanDao.queryForEq(SewaConstants.LIST_VALUE_BEAN_DATAMAP, datamap);
            return sewaTransformer.convertListValueBeansToFieldValueDataBean(listValueBeans);
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public List<LoggerBean> getWorkLog() {
        List<LoggerBean> loggerBeans = null;
        try {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE, -3);
            instance.set(Calendar.HOUR, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);

            QueryBuilder<LoggerBean, Integer> queryBuilder = loggerBeanDao.queryBuilder();
            Where<LoggerBean, Integer> where = queryBuilder.where();
            where.eq(FieldNameConstants.RECORD_URL, WSConstants.CONTEXT_URL_TECHO)
                    .and().ge("date", instance.getTimeInMillis());

            loggerBeans = queryBuilder.orderBy("_ID", false).query();
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }

        return loggerBeans;
    }

    @Override
    public List<AnnouncementBean> getAllAnnouncement() {
        List<AnnouncementBean> announcementBeans = null;
        try {
            announcementBeans = announcementBeanDao.queryForAll();
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return announcementBeans;
    }

    /*
     *  Create Methods
     */
    @Override
    public Integer createUploadFileDataBean(UploadFileDataBean uploadFileDataBean) {
        Integer result;
        try {
            result = uploadFileDataBeanDao.create(uploadFileDataBean);
        } catch (SQLException ex) {
            result = null;
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return result;
    }

    @Override
    public Integer createStoreAnswerBean(StoreAnswerBean storeAnswerBean) {
        Integer result;
        try {
            storeAnswerBeanDao.create(storeAnswerBean);
            QueryBuilder<StoreAnswerBean, Integer> storeAnswerBeanIntegerQueryBuilder = storeAnswerBeanDao.queryBuilder();
            storeAnswerBeanIntegerQueryBuilder.selectRaw("MAX(`_ID`)");
            String[] firstResult = storeAnswerBeanDao.queryRaw(storeAnswerBeanIntegerQueryBuilder.prepareStatementString()).getFirstResult();
            result = Integer.valueOf(firstResult[0]);
        } catch (SQLException ex) {
            result = null;
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return result;
    }

    @Override
    public void deleteStoreAnswerBeanByStoreAnswerBeanId(Long storeAnswerBeanId) {
        try {
            List<StoreAnswerBean> storeAnswerBeans = storeAnswerBeanDao.queryForEq("_ID", storeAnswerBeanId);
            if (storeAnswerBeans != null && !storeAnswerBeans.isEmpty()) {
                StoreAnswerBean storeAnswerBean = storeAnswerBeans.get(0);
                int id = storeAnswerBeanDao.delete(storeAnswerBean);
                Log.i(getClass().getSimpleName(), "DELETE STORE_ANSWER_BEAN ID : " + id);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public Integer createLoggerBean(LoggerBean loggerBean) {
        Integer result;
        try {
            loggerBeanDao.create(loggerBean);
            QueryBuilder<LoggerBean, Integer> loggerBeanQueryBuilder = loggerBeanDao.queryBuilder();
            loggerBeanQueryBuilder.selectRaw("MAX(`_ID`)");
            String[] firstResult = loggerBeanDao.queryRaw(loggerBeanQueryBuilder.prepareStatementString()).getFirstResult();
            result = Integer.valueOf(firstResult[0]);
        } catch (SQLException ex) {
            result = null;
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return result;
    }

    @Override
    public void deleteLoggerBeanByLoggerBeanId(Long loggerBeanId) {
        try {
            List<LoggerBean> loggerBeans = loggerBeanDao.queryForEq("_ID", loggerBeanId);
            if (loggerBeans != null && !loggerBeans.isEmpty()) {
                LoggerBean loggerBean = loggerBeans.get(0);
                int id = loggerBeanDao.delete(loggerBean);
                Log.i(getClass().getSimpleName(), "DELETE LOGGER_BEAN ID : " + id);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    private void clearTable(Class<?> clazz) {
        try {
            TableUtils.clearTable(loginBeanDao.getConnectionSource(), clazz);
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public List<LabelBean> getAllLabelBean() {
        try {
            return labelBeanDao.queryForAll();
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return new ArrayList<>();
    }

    //    @Override
    private List<VersionBean> retriveSheetStatusesByFiletr(VersionBean status) {
        List<VersionBean> sheetStatuses = null;
        try {
            sheetStatuses = syncBeanDao.queryForMatching(status);
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return sheetStatuses;
    }

    @Override
    public VersionBean getSheetStatusByFiletr(VersionBean status) {
        List<VersionBean> sheetStatuses = retriveSheetStatusesByFiletr(status);
        if (sheetStatuses != null && !sheetStatuses.isEmpty()) {
            return sheetStatuses.get(0);
        }
        return null;
    }

    @Override
    public NotificationBean getNotificationsById(Long id) {
        NotificationBean bean = new NotificationBean();
        bean.setNotificationId(id);
        try {
            List<NotificationBean> queryForMatching = notificationBeanDao.queryForMatching(bean);
            if (queryForMatching != null && !queryForMatching.isEmpty()) {
                return queryForMatching.get(0);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return null;
    }

    @Override
    public void deleteNotificationByNotificationId(Long notificationId) {
        try {
            List<NotificationBean> notificationBeans = notificationBeanDao.queryForEq("notificationId", notificationId);
            if (notificationBeans != null && !notificationBeans.isEmpty()) {
                NotificationBean notificationBean = notificationBeans.get(0);
                int id = notificationBeanDao.delete(notificationBean);
                Log.i(getClass().getSimpleName(), "DELETE NOTIFICATION ID : " + id);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public void updateVersionBean(VersionBean status) {
        try {
            syncBeanDao.createOrUpdate(status);
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public int isUserInProduction(String userName) {
        userName = userName.trim().toLowerCase();
        WSConstants.setLiveContextUrl();
        apiManager.createApiService();
        if (isOnline()) {
            try {
                Boolean userInProduction = sewaServiceRestClient.retrieveUserInProductionFromServer(userName);
                if (Boolean.TRUE.equals(userInProduction)) {
                    WSConstants.setLiveContextUrl();
                    apiManager.createApiService();
                    return 1;
                } else {
                    Boolean userInTraining = sewaServiceRestClient.retrieveUserInTrainingFromServer(userName);
                    if (Boolean.TRUE.equals(userInTraining)) {
                        WSConstants.setTrainingContextUrl();
                        apiManager.createApiService();
                        return 2;
                    } else {
                        WSConstants.setLastContextUrlStore();
                        apiManager.createApiService();
                        return 0;
                    }
                }
            } catch (RestHttpException ex) {
                Log.e(getClass().getSimpleName(), null, ex);
            }
        }
        WSConstants.setLastContextUrlStore();
        apiManager.createApiService();
        return 3;
    }

    //Retrieving All Location By Level For Surveyor
    @Override
    public List<LocationBean> retrieveLocationByLevel(Integer level) {
        List<LocationBean> locationsByLevel;
        try {
            QueryBuilder<LocationBean, Integer> queryBuilder = locationBeanDao.queryBuilder();
            Where<LocationBean, Integer> where = queryBuilder.where();
            where.eq("level", level);
            locationsByLevel = queryBuilder.orderBy("name", true).query();
            if (locationsByLevel == null || locationsByLevel.isEmpty()) {
                return new ArrayList<>();
            } else {
                return locationsByLevel;
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return new ArrayList<>();
    }

    //Retrieve all Locations By Parent For Surveyor and store in shared structure data
    @Override
    public void retrieveLocationByParent(Integer parent) {
        List<LocationBean> locationsByLevel;
        List<FieldValueMobDataBean> allLocationsByParent;
        try {
            QueryBuilder<LocationBean, Integer> queryBuilder = locationBeanDao.queryBuilder();
            Where<LocationBean, Integer> where = queryBuilder.where();
            where.eq("parent", parent);
            locationsByLevel = queryBuilder.orderBy("name", true).query();
            if (locationsByLevel != null && !locationsByLevel.isEmpty()) {
                allLocationsByParent = new ArrayList<>();
                for (LocationBean loc : locationsByLevel) {
                    FieldValueMobDataBean locBean = new FieldValueMobDataBean();
                    locBean.setIdOfValue(loc.getActualID());
                    locBean.setValue(loc.getName());
                    allLocationsByParent.add(locBean);
                }
                SharedStructureData.mapDataMapLabelBean.put(String.valueOf(parent), allLocationsByParent);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    @Override
    public void uploadUncaughtExceptionDetailToServer() {
        try {
            List<UncaughtExceptionBean> allExceptionDetails = uncaughtExceptionBeanDao.queryForAll();
            if (allExceptionDetails != null && !allExceptionDetails.isEmpty()) {
                String response = sewaServiceRestClient.uploadUncaughtExceptionToServer(SewaTransformer.loginBean.getUserID(), allExceptionDetails);
                if (response != null && response.equalsIgnoreCase(SewaConstants.SUCCESS)) {
                    uncaughtExceptionBeanDao.delete(allExceptionDetails);
                }
            }
        } catch (SQLException | RestHttpException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
    }

    /**
     * This method is for retrieving notification By notification id
     */
    @Override
    public NotificationBean retrieveNotificationById(Long notificationId) {
        try {
            NotificationBean notify = new NotificationBean();
            notify.setNotificationId(notificationId);
            List<NotificationBean> notifications = notificationBeanDao.queryForMatching(notify);
            if (!notifications.isEmpty()) {
                return notifications.get(0);
            }
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return null;
    }

    @Override
    public void storeException(Throwable ex, String exceptionType) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        String s = writer.toString();
        String sheetVersion = "N/A";
        try {
            List<VersionBean> versionBeans = syncBeanDao.queryForEq("key", GlobalTypes.FHW_SHEET_VERSION);
            if (versionBeans != null && !versionBeans.isEmpty()) {
                sheetVersion = versionBeans.get(0).getVersion();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        UncaughtExceptionBean exceptionBean = new UncaughtExceptionBean();
        exceptionBean.setStackTrace(s);
        exceptionBean.setUsername(SewaTransformer.loginBean.getUsername());
        exceptionBean.setAndroidVersion(Build.VERSION.RELEASE);
        exceptionBean.setManufacturer(Build.MANUFACTURER);
        exceptionBean.setModel(Build.MODEL + " Sheet Version:" + sheetVersion);
        exceptionBean.setOnDate(new Date());
        exceptionBean.setRevisionNumber(BuildConfig.VERSION_CODE);
        exceptionBean.setExceptionType(exceptionType);

        try {
            uncaughtExceptionBeanDao.createOrUpdate(exceptionBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void createNotificationBean(NotificationBean notificationBean) {
        if (notificationBean != null) {
            try {
                notificationBeanDao.create(notificationBean);
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }
    }

    @Override
    public void updateAnswerBeanTokensAndUserIdsOnce() throws RestHttpException {
        try {
            Set<String> distinctTokens = new HashSet<>();
            List<StoreAnswerBean> answerBeansWithoutUserId = storeAnswerBeanDao.queryBuilder()
                    .where().isNull(FieldNameConstants.USER_ID)
                    .and().eq(FieldNameConstants.RECORD_URL, WSConstants.CONTEXT_URL_TECHO).query();

            if (answerBeansWithoutUserId == null || answerBeansWithoutUserId.isEmpty()) {
                return;
            }

            for (StoreAnswerBean storeAnswerBean : answerBeansWithoutUserId) {
                distinctTokens.add(storeAnswerBean.getToken());
            }

            if (!distinctTokens.isEmpty()) {
                MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
                mobileRequestParamDto.setUserTokens(new ArrayList<>(distinctTokens));
                Map<String, String[]> responseObject = sewaServiceRestClient.getUserIdAndTokenFromToken(mobileRequestParamDto);

                if (responseObject == null) {
                    return;
                }

                for (Map.Entry<String, String[]> aTokenEntry : responseObject.entrySet()) {
                    if (aTokenEntry.getValue() != null && Long.parseLong((aTokenEntry.getValue()[0])) != -1L) {
                        UpdateBuilder<StoreAnswerBean, Integer> storeAnswerBeanIntegerUpdateBuilder = storeAnswerBeanDao.updateBuilder();
                        storeAnswerBeanIntegerUpdateBuilder.updateColumnValue(FieldNameConstants.USER_ID, Long.valueOf(aTokenEntry.getValue()[0]))
                                .updateColumnValue(FieldNameConstants.TOKEN, aTokenEntry.getValue()[1])
                                .where().eq(FieldNameConstants.TOKEN, aTokenEntry.getKey())
                                .and().eq(FieldNameConstants.RECORD_URL, WSConstants.CONTEXT_URL_TECHO);
                        storeAnswerBeanIntegerUpdateBuilder.update();
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void updateLatestTokenInStoreAnswerBean() {
        if (SewaTransformer.loginBean == null) {
            return;
        }
        UpdateBuilder<StoreAnswerBean, Integer> storeAnswerBeanIntegerUpdateBuilder = storeAnswerBeanDao.updateBuilder();
        try {
            storeAnswerBeanIntegerUpdateBuilder.updateColumnValue(FieldNameConstants.TOKEN, SewaTransformer.loginBean.getUserToken())
                    .where().eq(FieldNameConstants.USER_ID, SewaTransformer.loginBean.getUserID())
                    .and().eq(FieldNameConstants.RECORD_URL, WSConstants.CONTEXT_URL_TECHO);
            storeAnswerBeanIntegerUpdateBuilder.update();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    private void updateUserTokenInStoreAnswerBeanForExistingUser(LoginBean loginBean) {
        try {
            if (storeAnswerBeanDao.countOf() > 0 && loginBean.getPasswordPlain() != null && !loginBean.getPasswordPlain().isEmpty()) {
                this.revalidateUserTokenForUser();
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void revalidateUserTokenForUser() {
        try {
            MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
            mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
            Boolean isTokenValid = sewaServiceRestClient.getTokenValidity(mobileRequestParamDto);
            if (isTokenValid != null && !isTokenValid) {
                String[] userPassMap = new String[2];
                userPassMap[0] = SewaTransformer.loginBean.getUsername();
                userPassMap[1] = SewaTransformer.loginBean.getPasswordPlain();
                MobileRequestParamDto mobileRequestParamDto2 = new MobileRequestParamDto();
                mobileRequestParamDto2.setUserPassMap(userPassMap);
                LoggedInUserPrincipleDataBean loggedInUserPrincipleDataBean = sewaServiceRestClient.revalidateUserToken(mobileRequestParamDto2);
                String updatedToken = loggedInUserPrincipleDataBean.getNewToken();
                LoginBean loginBean = loginBeanDao.queryForAll().get(0);
                loginBean.setUserToken(updatedToken);
                loginBeanDao.update(loginBean);
                SewaTransformer.loginBean.setUserToken(updatedToken);
            }
            this.updateLatestTokenInStoreAnswerBean();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public LoginBean getCurrentLoggedInUser() {
        try {
            return loginBeanDao.queryForAll().get(0);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
            return null;
        }
    }

//    @Override
//    public void addFireBaseToken() {
//        try {
//            MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
//            mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
//            mobileRequestParamDto.setUserId(SewaTransformer.loginBean.getUserID());
//            Task<String> task = FirebaseMessaging.getInstance().getToken();
//            String token = Tasks.await(task);
//            mobileRequestParamDto.setFirebaseToken(token);
//            sewaServiceRestClient.addFireBaseToken(mobileRequestParamDto);
//        } catch (Exception e) {
//            Log.e(getClass().getSimpleName(), null, e);
//        }
//    }
}
