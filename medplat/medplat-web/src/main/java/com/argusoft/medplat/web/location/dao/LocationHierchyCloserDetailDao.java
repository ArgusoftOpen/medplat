/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.location.model.LocationHierchyCloserDetail;

import java.util.List;

/**
 *
 * <p>
 * Define methods for location hierarchy closer details.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
public interface LocationHierchyCloserDetailDao extends GenericDao<LocationHierchyCloserDetail, Integer> {

    /**
     * Retrieves parent location name by child location id.
     * @param childId Child location id.
     * @return Returns list of parent's location names by child id.
     */
    List<String> retrieveParentLocations(Integer childId);

    /**
     * Retrieves parent location id by child location id.
     * @param childId Child location id.
     * @return Returns list of parent's location ids by child id.
     */
    List<Integer> retrieveParentLocationIds(Integer childId);

    /**
     * Retrieves child location id by parent id.
     * @param parentId Parent location id.
     * @return Returns list of child location ids.
     */
    List<Integer> retrieveChildLocationIds(Integer parentId);

    /**
     * Retrieves list of parent location ids by child location id.
     * @param childId Child location id.
     * @return Returns list of parent location ids.
     */
    List<Integer> getParentLocationIds(Integer childId);

    /**
     * Retrieves list of child location ids by parent location ids list and child location type.
     * @param parentIds List of parent location ids.
     * @param childLocationType List of child location types.
     * @return Returns list of child location ids.
     */
    List<Integer> retrieveChildLocationIdsFromParentList(List<Integer> parentIds, List<String> childLocationType);

    /**
     * Retrieves location hierarchy by location id.
     * @param locationId Location id.
     * @return Returns location hierarchy.
     */
    String getLocationHierarchyStringByLocationId(Integer locationId);
}
