/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.model.TimerEvent;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in timer event dao.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class TimerEventDaoImpl extends GenericDaoImpl<TimerEvent, Integer> implements TimerEventDao {
    private static final String UPDATE_TIMER_EVENT = "update TimerEvent set ";
    /**
     * {@inheritDoc}
     */
    @Override
    public TimerEvent createUpdate(TimerEvent timerEvent) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(timerEvent);
        return timerEvent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimerEvent> retrieveTimerEvents(Date toTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<TimerEvent> criteria = criteriaBuilder.createQuery(TimerEvent.class);
        Root<TimerEvent> root = criteria.from(TimerEvent.class);
        Predicate status = criteriaBuilder.equal(root.get(TimerEvent.TimerEventFields.STATUS), TimerEvent.STATUS.NEW);
        Predicate systemTriggerOn = criteriaBuilder.lessThanOrEqualTo(root.get(TimerEvent.TimerEventFields.SYSTEM_TRIGGER_ON), toTime);
        criteria.select(root).where(criteriaBuilder.and(status, systemTriggerOn));
        return session.createQuery(criteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimerEvent> retrieveTimerEventsByConfig(Integer eventConfigId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<TimerEvent> criteria = criteriaBuilder.createQuery(TimerEvent.class);
        Root<TimerEvent> root = criteria.from(TimerEvent.class);
        Predicate status = criteriaBuilder.equal(root.get(TimerEvent.TimerEventFields.STATUS), TimerEvent.STATUS.NEW);
        Predicate type = criteriaBuilder.equal(root.get(TimerEvent.TimerEventFields.TYPE), TimerEvent.TYPE.TIMER_EVENT);
        Predicate configId = criteriaBuilder.equal(root.get(TimerEvent.TimerEventFields.EVENT_CONFIG_ID), eventConfigId);
        criteria.select(root).where(criteriaBuilder.and(status, type, configId));
        return session.createQuery(criteria).list();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changeTimeEventsStatus(Collection<Integer> timerEventIds, TimerEvent.STATUS status) {
        String hql = UPDATE_TIMER_EVENT + TimerEvent.TimerEventFields.STATUS + "=:status , " + TimerEvent.TimerEventFields.PROCESSED_ON + "= :time where id in (:ids)";
        Query<Integer> query = getCurrentSession().createQuery(hql);
        query.setParameter("time", new Date());
        query.setParameter("status", status);
        query.setParameterList("ids", timerEventIds);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIsProcessedTrue(Integer timerEventId) {
        String hql = UPDATE_TIMER_EVENT + TimerEvent.TimerEventFields.IS_PROCESSED + "= true , " + TimerEvent.TimerEventFields.PROCESSED_ON + "= :time where id = :id";
        Query<Integer> query = getCurrentSession().createQuery(hql);
        query.setParameter("time", new Date());
        query.setParameter("id", timerEventId);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsExcetion(Integer id, String excetion) {
        String hql = UPDATE_TIMER_EVENT + TimerEvent.TimerEventFields.STATUS + "= 'EXCEPTION' "
                + "," + TimerEvent.TimerEventFields.EXCEPTION_STRING + "=:exceptionString"
                + "," + TimerEvent.TimerEventFields.COMPLETED_ON + "=:completedOn"
                + " where " + TimerEvent.TimerEventFields.ID + " in (:id)";
        Query<Integer> query = getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        query.setParameter("exceptionString", excetion);
        query.setParameter("completedOn", new Date());
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsComplete(Integer id) {
        String hql = UPDATE_TIMER_EVENT + TimerEvent.TimerEventFields.STATUS + "= 'COMPLETED' "
                + "," + TimerEvent.TimerEventFields.COMPLETED_ON + "=:completedOn"
                + " where " + TimerEvent.TimerEventFields.ID + " in (:id)";
        Query<Integer> query = getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        query.setParameter("completedOn", new Date());
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewStatusToProcessingEvents() {
        System.out.println("Converting timer event status from processing to new.... ");
        String sql = "update timer_event set status = :new where status = :processed and processed = false";
        NativeQuery<Integer> query = getCurrentSession().createNativeQuery(sql);
        query.setParameter("new", TimerEvent.STATUS.NEW.toString());
        query.setParameter("processed", TimerEvent.STATUS.PROCESSED.toString());
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimerEvent findByRefIdAndType(Integer refId, TimerEvent.TYPE type) {
        PredicateBuilder<TimerEvent> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(TimerEvent.TimerEventFields.REF_ID), refId));
            predicates.add(builder.lessThanOrEqualTo(root.get(TimerEvent.TimerEventFields.TYPE), type));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByRefIdAndType(Integer refId, TimerEvent.TYPE type) {
        String sql = "delete from timer_event where ref_id= :refId and type= :type";
        NativeQuery<Integer> query = getCurrentSession().createNativeQuery(sql);
        query.setParameter("refId", refId);
        query.setParameter("type", type.toString());
        query.executeUpdate();
    }

}
