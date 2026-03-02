package com.argusoft.medplat.analytics.plan;

import java.util.HashMap;
import java.util.Map;

public class QueryPlan {

    private String entity;
    private String aggregation; 
    private Map<String, Object> filters = new HashMap<>();

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void addFilter(String field, Object value) {
        this.filters.put(field, value);
    }
}