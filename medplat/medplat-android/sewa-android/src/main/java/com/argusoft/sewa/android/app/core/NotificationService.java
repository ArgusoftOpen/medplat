package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface NotificationService {

    List<NotificationMobDataBean> retrieveNotificationsForUser(List<Integer> villageIds, List<Integer> areaIds,
                                                               String notificationCode, CharSequence searchString,
                                                               long limit, long offset,
                                                               LinkedHashMap<String, String> qrData) throws SQLException;

    Map<String, Integer> retrieveCountForNotificationType(List<Integer> villageIds, List<Integer> areaIds);

    Map<String, Integer> retrieveCountForAshaReportedEventNotificationType(List<Integer> villageIds, List<Integer> areaIds);

    Map<String, Integer> retrieveCountForOtherServicesNotificationType(List<Integer> villageIds, List<Integer> areaIds);

    Boolean checkIfThereArePendingNotificationsForMember(Long memberId, String notificationType);

    Integer getVillageIdFromAshaArea(Integer areaId);
}
