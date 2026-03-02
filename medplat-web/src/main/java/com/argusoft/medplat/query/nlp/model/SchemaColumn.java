package com.argusoft.medplat.query.nlp.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class SchemaColumn {

    private final String key;
    private final String dbColumn;
    private final Set<String> aliases;
    private final ColumnValueType valueType;
    private final boolean selectable;
    private final boolean filterable;

    public SchemaColumn(String key, String dbColumn, Set<String> aliases, ColumnValueType valueType, boolean selectable, boolean filterable) {
        this.key = key;
        this.dbColumn = dbColumn;
        this.aliases = Collections.unmodifiableSet(new LinkedHashSet<>(aliases));
        this.valueType = valueType;
        this.selectable = selectable;
        this.filterable = filterable;
    }

    public String getKey() {
        return key;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public ColumnValueType getValueType() {
        return valueType;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isFilterable() {
        return filterable;
    }
}
