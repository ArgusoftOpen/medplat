package com.argusoft.medplat.nlpquery.parser;

import java.util.List;

/**
 * Represents the parsed result of a natural language query.
 * This is the intermediate representation between raw NL input and SQL generation.
 *
 * @author medplat
 * @since 02/03/2026
 */
public class ParsedQuery {

    private String intent;           // SELECT, COUNT, AVG, etc.
    private String tableName;        // Resolved table name
    private String tableKeyword;     // Original keyword that matched the table
    private List<String> columns;    // Columns to select
    private List<WhereCondition> conditions; // WHERE conditions
    private String aggregateFunction; // Aggregate function if any
    private String aggregateColumn;  // Column to aggregate
    private String orderByColumn;    // ORDER BY column
    private String orderDirection;   // ASC or DESC
    private Integer limit;           // LIMIT value
    private boolean valid;
    private String errorMessage;
    private List<String> suggestions;

    public ParsedQuery() {
        this.valid = true;
    }

    // --- Getters and Setters ---

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getTableKeyword() { return tableKeyword; }
    public void setTableKeyword(String tableKeyword) { this.tableKeyword = tableKeyword; }

    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }

    public List<WhereCondition> getConditions() { return conditions; }
    public void setConditions(List<WhereCondition> conditions) { this.conditions = conditions; }

    public String getAggregateFunction() { return aggregateFunction; }
    public void setAggregateFunction(String aggregateFunction) { this.aggregateFunction = aggregateFunction; }

    public String getAggregateColumn() { return aggregateColumn; }
    public void setAggregateColumn(String aggregateColumn) { this.aggregateColumn = aggregateColumn; }

    public String getOrderByColumn() { return orderByColumn; }
    public void setOrderByColumn(String orderByColumn) { this.orderByColumn = orderByColumn; }

    public String getOrderDirection() { return orderDirection; }
    public void setOrderDirection(String orderDirection) { this.orderDirection = orderDirection; }

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    /**
     * Represents a single WHERE condition parsed from natural language.
     */
    public static class WhereCondition {
        private String column;
        private String operator;
        private String value;
        private String logicalOperator; // AND, OR

        public WhereCondition() {
            this.logicalOperator = "AND";
        }

        public WhereCondition(String column, String operator, String value) {
            this.column = column;
            this.operator = operator;
            this.value = value;
            this.logicalOperator = "AND";
        }

        public String getColumn() { return column; }
        public void setColumn(String column) { this.column = column; }

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getLogicalOperator() { return logicalOperator; }
        public void setLogicalOperator(String logicalOperator) { this.logicalOperator = logicalOperator; }

        @Override
        public String toString() {
            return column + " " + operator + " " + value;
        }
    }
}
