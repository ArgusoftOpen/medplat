package com.argusoft.medplat.systemconstraint.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFormVersion;

import java.util.UUID;

public interface SystemConstraintFormVersionDao
        extends GenericDao<SystemConstraintFormVersion, UUID> {

    SystemConstraintFormVersion getFormVersionByFormUuidAndType(UUID formUuid, String type);
}
