
package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.web.users.dao.RoleManagementDao;
import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.mapper.RoleMapper;
import com.argusoft.medplat.web.users.model.RoleManagement;
import com.argusoft.medplat.web.users.service.RoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements methods of RoleManagementService
 * @author vaishali
 * @since 28/08/2020 4:30
 */
@Service()
@Transactional
public class RoleManagementServiceImpl implements RoleManagementService {

    @Autowired
    private RoleManagementDao roleManagementDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleMasterDto> retrieveRolesManagedByRoleId(Integer roleId) {
        List<RoleMasterDto> roles = new LinkedList<>();
        for (RoleManagement role : roleManagementDao.retrieveRolesManagedByRoleId(roleId)) {
            roles.add(RoleMapper.convertRoleMasterToDto(role.getRoleMaster()));
        }
        return roles;
    }

}
