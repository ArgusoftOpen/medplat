package com.argusoft.medplat.nlpquery.dto;

/**
 * DTO that encapsulates a confirmed query execution request.
 * Used when the user reviews the generated SQL and confirms execution.
 *
 * @author medplat
 * @since 02/03/2026
 */
public class NlpQueryConfirmRequest {

    private String generatedSql;
    private String originalQuery;

    public NlpQueryConfirmRequest() {
    }

    public String getGeneratedSql() {
        return generatedSql;
    }

    public void setGeneratedSql(String generatedSql) {
        this.generatedSql = generatedSql;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    @Override
    public String toString() {
        return "NlpQueryConfirmRequest{" +
                "generatedSql='" + generatedSql + '\'' +
                ", originalQuery='" + originalQuery + '\'' +
                '}';
    }
}
