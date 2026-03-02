
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcFollowUpDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcFollowUp;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) followup to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcFollowUpMapper {

    private ChildCmtcNrcFollowUpMapper(){
    }

    /**
     * Converts child cmtc nrc followup dto to modal
     * @param childCmtcNrcFollowUpDto An instance of ChildCmtcNrcFollowUpDto
     * @return An instance of ChildCmtcNrcFollowUp
     */
    public static ChildCmtcNrcFollowUp convertDtoToEntity(ChildCmtcNrcFollowUpDto childCmtcNrcFollowUpDto) {
        ChildCmtcNrcFollowUp childCmtcNrcFollowUp = new ChildCmtcNrcFollowUp();
        childCmtcNrcFollowUp.setChildId(childCmtcNrcFollowUpDto.getChildId());
        childCmtcNrcFollowUp.setAdmissionId(childCmtcNrcFollowUpDto.getAdmissionId());
        childCmtcNrcFollowUp.setReferredBy(childCmtcNrcFollowUpDto.getReferredBy());
        childCmtcNrcFollowUp.setFollowUpVisit(childCmtcNrcFollowUpDto.getFollowUpVisit());
        childCmtcNrcFollowUp.setFollowUpDate(childCmtcNrcFollowUpDto.getFollowUpDate());
        childCmtcNrcFollowUp.setBilateralPittingOedema(childCmtcNrcFollowUpDto.getBilateralPittingOedema());
        childCmtcNrcFollowUp.setWeight(childCmtcNrcFollowUpDto.getWeight());
        childCmtcNrcFollowUp.setHeight(childCmtcNrcFollowUpDto.getHeight());
        childCmtcNrcFollowUp.setMidUpperArmCircumference(childCmtcNrcFollowUpDto.getMidUpperArmCircumference());
        childCmtcNrcFollowUp.setIllness(childCmtcNrcFollowUpDto.getIllness());
        childCmtcNrcFollowUp.setOtherIllness(childCmtcNrcFollowUpDto.getOtherIllness());
        childCmtcNrcFollowUp.setSdScore(childCmtcNrcFollowUpDto.getSdScore());
        childCmtcNrcFollowUp.setProgramOutput(childCmtcNrcFollowUpDto.getProgramOutput());
        childCmtcNrcFollowUp.setDischargeFromProgram(childCmtcNrcFollowUpDto.getDischargeFromProgram());
        childCmtcNrcFollowUp.setOtherCmtcCenterFollowUp(childCmtcNrcFollowUpDto.getOtherCmtcCenterFollowUp());
        childCmtcNrcFollowUp.setFollowupOtherCenter(childCmtcNrcFollowUpDto.getFollowupOtherCenter());
        childCmtcNrcFollowUp.setCaseId(childCmtcNrcFollowUpDto.getCaseId());
        return childCmtcNrcFollowUp;
    }

    /**
     * Converts child cmtc nrc followup modal to dto
     * @param childCmtcNrcFollowUp An instance of ChildCmtcNrcFollowUp
     * @return An instance of ChildCmtcNrcFollowUpDto
     */
    public static ChildCmtcNrcFollowUpDto convertEntityToDto(ChildCmtcNrcFollowUp childCmtcNrcFollowUp) {
        ChildCmtcNrcFollowUpDto childCmtcNrcFollowUpDto = new ChildCmtcNrcFollowUpDto();
        childCmtcNrcFollowUpDto.setChildId(childCmtcNrcFollowUp.getChildId());
        childCmtcNrcFollowUpDto.setAdmissionId(childCmtcNrcFollowUp.getAdmissionId());
        childCmtcNrcFollowUpDto.setReferredBy(childCmtcNrcFollowUp.getReferredBy());
        childCmtcNrcFollowUpDto.setFollowUpVisit(childCmtcNrcFollowUp.getFollowUpVisit());
        childCmtcNrcFollowUpDto.setFollowUpDate(childCmtcNrcFollowUp.getFollowUpDate());
        childCmtcNrcFollowUpDto.setBilateralPittingOedema(childCmtcNrcFollowUp.getBilateralPittingOedema());
        childCmtcNrcFollowUpDto.setWeight(childCmtcNrcFollowUp.getWeight());
        childCmtcNrcFollowUpDto.setHeight(childCmtcNrcFollowUp.getHeight());
        childCmtcNrcFollowUpDto.setMidUpperArmCircumference(childCmtcNrcFollowUp.getMidUpperArmCircumference());
        childCmtcNrcFollowUpDto.setIllness(childCmtcNrcFollowUp.getIllness());
        childCmtcNrcFollowUpDto.setOtherIllness(childCmtcNrcFollowUp.getOtherIllness());
        childCmtcNrcFollowUpDto.setSdScore(childCmtcNrcFollowUp.getSdScore());
        childCmtcNrcFollowUpDto.setProgramOutput(childCmtcNrcFollowUp.getProgramOutput());
        childCmtcNrcFollowUpDto.setDischargeFromProgram(childCmtcNrcFollowUp.getDischargeFromProgram());
        childCmtcNrcFollowUpDto.setOtherCmtcCenterFollowUp(childCmtcNrcFollowUp.getOtherCmtcCenterFollowUp());
        childCmtcNrcFollowUpDto.setFollowupOtherCenter(childCmtcNrcFollowUp.getFollowupOtherCenter());
        childCmtcNrcFollowUpDto.setCaseId(childCmtcNrcFollowUp.getCaseId());
        return childCmtcNrcFollowUpDto;
    }

}
