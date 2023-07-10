package com.argusoft.sewa.android.app.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NDHMConstants {

    private NDHMConstants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String AUTH_MODE_DEMOGRAPHICS = "DEMOGRAPHICS";
    public static final String AUTH_MODE_AADHAAR_OTP = "AADHAAR_OTP";
    public static final String AUTH_MODE_MOBILE_OTP = "MOBILE_OTP";
    public static final String AUTH_MODE_AADHAAR_BIO = "AADHAAR_BIO";
    public static final String AUTH_MODE_PASSWORD = "PASSWORD";

    public static final Map<String, String> AUTH_MODES = Collections.unmodifiableMap(getAuthModes());
    public static final Map<String, String> AUTH_MODE_VALUE_MAP = Collections.unmodifiableMap(getAuthModeValueMap());

    private static Map<String, String> getAuthModes() {
        Map<String, String> map = new HashMap<>();
        map.put(AUTH_MODE_DEMOGRAPHICS, "Demographics");
        map.put(AUTH_MODE_AADHAAR_OTP, "Aadhaar OTP");
        map.put(AUTH_MODE_MOBILE_OTP, "Mobile OTP");
        map.put(AUTH_MODE_AADHAAR_BIO, "Aadhaar BIO");
        map.put(AUTH_MODE_PASSWORD, "Password");
        return map;
    }

    private static Map<String, String> getAuthModeValueMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Demographics", AUTH_MODE_DEMOGRAPHICS);
        map.put("Aadhaar OTP", AUTH_MODE_AADHAAR_OTP);
        map.put("Mobile OTP", AUTH_MODE_MOBILE_OTP);
        map.put("Aadhaar BIO", AUTH_MODE_AADHAAR_BIO);
        map.put("Password", AUTH_MODE_PASSWORD);
        return map;
    }

    public static String getAuthModeFullName(String authMode) {
        return AUTH_MODES.containsKey(authMode) ? AUTH_MODES.get(authMode) : authMode;
    }

    public static String getAuthModeKey(String authMode) {
        return AUTH_MODE_VALUE_MAP.containsKey(authMode) ? AUTH_MODE_VALUE_MAP.get(authMode) : authMode;
    }
}
