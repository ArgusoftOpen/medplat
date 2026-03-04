package com.argusoft.sewa.android.app.databean;

public class IDSP2FamilyDataBean {
    private String familyId;
    private boolean anyIllness;
    private boolean anyOneTravelFromFamily;
    private boolean anyOneCovidContact;
    private Long serviceDate;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public boolean isAnyIllness() {
        return anyIllness;
    }

    public void setAnyIllness(boolean anyIllness) {
        this.anyIllness = anyIllness;
    }

    public boolean isAnyOneTravelFromFamily() {
        return anyOneTravelFromFamily;
    }

    public void setAnyOneTravelFromFamily(boolean anyOneTravelFromFamily) {
        this.anyOneTravelFromFamily = anyOneTravelFromFamily;
    }

    public boolean isAnyOneCovidContact() {
        return anyOneCovidContact;
    }

    public void setAnyOneCovidContact(boolean anyOneCovidContact) {
        this.anyOneCovidContact = anyOneCovidContact;
    }

    public Long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Long serviceDate) {
        this.serviceDate = serviceDate;
    }
}
