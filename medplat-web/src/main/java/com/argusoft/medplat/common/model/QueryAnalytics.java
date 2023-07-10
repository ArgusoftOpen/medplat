/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *     Defines fields related to query analytics
 * </p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */

@Entity
@Table(name = "query_analytics")
public class QueryAnalytics implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
    @Column(name = "query")
    private String query;
    
    @Column(name="execution_time")
    private Integer executionTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }
    
    
}
