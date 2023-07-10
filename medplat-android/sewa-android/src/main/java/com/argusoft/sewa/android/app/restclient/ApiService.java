package com.argusoft.sewa.android.app.restclient;

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
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    @GET("api/mobile/retrieveAndroidVersion")
    Call<String> retrieveAndroidVersionFromServer();

    @GET("api/mobile/getserverisalive")
    Call<Boolean> retrieveServerIsAlive();

    @POST("api/mobile/techoisuserinproduction")
    Call<Boolean> retrieveUserInProductionFromServer(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST
    Call<Boolean> retrieveUserInTrainingFromServer(@Url String url, @Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techovalidateusernew")
    Call<UserInfoDataBean> getUser(@Body MobileRequestParamDto mobileRequestParamDto);

    @PUT("api/mobile/techouploaduncaughtexceptiondetail")
    Call<String> uploadUncaughtExceptionToServer(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techorecordentryfrommobiletodbservernew")
    Call<RecordStatusBean[]> recordEntryFromMobileToServer(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techopostmergedfamiliesinformation")
    Call<Boolean> syncMergedFamiliesInformationWithServer(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techogetfamilybysearchstring")
    Call<Map<String, FamilyDataBean>> getFamilyToBeAssignedBySearchString(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techopostassignfamilytouser")
    Call<FamilyDataBean> assignFamilyToUser(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techogetuserformaccessdetail")
    Call<List<FormAccessibilityBean>> getUserFormAccessDetailFromServer(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techopostuserreadytomoveproduction")
    Call<List<FormAccessibilityBean>> postUserReadyToMoveProduction(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/getDetails")
    Call<LoggedInUserPrincipleDataBean> getDetails(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/getdetailsasha")
    Call<LoggedInUserPrincipleDataBean> getDetailsForAsha(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/getdetailsaww")
    Call<LoggedInUserPrincipleDataBean> getDetailsForAww(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/getdetailsrbsk")
    Call<LoggedInUserPrincipleDataBean> getDetailsForRbsk(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/getdetailsfhsr")
    Call<LoggedInUserPrincipleDataBean> getDetailsForFHSR(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/getfamiliesbylocation")
    Call<List<FamilyDataBean>> getFamiliesByLocationId(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/requestParam")
    Call<Boolean> getTokenValidity(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techorevalidatetoken")
    Call<LoggedInUserPrincipleDataBean> revalidateUserToken(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/techogetuserfromtoken")
    Call<Map<String, String[]>> getUserIdAndTokenFromToken(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/getdata")
    Call<QueryMobDataBean> executeQuery(@Body QueryMobDataBean queryMobDataBean);

    @POST("api/mobile/rbskscreeningdetails")
    Call<String> storeRbskScreeningDetails(@Body RbskScreeningDetailDto rbskScreeningDetailDto);

    @POST("api/mobile/generateotptecho")
    Call<ResponseBody> sendOtpRequest(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/verifyotptecho")
    Call<Boolean> verifyOtp(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/markattendance")
    Call<Integer> markAttendanceForTheDay(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/storeattendance")
    Call<ResponseBody> storeAttendanceForTheDay(@Body MobileRequestParamDto mobileRequestParamDto);

    @GET("api/mobile/getimeiblockedordeletedatabase")
    Call<Map<String, Object>> checkIfDeviceIsBlockedOrDeleteDatabase(@QueryMap Map<String, Object> stringObjectMap);

    @GET("api/mobile/removeimeiblockedentry")
    Call<ResponseBody> removeEntryForDeviceOfIMEI(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/storeOpdLabTest")
    Call<String> storeOpdLabFormDetails(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/syncData")
    Call<LoggedInUserPrincipleDataBean> syncData(@Body LoginRequestParamDetailDataBean loginRequestParamDetailDataBean);

    @POST("api/mobile/getMetadata")
    Call<Map<String, Boolean>> getMetadata(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/lmsevententry")
    Call<List<RecordStatusBean>> lmsEventEntryFromMobileToDBeans(@Body MobileRequestParamDto mobileRequestParamDto);

    @POST("api/mobile/failedHealthIdData")
    Call<ResponseBody> failedHealthIdData(@Body OfflineHealthIdBean offlineHealthIdBean);

    @Streaming
    @GET
    Call<ResponseBody> getFile(@Url String url);

    /*
     * NDHM API's
     */
    @POST("api/mobile/ndhm/healthid/generate-aadhar-otp")
    Call<Map<String, String>> generateAadhaarOTP(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/generate-aadhar-otp-non-gov")
    Call<Map<String, String>> generateAadhaarOTPForNonGov(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/create-using-aadhar-otp")
    Call<HealthIdAccountProfileResponse> createUsingAadhaarOTP(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/verify-aadhaar-otp-non-gov")
    Call<Map<String, String>> verifyAadhaarOTPForNonGov(@Body Map<String, Object> stringObjectMap, @QueryMap Map<String, Object> stringObjectMap2);

    @POST("api/mobile/ndhm/healthid/resend-aadhar-otp-non-gov")
    Call<Map<String, String>> resendOtpNonGov(@Body Map<String, Object> stringObjectMap, @QueryMap Map<String, Object> stringObjectMap2);

    @POST("api/mobile/ndhm/healthid/aadhaar-linked-mobile-otp-non-gov")
    Call<Map<String, String>> aadhaarLinkedMobileOtpForNonGov(@Body Map<String, Object> stringObjectMap, @QueryMap Map<String, Object> stringObjectMap2);

    @POST("api/mobile/ndhm/healthid/generate-mobile-otp-non-gov")
    Call<Map<String, String>> resendMobileOtpNonGov(@Body Map<String, Object> stringObjectMap, @QueryMap Map<String, Object> stringObjectMap2);

    @POST("api/mobile/ndhm/healthid/verify-mobile-otp-non-gov")
    Call<Map<String, String>> verifyMobileOtpForNonGov(@Body Map<String, Object> stringObjectMap, @QueryMap Map<String, Object> stringObjectMap2);

    @GET("api/mobile/ndhm/healthid/get-is-non-gov")
    Call<Boolean> getIsNonGov();

    @POST("api/mobile/ndhm/healthid/generate-abha-using-preverfied-gov")
    Call<HealthIdAccountProfileResponseForNonGov> createHealthIdForNonGov(@Body Map<String, Object> stringObjectMap, @QueryMap Map<String, Object> stringObjectMap2);

    @POST("api/mobile/ndhm/healthid/create-using-aadhaar-bio")
    Call<HealthIdAccountProfileResponse> createUsingAadhaarBiometrics(@QueryMap Map<String, Object> stringObjectMap, @Body CreateHidAadharBioRequest request);

    @POST("api/mobile/ndhm/healthid/verify-biometric-otp-non-gov")
    Call<Map<String, String>> verifyBiometricsForNonGov(@QueryMap Map<String, Object> stringObjectMap, @Body CreateHidAadharBioRequest request);

    @POST("api/mobile/ndhm/healthid/create-using-aadhar-demo")
    Call<HealthIdAccountProfileResponse> createUsingAadharDemographics(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/heath-id-card")
    Call<Long> saveHealthIdCard(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/heath-id-card-v2")
    Call<ResponseBody> saveHealthIdCardV2(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/search")
    Call<HealthIdSearchResponse> searchUserByHealthId(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/authentication")
    Call<Map<String, String>> healthIdVerificationAuthInit(@Query("userMobileToken") String userMobileToken, @Body Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/confirm-aadhar-demo")
    Call<Map<String, String>> confirmAadharDemo(@Query("userMobileToken") String userMobileToken, @Body Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/confirm-aadhar-otp")
    Call<Map<String, String>> confirmAadharOtp(@Query("userMobileToken") String userMobileToken, @Body Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/confirm-mobile-otp")
    Call<Map<String, String>> confirmMobileOtp(@Query("userMobileToken") String userMobileToken, @Body Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/confirm-password")
    Call<Map<String, String>> confirmPassword(@Query("userMobileToken") String userMobileToken, @Body Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/resend-otp")
    Call<Boolean> resendOtp(@Query("userMobileToken") String userMobileToken, @Body Map<String, Object> stringObjectMap);

    @POST("api/mobile/ndhm/healthid/states")
    Call<StateResponse[]> getStates(@Query("userMobileToken") String userMobileToken);

    @POST("api/mobile/ndhm/healthid/link-benefit")
    Call<LinkBenefitIdResponse> linkBenefitId(@QueryMap Map<String, Object> headeObjectMap);

    @POST("api/mobile/user/changeLanguage")
    Call<ResponseBody> updateLanguagePreference(@Body MobileRequestParamDto mobileRequestParamDto);

    //Device ID (MMID) and User ID (CID)
    @GET
    Call<ScannedHealthDataResponse> startDeviceScanForHealthData(@Url String url);

    @GET("api/mobile/chardham/retrievescreeningbydevice")
    Call<ChardhamTouristScreeningDataBean> retrieveScreeningByDevice(@QueryMap Map<String, Object> stringObjectMap);

    @GET("api/chardham/emergency/users")
    Call<List<ChardhamEmergencyUserDataBean>> getEmergencyUsers(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/chardham/emergency/request")
    Call<Map<String, Integer>> generateEmergencyRequest(@Body ChardhamEmergencyRequestDto chardhamEmergencyRequestDto);

    @GET("api/chardham/emergency/requeststatus")
    Call<ChardhamEmergencyDataBean> getEmergencyRequestStatus(@QueryMap Map<String, Object> stringObjectMap);

    @GET("api/chardham/emergency/requestbyhealthinfra")
    Call<List<ChardhamEmergencyDataBean>> getEmergencyRequestsByHealthInfraId(@QueryMap Map<String, Object> stringObjectMap);

    @GET("api/chardham/emergency/requestbyuser")
    Call<List<ChardhamEmergencyDataBean>> getEmergencyRequestsByUserId(@QueryMap Map<String, Object> stringObjectMap);

    @PUT("api/chardham/emergency/response")
    Call<ChardhamEmergencyDataBean> updateEmergencyResponse(@Body ChardhamEmergencyResponseDto chardhamEmergencyResponseDto);

    @PUT("api/chardham/emergency/request")
    Call<ResponseBody> updateEmergencyRequest(@Body ChardhamEmergencyRequestDto chardhamEmergencyRequestDto);

    @POST("api/chardham/emergency/savelocation")
    Call<ResponseBody> saveEmergencyResponderLocation(@Body ChardhamEmergencyResponderLocationDto chardhamEmergencyResponderLocationDto);

    @GET("api/chardham/screening/getscreenings")
    Call<List<ChardhamTouristScreeningDto>> getChardhamTouristScreeningsByHealthInfraId(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/mobile/firebaseToken")
    Call<ResponseBody> addFireBaseToken(@Body MobileRequestParamDto mobileRequestParamDto);

    @GET("api/chardham/screening/getmember")
    Call<List<ChardhamTouristsDataBean>> getTouristMembersOnline(@QueryMap Map<String, Object> stringObjectMap);

    @GET("api/mobile/announcements/byhealthinfra")
    Call<List<AnnouncementMobDataBean>> getAnnouncementsByHealthInfra(@QueryMap Map<String, Object> stringObjectMap);

    @GET("api/mobile/announcements/unreadcount")
    Call<Map<String, Integer>> getAnnouncementsUnreadCountByHealthInfra(@QueryMap Map<String, Object> stringObjectMap);

    @PUT("api/mobile/announcements/markseen")
    Call<ResponseBody> markAnnouncementAsSeen(@QueryMap Map<String, Object> stringObjectMap);

    @POST("api/chardham/screening/updatetouristwillingness")
    Call<ResponseBody> saveConsentForRedTourists(@Query("memberUniqueId") String memberUniqueId, @Query("checksum") String checksum, @Query("isTouristWillingToContinue") boolean isTouristWillingToContinue);
}
