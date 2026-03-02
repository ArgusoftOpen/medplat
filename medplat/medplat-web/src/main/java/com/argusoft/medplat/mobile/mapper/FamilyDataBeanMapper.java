/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.mapper;

import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.fhs.dto.FamilyAdditionalInfo;
import com.argusoft.medplat.fhs.dto.FamilyDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.mobile.dto.FamilyDataBean;
import com.argusoft.medplat.mobile.dto.MemberDataBean;
import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

/**
 * @author prateek
 */
public class FamilyDataBeanMapper {

    private FamilyDataBeanMapper() {
        throw new IllegalStateException("Utility Class");
    }

    public static FamilyDataBean convertFamilyEntityToFamilyDataBean(FamilyEntity family, List<MemberDataBean> members) {
        FamilyDataBean familyDataBean = new FamilyDataBean();
        familyDataBean.setId(family.getId());
        familyDataBean.setFamilyId(family.getFamilyId());
        familyDataBean.setHouseNumber(family.getHouseNumber());
        familyDataBean.setLocationId(family.getLocationId());
        familyDataBean.setAddress1(family.getAddress1());
        familyDataBean.setAddress2(family.getAddress2());
        familyDataBean.setReligion(family.getReligion());
        familyDataBean.setCaste(family.getCaste());
        familyDataBean.setBplFlag(family.getBplFlag());
        if (family.getAnganwadiId() != null) {
            familyDataBean.setAnganwadiId(family.getAnganwadiId().toString());
        }
        familyDataBean.setToiletAvailableFlag(family.getToiletAvailableFlag());
        familyDataBean.setIsVerifiedFlag(family.getIsVerifiedFlag());
        familyDataBean.setMembers(members);
        familyDataBean.setAssignedTo(family.getAssignedTo());
        familyDataBean.setCreatedBy(family.getCreatedBy());
        familyDataBean.setCreatedOn(family.getCreatedOn());
        familyDataBean.setUpdatedBy(family.getModifiedBy());
        familyDataBean.setUpdatedOn(family.getModifiedOn());
        familyDataBean.setState(family.getState().equals(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN) ? FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED : family.getState());
        familyDataBean.setMaaVatsalyaNumber(family.getMaaVatsalyaNumber());
        familyDataBean.setRsbyCardNumber(family.getRsbyCardNumber());
        familyDataBean.setComment(family.getComment());
        familyDataBean.setVulnerableFlag(family.getVulnerableFlag());
        familyDataBean.setSeasonalMigrantFlag(family.getMigratoryFlag());
        if (family.getAreaId() != null) {
            familyDataBean.setAreaId(family.getAreaId().toString());
        }
        familyDataBean.setContactPersonId(family.getContactPersonId());
        familyDataBean.setAnganwadiUpdateFlag(family.getAnganwadiUpdateFlag());
        familyDataBean.setAnyMemberCbacDone(family.getAnyMemberCbacDone());
        familyDataBean.setTypeOfHouse(family.getTypeOfHouse());
        familyDataBean.setTypeOfToilet(family.getTypeOfToilet());
        familyDataBean.setElectricityAvailability(family.getElectricityAvailability());
        familyDataBean.setDrinkingWaterSource(family.getDrinkingWaterSource());
        familyDataBean.setFuelForCooking(family.getFuelForCooking());
        familyDataBean.setVehicleDetails(convertStringSetToCommaSeparatedString(family.getVehicleDetails(), ","));
        familyDataBean.setHouseOwnershipStatus(family.getHouseOwnershipStatus());
        familyDataBean.setRationCardNumber(family.getRationCardNumber());
        familyDataBean.setAnnualIncome(family.getAnnualIncome());
        familyDataBean.setBplCardNumber(family.getBplCardNumber());
        familyDataBean.setProvidingConsent(family.getProvidingConsent());
        familyDataBean.setVulnerabilityCriteria(family.getVulnerabilityCriteria());
        familyDataBean.setEligibleForPmjay(family.getEligibleForPmjay());

        Gson gson = new Gson();
        FamilyAdditionalInfo additionalInfo;
        if (family.getAdditionalInfo() != null) {
            additionalInfo = gson.fromJson(family.getAdditionalInfo(), FamilyAdditionalInfo.class);
        } else {
            additionalInfo = new FamilyAdditionalInfo();
        }

        if (additionalInfo.getLastFhsDate() != null) {
            familyDataBean.setLastFhsDate(additionalInfo.getLastFhsDate());
        }
        if (additionalInfo.getLastMemberNcdScreeningDate() != null) {
            familyDataBean.setLastMemberNcdScreeningDate(additionalInfo.getLastMemberNcdScreeningDate());
        }
        if (additionalInfo.getLastIdspScreeningDate() != null) {
            familyDataBean.setLastIdspScreeningDate(additionalInfo.getLastIdspScreeningDate());
        }
        if (additionalInfo.getLastIdsp2ScreeningDate() != null) {
            familyDataBean.setLastIdsp2ScreeningDate(additionalInfo.getLastIdsp2ScreeningDate());
        }
        return familyDataBean;
    }

    public static FamilyDataBean convertFamilyDtoToFamilyDataBean(FamilyDto family, List<MemberDataBean> members) {
        FamilyDataBean familyDataBean = new FamilyDataBean();
        familyDataBean.setId(family.getId());
        familyDataBean.setFamilyId(family.getFamilyId());
        familyDataBean.setHouseNumber(family.getHouseNumber());
        familyDataBean.setLocationId(family.getLocationId());
        familyDataBean.setAddress1(family.getAddress1());
        familyDataBean.setAddress2(family.getAddress2());
        familyDataBean.setReligion(family.getReligion());
        familyDataBean.setCaste(family.getCaste());
        familyDataBean.setBplFlag(family.getBplFlag());
        if (family.getAnganwadiId() != null) {
            familyDataBean.setAnganwadiId(family.getAnganwadiId().toString());
        }
        familyDataBean.setToiletAvailableFlag(family.getToiletAvailableFlag());
        familyDataBean.setIsVerifiedFlag(family.getIsVerifiedFlag());
        familyDataBean.setMembers(members);
        familyDataBean.setAssignedTo(family.getAssignedTo());
        familyDataBean.setCreatedBy(family.getCreatedBy());
        familyDataBean.setCreatedOn(family.getCreatedOn());
        familyDataBean.setUpdatedBy(family.getModifiedBy());
        familyDataBean.setUpdatedOn(family.getModifiedOn());
        familyDataBean.setState(family.getState());
        familyDataBean.setMaaVatsalyaNumber(family.getMaaVatsalyaNumber());
        familyDataBean.setRsbyCardNumber(family.getRsbyCardNumber());
        familyDataBean.setComment(family.getComment());
        familyDataBean.setVulnerableFlag(family.getVulnerableFlag());
        familyDataBean.setSeasonalMigrantFlag(family.getMigratoryFlag());
        familyDataBean.setAreaId(family.getAreaId().toString());
        return familyDataBean;
    }

    public static String convertStringSetToCommaSeparatedString(Set<String> list, String seperator) {
        if (list != null && !list.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String item : list) {
                if (first) {
                    first = false;
                } else {
                    sb.append(seperator);
                }
                sb.append(item);
            }
            return sb.toString();
        } else {
            return null;
        }
    }
}
