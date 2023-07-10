/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.controller;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.nutrition.dto.ChildCmtcNrcSdScoreDto;
import com.argusoft.medplat.nutrition.service.ChildCmtcNrcScreeningService;
import com.argusoft.medplat.rch.dto.ChildServiceMasterDto;
import com.argusoft.medplat.rch.service.ChildService;
import com.argusoft.medplat.rch.service.ImmunisationService;
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
 * Define APIs for child visit service.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/childservice")
public class ChildServiceController {

    @Autowired
    ChildService childService;

    @Autowired
    ImmunisationService immunisationService;

    @Autowired
    private ChildCmtcNrcScreeningService cmtcNrcScreeningService;

    @Autowired
    MobileUtilService mobileUtilService;

    @Autowired
    ImtechoSecurityUser user;

    /**
     * Add child visit service details.
     *
     * @param childServiceMasterDto Child visit service details.
     */
    @PostMapping(value = "")
    public void create(@RequestBody ChildServiceMasterDto childServiceMasterDto) {
        String checkSum = user.getUserName() + new Date().getTime();
        SyncStatus syncStatus = new SyncStatus();
        syncStatus.setDevice(MobileConstantUtil.WEB);
        syncStatus.setId(checkSum);
        syncStatus.setActionDate(new Date());
        syncStatus.setStatus(MobileConstantUtil.PROCESSING_VALUE);
        syncStatus.setRecordString("CS_WEB-" + new Gson().toJson(childServiceMasterDto));
        syncStatus.setUserId(user.getId());
        mobileUtilService.createSyncStatus(syncStatus);
        try {
            childService.create(childServiceMasterDto);
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
            throw new ImtechoSystemException(ex.getMessage(), ex);
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
     * Retrieves SD score for child.
     *
     * @param gender Gender of child.
     * @param height Height of child.
     * @param weight Weight of child.
     * @return Returns SD score based on gender, height, weight.
     */
    @GetMapping(value = "/getsdscore")
    public ChildCmtcNrcSdScoreDto retrieveSdScore(@RequestParam String gender, @RequestParam Integer height, @RequestParam Float weight) {
        return cmtcNrcScreeningService.getSdScore(gender, height, weight);
    }

    /**
     * Retrieves last child visit details.
     *
     * @param memberId Member id.
     * @return Returns last child visit details.
     */
    @GetMapping(value = "/getlastchildvisit")
    public ChildServiceMasterDto getLastChildVisit(@RequestParam Integer memberId) {
        return childService.getLastChildVisit(memberId);
    }

    /**
     * Validate vaccine.
     *
     * @param dob                Date of birth.
     * @param givenDate          Given date of vaccine.
     * @param currentVaccine     Current vaccine name.
     * @param givenImmunisations Given immunisations details.
     * @return Returns validation message like vaccine is valid or not.
     */
    @GetMapping(value = "/vaccinationvalidationforchild", produces = MediaType.APPLICATION_JSON)
    public String vaccinationValidationChild(@RequestParam Long dob, @RequestParam Long givenDate, @RequestParam String currentVaccine, @RequestParam String givenImmunisations) {
        return immunisationService.vaccinationValidationChild(new Date(dob), new Date(givenDate), currentVaccine, givenImmunisations);
    }

}
