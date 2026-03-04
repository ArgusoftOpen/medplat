/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.controller;
import io.swagger.v3.oas.annotations.tags.Tag;


import com.argusoft.medplat.common.dto.SystemConfigSyncDto;
import com.argusoft.medplat.common.service.SystemConfigSyncService;
import com.argusoft.medplat.exception.ImtechoUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author Hiren Morzariya
 */
@RestController
@RequestMapping("/api/systemconfigsync")
@Tag(name = "System Config Sync Controller", description = "")
public class SystemConfigSyncController {

    @Autowired
    private SystemConfigSyncService systemConfigSyncService;

    @GetMapping(value = "/load/config")
    public List<SystemConfigSyncDto> techoDailyReport(
//            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "id") Integer id,
            @RequestHeader(value = "password") String serverPassword,
            @RequestHeader(value = "serverName") String serverName
    ) {

        if (serverName == null || serverPassword == null) {
            throw new ImtechoUserException("Please add valid username,password in request Header", 401);
        }
        if (id == null) {
            throw new ImtechoUserException("please add id in request body", 500);
        }
        if (!systemConfigSyncService.checkPassword(serverName, serverPassword)) {
            throw new ImtechoUserException("Please Enter Valid Credential ", 401);
        }

        return systemConfigSyncService.retriveSystemConfigBasedOnAccess(id, serverName);

    }

    @GetMapping(value = "/reset", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String resetSyncTables(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password) {

        if (username == null || password == null) {
            throw new ImtechoUserException("Please add valid username,password in request Header", 401);
        } else {
            return systemConfigSyncService.clearAndResetSync();
        }

    }
}