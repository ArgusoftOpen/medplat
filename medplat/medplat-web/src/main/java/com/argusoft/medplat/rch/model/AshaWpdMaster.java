package com.argusoft.medplat.rch.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_wpd_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_wpd_master")
public class AshaWpdMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pregnancy_reg_det_id")
    private Integer pregnancyRegDetId;

    @Column(name = "service_date")
    @Temporal(TemporalType.DATE)
    private Date serviceDate;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "has_delivery_happened")
    private Boolean hasDeliveryHappened;

    @Column(name = "mother_alive")
    private Boolean motherAlive;

    @Column(name = "death_date")
    @Temporal(TemporalType.DATE)
    private Date deathDate;

    @Column(name = "place_of_death")
    private String placeOfDeath;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.serviceDate = serviceDate;
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

    public Boolean getMotherAlive() {
        return motherAlive;
    }

    public void setMotherAlive(Boolean motherAlive) {
        this.motherAlive = motherAlive;
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

}
