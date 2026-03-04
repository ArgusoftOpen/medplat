/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.service.RoleManagementService;
import com.argusoft.medplat.web.users.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *<p>Defines rest end points for role</p>
 * @author vaishali
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/role")
@Tag(name = "Role Controller", description = "")
public class RoleController {

    @Autowired
    RoleService roleService;
    @Autowired
    RoleManagementService roleManagementService;
    @Autowired
    private ImtechoSecurityUser user;

    /**
     * Returns list of role based on given status
     * @param isActive A status value for role
     * @return A list of RoleMasterDto
     */
    @GetMapping(value = "")
    public List<RoleMasterDto> retrieveRoles(@RequestParam(name="is_active",required = false) Boolean isActive) {
        return roleService.retrieveAll(isActive);
    }

    /**
     * Returns all roles for admin or list of role mange by logged in user
     * @param isAdmin A boolean value for retrieve all role or only role managed by logged in user
     * @return A list of RoleMasterDto
     */
    @GetMapping(value = "/byroleid")
    public List<RoleMasterDto> retrieveRolesByRoleId(@RequestParam(name="is_admin",required = false) boolean isAdmin) {
        if (isAdmin) {
            return roleService.retrieveAll(true);
        } else {
            return roleManagementService.retrieveRolesManagedByRoleId(user.getRoleId());
        }
    }

    /**
     * Create or update given role
     * @param roleDto An instance of RoleMasterDto
     */
    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody RoleMasterDto roleDto)  {
        roleService.createOrUpdate(roleDto);
    }

    /**
     * Returns role of given id
     * @param id An id of role
     * @return An instance of RoleMasterDto
     */
    @GetMapping(value = "/{id}")
    public RoleMasterDto retrieveById(@PathVariable() Integer id) {
        return roleService.retrieveById(id);
    }

    /**
     * Updates status of given role
     * @param roleDto An instance of RoleMasterDto
     * @param isActive A status value for role
     */
    @PutMapping(value = "")
    public void toggleActive(@RequestBody RoleMasterDto roleDto, @RequestParam("is_active") Boolean isActive) {
        roleService.toggleActive(roleDto, isActive);
    }
}