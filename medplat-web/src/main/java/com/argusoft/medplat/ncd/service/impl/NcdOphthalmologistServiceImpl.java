package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.ncd.dao.NcdOphthalmologistDao;
import com.argusoft.medplat.ncd.dao.NcdSpecicalistMasterDao;
import com.argusoft.medplat.ncd.dto.NcdOphthalmologistDto;
import com.argusoft.medplat.ncd.mapper.NcdOphthalmologistDataMapper;
import com.argusoft.medplat.ncd.model.NcdOphthalmologistData;
import com.argusoft.medplat.ncd.model.NcdSpecialistMaster;
import com.argusoft.medplat.ncd.service.NcdOphthalmologistService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class NcdOphthalmologistServiceImpl implements NcdOphthalmologistService {

    @Autowired
    private NcdOphthalmologistDao ncdOphthalmologistDao;
    @Autowired
    private NcdSpecicalistMasterDao ncdSpecicalistMasterDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;

    @Override
    public void saveOphthalmologistResponse(NcdOphthalmologistDto ncdOphthalmologistDto) {
        Integer id = ncdOphthalmologistDao.create(NcdOphthalmologistDataMapper.dtoToEntity(ncdOphthalmologistDto));
        //check if ECG image is unsatisfactory
        if(ncdOphthalmologistDto.getLeftEyeFeedback().equals("unsatisfactory") || ncdOphthalmologistDto.getRightEyeFeedback().equals("unsatisfactory")){
            // Create Mobile Notification
            MemberEntity memberEntity=memberDao.retrieveMemberById(ncdOphthalmologistDto.getMemberId());
            FamilyEntity familyEntity=familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
            TechoNotificationMaster notificationMaster = new TechoNotificationMaster();
//            Integer notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NCD_RETINOPATHY_TEST).getId();
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
        NcdSpecialistMaster ncdSpecialistMaster = ncdSpecicalistMasterDao.retrieveByMemberId(ncdOphthalmologistDto.getMemberId());
        if(ncdSpecialistMaster == null){
            ncdSpecialistMaster = new NcdSpecialistMaster();
            ncdSpecialistMaster.setMemberId(ncdOphthalmologistDto.getMemberId());
        }
        ncdSpecialistMaster.setLastOpthamologistId(id);
        ncdSpecicalistMasterDao.createOrUpdate(ncdSpecialistMaster);
    }

    @Override
    public NcdOphthalmologistData retrieveOphthalmologistReponse(Integer memberId, Date date) {
        return ncdOphthalmologistDao.retrieveOphthalmologistReponseByDateAndMemberId(memberId,date);
    }
}
