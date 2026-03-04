package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Define rch_asha_pnc_mother_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_pnc_mother_master")
public class AshaPncMotherMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pnc_master_id")
    private Integer pncMasterId;

    @Column(name = "mother_id")
    private Integer motherId;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @Column(name = "death_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "bleeding_continues")
    private Boolean bleedingContinues;

    @Column(name = "pads_changed_in_24_hours")
    private Integer padsChangedIn24Hours;

    @Column(name = "foul_smell_discharge")
    private Boolean foulSmellDischarge;

    @Column(name = "abnormal_behaviour")
    private Boolean abnormalBehaviour;

    @Column(name = "have_fever")
    private Boolean haveFever;

    @Column(name = "have_visual_disturbances")
    private Boolean haveVisualDisturbances;

    @Column(name = "difficulty_in_breastfeeding")
    private Boolean difficultyInBreastfeeding;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_asha_pnc_mother_problem_present", joinColumns = @JoinColumn(name = "pnc_id"))
    @Column(name = "problem")
    private Set<String> problemsPresent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPncMasterId() {
        return pncMasterId;
    }

    public void setPncMasterId(Integer pncMasterId) {
        this.pncMasterId = pncMasterId;
    }

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
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

    public Boolean getBleedingContinues() {
        return bleedingContinues;
    }

    public void setBleedingContinues(Boolean bleedingContinues) {
        this.bleedingContinues = bleedingContinues;
    }

    public Integer getPadsChangedIn24Hours() {
        return padsChangedIn24Hours;
    }

    public void setPadsChangedIn24Hours(Integer padsChangedIn24Hours) {
        this.padsChangedIn24Hours = padsChangedIn24Hours;
    }

    public Boolean getFoulSmellDischarge() {
        return foulSmellDischarge;
    }

    public void setFoulSmellDischarge(Boolean foulSmellDischarge) {
        this.foulSmellDischarge = foulSmellDischarge;
    }

    public Boolean getAbnormalBehaviour() {
        return abnormalBehaviour;
    }

    public void setAbnormalBehaviour(Boolean abnormalBehaviour) {
        this.abnormalBehaviour = abnormalBehaviour;
    }

    public Boolean getHaveFever() {
        return haveFever;
    }

    public void setHaveFever(Boolean haveFever) {
        this.haveFever = haveFever;
    }

    public Boolean getHaveVisualDisturbances() {
        return haveVisualDisturbances;
    }

    public void setHaveVisualDisturbances(Boolean haveVisualDisturbances) {
        this.haveVisualDisturbances = haveVisualDisturbances;
    }

    public Boolean getDifficultyInBreastfeeding() {
        return difficultyInBreastfeeding;
    }

    public void setDifficultyInBreastfeeding(Boolean difficultyInBreastfeeding) {
        this.difficultyInBreastfeeding = difficultyInBreastfeeding;
    }

    public Set<String> getProblemsPresent() {
        return problemsPresent;
    }

    public void setProblemsPresent(Set<String> problemsPresent) {
        this.problemsPresent = problemsPresent;
    }

    /**
     * Define fields name for rch_asha_pnc_mother_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String PNC_MASTER_ID = "pncMasterId";
        public static final String MOTHER_ID = "motherId";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
    }

}
