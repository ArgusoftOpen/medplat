package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.ncd.dao.MemberFollowupDetailDao;
import com.argusoft.medplat.ncd.dao.MoReviewDao;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.FormType;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;
import com.argusoft.medplat.ncd.model.MemberFollowupDetail;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import com.argusoft.medplat.ncd.service.NcdMOReviewService;
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
public class NcdMOReviewServiceImpl implements NcdMOReviewService {

    @Autowired
    private MoReviewDao moReviewDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberFollowupDetailDao memberFollowupDetailDao;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;
    @Autowired
    private ImtechoSecurityUser user;

    @Override
    public void saveMOReview(MOReviewDto moReviewDto) {
        String notificationStatus = null;

        MOReviewDetail moReviewDetail= MemberDetailMapper.dtoToEntityForMOReviewDetail(moReviewDto);
        moReviewDetail.setIsFollowup(true);
        Integer id = moReviewDao.create(moReviewDetail);
        MemberEntity memberEntity=memberDao.retrieveMemberById(moReviewDto.getMemberId());
        FamilyEntity familyEntity=familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        //Handle Medicine
        ncdService.createMedicineDetails(moReviewDto.getMedicineDetail(),moReviewDto.getMemberId(),moReviewDto.getScreeningDate(),id,null,moReviewDto.getHealthInfraId());
        //handle edited medicines
        ncdService.handleEditMedicineDetails(moReviewDto.getEditedMedicineDetail(), moReviewDto.getMemberId(), moReviewDto.getScreeningDate(), id, null,moReviewDto.getHealthInfraId());

        //handle deleted medicines
        ncdService.handleDeleteMedicineDetails(moReviewDto.getDeletedMedicineDetail(), moReviewDto.getMemberId(),moReviewDto.getHealthInfraId());

        List<String> diseases = new ArrayList<>();

        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<moReviewDto.getMedicineDetail().size(); i++){
            GeneralDetailMedicineDto generalDetailMedicineDto = moReviewDto.getMedicineDetail().get(i);
            generalDetailMedicineDto.setIssuedDate(moReviewDto.getScreeningDate());
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


        //Update details in imt_member_ncd_details
        createOrUpdateNcdMemberDetailsForWeb(moReviewDto,memberEntity,familyEntity, list);

        if(moReviewDto.getFollowUpDisease() != null){
            if(!moReviewDto.getFollowUpDisease().isEmpty() && moReviewDto.getFollowUpDisease().size()>0){
                for (String diseaseCode:moReviewDto.getFollowUpDisease()
                ) {
                    diseases.add(diseaseCode);
                    //create Followup record for each disease
                    MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                    memberFollowupDetail.setMemberId(moReviewDto.getMemberId());
                    memberFollowupDetail.setDiseaseCode(diseaseCode);
                    memberFollowupDetail.setReferenceId(id);
                    memberFollowupDetail.setCreatedFrom("MOREVIEW");
                    Integer followupId = memberFollowupDetailDao.create(memberFollowupDetail);

                    Status status = null;
                    if(moReviewDto.getDoesRequiredRef()){
                        switch(moReviewDto.getRefferralPlace()){
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
                        status = moReviewDto.getFollowupPlace().equals("doctor") ? Status.REFERRED_MO : Status.REFERRED_BACK_MO;
                    }
                    if (diseaseCode.equalsIgnoreCase(DiseaseCode.HT.toString()) || diseaseCode.equalsIgnoreCase(DiseaseCode.D.toString()) || diseaseCode.equalsIgnoreCase(DiseaseCode.MH.toString())) {
                        notificationStatus = ncdService.retrieveMemberStatusForNotification(moReviewDto.getMemberId(), diseaseCode, status);
                    }
                    ncdService.updateNcdMasterStatus(moReviewDto.getMemberId(),DiseaseCode.valueOf(diseaseCode),status,moReviewDto.getHealthInfraId(),moReviewDto.getScreeningDate());

                }
                moReviewDetail.setDiseases(diseases.toString());
                moReviewDao.update(moReviewDetail);
            }
        }

        //check if followup in clinic or home
        if(!moReviewDto.getDoesRequiredRef() && moReviewDto.getFollowUpDate()!=null && !moReviewDto.getFollowupPlace().equals("doctor")){
            Integer notificationTypeId = null;
            //if notification does not exist, create new notification
            TechoNotificationMaster notificationMaster = new TechoNotificationMaster();
            notificationMaster.setScheduleDate(moReviewDto.getFollowUpDate());
            if (moReviewDto.getFollowupPlace().equals("clinic")) {
                notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_NCD_CLINIC_VISIT).getId();
//                techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
                Calendar instance = Calendar.getInstance();
                instance.setTime(moReviewDto.getFollowUpDate());
                instance.add(Calendar.DATE, 7);
                notificationMaster.setDueOn(instance.getTime());
                instance.add(Calendar.DATE, 60);
                notificationMaster.setExpiryDate(instance.getTime());
            } else if (moReviewDto.getFollowupPlace().equals("home")) {
                notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NOTIFICATION_NCD_HOME_VISIT).getId();
//                techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
                Calendar instance = Calendar.getInstance();
                instance.setTime(moReviewDto.getFollowUpDate());
                instance.add(Calendar.DATE, 7);
                notificationMaster.setDueOn(instance.getTime());
                instance.add(Calendar.DATE, 90);
                notificationMaster.setExpiryDate(instance.getTime());
            }
            notificationMaster.setState(TechoNotificationMaster.State.PENDING);
            notificationMaster.setNotificationTypeId(notificationTypeId);
            notificationMaster.setLocationId(familyEntity.getAreaId());
            notificationMaster.setMemberId(moReviewDto.getMemberId());
            notificationMaster.setFamilyId(familyEntity.getId());
            notificationMaster.setRelatedId(id);
            notificationMaster.setOtherDetails("MO_REVIEW");
//            notificationMaster.setOtherDetails(moReviewDto.getFollowUpDisease().toString());
            techoNotificationMasterDao.create(notificationMaster);

        }
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MOReviewDto moReviewDto, MemberEntity memberEntity, FamilyEntity familyEntity, List<JSONObject> medicineDtoList) {
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
        ncdMember.setLastMoVisit(moReviewDto.getScreeningDate());
        ncdMember.setLastServiceDate(moReviewDto.getScreeningDate());
        if(moReviewDto.getComment()!=null) {
            ncdMember.setLastMoComment(moReviewDto.getComment());
            ncdMember.setLastMoCommentBy(user.getId());
            ncdMember.setLastMoCommentFormType(FormType.MO_review.toString());
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
    public MOReviewDetail retrieveMoReviewByMemberAndDate(Integer memberId, Date screeningDate) {
        return moReviewDao.retrieveMoReviewByMemberAndDate(memberId,screeningDate);
    }

    @Override
    public List<MemberDetailDto> retreiveSearchedMembersMOReview(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag) {
        return moReviewDao.retreiveSearchedMembersMOReview(limit,offset,searchBy,searchString,flag);
    }

    @Override
    public MOReviewDto retrieveLastCommentByMOReview(Integer memberId) {
        return moReviewDao.retrieveLastCommentByMOReview(memberId);
    }
}
