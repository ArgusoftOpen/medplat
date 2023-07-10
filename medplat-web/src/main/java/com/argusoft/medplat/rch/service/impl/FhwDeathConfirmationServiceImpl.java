package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AshaReportedEventDao;
import com.argusoft.medplat.rch.model.AshaReportedEventMaster;
import com.argusoft.medplat.rch.service.FhwDeathConfirmationService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Define services for FHW death confirmation.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class FhwDeathConfirmationServiceImpl implements FhwDeathConfirmationService {

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;

    @Autowired
    private QueryMasterService queryMasterService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private AshaReportedEventDao ashaReportedEventDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeDeathConfirmationForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        TechoNotificationMaster notificationMaster = techoNotificationMasterDao.retrieveById(Integer.valueOf(parsedRecordBean.getNotificationId()));

        if (notificationMaster == null) {
            throw new ImtechoMobileException("Notification not found", 100);
        }

        MemberEntity memberEntity = memberDao.retrieveById(notificationMaster.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveById(notificationMaster.getFamilyId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        long dateOfDeath = Long.parseLong(keyAndAnswerMap.get("10"));
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("mark_member_as_death");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("member_id", memberEntity.getId());
        if (familyEntity.getAreaId() != null) {
            parameters.put("location_id", familyEntity.getAreaId());
        } else {
            parameters.put("location_id", familyEntity.getLocationId());
        }
        parameters.put("action_by", user.getId());
        parameters.put("family_id", familyEntity.getId());
        parameters.put("death_date", sdf.format(new Date(dateOfDeath)));
        parameters.put("place_of_death", keyAndAnswerMap.get("11"));
        parameters.put("death_reason", keyAndAnswerMap.get("12"));
        parameters.put("other_death_reason", keyAndAnswerMap.get("14"));
        parameters.put("service_type", "FHW_DEATH_CONF");
        parameters.put("reference_id", notificationMaster.getId());
        parameters.put("health_infra_id", keyAndAnswerMap.get("20"));
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        queryMasterService.executeQuery(queryDtos, true);

        notificationMaster.setState(TechoNotificationMaster.State.COMPLETED);
        notificationMaster.setActionBy(user.getId());
        techoNotificationMasterDao.update(notificationMaster);

        if (notificationMaster.getRelatedId() != null && notificationMaster.getOtherDetails() != null
                && notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_MEMBER_DEATH)) {
            AshaReportedEventMaster eventMaster = ashaReportedEventDao.retrieveById(notificationMaster.getRelatedId());
            eventMaster.setAction(RchConstants.ASHA_REPORTED_EVENT_CONFIRMED);
            eventMaster.setActionOn(new Date(Long.parseLong(parsedRecordBean.getMobileDate())));
            eventMaster.setActionBy(user.getId());
            ashaReportedEventDao.update(eventMaster);
        }

        sendReadonlyNotificationToAsha(memberEntity, familyEntity, user);
        return 1;
    }

    /**
     * Send read only notification for ASHA.
     *
     * @param memberEntity Member details.
     * @param familyEntity Family details.
     * @param user         User details.
     */
    private void sendReadonlyNotificationToAsha(MemberEntity memberEntity, FamilyEntity familyEntity, UserMaster user) {
        StringBuilder header = new StringBuilder();
        header.append(memberEntity.getUniqueHealthId())
                .append(" - ")
                .append(memberEntity.getFirstName())
                .append(" ")
                .append(memberEntity.getMiddleName())
                .append(" ")
                .append(memberEntity.getLastName());

        StringBuilder text = new StringBuilder();
        text.append("Your request for member death has been confirmed.")
                .append("\n\n")
                .append("Name : ")
                .append(memberEntity.getFirstName())
                .append(" ")
                .append(memberEntity.getMiddleName())
                .append(" ")
                .append(memberEntity.getLastName())
                .append("\n")
                .append("Health Id : ")
                .append(memberEntity.getUniqueHealthId())
                .append("\n")
                .append("Family Id : ")
                .append(memberEntity.getFamilyId())
                .append("\n")
                .append("Confirmed by : ")
                .append(user.getFirstName())
                .append(" ")
                .append(user.getMiddleName())
                .append(" ")
                .append(user.getLastName())
                .append(" (")
                .append(user.getContactNumber())
                .append(")");

        TechoNotificationMaster readOnlyNotification = new TechoNotificationMaster();
        readOnlyNotification.setState(TechoNotificationMaster.State.PENDING);
        readOnlyNotification.setNotificationTypeId(notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_ASHA_READ_ONLY).getId());
        readOnlyNotification.setLocationId(familyEntity.getAreaId());
        readOnlyNotification.setMemberId(memberEntity.getId());
        readOnlyNotification.setFamilyId(familyEntity.getId());
        readOnlyNotification.setScheduleDate(new Date());
        readOnlyNotification.setHeader(header.toString());
        readOnlyNotification.setOtherDetails(text.toString());

        techoNotificationMasterDao.create(readOnlyNotification);
    }
}
