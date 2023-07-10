/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.mapper;

import com.argusoft.medplat.notification.dto.NotificationTypeMasterDto;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * <p>
 *     Mapper for notification type master in order to convert dto to model or model to dto.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
public class NotificationTypeMasterMapper {
    private NotificationTypeMasterMapper(){
    }

    /**
     * Convert notification type details into entity.
     * @param notificationMasterDto Details of notification type.
     * @param notificationMaster Entity of notification type.
     * @param isMetodCallBySyncFunction Is method called by sync function.
     * @return Returns entity of notification type.
     */
    public static NotificationTypeMaster getNotificationMasterEntity(NotificationTypeMasterDto notificationMasterDto, NotificationTypeMaster notificationMaster,boolean isMetodCallBySyncFunction) {
        if (notificationMasterDto != null) {
            if (notificationMaster == null) {
                notificationMaster = new NotificationTypeMaster();
                notificationMaster.setState(NotificationTypeMaster.State.ACTIVE);
            }
            if (!isMetodCallBySyncFunction) {
                notificationMaster.setId(notificationMasterDto.getId());
            }
            notificationMaster.setNotificationName(notificationMasterDto.getNotificationName());
            notificationMaster.setCode(notificationMasterDto.getCode());
            notificationMaster.setNotificationType(notificationMasterDto.getNotificationType());
            notificationMaster.setRoleId(notificationMasterDto.getRoleId());
            notificationMaster.setNotificationFor(notificationMasterDto.getNotificationFor());
            notificationMaster.setRoles(notificationMasterDto.getRoles());
            notificationMaster.setDataQuery(notificationMasterDto.getDataQuery());
            notificationMaster.setActionQuery(notificationMasterDto.getActionQuery());
            notificationMaster.setOrderNo(notificationMasterDto.getOrderNo());
            notificationMaster.setColorCode(notificationMasterDto.getColorCode());
            notificationMaster.setDataFor(notificationMasterDto.getDataFor());
            notificationMaster.setUrlBasedAction(notificationMasterDto.getUrlBasedAction());
            notificationMaster.setUrl(notificationMasterDto.getUrl());
            notificationMaster.setModalBasedAction(notificationMasterDto.getModalBasedAction());
            notificationMaster.setModalName(notificationMasterDto.getModalName());
            notificationMaster.setIsLocationFilterRequired(notificationMasterDto.getIsLocationFilterRequired());
            notificationMaster.setFetchUptoLevel(notificationMasterDto.getFetchUptoLevel());
            notificationMaster.setRequiredUptoLevel(notificationMasterDto.getRequiredUptoLevel());
            notificationMaster.setIsFetchAccordingAOI(notificationMasterDto.getIsFetchAccordingAOI());
            notificationMaster.setUuid(notificationMasterDto.getUuid());
            return notificationMaster;
        }
        return null;
    }

    /**
     * Convert notification type entity into details.
     * @param notificationMaster Entity of notification type.
     * @return Returns notification type details.
     */
    public static NotificationTypeMasterDto getNotificationMasterDto(NotificationTypeMaster notificationMaster) {

        NotificationTypeMasterDto notificationMasterDto = new NotificationTypeMasterDto();
        notificationMasterDto.setId(notificationMaster.getId());
        notificationMasterDto.setNotificationName(notificationMaster.getNotificationName());
        notificationMasterDto.setCode(notificationMaster.getCode());
        notificationMasterDto.setNotificationType(notificationMaster.getNotificationType());
        notificationMasterDto.setRoleId(notificationMaster.getRoleId());
        notificationMasterDto.setState(notificationMaster.getState());
        notificationMasterDto.setNotificationFor(notificationMaster.getNotificationFor());
        notificationMasterDto.setRoles(notificationMaster.getRoles());
        notificationMasterDto.setDataQuery(notificationMaster.getDataQuery());
        notificationMasterDto.setActionQuery(notificationMaster.getActionQuery());
        notificationMasterDto.setOrderNo(notificationMaster.getOrderNo());
        notificationMasterDto.setColorCode(notificationMaster.getColorCode());
        notificationMasterDto.setDataFor(notificationMaster.getDataFor());
        notificationMasterDto.setUrlBasedAction(notificationMaster.getUrlBasedAction());
        notificationMasterDto.setUrl(notificationMaster.getUrl());
        notificationMasterDto.setModalBasedAction(notificationMaster.getModalBasedAction());
        notificationMasterDto.setModalName(notificationMaster.getModalName());
        notificationMasterDto.setIsLocationFilterRequired(notificationMaster.getIsLocationFilterRequired());
        notificationMasterDto.setFetchUptoLevel(notificationMaster.getFetchUptoLevel());
        notificationMasterDto.setRequiredUptoLevel(notificationMaster.getRequiredUptoLevel());
        notificationMasterDto.setIsFetchAccordingAOI(notificationMaster.getIsFetchAccordingAOI());
        notificationMasterDto.setUuid(notificationMaster.getUuid());
        return notificationMasterDto;
    }

    /**
     * Convert list of notification type entities into dto list.
     * @param notificationMasterList List of notification type entities.
     * @return Returns list of notification type details.
     */
    public static List<NotificationTypeMasterDto> getNotificationMasterDtoList(List<NotificationTypeMaster> notificationMasterList) {
        LinkedList<NotificationTypeMasterDto> notificationMasterDtoList = new LinkedList<>();
        for (NotificationTypeMaster notificationMaster : notificationMasterList) {
            NotificationTypeMasterDto notificationMasterDto = new NotificationTypeMasterDto();
            notificationMasterDto.setId(notificationMaster.getId());
            notificationMasterDto.setNotificationType(notificationMaster.getNotificationType());
            notificationMasterDto.setNotificationName(notificationMaster.getNotificationName());
            notificationMasterDto.setCode(notificationMaster.getCode());
            notificationMasterDto.setState(notificationMaster.getState());
            notificationMasterDto.setNotificationFor(notificationMaster.getNotificationFor());
            notificationMasterDtoList.push(notificationMasterDto);
            notificationMasterDto.setRoles(notificationMaster.getRoles());
            notificationMasterDto.setDataQuery(notificationMaster.getDataQuery());
            notificationMasterDto.setActionQuery(notificationMaster.getActionQuery());
            notificationMasterDto.setOrderNo(notificationMaster.getOrderNo());
            notificationMasterDto.setColorCode(notificationMaster.getColorCode());
            notificationMasterDto.setDataFor(notificationMaster.getDataFor());
            notificationMasterDto.setUrlBasedAction(notificationMaster.getUrlBasedAction());
            notificationMasterDto.setUrl(notificationMaster.getUrl());
            notificationMasterDto.setModalBasedAction(notificationMaster.getModalBasedAction());
            notificationMasterDto.setModalName(notificationMaster.getModalName());
            notificationMasterDto.setIsLocationFilterRequired(notificationMaster.getIsLocationFilterRequired());
            notificationMasterDto.setFetchUptoLevel(notificationMaster.getFetchUptoLevel());
            notificationMasterDto.setRequiredUptoLevel(notificationMaster.getRequiredUptoLevel());
            notificationMasterDto.setIsFetchAccordingAOI(notificationMaster.getIsFetchAccordingAOI());
            notificationMasterDto.setUuid(notificationMaster.getUuid());
        }
        return notificationMasterDtoList;
    }
}
