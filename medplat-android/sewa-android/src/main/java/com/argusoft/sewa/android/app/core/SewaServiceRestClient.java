package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.AnnouncementMobDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyUserDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamTouristScreeningDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamTouristScreeningDto;
import com.argusoft.sewa.android.app.databean.ChardhamTouristsDataBean;
import com.argusoft.sewa.android.app.databean.CreateHidAadharBioRequest;
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
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.LmsEventBean;
import com.argusoft.sewa.android.app.model.MergedFamiliesBean;
import com.argusoft.sewa.android.app.model.UncaughtExceptionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author Alpesh
 */

public interface SewaServiceRestClient {

    UserInfoDataBean getUser(String username, String password, String firebaseToken, Boolean firstTimeLoggedIn) throws RestHttpException;

    RecordStatusBean[] recordEntryFromMobileToServer(String token, String[] records) throws RestHttpException;

    String uploadUncaughtExceptionToServer(Long userId, List<UncaughtExceptionBean> exceptionBeans) throws RestHttpException;

    String retrieveAndroidVersionFromServer() throws RestHttpException;

    boolean retrieveServerIsAlive() throws RestHttpException;

    Boolean retrieveUserInProductionFromServer(String userName) throws RestHttpException;

    Boolean retrieveUserInTrainingFromServer(String userName) throws RestHttpException;

    Boolean syncMergedFamiliesInformationWithServer(List<MergedFamiliesBean> mergedFamiliesBeans) throws RestHttpException;

    Map<String, FamilyDataBean> getFamilyToBeAssignedBySearchString(String searchString, Boolean searchByFamilyId) throws RestHttpException;

    FamilyDataBean assignFamilyToUser(String locationId, String familyId) throws RestHttpException;

    List<FormAccessibilityBean> getUserFormAccessDetailFromServer() throws RestHttpException;

    void postUserReadyToMoveProduction(String formType) throws RestHttpException;

    LoggedInUserPrincipleDataBean getDetails(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException;

    LoggedInUserPrincipleDataBean getDetailsForAsha(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException;

    LoggedInUserPrincipleDataBean getDetailsForAww(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException;

    LoggedInUserPrincipleDataBean getDetailsForRbsk(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException;

    LoggedInUserPrincipleDataBean getDetailsForFHSR(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException;

    List<FamilyDataBean> getFamiliesByLocationId(Long locationId, String lastUpdateDate) throws RestHttpException;

    Boolean getTokenValidity(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException;

    LoggedInUserPrincipleDataBean revalidateUserToken(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException;

    Map<String, String[]> getUserIdAndTokenFromToken(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException;

    QueryMobDataBean executeQuery(QueryMobDataBean queryMobDataBean) throws RestHttpException;

    String storeRbskScreeningDetails(RbskScreeningDetailDto rbskScreeningDetailDto) throws RestHttpException;

    void sendOtpRequest(String mobileNumber) throws RestHttpException;

    Boolean verifyOtp(String mobileNumber, String otp) throws RestHttpException;

    Integer markAttendanceForTheDay(String gpsRecord) throws RestHttpException;

    void storeAttendanceForTheDay(String gpsRecords, Integer attendanceId) throws RestHttpException;

    Map<String, Object> checkIfDeviceIsBlockedOrDeleteDatabase(String imei) throws RestHttpException;

    void removeEntryForDeviceOfIMEI(String imei) throws RestHttpException;

    String storeOpdLabFormDetails(String answerString, Integer labTestDetId, String labTestVersion) throws RestHttpException;

    LoggedInUserPrincipleDataBean syncData(LoginRequestParamDetailDataBean loginRequestParamDetailDataBean) throws RestHttpException;

    Map<String, Boolean> getMetadata() throws RestHttpException;

    Map<String, String> generateAadhaarOTP(String aadhaar, Boolean isNoneGov, Integer memberId) throws RestHttpException;

    Map<String, String> resendOtpNonGov(String txnId, String mobileNumber) throws RestHttpException;

    Map<String, String> verifyAadhaarOTPForNonGov(String otp, String txnId) throws RestHttpException;

    Map<String, String> aadhaarLinkedMobileOtpForNonGov(String txnId, String mobileNumber) throws RestHttpException;

    Boolean getIsNonGov() throws RestHttpException;

    HealthIdAccountProfileResponseForNonGov createHealthIdForNonGov(String txnId, Integer memberId, String mobile, String featureName, String consentValues, Boolean isAadhaarDetailMatchConsentGiven, String authMethodType) throws RestHttpException;

    Map<String, String> verifyMobileOtpForNonGov(String txnId, String otp) throws RestHttpException;

    Map<String, String> resendMobileOtpNonGov(String txnId, String mobileNumber) throws RestHttpException;

    HealthIdAccountProfileResponse createUsingAadhaarOTP(Integer memberId, String mobile, String otp, String txnId, String featureName, String consentValues, Boolean isAadhaarDetailMatchConsentGiven) throws RestHttpException;

    HealthIdAccountProfileResponse createUsingAadharDemographics(Integer memberId, String aadhar, String dateOfBirth, String gender, String mobile, String name, String consentValues) throws RestHttpException;

    HealthIdAccountProfileResponse createUsingAadhaarBiometrics(Integer memberId, String featureName, CreateHidAadharBioRequest request, String consentValues, Boolean isAadhaarDetailMatchConsentGiven) throws RestHttpException;

    Map<String, String> verifyBiometricsForNonGov(CreateHidAadharBioRequest request) throws RestHttpException;

    Long saveHealthIdCard(String token, String healthIdNumber) throws RestHttpException;

    byte[] saveHealthIdCardV2(String token, String healthIdNumber) throws RestHttpException, IOException;

    HealthIdSearchResponse searchUserByHealthId(String healthId) throws RestHttpException;

    Map<String, String> healthIdVerificationAuthInit(String healthId, String authMethod) throws RestHttpException, UnsupportedEncodingException;

    Map<String, String> confirmAadharDemo(String txnId, String gender, String name, String yearOfBirth) throws RestHttpException, UnsupportedEncodingException;

    Map<String, String> confirmAadharOtp(String txnId, String otp) throws RestHttpException, UnsupportedEncodingException;

    Map<String, String> confirmMobileOtp(String txnId, String otp) throws RestHttpException, UnsupportedEncodingException;

    Map<String, String> confirmPassword(String txnId, String password) throws RestHttpException, UnsupportedEncodingException;

    Boolean resendOtp(String txnId, String authMethod) throws RestHttpException, UnsupportedEncodingException;

    StateResponse[] getStates() throws RestHttpException, UnsupportedEncodingException;

    LinkBenefitIdResponse linkBenefitId(Integer memberId, String userToken, String stateCode, String featureName, String authMethodType) throws RestHttpException;

    List<RecordStatusBean> lmsEventEntryFromMobileToDBeans(List<LmsEventBean> lmsEventBeans) throws RestHttpException;

    void failedHealthIdData(OfflineHealthIdBean offlineHealthIdBean) throws RestHttpException;

    void updateLanguagePreference(String preferredLanguage) throws RestHttpException;

    ScannedHealthDataResponse startDeviceScanForHealthData(String mmid, String cid) throws RestHttpException;

    ChardhamTouristScreeningDataBean retrieveScreeningByDevice(String mmId, String uniqueId, String scanId) throws RestHttpException;

    List<ChardhamEmergencyUserDataBean> getEmergencyUsers(String type, Integer healthInfraId, Integer requestId) throws RestHttpException;

    Map<String, Integer> generateEmergencyRequest(Integer requestId,
                                                  Integer healthInfraId,
                                                  String requestDescription,
                                                  String requestToType,
                                                  Integer userId) throws RestHttpException;

    ChardhamEmergencyDataBean getEmergencyRequestStatus(Integer requestId, Integer responseId) throws RestHttpException;

    List<ChardhamEmergencyDataBean> getEmergencyRequestsByHealthInfraId(Integer healthInfraId, String state, String filterDateFrom, String filterDateTo, Integer limit, Integer offset) throws RestHttpException;

    List<ChardhamEmergencyDataBean> getEmergencyRequestsByUserId(Integer userId, String state, String filterDateFrom, String filterDateTo, Integer limit, Integer offset) throws RestHttpException;

    ChardhamEmergencyDataBean updateEmergencyResponse(Integer responseId, String responseState, String rejectionReason) throws RestHttpException;

    void updateEmergencyRequest(Integer requestId,
                                String requestState,
                                String completedRemarks) throws RestHttpException;

    void saveEmergencyResponderLocation(Integer userId,
                                        String latitude,
                                        String longitude) throws RestHttpException;

    List<ChardhamTouristScreeningDto> getChardhamTouristScreeningsByHealthInfraId(Integer healthInfraId, String uniqueId, String filterDateFrom, String filterDateTo, Integer limit, Integer offset) throws RestHttpException;

    void addFireBaseToken(MobileRequestParamDto mobileRequestParamDto) throws RestHttpException;

    List<ChardhamTouristsDataBean> getTouristMembersOnline(String uniqueId) throws RestHttpException;

    List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(Integer healthInfraId, Integer limit, Integer offset) throws RestHttpException;

    Map<String, Integer> getAnnouncementsUnreadCountByHealthInfra(Integer healthInfraId) throws RestHttpException;

    void markAnnouncementAsSeen(Integer announcementId, Integer healthInfraId) throws RestHttpException;

    void saveConsentForRedTourists(String memberUniqueId, String checksum, boolean isTouristWillingToContinue) throws RestHttpException;
}
