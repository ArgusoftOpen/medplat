/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Used for member breast details.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 11:00 AM
 */
@Getter
@Setter
public class MemberBreastDto {

    private Integer id;
    private Integer cbacId;
    private Long hmisId;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Boolean sizeChange;
    private Boolean nippleNotOnSameLevel;
    private Boolean anyRetractionOfNipple;
    private Boolean lymphadenopathy;
    private Boolean skinEdema;
    private Boolean dischargeFromNipple;
    private String visualDischargeFromNipple;
    private String visualSkinDimplingRetraction;
    private String visualNippleRetractionDistortion;
    private String visualSkinRetraction;
    private String visualLumpInBreast;
    private String visualUlceration;
    private Date screeningDate;
    private DoneBy doneBy;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Status status;
    private Boolean consultantFlag;
    private Integer healthInfraId;
    private Integer referredFromHealthInfrastructureId;
    private Boolean anyBreastRelatedSymptoms;
    private Boolean lumpInBreast;
    private Boolean nippleShapeAndPositionChange;
    private Boolean rednessOfSkinOverNipple;
    private Boolean erosionsOfNipple;
    private String remarks;
    private Boolean agreedForSelfBreastExam;
    private String visualSwellingInArmpit;
    private String visualRemarks;
    private String consistencyOfLumps;
    private String otherSymptoms;
    private Boolean skinDimpling;
    private Boolean ulceration;
    private Boolean retractionOfSkin;
    @JsonProperty("isAxillary")
    private Boolean isAxillary;
    @JsonProperty("isSuperClavicularArea")
    private Boolean isSuperClavicularArea;
    @JsonProperty("isInfraClavicularArea")
    private Boolean isInfraClavicularArea;
    private Boolean doesSuffering;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private Boolean takeMedicine;
    private Boolean htn;
    private String referralPlace;
    private Date followUpDate;
    private String reason;
    private String pvtHealthInfraName;

    public String getPvtHealthInfraName() {
        return pvtHealthInfraName;
    }

    public void setPvtHealthInfraName(String pvtHealthInfraName) {
        this.pvtHealthInfraName = pvtHealthInfraName;
    }

    public Boolean getAxillary() {
        return isAxillary;
    }

    public void setAxillary(Boolean axillary) {
        isAxillary = axillary;
    }

    public Boolean getSuperClavicularArea() {
        return isSuperClavicularArea;
    }

    public void setSuperClavicularArea(Boolean superClavicularArea) {
        isSuperClavicularArea = superClavicularArea;
    }

    public Boolean getInfraClavicularArea() {
        return isInfraClavicularArea;
    }

    public void setInfraClavicularArea(Boolean infraClavicularArea) {
        isInfraClavicularArea = infraClavicularArea;
    }
}
