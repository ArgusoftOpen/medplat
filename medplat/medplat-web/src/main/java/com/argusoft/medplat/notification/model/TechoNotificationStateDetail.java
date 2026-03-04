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
 *     Define techo_notification_state_detail entity and its fields.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "techo_notification_state_detail")
public class TechoNotificationStateDetail extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "notification_id")
    private Integer notificationId;
    @Enumerated(EnumType.STRING)
    @Column(name = "from_state")
    private TechoNotificationMaster.State fromState;
    @Enumerated(EnumType.STRING)
    @Column(name = "to_state")
    private TechoNotificationMaster.State toState;
    @Enumerated(EnumType.STRING)
    @Column(name = "from_schedule_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fromScheduleDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "to_schedule_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date toScheduleDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public TechoNotificationMaster.State getFromState() {
        return fromState;
    }

    public void setFromState(TechoNotificationMaster.State fromState) {
        this.fromState = fromState;
    }

    public TechoNotificationMaster.State getToState() {
        return toState;
    }

    public void setToState(TechoNotificationMaster.State toState) {
        this.toState = toState;
    }

    public Date getFromScheduleDate() {
        return fromScheduleDate;
    }

    public void setFromScheduleDate(Date fromScheduleDate) {
        this.fromScheduleDate = fromScheduleDate;
    }

    public Date getToScheduleDate() {
        return toScheduleDate;
    }

    public void setToScheduleDate(Date toScheduleDate) {
        this.toScheduleDate = toScheduleDate;
    }



}
