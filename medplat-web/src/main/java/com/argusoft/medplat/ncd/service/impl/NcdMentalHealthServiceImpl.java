package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dao.MemberMentalHealthDetailDao;
import com.argusoft.medplat.ncd.dao.MemberReferralDao;
import com.argusoft.medplat.ncd.dao.NcdMasterDao;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MemberMentalHealthDto;
import com.argusoft.medplat.ncd.dto.NcdMentalHealthDetailDataBean;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.NcdMentalHealthService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class NcdMentalHealthServiceImpl implements NcdMentalHealthService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberMentalHealthDetailDao mentalHealthDetailDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;
    @Autowired
    private NcdMasterDao ncdMasterDao;

    @Override
    public MemberMentalHealthDetails saveMentalHealth(MemberMentalHealthDto memberMentalHealthDto) {
        MemberEntity memberEntity = memberDao.retrieveById(memberMentalHealthDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        MemberMentalHealthDetails previousMemberMentalHealthDetails = mentalHealthDetailDao.retrieveLastRecordByMemberId(memberMentalHealthDto.getMemberId());
        if (previousMemberMentalHealthDetails != null) {
            memberMentalHealthDto.setSufferingEarlier(previousMemberMentalHealthDetails.getDoesSuffering()!=null?previousMemberMentalHealthDetails.getDoesSuffering():previousMemberMentalHealthDetails.getIsSuffering());
        }
        MemberMentalHealthDetails memberMentalHealthDetails = MemberDetailMapper.dtoToEntityForMentalHealth(memberMentalHealthDto);
        memberMentalHealthDetails.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberMentalHealthDetails.setFamilyId(familyEntity.getId());
        memberMentalHealthDetails.setHealthInfraId(memberMentalHealthDto.getHealthInfraId());

        if (memberMentalHealthDto.getTakeMedicine()) {
            memberMentalHealthDetails.setTreatmentStatus("CPHC");
        } else {
            memberMentalHealthDetails.setTreatmentStatus("OUTSIDE");
        }

        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<memberMentalHealthDto.getMedicineDetail().size(); i++){
            GeneralDetailMedicineDto generalDetailMedicineDto = memberMentalHealthDto.getMedicineDetail().get(i);
            generalDetailMedicineDto.setIssuedDate(memberMentalHealthDto.getScreeningDate());
            JSONObject medicineJson = new JSONObject();
            medicineJson.put("medicineId", generalDetailMedicineDto.getMedicineId());
            medicineJson.put("medicineName", generalDetailMedicineDto.getMedicineName());
            medicineJson.put("frequency", generalDetailMedicineDto.getFrequency());
            medicineJson.put("quantity", generalDetailMedicineDto.getQuantity());
            medicineJson.put("duration", generalDetailMedicineDto.getDuration());
            medicineJson.put("specialInstruction", generalDetailMedicineDto.getSpecialInstruction());
            medicineJson.put("expiryDate", generalDetailMedicineDto.getExpiryDate().getTime()); // Replace with your long value
            medicineJson.put("id", generalDetailMedicineDto.getId());
            medicineJson.put("issuedDate", generalDetailMedicineDto.getIssuedDate().getTime()); // Replace with your long value
            medicineJson.put("startDate", generalDetailMedicineDto.getStartDate().getTime()); // Replace with your long value
            medicineJson.put("isReturn", generalDetailMedicineDto.getReturn());
            String jsonString = medicineJson.toString();
            list.add(medicineJson);
        }
        createOrUpdateNcdMemberDetailsForWeb(memberMentalHealthDetails,list);

        //Update status in master table also get id for disease table
        if(memberMentalHealthDto.getDoesSuffering()!=null && !memberMentalHealthDto.getDoesSuffering()){
            Integer masterId = ncdService.updateNcdMasterStatus(memberMentalHealthDto.getMemberId(), DiseaseCode.MH,Status.NORMAL, memberMentalHealthDto.getHealthInfraId(),memberMentalHealthDto.getScreeningDate());
            //ncdService.updateNcdMasterSubStatus(memberMentalHealthDto.getMemberId(), DiseaseCode.MH.toString() , null, true, memberMentalHealthDto.getScreeningDate());
            memberMentalHealthDetails.setMasterId(masterId);
        }else {
            //if (memberMentalHealthDto.getDoneBy().equals(DoneBy.MO)) {
                Status status = memberMentalHealthDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : memberMentalHealthDto.getStatus().equals(Status.CONTROLLED) ? Status.UNDERTREATMENT_CONTROLLED : Status.UNDERTREATMENT_UNCONTROLLED;
                Integer masterId = ncdService.updateNcdMasterStatus(memberMentalHealthDto.getMemberId(), DiseaseCode.MH, status, memberMentalHealthDto.getHealthInfraId(), memberMentalHealthDto.getScreeningDate());
                memberMentalHealthDetails.setMasterId(masterId);
//            }
//            if (memberMentalHealthDto.getDoneBy().equals(DoneBy.CONSULTANT)) {
//                Status status = memberMentalHealthDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : memberMentalHealthDto.getStatus().equals(Status.CONTROLLED) ? Status.UNDERTREATEMENT_CONTROLLERD : Status.UNDERTREATEMENT_UNCONTROLLED;
//                Integer masterId = ncdService.updateNcdMasterStatus(memberMentalHealthDto.getMemberId(), DiseaseCode.MH, status, memberMentalHealthDto.getHealthInfraId(), memberMentalHealthDto.getScreeningDate());
//                ncdService.updateNcdMasterSubStatus(memberMentalHealthDto.getMemberId(), DiseaseCode.MH.toString(), SubStatus.REFERRED_BACK, true, memberMentalHealthDto.getScreeningDate());
//                memberMentalHealthDetails.setMasterId(masterId);
            //}
        }

        Integer mentalHealthId = mentalHealthDetailDao.create(memberMentalHealthDetails);

        //createMedicineDetailsForMentalHealth(memberMentalHealthDto, memberReferralId, mentalHealthId);
        ncdService.createMedicineDetails(memberMentalHealthDto.getMedicineDetail(), memberMentalHealthDto.getMemberId(),memberMentalHealthDto.getScreeningDate(), mentalHealthId, MemberDiseaseDiagnosis.DiseaseCode.MH,memberMentalHealthDto.getHealthInfraId());

        //create visit history
        String reading = "Talk : "+memberMentalHealthDetails.getTalk() + ", Daily work : "+memberMentalHealthDetails.getOwnDailyWork() + ", Social work : "+memberMentalHealthDetails.getSocialWork()+ ", Understanding : "+memberMentalHealthDetails.getUnderstanding();
        ncdService.createVisitHistory(memberMentalHealthDetails.getMasterId(),memberMentalHealthDetails.getMemberId(),memberMentalHealthDetails.getScreeningDate(),memberMentalHealthDetails.getDoneBy(),mentalHealthId,memberMentalHealthDetails.getStatus(),DiseaseCode.MH,null,reading);

        updateMemberAdditionalInfoFromMentalHealth(memberEntity, memberMentalHealthDetails,
                memberMentalHealthDto.getStatus().equals(Status.UNCONTROLLED) || memberMentalHealthDto.getStatus().equals(Status.TREATMENT_STARTED) || memberMentalHealthDto.getStatus().equals(Status.REFERRED));
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberMentalHealthDetails.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
        memberMentalHealthDetails = mentalHealthDetailDao.retrieveById(mentalHealthId);
        mentalHealthDetailDao.updateMentalHealthDetailsInNcdMemberDetail(memberEntity.getId());
        return memberMentalHealthDetails;
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MemberMentalHealthDetails memberMentalHealthDetails, List<JSONObject> medicineDtoList) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(memberMentalHealthDetails.getMemberId());
        if(ncdMember == null){
            ncdMember = new NcdMemberEntity();
        }
        ncdMember.setMemberId(memberMentalHealthDetails.getMemberId());
        ncdMember.setLocationId(memberMentalHealthDetails.getLocationId());
        ncdMember.setLastMoVisit(memberMentalHealthDetails.getScreeningDate());
        ncdMember.setLastServiceDate(memberMentalHealthDetails.getScreeningDate());
        ncdMember.setMentalHealthStatus(memberMentalHealthDetails.getStatus());
        ncdMember.setMentalHealthTreatmentStatus(memberMentalHealthDetails.getTreatmentStatus());
        ncdMember.setSufferingMentalHealth(memberMentalHealthDetails.getDoesSuffering());
        ncdMember.setMoConfirmedMentalHealth(true);

        ncdMember.setMedicineDetails(medicineDtoList.toString());
//        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberEntity.getId(),DiseaseCode.MH.toString());
//        if(ncdMaster!=null){
//            ncdMember.setMentalHealthState(ncdMaster.getStatus());
//        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public Integer storeMentalHealthForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberMentalHealthDetails mentalHealthDetails = setBasicDataForMentalHealthDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            mentalHealthDetails.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            mentalHealthDetails.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            mentalHealthDetails.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            mentalHealthDetails.setDoneBy(DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberMentalHealthDetail(key, answer, mentalHealthDetails, keyAndAnswerMap));

        //get master id and add it in disease table
        Integer masterId = ncdService.createMasterRecord(mentalHealthDetails.getMemberId(),mentalHealthDetails.getLocationId(),DiseaseCode.MH, mentalHealthDetails.getFlag(),mentalHealthDetails.getScreeningDate(), mentalHealthDetails.getIsSuffering());
        mentalHealthDetails.setMasterId(masterId);

        mentalHealthDetails.setVisitType(MobileConstantUtil.SCREENED);
        Integer mentalHealthId = mentalHealthDetailDao.create(mentalHealthDetails);

        //create visit history
        ncdService.createVisitHistory(mentalHealthDetails.getMasterId(),mentalHealthDetails.getMemberId(),mentalHealthDetails.getScreeningDate(),mentalHealthDetails.getDoneBy(),mentalHealthId,mentalHealthDetails.getIsSuffering()!=null && mentalHealthDetails.getIsSuffering()?"SUSPECTED":"NORMAL",DiseaseCode.MH,mentalHealthDetails.getFlag(),null);

        updateMemberAdditionalInfoFromMentalHealth(member, mentalHealthDetails,false);
        ncdService.updateFamilyAdditionalInfo(familyEntity, mentalHealthDetails.getScreeningDate());

        // Update data in ncd member entity
        createOrUpdateNcdMemberDetails(mentalHealthDetails);

        memberDao.update(member);
        familyDao.update(familyEntity);
        mentalHealthDetailDao.updateMentalHealthDetailsInNcdMemberDetail(member.getId());
        mentalHealthDetailDao.flush();
        return mentalHealthDetails.getId();
    }

    @Override
    public MemberMentalHealthDetails retrieveMentalHealthDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type) {
        return mentalHealthDetailDao.retrieveByMemberIdAndScreeningDate(memberId,date,type);
    }

    @Override
    public MemberMentalHealthDetails retrieveLastRecordForMentalHealthByMemberId(Integer memberId) {
        return mentalHealthDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    private void updateMemberAdditionalInfoFromMentalHealth(MemberEntity member, MemberMentalHealthDetails mentalHealthDetails,Boolean isMental) {
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (mentalHealthDetails.getTodayResult().equals(MemberMentalHealthDetails.Status.MILD) ||
                mentalHealthDetails.getTodayResult().equals(MemberMentalHealthDetails.Status.SEVERE) || mentalHealthDetails.getTodayResult().equals(MemberMentalHealthDetails.Status.CONFIRMED)) {
            memberAdditionalInfo.setSuspectedForMentalHealth(true);
        }
//        if (mentalHealthDetails.getSuffering()) {
//            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.MH.toString()));
//        }
//        if (mentalHealthDetails.getObservation() != null) {
//            memberAdditionalInfo.setMentalHealthObservation(mentalHealthDetails.getObservation());
//        }
        if (Objects.nonNull(mentalHealthDetails.getDoesSuffering())) {
            memberAdditionalInfo.setSufferingMentalHealth(mentalHealthDetails.getDoesSuffering());
        }
        memberAdditionalInfo.setLastServiceLongDate(mentalHealthDetails.getScreeningDate().getTime());

        memberAdditionalInfo.setMentalHealthYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getMentalHealthYear(), ImtechoUtil.getFinancialYearFromDate(mentalHealthDetails.getScreeningDate())));
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private MemberMentalHealthDetails setBasicDataForMentalHealthDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberMentalHealthDetails mentalHealthDetails = new MemberMentalHealthDetails();
        mentalHealthDetails.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        mentalHealthDetails.setFamilyId(familyId);
        mentalHealthDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        mentalHealthDetails.setLongitude(keyAndAnswerMap.get("-1"));
        mentalHealthDetails.setLatitude(keyAndAnswerMap.get("-2"));
        mentalHealthDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        mentalHealthDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        mentalHealthDetails.setDoneOn(new Date());
        return mentalHealthDetails;
    }

    private void setAnswersToMemberMentalHealthDetail(String key, String answer, MemberMentalHealthDetails
            mentalHealthDetails, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "8":
                mentalHealthDetails.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "9":
                mentalHealthDetails.setSufferingEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                if (keyAndAnswerMap.get("9").equals("1")) {
                    mentalHealthDetails.setDiagnosis(answer);
                }
                break;
            case "11":
                if (keyAndAnswerMap.get("9").equals("1")) {
                    mentalHealthDetails.setCurrentlyUnderTreatement(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "12":
                if (keyAndAnswerMap.get("11").equals("1")) {
                    mentalHealthDetails.setCurrentTreatmentPlace(answer);
                }
                break;
            case "3003":
                if (keyAndAnswerMap.get("11").equals("1")) {
                    mentalHealthDetails.setCurrentTreatmentPlaceOther(answer);
                }
                break;
            case "13":
                if (keyAndAnswerMap.get("11").equals("1")) {
                    mentalHealthDetails.setIsContinueTreatmentFromCurrentPlace(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    if (answer.equalsIgnoreCase("1")) {
                        mentalHealthDetails.setTreatmentStatus("OUTSIDE");
                    } else {
                        mentalHealthDetails.setTreatmentStatus("CPHC");
                    }
                }
                break;
            case "14":
                mentalHealthDetails.setObservation(answer);
                break;
            case "15":
                if (answer.equals("normal")) {
                    mentalHealthDetails.setTodayResult(MemberMentalHealthDetails.Status.NORMAL);
                } else if (answer.equals("mild")) {
                    mentalHealthDetails.setTodayResult(MemberMentalHealthDetails.Status.MILD);
                } else {
                    mentalHealthDetails.setTodayResult(MemberMentalHealthDetails.Status.SEVERE);
                }
                break;
            case "16":
                mentalHealthDetails.setIsSuffering(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "17":
                mentalHealthDetails.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "453":
                mentalHealthDetails.setOtherDiagnosis(answer);
                break;
            case "451":
                mentalHealthDetails.setOtherProblems(answer);
                break;
            case "22":
                mentalHealthDetails.setNote(answer);
                break;
            default:
        }
    }

    private void createOrUpdateNcdMemberDetails(MemberMentalHealthDetails mentalHealthDetails) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(mentalHealthDetails.getMemberId());
        if (ncdMember == null) {
            ncdMember = new NcdMemberEntity();
        }
        ncdMember.setMemberId(mentalHealthDetails.getMemberId());
        ncdMember.setLocationId(mentalHealthDetails.getLocationId());
        ncdMember.setLastServiceDate(mentalHealthDetails.getScreeningDate());
        ncdMember.setLastMobileVisit(mentalHealthDetails.getScreeningDate());

        if (mentalHealthDetails.getStatus() != null) {
            ncdMember.setMentalHealthStatus(mentalHealthDetails.getStatus());
        } else if (mentalHealthDetails.getTodayResult() != null) {
            ncdMember.setMentalHealthStatus(String.valueOf(mentalHealthDetails.getTodayResult()));
        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public NcdMentalHealthDetailDataBean getMentalHealthDetailDataBean(MemberMentalHealthDetails mentalHealthDetails, NcdMemberEntity ncdMember) {
        NcdMentalHealthDetailDataBean mentalHealthDetailDataBean = new NcdMentalHealthDetailDataBean();
        mentalHealthDetailDataBean.setDoneBy(String.valueOf(mentalHealthDetails.getDoneBy()));
        mentalHealthDetailDataBean.setScreeningDate(mentalHealthDetails.getScreeningDate().getTime());
        mentalHealthDetailDataBean.setStatus(ncdMember.getMentalHealthStatus());
        if (mentalHealthDetails.getTalk() != null) {
            mentalHealthDetailDataBean.setTalk(mentalHealthDetails.getTalk());
        }
        if (mentalHealthDetails.getSocialWork() != null) {
            mentalHealthDetailDataBean.setSocialWork(mentalHealthDetails.getSocialWork());
        }
        if (mentalHealthDetails.getUnderstanding() != null) {
            mentalHealthDetailDataBean.setUnderstanding(mentalHealthDetails.getUnderstanding());
        }
        if (mentalHealthDetails.getOwnDailyWork() != null) {
            mentalHealthDetailDataBean.setOwnDailyWork(mentalHealthDetails.getOwnDailyWork());
        }
        return mentalHealthDetailDataBean;
    }

}
