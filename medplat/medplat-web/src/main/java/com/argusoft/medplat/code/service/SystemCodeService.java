/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.code.service;

import com.argusoft.medplat.code.dto.SystemCodeDto;
import com.argusoft.medplat.code.mapper.CodeType;
import com.argusoft.medplat.code.mapper.TableType;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *     Defines methods for system code
 * </p>
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */
public interface SystemCodeService {

    /**
     * Creates or update system code
     * @param dto An instance of SystemCodeDto
     * @return An instance of SystemCodeDto
     */
    SystemCodeDto saveOrUpdate(SystemCodeDto dto);

    /**
     * Returns a system code of given id
     * @param id A system code id
     * @return An instance of SystemCodeDto
     */
    SystemCodeDto getById(UUID id);

    /**
     * Returns a list of system code based on given type and code
     * @param tableType A table type
     * @param tableId A table id
     * @param codeType A code type
     * @return A list of SystemCodeDto
     */
    public List<SystemCodeDto> getSystemCodesByTypeAndCode(TableType tableType, String tableId, CodeType codeType);
    /**
     * Returns a list of system code based on given type
     * @param tableType A table type
     * @return A list of SystemCodeDto
     */
    List<SystemCodeDto> getSystemCodesByTableType(String tableType);

    /**
     * Returns a list of system code based on given code
     * @param codeType A code type
     * @return A list of SystemCodeDto
     */
    List<SystemCodeDto> getSystemCodesByCodeType(String codeType);

    /**
     * Returns a list of system code
     * @return A list of SystemCodeDto
     */
    List<SystemCodeDto> getSystemCodes();

    /**
     * Deletes system code of given id
     * @param id A system code id
     */
    void deleteSystemCode(UUID id);
}
