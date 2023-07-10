
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcAdmissionDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcAdmission;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) admission to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcAdmissionMapper {

    private ChildCmtcNrcAdmissionMapper(){
    }

    /**
     * Converts child cmtc nrc admission dto to modal
     * @param childCmtcNrcAdmissionDto An instance of QueryHistoryDto
     * @return An instance of QueryHistory
     */
    public static ChildCmtcNrcAdmission convertDtoToEntity(ChildCmtcNrcAdmissionDto childCmtcNrcAdmissionDto) {
        ChildCmtcNrcAdmission childCmtcNrcAdmission = new ChildCmtcNrcAdmission();
        if (childCmtcNrcAdmissionDto.getId() != null) {
            childCmtcNrcAdmission.setId(childCmtcNrcAdmissionDto.getId());
        }
        childCmtcNrcAdmission.setChildId(childCmtcNrcAdmissionDto.getChildId());
        childCmtcNrcAdmission.setState(childCmtcNrcAdmissionDto.getState());
        childCmtcNrcAdmission.setAdmissionDate(childCmtcNrcAdmissionDto.getAdmissionDate());
        childCmtcNrcAdmission.setApetiteTest(childCmtcNrcAdmissionDto.getApetiteTest());
        childCmtcNrcAdmission.setBilateralPittingOedema(childCmtcNrcAdmissionDto.getBilateralPittingOedema());
        childCmtcNrcAdmission.setHeight(childCmtcNrcAdmissionDto.getHeight());
        childCmtcNrcAdmission.setIllness(childCmtcNrcAdmissionDto.getIllness());
        childCmtcNrcAdmission.setOtherIllness(childCmtcNrcAdmissionDto.getOtherIllness());
        childCmtcNrcAdmission.setMedicalOfficerVisit(childCmtcNrcAdmissionDto.getMedicalOfficerVisit());
        childCmtcNrcAdmission.setMidUpperArmCircumference(childCmtcNrcAdmissionDto.getMidUpperArmCircumference());
        childCmtcNrcAdmission.setReferredBy(childCmtcNrcAdmissionDto.getReferredBy());
        childCmtcNrcAdmission.setSdScore(childCmtcNrcAdmissionDto.getSdScore());
        childCmtcNrcAdmission.setSpecialistPediatricianVisit(childCmtcNrcAdmissionDto.getSpecialistPediatricianVisit());
        childCmtcNrcAdmission.setTypeOfAdmission(childCmtcNrcAdmissionDto.getTypeOfAdmission());
        childCmtcNrcAdmission.setWeightAtAdmission(childCmtcNrcAdmissionDto.getWeightAtAdmission());
        childCmtcNrcAdmission.setDeathDate(childCmtcNrcAdmissionDto.getDeathDate());
        childCmtcNrcAdmission.setDeathReason(childCmtcNrcAdmissionDto.getDeathReason());
        childCmtcNrcAdmission.setDeathPlace(childCmtcNrcAdmissionDto.getDeathPlace());
        childCmtcNrcAdmission.setOtherDeathPlace(childCmtcNrcAdmissionDto.getOtherDeathPlace());
        childCmtcNrcAdmission.setOtherDeathReason(childCmtcNrcAdmissionDto.getOtherDeathReason());
        childCmtcNrcAdmission.setDefaulterDate(childCmtcNrcAdmissionDto.getDefaulterDate());
        childCmtcNrcAdmission.setScreeningCenter(childCmtcNrcAdmissionDto.getScreeningCenter());
        childCmtcNrcAdmission.setComplementaryFeeding(childCmtcNrcAdmissionDto.getComplementaryFeeding());
        childCmtcNrcAdmission.setBreastFeeding(childCmtcNrcAdmissionDto.getBreastFeeding());
        childCmtcNrcAdmission.setCaseId(childCmtcNrcAdmissionDto.getCaseId());
        childCmtcNrcAdmission.setProblemInBreastFeeding(childCmtcNrcAdmissionDto.getProblemInBreastFeeding());
        childCmtcNrcAdmission.setProblemInMilkInjection(childCmtcNrcAdmissionDto.getProblemInMilkInjection());
        childCmtcNrcAdmission.setVisibleWasting(childCmtcNrcAdmissionDto.getVisibleWasting());
        childCmtcNrcAdmission.setKmcProvided(childCmtcNrcAdmissionDto.getKmcProvided());
        childCmtcNrcAdmission.setNoOfTimesKmcDone(childCmtcNrcAdmissionDto.getNoOfTimesKmcDone());
        childCmtcNrcAdmission.setNoOfTimesAmoxicillinGiven(childCmtcNrcAdmissionDto.getNoOfTimesAmoxicillinGiven());
        childCmtcNrcAdmission.setConsecutive3DaysWeightGain(childCmtcNrcAdmissionDto.getConsecutive3DaysWeightGain());
        return childCmtcNrcAdmission;
    }

    /**
     * Converts child cmtc nrc admission modal to dto
     * @param childCmtcNrcAdmission An instance of ChildCmtcNrcAdmission
     * @return An instance of ChildCmtcNrcAdmissionDto
     */
    public static ChildCmtcNrcAdmissionDto convertEntityToDto(ChildCmtcNrcAdmission childCmtcNrcAdmission) {
        ChildCmtcNrcAdmissionDto childCmtcNrcAdmissionDto = new ChildCmtcNrcAdmissionDto();
        childCmtcNrcAdmissionDto.setChildId(childCmtcNrcAdmission.getChildId());
        childCmtcNrcAdmissionDto.setState(childCmtcNrcAdmission.getState());
        childCmtcNrcAdmissionDto.setAdmissionDate(childCmtcNrcAdmission.getAdmissionDate());
        childCmtcNrcAdmissionDto.setApetiteTest(childCmtcNrcAdmission.getApetiteTest());
        childCmtcNrcAdmissionDto.setBilateralPittingOedema(childCmtcNrcAdmission.getBilateralPittingOedema());
        childCmtcNrcAdmissionDto.setHeight(childCmtcNrcAdmission.getHeight());
        childCmtcNrcAdmissionDto.setIllness(childCmtcNrcAdmission.getIllness());
        childCmtcNrcAdmissionDto.setOtherIllness(childCmtcNrcAdmission.getOtherIllness());
        childCmtcNrcAdmissionDto.setMedicalOfficerVisit(childCmtcNrcAdmission.getMedicalOfficerVisit());
        childCmtcNrcAdmissionDto.setMidUpperArmCircumference(childCmtcNrcAdmission.getMidUpperArmCircumference());
        childCmtcNrcAdmissionDto.setReferredBy(childCmtcNrcAdmission.getReferredBy());
        childCmtcNrcAdmissionDto.setSdScore(childCmtcNrcAdmission.getSdScore());
        childCmtcNrcAdmissionDto.setSpecialistPediatricianVisit(childCmtcNrcAdmission.getSpecialistPediatricianVisit());
        childCmtcNrcAdmissionDto.setTypeOfAdmission(childCmtcNrcAdmission.getTypeOfAdmission());
        childCmtcNrcAdmissionDto.setWeightAtAdmission(childCmtcNrcAdmission.getWeightAtAdmission());
        childCmtcNrcAdmissionDto.setDeathDate(childCmtcNrcAdmission.getDeathDate());
        childCmtcNrcAdmissionDto.setDeathPlace(childCmtcNrcAdmission.getDeathPlace());
        childCmtcNrcAdmissionDto.setOtherDeathPlace(childCmtcNrcAdmission.getOtherDeathPlace());
        childCmtcNrcAdmissionDto.setDeathReason(childCmtcNrcAdmission.getDeathReason());
        childCmtcNrcAdmissionDto.setOtherDeathReason(childCmtcNrcAdmission.getOtherDeathReason());
        childCmtcNrcAdmissionDto.setDefaulterDate(childCmtcNrcAdmission.getDefaulterDate());
        childCmtcNrcAdmissionDto.setScreeningCenter(childCmtcNrcAdmission.getScreeningCenter());
        childCmtcNrcAdmissionDto.setComplementaryFeeding(childCmtcNrcAdmission.getComplementaryFeeding());
        childCmtcNrcAdmissionDto.setBreastFeeding(childCmtcNrcAdmission.getBreastFeeding());
        childCmtcNrcAdmissionDto.setCaseId(childCmtcNrcAdmission.getCaseId());
        childCmtcNrcAdmissionDto.setProblemInBreastFeeding(childCmtcNrcAdmission.getProblemInBreastFeeding());
        childCmtcNrcAdmissionDto.setProblemInMilkInjection(childCmtcNrcAdmission.getProblemInMilkInjection());
        childCmtcNrcAdmissionDto.setVisibleWasting(childCmtcNrcAdmission.getVisibleWasting());
        childCmtcNrcAdmissionDto.setKmcProvided(childCmtcNrcAdmission.getKmcProvided());
        childCmtcNrcAdmissionDto.setNoOfTimesKmcDone(childCmtcNrcAdmission.getNoOfTimesKmcDone());
        childCmtcNrcAdmissionDto.setNoOfTimesAmoxicillinGiven(childCmtcNrcAdmission.getNoOfTimesAmoxicillinGiven());
        childCmtcNrcAdmissionDto.setConsecutive3DaysWeightGain(childCmtcNrcAdmission.getConsecutive3DaysWeightGain());
        return childCmtcNrcAdmissionDto;
    }

}
