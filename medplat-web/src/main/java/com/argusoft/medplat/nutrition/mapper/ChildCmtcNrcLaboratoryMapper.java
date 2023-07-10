
package com.argusoft.medplat.nutrition.mapper;

import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcLaboratoryDto;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcLaboratory;

/**
 *<p>
 *     An util class for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) laboratory to convert dto to modal or modal to dto
 *</p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcLaboratoryMapper {

    private ChildCmtcNrcLaboratoryMapper(){
    }

    /**
     * Converts child cmtc nrc laboratory dto to modal
     * @param childCmtcNrcLaboratoryDto An instance of ChildCmtcNrcLaboratoryDto
     * @return An instance of ChildCmtcNrcLaboratory
     */
    public static ChildCmtcNrcLaboratory convertDtoToEntity(ChildCmtcNrcLaboratoryDto childCmtcNrcLaboratoryDto) {
        ChildCmtcNrcLaboratory childCmtcNrcLaboratory = new ChildCmtcNrcLaboratory();
        if (childCmtcNrcLaboratoryDto.getId() != null) {
            childCmtcNrcLaboratory.setId(childCmtcNrcLaboratoryDto.getId());
        }
        childCmtcNrcLaboratory.setAdmissionId(childCmtcNrcLaboratoryDto.getAdmissionId());
        childCmtcNrcLaboratory.setLaboratoryDate(childCmtcNrcLaboratoryDto.getLaboratoryDate());
        childCmtcNrcLaboratory.setHemoglobinChecked(childCmtcNrcLaboratoryDto.getHemoglobinChecked());
        childCmtcNrcLaboratory.setHemoglobin(childCmtcNrcLaboratoryDto.getHemoglobin());
        childCmtcNrcLaboratory.setPsForMpChecked(childCmtcNrcLaboratoryDto.getPsForMpChecked());
        childCmtcNrcLaboratory.setPsForMp(childCmtcNrcLaboratoryDto.getPsForMp());
        childCmtcNrcLaboratory.setPsForMpValue(childCmtcNrcLaboratoryDto.getPsForMpValue());
        childCmtcNrcLaboratory.setMonotouxTestChecked(childCmtcNrcLaboratoryDto.getMonotouxTestChecked());
        childCmtcNrcLaboratory.setMonotouxTest(childCmtcNrcLaboratoryDto.getMonotouxTest());
        childCmtcNrcLaboratory.setXrayChestChecked(childCmtcNrcLaboratoryDto.getXrayChestChecked());
        childCmtcNrcLaboratory.setXrayChest(childCmtcNrcLaboratoryDto.getXrayChest());
        childCmtcNrcLaboratory.setUrineAlbuminChecked(childCmtcNrcLaboratoryDto.getUrineAlbuminChecked());
        childCmtcNrcLaboratory.setUrineAlbumin(childCmtcNrcLaboratoryDto.getUrineAlbumin());
        childCmtcNrcLaboratory.setBloodGroup(childCmtcNrcLaboratoryDto.getBloodGroup());
        childCmtcNrcLaboratory.setUrinePusCellsChecked(childCmtcNrcLaboratoryDto.getUrinePusCellsChecked());
        childCmtcNrcLaboratory.setUrinePusCells(childCmtcNrcLaboratoryDto.getUrinePusCells());
        childCmtcNrcLaboratory.setHivChecked(childCmtcNrcLaboratoryDto.getHivChecked());
        childCmtcNrcLaboratory.setHiv(childCmtcNrcLaboratoryDto.getHiv());
        childCmtcNrcLaboratory.setSickleChecked(childCmtcNrcLaboratoryDto.getSickleChecked());
        childCmtcNrcLaboratory.setSickle(childCmtcNrcLaboratoryDto.getSickle());
        childCmtcNrcLaboratory.setTestOutputState(childCmtcNrcLaboratoryDto.getTestOutputState());
        return childCmtcNrcLaboratory;
    }

    /**
     * Converts child cmtc nrc laboratory modal to dto
     * @param childCmtcNrcLaboratory An instance of ChildCmtcNrcLaboratory
     * @return An instance of ChildCmtcNrcLaboratoryDto
     */
    public static ChildCmtcNrcLaboratoryDto convertEntityToDto(ChildCmtcNrcLaboratory childCmtcNrcLaboratory) {
        ChildCmtcNrcLaboratoryDto childCmtcNrcLaboratoryDto = new ChildCmtcNrcLaboratoryDto();
        childCmtcNrcLaboratoryDto.setId(childCmtcNrcLaboratory.getId());
        childCmtcNrcLaboratoryDto.setAdmissionId(childCmtcNrcLaboratory.getAdmissionId());
        childCmtcNrcLaboratoryDto.setLaboratoryDate(childCmtcNrcLaboratory.getLaboratoryDate());
        childCmtcNrcLaboratoryDto.setHemoglobinChecked(childCmtcNrcLaboratory.getHemoglobinChecked());
        childCmtcNrcLaboratoryDto.setHemoglobin(childCmtcNrcLaboratory.getHemoglobin());
        childCmtcNrcLaboratoryDto.setPsForMpChecked(childCmtcNrcLaboratory.getPsForMpChecked());
        childCmtcNrcLaboratoryDto.setPsForMp(childCmtcNrcLaboratory.getPsForMp());
        childCmtcNrcLaboratoryDto.setPsForMpValue(childCmtcNrcLaboratory.getPsForMpValue());
        childCmtcNrcLaboratoryDto.setMonotouxTestChecked(childCmtcNrcLaboratory.getMonotouxTestChecked());
        childCmtcNrcLaboratoryDto.setMonotouxTest(childCmtcNrcLaboratory.getMonotouxTest());
        childCmtcNrcLaboratoryDto.setXrayChestChecked(childCmtcNrcLaboratory.getXrayChestChecked());
        childCmtcNrcLaboratoryDto.setXrayChest(childCmtcNrcLaboratory.getXrayChest());
        childCmtcNrcLaboratoryDto.setUrineAlbuminChecked(childCmtcNrcLaboratory.getUrineAlbuminChecked());
        childCmtcNrcLaboratoryDto.setUrineAlbumin(childCmtcNrcLaboratory.getUrineAlbumin());
        childCmtcNrcLaboratoryDto.setBloodGroup(childCmtcNrcLaboratory.getBloodGroup());
        childCmtcNrcLaboratoryDto.setUrinePusCellsChecked(childCmtcNrcLaboratory.getUrinePusCellsChecked());
        childCmtcNrcLaboratoryDto.setUrinePusCells(childCmtcNrcLaboratory.getUrinePusCells());
        childCmtcNrcLaboratoryDto.setHivChecked(childCmtcNrcLaboratory.getHivChecked());
        childCmtcNrcLaboratoryDto.setHiv(childCmtcNrcLaboratory.getHiv());
        childCmtcNrcLaboratoryDto.setSickleChecked(childCmtcNrcLaboratory.getSickleChecked());
        childCmtcNrcLaboratoryDto.setSickle(childCmtcNrcLaboratory.getSickle());
        childCmtcNrcLaboratoryDto.setTestOutputState(childCmtcNrcLaboratory.getTestOutputState());
        return childCmtcNrcLaboratoryDto;
    }

}
