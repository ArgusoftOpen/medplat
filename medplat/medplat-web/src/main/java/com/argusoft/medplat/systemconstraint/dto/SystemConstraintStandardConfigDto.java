package com.argusoft.medplat.systemconstraint.dto;

import java.util.List;

public class SystemConstraintStandardConfigDto {

    private Integer standardId;
    private String standardName;
    private List<SystemConstraintStandardFieldMasterDto> systemConstraintStandardFieldMasterDtos;

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public List<SystemConstraintStandardFieldMasterDto> getSystemConstraintStandardFieldMasterDtos() {
        return systemConstraintStandardFieldMasterDtos;
    }

    public void setSystemConstraintStandardFieldMasterDtos(List<SystemConstraintStandardFieldMasterDto> systemConstraintStandardFieldMasterDtos) {
        this.systemConstraintStandardFieldMasterDtos = systemConstraintStandardFieldMasterDtos;
    }
}
