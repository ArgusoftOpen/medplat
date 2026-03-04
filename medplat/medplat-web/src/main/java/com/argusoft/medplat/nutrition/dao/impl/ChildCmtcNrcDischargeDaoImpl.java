
package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.nutrition.dao.ChildCmtcNrcDischargeDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcDischarge;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Implements methods of ChildCmtcNrcDischargeDao
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildCmtcNrcDischargeDaoImpl extends GenericDaoImpl<ChildCmtcNrcDischarge, Integer> implements ChildCmtcNrcDischargeDao {
    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcDischarge getCmtcNrcDischargeFromAdmissionId(Integer admissionId) {
        if (admissionId == null) {
            return null;
        }
        return super.findEntityByCriteriaList((root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(ChildCmtcNrcDischarge.Fields.ADMISSION_ID), admissionId));
            return predicates;
        });
    }
}
