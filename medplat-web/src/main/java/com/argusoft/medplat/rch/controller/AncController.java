package com.argusoft.medplat.rch.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.rch.dto.AncMasterDto;
import com.argusoft.medplat.rch.service.AncService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Define APIs for anc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/manageanc")
@Tag(name = "Anc Controller", description = "")
public class AncController {

    @Autowired
    private AncService ancService;

    @Autowired
    MobileUtilService mobileUtilService;

    @Autowired
    ImtechoSecurityUser user;

    @GetMapping(value = "/membersearch")
    public List<MemberDto> retrieveWpdMembers(@RequestParam(name = "id", required = false) Boolean byId, @RequestParam(name = "memberId", required = false) Boolean byMemberId, @RequestParam(name = "familyId", required = false) Boolean byFamilyId, @RequestParam(name = "mobileNumber", required = false) Boolean byMobileNumber, @RequestParam(name = "name", required = false) Boolean byName, @RequestParam(name = "lmp", required = false) Boolean byLmp, @RequestParam(name = "edd", required = false) Boolean byEdd, @RequestParam(name = "organizationUnit", required = false) Boolean byOrganizationUnit, @RequestParam(name = "abha number", required = false) Boolean byAbhaNumber, @RequestParam(name = "abha address", required = false) Boolean byAbhaAddress, @RequestParam(name = "locationId", required = false) Integer locationId, @RequestParam(name = "searchString", required = false) String searchString, @RequestParam(name = "byFamilyMobileNumber", required = false) Boolean byFamilyMobileNumber, @RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "offSet", required = false) Integer offSet) {
        return ancService.retrieveAncMembers(byId, byMemberId, byFamilyId, byMobileNumber, byName, byLmp, byEdd, byOrganizationUnit, byAbhaNumber, byAbhaAddress, locationId, searchString, byFamilyMobileNumber, limit, offSet);
    }

    /**
     * Add anc form details.
     *
     * @param ancMasterDto Anc form details.
     * @return Anc master id.
     */
    @PostMapping(value = "")
    public Integer create(@RequestBody AncMasterDto ancMasterDto) {
        Integer ancServiceId;
        String checkSum = user.getUserName() + new Date().getTime();
        SyncStatus syncStatus = new SyncStatus();
        syncStatus.setDevice(MobileConstantUtil.WEB);
        syncStatus.setId(checkSum);
        syncStatus.setActionDate(new Date());
        syncStatus.setStatus(MobileConstantUtil.PROCESSING_VALUE);
        syncStatus.setRecordString("ANC_WEB-" + new Gson().toJson(ancMasterDto));
        syncStatus.setUserId(user.getId());
        mobileUtilService.createSyncStatus(syncStatus);
        try {
            ancServiceId = ancService.create(ancMasterDto);
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
        return ancServiceId;
    }
}