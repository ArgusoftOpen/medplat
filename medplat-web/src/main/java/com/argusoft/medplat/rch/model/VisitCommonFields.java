/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * <p>
 * Define VisitCommonFields entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@MappedSuperclass
public class VisitCommonFields extends EntityAuditInfo {

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "family_id", nullable = false)
    private Integer familyId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "notification_id")
    private Integer notificationId;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getMobileStartDate() {
        return mobileStartDate;
    }

    public void setMobileStartDate(Date mobileStartDate) {
        this.mobileStartDate = mobileStartDate;
    }

    public Date getMobileEndDate() {
        return mobileEndDate;
    }

    public void setMobileEndDate(Date mobileEndDate) {
        this.mobileEndDate = mobileEndDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Define fields name for VisitCommonFields entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String MEMBER_ID = "memberId";
        public static final String FAMILY_ID = "familyId";
        public static final String MOBILE_START_DATE = "mobileStartDate";
        public static final String MOBILE_END_DATE = "mobileEndDate";
        public static final String LOCATION_ID = "locationId";
    }
}
