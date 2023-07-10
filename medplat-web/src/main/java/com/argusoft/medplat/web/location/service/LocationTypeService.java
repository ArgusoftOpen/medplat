/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.service;

import com.argusoft.medplat.web.location.dto.LocationTypeMasterDto;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;

import java.util.List;

/**
 *
 * <p>
 *     Define services for location type.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public interface LocationTypeService {

    /**
     * Retrieves all types of location.
     * @return Returns list of location types.
     */
    List<LocationTypeMasterDto> retrieveAll();

    /**
     * Retrieve location type of given type
     * @return An instance of LocationTypeMasterDto.
     */
    LocationTypeMasterDto retrieveLocationType(String type);

    /**
     * Retrieves all types of location which are modified after last sync of mobile server
     * @param modifiedOn last modified time of mobile database for locationTypeMaster.
     * @return Returns list of location types.
     */
    public List<LocationTypeMaster> retrieveLocationTypeMasterByModifiedOn(Long modifiedOn);
}
