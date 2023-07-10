/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.notification.dao.EscalationLevelMasterDao;
import com.argusoft.medplat.notification.model.EscalationLevelMaster;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 * Implementation of methods define in escalation level dao.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@Repository
public class EscalationLevelMasterDaoImpl extends GenericDaoImpl<EscalationLevelMaster, Integer> implements EscalationLevelMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EscalationLevelMaster> retrieveByNotificationId(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EscalationLevelMaster> cq = cb.createQuery(EscalationLevelMaster.class);
        Root<EscalationLevelMaster> root = cq.from(EscalationLevelMaster.class);

        cq.select(root).where(cb.equal(root.get("notificationTypeId"), id));
        Query<EscalationLevelMaster> query = session.createQuery(cq);
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EscalationLevelMaster retrieveByUUID(UUID uuid) {
        PredicateBuilder<EscalationLevelMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(EscalationLevelMaster.Fields.UUID), uuid));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

}
