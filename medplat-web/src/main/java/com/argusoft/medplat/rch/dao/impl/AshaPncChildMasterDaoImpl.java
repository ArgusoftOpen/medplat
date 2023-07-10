package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.AshaPncChildMasterDao;
import com.argusoft.medplat.rch.model.AshaPncChildMaster;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in ASHA pnc child master dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class AshaPncChildMasterDaoImpl extends GenericDaoImpl<AshaPncChildMaster, Integer> implements AshaPncChildMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public AshaPncChildMaster retrievePncChildMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId) {
        return super.findEntityByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (pncMasterId != null) {
                predicates.add(criteriaBuilder.equal(root.get(AshaPncChildMaster.Fields.PNC_MASTER_ID), pncMasterId));
            }
            if (memberId != null) {
                predicates.add(criteriaBuilder.equal(root.get(AshaPncChildMaster.Fields.CHILD_ID), memberId));
            }
            return predicates;
        });
    }
}
