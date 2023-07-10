/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines database methods for table
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public interface    TableDao {

     /**
     * Returns combo parameter from query
     * @param query A query string
     * @return A list of query result
     */
    List<Map<String, Object>> executeQueryForCombo(String query, String reportName);

    /**
     * Executes given query
     * @param query A query string
     * @return A list of query result
     */
    List<LinkedHashMap<String, Object>> executeQuery(String query);

    /**
     * Executes given query by replacing given parameter in query
     * @param query A query string
     * @param parameterValue A map of parameter key and value
     * @return A list of query result
     */
    List<LinkedHashMap<String, Object>> execute(String query, LinkedHashMap<String, Object> parameterValue);

    /**
     * Execute given update query
     * @param query A query string
     */
    void executeUpdate(String query);

    /**
     * Executes given update query by replacing given parameter in query
     * @param query A query string
     * @param parameterValue A map of parameter key and value
     */
    void executeUpdateQuery(String query, LinkedHashMap<String, Object> parameterValue);

    void executeUpdateQueryWithoutCast(String query, LinkedHashMap<String, Object> parameterValue);

}
