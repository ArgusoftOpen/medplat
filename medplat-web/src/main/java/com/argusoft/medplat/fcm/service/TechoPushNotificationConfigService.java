package com.argusoft.medplat.fcm.service;

import com.argusoft.medplat.fcm.dto.TechoPushNotificationConfigDto;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDisplayDto;

import java.math.BigInteger;
import java.util.List;

/**
 * @author nihar
 * @since 13/10/22 2:19 PM
 */
public interface TechoPushNotificationConfigService {

    void createOrUpdateNotificationConfig(TechoPushNotificationConfigDto
                                                  techoPushNotificationConfigDto);


    List<TechoPushNotificationDisplayDto> getPushNotificationConfigs(BigInteger limit, Integer offset);

    TechoPushNotificationConfigDto getNotificationConfigById(Integer id);

    void toggleNotificationConfigState(Integer id);
}
