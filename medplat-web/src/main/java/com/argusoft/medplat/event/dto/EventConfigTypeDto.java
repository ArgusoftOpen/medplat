/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dto;

import com.argusoft.medplat.event.model.EventConfigurationType;

import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 *     Used for event config type.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class EventConfigTypeDto {

    private String id;
    private EventConfigurationType.Type type;
    private String template;
    private Integer mobileNotificationType;
    private UUID mobileNotificationTypeUUID;
    private String emailSubject;
    private String emailSubjectParameter;
    private String templateParameter;
    private String baseDateFieldName;
    private String refCode;
    private String memberFieldName;
    private String userFieldName;
    private String familyFieldName;
    private EventConfigurationType.TriggerWhen trigerWhen; // miniute/hourly/daily/monthly/yearly/immidetly
    private Short day;
    private Short hour;
    private Short miniute;
    private List<MobileEventConfigDto> mobileNotificationConfigs;
    private Integer configId;
    private Integer queryMasterId;
    private String queryCode;
    private List<QueryMasterParamterDto> queryMasterParamJson;
    private String exceptionType;
    private String exceptionMessage;
    private SmsConfigDto smsConfigJson;
    private PushNotificationConfigDto pushNotificationConfigJson;
    private Integer systemFunctionId;
    private List<QueryMasterParamterDto> functionParams;

    public EventConfigurationType.Type getType() {
        return type;
    }

    public void setType(EventConfigurationType.Type type) {
        this.type = type;
    }

    public EventConfigurationType.TriggerWhen getTrigerWhen() {
        return trigerWhen;
    }

    public void setTrigerWhen(EventConfigurationType.TriggerWhen trigerWhen) {
        this.trigerWhen = trigerWhen;
    }

    public Short getDay() {
        return day;
    }

    public void setDay(Short day) {
        this.day = day;
    }

    public Short getHour() {
        return hour;
    }

    public void setHour(Short hour) {
        this.hour = hour;
    }

    public Short getMiniute() {
        return miniute;
    }

    public void setMiniute(Short miniute) {
        this.miniute = miniute;
    }

    public List<MobileEventConfigDto> getMobileNotificationConfigs() {
        return mobileNotificationConfigs;
    }

    public void setMobileNotificationConfigs(List<MobileEventConfigDto> mobileNotificationConfigs) {
        this.mobileNotificationConfigs = mobileNotificationConfigs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getMobileNotificationType() {
        return mobileNotificationType;
    }

    public void setMobileNotificationType(Integer mobileNotificationType) {
        this.mobileNotificationType = mobileNotificationType;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailSubjectParameter() {
        return emailSubjectParameter;
    }

    public void setEmailSubjectParameter(String emailSubjectParameter) {
        this.emailSubjectParameter = emailSubjectParameter;
    }

    public String getTemplateParameter() {
        return templateParameter;
    }

    public void setTemplateParameter(String templateParameter) {
        this.templateParameter = templateParameter;
    }

    public String getBaseDateFieldName() {
        return baseDateFieldName;
    }

    public void setBaseDateFieldName(String baseDateFieldName) {
        this.baseDateFieldName = baseDateFieldName;
    }

    public PushNotificationConfigDto getPushNotificationConfigJson() {
        return pushNotificationConfigJson;
    }

    public void setPushNotificationConfigJson(PushNotificationConfigDto pushNotificationConfigJson) {
        this.pushNotificationConfigJson = pushNotificationConfigJson;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getMemberFieldName() {
        return memberFieldName;
    }

    public void setMemberFieldName(String memberFieldName) {
        this.memberFieldName = memberFieldName;
    }

    public String getUserFieldName() {
        return userFieldName;
    }

    public void setUserFieldName(String userFieldName) {
        this.userFieldName = userFieldName;
    }

    public String getFamilyFieldName() {
        return familyFieldName;
    }

    public void setFamilyFieldName(String familyFieldName) {
        this.familyFieldName = familyFieldName;
    }

    public Integer getQueryMasterId() {
        return queryMasterId;
    }

    public void setQueryMasterId(Integer queryMasterId) {
        this.queryMasterId = queryMasterId;
    }

    public List<QueryMasterParamterDto> getQueryMasterParamJson() {
        return queryMasterParamJson;
    }

    public void setQueryMasterParamJson(List<QueryMasterParamterDto> queryMasterParamJson) {
        this.queryMasterParamJson = queryMasterParamJson;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public SmsConfigDto getSmsConfigJson() {
        return smsConfigJson;
    }

    public void setSmsConfigJson(SmsConfigDto smsConfigJson) {
        this.smsConfigJson = smsConfigJson;
    }

    public Integer getSystemFunctionId() {
        return this.systemFunctionId;
    }

    public void setSystemFunctionId(Integer systemFunctionId) {
        this.systemFunctionId = systemFunctionId;
    }

    public List<QueryMasterParamterDto> getFunctionParams() {
        return functionParams;
    }

    public void setFunctionParams(List<QueryMasterParamterDto> functionParams) {
        this.functionParams = functionParams;
    }

    public UUID getMobileNotificationTypeUUID() {
        return mobileNotificationTypeUUID;
    }

    public void setMobileNotificationTypeUUID(UUID mobileNotificationTypeUUID) {
        this.mobileNotificationTypeUUID = mobileNotificationTypeUUID;
    }

}