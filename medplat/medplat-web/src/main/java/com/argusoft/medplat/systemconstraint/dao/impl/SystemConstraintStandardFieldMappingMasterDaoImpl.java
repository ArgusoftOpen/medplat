package com.argusoft.medplat.systemconstraint.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.systemconstraint.dao.SystemConstraintStandardFieldMappingMasterDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldMappingMaster;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SystemConstraintStandardFieldMappingMasterDaoImpl extends GenericDaoImpl<SystemConstraintStandardFieldMappingMaster, UUID> implements SystemConstraintStandardFieldMappingMasterDao {
}
