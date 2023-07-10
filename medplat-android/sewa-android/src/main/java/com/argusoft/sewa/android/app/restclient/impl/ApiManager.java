package com.argusoft.sewa.android.app.restclient.impl;

import static com.argusoft.sewa.android.app.restclient.impl.RestConstantMsg.MSG_INTERNAL_SERVER_ERROR;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.restclient.ApiService;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.restclient.RestHttpResponseCode;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import org.androidannotations.annotations.EBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class ApiManager {

    private ApiService apiService;
    private final Gson gson;
    private final OkHttpClient.Builder okHttpClient;

    public ApiManager() {
        okHttpClient = new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.MINUTES)
                .connectTimeout(15, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            okHttpClient.addInterceptor(httpLoggingInterceptor);
        }
        okHttpClient.addNetworkInterceptor(chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("application-version", String.valueOf(BuildConfig.VERSION_CODE))
                    .build();
            return chain.proceed(request);
        });

        JsonSerializer<Date> dateSerializer = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());
        JsonDeserializer<Date> dateDeserializer = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());
        gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer)
                .setLenient()
                .registerTypeAdapter(Date.class, dateDeserializer).create();

        createApiService();
    }

    public void createApiService() {
        apiService = new Retrofit.Builder()
                .baseUrl(WSConstants.CONTEXT_URL_TECHO)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService.class);
    }

    public ApiService getInstance() {
        return apiService;
    }

    public <T> T execute(Call<T> apiRequest) throws RestHttpException {
        try {
            Response<T> tResponse = apiRequest.execute();
            if (tResponse.isSuccessful()) {
                return tResponse.body();
            } else {
                ResponseBody responseBody = tResponse.errorBody();
                if (responseBody != null) {
                    String errorBody = responseBody.string();
                    JSONObject jsonObject = new JSONObject(errorBody);
                    String message = jsonObject.optString("message", "");
                    if (!message.isEmpty()) {
                        throw new RestHttpException(RestHttpResponseCode.SC_BAD_REQUEST, message);
                    } else {
                        throw new RestHttpException(RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR, MSG_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    throw new RestHttpException(RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR, MSG_INTERNAL_SERVER_ERROR);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RestHttpException(e);
        }
    }
}
