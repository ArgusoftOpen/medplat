package com.argusoft.medplat.systemconstraint.dto;

import java.util.List;

public class SystemConstraintFormConfigDto {

    private SystemConstraintFormMasterDto systemConstraintFormMasterDto;
    private List<SystemConstraintFieldMasterDto> systemConstraintFieldMasterDtos;

    public SystemConstraintFormMasterDto getSystemConstraintFormMasterDto() {
        return systemConstraintFormMasterDto;
    }

    public void setSystemConstraintFormMasterDto(SystemConstraintFormMasterDto systemConstraintFormMasterDto) {
        this.systemConstraintFormMasterDto = systemConstraintFormMasterDto;
    }

    public List<SystemConstraintFieldMasterDto> getSystemConstraintFieldMasterDtos() {
        return systemConstraintFieldMasterDtos;
    }

    public void setSystemConstraintFieldMasterDtos(List<SystemConstraintFieldMasterDto> systemConstraintFieldMasterDtos) {
        this.systemConstraintFieldMasterDtos = systemConstraintFieldMasterDtos;
    }
}
