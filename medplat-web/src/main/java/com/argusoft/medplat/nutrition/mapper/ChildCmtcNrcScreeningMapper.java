
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcScreeningDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcScreening;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) screening to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcScreeningMapper {

    private ChildCmtcNrcScreeningMapper(){
    }

    /**
     * Converts child cmtc nrc screnning dto to modal
     * @param childCmtcNrcScreeningDto An instance of ChildCmtcNrcScreeningDto
     * @return An instance of ChildCmtcNrcScreening
     */
    public static ChildCmtcNrcScreening convertEntityToDto(ChildCmtcNrcScreeningDto childCmtcNrcScreeningDto) {
        ChildCmtcNrcScreening childCmtcNrcScreening = new ChildCmtcNrcScreening();
        childCmtcNrcScreening.setId(childCmtcNrcScreeningDto.getId());
        childCmtcNrcScreening.setChildId(childCmtcNrcScreeningDto.getChildId());
        childCmtcNrcScreening.setAdmissionId(childCmtcNrcScreeningDto.getAdmissionId());
        childCmtcNrcScreening.setDischargeId(childCmtcNrcScreeningDto.getDischargeId());
        childCmtcNrcScreening.setScreenedOn(childCmtcNrcScreeningDto.getScreenedOn());
        childCmtcNrcScreening.setLocationId(childCmtcNrcScreeningDto.getLocationId());
        childCmtcNrcScreening.setState(childCmtcNrcScreeningDto.getState());
        childCmtcNrcScreening.setScreeningCenter(childCmtcNrcScreeningDto.getScreeningCenter());
        childCmtcNrcScreening.setIsCaseCompleted(childCmtcNrcScreeningDto.getIsCaseCompleted());
        childCmtcNrcScreening.setReferredFrom(childCmtcNrcScreeningDto.getReferredFrom());
        childCmtcNrcScreening.setReferredTo(childCmtcNrcScreeningDto.getReferredTo());
        childCmtcNrcScreening.setReferredDate(childCmtcNrcScreeningDto.getReferredDate());
        childCmtcNrcScreening.setIsArchive(childCmtcNrcScreeningDto.getIsArchive());
        return childCmtcNrcScreening;
    }
}
