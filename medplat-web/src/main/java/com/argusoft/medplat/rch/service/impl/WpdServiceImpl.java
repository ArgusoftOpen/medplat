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
import com.argusoft.medplat.fhs.mapper.MemberMapper;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.listvalues.service.ListValueFieldValueDetailService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AshaReportedEventDao;
import com.argusoft.medplat.rch.dao.RchInstitutionMasterDao;
import com.argusoft.medplat.rch.dao.WpdChildDao;
import com.argusoft.medplat.rch.dao.WpdMotherDao;
import com.argusoft.medplat.rch.dto.ImmunisationDto;
import com.argusoft.medplat.rch.dto.WpdChildDto;
import com.argusoft.medplat.rch.dto.WpdMasterDto;
import com.argusoft.medplat.rch.dto.WpdMotherDto;
import com.argusoft.medplat.rch.mapper.WpdMapper;
import com.argusoft.medplat.rch.model.AshaReportedEventMaster;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.model.WpdChildMaster;
import com.argusoft.medplat.rch.model.WpdMotherMaster;
import com.argusoft.medplat.rch.service.AshaReportedEventService;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.rch.service.WpdService;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Define services for volunteers.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class WpdServiceImpl implements WpdService {

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private WpdChildDao wpdChildDao;

    @Autowired
    private WpdMotherDao wpdMotherDao;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private ImmunisationService immunisationService;

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;

    @Autowired
    private RchInstitutionMasterDao rchInstitutionMasterDao;

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private HealthInfrastructureDetailsDao healthInfrastructureDetailsDao;

    @Autowired
    private AshaReportedEventService ashaReportedEventService;

    @Autowired
    private AshaReportedEventDao ashaReportedEventDao;

    @Autowired
    private ListValueFieldValueDetailService listValueFieldValueDetailService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeWpdVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity motherEntity = memberDao.retrieveById(memberId);
        Integer pregnancyRegDetId = null;

        if (keyAndAnswerMap.get("-7") != null && !keyAndAnswerMap.get("-7").equals("null")) {
            pregnancyRegDetId = Integer.valueOf(keyAndAnswerMap.get("-7"));
        }

        if (motherEntity.getState() != null && FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_DEAD.contains(motherEntity.getState())) {
            throw new ImtechoMobileException("Mother is already marked as dead.", 1);
        }
        if (motherEntity.getIsPregnantFlag() == null || !motherEntity.getIsPregnantFlag()) {
            throw new ImtechoMobileException("Member is not marked as pregnant.", 1);
        }

        if (motherEntity.getCurPregRegDetId() != null) {
            pregnancyRegDetId = motherEntity.getCurPregRegDetId();
        }
        if (pregnancyRegDetId == null && ConstantUtil.DROP_TYPE.equals("P")) {
            if (motherEntity.getState().equals(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED) || FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.contains(motherEntity.getState())) {
                throw new ImtechoMobileException("Member is not verified. Please Verified thru FHS.", 1);
            }
            throw new ImtechoUserException("Pregnancy Registration Details has not been generated yet.", 1);
        }

        if (keyAndAnswerMap.get("9057") != null && !keyAndAnswerMap.get("9057").isEmpty()) {
            FamilyEntity family = familyDao.retrieveById(familyId);
            family.setAnganwadiId(Integer.valueOf(keyAndAnswerMap.get("9057")));
            family.setAnganwadiUpdateFlag(Boolean.FALSE);
            familyDao.update(family);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String phone = keyAndAnswerMap.get("9002");
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
        mobileFhsService.updateMemberDetailsFromRchForms(phone, aadharMap, aadharNumber, null, null, motherEntity);

        WpdMotherMaster wpdMotherMaster = new WpdMotherMaster();
        wpdMotherMaster.setMemberId(memberId);
        wpdMotherMaster.setFamilyId(familyId);
        wpdMotherMaster.setLocationId(locationId);
        wpdMotherMaster.setPregnancyRegDetId(pregnancyRegDetId);
        if (keyAndAnswerMap.get("-8") != null && !keyAndAnswerMap.get("-8").equals("null")) {
            wpdMotherMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        } else {
            wpdMotherMaster.setMobileStartDate(new Date(0L));
        }
        if (keyAndAnswerMap.get("-9") != null && !keyAndAnswerMap.get("-9").equals("null")) {
            wpdMotherMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        } else {
            wpdMotherMaster.setMobileEndDate(new Date(0L));
        }
        wpdMotherMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));

        if (keyAndAnswerMap.get("511") != null && keyAndAnswerMap.get("511").equalsIgnoreCase("2")) {
            if (keyAndAnswerMap.get("-1") != null) {
                wpdMotherMaster.setLongitude(keyAndAnswerMap.get("-1"));
            }
            if (keyAndAnswerMap.get("-2") != null) {
                wpdMotherMaster.setLatitude(keyAndAnswerMap.get("-2"));
            }
            if (keyAndAnswerMap.get("51") != null) {
                wpdMotherMaster.setMemberStatus(keyAndAnswerMap.get("51"));
            }
            wpdMotherMaster.setHasDeliveryHappened(false);

            if (Boolean.FALSE.equals(wpdMotherMaster.getMotherAlive())) {
                mobileFhsService.checkIfMemberDeathEntryExists(memberId);
            }
            wpdMotherDao.create(wpdMotherMaster);
            wpdMotherDao.flush();
            eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_WPD, wpdMotherMaster.getId()));
            return wpdMotherMaster.getId();
        }

        List<String> keysForWpdMotherMasterQuestions = this.getKeysForWpdMotherMasterQuestions();
        Map<String, String> motherKeyAndAnswerMap = new HashMap<>();
        Map<String, String> childKeyAndAnswerMap = new HashMap<>();

        for (Map.Entry<String, String> keyAnswerSet : keyAndAnswerMap.entrySet()) {
            String key = keyAnswerSet.getKey();
            String answer = keyAnswerSet.getValue();
            if (keysForWpdMotherMasterQuestions.contains(key)) {
                motherKeyAndAnswerMap.put(key, answer);
            } else {
                childKeyAndAnswerMap.put(key, answer);
            }
        }

        for (Map.Entry<String, String> entrySet : motherKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToWpdMotherMaster(key, answer, wpdMotherMaster, keyAndAnswerMap);
        }

        if (wpdMotherMaster.getHasDeliveryHappened() != null && wpdMotherMaster.getHasDeliveryHappened() && wpdMotherMaster.getDeliveryPlace().equals(RchConstants.DELIVERY_PLACE_HOSPITAL) && wpdMotherMaster.getTypeOfHospital() == null) {
            throw new ImtechoMobileException("Type of hospital is not selected while filling the form. Please fill form again.", 1);
        }

        if (keyAndAnswerMap.get("14") != null) {
            wpdMotherMaster.setPregnancyOutcome(keyAndAnswerMap.get("14"));
        }

        if (keyAndAnswerMap.get("141") != null) {
            wpdMotherMaster.setTypeOfDelivery(keyAndAnswerMap.get("141"));
        }

        if (wpdMotherMaster.getMotherAlive() != null && !wpdMotherMaster.getMotherAlive()
                && wpdMotherMaster.getDeliveryPlace() != null
                && wpdMotherMaster.getDeliveryPlace().equals(RchConstants.DELIVERY_PLACE_HOSPITAL)
                && wpdMotherMaster.getDeathDate() != null) {
            wpdMotherMaster.setIsDischarged(Boolean.TRUE);
            wpdMotherMaster.setDischargeDate(wpdMotherMaster.getDeathDate());
        }

        if (wpdMotherMaster.getReferralDone() != null
                && wpdMotherMaster.getReferralDone().equals(RchConstants.REFFERAL_DONE_YES)
                && wpdMotherMaster.getDeliveryPlace() != null
                && wpdMotherMaster.getDeliveryPlace().equals(RchConstants.DELIVERY_PLACE_HOSPITAL)) {
            wpdMotherMaster.setIsDischarged(Boolean.TRUE);
            wpdMotherMaster.setDischargeDate(wpdMotherMaster.getDateOfDelivery());
        }

        wpdMotherDao.create(wpdMotherMaster);
        this.updateMemberAdditionalInfo(motherEntity, wpdMotherMaster);

        if (parsedRecordBean.getNotificationId() != null && !parsedRecordBean.getNotificationId().equals("-1")) {
            TechoNotificationMaster notificationMaster = techoNotificationMasterDao.retrieveById(Integer.parseInt(parsedRecordBean.getNotificationId()));
            if (notificationMaster != null) {
                NotificationTypeMaster notificationTypeMaster = notificationTypeMasterDao.retrieveById(notificationMaster.getNotificationTypeId());
                if (notificationTypeMaster != null && notificationTypeMaster.getCode() != null
                        && notificationTypeMaster.getCode().equals(MobileConstantUtil.NOTIFICATION_FHW_DELIVERY_CONF)) {
                    ashaReportedEventService.createReadOnlyNotificationForAsha(true, MobileConstantUtil.NOTIFICATION_FHW_DELIVERY_CONF,
                            motherEntity, familyDao.retrieveById(familyId), user);
                }

                if (notificationMaster.getRelatedId() != null && notificationMaster.getOtherDetails() != null
                        && notificationMaster.getOtherDetails().equals(MobileConstantUtil.ASHA_REPORT_MEMBER_DELIVERY)) {
                    AshaReportedEventMaster eventMaster = ashaReportedEventDao.retrieveById(notificationMaster.getRelatedId());
                    eventMaster.setAction(RchConstants.ASHA_REPORTED_EVENT_CONFIRMED);
                    eventMaster.setActionOn(new Date(Long.parseLong(parsedRecordBean.getMobileDate())));
                    eventMaster.setActionBy(user.getId());
                    ashaReportedEventDao.update(eventMaster);
                }
            }
        }

        if (wpdMotherMaster.getHasDeliveryHappened() != null && !wpdMotherMaster.getHasDeliveryHappened()) {
            motherEntity.setModifiedOn(new Date());
        }

        Map<String, WpdChildMaster> mapOfChildWithLoopIdAsKey = new HashMap<>();
        for (Map.Entry<String, String> entrySet : childKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            WpdChildMaster wpdChildMaster;
            if (key.contains(".")) {
                String[] splitKey = key.split("\\.");
                wpdChildMaster = mapOfChildWithLoopIdAsKey.get(splitKey[1]);
                if (wpdChildMaster == null) {
                    wpdChildMaster = new WpdChildMaster();
                    wpdChildMaster.setFamilyId(familyId);
                    wpdChildMaster.setLocationId(locationId);
                    if ((keyAndAnswerMap.get("-8")) != null && !keyAndAnswerMap.get("-8").equals("null")) {
                        wpdChildMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
                    } else {
                        wpdChildMaster.setMobileStartDate(new Date(0L));
                    }
                    if ((keyAndAnswerMap.get("-9")) != null && !keyAndAnswerMap.get("-9").equals("null")) {
                        wpdChildMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
                    } else {
                        wpdChildMaster.setMobileEndDate(new Date(0L));
                    }
                    wpdChildMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));
                    wpdChildMaster.setWpdMotherId(wpdMotherMaster.getId());
                    wpdChildMaster.setMotherId(memberId);
                    wpdChildMaster.setLatitude(keyAndAnswerMap.get("-2"));
                    wpdChildMaster.setLongitude(keyAndAnswerMap.get("-1"));
                    wpdChildMaster.setDateOfDelivery(wpdMotherMaster.getDateOfDelivery());
                    wpdChildMaster.setMemberStatus(wpdMotherMaster.getMemberStatus());
                    wpdChildMaster.setBreastFeedingInOneHour(wpdMotherMaster.getBreastFeedingInOneHour());
                    mapOfChildWithLoopIdAsKey.put(splitKey[1], wpdChildMaster);
                }
                this.setAnswersToWpdChildMaster(splitKey[0], answer, wpdChildMaster, keyAndAnswerMap, splitKey[1]);
            } else {
                wpdChildMaster = mapOfChildWithLoopIdAsKey.get("0");
                if (wpdChildMaster == null) {
                    wpdChildMaster = new WpdChildMaster();
                    wpdChildMaster.setFamilyId(familyId);
                    wpdChildMaster.setLocationId(locationId);
                    if ((keyAndAnswerMap.get("-8")) != null && !keyAndAnswerMap.get("-8").equals("null")) {
                        wpdChildMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
                    } else {
                        wpdChildMaster.setMobileStartDate(new Date(0L));
                    }
                    if ((keyAndAnswerMap.get("-9")) != null && !keyAndAnswerMap.get("-8").equals("null")) {
                        wpdChildMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
                    } else {
                        wpdChildMaster.setMobileEndDate(new Date(0L));
                    }
                    wpdChildMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));
                    wpdChildMaster.setWpdMotherId(wpdMotherMaster.getId());
                    wpdChildMaster.setMotherId(memberId);
                    wpdChildMaster.setLatitude(keyAndAnswerMap.get("-2"));
                    wpdChildMaster.setLongitude(keyAndAnswerMap.get("-1"));
                    wpdChildMaster.setDateOfDelivery(wpdMotherMaster.getDateOfDelivery());
                    wpdMotherMaster.setTypeOfDelivery(keyAndAnswerMap.get("141"));
                    wpdChildMaster.setMemberStatus(wpdMotherMaster.getMemberStatus());
                    wpdChildMaster.setBreastFeedingInOneHour(wpdMotherMaster.getBreastFeedingInOneHour());
                    mapOfChildWithLoopIdAsKey.put("0", wpdChildMaster);
                }
                this.setAnswersToWpdChildMaster(key, answer, wpdChildMaster, keyAndAnswerMap, null);
            }
        }

        for (Map.Entry<String, WpdChildMaster> entrySet : mapOfChildWithLoopIdAsKey.entrySet()) {
            String loopId = entrySet.getKey();
            WpdChildMaster wpdChildMaster = entrySet.getValue();

            if (wpdChildMaster.getPregnancyOutcome() != null
                    && wpdChildMaster.getPregnancyOutcome().equals(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)) {

                MemberEntity childEntity = new MemberEntity();
                childEntity.setFamilyId(motherEntity.getFamilyId());
                childEntity.setFirstName("B/o " + motherEntity.getFirstName());
                childEntity.setMiddleName(motherEntity.getMiddleName());
                childEntity.setLastName(motherEntity.getLastName());
                childEntity.setGender(wpdChildMaster.getGender());
                childEntity.setDob(wpdMotherMaster.getDateOfDelivery());
                childEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW);
                childEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                childEntity.setMotherId(memberId);
                childEntity.setPlaceOfBirth(wpdMotherMaster.getDeliveryPlace());
                childEntity.setBirthWeight(wpdChildMaster.getBirthWeight());
                childEntity.setIsHighRiskCase(this.identifyHighRiskForChildRchWpd(wpdChildMaster, wpdMotherMaster));
                childEntity.setMaritalStatus(ConstantUtil.LIST_VALUE_UNMARRIED);
                updateChildAdditionalInfo(childEntity, wpdChildMaster);
                memberDao.createMember(childEntity);

                wpdChildMaster.setMemberId(childEntity.getId());
                wpdChildMaster.setIsHighRiskCase(childEntity.getIsHighRiskCase());
                wpdChildMaster.setName(childEntity.getFirstName());
                wpdChildDao.create(wpdChildMaster);
                if (!wpdMotherMaster.getPregnancyOutcome().equals(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)) {
                    wpdMotherMaster.setPregnancyOutcome(wpdChildMaster.getPregnancyOutcome());
                    wpdMotherDao.update(wpdMotherMaster);
                }

                if (!loopId.equals("0")) {
                    if (keyAndAnswerMap.containsKey("85" + "." + loopId)) {
                        StringBuilder immunisationGiven = new StringBuilder();
                        String answer = keyAndAnswerMap.get("85" + "." + loopId).trim();
                        String[] split = answer.split("-");
                        for (String split1 : split) {
                            String[] immunisation = split1.split("/");
                            if (immunisation[1].equalsIgnoreCase("T")) {
                                ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                        MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                                        immunisation[0].trim(), new Date(Long.parseLong(immunisation[2])), user.getId(), locationId, null);
                                if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                                    immunisationService.createImmunisationMaster(immunisationMaster);
                                    immunisationGiven.append(immunisation[0].trim());
                                    immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                                    immunisationGiven.append(sdf.format(new Date(Long.parseLong(immunisation[2]))));
                                    immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                                }
                            }
                        }

                        if (immunisationGiven.length() > 1) {
                            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                            if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                                String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                                String immunisation = sb.replace(" ", "");
                                childEntity.setImmunisationGiven(immunisation);
                            } else {
                                String immunisation = immunisationGiven.toString().replace(" ", "");
                                childEntity.setImmunisationGiven(immunisation);
                            }
                            memberDao.update(childEntity);
                        }
                    }

                    if (keyAndAnswerMap.get("87" + "." + loopId) != null && !keyAndAnswerMap.get("87" + "." + loopId).equals("NONE")) {
                        String vaccineGiven = keyAndAnswerMap.get("87" + "." + loopId);
                        Date givenOn = new Date(Long.parseLong(keyAndAnswerMap.get("88" + "." + loopId)));
                        ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                                vaccineGiven.trim(), givenOn, user.getId(), locationId, null);
                        StringBuilder immunisationGiven = new StringBuilder();
                        if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                            immunisationService.createImmunisationMaster(immunisationMaster);
                            immunisationGiven.append(vaccineGiven.trim());
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                            immunisationGiven.append(sdf.format(givenOn));
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                        }

                        if (immunisationGiven.length() > 1) {
                            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                            if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                                String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                                childEntity.setImmunisationGiven(sb);
                            } else {
                                childEntity.setImmunisationGiven(immunisationGiven.toString());
                            }
                        }
                    }

                    if (keyAndAnswerMap.get("114" + "." + loopId) != null && !keyAndAnswerMap.get("114" + "." + loopId).equals("NONE")) {
                        String vaccineGiven = keyAndAnswerMap.get("114" + "." + loopId);
                        Date givenOn = new Date(Long.parseLong(keyAndAnswerMap.get("115" + "." + loopId)));
                        ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                                vaccineGiven.trim(), givenOn, user.getId(), locationId, null);
                        StringBuilder immunisationGiven = new StringBuilder();
                        if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                            immunisationService.createImmunisationMaster(immunisationMaster);
                            immunisationGiven.append(vaccineGiven.trim());
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                            immunisationGiven.append(sdf.format(givenOn));
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                        }

                        if (immunisationGiven.length() > 1) {
                            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                            if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                                String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                                childEntity.setImmunisationGiven(sb);
                            } else {
                                childEntity.setImmunisationGiven(immunisationGiven.toString());
                            }
                        }
                    }

                } else if (keyAndAnswerMap.containsKey("85")) {
                    StringBuilder immunisationGiven = new StringBuilder();
                    String answer = keyAndAnswerMap.get("85").trim();
                    String[] split = answer.split("-");
                    for (String split1 : split) {
                        String[] immunisation = split1.split("/");
                        if (immunisation[1].equalsIgnoreCase("T")) {
                            ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                    MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                                    immunisation[0].trim(), new Date(Long.parseLong(immunisation[2])), user.getId(), locationId, null);
                            if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                                immunisationService.createImmunisationMaster(immunisationMaster);
                                immunisationGiven.append(immunisation[0].trim());
                                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                                immunisationGiven.append(sdf.format(new Date(Long.parseLong(immunisation[2]))));
                                immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                            }
                        }
                    }

                    if (immunisationGiven.length() > 1) {
                        immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                        if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                            String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                            String immunisation = sb.replace(" ", "");
                            childEntity.setImmunisationGiven(immunisation);
                        } else {
                            String immunisation = immunisationGiven.toString().replace(" ", "");
                            childEntity.setImmunisationGiven(immunisation);
                        }
                        memberDao.update(childEntity);
                    }
                }

                if (loopId.equals("0")) {
                    if (keyAndAnswerMap.get("87") != null && !keyAndAnswerMap.get("87").equals("NONE")) {
                        String vaccineGiven = keyAndAnswerMap.get("87");
                        Date givenOn = new Date(Long.parseLong(keyAndAnswerMap.get("88")));
                        ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                                vaccineGiven.trim(), givenOn, user.getId(), locationId, null);
                        StringBuilder immunisationGiven = new StringBuilder();
                        if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                            immunisationService.createImmunisationMaster(immunisationMaster);
                            immunisationGiven.append(vaccineGiven.trim());
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                            immunisationGiven.append(sdf.format(givenOn));
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                        }

                        if (immunisationGiven.length() > 1) {
                            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                            if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                                String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                                childEntity.setImmunisationGiven(sb);
                            } else {
                                childEntity.setImmunisationGiven(immunisationGiven.toString());
                            }
                        }
                    }

                    if (keyAndAnswerMap.get("114") != null && !keyAndAnswerMap.get("114").equals("NONE")) {
                        String vaccineGiven = keyAndAnswerMap.get("114");
                        Date givenOn = new Date(Long.parseLong(keyAndAnswerMap.get("115")));
                        ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                                vaccineGiven.trim(), givenOn, user.getId(), locationId, null);
                        StringBuilder immunisationGiven = new StringBuilder();
                        if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                            immunisationService.createImmunisationMaster(immunisationMaster);
                            immunisationGiven.append(vaccineGiven.trim());
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                            immunisationGiven.append(sdf.format(givenOn));
                            immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                        }

                        if (immunisationGiven.length() > 1) {
                            immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                            if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                                String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                                childEntity.setImmunisationGiven(sb);
                            } else {
                                childEntity.setImmunisationGiven(immunisationGiven.toString());
                            }
                        }
                    }
                }

            } else if (wpdChildMaster.getPregnancyOutcome() != null
                    && wpdChildMaster.getPregnancyOutcome().equals(RchConstants.PREGNANCY_OUTCOME_STILL_BIRTH)) {
                wpdChildMaster.setMemberId(-1);
                wpdChildDao.create(wpdChildMaster);
            }
        }
        wpdChildDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_WPD, wpdMotherMaster.getId()));
        return 1;
    }
    private void setAnswersToWpdMotherMaster(String key, String answer, WpdMotherMaster wpdMotherMaster, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "-2":
                wpdMotherMaster.setLatitude(answer);
                break;
            case "-1":
                wpdMotherMaster.setLongitude(answer);
                break;
            case "51":
                wpdMotherMaster.setMemberStatus(answer);
                break;
            case "511":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    wpdMotherMaster.setHasDeliveryHappened(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "4":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(Long.parseLong(answer)));

                    String time = keyAndAnswerMap.get("41");
                    String[] split = time.split(":");

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));

                    wpdMotherMaster.setDateOfDelivery(calendar.getTime());
                }
                break;
            case "62":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    wpdMotherMaster.setCorticoSteroidGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "7":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    wpdMotherMaster.setDeliveryPlace(answer);
                    if (answer.equals(RchConstants.DELIVERY_PLACE_PRIVATE_HOSPITAL)) {
                        wpdMotherMaster.setDeliveryPlace(RchConstants.DELIVERY_PLACE_HOSPITAL);
                        wpdMotherMaster.setTypeOfHospital(893);
                    }
                }
                break;
            case "71":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("7").equalsIgnoreCase(RchConstants.DELIVERY_PLACE_HOSPITAL)) {
                    wpdMotherMaster.setTypeOfHospital(Integer.valueOf(answer));
                }
                break;
            case "72"://Health Infra Id
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    if (answer.equals("-1")) {
                        wpdMotherMaster.setHealthInfrastructureId(-1);
                        wpdMotherMaster.setTypeOfHospital(1013);// 1013 is the Health Infra Type ID for Private Hospital.
                    } else {
                        HealthInfrastructureDetails infra = healthInfrastructureDetailsDao.retrieveById(Integer.valueOf(answer));
                        wpdMotherMaster.setHealthInfrastructureId(infra.getId());
                        wpdMotherMaster.setTypeOfHospital(infra.getType());
                    }
                }
                break;
            case "76":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    wpdMotherMaster.setEligibleForChiranjeevi(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "8":
            case "81":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    wpdMotherMaster.setDeliveryDoneBy(answer);
                }
                break;
            case "20":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    wpdMotherMaster.setMotherAlive(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "2602":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equalsIgnoreCase("2")) {
                    wpdMotherMaster.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;

            case "2603":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equalsIgnoreCase("2")) {
                    wpdMotherMaster.setPlaceOfDeath(answer);
                }
                break;
            case "26":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equals("2")) {
                    if (answer.equals("OTHER")) {
                        wpdMotherMaster.setDeathReason("-1");
                    } else if (!answer.equals("NONE")) {
                        wpdMotherMaster.setDeathReason(answer);
                    }
                }
                break;
            case "2607":
                wpdMotherMaster.setDeathInfrastructureId(Integer.valueOf(answer));
                break;
            case "2605":
                wpdMotherMaster.setOtherDeathReason(answer);
                break;
            case "11":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equals("1")) {
                    if (!answer.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                        Set<Integer> motherDangerSignsSet = new HashSet<>();
                        String[] motherDangerSignsArray = answer.split(",");
                        for (String motherDangerSignsId : motherDangerSignsArray) {
                            if (motherDangerSignsId.equals(RchConstants.DANGEROUS_SIGN_OTHER)) {
                                wpdMotherMaster.setOtherDangerSigns(keyAndAnswerMap.get("93"));
                            } else {
                                motherDangerSignsSet.add(Integer.valueOf(motherDangerSignsId));
                            }
                        }
                        wpdMotherMaster.setMotherDangerSigns(motherDangerSignsSet);
                        wpdMotherMaster.setIsHighRiskCase(Boolean.TRUE);
                    } else {
                        wpdMotherMaster.setIsHighRiskCase(Boolean.FALSE);
                    }
                }
                break;
            case "12":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equals("1")) {
                    switch (answer) {
                        case "1":
                            wpdMotherMaster.setReferralDone(RchConstants.REFFERAL_DONE_YES);
                            break;
                        case "2":
                            wpdMotherMaster.setReferralDone(RchConstants.REFFERAL_DONE_NO);
                            break;
                        case "3":
                            wpdMotherMaster.setReferralDone(RchConstants.REFFERAL_DONE_NOT_REQUIRED);
                            break;
                        default:
                    }
                }
                break;
            case "13":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equals("1")
                        && keyAndAnswerMap.get("12").equals("1")) {
                    wpdMotherMaster.setReferralPlace(Integer.valueOf(answer));
                }
                break;
            case "8001":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("20").equals("1")
                        && keyAndAnswerMap.get("12").equals("1")) {
                    wpdMotherMaster.setReferralInfraId(Integer.valueOf(answer));
                }
                break;
            case "141":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && keyAndAnswerMap.get("7").equalsIgnoreCase(RchConstants.DELIVERY_PLACE_HOSPITAL)) {
                    wpdMotherMaster.setTypeOfDelivery(answer);
                }
                break;
            case "21":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    wpdMotherMaster.setBreastFeedingInOneHour(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "1801":
                if (keyAndAnswerMap.get("14").equals(RchConstants.PREGNANCY_OUTCOME_MTP)) {
                    wpdMotherMaster.setMtpDoneAt(Integer.parseInt(answer));
                }
                break;
            case "1802":
                if (keyAndAnswerMap.get("14").equals(RchConstants.PREGNANCY_OUTCOME_MTP)) {
                    wpdMotherMaster.setMtpPerformedBy(answer);
                }
                break;
            case "1902":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")) {
                    wpdMotherMaster.setIsDischarged(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "19":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && (keyAndAnswerMap.get("1902") == null || keyAndAnswerMap.get("1902").equalsIgnoreCase("1"))) {
                    wpdMotherMaster.setDischargeDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "1903":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && (keyAndAnswerMap.get("1902") == null || keyAndAnswerMap.get("1902").equalsIgnoreCase("1"))) {
                    wpdMotherMaster.setFreeMedicines(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "1904":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && (keyAndAnswerMap.get("1902") == null || keyAndAnswerMap.get("1902").equalsIgnoreCase("1"))) {
                    wpdMotherMaster.setFreeDiet(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "1905":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && (keyAndAnswerMap.get("1902") == null || keyAndAnswerMap.get("1902").equalsIgnoreCase("1"))) {
                    wpdMotherMaster.setFreeLabTest(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "1906":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && (keyAndAnswerMap.get("1902") == null || keyAndAnswerMap.get("1902").equalsIgnoreCase("1"))) {
                    wpdMotherMaster.setFreeBloodTransfusion(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "1907":
                if (keyAndAnswerMap.get("51").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswerMap.get("511").equalsIgnoreCase("1")
                        && (keyAndAnswerMap.get("1902") == null || keyAndAnswerMap.get("1902").equalsIgnoreCase("1"))) {
                    wpdMotherMaster.setFreeDropTransport(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "74":
                wpdMotherMaster.setMisoprostolGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "64":
            case "66":
                wpdMotherMaster.setPregnancyOutcome(answer);
                switch (answer) {
                    case "SPONT_ABORTION":
                        wpdMotherMaster.setDeliveryPlace(RchConstants.DELIVERY_PLACE_HOME);
                        break;
                    case "ABORTION":
                    case "MTP":
                        wpdMotherMaster.setDeliveryPlace(RchConstants.DELIVERY_PLACE_HOSPITAL);
                        break;
                    default:
                }
                break;
            default:
        }
    }

    private void setAnswersToWpdChildMaster(String key, String answer, WpdChildMaster wpdChildMaster, Map<String, String> keyAndAnswerMap, String childCount) {
        switch (key) {
            case "14":
                wpdChildMaster.setPregnancyOutcome(answer);
                break;

            case "141":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)
                        || keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_STILL_BIRTH)) {
                    wpdChildMaster.setTypeOfDelivery(answer);
                }
                break;

            case "15":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)
                        || keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_STILL_BIRTH)) {
                    wpdChildMaster.setGender(answer);
                }
                break;
            case "8003":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)) {
                    switch (answer) {
                        case "1":
                            wpdChildMaster.setReferralDone(RchConstants.REFFERAL_DONE_YES);
                            break;
                        case "2":
                            wpdChildMaster.setReferralDone(RchConstants.REFFERAL_DONE_NO);
                            break;
                        case "3":
                            wpdChildMaster.setReferralDone(RchConstants.REFFERAL_DONE_NOT_REQUIRED);
                            break;
                        default:
                    }
                }
                break;
            case "8004":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)
                        && keyAndAnswerMap.get("8003").equals("1")) {
                    wpdChildMaster.setReferralInfraId(Integer.valueOf(answer));
                }
                break;
            case "16":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)
                        || keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_STILL_BIRTH)) {
                    wpdChildMaster.setBirthWeight(Float.valueOf(answer));
                }
                break;

            case "17":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)
                        || keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_STILL_BIRTH)) {
                    if (!answer.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                        Set<Integer> congentialDeformitySet = new HashSet<>();
                        String[] congentialDeformityArray = answer.split(",");
                        for (String congentialDeformityArray1 : congentialDeformityArray) {
                            if (congentialDeformityArray1.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_OTHER)) {
                                if (childCount != null) {
                                    wpdChildMaster.setOtherCongentialDeformity(keyAndAnswerMap.get("172" + "." + childCount));
                                } else {
                                    wpdChildMaster.setOtherCongentialDeformity(keyAndAnswerMap.get("172"));
                                }
                            } else {
                                congentialDeformitySet.add(Integer.valueOf(congentialDeformityArray1));
                            }
                        }
                        wpdChildMaster.setCongentialDeformity(congentialDeformitySet);
                        wpdChildMaster.setIsHighRiskCase(Boolean.TRUE);
                    } else {
                        wpdChildMaster.setIsHighRiskCase(Boolean.FALSE);
                    }
                }
                break;

            case "80":
                if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)
                        || keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.PREGNANCY_OUTCOME_STILL_BIRTH)) {
                    wpdChildMaster.setBabyCriedAtBirth(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            default:
        }
    }

    private List<String> getKeysForWpdMotherMasterQuestions() {
        List<String> keys = new ArrayList<>();
        keys.add("-1");         //Longitude
        keys.add("-2");         //Latitude
        keys.add("-4");         //Member ID
        keys.add("-5");         //Family ID
        keys.add("-6");         //Location ID
        keys.add("-7");         //Curr Preg ID
        keys.add("-8");         //Mobile Start Date
        keys.add("-9");         //Mobile End Date
        keys.add("1");          //Member Unique Health Id
        keys.add("2");          //Member Name
        keys.add("3");          //Age
        keys.add("5");          //Total weeks of pregnancy
        keys.add("282");        //Address
        keys.add("283");        //Phone number of family
        keys.add("284");        //Number of living children
        keys.add("286");        //Date of pregnancy registration
        keys.add("287");        //Is early registration?
        keys.add("288");        //Immunisation Details
        keys.add("289");        //ANC Visit Detail
        keys.add("290");        //Blood Group
        keys.add("291");        //Last weight information
        keys.add("292");        //LMP
        keys.add("293");        //EDD
        keys.add("294");        //Previous Illness
        keys.add("295");        //High Risk Conditions
        keys.add("296");        //Bank Account Number
        keys.add("297");        //Is JSY Beneficiary?
        keys.add("298");        //Is JSY payment done?
        keys.add("299");        //Services during last visit
        keys.add("9001");       //Asha name and phone number
        keys.add("9050");       //Hidden to check Anganwadi Available
        keys.add("9051");       //Anganwadi Area
        keys.add("9052");       //Hidden to check if anganwadi is to be updated
        keys.add("9052");       //Hidden to check if anganwadi is to be updated
        keys.add("9053");       //Is the anganwadi correct?
        keys.add("51");         //Member Status
        keys.add("511");        //Has Delivery Happened
        keys.add("4");          //Date of Delivery
        keys.add("41");         //Time of Delivery
        keys.add("9054");       //Hidden to check Anganwadi Available
        keys.add("9055");       //Hidden to check if anganwadi question to ask
        keys.add("9056");       //Select Sub-Centre For Anganwadi
        keys.add("9057");       //Anganwadi ID
        keys.add("9002");       //Phone Number
        keys.add("9006");       //Hidden to check if Aadhar is available
        keys.add("9003");       //Scan Aadhar
        keys.add("9004");       //QRS Data
        keys.add("9005");       //Aadhar Number
        keys.add("61");         //Hidden to check preterm birth
        keys.add("62");         //Cortico Asteroid Given
        keys.add("63");         //hidden to check term
        keys.add("64");         //Pregnancy Outcome
        keys.add("641");        //Hidden to reset property Pregnancy outcome
        keys.add("65");         //hidden to check term
        keys.add("66");         //Pregnancy Outcome
        keys.add("651");        //Hidden to reset property Pregnancy outcome
        keys.add("7");          //Delivery Place
        keys.add("71");         //Type of Hospital
        keys.add("72");         //Health Infrastructure
        keys.add("73");         //Hidden to check misoprostol
        keys.add("74");         //Misoprotol Given
        keys.add("75");         //Hidden to check chiranjeevi eligibility
        keys.add("76");         //Is beneficiary eligible for Chirajeevi?
        keys.add("8");          //Delivery Done By
        keys.add("81");         //Delivery Done By
        keys.add("20");         //Mother Alive
        keys.add("2601");       //Are you sure beneficiary is dead?
        keys.add("2602");       //Death Date
        keys.add("2603");       //Place Of Death
        keys.add("2607");       //Death Infrastructure
        keys.add("26");         //Death Reason
        keys.add("11");         //Danger Signs
        keys.add("92");         //Hidden to check if other danger sign is selected
        keys.add("93");         //Other Danger Sign
        keys.add("91");         //Hidden to check if there are danger signs.
        keys.add("12");         //Refferal Done
        keys.add("13");         //Refferal Place
        keys.add("1801");       //MTP Done at
        keys.add("1802");       //MTP Performed by
        keys.add("1901");       //Hidden to check if institutional delivery
        keys.add("1902");       //Is discharged
        keys.add("19");         //Discharge Date
        keys.add("1903");       //Was free medicines dispensed?
        keys.add("1904");       //Was free diet provided?
        keys.add("1905");       //Was Free Lab Tests done?
        keys.add("1906");       //Was Free Blood Transfusion done?
        keys.add("1907");       //Was free drop back/ referral transport provided?
        keys.add("190");        //Hidden to check pregnancyOutcome
        keys.add("191");        //Hidden to check if mother is alive
        keys.add("21");         //Breastfeeding in one hour
        keys.add("9998");       //Submit or Review
        keys.add("9999");       //Form Complete
        keys.add("8001");
        return keys;
    }

    public Boolean identifyHighRiskForChildRchWpd(WpdChildMaster wpdChildMaster, WpdMotherMaster wpdMotherMaster) {
        return (wpdChildMaster.getBirthWeight() != null && wpdChildMaster.getBirthWeight() < 2.5f)
                || (!CollectionUtils.isEmpty(wpdChildMaster.getCongentialDeformity()))
                || (!CollectionUtils.isEmpty(wpdChildMaster.getDangerSigns()))
                || (wpdMotherMaster.getMotherAlive() != null && !wpdMotherMaster.getMotherAlive());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeWpdDischargeForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        if (parsedRecordBean.getNotificationId() != null && !parsedRecordBean.getNotificationId().equals("-1")) {
            TechoNotificationMaster techoNotificationMaster = techoNotificationMasterDao.retrieveById(Integer.valueOf(parsedRecordBean.getNotificationId()));
            if (techoNotificationMaster != null && techoNotificationMaster.getRelatedId() != null) {
                WpdMotherMaster wpdMotherMaster = wpdMotherDao.retrieveById(techoNotificationMaster.getRelatedId());
                if (keyAndAnswerMap.get("4").equals("1") && keyAndAnswerMap.get("5") != null) {
                    wpdMotherMaster.setDischargeDate(new Date(Long.parseLong(keyAndAnswerMap.get("5"))));
                    for (Map.Entry<String, String> entry : keyAndAnswerMap.entrySet()) {
                        setAnswersToDischargeForm(entry.getKey(), entry.getValue(), wpdMotherMaster);
                    }
                    wpdMotherDao.update(wpdMotherMaster);
                    techoNotificationMaster.setState(TechoNotificationMaster.State.COMPLETED);
                    techoNotificationMaster.setActionBy(user.getId());
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 3);
                    techoNotificationMaster.setState(TechoNotificationMaster.State.RESCHEDULE);
                    techoNotificationMaster.setScheduleDate(calendar.getTime());
                    calendar.add(Calendar.DATE, 3);
                    techoNotificationMaster.setDueOn(calendar.getTime());
                }
                techoNotificationMasterDao.update(techoNotificationMaster);
                return 1;
            }
        }
        return null;
    }

    private void setAnswersToDischargeForm(String key, String answer, WpdMotherMaster wpdMotherMaster) {
        switch (key) {
            case "6":
                switch (answer) {
                    case "1":
                        wpdMotherMaster.setFreeMedicines(Boolean.TRUE);
                        break;
                    case "2":
                        wpdMotherMaster.setFreeMedicines(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "7":
                switch (answer) {
                    case "1":
                        wpdMotherMaster.setFreeDiet(Boolean.TRUE);
                        break;
                    case "2":
                        wpdMotherMaster.setFreeDiet(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "8":
                switch (answer) {
                    case "1":
                        wpdMotherMaster.setFreeLabTest(Boolean.TRUE);
                        break;
                    case "2":
                        wpdMotherMaster.setFreeLabTest(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "9":
                switch (answer) {
                    case "1":
                        wpdMotherMaster.setFreeBloodTransfusion(Boolean.TRUE);
                        break;
                    case "2":
                        wpdMotherMaster.setFreeBloodTransfusion(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "10":
                switch (answer) {
                    case "1":
                        wpdMotherMaster.setFreeDropTransport(Boolean.TRUE);
                        break;
                    case "2":
                        wpdMotherMaster.setFreeDropTransport(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            default:
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDto> create(WpdMasterDto wpdMasterDto) {
        List<MemberDto> generatedChildren = new ArrayList<>();
        MemberEntity memberEntity = memberDao.retrieveMemberById(wpdMasterDto.getMemberId());

        if (memberEntity.getIsPregnantFlag() == null || !memberEntity.getIsPregnantFlag()) {
            throw new ImtechoUserException("Member is not marked as Pregnant.", 1);
        }

        if (memberEntity.getCurPregRegDetId() != null) {
            wpdMasterDto.setPregRegDetId(memberEntity.getCurPregRegDetId());
        } else {
            throw new ImtechoUserException("Pregnancy Registration Details has not been generated yet.", 1);
        }

        if (wpdMasterDto.getAccountNumber() != null || wpdMasterDto.getIfsc() != null || wpdMasterDto.getContactNumber() != null) {
            if (wpdMasterDto.getAccountNumber() != null) {
                memberEntity.setAccountNumber(wpdMasterDto.getAccountNumber());
            }
            if (wpdMasterDto.getIfsc() != null) {
                memberEntity.setIfsc(wpdMasterDto.getIfsc());
            }
            if (wpdMasterDto.getContactNumber() != null) {
                memberEntity.setMobileNumber(wpdMasterDto.getContactNumber());
            }
            memberDao.update(memberEntity);
        }

        WpdMotherMaster wpdMotherMaster = WpdMapper.convertWpdMasterDtoToWpdMotherMaster(wpdMasterDto);
        if (wpdMotherMaster.getTypeOfHospital() == null && wpdMotherMaster.getHealthInfrastructureId() != null) {
            HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(wpdMotherMaster.getHealthInfrastructureId());
            if (healthInfrastructureDetails != null) {
                wpdMotherMaster.setTypeOfHospital(healthInfrastructureDetails.getType());
            }
        }

        wpdMotherMaster.setPregnancyOutcome(wpdMasterDto.getChildDetails().get(0).getPregnancyOutcome());
        for (WpdChildDto wpdChildDto : wpdMasterDto.getChildDetails()) {
            if (wpdChildDto.getBreastFeeding() != null && wpdChildDto.getBreastFeeding()) {
                wpdMotherMaster.setBreastFeedingInOneHour(Boolean.TRUE);
                break;
            }
        }
        if (wpdMotherMaster.getBreastFeedingInOneHour() == null) {
            wpdMotherMaster.setBreastFeedingInOneHour(Boolean.FALSE);
        }
        if (wpdMotherMaster.getDeliveryPlace().equals("HOSP") || wpdMotherMaster.getDeliveryPlace().equals("THISHOSP")) {
            wpdMotherMaster.setDeliveryPlace("HOSP");
        }
        if (!wpdMotherMaster.getDeliveryPlace().equals("108_AMBULANCE") && wpdMotherMaster.getHealthInfrastructureId() == null) {
            throw new ImtechoUserException("Please Select Health Infrastructure", 1);
        }
        wpdMotherDao.create(wpdMotherMaster);
        updateMemberAdditionalInfo(memberEntity, wpdMotherMaster);

        for (WpdChildDto wpdChildDto : wpdMasterDto.getChildDetails()) {
            WpdChildMaster wpdChildMaster = WpdMapper.convertWpdChildDtoToWpdChildMaster(wpdChildDto);
            if (wpdChildMaster != null) {
                if (wpdChildMaster.getPregnancyOutcome() != null && wpdChildMaster.getPregnancyOutcome().equals(RchConstants.PREGNANCY_OUTCOME_LIVE_BIRTH)) {
                    MemberEntity childEntity = new MemberEntity();
                    childEntity.setFamilyId(memberEntity.getFamilyId());
                    childEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                    childEntity.setMotherId(wpdMotherMaster.getMemberId());
                    childEntity.setFirstName(wpdChildMaster.getName());
                    childEntity.setMiddleName(memberEntity.getMiddleName());
                    childEntity.setLastName(memberEntity.getLastName());
                    childEntity.setDob(wpdMotherMaster.getDateOfDelivery());
                    childEntity.setGender(wpdChildMaster.getGender());
                    childEntity.setBirthWeight(wpdChildMaster.getBirthWeight());
                    childEntity.setPlaceOfBirth(wpdMotherMaster.getDeliveryPlace());
                    childEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW);
                    childEntity.setIsHighRiskCase(this.identifyHighRiskForChildRchWpd(wpdChildMaster, wpdMotherMaster));
                    childEntity.setMaritalStatus(ConstantUtil.LIST_VALUE_UNMARRIED);
                    if (wpdChildDto.getImmunisationDetails() != null) {
                        String existingImmunisationString = "";
                        childEntity.setImmunisationGiven(buildImmunisationStringForNewChild(wpdChildDto.getImmunisationDetails(), existingImmunisationString));
                    }
                    memberDao.createMember(childEntity);
                    generatedChildren.add(MemberMapper.getMemberDto(childEntity));
                    wpdChildMaster.setMemberId(childEntity.getId());
                } else {
                    wpdChildMaster.setMemberId(-1);
                }

                wpdChildMaster.setFamilyId(wpdMotherMaster.getFamilyId());
                wpdChildMaster.setLatitude(wpdMotherMaster.getLatitude());
                wpdChildMaster.setLongitude(wpdMotherMaster.getLongitude());
                wpdChildMaster.setMobileStartDate(wpdMotherMaster.getMobileStartDate());
                wpdChildMaster.setMobileEndDate(wpdMotherMaster.getMobileEndDate());
                wpdChildMaster.setLocationId(wpdMotherMaster.getLocationId());
                wpdChildMaster.setWpdMotherId(wpdMotherMaster.getId());
                wpdChildMaster.setMotherId(wpdMotherMaster.getMemberId());
                wpdChildMaster.setDateOfDelivery(wpdMotherMaster.getDateOfDelivery());
                wpdChildMaster.setMemberStatus(wpdMotherMaster.getMemberStatus());
                wpdChildMaster.setTypeOfDelivery(wpdMotherMaster.getTypeOfDelivery());
                wpdChildMaster.setIsHighRiskCase(this.identifyHighRiskForChildRchWpd(wpdChildMaster, wpdMotherMaster));
                wpdChildDao.create(wpdChildMaster);

                for (ImmunisationDto immunisationDto : wpdChildDto.getImmunisationDetails()) {
                    ImmunisationMaster immunisationMaster = new ImmunisationMaster(wpdChildMaster.getFamilyId(), wpdChildMaster.getMemberId(),
                            MobileConstantUtil.CHILD_BENEFICIARY,
                            MobileConstantUtil.WPD_VISIT, wpdChildMaster.getId(), null,
                            immunisationDto.getImmunisationGiven().trim(), immunisationDto.getImmunisationDate(), user.getId(),
                            wpdChildMaster.getLocationId(), null);
                    immunisationService.createImmunisationMaster(immunisationMaster);
                }
            }
        }

        wpdMotherDao.flush();
        wpdChildDao.flush();
        memberDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_WPD, wpdMotherMaster.getId()));
        return generatedChildren;
    }

    private String buildImmunisationStringForNewChild(List<ImmunisationDto> immunisationDtos, String existingImmunisationString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder immunisationString = new StringBuilder();
        for (ImmunisationDto immunisationDto : immunisationDtos) {
            if (immunisationDto.getImmunisationGiven() != null && !immunisationDto.getImmunisationGiven().isEmpty()) {
                immunisationString.append(immunisationDto.getImmunisationGiven().trim());
                immunisationString.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                immunisationString.append(sdf.format(immunisationDto.getImmunisationDate()));
                immunisationString.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
            }
        }
        if (immunisationString.length() > 0) {
            String immunisation = immunisationString.toString().replace(" ", "");
            if (existingImmunisationString != null && existingImmunisationString.length() > 0) {
                immunisation = existingImmunisationString.concat("," + immunisation);
            }
            return immunisation.substring(0, immunisation.length() - 1);
        } else {
            if (existingImmunisationString != null && existingImmunisationString.length() > 0) {
                return existingImmunisationString;
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdChildDto> getWpdChildbyMemberid(Integer memberId) {
        List<WpdChildDto> pncChildDtos = new ArrayList<>();
        List<WpdChildMaster> pncChildMasters = wpdChildDao.getWpdChildbyMemberid(memberId);
        if (pncChildMasters != null) {
            for (WpdChildMaster childMaster : pncChildMasters) {
                pncChildDtos.add(WpdMapper.convertWpdChildMasterToWpdChildMasterDto(childMaster));
            }
            return pncChildDtos;
        }

        return Collections.emptyList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdMotherDto> getWpdMotherbyMemberid(Integer memberId) {
        List<WpdMotherDto> wpdMotherDtos = new ArrayList<>();
        List<WpdMotherMaster> wpdMotherMasters = wpdMotherDao.getWpdMotherbyMemberid(memberId);
        if (wpdMotherMasters != null) {
            for (WpdMotherMaster motherMaster : wpdMotherMasters) {
                wpdMotherDtos.add(WpdMapper.convertWpdMotherMasterToWpdMotherMasterDto(motherMaster));
            }
            return wpdMotherDtos;
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdMasterDto> retrievePendingDischargeList(Integer userId) {
        return wpdMotherDao.retrievePendingDischargeList(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDischargeDetails(WpdMasterDto wpdMasterDto) {
        WpdMotherMaster wpdMotherMaster = wpdMotherDao.retrieveById(wpdMasterDto.getId());
        if (wpdMotherMaster != null) {
            if (wpdMasterDto.getChildDetails() != null) {
                for (WpdChildDto wpdChildDto : wpdMasterDto.getChildDetails()) {
                    MemberEntity childMemberEntity = memberDao.retrieveMemberById(wpdChildDto.getMemberId());
                    if (childMemberEntity != null) {
                        String existingImmunisationString = childMemberEntity.getImmunisationGiven();
                        childMemberEntity.setImmunisationGiven(buildImmunisationStringForNewChild(wpdChildDto.getImmunisationDetails(), existingImmunisationString));
                        memberDao.update(childMemberEntity);
                        for (ImmunisationDto immunisationDto : wpdChildDto.getImmunisationDetails()) {
                            ImmunisationMaster immunisationMaster = new ImmunisationMaster(wpdChildDto.getFamilyId(), wpdChildDto.getMemberId(),
                                    MobileConstantUtil.CHILD_BENEFICIARY,
                                    MobileConstantUtil.WPD_VISIT, wpdChildDto.getId(), null,
                                    immunisationDto.getImmunisationGiven().trim(), immunisationDto.getImmunisationDate(), user.getId(),
                                    wpdChildDto.getLocationId(), null);
                            immunisationService.createImmunisationMaster(immunisationMaster);
                        }
                    }
                }
            }
            wpdMotherMaster.setDischargeDate(wpdMasterDto.getDischargeDate());
            wpdMotherMaster.setFreeDropDelivery(wpdMasterDto.getFreeDropDelivery());
            wpdMotherMaster.setIsDischarged(wpdMasterDto.getIsDischarged());
            wpdMotherMaster.setFreeMedicines(wpdMasterDto.getFreeMedicines());
            wpdMotherMaster.setFreeLabTest(wpdMasterDto.getFreeLabTest());
            wpdMotherMaster.setFreeDiet(wpdMasterDto.getFreeDiet());
            wpdMotherMaster.setFreeBloodTransfusion(wpdMasterDto.getFreeBloodTransfusion());
            wpdMotherMaster.setFreeDropTransport(wpdMasterDto.getFreeDropTransport());
            wpdMotherDao.update(wpdMotherMaster);
        }
    }

    @Override
    public List<MemberDto> retrieveWpdMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet) {
        return wpdMotherDao.retrieveWpdMembers(byId, byMemberId, byFamilyId, byMobileNumber, byName, byLmp, byEdd, byOrganizationUnit, byAbhaNumber, byAbhaAddress, locationId, searchString, byFamilyMobileNumber, limit, offSet);
    }

    /**
     * Update additional info for anc.
     *
     * @param memberEntity Member details.
     * @param wpdMother    Wpd mother details.
     */
    private void updateMemberAdditionalInfo(MemberEntity memberEntity, WpdMotherMaster wpdMother) {

        Gson gson = new Gson();
        if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
            boolean isUpdate = false;
            MemberAdditionalInfo memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);

            if (wpdMother.getDateOfDelivery() != null) {
                memberAdditionalInfo.setLastServiceLongDate(wpdMother.getDateOfDelivery().getTime());
                isUpdate = true;
            }

            if (isUpdate) {
                memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            }
        } else {
            MemberAdditionalInfo memberAdditionalInfo = new MemberAdditionalInfo();
            boolean isUpdate = false;

            if (wpdMother.getDateOfDelivery() != null) {
                memberAdditionalInfo.setLastServiceLongDate(wpdMother.getDateOfDelivery().getTime());
                isUpdate = true;
            }

            if (isUpdate) {
                memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            }
        }
    }

    private void updateChildAdditionalInfo(MemberEntity memberEntity, WpdChildMaster wpdChildMaster) {
        Gson gson = new Gson();
        boolean isUpdate = false;
        MemberAdditionalInfo memberAdditionalInfo;
        if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(wpdChildMaster.getCongentialDeformity())) {
            for (Integer dSign : wpdChildMaster.getCongentialDeformity()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(listValueFieldValueDetailService.getListValueNameFormId(dSign));
            }
        }
        if (!CollectionUtils.isEmpty(wpdChildMaster.getDangerSigns())) {
            for (Integer dSign : wpdChildMaster.getDangerSigns()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(listValueFieldValueDetailService.getListValueNameFormId(dSign));
            }
        }

        if (sb.length() > 0) {
            memberAdditionalInfo.setHighRiskReasons(sb.toString());
            isUpdate = true;
        }

        if (isUpdate) {
            memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
        }
    }
}
