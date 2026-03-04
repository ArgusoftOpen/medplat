/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 *
 * <p>
 *     Define notification_type_master entity and its fields.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "notification_type_master")
public class NotificationTypeMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "name", length = 300)
    private String notificationName;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "type", length = 50)
    private String notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 255)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_for", length = 255)
    private NotificationFor notificationFor;

    @Column(name = "data_query")
    private String dataQuery;

    @Column(name = "action_query")
    private String actionQuery;

    @Column(name = "action_on_role_id")
    private Integer actionOnRoleId;

    @Column(name = "order_no")
    private Integer orderNo;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "data_for")
    private String dataFor;

    @Column(name = "url_based_action")
    private Boolean urlBasedAction;

    @Column(name = "url")
    private String url;

    @Column(name = "modal_based_action")
    private Boolean modalBasedAction;

    @Column(name = "modal_name")
    private String modalName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notification_type_role_rel", joinColumns = @JoinColumn(name = "notification_type_id"))
    @Column(name = "role_id")
    private Set<Integer> roles;

    @Transient
    private String notificationForString;

    @Column(name = "is_location_filter_required")
    private Boolean isLocationFilterRequired;

    @Column(name = "fetch_up_to_level")
    private Integer fetchUptoLevel;

    @Column(name = "required_up_to_level")
    private Integer requiredUptoLevel;

    @Column(name = "is_fetch_according_aoi")
    private Boolean isFetchAccordingAOI;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public NotificationFor getNotificationFor() {
        return notificationFor;
    }

    public void setNotificationFor(NotificationFor notificationFor) {
        this.notificationFor = notificationFor;
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

    public Integer getActionOnRoleId() {
        return actionOnRoleId;
    }

    public void setActionOnRoleId(Integer actionOnRoleId) {
        this.actionOnRoleId = actionOnRoleId;
    }

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }

    public String getNotificationForString() {
        return notificationForString;
    }

    public void setNotificationForString(String notificationForString) {
        this.notificationForString = notificationForString;
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
        return urlBasedAction == null ? null : urlBasedAction;
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
        return modalBasedAction == null ? null : modalBasedAction;
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

    /**
     * Define fields name for notification_type_master entity.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }

        public static final String ID = "id";
        public static final String CODE = "code";
        public static final String ROLE_ID = "roleId";
        public static final String STATE = "state";
        public static final String UUID = "uuid";

    }

    public enum State {

        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    public enum NotificationFor {

        USER,
        MEMBER,
        FAMILY
    }
}
