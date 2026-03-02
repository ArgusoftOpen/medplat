package com.argusoft.medplat.common.mapper;

import com.argusoft.medplat.common.dto.FieldValueMasterDto;
import com.argusoft.medplat.common.model.FieldValueMaster;

/**
 *<p>
 *    An util class for field value to convert modal to dto  or dto to modal
 *</p>
 * @author shrey
 * @since 26/08/2020 5:30
 */
public class FieldValueMasterMapper {

    private FieldValueMasterMapper() {
            
    }

    /**
     * Converts field value modal to field value dto
     * @param fieldValueMaster An instance of FieldValueMaster
     * @return An instance of FieldValueMasterDto
     */
    public static FieldValueMasterDto entityToDto(FieldValueMaster fieldValueMaster){
        FieldValueMasterDto fieldValueMasterDto = new FieldValueMasterDto();
        fieldValueMaster.setFieldId(fieldValueMaster.getFieldId());
        fieldValueMasterDto.setId(fieldValueMaster.getId());
        fieldValueMasterDto.setFieldValue(fieldValueMaster.getFieldValue());
        return fieldValueMasterDto;
    }
}
