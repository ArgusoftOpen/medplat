/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.model.PncMaster;

import java.util.List;

/**
 * <p>
 * Define methods for pnc master.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface PncMasterDao extends GenericDao<PncMaster, Integer> {

    /**
     * Retrieves all members whom service date is greater than current date.
     *
     * @return Returns list of members details.
     */
    List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture();

}
