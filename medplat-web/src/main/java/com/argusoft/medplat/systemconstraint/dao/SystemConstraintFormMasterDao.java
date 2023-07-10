package com.argusoft.medplat.systemconstraint.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFormMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintMobileTemplateConfigDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFormMaster;

import java.util.List;
import java.util.UUID;

public interface SystemConstraintFormMasterDao extends GenericDao<SystemConstraintFormMaster, UUID> {

    SystemConstraintFormMasterDto getSystemConstraintFormByUuid(UUID uuid);

    public List<SystemConstraintFormMasterDto> getSystemConstraintForms(Integer menuConfigId);

    public String getSystemConstraintFormConfigByUuid(UUID uuid, String appName);

    public List<SystemConstraintStandardFieldMasterDto> getSystemConstraintStandardFields(Boolean fetchOnlyActive);

    public String getSystemConstraintStandardConfigById(Integer id);

    public List<SystemConstraintMobileTemplateConfigDto> getMobileTemplateConfig(String uuid);

    public List<String> getActiveMobileForms();

    public String getSystemConstraintConfigsByMenuConfigId(Integer menuConfigId);
}
