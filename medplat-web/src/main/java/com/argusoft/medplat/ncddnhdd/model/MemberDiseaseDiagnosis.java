/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncddnhdd.enums.Status;
import com.argusoft.medplat.ncddnhdd.enums.SubType;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * <p>
 *     Define ncd_member_diseases_diagnosis entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_diseases_diagnosis")
public class MemberDiseaseDiagnosis extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "disease_code")
    @Enumerated(EnumType.STRING)
    private DiseaseCode diseaseCode;

    @Column(name = "diagnosed_on")
    private Date diagnosedOn;

    @Column(name = "readings")
    private String readings;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "sub_type")
    @Enumerated(EnumType.STRING)
    private SubType subType;

    @Column(name = "is_case_completed")
    private Boolean isCaseCompleted;

    public String getReadings() {
        return readings;
    }

    public void setReadings(String readings) {
        this.readings = readings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
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

    public DiseaseCode getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(DiseaseCode diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public Date getDiagnosedOn() {
        return diagnosedOn;
    }

    public void setDiagnosedOn(Date diagnosedOn) {
        this.diagnosedOn = diagnosedOn;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public enum DiseaseCode {
        O, C, B, HT, D,IA,G,MH,CVC
    }

    /**
     * Define fields name for ncd_member_diseases_diagnosis.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String LOCATION_ID = "locationId";
        public static final String REMARKS = "remarks";
        public static final String DISEASE_CODE = "diseaseCode";
        public static final String STATUS = "status";
        public static final String IS_CASE_COMPLETED = "isCaseCompleted";
    }
}
