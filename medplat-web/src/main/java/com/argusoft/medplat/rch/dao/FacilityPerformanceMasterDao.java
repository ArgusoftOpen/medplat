/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.FacilityPerformanceMaster;

import java.util.Date;


/**
 * <p>
 * Define methods for facility performance.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 10:19 AM
 */
public interface FacilityPerformanceMasterDao extends GenericDao<FacilityPerformanceMaster, Integer> {

    /**
     * Retrieves facility performance details by health infra id and performance date.
     *
     * @param hid             Health infrastructure id.
     * @param perfromanceDate Performance date of facility.
     * @return Returns facility performance details by defined params.
     */
    FacilityPerformanceMaster getFacilityPerformaceByHidAndDate(Integer hid, Date perfromanceDate);

    /**
     * Add/Update facility performance details.
     *
     * @param facilityPerformanceMaster Facility performance details.
     */
    @Override
    void createOrUpdate(FacilityPerformanceMaster facilityPerformanceMaster);

}
