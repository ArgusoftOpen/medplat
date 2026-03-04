/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.cfhc.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>Database fields for Community First Health Co-op survey</p>
 *
 * @author rahul
 * @since 25/08/20 4:30 PM
 */
@javax.persistence.Entity
@Table(name = "imt_member_cfhc_master")
public class MemberCFHCEntity extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "is_child_going_school")
    private Boolean isChildGoingSchool;

    @Column(name = "current_studying_standard")
    private String currentStudyingStandard;

    @Column(name = "current_school")
    private Integer currentSchool;

    @Column(name = "ready_for_more_child")
    private Boolean readyForMoreChild;

    @Column(name = "family_planning_method")
    private String familyPlanningMethod;

    @Column(name = "another_family_planning_method")
    private String anotherFamilyPlanningMethod;

    @Column(name = "is_fever_with_cs_for_da_days")
    private Boolean isFeverWithCSForDADays;

    @Column(name = "is_fever_with_h_ep_mp_sr")
    private Boolean isFeverWithHEPMPSR;

    @Column(name = "is_fever_with_h_jp")
    private Boolean isFeverWithHJP;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "imt_member_sickle_cell_anemia_rel", joinColumns = @JoinColumn(name = "cfhc_id"))
    @Column(name = "sickle_cell_anemia_id")
    private Set<Integer> sickleCellAnemiaDetails;

    @Column(name = "sickle_cell_anemia")
    private String sickleCellAnemia;

    @Column(name = "is_skin_patches")
    private Boolean isSkinPatches;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "is_cough_for_mt_one_week")
    private Boolean isCoughForMTOneWeek;

    @Column(name = "is_fever_at_evening_time")
    private Boolean isFeverAtEveningTime;

    @Column(name = "is_feeling_any_weakness")
    private Boolean isFeelingAnyWeakness;

    @Column(name = "currently_using_fp_method")
    private Boolean currentlyUsingFpMethod;

    @Column(name = "change_fp_method")
    private Boolean changeFpMethod;

    @Column(name = "fp_method")
    private String fpMethod;

    @Column(name = "fp_insert_operate_date")
    @Temporal(value = TemporalType.DATE)
    private Date fpInsertOperateDate;

    @Column(name = "pmjay_number")
    private String pmjayNumber;

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

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Boolean getIsChildGoingSchool() {
        return isChildGoingSchool;
    }

    public void setIsChildGoingSchool(Boolean isChildGoingSchool) {
        this.isChildGoingSchool = isChildGoingSchool;
    }

    public String getCurrentStudyingStandard() {
        return currentStudyingStandard;
    }

    public void setCurrentStudyingStandard(String currentStudyingStandard) {
        this.currentStudyingStandard = currentStudyingStandard;
    }

    public Integer getCurrentSchool() {
        return currentSchool;
    }

    public void setCurrentSchool(Integer currentSchool) {
        this.currentSchool = currentSchool;
    }

    public Boolean getReadyForMoreChild() {
        return readyForMoreChild;
    }

    public void setReadyForMoreChild(Boolean readyForMoreChild) {
        this.readyForMoreChild = readyForMoreChild;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public String getAnotherFamilyPlanningMethod() {
        return anotherFamilyPlanningMethod;
    }

    public void setAnotherFamilyPlanningMethod(String anotherFamilyPlanningMethod) {
        this.anotherFamilyPlanningMethod = anotherFamilyPlanningMethod;
    }

    public Boolean getIsFeverWithCSForDADays() {
        return isFeverWithCSForDADays;
    }

    public void setIsFeverWithCSForDADays(Boolean isFeverWithCSForDADays) {
        this.isFeverWithCSForDADays = isFeverWithCSForDADays;
    }

    public Boolean getIsFeverWithHEPMPSR() {
        return isFeverWithHEPMPSR;
    }

    public void setIsFeverWithHEPMPSR(Boolean isFeverWithHEPMPSR) {
        this.isFeverWithHEPMPSR = isFeverWithHEPMPSR;
    }

    public Boolean getIsFeverWithHJP() {
        return isFeverWithHJP;
    }

    public void setIsFeverWithHJP(Boolean isFeverWithHJP) {
        this.isFeverWithHJP = isFeverWithHJP;
    }

    public Set<Integer> getSickleCellAnemiaDetails() {
        return sickleCellAnemiaDetails;
    }

    public void setSickleCellAnemiaDetails(Set<Integer> sickleCellAnemiaDetails) {
        this.sickleCellAnemiaDetails = sickleCellAnemiaDetails;
    }

    public String getSickleCellAnemia() {
        return sickleCellAnemia;
    }

    public void setSickleCellAnemia(String sickleCellAnemia) {
        this.sickleCellAnemia = sickleCellAnemia;
    }

    public Boolean getIsSkinPatches() {
        return isSkinPatches;
    }

    public void setIsSkinPatches(Boolean isSkinPatches) {
        this.isSkinPatches = isSkinPatches;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Boolean getIsCoughForMTOneWeek() {
        return isCoughForMTOneWeek;
    }

    public void setIsCoughForMTOneWeek(Boolean isCoughForMTOneWeek) {
        this.isCoughForMTOneWeek = isCoughForMTOneWeek;
    }

    public Boolean getIsFeverAtEveningTime() {
        return isFeverAtEveningTime;
    }

    public void setIsFeverAtEveningTime(Boolean isFeverAtEveningTime) {
        this.isFeverAtEveningTime = isFeverAtEveningTime;
    }

    public Boolean getIsFeelingAnyWeakness() {
        return isFeelingAnyWeakness;
    }

    public void setIsFeelingAnyWeakness(Boolean isFeelingAnyWeakness) {
        this.isFeelingAnyWeakness = isFeelingAnyWeakness;
    }

    public Boolean getCurrentlyUsingFpMethod() {
        return currentlyUsingFpMethod;
    }

    public void setCurrentlyUsingFpMethod(Boolean currentlyUsingFpMethod) {
        this.currentlyUsingFpMethod = currentlyUsingFpMethod;
    }

    public Boolean getChangeFpMethod() {
        return changeFpMethod;
    }

    public void setChangeFpMethod(Boolean changeFpMethod) {
        this.changeFpMethod = changeFpMethod;
    }

    public String getFpMethod() {
        return fpMethod;
    }

    public void setFpMethod(String fpMethod) {
        this.fpMethod = fpMethod;
    }

    public Date getFpInsertOperateDate() {
        return fpInsertOperateDate;
    }

    public void setFpInsertOperateDate(Date fpInsertOperateDate) {
        this.fpInsertOperateDate = fpInsertOperateDate;
    }

    public String getPmjayNumber() {
        return pmjayNumber;
    }

    public void setPmjayNumber(String pmjayNumber) {
        this.pmjayNumber = pmjayNumber;
    }

    public static class Fields {

        private Fields() {

        }

        public static final String FAMILY_ID_PROPERTY = "familyId";
        public static final String MEMBER_ID_PROPERTY = "memberId";
    }
}