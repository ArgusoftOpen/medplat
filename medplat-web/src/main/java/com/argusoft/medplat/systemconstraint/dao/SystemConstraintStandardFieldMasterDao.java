package com.argusoft.medplat.systemconstraint.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldMaster;

import java.util.UUID;

public interface SystemConstraintStandardFieldMasterDao extends GenericDao<SystemConstraintStandardFieldMaster, UUID> {

    String getSystemConstraintFieldConfigByUuid(UUID standardFieldMappingMasterUuid);
}
