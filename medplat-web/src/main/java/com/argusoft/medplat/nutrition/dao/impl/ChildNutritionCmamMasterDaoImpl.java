package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.nutrition.dao.ChildNutritionCmamMasterDao;
import com.argusoft.medplat.nutrition.model.ChildNutritionCmamMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * <p>
 *     Implements methods of ChildNutritionCmamMasterDao
 * </p>
 * @author smeet
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildNutritionCmamMasterDaoImpl extends GenericDaoImpl<ChildNutritionCmamMaster, Integer>
        implements ChildNutritionCmamMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildNutritionCmamMaster retrieveLastCmamMasterByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildNutritionCmamMaster> criteriaQuery = criteriaBuilder.createQuery(ChildNutritionCmamMaster.class);
        Root<ChildNutritionCmamMaster> root = criteriaQuery.from(ChildNutritionCmamMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(ChildNutritionCmamMaster.Fields.CHILD_ID), memberId);
        Predicate isCaseCompletedEqual = criteriaBuilder.or(
                criteriaBuilder.equal(root.get(ChildNutritionCmamMaster.Fields.IS_CASE_COMPLETED), Boolean.FALSE),
                criteriaBuilder.isNull(root.get(ChildNutritionCmamMaster.Fields.IS_CASE_COMPLETED))
        );
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, isCaseCompletedEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildNutritionCmamMaster.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildNutritionCmamMaster retrieveActiveChildByChildId(Integer childId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildNutritionCmamMaster> criteriaQuery = criteriaBuilder.createQuery(ChildNutritionCmamMaster.class);
        Root<ChildNutritionCmamMaster> root = criteriaQuery.from(ChildNutritionCmamMaster.class);
        Predicate childIdEqual = criteriaBuilder.equal(root.get(ChildNutritionCmamMaster.Fields.CHILD_ID), childId);
        Predicate isCaseCompletedEqual = criteriaBuilder.isNull(root.get(ChildNutritionCmamMaster.Fields.IS_CASE_COMPLETED));
        criteriaQuery.select(root).where(criteriaBuilder.and(childIdEqual, isCaseCompletedEqual));
        return session.createQuery(criteriaQuery).uniqueResult();
    }
}
