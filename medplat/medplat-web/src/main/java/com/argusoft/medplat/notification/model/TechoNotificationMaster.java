/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define techo_notification_master entity and its fields.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "techo_notification_master")
public class TechoNotificationMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "notification_type_id")
    private Integer notificationTypeId;
    @Column(name = "notification_code")
    private String notificationCode;
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "family_id")
    private Integer familyId;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "migration_id")
    private Integer migrationId;
    @Column(name = "schedule_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date scheduleDate;
    @Column(name = "due_on")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dueOn;
    @Column(name = "expiry_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date expiryDate;
    @Column(name = "action_by")
    private Integer actionBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;
    @Column(name = "other_details")
    private String otherDetails;
    @Column(name = "ref_code")
    private Integer refCode;
    @JoinColumn(name = "notification_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private NotificationTypeMaster notificationTypeMaster;
    @Column(name = "related_id")
    private Integer relatedId;
    @Column(name = "header")
    private String header;

    public enum State {
        PENDING,
        COMPLETED,
        DEATH_VERIFICATION_PENDING,
        MEMBER_DEATH,
        RESCHEDULE,
        RESCHEDULE_DEATH_VERIFICATION_PENDING,
        MISSED,
        SKIP,
        DONE_ON_EMAMTA,
        MARK_AS_MIGRATED,
        MARK_AS_MIGRATED_PENDING,
        MARK_AS_MIGRATED_RESCHEDULE,
        MARK_AS_MENOPAUSE_ARRIVED,
        MARK_AS_DEATH,
        MARK_AS_PREGNANT,
        MARK_AS_DELIVERY_HAPPENED,
        MARK_AS_HYSTERECTOMY_DONE,
        MARK_AS_READ,
        MARK_AS_MIGRATION_REVERT,
        MARK_AS_OUT_OF_STATE,
        MARK_AS_ARCHIVED,
        MARK_AS_NO_RESPONSE,
        MARK_AS_WRONGLY_PREGNANT,
        DELIVERY_DATA_NOT_FOUND_FROM_EMAMTA,
        MARK_AS_DUPLICATE,
        DONE_ON_CS,
        DONE_BY_ASHA
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(Integer notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getNotificationCode() {
        return notificationCode;
    }

    public void setNotificationCode(String notificationCode) {
        this.notificationCode = notificationCode;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Date getDueOn() {
        return dueOn;
    }

    public void setDueOn(Date dueOn) {
        this.dueOn = dueOn;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getActionBy() {
        return actionBy;
    }

    public void setActionBy(Integer actionBy) {
        this.actionBy = actionBy;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public NotificationTypeMaster getNotificationTypeMaster() {
        return notificationTypeMaster;
    }

    public void setNotificationTypeMaster(NotificationTypeMaster notificationTypeMaster) {
        this.notificationTypeMaster = notificationTypeMaster;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Integer getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Integer migrationId) {
        this.migrationId = migrationId;
    }

    public Integer getRefCode() {
        return refCode;
    }

    public void setRefCode(Integer refCode) {
        this.refCode = refCode;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Define fields name for techo_notification_master entity.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }

        public static final String ID = "id";
        public static final String NOTIFICATION_TYPE_ID = "notificationTypeId";
        public static final String LOCATION_ID = "locationId";
        public static final String USER_ID = "userId";
        public static final String FAMILY_ID = "familyId";
        public static final String MEMBER_ID = "memberId";
        public static final String STATE = "state";
        public static final String MIGRATION_ID = "migrationId";
    }

    @Override
    public String toString() {
        return "TechoNotificationMaster{" + "id=" + id + ", notificationTypeId=" + notificationTypeId + ", notificationCode=" + notificationCode + ", locationId=" + locationId + ", userId=" + userId + ", familyId=" + familyId + ", memberId=" + memberId + ", migrationId=" + migrationId + ", scheduleDate=" + scheduleDate + ", dueOn=" + dueOn + ", expiryDate=" + expiryDate + ", actionBy=" + actionBy + ", state=" + state + ", otherDetails=" + otherDetails + ", notificationTypeMaster=" + notificationTypeMaster + '}';
    }
}
