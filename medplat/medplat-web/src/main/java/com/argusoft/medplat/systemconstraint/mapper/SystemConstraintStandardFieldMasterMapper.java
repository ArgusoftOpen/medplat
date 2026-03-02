package com.argusoft.medplat.systemconstraint.mapper;

import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldValueMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldMaster;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SystemConstraintStandardFieldMasterMapper {

    public static SystemConstraintStandardFieldMasterDto mapModelToDto(SystemConstraintStandardFieldMaster systemConstraintStandardFieldMaster, List<SystemConstraintStandardFieldValueMasterDto> systemConstraintStandardFieldValueMasterDtoList) {
        SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto = new SystemConstraintStandardFieldMasterDto();
        if (systemConstraintStandardFieldMaster != null) {
            systemConstraintStandardFieldMasterDto.setUuid(systemConstraintStandardFieldMaster.getUuid());
            systemConstraintStandardFieldMasterDto.setFieldKey(systemConstraintStandardFieldMaster.getFieldKey());
            systemConstraintStandardFieldMasterDto.setFieldName(systemConstraintStandardFieldMaster.getFieldName());
            systemConstraintStandardFieldMasterDto.setFieldType(systemConstraintStandardFieldMaster.getFieldType());
            systemConstraintStandardFieldMasterDto.setAppName(systemConstraintStandardFieldMaster.getAppName());
            systemConstraintStandardFieldMasterDto.setCategoryId(systemConstraintStandardFieldMaster.getCategoryId());
            systemConstraintStandardFieldMasterDto.setState(systemConstraintStandardFieldMaster.getState());
            systemConstraintStandardFieldMasterDto.setCreatedOn(systemConstraintStandardFieldMaster.getCreatedOn());
            systemConstraintStandardFieldMasterDto.setCreatedBy(systemConstraintStandardFieldMaster.getCreatedBy());
            if (CollectionUtils.isEmpty(systemConstraintStandardFieldValueMasterDtoList)) {
                systemConstraintStandardFieldMasterDto.setSystemConstraintStandardFieldValueMasterDtos(new ArrayList<>());
            } else {
                systemConstraintStandardFieldMasterDto.setSystemConstraintStandardFieldValueMasterDtos(systemConstraintStandardFieldValueMasterDtoList);
            }
        }
        return systemConstraintStandardFieldMasterDto;
    }

    public static SystemConstraintStandardFieldMaster mapDtoToModel(SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto) {
        SystemConstraintStandardFieldMaster systemConstraintStandardFieldMaster = new SystemConstraintStandardFieldMaster();
        if (systemConstraintStandardFieldMasterDto != null) {
            systemConstraintStandardFieldMaster.setUuid(systemConstraintStandardFieldMasterDto.getUuid());
            systemConstraintStandardFieldMaster.setFieldKey(systemConstraintStandardFieldMasterDto.getFieldKey());
            systemConstraintStandardFieldMaster.setFieldName(systemConstraintStandardFieldMasterDto.getFieldName());
            systemConstraintStandardFieldMaster.setFieldType(systemConstraintStandardFieldMasterDto.getFieldType());
            systemConstraintStandardFieldMaster.setAppName(systemConstraintStandardFieldMasterDto.getAppName());
            systemConstraintStandardFieldMaster.setCategoryId(systemConstraintStandardFieldMasterDto.getCategoryId());
            systemConstraintStandardFieldMaster.setState(systemConstraintStandardFieldMasterDto.getState());
            systemConstraintStandardFieldMaster.setCreatedOn(systemConstraintStandardFieldMasterDto.getCreatedOn());
            systemConstraintStandardFieldMaster.setCreatedBy(systemConstraintStandardFieldMasterDto.getCreatedBy());
        }
        return systemConstraintStandardFieldMaster;
    }
}
