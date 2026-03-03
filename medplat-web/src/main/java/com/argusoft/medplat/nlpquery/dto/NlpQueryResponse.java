package com.argusoft.medplat.nlpquery.dto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Response DTO for NLP query processing.
 * Contains the generated SQL, preview information, and execution results.
 *
 * @author medplat
 * @since 02/03/2026
 */
public class NlpQueryResponse {

    private String originalQuery;
    private String generatedSql;
    private String interpretedIntent;
    private String targetTable;
    private List<String> selectedColumns;
    private List<String> conditions;
    private boolean valid;
    private String errorMessage;
    private List<String> suggestions;
    private List<LinkedHashMap<String, Object>> results;
    private boolean executed;
    private int resultCount;
    private String previewDescription;

    public NlpQueryResponse() {
        this.valid = true;
        this.executed = false;
    }

    // --- Getters and Setters ---

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public String getGeneratedSql() {
        return generatedSql;
    }

    public void setGeneratedSql(String generatedSql) {
        this.generatedSql = generatedSql;
    }

    public String getInterpretedIntent() {
        return interpretedIntent;
    }

    public void setInterpretedIntent(String interpretedIntent) {
        this.interpretedIntent = interpretedIntent;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<LinkedHashMap<String, Object>> getResults() {
        return results;
    }

    public void setResults(List<LinkedHashMap<String, Object>> results) {
        this.results = results;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public String getPreviewDescription() {
        return previewDescription;
    }

    public void setPreviewDescription(String previewDescription) {
        this.previewDescription = previewDescription;
    }

    @Override
    public String toString() {
        return "NlpQueryResponse{" +
                "originalQuery='" + originalQuery + '\'' +
                ", generatedSql='" + generatedSql + '\'' +
                ", valid=" + valid +
                ", executed=" + executed +
                ", resultCount=" + resultCount +
                '}';
    }
}
