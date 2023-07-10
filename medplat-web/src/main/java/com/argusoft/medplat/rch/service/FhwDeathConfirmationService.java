package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Map;

/**
 * <p>
 * Define services for FHW death confirmation.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface FhwDeathConfirmationService {

    /**
     * Store death confirmation details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storeDeathConfirmationForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
}
