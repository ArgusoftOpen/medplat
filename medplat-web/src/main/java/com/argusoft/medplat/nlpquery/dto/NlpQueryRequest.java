package com.argusoft.medplat.nlpquery.dto;

/**
 * Request DTO for NLP query processing.
 * Contains the natural language query input from the user.
 *
 * @author medplat
 * @since 02/03/2026
 */
public class NlpQueryRequest {

    private String naturalLanguageQuery;
    private boolean executeImmediately;

    public NlpQueryRequest() {
    }

    public NlpQueryRequest(String naturalLanguageQuery) {
        this.naturalLanguageQuery = naturalLanguageQuery;
        this.executeImmediately = false;
    }

    public String getNaturalLanguageQuery() {
        return naturalLanguageQuery;
    }

    public void setNaturalLanguageQuery(String naturalLanguageQuery) {
        this.naturalLanguageQuery = naturalLanguageQuery;
    }

    public boolean isExecuteImmediately() {
        return executeImmediately;
    }

    public void setExecuteImmediately(boolean executeImmediately) {
        this.executeImmediately = executeImmediately;
    }

    @Override
    public String toString() {
        return "NlpQueryRequest{" +
                "naturalLanguageQuery='" + naturalLanguageQuery + '\'' +
                ", executeImmediately=" + executeImmediately +
                '}';
    }
}
