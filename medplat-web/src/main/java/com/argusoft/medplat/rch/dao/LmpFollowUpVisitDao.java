/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.model.LmpFollowUpVisit;

import java.util.List;

/**
 *
 * <p>
 * Define methods for lmp follow up visits.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface LmpFollowUpVisitDao extends GenericDao<LmpFollowUpVisit, Integer> {

    /**
     * Update anmol details by lmp follow up visit id.
     * @param id Lmp follow up visit Id.
     * @param anmolId Anmol id.
     * @param status Define status like SUCCESS, FAIL.
     * @param code Define code for anmol follow up wsdl.
     * @param caseNo Define case number.
     * @param xml define xml sheet.
     */
    void updateAnmolById(Integer id, String anmolId,String status,String code,int caseNo,String xml);

    /**
     * Retrieves all members whom service date is greater than current date.
     * @return Returns list of members details.
     */
    List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture();


}
