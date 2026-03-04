
package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.nutrition.dao.ChildCmtcNrcWeightDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcWeight;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Implements methods of ChildCmtcNrcWeightDao
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildCmtcNrcWeightDaoImpl extends GenericDaoImpl<ChildCmtcNrcWeight, Integer> implements ChildCmtcNrcWeightDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfEntryForWeightExists(Integer admissionId, Date date) {
        String query = "select count(*) from child_cmtc_nrc_weight_detail  where admission_id  =  :admissionId and weight_date = :weightDate";
        NativeQuery<BigInteger> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("admissionId", admissionId);
        sQLQuery.setParameter("weightDate", date);
        BigInteger count = sQLQuery.uniqueResult();
        return count.longValue() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcWeight> getCmtcNrcWeightEntitiesFromAdmissionId(Integer admissionId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildCmtcNrcWeight> criteriaQuery = criteriaBuilder.createQuery(ChildCmtcNrcWeight.class);
        Root<ChildCmtcNrcWeight> root = criteriaQuery.from(ChildCmtcNrcWeight.class);
        Predicate admissionIdEqual = criteriaBuilder.equal(root.get(ChildCmtcNrcWeight.Fields.ADMISSION_ID), admissionId);
        criteriaQuery.select(root).where(criteriaBuilder.and(admissionIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildCmtcNrcWeight.Fields.WEIGHT_DATE)));
        return session.createQuery(criteriaQuery).getResultList();
    }
}
