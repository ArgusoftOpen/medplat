package com.argusoft.medplat.rch.model;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * Define rch_member_pregnancy_status entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_member_pregnancy_status")
public class MemberPregnancyStatusMaster {

    @Id
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "reg_date")
    @Temporal(value = TemporalType.DATE)
    private Date regDate;

    @Column(name = "anc_date")
    private String ancDate;

    @Column(name = "bp")
    private String bp;

    @Column(name = "haemoglobin")
    private String haemoglobin;

    @Column(name = "urine_test")
    private String urineTest;

    @Column(name = "weight")
    private String weight;

    @Column(name = "immunisation")
    private String immunisation;

    @Column(name = "fa_tablets")
    private String faTablets;

    @Column(name = "ifa_tablets")
    private String ifaTablets;

    @Column(name = "calcium_tablets")
    private String calciumTablets;

    @Column(name = "night_blindness")
    private Boolean nightBlindness;

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getAncDate() {
        return ancDate;
    }

    public void setAncDate(String ancDate) {
        this.ancDate = ancDate;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(String haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public String getUrineTest() {
        return urineTest;
    }

    public void setUrineTest(String urineTest) {
        this.urineTest = urineTest;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImmunisation() {
        return immunisation;
    }

    public void setImmunisation(String immunisation) {
        this.immunisation = immunisation;
    }

    public String getFaTablets() {
        return faTablets;
    }

    public void setFaTablets(String faTablets) {
        this.faTablets = faTablets;
    }

    public String getIfaTablets() {
        return ifaTablets;
    }

    public void setIfaTablets(String ifaTablets) {
        this.ifaTablets = ifaTablets;
    }

    public String getCalciumTablets() {
        return calciumTablets;
    }

    public void setCalciumTablets(String calciumTablets) {
        this.calciumTablets = calciumTablets;
    }

    public Boolean getNightBlindness() {
        return nightBlindness;
    }

    public void setNightBlindness(Boolean nightBlindness) {
        this.nightBlindness = nightBlindness;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
