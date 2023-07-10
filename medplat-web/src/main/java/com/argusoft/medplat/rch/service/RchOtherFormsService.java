package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Map;

/**
 * <p>
 * Define services for rch other forms.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface RchOtherFormsService {

    /**
     * Store FHSR phone verification form details.
     *
     * @param user            User details.
     * @param keyAndAnswerMap Contains key and answers.
     * @return Returns id of store details.
     */
    Integer storeFhsrPhoneVerificationForm(UserMaster user, Map<String, String> keyAndAnswerMap);

    /**
     * Store TT2 alert form details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storeTT2AlertForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    /**
     * Store iron sucrose form details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap  Contains key and answers.
     * @param user             User details.
     * @return Returns id of store details.
     */
    Integer storeIronSucroseForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
}
