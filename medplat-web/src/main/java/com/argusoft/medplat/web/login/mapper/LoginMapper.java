/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.login.mapper;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.web.login.dto.LoginDto;

/**
 *<p>
 *    An util class for login to convert modal to dto  or dto to modal
 *</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class LoginMapper {

    private LoginMapper() {
            
    }

    /**
     * Converts imtecho security user modal to login dto
     * @param user An instance of ImtechoSecurityUser
     * @return An instance of LoginDto
     */
    public static LoginDto convertToLoginDto(ImtechoSecurityUser user) {
        LoginDto loginDto = new LoginDto(); 
        loginDto.setId(user.getId());
        loginDto.setName(user.getName());
        loginDto.setRoleId(user.getRoleId());
        loginDto.setRoleName(user.getRoleName());
        loginDto.setUserName(user.getUserName())    ;
        loginDto.setMinLocationId(user.getMinLocationId());
        loginDto.setMinLocationName(user.getMinLocationName());
        loginDto.setRchInstitutionId(user.getRchInstitutionId());
        loginDto.setLanguagePreference(user.getLanguagePreference());
        loginDto.setAddress(user.getAddress());
        loginDto.setGender(user.getGender());
        loginDto.setDob(user.getDob());
        loginDto.setMinLocationLevel(user.getMinLocationLevel());
        return loginDto;
    }
    
}
