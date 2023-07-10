package com.argusoft.medplat.web.users.service;

import com.argusoft.medplat.web.users.model.UserHealthInfrastructure;

import java.util.List;

public interface UserHealthInfrastructureService {

    List<UserHealthInfrastructure> retrieveByUserId(Integer userId, Long modifiedOn);

}
