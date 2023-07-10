package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dto.AshaEventRejectionDataBean;
import com.argusoft.medplat.rch.dto.AshaReportedEventDataBean;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Map;

/**
 * <p>
 * Define services for ASHA reported event.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface AshaReportedEventService {

    /**
     * Store ASHA delivery reported details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storeDeliveryReportedByAsha(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    /**
     * Store ASHA reported event details.
     *
     * @param parsedRecordBean          Contains details like form fill up time, relative id, village id etc.
     * @param ashaReportedEventDataBean Asha reported event details.
     * @param user                      User details.
     * @return Returns id of store details.
     */
    Integer storeAshaReportedEvent(ParsedRecordBean parsedRecordBean, AshaReportedEventDataBean ashaReportedEventDataBean, UserMaster user);

    /**
     * Store ASHA event rejection form details.
     *
     * @param parsedRecordBean  Contains details like form fill up time, relative id, village id etc.
     * @param rejectionDataBean Rejection details.
     * @param user              User details.
     * @return Returns id of store details.
     */
    Integer storeAshaEventRejectionForm(ParsedRecordBean parsedRecordBean, AshaEventRejectionDataBean rejectionDataBean, UserMaster user);

    /**
     * Create notification for reported event by ASHA.
     *
     * @param memberId         Member id.
     * @param notificationType Notification type.
     * @param familyId         Family id.
     * @param locationId       Location id.
     * @param relatedId        Related id.
     * @param eventType        Event type.
     */
    void createNotificationForReportedEventByAsha(Integer memberId, String notificationType, Integer familyId, Integer locationId, Integer relatedId, String eventType);

    /**
     * Create read only notification for ASHA.
     *
     * @param isConfirmed      Is action confirmed or not.
     * @param notificationCode Notification code.
     * @param memberEntity     Member details.
     * @param familyEntity     Family details.
     * @param user             User details.
     */
    void createReadOnlyNotificationForAsha(boolean isConfirmed, String notificationCode, MemberEntity memberEntity, FamilyEntity familyEntity, UserMaster user);
}
