/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.query.model.QueryMaster;

import java.util.List;
import java.util.UUID;

/**
 * Defines database methods for query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public interface QueryMasterDao extends GenericDao<QueryMaster, UUID>{

    /**
     * Checks given query code is already register or not
     * @param code A query code
     * @param uuid An UUID
     * @return Whether query code is available or not
     */
    Boolean isCodeAvailable(String code,UUID uuid);

    /**
     * Returns a list of query based on given state
     * @param isActive A state value
     * @return A list of QueryMaster
     */
    List<QueryMaster> retrieveAll(Boolean isActive);

    /**
     * Returns an instance of query based on given code
     * @param code A query code
     * @return An instance of QueryMaster
     */
    QueryMaster retrieveByCode(String code);

    /**
     * Returns an instance of query based on given uuid
     * @param uuid An UUID
     * @return An instance of QueryMaster
     */
    QueryMaster retrieveByUUID(UUID uuid);
    
    
    
}
