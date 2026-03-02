/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.constants;

/**
 * @author kunjan
 */
public class MobileApiPathConstants {

    private MobileApiPathConstants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String VALIDATE_USER = "validateUser";
    public static final String GET_ALL_XMLS_DATA_IN_MOBILE = "getAllXlsDataInMobile";
    public static final String RECORD_ENTRY_MOBILE_TO_DB_SERVER = "recordEntryFromMobileToDBServerNew";
    public static final String VALIDATE_UNIQUE_CODE = "validateUniqueCode";
    public static final String GET_ALL_SURVEY_LOCATION = "getAllSurveyLocation";
    public static final String GET_ALL_END_LINE_SURVEY_LOCATION = "getAllEndlineSurveyLocation";
    public static final String UPLOAD_DATA_USAGE_TO_SERVER_BY_DAY = "uploadDataUsageToServerByDay";
    public static final String UPLOAD_DATA_USAGE_TO_SERVER_BY_APP = "uploadDataUsageDetailByApp";
    public static final String UPLOAD_USER_LOCATION_DETAIL = "uploadUserLocationDetail";
    public static final String UPLOAD_UNCAUGHT_EXCEPTION_DETAIL = "uploadUncaughtExceptionDetail";
    /// Sewa LAble Services Methods
    public static final String GET_LABEL_BY_LANG_ON_DATE = "getLabelsByLanguageChangedAfterDate";
    public static final String GET_SETU_LABEL_BY_LANG_ON_DATE = "getLabelsByLanguageChangedAfterDateForSetu";
    public static final String GET_FIELD_VALUES_ON_DATE = "getFieldValuesAfterDate";
    public static final String GET_INVENTORIES = "retriveInventories";
    public static final String GET_INCENTIVE_DETAILS_FOR_ASHA = "retriveIncentiveDetailsForAsha";
    public static final String GET_FILE_DATA_FOR_ANNOUNCEMENT = "getFileDataForAnnouncement";
    public static final String GET_LIST_VALUE_DATA = "readListValueData";
    public static final String GET_ANNOUNCEMENT = "readAnnouncement";
    public static final String GET_CATEG_BENF_UNDER_AREA_OF_INTER_OF_ASHA = "retrieveCategorizedBeneficiarysUnderAreaOfInterventionOfAsha";
    public static final String GET_NOTIFICATION_BY_ASHA = "retrieveNotificationByAshaUserName";
    public static final String GET_NOTIFICATION_BY_SETU = "retrieveNotificationBySetuUserName";
    public static final String GET_DATA_POOL = "retrieveDataPool";
    public static final String GET_DATA_POOL_ERT = "retrieveDataPoolForERT";
    public static final String GET_DATA_POOL_SETU = "retrieveDataPoolForSETU";
    public static final String GET_ASSIGNED_BENEFICIARIES = "retrieveAssignedBeneficiaries";
    public static final String GET_INCENTIVE_PAYMENT_DATA = "retrieveIncentivePaymentData";
    public static final String GET_MCH_LIST = "retrieveMCHList";
    public static final String GET_ANDROID_VERSION = "retrieveAndroidVersion";
    public static final String GET_SERVER_IS_ALIVE = "getserverisalive";
    public static final String IS_USER_IN_PRODUCTION = "isUserInProduction";
    public static final String GET_PENDING_SERVICES = "retrievePendingServices";
    public static final String GET_SHEET_VERSION_BY_ROLE = "retrieveSheetVersionByRole";
    public static final String GET_FONT_SIZE = "retrieveFontSize";
    //added for FHW Module
    public static final String GET_CLIENT_DETAIL_FOR_FHW = "retrieveClientDetailForFHW";
    public static final String GET_NOTIFICATIONS_BY_FHW_USER = "retrieveAllNotificationByFHWUser";
    public static final String GET_DUE_SERVICES_FOR_FHW = "retrieveDueServicesForFHW";
    public static final String GET_ASHA_MONTHLY_REVIEW_INFO_FHW = "retrieveAshaMonthlyReviewInfoFHW";
    public static final String GET_PERFORMANCE_INFO_FHW = "retrievePerformanceInfoFHW";
    public static final String GET_FHW_SERVICE_DETAIL_BEAN = "retrieveFhwServiceDetailBean";

    //for FHS Module
    public static final String GET_ASSIGNED_FAMILIES_FHS = "retrieveAssignedFamiliesForFHS";
    public static final String GET_SUBAREAS_BY_LOCATION = "retrieveSubAreasByLocation";
    public static final String GET_LOCATION_BEANS_ASSIGNED_TO_USER = "retrieveLocationBeansAssignedToUser";
    public static final String GET_ORPHANED_REVERIFICATION_FAMILIES_FHS = "retrieveOrphanedReverificationFamiliesForFHS";
    public static final String GET_AREA_ASHA_MAPPING = "retrieveAreaAshaMapping";
    public static final String POST_AADHAR_UPDATE_DETAILS = "postAadharUpdateDetails";
    public static final String GET_UPDATED_FAMILY_DATA = "getUpdatedFamilyData";
    public static final String POST_MERGED_FAMILIES_INFORMATION = "postMergedFamiliesInformation";
    public static final String GET_FAMILY_TO_BE_ASSIGNED_BY_SEARCH_STRING = "getFamilyToBeAssignedBySearchString";
    public static final String POST_ASSIGN_FAMILY_TO_USER = "postAssignFamilyToUser";

    public static final String GET_USER_FORM_ACCESS_DETAIL = "getUserFormAccessDetail";
    public static final String POST_USER_READY_TO_MOVE_PRODUCTION = "postUserReadyToMoveProduction";

    //for RCH FHW Module
    public static final String GET_NOTIFICATIONS = "getNotifications";

    public static final String SYNC_DATA = "syncData";
    public static final String GET_DETAILS_FHW = "getDetails";
    public static final String GET_DETAILS_ASHA = "getdetailsasha";
    public static final String GET_DETAILS_FHSR = "getdetailsfhsr";
    public static final String GET_FAMILIES_BY_LOCATION = "getfamiliesbylocation";
    public static final String GET_FILE = "getFile";
    public static final String DOWNLOAD_LIBRARY_FILE = "downloadlibraryfile";
    public static final String DOWNLOAD_APPLICATION = "downloadapk";

    public static final String GET_METADATA = "getMetadata";

    //for Migration
    public static final String POST_SYNC_MIGRATION_DETAILS = "postSyncMigrationDetails";

    public static final String TECHO_VALIDATE_USER = "techovalidateuser";
    public static final String TECHO_RECORD_ENTRY_MOBILE_TO_DB_SERVER = "techorecordentryfrommobiletodbservernew";
    public static final String TECHO_POST_MERGED_FAMILIES_INFORMATION = "techopostmergedfamiliesinformation";
    public static final String TECHO_GET_FAMILY_TO_BE_ASSIGNED_BY_SEARCH_STRING = "techogetfamilybysearchstring";
    public static final String TECHO_POST_ASSIGN_FAMILY_TO_USER = "techopostassignfamilytouser";
    public static final String TECHO_GET_USER_FORM_ACCESS_DETAIL = "techogetuserformaccessdetail";
    public static final String TECHO_POST_USER_READY_TO_MOVE_PRODUCTION = "techopostuserreadytomoveproduction";
    public static final String TECHO_POST_AADHAR_UPDATE_DETAILS = "techopostaadharupdatedetails";
    public static final String TECHO_POST_SYNC_MIGRATION_DETAILS = "techopostsyncmigrationdetails";
    public static final String TECHO_IS_USER_IN_PRODUCTION = "techoisuserinproduction";
    public static final String TECHO_UPLOAD_UNCAUGHT_EXCEPTION_DETAIL = "techouploaduncaughtexceptiondetail";
    public static final String TECHO_GET_TOKEN_VALIDITY = "techogettokenvalidity";
    public static final String TECHO_REVALIDATE_TOKEN = "techorevalidatetoken";
    public static final String TECHO_GET_USER_FROM_TOKEN = "techogetuserfromtoken";
    public static final String TECHO_VALIDATE_USER_NEW = "techovalidateusernew";
    public static final String TECHO_GET_IMEI_BLOCKED = "getimeiblocked";
    public static final String TECHO_TEST_RECORD_ENTRY = "testrecordentry";
    public static final String RUN_PATCH = "runpatch";
    public static final String TECHO_GET_NAME_BASED_ON_AADHAR = "getnamebasedonaadhar";
    public static final String TECHO_PLUS_USER_COUNT = "gettechoplususercount";
    public static final String TECHO_POST_RBSK_SCREENING_DETAILS = "rbskscreeningdetails";
    public static final String TECHO_POST_GENERATE_OTP = "generateotptecho";
    public static final String TECHO_POST_VERIFY_OTP = "verifyotptecho";
    public static final String TECHO_POST_MARK_ATTENDANCE = "markattendance";
    public static final String TECHO_POST_STORE_ATTENDANCE = "storeattendance";
    public static final String TECHO_GET_IMEI_BLOCKED_OR_DELETE_DATABASE = "getimeiblockedordeletedatabase";
    public static final String TECHO_REMOVE_IMEI_BLOCKED_ENTRY = "removeimeiblockedentry";
    public static final String TECHO_FAILED_HEALTH_ID = "failedHealthIdData";

    public static final String TECHO_STORE_OPD_LAB_TEST_FORM = "storeOpdLabTest";

    public static final String TECHO_STORE_COVID_SYMPTOM_CHECKER_DUMP = "storeCovidSymtomCheckerDump";

    //for Aww module
    public static final String GET_DETAILS_AWW = "getdetailsaww";

    //for Rbsk module
    public static final String GET_DETAILS_RBSK = "getdetailsrbsk";
}
