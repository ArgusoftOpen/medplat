/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.rch.dto.FacilityPerformanceMasterDto;

import java.util.Date;


/**
 * <p>
 * Define services for facility performance.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 11:00 AM
 */
public interface FacilityPerformanceService {

    /**
     * Add/Update facility performance details.
     *
     * @param facilityPerformanceMasterDto Facility performance details.
     */
    void createOrUpdate(FacilityPerformanceMasterDto facilityPerformanceMasterDto);

    /**
     * Retrieves facility performance details by health infra id and performance date.
     *
     * @param hid             Health infrastructure id.
     * @param perfromanceDate Performance date of facility.
     * @return Returns facility performance details by defined params.
     */
    FacilityPerformanceMasterDto getFacilityPerformaceByHidAndDate(Integer hid, Date perfromanceDate);

}
