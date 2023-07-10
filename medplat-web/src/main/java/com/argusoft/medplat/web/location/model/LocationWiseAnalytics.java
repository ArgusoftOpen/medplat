/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define location_wise_analytics entity and its fields.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
 
@Entity
@Table(name = "location_wise_analytics")
public class LocationWiseAnalytics implements Serializable {
    
    @Id
    @Column(name = "loc_id")
    private Integer locId;
    
    @Column(name = "fhs_imported_from_emamta_family")
    private Integer fhsImportedFromEmamtaFamily;
    
    @Column(name = "fhs_imported_from_emamta_member")
    private Integer fhsImportedFromEmamtaMember;
    
    @Column(name = "fhs_to_be_processed_family")
    private Integer fhsToBeProcessedFamily;
    
    @Column(name = "fhs_verified_family")
    private Integer fhsVerifiedFamily;
    
    @Column(name = "fhs_archived_family")
    private Integer fhsArchivedFamily;
    
    @Column(name = "fhs_new_family")
    private Integer fhsNewFamily;
    
    @Column(name = "fhs_total_member")
    private Integer fhsTotalMember;
    
    @Column(name = "total_male")
    private Integer totalMale;
    
    @Column(name = "total_female")
    private Integer totalFemale;
    
    @Column(name = "total_member_over_thirty")
    private Integer totalMemberOverThirty;
    
    @Column(name = "total_male_over_thirty")
    private Integer totalMaleOverThirty;
    
    @Column(name = "total_female_over_thirty")
    private Integer totalFemaleOverThirty;
    
    @Column(name = "fhs_inreverification_family")
    private Integer fhsInreverificationFamily;

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Integer getFhsImportedFromEmamtaFamily() {
        return fhsImportedFromEmamtaFamily;
    }

    public void setFhsImportedFromEmamtaFamily(Integer fhsImportedFromEmamtaFamily) {
        this.fhsImportedFromEmamtaFamily = fhsImportedFromEmamtaFamily;
    }

    public Integer getFhsImportedFromEmamtaMember() {
        return fhsImportedFromEmamtaMember;
    }

    public void setFhsImportedFromEmamtaMember(Integer fhsImportedFromEmamtaMember) {
        this.fhsImportedFromEmamtaMember = fhsImportedFromEmamtaMember;
    }

    public Integer getFhsToBeProcessedFamily() {
        return fhsToBeProcessedFamily;
    }

    public void setFhsToBeProcessedFamily(Integer fhsToBeProcessedFamily) {
        this.fhsToBeProcessedFamily = fhsToBeProcessedFamily;
    }

    public Integer getFhsVerifiedFamily() {
        return fhsVerifiedFamily;
    }

    public void setFhsVerifiedFamily(Integer fhsVerifiedFamily) {
        this.fhsVerifiedFamily = fhsVerifiedFamily;
    }

    public Integer getFhsArchivedFamily() {
        return fhsArchivedFamily;
    }

    public void setFhsArchivedFamily(Integer fhsArchivedFamily) {
        this.fhsArchivedFamily = fhsArchivedFamily;
    }

    public Integer getFhsNewFamily() {
        return fhsNewFamily;
    }

    public void setFhsNewFamily(Integer fhsNewFamily) {
        this.fhsNewFamily = fhsNewFamily;
    }

    public Integer getFhsTotalMember() {
        return fhsTotalMember;
    }

    public void setFhsTotalMember(Integer fhsTotalMember) {
        this.fhsTotalMember = fhsTotalMember;
    }

    public Integer getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(Integer totalMale) {
        this.totalMale = totalMale;
    }

    public Integer getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(Integer totalFemale) {
        this.totalFemale = totalFemale;
    }

    public Integer getTotalMemberOverThirty() {
        return totalMemberOverThirty;
    }

    public void setTotalMemberOverThirty(Integer totalMemberOverThirty) {
        this.totalMemberOverThirty = totalMemberOverThirty;
    }

    public Integer getTotalMaleOverThirty() {
        return totalMaleOverThirty;
    }

    public void setTotalMaleOverThirty(Integer totalMaleOverThirty) {
        this.totalMaleOverThirty = totalMaleOverThirty;
    }

    public Integer getTotalFemaleOverThirty() {
        return totalFemaleOverThirty;
    }

    public void setTotalFemaleOverThirty(Integer totalFemaleOverThirty) {
        this.totalFemaleOverThirty = totalFemaleOverThirty;
    }

    public Integer getFhsInreverificationFamily() {
        return fhsInreverificationFamily;
    }

    public void setFhsInreverificationFamily(Integer fhsInreverificationFamily) {
        this.fhsInreverificationFamily = fhsInreverificationFamily;
    }
        
}
