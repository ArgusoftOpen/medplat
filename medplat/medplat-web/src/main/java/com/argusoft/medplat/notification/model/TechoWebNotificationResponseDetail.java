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
 *     Define techo_web_notification_response_det entity and its fields.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "techo_web_notification_response_det")
public class TechoWebNotificationResponseDetail extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "notification_id")
    private Integer notificationId;
    @Column(name = "notification_type_id")
    private Integer notificationTypeId;
    @Column(name = "location_id")
    private Integer locationId;
    @Column(name = "schedule_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date scheduleDate;
    @Column(name = "due_on")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dueOn;
    @Column(name = "expiry_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date expiryDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "from_state")
    private TechoWebNotificationMaster.State fromState;
    @Enumerated(EnumType.STRING)
    @Column(name = "to_state")
    private TechoWebNotificationMaster.State toState;
    @Column(name = "other_details")
    private String otherDetails;
    @Column(name = "action_taken")
    private String actionTaken;
    @Column(name = "ref_code")
    private Integer refCode;
    @Column(name = "notification_type_escalation_id")
    private String notificationTypeEscalationId;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public TechoWebNotificationMaster.State getFromState() {
        return fromState;
    }

    public void setFromState(TechoWebNotificationMaster.State fromState) {
        this.fromState = fromState;
    }

    public TechoWebNotificationMaster.State getToState() {
        return toState;
    }

    public void setToState(TechoWebNotificationMaster.State toState) {
        this.toState = toState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public Integer getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(Integer notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
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
