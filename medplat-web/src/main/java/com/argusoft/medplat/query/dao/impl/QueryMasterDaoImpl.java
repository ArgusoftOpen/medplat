/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.query.dao.QueryMasterDao;
import com.argusoft.medplat.query.model.QueryMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implements methods of QueryMasterDao
 * @author vaishali
 * @since 02/09/2020 10:30
 */
@Repository
public class QueryMasterDaoImpl extends GenericDaoImpl<QueryMaster, UUID> implements QueryMasterDao {

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Boolean isCodeAvailable(String code, UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<QueryMaster> criteriaQuery = criteriaBuilder.createQuery(QueryMaster.class);
        Root<QueryMaster> root = criteriaQuery.from(QueryMaster.class);
        List<Predicate> predicates = new ArrayList<>();
        Predicate codeEqual = criteriaBuilder.equal(root.get(QueryMaster.Fields.CODE), code);
        predicates.add(codeEqual);
        if (uuid != null) {
            Predicate uuidEqual = criteriaBuilder.notEqual(root.get(QueryMaster.Fields.UUID), uuid);
            predicates.add(uuidEqual);
        }
        Predicate[] predicate = new Predicate[predicates.size()];
        criteriaQuery.select(root).where(criteriaBuilder.and(predicates.toArray(predicate)));
        List<QueryMaster> resultList = session.createQuery(criteriaQuery).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<QueryMaster> retrieveAll(Boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<QueryMaster> criteriaQuery = criteriaBuilder.createQuery(QueryMaster.class);
        Root<QueryMaster> root = criteriaQuery.from(QueryMaster.class);
        Predicate activeEqual = null;
        if(isActive != null) {
            if (Boolean.TRUE.equals(isActive)) {
                activeEqual = criteriaBuilder.equal(root.get(QueryMaster.Fields.STATE), QueryMaster.State.ACTIVE);
            } else {
                activeEqual = criteriaBuilder.notEqual(root.get(QueryMaster.Fields.STATE), QueryMaster.State.ACTIVE);
            }
        }
        if (activeEqual != null) {
            criteriaQuery.select(root).where(criteriaBuilder.and(activeEqual));
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(QueryMaster.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public QueryMaster retrieveByCode(String code) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<QueryMaster> criteriaQuery = criteriaBuilder.createQuery(QueryMaster.class);
        Root<QueryMaster> root = criteriaQuery.from(QueryMaster.class);
        Predicate codeEqual = criteriaBuilder.equal(root.get(QueryMaster.Fields.CODE), code);
        Predicate stateEqual = criteriaBuilder.equal(root.get(QueryMaster.Fields.STATE), QueryMaster.State.ACTIVE);
        criteriaQuery.select(root).where(criteriaBuilder.and(codeEqual, stateEqual));
        return session.createQuery(criteriaQuery).uniqueResult();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public QueryMaster retrieveByUUID(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<QueryMaster> criteriaQuery = criteriaBuilder.createQuery(QueryMaster.class);
        Root<QueryMaster> root = criteriaQuery.from(QueryMaster.class);
        Predicate uuidEqual = criteriaBuilder.equal(root.get(QueryMaster.Fields.UUID), uuid);
        criteriaQuery.select(root).where(criteriaBuilder.and(uuidEqual));
        return session.createQuery(criteriaQuery).uniqueResult();
    }

}
