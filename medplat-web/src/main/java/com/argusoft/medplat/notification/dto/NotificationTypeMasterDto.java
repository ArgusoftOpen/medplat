/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dto;

import com.argusoft.medplat.notification.model.NotificationTypeMaster;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 * <p>
 *     Used for notification type.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
public class NotificationTypeMasterDto {

    private Integer id;
    private String notificationName;
    private String code;
    private Integer roleId;
    private String notificationType;
    private NotificationTypeMaster.State state;
    private NotificationTypeMaster.NotificationFor notificationFor;
    private Set<Integer> roles;
    private String dataQuery;
    private String actionQuery;
    private List<EscalationLevelMasterDto> escalationLevels;
    private Integer orderNo;
    private String colorCode;
    private String dataFor;
    private Boolean urlBasedAction;
    private String url;
    private Boolean modalBasedAction;
    private String modalName;
    private Boolean isLocationFilterRequired;
    private Integer fetchUptoLevel;
    private Integer requiredUptoLevel;
    private Boolean isFetchAccordingAOI;
    private UUID uuid;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
  
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationTypeMaster.State getState() {
        return state;
    }

    public void setState(NotificationTypeMaster.State state) {
        this.state = state;
    }

    public NotificationTypeMaster.NotificationFor getNotificationFor() {
        return notificationFor;
    }

    public void setNotificationFor(NotificationTypeMaster.NotificationFor notificationFor) {
        this.notificationFor = notificationFor;
    }

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }

    public String getDataQuery() {
        return dataQuery;
    }

    public void setDataQuery(String dataQuery) {
        this.dataQuery = dataQuery;
    }

    public String getActionQuery() {
        return actionQuery;
    }

    public void setActionQuery(String actionQuery) {
        this.actionQuery = actionQuery;
    }

    public List<EscalationLevelMasterDto> getEscalationLevels() {
        return escalationLevels;
    }

    public void setEscalationLevels(List<EscalationLevelMasterDto> escalationLevels) {
        this.escalationLevels = escalationLevels;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getDataFor() {
        return dataFor;
    }

    public void setDataFor(String dataFor) {
        this.dataFor = dataFor;
    }

    public Boolean getUrlBasedAction() {
        return urlBasedAction;
    }

    public void setUrlBasedAction(Boolean urlBasedAction) {
        this.urlBasedAction = urlBasedAction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getModalBasedAction() {
        return modalBasedAction;
    }

    public void setModalBasedAction(Boolean modalBasedAction) {
        this.modalBasedAction = modalBasedAction;
    }

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    public Boolean getIsLocationFilterRequired() {
        return isLocationFilterRequired;
    }

    public void setIsLocationFilterRequired(Boolean isLocationFilterRequired) {
        this.isLocationFilterRequired = isLocationFilterRequired;
    }

    public Integer getFetchUptoLevel() {
        return fetchUptoLevel;
    }

    public void setFetchUptoLevel(Integer fetchUptoLevel) {
        this.fetchUptoLevel = fetchUptoLevel;
    }

    public Integer getRequiredUptoLevel() {
        return requiredUptoLevel;
    }

    public void setRequiredUptoLevel(Integer requiredUptoLevel) {
        this.requiredUptoLevel = requiredUptoLevel;
    }

    public Boolean getIsFetchAccordingAOI() {
        return isFetchAccordingAOI;
    }

    public void setIsFetchAccordingAOI(Boolean isFetchAccordingAOI) {
        this.isFetchAccordingAOI = isFetchAccordingAOI;
    }

}
