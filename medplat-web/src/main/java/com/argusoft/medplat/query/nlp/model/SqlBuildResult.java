package com.argusoft.medplat.query.nlp.model;

import java.util.LinkedHashMap;
import java.util.List;

public class SqlBuildResult {

    private final String sql;
    private final LinkedHashMap<String, Object> parameters;
    private final String table;
    private final List<String> selectedColumns;

    public SqlBuildResult(String sql, LinkedHashMap<String, Object> parameters, String table, List<String> selectedColumns) {
        this.sql = sql;
        this.parameters = parameters;
        this.table = table;
        this.selectedColumns = selectedColumns;
    }

    public String getSql() {
        return sql;
    }

    public LinkedHashMap<String, Object> getParameters() {
        return parameters;
    }

    public String getTable() {
        return table;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }
}
