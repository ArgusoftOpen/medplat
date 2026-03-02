package com.argusoft.sewa.android.app.restclient;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author alpesh
 */
public interface RestRequest {

    /**
     * @param url                on which we fire service
     * @param httpMethodType     http method type GET , POST , PUT etc
     * @param requestParams      request parameters
     * @param data               body data ignore if method Type is GET and DELETE
     * @param expectedResultType The specific genericized type of src. You can
     *                           obtain this type by using the TypeToken class. For example, to get the
     *                           type for Collection&lt;Foo&gt, you should use: Type typeOfT = new
     *                           TypeToken &lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * @return Object of expected result class type
     * @throws RestHttpException *
     */
    <T> T callWebService(
            String url,
            RestHttpMethodType httpMethodType,
            Map<String, Object> requestParams,
            Object data,
            Type expectedResultType) throws RestHttpException;

    /**
     * All these headers to all the requests
     */
    void addFixedHeader(Map<String, String> fixedHeader);
}
