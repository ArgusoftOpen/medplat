package com.argusoft.medplat.web.BackendController.repository;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class GenericRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Integer getColumnCount(String table, String column) {
        String sql = String.format("SELECT COUNT(\"%s\") AS cnt FROM \"%s\"", column, table);
        NativeQuery<Number> query = (NativeQuery<Number>) entityManager.createNativeQuery(sql);
        Number result = query.uniqueResult();
        return result != null ? result.intValue() : 0;
    }

    public List<String> getAllTables() {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        NativeQuery<String> query = (NativeQuery<String>) entityManager.createNativeQuery(sql);
        return query.list();
    }

    public List<String> getColumns(String table) {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :table";
        NativeQuery<String> query = (NativeQuery<String>) entityManager.createNativeQuery(sql);
        query.setParameter("table", table);
        return query.list();
    }

    public List<Map<String, Object>> getTableMetadata(String table) {
        String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :table";
        NativeQuery<Object[]> query = (NativeQuery<Object[]>) entityManager.createNativeQuery(sql);
        query.setParameter("table", table);
        List<Object[]> results = query.list();

        List<Map<String, Object>> metadata = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> columnInfo = new HashMap<>();
            columnInfo.put("column_name", row[0]);
            columnInfo.put("data_type", row[1]);
            metadata.add(columnInfo);
        }
        return metadata;
    }

    public List<Map<String, Object>> getSampleData(String table, int limit) {
        String sql = String.format("SELECT * FROM \"%s\" LIMIT %d", table, limit);
        NativeQuery<Object[]> query = (NativeQuery<Object[]>) entityManager.createNativeQuery(sql);
        List<Object[]> results = query.list();

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        // Get column names
        String metaSql = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :table ORDER BY ordinal_position";
        NativeQuery<String> metaQuery = (NativeQuery<String>) entityManager.createNativeQuery(metaSql);
        metaQuery.setParameter("table", table);
        List<String> columnNames = metaQuery.list();

        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 0; i < columnNames.size() && i < row.length; i++) {
                rowData.put(columnNames.get(i), row[i]);
            }
            data.add(rowData);
        }
        return data;
    }

    public List<Map<String, Object>> getData(String table, List<String> columns) {
        String colString = String.join("\", \"", columns);
        String sql = String.format("SELECT \"%s\" FROM \"%s\"", colString, table);
        NativeQuery<Object[]> query = (NativeQuery<Object[]>) entityManager.createNativeQuery(sql);
        List<Object[]> results = query.list();

        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 0; i < columns.size() && i < row.length; i++) {
                rowData.put(columns.get(i), row[i]);
            }
            data.add(rowData);
        }
        return data;
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        NativeQuery query = (NativeQuery) entityManager.createNativeQuery(sql);
        List results = query.list();

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> data = new ArrayList<>();
        for (Object result : results) {
            Map<String, Object> rowData = new HashMap<>();
            if (result instanceof Object[]) {
                Object[] row = (Object[]) result;
                for (int i = 0; i < row.length; i++) {
                    rowData.put("column_" + i, row[i]);
                }
            } else {
                rowData.put("result", result);
            }
            data.add(rowData);
        }
        return data;
    }

    public List<Map<String, Object>> getGroupedCount(String table, String xAxis, String indicator) {
        String sql;
        if (indicator != null) {
            sql = String.format(
                    "SELECT \"%s\" as xAxisValue, COUNT(\"%s\") as count FROM \"%s\" GROUP BY \"%s\" ORDER BY xAxisValue",
                    xAxis, indicator, table, xAxis
            );
        } else {
            sql = String.format(
                    "SELECT \"%s\" as xAxisValue, COUNT(*) as count FROM \"%s\" GROUP BY \"%s\" ORDER BY xAxisValue",
                    xAxis, table, xAxis
            );
        }

        NativeQuery<Object[]> query = (NativeQuery<Object[]>) entityManager.createNativeQuery(sql);
        List<Object[]> results = query.list();

        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> rowData = new HashMap<>();
            rowData.put("xAxisValue", row[0]);
            rowData.put("count", row[1]);
            data.add(rowData);
        }
        return data;
    }
}