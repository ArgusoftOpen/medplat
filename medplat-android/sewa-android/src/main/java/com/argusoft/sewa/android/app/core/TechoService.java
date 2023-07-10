package com.argusoft.sewa.android.app.core;

import android.content.Context;

import java.util.Map;

public interface TechoService {

    Map<String, Boolean> checkIfDeviceIsBlockedOrDeleteDatabase(Context context);

    void deleteDatabaseFileFromLocal(Context context);

    Boolean syncMergedFamiliesInformationWithServer();

    void getLgdCodeWiseCoordinates();

    boolean checkIfFeatureIsReleased();

    Boolean checkIfOfflineAnyFormFilledForMember(Long memberId);

    void deleteQuestionAndAnswersByFormCode(String formCode);

    boolean isNewNotification();
}
