package com.argusoft.medplat.systemconstraint.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFieldValueMaster;

import java.util.List;
import java.util.UUID;

public interface SystemConstraintFieldValueMasterDao extends GenericDao<SystemConstraintFieldValueMaster, UUID> {

    void deleteSystemConstraintFieldValueConfigsByUuids(List<UUID> fieldValueUuidsToBeRemoved);
}
