/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AshaAncMasterDao;
import com.argusoft.medplat.rch.model.AshaAncMaster;
import com.argusoft.medplat.rch.service.AshaAncService;
import com.argusoft.medplat.rch.service.AshaReportedEventService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * Define services for ASHA anc service.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AshaAncServiceImpl implements AshaAncService {

    @Autowired
    private AshaAncMasterDao ashaAncMasterDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private TechoNotificationMasterDao notificationMasterDao;

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private AshaReportedEventService ashaReportedEventService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAncServiceVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        AshaAncMaster ashaAncMaster = new AshaAncMaster();
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity member = memberDao.retrieveById(memberId);

        ashaAncMaster.setMemberId(memberId);
        ashaAncMaster.setFamilyId(familyId);
        ashaAncMaster.setLocationId(locationId);
        ashaAncMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));
        ashaAncMaster.setPregnancyRegDetId(member.getCurPregRegDetId());
        ashaAncMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        ashaAncMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));

        if (keyAndAnswerMap.get("51") != null && !ImtechoUtil.returnTrueFalseFromInitials(keyAndAnswerMap.get("51"))) {
            //This means that the member is not present. So not storing anything.
            if (ashaAncMaster.getNotificationId() != null && ashaAncMaster.getNotificationId() > 0) {
                TechoNotificationMaster notification = notificationMasterDao.retrieveById(ashaAncMaster.getNotificationId());
                if (notification != null) {
                    notification.setModifiedOn(new Date());
                    notificationMasterDao.update(notification);
                }
            }
            return -1;
        }

        String isAadharScanned = keyAndAnswerMap.get("107");
        if (isAadharScanned == null) {
            isAadharScanned = keyAndAnswerMap.get("108");
        }
        String aadharMap = null;
        String aadharNumber = null;
        if (isAadharScanned != null) {
            if (isAadharScanned.equals("1")) {
                aadharMap = keyAndAnswerMap.get("109");
            } else {
                String answer = keyAndAnswerMap.get("110");
                if (answer != null && !answer.equals("T")) {
                    aadharNumber = answer.replace("F/", "");
                }
            }
        }
        mobileFhsService.updateMemberDetailsFromRchForms(keyAndAnswerMap.get("100"), aadharMap, aadharNumber,
                keyAndAnswerMap.get("102"), keyAndAnswerMap.get("104"), member);

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToAncVisitEntity(key, answer, ashaAncMaster, keyAndAnswerMap);
        }

        ashaAncMasterDao.create(ashaAncMaster);

        if (ashaAncMaster.getMemberStatus().equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
            ashaReportedEventService.createNotificationForReportedEventByAsha(ashaAncMaster.getMemberId(),
                    MobileConstantUtil.NOTIFICATION_FHW_DEATH_CONF,
                    ashaAncMaster.getFamilyId(), ashaAncMaster.getLocationId(), ashaAncMaster.getId(),
                    MobileConstantUtil.ASHA_ANC_VISIT);
            return ashaAncMaster.getId();
        }

        if (ashaAncMaster.getMemberStatus().equalsIgnoreCase(RchConstants.MEMBER_STATUS_MIGRATED)) {
            ashaReportedEventService.createNotificationForReportedEventByAsha(ashaAncMaster.getMemberId(),
                    MobileConstantUtil.NOTIFICATION_FHW_MEMBER_MIGRATION,
                    ashaAncMaster.getFamilyId(), ashaAncMaster.getLocationId(), ashaAncMaster.getId(),
                    MobileConstantUtil.ASHA_ANC_VISIT);
            return ashaAncMaster.getId();
        }

        ashaAncMasterDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.ASHA_ANC, ashaAncMaster.getId()));
        return ashaAncMaster.getId();
    }

    /**
     * Set answer to anc visit morbidity.
     *
     * @param key      Key.
     * @param answer   Answer for member's anc visit details.
     * @param ancVisit Anc visit details.
     */
    private void setAnswersToAncVisitEntity(String key, String answer, AshaAncMaster ancVisit, Map<String, String> keyAndAnswersMap) {
        switch (key) {
            case "-2":
                ancVisit.setLatitude(answer);
                break;
            case "-1":
                ancVisit.setLongitude(answer);
                break;
            case "49":
                ancVisit.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "50":
                ancVisit.setMemberStatus(answer);
                if (answer.equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setIsAlive(Boolean.FALSE);
                } else {
                    ancVisit.setIsAlive(Boolean.TRUE);
                }
                break;
            case "53":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "54":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setDeathPlace(answer);
                }
                break;
            case "55":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    if (answer.equals("OTHER")) {
                        ancVisit.setDeathReason("-1");
                    } else if (!answer.equals("NONE")) {
                        ancVisit.setDeathReason(answer);
                    }
                }
                break;
            case "57":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)
                        && keyAndAnswersMap.get("55").equals(RchConstants.DEATH_REASON_OTHER)) {
                    ancVisit.setOtherDeathReason(answer);
                }
                break;
            case "115":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFamilyUnderstands(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "119":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setTrainedDaiIntroduced(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "121":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setPlannedDeliveryAt(answer);
                }
                break;
            case "122":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setExpectedDeliveryPlace(answer);
                }
                break;
            case "123":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setMoneyArrangements(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "125":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setAccompanyIdentified(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "129":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setIfaTabletsTaken(Integer.valueOf(answer));
                }
                break;
            case "131":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFamilyUnderstandsDangerSigns(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "136":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setHeadache(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "137":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setVisualDisturbances(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "138":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setConvulsions(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "139":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setCough(answer);
                }
                break;
            case "140":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFever(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "141":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setChillsRigors(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "142":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setJaundice(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "143":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setSevereJointPain(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "145":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setVomitting(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "146":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setVaginalBleeding(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "147":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setBurningUrination(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "148":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setVaginalDischarge(answer);
                }
                break;
            case "149":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setLeakingPerVaginally(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "150":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setEasilyTired(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "151":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setShortOfBreath(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "152":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setSwellingFeetHandsFace(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "154":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    if (answer.startsWith("T")) {
                        ancVisit.setVisitedDoctorSinceLastVisit(Boolean.TRUE);
                        String[] split = answer.split("/");
                        if (split[1] != null) {
                            ancVisit.setDoctorVisitDate(new Date(Long.parseLong(split[1])));
                        }
                    } else {
                        ancVisit.setVisitedDoctorSinceLastVisit(Boolean.TRUE);
                    }
                }
                break;
            case "155":
                if (keyAndAnswersMap.get("50").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setConjuctivaAndPalmsPale(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            default:
        }
    }
}
