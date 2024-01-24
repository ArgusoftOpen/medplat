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
import com.argusoft.medplat.ncd.dto.MemberHyperTensionDto;
import com.argusoft.medplat.ncd.dto.NcdHypertensionDetailDataBean;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.NcdHypertensionService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class NcdHypertensionServiceImpl implements NcdHypertensionService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberHypertensionDetailDao hypertensionDetailDao;
    @Autowired
    private MemberDiseaseMedicineDao memberDiseaseMedicineDao;
    @Autowired
    private MedicineMasterDao medicineMasterDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;
    @Autowired
    private NcdMasterDao ncdMasterDao;

    @Override
    public MemberHypertensionDetail saveHypertension(MemberHyperTensionDto hyperTensionDto) {
        MemberEntity memberEntity = memberDao.retrieveById(hyperTensionDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        boolean currentlyUnderTreatment = false;

        List<MemberHypertensionDetail> memberHypertensionDetails = hypertensionDetailDao.retrieveLastRecordByMemberId(hyperTensionDto.getMemberId());
        MemberHypertensionDetail previousMemberHypertensionDetail = null;
        if (memberHypertensionDetails.size() > 0) {
            previousMemberHypertensionDetail = memberHypertensionDetails.get(0);
        }
        if(previousMemberHypertensionDetail != null) {
            hyperTensionDto.setDiagnosedEarlier(previousMemberHypertensionDetail.getDoesSuffering()!=null?previousMemberHypertensionDetail.getDoesSuffering():previousMemberHypertensionDetail.getSuspected());
        }
        if ((previousMemberHypertensionDetail != null && previousMemberHypertensionDetail.getCurrentlyUnderTreatement() != null && previousMemberHypertensionDetail.getCurrentlyUnderTreatement()) || hyperTensionDto.getStatus().equals(Status.TREATMENT_STARTED)) {
            currentlyUnderTreatment = true;
        }
        MemberHypertensionDetail memberHypertensionDetail = MemberDetailMapper.dtoToEntityForHyperTension(hyperTensionDto);
        memberHypertensionDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberHypertensionDetail.setFamilyId(familyEntity.getId());
        memberHypertensionDetail.setCurrentlyUnderTreatement(currentlyUnderTreatment);

        if (hyperTensionDto.getTakeMedicine()) {
            memberHypertensionDetail.setTreatmentStatus("CPHC");
        } else {
            memberHypertensionDetail.setTreatmentStatus("OUTSIDE");
        }

        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<hyperTensionDto.getMedicineDetail().size(); i++){
            GeneralDetailMedicineDto generalDetailMedicineDto = hyperTensionDto.getMedicineDetail().get(i);
            generalDetailMedicineDto.setIssuedDate(hyperTensionDto.getScreeningDate());
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

        createOrUpdateNcdMemberDetailsForWeb(memberHypertensionDetail, list);

        //Update status in master table also get id for disease table
        if(hyperTensionDto.getDoesSuffering()!=null && !hyperTensionDto.getDoesSuffering()){
            Integer masterId = ncdService.updateNcdMasterStatus(hyperTensionDto.getMemberId(), DiseaseCode.HT,Status.NORMAL, hyperTensionDto.getHealthInfraId(),hyperTensionDto.getScreeningDate());
            //ncdService.updateNcdMasterSubStatus(hyperTensionDto.getMemberId(), DiseaseCode.HT.toString() , null, true, hyperTensionDto.getScreeningDate());
            memberHypertensionDetail.setMasterId(masterId);
        }else{
            //if(hyperTensionDto.getDoneBy().equals(DoneBy.MO)){
                Status status = hyperTensionDto.getMedicineDetail().isEmpty()? Status.CONFIRMED : hyperTensionDto.getStatus().equals(Status.CONTROLLED) ? Status.UNDERTREATMENT_CONTROLLED:Status.UNDERTREATMENT_UNCONTROLLED;
                Integer masterId = ncdService.updateNcdMasterStatus(hyperTensionDto.getMemberId(), DiseaseCode.HT,status, hyperTensionDto.getHealthInfraId(),hyperTensionDto.getScreeningDate());
                memberHypertensionDetail.setMasterId(masterId);
//            }
//            if(hyperTensionDto.getDoneBy().equals(DoneBy.CONSULTANT)){
//                Status status = hyperTensionDto.getMedicineDetail().isEmpty()? Status.CONFIRMED : hyperTensionDto.getStatus().equals(Status.CONTROLLED) ? Status.UNDERTREATEMENT_CONTROLLERD:Status.UNDERTREATEMENT_UNCONTROLLED;
//                Integer masterId = ncdService.updateNcdMasterStatus(hyperTensionDto.getMemberId(), DiseaseCode.HT,status, hyperTensionDto.getHealthInfraId(),hyperTensionDto.getScreeningDate());
//                ncdService.updateNcdMasterSubStatus(hyperTensionDto.getMemberId(), DiseaseCode.HT.toString() , SubStatus.REFERRED_BACK, true, hyperTensionDto.getScreeningDate());
//                memberHypertensionDetail.setMasterId(masterId);
            //}
        }

        Integer hypertensionId = hypertensionDetailDao.create(memberHypertensionDetail);

        //createMedicineDetailsForHypertension(hyperTensionDto, memberReferralId, hypertensionId);
        ncdService.createMedicineDetails(hyperTensionDto.getMedicineDetail(), hyperTensionDto.getMemberId(), hyperTensionDto.getScreeningDate(),  hypertensionId, MemberDiseaseDiagnosis.DiseaseCode.HT, hyperTensionDto.getHealthInfraId());

        //create visit history
        String reading = memberHypertensionDetail.getSystolicBp() + "/" + memberHypertensionDetail.getDiastolicBp();
        ncdService.createVisitHistory(memberHypertensionDetail.getMasterId(),memberHypertensionDetail.getMemberId(),memberHypertensionDetail.getScreeningDate(),memberHypertensionDetail.getDoneBy(),hypertensionId,memberHypertensionDetail.getStatus(),DiseaseCode.HT,null,reading);

        updateMemberAdditionalInfoFromHypertension(memberEntity, memberHypertensionDetail,
                hyperTensionDto.getStatus().equals(Status.UNCONTROLLED) || hyperTensionDto.getStatus().equals(Status.TREATMENT_STARTED) || hyperTensionDto.getStatus().equals(Status.REFERRED));
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberHypertensionDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
        hypertensionDetailDao.updateHypertensionDetailsInNcdMemberDetail(memberEntity.getId());
        return  hypertensionDetailDao.retrieveById(hypertensionId);
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MemberHypertensionDetail memberHypertensionDetail, List<JSONObject> medicineDtoList) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(memberHypertensionDetail.getMemberId());
        if (ncdMember == null) {
            ncdMember = new NcdMemberEntity();
        }
        ncdMember.setMemberId(memberHypertensionDetail.getMemberId());
        ncdMember.setLocationId(memberHypertensionDetail.getLocationId());
        ncdMember.setLastMoVisit(memberHypertensionDetail.getScreeningDate());
        ncdMember.setLastServiceDate(memberHypertensionDetail.getScreeningDate());
        ncdMember.setHypertensionStatus(memberHypertensionDetail.getStatus());
        ncdMember.setHypertensionTreatmentStatus(memberHypertensionDetail.getTreatmentStatus());
        ncdMember.setSufferingHypertension(memberHypertensionDetail.getDoesSuffering());
        ncdMember.setMoConfirmedHypertension(true);

        ncdMember.setMedicineDetails(medicineDtoList.toString());
//        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberEntity.getId(),DiseaseCode.HT.toString());
//        if(ncdMaster!=null){
//            ncdMember.setHypertensionState(ncdMaster.getStatus());
//        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public Integer storeHypertensionForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberHypertensionDetail hypertensionDetail = setBasicDataForHypertension(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            hypertensionDetail.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            hypertensionDetail.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            hypertensionDetail.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            hypertensionDetail.setDoneBy(DoneBy.CC);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberHypertensionDetail(key, answer, hypertensionDetail, keyAndAnswerMap));

        //get master id and add it in disease table
        Integer masterId = ncdService.createMasterRecord(hypertensionDetail.getMemberId(),hypertensionDetail.getLocationId(),DiseaseCode.HT, hypertensionDetail.getFlag(), hypertensionDetail.getScreeningDate(), hypertensionDetail.getSuspected());
        hypertensionDetail.setMasterId(masterId);

        hypertensionDetail.setVisitType(MobileConstantUtil.SCREENED);
        Integer hypertensionId = hypertensionDetailDao.create(hypertensionDetail);

        if (hypertensionDetail.getBpMachineAvailable() != null && hypertensionDetail.getBpMachineAvailable()) {
            updateMemberAdditionalInfoFromHypertension(member, hypertensionDetail, false);
            ncdService.updateFamilyAdditionalInfo(familyEntity, hypertensionDetail.getScreeningDate());
        }

        // Update data in ncd member entity
        createOrUpdateNcdMemberDetails(hypertensionDetail);

        //create visit history
        String reading = hypertensionDetail.getSystolicBp() + "/" + hypertensionDetail.getDiastolicBp();
        ncdService.createVisitHistory(hypertensionDetail.getMasterId(),hypertensionDetail.getMemberId(),hypertensionDetail.getScreeningDate(),hypertensionDetail.getDoneBy(),hypertensionId,hypertensionDetail.getSuspected()!=null && hypertensionDetail.getSuspected()?"SUSPECTED":"NORMAL",DiseaseCode.HT,hypertensionDetail.getFlag(),reading);

        memberDao.update(member);
        familyDao.update(familyEntity);
        hypertensionDetailDao.updateHypertensionDetailsInNcdMemberDetail(member.getId());
        hypertensionDetailDao.flush();
        return hypertensionDetail.getId();
    }

    @Override
    public MemberHypertensionDetail retrieveHypertensionDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type) {
        return hypertensionDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate,type);
    }

    public List<MemberHyperTensionDto> retrieveLastRecordForHypertensionByMemberId(Integer memberId) {
        List<MemberHypertensionDetail> memberHypertensionDetails = hypertensionDetailDao.retrieveLastRecordByMemberId(memberId);
        if (!memberHypertensionDetails.isEmpty() && memberHypertensionDetails.size()>0) {
            List<MemberHyperTensionDto> memberHyperTensionDtos = new ArrayList<MemberHyperTensionDto>();
            for (MemberHypertensionDetail memberHypertensionDetail :memberHypertensionDetails
            ) {
                MemberHyperTensionDto memberHyperTensionDto = MemberDetailMapper.entityToDtoForHyperTension(memberHypertensionDetail);
//                List<GeneralDetailMedicineDto> medicines = new ArrayList<>();
//                List<MemberDiseaseMedicine> memberDiseaseMedicines = memberDiseaseMedicineDao.retrieveMedicinesByReferenceId(memberHypertensionDetail.getId());
//                for (MemberDiseaseMedicine memberDiseaseMedicine : memberDiseaseMedicines) {
//                    medicines.add(medicineMasterDao.retrieveById(memberDiseaseMedicine.getMedicineId()));
//                }
//                memberHyperTensionDto.setMedicineMasters(medicines);
//                for(MemberDiseaseMedicine memberDiseaseMedicine : memberDiseaseMedicines) {
//                    medicines.add()
//                }
//                memberHyperTensionDto.setMedicineDetail(medicines);
                memberHyperTensionDtos.add(memberHyperTensionDto);
            }

            return memberHyperTensionDtos;
        } else {
            return null;
        }
    }

    private MemberHypertensionDetail setBasicDataForHypertension(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberHypertensionDetail hypertensionDetail = new MemberHypertensionDetail();
        hypertensionDetail.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        hypertensionDetail.setFamilyId(familyId);
        hypertensionDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        hypertensionDetail.setLongitude(keyAndAnswerMap.get("-1"));
        hypertensionDetail.setLatitude(keyAndAnswerMap.get("-2"));
        hypertensionDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        hypertensionDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        hypertensionDetail.setDoneOn(new Date());
        return hypertensionDetail;
    }

    private void setAnswersToMemberHypertensionDetail(String key, String answer, MemberHypertensionDetail hypertensionDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "8":
                hypertensionDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "9":
                hypertensionDetail.setDiagnosedEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                if (keyAndAnswerMap.get("9").equals("1")) {
                    hypertensionDetail.setCurrentlyUnderTreatement(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "11":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    hypertensionDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "3003":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    hypertensionDetail.setCurrentTreatmentPlaceOther(answer);
                }
                break;
            case "12":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    hypertensionDetail.setContinueTreatmentFromCurrentPlace(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    if (answer.equalsIgnoreCase("1")) {
                        hypertensionDetail.setTreatmentStatus("OUTSIDE");
                    } else {
                        hypertensionDetail.setTreatmentStatus("CPHC");
                    }
                }
                break;
            case "13":
                String[] arr = answer.split("-");
                if (arr.length > 1) {
                    hypertensionDetail.setBpMachineAvailable(Boolean.TRUE);
                    hypertensionDetail.setSystolicBp(Integer.valueOf(arr[1].split("\\.")[0]));
                    hypertensionDetail.setDiastolicBp(Integer.valueOf(arr[2].split("\\.")[0]));
                } else {
                    hypertensionDetail.setBpMachineAvailable(Boolean.FALSE);
                }
                break;
            case "14":
                answer = answer.replace(".", "");
                if (!answer.trim().isEmpty()) {
                    hypertensionDetail.setPulseRate(Integer.valueOf(answer));
                }
                break;
            case "15":
                String[] arr1 = answer.split("-");
                if (arr1.length > 1) {
                    hypertensionDetail.setBpMachineAvailable(Boolean.TRUE);
                    hypertensionDetail.setSystolicBp2(Integer.valueOf(arr1[1].split("\\.")[0]));
                    hypertensionDetail.setDiastolicBp2(Integer.valueOf(arr1[2].split("\\.")[0]));
                } else {
                    hypertensionDetail.setBpMachineAvailable(Boolean.FALSE);
                }
                break;
            case "16":
                answer = answer.replace(".", "");
                if (!answer.trim().isEmpty()) {
                    hypertensionDetail.setPulseRate2(Integer.valueOf(answer));
                }
                break;
            case "19":
                hypertensionDetail.setSuspected(answer.equals("suspected"));
                break;
            case "20":
                hypertensionDetail.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "35":
                String[] ansSplit = answer.split("/");
                if (ansSplit.length == 3) {
                    try {
                        hypertensionDetail.setHeight(Integer.valueOf(ansSplit[0]));
                        hypertensionDetail.setWeight(Float.valueOf(ansSplit[1]));
                        hypertensionDetail.setBmi(Float.valueOf(ansSplit[2]));
                    } catch (NumberFormatException e) {
                        hypertensionDetail.setWeight(Float.valueOf(ansSplit[0]));
                        hypertensionDetail.setHeight(Integer.valueOf(ansSplit[1].split("\\.")[0]));
                        hypertensionDetail.setBmi(Float.valueOf(ansSplit[2]));
                    }
                }
                break;
            case "37":
                hypertensionDetail.setWaist(Integer.valueOf(answer));
                break;
            case "38":
                hypertensionDetail.setDiseaseHistory(answer);
                break;
            case "40":
                hypertensionDetail.setOtherDisease(answer);
                break;
            case "41":
                hypertensionDetail.setRiskFactor(answer);
                break;
            case "42":
                hypertensionDetail.setUndertakePhysicalActivity(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "43":
                hypertensionDetail.setHaveFamilyHistory(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "22":
                hypertensionDetail.setNote(answer);
                break;
            default:
        }
    }

    private void updateMemberAdditionalInfoFromHypertension(MemberEntity member, MemberHypertensionDetail hypertensionDetail, boolean isHypertension) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (hypertensionDetail.getSystolicBp() != null) {
            memberAdditionalInfo.setSystolicBp(hypertensionDetail.getSystolicBp());
        }
        if (hypertensionDetail.getDiastolicBp() != null) {
            memberAdditionalInfo.setDiastolicBp(hypertensionDetail.getDiastolicBp());
        }
        if (hypertensionDetail.getSystolicBp2() != null) {
            memberAdditionalInfo.setSystolicBp(hypertensionDetail.getSystolicBp2());
        }
        if (hypertensionDetail.getDiastolicBp2() != null) {
            memberAdditionalInfo.setDiastolicBp(hypertensionDetail.getDiastolicBp2());
        }
        if (Objects.nonNull(hypertensionDetail.getSuspected()) && hypertensionDetail.getSuspected()) {
            memberAdditionalInfo.setSuspectedForHypertension(true);
        }
        if (Objects.nonNull(hypertensionDetail.getDoesSuffering())) {
            memberAdditionalInfo.setSufferingHypertension(hypertensionDetail.getDoesSuffering());
//            memberAdditionalInfo.setMoConfirmedHypertension(hypertensionDetail.getDoesSuffering());
        }
        if (hypertensionDetail.getHeight() != null) {
            memberAdditionalInfo.setHeight(hypertensionDetail.getHeight());
        }
        if (hypertensionDetail.getWeight() != null) {
            memberAdditionalInfo.setWeight(hypertensionDetail.getWeight());
            member.setWeight(hypertensionDetail.getWeight());
        }
        if (hypertensionDetail.getBmi() != null) {
            memberAdditionalInfo.setBmi(hypertensionDetail.getBmi());
        }
        if (hypertensionDetail.getDiseaseHistory() != null) {
            memberAdditionalInfo.setDiseaseHistory(hypertensionDetail.getDiseaseHistory());
        }
        if (hypertensionDetail.getOtherDisease() != null) {
            memberAdditionalInfo.setOtherDiseaseHistory(hypertensionDetail.getOtherDisease());
        }

        if (Objects.nonNull(isHypertension)) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.HT.toString()));
        }

        memberAdditionalInfo.setLastServiceLongDate(hypertensionDetail.getScreeningDate().getTime());

        memberAdditionalInfo.setHypYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getHypYear(), ImtechoUtil.getFinancialYearFromDate(hypertensionDetail.getScreeningDate())));
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private void createOrUpdateNcdMemberDetails(MemberHypertensionDetail hypertensionDetail) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(hypertensionDetail.getMemberId());
        if (ncdMember == null) {
            ncdMember = new NcdMemberEntity();
        }
        ncdMember.setMemberId(hypertensionDetail.getMemberId());
        ncdMember.setLocationId(hypertensionDetail.getLocationId());
        ncdMember.setLastServiceDate(hypertensionDetail.getScreeningDate());
        ncdMember.setLastMobileVisit(hypertensionDetail.getScreeningDate());
        if (hypertensionDetail.getDiseaseHistory() != null && !hypertensionDetail.getDiseaseHistory().equalsIgnoreCase("NONE")) {
            ncdMember.setDiseaseHistory(hypertensionDetail.getDiseaseHistory());
        }
        else{
            ncdMember.setDiseaseHistory(null);
        }
        if (hypertensionDetail.getOtherDisease() != null) {
            // Pending
            ncdMember.setOtherDiseaseHistory(hypertensionDetail.getOtherDisease());
        }
        else{
            ncdMember.setOtherDiseaseHistory(null);
        }
        if (hypertensionDetail.getSuspected() != null) {
            ncdMember.setHypertensionStatus(hypertensionDetail.getSuspected() ? "SUSPECTED" : "NORMAL");
        } else {
            ncdMember.setHypertensionStatus(hypertensionDetail.getStatus());
        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public NcdHypertensionDetailDataBean getHypertensionDetailDataBean(MemberHypertensionDetail hypertensionDetail, NcdMemberEntity ncdMember) {
        NcdHypertensionDetailDataBean hypertensionDetailDataBean = new NcdHypertensionDetailDataBean();
        hypertensionDetailDataBean.setDoneBy(hypertensionDetail.getDoneBy().toString());
        hypertensionDetailDataBean.setScreeningDate(hypertensionDetail.getScreeningDate().getTime());
        hypertensionDetailDataBean.setStatus(ncdMember.getHypertensionStatus());
        hypertensionDetailDataBean.setDiastolicBp(hypertensionDetail.getDiastolicBp());
        hypertensionDetailDataBean.setSystolicBp(hypertensionDetail.getSystolicBp());
        hypertensionDetailDataBean.setPulseRate(hypertensionDetail.getPulseRate());
        return hypertensionDetailDataBean;
    }
}
