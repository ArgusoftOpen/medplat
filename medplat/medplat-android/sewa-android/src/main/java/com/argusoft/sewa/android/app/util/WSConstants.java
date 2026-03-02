package com.argusoft.sewa.android.app.util;

import com.argusoft.sewa.android.app.BuildConfig;

/**
 * @author kelvin
 */
public class WSConstants {

    private WSConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static String CONTEXT_URL_TECHO = BuildConfig.BASE_URL;
    public static String CONTEXT_URL_TECHO_LIVE = BuildConfig.BASE_URL;
    public static String CONTEXT_URL_TECHO_TRAINING = BuildConfig.BASE_URL_TRAINING;
    public static String REST_TECHO_SERVICE_URL = CONTEXT_URL_TECHO + "api/mobile/";
    public static String REST_TECHO_NDHM_SERVICE_URL = REST_TECHO_SERVICE_URL + "ndhm/healthid/";
    private static String lastUrlTecho;

    private static void setAfterContextConfig() {
        REST_TECHO_SERVICE_URL = CONTEXT_URL_TECHO + "api/mobile/";
    }

    public static void setLiveContextUrl() {
        storeLastContextUrl();
        CONTEXT_URL_TECHO = CONTEXT_URL_TECHO_LIVE;
        setAfterContextConfig();
        Log.i(WSConstants.class.getSimpleName(), "********** Setting Live URL TeCHO :" + CONTEXT_URL_TECHO);
    }

    public static void setTrainingContextUrl() {
        storeLastContextUrl();
        CONTEXT_URL_TECHO = CONTEXT_URL_TECHO_TRAINING;
        setAfterContextConfig();
        Log.i(WSConstants.class.getSimpleName(), "********** Setting Training URL TeCHO :" + CONTEXT_URL_TECHO);
    }

    private static void storeLastContextUrl() {
        lastUrlTecho = CONTEXT_URL_TECHO;
        setAfterContextConfig();
    }

    public static void setLastContextUrlStore() {
        CONTEXT_URL_TECHO = lastUrlTecho;
        setAfterContextConfig();
    }

    public static class ApiCalls {

        private ApiCalls() {
            throw new IllegalStateException("Utility Class");
        }

        // API Calls for TeCHO+
        public static final String GET_ANDROID_VERSION = "retrieveAndroidVersion";
        public static final String GET_SERVER_IS_ALIVE = "getserverisalive";
        public static final String GET_FONT_SIZE = "retrieveFontSize";
        public static final String SYNC_DATA = "syncData";
        public static final String GET_DETAILS_FHW = "getDetails";
        public static final String GET_DETAILS_ASHA = "getdetailsasha";
        public static final String GET_DETAILS_FHSR = "getdetailsfhsr";
        public static final String GET_FAMILIES_BY_LOCATION = "getfamiliesbylocation";
        public static final String TECHO_VALIDATE_USER_NEW = "techovalidateusernew";
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
        public static final String TECHO_GET_TOKEN_VALIDITY = "requestParam";
        public static final String TECHO_REVALIDATE_TOKEN = "techorevalidatetoken";
        public static final String TECHO_GET_USER_FROM_TOKEN = "techogetuserfromtoken";
        public static final String TECHO_GET_DATA_FROM_QUERY_MASTER = "getdata";
        public static final String TECHO_DOWNLOAD_APPLICATION = "downloadapk";

        //Blocked Imei
        public static final String TECHO_GET_IMEI_BLOCKED_OR_DELETE_DATABASE = "getimeiblockedordeletedatabase";
        public static final String TECHO_REMOVE_IMEI_BLOCKED_ENTRY = "removeimeiblockedentry";

        //Aww module
        public static final String GET_DETAILS_AWW = "getdetailsaww";

        //Rbsk module
        public static final String GET_DETAILS_RBSK = "getdetailsrbsk";
        public static final String TECHO_POST_RBSK_SCREENING_DETAILS = "rbskscreeningdetails";

        //for generating OTP
        public static final String TECHO_OTP_REQUEST = "generateotptecho";
        public static final String TECHO_OTP_VERIFICATION = "verifyotptecho";

        //Marking attendance for CHO role
        public static final String TECHO_MARK_ATTENDANCE = "markattendance";
        public static final String TECHO_STORE_ATTENDANCE = "storeattendance";

        //Storing data for OPD Lab form
        public static final String TECHO_STORE_OPD_LAB_TEST_FORM = "storeOpdLabTest";

        // Sync feature
        public static final String GET_METADATA = "getMetadata";

        public static final String TECHO_NDHM_GENERATE_AADHAAR_OTP = "generate-aadhar-otp";
        public static final String TECHO_NDHM_CREATE_USING_AADHAAR_OTP = "create-using-aadhar-otp";
        public static final String TECHO_NDHM_CREATE_USING_AADHAAR_DEMO = "create-using-aadhar-demo";
        public static final String TECHO_NDHM_HEALTH_ID_CARD = "heath-id-card";
        public static final String TECHO_NDHM_SEARCH = "search";
        public static final String TECHO_NDHM_AUTHENTICATION = "authentication";
        public static final String TECHO_NDHM_CONFIRM_AADHAAR_DEMO = "confirm-aadhar-demo";
        public static final String TECHO_NDHM_CONFIRM_AADHAAR_OTP = "confirm-aadhar-otp";
        public static final String TECHO_NDHM_CONFIRM_MOBILE_OTP = "confirm-mobile-otp";
        public static final String TECHO_NDHM_CONFIRM_PASSWORD = "confirm-password";
        public static final String TECHO_NDHM_RESEND_OTP = "resend-otp";
        public static final String TECHO_NDHM_STATES = "states";
        public static final String TECHO_NDHM_LINK_BENEFIT = "link-benefit";
    }
}
