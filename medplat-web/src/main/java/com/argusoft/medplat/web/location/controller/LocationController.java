/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.web.location.dto.LocationDetailDto;
import com.argusoft.medplat.web.location.dto.LocationHierchyDto;
import com.argusoft.medplat.web.location.dto.LocationMasterDto;
import com.argusoft.medplat.web.location.dto.LocationTypeMasterDto;
import com.argusoft.medplat.web.location.service.LocationService;
import com.argusoft.medplat.web.location.service.LocationTypeService;
import com.argusoft.medplat.web.users.service.RoleHierarchyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 * Define APIs for location.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/location")
@Tag(name = "Location Controller", description = "")
public class LocationController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationTypeService locationTypeService;
    @Autowired
    private ImtechoSecurityUser user;
    @Autowired
    private RoleHierarchyManagementService roleHierarchyService;

    /**
     * Retrieves next level location hierarchy by location id or current level or location hierarchy.
     * @param locationId Location id.
     * @param level Current level of location.
     * @param fetchAccordingToUserAOI Fetch according to user id or not.
     * @param hierarchy Location hierarchy.
     * @param languagePreference Preferred language.
     * @return Returns list of location hierarchy by defined params.
     */
    @GetMapping(value = "/hierarchy")
    public List<LocationHierchyDto> retrieveLocationHierarchyDetail(@RequestParam(name = "locationIds", required = false) List<Integer> locationIds,
                                                                    @RequestParam(name = "level", required = false) Integer level, @RequestParam(name = "fetchAccordingToUserAOI", required = true) Boolean fetchAccordingToUserAOI,
                                                                    @RequestParam(name = "hierarchy", required = false) String hierarchy,
                                                                    @RequestParam(name = "languagePreference", required = false) String languagePreference) {
        return locationService.retrieveLocationHierarchyDetailByCriteria(user.getId(), locationIds, level, fetchAccordingToUserAOI, languagePreference);
    }

    /**
     * Retrieves parent location details by child location.
     * @param locationId Child location id.
     * @param languagePreference Preferred language.
     * @return Returns parent location details.
     */
    @GetMapping(value = "/parent")
    public LocationDetailDto retrieveParentLocationDetail(@RequestParam(name = "locationId", required = false) Integer locationId,
                                                          @RequestParam(name = "languagePreference", required = false) String languagePreference) {
        return locationService.retrieveParentLocationDetail(locationId, languagePreference);
    }

    /**
     * Retrieves location hierarchy details by role id.
     * @param roleId Role id.
     * @return Returns a map of location hierarchy and role of given role id.
     */
    @GetMapping(value = "/byroleid")
    public Map<String, Object> retrieveLocationHierarchyDetail(@RequestParam("role_id") Integer roleId) {
        return roleHierarchyService.retrieveLocationHierarchyByRoleId(roleId);
    }

    /**
     * Retrieves location types.
     * @return Returns list of location types.
     */
    @GetMapping(value = "/locationtypes")
    public List<LocationTypeMasterDto> retrieveLocationTypes() {
        return locationTypeService.retrieveAll();
    }

    /**
     * Retrieves location types.
     * @return Returns list of location types.
     */
    @GetMapping(value = "/locationtype")
    public LocationTypeMasterDto retrieveLocationType(@RequestParam String type) {
        return locationTypeService.retrieveLocationType(type);
    }

    /**
     * Add/Update location.
     * @param locationMasterDto Location details.
     */
    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody LocationMasterDto locationMasterDto) {
        locationService.createOrUpdate(locationMasterDto, user.getId());
    }

    /**
     * Retrieves child location details by parent location id.
     * @param locationId Parent location id.
     * @return Returns list of child location by given location id.
     */
    @GetMapping(value = "/childlocations")
    public List<LocationMasterDto> retrieveChildLocationsLocationId(@RequestParam("locationId") Integer locationId) {
        return locationService.retrieveChildLocationsByLocationId(locationId);
    }

    /**
     * Retrieves location details by location id.
     * @param id Id of location.
     * @return Returns location details of given location id.
     */
    @GetMapping(value = "/byid")
    public LocationMasterDto retrieveById(@RequestParam("location_id") Integer id) {
        return locationService.retrieveById(id);
    }

    /**
     * Retrieves location hierarchy.
     * @param id Location id.
     * @return Returns location details which contains hierarchy.
     */
    @GetMapping(value = "/gethiererchystring")
    public LocationMasterDto retrieveLocationHierarchyById(@RequestParam("locationId") Integer id) {
        return locationService.retrieveLocationHierarchyById(id);
    }

    /**
     * Retrieves location hierarchy in english.
     * @param id Location id.
     * @return Returns location details which contains hierarchy in english.
     */
    @GetMapping(value = "/getenghiererchystring")
    public LocationMasterDto retrieveEngLocationHierarchyById(@RequestParam("locationId") Integer id) {
        return locationService.retrieveEngLocationHierarchyById(id);
    }


}