package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.web.users.dao.UserHealthInfrastructureDao;
import com.argusoft.medplat.web.users.model.UserHealthInfrastructure;
import com.argusoft.medplat.web.users.service.UserHealthInfrastructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserHealthInfrastructureServiceImpl implements UserHealthInfrastructureService {

    @Autowired
    private UserHealthInfrastructureDao userHealthInfrastructureDao;

    @Override
    public List<UserHealthInfrastructure> retrieveByUserId(Integer userId, Long modifiedOn) {
        Date lastModifiedDate = null;
        if (modifiedOn != null && modifiedOn != 0L) {
            lastModifiedDate = new Date(modifiedOn);
        }
        return userHealthInfrastructureDao.retrieveByUserId(userId, lastModifiedDate);
    }
}
