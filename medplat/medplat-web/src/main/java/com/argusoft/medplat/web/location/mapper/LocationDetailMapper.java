/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.mapper;

import com.argusoft.medplat.web.location.dto.LocationDetailDto;
import com.argusoft.medplat.web.location.model.LocationMaster;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Mapper for location details in order to convert dto to model or model to dto.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
public class LocationDetailMapper {

    private LocationDetailMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert location entity into details.
     *
     * @param locationMaster Entity of location.
     * @return Returns details of location.
     */
    public static LocationDetailDto entityToDtoLocationDetail(LocationMaster locationMaster) {
        LocationDetailDto locationDetailDto = new LocationDetailDto();

        locationDetailDto.setId(locationMaster.getId());
        locationDetailDto.setName(locationMaster.getName());
        locationDetailDto.setType(locationMaster.getType());

        return locationDetailDto;
    }

    /**
     * Convert list of location entity into details.
     *
     * @param locationMasters List of location entity.
     * @return Returns list of location details.
     */
    public static List<LocationDetailDto> entityToDtoLocationDetailList(List<LocationMaster> locationMasters) {
        List<LocationDetailDto> locationDetailDtos = new LinkedList<>();

        for (LocationMaster locationMaster : locationMasters) {
            locationDetailDtos.add(LocationDetailMapper.entityToDtoLocationDetail(locationMaster));
        }

        return locationDetailDtos;
    }
}
