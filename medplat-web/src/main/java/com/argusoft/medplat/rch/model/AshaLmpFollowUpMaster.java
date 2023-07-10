package com.argusoft.medplat.rch.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_lmp_follow_up entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_lmp_follow_up")
public class AshaLmpFollowUpMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "year")
    private Short year;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "lmp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lmp;

    @Column(name = "pregnancy_test_done")
    private Boolean pregnancyTestDone;

    @Column(name = "is_pregnant", nullable = false)
    private Boolean isPregnant;

    @Column(name = "family_planning_method")
    private String familyPlanningMethod;

    @Column(name = "fp_insert_operate_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fpInsertOperateDate;

    @Column(name = "death_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDate;

    @Column(name = "place_of_death")
    private String placeOfDeath;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "preg_conf_status")
    private String pregConfStatus;

    @Column(name = "current_gravida")
    private Short currentGravida;

    @Column(name = "current_para")
    private Short currentPara;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getLmp() {
        return lmp;
    }

    public void setLmp(Date lmp) {
        this.lmp = lmp;
    }

    public Boolean getPregnancyTestDone() {
        return pregnancyTestDone;
    }

    public void setPregnancyTestDone(Boolean pregnancyTestDone) {
        this.pregnancyTestDone = pregnancyTestDone;
    }

    public Boolean getIsPregnant() {
        return isPregnant;
    }

    public void setIsPregnant(Boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Date getFpInsertOperateDate() {
        return fpInsertOperateDate;
    }

    public void setFpInsertOperateDate(Date fpInsertOperateDate) {
        this.fpInsertOperateDate = fpInsertOperateDate;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public String getPregConfStatus() {
        return pregConfStatus;
    }

    public void setPregConfStatus(String pregConfStatus) {
        this.pregConfStatus = pregConfStatus;
    }

    public Short getCurrentGravida() {
        return currentGravida;
    }

    public void setCurrentGravida(Short currentGravida) {
        this.currentGravida = currentGravida;
    }

    public Short getCurrentPara() {
        return currentPara;
    }

    public void setCurrentPara(Short currentPara) {
        this.currentPara = currentPara;
    }
}
