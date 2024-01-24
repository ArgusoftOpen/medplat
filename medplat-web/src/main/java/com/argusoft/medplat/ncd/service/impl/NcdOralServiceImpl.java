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
import com.argusoft.medplat.ncd.dao.MemberOralDetailDao;
import com.argusoft.medplat.ncd.dao.MemberReferralDao;
import com.argusoft.medplat.ncd.dto.MemberOralDto;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;
import com.argusoft.medplat.ncd.model.MemberOralDetail;
import com.argusoft.medplat.ncd.service.NcdOralService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class NcdOralServiceImpl implements NcdOralService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberOralDetailDao oralDetailDao;

    @Override
    public void saveOral(MemberOralDto oralDto) {
        MemberEntity memberEntity = memberDao.retrieveById(oralDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        MemberOralDetail memberOralDetail = MemberDetailMapper.dtoToEntityForOral(oralDto);
        memberOralDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberOralDetail.setFamilyId(familyEntity.getId());

        //Update status in master table also get id for disease table
        if(oralDto.getDoesSuffering()!=null && !oralDto.getDoesSuffering()){
            Integer masterId = ncdService.updateNcdMasterStatus(oralDto.getMemberId(), DiseaseCode.O,Status.NORMAL, oralDto.getHealthInfraId(),oralDto.getScreeningDate());
            //ncdService.updateNcdMasterSubStatus(oralDto.getMemberId(), DiseaseCode.O.toString() , null, true, oralDto.getScreeningDate());
            memberOralDetail.setMasterId(masterId);
        }else {
//            if (oralDto.getDoneBy().equals(DoneBy.MO)) {
                Status status = oralDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : Status.UNDERTREATMENT;
                Integer masterId = ncdService.updateNcdMasterStatus(oralDto.getMemberId(), DiseaseCode.O, status, oralDto.getHealthInfraId(), oralDto.getScreeningDate());
                memberOralDetail.setMasterId(masterId);
//            }
//            if (oralDto.getDoneBy().equals(DoneBy.CONSULTANT)) {
//                Status status = oralDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : Status.UNDERTREATEMENT;
//                Integer masterId = ncdService.updateNcdMasterStatus(oralDto.getMemberId(), DiseaseCode.O, status, oralDto.getHealthInfraId(), oralDto.getScreeningDate());
//                ncdService.updateNcdMasterSubStatus(oralDto.getMemberId(), DiseaseCode.O.toString(), SubStatus.REFERRED_BACK, true, oralDto.getScreeningDate());
//                memberOralDetail.setMasterId(masterId);
            //}
        }

        if (oralDto.getTakeMedicine()) {
            memberOralDetail.setTreatmentStatus("CPHC");
        } else {
            memberOralDetail.setTreatmentStatus("OUTSIDE");
        }

        Integer oralId = oralDetailDao.create(memberOralDetail);

        //add medicine details
        ncdService.createMedicineDetails(oralDto.getMedicineDetail(), oralDto.getMemberId(), oralDto.getScreeningDate(), oralId, MemberDiseaseDiagnosis.DiseaseCode.O, oralDto.getHealthInfraId());

        //create visit history
        String reading = "";
        reading = memberOralDetail.getUlcer()!=null ? "Ulcer, ":reading;
        reading = memberOralDetail.getGrowthOfRecentOrigins()!=null ? reading+"Growth, " : reading;
        reading = memberOralDetail.getLichenPlanus()!=null?reading+"Lichen Planus, ":reading;
        reading = memberOralDetail.getSmokersPalate()!=null?reading+"Smokers Palate, ":reading;
        reading = memberOralDetail.getSubmucousFibrosis()!=null?reading+"Submucous Fibrosis, ":reading;
        reading = memberOralDetail.getRedPatches()!=null?reading+"Red Patches, ":reading;
        reading = memberOralDetail.getWhitePatches()!=null?reading+"White Patches, ":reading;
        reading = memberOralDetail.getRestrictedMouthOpening()!=null?reading+"Locked Jaw, ":reading;
        ncdService.createVisitHistory(memberOralDetail.getMasterId(),memberOralDetail.getMemberId(),memberOralDetail.getScreeningDate(),memberOralDetail.getDoneBy(),oralId,memberOralDetail.getStatus(),DiseaseCode.O,null,reading);

        updateMemberAdditionalInfoFromOral(memberEntity, memberOralDetail,
                oralDto.getStatus().equals(Status.CONFIRMED) || oralDto.getStatus().equals(Status.REFERRED));
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberOralDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public Integer storeOralForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberOralDetail oralDetail = setBasicDataForOral(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            oralDetail.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            oralDetail.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            oralDetail.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            oralDetail.setDoneBy(DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberOralDetail(key, answer, oralDetail, keyAndAnswerMap));

        oralDetail.setSuspected(this.isSuspectedForOralCancer(oralDetail));

        //get master id and add it in disease table
        Integer masterId = ncdService.createMasterRecord(oralDetail.getMemberId(),oralDetail.getLocationId(),DiseaseCode.O, oralDetail.getFlag(),oralDetail.getScreeningDate(), oralDetail.getSuspected());
        oralDetail.setMasterId(masterId);

        Integer oralId = oralDetailDao.create(oralDetail);

        //create visit history
        String reading = "";
        reading = oralDetail.getUlcer()!=null ? "Ulcer, ":reading;
        reading = oralDetail.getGrowthOfRecentOrigins()!=null ? reading+"Growth, " : reading;
        reading = oralDetail.getLichenPlanus()!=null?reading+"Lichen Planus, ":reading;
        reading = oralDetail.getSmokersPalate()!=null?reading+"Smokers Palate, ":reading;
        reading = oralDetail.getSubmucousFibrosis()!=null?reading+"Submucous Fibrosis, ":reading;
        reading = oralDetail.getRedPatches()!=null?reading+"Red Patches, ":reading;
        reading = oralDetail.getWhitePatches()!=null?reading+"White Patches, ":reading;
        reading = oralDetail.getRestrictedMouthOpening()!=null?reading+"Locked Jaw, ":reading;
        ncdService.createVisitHistory(oralDetail.getMasterId(),oralDetail.getMemberId(),oralDetail.getScreeningDate(),oralDetail.getDoneBy(),oralId,oralDetail.getDoesSuffering()!=null && oralDetail.getSuspected()?"SUSPECTED":"NORMAL",DiseaseCode.O,oralDetail.getFlag(), reading);

        updateMemberAdditionalInfoFromOral(member, oralDetail, false);
        ncdService.updateFamilyAdditionalInfo(familyEntity, oralDetail.getScreeningDate());
        memberDao.update(member);
        familyDao.update(familyEntity);

        oralDetailDao.flush();
        return oralDetail.getId();
    }

    @Override
    public MemberOralDetail retrieveOralDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type) {
        return oralDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, type);
    }

    @Override
    public MemberOralDetail retrieveLastRecordForOralByMemberId(Integer memberId) {
        return oralDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    private void setAnswersToMemberOralDetail(String key, String answer, MemberOralDetail oralDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "3":
                oralDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;

            case "4":
                oralDetail.setAnyIssuesInMouth(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "5":
                oralDetail.setWhiteRedPatchOralCavity(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "6":
                oralDetail.setDifficultyInSpicyFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "7":
                oralDetail.setVoiceChange(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "8":
                oralDetail.setDifficultyInOpeningMouth(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "9":
                oralDetail.setThreeWeeksMouthUlcer(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "10":
                oralDetail.setRemarks(answer);
                break;

            case "50":
                oralDetail.setGrowthOfRecentOrigins(answer);
                break;

            case "51":
                oralDetail.setNonHealingUlcers(answer);
                break;

            case "52":
                oralDetail.setRedPatches(answer);
                break;

            case "53":
                oralDetail.setWhitePatches(answer);
                break;

            case "54":
                oralDetail.setGrowthOfRecentOriginFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "11":
                oralDetail.setRefferalDone(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "12":
                if (keyAndAnswerMap.get("11").equals("1")) {
                    oralDetail.setRefferalPlace(answer);
                }
                break;

            case "13":
                if (keyAndAnswerMap.get("11").equals("1")) {
                    oralDetail.setHealthInfraId(Integer.valueOf(answer));
                }
                break;
            case "61":
                oralDetail.setRedPatch(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "105":
                oralDetail.setWhiteOrRedPatch(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "109":
                oralDetail.setUlcerationRoughenedAreas(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "110":
                oralDetail.setSymptomsRemarks(answer);
                break;
            default:
        }
    }

    private MemberOralDetail setBasicDataForOral(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberOralDetail oralDetail = new MemberOralDetail();
        oralDetail.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        oralDetail.setFamilyId(familyId);
        oralDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        oralDetail.setLongitude(keyAndAnswerMap.get("-1"));
        oralDetail.setLatitude(keyAndAnswerMap.get("-2"));
        oralDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        oralDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        oralDetail.setDoneOn(new Date());
        return oralDetail;
    }
    private void updateMemberAdditionalInfoFromOral(MemberEntity member, MemberOralDetail oralDetail, boolean isOral) {
        Gson gson = new Gson();
        boolean isSuspected = isSuspectedForOralCancer(oralDetail);

        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (Objects.nonNull(isSuspected) && isSuspected) {
            memberAdditionalInfo.setSuspectedForOralCancer(true);
        }
        if (Objects.nonNull(isOral)) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.O.toString()));
        }
        memberAdditionalInfo.setLastServiceLongDate(oralDetail.getScreeningDate().getTime());

        memberAdditionalInfo.setOralYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getOralYear(), ImtechoUtil.getFinancialYearFromDate(oralDetail.getScreeningDate())));
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private boolean isSuspectedForOralCancer(MemberOralDetail oralDetail) {
        return Boolean.TRUE.equals(oralDetail.getWhiteRedPatchOralCavity())
                || Boolean.TRUE.equals(oralDetail.getDifficultyInSpicyFood())
                || Boolean.TRUE.equals(oralDetail.getVoiceChange())
                || Boolean.TRUE.equals(oralDetail.getDifficultyInOpeningMouth())
                || Boolean.TRUE.equals(oralDetail.getThreeWeeksMouthUlcer())
                || oralDetail.getGrowthOfRecentOrigins() != null
                || oralDetail.getNonHealingUlcers() != null
                || oralDetail.getWhitePatches() != null
                || oralDetail.getRedPatches() != null;
    }


}
