/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for location type master details.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public interface LocationTypeMasterDao extends GenericDao<LocationTypeMaster, Integer> {

    /**
     * Retrieves all types of location which are modified after last sync of mobile server
     * @param modifiedOn last modified time of mobile database for locationTypeMaster.
     * @return Returns list of location types.
     */
    public List<LocationTypeMaster> retrieveLocationTypeMasterByModifiedOn(Date modifiedOn);

    /**
     * Retrieve location type of given type
     * @return An instance of LocationTypeMaster.
     */
    LocationTypeMaster retrieveLocationType(String type);
}
