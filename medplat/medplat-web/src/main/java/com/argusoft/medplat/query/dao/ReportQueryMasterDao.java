/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.query.model.ReportQueryMaster;

import java.util.List;
import java.util.UUID;

/**
 * Defines database methods for report related query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public interface ReportQueryMasterDao extends GenericDao<ReportQueryMaster, Integer> {

    /**
     * Returns a list of report query based on given state
     * @param isActive A state value
     * @return A list of ReportQueryMasterDto
     */
    List<ReportQueryMaster> retrieveAll(Boolean isActive);

    /**
     * Returns an instance of report query based on given code
     * @param code A query code
     * @return An instance of ReportQueryMasterDto
     */
    ReportQueryMaster retrieveByCode(String code);

    /**
     * Returns an instance of report query based on given uuid
     * @param uuid An UUID
     * @return An instance of ReportQueryMasterDto
     */
    ReportQueryMaster retrieveByUUID(UUID uuid);
}
