/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.exception.ImtechoMobileException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_immunisation_master entity and its fields.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_immunisation_master")
public class ImmunisationMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "member_type")
    private String memberType;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "visit_id")
    private Integer visitId;

    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "immunisation_given")
    private String immunisationGiven;

    @Column(name = "given_on")
    @Temporal(TemporalType.DATE)
    private Date givenOn;

    @Column(name = "given_by")
    private Integer givenBy;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "pregnancy_reg_det_id")
    private Integer pregnancyRegDetId;

    @Column(name = "vitamin_a_no")
    private Integer vitaminANo;

    public ImmunisationMaster(Integer familyId, Integer memberId, String memberType, String visitType, Integer visitId, Integer notificationId, String immunisationGiven, Date givenOn, Integer givenBy, Integer locationId, Integer pregnancyRegDetId) {
        this.familyId = familyId;
        this.memberId = memberId;
        this.memberType = memberType;
        this.visitType = visitType;
        this.visitId = visitId;
        this.notificationId = notificationId;
        this.immunisationGiven = immunisationGiven;
        this.givenOn = givenOn;
        this.givenBy = givenBy;
        this.locationId = locationId;
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public ImmunisationMaster() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public Date getGivenOn() {
        return givenOn;
    }

    public void setGivenOn(Date givenOn) {
        if (givenOn != null && givenOn.after(new Date())) {
            throw new ImtechoMobileException("Vaccination given date cannot be future", 100);
        }
        this.givenOn = givenOn;
    }

    public Integer getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(Integer givenBy) {
        this.givenBy = givenBy;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPregnancyRegDetId() {
        return pregnancyRegDetId;
    }

    public void setPregnancyRegDetId(Integer pregnancyRegDetId) {
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public Integer getVitaminANo() {
        return vitaminANo;
    }

    public void setVitaminANo(Integer vitaminANo) {
        this.vitaminANo = vitaminANo;
    }

    /**
     * Define fields name for rch_immunisation_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String MEMBER_ID = "memberId";
        public static final String FAMILY_ID = "familyId";
        public static final String VISIT_ID = "visitId";
        public static final String IMMUNISATION_GIVEN = "immunisationGiven";
        public static final String GIVEN_ON = "givenOn";
        public static final String LOCATION_ID = "locationId";
    }

    @Override
    public String toString() {
        return "ImmunisationMaster{" + "id=" + id + ", familyId=" + familyId + ", memberId=" + memberId + ", memberType=" + memberType + ", visitType=" + visitType + ", visitId=" + visitId + ", notificationId=" + notificationId + ", immunisationGiven=" + immunisationGiven + ", givenOn=" + givenOn + ", givenBy=" + givenBy + ", locationId=" + locationId + ", pregnancyRegDetId=" + pregnancyRegDetId + '}';
    }
}
