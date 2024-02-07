package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.ncd.dao.*;
import com.argusoft.medplat.ncd.dto.NcdAmputationMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdEcgMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdRenalMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdStrokeMemberDetailDto;
import com.argusoft.medplat.ncd.mapper.NcdAmputationMemberDetailMapper;
import com.argusoft.medplat.ncd.mapper.NcdEcgMemberDetailMapper;
import com.argusoft.medplat.ncd.mapper.NcdRenalMemberDetailMapper;
import com.argusoft.medplat.ncd.mapper.NcdStrokeMemberDetailMapper;
import com.argusoft.medplat.ncd.model.NcdSpecialistMaster;
import com.argusoft.medplat.ncd.model.NcdStrokeMemberDetail;
import com.argusoft.medplat.ncd.service.NcdMoSpecialistReviewService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class NcdMoSpecialistReviewServiceImpl implements NcdMoSpecialistReviewService {

    @Autowired
    NcdEcgMemberDetailDao ncdEcgMemberDetailDao;
    @Autowired
    NcdStrokeMemberDetailDao strokeDao;
    @Autowired
    NcdAmputationMemberDetailDao amputationDao;
    @Autowired
    NcdRenalMemberDetailDao renalDao;
    @Autowired
    NcdSpecicalistMasterDao ncdSpecicalistMasterDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;

    @Override
    public void saveEcgData(NcdEcgMemberDetailDto ncdEcgMemberDetailDto) {
        Integer id = ncdEcgMemberDetailDao.create(NcdEcgMemberDetailMapper.dtoToEntity(ncdEcgMemberDetailDto));
        //check if ECG image is unsatisfactory
        if(!ncdEcgMemberDetailDto.getNeedsRetake()){
            // Create Mobile Notification
            MemberEntity memberEntity=memberDao.retrieveMemberById(ncdEcgMemberDetailDto.getMemberId());
            FamilyEntity familyEntity=familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
            TechoNotificationMaster notificationMaster = new TechoNotificationMaster();
//            Integer notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NCD_ECG_TEST).getId();
//            techoNotificationMasterDao.markOlderNotificationAsMissed(memberEntity.getId(), notificationTypeId);
            notificationMaster.setScheduleDate(new Date());
            notificationMaster.setState(TechoNotificationMaster.State.PENDING);
//            notificationMaster.setNotificationTypeId(notificationTypeId);
            notificationMaster.setLocationId(familyEntity.getAreaId());
            notificationMaster.setMemberId(memberEntity.getId());
            notificationMaster.setFamilyId(familyEntity.getId());
            notificationMaster.setRelatedId(id);
            techoNotificationMasterDao.create(notificationMaster);
        }
        createOrUpdateMasterRecord(ncdEcgMemberDetailDto.getMemberId(),id, "ECG");
    }

    @Override
    public void saveStrokeData(NcdStrokeMemberDetailDto dto) {
        Integer id= strokeDao.create(NcdStrokeMemberDetailMapper.dtoToEntity(dto));
        createOrUpdateMasterRecord(dto.getMemberId(),id, "Stroke");
    }

    @Override
    public void saveAmputationData(NcdAmputationMemberDetailDto dto) {
        Integer id= amputationDao.create(NcdAmputationMemberDetailMapper.dtoToEntity(dto));
        createOrUpdateMasterRecord(dto.getMemberId(),id, "Amputation");
    }

    @Override
    public void saveRenalData(NcdRenalMemberDetailDto dto) {
        Integer id= renalDao.create(NcdRenalMemberDetailMapper.dtoToEntity(dto));
        createOrUpdateMasterRecord(dto.getMemberId(),id, "Renal");
    }

    private void createOrUpdateMasterRecord(Integer memberId, Integer id, String type) {
        NcdSpecialistMaster ncdSpecialistMaster = ncdSpecicalistMasterDao.retrieveByMemberId(memberId);
        if(ncdSpecialistMaster == null){
            ncdSpecialistMaster = new NcdSpecialistMaster();
            ncdSpecialistMaster.setMemberId(memberId);
        }
        switch (type){
            case "ECG":
                ncdSpecialistMaster.setLastEcgSpecialistId(id);
                break;
            case "Stroke":
                ncdSpecialistMaster.setLastStrokeSpecialistId(id);
                break;
            case "Amputation":
                ncdSpecialistMaster.setLastAmputationSpecialistId(id);
                break;
            case "Renal":
                ncdSpecialistMaster.setLastRenalSpecialistId(id);
                break;
            default:
                break;
        }
        ncdSpecicalistMasterDao.createOrUpdate(ncdSpecialistMaster);
    }
}
