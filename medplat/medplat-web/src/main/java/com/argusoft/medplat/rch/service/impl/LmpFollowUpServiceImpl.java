package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.LmpFollowUpVisitDao;
import com.argusoft.medplat.rch.model.LmpFollowUpVisit;
import com.argusoft.medplat.rch.service.LmpFollowUpService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * <p>
 *     Define services for lmp follow up.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
@Service
@Transactional
public class LmpFollowUpServiceImpl implements LmpFollowUpService {

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private LmpFollowUpVisitDao lmpFollowUpVisitDao;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private MobileFhsService mobileFhsService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeLmpFollowUpForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity memberEntity = memberDao.retrieveById(memberId);

        if (memberEntity.getState() != null && FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_DEAD.contains(memberEntity.getState())) {
            throw new ImtechoMobileException("Member is already marked as dead.", 1);
        }
        if (memberEntity.getIsPregnantFlag() != null && memberEntity.getIsPregnantFlag()) {
            throw new ImtechoMobileException("Member is already marked as pregnant.", 1);
        }

        if (keyAndAnswerMap.get("9057") != null && !keyAndAnswerMap.get("9057").isEmpty()) {
            FamilyEntity family = familyDao.retrieveById(familyId);
            family.setAnganwadiId(Integer.valueOf(keyAndAnswerMap.get("9057")));
            family.setAnganwadiUpdateFlag(Boolean.FALSE);
            familyDao.update(family);
        }

        if (keyAndAnswerMap.get("65") != null && !keyAndAnswerMap.get("65").isEmpty()) {
            memberEntity.setMobileNumber(keyAndAnswerMap.get("65"));
            memberDao.update(memberEntity);
        }

        LmpFollowUpVisit lmpFollowUpVisit = new LmpFollowUpVisit();
        lmpFollowUpVisit.setMemberId(memberId);
        lmpFollowUpVisit.setFamilyId(familyId);
        lmpFollowUpVisit.setLocationId(locationId);

        if ((keyAndAnswerMap.get("-8")) != null && !keyAndAnswerMap.get("-8").equals("null")) {
            lmpFollowUpVisit.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        } else {
            lmpFollowUpVisit.setMobileStartDate(new Date(0L));
        }
        if ((keyAndAnswerMap.get("-9")) != null && !keyAndAnswerMap.get("-9").equals("null")) {
            lmpFollowUpVisit.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        } else {
            lmpFollowUpVisit.setMobileEndDate(new Date(0L));
        }

        lmpFollowUpVisit.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));
        lmpFollowUpVisit.setIsPregnant(Boolean.FALSE);

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToLmpFollowUpVisitEntity(key, answer, lmpFollowUpVisit, keyAndAnswerMap, memberEntity);
        }

        if (lmpFollowUpVisit.getMemberStatus().equals(RchConstants.MEMBER_STATUS_DEATH)) {
            mobileFhsService.checkIfMemberDeathEntryExists(memberId);
        }

        if (lmpFollowUpVisit.getServiceDate() != null && lmpFollowUpVisit.getLmp() != null && Boolean.TRUE.equals(lmpFollowUpVisit.getIsPregnant())) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(lmpFollowUpVisit.getServiceDate());
            instance.add(Calendar.MONTH, -9);
            if (ImtechoUtil.clearTimeFromDate(lmpFollowUpVisit.getLmp()).before(ImtechoUtil.clearTimeFromDate(instance.getTime()))) {
                throw new ImtechoMobileException("LMP date can’t be before 9 months from service date", 100);
            }

            instance.setTime(lmpFollowUpVisit.getServiceDate());
            instance.add(Calendar.DATE, -28);
            if (ImtechoUtil.clearTimeFromDate(lmpFollowUpVisit.getLmp()).after(ImtechoUtil.clearTimeFromDate(instance.getTime()))) {
                throw new ImtechoMobileException("LMP date can’t be within last 28 days from service date", 100);
            }
        }

        lmpFollowUpVisitDao.create(lmpFollowUpVisit);
        this.updateMemberAdditionalInfo(memberEntity, lmpFollowUpVisit);
        if (keyAndAnswerMap.get("9996") != null) {
            //Setting Children for this Mother
            String answer = keyAndAnswerMap.get("9996");
            List<Integer> childIds = new ArrayList<>();
            if (answer.contains(",")) {
                String[] split = answer.split(",");
                for (String s : split) {
                    childIds.add(Integer.valueOf(s));
                }
            } else {
                childIds.add(Integer.valueOf(answer));
            }
            memberDao.updateMotherIdInChildren(memberEntity.getId(), childIds);
        }
        lmpFollowUpVisitDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_LMPFU, lmpFollowUpVisit.getId()));
        return lmpFollowUpVisit.getId();
    }

    /**
     * Set answer to lmp follow up visit details.
     * @param key Key.
     * @param answer Answer for member's lmp follow details.
     * @param keyAndAnswerMap Contains key and answers.
     * @param lmpFollowUpVisit Lmp follow up visit details.
     * @param memberEntity Member details.
     */
    private void setAnswersToLmpFollowUpVisitEntity(String key, String answer, LmpFollowUpVisit lmpFollowUpVisit, Map<String, String> keyAndAnswerMap, MemberEntity memberEntity) {
        switch (key) {
            case "-2":
                lmpFollowUpVisit.setLatitude(answer);
                break;
            case "-1":
                lmpFollowUpVisit.setLongitude(answer);
                break;
            case "29":
                lmpFollowUpVisit.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "22":
                if (answer.length() == 2) {
                    Date dob = memberEntity.getDob();
                    if (dob != null) {
                        Calendar instance = Calendar.getInstance();
                        instance.setTime(dob);
                        int yob = instance.get(Calendar.YEAR);
                        int yom = yob + Integer.parseInt(answer);
                        lmpFollowUpVisit.setYear(Short.valueOf(Integer.toString(yom)));
                    }
                }
                if (answer.length() == 4) {
                    lmpFollowUpVisit.setYear(Short.valueOf(answer));
                }
                break;
            case "222":
                lmpFollowUpVisit.setDateOfWedding(new Date(Long.parseLong(answer)));
                break;
            case "220":
                Date dob = memberEntity.getDob();
                if (dob != null) {
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(dob);
                    int yob = instance.get(Calendar.YEAR);
                    int yom = yob + Integer.parseInt(answer);
                    lmpFollowUpVisit.setYear(Short.valueOf(Integer.toString(yom)));
                }
                break;
            case "30":
                lmpFollowUpVisit.setMemberStatus(answer);
                break;
            case "4":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("1")) {
                    lmpFollowUpVisit.setLmp(new Date(Long.parseLong(answer)));
                }
                break;
            case "42":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("1") && keyAndAnswerMap.get("41").equals("1")) {
                    lmpFollowUpVisit.setFamilyPlanningMethod(answer);
                }
                break;
            case "421":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    lmpFollowUpVisit.setFpInsertOperateDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "5":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("2")) {
                    lmpFollowUpVisit.setPregnancyTestDone(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "6":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("2") && keyAndAnswerMap.get("5").equals("1")) {
                    lmpFollowUpVisit.setIsPregnant(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "63":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("2") && keyAndAnswerMap.get("5").equals("1")
                        && keyAndAnswerMap.get("6").equals("1")) {
                    lmpFollowUpVisit.setLmp(new Date(Long.parseLong(answer)));
                }
                break;
            case "65":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("2") && keyAndAnswerMap.get("5").equals("1") && keyAndAnswerMap.get("6").equals("1")) {
                    lmpFollowUpVisit.setPhoneNumber(answer);
                }
                break;
            case "7":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("3").equals("2") && keyAndAnswerMap.get("5").equals("1") && keyAndAnswerMap.get("6").equals("1")) {
                    lmpFollowUpVisit.setRegisterNowForPregnancy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "2602":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    lmpFollowUpVisit.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "2603":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    lmpFollowUpVisit.setPlaceOfDeath(answer);
                }
                break;
            case "26":
                if (keyAndAnswerMap.get("30").equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    if (answer.equals("OTHER")) {
                        lmpFollowUpVisit.setDeathReason("-1");
                    } else if (!answer.equals("NONE")) {
                        lmpFollowUpVisit.setDeathReason(answer);
                    }
                }
                break;
            case "2605":
                if (keyAndAnswerMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)
                        && keyAndAnswerMap.get("26") != null && keyAndAnswerMap.get("26").equals("OTHER")) {
                    lmpFollowUpVisit.setOtherDeathReason(answer);
                }
                break;
            case "2607":
                lmpFollowUpVisit.setDeathInfrastructureId(Integer.valueOf(answer));
                break;

            case "69":
                lmpFollowUpVisit.setCurrentGravida(Short.parseShort(answer));
                break;

            case "70":
                lmpFollowUpVisit.setCurrentPara(Short.parseShort(answer));
                break;
            default:
        }
    }

    /**
     * Update additional info for anc.
     * @param memberEntity Member details.
     * @param lmpVisit Lmp visit details.
     */
    private void updateMemberAdditionalInfo(MemberEntity memberEntity, LmpFollowUpVisit lmpVisit) {
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;

        if (lmpVisit.getServiceDate() != null) {
            if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
                memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
            } else {
                memberAdditionalInfo = new MemberAdditionalInfo();
            }
            memberAdditionalInfo.setLastServiceLongDate(lmpVisit.getServiceDate().getTime());
            memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            memberDao.update(memberEntity);
        }
    }
}
