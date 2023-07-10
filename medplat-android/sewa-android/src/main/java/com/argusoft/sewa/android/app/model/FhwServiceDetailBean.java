package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by kunjan on 31/1/18.
 */
@DatabaseTable
public class FhwServiceDetailBean extends BaseEntity {

    @DatabaseField
    private Long locationId;
    @DatabaseField
    private Long familiesImportedFromEMamta;
    @DatabaseField
    private Long familiesVerifiedTillNow;
    @DatabaseField
    private Long familiesVerifiedLast3Days;
    @DatabaseField
    private Long familiesArchivedTillNow;
    @DatabaseField
    private Long newFamiliesAddedTillNow;
    @DatabaseField
    private Long totalFamiliesInIMTTillNow;
    @DatabaseField
    private Long totalNumberOfSeasonalMigrantFamilies;
    @DatabaseField
    private Long totalMembersInIMTTillNow;
    @DatabaseField
    private Long totalEligibleCouplesInTeCHO;
    @DatabaseField
    private Long totalPregnantWomenInTeCHO;
    @DatabaseField
    private Long numberOfMembersWithAadharNumberEntered;
    @DatabaseField
    private Long numberOfMembersWithMobileNumberEntered;
    @DatabaseField
    private Long under5ChildrenTillNow;
    @DatabaseField
    private Long numberOfTrueValidationsByGvkEmri;
    @DatabaseField
    private Long familiesImportedFromEMamtaExpectedValue;
    @DatabaseField
    private Long familiesVerifiedTillNowExpectedValue;
    @DatabaseField
    private Long familiesVerifiedLast3DaysExpectedValue;
    @DatabaseField
    private Long familiesArchivedTillNowExpectedValue;
    @DatabaseField
    private Long newFamiliesAddedTillNowExpectedValue;
    @DatabaseField
    private Long totalFamiliesInIMTTillNowExpectedValue;
    @DatabaseField
    private Long totalNumberOfSeasonalMigrantFamiliesExpectedValue;
    @DatabaseField
    private Long totalMembersInIMTTillNowExpectedValue;
    @DatabaseField
    private Long totalEligibleCouplesInTeCHOExpectedValue;
    @DatabaseField
    private Long totalPregnantWomenInTeCHOExpectedValue;
    @DatabaseField
    private Long numberOfMembersWithAadharNumberEnteredExpectedValue;
    @DatabaseField
    private Long numberOfMembersWithMobileNumberEnteredExpectedValue;
    @DatabaseField
    private Long under5ChildrenTillNowExpectedValue;
    @DatabaseField
    private Long numberOfTrueValidationsByGvkEmriExpectedValue;
    @DatabaseField
    private Long ncdTotalMembersScreened;
    @DatabaseField
    private Long ncdTotalMaleMembersScreened;
    @DatabaseField
    private Long ncdTotalFemaleMembersScreened;
    @DatabaseField
    private Long ncdTotalMembersScreenedForHypertension;
    @DatabaseField
    private Long ncdTotalMembersScreenedForDiabetes;
    @DatabaseField
    private Long ncdTotalMembersScreenedForOral;
    @DatabaseField
    private Long ncdTotalFemaleMembersScreenedForBreast;
    @DatabaseField
    private Long ncdTotalFemaleMembersScreenedForCervical;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getFamiliesImportedFromEMamta() {
        return familiesImportedFromEMamta;
    }

    public void setFamiliesImportedFromEMamta(Long familiesImportedFromEMamta) {
        this.familiesImportedFromEMamta = familiesImportedFromEMamta;
    }

    public Long getFamiliesVerifiedTillNow() {
        return familiesVerifiedTillNow;
    }

    public void setFamiliesVerifiedTillNow(Long familiesVerifiedTillNow) {
        this.familiesVerifiedTillNow = familiesVerifiedTillNow;
    }

    public Long getFamiliesVerifiedLast3Days() {
        return familiesVerifiedLast3Days;
    }

    public void setFamiliesVerifiedLast3Days(Long familiesVerifiedLast3Days) {
        this.familiesVerifiedLast3Days = familiesVerifiedLast3Days;
    }

    public Long getFamiliesArchivedTillNow() {
        return familiesArchivedTillNow;
    }

    public void setFamiliesArchivedTillNow(Long familiesArchivedTillNow) {
        this.familiesArchivedTillNow = familiesArchivedTillNow;
    }

    public Long getNewFamiliesAddedTillNow() {
        return newFamiliesAddedTillNow;
    }

    public void setNewFamiliesAddedTillNow(Long newFamiliesAddedTillNow) {
        this.newFamiliesAddedTillNow = newFamiliesAddedTillNow;
    }

    public Long getTotalFamiliesInIMTTillNow() {
        return totalFamiliesInIMTTillNow;
    }

    public void setTotalFamiliesInIMTTillNow(Long totalFamiliesInIMTTillNow) {
        this.totalFamiliesInIMTTillNow = totalFamiliesInIMTTillNow;
    }

    public Long getTotalNumberOfSeasonalMigrantFamilies() {
        return totalNumberOfSeasonalMigrantFamilies;
    }

    public void setTotalNumberOfSeasonalMigrantFamilies(Long totalNumberOfSeasonalMigrantFamilies) {
        this.totalNumberOfSeasonalMigrantFamilies = totalNumberOfSeasonalMigrantFamilies;
    }

    public Long getTotalMembersInIMTTillNow() {
        return totalMembersInIMTTillNow;
    }

    public void setTotalMembersInIMTTillNow(Long totalMembersInIMTTillNow) {
        this.totalMembersInIMTTillNow = totalMembersInIMTTillNow;
    }

    public Long getTotalEligibleCouplesInTeCHO() {
        return totalEligibleCouplesInTeCHO;
    }

    public void setTotalEligibleCouplesInTeCHO(Long totalEligibleCouplesInTeCHO) {
        this.totalEligibleCouplesInTeCHO = totalEligibleCouplesInTeCHO;
    }

    public Long getTotalPregnantWomenInTeCHO() {
        return totalPregnantWomenInTeCHO;
    }

    public void setTotalPregnantWomenInTeCHO(Long totalPregnantWomenInTeCHO) {
        this.totalPregnantWomenInTeCHO = totalPregnantWomenInTeCHO;
    }

    public Long getNumberOfMembersWithAadharNumberEntered() {
        return numberOfMembersWithAadharNumberEntered;
    }

    public void setNumberOfMembersWithAadharNumberEntered(Long numberOfMembersWithAadharNumberEntered) {
        this.numberOfMembersWithAadharNumberEntered = numberOfMembersWithAadharNumberEntered;
    }

    public Long getNumberOfMembersWithMobileNumberEntered() {
        return numberOfMembersWithMobileNumberEntered;
    }

    public void setNumberOfMembersWithMobileNumberEntered(Long numberOfMembersWithMobileNumberEntered) {
        this.numberOfMembersWithMobileNumberEntered = numberOfMembersWithMobileNumberEntered;
    }

    public Long getUnder5ChildrenTillNow() {
        return under5ChildrenTillNow;
    }

    public void setUnder5ChildrenTillNow(Long under5ChildrenTillNow) {
        this.under5ChildrenTillNow = under5ChildrenTillNow;
    }

    public Long getFamiliesImportedFromEMamtaExpectedValue() {
        return familiesImportedFromEMamtaExpectedValue;
    }

    public void setFamiliesImportedFromEMamtaExpectedValue(Long familiesImportedFromEMamtaExpectedValue) {
        this.familiesImportedFromEMamtaExpectedValue = familiesImportedFromEMamtaExpectedValue;
    }

    public Long getFamiliesVerifiedTillNowExpectedValue() {
        return familiesVerifiedTillNowExpectedValue;
    }

    public void setFamiliesVerifiedTillNowExpectedValue(Long familiesVerifiedTillNowExpectedValue) {
        this.familiesVerifiedTillNowExpectedValue = familiesVerifiedTillNowExpectedValue;
    }

    public Long getFamiliesVerifiedLast3DaysExpectedValue() {
        return familiesVerifiedLast3DaysExpectedValue;
    }

    public void setFamiliesVerifiedLast3DaysExpectedValue(Long familiesVerifiedLast3DaysExpectedValue) {
        this.familiesVerifiedLast3DaysExpectedValue = familiesVerifiedLast3DaysExpectedValue;
    }

    public Long getFamiliesArchivedTillNowExpectedValue() {
        return familiesArchivedTillNowExpectedValue;
    }

    public void setFamiliesArchivedTillNowExpectedValue(Long familiesArchivedTillNowExpectedValue) {
        this.familiesArchivedTillNowExpectedValue = familiesArchivedTillNowExpectedValue;
    }

    public Long getNewFamiliesAddedTillNowExpectedValue() {
        return newFamiliesAddedTillNowExpectedValue;
    }

    public void setNewFamiliesAddedTillNowExpectedValue(Long newFamiliesAddedTillNowExpectedValue) {
        this.newFamiliesAddedTillNowExpectedValue = newFamiliesAddedTillNowExpectedValue;
    }

    public Long getTotalFamiliesInIMTTillNowExpectedValue() {
        return totalFamiliesInIMTTillNowExpectedValue;
    }

    public void setTotalFamiliesInIMTTillNowExpectedValue(Long totalFamiliesInIMTTillNowExpectedValue) {
        this.totalFamiliesInIMTTillNowExpectedValue = totalFamiliesInIMTTillNowExpectedValue;
    }

    public Long getTotalNumberOfSeasonalMigrantFamiliesExpectedValue() {
        return totalNumberOfSeasonalMigrantFamiliesExpectedValue;
    }

    public void setTotalNumberOfSeasonalMigrantFamiliesExpectedValue(Long totalNumberOfSeasonalMigrantFamiliesExpectedValue) {
        this.totalNumberOfSeasonalMigrantFamiliesExpectedValue = totalNumberOfSeasonalMigrantFamiliesExpectedValue;
    }

    public Long getTotalMembersInIMTTillNowExpectedValue() {
        return totalMembersInIMTTillNowExpectedValue;
    }

    public void setTotalMembersInIMTTillNowExpectedValue(Long totalMembersInIMTTillNowExpectedValue) {
        this.totalMembersInIMTTillNowExpectedValue = totalMembersInIMTTillNowExpectedValue;
    }

    public Long getTotalEligibleCouplesInTeCHOExpectedValue() {
        return totalEligibleCouplesInTeCHOExpectedValue;
    }

    public void setTotalEligibleCouplesInTeCHOExpectedValue(Long totalEligibleCouplesInTeCHOExpectedValue) {
        this.totalEligibleCouplesInTeCHOExpectedValue = totalEligibleCouplesInTeCHOExpectedValue;
    }

    public Long getTotalPregnantWomenInTeCHOExpectedValue() {
        return totalPregnantWomenInTeCHOExpectedValue;
    }

    public void setTotalPregnantWomenInTeCHOExpectedValue(Long totalPregnantWomenInTeCHOExpectedValue) {
        this.totalPregnantWomenInTeCHOExpectedValue = totalPregnantWomenInTeCHOExpectedValue;
    }

    public Long getNumberOfMembersWithAadharNumberEnteredExpectedValue() {
        return numberOfMembersWithAadharNumberEnteredExpectedValue;
    }

    public void setNumberOfMembersWithAadharNumberEnteredExpectedValue(Long numberOfMembersWithAadharNumberEnteredExpectedValue) {
        this.numberOfMembersWithAadharNumberEnteredExpectedValue = numberOfMembersWithAadharNumberEnteredExpectedValue;
    }

    public Long getNumberOfMembersWithMobileNumberEnteredExpectedValue() {
        return numberOfMembersWithMobileNumberEnteredExpectedValue;
    }

    public void setNumberOfMembersWithMobileNumberEnteredExpectedValue(Long numberOfMembersWithMobileNumberEnteredExpectedValue) {
        this.numberOfMembersWithMobileNumberEnteredExpectedValue = numberOfMembersWithMobileNumberEnteredExpectedValue;
    }

    public Long getUnder5ChildrenTillNowExpectedValue() {
        return under5ChildrenTillNowExpectedValue;
    }

    public void setUnder5ChildrenTillNowExpectedValue(Long under5ChildrenTillNowExpectedValue) {
        this.under5ChildrenTillNowExpectedValue = under5ChildrenTillNowExpectedValue;
    }

    public Long getNumberOfTrueValidationsByGvkEmri() {
        return numberOfTrueValidationsByGvkEmri;
    }

    public void setNumberOfTrueValidationsByGvkEmri(Long numberOfTrueValidationsByGvkEmri) {
        this.numberOfTrueValidationsByGvkEmri = numberOfTrueValidationsByGvkEmri;
    }

    public Long getNumberOfTrueValidationsByGvkEmriExpectedValue() {
        return numberOfTrueValidationsByGvkEmriExpectedValue;
    }

    public void setNumberOfTrueValidationsByGvkEmriExpectedValue(Long numberOfTrueValidationsByGvkEmriExpectedValue) {
        this.numberOfTrueValidationsByGvkEmriExpectedValue = numberOfTrueValidationsByGvkEmriExpectedValue;
    }

    public Long getNcdTotalMembersScreened() {
        return ncdTotalMembersScreened;
    }

    public void setNcdTotalMembersScreened(Long ncdTotalMembersScreened) {
        this.ncdTotalMembersScreened = ncdTotalMembersScreened;
    }

    public Long getNcdTotalMaleMembersScreened() {
        return ncdTotalMaleMembersScreened;
    }

    public void setNcdTotalMaleMembersScreened(Long ncdTotalMaleMembersScreened) {
        this.ncdTotalMaleMembersScreened = ncdTotalMaleMembersScreened;
    }

    public Long getNcdTotalFemaleMembersScreened() {
        return ncdTotalFemaleMembersScreened;
    }

    public void setNcdTotalFemaleMembersScreened(Long ncdTotalFemaleMembersScreened) {
        this.ncdTotalFemaleMembersScreened = ncdTotalFemaleMembersScreened;
    }

    public Long getNcdTotalMembersScreenedForHypertension() {
        return ncdTotalMembersScreenedForHypertension;
    }

    public void setNcdTotalMembersScreenedForHypertension(Long ncdTotalMembersScreenedForHypertension) {
        this.ncdTotalMembersScreenedForHypertension = ncdTotalMembersScreenedForHypertension;
    }

    public Long getNcdTotalMembersScreenedForDiabetes() {
        return ncdTotalMembersScreenedForDiabetes;
    }

    public void setNcdTotalMembersScreenedForDiabetes(Long ncdTotalMembersScreenedForDiabetes) {
        this.ncdTotalMembersScreenedForDiabetes = ncdTotalMembersScreenedForDiabetes;
    }

    public Long getNcdTotalMembersScreenedForOral() {
        return ncdTotalMembersScreenedForOral;
    }

    public void setNcdTotalMembersScreenedForOral(Long ncdTotalMembersScreenedForOral) {
        this.ncdTotalMembersScreenedForOral = ncdTotalMembersScreenedForOral;
    }

    public Long getNcdTotalFemaleMembersScreenedForBreast() {
        return ncdTotalFemaleMembersScreenedForBreast;
    }

    public void setNcdTotalFemaleMembersScreenedForBreast(Long ncdTotalFemaleMembersScreenedForBreast) {
        this.ncdTotalFemaleMembersScreenedForBreast = ncdTotalFemaleMembersScreenedForBreast;
    }

    public Long getNcdTotalFemaleMembersScreenedForCervical() {
        return ncdTotalFemaleMembersScreenedForCervical;
    }

    public void setNcdTotalFemaleMembersScreenedForCervical(Long ncdTotalFemaleMembersScreenedForCervical) {
        this.ncdTotalFemaleMembersScreenedForCervical = ncdTotalFemaleMembersScreenedForCervical;
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
