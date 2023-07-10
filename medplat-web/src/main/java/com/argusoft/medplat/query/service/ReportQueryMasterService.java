/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.service;

import com.argusoft.medplat.query.dto.ReportQueryMasterDto;

import java.util.List;
import java.util.UUID;

/**
 * Defines methods for report related query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public interface ReportQueryMasterService {
    /**
     * Create or update report query
     * @param queryMasterDto An instance of ReportQueryMasterDto
     * @return An id of created or updated row
     */
    Integer createOrUpdate(ReportQueryMasterDto queryMasterDto);
    
    /**
     * Returns an instance of ReportQueryMasterDto based on given id
     * @param id An id of report query
     * @return An instance of ReportQueryMasterDto
     */
    ReportQueryMasterDto retrieveById(Integer id);

    /**
     * Returns an instance of report query based on given uuid
     * @param uuid An UUID
     * @return An instance of ReportQueryMasterDto
     */
    ReportQueryMasterDto retrieveByUUID(UUID uuid);

    /**
     * Returns a list of ReportQueryMasterDto based on given status
     * @param isActive A status value
     * @return A list of ReportQueryMasterDto
     */
    List<ReportQueryMasterDto> retrieveAll(Boolean isActive);

    /**
     * Updates status of given report query
     * @param queryMasterDto An instance of ReportQueryMasterDto
     */
    void toggleActive(ReportQueryMasterDto queryMasterDto);
    
}
