
package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.nutrition.dao.ChildCmtcNrcLaboratoryDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcLaboratory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 *     Implements methods of ChildCmtcNrcLaboratoryDao
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildCmtcNrcLaboratoryDaoImpl extends GenericDaoImpl<ChildCmtcNrcLaboratory, Integer> implements ChildCmtcNrcLaboratoryDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcLaboratory retrieveLastLaboratoryTest(Integer admissionId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildCmtcNrcLaboratory> criteriaQuery = criteriaBuilder.createQuery(ChildCmtcNrcLaboratory.class);
        Root<ChildCmtcNrcLaboratory> root = criteriaQuery.from(ChildCmtcNrcLaboratory.class);
        Predicate admissionIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcLaboratory.Fields.ADMISSION_ID), admissionId);
        criteriaQuery.select(root).where(criteriaBuilder.and(admissionIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildCmtcNrcLaboratory.Fields.LABORATORY_DATE)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcLaboratory> getCmtcNrcLaboratoryEntitiesFromAdmissionId(Integer admissionId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildCmtcNrcLaboratory> criteriaQuery = criteriaBuilder.createQuery(ChildCmtcNrcLaboratory.class);
        Root<ChildCmtcNrcLaboratory> root = criteriaQuery.from(ChildCmtcNrcLaboratory.class);
        Predicate admissionIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcLaboratory.Fields.ADMISSION_ID), admissionId);
        criteriaQuery.select(root).where(criteriaBuilder.and(admissionIdEqual));
        return session.createQuery(criteriaQuery).getResultList();
    }

}
