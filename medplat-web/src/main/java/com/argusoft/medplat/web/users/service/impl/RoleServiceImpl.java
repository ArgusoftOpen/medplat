
package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.common.dao.RoleHealthInfrastructureDao;
import com.argusoft.medplat.common.model.RoleHealthInfrastructure;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.web.location.dto.LocationTypeMasterDto;
import com.argusoft.medplat.web.users.dao.RoleCategoryDao;
import com.argusoft.medplat.web.users.dao.RoleDao;
import com.argusoft.medplat.web.users.dao.RoleHierarchyManagementDao;
import com.argusoft.medplat.web.users.dao.RoleManagementDao;
import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.mapper.RoleMapper;
import com.argusoft.medplat.web.users.model.RoleCategoryMaster;
import com.argusoft.medplat.web.users.model.RoleHierarchyManagement;
import com.argusoft.medplat.web.users.model.RoleManagement;
import com.argusoft.medplat.web.users.model.RoleMaster;
import com.argusoft.medplat.web.users.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implements methods of RoleService
 * @author vaishali
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleManagementDao roleManagementDao;

    @Autowired
    private RoleHealthInfrastructureDao roleHealthInfrastructureDao;

    @Autowired
    private RoleHierarchyManagementDao roleHierarchyManagementDao;

    @Autowired
    private RoleCategoryDao roleCategoryDao;
    
//    @Autowired
//    private TeamTypeRoleMasterDao teamTypeRoleMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleMasterDto> retrieveAll(Boolean isActive) {
        List<RoleMaster> roleMasters = roleDao.retrieveAll(isActive);
        List<RoleMasterDto> roleMasterDtos = new ArrayList<>();
        for (RoleMaster roleMaster : roleMasters) {
            RoleMasterDto roleMasterDto;
            roleMasterDto = RoleMapper.convertRoleMasterToDto(roleMaster);
            String assignedFeature = roleDao.getAssignedFeaturesByRoleId(roleMaster.getId());
            roleMasterDto.setAssignedFeatures(assignedFeature);
            roleMasterDto.setAssignedFeatureList(roleDao.getAssignedFeatureList(roleMaster.getId())); // Retrieves list of feature name and user menu item id
            roleMasterDtos.add(roleMasterDto);
        }
        return roleMasterDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(RoleMasterDto roleDto)  {
        RoleMaster role = RoleMapper.convertRoleDtoToMaster(roleDto);

        List<RoleMasterDto> roleList = retrieveAll(null);
        handleException(role,roleList);

        if (role.getId() == null) {
            role.setState(RoleMaster.State.ACTIVE);
            roleDao.create(role);
            for (Integer roleId : roleDto.getRoleIds()) {
                RoleManagement roleManagement = new RoleManagement();
                roleManagement.setRoleId(roleId);
                roleManagement.setManagedByRoleId(role.getId());
                roleManagement.setState(RoleManagement.State.ACTIVE);
                roleManagementDao.create(roleManagement);
            }
//            if (roleDto.getManageByTeamTypeIds()!= null) {
//                for (Integer teamTypeId : roleDto.getManageByTeamTypeIds()) {
//                    TeamTypeRoleMaster teamTypeRoleMaster = new TeamTypeRoleMaster();
//                    teamTypeRoleMaster.setRoleId(role.getId());
//                    teamTypeRoleMaster.setTeamTypeId(teamTypeId);
//                    teamTypeRoleMaster.setState(TeamTypeRoleMaster.State.ACTIVE);
//                    teamTypeRoleMasterDao.create(teamTypeRoleMaster);
//                }
//            }
            if (roleDto.getHealthInfrastructureIds() != null) {
                for (Integer infrastructureId : roleDto.getHealthInfrastructureIds()) {
                    RoleHealthInfrastructure healthInfrastructure = new RoleHealthInfrastructure();
                    healthInfrastructure.setRoleId(role.getId());
                    healthInfrastructure.setHealthInfrastructureTypeId(infrastructureId);
                    healthInfrastructure.setState(RoleHealthInfrastructure.State.ACTIVE);
                    roleHealthInfrastructureDao.create(healthInfrastructure);
                }
            }
            //category for role

            handelCategoryForRole(role,roleDto);
        } else {
            RoleMasterDto roleMasterDto = this.retrieveById(role.getId());
            role.setCreatedBy(roleMasterDto.getCreatedBy());
            role.setCreatedOn(roleMasterDto.getCreatedOn());
            roleDao.merge(role);
            List<RoleManagement> roleManagements = roleManagementDao.retrieveRolesManagementByRoleId(role.getId());
            mergeRoleManagement(roleManagements,roleDto,role);
            //Team Type Management
//            handleTeamType(role,roleDto);
            //Team Type Management ends

            //Health Infrastructure Association
            handleHealthInfra(role,roleDto);
            //Health Infra ends

            //Role Category Management
            handleRoleCategoryManagement(role,roleDto);
            //role Category Management ends

            handleRoleHierarchy(role,roleDto);

        }
    }

    /**
     * Handle exception.
     * @param role Define instance of role.
     * @param roleList List of role.
     */
    private void handleException(RoleMaster role,List<RoleMasterDto> roleList){
        for (RoleMasterDto rmd : roleList) {
            if (rmd.getName().equalsIgnoreCase(role.getName()) && role.getId() == null) {
                throw new ImtechoUserException("Role with same name exists", 101);
            }
        }
    }

    /**
     * Handle category for role.
     * @param role  Define instance of role.
     * @param roleDto Define role details.
     */
    private void handelCategoryForRole(RoleMaster role,RoleMasterDto roleDto){
        if (roleDto.getCategoryIds()!= null) {
            for (Integer categoryId :roleDto.getCategoryIds()) {
                RoleCategoryMaster roleCategoryMaster = new RoleCategoryMaster();
                roleCategoryMaster.setRoleId(role.getId());
                roleCategoryMaster.setCategoryId(categoryId);
                roleCategoryMaster.setState(RoleCategoryMaster.State.ACTIVE);
                roleCategoryDao.create(roleCategoryMaster);
            }
        }
        if (roleDto.isCanSelfManage()) {
            RoleManagement roleManagement = new RoleManagement();
            roleManagement.setRoleId(role.getId());
            roleManagement.setManagedByRoleId(role.getId());
            roleManagement.setState(RoleManagement.State.ACTIVE);
            roleManagement.setId(roleManagementDao.create(roleManagement));
        }
        for (LocationTypeMasterDto locationTypeMaster : roleDto.getLocationTypes()) {
            RoleHierarchyManagement roleHierarchyManagement = new RoleHierarchyManagement();
            roleHierarchyManagement.setRoleId(role.getId());
            roleHierarchyManagement.setLocationType(locationTypeMaster.getType());
            roleHierarchyManagement.setLevel(locationTypeMaster.getLevel());
            roleHierarchyManagement.setState(RoleManagement.State.ACTIVE);
            roleHierarchyManagement.setId(roleHierarchyManagementDao.create(roleHierarchyManagement));
        }
    }

    /**
     * Merge role management.
     * @param roleManagements List of role managements.
     * @param roleDto Define role details.
     * @param role Define instance of role.
     */
    private void mergeRoleManagement(List<RoleManagement> roleManagements,RoleMasterDto roleDto,RoleMaster role){
        for (RoleManagement management : roleManagements) {
            management.setState(RoleManagement.State.INACTIVE);
            for (Integer roleId : roleDto.getRoleIds()) {
                if (roleId.equals(management.getRoleId())) {
                    management.setState(RoleManagement.State.ACTIVE);
                }
            }
            roleManagementDao.merge(management);
        }
        for (Integer roleId : roleDto.getRoleIds()) {
            Boolean ifExists = false;
            for (RoleManagement management : roleManagements) {
                if (roleId.equals(management.getRoleId())) {
                    ifExists = true;
                    break;
                }
            }
            if (Boolean.FALSE.equals(ifExists)) {
                RoleManagement roleManagement = new RoleManagement();
                roleManagement.setRoleId(roleId);
                roleManagement.setManagedByRoleId(role.getId());
                roleManagement.setState(RoleManagement.State.ACTIVE);
                roleManagement.setId(roleManagementDao.create(roleManagement));
            }
        }
    }

    /**
     * Handle role category management.
     * @param role Define instance of role.
     * @param roleDto Define role details.
     */
    private void handleRoleCategoryManagement(RoleMaster role,RoleMasterDto roleDto){
        List<RoleCategoryMaster> categoryMasters = roleCategoryDao.retrieveByRoleId(role.getId());
        for (RoleCategoryMaster categoryMaster : categoryMasters) {
            categoryMaster.setState(RoleCategoryMaster.State.INACTIVE);
            for (Integer categoryId : roleDto.getCategoryIds()) {
                if (categoryId.equals(categoryMaster.getCategoryId())) {
                    categoryMaster.setState(RoleCategoryMaster.State.ACTIVE);
                }
            }
            roleCategoryDao.merge(categoryMaster);
        }
        for (Integer categoryId : roleDto.getCategoryIds()) {
            Boolean ifExists = false;
            for (RoleCategoryMaster categoryMaster : categoryMasters) {
                if (categoryId.equals(categoryMaster.getCategoryId())) {
                    ifExists = true;
                    break;
                }
            }
            if (Boolean.FALSE.equals(ifExists)) {
                RoleCategoryMaster categoryMaster = new RoleCategoryMaster();
                categoryMaster.setCategoryId(categoryId);
                categoryMaster.setRoleId(role.getId());
                categoryMaster.setState(RoleCategoryMaster.State.ACTIVE);
                roleCategoryDao.create(categoryMaster);
            }
        }
    }

    /**
     * Handle health infra details.
     * @param role Define instance of role.
     * @param roleDto Define role details.
     */
    private void handleHealthInfra(RoleMaster role,RoleMasterDto roleDto){
        List<RoleHealthInfrastructure> healthInfrastructures = roleHealthInfrastructureDao.retrieveByRoleId(role.getId());
        for (RoleHealthInfrastructure healthInfrastructure : healthInfrastructures) {
            healthInfrastructure.setState(RoleHealthInfrastructure.State.INACTIVE);
            for (Integer healthInfrastructureId : roleDto.getHealthInfrastructureIds()) {
                if (healthInfrastructureId.equals(healthInfrastructure.getHealthInfrastructureTypeId())) {
                    healthInfrastructure.setState(RoleHealthInfrastructure.State.ACTIVE);
                }
            }
            roleHealthInfrastructureDao.merge(healthInfrastructure);
        }
        for (Integer healthInfrastructureId : roleDto.getHealthInfrastructureIds()) {
            Boolean ifExists = false;
            for (RoleHealthInfrastructure healthInfrastructure : healthInfrastructures) {
                if (healthInfrastructureId.equals(healthInfrastructure.getHealthInfrastructureTypeId())) {
                    ifExists = true;
                    break;
                }
            }
            if (Boolean.FALSE.equals(ifExists)) {
                RoleHealthInfrastructure healthInfrastructure = new RoleHealthInfrastructure();
                healthInfrastructure.setHealthInfrastructureTypeId(healthInfrastructureId);
                healthInfrastructure.setRoleId(role.getId());
                healthInfrastructure.setState(RoleHealthInfrastructure.State.ACTIVE);
                roleHealthInfrastructureDao.create(healthInfrastructure);
            }
        }
    }

//    /**
//     * Handle team type.
//     * @param role Define instance of role.
//     * @param roleDto Define role details.
//     */
//    private void handleTeamType(RoleMaster role,RoleMasterDto roleDto){
//        List<TeamTypeRoleMaster> teamTypeRoleMasters = teamTypeRoleMasterDao.retrieveByRoleId(role.getId());
//        for (TeamTypeRoleMaster teamTypeRoleMaster : teamTypeRoleMasters) {
//            teamTypeRoleMaster.setState(TeamTypeRoleMaster.State.INACTIVE);
//            for (Integer teamTypeId : roleDto.getManageByTeamTypeIds()) {
//                if (teamTypeId.equals(teamTypeRoleMaster.getTeamTypeId())) {
//                    teamTypeRoleMaster.setState(TeamTypeRoleMaster.State.ACTIVE);
//                }
//            }
//            teamTypeRoleMasterDao.merge(teamTypeRoleMaster);
//        }
//        for (Integer teamTypeId : roleDto.getManageByTeamTypeIds()) {
//            Boolean ifExists = false;
//            for (TeamTypeRoleMaster teamTypeRoleMaster  : teamTypeRoleMasters) {
//                if (teamTypeId.equals(teamTypeRoleMaster.getTeamTypeId())) {
//                    ifExists = true;
//                }
//            }
//            if (Boolean.FALSE.equals(ifExists)) {
//                TeamTypeRoleMaster teamTypeRoleMaster = new TeamTypeRoleMaster();
//                teamTypeRoleMaster.setTeamTypeId(teamTypeId);
//                teamTypeRoleMaster.setRoleId(role.getId());
//                teamTypeRoleMaster.setState(TeamTypeRoleMaster.State.ACTIVE);
//                teamTypeRoleMasterDao.create(teamTypeRoleMaster);
//            }
//        }
//    }

    /**
     * Handle role hierarchy.
     * @param role Define instance of role.
     * @param roleDto Define role details.
     */
    private void handleRoleHierarchy(RoleMaster role,RoleMasterDto roleDto){
        List<RoleHierarchyManagement> roleHierarchyManagements = roleHierarchyManagementDao.retrieveLocationByRoleId(role.getId());
        for (RoleHierarchyManagement hierarchyManagement : roleHierarchyManagements) {
            hierarchyManagement.setState(RoleManagement.State.INACTIVE);
            for (LocationTypeMasterDto locationTypeMasterDto : roleDto.getLocationTypes()) {
                if (locationTypeMasterDto.getType().equals(hierarchyManagement.getLocationType())) {
                    hierarchyManagement.setState(RoleManagement.State.ACTIVE);
                }
            }
            roleHierarchyManagementDao.merge(hierarchyManagement);
        }
        for (LocationTypeMasterDto locationTypeMasterDto : roleDto.getLocationTypes()) {
            Boolean ifExists = false;
            for (RoleHierarchyManagement hierarchyManagement : roleHierarchyManagements) {
                if (locationTypeMasterDto.getType().equals(hierarchyManagement.getLocationType())) {
                    ifExists = true;
                    break;
                }
            }
            if (Boolean.FALSE.equals(ifExists)) {
                RoleHierarchyManagement roleHierarchyManagement = new RoleHierarchyManagement();
                roleHierarchyManagement.setRoleId(role.getId());
                roleHierarchyManagement.setLocationType(locationTypeMasterDto.getType());
                roleHierarchyManagement.setLevel(locationTypeMasterDto.getLevel());
                roleHierarchyManagement.setState(RoleManagement.State.ACTIVE);
                roleHierarchyManagement.setId(roleHierarchyManagementDao.create(roleHierarchyManagement));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleMasterDto> getRolesByIds(Set<Integer> trainerRoleIds) {
        return RoleMapper.convertMasterListToDto(roleDao.getRolesByIds(trainerRoleIds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleMasterDto retrieveById(Integer id)  {
        RoleMaster role = roleDao.retrieveById(id);
        if (role != null) {
            RoleMasterDto roleDto = RoleMapper.convertRoleMasterToDto(role);
            List<RoleManagement> roleManagements = roleManagementDao.retrieveActiveRolesManagementByRoleId(id);
//            List<TeamTypeRoleMaster> teamTypeRoleMasters = teamTypeRoleMasterDao.retrieveByRoleId(id);
            List<RoleHealthInfrastructure> healthInfrastructures = roleHealthInfrastructureDao.retrieveByRoleId(id);
            List<RoleCategoryMaster> categoryMasters = roleCategoryDao.retrieveByRoleId(id);
            List<RoleHierarchyManagement> roleHierarchyManagements = roleHierarchyManagementDao.retrieveActiveLocationByRoleId(id);
            List<Integer> roleIds = new ArrayList<>();
//            List<Integer> teamTypeIds = new ArrayList<>();
            List<Integer> healthInfrastructureIds = new ArrayList<>();
            List<Integer> categoryIds = new ArrayList<>();
            List<LocationTypeMasterDto> locationTypes = new ArrayList<>();

            for (RoleManagement rm : roleManagements) {
                roleIds.add(rm.getRoleId());
            }
//            for (TeamTypeRoleMaster tr : teamTypeRoleMasters) {
//                teamTypeIds.add(tr.getTeamTypeId());
//            }
            for (RoleHealthInfrastructure healthInfrastructure : healthInfrastructures) {
                healthInfrastructureIds.add(healthInfrastructure.getHealthInfrastructureTypeId());
            }
            for (RoleCategoryMaster categoryMaster : categoryMasters) {
                categoryIds.add(categoryMaster.getCategoryId());
            }
            for (RoleHierarchyManagement rhm : roleHierarchyManagements) {
                LocationTypeMasterDto locationTypeMasterDto = new LocationTypeMasterDto();
                locationTypeMasterDto.setType(rhm.getLocationType());
                locationTypeMasterDto.setLevel(rhm.getLevel());
                locationTypes.add(locationTypeMasterDto);
            }

            roleDto.setRoleIds(roleIds);
//            roleDto.setManageByTeamTypeIds(teamTypeIds);
            roleDto.setHealthInfrastructureIds(healthInfrastructureIds);
            roleDto.setLocationTypes(locationTypes);
            roleDto.setCategoryIds(categoryIds);

            return roleDto;
        } else {
            throw new ImtechoUserException("Role does not exist with this id", 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(RoleMasterDto roleDto, Boolean isActive) {
        if (Boolean.TRUE.equals(isActive)) {
            RoleMasterDto roleMasterDto = this.retrieveById(roleDto.getId());
            roleDto.setCreatedBy(roleMasterDto.getCreatedBy());
            roleDto.setCreatedOn(roleMasterDto.getCreatedOn());
            roleDto.setState(RoleMaster.State.ACTIVE);
        } else {
            RoleMasterDto roleMasterDto = this.retrieveById(roleDto.getId());
            roleDto.setCreatedBy(roleMasterDto.getCreatedBy());
            roleDto.setCreatedOn(roleMasterDto.getCreatedOn());
            roleDto.setState(RoleMaster.State.INACTIVE);
        }
        RoleMaster role = RoleMapper.convertRoleDtoToMaster(roleDto);
        role.setCreatedBy(roleDto.getCreatedBy());
        role.setCreatedOn(roleDto.getCreatedOn());
        roleDao.merge(role);
    }
}
