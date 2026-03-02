/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.PncChildMasterDao;
import com.argusoft.medplat.rch.model.PncChildMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in phc child master dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class PncChildMasterDaoImpl extends GenericDaoImpl<PncChildMaster, Integer> implements PncChildMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PncChildMaster> getPncChildbyMemberid(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<PncChildMaster> criteriaQuery = criteriaBuilder.createQuery(PncChildMaster.class);
        Root<PncChildMaster> root = criteriaQuery.from(PncChildMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(PncChildMaster.Fields.CHILD_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(EntityAuditInfo.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).setMaxResults(3).getResultList();
    }
}
