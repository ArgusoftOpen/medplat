/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.service;

import com.argusoft.medplat.fhs.dto.AnganwadiDto;
import java.util.List;

/**
 *
 * <p>
 *     Define all services for anganwadi.
 * </p>
 * @author shrey
 * @since 26/08/20 11:00 AM
 *
 */
public interface AnganwadiService {
    /**
     * Retrieves all anganwadi by location id.
     * @param locationId Id of location.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @return Returns list of anganwadi details.
     */
    List<AnganwadiDto> getAnganwadisByUserId(Integer locationId,Integer limit,Integer offset);

    /**
     * Used to toggle the state of anganwadi.
     * @param id Id of anganwadi.
     * @param isActive Is anganwadi active or not.
     */
    void toggleActive(Integer id,Boolean isActive);

    /**
     * Add anganwadi details.
     * @param anganwadiDto Details of anganwadi.
     */
    void createAnganwadi(AnganwadiDto anganwadiDto);

    /**
     * Retrieves anganwadi by id.
     * @param id Id of anganwadi.
     * @return Returns anganwadi details.
     */
    AnganwadiDto getAnganwadiById(Integer id);

    /**
     * Update anganwadi.
     * @param anganwadiDto Details of anganwadi.
     */
    void updateAnganwadi(AnganwadiDto anganwadiDto) ;

    /**
     * Check for ICDS available or not.
     * @param code ICDS code.
     * @return Returns ICDS available or not.
     */
    boolean isIcdsAvailable(String code);
}
