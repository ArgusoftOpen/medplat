
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcWeightDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcWeight;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) weight to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcWeightMapper {

    private ChildCmtcNrcWeightMapper(){
    }

    /**
     * Converts child cmtc nrc weight dto to modal
     * @param childCmtcNrcWeightDto An instance of ChildCmtcNrcWeightDto
     * @return An instance of ChildCmtcNrcWeight
     */
    public static ChildCmtcNrcWeight convertDtoToEntity(ChildCmtcNrcWeightDto childCmtcNrcWeightDto) {
        ChildCmtcNrcWeight childCmtcNrcWeight = new ChildCmtcNrcWeight();
        childCmtcNrcWeight.setAdmissionId(childCmtcNrcWeightDto.getAdmissionId());
        childCmtcNrcWeight.setWeight(childCmtcNrcWeightDto.getWeight());
        childCmtcNrcWeight.setWeightDate(childCmtcNrcWeightDto.getWeightDate());
        childCmtcNrcWeight.setIsMotherCouncelling(childCmtcNrcWeightDto.getIsMotherCouncelling());
        childCmtcNrcWeight.setIsAmoxicillin(childCmtcNrcWeightDto.getIsAmoxicillin());
        childCmtcNrcWeight.setIsVitaminA(childCmtcNrcWeightDto.getIsVitaminA());
        childCmtcNrcWeight.setIsSugarSolution(childCmtcNrcWeightDto.getIsSugarSolution());
        childCmtcNrcWeight.setIsAlbendazole(childCmtcNrcWeightDto.getIsAlbendazole());
        childCmtcNrcWeight.setIsFolicAcid(childCmtcNrcWeightDto.getIsFolicAcid());
        childCmtcNrcWeight.setIsPotassium(childCmtcNrcWeightDto.getIsPotassium());
        childCmtcNrcWeight.setIsMagnesium(childCmtcNrcWeightDto.getIsMagnesium());
        childCmtcNrcWeight.setIsZinc(childCmtcNrcWeightDto.getIsZinc());
        childCmtcNrcWeight.setIsIron(childCmtcNrcWeightDto.getIsIron());
        childCmtcNrcWeight.setBilateralPittingOedema(childCmtcNrcWeightDto.getBilateralPittingOedema());
        childCmtcNrcWeight.setFormulaGiven(childCmtcNrcWeightDto.getFormulaGiven());
        childCmtcNrcWeight.setOtherHigherNutrientsGiven(childCmtcNrcWeightDto.getOtherHigherNutrientsGiven());
        childCmtcNrcWeight.setMidUpperArmCircumference(childCmtcNrcWeightDto.getMidUpperArmCircumference());
        childCmtcNrcWeight.setHeight(childCmtcNrcWeightDto.getHeight());
        childCmtcNrcWeight.setHigherFacilityReferral(childCmtcNrcWeightDto.getHigherFacilityReferral());
        childCmtcNrcWeight.setReferralReason(childCmtcNrcWeightDto.getReferralReason());
        childCmtcNrcWeight.setMultiVitaminSyrup(childCmtcNrcWeightDto.getMultiVitaminSyrup());
        childCmtcNrcWeight.setNightStay(childCmtcNrcWeightDto.getNightStay());
        childCmtcNrcWeight.setHigherFacilityReferralPlace(childCmtcNrcWeightDto.getHigherFacilityReferralPlace());
        childCmtcNrcWeight.setKmcProvided(childCmtcNrcWeightDto.getKmcProvided());
        childCmtcNrcWeight.setNoOfTimesKmcDone(childCmtcNrcWeightDto.getNoOfTimesKmcDone());
        childCmtcNrcWeight.setWeightGain5Gm1Day(childCmtcNrcWeightDto.getWeightGain5Gm1Day());
        childCmtcNrcWeight.setWeightGain5Gm2Day(childCmtcNrcWeightDto.getWeightGain5Gm2Day());
        childCmtcNrcWeight.setWeightGain5Gm3Day(childCmtcNrcWeightDto.getWeightGain5Gm3Day());
        childCmtcNrcWeight.setChildId(childCmtcNrcWeightDto.getChildId());
        return childCmtcNrcWeight;
    }

    /**
     * Converts child cmtc nrc weight modal to dto
     * @param childCmtcNrcWeightEntity An instance of ChildCmtcNrcWeight
     * @return An instance of ChildCmtcNrcWeightDto
     */
    public static ChildCmtcNrcWeightDto convertEntityToDto(ChildCmtcNrcWeight childCmtcNrcWeightEntity) {
        ChildCmtcNrcWeightDto childCmtcNrcWeightDto = new ChildCmtcNrcWeightDto();
        childCmtcNrcWeightDto.setAdmissionId(childCmtcNrcWeightEntity.getAdmissionId());
        childCmtcNrcWeightDto.setWeight(childCmtcNrcWeightEntity.getWeight());
        childCmtcNrcWeightDto.setWeightDate(childCmtcNrcWeightEntity.getWeightDate());
        childCmtcNrcWeightDto.setIsMotherCouncelling(childCmtcNrcWeightEntity.getIsMotherCouncelling());
        childCmtcNrcWeightDto.setIsAmoxicillin(childCmtcNrcWeightEntity.getIsAmoxicillin());
        childCmtcNrcWeightDto.setIsVitaminA(childCmtcNrcWeightEntity.getIsVitaminA());
        childCmtcNrcWeightDto.setIsSugarSolution(childCmtcNrcWeightEntity.getIsSugarSolution());
        childCmtcNrcWeightDto.setIsAlbendazole(childCmtcNrcWeightEntity.getIsAlbendazole());
        childCmtcNrcWeightDto.setIsFolicAcid(childCmtcNrcWeightEntity.getIsFolicAcid());
        childCmtcNrcWeightDto.setIsPotassium(childCmtcNrcWeightEntity.getIsPotassium());
        childCmtcNrcWeightDto.setIsMagnesium(childCmtcNrcWeightEntity.getIsMagnesium());
        childCmtcNrcWeightDto.setIsZinc(childCmtcNrcWeightEntity.getIsZinc());
        childCmtcNrcWeightDto.setIsIron(childCmtcNrcWeightEntity.getIsIron());
        childCmtcNrcWeightDto.setBilateralPittingOedema(childCmtcNrcWeightEntity.getBilateralPittingOedema());
        childCmtcNrcWeightDto.setFormulaGiven(childCmtcNrcWeightEntity.getFormulaGiven());
        childCmtcNrcWeightDto.setOtherHigherNutrientsGiven(childCmtcNrcWeightEntity.getOtherHigherNutrientsGiven());
        childCmtcNrcWeightDto.setMidUpperArmCircumference(childCmtcNrcWeightEntity.getMidUpperArmCircumference());
        childCmtcNrcWeightDto.setHeight(childCmtcNrcWeightEntity.getHeight());
        childCmtcNrcWeightDto.setHigherFacilityReferral(childCmtcNrcWeightEntity.getHigherFacilityReferral());
        childCmtcNrcWeightDto.setReferralReason(childCmtcNrcWeightEntity.getReferralReason());
        childCmtcNrcWeightDto.setMultiVitaminSyrup(childCmtcNrcWeightEntity.getMultiVitaminSyrup());
        childCmtcNrcWeightDto.setNightStay(childCmtcNrcWeightEntity.getNightStay());
        childCmtcNrcWeightDto.setHigherFacilityReferralPlace(childCmtcNrcWeightEntity.getHigherFacilityReferralPlace());
        childCmtcNrcWeightDto.setKmcProvided(childCmtcNrcWeightEntity.getKmcProvided());
        childCmtcNrcWeightDto.setNoOfTimesKmcDone(childCmtcNrcWeightEntity.getNoOfTimesKmcDone());
        childCmtcNrcWeightDto.setWeightGain5Gm1Day(childCmtcNrcWeightEntity.getWeightGain5Gm1Day());
        childCmtcNrcWeightDto.setWeightGain5Gm2Day(childCmtcNrcWeightEntity.getWeightGain5Gm2Day());
        childCmtcNrcWeightDto.setWeightGain5Gm3Day(childCmtcNrcWeightEntity.getWeightGain5Gm3Day());
        childCmtcNrcWeightDto.setChildId(childCmtcNrcWeightEntity.getChildId());
        childCmtcNrcWeightDto.setCreatedOn(childCmtcNrcWeightEntity.getCreatedOn());
        return childCmtcNrcWeightDto;
    }

}
