/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.FacilityPerformanceMasterDto;
import com.argusoft.medplat.rch.model.FacilityPerformanceMaster;

/**
 * <p>
 * Mapper for facility performance in order to convert dto to model or model to dto.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 11:00 AM
 */
public class FacilityPerformanceMapper {
    private FacilityPerformanceMapper() {
    }

    /**
     * Convert facility performance details into entity.
     *
     * @param facilityPerformanceMasterDto Facility performance details.
     * @return Returns entity of facility performance.
     */
    public static FacilityPerformanceMaster convertDtoToEntity(FacilityPerformanceMasterDto facilityPerformanceMasterDto) {
        FacilityPerformanceMaster facilityPerformanceMaster = new FacilityPerformanceMaster();
        facilityPerformanceMaster.setId(facilityPerformanceMasterDto.getId());
        facilityPerformanceMaster.setHealthInfrastructureId(facilityPerformanceMasterDto.getHealthInfrastructureId());
        facilityPerformanceMaster.setPerformanceDate(facilityPerformanceMasterDto.getPerformanceDate());
        facilityPerformanceMaster.setNoOfOpdAttended(facilityPerformanceMasterDto.getNoOfOpdAttended());
        facilityPerformanceMaster.setNoOfIpdAttended(facilityPerformanceMasterDto.getNoOfIpdAttended());
        facilityPerformanceMaster.setNoOfDeliveresConducted(facilityPerformanceMasterDto.getNoOfDeliveresConducted());
        facilityPerformanceMaster.setNoOfSectionConducted(facilityPerformanceMasterDto.getNoOfSectionConducted());
        facilityPerformanceMaster.setNoOfMajorOperationConducted(facilityPerformanceMasterDto.getNoOfMajorOperationConducted());
        facilityPerformanceMaster.setNoOfMinorOperationConducted(facilityPerformanceMasterDto.getNoOfMinorOperationConducted());
        facilityPerformanceMaster.setNoOfLaboratoryTestConducted(facilityPerformanceMasterDto.getNoOfLaboratoryTestConducted());
        return facilityPerformanceMaster;
    }

    /**
     * Convert facility performance entity into dto.
     *
     * @param facilityPerformanceMaster Entity of facility performance.
     * @return Returns facility performance details.
     */
    public static FacilityPerformanceMasterDto convertEntityToDto(FacilityPerformanceMaster facilityPerformanceMaster) {
        FacilityPerformanceMasterDto facilityPerformanceMasterDto = new FacilityPerformanceMasterDto();
        facilityPerformanceMasterDto.setId(facilityPerformanceMaster.getId());
        facilityPerformanceMasterDto.setHealthInfrastructureId(facilityPerformanceMaster.getHealthInfrastructureId());
        facilityPerformanceMasterDto.setPerformanceDate(facilityPerformanceMaster.getPerformanceDate());
        facilityPerformanceMasterDto.setNoOfOpdAttended(facilityPerformanceMaster.getNoOfOpdAttended());
        facilityPerformanceMasterDto.setNoOfIpdAttended(facilityPerformanceMaster.getNoOfIpdAttended());
        facilityPerformanceMasterDto.setNoOfDeliveresConducted(facilityPerformanceMaster.getNoOfDeliveresConducted());
        facilityPerformanceMasterDto.setNoOfSectionConducted(facilityPerformanceMaster.getNoOfSectionConducted());
        facilityPerformanceMasterDto.setNoOfMajorOperationConducted(facilityPerformanceMaster.getNoOfMajorOperationConducted());
        facilityPerformanceMasterDto.setNoOfMinorOperationConducted(facilityPerformanceMaster.getNoOfMinorOperationConducted());
        facilityPerformanceMasterDto.setNoOfLaboratoryTestConducted(facilityPerformanceMaster.getNoOfLaboratoryTestConducted());
        return facilityPerformanceMasterDto;
    }

}
