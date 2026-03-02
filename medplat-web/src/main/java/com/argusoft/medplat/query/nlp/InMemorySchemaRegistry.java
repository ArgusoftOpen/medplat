package com.argusoft.medplat.query.nlp;

import com.argusoft.medplat.query.nlp.model.ColumnValueType;
import com.argusoft.medplat.query.nlp.model.SchemaColumn;
import com.argusoft.medplat.query.nlp.model.SchemaRelationship;
import com.argusoft.medplat.query.nlp.model.SchemaTable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemorySchemaRegistry implements SchemaRegistry {

    private final Map<String, SchemaTable> tableMap;

    public InMemorySchemaRegistry() {
        this.tableMap = new LinkedHashMap<>();
        tableMap.put("member", buildMemberTable());
        tableMap.put("family", buildFamilyTable());
    }

    @Override
    public Collection<SchemaTable> getTables() {
        return tableMap.values();
    }

    @Override
    public SchemaTable getTable(String tableKey) {
        return tableMap.get(tableKey);
    }

    private SchemaTable buildMemberTable() {
        Map<String, SchemaColumn> columns = new LinkedHashMap<>();
        columns.put("id", new SchemaColumn("id", "id", setOf("id", "member id"), ColumnValueType.NUMBER, true, true));
        columns.put("name", new SchemaColumn("name", "first_name", setOf("name", "member name", "first name"), ColumnValueType.STRING, true, true));
        columns.put("age", new SchemaColumn("age", "age", setOf("age"), ColumnValueType.NUMBER, true, true));
        columns.put("gender", new SchemaColumn("gender", "gender", setOf("gender", "sex"), ColumnValueType.STRING, true, true));
        columns.put("mobile", new SchemaColumn("mobile", "mobile_number", setOf("mobile", "phone", "phone number"), ColumnValueType.STRING, true, true));
        columns.put("family_id", new SchemaColumn("family_id", "family_id", setOf("family id"), ColumnValueType.NUMBER, true, true));
        return new SchemaTable(
                "member",
                "imt_member",
                "m",
                setOf("member", "members", "beneficiary", "beneficiaries", "person", "people"),
                Arrays.asList("id", "name", "age", "gender"),
                columns,
                Collections.singletonList(new SchemaRelationship("family", "m.family_id = f.family_id"))
        );
    }

    private SchemaTable buildFamilyTable() {
        Map<String, SchemaColumn> columns = new LinkedHashMap<>();
        columns.put("id", new SchemaColumn("id", "family_id", setOf("id", "family id"), ColumnValueType.NUMBER, true, true));
        columns.put("head_name", new SchemaColumn("head_name", "head_of_family", setOf("head", "head name", "family head"), ColumnValueType.STRING, true, true));
        columns.put("village", new SchemaColumn("village", "village_name", setOf("village"), ColumnValueType.STRING, true, true));
        columns.put("member_count", new SchemaColumn("member_count", "member_count", setOf("members", "member count", "family size"), ColumnValueType.NUMBER, true, true));
        return new SchemaTable(
                "family",
                "imt_family",
                "f",
                setOf("family", "families", "household", "households"),
                Arrays.asList("id", "head_name", "village", "member_count"),
                columns,
                Collections.singletonList(new SchemaRelationship("member", "f.family_id = m.family_id"))
        );
    }

    private Set<String> setOf(String... values) {
        return Set.copyOf(Arrays.asList(values));
    }
}
