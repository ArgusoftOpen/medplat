
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) laboratory
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_laboratory_detail")
public class ChildCmtcNrcLaboratory extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "laboratory_date")
    @Temporal(TemporalType.DATE)
    private Date laboratoryDate;

    @Column(name = "hemoglobin_checked")
    private Boolean hemoglobinChecked;

    @Column(name = "hemoglobin", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float hemoglobin;

    @Column(name = "ps_for_mp_checked")
    private Boolean psForMpChecked;

    @Column(name = "ps_for_mp")
    private String psForMp;

    @Column(name = "ps_for_mp_value")
    private String psForMpValue;

    @Column(name = "monotoux_test_checked")
    private Boolean monotouxTestChecked;

    @Column(name = "monotoux_test")
    private String monotouxTest;

    @Column(name = "xray_chest_checked")
    private Boolean xrayChestChecked;

    @Column(name = "xray_chest", columnDefinition = "numeric", precision = 12, scale = 2)
    private Float xrayChest;

    @Column(name = "urine_pus_cells_checked")
    private Boolean urinePusCellsChecked;

    @Column(name = "urine_pus_cells", columnDefinition = "numeric", precision = 12, scale = 2)
    private Float urinePusCells;

    @Column(name = "urine_albumin_checked")
    private Boolean urineAlbuminChecked;

    @Column(name = "urine_albumin")
    private String urineAlbumin;

    @Column(name = "hiv_checked")
    private Boolean hivChecked;

    @Column(name = "hiv")
    private String hiv;

    @Column(name = "sickle_checked")
    private Boolean sickleChecked;

    @Column(name = "sickle")
    private String sickle;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "test_output_state")
    private String testOutputState;

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

    public static class Fields {
        private Fields(){
        }
        public static final String ID = "id";
        public static final String ADMISSION_ID = "admissionId";
        public static final String LABORATORY_DATE = "laboratoryDate";
    }
}
