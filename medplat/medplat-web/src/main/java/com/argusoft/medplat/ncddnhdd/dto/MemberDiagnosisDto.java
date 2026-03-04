/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.ncddnhdd.enums.Status;
import com.argusoft.medplat.ncddnhdd.enums.SubType;
import com.argusoft.medplat.ncddnhdd.model.MemberDiseaseDiagnosis.DiseaseCode;

import java.util.Date;
import java.util.List;

/**
 * @author vaishali
 */
public class MemberDiagnosisDto {

    private String diagnosis;

    private String remarks;

    private List<Integer> medicineIds;

    private Integer memberId;

    private DiseaseCode diseaseCode;

    private Integer id;

    private Date diagnosedOn;

    private String readings;

    private Status status;

    private Boolean isCaseCompleted;

    private SubType subType;

    public String getReadings() {
        return readings;
    }

    public void setReadings(String readings) {
        this.readings = readings;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Integer> getMedicineIds() {
        return medicineIds;
    }

    public void setMedicineIds(List<Integer> medicineIds) {
        this.medicineIds = medicineIds;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public DiseaseCode getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(DiseaseCode diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDiagnosedOn() {
        return diagnosedOn;
    }

    public void setDiagnosedOn(Date diagnosedOn) {
        this.diagnosedOn = diagnosedOn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getCaseCompleted() {
        return isCaseCompleted;
    }

    public void setCaseCompleted(Boolean caseCompleted) {
        isCaseCompleted = caseCompleted;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }
}
