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
 * Define rch_wpd_child_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_wpd_child_master")
public class WpdChildMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "wpd_mother_id")
    private Integer wpdMotherId;

    @Column(name = "mother_id")
    private Integer motherId;

    @Column(name = "pregnancy_outcome")
    private String pregnancyOutcome;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float birthWeight;

    @Column(name = "date_of_delivery")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfDelivery;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_wpd_child_congential_deformity_rel", joinColumns = @JoinColumn(name = "wpd_id"))
    @Column(name = "congential_deformity")
    private Set<Integer> congentialDeformity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_wpd_child_danger_signs_rel", joinColumns = @JoinColumn(name = "wpd_id"))
    @Column(name = "danger_signs")
    private Set<Integer> dangerSigns;

    @Column(name = "other_congential_deformity")
    private String otherCongentialDeformity;

    @Column(name = "other_danger_sign")
    private String otherDangerSign;

    @Column(name = "baby_cried_at_birth")
    private Boolean babyCriedAtBirth;

    @Column(name = "breast_feeding_in_one_hour")
    private Boolean breastFeedingInOneHour;

    @Column(name = "type_of_delivery")
    private String typeOfDelivery;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "death_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDate;

    @Column(name = "place_of_death")
    private String placeOfDeath;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfrastructureId;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "is_high_risk_case")
    private Boolean isHighRiskCase;

    @Column(name = "was_premature")
    private Boolean wasPremature;

    @Column(name = "referral_done")
    private String referralDone;

    @Column(name = "referral_reason")
    private String referralReason;

    @Column(name = "referral_transport")
    private String referralTransport;

    @Column(name = "referral_place")
    private Integer referralPlace;

    @Column(name = "name")
    private String name;

    @Column(name = "breast_crawl")
    private Boolean breastCrawl;

    @Column(name = "kangaroo_care")
    private Boolean kangarooCare;

    @Column(name = "referral_infra_id")
    private Integer referralInfraId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWpdMotherId() {
        return wpdMotherId;
    }

    public void setWpdMotherId(Integer wpdMotherId) {
        this.wpdMotherId = wpdMotherId;
    }

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public String getPregnancyOutcome() {
        return pregnancyOutcome;
    }

    public void setPregnancyOutcome(String pregnancyOutcome) {
        this.pregnancyOutcome = pregnancyOutcome;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Float getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(Float birthWeight) {
        this.birthWeight = birthWeight;
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

    public Set<Integer> getCongentialDeformity() {
        return congentialDeformity;
    }

    public void setCongentialDeformity(Set<Integer> congentialDeformity) {
        this.congentialDeformity = congentialDeformity;
    }

    public String getOtherCongentialDeformity() {
        return otherCongentialDeformity;
    }

    public void setOtherCongentialDeformity(String otherCongentialDeformity) {
        this.otherCongentialDeformity = otherCongentialDeformity;
    }

    public Boolean getBabyCriedAtBirth() {
        return babyCriedAtBirth;
    }

    public void setBabyCriedAtBirth(Boolean babyCriedAtBirth) {
        this.babyCriedAtBirth = babyCriedAtBirth;
    }

    public Boolean getBreastFeedingInOneHour() {
        return breastFeedingInOneHour;
    }

    public void setBreastFeedingInOneHour(Boolean breastFeedingInOneHour) {
        this.breastFeedingInOneHour = breastFeedingInOneHour;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
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

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public Boolean getWasPremature() {
        return wasPremature;
    }

    public void setWasPremature(Boolean wasPremature) {
        this.wasPremature = wasPremature;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
    }

    public String getReferralTransport() {
        return referralTransport;
    }

    public void setReferralTransport(String referralTransport) {
        this.referralTransport = referralTransport;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBreastCrawl() {
        return breastCrawl;
    }

    public void setBreastCrawl(Boolean breastCrawl) {
        this.breastCrawl = breastCrawl;
    }

    public Boolean getKangarooCare() {
        return kangarooCare;
    }

    public void setKangarooCare(Boolean kangarooCare) {
        this.kangarooCare = kangarooCare;
    }

    public Set<Integer> getDangerSigns() {
        return dangerSigns;
    }

    public void setDangerSigns(Set<Integer> dangerSigns) {
        this.dangerSigns = dangerSigns;
    }

    public String getOtherDangerSign() {
        return otherDangerSign;
    }

    public void setOtherDangerSign(String otherDangerSign) {
        this.otherDangerSign = otherDangerSign;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    public String getReferralDone() {
        return referralDone;
    }

    public void setReferralDone(String referralDone) {
        this.referralDone = referralDone;
    }

    /**
     * Define fields name for rch_wpd_child_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String WPD_MOTHER_ID = "wpdMotherId";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
        public static final String REFERRAL_PLACE = "referralPlace";
        public static final String NAME = "name";
    }

    @Override
    public String toString() {
        return "WpdChildMaster{" + "id=" + id + ", wpdMotherId=" + wpdMotherId + ", motherId=" + motherId + ", pregnancyOutcome=" + pregnancyOutcome + ", gender=" + gender + ", birthWeight=" + birthWeight + ", dateOfDelivery=" + dateOfDelivery + ", congentialDeformity=" + congentialDeformity + ", dangerSigns=" + dangerSigns + ", otherCongentialDeformity=" + otherCongentialDeformity + ", otherDangerSign=" + otherDangerSign + ", babyCriedAtBirth=" + babyCriedAtBirth + ", breastFeedingInOneHour=" + breastFeedingInOneHour + ", typeOfDelivery=" + typeOfDelivery + ", memberStatus=" + memberStatus + ", deathDate=" + deathDate + ", placeOfDeath=" + placeOfDeath + ", deathReason=" + deathReason + ", isHighRiskCase=" + isHighRiskCase + ", wasPremature=" + wasPremature + ", referralDone=" + referralDone + ", referralReason=" + referralReason + ", referralTransport=" + referralTransport + ", referralPlace=" + referralPlace + ", name=" + name + ", breastCrawl=" + breastCrawl + ", kangarooCare=" + kangarooCare + ", referralInfraId=" + referralInfraId + '}';
    }

}
