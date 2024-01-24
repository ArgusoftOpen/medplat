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
import com.argusoft.medplat.ncd.dao.*;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MemberDiabetesDto;
import com.argusoft.medplat.ncd.dto.NcdDiabetesDetailDataBean;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.NcdDiabetesService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class NcdDiabetesServiceImpl implements NcdDiabetesService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberDiabetesDetailDao diabetesDetailDao;
    @Autowired
    private MemberDiseaseMedicineDao memberDiseaseMedicineDao;
    @Autowired
    private MedicineMasterDao medicineMasterDao;
    @Autowired
    private MemberDiabetesConfirmationDetailDao diabetesConfirmationDetailDao;
    @Autowired
    private NcdMasterDao ncdMasterDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;

    @Override
    public MemberDiabetesDetail saveDiabetes(MemberDiabetesDto diabetesDto) {
        MemberEntity memberEntity = memberDao.retrieveById(diabetesDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        boolean currentlyUnderTreatment = false;
        MemberDiabetesDetail previousMemberDiabetesDetail = diabetesDetailDao.retrieveLastRecordByMemberId(diabetesDto.getMemberId());
        if ((previousMemberDiabetesDetail != null && previousMemberDiabetesDetail.getCurrentlyUnderTreatment() != null && previousMemberDiabetesDetail.getCurrentlyUnderTreatment()) || diabetesDto.getStatus().equals(Status.TREATMENT_STARTED)) {
            currentlyUnderTreatment = true;
        }
        if(previousMemberDiabetesDetail != null){
            diabetesDto.setEarlierDiabetesDiagnosis(previousMemberDiabetesDetail.isDoesSuffering()!=null?previousMemberDiabetesDetail.isDoesSuffering():previousMemberDiabetesDetail.getSuspected());
        }

        MemberDiabetesDetail memberDiabetesDetail = MemberDetailMapper.dtoToEntityForDiabetes(diabetesDto);
        memberDiabetesDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberDiabetesDetail.setFamilyId(familyEntity.getId());
        memberDiabetesDetail.setCurrentlyUnderTreatment(currentlyUnderTreatment);

        if (diabetesDto.getTakeMedicine()) {
            memberDiabetesDetail.setTreatmentStatus("CPHC");
        } else {
            memberDiabetesDetail.setTreatmentStatus("OUTSIDE");
        }

        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<diabetesDto.getMedicineDetail().size(); i++){
            GeneralDetailMedicineDto generalDetailMedicineDto = diabetesDto.getMedicineDetail().get(i);
            generalDetailMedicineDto.setIssuedDate(diabetesDto.getScreeningDate());
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

        createOrUpdateNcdMemberDetailsForWeb(memberDiabetesDetail,familyEntity,memberEntity, list);

        //Update status in master table also get id for disease table
        if(diabetesDto.getDoesSuffering()!=null && !diabetesDto.getDoesSuffering()){
            Integer masterId = ncdService.updateNcdMasterStatus(diabetesDto.getMemberId(), DiseaseCode.D,Status.NORMAL, diabetesDto.getHealthInfraId(),diabetesDto.getScreeningDate());
            //ncdService.updateNcdMasterSubStatus(diabetesDto.getMemberId(), DiseaseCode.D.toString() , null, true, diabetesDto.getScreeningDate());
            memberDiabetesDetail.setMasterId(masterId);
        }else {
            //if (diabetesDto.getDoneBy().equals(DoneBy.MO)) {
                Status status = diabetesDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : diabetesDto.getStatus().equals(Status.CONTROLLED) ? Status.UNDERTREATMENT_CONTROLLED : Status.UNDERTREATMENT_UNCONTROLLED;
                Integer masterId = ncdService.updateNcdMasterStatus(diabetesDto.getMemberId(), DiseaseCode.D, status, diabetesDto.getHealthInfraId(), diabetesDto.getScreeningDate());
                memberDiabetesDetail.setMasterId(masterId);
//            }
//            if (diabetesDto.getDoneBy().equals(DoneBy.CONSULTANT)) {
//                Status status = diabetesDto.getMedicineDetail().isEmpty() ? Status.CONFIRMED : diabetesDto.getStatus().equals(Status.CONTROLLED) ? Status.UNDERTREATEMENT_CONTROLLERD : Status.UNDERTREATEMENT_UNCONTROLLED;
//                Integer masterId = ncdService.updateNcdMasterStatus(diabetesDto.getMemberId(), DiseaseCode.D, status, diabetesDto.getHealthInfraId(), diabetesDto.getScreeningDate());
//                ncdService.updateNcdMasterSubStatus(diabetesDto.getMemberId(), DiseaseCode.D.toString(), SubStatus.REFERRED_BACK, true, diabetesDto.getScreeningDate());
//                memberDiabetesDetail.setMasterId(masterId);
            //}
        }

        Integer diabetesId = diabetesDetailDao.create(memberDiabetesDetail);


        //createMedicineDetailsForDiabetes(diabetesDto, memberReferralId, diabetesId);
        ncdService.createMedicineDetails(diabetesDto.getMedicineDetail(), diabetesDto.getMemberId(), diabetesDto.getScreeningDate(), diabetesId, MemberDiseaseDiagnosis.DiseaseCode.D,diabetesDto.getHealthInfraId());

        //create visit history
        ncdService.createVisitHistory(memberDiabetesDetail.getMasterId(),memberDiabetesDetail.getMemberId(),memberDiabetesDetail.getScreeningDate(),memberDiabetesDetail.getDoneBy(),diabetesId,memberDiabetesDetail.getStatus(),DiseaseCode.D,null, memberDiabetesDetail.getMeasurementType() + " : "+ memberDiabetesDetail.getBloodSugar().toString());

        updateMemberAdditionalInfoFromDiabetes(memberEntity, memberDiabetesDetail,
                diabetesDto.getStatus().equals(Status.UNCONTROLLED) || diabetesDto.getStatus().equals(Status.TREATMENT_STARTED) || diabetesDto.getStatus().equals(Status.REFERRED));
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberDiabetesDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
        diabetesDetailDao.updateDiabetesDetailsInNcdMemberDetail(memberEntity.getId());
        return  diabetesDetailDao.retrieveById(diabetesId);
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MemberDiabetesDetail memberDiabetesDetail, FamilyEntity familyEntity, MemberEntity memberEntity, List<JSONObject> medicineDtoList) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(memberEntity.getId());
        if(ncdMember == null){
            ncdMember = new NcdMemberEntity();
        }
        ncdMember.setMemberId(memberEntity.getId());
        if (familyEntity.getAreaId() != null) {
            ncdMember.setLocationId(familyEntity.getAreaId());
        } else {
            ncdMember.setLocationId(familyEntity.getLocationId());
        }
        ncdMember.setLastMoVisit(memberDiabetesDetail.getScreeningDate());
        ncdMember.setLastServiceDate(memberDiabetesDetail.getScreeningDate());
        ncdMember.setDiabetesStatus(memberDiabetesDetail.getStatus());
        ncdMember.setDiabetesTreatmentStatus(memberDiabetesDetail.getTreatmentStatus());
        ncdMember.setSufferingDiabetes(memberDiabetesDetail.getDoesSuffering());
        ncdMember.setMoConfirmedDiabetes(true);

        ncdMember.setMedicineDetails(medicineDtoList.toString());
//        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberEntity.getId(),DiseaseCode.D.toString());
//        if(ncdMaster!=null){
//            ncdMember.setDiabetesState(ncdMaster.getStatus());
//        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public Integer storeDiabetesForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberDiabetesDetail diabetesDetail = setBasicDataForDiabetes(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            diabetesDetail.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            diabetesDetail.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            diabetesDetail.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            diabetesDetail.setDoneBy(DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberDiabetesDetail(key, answer, diabetesDetail, keyAndAnswerMap));

        //get master id and add it in disease table
        Integer masterId = ncdService.createMasterRecord(diabetesDetail.getMemberId(),diabetesDetail.getLocationId(),DiseaseCode.D, diabetesDetail.getFlag(),diabetesDetail.getScreeningDate(), diabetesDetail.getSuspected());
        diabetesDetail.setMasterId(masterId);

        diabetesDetail.setVisitType(MobileConstantUtil.SCREENED);
        Integer diabetesId = diabetesDetailDao.create(diabetesDetail);

        //create visit history
        ncdService.createVisitHistory(diabetesDetail.getMasterId(),diabetesDetail.getMemberId(),diabetesDetail.getScreeningDate(),diabetesDetail.getDoneBy(),diabetesId,diabetesDetail.getSuspected()!=null && diabetesDetail.getSuspected()?"SUSPECTED":"NORMAL",DiseaseCode.D,diabetesDetail.getFlag(),diabetesDetail.getMeasurementType() + " : " + diabetesDetail.getBloodSugar().toString());

        updateMemberAdditionalInfoFromDiabetes(member, diabetesDetail, false);
        ncdService.updateFamilyAdditionalInfo(familyEntity, diabetesDetail.getScreeningDate());

        // Update data in ncd member entity
        createOrUpdateNcdMemberDetails(member, familyEntity, diabetesDetail, null);
        memberDao.update(member);
        familyDao.update(familyEntity);
        diabetesDetailDao.updateDiabetesDetailsInNcdMemberDetail(member.getId());
        diabetesDetailDao.flush();
        return diabetesDetail.getId();
    }

    @Override
    public Integer storeDiabetesConfirmationForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberDiabetesConfirmationDetail diabetesDetail = setBasicDataForDiabetesConfirmation(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            diabetesDetail.setDoneBy(MemberDiabetesConfirmationDetail.DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            diabetesDetail.setDoneBy(MemberDiabetesConfirmationDetail.DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            diabetesDetail.setDoneBy(MemberDiabetesConfirmationDetail.DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            diabetesDetail.setDoneBy(MemberDiabetesConfirmationDetail.DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToDiabetesConfirmationDetail(key, answer, diabetesDetail, keyAndAnswerMap));

        diabetesConfirmationDetailDao.create(diabetesDetail);
        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(diabetesDetail.getMemberId(),DiseaseCode.D.toString());
        Status masterStatus;
        if(ncdMaster.getStatus().equals(Status.CONFIRMATION_PENDING.toString())){
            masterStatus = Status.SUSPECTED;
            ncdService.updateNcdMasterStatus(diabetesDetail.getMemberId(),DiseaseCode.D,masterStatus,null,diabetesDetail.getScreeningDate());
        }

        updateMemberAdditionalInfoFromDiabetesConfirmation(member, diabetesDetail, false);

        // Update data in ncd member entity
        createOrUpdateNcdMemberDetails(member, familyEntity, null, diabetesDetail);
        memberDao.update(member);
        familyDao.update(familyEntity);
        diabetesDetailDao.updateDiabetesDetailsInNcdMemberDetail(member.getId());
        diabetesConfirmationDetailDao.flush();
        return diabetesDetail.getId();
    }

    @Override
    public MemberDiabetesDetail retrieveDiabetesDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type) {
        return diabetesDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, type);
    }

    @Override
    public MemberDiabetesDto retrieveLastRecordForDiabetesByMemberId(Integer memberId) {
        MemberDiabetesDetail memberDiabetesDetail = diabetesDetailDao.retrieveLastRecordByMemberId(memberId);
        if (memberDiabetesDetail != null) {
            MemberDiabetesDto memberDiabetesDto = MemberDetailMapper.entityToDtoForDiabetes(memberDiabetesDetail);
            List<MedicineMaster> medicines = new ArrayList<>();
            List<MemberDiseaseMedicine> memberDiseaseMedicines = memberDiseaseMedicineDao.retrieveMedicinesByReferenceId(memberDiabetesDetail.getId());
            for (MemberDiseaseMedicine memberDiseaseMedicine : memberDiseaseMedicines) {
                medicines.add(medicineMasterDao.retrieveById(memberDiseaseMedicine.getMedicineId()));
            }
            memberDiabetesDto.setMedicineMasters(medicines);
            return memberDiabetesDto;
        } else {
            return null;
        }
    }

    private void setAnswersToMemberDiabetesDetail(String key, String answer, MemberDiabetesDetail
            diabetesDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "8":
                diabetesDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "9":
                diabetesDetail.setEarlierDiabetesDiagnosis(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                if (keyAndAnswerMap.get("9").equals("1")) {
                    diabetesDetail.setCurrentlyUnderTreatment(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "11":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    diabetesDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "3003":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    diabetesDetail.setCurrentTreatmentPlaceOther(answer);
                }
                break;
            case "12":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    diabetesDetail.setContinueTreatmentFromCurrentPlace(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    if (answer.equalsIgnoreCase("1")) {
                        diabetesDetail.setTreatmentStatus("OUTSIDE");
                    } else {
                        diabetesDetail.setTreatmentStatus("CPHC");
                    }
                }
                break;
            case "13":
                diabetesDetail.setMeasurementType(answer);
                break;
            case "14":
                diabetesDetail.setBloodSugar(Integer.valueOf(answer.replace(".", "")));
                break;
            case "15":
                diabetesDetail.setSuspected(answer.equals("suspected"));
                break;
            case "16":
                diabetesDetail.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "22":
                diabetesDetail.setNote(answer);
                break;
            case "35":
                String[] ansSplit = answer.split("/");
                if (ansSplit.length == 3) {
                    try {
                        diabetesDetail.setHeight(Integer.valueOf(ansSplit[0]));
                        diabetesDetail.setWeight(Float.valueOf(ansSplit[1]));
                        diabetesDetail.setBmi(Float.valueOf(ansSplit[2]));
                    } catch (NumberFormatException e) {
                        diabetesDetail.setWeight(Float.valueOf(ansSplit[0]));
                        diabetesDetail.setHeight(Integer.valueOf(ansSplit[1].split("\\.")[0]));
                        diabetesDetail.setBmi(Float.valueOf(ansSplit[2]));
                    }
                }
                break;
            default:
        }
    }

    private MemberDiabetesDetail setBasicDataForDiabetes(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberDiabetesDetail diabetesDetail = new MemberDiabetesDetail();
        diabetesDetail.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        diabetesDetail.setFamilyId(familyId);
        diabetesDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        diabetesDetail.setLongitude(keyAndAnswerMap.get("-1"));
        diabetesDetail.setLatitude(keyAndAnswerMap.get("-2"));
        diabetesDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        diabetesDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        diabetesDetail.setDoneOn(new Date());
        return diabetesDetail;
    }

    private void updateMemberAdditionalInfoFromDiabetes(MemberEntity member, MemberDiabetesDetail diabetesDetail, boolean isDiabetes) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (diabetesDetail.getBloodSugar() != null) {
            memberAdditionalInfo.setBloodSugar(diabetesDetail.getBloodSugar());
        }
        if (Objects.nonNull(diabetesDetail.getSuspected()) && diabetesDetail.getSuspected()) {
            memberAdditionalInfo.setSuspectedForDiabetes(true);
        }
        if (Objects.nonNull(diabetesDetail.getDoesSuffering())) {
            memberAdditionalInfo.setSufferingDiabetes(diabetesDetail.getDoesSuffering());
//            memberAdditionalInfo.setMoConfirmedDiabetes(diabetesDetail.getDoesSuffering());
        }
        if (diabetesDetail.getHeight() != null) {
            memberAdditionalInfo.setHeight(diabetesDetail.getHeight());
        }
        if (diabetesDetail.getWeight() != null) {
            memberAdditionalInfo.setWeight(diabetesDetail.getWeight());
            member.setWeight(diabetesDetail.getWeight());
        }
        if (diabetesDetail.getBmi() != null) {
            memberAdditionalInfo.setBmi(diabetesDetail.getBmi());
        }
        if (isDiabetes) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.D.toString()));
        }
        memberAdditionalInfo.setLastServiceLongDate(diabetesDetail.getScreeningDate().getTime());

        memberAdditionalInfo.setDiabetesYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getDiabetesYear(), ImtechoUtil.getFinancialYearFromDate(diabetesDetail.getScreeningDate())));
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberDiabetesConfirmationDetail setBasicDataForDiabetesConfirmation(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberDiabetesConfirmationDetail diabetesDetail = new MemberDiabetesConfirmationDetail();
        diabetesDetail.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        diabetesDetail.setFamilyId(familyId);
        diabetesDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        diabetesDetail.setLongitude(keyAndAnswerMap.get("-1"));
        diabetesDetail.setLatitude(keyAndAnswerMap.get("-2"));
        diabetesDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        diabetesDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        diabetesDetail.setDoneOn(new Date());
        return diabetesDetail;
    }

    private void setAnswersToDiabetesConfirmationDetail(String key, String answer, MemberDiabetesConfirmationDetail
            diabetesDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "8":
                diabetesDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "14":
                diabetesDetail.setFastingBloodSugar(Integer.valueOf(answer.replace(".", "")));
                break;
            case "17":
                diabetesDetail.setPostPrandialBloodSugar(Integer.valueOf(answer.replace(".", "")));
                break;
            case "16":
                diabetesDetail.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "22":
                diabetesDetail.setNote(answer);
                break;
            default:
        }
    }

    private void updateMemberAdditionalInfoFromDiabetesConfirmation(MemberEntity member, MemberDiabetesConfirmationDetail diabetesDetail, boolean isDiabetes) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setConfirmedDiabetes(true);
        memberAdditionalInfo.setLastServiceLongDate(diabetesDetail.getScreeningDate().getTime());
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private void createOrUpdateNcdMemberDetails(MemberEntity member, FamilyEntity family, MemberDiabetesDetail diabetesDetail, MemberDiabetesConfirmationDetail diabetesConfirmationDetail) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(member.getId());
        if (ncdMember == null) {
            ncdMember = new NcdMemberEntity();
        }
        ncdMember.setMemberId(member.getId());
        if (family.getAreaId() != null) {
            ncdMember.setLocationId(family.getAreaId());
        } else {
            ncdMember.setLocationId(family.getLocationId());
        }
        if (diabetesConfirmationDetail != null) {
            ncdMember.setLastServiceDate(diabetesConfirmationDetail.getScreeningDate());
            ncdMember.setLastMobileVisit(diabetesConfirmationDetail.getScreeningDate());
            ncdMemberDao.createOrUpdate(ncdMember);
            return;
        }
        ncdMember.setLastServiceDate(diabetesDetail.getScreeningDate());
        ncdMember.setLastMobileVisit(diabetesDetail.getScreeningDate());

        if (diabetesDetail.getStatus() != null) {
            ncdMember.setDiabetesStatus(diabetesDetail.getStatus());
        } else if (diabetesDetail.getSuspected() != null) {
            ncdMember.setDiabetesStatus(diabetesDetail.getSuspected() ? "SUSPECTED" : "NORMAL");
        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public NcdDiabetesDetailDataBean getDiabetesDetailDataBean(MemberDiabetesDetail diabetesDetail, NcdMemberEntity ncdMember) {
        NcdDiabetesDetailDataBean diabetesDetailDataBean = new NcdDiabetesDetailDataBean();
        diabetesDetailDataBean.setDoneBy(String.valueOf(diabetesDetail.getDoneBy()));
        diabetesDetailDataBean.setScreeningDate(diabetesDetail.getScreeningDate().getTime());
        diabetesDetailDataBean.setStatus(ncdMember.getDiabetesStatus());
        if (diabetesDetail.getMeasurementType().equalsIgnoreCase("RBS")) {
            diabetesDetailDataBean.setBloodSugar(diabetesDetail.getBloodSugar());
        }
        return diabetesDetailDataBean;
    }
}
