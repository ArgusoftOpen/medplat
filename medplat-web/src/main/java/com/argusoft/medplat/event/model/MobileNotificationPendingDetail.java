/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define event_mobile_notification_pending entity and its fields.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "event_mobile_notification_pending")
public class MobileNotificationPendingDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "notification_configuration_type_id")
    private String notificationConfigTypeId;
    @Column(name = "base_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date baseDate;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "family_id")
    private Integer familyId;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "state")
    private String state;
    @Column(name = "ref_code")
    private Integer refCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotificationConfigTypeId() {
        return notificationConfigTypeId;
    }

    public void setNotificationConfigTypeId(String notificationConfigTypeId) {
        this.notificationConfigTypeId = notificationConfigTypeId;
    }

    public Date getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getRefCode() {
        return refCode;
    }

    public void setRefCode(Integer refCode) {
        this.refCode = refCode;
    }

}
