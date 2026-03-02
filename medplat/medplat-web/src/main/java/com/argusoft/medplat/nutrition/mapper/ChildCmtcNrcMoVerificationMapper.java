
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcMoVerificationDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcMoVerification;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) mo verification to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcMoVerificationMapper {

    private ChildCmtcNrcMoVerificationMapper(){
    }

    /**
     * Converts child cmtc nrc mo verification dto to modal
     * @param childCmtcNrcMoVerificationDto An instance of ChildCmtcNrcMoVerificationDto
     * @return An instance of ChildCmtcNrcMoVerification
     */
    public static ChildCmtcNrcMoVerification convertDtoToEntity(ChildCmtcNrcMoVerificationDto childCmtcNrcMoVerificationDto) {
        ChildCmtcNrcMoVerification childCmtcNrcMoVerification = new ChildCmtcNrcMoVerification();
        childCmtcNrcMoVerification.setChildId(childCmtcNrcMoVerificationDto.getChildId());
        childCmtcNrcMoVerification.setWeight(childCmtcNrcMoVerificationDto.getWeight());
        childCmtcNrcMoVerification.setHeight(childCmtcNrcMoVerificationDto.getHeight());
        childCmtcNrcMoVerification.setMidUpperArmCircumference(childCmtcNrcMoVerificationDto.getMidUpperArmCircumference());
        childCmtcNrcMoVerification.setSdScore(childCmtcNrcMoVerificationDto.getSdScore());
        childCmtcNrcMoVerification.setBilateralPittingOedema(childCmtcNrcMoVerificationDto.getBilateralPittingOedema());
        return childCmtcNrcMoVerification;
    }

}
