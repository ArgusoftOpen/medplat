/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.listvalues.service.ListValueFieldValueDetailService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AncVisitDao;
import com.argusoft.medplat.rch.dto.AncMasterDto;
import com.argusoft.medplat.rch.dto.ImmunisationDto;
import com.argusoft.medplat.rch.mapper.AncMapper;
import com.argusoft.medplat.rch.model.AncVisit;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.service.AncService;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Define services for anc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AncServiceImpl implements AncService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private AncVisitDao ancVisitDao;

    @Autowired
    private ImmunisationService immunisationService;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private HealthInfrastructureDetailsDao healthInfrastructureDetailsDao;

    @Autowired
    private ListValueFieldValueDetailService listValueFieldValueDetailService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAncVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId;
        Integer locationId = null;
        if (!keyAndAnswerMap.get("-5").equals("null")) {
            familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        } else {
            FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberDao.retrieveById(memberId).getFamilyId());
            familyId = familyEntity.getId();
            if (keyAndAnswerMap.get("-6").equals("null")) {
                locationId = familyEntity.getLocationId();
            }
        }

        if (locationId == null) {
            locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        }
        MemberEntity motherEntity = memberDao.retrieveById(memberId);
        Integer pregnancyRegDetId = null;

        if (keyAndAnswerMap.get("-7") != null && !keyAndAnswerMap.get("-7").equals("null")) {
            pregnancyRegDetId = Integer.valueOf(keyAndAnswerMap.get("-7"));
        }

        if (motherEntity.getState() != null
                && FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_DEAD.contains(motherEntity.getState())) {
            throw new ImtechoMobileException("Member is already marked as dead.", 1);
        }

        if (motherEntity.getIsPregnantFlag() == null || !motherEntity.getIsPregnantFlag()) {
            throw new ImtechoMobileException("Member is not marked as pregnant.", 1);
        }

        if (motherEntity.getCurPregRegDetId() != null) {
            pregnancyRegDetId = motherEntity.getCurPregRegDetId();
        }

        if (pregnancyRegDetId == null && ConstantUtil.DROP_TYPE.equals("P")) {
            if (motherEntity.getState().equals(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED)
                    || FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.contains(motherEntity.getState())) {
                throw new ImtechoMobileException("Member is not verified. Please Verified thru FHS.", 1);
            }
            throw new ImtechoUserException("Pregnancy Registration Details has not been generated yet.", 1);
        }

        if (motherEntity.getLmpDate() != null && keyAndAnswerMap.get("301") != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(motherEntity.getLmpDate());
            calendar.add(Calendar.DATE, 91);
            Date newLmp = new Date(Long.parseLong(keyAndAnswerMap.get("301")));
            if (newLmp.after(calendar.getTime())) {
                throw new ImtechoMobileException("Sorry, LMP Date in ANC Form should be changed only till 91 days.", 1);
            }
        }

        if (keyAndAnswerMap.get("801") != null && !keyAndAnswerMap.get("801").isEmpty()) {
            FamilyEntity family = familyDao.retrieveById(familyId);
            family.setAnganwadiId(Integer.valueOf(keyAndAnswerMap.get("801")));
            family.setAnganwadiUpdateFlag(Boolean.FALSE);
            familyDao.update(family);
        } else if (keyAndAnswerMap.get("9057") != null && !keyAndAnswerMap.get("9057").isEmpty()) {
            FamilyEntity family = familyDao.retrieveById(familyId);
            family.setAnganwadiId(Integer.valueOf(keyAndAnswerMap.get("9057")));
            family.setAnganwadiUpdateFlag(Boolean.FALSE);
            familyDao.update(family);
        }

        if (keyAndAnswerMap.get("511") != null && keyAndAnswerMap.get("511").equalsIgnoreCase("2")) {
            List<String> split = Arrays.asList(keyAndAnswerMap.get("512").split(","));

            List<MemberEntity> childMembers = memberDao.getChildMembersByMotherId(memberId, null);
            if (!childMembers.isEmpty()) {
                for (MemberEntity memberEntity : childMembers) {
                    memberEntity.setMotherId(null);
                    memberDao.update(memberEntity);
                }
            }

            for (String childId : split) {
                if (!childId.equals("ADDNEW") && !childId.equals("")) {
                    MemberEntity childMember = memberDao.retrieveById(Integer.valueOf(childId));
                    childMember.setMotherId(memberId);
                }
            }

            if (split.contains("ADDNEW")) {
                Map<String, String> newChildKeyAndAnswerMap = new HashMap<>();
                List<String> keysForNewChildMembers = this.getKeysForNewChildMembers();
                for (Map.Entry<String, String> entry : keyAndAnswerMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key.contains(".")) {
                        String[] split1 = key.split("\\.");
                        if (keysForNewChildMembers.contains(split1[0])) {
                            newChildKeyAndAnswerMap.put(key, value);
                        }
                    }
                    if (keysForNewChildMembers.contains(key)) {
                        newChildKeyAndAnswerMap.put(key, value);
                    }
                }
                Map<String, MemberEntity> mapOfNewChildWithLoopIdAsKey = new HashMap<>();

                for (Map.Entry<String, String> entry : newChildKeyAndAnswerMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    MemberEntity memberEntity;
                    if (key.contains(".")) {
                        String[] splitKey = key.split("\\.");
                        memberEntity = mapOfNewChildWithLoopIdAsKey.get(splitKey[1]);
                        if (memberEntity == null) {
                            memberEntity = new MemberEntity();
                            memberEntity.setMotherId(memberId);
                            memberEntity.setFamilyId(memberDao.retrieveById(memberId).getFamilyId());
                            memberEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW);
                            memberEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                            mapOfNewChildWithLoopIdAsKey.put(splitKey[1], memberEntity);
                        }
                        this.setAnswersToNewChildMembers(splitKey[0], value, memberEntity);
                    } else {
                        memberEntity = mapOfNewChildWithLoopIdAsKey.get("0");
                        if (memberEntity == null) {
                            memberEntity = new MemberEntity();
                            memberEntity.setMotherId(memberId);
                            memberEntity.setFamilyId(memberDao.retrieveById(memberId).getFamilyId());
                            memberEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW);
                            memberEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                            mapOfNewChildWithLoopIdAsKey.put("0", memberEntity);
                        }
                        this.setAnswersToNewChildMembers(key, value, memberEntity);
                    }
                }

                for (Map.Entry<String, MemberEntity> entry : mapOfNewChildWithLoopIdAsKey.entrySet()) {
                    MemberEntity value = entry.getValue();
                    memberDao.create(value);
                }
            }
        }

        AncVisit ancVisit = new AncVisit();
        ancVisit.setMemberId(memberId);
        ancVisit.setFamilyId(familyId);
        ancVisit.setLocationId(locationId);
        ancVisit.setPregnancyRegDetId(pregnancyRegDetId);
        if (keyAndAnswerMap.get("-8") != null && !keyAndAnswerMap.get("-8").equals("null")) {
            ancVisit.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        } else {
            ancVisit.setMobileStartDate(new Date(0L));
        }
        if (keyAndAnswerMap.get("-9") != null && !keyAndAnswerMap.get("-9").equals("null")) {
            ancVisit.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        } else {
            ancVisit.setMobileEndDate(new Date(0L));
        }
        ancVisit.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToAncVisitEntity(key, answer, ancVisit, keyAndAnswerMap);
        }

        ancVisit.setIsHighRiskCase(this.identifyHighRiskForRchAnc(ancVisit));
        motherEntity.setIsHighRiskCase(ancVisit.getIsHighRiskCase());

        if (ancVisit.getMemberStatus().equals(RchConstants.MEMBER_STATUS_DEATH)) {
            mobileFhsService.checkIfMemberDeathEntryExists(memberId);
        }
        ancVisitDao.create(ancVisit);

        StringBuilder immunisationGiven = new StringBuilder();

        if (keyAndAnswerMap.containsKey("2207") && keyAndAnswerMap.get("2207").equals("1")) {
            Date givenDate = new Date(Long.parseLong(keyAndAnswerMap.get("2208")));
            ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, memberId, MobileConstantUtil.MOTHER_BENEFICIARY,
                    MobileConstantUtil.ANC_VISIT, ancVisit.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                    MobileConstantUtil.IMMUNISATION_TT_BOOSTER, givenDate, user.getId(), locationId, pregnancyRegDetId);
            if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                immunisationService.createImmunisationMaster(immunisationMaster);
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_TT_BOOSTER);
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                immunisationGiven.append(sdf.format(givenDate));
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
            }
        }

        if (keyAndAnswerMap.containsKey("2204") && keyAndAnswerMap.get("2204").equals("1")) {
            Date givenDate = new Date(Long.parseLong(keyAndAnswerMap.get("2205")));
            ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, memberId, MobileConstantUtil.MOTHER_BENEFICIARY,
                    MobileConstantUtil.ANC_VISIT, ancVisit.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                    MobileConstantUtil.IMMUNISATION_TT_2, givenDate, user.getId(), locationId, pregnancyRegDetId);
            if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                immunisationService.createImmunisationMaster(immunisationMaster);
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_TT_2);
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                immunisationGiven.append(sdf.format(givenDate));
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
            }
        }

        if (keyAndAnswerMap.containsKey("22") && keyAndAnswerMap.get("22").equals("1")) {
            Date givenDate = new Date(Long.parseLong(keyAndAnswerMap.get("221")));
            ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, memberId, MobileConstantUtil.MOTHER_BENEFICIARY,
                    MobileConstantUtil.ANC_VISIT, ancVisit.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                    MobileConstantUtil.IMMUNISATION_TT_1, givenDate, user.getId(), locationId, pregnancyRegDetId);
            if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                immunisationService.createImmunisationMaster(immunisationMaster);
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_TT_1);
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                immunisationGiven.append(sdf.format(givenDate));
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
            }
        }

        if (immunisationGiven.length() > 1) {
            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
            if (motherEntity.getImmunisationGiven() != null && motherEntity.getImmunisationGiven().length() > 0) {
                String sb = motherEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                motherEntity.setImmunisationGiven(sb.replace(" ", ""));
            } else {
                String immunisation = immunisationGiven.toString().replace(" ", "");
                motherEntity.setImmunisationGiven(immunisation);
            }
        }

        String isAadharScanned = keyAndAnswerMap.get("9003");
        String aadharMap = null;
        String aadharNumber = null;
        if (isAadharScanned != null) {
            if (isAadharScanned.equals("1")) {
                aadharMap = keyAndAnswerMap.get("9004");
            } else {
                String answer = keyAndAnswerMap.get("9005");
                if (answer != null && !answer.equals("T")) {
                    aadharNumber = answer.replace("F/", "");
                }
            }
        }

        this.updateMemberAdditionalInfoFromAnc(motherEntity, ancVisit);
        mobileFhsService.updateMemberDetailsFromRchForms(keyAndAnswerMap.get("9002"), aadharMap, aadharNumber,
                keyAndAnswerMap.get("9008"), keyAndAnswerMap.get("9010"), motherEntity);
        ancVisitDao.flush();
        eventHandler.handle(
                new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_ANC, ancVisit.getId()));
        return 1;
    }

    /**
     * Set answer to anc visit entity.
     *
     * @param key              Key.
     * @param answer           Answer for member's anc visit details.
     * @param ancVisit         Anc visit details.
     * @param keyAndAnswersMap Contains key and answers.
     */
    private void setAnswersToAncVisitEntity(String key, String answer, AncVisit ancVisit, Map<String, String> keyAndAnswersMap) {
        switch (key) {
            case "-2":
                ancVisit.setLatitude(answer);
                break;
            case "-1":
                ancVisit.setLongitude(answer);
                break;
            case "29":
                ancVisit.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "30":
                ancVisit.setMemberStatus(answer);
                break;
            case "301":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setLmp(new Date(Long.parseLong(answer)));
                }
                break;
            case "304":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setBloodGroup(answer);
                }
                break;
            case "503":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setLastDeliveryOutcome(answer);
                }
                break;
            case "501":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    Set<String> previousPregnancyComplicationIdsSet = new HashSet<>();
                    String[] previousPregnancyComplicationIdsArray = answer.split(",");
                    for (String previousPregnancyComplicationId : previousPregnancyComplicationIdsArray) {
                        if (!previousPregnancyComplicationId.equalsIgnoreCase(RchConstants.PREVIOUS_PREGNANCY_COMPLICATION_NONE)) {
                            if (previousPregnancyComplicationId.equalsIgnoreCase(RchConstants.PREVIOUS_PREGNANCY_COMPLICATION_OTHER)) {
                                ancVisit.setOtherPreviousPregnancyComplication(keyAndAnswersMap.get("505"));
                            } else {
                                previousPregnancyComplicationIdsSet.add(previousPregnancyComplicationId);
                            }
                        }
                    }
                    ancVisit.setPreviousPregnancyComplication(previousPregnancyComplicationIdsSet);
                }
                break;
            case "31":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setJsyBeneficiary(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "313":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setJsyPaymentDone(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "32":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setKpsyBeneficiary(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "33":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setIayBeneficiary(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "34":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setChiranjeeviYojnaBeneficiary(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "4":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setAncPlace(Integer.valueOf(answer));
                }
                break;
            case "41":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setDeliveryPlace(answer);
                    if (answer.equals(RchConstants.DELIVERY_PLACE_PRIVATE_HOSPITAL)) {
                        ancVisit.setDeliveryPlace(RchConstants.DELIVERY_PLACE_HOSPITAL);
                        ancVisit.setTypeOfHospital(893);
                    }
                }
                break;
            case "42":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("41").equalsIgnoreCase(RchConstants.DELIVERY_PLACE_HOSPITAL)) {
                    if (answer.equals("-1")) {
                        ancVisit.setHealthInfrastructureId(-1);
                        ancVisit.setTypeOfHospital(1013);// 1013 is the Health Infra Type ID for Private Hospital.
                    } else {
                        HealthInfrastructureDetails infra = healthInfrastructureDetailsDao.retrieveById(Integer.valueOf(answer));
                        ancVisit.setHealthInfrastructureId(infra.getId());
                        ancVisit.setTypeOfHospital(infra.getType());
                    }
                }
                break;
            case "6":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setWeight(Float.valueOf(answer));
                }
                break;
            case "7":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setHaemoglobinCount(Float.valueOf(answer));
                }
                break;
            case "8":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    String[] arr = answer.split("-");
                    if (arr.length > 1) {
                        ancVisit.setSystolicBp(Integer.valueOf(arr[1].split("\\.")[0]));
                        ancVisit.setDiastolicBp(Integer.valueOf(arr[2].split("\\.")[0]));
                    }
                }
                break;
            case "81":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setMemberHeight(Integer.valueOf(answer.split("\\.")[0]));
                }
                break;
            case "9":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFoetalMovement(answer);
                }
                break;
            case "10":
                ancVisit.setFoetalHeight(Integer.valueOf(answer.split("\\.")[0]));
                break;
            case "11":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFoetalHeartSound(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "12":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFoetalPosition(answer);
                }
                break;
            case "13":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setIfaTabletsGiven(Integer.valueOf(answer.split("\\.")[0]));
                }
                break;
            case "14":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFaTabletsGiven(Integer.valueOf(answer.split("\\.")[0]));
                }
                break;
            case "15":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setCalciumTabletsGiven(Integer.valueOf(answer.split("\\.")[0]));
                }
                break;
            case "16":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setHbsagTest(answer);
                }
                break;
            case "17":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setBloodSugarTest(answer);
                }
                break;
            case "18":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    if (keyAndAnswersMap.get("17").equalsIgnoreCase(RchConstants.BLOOD_SUGAR_TEST_EMPTY)) {
                        ancVisit.setSugarTestBeforeFoodValue(Integer.valueOf(answer.split("\\.")[0]));
                    } else if (keyAndAnswersMap.get("17").equalsIgnoreCase(RchConstants.BLOOD_SUGAR_TEST_NON_EMPTY)) {
                        ancVisit.setSugarTestAfterFoodValue(Integer.valueOf(answer.split("\\.")[0]));
                    }
                }
                break;
            case "19":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setUrineTestDone(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "20":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE) &&
                        keyAndAnswersMap.get("19").equalsIgnoreCase(RchConstants.TRUE)) {
                    ancVisit.setUrineAlbumin(answer);
                }
                break;
            case "201":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE) &&
                        keyAndAnswersMap.get("19").equalsIgnoreCase(RchConstants.TRUE)) {
                    ancVisit.setUrineSugar(answer);
                }
                break;
            case "202":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setVdrlTest(answer);
                }
                break;
            case "203":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setHivTest(answer);
                }
                break;
            case "21":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setAlbendazoleGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "23":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE) &&
                        !answer.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                    Set<Integer> dangerousSignIdsSet = new HashSet<>();
                    String[] dangerousSignIdsArray = answer.split(",");
                    for (String dangerousSignId : dangerousSignIdsArray) {
                        if (dangerousSignId.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_OTHER)) {
                            ancVisit.setOtherDangerousSign(keyAndAnswersMap.get("92"));
                        } else {
                            dangerousSignIdsSet.add(Integer.valueOf(dangerousSignId));
                        }
                    }
                    ancVisit.setDangerousSignIds(dangerousSignIdsSet);
                }
                break;
            case "9997":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    if (!answer.equalsIgnoreCase(RchConstants.NO_RISK_FOUND)) {
                        ancVisit.setIsHighRiskCase(Boolean.TRUE);
                    } else {
                        ancVisit.setIsHighRiskCase(Boolean.FALSE);
                    }
                }
                break;
            case "24":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE) &&
                        !keyAndAnswersMap.get("23").equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                    switch (answer) {
                        case "1":
                            ancVisit.setReferralDone(RchConstants.REFFERAL_DONE_YES);
                            break;
                        case "2":
                            ancVisit.setReferralDone(RchConstants.REFFERAL_DONE_NO);
                            break;
                        case "3":
                            ancVisit.setReferralDone(RchConstants.REFFERAL_DONE_NOT_REQUIRED);
                            break;
                        default:
                    }
                }
                break;
            case "25":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE) &&
                        keyAndAnswersMap.get("24").equalsIgnoreCase("1")) {
                    ancVisit.setReferralPlace(Integer.valueOf(answer));
                }
                break;
            case "2501":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setExpectedDeliveryPlace(answer);
                }
                break;
            case "2502":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    ancVisit.setFamilyPlanningMethod(answer);
                }
                break;
            case "2601":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setDeadFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "2602":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "2603":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setPlaceOfDeath(answer);
                }
                break;
            case "2607":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    ancVisit.setDeathInfrastructureId(Integer.valueOf(answer));
                }
                break;
            case "26":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
                    if (answer.equals("OTHER")) {
                        ancVisit.setDeathReason("-1");
                    } else if (!answer.equals("NONE")) {
                        ancVisit.setDeathReason(answer);
                    }
                }
                break;
            case "2605":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH) &&
                        keyAndAnswersMap.get("26").equals("OTHER")) {
                    ancVisit.setOtherDeathReason(answer);
                }
                break;
            case "2801":
                if (keyAndAnswersMap.get("30").equalsIgnoreCase(RchConstants.MEMBER_STATUS_WRONGLY_REGISTERED)) {
                    ancVisit.setLmp(new Date(Long.parseLong(answer)));
                }
                break;
            case "204":
                ancVisit.setSickleCellTest(answer);
                break;
            default:
        }
    }

    /**
     * Set answer to new child members entity.
     *
     * @param key          Key.
     * @param answer       Answer for member's new child details.
     * @param memberEntity New child member entity.
     */
    private void setAnswersToNewChildMembers(String key, String answer, MemberEntity memberEntity) {
        switch (key) {
            case "514":
                memberEntity.setFirstName(answer);
                break;
            case "516":
                memberEntity.setMiddleName(answer);
                break;
            case "517":
                memberEntity.setLastName(answer);
                break;
            case "518":
                memberEntity.setEmamtaHealthId(answer);
                break;
            case "5181":
                memberEntity.setGender(answer);
                break;
            case "519":
                memberEntity.setDob(new Date(Long.parseLong(answer)));
                break;
            default:
        }

    }

    /**
     * Retrieves keys for new child members.
     *
     * @return Returns keys.
     */
    private List<String> getKeysForNewChildMembers() {
        List<String> keys = new ArrayList<>();
        keys.add("514");
        keys.add("516");
        keys.add("517");
        keys.add("518");
        keys.add("5181");
        keys.add("519");
        return keys;
    }

    /**
     * Check for high risk in rch anc members.
     *
     * @param ancVisit Anc visit details.
     * @return Returns true/false based on high risk.
     */
    private Boolean identifyHighRiskForRchAnc(AncVisit ancVisit) {
        return (ancVisit.getSystolicBp() != null && ancVisit.getSystolicBp() > 139)
                || (ancVisit.getDiastolicBp() != null && ancVisit.getDiastolicBp() > 89)
                || (ancVisit.getHaemoglobinCount() != null && ancVisit.getHaemoglobinCount() <= 7f)
                || (ancVisit.getWeight() != null && ancVisit.getWeight() < 45f)
                || (ancVisit.getUrineAlbumin() != null && !ancVisit.getUrineAlbumin().equals("0"))
                || (ancVisit.getUrineSugar() != null && !ancVisit.getUrineSugar().equals("0"))
                || ancVisit.getDangerousSignIds() != null
                || ancVisit.getPreviousPregnancyComplication() != null && !ancVisit.getPreviousPregnancyComplication().isEmpty()
                || ancVisit.getOtherDangerousSign() != null;
    }

    /**
     * Update additional info for anc.
     *
     * @param memberEntity Member details.
     * @param ancVisit     Anc details.
     */
    private void updateMemberAdditionalInfoFromAnc(MemberEntity memberEntity, AncVisit ancVisit) {
        Gson gson = new Gson();
        if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
            boolean isUpdate = false;
            MemberAdditionalInfo memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);

            if (ancVisit.getMemberHeight() != null) {
                memberAdditionalInfo.setHeight(ancVisit.getMemberHeight());
                isUpdate = true;
            }

            if (ancVisit.getWeight() != null) {
                memberAdditionalInfo.setWeight(ancVisit.getWeight());
                isUpdate = true;
            }

            if (ancVisit.getHaemoglobinCount() != null) {
                memberAdditionalInfo.setHaemoglobin(ancVisit.getHaemoglobinCount());
                isUpdate = true;
            }

            if (ancVisit.getSystolicBp() != null) {
                memberAdditionalInfo.setSystolicBp(ancVisit.getSystolicBp());
                isUpdate = true;
            }

            if (ancVisit.getDiastolicBp() != null) {
                memberAdditionalInfo.setDiastolicBp(ancVisit.getDiastolicBp());
                isUpdate = true;
            }

            if (ancVisit.getBloodSugarTest() != null) {
                memberAdditionalInfo.setAncBloodSugarTest(ancVisit.getBloodSugarTest());
                isUpdate = true;
            }

            if (ancVisit.getSugarTestAfterFoodValue() != null) {
                memberAdditionalInfo.setSugarTestAfterFoodValue(ancVisit.getSugarTestAfterFoodValue());
                isUpdate = true;
            }

            if (ancVisit.getSugarTestBeforeFoodValue() != null) {
                memberAdditionalInfo.setSugarTestBeforeFoodValue(ancVisit.getSugarTestBeforeFoodValue());
                isUpdate = true;
            }

            if (ancVisit.getHbsagTest() != null) {
                memberAdditionalInfo.setHbsagTest(ancVisit.getHbsagTest());
                isUpdate = true;
            }
            if (ancVisit.getHivTest() != null) {
                memberAdditionalInfo.setHivTest(ancVisit.getHivTest());
                isUpdate = true;
            }

            if (ancVisit.getVdrlTest() != null) {
                memberAdditionalInfo.setVdrlTest(ancVisit.getVdrlTest());
                isUpdate = true;
            }

            if (ancVisit.getSickleCellTest() != null) {
                memberAdditionalInfo.setSickleCellTest(ancVisit.getSickleCellTest());
                isUpdate = true;
            }

            if (ancVisit.getAlbendazoleGiven() != null) {
                memberAdditionalInfo.setAlbendanzoleGiven(ancVisit.getAlbendazoleGiven());
                isUpdate = true;
            }

            if (ancVisit.getIfaTabletsGiven() != null && ancVisit.getIfaTabletsGiven() > 0) {
                memberAdditionalInfo.setAncIfa(ancVisit.getIfaTabletsGiven());
                isUpdate = true;
            }

            if (ancVisit.getFaTabletsGiven() != null && ancVisit.getFaTabletsGiven() > 0) {
                memberAdditionalInfo.setAncFa(ancVisit.getFaTabletsGiven());
                isUpdate = true;
            }

            if (ancVisit.getCalciumTabletsGiven() != null && ancVisit.getCalciumTabletsGiven() > 0) {
                memberAdditionalInfo.setAncCalcium(ancVisit.getCalciumTabletsGiven());
                isUpdate = true;
            }

            if (ancVisit.getExpectedDeliveryPlace() != null) {
                memberAdditionalInfo.setExpectedDeliveryPlace(ancVisit.getExpectedDeliveryPlace());
                isUpdate = true;
            }

            if (ancVisit.getPreviousPregnancyComplication() != null) {
                memberAdditionalInfo.setPreviousPregnancyComplication(ancVisit.getPreviousPregnancyComplication());
                isUpdate = true;
            }

            if (ancVisit.getServiceDate() != null) {
                memberAdditionalInfo.setLastServiceLongDate(ancVisit.getServiceDate().getTime());
                isUpdate = true;
            }

            String highRiskReasonString = getHighRiskReasonString(ancVisit);
            if (highRiskReasonString != null) {
                memberAdditionalInfo.setHighRiskReasons(highRiskReasonString);
                isUpdate = true;
            }

            if (isUpdate) {
                memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            }
        } else {
            MemberAdditionalInfo memberAdditionalInfo = new MemberAdditionalInfo();
            boolean isUpdate = false;
            if (ancVisit.getMemberHeight() != null) {
                memberAdditionalInfo.setHeight(ancVisit.getMemberHeight());
                isUpdate = true;
            }

            if (ancVisit.getWeight() != null) {
                memberAdditionalInfo.setWeight(ancVisit.getWeight());
                isUpdate = true;
            }

            if (ancVisit.getHaemoglobinCount() != null) {
                memberAdditionalInfo.setHaemoglobin(ancVisit.getHaemoglobinCount());
                isUpdate = true;
            }

            if (ancVisit.getSystolicBp() != null) {
                memberAdditionalInfo.setSystolicBp(ancVisit.getSystolicBp());
                isUpdate = true;
            }

            if (ancVisit.getDiastolicBp() != null) {
                memberAdditionalInfo.setDiastolicBp(ancVisit.getDiastolicBp());
                isUpdate = true;
            }

            if (ancVisit.getBloodSugarTest() != null) {
                memberAdditionalInfo.setAncBloodSugarTest(ancVisit.getBloodSugarTest());
                isUpdate = true;
            }

            if (ancVisit.getSugarTestAfterFoodValue() != null) {
                memberAdditionalInfo.setSugarTestAfterFoodValue(ancVisit.getSugarTestAfterFoodValue());
                isUpdate = true;
            }

            if (ancVisit.getSugarTestBeforeFoodValue() != null) {
                memberAdditionalInfo.setSugarTestBeforeFoodValue(ancVisit.getSugarTestBeforeFoodValue());
                isUpdate = true;
            }

            if (ancVisit.getHbsagTest() != null) {
                memberAdditionalInfo.setHbsagTest(ancVisit.getHbsagTest());
                isUpdate = true;
            }
            if (ancVisit.getHivTest() != null) {
                memberAdditionalInfo.setHivTest(ancVisit.getHivTest());
                isUpdate = true;
            }

            if (ancVisit.getVdrlTest() != null) {
                memberAdditionalInfo.setVdrlTest(ancVisit.getVdrlTest());
                isUpdate = true;
            }

            if (ancVisit.getSickleCellTest() != null) {
                memberAdditionalInfo.setSickleCellTest(ancVisit.getSickleCellTest());
                isUpdate = true;
            }

            if (ancVisit.getAlbendazoleGiven() != null) {
                memberAdditionalInfo.setAlbendanzoleGiven(ancVisit.getAlbendazoleGiven());
                isUpdate = true;
            }

            if (ancVisit.getIfaTabletsGiven() != null && ancVisit.getIfaTabletsGiven() > 0) {
                memberAdditionalInfo.setAncIfa(ancVisit.getIfaTabletsGiven());
                isUpdate = true;
            }

            if (ancVisit.getFaTabletsGiven() != null && ancVisit.getFaTabletsGiven() > 0) {
                memberAdditionalInfo.setAncFa(ancVisit.getFaTabletsGiven());
                isUpdate = true;
            }

            if (ancVisit.getCalciumTabletsGiven() != null && ancVisit.getCalciumTabletsGiven() > 0) {
                memberAdditionalInfo.setAncCalcium(ancVisit.getCalciumTabletsGiven());
                isUpdate = true;
            }

            if (ancVisit.getExpectedDeliveryPlace() != null) {
                memberAdditionalInfo.setExpectedDeliveryPlace(ancVisit.getExpectedDeliveryPlace());
                isUpdate = true;
            }

            if (ancVisit.getPreviousPregnancyComplication() != null) {
                memberAdditionalInfo.setPreviousPregnancyComplication(ancVisit.getPreviousPregnancyComplication());
                isUpdate = true;
            }

            if (ancVisit.getServiceDate() != null) {
                memberAdditionalInfo.setLastServiceLongDate(ancVisit.getServiceDate().getTime());
            }

            String highRiskReasonString = getHighRiskReasonString(ancVisit);
            if (highRiskReasonString != null) {
                memberAdditionalInfo.setHighRiskReasons(highRiskReasonString);
                isUpdate = true;
            }

            if (isUpdate) {
                memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            }
        }
    }

    private String getHighRiskReasonString(AncVisit ancVisit) {
        StringBuilder sb = new StringBuilder();
        if ((ancVisit.getSystolicBp() != null && ancVisit.getSystolicBp() > 139) || (ancVisit.getDiastolicBp() != null && ancVisit.getDiastolicBp() > 89)) {
            sb.append("High Blood Pressure");
        }
        if (ancVisit.getHaemoglobinCount() != null && ancVisit.getHaemoglobinCount() < 7) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("Low Haemoglobin");
        }
        if (ancVisit.getMemberHeight() != null && ancVisit.getMemberHeight() < 145) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("Low Height");
        }
        if (ancVisit.getWeight() != null && ancVisit.getWeight() < 45f) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("Very Low Weight");
        }
        if (ancVisit.getUrineAlbumin() != null && !ancVisit.getUrineAlbumin().equals("0")) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("Urine Albumin");
        }
        if (ancVisit.getUrineSugar() != null && !ancVisit.getUrineSugar().equals("0")) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("Urine Sugar");
        }
        if (ancVisit.getDangerousSignIds() != null) {
            for (Integer dSign : ancVisit.getDangerousSignIds()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(listValueFieldValueDetailService.getListValueNameFormId(dSign));
            }
        }
        if (ancVisit.getOtherDangerousSign() != null) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(ancVisit.getOtherDangerousSign());
        }
        if (ancVisit.getPreviousPregnancyComplication() != null && !ancVisit.getPreviousPregnancyComplication().isEmpty()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(ancVisit.getPreviousPregnancyComplication());
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer create(AncMasterDto ancMasterDto) {
        Integer ancServiceId;
        MemberEntity memberEntity = memberDao.retrieveMemberById(ancMasterDto.getMemberId());

        if (memberEntity.getIsPregnantFlag() != null && !memberEntity.getIsPregnantFlag()) {
            throw new ImtechoUserException("Member is not marked as pregnant.", 1);
        }

        if (memberEntity.getCurPregRegDetId() != null) {
            ancMasterDto.setPregnancyRegDetId(memberEntity.getCurPregRegDetId());
        } else {
            throw new ImtechoUserException("Pregnancy Registration Details has not been generated yet.", 1);
        }

        if (ancMasterDto.getTypeOfHospital() == null) {
            HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(ancMasterDto.getHealthInfrastructureId());
            ancMasterDto.setTypeOfHospital(healthInfrastructureDetails.getType());
        }

        AncVisit ancVisit = AncMapper.convertAncMasterDtoToAncMaster(ancMasterDto);
        ancVisit.setIsHighRiskCase(this.identifyHighRiskForRchAnc(ancVisit));
        ancVisit.setIsFromWeb(Boolean.TRUE);
        ancServiceId = ancVisitDao.create(ancVisit);

        StringBuilder immunisationGiven = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (ImmunisationDto immunisationDto : ancMasterDto.getImmunisationDetails()) {
            ImmunisationMaster immunisationMaster = new ImmunisationMaster(ancVisit.getFamilyId(), ancVisit.getMemberId(), MobileConstantUtil.MOTHER_BENEFICIARY,
                    MobileConstantUtil.ANC_VISIT, ancVisit.getId(), null,
                    immunisationDto.getImmunisationGiven().trim(), immunisationDto.getImmunisationDate(), user.getId(),
                    ancVisit.getLocationId(), ancVisit.getPregnancyRegDetId());
            if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                immunisationService.createImmunisationMaster(immunisationMaster);
                immunisationGiven.append(immunisationDto.getImmunisationGiven().trim());
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                immunisationGiven.append(sdf.format(immunisationDto.getImmunisationDate()));
                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
            }
        }

        if (immunisationGiven.length() > 1) {
            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
            if (memberEntity.getImmunisationGiven() != null && memberEntity.getImmunisationGiven().length() > 0) {
                String sb = memberEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                memberEntity.setImmunisationGiven(sb.replace(" ", ""));
            } else {
                String immunisation = immunisationGiven.toString().replace(" ", "");
                memberEntity.setImmunisationGiven(immunisation);
            }
        }
        this.updateMemberAdditionalInfoFromAnc(memberEntity, ancVisit);
        mobileFhsService.updateMemberDetailsFromRchForms(ancMasterDto.getMobileNumber(), null, null,
                ancMasterDto.getAccountNumber(), ancMasterDto.getIfsc(), memberEntity);
        ancVisitDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_ANC, ancVisit.getId()));
        return ancServiceId;
    }

    @Override
    public List<MemberDto> retrieveAncMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet) {
        return ancVisitDao.retrieveAncMembers(byId, byMemberId, byFamilyId, byMobileNumber, byName, byLmp, byEdd, byOrganizationUnit, byAbhaNumber, byAbhaAddress, locationId, searchString, byFamilyMobileNumber, limit, offSet);
    }

}
