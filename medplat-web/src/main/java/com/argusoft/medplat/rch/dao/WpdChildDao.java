/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.WpdChildMaster;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Define methods for wpd child.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface WpdChildDao extends GenericDao<WpdChildMaster, Integer> {

    /**
     * Retrieves wpd children by member id.
     *
     * @param memberId Member id.
     * @return Returns list of wpd children.
     */
    List<WpdChildMaster> getWpdChildbyMemberid(Integer memberId);

    /**
     * Retrieves wpd children by mother id.
     *
     * @param wpdMotherId Wpd mother id.
     * @return Returns list of wpd children.
     */
    List<WpdChildMaster> getWpdChildbyWpdMotherId(Integer wpdMotherId);

    /**
     * Check for wpd mother has child or not.
     *
     * @param wpdMotherId Wpd mother id.
     * @return Returns true/false based on wpd mother has child or not.
     */
    boolean getWpdChildExistsByWpdMotherId(Integer wpdMotherId);

    /**
     * Add query details for patch notification.
     *
     * @param memberId Member id.
     * @param familyId Family id.
     * @param dob      Date of birth.
     */
    void insertQueryForPatchNotification(Integer memberId, Integer familyId, Date dob);

}
