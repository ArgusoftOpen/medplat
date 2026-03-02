package com.argusoft.sewa.android.app.databean;

public class AbhaConsentCheckboxModel {
    private Boolean isAadhaarSharingConsentGiven;
    private Boolean isDocOtherThanAadhaarConsentGiven;
    private Boolean isAbhaUsageConsentGiven;
    private Boolean isSharingHealthRecordsConsentGiven;
    private Boolean isAnonymizationConsentGiven;
    private Boolean isHealthWorkerConsentGiven;
    private Boolean isBeneficiaryConsentGiven;

    public Boolean getAadhaarSharingConsentGiven() {
        return isAadhaarSharingConsentGiven;
    }

    public void setAadhaarSharingConsentGiven(Boolean aadhaarSharingConsentGiven) {
        isAadhaarSharingConsentGiven = aadhaarSharingConsentGiven;
    }

    public Boolean getDocOtherThanAadhaarConsentGiven() {
        return isDocOtherThanAadhaarConsentGiven;
    }

    public void setDocOtherThanAadhaarConsentGiven(Boolean docOtherThanAadhaarConsentGiven) {
        isDocOtherThanAadhaarConsentGiven = docOtherThanAadhaarConsentGiven;
    }

    public Boolean getAbhaUsageConsentGiven() {
        return isAbhaUsageConsentGiven;
    }

    public void setAbhaUsageConsentGiven(Boolean abhaUsageConsentGiven) {
        isAbhaUsageConsentGiven = abhaUsageConsentGiven;
    }

    public Boolean getSharingHealthRecordsConsentGiven() {
        return isSharingHealthRecordsConsentGiven;
    }

    public void setSharingHealthRecordsConsentGiven(Boolean sharingHealthRecordsConsentGiven) {
        isSharingHealthRecordsConsentGiven = sharingHealthRecordsConsentGiven;
    }

    public Boolean getAnonymizationConsentGiven() {
        return isAnonymizationConsentGiven;
    }

    public void setAnonymizationConsentGiven(Boolean anonymizationConsentGiven) {
        isAnonymizationConsentGiven = anonymizationConsentGiven;
    }

    public Boolean getHealthWorkerConsentGiven() {
        return isHealthWorkerConsentGiven;
    }

    public void setHealthWorkerConsentGiven(Boolean healthWorkerConsentGiven) {
        isHealthWorkerConsentGiven = healthWorkerConsentGiven;
    }

    public Boolean getBeneficiaryConsentGiven() {
        return isBeneficiaryConsentGiven;
    }

    public void setBeneficiaryConsentGiven(Boolean beneficiaryConsentGiven) {
        isBeneficiaryConsentGiven = beneficiaryConsentGiven;
    }

    @Override
    public String toString() {
        return "AbhaConsentCheckboxModel{" +
                "isAadhaarSharingConsentGiven=" + isAadhaarSharingConsentGiven +
                ", isDocOtherThanAadhaarConsentGiven=" + isDocOtherThanAadhaarConsentGiven +
                ", isAbhaUsageConsentGiven=" + isAbhaUsageConsentGiven +
                ", isSharingHealthRecordsConsentGiven=" + isSharingHealthRecordsConsentGiven +
                ", isAnonymizationConsentGiven=" + isAnonymizationConsentGiven +
                ", isHealthWorkerConsentGiven=" + isHealthWorkerConsentGiven +
                ", isBeneficiaryConsentGiven=" + isBeneficiaryConsentGiven +
                '}';
    }
}
