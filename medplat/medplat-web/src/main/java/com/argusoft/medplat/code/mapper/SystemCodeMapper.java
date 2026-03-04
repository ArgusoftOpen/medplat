/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.code.mapper;

import com.argusoft.medplat.code.dto.SystemCodeDto;
import com.argusoft.medplat.code.model.SystemCodeMaster;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * An util class for system code to convert dto to modal or modal to dto
 * </p>
 *
 * @author vaishali
 * @since 16/09/2020 4:30
 */
public class SystemCodeMapper {

    private SystemCodeMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Converts system code modal to dto
     *
     * @param code An instance of SystemCodeMaster
     * @return An instance of SystemCodeDto
     */
    public static SystemCodeDto convertCodeMasterToDto(SystemCodeMaster code) {
        SystemCodeDto codeDto = new SystemCodeDto();
        codeDto.setCode(code.getCode());
        codeDto.setDescription(code.getDescription());
        codeDto.setId(code.getId());
        codeDto.setTableType(code.getTableType());
        codeDto.setCodeType(code.getCodeType());
        codeDto.setTableId(code.getTableId());
        codeDto.setParentCode(code.getParentCode());

        return codeDto;
    }

    /**
     * Converts a list of system code modal to dto
     *
     * @param codes A list of SystemCodeMaster
     * @return A list of SystemCodeDto
     */
    public static List<SystemCodeDto> convertMasterListToDto(List<SystemCodeMaster> codes) {
        List<SystemCodeDto> roleMasterDtos = new LinkedList<>();
        for (SystemCodeMaster code : codes) {
            roleMasterDtos.add(convertCodeMasterToDto(code));
        }
        return roleMasterDtos;
    }

    /**
     * Converts system code dto to modal
     *
     * @param dto  An instance of SystemCodeDto
     * @param code An instance of SystemCodeMaster
     * @return An instance of SystemCodeMaster
     */
    public static SystemCodeMaster convertRoleDtoToMaster(SystemCodeDto dto, SystemCodeMaster code) {

        if (code == null) {
            code = new SystemCodeMaster();
        }

        code.setCode(dto.getCode() != null ? dto.getCode().trim() : null);
        code.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        code.setId(dto.getId());
        code.setTableType(dto.getTableType() != null ? dto.getTableType().trim() : null);
        code.setCodeType(dto.getCodeType() != null ? dto.getCodeType().trim() : null);
        code.setTableId(dto.getTableId());
        code.setParentCode(dto.getParentCode() != null ? dto.getParentCode().trim() : null);
        return code;
    }
}
