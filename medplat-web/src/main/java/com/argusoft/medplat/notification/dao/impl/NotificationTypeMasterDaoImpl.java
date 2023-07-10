/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;
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
 *
 * <p>
 * Implementation of methods define in notification type dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class NotificationTypeMasterDaoImpl extends GenericDaoImpl<NotificationTypeMaster, Integer> implements NotificationTypeMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationTypeMaster> retrieveAll(Boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<NotificationTypeMaster> cq = cb.createQuery(NotificationTypeMaster.class);
        Root<NotificationTypeMaster> root = cq.from(NotificationTypeMaster.class);

        cq.select(root);
        if (isActive != null && isActive.equals(Boolean.TRUE)) {
            cq.where(cb.equal(root.get(NotificationTypeMaster.Fields.STATE), NotificationTypeMaster.State.ACTIVE));
        }
        if (isActive != null && isActive.equals(Boolean.FALSE)) {
            cq.where(cb.notEqual(root.get(NotificationTypeMaster.Fields.STATE), NotificationTypeMaster.State.ACTIVE));
        }
        cq.orderBy(cb.asc(root.get(NotificationTypeMaster.Fields.ID)));
        return session.createQuery(cq).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeMaster retrieveByCode(String code) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<NotificationTypeMaster> cq = cb.createQuery(NotificationTypeMaster.class);
        Root<NotificationTypeMaster> root = cq.from(NotificationTypeMaster.class);

        cq.select(root)
                .where(cb.and(
                        cb.equal(root.get(NotificationTypeMaster.Fields.STATE), NotificationTypeMaster.State.ACTIVE),
                        cb.equal(root.get(NotificationTypeMaster.Fields.CODE), code)
                ));

        return session.createQuery(cq).uniqueResult();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeMaster retrieveByUUID(UUID uuid) {
        PredicateBuilder<NotificationTypeMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(NotificationTypeMaster.Fields.UUID), uuid));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }
}
