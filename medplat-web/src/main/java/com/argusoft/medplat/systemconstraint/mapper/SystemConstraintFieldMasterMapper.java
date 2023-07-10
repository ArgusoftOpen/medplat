package com.argusoft.medplat.systemconstraint.mapper;

import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldValueMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFieldMaster;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SystemConstraintFieldMasterMapper {

    public static SystemConstraintFieldMasterDto mapModelToDto(SystemConstraintFieldMaster systemConstraintFieldMaster, List<SystemConstraintFieldValueMasterDto> systemConstraintFieldValueMasterDtoList) {
        SystemConstraintFieldMasterDto systemConstraintFieldMasterDto = new SystemConstraintFieldMasterDto();
        if (systemConstraintFieldMaster != null) {
            systemConstraintFieldMasterDto.setUuid(systemConstraintFieldMaster.getUuid());
            systemConstraintFieldMasterDto.setFormMasterUuid(systemConstraintFieldMaster.getFormMasterUuid());
            systemConstraintFieldMasterDto.setFieldKey(systemConstraintFieldMaster.getFieldKey());
            systemConstraintFieldMasterDto.setFieldName(systemConstraintFieldMaster.getFieldName());
            systemConstraintFieldMasterDto.setFieldType(systemConstraintFieldMaster.getFieldType());
            systemConstraintFieldMasterDto.setNgModel(systemConstraintFieldMaster.getNgModel());
            systemConstraintFieldMasterDto.setAppName(systemConstraintFieldMaster.getAppName());
            systemConstraintFieldMasterDto.setStandardFieldMasterUuid(systemConstraintFieldMaster.getStandardFieldMasterUuid());
            systemConstraintFieldMasterDto.setCreatedOn(systemConstraintFieldMaster.getCreatedOn());
            systemConstraintFieldMasterDto.setCreatedBy(systemConstraintFieldMaster.getCreatedBy());
            if (CollectionUtils.isEmpty(systemConstraintFieldValueMasterDtoList)) {
                systemConstraintFieldMasterDto.setSystemConstraintFieldValueMasterDtos(new ArrayList<>());
            } else {
                systemConstraintFieldMasterDto.setSystemConstraintFieldValueMasterDtos(systemConstraintFieldValueMasterDtoList);
            }
        }
        return systemConstraintFieldMasterDto;
    }

    public static SystemConstraintFieldMaster mapDtoToModel(SystemConstraintFieldMasterDto systemConstraintFieldMasterDto, UUID formMasterUuid) {
        SystemConstraintFieldMaster systemConstraintFieldMaster = new SystemConstraintFieldMaster();
        if (systemConstraintFieldMasterDto != null) {
            systemConstraintFieldMaster.setUuid(systemConstraintFieldMasterDto.getUuid());
            systemConstraintFieldMaster.setFormMasterUuid(formMasterUuid != null ? formMasterUuid : systemConstraintFieldMasterDto.getFormMasterUuid());
            systemConstraintFieldMaster.setFieldKey(systemConstraintFieldMasterDto.getFieldKey());
            systemConstraintFieldMaster.setFieldName(systemConstraintFieldMasterDto.getFieldName());
            systemConstraintFieldMaster.setFieldType(systemConstraintFieldMasterDto.getFieldType());
            systemConstraintFieldMaster.setNgModel(systemConstraintFieldMasterDto.getNgModel());
            systemConstraintFieldMaster.setAppName(systemConstraintFieldMasterDto.getAppName());
            systemConstraintFieldMaster.setStandardFieldMasterUuid(systemConstraintFieldMasterDto.getStandardFieldMasterUuid());
            systemConstraintFieldMaster.setCreatedOn(systemConstraintFieldMasterDto.getCreatedOn());
            systemConstraintFieldMaster.setCreatedBy(systemConstraintFieldMasterDto.getCreatedBy());
        }
        return systemConstraintFieldMaster;
    }
}
