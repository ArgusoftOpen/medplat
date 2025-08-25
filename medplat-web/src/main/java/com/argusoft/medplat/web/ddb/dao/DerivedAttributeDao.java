package com.argusoft.medplat.web.ddb.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import java.util.List;

public interface DerivedAttributeDao extends GenericDao<DerivedAttribute, Integer> {
    List<DerivedAttribute> getAllDerivedAttributes();
}
