
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.model.Email;
import com.argusoft.medplat.event.dto.EventConfigTypeDto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 *     Define methods for email
 * </p>
 * @author kunjan
 * @since 27/08/2020 4:30
 */
public interface EmailService {
    /**
     * Sends given email
     * @param email An instance of Email
     */
     void sendMail(Email email);

    /**
     * Sends given email
     * @param email An instance of Email
     * @param emailPropertyFile An email property file
     */
     void sendMail(Email email, String emailPropertyFile);

    /**
     * Sends email of notification or timer event
     * @param notificationConfigTypeDto An instance of EventConfigTypeDto
     * @param queryDataLists A list of query data
     */
     void handle(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists);
    
}
