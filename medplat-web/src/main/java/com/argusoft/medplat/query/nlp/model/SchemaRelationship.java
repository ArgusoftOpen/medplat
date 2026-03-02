package com.argusoft.medplat.query.nlp.model;

public class SchemaRelationship {

    private final String relatedTableKey;
    private final String joinExpression;

    public SchemaRelationship(String relatedTableKey, String joinExpression) {
        this.relatedTableKey = relatedTableKey;
        this.joinExpression = joinExpression;
    }

    public String getRelatedTableKey() {
        return relatedTableKey;
    }

    public String getJoinExpression() {
        return joinExpression;
    }
}
