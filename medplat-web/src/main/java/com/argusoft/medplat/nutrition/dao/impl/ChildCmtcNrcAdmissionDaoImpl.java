
package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.nutrition.dao.ChildCmtcNrcAdmissionDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcAdmission;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Implements methods of ChildCmtcNrcAdmissionDao
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildCmtcNrcAdmissionDaoImpl extends GenericDaoImpl<ChildCmtcNrcAdmission, Integer> implements ChildCmtcNrcAdmissionDao {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcAdmission> getAllCmtcByMemberId(Integer memberId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(ChildCmtcNrcAdmission.Fields.CHILD_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(ChildCmtcNrcAdmission.Fields.STATE), "ACTIVE"));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildCmtcNrcAdmission.Fields.ADMISSION_DATE)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcAdmission> getAllCmtcByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(ChildCmtcNrcAdmission.Fields.CHILD_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(ChildCmtcNrcAdmission.Fields.HEALTH_INFRA_ID), healthInfraId));
            predicates.add(criteriaBuilder.equal(root.get(ChildCmtcNrcAdmission.Fields.STATE), "ACTIVE"));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildCmtcNrcAdmission.Fields.ADMISSION_DATE)));
            return predicates;
        });
    }
}
