
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.sms_queue.dto.SmsQueueDto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 *     Define methods for sms
 * </p>
 * @author prateek
 * @since 27/08/2020 4:30
 */
public interface SmsService {
     /**
      * Sends a sms
      * @param mobileNumber A mobile number
      * @param message A message of sms
      * @param isPriority Is it priority sms or not
      * @param messageType A type of sms
      * @return A crated id of sms
      */
     Integer sendSms(String mobileNumber, String message, Boolean isPriority, String messageType);

     /**
      * Sends sms of event configuration
      * @param notificationConfigTypeDto An instance of EventConfigTypeDto
      * @param queryDataLists A list of queryDataLists
      */
     void handle(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists);

     public Integer sendSms(SmsQueueDto smsQueueDto);

//     public void loadAllSmsTypes();

//     void updateSmsTypeMap(String smsType, String templateId, State state);
}
