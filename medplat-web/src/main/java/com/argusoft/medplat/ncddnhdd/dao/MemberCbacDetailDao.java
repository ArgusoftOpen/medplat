/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.model.MemberCbacDetail;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for member CBAC details.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public interface MemberCbacDetailDao extends GenericDao<MemberCbacDetail, Integer> {

    /**
     * Retrieves CBAC details for ASHA.
     * @param userId User id.
     * @param lastModifiedOn Last modified on date.
     * @return Returns list of member's CBAC details.
     */
    List<MemberCbacDetail> retrieveCbacDetailsForAsha(Integer userId, Date lastModifiedOn);
}
