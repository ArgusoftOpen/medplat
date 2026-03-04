package com.argusoft.medplat.systemconstraint.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFieldMaster;

import java.util.List;
import java.util.UUID;

public interface SystemConstraintFieldMasterDao extends GenericDao<SystemConstraintFieldMaster, UUID> {

    void deleteSystemConstraintFieldConfigByUuid(UUID uuid);

    String getSystemConstraintFieldConfigByUuid(UUID uuid);

    List<SystemConstraintFieldMasterDto> getSystemConstraintFieldsByFormMasterUuid(UUID formMasterUuid);
}
