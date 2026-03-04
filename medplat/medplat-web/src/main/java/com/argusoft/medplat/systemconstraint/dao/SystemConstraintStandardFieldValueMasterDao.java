package com.argusoft.medplat.systemconstraint.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldValueMaster;

import java.util.List;
import java.util.UUID;

public interface SystemConstraintStandardFieldValueMasterDao extends GenericDao<SystemConstraintStandardFieldValueMaster, UUID> {

    void deleteSystemConstraintStandardFieldValueConfigsByUuids(List<UUID> standardFieldValueUuidsToBeRemoved);
}
