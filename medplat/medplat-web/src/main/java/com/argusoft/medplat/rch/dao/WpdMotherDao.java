/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.rch.dto.WpdMasterDto;
import com.argusoft.medplat.rch.model.WpdMotherMaster;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Define methods for wpd mother.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface WpdMotherDao extends GenericDao<WpdMotherMaster, Integer> {

    /**
     * Retrieves pending discharge list by user id for wpd.
     *
     * @param userId User id.
     * @return Returns list of pending discharge list.
     */
    List<WpdMasterDto> retrievePendingDischargeList(Integer userId);

    /**
     * Retrieves wpd mother by member id.
     *
     * @param memberId Member id.
     * @return Returns wpd mother details for given member id.
     */
    List<WpdMotherMaster> getWpdMotherbyMemberid(Integer memberId);

    List<WpdMotherMaster> getWpdMotherbyMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfrasturctureId);

    /**
     * Retrieves wpd mother details by pregnancy registration id, member id.
     *
     * @param pregnancyRegistrationId Pregnancy registration id.
     * @param memberId                Member id.
     * @return Returns list of wpd mothers for given criteria.
     */
    List<WpdMotherMaster> getWpdMotherMasterByCriteria(Integer pregnancyRegistrationId, Integer memberId);

    /**
     * Retrieves wpd mother details for breast feeding update.
     *
     * @param actionDate Action date.
     * @param memberId   Member id.
     * @return Returns mother details for breast feeding update.
     */
    WpdMotherMaster getWpdMotherMasterForBreastFeedingUpdate(Date actionDate, Integer memberId);

    /**
     * Update discharge details for member.
     *
     * @param memberId      Member id.
     * @param dischargeDate Discharge date.
     * @param pregRegId     Pregnancy registration id.
     * @param userId        User id.
     */
    void updateDischargeDetailsOfMember(Integer memberId, Date dischargeDate, Integer pregRegId, Integer userId);

    List<MemberDto> retrieveWpdMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet);
}
