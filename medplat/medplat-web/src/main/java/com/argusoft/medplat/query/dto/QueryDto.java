/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Defines fields of query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public class QueryDto {
    
    private String code;
    private List<LinkedHashMap<String,Object>> result;
    private LinkedHashMap<String,Object> parameters;
    private Integer sequence;

    public String getCode() {
        return code;
    }

    public void setCode(String queryCode) {
        this.code = queryCode;
    }

    public List<LinkedHashMap<String, Object>> getResult() {
        return result;
    }

    public void setResult(List<LinkedHashMap<String, Object>> resultList) {
        this.result = resultList;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public LinkedHashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "QueryDto{" + "code=" + code + ", result=" + result + ", parameters=" + parameters + ", sequence=" + sequence + '}';
    }
}
