/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.mapper;

import com.argusoft.medplat.notification.dto.FormMasterDto;
import com.argusoft.medplat.notification.model.FormMaster;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * <p>
 *     Mapper for form master in order to convert dto to model or model to dto.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class FormMasterMapper {
    private FormMasterMapper(){
    }

    /**
     * Convert form master entity into details.
     * @param formMaster Form master entity.
     * @return Returns form master details.
     */
    public static FormMasterDto convertMasterToDto(FormMaster formMaster) {

        FormMasterDto formMasterDto = new FormMasterDto();

        formMasterDto.setCode(formMaster.getCode());
        formMasterDto.setName(formMaster.getName());
        formMasterDto.setId(formMaster.getId());
        formMasterDto.setCreatedBy(formMaster.getCreatedBy());
        formMasterDto.setCreatedOn(formMaster.getCreatedOn());
        formMasterDto.setState(formMaster.getState());

        return formMasterDto;
    }

    /**
     * Convert list of form master entities into details.
     * @param formMasters List of form master entities.
     * @return Returns list of form master details.
     */
    public static List<FormMasterDto> convertListMasterToDtoList(List<FormMaster> formMasters) {
        List<FormMasterDto> formMasterDtos = new LinkedList<>();
        for (FormMaster formMaster : formMasters) {
            formMasterDtos.add(convertMasterToDto(formMaster));
        }
        return formMasterDtos;
    }

}
