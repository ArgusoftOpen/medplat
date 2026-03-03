package com.argusoft.medplat.nlpquery.schema;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Maps natural language keywords to the actual database schema (tables, columns, relationships).
 * This is the central knowledge base that the NLP parser references to understand user intent.
 *
 * <p>Designed to be extensible - future enhancements can load mappings from a database table
 * or configuration file instead of hardcoded values.</p>
 *
 * @author medplat
 * @since 02/03/2026
 */
@Component
public class SchemaMapping {

    // Maps natural language keywords -> actual table names
    private final Map<String, String> tableKeywordMap = new LinkedHashMap<>();

    // Maps table -> list of allowed columns
    private final Map<String, List<String>> tableColumnsMap = new LinkedHashMap<>();

    // Maps natural language column keywords -> actual column names (per table)
    private final Map<String, Map<String, String>> columnKeywordMap = new LinkedHashMap<>();

    // Maps table relationships for potential JOIN support
    private final Map<String, List<TableRelationship>> tableRelationships = new LinkedHashMap<>();

    // Known aggregate function keywords
    private final Map<String, String> aggregateFunctions = new LinkedHashMap<>();

    // Known comparison operator keywords
    private final Map<String, String> operatorKeywords = new LinkedHashMap<>();

    // Known sort keywords
    private final Map<String, String> sortKeywords = new LinkedHashMap<>();

    @PostConstruct
    public void initialize() {
        initializeTableKeywords();
        initializeTableColumns();
        initializeColumnKeywords();
        initializeTableRelationships();
        initializeAggregateFunctions();
        initializeOperatorKeywords();
        initializeSortKeywords();
    }

    private void initializeTableKeywords() {
        // Users / Members
        tableKeywordMap.put("users", "um_user_master");
        tableKeywordMap.put("user", "um_user_master");
        tableKeywordMap.put("members", "imt_member");
        tableKeywordMap.put("member", "imt_member");
        tableKeywordMap.put("people", "imt_member");
        tableKeywordMap.put("person", "imt_member");
        tableKeywordMap.put("patients", "imt_member");
        tableKeywordMap.put("patient", "imt_member");

        // Family
        tableKeywordMap.put("families", "imt_family");
        tableKeywordMap.put("family", "imt_family");
        tableKeywordMap.put("household", "imt_family");
        tableKeywordMap.put("households", "imt_family");

        // Location
        tableKeywordMap.put("locations", "location_master");
        tableKeywordMap.put("location", "location_master");
        tableKeywordMap.put("village", "location_master");
        tableKeywordMap.put("villages", "location_master");
        tableKeywordMap.put("district", "location_master");
        tableKeywordMap.put("districts", "location_master");
        tableKeywordMap.put("area", "location_master");
        tableKeywordMap.put("areas", "location_master");

        // Health Infrastructure
        tableKeywordMap.put("health infrastructure", "health_infrastructure_details");
        tableKeywordMap.put("health facility", "health_infrastructure_details");
        tableKeywordMap.put("hospital", "health_infrastructure_details");
        tableKeywordMap.put("hospitals", "health_infrastructure_details");
        tableKeywordMap.put("clinic", "health_infrastructure_details");
        tableKeywordMap.put("clinics", "health_infrastructure_details");
        tableKeywordMap.put("facilities", "health_infrastructure_details");
        tableKeywordMap.put("facility", "health_infrastructure_details");

        // Notifications
        tableKeywordMap.put("notifications", "notification_master");
        tableKeywordMap.put("notification", "notification_master");
        tableKeywordMap.put("alerts", "notification_master");
        tableKeywordMap.put("alert", "notification_master");

        // Events
        tableKeywordMap.put("events", "event_master");
        tableKeywordMap.put("event", "event_master");

        // Query Master (existing analytics queries)
        tableKeywordMap.put("queries", "query_master");
        tableKeywordMap.put("query", "query_master");
        tableKeywordMap.put("reports", "query_master");
        tableKeywordMap.put("report", "query_master");
    }

    private void initializeTableColumns() {
        // um_user_master columns
        tableColumnsMap.put("um_user_master", Arrays.asList(
                "id", "user_name", "first_name", "last_name", "contact_number",
                "email_id", "gender", "role_id", "state", "created_on",
                "modified_on", "preferred_language", "aadhar_number"
        ));

        // imt_member columns
        tableColumnsMap.put("imt_member", Arrays.asList(
                "id", "family_id", "first_name", "middle_name", "last_name",
                "gender", "dob", "marital_status", "mobile_number",
                "aadhar_number", "is_pregnant", "state", "created_on",
                "modified_on", "cur_living_status", "unique_health_id"
        ));

        // imt_family columns
        tableColumnsMap.put("imt_family", Arrays.asList(
                "id", "family_id", "location_id", "area_id",
                "state", "created_on", "modified_on", "anganwadi_id",
                "bpl_flag", "religion", "caste"
        ));

        // location_master columns
        tableColumnsMap.put("location_master", Arrays.asList(
                "id", "name", "type", "parent", "state",
                "created_on", "modified_on", "english_name", "level"
        ));

        // health_infrastructure_details columns
        tableColumnsMap.put("health_infrastructure_details", Arrays.asList(
                "id", "name", "type", "location_id", "state",
                "created_on", "modified_on", "address", "contact_number"
        ));

        // notification_master columns
        tableColumnsMap.put("notification_master", Arrays.asList(
                "id", "notification_type_id", "state", "created_on",
                "modified_on", "location_id", "user_id", "member_id"
        ));

        // event_master columns
        tableColumnsMap.put("event_master", Arrays.asList(
                "id", "event_name", "state", "is_active",
                "created_on", "modified_on"
        ));

        // query_master columns
        tableColumnsMap.put("query_master", Arrays.asList(
                "uuid", "query", "description", "code",
                "returns_result_set", "params", "state", "is_public"
        ));
    }

    private void initializeColumnKeywords() {
        // User column keywords
        Map<String, String> userColumns = new LinkedHashMap<>();
        userColumns.put("name", "first_name");
        userColumns.put("first name", "first_name");
        userColumns.put("last name", "last_name");
        userColumns.put("username", "user_name");
        userColumns.put("phone", "contact_number");
        userColumns.put("mobile", "contact_number");
        userColumns.put("contact", "contact_number");
        userColumns.put("email", "email_id");
        userColumns.put("gender", "gender");
        userColumns.put("role", "role_id");
        userColumns.put("status", "state");
        userColumns.put("state", "state");
        userColumns.put("created", "created_on");
        userColumns.put("created on", "created_on");
        userColumns.put("modified", "modified_on");
        userColumns.put("language", "preferred_language");
        userColumns.put("aadhar", "aadhar_number");
        columnKeywordMap.put("um_user_master", userColumns);

        // Member column keywords
        Map<String, String> memberColumns = new LinkedHashMap<>();
        memberColumns.put("name", "first_name");
        memberColumns.put("first name", "first_name");
        memberColumns.put("middle name", "middle_name");
        memberColumns.put("last name", "last_name");
        memberColumns.put("gender", "gender");
        memberColumns.put("age", "dob");
        memberColumns.put("date of birth", "dob");
        memberColumns.put("dob", "dob");
        memberColumns.put("birthday", "dob");
        memberColumns.put("marital status", "marital_status");
        memberColumns.put("marital", "marital_status");
        memberColumns.put("married", "marital_status");
        memberColumns.put("phone", "mobile_number");
        memberColumns.put("mobile", "mobile_number");
        memberColumns.put("contact", "mobile_number");
        memberColumns.put("aadhar", "aadhar_number");
        memberColumns.put("pregnant", "is_pregnant");
        memberColumns.put("pregnancy", "is_pregnant");
        memberColumns.put("status", "state");
        memberColumns.put("state", "state");
        memberColumns.put("living status", "cur_living_status");
        memberColumns.put("health id", "unique_health_id");
        memberColumns.put("family", "family_id");
        columnKeywordMap.put("imt_member", memberColumns);

        // Family column keywords
        Map<String, String> familyColumns = new LinkedHashMap<>();
        familyColumns.put("family id", "family_id");
        familyColumns.put("location", "location_id");
        familyColumns.put("area", "area_id");
        familyColumns.put("status", "state");
        familyColumns.put("state", "state");
        familyColumns.put("bpl", "bpl_flag");
        familyColumns.put("below poverty", "bpl_flag");
        familyColumns.put("religion", "religion");
        familyColumns.put("caste", "caste");
        familyColumns.put("anganwadi", "anganwadi_id");
        columnKeywordMap.put("imt_family", familyColumns);

        // Location column keywords
        Map<String, String> locationColumns = new LinkedHashMap<>();
        locationColumns.put("name", "name");
        locationColumns.put("type", "type");
        locationColumns.put("parent", "parent");
        locationColumns.put("status", "state");
        locationColumns.put("state", "state");
        locationColumns.put("english name", "english_name");
        locationColumns.put("level", "level");
        columnKeywordMap.put("location_master", locationColumns);

        // Health infrastructure column keywords
        Map<String, String> healthColumns = new LinkedHashMap<>();
        healthColumns.put("name", "name");
        healthColumns.put("type", "type");
        healthColumns.put("location", "location_id");
        healthColumns.put("status", "state");
        healthColumns.put("state", "state");
        healthColumns.put("address", "address");
        healthColumns.put("phone", "contact_number");
        healthColumns.put("contact", "contact_number");
        columnKeywordMap.put("health_infrastructure_details", healthColumns);
    }

    private void initializeTableRelationships() {
        // Member -> Family
        List<TableRelationship> memberRelations = new ArrayList<>();
        memberRelations.add(new TableRelationship("imt_member", "imt_family", "family_id", "family_id"));
        tableRelationships.put("imt_member", memberRelations);

        // Family -> Location
        List<TableRelationship> familyRelations = new ArrayList<>();
        familyRelations.add(new TableRelationship("imt_family", "location_master", "location_id", "id"));
        tableRelationships.put("imt_family", familyRelations);
    }

    private void initializeAggregateFunctions() {
        aggregateFunctions.put("count", "COUNT");
        aggregateFunctions.put("total", "COUNT");
        aggregateFunctions.put("number of", "COUNT");
        aggregateFunctions.put("how many", "COUNT");
        aggregateFunctions.put("sum", "SUM");
        aggregateFunctions.put("total of", "SUM");
        aggregateFunctions.put("average", "AVG");
        aggregateFunctions.put("avg", "AVG");
        aggregateFunctions.put("mean", "AVG");
        aggregateFunctions.put("maximum", "MAX");
        aggregateFunctions.put("max", "MAX");
        aggregateFunctions.put("highest", "MAX");
        aggregateFunctions.put("minimum", "MIN");
        aggregateFunctions.put("min", "MIN");
        aggregateFunctions.put("lowest", "MIN");
    }

    private void initializeOperatorKeywords() {
        operatorKeywords.put("equals", "=");
        operatorKeywords.put("equal to", "=");
        operatorKeywords.put("is", "=");
        operatorKeywords.put("greater than", ">");
        operatorKeywords.put("more than", ">");
        operatorKeywords.put("above", ">");
        operatorKeywords.put("over", ">");
        operatorKeywords.put("less than", "<");
        operatorKeywords.put("below", "<");
        operatorKeywords.put("under", "<");
        operatorKeywords.put("not equal", "!=");
        operatorKeywords.put("not", "!=");
        operatorKeywords.put("like", "LIKE");
        operatorKeywords.put("contains", "LIKE");
        operatorKeywords.put("containing", "LIKE");
        operatorKeywords.put("starts with", "LIKE");
        operatorKeywords.put("starting with", "LIKE");
    }

    private void initializeSortKeywords() {
        sortKeywords.put("ascending", "ASC");
        sortKeywords.put("asc", "ASC");
        sortKeywords.put("oldest first", "ASC");
        sortKeywords.put("a to z", "ASC");
        sortKeywords.put("descending", "DESC");
        sortKeywords.put("desc", "DESC");
        sortKeywords.put("newest first", "DESC");
        sortKeywords.put("z to a", "DESC");
        sortKeywords.put("latest", "DESC");
        sortKeywords.put("recent", "DESC");
        sortKeywords.put("newest", "DESC");
    }

    // --- Public accessor methods ---

    public String resolveTable(String keyword) {
        if (keyword == null) return null;
        String lowerKey = keyword.toLowerCase().trim();
        return tableKeywordMap.get(lowerKey);
    }

    public String resolveColumn(String tableName, String keyword) {
        if (tableName == null || keyword == null) return null;
        Map<String, String> columns = columnKeywordMap.get(tableName);
        if (columns == null) return null;
        String lowerKey = keyword.toLowerCase().trim();
        return columns.get(lowerKey);
    }

    public List<String> getAllowedColumns(String tableName) {
        return tableColumnsMap.getOrDefault(tableName, Collections.emptyList());
    }

    public boolean isValidColumn(String tableName, String columnName) {
        List<String> columns = tableColumnsMap.get(tableName);
        return columns != null && columns.contains(columnName);
    }

    public boolean isValidTable(String tableName) {
        return tableColumnsMap.containsKey(tableName);
    }

    public Map<String, String> getTableKeywordMap() {
        return Collections.unmodifiableMap(tableKeywordMap);
    }

    public Map<String, String> getAggregateFunctions() {
        return Collections.unmodifiableMap(aggregateFunctions);
    }

    public Map<String, String> getOperatorKeywords() {
        return Collections.unmodifiableMap(operatorKeywords);
    }

    public Map<String, String> getSortKeywords() {
        return Collections.unmodifiableMap(sortKeywords);
    }

    public List<TableRelationship> getRelationships(String tableName) {
        return tableRelationships.getOrDefault(tableName, Collections.emptyList());
    }

    public Set<String> getAllTableKeywords() {
        return Collections.unmodifiableSet(tableKeywordMap.keySet());
    }

    public Set<String> getAllTableNames() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(tableKeywordMap.values()));
    }

    public Map<String, String> getColumnKeywordsForTable(String tableName) {
        Map<String, String> columns = columnKeywordMap.get(tableName);
        return columns != null ? Collections.unmodifiableMap(columns) : null;
    }

    /**
     * Inner class representing a table relationship (for future JOIN support).
     */
    public static class TableRelationship {
        private final String sourceTable;
        private final String targetTable;
        private final String sourceColumn;
        private final String targetColumn;

        public TableRelationship(String sourceTable, String targetTable, String sourceColumn, String targetColumn) {
            this.sourceTable = sourceTable;
            this.targetTable = targetTable;
            this.sourceColumn = sourceColumn;
            this.targetColumn = targetColumn;
        }

        public String getSourceTable() { return sourceTable; }
        public String getTargetTable() { return targetTable; }
        public String getSourceColumn() { return sourceColumn; }
        public String getTargetColumn() { return targetColumn; }
    }
}
