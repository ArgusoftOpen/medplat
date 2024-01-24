/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import java.util.Date;

/**
 *
 * <p>
 *     Used for member treatment history.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberTreatmentHistoryDto {
    
    private String medicine;
    
    private String diagnosis;
    
    private Date diagnosedOn;
    
    private String diagnosedBy;
    
    private String readings;

    public String getReadings() {
        return readings;
    }

    public void setReadings(String readings) {
        this.readings = readings;
    }
    
    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getDiagnosedOn() {
        return diagnosedOn;
    }

    public void setDiagnosedOn(Date diagnosedOn) {
        this.diagnosedOn = diagnosedOn;
    }

    public String getDiagnosedBy() {
        return diagnosedBy;
    }

    public void setDiagnosedBy(String diagnosedBy) {
        this.diagnosedBy = diagnosedBy;
    }
    
    
}
