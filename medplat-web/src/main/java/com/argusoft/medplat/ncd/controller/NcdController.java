/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.*;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.NcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * <p>
 * Define APIs for ncd.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/ncd")
public class NcdController {

    @Autowired
    private NcdService ncdService;

    /**
     * Retrieves ncd member details by member id.
     * @param memberId Member id.
     * @return Returns ncd member details.
     */
    @GetMapping(value = "")
    public MemberMasterDto retrieveMemberDetailsById(@RequestParam(name = "memberId", required = false) Integer memberId) {
        return ncdService.retrieveMemberDetailMaster(memberId);
    }

    /**
     * Save ncd member details.
     * @param memberDetailDto Ncd member details.
     */
    @PostMapping(value = "/member")
    public void saveMemberDetails(@RequestBody MemberDetailDto memberDetailDto) {
        ncdService.saveMemberDetails(memberDetailDto);
    }

    /**
     * Retrieves all members of ncd.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @param healthInfrastructureType Type of health infrastructure.
     * @param searchString Search text.
     * @param searchBy Search by like member id, family id, Mobile number etc.
     * @return Returns list of members details by defined params.
     */
    @GetMapping(value = "/members")
    public List<MemberDetailDto> retrieveMembers(@RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset, @RequestParam(required = false) String healthInfrastructureType, @RequestParam(required = false) String searchString, @RequestParam(required = false) String searchBy) {
        return ncdService.retrieveMembers(limit, offset, healthInfrastructureType, searchBy, searchString);
    }

    @GetMapping(value = "/cfsmembers")
    public List<MemberDetailDto> retrieveMembersForCFS(@RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset, @RequestParam(required = false) String healthInfrastructureType, @RequestParam(required = false) String searchString, @RequestParam(required = false) String searchBy) {
        return ncdService.retrieveMembersForCFS(limit, offset, healthInfrastructureType, searchBy, searchString);
    }

    /**
     * Save complaint details.
     * @param complaintsDto Complaint details.
     */
    @PostMapping(value = "/complaint")
    public void saveComplaint(@RequestBody MemberComplaintsDto complaintsDto) {
        ncdService.saveComplaints(complaintsDto);
    }

    /**
     * Retrieves complaint by member id.
     * @param memberId Member id.
     * @return Returns list of complaint details by member id.
     */
    @GetMapping(value = "/complaints/{memberId}")
    public List<MemberComplaintsDto> retrieveComplaintsByMemberId(@PathVariable Integer memberId) {
        return ncdService.retrieveComplaintsByMemberId(memberId);
    }

    /**
     * Retrieves all medicine details.
     *
     * @return Returns list of medicine details.
     */
    @GetMapping(value = "/medicines")
    public List<MedicineMaster> retrieveAllMedicines() {
        return ncdService.retrieveAllMedicines();
    }

    /**
     * Save diagnosis details.
     * @param mdd Member diagnosis details.
     */
    @PostMapping(value = "/diagnosis")
    public void saveDiagnosis(@RequestBody MemberDiagnosisDto mdd) {
        ncdService.saveDiagnosis(mdd);
    }

    @GetMapping(value = "/treatmentHistory/{memberId}")
    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(@PathVariable Integer memberId, @RequestParam String diseaseCode) {
        return ncdService.retrieveTreatmentHistory(memberId, diseaseCode);
    }

    /**
     * Save referral details.
     * @param memberReferralDto Referral details of member.
     */
    @PostMapping(value = "/referral")
    public void saveReferral(@RequestBody MemberReferralDto memberReferralDto) {
        ncdService.saveReferral(memberReferralDto);
    }

    /**
     * Save followup details.
     * @param memberDiseaseFollowupDto Follow up details of member.
     */
    @PostMapping(value = "/followup")
    public void saveFollowup(@RequestBody MemberDiseaseFollowupDto memberDiseaseFollowupDto) {
        ncdService.saveFollowUp(memberDiseaseFollowupDto);
    }

    /**
     * Retrieves all follow up details of member.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @param healthInfrastructureType Type of health infrastructure.
     * @param status Define status like pending, completed.
     * @return Returns list of members based on define params.
     */
    @GetMapping(value = "/membersForFollowup")
    public List<MemberDetailDto> retrieveMembersForFollowup(@RequestParam Integer limit, @RequestParam Integer offset, @RequestParam(required = false) String healthInfrastructureType, @RequestParam(required = false) String[] status) {
        return ncdService.retrieveMembersForFollowup(limit, offset, healthInfrastructureType, status);
    }

    /**
     * Retrieves referral for today for particular member.
     * @param memberId Member id.
     * @return Returns list of member referral.
     */
    @GetMapping(value = "/reffForToday/{memberId}")
    public List<MemberReferralDto> retrieveReffForToday(@PathVariable Integer memberId) {
        return ncdService.retrieveReffForToday(memberId);
    }

    /**
     * Retrieves next follow up details for particular member.
     * @param memberId Member id.
     * @return Returns list of next follow up details.
     */
    @GetMapping(value = "/nextFollowup/{memberId}")
    public List<MemberDiseaseFollowupDto> retrieveNextFollowup(@PathVariable Integer memberId) {
        return ncdService.retrieveNextFollowUp(memberId);
    }

    @GetMapping(value = "/retrievePrescribedMedicineForUser")
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineForUser(@RequestParam Integer memberId){
        return  ncdService.retrievePrescribedMedicineForUser(memberId);
    }

    @GetMapping(value = "/retrievePrescribedMedicineHistoryForUser")
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineHistoryForUser(@RequestParam Integer memberId){
        return  ncdService.retrievePrescribedMedicineHistoryForUser(memberId);
    }

    @GetMapping(value = "/retrieveMembers")
    public List<MemberDetailDto> retrieveAllMembers(@RequestParam String type, @RequestParam(required = false) Integer limit,@RequestParam Integer offset){
        return ncdService.retrieveAllMembers(type,limit,offset);
    }

    @GetMapping(value = "/retreiveSearchedMembers")
    public List<MemberDetailDto> retreiveSearchedMembers(@RequestParam(required = false) Integer limit,@RequestParam Integer offset, @RequestParam String searchBy, @RequestParam String searchString, @RequestParam(required = false) Boolean flag, @RequestParam(required = false) Boolean review){
        return ncdService.retreiveSearchedMembers(limit,offset,searchBy,searchString,flag,review);
    }
}
