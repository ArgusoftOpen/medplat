package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AshaReportedEventDao;
import com.argusoft.medplat.rch.dto.AshaEventRejectionDataBean;
import com.argusoft.medplat.rch.dto.AshaReportedEventDataBean;
import com.argusoft.medplat.rch.mapper.AshaReportedEventMapper;
import com.argusoft.medplat.rch.model.AshaReportedEventMaster;
import com.argusoft.medplat.rch.service.AshaReportedEventService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * Define services for ASHA reported event.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AshaReportedEventServiceImpl implements AshaReportedEventService {
    public static final String CONST_NAME = "Name : ";
    public static final String CONST_HEALTH_ID = "Health Id : ";
    public static final String CONST_FAMILY_ID = "Family Id : ";
    public static final String CONST_REJECTED_BY = "Rejected by : ";
    public static final String CONST_HOF = "Head of the family : ";

    @Autowired
    private AshaReportedEventDao ashaReportedEventDao;

    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeDeliveryReportedByAsha(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        if (keyAndAnswerMap.get("51") != null && keyAndAnswerMap.get("51").equals("1")) {
            Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
            Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
            Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));

            AshaReportedEventMaster ashaReportedEventMaster = new AshaReportedEventMaster();
            ashaReportedEventMaster.setEventType(MobileConstantUtil.ASHA_REPORT_MEMBER_DELIVERY);
            ashaReportedEventMaster.setLocationId(locationId);
            ashaReportedEventMaster.setFamilyId(familyId);
            ashaReportedEventMaster.setMemberId(memberId);
            ashaReportedEventMaster.setReportedOn(new Date(Long.parseLong(parsedRecordBean.getMobileDate())));
            ashaReportedEventDao.create(ashaReportedEventMaster);

            this.createNotificationForReportedEventByAsha(memberId, MobileConstantUtil.NOTIFICATION_FHW_DELIVERY_CONF,
                    familyId, locationId, ashaReportedEventMaster.getId(), MobileConstantUtil.ASHA_REPORT_MEMBER_DELIVERY);

            return ashaReportedEventMaster.getId();
        }
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAshaReportedEvent(ParsedRecordBean parsedRecordBean, AshaReportedEventDataBean ashaReportedEventDataBean, UserMaster user) {
        AshaReportedEventMaster reportedEventMaster = AshaReportedEventMapper.convertAshaReportedEventDataBeanToMaster(ashaReportedEventDataBean);
        ashaReportedEventDao.create(reportedEventMaster);

        switch (reportedEventMaster.getEventType()) {
            case MobileConstantUtil.ASHA_REPORT_MEMBER_DELIVERY:
                this.createNotificationForReportedEventByAsha(reportedEventMaster.getMemberId(),
                        MobileConstantUtil.NOTIFICATION_FHW_DELIVERY_CONF,
                        reportedEventMaster.getFamilyId(), reportedEventMaster.getLocationId(), reportedEventMaster.getId(),
                        MobileConstantUtil.ASHA_REPORT_MEMBER_DELIVERY);
                break;
            case MobileConstantUtil.ASHA_REPORT_MEMBER_DEATH:
                this.createNotificationForReportedEventByAsha(reportedEventMaster.getMemberId(),
                        MobileConstantUtil.NOTIFICATION_FHW_DEATH_CONF,
                        reportedEventMaster.getFamilyId(), reportedEventMaster.getLocationId(), reportedEventMaster.getId(),
                        MobileConstantUtil.ASHA_REPORT_MEMBER_DEATH);
                break;
            case MobileConstantUtil.ASHA_REPORT_MEMBER_MIGRATION:
                this.createNotificationForReportedEventByAsha(reportedEventMaster.getMemberId(),
                        MobileConstantUtil.NOTIFICATION_FHW_MEMBER_MIGRATION,
                        reportedEventMaster.getFamilyId(), reportedEventMaster.getLocationId(), reportedEventMaster.getId(),
                        MobileConstantUtil.ASHA_REPORT_MEMBER_MIGRATION);
                break;
            case MobileConstantUtil.ASHA_REPORT_FAMILY_MIGRATION:
                this.createNotificationForReportedEventByAsha(reportedEventMaster.getMemberId(),
                        MobileConstantUtil.NOTIFICATION_FHW_FAMILY_MIGRATION,
                        reportedEventMaster.getFamilyId(), reportedEventMaster.getLocationId(), reportedEventMaster.getId(),
                        MobileConstantUtil.ASHA_REPORT_FAMILY_MIGRATION);
                break;
            case MobileConstantUtil.ASHA_REPORT_FAMILY_SPLIT:
                this.createNotificationForReportedEventByAsha(reportedEventMaster.getMemberId(),
                        MobileConstantUtil.NOTIFICATION_FHW_FAMILY_SPLIT,
                        reportedEventMaster.getFamilyId(), reportedEventMaster.getLocationId(), reportedEventMaster.getId(),
                        MobileConstantUtil.ASHA_REPORT_FAMILY_SPLIT);
                break;
            default:
        }

        return reportedEventMaster.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAshaEventRejectionForm(ParsedRecordBean parsedRecordBean, AshaEventRejectionDataBean rejectionDataBean, UserMaster user) {
        TechoNotificationMaster notificationMaster = techoNotificationMasterDao.retrieveById(rejectionDataBean.getNotificationId());

        if (notificationMaster.getOtherDetails() != null &&
                (notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_MEMBER_DELIVERY)
                        || notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_MEMBER_DEATH)
                        || notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_MEMBER_MIGRATION)
                        || notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_FAMILY_MIGRATION)
                        || notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_FAMILY_SPLIT))) {
            Integer relatedId = notificationMaster.getRelatedId();
            AshaReportedEventMaster eventMaster = ashaReportedEventDao.retrieveById(relatedId);
            eventMaster.setAction(RchConstants.ASHA_REPORTED_EVENT_REJECTED);
            eventMaster.setActionOn(new Date(rejectionDataBean.getRejectedOn()));
            eventMaster.setActionBy(user.getId());
            ashaReportedEventDao.update(eventMaster);
        }

        notificationMaster.setState(TechoNotificationMaster.State.COMPLETED);
        notificationMaster.setActionBy(user.getId());
        techoNotificationMasterDao.update(notificationMaster);

        NotificationTypeMaster typeMaster = notificationTypeMasterDao.retrieveById(notificationMaster.getNotificationTypeId());
        StringBuilder header = new StringBuilder();
        StringBuilder text = new StringBuilder();
        MemberEntity memberEntity;
        FamilyEntity familyEntity;

        switch (typeMaster.getCode()) {
            case MobileConstantUtil.NOTIFICATION_FHW_DELIVERY_CONF:
                memberEntity = memberDao.retrieveById(notificationMaster.getMemberId());
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                text.append("Your request for member delivery is rejected.")
                        .append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n")
                        .append(CONST_REJECTED_BY)
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getMiddleName())
                        .append(" ")
                        .append(user.getLastName())
                        .append(" (")
                        .append(user.getContactNumber())
                        .append(")");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_DEATH_CONF:
                memberEntity = memberDao.retrieveById(notificationMaster.getMemberId());
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                text.append("Your request for member death is rejected.")
                        .append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n")
                        .append(CONST_REJECTED_BY)
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getMiddleName())
                        .append(" ")
                        .append(user.getLastName())
                        .append(" (")
                        .append(user.getContactNumber())
                        .append(")");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_MEMBER_MIGRATION:
                memberEntity = memberDao.retrieveById(notificationMaster.getMemberId());
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                text.append("Your request for member migration is rejected.")
                        .append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n")
                        .append(CONST_REJECTED_BY)
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getMiddleName())
                        .append(" ")
                        .append(user.getLastName())
                        .append(" (")
                        .append(user.getContactNumber())
                        .append(")");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_FAMILY_MIGRATION:
                familyEntity = familyDao.retrieveById(notificationMaster.getFamilyId());
                memberEntity = memberDao.retrieveById(familyEntity.getHeadOfFamily());
                header.append(familyEntity.getFamilyId());
                if (memberEntity != null) {
                    header.append(" - ")
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName());
                }
                text.append("Your request for family migration is rejected.")
                        .append("\n\n")
                        .append(CONST_FAMILY_ID)
                        .append(familyEntity.getFamilyId());
                if (memberEntity != null) {
                    text.append("\n")
                            .append(CONST_HOF)
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName());
                }

                text.append("\n")
                        .append(CONST_REJECTED_BY)
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getMiddleName())
                        .append(" ")
                        .append(user.getLastName())
                        .append(" (")
                        .append(user.getContactNumber())
                        .append(")");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_FAMILY_SPLIT:
                familyEntity = familyDao.retrieveById(notificationMaster.getFamilyId());
                memberEntity = memberDao.retrieveById(familyEntity.getHeadOfFamily());
                header.append(familyEntity.getFamilyId());
                if (memberEntity != null) {
                    header.append(" - ")
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName());
                }
                text.append("Your request for family split is rejected.")
                        .append("\n\n")
                        .append(CONST_FAMILY_ID)
                        .append(familyEntity.getFamilyId());
                if (memberEntity != null) {
                    text.append("\n")
                            .append(CONST_HOF)
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName());
                }

                text.append("\n")
                        .append(CONST_REJECTED_BY)
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getMiddleName())
                        .append(" ")
                        .append(user.getLastName())
                        .append(" (")
                        .append(user.getContactNumber())
                        .append(")");
                break;
            default:
        }

        TechoNotificationMaster readOnlyNotification = new TechoNotificationMaster();
        readOnlyNotification.setState(TechoNotificationMaster.State.PENDING);
        readOnlyNotification.setNotificationTypeId(notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_ASHA_READ_ONLY).getId());
        readOnlyNotification.setLocationId(notificationMaster.getLocationId());
        readOnlyNotification.setMemberId(notificationMaster.getMemberId());
        readOnlyNotification.setFamilyId(notificationMaster.getFamilyId());
        readOnlyNotification.setScheduleDate(new Date());
        readOnlyNotification.setHeader(header.toString());
        readOnlyNotification.setOtherDetails(text.toString());

        techoNotificationMasterDao.create(readOnlyNotification);
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNotificationForReportedEventByAsha(Integer memberId, String notificationType, Integer familyId, Integer locationId, Integer relatedId, String eventType) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 3);
        Date dueDate = instance.getTime();

        TechoNotificationMaster notification = new TechoNotificationMaster();
        notification.setNotificationTypeId(notificationTypeMasterDao.retrieveByCode(notificationType).getId());
        notification.setMemberId(memberId);
        notification.setFamilyId(familyId);
        notification.setLocationId(locationId);
        notification.setScheduleDate(new Date());
        notification.setDueOn(dueDate);
        notification.setState(TechoNotificationMaster.State.PENDING);
        notification.setOtherDetails(eventType);
        notification.setRelatedId(relatedId);
        techoNotificationMasterDao.create(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReadOnlyNotificationForAsha(boolean isConfirmed, String notificationCode, MemberEntity memberEntity, FamilyEntity familyEntity, UserMaster user) {
        StringBuilder header = new StringBuilder();
        StringBuilder text = new StringBuilder();

        switch (notificationCode) {
            case MobileConstantUtil.NOTIFICATION_FHW_PREGNANCY_CONF:
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                if (isConfirmed) {
                    text.append("Your request for member pregnancy is approved.");
                } else {
                    text.append("Your request for member pregnancy is rejected.");
                }
                text.append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n");
                break;

            case MobileConstantUtil.NOTIFICATION_FHW_DEATH_CONF:
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                if (isConfirmed) {
                    text.append("Your request for member death is approved.");
                } else {
                    text.append("Your request for member death is rejected.");
                }
                text.append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_DELIVERY_CONF:
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                if (isConfirmed) {
                    text.append("Your request for member delivery is approved.");
                } else {
                    text.append("Your request for member delivery is rejected.");
                }
                text.append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_MEMBER_MIGRATION:
                header.append(memberEntity.getUniqueHealthId())
                        .append(" - ")
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName());

                if (isConfirmed) {
                    text.append("Your request for member migration is approved.");
                } else {
                    text.append("Your request for member migration is rejected.");
                }
                text.append("\n\n")
                        .append(CONST_NAME)
                        .append(memberEntity.getFirstName())
                        .append(" ")
                        .append(memberEntity.getMiddleName())
                        .append(" ")
                        .append(memberEntity.getLastName())
                        .append("\n")
                        .append(CONST_HEALTH_ID)
                        .append(memberEntity.getUniqueHealthId())
                        .append("\n")
                        .append(CONST_FAMILY_ID)
                        .append(memberEntity.getFamilyId())
                        .append("\n");
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_FAMILY_MIGRATION:
                memberEntity = memberDao.retrieveById(familyEntity.getHeadOfFamily());
                header.append(familyEntity.getFamilyId());
                if (isConfirmed) {
                    text.append("Your request for family migration is approved.");
                } else {
                    text.append("Your request for family migration is rejected.");
                }
                text.append("\n\n")
                        .append(CONST_FAMILY_ID)
                        .append(familyEntity.getFamilyId())
                        .append("\n");

                if (memberEntity != null) {
                    header.append(" - ")
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName());
                    text.append(CONST_HOF)
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName())
                            .append("\n");
                }
                break;
            case MobileConstantUtil.NOTIFICATION_FHW_FAMILY_SPLIT:
                memberEntity = memberDao.retrieveById(familyEntity.getHeadOfFamily());
                header.append(familyEntity.getFamilyId());
                if (isConfirmed) {
                    text.append("Your request for family split is approved.");
                } else {
                    text.append("Your request for family split is rejected.");
                }
                text.append("\n\n")
                        .append(CONST_FAMILY_ID)
                        .append(familyEntity.getFamilyId())
                        .append("\n");

                if (memberEntity != null) {
                    header.append(" - ")
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName());
                    text.append(CONST_HOF)
                            .append(memberEntity.getFirstName())
                            .append(" ")
                            .append(memberEntity.getMiddleName())
                            .append(" ")
                            .append(memberEntity.getLastName())
                            .append("\n");
                }
                break;
            default:
        }

        if (isConfirmed) {
            text.append("Approved by : ");
        } else {
            text.append(CONST_REJECTED_BY);
        }
        text.append(user.getFirstName())
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
        if (memberEntity != null) {
            readOnlyNotification.setMemberId(memberEntity.getId());
        }
        readOnlyNotification.setFamilyId(familyEntity.getId());
        readOnlyNotification.setScheduleDate(new Date());
        readOnlyNotification.setHeader(header.toString());
        readOnlyNotification.setOtherDetails(text.toString());
        techoNotificationMasterDao.create(readOnlyNotification);
    }
}
