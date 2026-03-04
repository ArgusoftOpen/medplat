package com.argusoft.medplat.systemconstraint.mapper;

import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldValueMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldValueMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SystemConstraintStandardFieldValueMasterMapper {

    public static SystemConstraintStandardFieldValueMasterDto mapModelToDto(SystemConstraintStandardFieldValueMaster systemConstraintStandardFieldValueMaster) {
        SystemConstraintStandardFieldValueMasterDto systemConstraintStandardFieldValueMasterDto = new SystemConstraintStandardFieldValueMasterDto();
        if (systemConstraintStandardFieldValueMaster != null) {
            systemConstraintStandardFieldValueMasterDto.setUuid(systemConstraintStandardFieldValueMaster.getUuid());
            systemConstraintStandardFieldValueMasterDto.setStandardFieldMappingMasterUuid(systemConstraintStandardFieldValueMaster.getStandardFieldMappingMasterUuid());
            systemConstraintStandardFieldValueMasterDto.setValueType(systemConstraintStandardFieldValueMaster.getValueType());
            systemConstraintStandardFieldValueMasterDto.setKey(systemConstraintStandardFieldValueMaster.getKey());
            systemConstraintStandardFieldValueMasterDto.setValue(systemConstraintStandardFieldValueMaster.getValue());
            systemConstraintStandardFieldValueMasterDto.setDefaultValue(systemConstraintStandardFieldValueMaster.getDefaultValue());
            systemConstraintStandardFieldValueMasterDto.setCreatedOn(systemConstraintStandardFieldValueMaster.getCreatedOn());
            systemConstraintStandardFieldValueMasterDto.setCreatedBy(systemConstraintStandardFieldValueMaster.getCreatedBy());
        }
        return systemConstraintStandardFieldValueMasterDto;
    }

    public static List<SystemConstraintStandardFieldValueMasterDto> mapModelListToDtoList(List<SystemConstraintStandardFieldValueMaster> systemConstraintStandardFieldValueMasterList) {
        List<SystemConstraintStandardFieldValueMasterDto> systemConstraintStandardFieldValueMasterDtoList = new ArrayList<>();
        for (SystemConstraintStandardFieldValueMaster systemConstraintStandardFieldValueMaster : systemConstraintStandardFieldValueMasterList) {
            systemConstraintStandardFieldValueMasterDtoList.add(mapModelToDto(systemConstraintStandardFieldValueMaster));
        }
        return systemConstraintStandardFieldValueMasterDtoList;
    }

    public static SystemConstraintStandardFieldValueMaster mapDtoToModel(SystemConstraintStandardFieldValueMasterDto systemConstraintStandardFieldValueMasterDto, UUID standardFieldMappingMasterUuid) {
        SystemConstraintStandardFieldValueMaster systemConstraintStandardFieldValueMaster = new SystemConstraintStandardFieldValueMaster();
        if (systemConstraintStandardFieldValueMasterDto != null) {
            systemConstraintStandardFieldValueMaster.setUuid(systemConstraintStandardFieldValueMasterDto.getUuid() != null ? systemConstraintStandardFieldValueMasterDto.getUuid() : UUID.randomUUID());
            systemConstraintStandardFieldValueMaster.setStandardFieldMappingMasterUuid(standardFieldMappingMasterUuid != null ? standardFieldMappingMasterUuid : systemConstraintStandardFieldValueMasterDto.getStandardFieldMappingMasterUuid());
            systemConstraintStandardFieldValueMaster.setValueType(systemConstraintStandardFieldValueMasterDto.getValueType());
            systemConstraintStandardFieldValueMaster.setKey(systemConstraintStandardFieldValueMasterDto.getKey());
            systemConstraintStandardFieldValueMaster.setValue(systemConstraintStandardFieldValueMasterDto.getValue());
            systemConstraintStandardFieldValueMaster.setDefaultValue(systemConstraintStandardFieldValueMasterDto.getDefaultValue());
            systemConstraintStandardFieldValueMaster.setCreatedOn(systemConstraintStandardFieldValueMasterDto.getCreatedOn());
            systemConstraintStandardFieldValueMaster.setCreatedBy(systemConstraintStandardFieldValueMasterDto.getCreatedBy());
        }
        return systemConstraintStandardFieldValueMaster;
    }

    public static List<SystemConstraintStandardFieldValueMaster> mapDtoListToModelList(List<SystemConstraintStandardFieldValueMasterDto> systemConstraintStandardFieldValueMasterDtoList, UUID standardFieldMappingMasterUuid) {
        List<SystemConstraintStandardFieldValueMaster> systemConstraintStandardFieldValueMasterList = new ArrayList<>();
        for (SystemConstraintStandardFieldValueMasterDto systemConstraintStandardFieldValueMasterDto : systemConstraintStandardFieldValueMasterDtoList) {
            systemConstraintStandardFieldValueMasterList.add(mapDtoToModel(systemConstraintStandardFieldValueMasterDto, standardFieldMappingMasterUuid));
        }
        return systemConstraintStandardFieldValueMasterList;
    }
}
