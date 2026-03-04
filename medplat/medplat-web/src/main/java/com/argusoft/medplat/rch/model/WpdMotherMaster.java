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
 * Define rch_wpd_mother_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_wpd_mother_master")
public class WpdMotherMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "date_of_delivery")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfDelivery;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "has_delivery_happened")
    private Boolean hasDeliveryHappened;

    @Column(name = "cortico_steroid_given")
    private Boolean corticoSteroidGiven;

    @Column(name = "is_preterm_birth")
    private Boolean isPretermBirth;

    @Column(name = "delivery_place")
    private String deliveryPlace;

    @Column(name = "institutional_delivery_place")
    private String institutionalDeliveryPlace;

    @Column(name = "type_of_hospital")
    private Integer typeOfHospital;

    @Column(name = "delivery_done_by")
    private String deliveryDoneBy;

    @Column(name = "type_of_delivery")
    private String typeOfDelivery;

    @Column(name = "mother_alive")
    private Boolean motherAlive;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfrastructureId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_wpd_mother_danger_signs_rel", joinColumns = @JoinColumn(name = "wpd_id"))
    @Column(name = "mother_danger_signs")
    private Set<Integer> motherDangerSigns;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_wpd_mother_high_risk_rel", joinColumns = @JoinColumn(name = "wpd_id"))
    @Column(name = "mother_high_risk")
    private Set<Integer> highRisks;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_wpd_mother_treatment_rel", joinColumns = @JoinColumn(name = "wpd_id"))
    @Column(name = "mother_treatment")
    private Set<Integer> treatments;

    @Column(name = "other_danger_signs")
    private String otherDangerSigns;

    @Column(name = "referral_done")
    private String referralDone;

    @Column(name = "referral_place")
    private Integer referralPlace;

    @Column(name = "is_discharged")
    private Boolean isDischarged;

    @Column(name = "discharge_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dischargeDate;

    @Column(name = "breast_feeding_in_one_hour")
    private Boolean breastFeedingInOneHour;

    @Column(name = "mtp_done_at")
    private Integer mtpDoneAt;

    @Column(name = "mtp_performed_by")
    private String mtpPerformedBy;

    @Column(name = "death_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDate;

    @Column(name = "place_of_death")
    private String placeOfDeath;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "is_high_risk_case")
    private Boolean isHighRiskCase;

    @Column(name = "pregnancy_reg_det_id")
    private Integer pregnancyRegDetId;

    @Column(name = "pregnancy_outcome")
    private String pregnancyOutcome;

    @Column(name = "misoprostol_given")
    private Boolean misoprostolGiven;

    @Column(name = "free_drop_delivery")
    private String freeDropDelivery;

    @Column(name = "delivery_person")
    private Integer deliveryPerson;

    @Column(name = "delivery_person_name")
    private String deliveryPersonName;

    @Column(name = "is_from_web")
    private Boolean isFromWeb;

    @Column(name = "free_medicines")
    private Boolean freeMedicines;

    @Column(name = "free_diet")
    private Boolean freeDiet;

    @Column(name = "free_lab_test")
    private Boolean freeLabTest;

    @Column(name = "free_blood_transfusion")
    private Boolean freeBloodTransfusion;

    @Column(name = "free_drop_transport")
    private Boolean freeDropTransport;

    @Column(name = "family_planning_method")
    private String familyPlanningMethod;

    @Column(name = "eligible_for_chiranjeevi")
    private Boolean eligibleForChiranjeevi;

    @Column(name = "fbmdsr")
    private Boolean fbmdsr;

    @Column(name = "referral_infra_id")
    private Integer referralInfraId;

    @Column(name = "death_infra_id")
    private Integer deathInfrastructureId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(Date dateOfDelivery) {
        if (dateOfDelivery != null && dateOfDelivery.after(new Date())) {
            throw new ImtechoMobileException("Delivery date cannot be future", 100);
        }
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Boolean getHasDeliveryHappened() {
        return hasDeliveryHappened;
    }

    public void setHasDeliveryHappened(Boolean hasDeliveryHappened) {
        this.hasDeliveryHappened = hasDeliveryHappened;
    }

    public Boolean getCorticoSteroidGiven() {
        return corticoSteroidGiven;
    }

    public void setCorticoSteroidGiven(Boolean corticoSteroidGiven) {
        this.corticoSteroidGiven = corticoSteroidGiven;
    }

    public Boolean getIsPretermBirth() {
        return isPretermBirth;
    }

    public void setIsPretermBirth(Boolean isPretermBirth) {
        this.isPretermBirth = isPretermBirth;
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

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public Boolean getMotherAlive() {
        return motherAlive;
    }

    public void setMotherAlive(Boolean motherAlive) {
        this.motherAlive = motherAlive;
    }

    public Set<Integer> getMotherDangerSigns() {
        return motherDangerSigns;
    }

    public void setMotherDangerSigns(Set<Integer> motherDangerSigns) {
        this.motherDangerSigns = motherDangerSigns;
    }

    public Set<Integer> getHighRisks() {
        return highRisks;
    }

    public void setHighRisks(Set<Integer> highRisks) {
        this.highRisks = highRisks;
    }

    public Set<Integer> getTreatments() {
        return treatments;
    }

    public void setTreatments(Set<Integer> treatments) {
        this.treatments = treatments;
    }

    public String getOtherDangerSigns() {
        return otherDangerSigns;
    }

    public void setOtherDangerSigns(String otherDangerSigns) {
        this.otherDangerSigns = otherDangerSigns;
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

    public Boolean getIsDischarged() {
        return isDischarged;
    }

    public void setIsDischarged(Boolean isDischarged) {
        this.isDischarged = isDischarged;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        if (dischargeDate != null && ImtechoUtil.clearTimeFromDate(dischargeDate).after(new Date())) {
            throw new ImtechoMobileException("Discharge date cannot be future", 100);
        }
        this.dischargeDate = dischargeDate;
    }

    public Boolean getBreastFeedingInOneHour() {
        return breastFeedingInOneHour;
    }

    public void setBreastFeedingInOneHour(Boolean breastFeedingInOneHour) {
        this.breastFeedingInOneHour = breastFeedingInOneHour;
    }

    public Integer getMtpDoneAt() {
        return mtpDoneAt;
    }

    public void setMtpDoneAt(Integer mtpDoneAt) {
        this.mtpDoneAt = mtpDoneAt;
    }

    public String getMtpPerformedBy() {
        return mtpPerformedBy;
    }

    public void setMtpPerformedBy(String mtpPerformedBy) {
        this.mtpPerformedBy = mtpPerformedBy;
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

    public String getPregnancyOutcome() {
        return pregnancyOutcome;
    }

    public void setPregnancyOutcome(String pregnancyOutcome) {
        this.pregnancyOutcome = pregnancyOutcome;
    }

    public Boolean getMisoprostolGiven() {
        return misoprostolGiven;
    }

    public void setMisoprostolGiven(Boolean misoprostolGiven) {
        this.misoprostolGiven = misoprostolGiven;
    }

    public String getFreeDropDelivery() {
        return freeDropDelivery;
    }

    public void setFreeDropDelivery(String freeDropDelivery) {
        this.freeDropDelivery = freeDropDelivery;
    }

    public Integer getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Integer deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    public Boolean getIsFromWeb() {
        return isFromWeb;
    }

    public void setIsFromWeb(Boolean isFromWeb) {
        this.isFromWeb = isFromWeb;
    }

    public String getInstitutionalDeliveryPlace() {
        return institutionalDeliveryPlace;
    }

    public void setInstitutionalDeliveryPlace(String institutionalDeliveryPlace) {
        this.institutionalDeliveryPlace = institutionalDeliveryPlace;
    }

    public Boolean getFreeMedicines() {
        return freeMedicines;
    }

    public void setFreeMedicines(Boolean freeMedicines) {
        this.freeMedicines = freeMedicines;
    }

    public Boolean getFreeDiet() {
        return freeDiet;
    }

    public void setFreeDiet(Boolean freeDiet) {
        this.freeDiet = freeDiet;
    }

    public Boolean getFreeLabTest() {
        return freeLabTest;
    }

    public void setFreeLabTest(Boolean freeLabTest) {
        this.freeLabTest = freeLabTest;
    }

    public Boolean getFreeBloodTransfusion() {
        return freeBloodTransfusion;
    }

    public void setFreeBloodTransfusion(Boolean freeBloodTransfusion) {
        this.freeBloodTransfusion = freeBloodTransfusion;
    }

    public Boolean getFreeDropTransport() {
        return freeDropTransport;
    }

    public void setFreeDropTransport(Boolean freeDropTransport) {
        this.freeDropTransport = freeDropTransport;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Boolean getEligibleForChiranjeevi() {
        return eligibleForChiranjeevi;
    }

    public void setEligibleForChiranjeevi(Boolean eligibleForChiranjeevi) {
        this.eligibleForChiranjeevi = eligibleForChiranjeevi;
    }

    public Boolean getFbmdsr() {
        return fbmdsr;
    }

    public void setFbmdsr(Boolean fbmdsr) {
        this.fbmdsr = fbmdsr;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    public Integer getDeathInfrastructureId() {
        return deathInfrastructureId;
    }

    public void setDeathInfrastructureId(Integer deathInfrastructureId) {
        this.deathInfrastructureId = deathInfrastructureId;
    }

    /**
     * Define fields name for rch_wpd_mother_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String REFERRAL_PLACE = "referralPlace";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
        public static final String PREGNANCY_REG_DET_ID = "pregnancyRegDetId";
        public static final String HEALTH_INFRA_ID = "healthInfrastructureId";
    }

    @Override
    public String toString() {
        return "WpdMotherMaster{" + "id=" + id + ", dateOfDelivery=" + dateOfDelivery + ", memberStatus=" + memberStatus + ", hasDeliveryHappened=" + hasDeliveryHappened + ", corticoSteroidGiven=" + corticoSteroidGiven + ", isPretermBirth=" + isPretermBirth + ", deliveryPlace=" + deliveryPlace + ", institutionalDeliveryPlace=" + institutionalDeliveryPlace + ", typeOfHospital=" + typeOfHospital + ", deliveryDoneBy=" + deliveryDoneBy + ", typeOfDelivery=" + typeOfDelivery + ", motherAlive=" + motherAlive + ", healthInfrastructureId=" + healthInfrastructureId + ", motherDangerSigns=" + motherDangerSigns + ", highRisks=" + highRisks + ", treatments=" + treatments + ", otherDangerSigns=" + otherDangerSigns + ", referralDone=" + referralDone + ", referralPlace=" + referralPlace + ", isDischarged=" + isDischarged + ", dischargeDate=" + dischargeDate + ", breastFeedingInOneHour=" + breastFeedingInOneHour + ", mtpDoneAt=" + mtpDoneAt + ", mtpPerformedBy=" + mtpPerformedBy + ", deathDate=" + deathDate + ", placeOfDeath=" + placeOfDeath + ", deathReason=" + deathReason + ", otherDeathReason=" + otherDeathReason + ", isHighRiskCase=" + isHighRiskCase + ", pregnancyRegDetId=" + pregnancyRegDetId + ", pregnancyOutcome=" + pregnancyOutcome + ", misoprostolGiven=" + misoprostolGiven + ", freeDropDelivery=" + freeDropDelivery + ", deliveryPerson=" + deliveryPerson + ", deliveryPersonName=" + deliveryPersonName + ", isFromWeb=" + isFromWeb + ", freeMedicines=" + freeMedicines + ", freeDiet=" + freeDiet + ", freeLabTest=" + freeLabTest + ", freeBloodTransfusion=" + freeBloodTransfusion + ", freeDropTransport=" + freeDropTransport + ", familyPlanningMethod=" + familyPlanningMethod + ", eligibleForChiranjeevi=" + eligibleForChiranjeevi + ", fbmdsr=" + fbmdsr + ", referralInfraId=" + referralInfraId + '}';
    }

}
