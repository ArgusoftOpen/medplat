/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

/**
 * @author kelvin
 */
public class ClientPNCVisitMobDataBean {

    private List<ChildPNCVisitMobDataBean> childPNCVisitMobDataBeans;
    private Long id;
    private long client;
    private Boolean presentFlag;
    private Boolean familyFlag;
    private String pcmTabletStatus;
    private Boolean startIfaFlag;
    private Boolean bleedingFlag;
    private Integer padsCount;
    private Boolean foulSmellFlag;
    private Boolean sleepFlag;
    private Boolean abnormalBehaviourFlag;
    private Boolean feverFlag;
    private Boolean headacheFlag;
    private Boolean breastfeedingDifficultyFlag;
    private String problemPresent;
    private String breastProblemFlag;
    private Boolean isActive;
    private Boolean isArchive;
    private String lastModifiedBy;
    private Date createdOn;
    private String imageName;
    private Boolean isInterimVisit;
    private Date lastModifiedOn;
    private long byUser;
    private String motherName;
    private Boolean isMotherAlive;
    private String healthId;

    public Boolean getIsMotherAlive() {
        return isMotherAlive;
    }

    public void setIsMotherAlive(Boolean isMotherAlive) {
        this.isMotherAlive = isMotherAlive;
    }

    public List<ChildPNCVisitMobDataBean> getChildPNCVisitMobDataBeans() {
        return childPNCVisitMobDataBeans;
    }

    public void setChildPNCVisitMobDataBeans(List<ChildPNCVisitMobDataBean> childPNCVisitMobDataBeans) {
        this.childPNCVisitMobDataBeans = childPNCVisitMobDataBeans;
    }

    public Boolean getAbnormalBehaviourFlag() {
        return abnormalBehaviourFlag;
    }

    public void setAbnormalBehaviourFlag(Boolean abnormalBehaviourFlag) {
        this.abnormalBehaviourFlag = abnormalBehaviourFlag;
    }

    public Boolean getBleedingFlag() {
        return bleedingFlag;
    }

    public void setBleedingFlag(Boolean bleedingFlag) {
        this.bleedingFlag = bleedingFlag;
    }

    public String getBreastProblemFlag() {
        return breastProblemFlag;
    }

    public void setBreastProblemFlag(String breastProblemFlag) {
        this.breastProblemFlag = breastProblemFlag;
    }

    public Boolean getBreastfeedingDifficultyFlag() {
        return breastfeedingDifficultyFlag;
    }

    public void setBreastfeedingDifficultyFlag(Boolean breastfeedingDifficultyFlag) {
        this.breastfeedingDifficultyFlag = breastfeedingDifficultyFlag;
    }

    public long getByUser() {
        return byUser;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public long getClient() {
        return client;
    }

    public void setClient(long client) {
        this.client = client;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getFamilyFlag() {
        return familyFlag;
    }

    public void setFamilyFlag(Boolean familyFlag) {
        this.familyFlag = familyFlag;
    }

    public Boolean getFeverFlag() {
        return feverFlag;
    }

    public void setFeverFlag(Boolean feverFlag) {
        this.feverFlag = feverFlag;
    }

    public Boolean getFoulSmellFlag() {
        return foulSmellFlag;
    }

    public void setFoulSmellFlag(Boolean foulSmellFlag) {
        this.foulSmellFlag = foulSmellFlag;
    }

    public Boolean getHeadacheFlag() {
        return headacheFlag;
    }

    public void setHeadacheFlag(Boolean headacheFlag) {
        this.headacheFlag = headacheFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Boolean getIsInterimVisit() {
        return isInterimVisit;
    }

    public void setIsInterimVisit(Boolean isInterimVisit) {
        this.isInterimVisit = isInterimVisit;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Integer getPadsCount() {
        return padsCount;
    }

    public void setPadsCount(Integer padsCount) {
        this.padsCount = padsCount;
    }

    public String getPcmTabletStatus() {
        return pcmTabletStatus;
    }

    public void setPcmTabletStatus(String pcmTabletStatus) {
        this.pcmTabletStatus = pcmTabletStatus;
    }

    public Boolean getPresentFlag() {
        return presentFlag;
    }

    public void setPresentFlag(Boolean presentFlag) {
        this.presentFlag = presentFlag;
    }

    public String getProblemPresent() {
        return problemPresent;
    }

    public void setProblemPresent(String problemPresent) {
        this.problemPresent = problemPresent;
    }

    public Boolean getSleepFlag() {
        return sleepFlag;
    }

    public void setSleepFlag(Boolean sleepFlag) {
        this.sleepFlag = sleepFlag;
    }

    public Boolean getStartIfaFlag() {
        return startIfaFlag;
    }

    public void setStartIfaFlag(Boolean startIfaFlag) {
        this.startIfaFlag = startIfaFlag;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClientPNCVisitMobDataBean{" + "id=" + id + ", client=" + client + ", presentFlag=" + presentFlag + ", familyFlag=" + familyFlag + ", pcmTabletStatus=" + pcmTabletStatus + ", startIfaFlag=" + startIfaFlag + ", bleedingFlag=" + bleedingFlag + ", padsCount=" + padsCount + ", foulSmellFlag=" + foulSmellFlag + ", sleepFlag=" + sleepFlag + ", abnormalBehaviourFlag=" + abnormalBehaviourFlag + ", feverFlag=" + feverFlag + ", headacheFlag=" + headacheFlag + ", breastfeedingDifficultyFlag=" + breastfeedingDifficultyFlag + ", problemPresent=" + problemPresent + ", breastProblemFlag=" + breastProblemFlag + ", isActive=" + isActive + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", createdOn=" + createdOn + ", imageName=" + imageName + ", isInterimVisit=" + isInterimVisit + ", lastModifiedOn=" + lastModifiedOn + ", byUser=" + byUser + ", childPNCVisitMobDataBeans=" + childPNCVisitMobDataBeans + ", motherName=" + motherName + '}';
    }
}
