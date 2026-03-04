/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.service;

import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.dto.QueryMasterDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Defines methods for query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public interface QueryMasterService {
    /**
     * Creates or update given query
     * @param queryMasterDto An instance of QueryMasterDto
     * @param isMetodCallBySyncFunction Whether method is called by sync function or not
     * @return An UUID
     */
    UUID createOrUpdate(QueryMasterDto queryMasterDto,boolean isMetodCallBySyncFunction);

    /**
     * Returns an instance of query based on given id
     * @param id An id of query
     * @return An instance of QueryMasterDto
     */
    QueryMasterDto retrieveById(Integer id);

    /**
     * Returns an instance of query based on given query code
     * @param code A query code
     * @return An instance of QueryMasterDto
     */
    QueryMasterDto retrieveByCode(String code);

    /**
     * Returns an instance of query based on given uuid
     * @param uuid An UUID
     * @return An instance of QueryMasterDto
     */
    QueryMasterDto retrieveByUUID(UUID uuid);

    /**
     * Returns all query based on status
     * @param isActive Whether user is authorize to execute query or not
     * @return A list of QueryMasterDto
     */
    List<QueryMasterDto> retrieveAll(Boolean isActive);

    /**
     * Updates status of query
     * @param queryMasterDto An instance of QueryMasterDto
     */
    void toggleActive(QueryMasterDto queryMasterDto);

    /**
     * Executes given list of query
     * @param queryDtos A list of QueryDto
     * @param isSecure Whether user is authorize to execute query or not
     * @return A list of QueryDto
     */
    List<QueryDto> executeQuery(List<QueryDto> queryDtos, boolean isSecure);

    /**
     * Executes given list of query
     * @param queryDtos A list of QueryDto
     * @param isSecure Whether user is authorize to execute query or not
     * @return A list of QueryDto
     */
    List<QueryDto> execute(List<QueryDto> queryDtos, boolean isSecure);

    /**
     * Executes given update query
     * @param query A query string
     */
    void runUpdateQuery(String query);

    /**
     * Executes an query from given query dto and returns result
     * @param queryDto An instance of QueryDto
     * @return A result list
     */
    List<LinkedHashMap<String, Object>> getQueryResult(QueryDto queryDto);
}
