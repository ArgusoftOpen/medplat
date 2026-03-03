package com.argusoft.medplat.nlpquery.parser;

import com.argusoft.medplat.nlpquery.schema.SchemaMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Rule-based NLP parsing engine that interprets natural language queries
 * and converts them into a structured ParsedQuery representation.
 *
 * <p>The parser works in multiple phases:
 * <ol>
 *   <li>Tokenization and normalization</li>
 *   <li>Intent detection (SELECT, COUNT, etc.)</li>
 *   <li>Table identification</li>
 *   <li>Column extraction</li>
 *   <li>WHERE condition parsing</li>
 *   <li>ORDER BY and LIMIT detection</li>
 * </ol>
 * </p>
 *
 * <p>This modular design allows future replacement with ML-based or AI-driven
 * parsing without changing the downstream SQL generation logic.</p>
 *
 * @author medplat
 * @since 02/03/2026
 */
@Component
public class NlpQueryParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(NlpQueryParser.class);

    @Autowired
    private SchemaMapping schemaMapping;

    // Patterns for extracting numbers
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\b(\\d+)\\b");

    // Patterns for 'where' / 'with' / 'having' style conditions
    private static final Pattern CONDITION_PATTERN = Pattern.compile(
            "(?:where|with|having|whose|that have|who have|who are|that are|which are|which have)\\s+(.+)",
            Pattern.CASE_INSENSITIVE
    );

    // Pattern for "order by" / "sort by"
    private static final Pattern ORDER_PATTERN = Pattern.compile(
            "(?:order(?:ed)?\\s+by|sort(?:ed)?\\s+by|sorted)\\s+(\\w+)(?:\\s+(asc|desc|ascending|descending))?",
            Pattern.CASE_INSENSITIVE
    );

    // Pattern for limit
    private static final Pattern LIMIT_PATTERN = Pattern.compile(
            "(?:limit(?:ed)?\\s+(?:to\\s+)?|top\\s+|first\\s+|last\\s+)(\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Parse a natural language query into a structured ParsedQuery.
     *
     * @param naturalLanguageQuery The raw NL input from the user
     * @return A ParsedQuery object representing the parsed intent
     */
    public ParsedQuery parse(String naturalLanguageQuery) {
        ParsedQuery parsed = new ParsedQuery();

        if (naturalLanguageQuery == null || naturalLanguageQuery.trim().isEmpty()) {
            parsed.setValid(false);
            parsed.setErrorMessage("Query cannot be empty. Please enter a natural language query.");
            parsed.setSuggestions(getExampleQueries());
            return parsed;
        }

        String query = naturalLanguageQuery.trim().toLowerCase();
        LOGGER.info("Parsing natural language query: {}", query);

        // Phase 1: Detect intent (aggregate or simple select)
        detectIntent(query, parsed);

        // Phase 2: Identify target table
        detectTable(query, parsed);
        if (!parsed.isValid()) {
            return parsed;
        }

        // Phase 3: Extract columns
        detectColumns(query, parsed);

        // Phase 4: Parse WHERE conditions
        detectConditions(query, parsed);

        // Phase 5: Detect ORDER BY
        detectOrderBy(query, parsed);

        // Phase 6: Detect LIMIT
        detectLimit(query, parsed);

        LOGGER.info("Parsed query result: table={}, columns={}, conditions={}, aggregate={}",
                parsed.getTableName(), parsed.getColumns(), parsed.getConditions(), parsed.getAggregateFunction());

        return parsed;
    }

    /**
     * Phase 1: Detect the intent of the query (COUNT, SUM, AVG, or regular SELECT).
     */
    private void detectIntent(String query, ParsedQuery parsed) {
        Map<String, String> aggregates = schemaMapping.getAggregateFunctions();

        for (Map.Entry<String, String> entry : aggregates.entrySet()) {
            if (query.contains(entry.getKey())) {
                parsed.setIntent("AGGREGATE");
                parsed.setAggregateFunction(entry.getValue());
                LOGGER.debug("Detected aggregate intent: {}", entry.getValue());
                return;
            }
        }

        // Check for "show", "list", "get", "find", "display", "fetch", "retrieve", "all"
        if (query.matches(".*\\b(show|list|get|find|display|fetch|retrieve|select|all|give)\\b.*")) {
            parsed.setIntent("SELECT");
        } else {
            parsed.setIntent("SELECT"); // Default intent
        }
    }

    /**
     * Phase 2: Identify which database table the query is about.
     */
    private void detectTable(String query, ParsedQuery parsed) {
        String bestMatch = null;
        String bestKeyword = null;
        int bestMatchLength = 0;

        // Try multi-word keywords first (longer match wins)
        for (String keyword : schemaMapping.getAllTableKeywords()) {
            if (query.contains(keyword) && keyword.length() > bestMatchLength) {
                bestMatch = schemaMapping.resolveTable(keyword);
                bestKeyword = keyword;
                bestMatchLength = keyword.length();
            }
        }

        if (bestMatch != null) {
            parsed.setTableName(bestMatch);
            parsed.setTableKeyword(bestKeyword);
            LOGGER.debug("Detected table: {} (from keyword: {})", bestMatch, bestKeyword);
        } else {
            parsed.setValid(false);
            parsed.setErrorMessage("Could not identify the data source. Please mention what data you want " +
                    "(e.g., 'users', 'members', 'families', 'locations', 'hospitals').");
            parsed.setSuggestions(Arrays.asList(
                    "Show all users",
                    "Count members where gender is male",
                    "List all families",
                    "Show locations where type is village",
                    "Find hospitals",
                    "Get all patients where state is active"
            ));
        }
    }

    /**
     * Phase 3: Extract which columns the user wants to see.
     */
    private void detectColumns(String query, ParsedQuery parsed) {
        String tableName = parsed.getTableName();
        List<String> detectedColumns = new ArrayList<>();
        Map<String, String> columnKeywords = getColumnKeywordsForTable(tableName);

        if (columnKeywords != null) {
            // Check for multi-word keywords first (sorted by length desc)
            List<Map.Entry<String, String>> sortedEntries = columnKeywords.entrySet().stream()
                    .sorted((a, b) -> b.getKey().length() - a.getKey().length())
                    .collect(Collectors.toList());

            for (Map.Entry<String, String> entry : sortedEntries) {
                if (query.contains(entry.getKey()) && !detectedColumns.contains(entry.getValue())) {
                    // Don't add columns that are part of condition values
                    detectedColumns.add(entry.getValue());
                }
            }
        }

        // If "all" is mentioned or no specific columns detected, use all columns
        if (detectedColumns.isEmpty() || query.contains("all")) {
            if ("AGGREGATE".equals(parsed.getIntent())) {
                // For aggregate intent, default to counting all
                parsed.setAggregateColumn("*");
            }
            // Set as empty to signal "SELECT *" in SQL generation
            parsed.setColumns(new ArrayList<>());
        } else {
            parsed.setColumns(detectedColumns);
            if ("AGGREGATE".equals(parsed.getIntent()) && !detectedColumns.isEmpty()) {
                parsed.setAggregateColumn(detectedColumns.get(0));
            }
        }
    }

    /**
     * Phase 4: Parse WHERE conditions from the query.
     */
    private void detectConditions(String query, ParsedQuery parsed) {
        List<ParsedQuery.WhereCondition> conditions = new ArrayList<>();
        String tableName = parsed.getTableName();

        Matcher conditionMatcher = CONDITION_PATTERN.matcher(query);
        if (conditionMatcher.find()) {
            String conditionPart = conditionMatcher.group(1).trim();
            parseConditionString(conditionPart, tableName, conditions);
        } else {
            // Try to find inline conditions like "active users" or "male members"
            detectInlineConditions(query, tableName, conditions);
        }

        parsed.setConditions(conditions);
    }

    /**
     * Parse explicit condition strings like "gender is male and state is active"
     */
    private void parseConditionString(String conditionStr, String tableName, List<ParsedQuery.WhereCondition> conditions) {
        // Split by 'and' or 'or'
        String[] parts = conditionStr.split("\\b(and|or)\\b");
        String[] logicalOps = extractLogicalOperators(conditionStr);

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty()) continue;

            ParsedQuery.WhereCondition condition = parseIndividualCondition(part, tableName);
            if (condition != null) {
                if (i > 0 && i - 1 < logicalOps.length) {
                    condition.setLogicalOperator(logicalOps[i - 1].toUpperCase());
                }
                conditions.add(condition);
            }
        }
    }

    /**
     * Parse an individual condition like "gender is male" or "state equals active"
     */
    private ParsedQuery.WhereCondition parseIndividualCondition(String part, String tableName) {
        Map<String, String> columnKeywords = getColumnKeywordsForTable(tableName);
        Map<String, String> operators = schemaMapping.getOperatorKeywords();

        if (columnKeywords == null) return null;

        // Find the column in this condition part
        String detectedColumn = null;
        String columnKeyword = null;

        // Sort by length descending to match longer phrases first
        List<Map.Entry<String, String>> sortedColumns = columnKeywords.entrySet().stream()
                .sorted((a, b) -> b.getKey().length() - a.getKey().length())
                .collect(Collectors.toList());

        for (Map.Entry<String, String> entry : sortedColumns) {
            if (part.contains(entry.getKey())) {
                detectedColumn = entry.getValue();
                columnKeyword = entry.getKey();
                break;
            }
        }

        if (detectedColumn == null) return null;

        // Find the operator
        String detectedOperator = "="; // default
        String operatorKeyword = "";

        List<Map.Entry<String, String>> sortedOps = operators.entrySet().stream()
                .sorted((a, b) -> b.getKey().length() - a.getKey().length())
                .collect(Collectors.toList());

        for (Map.Entry<String, String> entry : sortedOps) {
            if (part.contains(entry.getKey())) {
                detectedOperator = entry.getValue();
                operatorKeyword = entry.getKey();
                break;
            }
        }

        // Extract the value (everything after column keyword and operator keyword)
        String value = part;
        if (!columnKeyword.isEmpty()) {
            int afterColumn = part.indexOf(columnKeyword) + columnKeyword.length();
            value = part.substring(afterColumn).trim();
        }
        if (!operatorKeyword.isEmpty()) {
            int afterOp = value.indexOf(operatorKeyword);
            if (afterOp >= 0) {
                value = value.substring(afterOp + operatorKeyword.length()).trim();
            }
        }

        // Clean value
        value = value.replaceAll("^\\s*(is|equals|equal to|=)\\s*", "").trim();
        value = value.replaceAll("[\"']", "").trim();

        if (value.isEmpty()) return null;

        // Handle LIKE operator value
        if ("LIKE".equals(detectedOperator)) {
            if (part.contains("starts with") || part.contains("starting with")) {
                value = value + "%";
            } else {
                value = "%" + value + "%";
            }
        }

        return new ParsedQuery.WhereCondition(detectedColumn, detectedOperator, value);
    }

    /**
     * Detect inline conditions like "active users", "male members", "pregnant patients"
     */
    private void detectInlineConditions(String query, String tableName, List<ParsedQuery.WhereCondition> conditions) {
        // Check for state/status keywords
        if (query.contains("active")) {
            conditions.add(new ParsedQuery.WhereCondition("state", "=", "ACTIVE"));
        } else if (query.contains("inactive")) {
            conditions.add(new ParsedQuery.WhereCondition("state", "=", "INACTIVE"));
        }

        // Check for gender keywords
        if (tableName != null && (tableName.equals("imt_member") || tableName.equals("um_user_master"))) {
            if (query.contains("male") && !query.contains("female")) {
                conditions.add(new ParsedQuery.WhereCondition("gender", "=", "M"));
            } else if (query.contains("female")) {
                conditions.add(new ParsedQuery.WhereCondition("gender", "=", "F"));
            }
        }

        // Check for pregnancy
        if (tableName != null && tableName.equals("imt_member")) {
            if (query.contains("pregnant")) {
                conditions.add(new ParsedQuery.WhereCondition("is_pregnant", "=", "true"));
            }
        }
    }

    /**
     * Phase 5: Detect ORDER BY clauses.
     */
    private void detectOrderBy(String query, ParsedQuery parsed) {
        Matcher orderMatcher = ORDER_PATTERN.matcher(query);
        if (orderMatcher.find()) {
            String columnKeyword = orderMatcher.group(1);
            String direction = orderMatcher.group(2);

            String resolvedColumn = schemaMapping.resolveColumn(parsed.getTableName(), columnKeyword);
            if (resolvedColumn != null) {
                parsed.setOrderByColumn(resolvedColumn);
            } else if (schemaMapping.isValidColumn(parsed.getTableName(), columnKeyword)) {
                parsed.setOrderByColumn(columnKeyword);
            }

            if (direction != null) {
                String resolvedDir = schemaMapping.getSortKeywords().get(direction.toLowerCase());
                parsed.setOrderDirection(resolvedDir != null ? resolvedDir : "ASC");
            } else {
                parsed.setOrderDirection("ASC");
            }
        } else {
            // Check for standalone sort keywords
            for (Map.Entry<String, String> entry : schemaMapping.getSortKeywords().entrySet()) {
                if (query.contains(entry.getKey())) {
                    parsed.setOrderDirection(entry.getValue());
                    if (parsed.getOrderByColumn() == null) {
                        parsed.setOrderByColumn("created_on");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Phase 6: Detect LIMIT clause.
     */
    private void detectLimit(String query, ParsedQuery parsed) {
        Matcher limitMatcher = LIMIT_PATTERN.matcher(query);
        if (limitMatcher.find()) {
            try {
                int limit = Integer.parseInt(limitMatcher.group(1));
                parsed.setLimit(Math.min(limit, 1000)); // Cap at 1000 for safety
            } catch (NumberFormatException e) {
                LOGGER.debug("Could not parse limit value");
            }
        }

        // Check for "top N" pattern
        Pattern topPattern = Pattern.compile("\\btop\\s+(\\d+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher topMatcher = topPattern.matcher(query);
        if (topMatcher.find() && parsed.getLimit() == null) {
            try {
                int limit = Integer.parseInt(topMatcher.group(1));
                parsed.setLimit(Math.min(limit, 1000));
            } catch (NumberFormatException e) {
                LOGGER.debug("Could not parse top value");
            }
        }

        // Default limit if none specified
        if (parsed.getLimit() == null) {
            parsed.setLimit(100); // Default safety limit
        }
    }

    /**
     * Get column keywords map for a given table.
     */
    private Map<String, String> getColumnKeywordsForTable(String tableName) {
        return schemaMapping.getColumnKeywordsForTable(tableName);
    }

    /**
     * Extract logical operators (AND/OR) from a condition string.
     */
    private String[] extractLogicalOperators(String conditionStr) {
        List<String> ops = new ArrayList<>();
        Pattern logicalPattern = Pattern.compile("\\b(and|or)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = logicalPattern.matcher(conditionStr);
        while (matcher.find()) {
            ops.add(matcher.group(1));
        }
        return ops.toArray(new String[0]);
    }

    /**
     * Return example queries for user guidance.
     */
    public List<String> getExampleQueries() {
        return Arrays.asList(
                "Show all users",
                "Count members where gender is male",
                "List all active members",
                "Show families where state is active",
                "Find locations where type is village",
                "Get all hospitals",
                "Show top 10 users sorted by created on descending",
                "Count pregnant patients",
                "How many female members are there",
                "Show members where name contains kumar"
        );
    }
}
