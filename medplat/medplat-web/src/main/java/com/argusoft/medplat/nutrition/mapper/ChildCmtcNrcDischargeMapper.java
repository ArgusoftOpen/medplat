
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcDischargeDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcDischarge;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) discharge to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcDischargeMapper {

    private ChildCmtcNrcDischargeMapper(){
    }
    /**
     * Converts child cmtc nrc discharge dto to modal
     * @param childCmtcNrcDischargeDto An instance of ChildCmtcNrcDischargeDto
     * @return An instance of ChildCmtcNrcDischarge
     */
    public static ChildCmtcNrcDischarge convertDtoToEntity(ChildCmtcNrcDischargeDto childCmtcNrcDischargeDto) {
        ChildCmtcNrcDischarge childCmtcNrcDischarge = new ChildCmtcNrcDischarge();
        childCmtcNrcDischarge.setChildId(childCmtcNrcDischargeDto.getChildId());
        childCmtcNrcDischarge.setAdmissionId(childCmtcNrcDischargeDto.getAdmissionId());
        childCmtcNrcDischarge.setReferredBy(childCmtcNrcDischargeDto.getReferredBy());
        childCmtcNrcDischarge.setHigherFacilityReferral(childCmtcNrcDischargeDto.getHigherFacilityReferral());
        childCmtcNrcDischarge.setReferralReason(childCmtcNrcDischargeDto.getReferralReason());
        childCmtcNrcDischarge.setDischargeDate(childCmtcNrcDischargeDto.getDischargeDate());
        childCmtcNrcDischarge.setBilateralPittingOedema(childCmtcNrcDischargeDto.getBilateralPittingOedema());
        childCmtcNrcDischarge.setWeight(childCmtcNrcDischargeDto.getWeight());
        childCmtcNrcDischarge.setHeight(childCmtcNrcDischargeDto.getHeight());
        childCmtcNrcDischarge.setMidUpperArmCircumference(childCmtcNrcDischargeDto.getMidUpperArmCircumference());
        childCmtcNrcDischarge.setIllness(childCmtcNrcDischargeDto.getIllness());
        childCmtcNrcDischarge.setOtherIllness(childCmtcNrcDischargeDto.getOtherIllness());
        childCmtcNrcDischarge.setSdScore(childCmtcNrcDischargeDto.getSdScore());
        childCmtcNrcDischarge.setDischargeStatus(childCmtcNrcDischargeDto.getDischargeStatus());
        childCmtcNrcDischarge.setHigherFacilityReferralPlace(childCmtcNrcDischargeDto.getHigherFacilityReferralPlace());
        childCmtcNrcDischarge.setCaseId(childCmtcNrcDischargeDto.getCaseId());
        childCmtcNrcDischarge.setKmcProvided(childCmtcNrcDischargeDto.getKmcProvided());
        childCmtcNrcDischarge.setNoOfTimesKmcDone(childCmtcNrcDischargeDto.getNoOfTimesKmcDone());
        return childCmtcNrcDischarge;
    }
    /**
     * Converts child cmtc nrc discharge modal to dto
     * @param childCmtcNrcDischarge An instance of ChildCmtcNrcDischarge
     * @return An instance of ChildCmtcNrcDischargeDto
     */
    public static ChildCmtcNrcDischargeDto convertEntityToDto(ChildCmtcNrcDischarge childCmtcNrcDischarge) {
        ChildCmtcNrcDischargeDto childCmtcNrcDischargeDto = new ChildCmtcNrcDischargeDto();
        childCmtcNrcDischargeDto.setId(childCmtcNrcDischarge.getId());
        childCmtcNrcDischargeDto.setChildId(childCmtcNrcDischarge.getChildId());
        childCmtcNrcDischargeDto.setAdmissionId(childCmtcNrcDischarge.getAdmissionId());
        childCmtcNrcDischargeDto.setReferredBy(childCmtcNrcDischarge.getReferredBy());
        childCmtcNrcDischargeDto.setHigherFacilityReferral(childCmtcNrcDischarge.getHigherFacilityReferral());
        childCmtcNrcDischargeDto.setReferralReason(childCmtcNrcDischarge.getReferralReason());
        childCmtcNrcDischargeDto.setDischargeDate(childCmtcNrcDischarge.getDischargeDate());
        childCmtcNrcDischargeDto.setBilateralPittingOedema(childCmtcNrcDischarge.getBilateralPittingOedema());
        childCmtcNrcDischargeDto.setWeight(childCmtcNrcDischarge.getWeight());
        childCmtcNrcDischargeDto.setHeight(childCmtcNrcDischarge.getHeight());
        childCmtcNrcDischargeDto.setMidUpperArmCircumference(childCmtcNrcDischarge.getMidUpperArmCircumference());
        childCmtcNrcDischargeDto.setIllness(childCmtcNrcDischarge.getIllness());
        childCmtcNrcDischargeDto.setOtherIllness(childCmtcNrcDischarge.getOtherIllness());
        childCmtcNrcDischargeDto.setSdScore(childCmtcNrcDischarge.getSdScore());
        childCmtcNrcDischargeDto.setDischargeStatus(childCmtcNrcDischarge.getDischargeStatus());
        childCmtcNrcDischargeDto.setHigherFacilityReferralPlace(childCmtcNrcDischarge.getHigherFacilityReferralPlace());
        childCmtcNrcDischargeDto.setCaseId(childCmtcNrcDischarge.getCaseId());
        childCmtcNrcDischargeDto.setKmcProvided(childCmtcNrcDischarge.getKmcProvided());
        childCmtcNrcDischargeDto.setNoOfTimesKmcDone(childCmtcNrcDischarge.getNoOfTimesKmcDone());
        return childCmtcNrcDischargeDto;
    }

}
