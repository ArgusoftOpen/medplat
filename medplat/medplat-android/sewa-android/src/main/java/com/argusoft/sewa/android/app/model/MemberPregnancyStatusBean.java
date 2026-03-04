package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by prateek on 1/10/20
 */
@DatabaseTable
public class MemberPregnancyStatusBean extends BaseEntity {

    @DatabaseField
    private Integer memberId;

    @DatabaseField
    private Date regDate;

    @DatabaseField
    private String ancDate;

    @DatabaseField
    private String bp;

    @DatabaseField
    private String haemoglobin;

    @DatabaseField
    private String urineTest;

    @DatabaseField
    private String weight;

    @DatabaseField
    private String immunisation;

    @DatabaseField
    private String faTablets;

    @DatabaseField
    private String ifaTablets;

    @DatabaseField
    private String calciumTablets;

    @DatabaseField
    private Boolean nightBlindness;

    @DatabaseField
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
