package com.argusoft.medplat.query.nlp.dto;

import java.util.LinkedHashMap;
import java.util.List;

public class NlpQueryExecuteResponseDto {

    private String previewId;
    private String executedSql;
    private Integer rowCount;
    private List<LinkedHashMap<String, Object>> rows;

    public String getPreviewId() {
        return previewId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }

    public String getExecutedSql() {
        return executedSql;
    }

    public void setExecutedSql(String executedSql) {
        this.executedSql = executedSql;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public List<LinkedHashMap<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<LinkedHashMap<String, Object>> rows) {
        this.rows = rows;
    }
}
