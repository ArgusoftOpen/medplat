/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.PncMotherMaster;

import java.util.List;

/**
 * <p>
 * Define methods for pnc mother master.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface PncMotherMasterDao extends GenericDao<PncMotherMaster, Integer> {

    /**
     * Retrieves pnc mothers by member id.
     *
     * @param memberId Member id.
     * @return Returns list of pnc mothers.
     */
    List<PncMotherMaster> getPncMotherbyMemberid(Integer memberId);

}
