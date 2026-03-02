
package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.nutrition.dao.ChildCmtcNrcFollowUpDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcFollowUp;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 *     Implements methods of ChildCmtcNrcFollowUpDao
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildCmtcNrcFollowUpDaoImpl extends GenericDaoImpl<ChildCmtcNrcFollowUp, Integer> implements ChildCmtcNrcFollowUpDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcFollowUp getLastFollowUpVisit(Integer childId, Integer admissionId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildCmtcNrcFollowUp> criteriaQuery = criteriaBuilder.createQuery(ChildCmtcNrcFollowUp.class);
        Root<ChildCmtcNrcFollowUp> root = criteriaQuery.from(ChildCmtcNrcFollowUp.class);
        Predicate childIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcFollowUp.Fields.CHILD_ID), childId);
        Predicate admissionIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcFollowUp.Fields.ADMISSION_ID), admissionId);
        criteriaQuery.select(root).where(criteriaBuilder.and(childIdEqual, admissionIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildCmtcNrcFollowUp.Fields.FOLLOW_UP_VISIT)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcFollowUp> getAllFollowUpVisit(Integer childId, Integer admissionId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildCmtcNrcFollowUp> criteriaQuery = criteriaBuilder.createQuery(ChildCmtcNrcFollowUp.class);
        Root<ChildCmtcNrcFollowUp> root = criteriaQuery.from(ChildCmtcNrcFollowUp.class);
        Predicate childIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcFollowUp.Fields.CHILD_ID), childId);
        Predicate admissionIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcFollowUp.Fields.ADMISSION_ID), admissionId);
        criteriaQuery.select(root).where(criteriaBuilder.and(childIdEqual, admissionIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(ChildCmtcNrcFollowUp.Fields.FOLLOW_UP_VISIT)));
        return session.createQuery(criteriaQuery).list();
    }
}
