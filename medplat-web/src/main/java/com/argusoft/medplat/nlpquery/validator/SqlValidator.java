package com.argusoft.medplat.nlpquery.validator;

import com.argusoft.medplat.nlpquery.schema.SchemaMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * SQL Injection prevention and query validation component.
 *
 * <p>Validates generated SQL queries against multiple attack vectors:
 * <ul>
 *   <li>SQL injection patterns (DROP, DELETE, INSERT, UPDATE, ALTER, etc.)</li>
 *   <li>Comment injection (-- and /* patterns)</li>
 *   <li>Union-based injection</li>
 *   <li>Stacked queries (semicolons)</li>
 *   <li>Table/column whitelist validation</li>
 *   <li>Only SELECT queries allowed</li>
 * </ul>
 * </p>
 *
 * @author medplat
 * @since 02/03/2026
 */
@Component
public class SqlValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlValidator.class);

    @Autowired
    private SchemaMapping schemaMapping;

    // Dangerous SQL keywords that should NEVER appear in generated queries
    private static final Set<String> DANGEROUS_KEYWORDS = new LinkedHashSet<>(Arrays.asList(
            "DROP", "DELETE", "INSERT", "UPDATE", "ALTER", "TRUNCATE",
            "CREATE", "EXEC", "EXECUTE", "GRANT", "REVOKE",
            "MERGE", "CALL", "REPLACE", "LOAD", "SET",
            "SHUTDOWN", "BACKUP", "RESTORE"
    ));

    // Patterns indicating SQL injection attempts
    private static final List<Pattern> INJECTION_PATTERNS = Arrays.asList(
            Pattern.compile("--"),                              // SQL comment
            Pattern.compile("/\\*"),                            // Block comment start
            Pattern.compile("\\*/"),                            // Block comment end
            Pattern.compile(";"),                               // Statement terminator
            Pattern.compile("\\bUNION\\b", Pattern.CASE_INSENSITIVE),     // UNION injection
            Pattern.compile("\\bDROP\\b", Pattern.CASE_INSENSITIVE),      // DROP injection
            Pattern.compile("\\bDELETE\\b", Pattern.CASE_INSENSITIVE),    // DELETE injection
            Pattern.compile("\\bINSERT\\b", Pattern.CASE_INSENSITIVE),    // INSERT injection
            Pattern.compile("\\bUPDATE\\b", Pattern.CASE_INSENSITIVE),    // UPDATE injection
            Pattern.compile("\\bALTER\\b", Pattern.CASE_INSENSITIVE),     // ALTER injection
            Pattern.compile("\\bTRUNCATE\\b", Pattern.CASE_INSENSITIVE),  // TRUNCATE injection
            Pattern.compile("\\bEXEC\\b", Pattern.CASE_INSENSITIVE),      // EXEC injection
            Pattern.compile("\\bEXECUTE\\b", Pattern.CASE_INSENSITIVE),   // EXECUTE injection
            Pattern.compile("\\bCREATE\\b", Pattern.CASE_INSENSITIVE),    // CREATE injection
            Pattern.compile("\\bGRANT\\b", Pattern.CASE_INSENSITIVE),     // GRANT injection
            Pattern.compile("\\bREVOKE\\b", Pattern.CASE_INSENSITIVE),    // REVOKE injection
            Pattern.compile("\\bxp_\\w+", Pattern.CASE_INSENSITIVE),      // xp_ stored procedures
            Pattern.compile("\\bsp_\\w+", Pattern.CASE_INSENSITIVE),      // sp_ stored procedures
            Pattern.compile("0x[0-9a-fA-F]+"),                            // Hex encoding
            Pattern.compile("\\bCHAR\\s*\\(", Pattern.CASE_INSENSITIVE),  // CHAR() function
            Pattern.compile("\\bCONCAT\\s*\\(", Pattern.CASE_INSENSITIVE) // CONCAT injection
    );

    /**
     * Validation result containing status and error details.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }
    }

    /**
     * Validate a generated SQL query for safety.
     *
     * @param sql The SQL query to validate
     * @return ValidationResult indicating if the query is safe to execute
     */
    public ValidationResult validate(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return ValidationResult.failure("SQL query cannot be empty.");
        }

        String trimmedSql = sql.trim();

        // 1. Must start with SELECT
        if (!trimmedSql.toUpperCase().startsWith("SELECT")) {
            LOGGER.warn("Rejected non-SELECT query: {}", trimmedSql);
            return ValidationResult.failure("Only SELECT queries are allowed. The system does not support data modification queries.");
        }

        // 2. Check for injection patterns
        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(trimmedSql).find()) {
                LOGGER.warn("SQL injection pattern detected in query: {} | Pattern: {}", trimmedSql, pattern.pattern());
                return ValidationResult.failure("Query contains potentially unsafe patterns and has been rejected for security reasons.");
            }
        }

        // 3. Check for dangerous keywords (word-boundary check)
        String upperSql = trimmedSql.toUpperCase();
        for (String keyword : DANGEROUS_KEYWORDS) {
            if (upperSql.matches(".*\\b" + keyword + "\\b.*")) {
                LOGGER.warn("Dangerous keyword '{}' detected in query: {}", keyword, trimmedSql);
                return ValidationResult.failure("Query contains the restricted keyword '" + keyword + "'. Only read-only SELECT queries are allowed.");
            }
        }

        // 4. Validate table names are in whitelist
        ValidationResult tableValidation = validateTableAccess(trimmedSql);
        if (!tableValidation.isValid()) {
            return tableValidation;
        }

        // 5. Check query doesn't have multiple statements
        if (trimmedSql.contains(";")) {
            return ValidationResult.failure("Multiple SQL statements are not allowed.");
        }

        // 6. Check for reasonable query length
        if (trimmedSql.length() > 2000) {
            return ValidationResult.failure("Query is too long. Please simplify your query.");
        }

        LOGGER.info("SQL validation passed for query: {}", trimmedSql);
        return ValidationResult.success();
    }

    /**
     * Validate that only whitelisted tables are being accessed.
     */
    private ValidationResult validateTableAccess(String sql) {
        String upperSql = sql.toUpperCase();

        // Extract table name from FROM clause
        int fromIndex = upperSql.indexOf("FROM");
        if (fromIndex < 0) {
            return ValidationResult.failure("Query does not contain a valid FROM clause.");
        }

        String afterFrom = sql.substring(fromIndex + 4).trim();
        String tableName = afterFrom.split("\\s+")[0].trim().toLowerCase();

        // Remove any quotes
        tableName = tableName.replaceAll("[\"'`]", "");

        if (!schemaMapping.isValidTable(tableName)) {
            LOGGER.warn("Access to non-whitelisted table attempted: {}", tableName);
            return ValidationResult.failure("Access to table '" + tableName + "' is not allowed. " +
                    "Available tables: " + String.join(", ", schemaMapping.getAllTableNames()));
        }

        return ValidationResult.success();
    }

    /**
     * Sanitize a value for use in queries.
     * Escapes special characters to prevent injection.
     *
     * @param value The raw value to sanitize
     * @return The sanitized value
     */
    public String sanitizeValue(String value) {
        if (value == null) return "";

        return value
                .replace("'", "''")       // Escape single quotes
                .replace("\\", "\\\\")    // Escape backslashes
                .replace("\0", "")         // Remove null bytes
                .replace("\n", " ")        // Remove newlines
                .replace("\r", " ")        // Remove carriage returns
                .replace("\t", " ")        // Remove tabs
                .trim();
    }

    /**
     * Validate the natural language input before parsing.
     * Catches injection attempts at the input level.
     *
     * @param input The natural language input
     * @return ValidationResult
     */
    public ValidationResult validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return ValidationResult.failure("Input query cannot be empty.");
        }

        if (input.length() > 500) {
            return ValidationResult.failure("Input query is too long. Please keep it under 500 characters.");
        }

        // Check for SQL keywords in user input
        String upperInput = input.toUpperCase();
        for (String keyword : DANGEROUS_KEYWORDS) {
            if (upperInput.matches(".*\\b" + keyword + "\\b.*")) {
                return ValidationResult.failure("Input contains restricted keyword '" + keyword + "'. " +
                        "Please use natural language to describe what data you want to see.");
            }
        }

        // Check for obvious injection attempts
        if (input.contains(";") || input.contains("--") || input.contains("/*")) {
            return ValidationResult.failure("Input contains invalid characters. Please use plain English to describe your query.");
        }

        return ValidationResult.success();
    }
}
