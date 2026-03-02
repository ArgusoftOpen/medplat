
package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.web.users.dao.RoleDao;
import com.argusoft.medplat.web.users.dao.RoleHierarchyManagementDao;
import com.argusoft.medplat.web.users.mapper.RoleHierarchyManagementMapper;
import com.argusoft.medplat.web.users.mapper.RoleMapper;
import com.argusoft.medplat.web.users.model.RoleHierarchyManagement;
import com.argusoft.medplat.web.users.model.RoleMaster;
import com.argusoft.medplat.web.users.service.RoleHierarchyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements methods of RoleHierarchyManagementService
 * @author vaishali
 * @since 28/08/2020 4:30
 */
@Service()
@Transactional
public class RoleHierarchyManagementServiceImpl implements RoleHierarchyManagementService {

    @Autowired
    RoleHierarchyManagementDao roleHierarchyManagementdao;
    @Autowired
    RoleDao roleDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> retrieveLocationHierarchyByRoleId(Integer roleId) {
        Map<String, Object> result = new HashMap<>();
        
        List<RoleHierarchyManagement> roleHierarchyList = roleHierarchyManagementdao.retrieveLocationByRoleId(roleId);
        RoleMaster roleMaster = roleDao.retrieveById(roleId);
        if (roleHierarchyList != null) {
            result.put("hierarchy", RoleHierarchyManagementMapper.convertMasterListToDtoList(roleHierarchyList));
        }
        if(roleMaster != null) {
            result.put("roles", RoleMapper.convertRoleMasterToDto(roleMaster));
        }
        
        return result;
    }

}
