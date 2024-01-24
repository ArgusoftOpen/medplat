package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.ncd.dao.MemberFollowupDetailDao;
import com.argusoft.medplat.ncd.dao.MoReviewDao;
import com.argusoft.medplat.ncd.dao.NcdMOReviewFollowupDao;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MOReviewFollowupDto;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.FormType;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
import com.argusoft.medplat.ncd.model.MOReviewFollowupDetail;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;
import com.argusoft.medplat.ncd.model.MemberFollowupDetail;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import com.argusoft.medplat.ncd.service.NcdMOReviewFollowupService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
public class NcdMOReviewFollowupServiceImpl implements NcdMOReviewFollowupService {

    @Autowired
    private NcdMOReviewFollowupDao ncdMOReviewFollowupDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private MemberFollowupDetailDao memberFollowupDetailDao;
    @Autowired
    private MoReviewDao moReviewDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;
    @Autowired
    private ImtechoSecurityUser user;

    @Override
    public void saveMOReviewFollowup(MOReviewFollowupDto moReviewFollowupDto) {
        String notificationStatus = null;
        //Save MO review followup
        MOReviewFollowupDetail moReviewFollowupDetail = MemberDetailMapper.dtoToEntityForMOReviewFollowupDetail(moReviewFollowupDto);
        Integer id = ncdMOReviewFollowupDao.create(moReviewFollowupDetail);

        MemberEntity memberEntity=memberDao.retrieveMemberById(moReviewFollowupDto.getMemberId());
        FamilyEntity familyEntity=familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        //Handle medicine
        ncdService.createMedicineDetails(moReviewFollowupDto.getMedicineDetail(),moReviewFollowupDto.getMemberId(),moReviewFollowupDto.getScreeningDate(),id,null,moReviewFollowupDto.getHealthInfraId());
        //handle edited medicines
        ncdService.handleEditMedicineDetails(moReviewFollowupDto.getEditedMedicineDetail(), moReviewFollowupDto.getMemberId(), moReviewFollowupDto.getScreeningDate(), id, null,moReviewFollowupDto.getHealthInfraId());

        //handle deleted medicines
        ncdService.handleDeleteMedicineDetails(moReviewFollowupDto.getDeletedMedicineDetail(), moReviewFollowupDto.getMemberId(),moReviewFollowupDto.getHealthInfraId());

        List<String> diseases = new ArrayList<>();

        //Update is_followup flag in MOReview based on the flag isRemove
        if(moReviewFollowupDto.getIsRemove()!=null && moReviewFollowupDto.getIsRemove()){
            List<MOReviewDetail> moReviewDetails = moReviewDao.retrieveMOReviewsByMemberId(moReviewFollowupDto.getMemberId());
            if(!moReviewDetails.isEmpty() && moReviewDetails.size()>0){
                for (MOReviewDetail moReviewDetail: moReviewDetails
                     ) {
                    moReviewDetail.setIsFollowup(false);
                    moReviewDao.update(moReviewDetail);
                }
            }
        }

        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<moReviewFollowupDto.getMedicineDetail().size(); i++){
            GeneralDetailMedicineDto generalDetailMedicineDto = moReviewFollowupDto.getMedicineDetail().get(i);
            generalDetailMedicineDto.setIssuedDate(moReviewFollowupDto.getScreeningDate());
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

        //update details in imt_member_ncd_detail
        createOrUpdateNcdMemberDetailsForWeb(moReviewFollowupDto,memberEntity,familyEntity, list);

        //Handle referral & followup
        if(moReviewFollowupDto.getFollowUpDisease() != null){
            if(!moReviewFollowupDto.getFollowUpDisease().isEmpty() && moReviewFollowupDto.getFollowUpDisease().size()>0){
                for (String diseaseCode:moReviewFollowupDto.getFollowUpDisease()
                ) {
                    diseases.add(diseaseCode);
                    //create Followup record for each disease
                    MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                    memberFollowupDetail.setMemberId(moReviewFollowupDto.getMemberId());
                    memberFollowupDetail.setDiseaseCode(diseaseCode);
                    memberFollowupDetail.setReferenceId(id);
                    memberFollowupDetail.setCreatedFrom("MOREVIEWFOLLOWUP");
                    Integer followupId = memberFollowupDetailDao.create(memberFollowupDetail);

                    Status status = null;
                    if(moReviewFollowupDto.getDoesRequiredRef()){
                        switch(moReviewFollowupDto.getRefferralPlace()){
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
                        status = moReviewFollowupDto.getFollowupPlace().equals("doctor") ? Status.REFERRED_MO : Status.REFERRED_BACK_MO;
                    }
                    if (diseaseCode.equalsIgnoreCase(DiseaseCode.HT.toString()) || diseaseCode.equalsIgnoreCase(DiseaseCode.D.toString()) || diseaseCode.equalsIgnoreCase(DiseaseCode.MH.toString())) {
                        notificationStatus = ncdService.retrieveMemberStatusForNotification(moReviewFollowupDto.getMemberId(), diseaseCode, status);
                    }
                    ncdService.updateNcdMasterStatus(moReviewFollowupDto.getMemberId(),DiseaseCode.valueOf(diseaseCode),status,moReviewFollowupDto.getHealthInfraId(),moReviewFollowupDto.getScreeningDate());
                }
                moReviewFollowupDetail.setDiseases(diseases.toString());
                ncdMOReviewFollowupDao.update(moReviewFollowupDetail);
            }
        }

        //check if followup in clinic or home
        if(!moReviewFollowupDto.getDoesRequiredRef() && moReviewFollowupDto.getFollowUpDate()!=null && !moReviewFollowupDto.getFollowupPlace().equals("doctor")){
            Integer notificationTypeId = null;
            //if notification does not exist, create new notification
            TechoNotificationMaster notificationMaster = new TechoNotificationMaster();
            notificationMaster.setScheduleDate(moReviewFollowupDto.getFollowUpDate());
            if (moReviewFollowupDto.getFollowupPlace().equals("clinic")) {
                notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_NCD_CLINIC_VISIT).getId();
//                techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
                Calendar instance = Calendar.getInstance();
                instance.setTime(moReviewFollowupDto.getFollowUpDate());
                instance.add(Calendar.DATE, 7);
                notificationMaster.setDueOn(instance.getTime());
                instance.add(Calendar.DATE, 60);
                notificationMaster.setExpiryDate(instance.getTime());
            } else if (moReviewFollowupDto.getFollowupPlace().equals("home")) {
                notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_NCD_HOME_VISIT).getId();
//                techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
                Calendar instance = Calendar.getInstance();
                instance.setTime(moReviewFollowupDto.getFollowUpDate());
                instance.add(Calendar.DATE, 7);
                notificationMaster.setDueOn(instance.getTime());
                instance.add(Calendar.DATE, 90);
                notificationMaster.setExpiryDate(instance.getTime());
            }
            notificationMaster.setState(TechoNotificationMaster.State.PENDING);
            notificationMaster.setNotificationTypeId(notificationTypeId);
            notificationMaster.setLocationId(familyEntity.getAreaId());
            notificationMaster.setMemberId(moReviewFollowupDto.getMemberId());
            notificationMaster.setFamilyId(familyEntity.getId());
            notificationMaster.setRelatedId(id);
            notificationMaster.setOtherDetails(moReviewFollowupDto.getFollowUpDisease().toString());
            techoNotificationMasterDao.create(notificationMaster);
        }
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MOReviewFollowupDto moReviewFollowupDto, MemberEntity memberEntity, FamilyEntity familyEntity, List<JSONObject> medicineDtoList) {
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
        ncdMember.setLastMoVisit(moReviewFollowupDto.getScreeningDate());
        ncdMember.setLastServiceDate(moReviewFollowupDto.getScreeningDate());
        if(moReviewFollowupDto.getComment()!=null) {
            ncdMember.setLastMoComment(moReviewFollowupDto.getComment());
            ncdMember.setLastMoCommentBy(user.getId());
            ncdMember.setLastMoCommentFormType(FormType.MO_review_followup.toString());
        }
        else{
            ncdMember.setLastMoComment(null);
            ncdMember.setLastMoCommentBy(null);
            ncdMember.setLastMoCommentFormType(null);
        }

        ncdMember.setMedicineDetails(medicineDtoList.toString());
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public MOReviewFollowupDetail retrieveMoReviewFollowupByMemberAndDate(Integer memberId, Date date) {
        return ncdMOReviewFollowupDao.retrieveMoReviewFollowupByMemberAndDate(memberId,date);
    }

    @Override
    public MOReviewFollowupDto retrieveLastCommentByMOReviewFollowup(Integer memberId) {
        return ncdMOReviewFollowupDao.retrieveLastCommentByMOReviewFollowup(memberId);
    }
}
