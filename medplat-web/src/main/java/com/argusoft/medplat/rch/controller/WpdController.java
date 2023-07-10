/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.controller;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.rch.dto.WpdMasterDto;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.rch.service.WpdService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Define APIs for wpd.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/managewpd")
public class WpdController {

    @Autowired
    WpdService wpdService;

    @Autowired
    MobileUtilService mobileUtilService;

    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    ImmunisationService immunisationService;

    @Autowired
    private ImtechoSecurityUser imtechoSecurityUser;

    @GetMapping(value = "/membersearch")
    public List<MemberDto> retrieveWpdMembers(@RequestParam(name = "id", required = false) Boolean byId, @RequestParam(name = "memberId", required = false) Boolean byMemberId, @RequestParam(name = "familyId", required = false) Boolean byFamilyId, @RequestParam(name = "mobileNumber", required = false) Boolean byMobileNumber, @RequestParam(name = "name", required = false) Boolean byName, @RequestParam(name = "lmp", required = false) Boolean byLmp, @RequestParam(name = "edd", required = false) Boolean byEdd, @RequestParam(name = "organizationUnit", required = false) Boolean byOrganizationUnit, @RequestParam(name = "abha number", required = false) Boolean byAbhaNumber, @RequestParam(name = "abha address", required = false) Boolean byAbhaAddress, @RequestParam(name = "locationId", required = false) Integer locationId, @RequestParam(name = "searchString", required = false) String searchString, @RequestParam(name = "byFamilyMobileNumber", required = false) Boolean byFamilyMobileNumber, @RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "offSet", required = false) Integer offSet) {
        return wpdService.retrieveWpdMembers(byId, byMemberId, byFamilyId, byMobileNumber, byName, byLmp, byEdd, byOrganizationUnit, byAbhaNumber, byAbhaAddress, locationId, searchString, byFamilyMobileNumber, limit, offSet);
    }

    /**
     * Add wpd form details.
     *
     * @param wpdMasterDto Wpd form details.
     * @return Returns list of member details.
     */
    @PostMapping(value = "")
    public List<MemberDto> create(@RequestBody WpdMasterDto wpdMasterDto) {
        List<MemberDto> generatedChildren;
        String checkSum = user.getUserName() + new Date().getTime();
        SyncStatus syncStatus = new SyncStatus();
        syncStatus.setDevice(MobileConstantUtil.WEB);
        syncStatus.setId(checkSum);
        syncStatus.setActionDate(new Date());
        syncStatus.setStatus(MobileConstantUtil.PROCESSING_VALUE);
        syncStatus.setRecordString("WPD_WEB-" + new Gson().toJson(wpdMasterDto));
        syncStatus.setUserId(user.getId());
        mobileUtilService.createSyncStatus(syncStatus);
        try {
            generatedChildren = wpdService.create(wpdMasterDto);
            syncStatus.setStatus(MobileConstantUtil.SUCCESS_VALUE);
            mobileUtilService.updateSyncStatus(syncStatus);
        } catch (ImtechoMobileException ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            syncStatus.setStatus(MobileConstantUtil.ERROR_VALUE);
            syncStatus.setException(writer.toString());
            syncStatus.setErrorMessage(ex.getMessage());
            mobileUtilService.updateSyncStatus(syncStatus);
            throw new ImtechoUserException(ex.getMessage(), ex);
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
        return generatedChildren;
    }

    /**
     * Retrieves pending discharge details.
     *
     * @return Returns list of pending discharge details.
     */
    @GetMapping(value = "/retrievependingdischargelist")
    public List<WpdMasterDto> retrievePendingDischargeList() {
        return wpdService.retrievePendingDischargeList(imtechoSecurityUser.getId());
    }

    /**
     * Save discharge details.
     *
     * @param wpdMasterDto Discharge details.
     */
    @PostMapping(value = "/savedischargedetails")
    public void saveDischargeDetails(@RequestBody WpdMasterDto wpdMasterDto) {
        wpdService.saveDischargeDetails(wpdMasterDto);
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
