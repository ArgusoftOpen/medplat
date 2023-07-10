package com.argusoft.medplat.systemconstraint.mapper;


import com.argusoft.medplat.mobile.dto.ComponentTagDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintMobileTemplateConfigDto;

import java.util.ArrayList;
import java.util.List;


public class SystemConstraintComponentTagDtoMapper {

    public static ComponentTagDto mapToComponentTagDto(SystemConstraintMobileTemplateConfigDto
                                                               systemConstraintFieldMasterDto) {
        ComponentTagDto componentTagDto = new ComponentTagDto();
        if (systemConstraintFieldMasterDto != null) {
            componentTagDto.setId(Integer.parseInt(systemConstraintFieldMasterDto.getId()));
            componentTagDto.setTitle(systemConstraintFieldMasterDto.getTitle());
            componentTagDto.setSubtitle(systemConstraintFieldMasterDto.getSubTitle());
            componentTagDto.setInstruction(systemConstraintFieldMasterDto.getInstruction());
            componentTagDto.setQuestion(systemConstraintFieldMasterDto.getQuestion());
            componentTagDto.setType(systemConstraintFieldMasterDto.getType());
            componentTagDto.setIsmandatory(systemConstraintFieldMasterDto.getIsMandatory());
            componentTagDto.setMandatorymessage(systemConstraintFieldMasterDto.getMandatoryMessage());
            if (systemConstraintFieldMasterDto.getLength() != null) {
                componentTagDto.setLength(Integer.parseInt(systemConstraintFieldMasterDto.getLength()));
            }
            componentTagDto.setDatamap(systemConstraintFieldMasterDto.getDataMap());
            componentTagDto.setNext(systemConstraintFieldMasterDto.getNext());
            componentTagDto.setEvent(systemConstraintFieldMasterDto.getEvent());
            componentTagDto.setPage(systemConstraintFieldMasterDto.getPage());
            componentTagDto.setBinding(systemConstraintFieldMasterDto.getBinding());
//            componentTagDto.setSubform(systemConstraintFieldMasterDto.getS);
            componentTagDto.setIshidden(systemConstraintFieldMasterDto.getIsHidden());
            componentTagDto.setRelatedpropertyname(systemConstraintFieldMasterDto.getRelatedPropertyName());
//            componentTagDto.setDatadump(systemConstraintFieldMasterDto);
//            componentTagDto.setDatadumporder();
            componentTagDto.setHint(systemConstraintFieldMasterDto.getHint());
            componentTagDto.setHelpvideofield(systemConstraintFieldMasterDto.getHelpVideo());
//            componentTagDto.setRow(systemConstraintFieldMasterDto.get);
//            componentTagDto.setListValueFieldKey(systemConstraintFieldMasterDto);

            componentTagDto.setValidations(systemConstraintFieldMasterDto.getValidations());
            componentTagDto.setOptions(systemConstraintFieldMasterDto.getOptions());
            System.out.println();
            componentTagDto.setFormulas(systemConstraintFieldMasterDto.getFormulas());
        }
        return componentTagDto;
    }

    public static List<ComponentTagDto> mapToComponentTagDtoList(
            List<SystemConstraintMobileTemplateConfigDto>
                    systemConstraintStandardFieldValueMasterList) {
        List<ComponentTagDto> componentTagDtos = new ArrayList<>();
        for (SystemConstraintMobileTemplateConfigDto systemConstraintMobileTemplateConfigDto : systemConstraintStandardFieldValueMasterList) {
            componentTagDtos.add(mapToComponentTagDto(systemConstraintMobileTemplateConfigDto));
        }
        return componentTagDtos;
    }
}
