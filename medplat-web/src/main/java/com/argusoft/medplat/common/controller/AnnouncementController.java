/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.controller;

import com.argusoft.medplat.common.dto.AnnouncementMasterDto;
import com.argusoft.medplat.common.service.AnnouncementService;
import com.argusoft.medplat.exception.ImtechoResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * <p>Defines rest end points for announcement</p>
 * @author smeet
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    /**
     * Create or update an announcement
     * @param announcementMasterDto An instance of AnnouncementMasterDto
     */
    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody AnnouncementMasterDto announcementMasterDto){
        announcementService.createOrUpdate(announcementMasterDto);
    }

    /**
     * Uploads given file to the server
     * @param file A multi part file to upload
     * @return Name of the file
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA)
    public String uploadMedia(@RequestParam("file") MultipartFile[] file) {
        return announcementService.uploadFile(file);
    }

    /**
     * Removes a file of given filename from server
     * @param file A filename to be remove
     */
    @DeleteMapping(value = "/remove")
    public @ResponseBody
    void removeMedia(@QueryParam("file") String file) {
        announcementService.removeFile(file);
    }

    /**
     * Returns an announcement of given id
     * @param id An Id of AnnouncementMasterDto
     * @return instance of AnnouncementMasterDto
     */
    @GetMapping(value = "/{id}")
    public AnnouncementMasterDto retrieveById(@PathVariable() Integer id) {
        return announcementService.retrieveById(id);
    }

    /**
     * Updates status of an announcement of given id
     * @param id An Id of AnnouncementMasterDto
     */
    @PutMapping(value = "")
    public void toggleStatusById(@RequestBody Integer id) {
        announcementService.toggleStatusById(id);
    }

    /**
     * Returns a list of announcement based on given criteria
     * @param limit Number of announcement to be return
     * @param offset Value for offset
     * @param orderBy Field name for order by
     * @param order Type of order i.e. asc, desc
     * @return A list of announcement
     */
    @GetMapping(value = "")
    public List<AnnouncementMasterDto> retrieveByCriteria(
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "orderBy", required = false) String orderBy,
            @RequestParam(name = "order", required = false) String order) {
        return announcementService.retrieveByCriteria(limit, offset, orderBy, order);
    }

    /**
     * Checks whether location to be added is already exist in current location or in its child locations or not
     * @param locationIds A list of location id
     * @param toBeAdded A location to add
     * @return ImtechoResponseEntity with location name string or an exception string if location is already exist
     */
    @GetMapping(value = "/validateaoi")
    public ImtechoResponseEntity validateAOI(
            @RequestParam(value = "locationIds", required = false) List<Integer> locationIds,
            @RequestParam(value = "toBeAdded", required = false) Integer toBeAdded) {
        return announcementService.validateAOI(locationIds, toBeAdded);
    }
}
