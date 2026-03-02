package com.argusoft.medplat.systemconstraint.service;

import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFormMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintMobileTemplateConfigDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldMaster;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SystemConstraintService {

    List<SystemConstraintFormMasterDto> getSystemConstraintForms(Integer menuConfigId);

    String getSystemConstraintFormConfigByUuid(UUID uuid, String appName);

    List<SystemConstraintFieldMasterDto> getSystemConstraintFieldsByFormMasterUuid(UUID formMasterUuid);

    SystemConstraintFormMasterDto getSystemConstraintFormByUuid(UUID uuid);

    SystemConstraintFormMasterDto createOrUpdateSystemConstraintForm(SystemConstraintFormMasterDto systemConstraintFormMasterDto,String type);

    String createOrUpdateSystemConstraintFieldConfig(UUID formMasterUuid, SystemConstraintFieldMasterDto systemConstraintFieldMasterDto);

    void deleteSystemConstraintFieldConfig(UUID uuid);

    Map<String, Object> getSystemConstraintWebTemplateConfigsByMenuConfigId(Integer menuConfigId);

    List<SystemConstraintStandardFieldMasterDto> getSystemConstraintStandardFields(Boolean fetchOnlyActive);

    SystemConstraintStandardFieldMaster getSystemConstraintStandardFieldByUuid(UUID uuid);

    String getSystemConstraintStandardConfigById(Integer id);

    void createOrUpdateSystemConstraintStandardField(SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto);

    String createOrUpdateSystemConstraintStandardFieldConfig(SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto);

    List<SystemConstraintMobileTemplateConfigDto> getMobileTemplateConfig(String formCode);

    List<String> getActiveMobileForms();

    String getSystemConstraintConfigsByMenuConfigId(Integer menuConfigId);
}
