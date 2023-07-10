/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.service;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.event.dao.MobileNotificationPendingDetailDao;
import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.dto.MobileEventConfigDto;
import com.argusoft.medplat.event.model.MobileNotificationPendingDetail;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * <p>
 *     Define services for mobile notification handler.
 * </p>
 * @author kpatel
 * @since 26/08/20 11:00 AM
 *
 */
@Service("MobileNotificationHandler")
@Transactional // requirement of this could be seen later
public class MobileNotificationHandler {

    @Autowired
    private MobileNotificationPendingDetailDao mobileNotificationPendingDetailDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
//    @Autowired
//    private FamilyDao familyDao;

    /**
     * Handle mobile notification.
     * @param notificationConfigTypeDto Notification type details.
     * @param queryDataLists List of query data.
     */
    public void handle(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
            Object value = queryDataList.get(notificationConfigTypeDto.getBaseDateFieldName());
            if (!queryDataList.containsKey(notificationConfigTypeDto.getBaseDateFieldName())) {
                throw new ImtechoSystemException("Following base date field name not found : " + notificationConfigTypeDto.getBaseDateFieldName() + " and Event config id : " + notificationConfigTypeDto.getId(), 503);
            }
            Integer userId = retrieveUserIdForMobileNotification(notificationConfigTypeDto,queryDataList);
            Integer familyId = retrieveFamilyIdForMobileNotification(notificationConfigTypeDto,queryDataList);

            Integer memberId = retrieveMemberIdForMobileNotification(notificationConfigTypeDto,queryDataList);
            if (userId == null && familyId == null && memberId == null) {
                throw new ImtechoSystemException("No value found for user_id or family_id or member_id.Event Config Type Id : " + notificationConfigTypeDto.getId(), 503);
            }
            if (value == null) {
                throw new ImtechoSystemException(notificationConfigTypeDto.getBaseDateFieldName() + " field value found empty.Event Config Type Id : " + notificationConfigTypeDto.getId(), 503);
            }
            Integer refcode = -1;
            if (notificationConfigTypeDto.getRefCode() != null && queryDataList.get(notificationConfigTypeDto.getRefCode()) != null) {
                    refcode = Integer.parseInt(queryDataList.get(notificationConfigTypeDto.getRefCode()).toString());
            }
            Date baseDate = convertToDate(value.toString());
            MobileNotificationPendingDetail mobileNotificationPendingDetail = new MobileNotificationPendingDetail();
            mobileNotificationPendingDetail.setBaseDate(baseDate);
            mobileNotificationPendingDetail.setNotificationConfigTypeId(notificationConfigTypeDto.getId());
            mobileNotificationPendingDetail.setUserId(userId);
            mobileNotificationPendingDetail.setFamilyId(familyId);
            mobileNotificationPendingDetail.setMemberId(memberId);
            mobileNotificationPendingDetail.setRefCode(refcode);
            mobileNotificationPendingDetail.setState("PENDING");
            mobileNotificationPendingDetailDao.create(mobileNotificationPendingDetail);
            createCurrentNotificationIfAny(baseDate, mobileNotificationPendingDetail, notificationConfigTypeDto, familyId);
        }
    }

    /**
     * Retrieve user id for mobile notification.
     * @param notificationConfigTypeDto Notification type details.
     * @param queryDataList List of query data.
     * @return Returns user id.
     */
    private Integer retrieveUserIdForMobileNotification(EventConfigTypeDto notificationConfigTypeDto,LinkedHashMap<String, Object> queryDataList){
        Integer userId = null;
        if (notificationConfigTypeDto.getUserFieldName() != null && queryDataList.get(notificationConfigTypeDto.getUserFieldName()) != null) {
            userId = Integer.parseInt(queryDataList.get(notificationConfigTypeDto.getUserFieldName()).toString());
        }
        return userId;
    }

    /**
     * Retrieve family id for mobile notification.
     * @param notificationConfigTypeDto Notification type details.
     * @param queryDataList List of query data.
     * @return Returns family id.
     */
    private Integer retrieveFamilyIdForMobileNotification(EventConfigTypeDto notificationConfigTypeDto,LinkedHashMap<String, Object> queryDataList){
        Integer familyId = null;
        if (notificationConfigTypeDto.getFamilyFieldName() != null && queryDataList.get(notificationConfigTypeDto.getFamilyFieldName()) != null) {
            familyId = Integer.parseInt(queryDataList.get(notificationConfigTypeDto.getFamilyFieldName()).toString());
        }
        return familyId;
    }

    /**
     * Retrieve member id for mobile notification.
     * @param notificationConfigTypeDto Notification type details.
     * @param queryDataList List of query data.
     * @return Returns member id.
     */
    private Integer retrieveMemberIdForMobileNotification(EventConfigTypeDto notificationConfigTypeDto,LinkedHashMap<String, Object> queryDataList){
        Integer memberId = null;
        if (notificationConfigTypeDto.getMemberFieldName() != null && queryDataList.get(notificationConfigTypeDto.getMemberFieldName()) != null) {
            memberId = Integer.parseInt(queryDataList.get(notificationConfigTypeDto.getMemberFieldName()).toString());
        }
        return memberId;
    }
    /**
     * Create current notification.
     * @param baseDate Base date.
     * @param mobileNotificationPendingDetail Mobile notification pending details.
     * @param notificationConfigTypeDto Notification config type details.
     * @param familyId Family id.
     */
    private void createCurrentNotificationIfAny(Date baseDate,
            MobileNotificationPendingDetail mobileNotificationPendingDetail,
            EventConfigTypeDto notificationConfigTypeDto,
            Integer familyId) {
        List<MobileEventConfigDto> mobileNotificationConfigs = notificationConfigTypeDto.getMobileNotificationConfigs();
        for (MobileEventConfigDto mobileNotificationConfig : mobileNotificationConfigs) {
            Date scheduleDate = ImtechoUtil.calculateNewDate(baseDate, mobileNotificationConfig.getNumberOfDaysAddedForOnDate());
            Date expiryDate = null;
            if (scheduleDate.before(new Date())) {
                scheduleDate = new Date();
                Boolean needToCreateNotification = true;
                if (mobileNotificationConfig.getNumberOfDaysAddedForExpiryDate() != null) {
                    expiryDate = ImtechoUtil.calculateNewDate(baseDate, mobileNotificationConfig.getNumberOfDaysAddedForExpiryDate());
                    if (expiryDate.before(new Date())) {
                        needToCreateNotification = false;
                    }
                }
                createNotification(needToCreateNotification,notificationConfigTypeDto,
                        mobileNotificationPendingDetail,mobileNotificationConfig,familyId,scheduleDate,expiryDate);
            }
        }
    }

    /**
     * Create notification.
     * @param needToCreateNotification Check for need of create notification.
     * @param notificationConfigTypeDto Notification config type details.
     * @param mobileNotificationPendingDetail Mobile notification pending details.
     * @param mobileNotificationConfig Mobile notification config.
     * @param familyId Family id.
     * @param scheduleDate Schedule date.
     * @param expiryDate Expiry date.
     */
    private void createNotification(Boolean needToCreateNotification, EventConfigTypeDto notificationConfigTypeDto,
                                    MobileNotificationPendingDetail mobileNotificationPendingDetail,
                                    MobileEventConfigDto mobileNotificationConfig, Integer familyId,
                                    Date scheduleDate,Date expiryDate){
        if (needToCreateNotification != null) {
            TechoNotificationMaster techoNotificationMaster = new TechoNotificationMaster();
            techoNotificationMaster.setNotificationTypeId(notificationConfigTypeDto.getMobileNotificationType());
            techoNotificationMaster.setNotificationCode(mobileNotificationConfig.getNotificationCode());
//            if (mobileNotificationPendingDetail.getFamilyId() != null || mobileNotificationPendingDetail.getMemberId() != null) {
//                FamilyEntity familyEntity = familyDao.retrieveById(familyId);
//                if (familyEntity.getAreaId() != null) {
//                    techoNotificationMaster.setLocationId(familyEntity.getAreaId());
//                } else {
//                    techoNotificationMaster.setLocationId(familyEntity.getLocationId());
//                }
//            }
            techoNotificationMaster.setUserId(mobileNotificationPendingDetail.getUserId());
            techoNotificationMaster.setFamilyId(mobileNotificationPendingDetail.getFamilyId());
            techoNotificationMaster.setMemberId(mobileNotificationPendingDetail.getMemberId());
            techoNotificationMaster.setScheduleDate(scheduleDate);
            techoNotificationMaster.setExpiryDate(expiryDate);
            if (mobileNotificationConfig.getNumberOfDaysAddedForDueDate() != null) {
                techoNotificationMaster.setDueOn(ImtechoUtil.calculateNewDate(scheduleDate, mobileNotificationConfig.getNumberOfDaysAddedForDueDate()));
            }
            techoNotificationMaster.setState(TechoNotificationMaster.State.PENDING);
            techoNotificationMaster.setRefCode(mobileNotificationPendingDetail.getRefCode());
            techoNotificationMasterDao.create(techoNotificationMaster);
        }
    }

    /**
     * Convert string into date.
     * @param dateString Date string.
     * @return Returns date.
     */
    private Date convertToDate(String dateString) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        } catch (ParseException ex) {
            throw new ImtechoSystemException("Date format issue", 503);
        }
    }

}
