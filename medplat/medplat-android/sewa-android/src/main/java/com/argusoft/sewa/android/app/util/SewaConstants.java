package com.argusoft.sewa.android.app.util;

import android.content.Context;

import com.argusoft.sewa.android.app.BuildConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kelvin
 */
public class SewaConstants {

    private SewaConstants() {
        throw new IllegalStateException("Utility Class");
    }

    private static String directoryPath;

    public static String getDirectoryPath(Context context, String dirName) {
        if (directoryPath != null) {
            return directoryPath + dirName;
        }
        if (context == null) {
            if (BuildConfig.FLAVOR.equals("medplat")) {
                return "/storage/emulated/0/Android/data/com.argusoft.sewa.android.app/files" + dirName;
            } else if (BuildConfig.FLAVOR.equals(GlobalTypes.UTTARAKHAND_FLAVOR)) {
                return "/storage/emulated/0/Android/data/com.argusoft.uttarakhand.android.app/files" + dirName;
            } else {
                return "/storage/emulated/0/Android/data/com.argusoft.ammakosam.android.app/files" + dirName;
            }
        }

        File filesDir = context.getExternalFilesDir(null);
        if (filesDir == null) {
            if (BuildConfig.FLAVOR.equals("medplat")) {
                return "/storage/emulated/0/Android/data/com.argusoft.sewa.android.app/files" + dirName;
            } else if (BuildConfig.FLAVOR.equals(GlobalTypes.UTTARAKHAND_FLAVOR)) {
                return "/storage/emulated/0/Android/data/com.argusoft.uttarakhand.android.app/files" + dirName;
            } else {
                return "/storage/emulated/0/Android/data/com.argusoft.ammakosam.android.app/files" + dirName;
            }
        }

        directoryPath = filesDir.getAbsolutePath();
        return directoryPath + dirName;
    }

    public static void setDirectoryPath(Context context) {
        File filesDir = context.getExternalFilesDir(null);
        if (filesDir == null) {
            return;
        }
        directoryPath = filesDir.getAbsolutePath();
    }

    //  Directory RchConstants
    private static final String TEMP = "temp/";
    public static final String DIR_IMAGE = "/images/";
    public static final String DIR_AUDIO = "/audio/";
    public static final String DIR_VIDEO = "/video/";
    public static final String DIR_DOWNLOADED = "/downloads/";
    public static final String DIR_APK_DOWNLOADED = "/sewa_apk/";
    public static final String DIR_APK_DOWNLOADED_TEMP = DIR_APK_DOWNLOADED + TEMP;
    public static final String DIR_DATABASE = "/database/";
    public static final String DIR_LIBRARY = "/library/";
    public static final String DIR_LIBRARY_TEMP = DIR_LIBRARY + TEMP;
    public static final String DIR_BOOKMARK = "/bookmarks/";
    public static final String DIR_LMS = "/lms/";
    public static final String DIR_PDF= "/pdf/";
    public static final String DIR_LMS_TEMP = DIR_LMS + TEMP;

    //  Various Login Response Status RchConstants
    public static final String LOGIN_SUCCESS_LOCAL = "Login Successful Without Network";
    public static final String LOGIN_SUCCESS_WEB = "Login Successful With Network";
    public static final String LOGIN_FAILURE = "Invalid username and/or password";
    public static final String NO_INTERNET_CONNECTION = "No internet connection. Turn ON internet and try again";
    public static final String USER_NOT_FOUND = "User not found with this username";
    public static final String NETWORK_FAILURE = "Network Unavailable";
    public static final String NETWORK_AVAILABLE = "Network Available";
    public static final String NETWORK_NOT_PROPER = "Network Connection Error";
    public static final String EXCEPTION_USER_DOWNLOAD = "Network Issue During User related Download";
    public static final String EXCEPTION_INITIALIZING_DOWNLOAD = "Network Issue During Initial Data Download";
    public static final String EXCEPTION_TOKEN_UPDATION = "Network Issue During Internal Process";
    public static final String EXCEPTION_UPLOAD_TO_SERVER = "Network Issue During Upload to server";
    public static final String EXCEPTION_FETCHING_DATA_FOR_USER = "Network Issue During Fetching Data for User";
    public static final String EXCEPTION_FETCHING_USER_FORM_ACCESS = "Network Issue During Downloading Form Access";
    public static final String LOGIN_UNAUTHORISED_ACCESS = "User is not authorised to login";
    public static final String SQL_EXCEPTION = "Database issue while refresh/login. Please contact your co-ordinator.";
    public static final String LOCATION_NOT_ASSIGNED = "Location is not assigned correctly to the user, please contact your co-ordinator.";
    public static final String MENU_NOT_FOUND = "User is not authorised to login.";

    //  General RchConstants
    public static final String SUCCESS = "SUCCESS";
    public static final String ENTITY = "entity";
    public static final String ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE = "allowForm";
    //  Model Field Name Constant
    public static final String QUESTION_BEAN_ENTITY = "entity";
    public static final String LIST_VALUE_BEAN_DATAMAP = "field";
    public static final String LIST_VALUE_BEAN_ENTITY = "formCode";
    public static final String ANNOUNCEMENT_BEAN_ANNOUNCEMENT_ID = "announcementId";
    public static final String LIST_VALUE_BEAN_FIELD = "field";
    public static final String LIST_VALUE_BEAN_FIELD_TYPE = "fieldType";
    public static final String LIST_VALUE_BEAN_ID_OF_VALUE = "idOfValue";
    public static final String LOCATION_TYPE_BEAN_TYPE = "type";
    // aaded for show last refresh time stamp
    public static final String TIME_STAMP_LAST_REFRESH = "lastRefreshDate";
    //for different text colors in Survey sheet
    public static final String QUESTION_COLOR_1 = "color1";
    public static final String QUESTION_COLOR_2 = "color2";
    public static final String QUESTION_COLOR_3 = "color3";
    public static final String QUESTION_COLOR_4 = "color4";
    // required for download apk mapping in sharedPrefsFile
    public static final String DOWNLOAD_PREFS_NAME = "APP_DOWNLOAD_PREF";
    public static final String LIBRARY_DOWNLOAD_PREFS_NAME = "LIBRARY_DOWNLOAD_PREF";
    public static final String LMS_DOWNLOAD_PREFS_NAME = "LMS_DOWNLOAD_PREF";

    //  Font Name
    private static Map<String, Integer> locationLevels;

    static {
        Log.i("SewaConstants", "SewaConstants  is loading ...........");
        fillLocationLevels();
    }

    private static void fillLocationLevels() {
        locationLevels = new HashMap<>();
        locationLevels.put("Level-0", 0);
        locationLevels.put("Level-1", 1);
        locationLevels.put("Level-2", 2);
        locationLevels.put("Level-3", 3);
        locationLevels.put("Level-4", 4);
        locationLevels.put("Level-5", 5);
        locationLevels.put("Level-6", 6);
        locationLevels.put("areaList", 7);
        locationLevels.put("anganwadiList", 8);
    }

    public static Map<String, Integer> getLocationLevel() {
        return locationLevels;
    }

}
