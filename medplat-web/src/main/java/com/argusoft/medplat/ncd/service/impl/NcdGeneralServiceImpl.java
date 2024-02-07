package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.ncd.dao.MemberFollowupDetailDao;
import com.argusoft.medplat.ncd.dao.MemberGeneralDetailDao;
import com.argusoft.medplat.ncd.dao.MemberReferralDao;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MemberGeneralDto;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.NcdGeneralService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NcdGeneralServiceImpl implements NcdGeneralService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberGeneralDetailDao memberGeneralDetailDao;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private MemberFollowupDetailDao memberFollowupDetailDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;
    @Autowired
    private ImtechoSecurityUser user;

    @Override
    public MemberGeneralDetail saveGeneral(MemberGeneralDto memberGeneralDto) {
        String notificationStatus = null;
        MemberEntity memberEntity=memberDao.retrieveMemberById(memberGeneralDto.getMemberId());
        FamilyEntity familyEntity=familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        MemberGeneralDetail memberGeneralDetail = MemberDetailMapper.dtoToEntityForGeneral(memberGeneralDto);
        memberGeneralDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberGeneralDetail.setFamilyId(familyEntity.getId());


        //update master record
        //if(memberGeneralDto.getDoneBy().equals(DoneBy.MO)){
            Integer masterId = ncdService.createMasterRecord(memberGeneralDto.getMemberId(),memberGeneralDto.getHealthInfraId(), DiseaseCode.G, null,memberGeneralDto.getScreeningDate(),false);
            memberGeneralDetail.setMasterId(masterId);
//        }
//        if(memberGeneralDto.getDoneBy().equals(DoneBy.CONSULTANT)){
//            Integer masterId = ncdService.createMasterRecord(memberGeneralDto.getMemberId(),memberGeneralDto.getHealthInfraId(), DiseaseCode.G, null,memberGeneralDto.getScreeningDate(),false);
//            memberGeneralDetail.setMasterId(masterId);
//            ncdService.updateNcdMasterSubStatus(memberGeneralDto.getMemberId(), DiseaseCode.G.toString() , SubStatus.REFERRED_BACK, true, memberGeneralDto.getScreeningDate());
       // }

        if (memberGeneralDto.getTakeMedicine()) {
            memberGeneralDetail.setTreatmentStatus("CPHC");
        } else {
            memberGeneralDetail.setTreatmentStatus("OUTSIDE");
        }

        Integer generalId = memberGeneralDetailDao.create(memberGeneralDetail);

        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<memberGeneralDto.getMedicineDetail().size(); i++){
            GeneralDetailMedicineDto generalDetailMedicineDto = memberGeneralDto.getMedicineDetail().get(i);
            generalDetailMedicineDto.setIssuedDate(memberGeneralDto.getScreeningDate());
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
        createOrUpdateNcdMemberDetailsForWeb(memberGeneralDetail,memberEntity,familyEntity, list);

        //create followup / referral entry and followup notification
        if(memberGeneralDto.getFollowUpDisease() != null){
            if(!memberGeneralDto.getFollowUpDisease().isEmpty() && memberGeneralDto.getFollowUpDisease().size()>0){
                for (String diseaseCode:memberGeneralDto.getFollowUpDisease()
                ) {
                    //create Followup record for each disease
                    MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                    memberFollowupDetail.setMemberId(memberGeneralDto.getMemberId());
                    memberFollowupDetail.setDiseaseCode(diseaseCode);
                    memberFollowupDetail.setReferenceId(generalId);
                    Integer followupId = memberFollowupDetailDao.create(memberFollowupDetail);

                    //Check for all disease if master record exists. If so update sub_status to FOLLOWUP / REFERRAL for each
                    Status status = null;
                    if(memberGeneralDto.getDoesRequiredRef()){
                        //status = memberGeneralDto.getRefferralPlace().equals("Specialist") ? Status.REFERRED_CONSULTANT : SubStatus.REFERRED_OTHER;
                        switch(memberGeneralDto.getRefferralPlace()){
                            case "Specialist" :
                            case "Doctor" :
                                status = Status.REFERRED_CONSULTANT;
                                break;
                            case "MO" :
                                status = Status.REFERRED_BACK_CONSULTANT;
                                break;
                            case "higherCenter" :
                                status = Status.REFERRED_HIGHER_CENTER;
                                break;
                            default:
                                status = null;
                                break;
                        }
                    }
                    else {
                        status = memberGeneralDto.getFollowupPlace().equals("doctor") ? Status.REFERRED_MO : Status.REFERRED_BACK_MO;
                    }
                    if (diseaseCode.equalsIgnoreCase(DiseaseCode.HT.toString()) || diseaseCode.equalsIgnoreCase(DiseaseCode.D.toString()) || diseaseCode.equalsIgnoreCase(DiseaseCode.MH.toString())) {
                        notificationStatus = ncdService.retrieveMemberStatusForNotification(memberGeneralDetail.getMemberId(), diseaseCode, status);
                    }
//                    }
                    //ncdService.updateNcdMasterSubStatus(memberGeneralDto.getMemberId(), diseaseCode,subStatus,false,null);
                    ncdService.updateNcdMasterStatus(memberGeneralDto.getMemberId(),DiseaseCode.valueOf(diseaseCode),status,memberGeneralDto.getHealthInfraId(),memberGeneralDetail.getScreeningDate());
                }
            }
        }

        //check if followup in clinic or home
        if(!memberGeneralDto.getDoesRequiredRef() && memberGeneralDto.getFollowUpDate()!=null && !memberGeneralDto.getFollowupPlace().equals("doctor")){
            Integer notificationTypeId = null;
            //if notification does not exist, create new notification
            TechoNotificationMaster notificationMaster = new TechoNotificationMaster();
            notificationMaster.setScheduleDate(memberGeneralDto.getFollowUpDate());
            if (memberGeneralDto.getFollowupPlace().equals("clinic")) {
                notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_NCD_CLINIC_VISIT).getId();
//                techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
                Calendar instance = Calendar.getInstance();
                instance.setTime(memberGeneralDto.getFollowUpDate());
                instance.add(Calendar.DATE, 7);
                notificationMaster.setDueOn(instance.getTime());
                instance.add(Calendar.DATE, 60);
                notificationMaster.setExpiryDate(instance.getTime());
            } else if (memberGeneralDto.getFollowupPlace().equals("home")) {
                notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_NCD_HOME_VISIT).getId();
//                techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
                Calendar instance = Calendar.getInstance();
                instance.setTime(memberGeneralDto.getFollowUpDate());
                instance.add(Calendar.DATE, 7);
                notificationMaster.setDueOn(instance.getTime());
                instance.add(Calendar.DATE, 90);
                notificationMaster.setExpiryDate(instance.getTime());
            }
            notificationMaster.setState(TechoNotificationMaster.State.PENDING);
            notificationMaster.setNotificationTypeId(notificationTypeId);
            notificationMaster.setLocationId(familyEntity.getAreaId());
            notificationMaster.setMemberId(memberGeneralDto.getMemberId());
            notificationMaster.setFamilyId(familyEntity.getId());
            notificationMaster.setRelatedId(generalId);
            if (notificationStatus != null) {
                notificationMaster.setOtherDetails(notificationStatus);
            }
//            notificationMaster.setOtherDetails(memberGeneralDto.getFollowUpDisease().toString());
            techoNotificationMasterDao.create(notificationMaster);

        }

        //createMedicineDetailsForGeneral(memberGeneralDto, memberReferralId, generalId);
        ncdService.createMedicineDetails(memberGeneralDto.getMedicineDetail(), memberGeneralDto.getMemberId(), memberGeneralDto.getScreeningDate(), generalId, MemberDiseaseDiagnosis.DiseaseCode.G,memberGeneralDto.getHealthInfraId());

        //handle edited medicines
        ncdService.handleEditMedicineDetails(memberGeneralDto.getEditedMedicineDetail(), memberGeneralDto.getMemberId(), memberGeneralDto.getScreeningDate(), generalId, MemberDiseaseDiagnosis.DiseaseCode.G,memberGeneralDto.getHealthInfraId());

        //handle deleted medicines
        ncdService.handleDeleteMedicineDetails(memberGeneralDto.getDeletedMedicineDetail(), memberGeneralDto.getMemberId(),memberGeneralDto.getHealthInfraId());

        //create visit history
        ncdService.createVisitHistory(memberGeneralDetail.getMasterId(),memberGeneralDetail.getMemberId(),memberGeneralDetail.getScreeningDate(),memberGeneralDetail.getDoneBy(),generalId,null,DiseaseCode.G,null,memberGeneralDetail.getCategory()!=null ? memberGeneralDetail.getCategory().toString():null);

        ncdService.updateFamilyAdditionalInfo(familyEntity, memberGeneralDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
        return memberGeneralDetailDao.retrieveById(generalId);
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MemberGeneralDetail memberGeneralDetail, MemberEntity memberEntity, FamilyEntity familyEntity, List<JSONObject> medicineDtoList) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(memberEntity.getId());
        if(ncdMember == null){
            ncdMember = new NcdMemberEntity();
            ncdMember.setMemberId(memberEntity.getId());
            if (familyEntity.getAreaId() != null) {
                ncdMember.setLocationId(familyEntity.getAreaId());
            } else {
                ncdMember.setLocationId(familyEntity.getLocationId());
            }
        }
        ncdMember.setLastMoVisit(memberGeneralDetail.getScreeningDate());
        ncdMember.setLastServiceDate(memberGeneralDetail.getScreeningDate());
        if(memberGeneralDetail.getComment()!=null) {
            ncdMember.setLastMoComment(memberGeneralDetail.getComment());
            ncdMember.setLastMoCommentBy(user.getId());
            ncdMember.setLastMoCommentFormType(FormType.Referred_patient_general.toString());
        }
        else{
            ncdMember.setLastMoComment(null);
            ncdMember.setLastMoCommentBy(null);
            ncdMember.setLastMoCommentFormType(null);
        }
        if(memberGeneralDetail.getRemarks()!=null) {
            ncdMember.setLastRemark(memberGeneralDetail.getRemarks());
            ncdMember.setLastRemarkBy(user.getId());
            ncdMember.setLastRemarkFormType(FormType.Referred_patient_general.toString());
        }
        else{
            ncdMember.setLastRemark(null);
            ncdMember.setLastRemarkBy(null);
            ncdMember.setLastRemarkFormType(null);
        }

        ncdMember.setMedicineDetails(medicineDtoList.toString());
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public MemberGeneralDto retrieveGeneralDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type) {
        MemberGeneralDetail memberGeneralDetail = memberGeneralDetailDao.retrieveByMemberIdAndScreeningDate(memberId,date,type);
        if(memberGeneralDetail!=null){
            MemberGeneralDto memberGeneralDto = MemberDetailMapper.entityToDtoForGeneral(memberGeneralDetail);
            //get detail from followup table
            List<MemberFollowupDetail> memberFollowupDetails = memberFollowupDetailDao.getFollowUpByReferenceId(memberGeneralDto.getId());
            if(memberFollowupDetails != null){
                if(memberFollowupDetails.size()>0 && !memberFollowupDetails.isEmpty()){
                    List<String> followUpDiseases = new ArrayList<>();
                    for (MemberFollowupDetail memberFollowupDetail:memberFollowupDetails
                    ) {
                        followUpDiseases.add(memberFollowupDetail.getDiseaseCode());
                    }
                    memberGeneralDto.setFollowUpDisease(followUpDiseases);
                }
            }
            return memberGeneralDto;
        }
        else{
            return null;
        }
    }

    @Override
    public MemberGeneralDetail retrieveLastRecordForGeneralByMemberId(Integer memberId) {
        return memberGeneralDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    @Override
    public MemberGeneralDto retrieveLastCommentForGeneralByMemberIdAndType(Integer memberId, DoneBy type) {
        return memberGeneralDetailDao.retrieveLastCommentForGeneralByMemberIdAndType(memberId,type);
    }
}
