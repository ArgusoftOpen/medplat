/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dto.AncMasterDto;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Define services for anc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface AncService {

    /**
     * Store anc visit form details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storeAncVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    /**
     * Add anc form details.
     *
     * @param ancMasterDto Anc form details.
     * @return Returns id of anc master.
     */
    Integer create(AncMasterDto ancMasterDto);

    List<MemberDto> retrieveAncMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet);
}
