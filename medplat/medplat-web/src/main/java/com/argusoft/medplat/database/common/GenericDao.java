package com.argusoft.medplat.database.common;

import com.argusoft.medplat.common.util.IJoinEnum;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Basic CRUD operations are defined in GenericDao interface.
 *
 * @param <EntityType> Represents ORM entity
 * @param <IDType>     Identity property type
 * @author kelvin
 * @since 15-JAN-2014
 */
public interface GenericDao<EntityType, IDType extends Serializable> {

    EntityType retrieveById(IDType id);

    IDType create(EntityType entity);

    List<EntityType> retriveByIds(String idFieldName, List<IDType> ids);

    void createOrUpdate(EntityType entity);

    void update(EntityType entity);

    void updateWithFlush(EntityType entity);

    void delete(EntityType entity);

    void deleteById(IDType id);

    void deleteAll(List<EntityType> entityList);

    List<EntityType> retrieveAll();

    List<EntityType> findAll(String orderBy);

    List<EntityType> findByCriteria(Criterion... criterion);

    List<EntityType> findByCriteriaList(List<Criterion> criterions);

    List<EntityType> findByCriteriaList(PredicateBuilder<EntityType> predicateBuilder);

    List<EntityType> findByCriteriaList(PredicateBuilder<EntityType> predicateBuilder, Pageable pageable);

    EntityType findEntityByCriteriaList(List<Criterion> criterions);

    EntityType findEntityByCriteriaList(PredicateBuilder<EntityType> predicateBuilder);

    int findTotalCountforCriteria(Criteria criteria);

    void updateAll(List<EntityType> e);

    void createOrUpdateAll(List<EntityType> entitytypes);

    //-------------------------------------------------------------------------
    // Convenience finder methods for HQL strings
    //-------------------------------------------------------------------------
    List find(String queryString) throws DataAccessException;

    List find(String queryString, Object value) throws DataAccessException;

    /**
     * @param queryString
     * @param values
     * @return
     * @throws DataAccessException
     */
    List find(final String queryString, final Object[] values) throws DataAccessException;

    //-------------------------------------------------------------------------
    // Convenience finder methods for detached criteria
    //-------------------------------------------------------------------------
    List findByCriteria(DetachedCriteria criteria) throws DataAccessException;

    List findByCriteria(final DetachedCriteria criteria, final int firstResult, final int maxResults) throws DataAccessException;

    Serializable save(final Object entity) throws DataAccessException;

    void saveOrUpdate(final Object entity) throws DataAccessException;

    void saveOrUpdateAll(final Collection entities);

    Object merge(final Object entity) throws DataAccessException;

    void flush();

    void commit();

    void begin();

    void addFetchMode(Root<EntityType> root, IJoinEnum[] iJoinEnums);

}
