/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.rch.dto.FacilityPerformanceMasterDto;
import com.argusoft.medplat.rch.service.FacilityPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * Define APIs for facility performance.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/facilitydata")
@Tag(name = "Facility Performance Controller", description = "")
public class FacilityPerformanceController {

    @Autowired
    FacilityPerformanceService facilityPerformanceService;

    /**
     * Add facility performance details.
     *
     * @param facilityPerformanceMasterDto Details of facility performance.
     */
    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody FacilityPerformanceMasterDto facilityPerformanceMasterDto) {
        try {
            facilityPerformanceService.createOrUpdate(facilityPerformanceMasterDto);
        } catch (Exception e) {
            throw new ImtechoUserException(e.getMessage(), 1);
        }
    }

    /**
     * Retrieves facility performance by health infrastructure id.
     *
     * @param hid            Health infrastructure id.
     * @param performaceDate Facility performance date.
     * @return Returns facility performance details by health infrastructure id.
     */
    @GetMapping(value = "/{hid}")
    public FacilityPerformanceMasterDto facilityPerformaceByHidAndDate(@PathVariable() Integer hid, @RequestParam Long performaceDate) {
        try {
            return facilityPerformanceService.getFacilityPerformaceByHidAndDate(hid, new Date(performaceDate));
        } catch (Exception e) {
            throw new ImtechoUserException(e.getMessage(), 1);
        }
    }

}