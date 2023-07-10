/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.model.UploadFileDataBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;

import java.util.List;

/**
 * @author alpesh
 */
public interface SewaService {

    String validateUser(String username, String password, int contextUrlInfo, boolean isRemember);

    void doAfterSuccessfulLogin(boolean isLogin);

    void loadQuestionsBySheet(String entity);

    Integer createUploadFileDataBean(UploadFileDataBean uploadFileDataBean);

    Integer createStoreAnswerBean(StoreAnswerBean storeAnswerBean);

    void deleteStoreAnswerBeanByStoreAnswerBeanId(Long storeAnswerBeanId);

    Integer createLoggerBean(LoggerBean loggerBean);

    void deleteLoggerBeanByLoggerBeanId(Long loggerBeanId);

    List<LoggerBean> getWorkLog();

    List<AnnouncementBean> getAllAnnouncement();

    List<UploadFileDataBean> getFileWorkLog();

    List<FieldValueMobDataBean> getFieldValueMobDataBeanByDataMap(String datamap);

    void deleteNotificationByNotificationId(Long notificationId);

    NotificationBean getNotificationsById(Long id);

    String getAndroidVersion();

    boolean getServerIsAlive();

    VersionBean getSheetStatusByFiletr(VersionBean status);

    void updateVersionBean(VersionBean status);

    List<LabelBean> getAllLabelBean();

    boolean isOnline();

    void initLabelBeanMap();

    int isUserInProduction(String userName);

    //Retriving Location By Level For Surveyer
    List<LocationBean> retrieveLocationByLevel(Integer level);

    //Retrieving all Location By Parent For Surveyer
    void retrieveLocationByParent(Integer parent);

    NotificationBean retrieveNotificationById(Long notificationId);

    void storeException(Throwable ex, String exceptionType);

    void uploadUncaughtExceptionDetailToServer();

    void createNotificationBean(NotificationBean notificationBean);

    void updateAnswerBeanTokensAndUserIdsOnce() throws RestHttpException;

    void updateLatestTokenInStoreAnswerBean();

    void revalidateUserTokenForUser();

    LoginBean getCurrentLoggedInUser();

    void addFireBaseToken();
}
