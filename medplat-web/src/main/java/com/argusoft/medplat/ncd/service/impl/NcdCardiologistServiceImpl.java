package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.ncd.dao.NcdCardiologistDao;
import com.argusoft.medplat.ncd.dao.NcdSpecicalistMasterDao;
import com.argusoft.medplat.ncd.dto.NcdCardiologistDto;
import com.argusoft.medplat.ncd.mapper.NcdCardiologistDataMapper;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;
import com.argusoft.medplat.ncd.model.NcdSpecialistMaster;
import com.argusoft.medplat.ncd.service.NcdCardiologistService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class NcdCardiologistServiceImpl implements NcdCardiologistService {
    @Autowired
    private NcdCardiologistDao ncdCardiologistDao;
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
    public void saveCardiologistResponse(NcdCardiologistDto ncdCardiologistDto) {
        Integer id = ncdCardiologistDao.create(NcdCardiologistDataMapper.dtoToEntity(ncdCardiologistDto));
        //check if ECG image is unsatisfactory
        if(ncdCardiologistDto.getCaseConfirmed() == null && !ncdCardiologistDto.getSatisfactoryImage()){
            // Create Mobile Notification
            MemberEntity memberEntity=memberDao.retrieveMemberById(ncdCardiologistDto.getMemberId());
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
        NcdSpecialistMaster ncdSpecialistMaster = ncdSpecicalistMasterDao.retrieveByMemberId(ncdCardiologistDto.getMemberId());
        if(ncdSpecialistMaster == null){
            ncdSpecialistMaster = new NcdSpecialistMaster();
            ncdSpecialistMaster.setMemberId(ncdCardiologistDto.getMemberId());
        }
        ncdSpecialistMaster.setLastCardiologistId(id);
        ncdSpecicalistMasterDao.createOrUpdate(ncdSpecialistMaster);
    }

    @Override
    public NcdCardiologistData retrieveCardiologistReponse(Integer memberId, Date date) {
        return ncdCardiologistDao.retrieveCardiologistReponseByMemberIdAndDate(memberId,date);
    }
}
