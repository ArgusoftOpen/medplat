package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.FieldMasterDao;
import com.argusoft.medplat.common.model.FieldConstantMaster;
import com.argusoft.medplat.common.service.FieldMasterService;
import com.argusoft.medplat.database.common.PredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods of FieldMasterService
 * @author shrey
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class FieldMasterServiceImpl implements FieldMasterService {

    @Autowired
    FieldMasterDao fieldMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdsByNameForFieldConstants(List<String> names) {
        PredicateBuilder<FieldConstantMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get("fieldName")).value(names));
            return predicates;
        };
        List<FieldConstantMaster> fieldConstantMasters = fieldMasterDao.findByCriteriaList(predicateBuilder);
        List<Integer> ids = new ArrayList<>();
        for (FieldConstantMaster constantMaster : fieldConstantMasters) {
            ids.add(constantMaster.getId());
        }
        return ids;
    }
}
