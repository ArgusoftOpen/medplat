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
import com.argusoft.medplat.ncd.dao.MemberBreastDetailDao;
import com.argusoft.medplat.ncd.dao.MemberReferralDao;
import com.argusoft.medplat.ncd.dto.MemberBreastDto;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MemberBreastDetail;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;
import com.argusoft.medplat.ncd.service.NcdBreastService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class NcdBreastServiceImpl implements NcdBreastService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberBreastDetailDao breastDetailDao;

    @Override
    public void saveBreast(MemberBreastDto breastDto) {
        MemberEntity memberEntity = memberDao.retrieveById(breastDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        MemberBreastDetail memberBreastDetail = MemberDetailMapper.dtoToEntityForBreast(breastDto);
        memberBreastDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberBreastDetail.setFamilyId(familyEntity.getId());

        //Update status in master table also get id for disease table
        if(breastDto.getDoesSuffering()!=null && !breastDto.getDoesSuffering()){
            Integer masterId = ncdService.updateNcdMasterStatus(breastDto.getMemberId(), DiseaseCode.B,Status.NORMAL, breastDto.getHealthInfraId(),breastDto.getScreeningDate());
            //ncdService.updateNcdMasterSubStatus(breastDto.getMemberId(), DiseaseCode.B.toString() , null, true, breastDto.getScreeningDate());
            memberBreastDetail.setMasterId(masterId);
        }else {
//            if (breastDto.getDoneBy().equals(DoneBy.MO)) {
                Status status = breastDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : Status.UNDERTREATMENT;
                Integer masterId = ncdService.updateNcdMasterStatus(breastDto.getMemberId(), DiseaseCode.B, status, breastDto.getHealthInfraId(), breastDto.getScreeningDate());
                memberBreastDetail.setMasterId(masterId);
//            }
//
//            if (breastDto.getDoneBy().equals(DoneBy.CONSULTANT)) {
//                Status status = breastDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : Status.UNDERTREATEMENT;
//                Integer masterId = ncdService.updateNcdMasterStatus(breastDto.getMemberId(), DiseaseCode.B, status, breastDto.getHealthInfraId(), breastDto.getScreeningDate());
//                ncdService.updateNcdMasterSubStatus(breastDto.getMemberId(), DiseaseCode.B.toString(), SubStatus.REFERRED_BACK, true, breastDto.getScreeningDate());
//                memberBreastDetail.setMasterId(masterId);
            //}
        }

        if (breastDto.getTakeMedicine()) {
            memberBreastDetail.setTreatmentStatus("CPHC");
        } else {
            memberBreastDetail.setTreatmentStatus("OUTSIDE");
        }

        Integer breastId = breastDetailDao.create(memberBreastDetail);

        //add medicine details
        ncdService.createMedicineDetails(breastDto.getMedicineDetail(), breastDto.getMemberId(), breastDto.getScreeningDate(), breastId, MemberDiseaseDiagnosis.DiseaseCode.B,breastDto.getHealthInfraId());

        //create visit history
        String reading = "";
        reading = memberBreastDetail.getVisualUlceration()!=null ? "Ulcer, ":reading;
        reading = memberBreastDetail.getVisualLumpInBreast()!=null ? reading + "Lump in breast, " : reading;
        reading = memberBreastDetail.getVisualDischargeFromNipple()!=null? reading + "Nipple discharge, ":reading;
        reading = memberBreastDetail.getVisualSkinRetraction()!=null? reading + "Retraction of skin, ":reading;
        reading = memberBreastDetail.getVisualNippleRetractionDistortion()!=null? reading + "Retraction of nipple, ":reading;
        reading = memberBreastDetail.getVisualSkinDimplingRetraction()!=null? reading + "Skin dimpling, ":reading;
        ncdService.createVisitHistory(memberBreastDetail.getMasterId(),memberBreastDetail.getMemberId(),memberBreastDetail.getScreeningDate(),memberBreastDetail.getDoneBy(),breastId,memberBreastDetail.getStatus(),DiseaseCode.B,null,reading);

        updateMemberAdditionalInfoFromBreast(memberEntity, memberBreastDetail,
                breastDto.getStatus().equals(Status.CONFIRMED) || breastDto.getStatus().equals(Status.REFERRED));
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberBreastDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public Integer storeBreastForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberBreastDetail breastDetail = setBasicDataForBreastDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            breastDetail.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            breastDetail.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            breastDetail.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            breastDetail.setDoneBy(DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberBreastDetail(key, answer, breastDetail, keyAndAnswerMap));
        breastDetail.setSuspected(this.isSuspectedForBreastCancer(breastDetail));

        //get master id and add it in disease table
        Integer masterId = ncdService.createMasterRecord(breastDetail.getMemberId(),breastDetail.getLocationId(),DiseaseCode.B, null,breastDetail.getScreeningDate(), breastDetail.getSuspected());
        breastDetail.setMasterId(masterId);

        Integer breastId = breastDetailDao.create(breastDetail);

        //create visit history
        String reading = "";
        reading = breastDetail.getVisualUlceration()!=null ? "Ulcer, ":reading;
        reading = breastDetail.getVisualLumpInBreast()!=null ? reading + "Lump in breast, " : reading;
        reading = breastDetail.getVisualDischargeFromNipple()!=null? reading + "Nipple discharge, ":reading;
        reading = breastDetail.getVisualSkinRetraction()!=null? reading + "Retraction of skin, ":reading;
        reading = breastDetail.getVisualNippleRetractionDistortion()!=null? reading + "Retraction of nipple, ":reading;
        reading = breastDetail.getVisualSkinDimplingRetraction()!=null? reading + "Skin dimpling, ":reading;
        ncdService.createVisitHistory(breastDetail.getMasterId(),breastDetail.getMemberId(),breastDetail.getScreeningDate(),breastDetail.getDoneBy(),breastId,breastDetail.getDoesSuffering()!=null && breastDetail.getSuspected()?"SUSPECTED":"NORMAL",DiseaseCode.B,null, reading);

        updateMemberAdditionalInfoFromBreast(member, breastDetail, false);
        ncdService.updateFamilyAdditionalInfo(familyEntity, breastDetail.getScreeningDate());
        memberDao.update(member);
        familyDao.update(familyEntity);

        breastDetailDao.flush();
        return breastDetail.getId();
    }

    @Override
    public MemberBreastDetail retrieveBreastDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type) {
        return breastDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, type);
    }

    @Override
    public MemberBreastDetail retrieveLastRecordForBreastByMemberId(Integer memberId) {
        return breastDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    private MemberBreastDetail setBasicDataForBreastDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberBreastDetail breastDetail = new MemberBreastDetail();
        breastDetail.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        breastDetail.setFamilyId(familyId);
        breastDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        breastDetail.setLongitude(keyAndAnswerMap.get("-1"));
        breastDetail.setLatitude(keyAndAnswerMap.get("-2"));
        breastDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        breastDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        breastDetail.setDoneOn(new Date());
        return breastDetail;
    }

    private void setAnswersToMemberBreastDetail(String key, String answer, MemberBreastDetail breastDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "3":
                breastDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "4":
                breastDetail.setAnyBreastRelatedSymptoms(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "5":
                breastDetail.setLumpInBreast(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "6":
                breastDetail.setSizeChange(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "7":
                breastDetail.setNippleShapeAndPositionChange(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "8":
                breastDetail.setAnyRetractionOfNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "9":
                breastDetail.setDischargeFromNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                breastDetail.setRednessOfSkinOverNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "11":
                breastDetail.setErosionsOfNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "12":
                breastDetail.setRemarks(answer);
                break;
            case "50":
                breastDetail.setAgreedForSelfBreastExam(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "51":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setSwellingInArmpitFlag(answer);
                }
                break;
            case "52":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setDischargeFromNippleFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    breastDetail.setVisualDischargeFromNipple(answer);
                }
                break;
            case "53":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setVisualLumpInBreast(answer);
                }
                break;
            case "54":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setVisualNippleRetractionDistortion(answer);
                }
                break;
            case "55":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setVisualUlceration(answer);
                }
                break;
            case "56":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setVisualSkinDimplingRetraction(answer);
                }
                break;
            case "57":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setVisualRemarks(answer);
                }
                break;
            case "13":
                breastDetail.setRefferalDone(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "14":
                if (keyAndAnswerMap.get("13").equals("1")) {
                    breastDetail.setRefferalPlace(answer);
                }
                break;
            case "15":
                if (keyAndAnswerMap.get("13").equals("1")) {
                    breastDetail.setHealthInfraId(Integer.valueOf(answer));
                }
                break;
            case "101":
                breastDetail.setSwellingOrLump(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "102":
                breastDetail.setPuckeringOrDimpling(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "103":
                breastDetail.setConstantPainInBreast(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "104":
                breastDetail.setUlceration(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "115":
                breastDetail.setVisualSwellingInArmpit(answer);
                break;
            case "116":
                breastDetail.setVisualDischargeFromNipple(answer);
                break;
            case "110":
                breastDetail.setLumpInBreastFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "111":
                breastDetail.setNippleRetractionDistortionFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "112":
                breastDetail.setSkinDimplingRetractionFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }


    private void updateMemberAdditionalInfoFromBreast(MemberEntity member, MemberBreastDetail breastDetail, boolean isBreast) {
        Gson gson = new Gson();
        boolean isSuspected = isSuspectedForBreastCancer(breastDetail);
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (isSuspected) {
            memberAdditionalInfo.setSuspectedForBreastCancer(true);
        }
        if (isBreast) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.B.toString()));
        }
        memberAdditionalInfo.setLastServiceLongDate(breastDetail.getScreeningDate().getTime());

        memberAdditionalInfo.setBreastYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getBreastYear(), ImtechoUtil.getFinancialYearFromDate(breastDetail.getScreeningDate())));
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private boolean isSuspectedForBreastCancer(MemberBreastDetail breastDetail) {
        return Boolean.TRUE.equals(breastDetail.getLumpInBreast())
                || Boolean.TRUE.equals(breastDetail.getSizeChange())
                || Boolean.TRUE.equals(breastDetail.getNippleShapeAndPositionChange())
                || Boolean.TRUE.equals(breastDetail.getAnyRetractionOfNipple())
                || Boolean.TRUE.equals(breastDetail.getDischargeFromNipple())
                || Boolean.TRUE.equals(breastDetail.getRednessOfSkinOverNipple())
                || Boolean.TRUE.equals(breastDetail.getErosionsOfNipple())
                || Boolean.TRUE.equals(breastDetail.getDischargeFromNippleFlag())
                || (breastDetail.getSwellingInArmpitFlag() != null && !breastDetail.getSwellingInArmpitFlag().equals("NONE"))
                || breastDetail.getVisualLumpInBreast() != null
                || breastDetail.getVisualNippleRetractionDistortion() != null
                || breastDetail.getVisualUlceration() != null
                || breastDetail.getVisualSkinDimplingRetraction() != null;
    }

}
