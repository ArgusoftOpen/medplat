
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dto.PncChildDto;
import com.argusoft.medplat.rch.dto.PncMasterDto;
import com.argusoft.medplat.rch.dto.PncMotherDto;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Define services for pnc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface PncService {

    /**
     * Store pnc visit form.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storePncVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    /**
     * Add pnc visit details.
     *
     * @param pncMasterDto Pnc master details.
     */
    void create(PncMasterDto pncMasterDto);

    /**
     * Retrieves pnc child by member id.
     *
     * @param memberId Member id.
     * @return Returns list of pnc children details.
     */
    List<PncChildDto> getPncChildbyMemberid(Integer memberId);

    /**
     * Retrieves pnc mothers by member id.
     *
     * @param memberId Member id.
     * @return Returns list of pnc mothers.
     */
    List<PncMotherDto> getPncMotherbyMemberid(Integer memberId);
}
