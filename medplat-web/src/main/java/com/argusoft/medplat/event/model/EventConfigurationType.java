/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define event_configuration_type entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "event_configuration_type")
public class EventConfigurationType implements Serializable {

    @Id
    @Basic(optional = false)
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 100)
    private Type type;
    @Column(name = "template", nullable = true, length = 500)
    private String template;
    @Column(name = "mobile_notification_type", nullable = true)
    private Integer mobileNotificationType;
    @Column(name = "email_subject", nullable = true, length = 500)
    private String emailSubject;
    @Column(name = "email_subject_parameter", nullable = true, length = 500)
    private String emailSubjectParameter;
    @Column(name = "template_parameter", nullable = true, length = 500)
    private String templateParameter;
    @Column(name = "base_date_field_name", nullable = true, length = 100)
    private String baseDateFieldName;
    @Column(name = "user_field_name", nullable = true, length = 100)
    private String userFieldName;
    @Column(name = "member_field_name", nullable = true, length = 100)
    private String memberFieldName;
    @Column(name = "family_field_name", nullable = true, length = 100)
    private String familyFieldName;
    @Enumerated(EnumType.STRING)
    @Column(name = "triger_when")
    private TriggerWhen trigerWhen; // miniute/hourly/daily/monthly/yearly/immidetly
    @Column(name = "day")
    private Short day;
    @Column(name = "hour")
    private Short hour;
    @Column(name = "minute")
    private Short minute;
    @Column(name = "config_id")
    private Integer configId;
    @Column(name = "query_master_id")
    private Integer queryMasterId;
    @Column(name = "query_code")
    private String queryCode;
    @Column(name = "query_master_param_json")
    private String queryMasterParamJson;
    @Column(name = "sms_config_json")
    private String smsConfigJson;
    @Column(name = "push_notification_config_json")
    private String pushNotificationConfigJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public TriggerWhen getTrigerWhen() {
        return trigerWhen;
    }

    public void setTrigerWhen(TriggerWhen trigerWhen) {
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

    public Short getMinute() {
        return minute;
    }

    public void setMinute(Short minute) {
        this.minute = minute;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getUserFieldName() {
        return userFieldName;
    }

    public void setUserFieldName(String userFieldName) {
        this.userFieldName = userFieldName;
    }

    public String getMemberFieldName() {
        return memberFieldName;
    }

    public void setMemberFieldName(String memberFieldName) {
        this.memberFieldName = memberFieldName;
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
    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public String getQueryMasterParamJson() {
        return queryMasterParamJson;
    }

    public void setQueryMasterParamJson(String queryMasterParamJson) {
        this.queryMasterParamJson = queryMasterParamJson;
    }

    public String getSmsConfigJson() {
        return smsConfigJson;
    }

    public void setSmsConfigJson(String smsConfigJson) {
        this.smsConfigJson = smsConfigJson;
    }

    public String getPushNotificationConfigJson() {
        return pushNotificationConfigJson;
    }

    public void setPushNotificationConfigJson(String pushNotificationConfigJson) {
        this.pushNotificationConfigJson = pushNotificationConfigJson;
    }

    public enum Type {

        SMS, EMAIL, MOBILE, QUERY, EXCEPTION , SYSTEM_FUNCTION, PUSH_NOTIFICATION
    }

    public enum TriggerWhen {

        IMMEDIATELY, AFTER
    }
}
