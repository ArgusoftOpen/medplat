
package com.argusoft.medplat.nutrition.dto;

import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) laboratory
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcLaboratoryDto {

    private Integer id;

    private Integer admissionId;

    private Date laboratoryDate;

    private Boolean hemoglobinChecked;

    private Float hemoglobin;

    private Boolean psForMpChecked;

    private String psForMp;

    private String psForMpValue;

    private Boolean monotouxTestChecked;

    private String monotouxTest;

    private Boolean xrayChestChecked;

    private Float xrayChest;

    private Boolean urinePusCellsChecked;

    private Float urinePusCells;

    private Boolean urineAlbuminChecked;

    private String urineAlbumin;

    private String bloodGroup;

    private String testOutputState;

    private Boolean hivChecked;

    private String hiv;

    private Boolean sickleChecked;

    private String sickle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Date getLaboratoryDate() {
        return laboratoryDate;
    }

    public void setLaboratoryDate(Date laboratoryDate) {
        this.laboratoryDate = laboratoryDate;
    }

    public Boolean getHemoglobinChecked() {
        return hemoglobinChecked;
    }

    public void setHemoglobinChecked(Boolean hemoglobinChecked) {
        this.hemoglobinChecked = hemoglobinChecked;
    }

    public Float getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(Float hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public Boolean getPsForMpChecked() {
        return psForMpChecked;
    }

    public void setPsForMpChecked(Boolean psForMpChecked) {
        this.psForMpChecked = psForMpChecked;
    }

    public String getPsForMp() {
        return psForMp;
    }

    public void setPsForMp(String psForMp) {
        this.psForMp = psForMp;
    }

    public String getPsForMpValue() {
        return psForMpValue;
    }

    public void setPsForMpValue(String psForMpValue) {
        this.psForMpValue = psForMpValue;
    }

    public Boolean getMonotouxTestChecked() {
        return monotouxTestChecked;
    }

    public void setMonotouxTestChecked(Boolean monotouxTestChecked) {
        this.monotouxTestChecked = monotouxTestChecked;
    }

    public String getMonotouxTest() {
        return monotouxTest;
    }

    public void setMonotouxTest(String monotouxTest) {
        this.monotouxTest = monotouxTest;
    }

    public Boolean getXrayChestChecked() {
        return xrayChestChecked;
    }

    public void setXrayChestChecked(Boolean xrayChestChecked) {
        this.xrayChestChecked = xrayChestChecked;
    }

    public Float getXrayChest() {
        return xrayChest;
    }

    public void setXrayChest(Float xrayChest) {
        this.xrayChest = xrayChest;
    }

    public Boolean getUrineAlbuminChecked() {
        return urineAlbuminChecked;
    }

    public void setUrineAlbuminChecked(Boolean urineAlbuminChecked) {
        this.urineAlbuminChecked = urineAlbuminChecked;
    }

    public String getUrineAlbumin() {
        return urineAlbumin;
    }

    public void setUrineAlbumin(String urineAlbumin) {
        this.urineAlbumin = urineAlbumin;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getTestOutputState() {
        return testOutputState;
    }

    public void setTestOutputState(String testOutputState) {
        this.testOutputState = testOutputState;
    }

    public Boolean getUrinePusCellsChecked() {
        return urinePusCellsChecked;
    }

    public void setUrinePusCellsChecked(Boolean urinePusCellsChecked) {
        this.urinePusCellsChecked = urinePusCellsChecked;
    }

    public Float getUrinePusCells() {
        return urinePusCells;
    }

    public void setUrinePusCells(Float urinePusCells) {
        this.urinePusCells = urinePusCells;
    }

    public Boolean getHivChecked() {
        return hivChecked;
    }

    public void setHivChecked(Boolean hivChecked) {
        this.hivChecked = hivChecked;
    }

    public String getHiv() {
        return hiv;
    }

    public void setHiv(String hiv) {
        this.hiv = hiv;
    }

    public Boolean getSickleChecked() {
        return sickleChecked;
    }

    public void setSickleChecked(Boolean sickleChecked) {
        this.sickleChecked = sickleChecked;
    }

    public String getSickle() {
        return sickle;
    }

    public void setSickle(String sickle) {
        this.sickle = sickle;
    }
}
