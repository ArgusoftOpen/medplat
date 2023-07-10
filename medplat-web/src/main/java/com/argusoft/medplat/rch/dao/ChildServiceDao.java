/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.model.ChildServiceMaster;

import java.util.List;

/**
 * <p>
 * Define methods for child service.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface ChildServiceDao extends GenericDao<ChildServiceMaster, Integer> {

    /**
     * Retrieves child service by member id.
     *
     * @param childId Child id.
     * @param limit   The number of data need to fetch.
     * @return Returns list of child service details.
     */
    List<ChildServiceMaster> retrieveByMemberId(Integer childId, int limit);

    /**
     * Retrieves medical complications by member id.
     *
     * @param memberId Member id.
     * @return Returns medical complications by member id.
     */
    String retrieveMedicalComplications(Integer memberId);

    /**
     * Retrieves last child visit details by member id.
     *
     * @param memberId Member id.
     * @return Returns last child visit details.
     */
    ChildServiceMaster getLastChildVisit(Integer memberId);

    /**
     * Create cerebral palsy suspected entry.
     *
     * @param memberId       Member id.
     * @param locationId     Location id.
     * @param childServiceId Child service visit id.
     * @param userId         User id.
     */
    void createSuspectedCpEntry(Integer memberId, Integer locationId, Integer childServiceId, Integer userId);

    /**
     * Retrieves all members whom service date is greater than current date.
     *
     * @return Returns list of members details.
     */
    List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture();
}
