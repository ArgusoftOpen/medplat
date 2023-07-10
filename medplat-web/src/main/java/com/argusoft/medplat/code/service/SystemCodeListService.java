package com.argusoft.medplat.code.service;

import com.argusoft.medplat.code.dto.SystemCodeListDto;

import java.util.List;

/**
 * <p>
 *     Defines methods for system code list
 * </p>
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */
public interface SystemCodeListService {

    /**
     * Creates or update a list of system list code
     * @param dto A list of SystemCodeListDto
     * @return A list of SystemCodeListDto
     */
    List<SystemCodeListDto> saveOrUpdate(List<SystemCodeListDto> dto);

    /**
     * Creates or update system list code
     * @param dto An instance of SystemCodeListDto
     * @return An instance of SystemCodeListDto
     */
    SystemCodeListDto saveOrUpdate(SystemCodeListDto dto);
    
    /**
     * Returns a list of system code based on given search parameter
     * @param searchNameString A name search string
     * @param descTypeId A description type id
     * @param moduleId A module id
     * @param codeType A code type
     * @return A list of SystemCodeListDto
     */
    List<SystemCodeListDto> getSystemCodes(String searchNameString,String descTypeId,String moduleId, String codeType );

    /**
     * Returns a list of system code based on given code id and code type
     * @param codeId A code id
     * @param codeType A code type
     * @return A list of SystemCodeListDto
     */
    List<SystemCodeListDto> getSystemCodesByCodeId(String codeId, String codeType);

    /**
     * Returns a list of system code based on given code and code type
     * @param code A code value
     * @param codeType A code type
     * @return A list of SystemCodeListDto
     */
    List<SystemCodeListDto> getSystemCodesByCode(String code, String codeType);
    
}
