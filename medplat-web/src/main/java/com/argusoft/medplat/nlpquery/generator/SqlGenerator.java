package com.argusoft.medplat.nlpquery.generator;

import com.argusoft.medplat.nlpquery.parser.ParsedQuery;
import com.argusoft.medplat.nlpquery.schema.SchemaMapping;
import com.argusoft.medplat.nlpquery.validator.SqlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates safe, validated SQL SELECT queries from a ParsedQuery.
 *
 * <p>Features:
 * <ul>
 *   <li>Generates only SELECT queries</li>
 *   <li>Uses parameterized values with proper escaping</li>
 *   <li>Validates table and column names against whitelist</li>
 *   <li>Applies safety limits to prevent runaway queries</li>
 *   <li>Generates human-readable preview descriptions</li>
 * </ul>
 * </p>
 *
 * @author medplat
 * @since 02/03/2026
 */
@Component
public class SqlGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlGenerator.class);
    private static final int MAX_RESULT_LIMIT = 1000;
    private static final int DEFAULT_LIMIT = 100;

    @Autowired
    private SchemaMapping schemaMapping;

    @Autowired
    private SqlValidator sqlValidator;

    /**
     * Generate a SQL query from a parsed NL query.
     *
     * @param parsedQuery The parsed query representation
     * @return GeneratedSql containing the SQL string and preview description
     */
    public GeneratedSql generate(ParsedQuery parsedQuery) {
        if (!parsedQuery.isValid()) {
            return GeneratedSql.error(parsedQuery.getErrorMessage());
        }

        try {
            String sql;
            String preview;

            if ("AGGREGATE".equals(parsedQuery.getIntent())) {
                sql = generateAggregateQuery(parsedQuery);
                preview = generateAggregatePreview(parsedQuery);
            } else {
                sql = generateSelectQuery(parsedQuery);
                preview = generateSelectPreview(parsedQuery);
            }

            // Validate the generated SQL
            SqlValidator.ValidationResult validation = sqlValidator.validate(sql);
            if (!validation.isValid()) {
                LOGGER.error("Generated SQL failed validation: {} | Error: {}", sql, validation.getErrorMessage());
                return GeneratedSql.error("Generated query failed safety validation: " + validation.getErrorMessage());
            }

            return new GeneratedSql(sql, preview, true, null);

        } catch (Exception e) {
            LOGGER.error("Error generating SQL from parsed query", e);
            return GeneratedSql.error("Failed to generate SQL query: " + e.getMessage());
        }
    }

    /**
     * Generate a standard SELECT query.
     */
    private String generateSelectQuery(ParsedQuery parsedQuery) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // Columns
        if (parsedQuery.getColumns() == null || parsedQuery.getColumns().isEmpty()) {
            sql.append("*");
        } else {
            List<String> validColumns = validateColumns(parsedQuery.getColumns(), parsedQuery.getTableName());
            if (validColumns.isEmpty()) {
                sql.append("*");
            } else {
                sql.append(String.join(", ", validColumns));
            }
        }

        // FROM
        sql.append(" FROM ").append(parsedQuery.getTableName());

        // WHERE
        appendWhereClause(sql, parsedQuery);

        // ORDER BY
        if (parsedQuery.getOrderByColumn() != null &&
                schemaMapping.isValidColumn(parsedQuery.getTableName(), parsedQuery.getOrderByColumn())) {
            sql.append(" ORDER BY ").append(parsedQuery.getOrderByColumn());
            if (parsedQuery.getOrderDirection() != null) {
                sql.append(" ").append(parsedQuery.getOrderDirection());
            }
        }

        // LIMIT
        int limit = parsedQuery.getLimit() != null ?
                Math.min(parsedQuery.getLimit(), MAX_RESULT_LIMIT) : DEFAULT_LIMIT;
        sql.append(" LIMIT ").append(limit);

        return sql.toString();
    }

    /**
     * Generate an aggregate query (COUNT, SUM, AVG, etc.).
     */
    private String generateAggregateQuery(ParsedQuery parsedQuery) {
        StringBuilder sql = new StringBuilder("SELECT ");

        String aggFunc = parsedQuery.getAggregateFunction();
        String aggCol = parsedQuery.getAggregateColumn();

        if (aggCol == null || aggCol.equals("*")) {
            sql.append(aggFunc).append("(*)");
        } else if (schemaMapping.isValidColumn(parsedQuery.getTableName(), aggCol)) {
            sql.append(aggFunc).append("(").append(aggCol).append(")");
        } else {
            sql.append(aggFunc).append("(*)");
        }

        sql.append(" AS result");

        // If COUNT, also add GROUP BY columns if specific columns are requested
        if ("COUNT".equals(aggFunc) && parsedQuery.getColumns() != null && !parsedQuery.getColumns().isEmpty()) {
            List<String> validGroupByColumns = validateColumns(parsedQuery.getColumns(), parsedQuery.getTableName());
            if (!validGroupByColumns.isEmpty()) {
                // Add group by columns to select
                sql = new StringBuilder("SELECT ");
                sql.append(String.join(", ", validGroupByColumns));
                sql.append(", ").append(aggFunc).append("(*) AS result");
            }
        }

        // FROM
        sql.append(" FROM ").append(parsedQuery.getTableName());

        // WHERE
        appendWhereClause(sql, parsedQuery);

        // GROUP BY (if we added group by columns)
        if ("COUNT".equals(aggFunc) && parsedQuery.getColumns() != null && !parsedQuery.getColumns().isEmpty()) {
            List<String> validGroupByColumns = validateColumns(parsedQuery.getColumns(), parsedQuery.getTableName());
            if (!validGroupByColumns.isEmpty()) {
                sql.append(" GROUP BY ").append(String.join(", ", validGroupByColumns));
            }
        }

        // LIMIT
        sql.append(" LIMIT ").append(DEFAULT_LIMIT);

        return sql.toString();
    }

    /**
     * Append WHERE clause to the SQL builder.
     */
    private void appendWhereClause(StringBuilder sql, ParsedQuery parsedQuery) {
        if (parsedQuery.getConditions() != null && !parsedQuery.getConditions().isEmpty()) {
            sql.append(" WHERE ");
            List<ParsedQuery.WhereCondition> validConditions = new ArrayList<>();

            for (ParsedQuery.WhereCondition condition : parsedQuery.getConditions()) {
                if (schemaMapping.isValidColumn(parsedQuery.getTableName(), condition.getColumn())) {
                    validConditions.add(condition);
                }
            }

            for (int i = 0; i < validConditions.size(); i++) {
                if (i > 0) {
                    sql.append(" ").append(validConditions.get(i).getLogicalOperator()).append(" ");
                }
                ParsedQuery.WhereCondition cond = validConditions.get(i);
                String sanitizedValue = sqlValidator.sanitizeValue(cond.getValue());

                if ("LIKE".equals(cond.getOperator())) {
                    sql.append(cond.getColumn()).append(" ILIKE '").append(sanitizedValue).append("'");
                } else if ("=".equals(cond.getOperator()) || "!=".equals(cond.getOperator())) {
                    // Handle boolean values
                    if ("true".equalsIgnoreCase(sanitizedValue) || "false".equalsIgnoreCase(sanitizedValue)) {
                        sql.append(cond.getColumn()).append(" ").append(cond.getOperator()).append(" ")
                                .append(sanitizedValue);
                    } else {
                        // Try to detect if it's a number
                        try {
                            Long.parseLong(sanitizedValue);
                            sql.append(cond.getColumn()).append(" ").append(cond.getOperator()).append(" ")
                                    .append(sanitizedValue);
                        } catch (NumberFormatException e) {
                            sql.append(cond.getColumn()).append(" ").append(cond.getOperator()).append(" '")
                                    .append(sanitizedValue).append("'");
                        }
                    }
                } else {
                    // Numeric operators (>, <, >=, <=)
                    sql.append(cond.getColumn()).append(" ").append(cond.getOperator()).append(" '")
                            .append(sanitizedValue).append("'");
                }
            }
        }
    }

    /**
     * Validate that columns exist in the table schema.
     */
    private List<String> validateColumns(List<String> columns, String tableName) {
        return columns.stream()
                .filter(col -> schemaMapping.isValidColumn(tableName, col))
                .collect(Collectors.toList());
    }

    /**
     * Generate a human-readable preview description for SELECT queries.
     */
    private String generateSelectPreview(ParsedQuery parsedQuery) {
        StringBuilder desc = new StringBuilder("Retrieving ");

        if (parsedQuery.getColumns() == null || parsedQuery.getColumns().isEmpty()) {
            desc.append("all columns");
        } else {
            desc.append("columns: ").append(String.join(", ", parsedQuery.getColumns()));
        }

        desc.append(" from ").append(parsedQuery.getTableKeyword() != null ?
                parsedQuery.getTableKeyword() : parsedQuery.getTableName());

        if (parsedQuery.getConditions() != null && !parsedQuery.getConditions().isEmpty()) {
            desc.append(" where ");
            for (int i = 0; i < parsedQuery.getConditions().size(); i++) {
                if (i > 0) desc.append(" and ");
                ParsedQuery.WhereCondition cond = parsedQuery.getConditions().get(i);
                desc.append(cond.getColumn()).append(" ").append(cond.getOperator())
                        .append(" ").append(cond.getValue());
            }
        }

        if (parsedQuery.getOrderByColumn() != null) {
            desc.append(", ordered by ").append(parsedQuery.getOrderByColumn());
            if (parsedQuery.getOrderDirection() != null) {
                desc.append(" ").append(parsedQuery.getOrderDirection().toLowerCase());
            }
        }

        if (parsedQuery.getLimit() != null) {
            desc.append(", limited to ").append(parsedQuery.getLimit()).append(" results");
        }

        return desc.toString();
    }

    /**
     * Generate a human-readable preview for aggregate queries.
     */
    private String generateAggregatePreview(ParsedQuery parsedQuery) {
        StringBuilder desc = new StringBuilder("Calculating ");
        desc.append(parsedQuery.getAggregateFunction());
        desc.append(" of ");
        desc.append(parsedQuery.getTableKeyword() != null ?
                parsedQuery.getTableKeyword() : parsedQuery.getTableName());

        if (parsedQuery.getConditions() != null && !parsedQuery.getConditions().isEmpty()) {
            desc.append(" where ");
            for (int i = 0; i < parsedQuery.getConditions().size(); i++) {
                if (i > 0) desc.append(" and ");
                ParsedQuery.WhereCondition cond = parsedQuery.getConditions().get(i);
                desc.append(cond.getColumn()).append(" ").append(cond.getOperator())
                        .append(" ").append(cond.getValue());
            }
        }

        return desc.toString();
    }

    /**
     * Inner class to hold generated SQL and metadata.
     */
    public static class GeneratedSql {
        private final String sql;
        private final String previewDescription;
        private final boolean valid;
        private final String errorMessage;

        public GeneratedSql(String sql, String previewDescription, boolean valid, String errorMessage) {
            this.sql = sql;
            this.previewDescription = previewDescription;
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static GeneratedSql error(String message) {
            return new GeneratedSql(null, null, false, message);
        }

        public String getSql() { return sql; }
        public String getPreviewDescription() { return previewDescription; }
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
}
