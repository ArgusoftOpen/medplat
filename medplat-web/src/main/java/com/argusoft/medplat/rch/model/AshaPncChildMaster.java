package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_pnc_child_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_pnc_child_master")
public class AshaPncChildMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pnc_master_id")
    private Integer pncMasterId;

    @Column(name = "child_id")
    private Integer childId;

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

    @Column(name = "cry")
    private String cry;

    @Column(name = "fed_less_than_usual")
    private String fedLessThanUsual;

    @Column(name = "sucking")
    private String sucking;

    @Column(name = "throws_milk")
    private Boolean throwsMilk;

    @Column(name = "hands_feets_cold")
    private Boolean handsFeetsCold;

    @Column(name = "skin")
    private String skin;

    @Column(name = "skin_pustules")
    private Boolean skinPustules;

    @Column(name = "have_chest_indrawing")
    private Boolean haveChestIndrawing;

    @Column(name = "umbilicus")
    private String umbilicus;

    @Column(name = "abdomen")
    private String abdomen;

    @Column(name = "tempreature", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float tempreature;

    @Column(name = "limbs_neck")
    private String limbsNeck;

    @Column(name = "eyes")
    private String eyes;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "weight_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date weightDate;

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

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
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

    public String getCry() {
        return cry;
    }

    public void setCry(String cry) {
        this.cry = cry;
    }

    public String getFedLessThanUsual() {
        return fedLessThanUsual;
    }

    public void setFedLessThanUsual(String fedLessThanUsual) {
        this.fedLessThanUsual = fedLessThanUsual;
    }

    public String getSucking() {
        return sucking;
    }

    public void setSucking(String sucking) {
        this.sucking = sucking;
    }

    public Boolean getThrowsMilk() {
        return throwsMilk;
    }

    public void setThrowsMilk(Boolean throwsMilk) {
        this.throwsMilk = throwsMilk;
    }

    public Boolean getHandsFeetsCold() {
        return handsFeetsCold;
    }

    public void setHandsFeetsCold(Boolean handsFeetsCold) {
        this.handsFeetsCold = handsFeetsCold;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public Boolean getSkinPustules() {
        return skinPustules;
    }

    public void setSkinPustules(Boolean skinPustules) {
        this.skinPustules = skinPustules;
    }

    public Boolean getHaveChestIndrawing() {
        return haveChestIndrawing;
    }

    public void setHaveChestIndrawing(Boolean haveChestIndrawing) {
        this.haveChestIndrawing = haveChestIndrawing;
    }

    public String getUmbilicus() {
        return umbilicus;
    }

    public void setUmbilicus(String umbilicus) {
        this.umbilicus = umbilicus;
    }

    public String getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(String abdomen) {
        this.abdomen = abdomen;
    }

    public Float getTempreature() {
        return tempreature;
    }

    public void setTempreature(Float tempreature) {
        this.tempreature = tempreature;
    }

    public String getLimbsNeck() {
        return limbsNeck;
    }

    public void setLimbsNeck(String limbsNeck) {
        this.limbsNeck = limbsNeck;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Date getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(Date weightDate) {
        this.weightDate = weightDate;
    }

    /**
     * Define fields name for rch_asha_pnc_child_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String PNC_MASTER_ID = "pncMasterId";
        public static final String CHILD_ID = "childId";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
    }

}
