/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.code.mapper;

import com.argusoft.medplat.code.dto.SystemCodeListDto;
import com.argusoft.medplat.code.model.SystemCodeListMaster;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * An util class for system code list to convert dto to modal or modal to dto
 * </p>
 *
 * @author vaishali
 * @since 16/09/2020 4:30
 */
public class SystemCodeListMapper {

    private SystemCodeListMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Converts system code list modal to dto
     *
     * @param code An instance of SystemCodeListMaster
     * @return An instance of SystemCodeListDto
     */
    public static SystemCodeListDto convertCodeMasterToDto(SystemCodeListMaster code) {
        SystemCodeListDto codeDto = new SystemCodeListDto();
        codeDto.setCode(code.getCode());
        codeDto.setDescription(code.getDescription());
        codeDto.setName(code.getName());
        codeDto.setId(code.getId());
        codeDto.setCodeCategory(code.getCodeCategory());
        codeDto.setCodeType(code.getCodeType());
        codeDto.setEffectiveDate(code.getEffectiveDate());
        codeDto.setParentCode(code.getParentCode());
        codeDto.setPublishedEdition(code.getPublishedEdition());
        codeDto.setCodeId(code.getCodeId());
        codeDto.setDescTypeId(code.getDescTypeId());
        codeDto.setIsActive(code.getIsActive());
        codeDto.setOtherDetails(code.getOtherDetails());

        return codeDto;
    }

    /**
     * Converts a list of system code list modal to dto
     *
     * @param codes A list of SystemCodeListMaster
     * @return A list of SystemCodeListDto
     */
    public static List<SystemCodeListDto> convertMasterListToDto(List<SystemCodeListMaster> codes) {
        List<SystemCodeListDto> roleMasterDtos = new LinkedList<>();
        for (SystemCodeListMaster code : codes) {
            roleMasterDtos.add(convertCodeMasterToDto(code));
        }
        return roleMasterDtos;
    }

    /**
     * Converts json object to system code list dto
     *
     * @param jsonObj     An instance of JsonNode
     * @param version     A version number
     * @param code        A code value
     * @param childCode   A child code
     * @param releaseYear A release year
     * @return An instance of SystemCodeListDto
     */
    public static SystemCodeListDto convertJSONObjectToSystemCodeListDto(JsonNode jsonObj, Integer version, String code, String childCode, Integer releaseYear) {
        SystemCodeListDto systemCodeListDto = new SystemCodeListDto();
        systemCodeListDto.setId(UUID.randomUUID());
        systemCodeListDto.setCodeType("ICD_" + version);

        systemCodeListDto.setCodeCategory(code);

        systemCodeListDto.setCodeId(jsonObj.get("@id").textValue());

        if (childCode == null) {
            systemCodeListDto.setCode(code);
        } else {
            systemCodeListDto.setCode(childCode);
        }

        systemCodeListDto.setParentCode(code);

        systemCodeListDto.setName(jsonObj.get("title").get("@value").textValue());

        systemCodeListDto.setPublishedEdition(version + "/" + releaseYear);
        systemCodeListDto.setIsActive(Boolean.TRUE);

        return systemCodeListDto;
    }
}
