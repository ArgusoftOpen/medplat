/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.service;

import com.argusoft.medplat.event.dto.EventConfigTypeDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines methods for table
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public interface TableService {
    
    /**
     * Execute given query string
     * @param query A query string
     * @return A list of query result
     */
    List<Map<String, Object>> executeQueryForCombo(String query, String reportName);

    /**
     * Execute given query string
     * @param query A query string
     * @return A list of query result
     */
    List<LinkedHashMap<String, Object>> executeQuery(String query);

    /**
     * Execute given query string by replacing given parameter
     * @param query A query string
     * @param parameterValue A map of parameter key and value
     * @return A list of query result
     */
    List<LinkedHashMap<String, Object>> execute(String query, LinkedHashMap<String, Object> parameterValue);

    /**
     * Executes given update query string
     * @param query A query string
     */
    void executeUpdate(String query);

    /**
     * Execute given update query string by replacing given parameter
     * @param query A query string
     * @param parameterValue A map of parameter key and value
     */
    void executeUpdateQuery(String query, LinkedHashMap<String, Object> parameterValue);

    /**
     * Executes query of event configuration
     * @param notificationConfigTypeDto An instance of EventConfigTypeDto
     * @param queryDataLists A list of parameter key and value
     */
    void queryHandler(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists);

    void executeUpdateQueryWithoutCast(String query, LinkedHashMap<String, Object> parameterValue);
}
