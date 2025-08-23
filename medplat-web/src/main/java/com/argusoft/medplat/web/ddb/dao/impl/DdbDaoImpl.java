package com.argusoft.medplat.web.ddb.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.ddb.dao.DdbDao;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import com.argusoft.medplat.web.ddb.model.IndicatorMaster;
import com.argusoft.medplat.web.ddb.model.DatasetMaster;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements methods of DdbDao
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@Repository
@Transactional
public class DdbDaoImpl extends GenericDaoImpl<DerivedAttribute, Integer> implements DdbDao {

    private static final Logger log = LoggerFactory.getLogger(DdbDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> getAllTables() {
        Query query = entityManager.createNativeQuery(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"
        );
        return query.getResultList();
    }

    @Override
    public List<String> getColumnsByTable(String tableName) {
        Query query = entityManager.createNativeQuery(
                "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :tableName"
        );
        query.setParameter("tableName", tableName);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getTableMetadata(String tableName) {
        Query query = entityManager.createNativeQuery(
                "SELECT column_name, data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = :tableName"
        );
        query.setParameter("tableName", tableName);
        // Transform to Map result
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getSampleData(String tableName, int limit) {
        Query query = entityManager.createNativeQuery(
                String.format("SELECT * FROM \"%s\" LIMIT %d", tableName, limit)
        );
        // Transform to Map result
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> executeNativeQuery(String sqlQuery) {
        Query query = entityManager.createNativeQuery(sqlQuery);
        // Transform to Map result - this is the key fix!
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getDataByTableAndColumns(String table, List<String> columns) {
        String colString = String.join("\", \"", columns);
        String sql = String.format("SELECT \"%s\" FROM \"%s\"", colString, table);
        Query query = entityManager.createNativeQuery(sql);
        // Transform to Map result
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
        Query query = entityManager.createNativeQuery(sql);
        // Transform to Map result
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public Integer getAttributeValueByIndicator(String indicator) {
        try {
            Query query = entityManager.createNativeQuery(
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
        Query query = entityManager.createNativeQuery(
                String.format("SELECT COUNT(%s) AS cnt FROM %s", column, table)
        );
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public List<DerivedAttribute> getAllDerivedAttributes() {
        PredicateBuilder<DerivedAttribute> predicateBuilder = (root, builder, query) -> {
            List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
            query.orderBy(builder.desc(root.get(DerivedAttribute.DerivedAttributeFields.ID)));
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    @Override
    public void saveIndicatorMaster(IndicatorMaster indicatorMaster) {
        if (indicatorMaster.getId() == null) {
            entityManager.persist(indicatorMaster);
        } else {
            entityManager.merge(indicatorMaster);
        }
    }

    @Override
    public List<IndicatorMaster> getAllIndicatorMaster() {
        TypedQuery<IndicatorMaster> query = entityManager.createQuery(
                "SELECT i FROM IndicatorMaster i ORDER BY i.id DESC",
                IndicatorMaster.class
        );
        return query.getResultList();
    }

    @Override
    public void saveDatasetMaster(DatasetMaster datasetMaster) {
        if (datasetMaster.getId() == null) {
            entityManager.persist(datasetMaster);
        } else {
            entityManager.merge(datasetMaster);
        }
    }

    @Override
    public List<DatasetMaster> getAllDatasetMaster() {
        TypedQuery<DatasetMaster> query = entityManager.createQuery(
                "SELECT d FROM DatasetMaster d ORDER BY d.id DESC",
                DatasetMaster.class
        );
        return query.getResultList();
    }

    @Override
    public DatasetMaster getDatasetMasterById(Integer id) {
        return entityManager.find(DatasetMaster.class, id);
    }
}
