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
 *     Define techo_web_notification_master entity and its fields.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "techo_web_notification_master")
public class TechoWebNotificationMaster extends EntityAuditInfo implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "notification_type_id")
    private Integer notificationTypeId;
    @Column(name = "escalation_level_id")
    private Integer escalationLevelId;
    @Column(name = "location_id")
    private Integer locationId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "family_id")
    private Integer familyId;
    @Column(name = "member_id")
    private Integer memberId;
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
    @Column(name = "action_taken")
    private String actionTaken;
    @Column(name = "ref_code")
    private Integer refCode;
    @Column(name = "notification_type_escalation_id")
    private String notificationTypeEscalationId;
    @JoinColumn(name = "notification_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private NotificationTypeMaster notificationTypeMaster;
    @JoinColumn(name = "escalation_level_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private EscalationLevelMaster escalationLevelMaster;

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

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Integer getRefCode() {
        return refCode;
    }

    public void setRefCode(Integer refCode) {
        this.refCode = refCode;
    }

    public NotificationTypeMaster getNotificationTypeMaster() {
        return notificationTypeMaster;
    }

    public void setNotificationTypeMaster(NotificationTypeMaster notificationTypeMaster) {
        this.notificationTypeMaster = notificationTypeMaster;
    }

    public Integer getEscalationLevelId() {
        return escalationLevelId;
    }

    public void setEscalationLevelId(Integer escalationLevelId) {
        this.escalationLevelId = escalationLevelId;
    }

    public EscalationLevelMaster getEscalationLevelMaster() {
        return escalationLevelMaster;
    }

    public void setEscalationLevelMaster(EscalationLevelMaster escalationLevelMaster) {
        this.escalationLevelMaster = escalationLevelMaster;
    }

    public String getNotificationTypeEscalationId() {
        return notificationTypeEscalationId;
    }

    public void setNotificationTypeEscalationId(String notificationTypeEscalationId) {
        this.notificationTypeEscalationId = notificationTypeEscalationId;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }
    
    public enum State {
        PENDING,
        COMPLETED,
        RESCHEDULE,
        MISSED,
        SKIP
    }
    
}
