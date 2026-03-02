package com.argusoft.medplat.query.nlp;

import com.argusoft.medplat.query.nlp.model.SchemaTable;

import java.util.Collection;

public interface SchemaRegistry {

    Collection<SchemaTable> getTables();

    SchemaTable getTable(String tableKey);
}
