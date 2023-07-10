package com.argusoft.medplat.fhs.controller;

import com.argusoft.medplat.fhs.dto.AnganwadiDto;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.dto.MemberInformationDto;
import com.argusoft.medplat.fhs.service.AnganwadiService;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.mobile.dto.FamilyDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Define APIs for fhs.
 * </p>
 *
 * @author shrey
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/fhs")
public class FhsController {

    @Autowired
    AnganwadiService anganwadiService;

    @Autowired
    FamilyHealthSurveyService familyHealthSurveyService;

    /**
     * Fetch anganwadi details based on location.
     *
     * @param limit      The number of data need to fetch.
     * @param offset     The number of data to skip before starting to fetch details.
     * @param locationId Specify location
     * @return Returns list of anganwadi details based on location.
     */
    @GetMapping(value = "/anganwadilist/{limit}/{offset}")
    public List<AnganwadiDto> getAnganwadilist(@PathVariable("limit") Integer limit, @PathVariable("offset") Integer offset,
                                               @RequestParam(value = "locationId", required = false) Integer locationId) {
        return anganwadiService.getAnganwadisByUserId(locationId, limit, offset);
    }

    /**
     * Active/Inactive particular anganwadi by id.
     *
     * @param id       Id of anganwadi.
     * @param isActive Active or not.
     */
    @GetMapping(value = "/anganwadi/toggleactive")
    public void toggleActive(@RequestParam("id") Integer id,
                             @RequestParam("isActive") Boolean isActive) {
        anganwadiService.toggleActive(id, isActive);
    }

    /**
     * Create anganwadi details.
     *
     * @param anganwadiDto Details of anganwadi.
     */
    @PostMapping(value = "/anganwadi")
    public void createAnganwadi(@RequestBody AnganwadiDto anganwadiDto) {
        anganwadiService.createAnganwadi(anganwadiDto);
    }

    /**
     * Get anganwadi details by id.
     *
     * @param id Id of anganwadi.
     * @return Returns anganwadi by id.
     */
    @GetMapping(value = "/anganwadi")
    public AnganwadiDto getAnganwadiById(@RequestParam("id") Integer id) {
        return anganwadiService.getAnganwadiById(id);
    }

    /**
     * Update anganwadi details.
     *
     * @param anganwadiDto Details of anganwadi.
     */
    @PutMapping(value = "/anganwadi")
    public void updateAnganwadi(@RequestBody AnganwadiDto anganwadiDto) {
        anganwadiService.updateAnganwadi(anganwadiDto);
    }

    /**
     * Search family based on id of user, search text, id of family, id of location etc.
     *
     * @param userId             Id of user.
     * @param searchString       Search text.
     * @param searchByFamilyId   Id of family.
     * @param searchByLocationId Id of location.
     * @param isArchivedFamily   Is family archived or not.
     * @param isVerifiedFamily   Is family verified or not.
     * @return Returns list of family details based on defined criteria.
     */
    @GetMapping(value = "/familysearch")
    public List<Map<String, List<FamilyDataBean>>> getFamiliesToBeAssignedBySearchString(
            @RequestParam(name = "userId") Integer userId,
            @RequestParam("searchString") List<String> searchString,
            @RequestParam("searchByFamilyId") Boolean searchByFamilyId,
            @RequestParam("searchByLocationId") Boolean searchByLocationId,
            @RequestParam("isArchivedFamily") Boolean isArchivedFamily,
            @RequestParam("isVerifiedFamily") Boolean isVerifiedFamily) {
        return familyHealthSurveyService.getFamiliesToBeAssignedBySearchString(userId, searchString, searchByFamilyId, searchByLocationId, isArchivedFamily, isVerifiedFamily);
    }

    /**
     * Retrieve Member Details by memberId
     *
     * @param memberId ID of imt_member
     * @return Returns Member Details
     */
    @GetMapping(value = "/membersearch")
    public MemberDto retrieveDetailsByMemberId(@RequestParam(name = "memberId", required = true) Integer memberId) {
        return familyHealthSurveyService.retrieveDetailsByMemberId(memberId);
    }

    /**
     * Search member by unique health id.
     *
     * @param byUniqueHealthId Unique health id.
     * @return Returns member details by unique health id.
     */
    @GetMapping(value = "/membersearchbyuniquehealthid")
    public MemberInformationDto getMembersByCriteria(@RequestParam(name = "uniqueHealthId") String byUniqueHealthId) {
        return familyHealthSurveyService.searchMembersByUniqueHealthId(byUniqueHealthId);
    }

    @GetMapping(value = "/getMemberDetailsByUniqueHealthId")
    public MemberDto getMemberDetailsByUniqueHealthId(@RequestParam(name = "uniqueHealthId") String byUniqueHealthId) {
        return familyHealthSurveyService.getMemberDetailsByUniqueHealthId(byUniqueHealthId);
    }

    /**
     * Update verified family location.
     *
     * @param selectedMoveAnganwadiAreaId Id of anganwadi area.
     * @param selectedMoveAshaAreaId      Id of asha area.
     * @param familyList                  Returns list of family.
     */
    @PutMapping(value = "/updateVerifiedFamilyLocation")
    public void updateVerifiedFamilyLocation(
            @RequestParam("selectedMoveAnganwadiAreaId") Integer selectedMoveAnganwadiAreaId,
            @RequestParam("selectedMoveAshaAreaId") Integer selectedMoveAshaAreaId,
            @RequestBody List<String> familyList) {
        familyHealthSurveyService.updateVerifiedFamilyLocation(familyList, selectedMoveAnganwadiAreaId, selectedMoveAshaAreaId);
    }
}
