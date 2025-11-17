package com.argusoft.medplat.web.ddb.dao.impl;

import com.argusoft.medplat.web.ddb.dao.DdbDao;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
/**
 * @author ashwin
 * @since 23/08/2025 15:30
 */

@Repository
@Transactional
public class DdbDaoImpl extends GenericDaoImpl<Object, Long> implements DdbDao {

    private static final Logger log = LoggerFactory.getLogger(DdbDaoImpl.class);

    @Override
    public List<String> getAllTables() {
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"
        );
        return query.getResultList();
    }

    @Override
    public List<String> getColumnsByTable(String tableName) {
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(
                "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :tableName"
        );
        query.setParameter("tableName", tableName);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getTableMetadata(String tableName) {
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(
                "SELECT column_name, data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :tableName"
        );
        query.setParameter("tableName", tableName);
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getSampleData(String tableName, int limit) {
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(
                String.format("SELECT * FROM \"%s\" LIMIT %d", tableName, limit)
        );
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> executeNativeQuery(String sqlQuery) {
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(sqlQuery);
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getDataByTableAndColumns(String table, List<String> columns) {
        String colString = String.join("\", \"", columns);
        String sql = String.format("SELECT \"%s\" FROM \"%s\"", colString, table);
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(sql);
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
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
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(sql);
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public Integer getAttributeValueByIndicator(String indicator) {
        try {
            Session session = getCurrentSession();
            Query query = session.createNativeQuery(
                    "SELECT query_result FROM indicator_master WHERE indicator_name = :indicator"
            );
            query.setParameter("indicator", indicator);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).intValue() : null;
        } catch (NoResultException e) {
            log.debug("No result found for indicator: {}", indicator);
            return null;
        }
    }

    @Override
    public Integer getAttributeValueByTableAndColumn(String table, String column) {
        Session session = getCurrentSession();
        Query query = session.createNativeQuery(
                String.format("SELECT COUNT(%s) AS cnt FROM %s", column, table)
        );
        return ((Number) query.getSingleResult()).intValue();
    }
}
