/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.mapper;

import com.argusoft.medplat.mobile.dto.UserFormAccessBean;
import com.argusoft.medplat.mobile.model.UserFormAccess;

/**
 * @author avani
 */
public class UserFormAccessMapper {

    private UserFormAccessMapper() {
        throw new IllegalStateException("Utility Class");
    }

    public static UserFormAccessBean convertUserFormAccessToUserFormAccessBean(UserFormAccess userFormAccess) {

        UserFormAccessBean userFormAccessBean = new UserFormAccessBean();

        userFormAccessBean.setUserId(userFormAccess.getUserFormAccessPKey().getUserId());
        userFormAccessBean.setFormCode(userFormAccess.getUserFormAccessPKey().getFormCode());
        userFormAccessBean.setState(userFormAccess.getState().name());
        userFormAccessBean.setCreatedOn(userFormAccess.getCreatedOn());

        return userFormAccessBean;
    }

}
