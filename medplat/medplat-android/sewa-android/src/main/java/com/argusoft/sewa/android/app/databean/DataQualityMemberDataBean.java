package com.argusoft.sewa.android.app.databean;

public class DataQualityMemberDataBean {

    private String healthId;
    private String name;
    private Long memberId;
    private String serviceType;
    private boolean isDeliveryHappened;
    private boolean deliveryPlaceVerification;
    private boolean noOfChildGenderVerification;
    private boolean childServiceVaccinationStatus;
    private boolean isPregnant;
    private String ttInjectionReceivedStatus;

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public boolean isDeliveryPlaceVerification() {
        return deliveryPlaceVerification;
    }

    public void setDeliveryPlaceVerification(boolean deliveryPlaceVerification) {
        this.deliveryPlaceVerification = deliveryPlaceVerification;
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

    public String getTtInjectionReceivedStatus() {
        return ttInjectionReceivedStatus;
    }

    public void setTtInjectionReceivedStatus(String ttInjectionReceivedStatus) {
        this.ttInjectionReceivedStatus = ttInjectionReceivedStatus;
    }
}
