/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.model.AncVisit;

import java.util.List;

/**
 * <p>
 * Define methods for anc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface AncVisitDao extends GenericDao<AncVisit, Integer> {

    /**
     * Retrieves member details whom service date is greater then current date.
     *
     * @return Returns list of member service details.
     */
    List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture();

    /**
     * Retrieves anc service details by member id
     *
     * @return Returns list of anc service details.
     */
    List<AncVisit> retrieveByMemberId(Integer memberId);

    List<AncVisit> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfrasturctureId);

    List<MemberDto> retrieveAncMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet);
}
