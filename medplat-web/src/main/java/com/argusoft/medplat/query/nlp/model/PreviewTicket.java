package com.argusoft.medplat.query.nlp.model;

import java.time.Instant;
import java.util.LinkedHashMap;

public class PreviewTicket {

    private final String id;
    private final String sql;
    private final LinkedHashMap<String, Object> parameters;
    private final Instant createdAt;

    public PreviewTicket(String id, String sql, LinkedHashMap<String, Object> parameters, Instant createdAt) {
        this.id = id;
        this.sql = sql;
        this.parameters = parameters;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getSql() {
        return sql;
    }

    public LinkedHashMap<String, Object> getParameters() {
        return parameters;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
