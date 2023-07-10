/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.service;

import com.argusoft.medplat.dashboard.basketpreference.dto.UserBasketPreferenceDto;

/**
 * <p>Method signature for user basket preference service</p>
 * @author kunjan
 * @since 26/08/20 11:30 AM
 *
 */
public interface UserBasketPreferenceService {

    /**
     * Is used to create or modify user basket preference
     * @param userBasketPreferenceDto user basket preference dto
     */
    void createOrUpdate(UserBasketPreferenceDto userBasketPreferenceDto);

    /**
     * Retrieve user preference by passing userId
     * @param userId user id
     * @return a string representing the user preference
     */
    String retrievePreferenceByUserId(Integer userId);
    
}
