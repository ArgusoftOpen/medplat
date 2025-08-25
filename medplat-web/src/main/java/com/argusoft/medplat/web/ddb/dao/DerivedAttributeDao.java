package com.argusoft.medplat.web.ddb.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import java.util.List;
/**
 * @author ashwin
 * @since 23/08/2025 15:30
 */

public interface DerivedAttributeDao extends GenericDao<DerivedAttribute, Integer> {
    List<DerivedAttribute> getAllDerivedAttributes();
}
