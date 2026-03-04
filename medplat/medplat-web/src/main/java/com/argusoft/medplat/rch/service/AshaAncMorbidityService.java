package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Map;

/**
 * <p>
 * Define services for ASHA anc morbidity.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface AshaAncMorbidityService {

    /**
     * Store ASHA anc morbidity details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param dependencyId     Dependency id.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storeAshaAncMorbidity(ParsedRecordBean parsedRecordBean, Integer dependencyId, Map<String, String> keyAndAnswerMap, UserMaster user);
}
