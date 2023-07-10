package com.argusoft.medplat.systemconstraint.mapper;

import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFormMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFormMaster;

public class SystemConstraintFormMasterMapper {

    public static SystemConstraintFormMasterDto mapModelToDto(SystemConstraintFormMaster systemConstraintFormMaster) {
        SystemConstraintFormMasterDto systemConstraintFormMasterDto = new SystemConstraintFormMasterDto();
        if (systemConstraintFormMaster != null) {
            systemConstraintFormMasterDto.setUuid(systemConstraintFormMaster.getUuid());
            systemConstraintFormMasterDto.setFormName(systemConstraintFormMaster.getFormName());
            systemConstraintFormMasterDto.setFormCode(systemConstraintFormMaster.getFormCode());
            systemConstraintFormMasterDto.setMenuConfigId(systemConstraintFormMaster.getMenuConfigId());
            systemConstraintFormMasterDto.setWebTemplateConfig(systemConstraintFormMaster.getWebTemplateConfig());
            systemConstraintFormMasterDto.setWebState(systemConstraintFormMaster.getWebState());
            systemConstraintFormMasterDto.setMobileState(systemConstraintFormMaster.getMobileState());
            systemConstraintFormMasterDto.setCreatedOn(systemConstraintFormMaster.getCreatedOn());
            systemConstraintFormMasterDto.setCreatedBy(systemConstraintFormMaster.getCreatedBy());
        }
        return systemConstraintFormMasterDto;
    }

    public static SystemConstraintFormMaster mapDtoToModel(SystemConstraintFormMasterDto systemConstraintFormMasterDto) {
        SystemConstraintFormMaster systemConstraintFormMaster = new SystemConstraintFormMaster();
        if (systemConstraintFormMasterDto != null) {
            systemConstraintFormMaster.setUuid(systemConstraintFormMasterDto.getUuid());
            systemConstraintFormMaster.setFormName(systemConstraintFormMasterDto.getFormName());
            systemConstraintFormMaster.setFormCode(systemConstraintFormMasterDto.getFormCode());
            systemConstraintFormMaster.setMenuConfigId(systemConstraintFormMasterDto.getMenuConfigId());
            systemConstraintFormMaster.setWebTemplateConfig(systemConstraintFormMasterDto.getWebTemplateConfig());
            systemConstraintFormMaster.setWebState(systemConstraintFormMasterDto.getWebState());
            systemConstraintFormMaster.setMobileState(systemConstraintFormMasterDto.getMobileState());
            systemConstraintFormMaster.setCreatedOn(systemConstraintFormMasterDto.getCreatedOn());
            systemConstraintFormMaster.setCreatedBy(systemConstraintFormMasterDto.getCreatedBy());
        }
        return systemConstraintFormMaster;
    }
}
