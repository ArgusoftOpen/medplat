package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class HealthInfrastructureBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long actualId;
    @DatabaseField
    private String name;
    @DatabaseField
    private String englishName;
    @DatabaseField
    private Long locationId;
    @DatabaseField
    private Long typeId;
    @DatabaseField
    private String typeName;
    @DatabaseField
    private Boolean isNrc;
    @DatabaseField
    private Boolean isCmtc;
    @DatabaseField
    private Boolean isFru;
    @DatabaseField
    private Boolean isSncu;
    @DatabaseField
    private Boolean isChiranjeevi;
    @DatabaseField
    private Boolean isBalsaka;
    @DatabaseField
    private Boolean isPmjy;
    @DatabaseField
    private Boolean isBloodBank;
    @DatabaseField
    private Boolean isGynaec;
    @DatabaseField
    private Boolean isPediatrician;
    @DatabaseField
    private Boolean isCpConfirmationCenter;
    @DatabaseField
    private Boolean forNcd;
    @DatabaseField
    private Boolean isBalsakha1;
    @DatabaseField
    private Boolean isBalsakha3;
    @DatabaseField
    private Boolean isUsgFacility;
    @DatabaseField
    private Boolean isReferralFacility;
    @DatabaseField
    private Boolean isMaYojna;
    @DatabaseField
    private Boolean isNpcb;
    @DatabaseField
    private String state;
    @DatabaseField
    private Long modifiedOn;
    @DatabaseField
    private String latitude;
    @DatabaseField
    private String longitude;
    @DatabaseField
    private String abhayParimitiId;


    public Long getActualId() {
        return actualId;
    }

    public void setActualId(Long actualId) {
        this.actualId = actualId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Boolean getNrc() {
        return isNrc;
    }

    public void setNrc(Boolean nrc) {
        isNrc = nrc;
    }

    public Boolean getCmtc() {
        return isCmtc;
    }

    public void setCmtc(Boolean cmtc) {
        isCmtc = cmtc;
    }

    public Boolean getFru() {
        return isFru;
    }

    public void setFru(Boolean fru) {
        isFru = fru;
    }

    public Boolean getSncu() {
        return isSncu;
    }

    public void setSncu(Boolean sncu) {
        isSncu = sncu;
    }

    public Boolean getChiranjeevi() {
        return isChiranjeevi;
    }

    public void setChiranjeevi(Boolean chiranjeevi) {
        isChiranjeevi = chiranjeevi;
    }

    public Boolean getBalsaka() {
        return isBalsaka;
    }

    public void setBalsaka(Boolean balsaka) {
        isBalsaka = balsaka;
    }

    public Boolean getPmjy() {
        return isPmjy;
    }

    public void setPmjy(Boolean pmjy) {
        isPmjy = pmjy;
    }

    public Boolean getBloodBank() {
        return isBloodBank;
    }

    public void setBloodBank(Boolean bloodBank) {
        isBloodBank = bloodBank;
    }

    public Boolean getGynaec() {
        return isGynaec;
    }

    public void setGynaec(Boolean gynaec) {
        isGynaec = gynaec;
    }

    public Boolean getPediatrician() {
        return isPediatrician;
    }

    public void setPediatrician(Boolean pediatrician) {
        isPediatrician = pediatrician;
    }

    public Boolean getCpConfirmationCenter() {
        return isCpConfirmationCenter;
    }

    public void setCpConfirmationCenter(Boolean cpConfirmationCenter) {
        isCpConfirmationCenter = cpConfirmationCenter;
    }

    public Boolean getForNcd() {
        return forNcd;
    }

    public void setForNcd(Boolean forNcd) {
        this.forNcd = forNcd;
    }

    public Boolean getBalsakha1() {
        return isBalsakha1;
    }

    public void setBalsakha1(Boolean balsakha1) {
        isBalsakha1 = balsakha1;
    }

    public Boolean getBalsakha3() {
        return isBalsakha3;
    }

    public void setBalsakha3(Boolean balsakha3) {
        isBalsakha3 = balsakha3;
    }

    public Boolean getUsgFacility() {
        return isUsgFacility;
    }

    public void setUsgFacility(Boolean usgFacility) {
        isUsgFacility = usgFacility;
    }

    public Boolean getReferralFacility() {
        return isReferralFacility;
    }

    public void setReferralFacility(Boolean referralFacility) {
        isReferralFacility = referralFacility;
    }

    public Boolean getMaYojna() {
        return isMaYojna;
    }

    public void setMaYojna(Boolean maYojna) {
        isMaYojna = maYojna;
    }

    public Boolean getNpcb() {
        return isNpcb;
    }

    public void setNpcb(Boolean npcb) {
        isNpcb = npcb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
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

    public String getAbhayParimitiId() {
        return abhayParimitiId;
    }

    public void setAbhayParimitiId(String abhayParimitiId) {
        this.abhayParimitiId = abhayParimitiId;
    }

    @NonNull
    @Override
    public String toString() {
        return "HealthInfrastructureBean{" +
                "actualId=" + actualId +
                ", name='" + name + '\'' +
                ", englishName='" + englishName + '\'' +
                ", locationId=" + locationId +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", isNrc=" + isNrc +
                ", isCmtc=" + isCmtc +
                ", isFru=" + isFru +
                ", isSncu=" + isSncu +
                ", isChiranjeevi=" + isChiranjeevi +
                ", isBalsaka=" + isBalsaka +
                ", isPmjy=" + isPmjy +
                ", isBloodBank=" + isBloodBank +
                ", isGynaec=" + isGynaec +
                ", isPediatrician=" + isPediatrician +
                ", isCpConfirmationCenter=" + isCpConfirmationCenter +
                ", forNcd=" + forNcd +
                ", state='" + state + '\'' +
                ", modifiedOn=" + modifiedOn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
