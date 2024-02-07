/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.location.model.LocationLevelHierarchy;

/**
 *
 * <p>
 * Define methods for location level hierarchy details.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
public interface LocationLevelHierarchyDao extends GenericDao<LocationLevelHierarchy, Integer> {

    /**
     * Retrieves location level hierarchy details by location id.
     * @param locationId Location id.
     * @return Returns location level hierarchy details.
     */
    LocationLevelHierarchy retrieveByLocationId(Integer locationId);
    
}
