package com.argusoft.medplat.query.nlp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SchemaTable {

    private final String key;
    private final String dbTable;
    private final String alias;
    private final Set<String> aliases;
    private final List<String> defaultColumns;
    private final Map<String, SchemaColumn> columns;
    private final List<SchemaRelationship> relationships;

    public SchemaTable(String key, String dbTable, String alias, Set<String> aliases,
                       List<String> defaultColumns, Map<String, SchemaColumn> columns,
                       List<SchemaRelationship> relationships) {
        this.key = key;
        this.dbTable = dbTable;
        this.alias = alias;
        this.aliases = Collections.unmodifiableSet(new LinkedHashSet<>(aliases));
        this.defaultColumns = Collections.unmodifiableList(new ArrayList<>(defaultColumns));
        this.columns = Collections.unmodifiableMap(new LinkedHashMap<>(columns));
        this.relationships = Collections.unmodifiableList(new ArrayList<>(relationships));
    }

    public String getKey() {
        return key;
    }

    public String getDbTable() {
        return dbTable;
    }

    public String getAlias() {
        return alias;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public List<String> getDefaultColumns() {
        return defaultColumns;
    }

    public Map<String, SchemaColumn> getColumns() {
        return columns;
    }

    public List<SchemaRelationship> getRelationships() {
        return relationships;
    }
}
