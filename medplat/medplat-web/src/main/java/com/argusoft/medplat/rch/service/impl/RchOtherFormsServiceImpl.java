package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.RchOtherFormsDao;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.model.RchOtherFormsMaster;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.rch.service.RchOtherFormsService;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * Define services for rch other forms.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class RchOtherFormsServiceImpl implements RchOtherFormsService {

    @Autowired
    private RchOtherFormsDao rchOtherFormsDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private TechoNotificationMasterDao notificationMasterDao;

    @Autowired
    private ImmunisationService immunisationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeFhsrPhoneVerificationForm(UserMaster user, Map<String, String> keyAndAnswerMap) {
        String memberId = keyAndAnswerMap.get("memberId");
        String phoneNumber = keyAndAnswerMap.get("phoneNumber");

        MemberEntity memberEntity = memberDao.retrieveById(Integer.valueOf(memberId));
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        RchOtherFormsMaster rchOtherFormsMaster = new RchOtherFormsMaster();
        rchOtherFormsMaster.setMemberId(memberEntity.getId());
        rchOtherFormsMaster.setFamilyId(familyEntity.getId());
        rchOtherFormsMaster.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        rchOtherFormsMaster.setLatitude(keyAndAnswerMap.get("latitude"));
        rchOtherFormsMaster.setLongitude(keyAndAnswerMap.get("longitude"));
        rchOtherFormsMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("mobileStartDate"))));
        rchOtherFormsMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("mobileEndDate"))));
        rchOtherFormsMaster.setFormCode(MobileConstantUtil.FHSR_PHONE_UPDATE);
        rchOtherFormsMaster.setFormData(new Gson().toJson(keyAndAnswerMap));
        rchOtherFormsDao.create(rchOtherFormsMaster);

        memberEntity.setMobileNumber(phoneNumber);
        memberEntity.setFhsrPhoneVerified(Boolean.TRUE);
        memberDao.update(memberEntity);
        return rchOtherFormsMaster.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeTT2AlertForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        TechoNotificationMaster notificationMaster = notificationMasterDao.retrieveById(Integer.valueOf(parsedRecordBean.getNotificationId()));

        MemberEntity memberEntity = memberDao.retrieveById(notificationMaster.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        RchOtherFormsMaster rchOtherFormsMaster = new RchOtherFormsMaster();
        rchOtherFormsMaster.setMemberId(memberEntity.getId());
        rchOtherFormsMaster.setFamilyId(familyEntity.getId());
        rchOtherFormsMaster.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        rchOtherFormsMaster.setLongitude(keyAndAnswerMap.get("-1"));
        rchOtherFormsMaster.setLatitude(keyAndAnswerMap.get("-2"));
        rchOtherFormsMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        rchOtherFormsMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        rchOtherFormsMaster.setFormCode(MobileConstantUtil.TT2_ALERT);
        rchOtherFormsMaster.setFormData(new Gson().toJson(keyAndAnswerMap));
        rchOtherFormsDao.create(rchOtherFormsMaster);

        if (keyAndAnswerMap.containsKey("11") && keyAndAnswerMap.get("11").equals("T")
                && keyAndAnswerMap.containsKey("12")) {
            Date givenDate = new Date(Long.parseLong(keyAndAnswerMap.get("12")));
            StringBuilder immunisationGiven = new StringBuilder();
            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_TT_2);
            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
            immunisationGiven.append(sdf.format(givenDate));

            ImmunisationMaster immunisationMaster = new ImmunisationMaster(
                    familyEntity.getId(), memberEntity.getId(), "M", MobileConstantUtil.TT2_ALERT,
                    rchOtherFormsMaster.getId(), notificationMaster.getId(), RchConstants.VaccinationType.TT2,
                    givenDate, user.getId(),
                    (familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId()),
                    notificationMaster.getRefCode()
            );

            immunisationService.createImmunisationMaster(immunisationMaster);

            if (memberEntity.getImmunisationGiven() != null && memberEntity.getImmunisationGiven().length() > 0) {
                String sb = memberEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                String immunisation = sb.replace(" ", "");
                memberEntity.setImmunisationGiven(immunisation);
            } else {
                String immunisation = immunisationGiven.toString().replace(" ", "");
                memberEntity.setImmunisationGiven(immunisation);
            }

            memberDao.update(memberEntity);

            notificationMaster.setState(TechoNotificationMaster.State.COMPLETED);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 3);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            notificationMaster.setState(TechoNotificationMaster.State.RESCHEDULE);
            notificationMaster.setScheduleDate(new Date());
            notificationMaster.setDueOn(calendar.getTime());
        }
        notificationMaster.setActionBy(user.getId());
        notificationMasterDao.update(notificationMaster);

        return rchOtherFormsMaster.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeIronSucroseForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        TechoNotificationMaster notificationMaster = notificationMasterDao.retrieveById(Integer.valueOf(parsedRecordBean.getNotificationId()));

        MemberEntity memberEntity = memberDao.retrieveById(notificationMaster.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        RchOtherFormsMaster rchOtherFormsMaster = new RchOtherFormsMaster();
        rchOtherFormsMaster.setMemberId(memberEntity.getId());
        rchOtherFormsMaster.setFamilyId(familyEntity.getId());
        rchOtherFormsMaster.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        rchOtherFormsMaster.setLongitude(keyAndAnswerMap.get("-1"));
        rchOtherFormsMaster.setLatitude(keyAndAnswerMap.get("-2"));
        rchOtherFormsMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        rchOtherFormsMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        rchOtherFormsMaster.setFormCode(MobileConstantUtil.IRON_SUCROSE_ALERT);
        rchOtherFormsMaster.setFormData(new Gson().toJson(keyAndAnswerMap));
        rchOtherFormsDao.create(rchOtherFormsMaster);

        if (keyAndAnswerMap.containsKey("11") && keyAndAnswerMap.get("11").equals("T")) {
            notificationMaster.setState(TechoNotificationMaster.State.COMPLETED);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 3);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            notificationMaster.setState(TechoNotificationMaster.State.RESCHEDULE);
            notificationMaster.setScheduleDate(new Date());
            notificationMaster.setDueOn(calendar.getTime());
        }
        notificationMaster.setActionBy(user.getId());
        notificationMasterDao.update(notificationMaster);

        return rchOtherFormsMaster.getId();
    }
}
