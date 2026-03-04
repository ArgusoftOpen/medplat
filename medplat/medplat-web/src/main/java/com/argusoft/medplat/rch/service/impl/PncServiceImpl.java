package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
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
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.listvalues.service.ListValueFieldValueDetailService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.PncChildMasterDao;
import com.argusoft.medplat.rch.dao.PncMasterDao;
import com.argusoft.medplat.rch.dao.PncMotherMasterDao;
import com.argusoft.medplat.rch.dao.WpdMotherDao;
import com.argusoft.medplat.rch.dto.ImmunisationDto;
import com.argusoft.medplat.rch.dto.PncChildDto;
import com.argusoft.medplat.rch.dto.PncMasterDto;
import com.argusoft.medplat.rch.dto.PncMotherDto;
import com.argusoft.medplat.rch.mapper.PncMapper;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.model.PncChildMaster;
import com.argusoft.medplat.rch.model.PncMaster;
import com.argusoft.medplat.rch.model.PncMotherMaster;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.rch.service.PncService;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Define services for pnc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class PncServiceImpl implements PncService {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String OTHER_OPTION = "OTHER";

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private PncMasterDao pncMasterDao;

    @Autowired
    private PncMotherMasterDao pncMotherMasterDao;

    @Autowired
    private PncChildMasterDao pncChildMasterDao;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private ImmunisationService immunisationService;

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private HealthInfrastructureDetailsDao healthInfrastructureDetailsDao;

    @Autowired
    private WpdMotherDao wpdMotherDao;

    @Autowired
    private ListValueFieldValueDetailService listValueFieldValueDetailService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storePncVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity motherEntity = memberDao.retrieveById(memberId);
        Integer pregnancyRegDetId = null;

        if (motherEntity.getState() != null
                && FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_DEAD.contains(motherEntity.getState())) {
            throw new ImtechoMobileException("Member is already marked as dead.", 1);
        }

        if (keyAndAnswerMap.get("-7") != null && !keyAndAnswerMap.get("-7").equals("null")) {
            pregnancyRegDetId = Integer.valueOf(keyAndAnswerMap.get("-7"));
        }

        if (parsedRecordBean.getNotificationId() != null && !parsedRecordBean.getNotificationId().equals("-1") && !parsedRecordBean.getNotificationId().equals("0")) {
            TechoNotificationMaster notificationMaster = techoNotificationMasterDao.retrieveById(Integer.parseInt(parsedRecordBean.getNotificationId()));
            if (notificationMaster != null) {
                pregnancyRegDetId = notificationMaster.getRefCode();
            }
        }

        if (pregnancyRegDetId == null && ConstantUtil.DROP_TYPE.equals("P")) {
            if (motherEntity.getCurPregRegDetId() != null) {
                pregnancyRegDetId = motherEntity.getCurPregRegDetId();
            } else {
                throw new ImtechoUserException("Pregnancy Registration Details has not been generated yet.", 1);
            }
        }

        if (keyAndAnswerMap.get("9057") != null && !keyAndAnswerMap.get("9057").isEmpty()) {
            FamilyEntity family = familyDao.retrieveById(familyId);
            family.setAnganwadiId(Integer.valueOf(keyAndAnswerMap.get("9057")));
            family.setAnganwadiUpdateFlag(Boolean.FALSE);
            familyDao.update(family);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
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

        PncMaster pncMaster = new PncMaster();
        pncMaster.setMemberId(memberId);
        pncMaster.setFamilyId(familyId);
        pncMaster.setLocationId(locationId);
        pncMaster.setPregnancyRegDetId(pregnancyRegDetId);
        pncMaster.setLatitude(keyAndAnswerMap.get("-2"));
        pncMaster.setLongitude(keyAndAnswerMap.get("-1"));

        if (keyAndAnswerMap.get("29") != null) {
            pncMaster.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("29"))));
        }

        if ((keyAndAnswerMap.get("-8")) != null && !keyAndAnswerMap.get("-8").equals("null")) {
            pncMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        } else {
            pncMaster.setMobileStartDate(new Date(0L));
        }
        if ((keyAndAnswerMap.get("-9")) != null && !keyAndAnswerMap.get("-9").equals("null")) {
            pncMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        } else {
            pncMaster.setMobileEndDate(new Date(0L));
        }
        pncMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));

        pncMaster.setMemberStatus(keyAndAnswerMap.get("14"));

        if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)
                && keyAndAnswerMap.get("41") != null) {
            pncMaster.setDeliveryPlace(keyAndAnswerMap.get("41"));
            if (keyAndAnswerMap.get("41").equals(RchConstants.DELIVERY_PLACE_PRIVATE_HOSPITAL)) {
                pncMaster.setDeliveryPlace(RchConstants.DELIVERY_PLACE_HOSPITAL);
                pncMaster.setTypeOfHospital(893);
            } else if (keyAndAnswerMap.get("41").equalsIgnoreCase(RchConstants.DELIVERY_PLACE_HOSPITAL)) {

                if (keyAndAnswerMap.get("42") == null) {
                    throw new ImtechoMobileException("Health Infrastructure is not selected while filling the form. Please fill form again.", 1);
                }

                if (keyAndAnswerMap.get("42").equals("-1")) {
                    pncMaster.setHealthInfrastructureId(-1);
                    pncMaster.setTypeOfHospital(1013);// 1013 is the Health Infra Type ID for Private Hospital.
                } else {
                    HealthInfrastructureDetails infra = healthInfrastructureDetailsDao.retrieveById(Integer.valueOf(keyAndAnswerMap.get("42")));
                    pncMaster.setHealthInfrastructureId(infra.getId());
                    pncMaster.setTypeOfHospital(infra.getType());
                }
            }
        }

        pncMasterDao.create(pncMaster);

        List<String> keysForPncMotherMasterQuestions = this.getKeysForPncMotherMasterQuestions();
        Map<String, String> motherKeyAndAnswerMap = new HashMap<>();
        Map<String, String> childKeyAndAnswerMap = new HashMap<>();

        for (Map.Entry<String, String> keyAnswerSet : keyAndAnswerMap.entrySet()) {
            String key = keyAnswerSet.getKey();
            String answer = keyAnswerSet.getValue();
            if (keysForPncMotherMasterQuestions.contains(key)) {
                motherKeyAndAnswerMap.put(key, answer);
            } else {
                childKeyAndAnswerMap.put(key, answer);
            }
        }

        PncMotherMaster pncMotherMaster = new PncMotherMaster();
        pncMotherMaster.setPncMasterId(pncMaster.getId());
        pncMotherMaster.setMotherId(memberId);

        for (Map.Entry<String, String> entrySet : motherKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToPncMotherMaster(key, answer, pncMotherMaster, keyAndAnswerMap);
        }

        if (pncMotherMaster.getMotherReferralDone() != null
                && pncMotherMaster.getMotherReferralDone().equals(RchConstants.REFFERAL_DONE_YES)) {
            wpdMotherDao.updateDischargeDetailsOfMember(pncMaster.getMemberId(),
                    pncMaster.getServiceDate(), pncMaster.getPregnancyRegDetId(), user.getId());
        }

        pncMotherMaster.setIsHighRiskCase(this.identifyHighRiskForMotherRchPnc(pncMotherMaster));
        updateMemberAdditionalInfo(motherEntity, pncMotherMaster, pncMaster);

        if (Objects.nonNull(pncMotherMaster.getIsAlive()) && pncMotherMaster.getIsAlive().equals(Boolean.FALSE)) {
            mobileFhsService.checkIfMemberDeathEntryExists(memberId);
        }
        pncMotherMasterDao.create(pncMotherMaster);

        Map<String, PncChildMaster> mapOfChildWithLoopIdAsKey = new HashMap<>();
        for (Map.Entry<String, String> entrySet : childKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            PncChildMaster pncChildMaster;
            if (key.contains(".")) {
                String[] splitKey = key.split("\\.");
                pncChildMaster = mapOfChildWithLoopIdAsKey.get(splitKey[1]);
                if (pncChildMaster == null) {
                    pncChildMaster = new PncChildMaster();
                    pncChildMaster.setPncMasterId(pncMaster.getId());
                    mapOfChildWithLoopIdAsKey.put(splitKey[1], pncChildMaster);
                }
                this.setAnswersToPncChildMaster(splitKey[0], answer, pncChildMaster, keyAndAnswerMap, splitKey[1]);
            } else {
                pncChildMaster = mapOfChildWithLoopIdAsKey.get("0");
                if (pncChildMaster == null) {
                    pncChildMaster = new PncChildMaster();
                    pncChildMaster.setPncMasterId(pncMaster.getId());
                    mapOfChildWithLoopIdAsKey.put("0", pncChildMaster);
                }
                this.setAnswersToPncChildMaster(key, answer, pncChildMaster, keyAndAnswerMap, null);
            }
        }

        for (Map.Entry<String, PncChildMaster> entrySet : mapOfChildWithLoopIdAsKey.entrySet()) {
            String loopId = entrySet.getKey();
            PncChildMaster pncChildMaster = entrySet.getValue();
            if (pncChildMaster.getChildId() == null) {
                List<MemberEntity> childsBelow100Days = new ArrayList<>();
                List<MemberEntity> childs = memberDao.getChildMembersByMotherId(memberId, Boolean.TRUE);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -100);
                if (childs != null) {
                    for (MemberEntity memberEntity : childs) {
                        if (memberEntity.getDob().after(calendar.getTime())) {
                            childsBelow100Days.add(memberEntity);
                        }
                    }
                }
                if (childsBelow100Days.isEmpty()) {
                    throw new ImtechoUserException("No Children with age less than 100 days found for this member", 1);
                } else if (childsBelow100Days.size() == 1) {
                    pncChildMaster.setChildId(childsBelow100Days.get(0).getId());
                } else {
                    throw new ImtechoUserException("Record String doesn't contain Child ID and multiple childs found for this member", 1);
                }
            }

            pncChildMaster.setIsHighRiskCase(this.identifyHighRiskForChildRchPnc(pncChildMaster));
            pncChildMasterDao.create(pncChildMaster);

            if (loopId.equals("0")) {
                if (keyAndAnswerMap.containsKey("85")) {
                    StringBuilder immunisationGiven = new StringBuilder();
                    String answer = keyAndAnswerMap.get("85").trim();
                    String[] split = answer.split("-");
                    for (String split1 : split) {
                        String[] immunisation = split1.split("/");
                        if (immunisation[1].equalsIgnoreCase("T")) {
                            ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, pncChildMaster.getChildId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                    MobileConstantUtil.PNC_VISIT, pncChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
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
                        MemberEntity memberEntity = memberDao.retrieveById(pncChildMaster.getChildId());
                        if (memberEntity.getImmunisationGiven() != null && memberEntity.getImmunisationGiven().length() > 0) {
                            String sb = memberEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                            memberEntity.setImmunisationGiven(sb.replace(" ", ""));
                        } else {
                            String immunisation = immunisationGiven.toString().replace(" ", "");
                            memberEntity.setImmunisationGiven(immunisation);
                        }
                        memberDao.update(memberEntity);
                    }
                }
            } else if (keyAndAnswerMap.containsKey("85" + "." + loopId)) {
                StringBuilder immunisationGiven = new StringBuilder();
                String answer = keyAndAnswerMap.get("85" + "." + loopId).trim();
                String[] split = answer.split("-");
                for (String split1 : split) {
                    String[] immunisation = split1.split("/");
                    if (immunisation[1].equalsIgnoreCase("T")) {
                        ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, pncChildMaster.getChildId(), MobileConstantUtil.CHILD_BENEFICIARY,
                                MobileConstantUtil.PNC_VISIT, pncChildMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
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
                    MemberEntity memberEntity = memberDao.retrieveById(pncChildMaster.getChildId());
                    if (memberEntity.getImmunisationGiven() != null && memberEntity.getImmunisationGiven().length() > 0) {
                        String sb = memberEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                        memberEntity.setImmunisationGiven(sb.replace(" ", ""));
                    } else {
                        memberEntity.setImmunisationGiven(immunisationGiven.toString());
                    }
                    memberDao.update(memberEntity);
                }
            }
        }
        pncChildMasterDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_PNC, pncMaster.getId()));
        return pncMaster.getId();
    }

    /**
     * Set answer to pnc mother master.
     *
     * @param key             Key.
     * @param answer          Answer for member's pnc mother visit details.
     * @param pncMotherMaster Pnc mother visit details.
     * @param keyAndAnswerMap Contains key and answers.
     */
    private void setAnswersToPncMotherMaster(String key, String answer, PncMotherMaster pncMotherMaster, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "5":
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                try {
                    pncMotherMaster.setDateOfDelivery(sdf.parse(answer));
                } catch (ParseException ex) {
                    Logger.getLogger(PncServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "141":
                switch (answer) {
                    case "2":
                        pncMotherMaster.setIsAlive(Boolean.FALSE);
                        pncMotherMaster.setMemberStatus(RchConstants.MEMBER_STATUS_DEATH);
                        break;
                    case "1":
                        pncMotherMaster.setIsAlive(Boolean.TRUE);
                        if (keyAndAnswerMap.get("14").equalsIgnoreCase(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                            pncMotherMaster.setMemberStatus(RchConstants.MEMBER_STATUS_AVAILABLE);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "2602":
                if (keyAndAnswerMap.get("141").equals("2")) {
                    pncMotherMaster.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;

            case "2603":
                if (keyAndAnswerMap.get("141").equals("2")) {
                    pncMotherMaster.setPlaceOfDeath(answer);
                }
                break;
            case "26":
                if (keyAndAnswerMap.get("141").equals("2")) {
                    if (answer.equalsIgnoreCase(OTHER_OPTION)) {
                        pncMotherMaster.setOtherDeathReason(keyAndAnswerMap.get("262"));
                        pncMotherMaster.setDeathReason("-1");
                    } else if (!answer.equals("NONE")) {
                        pncMotherMaster.setDeathReason(answer);
                    }
                }
                break;
            case "2607":
                pncMotherMaster.setDeathInfrastructureId(Integer.valueOf(answer));
                break;
            case "8":
                if (keyAndAnswerMap.get("141").equals("1")) {
                    pncMotherMaster.setIfaTabletsGiven(Integer.valueOf(answer.split("\\.")[0]));
                }
                break;
            case "82":
                if (keyAndAnswerMap.get("141").equals("1")) {
                    pncMotherMaster.setCalciumTabletsGiven(Integer.valueOf(answer.split("\\.")[0]));
                }
                break;
            case "9":
                if (keyAndAnswerMap.get("141").equals("1") && !answer.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                    Set<Integer> dangerSignsSet = new HashSet<>();
                    String[] dangerSignsArray = answer.split(",");
                    for (String dangerSignsId : dangerSignsArray) {
                        if (dangerSignsId.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_OTHER)) {
                            pncMotherMaster.setOtherDangerSign(keyAndAnswerMap.get("93"));
                        } else {
                            dangerSignsSet.add(Integer.valueOf(dangerSignsId));
                        }
                    }
                    pncMotherMaster.setMotherDangerSigns(dangerSignsSet);
                }
                break;
            case "9992":
                if (keyAndAnswerMap.get("141").equals("1")) {
                    if (!answer.equalsIgnoreCase(RchConstants.NO_RISK_FOUND)) {
                        pncMotherMaster.setIsHighRiskCase(Boolean.TRUE);
                    } else {
                        pncMotherMaster.setIsHighRiskCase(Boolean.FALSE);
                    }
                }
                break;
            case "11":
                if (keyAndAnswerMap.get("141").equals("1")) {
                    switch (answer) {
                        case "1":
                            pncMotherMaster.setMotherReferralDone(RchConstants.REFFERAL_DONE_YES);
                            break;
                        case "2":
                            pncMotherMaster.setMotherReferralDone(RchConstants.REFFERAL_DONE_NO);
                            break;
                        case "3":
                            pncMotherMaster.setMotherReferralDone(RchConstants.REFFERAL_DONE_NOT_REQUIRED);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case "12":
                if (keyAndAnswerMap.get("141").equals("1") && keyAndAnswerMap.get("11").equals("1")) {
                    pncMotherMaster.setReferralPlace(Integer.valueOf(answer));
                }
                break;
            case "19":
                if (keyAndAnswerMap.get("141").equals("1")) {
                    pncMotherMaster.setFamilyPlanningMethod(answer);
                }
                break;
            case "421":
                pncMotherMaster.setFpInsertOperateDate(new Date(Long.parseLong(answer)));
                break;
            default:
                break;
        }
    }

    /**
     * Set answer to pnc child master.
     *
     * @param key             Key.
     * @param answer          Answer for member's pnc child visit details.
     * @param pncChildMaster  Pnc child visit details.
     * @param keyAndAnswerMap Contains key and answers.
     * @param childCount      Total number of children.
     */
    private void setAnswersToPncChildMaster(String key, String answer, PncChildMaster pncChildMaster, Map<String, String> keyAndAnswerMap, String childCount) {
        switch (key) {
            case "3":
                MemberEntity childEntity = memberDao.getMemberByUniqueHealthIdAndFamilyId(answer, null);
                pncChildMaster.setChildId(childEntity.getId());
                break;
            case "16":
                switch (answer) {
                    case "1":
                        pncChildMaster.setIsAlive(Boolean.FALSE);
                        pncChildMaster.setMemberStatus(RchConstants.MEMBER_STATUS_DEATH);
                        break;
                    case "2":
                        pncChildMaster.setIsAlive(Boolean.TRUE);
                        pncChildMaster.setMemberStatus(RchConstants.MEMBER_STATUS_AVAILABLE);
                        break;
                    default:
                        break;
                }
                break;
            case "161":
                switch (answer) {
                    case "1":
                        pncChildMaster.setIsAlive(Boolean.TRUE);
                        pncChildMaster.setMemberStatus(RchConstants.MEMBER_STATUS_AVAILABLE);
                        break;
                    case "2":
                        pncChildMaster.setIsAlive(Boolean.FALSE);
                        pncChildMaster.setMemberStatus(RchConstants.MEMBER_STATUS_DEATH);
                        break;
                    default:
                        break;
                }
                break;
            case "1701":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("1"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("2"))) {
                        pncChildMaster.setDeathDate(new Date(Long.parseLong(answer)));
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("1"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("2"))) {
                    pncChildMaster.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "1702":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("1"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("2"))) {
                        pncChildMaster.setPlaceOfDeath(answer);
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("1"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("2"))) {
                    pncChildMaster.setPlaceOfDeath(answer);
                }
                break;
            case "17":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("1"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("2"))) {
                        if (answer.equalsIgnoreCase(OTHER_OPTION)) {
                            pncChildMaster.setOtherDeathReason(keyAndAnswerMap.get("172" + "." + childCount));
                            pncChildMaster.setDeathReason("-1");
                        } else if (!answer.equals("NONE")) {
                            pncChildMaster.setDeathReason(answer);
                        }
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("1"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("2"))) {
                    if (answer.equalsIgnoreCase(OTHER_OPTION)) {
                        pncChildMaster.setOtherDeathReason(keyAndAnswerMap.get("172"));
                        pncChildMaster.setDeathReason("-1");
                    } else if (!answer.equals("NONE")) {
                        pncChildMaster.setDeathReason(answer);
                    }
                }
                break;
            case "10":
                if (childCount != null) {
                    if (((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("2"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("1"))) && !answer.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                        Set<Integer> dangerSignsSet = new HashSet<>();
                        String[] dangerSignsArray = answer.split(",");
                        for (String dangerSignsId : dangerSignsArray) {
                            if (dangerSignsId.equals(RchConstants.DANGEROUS_SIGN_OTHER)) {
                                pncChildMaster.setOtherDangerSign(keyAndAnswerMap.get("103" + "." + childCount));
                            } else {
                                dangerSignsSet.add(Integer.valueOf(dangerSignsId));
                            }
                        }
                        pncChildMaster.setChildDangerSigns(dangerSignsSet);
                    }
                } else if (((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("2"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("1"))) && !answer.equalsIgnoreCase(RchConstants.DANGEROUS_SIGN_NONE)) {
                    Set<Integer> dangerSignsSet = new HashSet<>();
                    String[] dangerSignsArray = answer.split(",");
                    for (String dangerSignsId : dangerSignsArray) {
                        if (dangerSignsId.equals(RchConstants.DANGEROUS_SIGN_OTHER)) {
                            pncChildMaster.setOtherDangerSign(keyAndAnswerMap.get("103"));
                        } else {
                            dangerSignsSet.add(Integer.valueOf(dangerSignsId));
                        }
                    }
                    pncChildMaster.setChildDangerSigns(dangerSignsSet);
                }
                break;

            case "9994":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("2"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("1"))) {
                        if (!answer.equalsIgnoreCase(RchConstants.NO_RISK_FOUND)) {
                            pncChildMaster.setIsHighRiskCase(Boolean.TRUE);
                        } else {
                            pncChildMaster.setIsHighRiskCase(Boolean.FALSE);
                        }
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("2"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("1"))) {
                    if (!answer.equalsIgnoreCase(RchConstants.NO_RISK_FOUND)) {
                        pncChildMaster.setIsHighRiskCase(Boolean.TRUE);
                    } else {
                        pncChildMaster.setIsHighRiskCase(Boolean.FALSE);
                    }
                }
                break;
            case "13":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("2"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("1"))) {
                        switch (answer) {
                            case "1":
                                pncChildMaster.setChildReferralDone(RchConstants.REFFERAL_DONE_YES);
                                break;
                            case "2":
                                pncChildMaster.setChildReferralDone(RchConstants.REFFERAL_DONE_NO);
                                break;
                            case "3":
                                pncChildMaster.setChildReferralDone(RchConstants.REFFERAL_DONE_NOT_REQUIRED);
                                break;
                            default:
                                break;
                        }
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("2"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("1"))) {
                    switch (answer) {
                        case "1":
                            pncChildMaster.setChildReferralDone(RchConstants.REFFERAL_DONE_YES);
                            break;
                        case "2":
                            pncChildMaster.setChildReferralDone(RchConstants.REFFERAL_DONE_NO);
                            break;
                        case "3":
                            pncChildMaster.setChildReferralDone(RchConstants.REFFERAL_DONE_NOT_REQUIRED);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case "1301":
                if (childCount != null) {
                    if (((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("2"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("1"))) && keyAndAnswerMap.get("13" + "." + childCount).equals("1")) {
                        pncChildMaster.setReferralPlace(Integer.parseInt(answer));
                    }
                } else if (((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("2"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("1"))) && keyAndAnswerMap.get("13").equals("1")) {
                    pncChildMaster.setReferralPlace(Integer.parseInt(answer));
                }
                break;
            case "18":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("2"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("1"))) {
                        pncChildMaster.setChildWeight(Float.valueOf(answer));
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("2"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("1"))) {
                    pncChildMaster.setChildWeight(Float.valueOf(answer));
                }
                break;
            case "1700":
                if (childCount != null) {
                    if ((keyAndAnswerMap.get("16" + "." + childCount) != null && keyAndAnswerMap.get("16" + "." + childCount).equals("1"))
                            || (keyAndAnswerMap.get("161" + "." + childCount) != null && keyAndAnswerMap.get("161" + "." + childCount).equals("2"))) {
                        pncChildMaster.setDeathInfrastructureId(Integer.valueOf(answer));
                    }
                } else if ((keyAndAnswerMap.get("16") != null && keyAndAnswerMap.get("16").equals("1"))
                        || (keyAndAnswerMap.get("161") != null && keyAndAnswerMap.get("161").equals("2"))) {
                    pncChildMaster.setDeathInfrastructureId(Integer.valueOf(answer));
                }
                break;
            default:
                break;
        }
    }

    /**
     * Retrieves keys for pnc mother master questions.
     *
     * @return Returns list of keys.
     */
    private List<String> getKeysForPncMotherMasterQuestions() {
        List<String> keys = new ArrayList<>();
        keys.add("-1");         //Longitude
        keys.add("-2");         //Latitude
        keys.add("-4");         //Member ID 
        keys.add("-5");         //Family ID
        keys.add("-6");         //Location ID
        keys.add("-7");         //Curr Preg ID
        keys.add("-8");         //Mobile Start Date
        keys.add("-9");         //Mobile End Date
        keys.add("1");          //Unique Health Id
        keys.add("2");          //Mother Name
        keys.add("5");          //Date of Delivery
        keys.add("6");          //Pnc Visit Number
        keys.add("601");        //Age
        keys.add("602");        //Religion
        keys.add("603");        //Caste
        keys.add("604");        //BPL
        keys.add("605");        //Blood Group
        keys.add("7");          //Address
        keys.add("9001");       //Asha Name & Phone
        keys.add("9050");       //Hidden for Null Anganwadi Area
        keys.add("9051");       //Anganwadi Area
        keys.add("9052");       //Hidden to check if anganwadi is to be updated
        keys.add("9053");       //Is Anganwadi Correct
        keys.add("29");         //Service Date
        keys.add("14");         //Member Status
        keys.add("9054");       //Hidden to check Anganwadi Available
        keys.add("9055");       //Hidden to check if anganwadi question to ask
        keys.add("9056");       //Subcenter for Anganwadi
        keys.add("9057");       //Anganwadi ID
        keys.add("141");        //Is Mother Alive
        keys.add("2601");       //Are you sure beneficiary is dead?
        keys.add("2602");       //Death Date
        keys.add("2603");       //Place of Death
        keys.add("2607");       //Death Infrastructure
        keys.add("26");         //Death Reason
        keys.add("261");        //Hidden for Other Death Reason
        keys.add("262");        //Other Death Reason
        keys.add("9002");       //Phone Number
        keys.add("9006");       //Hidden For Aadhar Available
        keys.add("9003");       //Scan Aadhar
        keys.add("9004");       //QRS Result
        keys.add("9005");       //Aadhar Number
        keys.add("41");         //PNC Done At
        keys.add("42");         //Health InfraStructure
        keys.add("81");         //Hidden to check if IFA given
        keys.add("8");          //IFA Tabs Given
        keys.add("83");         //Hidden to check if Calcium given
        keys.add("82");         //Calcium Tabs Given
        keys.add("9");          //Danger Signs For Mother
        keys.add("92");         //Hidden For other Danger Signs
        keys.add("93");         //Other Daner Sign
        keys.add("91");         //Hidden to check if there are danger signs.
        keys.add("11");         //Mother Refferal Done
        keys.add("12");         //Refferal Place
        keys.add("19");         //Family Planning Method
        keys.add("421");        //FP Insert Operate Date
        keys.add("9991");       //Hidden to Show High Risk
        keys.add("9992");       //High Risk Found
        keys.add("9998");       //Submit Or Review
        keys.add("9999");       //Form is complete.
        return keys;
    }

    public Boolean identifyHighRiskForChildRchPnc(PncChildMaster pncChildMaster) {
        return (pncChildMaster.getChildWeight() != null && pncChildMaster.getChildWeight() < 2.5f) || (pncChildMaster.getChildDangerSigns() != null || pncChildMaster.getOtherDangerSign() != null);
    }

    public Boolean identifyHighRiskForMotherRchPnc(PncMotherMaster pncMotherMaster) {
        return (pncMotherMaster.getMotherDangerSigns() != null || pncMotherMaster.getOtherDangerSign() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PncMasterDto pncMasterDto) {
        MemberEntity memberEntity = memberDao.retrieveById(pncMasterDto.getMemberId());
        PncMaster pncMaster = PncMapper.convertPncMasterDtoToPncMaster(pncMasterDto);

        if (memberEntity.getCurPregRegDetId() != null) {
            pncMaster.setPregnancyRegDetId(memberEntity.getCurPregRegDetId());
        }

        if (pncMaster.getTypeOfHospital() == null) {
            HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(pncMaster.getHealthInfrastructureId());
            pncMaster.setTypeOfHospital(healthInfrastructureDetails.getType());
        }

        if (pncMasterDto.getContactNumber() != null) {
            memberEntity.setMobileNumber(pncMasterDto.getContactNumber());
            memberDao.update(memberEntity);
        }

        pncMasterDao.create(pncMaster);

        PncMotherMaster pncMotherMaster = PncMapper.convertPncMotherDtoToPncMotherMaster(pncMasterDto.getMotherDetails());
        pncMotherMaster.setPncMasterId(pncMaster.getId());
        pncMotherMaster.setIsHighRiskCase(this.identifyHighRiskForMotherRchPnc(pncMotherMaster));
        if (pncMotherMaster.getMotherReferralDone().equals("YES") && pncMotherMaster.getReferralPlace() == null) {
            HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(pncMotherMaster.getReferralInfraId());
            pncMotherMaster.setReferralPlace(healthInfrastructureDetails.getType());
        }
        pncMotherMasterDao.create(pncMotherMaster);
        updateMemberAdditionalInfo(memberEntity, pncMotherMaster, pncMaster);

        for (PncChildDto childDetails : pncMasterDto.getChildDetails()) {
            MemberEntity childEntity = memberDao.retrieveById(childDetails.getChildId());

            PncChildMaster pncChildMaster = PncMapper.convertPncChildDtoToPncChildMaster(childDetails);
            pncChildMaster.setPncMasterId(pncMaster.getId());
            pncChildMaster.setIsHighRiskCase(identifyHighRiskForChildRchPnc(pncChildMaster));
            if (pncChildMaster.getChildReferralDone().equals("YES") && pncChildMaster.getReferralPlace() == null) {
                HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(pncChildMaster.getReferralInfraId());
                pncChildMaster.setReferralPlace(healthInfrastructureDetails.getType());
            }
            pncChildMasterDao.create(pncChildMaster);
            updateChildAdditionalInfo(childEntity, pncChildMaster, pncMaster);

            if (childDetails.getImmunisationDtos() != null && !childDetails.getImmunisationDtos().isEmpty()) {
                StringBuilder immunisationGiven = new StringBuilder();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                for (ImmunisationDto immunisationDto : childDetails.getImmunisationDtos()) {
                    ImmunisationMaster immunisationMaster = new ImmunisationMaster(pncMaster.getFamilyId(), pncChildMaster.getChildId(),
                            MobileConstantUtil.CHILD_BENEFICIARY,
                            MobileConstantUtil.PNC_VISIT, pncChildMaster.getId(), null,
                            immunisationDto.getImmunisationGiven().trim(), immunisationDto.getImmunisationDate(), user.getId(),
                            pncMaster.getLocationId(), null);
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
                    if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                        String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                        childEntity.setImmunisationGiven(sb.replace(" ", ""));
                    } else {
                        String immunisation = immunisationGiven.toString().replace(" ", "");
                        childEntity.setImmunisationGiven(immunisation);
                    }
                    memberDao.update(childEntity);
                }
            }
        }
        pncChildMasterDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_PNC, pncMaster.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PncChildDto> getPncChildbyMemberid(Integer memberId) {
        List<PncChildDto> pncChildDtos = new ArrayList<>();
        List<PncChildMaster> pncChildMasters = pncChildMasterDao.getPncChildbyMemberid(memberId);
        if (pncChildMasters != null) {
            for (PncChildMaster childMaster : pncChildMasters) {
                pncChildDtos.add(PncMapper.convertPncChildMasterToPncChildDto(childMaster));
            }
            return pncChildDtos;
        }

        return new ArrayList<>();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PncMotherDto> getPncMotherbyMemberid(Integer memberId) {
        List<PncMotherDto> pncMotherDtos = new ArrayList<>();
        List<PncMotherMaster> pncMotherMasters = pncMotherMasterDao.getPncMotherbyMemberid(memberId);
        if (pncMotherMasters != null) {
            for (PncMotherMaster motherMaster : pncMotherMasters) {
                pncMotherDtos.add(PncMapper.convertPncMotherMasterToPncMotherDto(motherMaster));
            }
            return pncMotherDtos;
        }

        return new ArrayList<>();
    }

    /**
     * Update additional info for anc.
     *
     * @param motherEntity    Mother details.
     * @param pncMotherMaster Pnc mother master details.
     * @param pncMaster       Pnc master details.
     */
    private void updateMemberAdditionalInfo(MemberEntity motherEntity, PncMotherMaster pncMotherMaster, PncMaster pncMaster) {
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;
        if (motherEntity.getAdditionalInfo() != null && !motherEntity.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(motherEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (pncMotherMaster.getIfaTabletsGiven() != null && pncMotherMaster.getIfaTabletsGiven() > 0) {
            memberAdditionalInfo.setPncIfa(pncMotherMaster.getIfaTabletsGiven());
        }
        if (pncMotherMaster.getCalciumTabletsGiven() != null && pncMotherMaster.getCalciumTabletsGiven() > 0) {
            memberAdditionalInfo.setPncCalcium(pncMotherMaster.getCalciumTabletsGiven());
        }

        if (pncMaster.getServiceDate() != null) {
            memberAdditionalInfo.setLastServiceLongDate(pncMaster.getServiceDate().getTime());
        }

        StringBuilder sb = new StringBuilder();
        if (pncMotherMaster.getMotherDangerSigns() != null) {
            for (Integer dSign : pncMotherMaster.getMotherDangerSigns()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(listValueFieldValueDetailService.getListValueNameFormId(dSign));
            }
        }
        if (pncMotherMaster.getOtherDangerSign() != null) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(pncMotherMaster.getOtherDangerSign());
        }
        if (sb.length() > 0) {
            memberAdditionalInfo.setHighRiskReasons(sb.toString());
        }

        motherEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void updateChildAdditionalInfo(MemberEntity childEntity, PncChildMaster pncChildMaster, PncMaster pncMaster) {
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;
        if (childEntity.getAdditionalInfo() != null && !childEntity.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(childEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        StringBuilder reasonSb = new StringBuilder();
        if (pncChildMaster.getChildWeight() != null && pncChildMaster.getChildWeight() < 2.5f) {
            reasonSb.append("Very low weight");
        }
        if (pncChildMaster.getChildDangerSigns() != null && !pncChildMaster.getChildDangerSigns().isEmpty()) {
            for (Integer id : pncChildMaster.getChildDangerSigns()) {
                if (reasonSb.length() > 0) {
                    reasonSb.append(",");
                }
                reasonSb.append(listValueFieldValueDetailService.getListValueNameFormId(id));
            }
        }
        if (reasonSb.length() > 0) {
            memberAdditionalInfo.setHighRiskReasons(reasonSb.toString());
        } else {
            memberAdditionalInfo.setHighRiskReasons(null);
        }
        if (pncMaster.getServiceDate() != null) {
            memberAdditionalInfo.setLastServiceLongDate(pncMaster.getServiceDate().getTime());
        }
        childEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }
}
