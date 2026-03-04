/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;

import java.util.Map;

/**
 *
 * <p>
 *     Define services for lmp follow up.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
public interface LmpFollowUpService {

    /**
     * Store follow up details.
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap Contains key and answers.
     * @return Returns id of store details.
     */
    Integer storeLmpFollowUpForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap);
}
