package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_reported_event_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_reported_event_master")
public class AshaReportedEventMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "event_type")
    private String eventType;
    @Column(name = "family_id")
    private Integer familyId;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "location_id")
    private Integer locationId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "reported_on")
    private Date reportedOn;
    @Column(name = "action")
    private String action;
    @Column(name = "action_by")
    private Integer actionBy;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "action_on")
    private Date actionOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Date getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Date reportedOn) {
        this.reportedOn = reportedOn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getActionBy() {
        return actionBy;
    }

    public void setActionBy(Integer actionBy) {
        this.actionBy = actionBy;
    }

    public Date getActionOn() {
        return actionOn;
    }

    public void setActionOn(Date actionOn) {
        this.actionOn = actionOn;
    }

}
