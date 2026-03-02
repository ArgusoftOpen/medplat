/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDto;
import com.argusoft.medplat.ncddnhdd.dto.*;
import com.argusoft.medplat.ncddnhdd.model.*;
import com.argusoft.medplat.ncddnhdd.dto.MemberNcdDetailDto;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDnhddDto;
import com.argusoft.medplat.ncddnhdd.dto.MemberRegistrationDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author kunjan
 */
public interface NcdDnhddService {
    public List<MemberReferralDnhddDto> retrieveMembers(Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString, Boolean isSus);

    public MemberNcdDetailDto retrieveMemberDetail(Integer memberId);

    MemberHyperTensionDto retrieveLastRecordForHypertensionByMemberId(Integer memberId);

    MemberDiabetesDto retrieveLastRecordForDiabetesByMemberId(Integer memberId);

    MemberOralDetail retrieveLastRecordForOralByMemberId(Integer memberId);

    MemberBreastDetail retrieveLastRecordForBreastByMemberId(Integer memberId);

    MemberCervicalDetail retrieveLastRecordForCervicalByMemberId(Integer memberId);

    public Integer storeCbacAndNutritionForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    public Integer storeHypertensionDiabetesAndMentalHealthForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    public Integer storeCancerForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    public void saveHypertension(MemberHyperTensionDto hyperTensionDto);

    public void saveDiabetes(MemberDiabetesDto diabetesDto);

    public void saveCervical(MemberCervicalDto cervicalDto);

    public void saveOral(MemberOralDto oralDto);

    public void saveBreast(MemberBreastDto breastDto);

    public List<MedicineMaster> retrieveAllMedicines();

    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(Integer memberId, String diseaseCode);

    public void saveFollowUp(MemberDiseaseFollowupDto memberDiseaseFollowupDto);

    public List<MemberDetailDto> retrieveMembersForFollowup(Integer limit, Integer offset, String healthInfrastructureType, String[] status);

    public List<MemberReferralDto> retrieveReffForToday(Integer memberId);

    public List<MemberDiseaseFollowupDto> retrieveNextFollowUp(Integer memberId);

    MemberHypertensionDetail retrieveHypertensionDetailsByMemberAndDate(Integer memberId, Date screeningDate);

    MemberDiabetesDetail retrieveDiabetesDetailsByMemberAndDate(Integer memberId, Date screeningDate);

    MemberOralDetail retrieveOralDetailsByMemberAndDate(Integer memberId, Date screeningDate);

    MemberBreastDetail retrieveBreastDetailsByMemberAndDate(Integer memberId, Date screeningDate);

    MemberCervicalDetail retrieveCervicalDetailsByMemberAndDate(Integer memberId, Date screeningDate);

    Map<String, String> registerNewMember(MemberRegistrationDto ncdMember);

    MemberDiseasesDto retrieveFirstRecordForDiseaseByMemberId(Integer memberId,String diseaseCode);
}
