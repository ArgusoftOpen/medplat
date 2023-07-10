/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * @author kelvin
 */
public class ChildPNCVisitMobDataBean {

    private Long id;
    private long child;
    private Boolean familyFlag;
    private String skinStatus;
    private Boolean skinPustules;
    private Boolean nasalFlaringFlag;
    private Boolean chestIndrawingFlag;
    private Integer respirationRate;
    private String umbilicus;
    private String abdomen;
    private String mCryStatus;
    private String mBabyFed;//changed
    private String mSucklingStatus;//Changed
    private String mFoodGiven;
    private Boolean mVomitingFlag;
    private Boolean mLooseMotionFlag;
    private String mConsciousnessStatus;
    private Boolean mFeverFlag;
    private Boolean mHandsTouchFlag;
    private Boolean mChestIndrawingFlag;
    private String mSkinProblem;
    private String mBleedingStatus;
    private Float temperature;
    private String consciousnessStatus;//changed
    private String cryStatus;//changed
    private Boolean oralThrushFlag;
    private Float weight;
    private String chinTouchStatus;
    private String mouthOpenStatus;
    private String lowerLipStatus;
    private String areolaStatus;
    private Boolean wearingClothesFlag;
    private Boolean properlyCoveredFlag;
    private Boolean givenBathFlag;
    private Boolean keptNearMotherFlag;
    private Boolean isActive;
    private Boolean isArchive;
    private String lastModifiedBy;
    private Date createdOn;
    private String imageName;
    private Boolean isInterimVisit;
    private Date lastModifiedOn;
    private long byUser;
    private String childName;
    private Boolean isChildAlive;
    //added in phase 2
    private String whetherBabyLimbsAndNeckMoreLimpThanBefore;//Phase II 117
    private String howAreBabyEyes;//Phase II 118
    private String healthId;

    public String getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(String abdomen) {
        this.abdomen = abdomen;
    }

    public String getAreolaStatus() {
        return areolaStatus;
    }

    public void setAreolaStatus(String areolaStatus) {
        this.areolaStatus = areolaStatus;
    }

    public long getByUser() {
        return byUser;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public Boolean getChestIndrawingFlag() {
        return chestIndrawingFlag;
    }

    public void setChestIndrawingFlag(Boolean chestIndrawingFlag) {
        this.chestIndrawingFlag = chestIndrawingFlag;
    }

    public long getChild() {
        return child;
    }

    public void setChild(long child) {
        this.child = child;
    }

    public String getChinTouchStatus() {
        return chinTouchStatus;
    }

    public void setChinTouchStatus(String chinTouchStatus) {
        this.chinTouchStatus = chinTouchStatus;
    }

    public String getConsciousnessStatus() {
        return consciousnessStatus;
    }

    public void setConsciousnessStatus(String consciousnessStatus) {
        this.consciousnessStatus = consciousnessStatus;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCryStatus() {
        return cryStatus;
    }

    public void setCryStatus(String cryStatus) {
        this.cryStatus = cryStatus;
    }

    public Boolean getFamilyFlag() {
        return familyFlag;
    }

    public void setFamilyFlag(Boolean familyFlag) {
        this.familyFlag = familyFlag;
    }

    public Boolean getGivenBathFlag() {
        return givenBathFlag;
    }

    public void setGivenBathFlag(Boolean givenBathFlag) {
        this.givenBathFlag = givenBathFlag;
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

    public Boolean getKeptNearMotherFlag() {
        return keptNearMotherFlag;
    }

    public void setKeptNearMotherFlag(Boolean keptNearMotherFlag) {
        this.keptNearMotherFlag = keptNearMotherFlag;
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

    public String getLowerLipStatus() {
        return lowerLipStatus;
    }

    public void setLowerLipStatus(String lowerLipStatus) {
        this.lowerLipStatus = lowerLipStatus;
    }

    public String getmBabyFed() {
        return mBabyFed;
    }

    public void setmBabyFed(String mBabyFed) {
        this.mBabyFed = mBabyFed;
    }

    public String getmBleedingStatus() {
        return mBleedingStatus;
    }

    public void setmBleedingStatus(String mBleedingStatus) {
        this.mBleedingStatus = mBleedingStatus;
    }

    public Boolean getmChestIndrawingFlag() {
        return mChestIndrawingFlag;
    }

    public void setmChestIndrawingFlag(Boolean mChestIndrawingFlag) {
        this.mChestIndrawingFlag = mChestIndrawingFlag;
    }

    public String getmConsciousnessStatus() {
        return mConsciousnessStatus;
    }

    public void setmConsciousnessStatus(String mConsciousnessStatus) {
        this.mConsciousnessStatus = mConsciousnessStatus;
    }

    public String getmCryStatus() {
        return mCryStatus;
    }

    public void setmCryStatus(String mCryStatus) {
        this.mCryStatus = mCryStatus;
    }

    public Boolean getmFeverFlag() {
        return mFeverFlag;
    }

    public void setmFeverFlag(Boolean mFeverFlag) {
        this.mFeverFlag = mFeverFlag;
    }

    public String getmFoodGiven() {
        return mFoodGiven;
    }

    public void setmFoodGiven(String mFoodGiven) {
        this.mFoodGiven = mFoodGiven;
    }

    public Boolean getmHandsTouchFlag() {
        return mHandsTouchFlag;
    }

    public void setmHandsTouchFlag(Boolean mHandsTouchFlag) {
        this.mHandsTouchFlag = mHandsTouchFlag;
    }

    public Boolean getmLooseMotionFlag() {
        return mLooseMotionFlag;
    }

    public void setmLooseMotionFlag(Boolean mLooseMotionFlag) {
        this.mLooseMotionFlag = mLooseMotionFlag;
    }

    public String getmSkinProblem() {
        return mSkinProblem;
    }

    public void setmSkinProblem(String mSkinProblem) {
        this.mSkinProblem = mSkinProblem;
    }

    public String getmSucklingStatus() {
        return mSucklingStatus;
    }

    public void setmSucklingStatus(String mSucklingStatus) {
        this.mSucklingStatus = mSucklingStatus;
    }

    public Boolean getmVomitingFlag() {
        return mVomitingFlag;
    }

    public void setmVomitingFlag(Boolean mVomitingFlag) {
        this.mVomitingFlag = mVomitingFlag;
    }

    public String getMouthOpenStatus() {
        return mouthOpenStatus;
    }

    public void setMouthOpenStatus(String mouthOpenStatus) {
        this.mouthOpenStatus = mouthOpenStatus;
    }

    public Boolean getNasalFlaringFlag() {
        return nasalFlaringFlag;
    }

    public void setNasalFlaringFlag(Boolean nasalFlaringFlag) {
        this.nasalFlaringFlag = nasalFlaringFlag;
    }

    public Boolean getOralThrushFlag() {
        return oralThrushFlag;
    }

    public void setOralThrushFlag(Boolean oralThrushFlag) {
        this.oralThrushFlag = oralThrushFlag;
    }

    public Boolean getProperlyCoveredFlag() {
        return properlyCoveredFlag;
    }

    public void setProperlyCoveredFlag(Boolean properlyCoveredFlag) {
        this.properlyCoveredFlag = properlyCoveredFlag;
    }

    public Integer getRespirationRate() {
        return respirationRate;
    }

    public void setRespirationRate(Integer respirationRate) {
        this.respirationRate = respirationRate;
    }

    public Boolean getSkinPustules() {
        return skinPustules;
    }

    public void setSkinPustules(Boolean skinPustules) {
        this.skinPustules = skinPustules;
    }

    public String getSkinStatus() {
        return skinStatus;
    }

    public void setSkinStatus(String skinStatus) {
        this.skinStatus = skinStatus;
    }

    public String getUmbilicus() {
        return umbilicus;
    }

    public void setUmbilicus(String umbilicus) {
        this.umbilicus = umbilicus;
    }

    public Boolean getWearingClothesFlag() {
        return wearingClothesFlag;
    }

    public void setWearingClothesFlag(Boolean wearingClothesFlag) {
        this.wearingClothesFlag = wearingClothesFlag;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public Boolean getIsChildAlive() {
        return isChildAlive;
    }

    public void setIsChildAlive(Boolean isChildAlive) {
        this.isChildAlive = isChildAlive;
    }

    public String getWhetherBabyLimbsAndNeckMoreLimpThanBefore() {
        return whetherBabyLimbsAndNeckMoreLimpThanBefore;
    }

    public void setWhetherBabyLimbsAndNeckMoreLimpThanBefore(String whetherBabyLimbsAndNeckMoreLimpThanBefore) {
        this.whetherBabyLimbsAndNeckMoreLimpThanBefore = whetherBabyLimbsAndNeckMoreLimpThanBefore;
    }

    public String getHowAreBabyEyes() {
        return howAreBabyEyes;
    }

    public void setHowAreBabyEyes(String howAreBabyEyes) {
        this.howAreBabyEyes = howAreBabyEyes;
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
        return "ChildPNCVisitMobDataBean{" + "id=" + id + ", child=" + child + ", familyFlag=" + familyFlag + ", skinStatus=" + skinStatus + ", skinPustules=" + skinPustules + ", nasalFlaringFlag=" + nasalFlaringFlag + ", chestIndrawingFlag=" + chestIndrawingFlag + ", respirationRate=" + respirationRate + ", umbilicus=" + umbilicus + ", abdomen=" + abdomen + ", mCryStatus=" + mCryStatus + ", mBabyFed=" + mBabyFed + ", mSucklingStatus=" + mSucklingStatus + ", mFoodGiven=" + mFoodGiven + ", mVomittingFlag=" + mVomitingFlag + ", mLooseMotionFlag=" + mLooseMotionFlag + ", mConsciousnessStatus=" + mConsciousnessStatus + ", mFeverFlag=" + mFeverFlag + ", mHandsTouchFlag=" + mHandsTouchFlag + ", mChestIndrawingFlag=" + mChestIndrawingFlag + ", mSkinProblem=" + mSkinProblem + ", mBleedingStatus=" + mBleedingStatus + ", temperature=" + temperature + ", consciousnessStatus=" + consciousnessStatus + ", cryStatus=" + cryStatus + ", oralThrushFlag=" + oralThrushFlag + ", weight=" + weight + ", chinTouchStatus=" + chinTouchStatus + ", mouthOpenStatus=" + mouthOpenStatus + ", lowerLipStatus=" + lowerLipStatus + ", areolaStatus=" + areolaStatus + ", wearingClothesFlag=" + wearingClothesFlag + ", properlyCoveredFlag=" + properlyCoveredFlag + ", givenBathFlag=" + givenBathFlag + ", keptNearMotherFlag=" + keptNearMotherFlag + ", isActive=" + isActive + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", createdOn=" + createdOn + ", imageName=" + imageName + ", isInterimVisit=" + isInterimVisit + ", lastModifiedOn=" + lastModifiedOn + ", byUser=" + byUser + ", childName=" + childName + ", isChildAlive=" + isChildAlive + ", WhetherBabysLimbsAndNeckMoreLimpThanBefore=" + whetherBabyLimbsAndNeckMoreLimpThanBefore + ", HowAreBabysEyes=" + howAreBabyEyes + '}';
    }
}
