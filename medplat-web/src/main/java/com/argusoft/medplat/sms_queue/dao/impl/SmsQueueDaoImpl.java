package com.argusoft.medplat.sms_queue.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.sms_queue.dao.SmsQueueDao;
import com.argusoft.medplat.sms_queue.model.SmsQueue;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Implements methods of SmsQueueDao
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
@Repository
@Transactional
public class SmsQueueDaoImpl extends GenericDaoImpl<SmsQueue, Integer> implements SmsQueueDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsQueueDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SmsQueue> retrieveSmsQueues(Date toTime, Integer max) {
        PredicateBuilder<SmsQueue> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(SmsQueue.SmsQueueFields.IS_PROCESSED),false));
            predicates.add(builder.lessThanOrEqualTo(root.get(SmsQueue.SmsQueueFields.CREATED_ON),toTime));
            return predicates;
        };
        return findByCriteriaList(predicateBuilder, PageRequest.of(0,max));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsProcessed(Integer id) {
        if (id != null) {
            String hql = "update SmsQueue set " + SmsQueue.SmsQueueFields.STATUS + "= '" + SmsQueue.STATUS.PROCESSED + "' , "
                    + SmsQueue.SmsQueueFields.PROCESSED_ON + " = :time ,"
                    + SmsQueue.SmsQueueFields.IS_PROCESSED + " = true where " + SmsQueue.SmsQueueFields.ID + " = :id";
            Query<Integer> query = getCurrentSession().createQuery(hql);
            query.setParameter("time", new Date());
            query.setParameter("id", id);
            query.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsSentAndSetSmsId(Integer id, Integer smsId) {
        if (id != null) {
            String hql = "update SmsQueue set " + SmsQueue.SmsQueueFields.STATUS + "= '" + SmsQueue.STATUS.SENT + "' "
                    + "," + SmsQueue.SmsQueueFields.COMPLETED_ON + " = :time ,"
                    + SmsQueue.SmsQueueFields.SMSID + " = :smsId ,"
                    + SmsQueue.SmsQueueFields.IS_SENT + " = true where " + SmsQueue.SmsQueueFields.ID + " = :id";
            Query<Integer> query = getCurrentSession().createQuery(hql);
            query.setParameter("time", new Date());
            query.setParameter("id", id);
            query.setParameter("smsId", smsId);
            query.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatusFromProcessedToNewOnServerStartup() {
        LOGGER.info("::: Updating Sms Queue status from PROCESSED to NEW :::");
        String hql = "update SmsQueue set " + SmsQueue.SmsQueueFields.STATUS + "= '" + SmsQueue.STATUS.NEW +"'"
                + " where " + SmsQueue.SmsQueueFields.STATUS + " = '" + SmsQueue.STATUS.PROCESSED + "'";
        Query<Integer> query = getCurrentSession().createQuery(hql);
        query.executeUpdate();
    }
}
