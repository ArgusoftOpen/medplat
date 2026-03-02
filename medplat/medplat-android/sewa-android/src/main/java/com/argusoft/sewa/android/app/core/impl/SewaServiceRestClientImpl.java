package com.argusoft.sewa.android.app.core.impl;

import static com.argusoft.sewa.android.app.util.WSConstants.ApiCalls.TECHO_NDHM_AUTHENTICATION;
import static com.argusoft.sewa.android.app.util.WSConstants.ApiCalls.TECHO_NDHM_CONFIRM_AADHAAR_OTP;
import static com.argusoft.sewa.android.app.util.WSConstants.ApiCalls.TECHO_NDHM_CONFIRM_MOBILE_OTP;
import static com.argusoft.sewa.android.app.util.WSConstants.ApiCalls.TECHO_NDHM_CONFIRM_PASSWORD;
import static com.argusoft.sewa.android.app.util.WSConstants.ApiCalls.TECHO_NDHM_RESEND_OTP;
import static com.argusoft.sewa.android.app.util.WSConstants.ApiCalls.TECHO_NDHM_STATES;

import com.argusoft.sewa.android.app.core.SewaServiceRestClient;
import com.argusoft.sewa.android.app.databean.AnnouncementMobDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyRequestDto;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyResponderLocationDto;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyResponseDto;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyUserDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamTouristScreeningDataBean;
import com.argusoft.sewa.android.app.databean.CreateHidAadharBioRequest;
import com.argusoft.sewa.android.app.databean.ChardhamTouristScreeningDto;
import com.argusoft.sewa.android.app.databean.ChardhamTouristsDataBean;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.HealthIdAccountProfileResponse;
import com.argusoft.sewa.android.app.databean.HealthIdAccountProfileResponseForNonGov;
import com.argusoft.sewa.android.app.databean.HealthIdSearchResponse;
import com.argusoft.sewa.android.app.databean.LinkBenefitIdResponse;
import com.argusoft.sewa.android.app.databean.LoggedInUserPrincipleDataBean;
import com.argusoft.sewa.android.app.databean.LoginRequestParamDetailDataBean;
import com.argusoft.sewa.android.app.databean.MobileRequestParamDto;
import com.argusoft.sewa.android.app.databean.OfflineHealthIdBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.databean.RbskScreeningDetailDto;
import com.argusoft.sewa.android.app.databean.RecordStatusBean;
import com.argusoft.sewa.android.app.databean.ScannedHealthDataResponse;
import com.argusoft.sewa.android.app.databean.StateResponse;
import com.argusoft.sewa.android.app.databean.UserInfoDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.LmsEventBean;
import com.argusoft.sewa.android.app.model.MergedFamiliesBean;
import com.argusoft.sewa.android.app.model.UncaughtExceptionBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.restclient.impl.ApiManager;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import okhttp3.ResponseBody;

/**
 * @author Alpesh
 */
@EBean(scope = EBean.Scope.Singleton)
public class SewaServiceRestClientImpl implements SewaServiceRestClient {

    @Bean
    ApiManager apiManager;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;

    private static final List<Call> listOfCalls = new ArrayList<>();

    public static List<Call> getListOfCalls() {
        return listOfCalls;
    }

//    private String getRelativeUrl(String relativePath) {
//
//        switch (relativePath) {
//            case WSConstants.ApiCalls.GET_ANDROID_VERSION:
//            case WSConstants.ApiCalls.GET_FONT_SIZE:
//            case WSConstants.ApiCalls.TECHO_IS_USER_IN_PRODUCTION:
//            case WSConstants.ApiCalls.TECHO_VALIDATE_USER_NEW:
//            case WSConstants.ApiCalls.TECHO_UPLOAD_UNCAUGHT_EXCEPTION_DETAIL:
//            case WSConstants.ApiCalls.TECHO_RECORD_ENTRY_MOBILE_TO_DB_SERVER:
//            case WSConstants.ApiCalls.TECHO_POST_AADHAR_UPDATE_DETAILS:
//            case WSConstants.ApiCalls.TECHO_POST_MERGED_FAMILIES_INFORMATION:
//            case WSConstants.ApiCalls.TECHO_GET_FAMILY_TO_BE_ASSIGNED_BY_SEARCH_STRING:
//            case WSConstants.ApiCalls.TECHO_POST_ASSIGN_FAMILY_TO_USER:
//            case WSConstants.ApiCalls.TECHO_GET_USER_FORM_ACCESS_DETAIL:
//            case WSConstants.ApiCalls.TECHO_POST_USER_READY_TO_MOVE_PRODUCTION:
//            case WSConstants.ApiCalls.TECHO_POST_SYNC_MIGRATION_DETAILS:
//            case WSConstants.ApiCalls.TECHO_GET_TOKEN_VALIDITY:
//            case WSConstants.ApiCalls.TECHO_REVALIDATE_TOKEN:
//            case WSConstants.ApiCalls.TECHO_GET_USER_FROM_TOKEN:
//            case WSConstants.ApiCalls.GET_DETAILS_FHW:
//            case WSConstants.ApiCalls.GET_DETAILS_ASHA:
//            case WSConstants.ApiCalls.GET_DETAILS_FHSR:
//            case WSConstants.ApiCalls.GET_FAMILIES_BY_LOCATION:
//            case WSConstants.ApiCalls.TECHO_OTP_REQUEST:
//            case WSConstants.ApiCalls.TECHO_OTP_VERIFICATION:
//            case WSConstants.ApiCalls.SYNC_DATA:
//            case WSConstants.ApiCalls.GET_METADATA:
//                try {
//                    VersionBean versionBean = versionBeanDao.queryBuilder().where().eq("key", GlobalTypes.VERSION_SSL_FLAG).queryForFirst();
//                    if (versionBean != null && Boolean.parseBoolean(versionBean.getValue())) {
//                        String url = WSConstants.REST_TECHO_SERVICE_URL + relativePath;
//                        return url.replace("http:", "https:");
//                    }
//                } catch (SQLException e) {
//                    Log.e(getClass().getSimpleName(), null, e);
//                }
//                return WSConstants.REST_TECHO_SERVICE_URL + relativePath;
//
//            case WSConstants.ApiCalls.TECHO_NDHM_GENERATE_AADHAAR_OTP:
//            case WSConstants.ApiCalls.TECHO_NDHM_CREATE_USING_AADHAAR_OTP:
//            case WSConstants.ApiCalls.TECHO_NDHM_CREATE_USING_AADHAAR_DEMO:
//            case WSConstants.ApiCalls.TECHO_NDHM_HEALTH_ID_CARD:
//            case WSConstants.ApiCalls.TECHO_NDHM_SEARCH:
//            case TECHO_NDHM_AUTHENTICATION:
//            case WSConstants.ApiCalls.TECHO_NDHM_CONFIRM_AADHAAR_DEMO:
//            case TECHO_NDHM_CONFIRM_AADHAAR_OTP:
//            case TECHO_NDHM_CONFIRM_MOBILE_OTP:
//            case TECHO_NDHM_CONFIRM_PASSWORD:
//            case TECHO_NDHM_RESEND_OTP:
//            case TECHO_NDHM_STATES:
//            case WSConstants.ApiCalls.TECHO_NDHM_LINK_BENEFIT:
//                return WSConstants.REST_TECHO_NDHM_SERVICE_URL + relativePath;
//
//            default:
//        }
//        return WSConstants.REST_TECHO_SERVICE_URL + relativePath;
//    }

    @Override
    public String retrieveAndroidVersionFromServer() throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().retrieveAndroidVersionFromServer());
    }

    @Override
    public boolean retrieveServerIsAlive() throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().retrieveServerIsAlive());
    }

    @Override
    public Boolean retrieveUserInProductionFromServer(String userName) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setUserName(userName);

        return apiManager.execute(apiManager.getInstance().retrieveUserInProductionFromServer(mobileRequestParamDto));
    }

    @Override
    public Boolean retrieveUserInTrainingFromServer(String userName) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setUserName(userName);

        String url = WSConstants.CONTEXT_URL_TECHO_TRAINING + "api/mobile/" + WSConstants.ApiCalls.TECHO_IS_USER_IN_PRODUCTION;

        return apiManager.execute(apiManager.getInstance().retrieveUserInTrainingFromServer(url, mobileRequestParamDto));
    }

    @Override
    public UserInfoDataBean getUser(String username, String password, String firebaseToken, Boolean firstTimeLoggedIn) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setUserName(username);
        mobileRequestParamDto.setPassword(password);
        mobileRequestParamDto.setFirebaseToken(firebaseToken);
        mobileRequestParamDto.setFirstTimeLogin(firstTimeLoggedIn);
        return apiManager.execute(apiManager.getInstance().getUser(mobileRequestParamDto));
    }

    @Override
    public String uploadUncaughtExceptionToServer(Long userId, List<UncaughtExceptionBean> exceptionBeans) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setUserId(userId);
        mobileRequestParamDto.setUncaughtExceptionBeans(exceptionBeans);

        return apiManager.execute(apiManager.getInstance().uploadUncaughtExceptionToServer(mobileRequestParamDto));
    }

    @Override
    public RecordStatusBean[] recordEntryFromMobileToServer(String token, String[] records) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(token);
        mobileRequestParamDto.setRecords(records);

        return apiManager.execute(apiManager.getInstance().recordEntryFromMobileToServer(mobileRequestParamDto));
    }

    @Override
    public Boolean syncMergedFamiliesInformationWithServer(List<MergedFamiliesBean> mergedFamiliesBeans) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setMergedFamiliesBeans(mergedFamiliesBeans);

        return apiManager.execute(apiManager.getInstance().syncMergedFamiliesInformationWithServer(mobileRequestParamDto));
    }

    @Override
    public Map<String, FamilyDataBean> getFamilyToBeAssignedBySearchString(String searchString, Boolean searchByFamilyId) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setSearchString(searchString);
        mobileRequestParamDto.setSearchByFamilyId(searchByFamilyId);

        return apiManager.execute(apiManager.getInstance().getFamilyToBeAssignedBySearchString(mobileRequestParamDto));
    }

    @Override
    public FamilyDataBean assignFamilyToUser(String locationId, String familyId) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setLocationId(locationId);
        mobileRequestParamDto.setFamilyId(familyId);

        return apiManager.execute(apiManager.getInstance().assignFamilyToUser(mobileRequestParamDto));
    }

    @Override
    public List<FormAccessibilityBean> getUserFormAccessDetailFromServer() throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().getUserFormAccessDetailFromServer(mobileRequestParamDto));
    }

    @Override
    public void postUserReadyToMoveProduction(String formType) throws RestHttpException {
        if (!SewaUtil.isUserInTraining) {
            return;
        }

        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setFormCode(formType);

        apiManager.execute(apiManager.getInstance().postUserReadyToMoveProduction(mobileRequestParamDto));
    }

    @Override
    public LoggedInUserPrincipleDataBean getDetails(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getDetails(loginRequestParamDetailDataBean));
    }

    @Override
    public LoggedInUserPrincipleDataBean getDetailsForAsha(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getDetailsForAsha(loginRequestParamDetailDataBean));
    }

    @Override
    public LoggedInUserPrincipleDataBean getDetailsForAww(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getDetailsForAww(loginRequestParamDetailDataBean));
    }

    @Override
    public LoggedInUserPrincipleDataBean getDetailsForRbsk(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getDetailsForRbsk(loginRequestParamDetailDataBean));
    }

    @Override
    public LoggedInUserPrincipleDataBean getDetailsForFHSR(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getDetailsForFHSR(loginRequestParamDetailDataBean));
    }

    @Override
    public List<FamilyDataBean> getFamiliesByLocationId(Long locationId, String lastUpdateDate) throws RestHttpException {
        LoginRequestParamDetailDataBean requestParam = new LoginRequestParamDetailDataBean();
        requestParam.setToken(SewaTransformer.loginBean.getUserToken());
        requestParam.setLocationId(locationId);
        requestParam.setLastUpdateDateForFamily(lastUpdateDate);

        return apiManager.execute(apiManager.getInstance().getFamiliesByLocationId(requestParam));
    }

    @Override
    public Boolean getTokenValidity(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getTokenValidity(mobileRequestParamDto));
    }

    @Override
    public LoggedInUserPrincipleDataBean revalidateUserToken(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().revalidateUserToken(mobileRequestParamDto));
    }

    @Override
    public Map<String, String[]> getUserIdAndTokenFromToken(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().getUserIdAndTokenFromToken(mobileRequestParamDto));
    }

    @Override
    public QueryMobDataBean executeQuery(QueryMobDataBean queryMobDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().executeQuery(queryMobDataBean));
    }

    @Override
    public String storeRbskScreeningDetails(RbskScreeningDetailDto rbskScreeningDetailDto) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().storeRbskScreeningDetails(rbskScreeningDetailDto));
    }

    @Override
    public void sendOtpRequest(String mobileNumber) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setMobileNumber(mobileNumber);

        apiManager.execute(apiManager.getInstance().sendOtpRequest(mobileRequestParamDto));
    }

    @Override
    public Boolean verifyOtp(String mobileNumber, String otp) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setMobileNumber(mobileNumber);
        mobileRequestParamDto.setOtp(otp);

        return apiManager.execute(apiManager.getInstance().verifyOtp(mobileRequestParamDto));
    }

    @Override
    public Integer markAttendanceForTheDay(String gpsRecord) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setGpsRecords(gpsRecord);

        return apiManager.execute(apiManager.getInstance().markAttendanceForTheDay(mobileRequestParamDto));
    }

    @Override
    public void storeAttendanceForTheDay(String gpsRecords, Integer attendanceId) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setGpsRecords(gpsRecords);
        mobileRequestParamDto.setAttendanceId(attendanceId);

        apiManager.execute(apiManager.getInstance().storeAttendanceForTheDay(mobileRequestParamDto));
    }

    @Override
    public Map<String, Object> checkIfDeviceIsBlockedOrDeleteDatabase(String imei) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("imei", imei);

        return apiManager.execute(apiManager.getInstance().checkIfDeviceIsBlockedOrDeleteDatabase(requestParams));
    }

    @Override
    public void removeEntryForDeviceOfIMEI(String imei) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("imei", imei);

        apiManager.execute(apiManager.getInstance().removeEntryForDeviceOfIMEI(requestParams));
    }

    @Override
    public String storeOpdLabFormDetails(String answerString, Integer labTestDetId, String labTestVersion) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setLabTestDetId(labTestDetId);
        mobileRequestParamDto.setAnswerString(answerString);
        mobileRequestParamDto.setLabTestVersion(labTestVersion);

        return apiManager.execute(apiManager.getInstance().storeOpdLabFormDetails(mobileRequestParamDto));
    }

    @Override
    public LoggedInUserPrincipleDataBean syncData(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException {
        return apiManager.execute(apiManager.getInstance().syncData(loginRequestParamDetailDataBean));
    }

    @Override
    public Map<String, Boolean> getMetadata() throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().getMetadata(mobileRequestParamDto));
    }

    @Override
    public Map<String, String> generateAadhaarOTP(String aadhaar, Boolean isNoneGov, Integer memberId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("aadhaar", aadhaar);
        requestParams.put("memberId", memberId);

        if(isNoneGov)
            return apiManager.execute(apiManager.getInstance().generateAadhaarOTPForNonGov(requestParams));
        else
            return apiManager.execute(apiManager.getInstance().generateAadhaarOTP(requestParams));
    }

    @Override
    public Map<String, String> resendOtpNonGov(String txnId, String mobileNumber) throws RestHttpException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mobileNumber", mobileNumber);
        requestBody.put("txnId", txnId);

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().resendOtpNonGov(requestBody, requestParams));
    }

    @Override
    public Map<String, String> resendMobileOtpNonGov(String txnId, String mobileNumber) throws RestHttpException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mobile", mobileNumber);
        requestBody.put("txnId", txnId);

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().resendMobileOtpNonGov(requestBody, requestParams));
    }

    @Override
    public Map<String, String> aadhaarLinkedMobileOtpForNonGov(String txnId, String mobileNumber) throws RestHttpException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mobile", mobileNumber);
        requestBody.put("txnId", txnId);

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().aadhaarLinkedMobileOtpForNonGov(requestBody, requestParams));
    }

    @Override
    public Map<String, String> verifyMobileOtpForNonGov(String txnId, String otp) throws RestHttpException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("otp", otp);
        requestBody.put("txnId", txnId);

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().verifyMobileOtpForNonGov(requestBody, requestParams));
    }

    @Override
    public Boolean getIsNonGov() throws RestHttpException {
        System.out.println("in-================");
        return apiManager.execute(apiManager.getInstance().getIsNonGov());
    }

    @Override
    public Map<String, String> verifyAadhaarOTPForNonGov(String otp, String txnId) throws RestHttpException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("otp", otp);
        requestBody.put("txnId", txnId);

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().verifyAadhaarOTPForNonGov(requestBody, requestParams));
    }

    @Override
    public HealthIdAccountProfileResponse createUsingAadhaarOTP(Integer memberId, String mobile, String otp, String txnId, String featureName, String consentValues, Boolean isAadhaarDetailMatchConsentGiven) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("memberId", memberId);
        requestParams.put("mobile", mobile);
        requestParams.put("otp", otp);
        requestParams.put("txnId", txnId);
        requestParams.put("featureName", featureName);
        requestParams.put("consentValues", consentValues);
        requestParams.put("isAadhaarDetailMatchConsentGiven", isAadhaarDetailMatchConsentGiven);

        return apiManager.execute(apiManager.getInstance().createUsingAadhaarOTP(requestParams));
    }

    @Override
    public HealthIdAccountProfileResponseForNonGov createHealthIdForNonGov(String txnId,  Integer memberId, String mobile, String featureName, String consentValues, Boolean isAadhaarDetailMatchConsentGiven, String authMethodType) throws RestHttpException {
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("txnId", txnId);

        Map<String, Object> requestParams= new HashMap<>();
        requestParams.put("memberId", memberId);
        requestParams.put("mobile", mobile);
        requestParams.put("featureName", featureName);
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("consentValues", consentValues);
        requestParams.put("isAadhaarDetailMatchConsentGiven", isAadhaarDetailMatchConsentGiven);
        requestParams.put("authMethodType", authMethodType);

        return apiManager.execute(apiManager.getInstance().createHealthIdForNonGov(requestBody, requestParams));
    }

    @Override
    public HealthIdAccountProfileResponse createUsingAadharDemographics(Integer memberId, String aadhar, String dateOfBirth, String gender, String mobile, String name, String consentValues) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("memberId", memberId);
        requestParams.put("aadhaar", aadhar);
        requestParams.put("dateOfBirth", dateOfBirth);
        requestParams.put("gender", gender);
        requestParams.put("mobile", mobile);
        requestParams.put("name", name);
        requestParams.put("consentValues",consentValues);

        return apiManager.execute(apiManager.getInstance().createUsingAadharDemographics(requestParams));
    }

    @Override
    public HealthIdAccountProfileResponse createUsingAadhaarBiometrics(Integer memberId, String featureName, CreateHidAadharBioRequest request, String consentValues,  Boolean isAadhaarDetailMatchConsentGiven) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("memberId", memberId);
        requestParams.put("featureName", featureName);
        requestParams.put("consentValues",consentValues);
        requestParams.put("isAadhaarDetailMatchConsentGiven", isAadhaarDetailMatchConsentGiven);

        return apiManager.execute(apiManager.getInstance().createUsingAadhaarBiometrics(requestParams, request));
    }

    @Override
    public Map<String, String> verifyBiometricsForNonGov(CreateHidAadharBioRequest request) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());

        return apiManager.execute(apiManager.getInstance().verifyBiometricsForNonGov(requestParams, request));
    }


    @Override
    public Long saveHealthIdCard(String token, String healthIdNumber) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("token", token);
        requestParams.put("healthIdNumber", healthIdNumber);

        return apiManager.execute(apiManager.getInstance().saveHealthIdCard(requestParams));
    }

    @Override
    public byte[] saveHealthIdCardV2(String token, String healthIdNumber) throws RestHttpException, IOException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("token", token);
        requestParams.put("healthIdNumber", healthIdNumber);

        ResponseBody responseBody = apiManager.execute(apiManager.getInstance().saveHealthIdCardV2(requestParams));
        return responseBody.bytes();
    }


    @Override
    public HealthIdSearchResponse searchUserByHealthId(String healthId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("healthId", healthId);

        return apiManager.execute(apiManager.getInstance().searchUserByHealthId(requestParams));
    }

    @Override
    public Map<String, String> healthIdVerificationAuthInit(String healthId, String authMethod) throws RestHttpException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("healthid", healthId);
        requestData.put("authMethod", authMethod);

        return apiManager.execute(apiManager.getInstance().healthIdVerificationAuthInit(SewaTransformer.loginBean.getUserToken(), requestData));
    }

    @Override
    public Map<String, String> confirmAadharDemo(String txnId, String gender, String name, String yearOfBirth) throws RestHttpException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("txnId", txnId);
        requestData.put("gender", gender);
        requestData.put("name", name);
        requestData.put("yearOfBirth", yearOfBirth);

        return apiManager.execute(apiManager.getInstance().confirmAadharDemo(SewaTransformer.loginBean.getUserToken(), requestData));
    }

    @Override
    public Map<String, String> confirmAadharOtp(String txnId, String otp) throws RestHttpException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("txnId", txnId);
        requestData.put("otp", otp);

        return apiManager.execute(apiManager.getInstance().confirmAadharOtp(SewaTransformer.loginBean.getUserToken(), requestData));
    }

    @Override
    public Map<String, String> confirmMobileOtp(String txnId, String otp) throws RestHttpException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("txnId", txnId);
        requestData.put("otp", otp);

        return apiManager.execute(apiManager.getInstance().confirmMobileOtp(SewaTransformer.loginBean.getUserToken(), requestData));
    }

    @Override
    public Map<String, String> confirmPassword(String txnId, String password) throws RestHttpException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("txnId", txnId);
        requestData.put("password", password);

        return apiManager.execute(apiManager.getInstance().confirmPassword(SewaTransformer.loginBean.getUserToken(), requestData));
    }

    @Override
    public Boolean resendOtp(String txnId, String authMethod) throws RestHttpException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("txnId", txnId);
        requestData.put("authMethod", authMethod);

        return apiManager.execute(apiManager.getInstance().resendOtp(SewaTransformer.loginBean.getUserToken(), requestData));
    }

    @Override
    public StateResponse[] getStates() throws RestHttpException, UnsupportedEncodingException {

        return apiManager.execute(apiManager.getInstance().getStates(SewaTransformer.loginBean.getUserToken()));
    }

    @Override
    public LinkBenefitIdResponse linkBenefitId(Integer memberId, String userToken, String stateCode, String featureName, String authMethodType) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userMobileToken", SewaTransformer.loginBean.getUserToken());
        requestParams.put("memberId", memberId);
        requestParams.put("userToken", userToken);
        requestParams.put("stateCode", stateCode);
        requestParams.put("featureName", featureName);
        requestParams.put("authMethodType", authMethodType);

        return apiManager.execute(apiManager.getInstance().linkBenefitId(requestParams));
    }

    @Override
    public List<RecordStatusBean> lmsEventEntryFromMobileToDBeans(List<LmsEventBean> lmsEventBeans) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setMobileEvents(lmsEventBeans);

        return apiManager.execute(apiManager.getInstance().lmsEventEntryFromMobileToDBeans(mobileRequestParamDto));
    }

    @Override
    public void failedHealthIdData(OfflineHealthIdBean offlineHealthIdBean) throws RestHttpException {
        apiManager.execute(apiManager.getInstance().failedHealthIdData(offlineHealthIdBean));
    }

    @Override
    public void updateLanguagePreference(String preferredLanguage) throws RestHttpException {
        MobileRequestParamDto mobileRequestParamDto = new MobileRequestParamDto();
        mobileRequestParamDto.setToken(SewaTransformer.loginBean.getUserToken());
        mobileRequestParamDto.setLanguageCode(preferredLanguage);
        apiManager.execute(apiManager.getInstance().updateLanguagePreference(mobileRequestParamDto));
    }

    @Override
    public ScannedHealthDataResponse startDeviceScanForHealthData(String mmid, String cid) throws RestHttpException {
        String url = "https://dev-main.abhayparimitii.cloudns.asia/scan/start/" + mmid + "/" + cid + "";
        Call<ScannedHealthDataResponse> scannedHealthDataResponseCall = apiManager.getInstance().startDeviceScanForHealthData(url);
        listOfCalls.add(scannedHealthDataResponseCall);
        ScannedHealthDataResponse scannedHealthDataResponse = apiManager.execute(scannedHealthDataResponseCall);
        listOfCalls.remove(scannedHealthDataResponseCall);
        return scannedHealthDataResponse;
    }

    @Override
    public ChardhamTouristScreeningDataBean retrieveScreeningByDevice(String mmId, String uniqueId, String scanId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("mmId", mmId);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("scanId", scanId);
        Call<ChardhamTouristScreeningDataBean> chardhamTouristScreeningDataBeanCall = apiManager.getInstance().retrieveScreeningByDevice(requestParams);
        listOfCalls.add(chardhamTouristScreeningDataBeanCall);
        ChardhamTouristScreeningDataBean chardhamTouristScreeningDataBean = apiManager.execute(chardhamTouristScreeningDataBeanCall);
        listOfCalls.remove(chardhamTouristScreeningDataBeanCall);
        return chardhamTouristScreeningDataBean;
    }

    @Override
    public List<ChardhamEmergencyUserDataBean> getEmergencyUsers(String type, Integer healthInfraId, Integer requestId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("type", type);
        requestParams.put("healthInfraId", healthInfraId);
        if (requestId != null) {
            requestParams.put("requestId", requestId);
        }
        return apiManager.execute(apiManager.getInstance().getEmergencyUsers(requestParams));
    }

    @Override
    public Map<String, Integer> generateEmergencyRequest(
            Integer requestId,
            Integer healthInfraId,
            String requestDescription,
            String requestToType,
            Integer userId) throws RestHttpException {
        ChardhamEmergencyRequestDto chardhamEmergencyRequestDto = new ChardhamEmergencyRequestDto();
        if (requestId != null) {
            chardhamEmergencyRequestDto.setRequestId(requestId);
        }
        chardhamEmergencyRequestDto.setHealthInfraId(healthInfraId);
        chardhamEmergencyRequestDto.setRequestDescription(requestDescription);
        chardhamEmergencyRequestDto.setRequestToType(requestToType);
        chardhamEmergencyRequestDto.setUserId(userId);
        return apiManager.execute(apiManager.getInstance().generateEmergencyRequest(chardhamEmergencyRequestDto));
    }

    @Override
    public ChardhamEmergencyDataBean getEmergencyRequestStatus(Integer requestId, Integer responseId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("requestId", requestId);
        requestParams.put("responseId", responseId);
        return apiManager.execute(apiManager.getInstance().getEmergencyRequestStatus(requestParams));
    }

    @Override
    public List<ChardhamEmergencyDataBean> getEmergencyRequestsByHealthInfraId(Integer healthInfraId, String state, String filterDateFrom, String filterDateTo, Integer limit, Integer offset) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("healthInfraId", healthInfraId);
        requestParams.put("limit", limit);
        requestParams.put("offset", offset);
        if (state != null) {
            requestParams.put("state", state);
        }
        if (filterDateFrom != null) {
            requestParams.put("filterDateFrom", filterDateFrom);
        }
        if (filterDateTo != null) {
            requestParams.put("filterDateTo", filterDateTo);
        }
        return apiManager.execute(apiManager.getInstance().getEmergencyRequestsByHealthInfraId(requestParams));
    }

    @Override
    public List<ChardhamEmergencyDataBean> getEmergencyRequestsByUserId(Integer userId, String state, String filterDateFrom, String filterDateTo, Integer limit, Integer offset) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userId", userId);
        requestParams.put("limit", limit);
        requestParams.put("offset", offset);
        if (state != null) {
            requestParams.put("state", state);
        }
        if (filterDateFrom != null) {
            requestParams.put("filterDateFrom", filterDateFrom);
        }
        if (filterDateTo != null) {
            requestParams.put("filterDateTo", filterDateTo);
        }
        return apiManager.execute(apiManager.getInstance().getEmergencyRequestsByUserId(requestParams));
    }

    @Override
    public ChardhamEmergencyDataBean updateEmergencyResponse(Integer responseId, String responseState, String rejectionReason) throws RestHttpException {
        ChardhamEmergencyResponseDto chardhamEmergencyResponseDto = new ChardhamEmergencyResponseDto();
        chardhamEmergencyResponseDto.setResponseId(responseId);
        chardhamEmergencyResponseDto.setResponseState(responseState);
        chardhamEmergencyResponseDto.setRejectionReason(rejectionReason);
        return apiManager.execute(apiManager.getInstance().updateEmergencyResponse(chardhamEmergencyResponseDto));
    }

    @Override
    public void updateEmergencyRequest(Integer requestId,
                                       String requestState,
                                       String completedRemarks) throws RestHttpException {
        ChardhamEmergencyRequestDto chardhamEmergencyRequestDto = new ChardhamEmergencyRequestDto();
        chardhamEmergencyRequestDto.setRequestId(requestId);
        chardhamEmergencyRequestDto.setRequestState(requestState);
        chardhamEmergencyRequestDto.setCompletedRemarks(completedRemarks);
        apiManager.execute(apiManager.getInstance().updateEmergencyRequest(chardhamEmergencyRequestDto));
    }

    @Override
    public void saveEmergencyResponderLocation(Integer userId,
                                               String latitude,
                                               String longitude) throws RestHttpException {
        ChardhamEmergencyResponderLocationDto chardhamEmergencyResponderLocationDto = new ChardhamEmergencyResponderLocationDto();
        chardhamEmergencyResponderLocationDto.setUserId(userId);
        chardhamEmergencyResponderLocationDto.setLatitude(latitude);
        chardhamEmergencyResponderLocationDto.setLongitude(longitude);
        apiManager.execute(apiManager.getInstance().saveEmergencyResponderLocation(chardhamEmergencyResponderLocationDto));
    }

    @Override
    public List<ChardhamTouristScreeningDto> getChardhamTouristScreeningsByHealthInfraId(Integer healthInfraId, String uniqueId, String filterDateFrom, String filterDateTo, Integer limit, Integer offset) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("limit", limit);
        requestParams.put("offset", offset);
        if (healthInfraId != null) {
            requestParams.put("healthInfraId", healthInfraId);
        }
        if (uniqueId != null) {
            requestParams.put("uniqueId", uniqueId);
        }
        if (filterDateFrom != null) {
            requestParams.put("filterDateFrom", filterDateFrom);
        }
        if (filterDateTo != null) {
            requestParams.put("filterDateTo", filterDateTo);
        }
        return apiManager.execute(apiManager.getInstance().getChardhamTouristScreeningsByHealthInfraId(requestParams));
    }

    @Override
    public void addFireBaseToken(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException {
        apiManager.execute(apiManager.getInstance().addFireBaseToken(mobileRequestParamDto));
    }

    @Override
    public List<ChardhamTouristsDataBean> getTouristMembersOnline(String uniqueId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("uniqueId", uniqueId);
        return apiManager.execute(apiManager.getInstance().getTouristMembersOnline(requestParams));
    }

    @Override
    public List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(Integer healthInfraId, Integer limit, Integer offset) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("healthInfraId", healthInfraId);
        requestParams.put("limit", limit);
        requestParams.put("offset", offset);
        return apiManager.execute(apiManager.getInstance().getAnnouncementsByHealthInfra(requestParams));
    }

    @Override
    public Map<String, Integer> getAnnouncementsUnreadCountByHealthInfra(Integer healthInfraId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        if (healthInfraId != null) {
            requestParams.put("healthInfraId", healthInfraId);
        }
        return apiManager.execute(apiManager.getInstance().getAnnouncementsUnreadCountByHealthInfra(requestParams));
    }

    @Override
    public void markAnnouncementAsSeen(Integer announcementId, Integer healthInfraId) throws RestHttpException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("announcementId", announcementId);
        requestParams.put("healthInfraId", healthInfraId);
        apiManager.execute(apiManager.getInstance().markAnnouncementAsSeen(requestParams));
    }

    @Override
    public void saveConsentForRedTourists(String memberUniqueId, String checksum, boolean isTouristWillingToContinue) throws RestHttpException {
        apiManager.execute(apiManager.getInstance().saveConsentForRedTourists(memberUniqueId, checksum, isTouristWillingToContinue));
    }
}
