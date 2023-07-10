/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.fhs.controller;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.service.FhsDashboardService;
import com.argusoft.medplat.dashboard.fhs.view.DisplayObject;
import com.argusoft.medplat.fhs.dto.FhsVillagesDto;
import com.argusoft.medplat.fhs.dto.StarPerformersOfTheDayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Family health survey controller</p>
 *
 * @author kunjan
 * @since 27/08/20 12:00 AM
 */
@RestController
@RequestMapping("/api/familyhealthsurvey")
public class FhsDashboardController {

    @Autowired
    private FhsDashboardService fhsDashboardService;

    @Autowired
    private ImtechoSecurityUser user;

    /**
     * Returns family and members detail for the given location id
     *
     * @param locationId Location id
     * @return List of display objects
     */
    @GetMapping(value = "/familiesandmembers")
    public List<DisplayObject> familiesAndMembers(@RequestParam("locationId") Integer locationId) {
        return fhsDashboardService.familiesAndMembers(locationId);
    }

    /**
     * Returns family and members detail by location id
     *
     * @param locationId Location id
     * @return List of DisplayObject
     */
    @GetMapping(value = "/familiesandmembers/locationId")
    public List<DisplayObject> familiesAndMembersByLocationId(@RequestParam("locationId") Integer locationId) {
        return fhsDashboardService.familiesAndMembersByLocationId(locationId);
    }

    /**
     * Returns list of family health survey villages
     *
     * @return List of FhsVillagesDto
     */
    @GetMapping(value = "/families/villages")
    public List<FhsVillagesDto> getFHSVillages() {
        return fhsDashboardService.getFhsVillagesByUserId(user.getId());
    }

    /**
     * Returns star performers of the day
     *
     * @return list of StarPerformersOfTheDayDto
     */
    @GetMapping(value = "/familiesandmembers/starperformersoftheday")
    public List<StarPerformersOfTheDayDto> starPerformersOfTheDay() {
        return fhsDashboardService.starPerformersOfTheDay();
    }

    /**
     * Modify last update time
     *
     * @return last update time string
     */
    @GetMapping(value = "/updateTime")
    public String getLastUpdateTime() {
        return fhsDashboardService.getLastUpdateTime();
    }
}
