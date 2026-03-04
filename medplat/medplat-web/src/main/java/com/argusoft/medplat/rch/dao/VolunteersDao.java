/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.VolunteersMaster;

import java.util.Date;

/**
 * <p>
 * Define methods for volunteers.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 10:19 AM
 */
public interface VolunteersDao extends GenericDao<VolunteersMaster, Integer> {

    /**
     * Retrieves volunteers details by health infrastructure id and moth year.
     *
     * @param healthInfrastructureId Health infrastructure id.
     * @param monthYear              Month year.
     * @return Returns volunteers details.
     */
    VolunteersMaster retrieveData(Integer healthInfrastructureId, Date monthYear);

}
