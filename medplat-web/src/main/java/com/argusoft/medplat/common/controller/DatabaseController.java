/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.controller;

import com.argusoft.medplat.common.service.DatabaseBackupService;
import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * <p>Defines rest endpoint to download database backup</p>
 * @author subhash
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private DatabaseBackupService databaseBackupService;

    @Autowired
    private ImtechoSecurityUser user;
    
    @Autowired SmsService smsService;

    /**
     * Returns latest database backup file
     * @param fileName Name of the backup file
     * @return A ResponseEntity consist of response related to backup file
     */
    @GetMapping(value = "download")
    public ResponseEntity<Resource> downloadDatabase(@RequestParam(name = "fileName", required = false) String fileName){
        if (SystemConstantUtil.ARGUS_ADMIN_ROLE.equals(user.getRoleCode())) {
            try {
                String msgBody = " Following UserId has requested to download the db  User name: " + user.getUserName() + " and  Mobile no : " + user.getMobileNumber();
                
                for (String mobileNo : SystemConstantUtil.SMS_TO_BE_SENT_ON_MOBILE_NO_FOR_DB_DOWNLOAD_REQUEST) {
                    smsService.sendSms(mobileNo, msgBody, Boolean.TRUE, "ALERT_SMS");
                }
                
                if (Arrays.asList(SystemConstantUtil.ALLOWED_USERS_TO_DOWNLOAD_DB).contains(user.getUserName())) {
                    Optional<File> lasterstDatabaseOptional = databaseBackupService.getLatestDatabaseBackupFile(fileName);
                    if (lasterstDatabaseOptional.isPresent()) {
                        File file = lasterstDatabaseOptional.get();
                        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                        HttpHeaders headers = new HttpHeaders();
                        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                        return ResponseEntity.ok()
                                .headers(headers)
                                .contentLength(file.length())
                                .contentType(MediaType.parseMediaType("application/octet-stream"))
                                .body(resource);
                    }
                }
                else{
                    return null;
                }
            } catch (Exception e) {
                throw new ImtechoSystemException("Problem while fectch file.", e);
            }
        } else {
            throw new ImtechoSystemException("Try to featch file by wrong by wrong user : " + user.getUserName(), 0);
        }
        throw new ImtechoSystemException("DB File not found", 0);
    }
    
}
