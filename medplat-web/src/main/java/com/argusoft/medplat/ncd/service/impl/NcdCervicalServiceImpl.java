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
import com.argusoft.medplat.ncd.dao.MemberCervicalDetailDao;
import com.argusoft.medplat.ncd.dao.MemberReferralDao;
import com.argusoft.medplat.ncd.dto.MemberCervicalDto;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MemberCervicalDetail;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;
import com.argusoft.medplat.ncd.service.NcdCervicalService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class NcdCervicalServiceImpl implements NcdCervicalService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberCervicalDetailDao cervicalDetailDao;

    @Override
    public void saveCervical(MemberCervicalDto cervicalDto) {
        MemberEntity memberEntity = memberDao.retrieveById(cervicalDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        MemberCervicalDetail memberCervicalDetail = MemberDetailMapper.dtoToEntityForCervical(cervicalDto);
        memberCervicalDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberCervicalDetail.setFamilyId(familyEntity.getId());

        //Update status in master table also get id for disease table
        if(cervicalDto.getDoesSuffering()!=null && !cervicalDto.getDoesSuffering()){
            Integer masterId = ncdService.updateNcdMasterStatus(cervicalDto.getMemberId(), DiseaseCode.C,Status.NORMAL, cervicalDto.getHealthInfraId(),cervicalDto.getScreeningDate());
            //ncdService.updateNcdMasterSubStatus(cervicalDto.getMemberId(), DiseaseCode.C.toString() , null, true, cervicalDto.getScreeningDate());
            memberCervicalDetail.setMasterId(masterId);
        }else {
           // if (cervicalDto.getDoneBy().equals(DoneBy.MO)) {
                Status status = cervicalDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : Status.UNDERTREATMENT;
                Integer masterId = ncdService.updateNcdMasterStatus(cervicalDto.getMemberId(), DiseaseCode.C, status, cervicalDto.getHealthInfraId(), cervicalDto.getScreeningDate());
                memberCervicalDetail.setMasterId(masterId);
//            }
//
//            if (cervicalDto.getDoneBy().equals(DoneBy.CONSULTANT)) {
//                Status status = cervicalDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : Status.UNDERTREATEMENT;
//                Integer masterId = ncdService.updateNcdMasterStatus(cervicalDto.getMemberId(), DiseaseCode.C, status, cervicalDto.getHealthInfraId(), cervicalDto.getScreeningDate());
//                ncdService.updateNcdMasterSubStatus(cervicalDto.getMemberId(), DiseaseCode.C.toString(), SubStatus.REFERRED_BACK, true, cervicalDto.getScreeningDate());
//                memberCervicalDetail.setMasterId(masterId);
            //}
        }

        if (cervicalDto.getTakeMedicine()) {
            memberCervicalDetail.setTreatmentStatus("CPHC");
        } else {
            memberCervicalDetail.setTreatmentStatus("OUTSIDE");
        }

        Integer cervicalId = cervicalDetailDao.create(memberCervicalDetail);

        //add medicine details
        ncdService.createMedicineDetails(cervicalDto.getMedicineDetail(), cervicalDto.getMemberId(), cervicalDto.getScreeningDate(), cervicalId, MemberDiseaseDiagnosis.DiseaseCode.C, cervicalDto.getHealthInfraId());

        //create visit history
        ncdService.createVisitHistory(memberCervicalDetail.getMasterId(),memberCervicalDetail.getMemberId(),memberCervicalDetail.getScreeningDate(),memberCervicalDetail.getDoneBy(),cervicalId,memberCervicalDetail.getStatus(),DiseaseCode.C,null,memberCervicalDetail.getBimanualExamination());

        updateMemberAdditionalInfoFromCervical(memberEntity, memberCervicalDetail,
                cervicalDto.getStatus().equals(Status.CONFIRMED) || cervicalDto.getStatus().equals(Status.REFERRED));
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberCervicalDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public Integer storeCervicalForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberCervicalDetail cervicalDetail = setBasicDataForCervicalDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            cervicalDetail.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            cervicalDetail.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            cervicalDetail.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            cervicalDetail.setDoneBy(DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberCervicalDetail(key, answer, cervicalDetail, keyAndAnswerMap));
        cervicalDetail.setSuspected(this.isSuspectedForCervicalCancer(cervicalDetail));

        //get master id and add it in disease table
        Integer masterId = ncdService.createMasterRecord(cervicalDetail.getMemberId(),cervicalDetail.getLocationId(),DiseaseCode.C, null,cervicalDetail.getScreeningDate(), cervicalDetail.getSuspected());
        cervicalDetail.setMasterId(masterId);

        Integer cervicalId = cervicalDetailDao.create(cervicalDetail);

        //create visit history
        ncdService.createVisitHistory(cervicalDetail.getMasterId(),cervicalDetail.getMemberId(),cervicalDetail.getScreeningDate(),cervicalDetail.getDoneBy(),cervicalId,cervicalDetail.getDoesSuffering()!=null && cervicalDetail.getSuspected()?"SUSPECTED":"NORMAL",DiseaseCode.C,null, null);

        updateMemberAdditionalInfoFromCervical(member, cervicalDetail, false);
        ncdService.updateFamilyAdditionalInfo(familyEntity, cervicalDetail.getScreeningDate());
        memberDao.update(member);
        familyDao.update(familyEntity);

        cervicalDetailDao.flush();
        return cervicalDetail.getId();
    }

    @Override
    public MemberCervicalDetail retrieveCervicalDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type) {
        return cervicalDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, type);
    }

    @Override
    public MemberCervicalDetail retrieveLastRecordForCervicalByMemberId(Integer memberId) {
        return cervicalDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    private MemberCervicalDetail setBasicDataForCervicalDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberCervicalDetail cervicalDetail = new MemberCervicalDetail();
        cervicalDetail.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        cervicalDetail.setFamilyId(familyId);
        cervicalDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        cervicalDetail.setLongitude(keyAndAnswerMap.get("-1"));
        cervicalDetail.setLatitude(keyAndAnswerMap.get("-2"));
        cervicalDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        cervicalDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        cervicalDetail.setDoneOn(new Date());
        return cervicalDetail;
    }

    private void setAnswersToMemberCervicalDetail(String key, String answer, MemberCervicalDetail
            cervicalDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "3":
                cervicalDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "4":
                cervicalDetail.setCervicalRelatedSymptoms(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "5":
                cervicalDetail.setExcessiveBleedingDuringPeriods(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "6":
                cervicalDetail.setBleedingBetweenPeriods(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "7":
                cervicalDetail.setBleedingAfterIntercourse(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "8":
                cervicalDetail.setExcessiveSmellingVaginalDischarge(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "9":
                cervicalDetail.setPostmenopausalBleeding(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                cervicalDetail.setRefferalDone(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "11":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    cervicalDetail.setRefferalPlace(answer);
                }
                break;
            case "13":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    cervicalDetail.setHealthInfraId(Integer.valueOf(answer));
                }
                break;
            case "12":
                cervicalDetail.setRemarks(answer);
                break;
            case "21":
                cervicalDetail.setTrainedViaExamination(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "22":
                cervicalDetail.setPolyp(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "23":
                cervicalDetail.setEctopy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "24":
                cervicalDetail.setHypertrophy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "25":
                cervicalDetail.setBleedsOnTouch(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "26":
                cervicalDetail.setUnhealthyCervix(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "27":
                cervicalDetail.setSuspiciousLookingCervix(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "28":
                cervicalDetail.setFrankMalignancy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "29":
                cervicalDetail.setProlapseUterus(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "30":
                cervicalDetail.setExcessiveDischarge(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "31":
                cervicalDetail.setVisualPolyp(answer);
                break;
            case "32":
                cervicalDetail.setVisualEctopy(answer);
                break;
            case "33":
                cervicalDetail.setVisualHypertrophy(answer);
                break;
            case "34":
                cervicalDetail.setVisualBleedsOnTouch(answer);
                break;
            case "35":
                cervicalDetail.setVisualUnhealthyCervix(answer);
                break;
            case "36":
                cervicalDetail.setVisualSuspiciousLooking(answer);
                break;
            case "37":
                cervicalDetail.setVisualFrankGrowth(answer);
                break;
            case "38":
                cervicalDetail.setVisualProlapseUterus(answer);
                break;
            default:
        }
    }

    private void updateMemberAdditionalInfoFromCervical(MemberEntity member, MemberCervicalDetail cervicalDetail, boolean isCervical) {
        Gson gson = new Gson();
        boolean isSuspected = isSuspectedForCervicalCancer(cervicalDetail);
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (isSuspected) {
            memberAdditionalInfo.setSuspectedForCervicalCancer(true);
        }
        if (isCervical) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.C.toString()));
        }
        memberAdditionalInfo.setLastServiceLongDate(cervicalDetail.getScreeningDate().getTime());

        memberAdditionalInfo.setCervicalYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getCervicalYear(), ImtechoUtil.getFinancialYearFromDate(cervicalDetail.getScreeningDate())));
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private boolean isSuspectedForCervicalCancer(MemberCervicalDetail cervicalDetail) {
        return Boolean.TRUE.equals(cervicalDetail.getExcessiveBleedingDuringPeriods())
                || Boolean.TRUE.equals(cervicalDetail.getBleedingBetweenPeriods())
                || Boolean.TRUE.equals(cervicalDetail.getBleedingAfterIntercourse())
                || Boolean.TRUE.equals(cervicalDetail.getExcessiveSmellingVaginalDischarge())
                || Boolean.TRUE.equals(cervicalDetail.getPostmenopausalBleeding());
    }
}
