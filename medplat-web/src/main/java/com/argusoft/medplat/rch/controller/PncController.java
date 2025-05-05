/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.rch.dto.PncMasterDto;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.rch.service.PncService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Define APIs for pnc.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/managepnc")
@Tag(name = "Pnc Controller", description = "")
public class PncController {

    @Autowired
    PncService pncService;

    @Autowired
    ImmunisationService immunisationService;

    @Autowired
    MobileUtilService mobileUtilService;

    @Autowired
    ImtechoSecurityUser user;

    /**
     * Add pnc form details.
     *
     * @param pncMasterDto Pnc form details.
     */
    @PostMapping(value = "")
    public void create(@RequestBody PncMasterDto pncMasterDto) {
        String checkSum = user.getUserName() + new Date().getTime();
        SyncStatus syncStatus = new SyncStatus();
        syncStatus.setDevice(MobileConstantUtil.WEB);
        syncStatus.setId(checkSum);
        syncStatus.setActionDate(new Date());
        syncStatus.setStatus(MobileConstantUtil.PROCESSING_VALUE);
        syncStatus.setRecordString("PNC_WEB-" + new Gson().toJson(pncMasterDto));
        syncStatus.setUserId(user.getId());
        mobileUtilService.createSyncStatus(syncStatus);
        try {
            pncService.create(pncMasterDto);
            syncStatus.setStatus(MobileConstantUtil.SUCCESS_VALUE);
            mobileUtilService.updateSyncStatus(syncStatus);
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            syncStatus.setStatus(MobileConstantUtil.ERROR_VALUE);
            syncStatus.setException(writer.toString());
            syncStatus.setErrorMessage(ex.getMessage());
            mobileUtilService.updateSyncStatus(syncStatus);
            throw new ImtechoSystemException(ex.getMessage(), 1);
        }
    }

    /**
     * Retrieves due date of given immunization for child.
     *
     * @param dateOfBirth        Date of birth.
     * @param givenImmunisations Given immunization details.
     * @return Returns due date of given immunization details.
     */
    @GetMapping(value = "/getimmunisationsforchild")
    public Set<String> getDueImmunisationsForChild(@RequestParam Long dateOfBirth, @RequestParam(required = false) String givenImmunisations) {
        return immunisationService.getDueImmunisationsForChild(new Date(dateOfBirth), givenImmunisations);
    }

    /**
     * Validate vaccine.
     *
     * @param dob                Date of birth.
     * @param givenDate          Vaccine given date.
     * @param currentVaccine     Current vaccine name.
     * @param givenImmunisations Given immunisations.
     * @return Returns validation message like vaccine is valid or not.
     */
    @GetMapping(value = "/vaccinationvalidationforchild", produces = MediaType.APPLICATION_JSON)
    public String vaccinationValidationChild(@RequestParam Long dob, @RequestParam Long givenDate, @RequestParam String currentVaccine, @RequestParam String givenImmunisations) {
        return immunisationService.vaccinationValidationChild(new Date(dob), new Date(givenDate), currentVaccine, givenImmunisations);
    }

}