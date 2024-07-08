/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.controller;

import com.argusoft.medplat.ncddnhdd.dto.*;
import com.argusoft.medplat.ncddnhdd.model.*;
import com.argusoft.medplat.ncddnhdd.dto.MemberNcdDetailDto;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDnhddDto;
import com.argusoft.medplat.ncddnhdd.dto.MemberRegistrationDto;
import com.argusoft.medplat.ncddnhdd.service.NcdDnhddService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author vaishali
 */
@Slf4j
@RestController
@RequestMapping("/api/ncd-dnhdd")
public class NcdDnhddController {

    @Autowired
    private NcdDnhddService ncdDnhddService;

    @GetMapping(value = "/members")
    public List<MemberReferralDnhddDto> retrieveMembers(@RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset, @RequestParam(required = false) String healthInfrastructureType, @RequestParam(required = false) String searchString, @RequestParam(required = false) String searchBy, @RequestParam(required = false) Boolean isSus) {
        return ncdDnhddService.retrieveMembers(limit, offset, healthInfrastructureType, searchBy, searchString, isSus);
    }

    @PostMapping(value = "")
    public Map<String, String> registerNewMember(@RequestBody MemberRegistrationDto ncdMember) {
        log.info(ncdMember.toString());
        return ncdDnhddService.registerNewMember(ncdMember);
    }

    @GetMapping(value = "")
    public MemberNcdDetailDto retrieveMemberDetailsById(@RequestParam(name = "memberId", required = false) Integer memberId) throws ParseException {
        return ncdDnhddService.retrieveMemberDetail(memberId);
    }

    @GetMapping(value = "/lastrecordforhypertension")
    public MemberHyperTensionDto retrieveLastRecordForHypertensionByMemberId(@RequestParam Integer memberId) {
        return ncdDnhddService.retrieveLastRecordForHypertensionByMemberId(memberId);
    }

    @GetMapping(value = "/lastrecordfordiabetes")
    public MemberDiabetesDto retrieveLastRecordForDiabetesByMemberId(@RequestParam Integer memberId) {
        return ncdDnhddService.retrieveLastRecordForDiabetesByMemberId(memberId);
    }

    @GetMapping(value = "/lastrecordfororal")
    public MemberOralDetail retrieveLastRecordForOralByMemberId(@RequestParam Integer memberId) {
        return ncdDnhddService.retrieveLastRecordForOralByMemberId(memberId);
    }

    @GetMapping(value = "/lastrecordforbreast")
    public MemberBreastDetail retrieveLastRecordForBreastByMemberId(@RequestParam Integer memberId) {
        return ncdDnhddService.retrieveLastRecordForBreastByMemberId(memberId);
    }

    @GetMapping(value = "/lastrecordforcervical")
    public MemberCervicalDetail retrieveLastRecordForCervicalByMemberId(@RequestParam Integer memberId) {
        return ncdDnhddService.retrieveLastRecordForCervicalByMemberId(memberId);
    }

    @GetMapping(value = "/firstrecordfordisease")
    public MemberDiseasesDto retrieveFirstRecordForDiseaseByMemberId(@RequestParam Integer memberId,@RequestParam(name = "diseaseCode", required = false) String diseaseCode) {
        return ncdDnhddService.retrieveFirstRecordForDiseaseByMemberId(memberId, diseaseCode);
    }

    @PostMapping(value = "/hypertension")
    public void saveHypertension(@RequestBody MemberHyperTensionDto hyperTensionDto) {
        ncdDnhddService.saveHypertension(hyperTensionDto);
    }

    @PostMapping(value = "/diabetes")
    public void saveDiabetes(@RequestBody MemberDiabetesDto diabetesDto) {
        ncdDnhddService.saveDiabetes(diabetesDto);
    }

    @PostMapping(value = "/cervical")
    public void saveCervical(@RequestBody MemberCervicalDto cervicalDto) {
        ncdDnhddService.saveCervical(cervicalDto);
    }

    @PostMapping(value = "/oral")
    public void saveOral(@RequestBody MemberOralDto oralDto) {
        ncdDnhddService.saveOral(oralDto);
    }

    @PostMapping(value = "/breast")
    public void saveBreast(@RequestBody MemberBreastDto breastDto) {
        ncdDnhddService.saveBreast(breastDto);
    }

    @GetMapping(value = "/medicines")
    public List<MedicineMaster> retrieveAllMedicines() {
        return ncdDnhddService.retrieveAllMedicines();
    }

    @GetMapping(value = "/treatmentHistory/{memberId}")
    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(@PathVariable Integer memberId, @RequestParam String diseaseCode) {
        return ncdDnhddService.retrieveTreatmentHistory(memberId, diseaseCode);
    }

    @PostMapping(value = "/followup")
    public void saveFollowup(@RequestBody MemberDiseaseFollowupDto memberDiseaseFollowupDto) {
        ncdDnhddService.saveFollowUp(memberDiseaseFollowupDto);
    }

    @GetMapping(value = "/membersForFollowup")
    public List<MemberDetailDto> retrieveMembersForFollowup(@RequestParam Integer limit, @RequestParam Integer offset, @RequestParam(required = false) String healthInfrastructureType, @RequestParam(required = false) String[] status) {
        return ncdDnhddService.retrieveMembersForFollowup(limit, offset, healthInfrastructureType, status);
    }

    @GetMapping(value = "/reffForToday/{memberId}")
    public List<MemberReferralDto> retrieveReffForToday(@PathVariable Integer memberId) {
        return ncdDnhddService.retrieveReffForToday(memberId);
    }

    @GetMapping(value = "/nextFollowup/{memberId}")
    public List<MemberDiseaseFollowupDto> retrieveNextFollowup(@PathVariable Integer memberId) {
        return ncdDnhddService.retrieveNextFollowUp(memberId);
    }

    @GetMapping(value = "/hypertensionbydate")
    public MemberHypertensionDetail retrieveHypertensionDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate) {
        return ncdDnhddService.retrieveHypertensionDetailsByMemberAndDate(memberId, new Date(screeningDate));
    }

    @GetMapping(value = "/diabetesbydate")
    public MemberDiabetesDetail retrieveDiabetesDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate) {
        return ncdDnhddService.retrieveDiabetesDetailsByMemberAndDate(memberId, new Date(screeningDate));
    }

    @GetMapping(value = "/oralbydate")
    public MemberOralDetail retrieveOralDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate) {
        return ncdDnhddService.retrieveOralDetailsByMemberAndDate(memberId, new Date(screeningDate));
    }

    @GetMapping(value = "/breastbydate")
    public MemberBreastDetail retrieveBreastDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate) {
        return ncdDnhddService.retrieveBreastDetailsByMemberAndDate(memberId, new Date(screeningDate));
    }

    @GetMapping(value = "/cervicalbydate")
    public MemberCervicalDetail retrieveCervicalDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate) {
        return ncdDnhddService.retrieveCervicalDetailsByMemberAndDate(memberId, new Date(screeningDate));
    }
}
