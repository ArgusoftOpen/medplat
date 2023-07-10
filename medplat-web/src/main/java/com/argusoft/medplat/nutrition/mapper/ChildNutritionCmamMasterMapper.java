package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildNutritionCmamMasterDto;
import com.argusoft.medplat.nutrition.model.ChildNutritionCmamMaster;
/**
 *<p>
 *     An util class for Child Nutrition CMAM to convert dto to modal or modal to dto
 *</p>
 * @author smeet
 * @since 09/09/2020 5:30
 */
public class ChildNutritionCmamMasterMapper {

    private ChildNutritionCmamMasterMapper(){
    }

    /**
     * Converts child nutrition cmam dto to modal
     * @param childNutritionCmamMasterDto An instance of ChildNutritionCmamMasterDto
     * @return An instance of ChildNutritionCmamMaster
     */
    public static ChildNutritionCmamMaster convertDtoToEntity(ChildNutritionCmamMasterDto childNutritionCmamMasterDto) {
        ChildNutritionCmamMaster childNutritionCmamMaster = new ChildNutritionCmamMaster();
        childNutritionCmamMaster.setChildId(childNutritionCmamMasterDto.getChildId());
        childNutritionCmamMaster.setLocationId(childNutritionCmamMasterDto.getLocationId());
        childNutritionCmamMaster.setState(childNutritionCmamMasterDto.getState());
        childNutritionCmamMaster.setServiceDate(childNutritionCmamMasterDto.getServiceDate());
        childNutritionCmamMaster.setIdentifiedFrom(childNutritionCmamMasterDto.getIdentifiedFrom());
        childNutritionCmamMaster.setReferenceId(childNutritionCmamMasterDto.getReferenceId());
        childNutritionCmamMaster.setCaseCompleted(childNutritionCmamMasterDto.getCaseCompleted());
        return childNutritionCmamMaster;
    }

    /**
     * Converts child nutrition cmam modal to dto
     * @param childNutritionCmamMaster An instance of ChildNutritionCmamMaster
     * @return An instance of ChildNutritionCmamMasterDto
     */
    public static ChildNutritionCmamMasterDto convertEntityToDto(ChildNutritionCmamMaster childNutritionCmamMaster) {
        ChildNutritionCmamMasterDto childNutritionCmamMasterDto = new ChildNutritionCmamMasterDto();
        childNutritionCmamMasterDto.setChildId(childNutritionCmamMaster.getChildId());
        childNutritionCmamMasterDto.setLocationId(childNutritionCmamMaster.getLocationId());
        childNutritionCmamMasterDto.setState(childNutritionCmamMaster.getState());
        childNutritionCmamMasterDto.setServiceDate(childNutritionCmamMaster.getServiceDate());
        childNutritionCmamMasterDto.setIdentifiedFrom(childNutritionCmamMaster.getIdentifiedFrom());
        childNutritionCmamMasterDto.setCaseCompleted(childNutritionCmamMaster.getCaseCompleted());
        return childNutritionCmamMasterDto;
    }
}
