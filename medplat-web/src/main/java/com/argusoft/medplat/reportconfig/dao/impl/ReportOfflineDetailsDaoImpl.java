package com.argusoft.medplat.reportconfig.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.reportconfig.dao.ReportOfflineDetailsDao;
import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implements methods of ReportOfflineDetailsDao
 * </p>
 *
 * @author sneha
 * @since 11-01-2021 01:50
 */
@Repository
@Transactional
public class ReportOfflineDetailsDaoImpl extends GenericDaoImpl<ReportOfflineDetails, Integer> implements ReportOfflineDetailsDao {

    private static final Logger log = LoggerFactory.getLogger(ReportOfflineDetailsDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportOfflineDetails> retrieveOfflineReport(Date toTime, Integer max) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReportOfflineDetails> cq = cb.createQuery(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = cq.from(ReportOfflineDetails.class);
        cq.select(root).where(cb.and(
                cb.equal(root.get(ReportOfflineDetails.Fields.STATUS), ReportOfflineDetails.STATUS.REQUESTED),
                cb.lessThanOrEqualTo(root.get(ReportOfflineDetails.Fields.CREATED_ON), toTime)
        )).orderBy(cb.asc(root.get(ReportOfflineDetails.Fields.CREATED_ON)));
        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportOfflineDetails> retrieveByUserId(Integer userId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReportOfflineDetails> cq = cb.createQuery(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = cq.from(ReportOfflineDetails.class);
        cq.select(root).where(cb.and(
                cb.equal(root.get(ReportOfflineDetails.Fields.USER_ID), userId),
                cb.equal(root.get(ReportOfflineDetails.Fields.STATE), ReportOfflineDetails.STATE.ACTIVE)
        )).orderBy(cb.desc(root.get(ReportOfflineDetails.Fields.CREATED_ON)));
        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsProcessed(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<ReportOfflineDetails> update = cb.createCriteriaUpdate(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = update.from(ReportOfflineDetails.class);
        update.set(root.get(ReportOfflineDetails.Fields.STATUS), ReportOfflineDetails.STATUS.PROCESSING);
        update.set(root.get(ReportOfflineDetails.Fields.MODIFIED_ON), new Date());
        update.where(cb.equal(root.get(ReportOfflineDetails.Fields.ID), id));
        session.createQuery(update).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsReadyForDownload(Integer id, Long fileId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<ReportOfflineDetails> update = cb.createCriteriaUpdate(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = update.from(ReportOfflineDetails.class);
        update.set(root.get(ReportOfflineDetails.Fields.STATUS), ReportOfflineDetails.STATUS.READY_FOR_DOWNLOAD);
        update.set(root.get(ReportOfflineDetails.Fields.FILE_ID), fileId);
        update.set(root.get(ReportOfflineDetails.Fields.MODIFIED_ON), new Date());
        update.set(root.get(ReportOfflineDetails.Fields.COMPLETED_ON), new Date());
        update.where(cb.equal(root.get(ReportOfflineDetails.Fields.ID), id));
        session.createQuery(update).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void markAsError(Integer id, String error) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<ReportOfflineDetails> update = cb.createCriteriaUpdate(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = update.from(ReportOfflineDetails.class);
        update.set(root.get(ReportOfflineDetails.Fields.STATUS), ReportOfflineDetails.STATUS.ERROR);
        update.set(root.get(ReportOfflineDetails.Fields.ERROR), error);
        update.set(root.get(ReportOfflineDetails.Fields.MODIFIED_ON), new Date());
        update.where(cb.equal(root.get(ReportOfflineDetails.Fields.ID), id));
        session.createQuery(update).executeUpdate();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatusFromProcessedToNewOnServerStartup() {
        log.info("::: Updating Report Queue status from PROCESSED to NEW :::");
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<ReportOfflineDetails> update = cb.createCriteriaUpdate(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = update.from(ReportOfflineDetails.class);
        update.set(root.get(ReportOfflineDetails.Fields.STATUS), ReportOfflineDetails.STATUS.REQUESTED);
        update.where(cb.and(
                cb.equal(root.get(ReportOfflineDetails.Fields.STATUS), ReportOfflineDetails.STATUS.PROCESSING)
//                cb.equal(root.get(ReportOfflineDetails.Fields.STATE), ReportOfflineDetails.STATE.ACTIVE)
        ));
        session.createQuery(update).executeUpdate();
    }

    public List<ReportOfflineDetails> retrieveOfflineReportByDate(Date toTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReportOfflineDetails> cq = cb.createQuery(ReportOfflineDetails.class);
        Root<ReportOfflineDetails> root = cq.from(ReportOfflineDetails.class);
        cq.select(root).where(cb.and(
                cb.lessThanOrEqualTo(root.get(ReportOfflineDetails.Fields.MODIFIED_ON), toTime),
                cb.equal(root.get(ReportOfflineDetails.Fields.STATE), ReportOfflineDetails.STATE.ACTIVE)
        ));
        return session.createQuery(cq).getResultList();
    }

}
