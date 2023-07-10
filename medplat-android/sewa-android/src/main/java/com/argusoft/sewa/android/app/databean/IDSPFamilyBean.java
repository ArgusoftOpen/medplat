package com.argusoft.sewa.android.app.databean;

import java.util.Date;

public class IDSPFamilyBean {
    private String familyId;
    private boolean anyIllness;
    private boolean anyOneTravelFromFamily;
    private boolean anyOneCovidContact;
    private Date serviceDate;

    public IDSPFamilyBean() {
    }

    public IDSPFamilyBean(boolean anyIllness, boolean anyOneTravelFromFamily) {
        this.anyIllness = anyIllness;
        this.anyOneTravelFromFamily = anyOneTravelFromFamily;
    }

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

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }
}
