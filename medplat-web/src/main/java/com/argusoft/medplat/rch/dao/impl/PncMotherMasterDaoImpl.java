/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.PncMotherMasterDao;
import com.argusoft.medplat.rch.model.PncMotherMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in pnc mother master dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class PncMotherMasterDaoImpl extends GenericDaoImpl<PncMotherMaster, Integer> implements PncMotherMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PncMotherMaster> getPncMotherbyMemberid(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<PncMotherMaster> criteriaQuery = criteriaBuilder.createQuery(PncMotherMaster.class);
        Root<PncMotherMaster> root = criteriaQuery.from(PncMotherMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(PncMotherMaster.Fields.MOTHER_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(EntityAuditInfo.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).setMaxResults(3).getResultList();
    }

}
