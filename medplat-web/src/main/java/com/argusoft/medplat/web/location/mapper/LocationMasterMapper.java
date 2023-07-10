/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.mapper;

import com.argusoft.medplat.web.location.dto.LocationMasterDto;
import com.argusoft.medplat.web.location.model.LocationMaster;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Mapper for location master details in order to convert dto to model or model to dto.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class LocationMasterMapper {

    private LocationMasterMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert location master details into entity.
     *
     * @param locationMasterDto Location master details.
     * @param locationMaster    Entity of location master.
     * @param userId            User id.
     * @return Returns entity of location master.
     */
    public static LocationMaster getLocationMasterEntity(LocationMasterDto locationMasterDto, LocationMaster locationMaster, Integer userId) {
        if (locationMasterDto != null) {
            if (locationMaster == null) {
                locationMaster = new LocationMaster();
                locationMaster.setIsActive(true);
                locationMaster.setIsArchive(false);
                locationMaster.setState(LocationMaster.State.ACTIVE);
                if (userId != null) {
                    locationMaster.setCreatedBy(String.valueOf(userId));
                }
                locationMaster.setCreatedOn(new Date());
            }
            locationMaster.setId(locationMasterDto.getId());
            locationMaster.setName(locationMasterDto.getName());
            locationMaster.setType(locationMasterDto.getType());
            locationMaster.setParent(locationMasterDto.getParent());
            locationMaster.setLocationCode(locationMasterDto.getLocationCode());
            locationMaster.setLocationFlag(locationMasterDto.getLocationFlag());
            locationMaster.setContainsCmtcCenter(locationMasterDto.getContainsCmtcCenter());
            locationMaster.setContainsNrcCenter(locationMasterDto.getContainsNrcCenter());
            locationMaster.setCerebralPalsyModule(locationMasterDto.getCerebralPalsyModule());
            locationMaster.setGeoFencing(locationMasterDto.getGeoFencing());
            locationMaster.setEnglishName(locationMasterDto.getEnglishName());
            locationMaster.setLgdCode(locationMasterDto.getLgdCode());
            locationMaster.setMddsCode(locationMasterDto.getMddsCode());
            locationMaster.setIsTaaho(locationMasterDto.getIsTaaho());
            if (userId != null) {
                locationMaster.setModifiedBy(String.valueOf(userId));
            }
            locationMaster.setModifiedOn(new Date());
            return locationMaster;
        }
        return null;
    }

    /**
     * Convert location master entity into details.
     *
     * @param locationMasters Entity of location master.
     * @return Returns location master details.
     */
    public static List<LocationMasterDto> getLocationMasterDtoList(List<LocationMaster> locationMasters) {
        LinkedList<LocationMasterDto> locationDtoList = new LinkedList<>();
        for (LocationMaster locationMaster : locationMasters) {
            LocationMasterDto locationMasterDto = new LocationMasterDto();
            locationMasterDto.setId(locationMaster.getId());
            locationMasterDto.setName(locationMaster.getName());
            locationMasterDto.setType(locationMaster.getType());
            locationMasterDto.setLevel(locationMaster.getHierarchyType().getLevel());
            locationMasterDto.setParent(locationMaster.getParent());
            locationMasterDto.setLocationCode(locationMaster.getLocationCode());
            locationMasterDto.setLocationFlag(locationMaster.getLocationFlag());
            locationMasterDto.setContainsCmtcCenter(locationMaster.getContainsCmtcCenter());
            locationMasterDto.setContainsNrcCenter(locationMaster.getContainsNrcCenter());
            locationMasterDto.setCerebralPalsyModule(locationMaster.getCerebralPalsyModule());
            locationMasterDto.setGeoFencing(locationMaster.getGeoFencing());
            locationMasterDto.setEnglishName(locationMaster.getEnglishName());
            locationMasterDto.setLgdCode(locationMaster.getLgdCode());
            locationMasterDto.setMddsCode(locationMaster.getMddsCode());
            locationMasterDto.setIsTaaho(locationMaster.getIsTaaho());
            locationDtoList.add(locationMasterDto);
        }
        return locationDtoList;
    }
}
