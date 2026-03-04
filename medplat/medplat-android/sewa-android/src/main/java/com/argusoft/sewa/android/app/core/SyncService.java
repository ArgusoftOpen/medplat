package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.LoginRequestParamDetailDataBean;

import java.util.Map;

public interface SyncService {

    void getDataFromServer();

    LoginRequestParamDetailDataBean generateSyncRequestParams(Map<String, Boolean> dataBeans);

    void changeVersionBeanVersionValueForGivenKey(String key, String value, String version);
}
