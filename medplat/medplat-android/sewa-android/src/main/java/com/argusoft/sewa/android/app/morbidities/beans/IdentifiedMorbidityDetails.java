/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class IdentifiedMorbidityDetails {

    private String morbidityCode;
    private String riskFactorOfIdentifiedMorbidities;
    private List<String> identifiedSymptoms = new ArrayList<>();

    public List<String> getIdentifiedSymptoms() {
        return identifiedSymptoms;
    }

    public void setIdentifiedSymptoms(List<String> identifiedSymptoms) {
        this.identifiedSymptoms = identifiedSymptoms;
    }

    public String getMorbidityCode() {
        return morbidityCode;
    }

    public void setMorbidityCode(String morbidityCode) {
        this.morbidityCode = morbidityCode;
    }

    public String getRiskFactorOfIdentifiedMorbidities() {
        return riskFactorOfIdentifiedMorbidities;
    }

    public void setRiskFactorOfIdentifiedMorbidities(String riskFactorOfIdentifiedMorbidities) {
        this.riskFactorOfIdentifiedMorbidities = riskFactorOfIdentifiedMorbidities;
    }

    @NonNull
    @Override
    public String toString() {
        return "IdentifiedMorbidityDetails{" + "MorbidityCode=" + MorbiditiesConstant.getMorbidityCodeAsKEYandMorbidityNameAsVALUE(morbidityCode) + ", riskFactorOfIdentifiedMorbidities=" + riskFactorOfIdentifiedMorbidities + ", identifiedSymptoms=" + identifiedSymptoms + '}';
    }
}
