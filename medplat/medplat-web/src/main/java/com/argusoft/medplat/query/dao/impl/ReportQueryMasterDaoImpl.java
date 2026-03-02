/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.query.dao.ReportQueryMasterDao;
import com.argusoft.medplat.query.model.QueryMaster;
import com.argusoft.medplat.query.model.ReportQueryMaster;
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
 * Implements methods of ReportQueryMasterDao
 * @author vaishali
 * @since 02/09/2020 10:30
 */
@Repository
public class ReportQueryMasterDaoImpl extends GenericDaoImpl<ReportQueryMaster, Integer> implements ReportQueryMasterDao {

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<ReportQueryMaster> retrieveAll(Boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ReportQueryMaster> criteriaQuery = criteriaBuilder.createQuery(ReportQueryMaster.class);
        Root<ReportQueryMaster> root = criteriaQuery.from(ReportQueryMaster.class);
        Predicate activeEqual;
        if (Boolean.TRUE.equals(isActive)) {
            activeEqual = criteriaBuilder.equal(root.get(ReportQueryMaster.Fields.STATE), QueryMaster.State.ACTIVE);
        } else {
            activeEqual = criteriaBuilder.notEqual(root.get(ReportQueryMaster.Fields.STATE), QueryMaster.State.ACTIVE);
        }
        if (activeEqual != null) {
            criteriaQuery.select(root).where(criteriaBuilder.and(activeEqual));
        }
        return session.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ReportQueryMaster retrieveByCode(String code) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ReportQueryMaster> criteriaQuery = criteriaBuilder.createQuery(ReportQueryMaster.class);
        Root<ReportQueryMaster> root = criteriaQuery.from(ReportQueryMaster.class);
        Predicate codeEqual = criteriaBuilder.equal(root.get(QueryMaster.Fields.CODE), code);
        criteriaQuery.select(root).where(criteriaBuilder.and(codeEqual));
        return session.createQuery(criteriaQuery).uniqueResult();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ReportQueryMaster retrieveByUUID(UUID uuid) {
        PredicateBuilder<ReportQueryMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(QueryMaster.Fields.UUID), uuid));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

}
