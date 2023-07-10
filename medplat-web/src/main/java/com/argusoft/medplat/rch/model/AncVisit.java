/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.exception.ImtechoMobileException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Define rch_anc_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_anc_master")
public class AncVisit extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "lmp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lmp;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "last_delivery_outcome")
    private String lastDeliveryOutcome;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_anc_previous_pregnancy_complication_rel", joinColumns = @JoinColumn(name = "anc_id"))
    @Column(name = "previous_pregnancy_complication")
    private Set<String> previousPregnancyComplication;

    @Column(name = "other_previous_pregnancy_complication")
    private String otherPreviousPregnancyComplication;

    @Column(name = "jsy_beneficiary")
    private Boolean jsyBeneficiary;

    @Column(name = "jsy_payment_done")
    private Boolean jsyPaymentDone;

    @Column(name = "kpsy_beneficiary")
    private Boolean kpsyBeneficiary;

    @Column(name = "iay_beneficiary")
    private Boolean iayBeneficiary;

    @Column(name = "chiranjeevi_yojna_beneficiary")
    private Boolean chiranjeeviYojnaBeneficiary;

    @Column(name = "anc_place")
    private Integer ancPlace;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "haemoglobin_count", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float haemoglobinCount;

    @Column(name = "systolic_bp")
    private Integer systolicBp;

    @Column(name = "diastolic_bp")
    private Integer diastolicBp;

    @Column(name = "member_height")
    private Integer memberHeight;

    @Column(name = "foetal_movement")
    private String foetalMovement;

    @Column(name = "foetal_height")
    private Integer foetalHeight;

    @Column(name = "foetal_heart_sound")
    private Boolean foetalHeartSound;

    @Column(name = "foetal_position")
    private String foetalPosition;

    @Column(name = "ifa_tablets_given")
    private Integer ifaTabletsGiven;

    @Column(name = "fa_tablets_given")
    private Integer faTabletsGiven;

    @Column(name = "calcium_tablets_given")
    private Integer calciumTabletsGiven;

    @Column(name = "hbsag_test", length = 30)
    private String hbsagTest;

    @Column(name = "blood_sugar_test", length = 30)
    private String bloodSugarTest;

    @Column(name = "sugar_test_after_food_val")
    private Integer sugarTestAfterFoodValue;

    @Column(name = "sugar_test_before_food_val")
    private Integer sugarTestBeforeFoodValue;

    @Column(name = "urine_test_done")
    private Boolean urineTestDone;

    @Column(name = "urine_albumin")
    private String urineAlbumin;

    @Column(name = "urine_sugar")
    private String urineSugar;

    @Column(name = "vdrl_test")
    private String vdrlTest;

    @Column(name = "sickle_cell_test")
    private String sickleCellTest;

    @Column(name = "hiv_test")
    private String hivTest;

    @Column(name = "albendazole_given")
    private Boolean albendazoleGiven;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_anc_dangerous_sign_rel", joinColumns = @JoinColumn(name = "anc_id"))
    @Column(name = "dangerous_sign_id")
    private Set<Integer> dangerousSignIds;

    @Column(name = "other_dangerous_sign")
    private String otherDangerousSign;

    @Column(name = "referral_done")
    private String referralDone;

    @Column(name = "referral_place")
    private Integer referralPlace;

    @Column(name = "expected_delivery_place")
    private String expectedDeliveryPlace;

    @Column(name = "family_planning_method")
    private String familyPlanningMethod;

    @Column(name = "dead_flag")
    private Boolean deadFlag;

    @Column(name = "death_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDate;

    @Column(name = "place_of_death")
    private String placeOfDeath;

    //    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "rch_anc_death_reason_rel", joinColumns = @JoinColumn(name = "anc_id"))
    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "edd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date edd;

    @Column(name = "is_high_risk_case")
    private Boolean isHighRiskCase;

    @Column(name = "pregnancy_reg_det_id")
    private Integer pregnancyRegDetId;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "is_from_web")
    private Boolean isFromWeb;

    @Column(name = "delivery_place")
    private String deliveryPlace;

    @Column(name = "type_of_hospital")
    private Integer typeOfHospital;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfrastructureId;

    @Column(name = "delivery_done_by")
    private String deliveryDoneBy;

    @Column(name = "delivery_person")
    private Integer deliveryPerson;

    @Column(name = "delivery_person_name")
    private String deliveryPersonName;

    @Column(name = "blood_transfusion")
    private Boolean bloodTransfusion;

    @Column(name = "iron_def_anemia_inj")
    private String ironDefAnemiaInj;

    @Column(name = "iron_def_anemia_inj_due_date")
    private Date ironDefAnemiaInjDueDate;

    @Column(name = "death_infra_id")
    private Integer deathInfrastructureId;

    @Column(name = "examined_by_gynecologist")
    private Boolean examinedByGynecologist;

    @Column(name = "is_inj_corticosteroid_given")
    private Boolean isInjCorticosteroidGiven;

    @Column(name = "referral_infra_id")
    private Integer referralInfraId;

    @Column(name = "is_linked_to_abha")
    private Boolean isLinkedToAbha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Date getLmp() {
        return lmp;
    }

    public void setLmp(Date lmp) {
        if (lmp != null && ImtechoUtil.clearTimeFromDate(lmp).after(new Date())) {
            throw new ImtechoMobileException("LMP date cannot be future", 100);
        }
        this.lmp = ImtechoUtil.clearTimeFromDate(lmp);
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLastDeliveryOutcome() {
        return lastDeliveryOutcome;
    }

    public void setLastDeliveryOutcome(String lastDeliveryOutcome) {
        this.lastDeliveryOutcome = lastDeliveryOutcome;
    }

    public Set<String> getPreviousPregnancyComplication() {
        return previousPregnancyComplication;
    }

    public void setPreviousPregnancyComplication(Set<String> previousPregnancyComplication) {
        this.previousPregnancyComplication = previousPregnancyComplication;
    }

    public String getOtherPreviousPregnancyComplication() {
        return otherPreviousPregnancyComplication;
    }

    public void setOtherPreviousPregnancyComplication(String otherPreviousPregnancyComplication) {
        this.otherPreviousPregnancyComplication = otherPreviousPregnancyComplication;
    }

    public Boolean getJsyBeneficiary() {
        return jsyBeneficiary;
    }

    public void setJsyBeneficiary(Boolean jsyBeneficiary) {
        this.jsyBeneficiary = jsyBeneficiary;
    }

    public Boolean getJsyPaymentDone() {
        return jsyPaymentDone;
    }

    public void setJsyPaymentDone(Boolean jsyPaymentDone) {
        this.jsyPaymentDone = jsyPaymentDone;
    }

    public Boolean getKpsyBeneficiary() {
        return kpsyBeneficiary;
    }

    public void setKpsyBeneficiary(Boolean kpsyBeneficiary) {
        this.kpsyBeneficiary = kpsyBeneficiary;
    }

    public Boolean getIayBeneficiary() {
        return iayBeneficiary;
    }

    public void setIayBeneficiary(Boolean iayBeneficiary) {
        this.iayBeneficiary = iayBeneficiary;
    }

    public Boolean getChiranjeeviYojnaBeneficiary() {
        return chiranjeeviYojnaBeneficiary;
    }

    public void setChiranjeeviYojnaBeneficiary(Boolean chiranjeeviYojnaBeneficiary) {
        this.chiranjeeviYojnaBeneficiary = chiranjeeviYojnaBeneficiary;
    }

    public Integer getAncPlace() {
        return ancPlace;
    }

    public void setAncPlace(Integer ancPlace) {
        this.ancPlace = ancPlace;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHaemoglobinCount() {
        return haemoglobinCount;
    }

    public void setHaemoglobinCount(Float haemoglobinCount) {
        this.haemoglobinCount = haemoglobinCount;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(Integer diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public Integer getMemberHeight() {
        return memberHeight;
    }

    public void setMemberHeight(Integer memberHeight) {
        this.memberHeight = memberHeight;
    }

    public String getFoetalMovement() {
        return foetalMovement;
    }

    public void setFoetalMovement(String foetalMovement) {
        this.foetalMovement = foetalMovement;
    }

    public Integer getFoetalHeight() {
        return foetalHeight;
    }

    public void setFoetalHeight(Integer foetalHeight) {
        this.foetalHeight = foetalHeight;
    }

    public Boolean getFoetalHeartSound() {
        return foetalHeartSound;
    }

    public void setFoetalHeartSound(Boolean foetalHeartSound) {
        this.foetalHeartSound = foetalHeartSound;
    }

    public String getFoetalPosition() {
        return foetalPosition;
    }

    public void setFoetalPosition(String foetalPosition) {
        this.foetalPosition = foetalPosition;
    }

    public Integer getIfaTabletsGiven() {
        return ifaTabletsGiven;
    }

    public void setIfaTabletsGiven(Integer ifaTabletsGiven) {
        this.ifaTabletsGiven = ifaTabletsGiven;
    }

    public Integer getFaTabletsGiven() {
        return faTabletsGiven;
    }

    public void setFaTabletsGiven(Integer faTabletsGiven) {
        this.faTabletsGiven = faTabletsGiven;
    }

    public Integer getCalciumTabletsGiven() {
        return calciumTabletsGiven;
    }

    public void setCalciumTabletsGiven(Integer calciumTabletsGiven) {
        this.calciumTabletsGiven = calciumTabletsGiven;
    }

    public String getHbsagTest() {
        return hbsagTest;
    }

    public void setHbsagTest(String hbsagTest) {
        this.hbsagTest = hbsagTest;
    }

    public String getBloodSugarTest() {
        return bloodSugarTest;
    }

    public void setBloodSugarTest(String bloodSugarTest) {
        this.bloodSugarTest = bloodSugarTest;
    }

    public Integer getSugarTestAfterFoodValue() {
        return sugarTestAfterFoodValue;
    }

    public void setSugarTestAfterFoodValue(Integer sugarTestAfterFoodValue) {
        this.sugarTestAfterFoodValue = sugarTestAfterFoodValue;
    }

    public Integer getSugarTestBeforeFoodValue() {
        return sugarTestBeforeFoodValue;
    }

    public void setSugarTestBeforeFoodValue(Integer sugarTestBeforeFoodValue) {
        this.sugarTestBeforeFoodValue = sugarTestBeforeFoodValue;
    }

    public Boolean getUrineTestDone() {
        return urineTestDone;
    }

    public void setUrineTestDone(Boolean urineTestDone) {
        this.urineTestDone = urineTestDone;
    }

    public String getUrineAlbumin() {
        return urineAlbumin;
    }

    public void setUrineAlbumin(String urineAlbumin) {
        this.urineAlbumin = urineAlbumin;
    }

    public String getUrineSugar() {
        return urineSugar;
    }

    public void setUrineSugar(String urineSugar) {
        this.urineSugar = urineSugar;
    }

    public String getVdrlTest() {
        return vdrlTest;
    }

    public void setVdrlTest(String vdrlTest) {
        this.vdrlTest = vdrlTest;
    }

    public String getHivTest() {
        return hivTest;
    }

    public void setHivTest(String hivTest) {
        this.hivTest = hivTest;
    }

    public Boolean getAlbendazoleGiven() {
        return albendazoleGiven;
    }

    public void setAlbendazoleGiven(Boolean albendazoleGiven) {
        this.albendazoleGiven = albendazoleGiven;
    }

    public Set<Integer> getDangerousSignIds() {
        return dangerousSignIds;
    }

    public void setDangerousSignIds(Set<Integer> dangerousSignIds) {
        this.dangerousSignIds = dangerousSignIds;
    }

    public String getOtherDangerousSign() {
        return otherDangerousSign;
    }

    public void setOtherDangerousSign(String otherDangerousSign) {
        this.otherDangerousSign = otherDangerousSign;
    }

    public String getReferralDone() {
        return referralDone;
    }

    public void setReferralDone(String referralDone) {
        this.referralDone = referralDone;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getExpectedDeliveryPlace() {
        return expectedDeliveryPlace;
    }

    public void setExpectedDeliveryPlace(String expectedDeliveryPlace) {
        this.expectedDeliveryPlace = expectedDeliveryPlace;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Boolean getDeadFlag() {
        return deadFlag;
    }

    public void setDeadFlag(Boolean deadFlag) {
        this.deadFlag = deadFlag;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        if (deathDate != null && ImtechoUtil.clearTimeFromDate(deathDate).after(new Date())) {
            throw new ImtechoMobileException("Death date cannot be future", 100);
        }
        this.deathDate = ImtechoUtil.clearTimeFromDate(deathDate);
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

    public Date getEdd() {
        return edd;
    }

    public void setEdd(Date edd) {
        this.edd = edd;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public Integer getPregnancyRegDetId() {
        return pregnancyRegDetId;
    }

    public void setPregnancyRegDetId(Integer pregnancyRegDetId) {
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        if (serviceDate != null && ImtechoUtil.clearTimeFromDate(serviceDate).after(new Date())) {
            throw new ImtechoMobileException("Service date cannot be future", 100);
        }
        this.serviceDate = ImtechoUtil.clearTimeFromDate(serviceDate);
    }

    public String getSickleCellTest() {
        return sickleCellTest;
    }

    public void setSickleCellTest(String sickleCellTest) {
        this.sickleCellTest = sickleCellTest;
    }

    public Boolean getIsFromWeb() {
        return isFromWeb;
    }

    public void setIsFromWeb(Boolean isFromWeb) {
        this.isFromWeb = isFromWeb;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public Integer getTypeOfHospital() {
        return typeOfHospital;
    }

    public void setTypeOfHospital(Integer typeOfHospital) {
        this.typeOfHospital = typeOfHospital;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
    }

    public Integer getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Integer deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    public Boolean getBloodTransfusion() {
        return bloodTransfusion;
    }

    public void setBloodTransfusion(Boolean bloodTransfusion) {
        this.bloodTransfusion = bloodTransfusion;
    }

    public String getIronDefAnemiaInj() {
        return ironDefAnemiaInj;
    }

    public void setIronDefAnemiaInj(String ironDefAnemiaInj) {
        this.ironDefAnemiaInj = ironDefAnemiaInj;
    }

    public Date getIronDefAnemiaInjDueDate() {
        return ironDefAnemiaInjDueDate;
    }

    public void setIronDefAnemiaInjDueDate(Date ironDefAnemiaInjDueDate) {
        this.ironDefAnemiaInjDueDate = ironDefAnemiaInjDueDate;
    }

    public Integer getDeathInfrastructureId() {
        return deathInfrastructureId;
    }

    public void setDeathInfrastructureId(Integer deathInfrastructureId) {
        this.deathInfrastructureId = deathInfrastructureId;
    }

    public Boolean getExaminedByGynecologist() {
        return examinedByGynecologist;
    }

    public void setExaminedByGynecologist(Boolean examinedByGynecologist) {
        this.examinedByGynecologist = examinedByGynecologist;
    }

    public Boolean isInjCorticosteroidGiven() {
        return isInjCorticosteroidGiven;
    }

    public void setIsInjCorticosteroidGiven(Boolean isInjCorticosteroidGiven) {
        this.isInjCorticosteroidGiven = isInjCorticosteroidGiven;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    public Boolean getIsLinkedToAbha() {
        return isLinkedToAbha;
    }

    public void setIsLinkedToAbha(Boolean isLinkedToAbha) {
        this.isLinkedToAbha = isLinkedToAbha;
    }

    /**
     * Define fields name for rch_anc_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String LMP = "lmp";
        public static final String REFERRAL_PLACE = "referralPlace";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
        public static final String EDD = "edd";
        public static final String SERVICE_DATE = "serviceDate";
        public static final String HEALTH_INFRA_ID = "healthInfrastructureId";
        public static final String IS_LINKED_TO_ABHA = "isLinkedToAbha";
    }

    @Override
    public String toString() {
        return "AncVisit{" +
                "id=" + id +
                ", memberStatus='" + memberStatus + '\'' +
                ", lmp=" + lmp +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", lastDeliveryOutcome='" + lastDeliveryOutcome + '\'' +
                ", previousPregnancyComplication=" + previousPregnancyComplication +
                ", otherPreviousPregnancyComplication='" + otherPreviousPregnancyComplication + '\'' +
                ", jsyBeneficiary=" + jsyBeneficiary +
                ", jsyPaymentDone=" + jsyPaymentDone +
                ", kpsyBeneficiary=" + kpsyBeneficiary +
                ", iayBeneficiary=" + iayBeneficiary +
                ", chiranjeeviYojnaBeneficiary=" + chiranjeeviYojnaBeneficiary +
                ", ancPlace=" + ancPlace +
                ", weight=" + weight +
                ", haemoglobinCount=" + haemoglobinCount +
                ", systolicBp=" + systolicBp +
                ", diastolicBp=" + diastolicBp +
                ", memberHeight=" + memberHeight +
                ", foetalMovement='" + foetalMovement + '\'' +
                ", foetalHeight=" + foetalHeight +
                ", foetalHeartSound=" + foetalHeartSound +
                ", foetalPosition='" + foetalPosition + '\'' +
                ", ifaTabletsGiven=" + ifaTabletsGiven +
                ", faTabletsGiven=" + faTabletsGiven +
                ", calciumTabletsGiven=" + calciumTabletsGiven +
                ", hbsagTest='" + hbsagTest + '\'' +
                ", bloodSugarTest='" + bloodSugarTest + '\'' +
                ", sugarTestAfterFoodValue=" + sugarTestAfterFoodValue +
                ", sugarTestBeforeFoodValue=" + sugarTestBeforeFoodValue +
                ", urineTestDone=" + urineTestDone +
                ", urineAlbumin='" + urineAlbumin + '\'' +
                ", urineSugar='" + urineSugar + '\'' +
                ", vdrlTest='" + vdrlTest + '\'' +
                ", sickleCellTest='" + sickleCellTest + '\'' +
                ", hivTest='" + hivTest + '\'' +
                ", albendazoleGiven=" + albendazoleGiven +
                ", dangerousSignIds=" + dangerousSignIds +
                ", otherDangerousSign='" + otherDangerousSign + '\'' +
                ", referralDone='" + referralDone + '\'' +
                ", referralPlace=" + referralPlace +
                ", expectedDeliveryPlace='" + expectedDeliveryPlace + '\'' +
                ", familyPlanningMethod='" + familyPlanningMethod + '\'' +
                ", deadFlag=" + deadFlag +
                ", deathDate=" + deathDate +
                ", placeOfDeath='" + placeOfDeath + '\'' +
                ", deathReason='" + deathReason + '\'' +
                ", otherDeathReason='" + otherDeathReason + '\'' +
                ", edd=" + edd +
                ", isHighRiskCase=" + isHighRiskCase +
                ", pregnancyRegDetId=" + pregnancyRegDetId +
                ", serviceDate=" + serviceDate +
                ", isFromWeb=" + isFromWeb +
                ", deliveryPlace='" + deliveryPlace + '\'' +
                ", typeOfHospital=" + typeOfHospital +
                ", healthInfrastructureId=" + healthInfrastructureId +
                ", deliveryDoneBy='" + deliveryDoneBy + '\'' +
                ", deliveryPerson=" + deliveryPerson +
                ", deliveryPersonName='" + deliveryPersonName + '\'' +
                ", bloodTransfusion=" + bloodTransfusion +
                ", ironDefAnemiaInj='" + ironDefAnemiaInj + '\'' +
                ", ironDefAnemiaInjDueDate=" + ironDefAnemiaInjDueDate +
                ", deathInfrastructureId=" + deathInfrastructureId +
                ", examinedByGynecologist=" + examinedByGynecologist +
                ", isInjCorticosteroidGiven=" + isInjCorticosteroidGiven +
                ", isLinkedToAbha=" + isLinkedToAbha +
                ", referralInfraId=" + referralInfraId +
                '}';
    }
}
