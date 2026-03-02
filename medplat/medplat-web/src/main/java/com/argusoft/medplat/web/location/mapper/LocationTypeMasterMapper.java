/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.mapper;

import com.argusoft.medplat.web.location.dto.LocationTypeMasterDto;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Mapper for location type master details in order to convert dto to model or model to dto.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
public class LocationTypeMasterMapper {

    private LocationTypeMasterMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert location type entity into details.
     *
     * @param locationTypeMaster Entity of location type master.
     * @return Returns location type details.
     */
    public static LocationTypeMasterDto convertMasterToDto(LocationTypeMaster locationTypeMaster) {
        LocationTypeMasterDto locationTypeMasterDto = new LocationTypeMasterDto();
        locationTypeMasterDto.setId(locationTypeMaster.getId());
        locationTypeMasterDto.setName(locationTypeMaster.getName());
        locationTypeMasterDto.setType(locationTypeMaster.getType());
        locationTypeMasterDto.setLevel(locationTypeMaster.getLevel());
        locationTypeMasterDto.setSohEnable(locationTypeMaster.isSohEnable());
        locationTypeMasterDto.setIsActive(locationTypeMaster.getIsActive());
        return locationTypeMasterDto;
    }

    /**
     * Convert list of location type master entity into details.
     *
     * @param locationTypeMasters List of location type master entity.
     * @return Returns list of location type details.
     */
    public static List<LocationTypeMasterDto> convertMasterListToDtoList(List<LocationTypeMaster> locationTypeMasters) {
        List<LocationTypeMasterDto> locationTypeMasterDtos = new LinkedList<>();
        for (LocationTypeMaster locationTypeMaster : locationTypeMasters) {
            locationTypeMasterDtos.add(convertMasterToDto(locationTypeMaster));
        }
        return locationTypeMasterDtos;
    }

}
