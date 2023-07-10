/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.mapper;

import com.argusoft.medplat.dashboard.basketpreference.dto.UserBasketPreferenceDto;
import com.argusoft.medplat.dashboard.basketpreference.model.UserBasketPreference;

/**
 * <p>Data conversion method for user basket preference</p>
 * @author kunjan
 * @since 26/08/20 11:30 AM
 *
 */
public class UserBasketPreferenceMapper {

    private UserBasketPreferenceMapper() {
    }

    /**
     * Returns UserBasketPreferenceDto based on given UserBasketPreference
     * @param userBasketPreference user basket preference
     * @return UserBasketPreferenceDto
     */
    public static UserBasketPreferenceDto convertUserBasketPreferenceToUserBasketPreferenceDto(UserBasketPreference userBasketPreference) {
        UserBasketPreferenceDto userBasketPreferenceDto = new UserBasketPreferenceDto();
        userBasketPreferenceDto.setId(userBasketPreference.getId());
        userBasketPreferenceDto.setUserId(userBasketPreference.getUserId());
        userBasketPreferenceDto.setPreference(userBasketPreference.getPreference());
        return userBasketPreferenceDto;
    }

    /**
     * Returns UserBasketPreference based on given UserBasketPreferenceDto
     * @param userBasketPreferenceDto user basket preference dto
     * @return UserBasketPreference
     */
    public static UserBasketPreference convertUserBasketPreferenceDtoToUserBasketPreference(UserBasketPreferenceDto userBasketPreferenceDto) {
        UserBasketPreference userBasketPreference = new UserBasketPreference();
        userBasketPreference.setId(userBasketPreferenceDto.getId());
        userBasketPreference.setUserId(userBasketPreferenceDto.getUserId());
        userBasketPreference.setPreference(userBasketPreferenceDto.getPreference());
        return userBasketPreference;
    }
}
