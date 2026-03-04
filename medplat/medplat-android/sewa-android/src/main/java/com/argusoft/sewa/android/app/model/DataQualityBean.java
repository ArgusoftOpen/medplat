package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class DataQualityBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long memberId;

    @DatabaseField
    private String serviceType;

    @DatabaseField
    private boolean isDeliveryHappened;

    @DatabaseField
    private boolean isDeliveryPlaceVerificationDone;

    @DatabaseField
    private boolean noOfChildGenderVerification;

    @DatabaseField
    private boolean childServiceVaccinationStatus;

    @DatabaseField
    private boolean isPregnant;

    @DatabaseField
    private String ttInjectionReceivedStatus;

    @DatabaseField
    private Date modifiedOn;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isDeliveryHappened() {
        return isDeliveryHappened;
    }

    public void setDeliveryHappened(boolean deliveryHappened) {
        isDeliveryHappened = deliveryHappened;
    }

    public boolean isDeliveryPlaceVerificationDone() {
        return isDeliveryPlaceVerificationDone;
    }

    public void setDeliveryPlaceVerificationDone(boolean deliveryPlaceVerificationDone) {
        isDeliveryPlaceVerificationDone = deliveryPlaceVerificationDone;
    }

    public boolean isNoOfChildGenderVerification() {
        return noOfChildGenderVerification;
    }

    public void setNoOfChildGenderVerification(boolean noOfChildGenderVerification) {
        this.noOfChildGenderVerification = noOfChildGenderVerification;
    }

    public boolean isChildServiceVaccinationStatus() {
        return childServiceVaccinationStatus;
    }

    public void setChildServiceVaccinationStatus(boolean childServiceVaccinationStatus) {
        this.childServiceVaccinationStatus = childServiceVaccinationStatus;
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public void setPregnant(boolean pregnant) {
        isPregnant = pregnant;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getTtInjectionReceivedStatus() {
        return ttInjectionReceivedStatus;
    }

    public void setTtInjectionReceivedStatus(String ttInjectionReceivedStatus) {
        this.ttInjectionReceivedStatus = ttInjectionReceivedStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "DataQualityBean{" +
                "memberId=" + memberId +
                ", serviceType='" + serviceType + '\'' +
                ", isDeliveryHappened=" + isDeliveryHappened +
                ", isDeliveryPlaceVerificationDone=" + isDeliveryPlaceVerificationDone +
                ", noOfChildGenderVerification=" + noOfChildGenderVerification +
                ", childServiceVaccinationStatus=" + childServiceVaccinationStatus +
                ", isPregnant=" + isPregnant +
                ", ttInjectionReceivedStatus=" + ttInjectionReceivedStatus +
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
