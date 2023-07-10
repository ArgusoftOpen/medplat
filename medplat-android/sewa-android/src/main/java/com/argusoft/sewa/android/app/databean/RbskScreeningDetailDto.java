package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class RbskScreeningDetailDto {

    private String token;
    private Integer memberId;
    private Integer familyId;
    private Integer locationId;
    private Date screeningDate;
    private String doneFrom;
    private List<RbskDefectDetailDto> rbskScreeningDefectDto;
    private String apgar1;
    private String apgar5;
    private Integer length;
    private Integer headCircumference;
    private String babyTone;
    private String moroReflex;
    private String stepReflex;
    private String plantarReflex;
    private String rootReflex;
    private String assymetricalTonic;
    private String suckingReflex;
    private String graspReflex;
    private Boolean visibleDefects;
    private Boolean referralDone;
    private Integer referralPlace;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public String getDoneFrom() {
        return doneFrom;
    }

    public void setDoneFrom(String doneFrom) {
        this.doneFrom = doneFrom;
    }

    public List<RbskDefectDetailDto> getRbskScreeningDefectDto() {
        return rbskScreeningDefectDto;
    }

    public void setRbskScreeningDefectDto(List<RbskDefectDetailDto> rbskScreeningDefectDto) {
        this.rbskScreeningDefectDto = rbskScreeningDefectDto;
    }

    public String getApgar1() {
        return apgar1;
    }

    public void setApgar1(String apgar1) {
        this.apgar1 = apgar1;
    }

    public String getApgar5() {
        return apgar5;
    }

    public void setApgar5(String apgar5) {
        this.apgar5 = apgar5;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getHeadCircumference() {
        return headCircumference;
    }

    public void setHeadCircumference(Integer headCircumference) {
        this.headCircumference = headCircumference;
    }

    public String getBabyTone() {
        return babyTone;
    }

    public void setBabyTone(String babyTone) {
        this.babyTone = babyTone;
    }

    public String getMoroReflex() {
        return moroReflex;
    }

    public void setMoroReflex(String moroReflex) {
        this.moroReflex = moroReflex;
    }

    public String getStepReflex() {
        return stepReflex;
    }

    public void setStepReflex(String stepReflex) {
        this.stepReflex = stepReflex;
    }

    public String getPlantarReflex() {
        return plantarReflex;
    }

    public void setPlantarReflex(String plantarReflex) {
        this.plantarReflex = plantarReflex;
    }

    public String getRootReflex() {
        return rootReflex;
    }

    public void setRootReflex(String rootReflex) {
        this.rootReflex = rootReflex;
    }

    public String getAssymetricalTonic() {
        return assymetricalTonic;
    }

    public void setAssymetricalTonic(String assymetricalTonic) {
        this.assymetricalTonic = assymetricalTonic;
    }

    public String getSuckingReflex() {
        return suckingReflex;
    }

    public void setSuckingReflex(String suckingReflex) {
        this.suckingReflex = suckingReflex;
    }

    public String getGraspReflex() {
        return graspReflex;
    }

    public void setGraspReflex(String graspReflex) {
        this.graspReflex = graspReflex;
    }

    public Boolean getVisibleDefects() {
        return visibleDefects;
    }

    public void setVisibleDefects(Boolean visibleDefects) {
        this.visibleDefects = visibleDefects;
    }

    public Boolean getReferralDone() {
        return referralDone;
    }

    public void setReferralDone(Boolean referralDone) {
        this.referralDone = referralDone;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    @NonNull
    @Override
    public String toString() {
        return "RbskScreeningDetailDto{" +
                "token='" + token + '\'' +
                ", memberId=" + memberId +
                ", familyId=" + familyId +
                ", locationId=" + locationId +
                ", screeningDate=" + screeningDate +
                ", doneFrom='" + doneFrom + '\'' +
                ", rbskScreeningDefectDto=" + rbskScreeningDefectDto +
                ", apgar1='" + apgar1 + '\'' +
                ", apgar5='" + apgar5 + '\'' +
                ", length=" + length +
                ", headCircumference=" + headCircumference +
                ", babyTone='" + babyTone + '\'' +
                ", moroReflex='" + moroReflex + '\'' +
                ", stepReflex='" + stepReflex + '\'' +
                ", plantarReflex='" + plantarReflex + '\'' +
                ", rootReflex='" + rootReflex + '\'' +
                ", assymetricalTonic='" + assymetricalTonic + '\'' +
                ", suckingReflex='" + suckingReflex + '\'' +
                ", graspReflex='" + graspReflex + '\'' +
                ", visibleDefects=" + visibleDefects +
                ", referralDone=" + referralDone +
                ", referralPlace=" + referralPlace +
                '}';
    }
}
