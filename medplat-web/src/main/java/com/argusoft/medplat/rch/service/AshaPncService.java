package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.model.AshaPncChildMaster;
import com.argusoft.medplat.rch.model.AshaPncMotherMaster;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Map;

/**
 * <p>
 * Define services for ASHA pnc service.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface AshaPncService {

    /**
     * Retrieves pnc mother master details by pnc master id and member id.
     *
     * @param pncMasterId Pnc master id.
     * @param memberId    Member id.
     * @return Returns pnc mother master details.
     */
    AshaPncMotherMaster retrievePncMotherMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId);

    /**
     * Retrieves pnc child master details by pnc master id and member id.
     *
     * @param pncMasterId Pnc master id.
     * @param memberId    Member id.
     * @return Returns pnc child master details.
     */
    AshaPncChildMaster retrievePncChildMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId);

    /**
     * Stor pnc visit form details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storePncServiceVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

}
