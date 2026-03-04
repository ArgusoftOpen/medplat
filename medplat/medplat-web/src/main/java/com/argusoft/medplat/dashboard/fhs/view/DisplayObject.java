/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.fhs.view;

import java.util.List;

/**
 * <p>Display object</p>
 *
 * @author prateek
 * @since 27/08/20 1:00 PM
 */
public class DisplayObject {

    private String id;
    private String value;
    private Integer longValue;
    private Integer counter;
    private Integer newFamily;
    private Integer unverifiedFHS;
    private Integer orphanFHS;
    private Integer verifiedFHS;
    private Integer archivedFHS;
    private Integer emriVerifiedOK;
    private Integer emriVerifiedEdited;
    private String locationType;
    private String personName;
    private List<Integer> personIds;
    private String locationName;
    private String mobileNumber;
    private String fhwName;
    private Integer importedFromEmamta;
    private Integer totalMember;
    private Integer worker;
    private Integer dayDiff;
    private Integer inReverification;
    private Integer totalFamily;
    private Integer importedFromEmamtaMember;

    public DisplayObject() {
    }

    public DisplayObject(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public DisplayObject(String id, Integer longValue) {
        this.id = id;
        this.longValue = longValue;
    }

    public DisplayObject(String id, String value, Integer counter) {
        this.id = id;
        this.value = value;
        this.counter = counter;
    }

    public DisplayObject(String id, String value, Integer counter, String locationType, List<Integer> personIds) {
        this.id = id;
        this.value = value;
        this.counter = counter;
        this.locationType = locationType;
        this.personIds = personIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLongValue() {
        return longValue;
    }

    public void setLongValue(Integer longValue) {
        this.longValue = longValue;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getNewFamily() {
        return newFamily;
    }

    public void setNewFamily(Integer newFamily) {
        this.newFamily = newFamily;
    }

    public Integer getUnverifiedFHS() {
        return unverifiedFHS;
    }

    public void setUnverifiedFHS(Integer unverifiedFHS) {
        this.unverifiedFHS = unverifiedFHS;
    }

    public Integer getOrphanFHS() {
        return orphanFHS;
    }

    public void setOrphanFHS(Integer orphanFHS) {
        this.orphanFHS = orphanFHS;
    }

    public Integer getVerifiedFHS() {
        return verifiedFHS;
    }

    public void setVerifiedFHS(Integer verifiedFHS) {
        this.verifiedFHS = verifiedFHS;
    }

    public Integer getArchivedFHS() {
        return archivedFHS;
    }

    public void setArchivedFHS(Integer archivedFHS) {
        this.archivedFHS = archivedFHS;
    }

    public Integer getEmriVerifiedOK() {
        return emriVerifiedOK;
    }

    public void setEmriVerifiedOK(Integer emriVerifiedOK) {
        this.emriVerifiedOK = emriVerifiedOK;
    }

    public Integer getEmriVerifiedEdited() {
        return emriVerifiedEdited;
    }

    public void setEmriVerifiedEdited(Integer emriVerifiedEdited) {
        this.emriVerifiedEdited = emriVerifiedEdited;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public List<Integer> getPersonIds() {
        return personIds;
    }

    public void setPersonIds(List<Integer> personIds) {
        this.personIds = personIds;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFhwName() {
        return fhwName;
    }

    public void setFhwName(String fhwName) {
        this.fhwName = fhwName;
    }

    public Integer getImportedFromEmamta() {
        return importedFromEmamta;
    }

    public void setImportedFromEmamta(Integer importedFromEmamta) {
        this.importedFromEmamta = importedFromEmamta;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Integer getWorker() {
        return worker;
    }

    public void setWorker(Integer worker) {
        this.worker = worker;
    }

    public Integer getDayDiff() {
        return dayDiff;
    }

    public void setDayDiff(Integer dayDiff) {
        this.dayDiff = dayDiff;
    }

    public Integer getInReverification() {
        return inReverification;
    }

    public void setInReverification(Integer inReverification) {
        this.inReverification = inReverification;
    }

    public Integer getTotalFamily() {
        return totalFamily;
    }

    public void setTotalFamily(Integer totalFamily) {
        this.totalFamily = totalFamily;
    }

    public Integer getImportedFromEmamtaMember() {
        return importedFromEmamtaMember;
    }

    public void setImportedFromEmamtaMember(Integer importedFromEmamtaMember) {
        this.importedFromEmamtaMember = importedFromEmamtaMember;
    }

    @Override
    public String toString() {
        return "DisplayObject{" + "id=" + id + ", value=" + value + ", longValue=" + longValue + ", counter=" + counter + ", newFamily=" + newFamily + ", unverifiedFHS=" + unverifiedFHS + ", orphanFHS=" + orphanFHS + ", verifiedFHS=" + verifiedFHS + ", archivedFHS=" + archivedFHS + ", emriVerifiedOK=" + emriVerifiedOK + ", emriVerifiedEdited=" + emriVerifiedEdited + ", locationType=" + locationType + ", personName=" + personName + ", personIds=" + personIds + ", locationName=" + locationName + ", mobileNumber=" + mobileNumber + ", fhwName=" + fhwName + ", importedFromEmamta=" + importedFromEmamta + ", totalMember=" + totalMember + ", worker=" + worker + ", dayDiff=" + dayDiff + ", inReverification=" + inReverification + ", totalFamily=" + totalFamily + '}';
    }

}
