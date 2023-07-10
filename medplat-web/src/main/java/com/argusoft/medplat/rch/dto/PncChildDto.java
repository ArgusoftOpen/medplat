/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Used for pnc child.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class PncChildDto {

    private Integer childId;
    private Boolean isAlive;
    private String otherDangerSign;
    private Float childWeight;
    private String memberStatus;
    private Date deathDate;
    private String deathReason;
    private String placeOfDeath;
    private Integer referralPlace;
    private String otherDeathReason;
    private Boolean isHighRiskCase;
    private String immunisationGiven;
    private String childReferralDone;
    private Set<Integer> childDangerSigns;
    private List<ImmunisationDto> immunisationDtos;
    private Integer referralInfraId;

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public String getOtherDangerSign() {
        return otherDangerSign;
    }

    public void setOtherDangerSign(String otherDangerSign) {
        this.otherDangerSign = otherDangerSign;
    }

    public Float getChildWeight() {
        return childWeight;
    }

    public void setChildWeight(Float childWeight) {
        this.childWeight = childWeight;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public String getChildReferralDone() {
        return childReferralDone;
    }

    public void setChildReferralDone(String childReferralDone) {
        this.childReferralDone = childReferralDone;
    }

    public Set<Integer> getChildDangerSigns() {
        return childDangerSigns;
    }

    public void setChildDangerSigns(Set<Integer> childDangerSigns) {
        this.childDangerSigns = childDangerSigns;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public List<ImmunisationDto> getImmunisationDtos() {
        return immunisationDtos;
    }

    public void setImmunisationDtos(List<ImmunisationDto> immunisationDtos) {
        this.immunisationDtos = immunisationDtos;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    @Override
    public String toString() {
        return "PncChildDto{" +
                "childId=" + childId +
                ", isAlive=" + isAlive +
                ", otherDangerSign='" + otherDangerSign + '\'' +
                ", childWeight=" + childWeight +
                ", memberStatus='" + memberStatus + '\'' +
                ", deathDate=" + deathDate +
                ", deathReason='" + deathReason + '\'' +
                ", placeOfDeath='" + placeOfDeath + '\'' +
                ", referralPlace=" + referralPlace +
                ", otherDeathReason='" + otherDeathReason + '\'' +
                ", isHighRiskCase=" + isHighRiskCase +
                ", immunisationGiven='" + immunisationGiven + '\'' +
                ", childReferralDone='" + childReferralDone + '\'' +
                ", childDangerSigns=" + childDangerSigns +
                ", immunisationDtos=" + immunisationDtos +
                ", referralInfraId=" + referralInfraId +
                '}';
    }
}
