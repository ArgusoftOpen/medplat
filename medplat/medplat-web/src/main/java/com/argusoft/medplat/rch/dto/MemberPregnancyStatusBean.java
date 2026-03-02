package com.argusoft.medplat.rch.dto;

import java.util.Date;

/**
 * <p>
 * Used for member pregnancy status.
 * </p>
 *
 * @author krishna
 * @since 26/08/20 11:00 AM
 */
public class MemberPregnancyStatusBean {

    private Integer memberId;

    private Date regDate;

    private String ancDate;

    private String bp;

    private String haemoglobin;

    private String urineTest;

    private String weight;

    private String immunisation;

    private String faTablets;

    private String ifaTablets;

    private String calciumTablets;

    private Boolean nightBlindness;

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

}
