/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dto;

/**
 *
 * <p>
 *     Used for FHW service status.
 * </p>
 * @author harsh
 * @since 26/08/20 11:00 AM
 *
 */
public class FhwServiceStatusDto {

    private Integer locationId;
    private Integer familiesImportedFromEMamta;
    private Integer familiesVerifiedTillNow;
    private Integer familiesVerifiedLast3Days;
    private Integer familiesArchivedTillNow;
    private Integer newFamiliesAddedTillNow;
    private Integer totalFamiliesInIMTTillNow;
    private Integer totalNumberOfSeasonalMigrantFamilies;
    private Integer totalMembersInIMTTillNow;
    private Integer totalEligibleCouplesInTeCHO;
    private Integer totalPregnantWomenInTeCHO;
    private Integer numberOfMembersWithAadharNumberEntered;
    private Integer numberOfMembersWithMobileNumberEntered;
    private Integer under5ChildrenTillNow;
    private Integer numberOfTrueValidationsByGvkEmri;
    private Integer familiesImportedFromEMamtaExpectedValue;
    private Integer familiesVerifiedTillNowExpectedValue;
    private Integer familiesVerifiedLast3DaysExpectedValue;
    private Integer familiesArchivedTillNowExpectedValue;
    private Integer newFamiliesAddedTillNowExpectedValue;
    private Integer totalFamiliesInIMTTillNowExpectedValue;
    private Integer totalNumberOfSeasonalMigrantFamiliesExpectedValue;
    private Integer totalMembersInIMTTillNowExpectedValue;
    private Integer totalEligibleCouplesInTeCHOExpectedValue;
    private Integer totalPregnantWomenInTeCHOExpectedValue;
    private Integer numberOfMembersWithAadharNumberEnteredExpectedValue;
    private Integer numberOfMembersWithMobileNumberEnteredExpectedValue;
    private Integer under5ChildrenTillNowExpectedValue;
    private Integer numberOfTrueValidationsByGvkEmriExpectedValue;
    private Integer ncdTotalMembersScreened;
    private Integer ncdTotalMaleMembersScreened;
    private Integer ncdTotalFemaleMembersScreened;
    private Integer ncdTotalMembersScreenedForHypertension;
    private Integer ncdTotalMembersScreenedForDiabetes;
    private Integer ncdTotalMembersScreenedForOral;
    private Integer ncdTotalFemaleMembersScreenedForBreast;
    private Integer ncdTotalFemaleMembersScreenedForCervical;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getFamiliesImportedFromEMamta() {
        return familiesImportedFromEMamta;
    }

    public void setFamiliesImportedFromEMamta(Integer familiesImportedFromEMamta) {
        this.familiesImportedFromEMamta = familiesImportedFromEMamta;
    }

    public Integer getFamiliesVerifiedTillNow() {
        return familiesVerifiedTillNow;
    }

    public void setFamiliesVerifiedTillNow(Integer familiesVerifiedTillNow) {
        this.familiesVerifiedTillNow = familiesVerifiedTillNow;
    }

    public Integer getFamiliesVerifiedLast3Days() {
        return familiesVerifiedLast3Days;
    }

    public void setFamiliesVerifiedLast3Days(Integer familiesVerifiedLast3Days) {
        this.familiesVerifiedLast3Days = familiesVerifiedLast3Days;
    }

    public Integer getFamiliesArchivedTillNow() {
        return familiesArchivedTillNow;
    }

    public void setFamiliesArchivedTillNow(Integer familiesArchivedTillNow) {
        this.familiesArchivedTillNow = familiesArchivedTillNow;
    }

    public Integer getNewFamiliesAddedTillNow() {
        return newFamiliesAddedTillNow;
    }

    public void setNewFamiliesAddedTillNow(Integer newFamiliesAddedTillNow) {
        this.newFamiliesAddedTillNow = newFamiliesAddedTillNow;
    }

    public Integer getTotalFamiliesInIMTTillNow() {
        return totalFamiliesInIMTTillNow;
    }

    public void setTotalFamiliesInIMTTillNow(Integer totalFamiliesInIMTTillNow) {
        this.totalFamiliesInIMTTillNow = totalFamiliesInIMTTillNow;
    }

    public Integer getTotalNumberOfSeasonalMigrantFamilies() {
        return totalNumberOfSeasonalMigrantFamilies;
    }

    public void setTotalNumberOfSeasonalMigrantFamilies(Integer totalNumberOfSeasonalMigrantFamilies) {
        this.totalNumberOfSeasonalMigrantFamilies = totalNumberOfSeasonalMigrantFamilies;
    }

    public Integer getTotalMembersInIMTTillNow() {
        return totalMembersInIMTTillNow;
    }

    public void setTotalMembersInIMTTillNow(Integer totalMembersInIMTTillNow) {
        this.totalMembersInIMTTillNow = totalMembersInIMTTillNow;
    }

    public Integer getTotalEligibleCouplesInTeCHO() {
        return totalEligibleCouplesInTeCHO;
    }

    public void setTotalEligibleCouplesInTeCHO(Integer totalEligibleCouplesInTeCHO) {
        this.totalEligibleCouplesInTeCHO = totalEligibleCouplesInTeCHO;
    }

    public Integer getTotalPregnantWomenInTeCHO() {
        return totalPregnantWomenInTeCHO;
    }

    public void setTotalPregnantWomenInTeCHO(Integer totalPregnantWomenInTeCHO) {
        this.totalPregnantWomenInTeCHO = totalPregnantWomenInTeCHO;
    }

    public Integer getNumberOfMembersWithAadharNumberEntered() {
        return numberOfMembersWithAadharNumberEntered;
    }

    public void setNumberOfMembersWithAadharNumberEntered(Integer numberOfMembersWithAadharNumberEntered) {
        this.numberOfMembersWithAadharNumberEntered = numberOfMembersWithAadharNumberEntered;
    }

    public Integer getNumberOfMembersWithMobileNumberEntered() {
        return numberOfMembersWithMobileNumberEntered;
    }

    public void setNumberOfMembersWithMobileNumberEntered(Integer numberOfMembersWithMobileNumberEntered) {
        this.numberOfMembersWithMobileNumberEntered = numberOfMembersWithMobileNumberEntered;
    }

    public Integer getUnder5ChildrenTillNow() {
        return under5ChildrenTillNow;
    }

    public void setUnder5ChildrenTillNow(Integer under5ChildrenTillNow) {
        this.under5ChildrenTillNow = under5ChildrenTillNow;
    }

    public Integer getNumberOfTrueValidationsByGvkEmri() {
        return numberOfTrueValidationsByGvkEmri;
    }

    public void setNumberOfTrueValidationsByGvkEmri(Integer numberOfTrueValidationsByGvkEmri) {
        this.numberOfTrueValidationsByGvkEmri = numberOfTrueValidationsByGvkEmri;
    }

    public Integer getFamiliesImportedFromEMamtaExpectedValue() {
        return familiesImportedFromEMamtaExpectedValue;
    }

    public void setFamiliesImportedFromEMamtaExpectedValue(Integer familiesImportedFromEMamtaExpectedValue) {
        this.familiesImportedFromEMamtaExpectedValue = familiesImportedFromEMamtaExpectedValue;
    }

    public Integer getFamiliesVerifiedTillNowExpectedValue() {
        return familiesVerifiedTillNowExpectedValue;
    }

    public void setFamiliesVerifiedTillNowExpectedValue(Integer familiesVerifiedTillNowExpectedValue) {
        this.familiesVerifiedTillNowExpectedValue = familiesVerifiedTillNowExpectedValue;
    }

    public Integer getFamiliesVerifiedLast3DaysExpectedValue() {
        return familiesVerifiedLast3DaysExpectedValue;
    }

    public void setFamiliesVerifiedLast3DaysExpectedValue(Integer familiesVerifiedLast3DaysExpectedValue) {
        this.familiesVerifiedLast3DaysExpectedValue = familiesVerifiedLast3DaysExpectedValue;
    }

    public Integer getFamiliesArchivedTillNowExpectedValue() {
        return familiesArchivedTillNowExpectedValue;
    }

    public void setFamiliesArchivedTillNowExpectedValue(Integer familiesArchivedTillNowExpectedValue) {
        this.familiesArchivedTillNowExpectedValue = familiesArchivedTillNowExpectedValue;
    }

    public Integer getNewFamiliesAddedTillNowExpectedValue() {
        return newFamiliesAddedTillNowExpectedValue;
    }

    public void setNewFamiliesAddedTillNowExpectedValue(Integer newFamiliesAddedTillNowExpectedValue) {
        this.newFamiliesAddedTillNowExpectedValue = newFamiliesAddedTillNowExpectedValue;
    }

    public Integer getTotalFamiliesInIMTTillNowExpectedValue() {
        return totalFamiliesInIMTTillNowExpectedValue;
    }

    public void setTotalFamiliesInIMTTillNowExpectedValue(Integer totalFamiliesInIMTTillNowExpectedValue) {
        this.totalFamiliesInIMTTillNowExpectedValue = totalFamiliesInIMTTillNowExpectedValue;
    }

    public Integer getTotalNumberOfSeasonalMigrantFamiliesExpectedValue() {
        return totalNumberOfSeasonalMigrantFamiliesExpectedValue;
    }

    public void setTotalNumberOfSeasonalMigrantFamiliesExpectedValue(Integer totalNumberOfSeasonalMigrantFamiliesExpectedValue) {
        this.totalNumberOfSeasonalMigrantFamiliesExpectedValue = totalNumberOfSeasonalMigrantFamiliesExpectedValue;
    }

    public Integer getTotalMembersInIMTTillNowExpectedValue() {
        return totalMembersInIMTTillNowExpectedValue;
    }

    public void setTotalMembersInIMTTillNowExpectedValue(Integer totalMembersInIMTTillNowExpectedValue) {
        this.totalMembersInIMTTillNowExpectedValue = totalMembersInIMTTillNowExpectedValue;
    }

    public Integer getTotalEligibleCouplesInTeCHOExpectedValue() {
        return totalEligibleCouplesInTeCHOExpectedValue;
    }

    public void setTotalEligibleCouplesInTeCHOExpectedValue(Integer totalEligibleCouplesInTeCHOExpectedValue) {
        this.totalEligibleCouplesInTeCHOExpectedValue = totalEligibleCouplesInTeCHOExpectedValue;
    }

    public Integer getTotalPregnantWomenInTeCHOExpectedValue() {
        return totalPregnantWomenInTeCHOExpectedValue;
    }

    public void setTotalPregnantWomenInTeCHOExpectedValue(Integer totalPregnantWomenInTeCHOExpectedValue) {
        this.totalPregnantWomenInTeCHOExpectedValue = totalPregnantWomenInTeCHOExpectedValue;
    }

    public Integer getNumberOfMembersWithAadharNumberEnteredExpectedValue() {
        return numberOfMembersWithAadharNumberEnteredExpectedValue;
    }

    public void setNumberOfMembersWithAadharNumberEnteredExpectedValue(Integer numberOfMembersWithAadharNumberEnteredExpectedValue) {
        this.numberOfMembersWithAadharNumberEnteredExpectedValue = numberOfMembersWithAadharNumberEnteredExpectedValue;
    }

    public Integer getNumberOfMembersWithMobileNumberEnteredExpectedValue() {
        return numberOfMembersWithMobileNumberEnteredExpectedValue;
    }

    public void setNumberOfMembersWithMobileNumberEnteredExpectedValue(Integer numberOfMembersWithMobileNumberEnteredExpectedValue) {
        this.numberOfMembersWithMobileNumberEnteredExpectedValue = numberOfMembersWithMobileNumberEnteredExpectedValue;
    }

    public Integer getUnder5ChildrenTillNowExpectedValue() {
        return under5ChildrenTillNowExpectedValue;
    }

    public void setUnder5ChildrenTillNowExpectedValue(Integer under5ChildrenTillNowExpectedValue) {
        this.under5ChildrenTillNowExpectedValue = under5ChildrenTillNowExpectedValue;
    }

    public Integer getNumberOfTrueValidationsByGvkEmriExpectedValue() {
        return numberOfTrueValidationsByGvkEmriExpectedValue;
    }

    public void setNumberOfTrueValidationsByGvkEmriExpectedValue(Integer numberOfTrueValidationsByGvkEmriExpectedValue) {
        this.numberOfTrueValidationsByGvkEmriExpectedValue = numberOfTrueValidationsByGvkEmriExpectedValue;
    }

    public Integer getNcdTotalMembersScreened() {
        return ncdTotalMembersScreened;
    }

    public void setNcdTotalMembersScreened(Integer ncdTotalMembersScreened) {
        this.ncdTotalMembersScreened = ncdTotalMembersScreened;
    }

    public Integer getNcdTotalMaleMembersScreened() {
        return ncdTotalMaleMembersScreened;
    }

    public void setNcdTotalMaleMembersScreened(Integer ncdTotalMaleMembersScreened) {
        this.ncdTotalMaleMembersScreened = ncdTotalMaleMembersScreened;
    }

    public Integer getNcdTotalFemaleMembersScreened() {
        return ncdTotalFemaleMembersScreened;
    }

    public void setNcdTotalFemaleMembersScreened(Integer ncdTotalFemaleMembersScreened) {
        this.ncdTotalFemaleMembersScreened = ncdTotalFemaleMembersScreened;
    }

    public Integer getNcdTotalMembersScreenedForHypertension() {
        return ncdTotalMembersScreenedForHypertension;
    }

    public void setNcdTotalMembersScreenedForHypertension(Integer ncdTotalMembersScreenedForHypertension) {
        this.ncdTotalMembersScreenedForHypertension = ncdTotalMembersScreenedForHypertension;
    }

    public Integer getNcdTotalMembersScreenedForDiabetes() {
        return ncdTotalMembersScreenedForDiabetes;
    }

    public void setNcdTotalMembersScreenedForDiabetes(Integer ncdTotalMembersScreenedForDiabetes) {
        this.ncdTotalMembersScreenedForDiabetes = ncdTotalMembersScreenedForDiabetes;
    }

    public Integer getNcdTotalMembersScreenedForOral() {
        return ncdTotalMembersScreenedForOral;
    }

    public void setNcdTotalMembersScreenedForOral(Integer ncdTotalMembersScreenedForOral) {
        this.ncdTotalMembersScreenedForOral = ncdTotalMembersScreenedForOral;
    }

    public Integer getNcdTotalFemaleMembersScreenedForBreast() {
        return ncdTotalFemaleMembersScreenedForBreast;
    }

    public void setNcdTotalFemaleMembersScreenedForBreast(Integer ncdTotalFemaleMembersScreenedForBreast) {
        this.ncdTotalFemaleMembersScreenedForBreast = ncdTotalFemaleMembersScreenedForBreast;
    }

    public Integer getNcdTotalFemaleMembersScreenedForCervical() {
        return ncdTotalFemaleMembersScreenedForCervical;
    }

    public void setNcdTotalFemaleMembersScreenedForCervical(Integer ncdTotalFemaleMembersScreenedForCervical) {
        this.ncdTotalFemaleMembersScreenedForCervical = ncdTotalFemaleMembersScreenedForCervical;
    }

    @Override
    public String toString() {
        return "FhwServiceStatusDto{" + "locationId=" + locationId + ", familiesImportedFromEMamta=" + familiesImportedFromEMamta + ", familiesVerifiedTillNow=" + familiesVerifiedTillNow + ", familiesVerifiedLast3Days=" + familiesVerifiedLast3Days + ", familiesArchivedTillNow=" + familiesArchivedTillNow + ", newFamiliesAddedTillNow=" + newFamiliesAddedTillNow + ", totalFamiliesInIMTTillNow=" + totalFamiliesInIMTTillNow + ", totalNumberOfSeasonalMigrantFamilies=" + totalNumberOfSeasonalMigrantFamilies + ", totalMembersInIMTTillNow=" + totalMembersInIMTTillNow + ", totalEligibleCouplesInTeCHO=" + totalEligibleCouplesInTeCHO + ", totalPregnantWomenInTeCHO=" + totalPregnantWomenInTeCHO + ", numberOfMembersWithAadharNumberEntered=" + numberOfMembersWithAadharNumberEntered + ", numberOfMembersWithMobileNumberEntered=" + numberOfMembersWithMobileNumberEntered + ", under5ChildrenTillNow=" + under5ChildrenTillNow + ", numberOfTrueValidationsByGvkEmri=" + numberOfTrueValidationsByGvkEmri + ", familiesImportedFromEMamtaExpectedValue=" + familiesImportedFromEMamtaExpectedValue + ", familiesVerifiedTillNowExpectedValue=" + familiesVerifiedTillNowExpectedValue + ", familiesVerifiedLast3DaysExpectedValue=" + familiesVerifiedLast3DaysExpectedValue + ", familiesArchivedTillNowExpectedValue=" + familiesArchivedTillNowExpectedValue + ", newFamiliesAddedTillNowExpectedValue=" + newFamiliesAddedTillNowExpectedValue + ", totalFamiliesInIMTTillNowExpectedValue=" + totalFamiliesInIMTTillNowExpectedValue + ", totalNumberOfSeasonalMigrantFamiliesExpectedValue=" + totalNumberOfSeasonalMigrantFamiliesExpectedValue + ", totalMembersInIMTTillNowExpectedValue=" + totalMembersInIMTTillNowExpectedValue + ", totalEligibleCouplesInTeCHOExpectedValue=" + totalEligibleCouplesInTeCHOExpectedValue + ", totalPregnantWomenInTeCHOExpectedValue=" + totalPregnantWomenInTeCHOExpectedValue + ", numberOfMembersWithAadharNumberEnteredExpectedValue=" + numberOfMembersWithAadharNumberEnteredExpectedValue + ", numberOfMembersWithMobileNumberEnteredExpectedValue=" + numberOfMembersWithMobileNumberEnteredExpectedValue + ", under5ChildrenTillNowExpectedValue=" + under5ChildrenTillNowExpectedValue + ", numberOfTrueValidationsByGvkEmriExpectedValue=" + numberOfTrueValidationsByGvkEmriExpectedValue + '}';
    }

}
