package com.argusoft.medplat.web.ddb.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.ddb.dao.DerivedAttributeDao;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class DerivedAttributeDaoImpl extends GenericDaoImpl<DerivedAttribute, Integer> implements DerivedAttributeDao {

    @Override
    public List<DerivedAttribute> getAllDerivedAttributes() {
        PredicateBuilder<DerivedAttribute> predicateBuilder = (root, builder, query) -> {
            List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
            query.orderBy(builder.desc(root.get(DerivedAttribute.DerivedAttributeFields.ID)));
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }
}
