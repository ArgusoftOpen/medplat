/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.PncChildMaster;

import java.util.List;

/**
 * <p>
 * Define methods for phc child master.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface PncChildMasterDao extends GenericDao<PncChildMaster, Integer> {

    /**
     * Retrieves pnc child by member id.
     *
     * @param memberId Member id.
     * @return Returns list of pnc children details.
     */
    List<PncChildMaster> getPncChildbyMemberid(Integer memberId);

}
