/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.rch.dto.VolunteersDto;

import java.util.Date;

/**
 * <p>
 * Define services for volunteers.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 11:00 AM
 */
public interface VolunteersService {

    /**
     * Add volunteers details.
     *
     * @param volunteersDto Volunteers details.
     */
    void createOrUpdate(VolunteersDto volunteersDto);

    /**
     * Retrieves volunteers details by health infra structure.
     *
     * @param healthInfrastructureId Health infrastructure id.
     * @param monthYear              Define month year.
     * @return Returns volunteers details.
     */
    VolunteersDto retrieveData(Integer healthInfrastructureId, Date monthYear);

}
