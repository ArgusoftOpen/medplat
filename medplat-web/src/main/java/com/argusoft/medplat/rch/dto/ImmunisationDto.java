/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;

/**
 * <p>
 * Used for immunisation.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class ImmunisationDto {

    Integer memberId;
    Integer familyId;
    Integer locationId;
    String memberType;
    String visitType;
    Integer visitId;
    String immunisationGiven;
    Date immunisationDate;
    Integer immunisationGivenBy;

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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public Date getImmunisationDate() {
        return immunisationDate;
    }

    public void setImmunisationDate(Date immunisationDate) {
        this.immunisationDate = immunisationDate;
    }

    public Integer getImmunisationGivenBy() {
        return immunisationGivenBy;
    }

    public void setImmunisationGivenBy(Integer immunisationGivenBy) {
        this.immunisationGivenBy = immunisationGivenBy;
    }
}
