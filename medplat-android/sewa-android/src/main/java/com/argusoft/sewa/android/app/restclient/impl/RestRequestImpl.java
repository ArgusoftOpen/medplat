/*
package com.argusoft.sewa.android.app.restclient.impl;

import android.content.Context;

import com.argusoft.sewa.android.app.restclient.RestContentType;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.restclient.RestHttpMethodType;
import com.argusoft.sewa.android.app.restclient.RestHttpResponseCode;
import com.argusoft.sewa.android.app.restclient.RestRequest;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.androidannotations.annotations.EBean;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * @author alpesh
 *//*

@EBean(scope = EBean.Scope.Singleton)
public class RestRequestImpl implements RestRequest {

    private final JsonSerializer<Date> dateSerializer = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());
    private final JsonDeserializer<Date> dateDeserializer = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer).registerTypeAdapter(Date.class, dateDeserializer).create();

    private static final String UTF_8 = "UTF-8";
    private static final String ACCEPT = "Accept";

    private final RestContentType resContentType;
    private final HttpParams httpParams;
    private final Map<String, String> fixedHeader;
    private String fileName = ".sewa.data";
    private CookieStore myCookieStore;

    public RestRequestImpl(Context context) {
        myCookieStore = new BasicCookieStore();
        httpParams = new BasicHttpParams();
        fixedHeader = new HashMap<>();
        int timeout = 60000 * 3;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, 0);
        resContentType = RestContentType.APPLICATION_JSON;
        if (context != null) {
            fileName = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DATABASE) + fileName;
            readCookies();
        }
    }

    @Override
    public void addFixedHeader(Map<String, String> fixedHeader) {
        this.fixedHeader.putAll(fixedHeader);
    }

    */
/**
     * @param url                on which we fire service
     * @param httpMethodType     http method type GET , POST , PUT etc
     * @param requestParams      request parameters
     * @param data               body data ignore if method Type is GET and DELETE
     * @param expectedResultType The specific generalized type of src. You can
     *                           obtain this type by using the TypeToken class. For example, to get the
     *                           type for Collection&lt;Foo&gt, you should use: Type typeOfT = new
     *                           TypeToken &lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * @return Object of expected result class type
     * @throws RestHttpException *
     *//*

    @Override
    public <T> T callWebService(
            String url,
            RestHttpMethodType httpMethodType,
            Map<String, Object> requestParams,
            Object data,
            Type expectedResultType) throws RestHttpException {
        if (url == null) {
            throw new RestHttpException(RestHttpResponseCode.SC_URL_NULL, RestConstantMsg.MSG_URL_NULL);
        } else {
            if (httpMethodType != null) {
                HttpRequestBase request;
                // generate request process 
                StringBuilder urlWithQuery = new StringBuilder(url);
                // add request param
                if (requestParams != null) {
                    try {

                        String querySeparator = "?";
                        for (Map.Entry<String, Object> entrySet : requestParams.entrySet()) {
                            String key = entrySet.getKey();
                            Object value = entrySet.getValue();
                            if (value != null) {
                                urlWithQuery.append(querySeparator).append(key).append("=").append(URLEncoder.encode(value.toString(), UTF_8));
                                querySeparator = "&";
                            }
                        }
                    } catch (Exception exception) {
                        throw new RestHttpException(exception);
                    }
                }
                url = urlWithQuery.toString();
                Log.i(getClass().getSimpleName(), "********** Rest Url Called : " + url);
                if (requestParams != null) {
                    Log.i(getClass().getSimpleName(), "********** Params : " + new Gson().toJson(requestParams));
                }
                if (data != null) {
                    Log.i(getClass().getSimpleName(), "********** Body : " + new Gson().toJson(data));
                }
                if (httpMethodType.equals(RestHttpMethodType.HTTP_GET)) {
                    //ignore the parameter 
                    request = new HttpGet(url);
                    if (resContentType != null) {
                        request.addHeader(ACCEPT, resContentType.getValue());
                    }
                } else if (httpMethodType.equals(RestHttpMethodType.HTTP_POST)) {
                    HttpPost httpPost = new HttpPost(url);
                    if (resContentType != null) {
                        httpPost.addHeader(ACCEPT, resContentType.getValue());
                        try {
                            if (data != null) {
                                StringEntity requestParam;
                                if (resContentType.equals(RestContentType.APPLICATION_JSON)) {
                                    requestParam = new StringEntity(gson.toJson(data), UTF_8);
                                    requestParam.setContentType(resContentType.getValue());
                                } else {
                                    requestParam = new StringEntity(data.toString(), UTF_8);
                                }
                                httpPost.setEntity(requestParam);
                            }
                        } catch (UnsupportedEncodingException ex) {
                            throw new RestHttpException(RestHttpResponseCode.SC_PERAMETER_NOT_PROPER, RestConstantMsg.MSG_PARAMETER_NOT_PROPER);
                        }
                    }
                    request = httpPost;
                } else if (httpMethodType.equals(RestHttpMethodType.HTTP_PUT)) {
                    HttpPut httpPut = new HttpPut(url);
                    if (resContentType != null) {
                        httpPut.addHeader(ACCEPT, resContentType.getValue());
                        try {
                            if (data != null) {
                                StringEntity requestParam;
                                if (resContentType.equals(RestContentType.APPLICATION_JSON)) {
                                    requestParam = new StringEntity(gson.toJson(data), UTF_8);
                                    requestParam.setContentType(resContentType.getValue());
                                } else {
                                    requestParam = new StringEntity(data.toString(), UTF_8);
                                }
                                httpPut.setEntity(requestParam);
                            }
                        } catch (UnsupportedEncodingException ex) {
                            throw new RestHttpException(RestHttpResponseCode.SC_PERAMETER_NOT_PROPER, RestConstantMsg.MSG_PARAMETER_NOT_PROPER);
                        }
                    }
                    request = httpPut;
                } else if (httpMethodType.equals(RestHttpMethodType.HTTP_DELETE)) {
                    request = new HttpDelete(url);
                    if (resContentType != null) {
                        request.addHeader(ACCEPT, resContentType.getValue());
                    }
                } else {
                    throw new RestHttpException(RestHttpResponseCode.SC_NOT_IMPLEMENTED_STILL, RestConstantMsg.MSG_NOT_IMPLEMENTED_STILL);
                }
                // Set the fixed headers to the request
                for (Map.Entry<String, String> headerEntry : fixedHeader.entrySet()) {
                    request.addHeader(headerEntry.getKey(), headerEntry.getValue());
                }
                // request is ready to fire 
                //StringBuilder responseText = executeRequest(request)
                JsonReader jsonReader = executeRequest(request);
                if (jsonReader != null) {
                    if (resContentType != null && resContentType.equals(RestContentType.APPLICATION_JSON)) {
                        try {
                            return gson.fromJson(jsonReader, expectedResultType);
                        } catch (JsonSyntaxException e) {
                            Log.e(getClass().getSimpleName(), null, e);
                            throw new RestHttpException(RestHttpResponseCode.SC_RESPONSE_NOT_FORMATED, RestConstantMsg.MSG_RESPONSE_NOT_FORMATTED);
                        }
                    } else {
                        throw new RestHttpException(RestHttpResponseCode.SC_RESPONSE_NOT_FORMATED, RestConstantMsg.MSG_RESPONSE_NOT_FORMATTED);
                    }
                } else {
                    return null;
                }
                // done
            } else {
                throw new RestHttpException(RestHttpResponseCode.SC_NO_HTTP_METHOD_TYPE, RestConstantMsg.MSG_NO_HTTP_METHOD_TYPE);
            }
        }
    }

    private JsonReader executeRequest(HttpRequestBase request) throws RestHttpException {
        HttpResponse httpResponse;
        try {
            httpResponse = executeHttp(new DefaultHttpClient(httpParams), request);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
            throw new RestHttpException(RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR, RestConstantMsg.MSG_INTERNAL_SERVER_ERROR);
        }
        if (httpResponse == null) {
            throw new RestHttpException(RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR, RestConstantMsg.MSG_INTERNAL_SERVER_ERROR);
        } else {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            StringBuilder responseText;
            if (statusCode == RestHttpResponseCode.SC_OK.getValue()) {
                try {
                    return new JsonReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));  //readData(entity.getContent())
                } catch (Exception ex) {
                    throw new RestHttpException(RestHttpResponseCode.SC_ERROR_IN_READING_RESONSE, RestConstantMsg.MSG_ERROR_IN_READING_RESPONSE);
                }
            } else if (statusCode == RestHttpResponseCode.SC_OK_NO_CONTENT.getValue()) {
                // request is valid but there is not data 
                return null;
            } else if (statusCode == RestHttpResponseCode.SC_BAD_REQUEST.getValue()) {
                String message = getErrorMessage(httpResponse);
                if (message != null) {
                    throw new RestHttpException(RestHttpResponseCode.SC_BAD_REQUEST, message);
                } else {
                    throw new RestHttpException(RestHttpResponseCode.SC_BAD_REQUEST, RestConstantMsg.MSG_BAD_REQUEST);
                }
            } else if (statusCode == RestHttpResponseCode.SC_UNAUTHORIZED.getValue()) {
                try {
                    responseText = readData(entity.getContent());
                } catch (Exception ex) {
                    throw new RestHttpException(RestHttpResponseCode.SC_ERROR_IN_READING_RESONSE, RestConstantMsg.MSG_ERROR_IN_READING_RESPONSE);
                }
                throw new RestHttpException(RestHttpResponseCode.SC_UNAUTHORIZED, responseText.toString());
            } else if (statusCode == RestHttpResponseCode.SC_FORBIDDEN.getValue()) {
                try {
                    responseText = readData(entity.getContent());
                } catch (Exception ex) {
                    throw new RestHttpException(RestHttpResponseCode.SC_ERROR_IN_READING_RESONSE, RestConstantMsg.MSG_ERROR_IN_READING_RESPONSE);
                }
                throw new RestHttpException(RestHttpResponseCode.SC_FORBIDDEN, responseText.toString());
            } else if (statusCode == RestHttpResponseCode.SC_NOT_FOUND.getValue()) {
                throw new RestHttpException(RestHttpResponseCode.SC_NOT_FOUND, RestConstantMsg.MSG_NOT_FOUND);
            } else if (statusCode == RestHttpResponseCode.SC_REQUEST_TIMEOUT.getValue()) {
                throw new RestHttpException(RestHttpResponseCode.SC_REQUEST_TIMEOUT, RestConstantMsg.MSG_REQUEST_TIME_OUT);
            } else if (statusCode == RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR.getValue()) {
                String message = getErrorMessage(httpResponse);
                if (message != null) {
                    throw new RestHttpException(RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR, message);
                } else {
                    throw new RestHttpException(RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR, RestConstantMsg.MSG_INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new RestHttpException(RestHttpResponseCode.SC_OTHER, RestConstantMsg.MSG_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private String getErrorMessage(HttpResponse httpResponse) {
        String msg = null;
        try {
            JsonObject obj = new Gson().fromJson(EntityUtils.toString(httpResponse.getEntity()), JsonObject.class);
            JsonElement message = obj.get("message");
            if (message != null) {
                msg = message.getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    private HttpResponse executeHttp(final DefaultHttpClient httpClient, final HttpRequestBase request) throws IOException {
        HttpResponse myResponse;
        httpClient.setCookieStore(myCookieStore);
        myResponse = httpClient.execute(request);
        try {
            myCookieStore = httpClient.getCookieStore();
            writeCookies();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Enable to write cookies. . . . . ", e);
        }
        return myResponse;
    }

    private StringBuilder readData(InputStream inputStream) {
        if (inputStream != null) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                for (String line = r.readLine(); line != null; line = r.readLine()) {
                    sb.append(line);
                }
                inputStream.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                return new StringBuilder();
            }
            return sb;
        } else {
            return new StringBuilder();
        }
    }

    private void writeData(OutputStream outputStream, String data) {
        if (outputStream != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(data, 0, data.length());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "No Write in File", e);
            }
        }
    }

    private void readCookies() {
        if (myCookieStore != null) {
            try (FileInputStream reader = new FileInputStream(fileName)) {
                StringBuilder readData = readData(reader);
                if (!readData.toString().isEmpty()) {
                    Type type = new TypeToken<List<BasicClientCookie>>() {
                    }.getType();
                    List<BasicClientCookie> fromJson = gson.fromJson(readData.toString(), type);
                    if (fromJson != null && !fromJson.isEmpty()) {
                        myCookieStore.clear();
                        for (Cookie cookie : fromJson) {
                            myCookieStore.addCookie(cookie);
                        }
                    }
                }
            } catch (JsonSyntaxException ex) {
                Log.e(getClass().getSimpleName(), null, ex);
            } catch (IOException ex) {
                Log.e(getClass().getSimpleName(), " No cookies Read", ex);
            }
        }
    }

    private synchronized void writeCookies() {
        if (myCookieStore != null) {
            try (FileOutputStream writer = new FileOutputStream(fileName)) {
                String cookies = gson.toJson(myCookieStore.getCookies());
                writeData(writer, cookies);
            } catch (IOException ex) {
                Log.e(getClass().getSimpleName(), " No cookies write", ex);
            }
        }
    }
}
*/
