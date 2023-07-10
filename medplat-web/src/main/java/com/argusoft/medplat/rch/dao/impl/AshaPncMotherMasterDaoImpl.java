package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.AshaPncMotherMasterDao;
import com.argusoft.medplat.rch.model.AshaPncMotherMaster;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in ASHA pnc mother master dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class AshaPncMotherMasterDaoImpl extends GenericDaoImpl<AshaPncMotherMaster, Integer> implements AshaPncMotherMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public AshaPncMotherMaster retrievePncMotherMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId) {
        return super.findEntityByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (pncMasterId != null) {
                predicates.add(criteriaBuilder.equal(root.get(AshaPncMotherMaster.Fields.PNC_MASTER_ID), pncMasterId));
            }
            if (memberId != null) {
                predicates.add(criteriaBuilder.equal(root.get(AshaPncMotherMaster.Fields.MOTHER_ID), memberId));
            }
            return predicates;
        });
    }
}
