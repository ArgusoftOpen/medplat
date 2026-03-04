/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author alpeshkyada
 */
public class BeneficiaryMorbidityDetails {

    private String beneficiaryId;
    private String beneficiaryName;
    private String beneficiaryType;
    private List<IdentifiedMorbidityDetails> identifiedMorbidities;

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public List<IdentifiedMorbidityDetails> getIdentifiedMorbidities() {
        return identifiedMorbidities;
    }

    public void setIdentifiedMorbidities(List<IdentifiedMorbidityDetails> identifiedMorbidities) {
        this.identifiedMorbidities = identifiedMorbidities;
    }

    @NonNull
    @Override
    public String toString() {
        return "BeneficiaryMorbidityDetails{" + "beneficiaryName=" + beneficiaryName + ", beneficiaryType=" + beneficiaryType + ", identifiedMorbidities=" + identifiedMorbidities + '}';
    }
}
