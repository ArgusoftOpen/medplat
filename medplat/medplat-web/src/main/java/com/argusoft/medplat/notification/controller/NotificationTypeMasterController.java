/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.notification.dto.NotificationTypeMasterDto;
import com.argusoft.medplat.notification.service.NotificationMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * <p>
 * Define APIs for notification type.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/notificationtype")
@Tag(name = "Notification Type Master Controller", description = "")
public class NotificationTypeMasterController {

    @Autowired
    NotificationMasterService notificationMasterService;

    /**
     * Add/Update notification.
     * @param notificationMasterDto Notification details.
     */
    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody NotificationTypeMasterDto notificationMasterDto) {
        notificationMasterService.createOrUpdate(notificationMasterDto,false);
    }

    /**
     * Retrieves notification by id.
     * @param id Notification id.
     * @return Returns notification details by id.
     */
    @GetMapping(value = "/{id}")
    public NotificationTypeMasterDto retrieveById(@PathVariable() Integer id) {
        return notificationMasterService.retrieveById(id);
    }

    /**
     * Retrieves all notifications.
     * @param isActive Is notification active or not.
     * @return Returns list of notifications.
     */
    @GetMapping(value = "")
    public List<NotificationTypeMasterDto> retrieveAll(@RequestParam(name = "is_active", required = false) Boolean isActive) {
        return notificationMasterService.retrieveAll(isActive);
    }

    /**
     * Make notification active or inactive.
     * @param id Notification id.
     * @param isActive Is notification active or not.
     */
    @PutMapping(value = "/{id}")
    public void toggleActive(@PathVariable() Integer id, @RequestParam("is_active") Boolean isActive) {
        notificationMasterService.toggleActive(id, isActive);
    }
}