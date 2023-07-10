package com.argusoft.medplat.common.util;

import java.util.ResourceBundle;

/**
 * A class for common constant
 *
 * @author harshit
 * @since 31/08/2020 4:30
 */
public class ConstantUtil {

    private ConstantUtil() {

    }

    private static final ResourceBundle serverPropertiesBundle = ResourceBundle.getBundle("server");
    private static final ResourceBundle jdbcPropertiesBundle = ResourceBundle.getBundle("jdbc");

    public static final long SERVER_COMPANY_ID =
            Integer.parseInt(serverPropertiesBundle.getString("server.company.id"));
    public static String DROP_TYPE = serverPropertiesBundle.getString("droptype");
    public static final String REPOSITORY_PATH = serverPropertiesBundle.getString("repositorypath");
    public static final String SMS_SERVICE_URL = serverPropertiesBundle.getString("smsserviceurl");
    public static final String SERVER_TYPE = serverPropertiesBundle.getString("servertype"); //LIVE OR DEV
    public static final String IMPLEMENTATION_TYPE = serverPropertiesBundle.getString("serverimplementationtype");
    public static final Integer SERVER_PORT =
            Integer.valueOf(serverPropertiesBundle.getString("server.port"));
    public static final Integer SERVER_REDIRECT_PORT =
            Integer.valueOf(serverPropertiesBundle.getString("serverredirectport"));
    public static final Boolean SERVER_IS_SECURE =
            Boolean.valueOf(serverPropertiesBundle.getString("serverissecure"));
    public static final Integer JDBC_MAX_ACTIVE_CONNECTION =
            Integer.valueOf(jdbcPropertiesBundle.getString("spring.datasource.max-active"));

//    public static final String JDBC_URL = jdbcPropertiesBundle.getString("spring.datasource.url");

    public static final String JDBC_USERNAME = jdbcPropertiesBundle.getString("spring.datasource.username");

    public static final String JDBC_PASSWORD = jdbcPropertiesBundle.getString("spring.datasource.password");

    public static final String JDBC_DRIVER_NAME = jdbcPropertiesBundle.getString("spring.database.driverClassName");

    public static final String ALLOW_ORIGIN = serverPropertiesBundle.getString("allow.origin");

    public static final String SPRING_SECURITY_CLIENT_ID_IMTECHO_UI = "imtecho-ui";
    public static final String SPRING_SECURITY_CLIENT_ID_MYTECHO_UI = "mytecho-ui";
    public static final String SPRING_SECURITY_CLIENT_ID_DRTECHO = "drtecho";

    public static final String TEST_DB_RESTORE_SCRIPT_PATH = "src/main/resources//db/restore/sql/";
    public static final String TEST_DB_TRUNCATE_SCRIPT_PATH = "src/test/resources//db/restore/sql/";
    public static final String TEST_DB_TENANT_NAME = "test";
    public static final String TEST_DB_DEFAULT_ACTIVE_PROFILE = "dev";

    public static final String TEST_DB_DROP_QUERY = "DROP DATABASE IF EXISTS %s";
    public static final String TEST_DB_CREATE_QUERY = "CREATE DATABASE %s";
    public static final String TEST_DB_RESTORE_FAIL_MESSAGE = "Database restore fail";
    public static final String TEST_DB_RESTORE_JDBC_URL = "jdbc:postgresql://%s:%s/";
    public static final String TEST_DB_JDBC_DRIVER_NAME = jdbcPropertiesBundle.getString("spring.database.driverClassName");

    public static final String DEFAULT_EXCEPTION_MAIL_SEND_TO = "kunjanp@argusoft.com";
    public static final String DEFAULT_WS_EXCEPTION_MAIL_MESSAGE = "Exception From TeCHO Web Service";
    public static final String DEFAULT_WS_MAIL_MESSAGE = "Message From TeCHO Web Service";
    public static final String DEFAULT_EXCEPTION_MAIL_SUBJECT = "Exception From TeCHO";
    public static final String EXCEPTION_MAIL_MESSAGE_WS_TECHO = "EXCEPTION_MAIL_MESSAGE_WS_TECHO";
    public static final String EXCEPTION_MAIL_SUBJECT_TECHO = "EXCEPTION_MAIL_SUBJECT_TECHO";
    public static final String EXCEPTION_MAIL_SEND_TO_TECHO = "EXCEPTION_MAIL_SEND_TO_TECHO";
    public static final String EVENT_CONFIG_FAILED_EXECUTION_EXCEPTION_MAIL = "EVENT_CONFIG_FAILED_EXECUTION_EXCEPTION_MAIL";
    public static final String TECHO_DB_BACKUP_PATH = "TECHO_DB_BACKUP_PATH";
    public static final String RCH_SERVICE_PUSH_ENABLE = "RCH_SERVICE_PUSH_ENABLE";

    public static final String SOH_REGISTER_REQUEST_ACCESS_EMAIL = "techo@gujarat.gov.in";
    public static final String SOH_REGISTER_REQUEST_ACCESS_SUBJECT = "Request for Access to TeCHO+ State of Health App";
    public static final String STATE_OF_HEALTH_USER_ROLE = "SOH_USER";

    public static final String SMS_SERVICE_USERNAME = serverPropertiesBundle.getString("smsServiceUsername");
    public static final String SMS_SERVICE_PASSWORD = serverPropertiesBundle.getString("smsServicePassword");
    public static final String SMS_SERVICE_SIGNATURE = serverPropertiesBundle.getString("smsServiceSignature");

    public static final String FHS_LAST_UPDATE_TIME_SYSTEM_KEY = "FHS_LAST_UPDATE_TIME";

    public static final String CMTC_WEB_NEW_ADMISSION = "NEW_ADMISSION";
    public static final String CMTC_WEB_RE_ADMISSION = "RE_ADMISSION";
    public static final String CMTC_WEB_RELAPSE = "RELAPSE";
    public static final String CMTC_WEB_REFERRED = "REFERRED";
    public static final String CMTC_ACTIVE_STATE = "ACTIVE";
    public static final String CMTC_DEATH_STATE = "DEATH";
    public static final String CMTC_DISCHARGE_STATE = "DISCHARGE";
    public static final String CMTC_DEFAULTER_STATE = "DEFAULTER";
    public static final String CMTC_TEST_OUTPUT_COMPLETED_STATE = "COMPLETED";
    public static final String CMTC_TEST_OUTPUT_PENDING_STATE = "PENDING";

    //MOBILE FEATURES
    public static final String CEREBRAL_PALSY_SCREENING = "CEREBRAL_PALSY_SCREENING";
    public static final String GEO_FENCING = "GEO_FENCING";

    public static final String LAN_EN = "EN";

    public static final Integer LIST_VALUE_UNMARRIED = 630;
    public static final Integer MYTECHO_USER_ROLE_ID = 202;
    public static final Integer DRTECHO_USER_ROLE_ID = 203;
    public static final Integer PRIVATE_HOSPITAL_FIELD_ID = 1013;

    public static final Integer MAX_REFRESH_MOBILE = 100;

    public static final String SYNC_NOTIFICATION = "NOTIFICATION_BUILDER";
    public static final String SYNC_EVENT = "EVENT_BUILDER";
    public static final String SYNC_QUERY_BUILDER = "QUERY_BUILDER";
    public static final String SYNC_REPORT_MASTER = "REPORT_CONFIGURATION";

    public static final Integer MAXIMUM_TIME_TAKEN_BY_API = 10000; // 10 s
    public static final Integer MAXIMUM_SIZE_OF_PAYLOAD_IN_BYTES = 5000000; // 5 MB
    public static final Integer MAXIMUM_AMOUNT_OF_ROWS_FETCH_FROM_DB = 10000;

    //MYTECHO
    public static final String MYTECHO_APP_VERSION = "MYTECHO_APP_VERSION";

    public static final String TRAVEL_HISTORY_OTHER_COUNTRY = "OTHER_COUNTRY";


    public static final String REQ_STATUS_SUCCESS = "SUCCESS";

    public static final String REQ_STATUS_FAIL = "FAIL";

    public static final String TELANGANA_IMPLEMENTATION = "telangana";

    public static final String UTTARAKHAND_IMPLEMENTATION = "uttarakhand";

    public static final String MEDPLAT_IMPLEMENTATION = "medplat";

    public static final String SEWA_RURAL_IMPLEMENTATION = "sewa_rural";

    public static final String SCREENING_STATUS_RED = "SCREENING_STATUS_RED";
    public static final String SCREENING_STATUS_GREEN = "SCREENING_STATUS_GREEN";
    public static final String SCREENING_STATUS_YELLOW = "SCREENING_STATUS_YELLOW";

}
