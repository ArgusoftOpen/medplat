package com.argusoft.medplat.query.nlp.model;

import java.util.ArrayList;
import java.util.List;

public class NlpParsedQuery {

    private String tableKey;
    private boolean countQuery;
    private List<String> selectedColumnKeys = new ArrayList<>();
    private List<NlpCondition> conditions = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
    private Integer limit;

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    public boolean isCountQuery() {
        return countQuery;
    }

    public void setCountQuery(boolean countQuery) {
        this.countQuery = countQuery;
    }

    public List<String> getSelectedColumnKeys() {
        return selectedColumnKeys;
    }

    public void setSelectedColumnKeys(List<String> selectedColumnKeys) {
        this.selectedColumnKeys = selectedColumnKeys;
    }

    public List<NlpCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<NlpCondition> conditions) {
        this.conditions = conditions;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
