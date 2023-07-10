package com.argusoft.sewa.android.app.restclient.impl;

/**
 * @author alpesh
 */
public class RestConstantMsg {

    private RestConstantMsg() {
        throw new IllegalStateException();
    }

    public static final String MSG_INTERNAL_SERVER_ERROR = "Internal server problem";
    public static final String MSG_BAD_REQUEST = "Request not matching or parameter is not proper";
    public static final String MSG_NOT_FOUND = "Service not available or check the http method type";
    public static final String MSG_REQUEST_TIME_OUT = "timeout error.  increase the request Time out";
    public static final String MSG_PARAMETER_NOT_PROPER = "check parameter it is not proper";
    public static final String MSG_RESPONSE_NOT_FORMATTED = "Response is not proper formatted that we want";
    public static final String MSG_URL_NULL = "Url is null";
    public static final String MSG_NOT_IMPLEMENTED_STILL = "Still there is no implementation of these thing";
    public static final String MSG_NO_HTTP_METHOD_TYPE = "Provide Http method it should be provide";
    public static final String MSG_ERROR_IN_READING_RESPONSE = "Error in reading response";
}
