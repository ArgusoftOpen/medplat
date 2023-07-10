package com.argusoft.medplat.systemconstraint.mapper;

import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldValueMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFieldValueMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SystemConstraintFieldValueMasterMapper {

    public static SystemConstraintFieldValueMasterDto mapModelToDto(SystemConstraintFieldValueMaster systemConstraintFieldValueMaster) {
        SystemConstraintFieldValueMasterDto systemConstraintFieldValueMasterDto = new SystemConstraintFieldValueMasterDto();
        if (systemConstraintFieldValueMaster != null) {
            systemConstraintFieldValueMasterDto.setUuid(systemConstraintFieldValueMaster.getUuid());
            systemConstraintFieldValueMasterDto.setFieldMasterUuid(systemConstraintFieldValueMaster.getFieldMasterUuid());
            systemConstraintFieldValueMasterDto.setValueType(systemConstraintFieldValueMaster.getValueType());
            systemConstraintFieldValueMasterDto.setKey(systemConstraintFieldValueMaster.getKey());
            systemConstraintFieldValueMasterDto.setValue(systemConstraintFieldValueMaster.getValue());
            systemConstraintFieldValueMasterDto.setDefaultValue(systemConstraintFieldValueMaster.getDefaultValue());
            systemConstraintFieldValueMasterDto.setCreatedOn(systemConstraintFieldValueMaster.getCreatedOn());
            systemConstraintFieldValueMasterDto.setCreatedBy(systemConstraintFieldValueMaster.getCreatedBy());
        }
        return systemConstraintFieldValueMasterDto;
    }

    public static List<SystemConstraintFieldValueMasterDto> mapModelListToDtoList(List<SystemConstraintFieldValueMaster> systemConstraintFieldValueMasterList) {
        List<SystemConstraintFieldValueMasterDto> systemConstraintFieldValueMasterDtoList = new ArrayList<>();
        for (SystemConstraintFieldValueMaster systemConstraintFieldValueMaster : systemConstraintFieldValueMasterList) {
            systemConstraintFieldValueMasterDtoList.add(mapModelToDto(systemConstraintFieldValueMaster));
        }
        return systemConstraintFieldValueMasterDtoList;
    }

    public static SystemConstraintFieldValueMaster mapDtoToModel(SystemConstraintFieldValueMasterDto systemConstraintFieldValueMasterDto, UUID fieldMasterUuid) {
        SystemConstraintFieldValueMaster systemConstraintFieldValueMaster = new SystemConstraintFieldValueMaster();
        if (systemConstraintFieldValueMasterDto != null) {
            systemConstraintFieldValueMaster.setUuid(systemConstraintFieldValueMasterDto.getUuid() != null ? systemConstraintFieldValueMasterDto.getUuid() : UUID.randomUUID());
            systemConstraintFieldValueMaster.setFieldMasterUuid(fieldMasterUuid != null ? fieldMasterUuid : systemConstraintFieldValueMasterDto.getFieldMasterUuid());
            systemConstraintFieldValueMaster.setValueType(systemConstraintFieldValueMasterDto.getValueType());
            systemConstraintFieldValueMaster.setKey(systemConstraintFieldValueMasterDto.getKey());
            systemConstraintFieldValueMaster.setValue(systemConstraintFieldValueMasterDto.getValue());
            systemConstraintFieldValueMaster.setDefaultValue(systemConstraintFieldValueMasterDto.getDefaultValue());
            systemConstraintFieldValueMaster.setCreatedOn(systemConstraintFieldValueMasterDto.getCreatedOn());
            systemConstraintFieldValueMaster.setCreatedBy(systemConstraintFieldValueMasterDto.getCreatedBy());
        }
        return systemConstraintFieldValueMaster;
    }

    public static List<SystemConstraintFieldValueMaster> mapDtoListToModelList(List<SystemConstraintFieldValueMasterDto> systemConstraintFieldValueMasterDtoList, UUID fieldMasterUuid) {
        List<SystemConstraintFieldValueMaster> systemConstraintFieldValueMasterList = new ArrayList<>();
        for (SystemConstraintFieldValueMasterDto systemConstraintFieldValueMasterDto : systemConstraintFieldValueMasterDtoList) {
            systemConstraintFieldValueMasterList.add(mapDtoToModel(systemConstraintFieldValueMasterDto, fieldMasterUuid));
        }
        return systemConstraintFieldValueMasterList;
    }
}
