package com.argusoft.medplat.query.nlp.dto;

public class NlpQueryRequestDto {

    private String queryText;
    private Integer limit;

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
