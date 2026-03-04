package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AshaPncChildMasterDao;
import com.argusoft.medplat.rch.dao.AshaPncMasterDao;
import com.argusoft.medplat.rch.dao.AshaPncMotherMasterDao;
import com.argusoft.medplat.rch.model.AshaPncChildMaster;
import com.argusoft.medplat.rch.model.AshaPncMaster;
import com.argusoft.medplat.rch.model.AshaPncMotherMaster;
import com.argusoft.medplat.rch.service.AshaPncService;
import com.argusoft.medplat.rch.service.AshaReportedEventService;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * <p>
 *     Define services for ASHA pnc service.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
@Service
@Transactional
public class AshaPncServiceImpl implements AshaPncService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private AshaPncMasterDao ashaPncMasterDao;

    @Autowired
    private AshaPncMotherMasterDao ashaPncMotherMasterDao;

    @Autowired
    private AshaPncChildMasterDao ashaPncChildMasterDao;

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private ImmunisationService immunisationService;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private AshaReportedEventService ashaReportedEventService;

    /**
     * {@inheritDoc}
     */
    @Override
    public AshaPncMotherMaster retrievePncMotherMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId) {
        return ashaPncMotherMasterDao.retrievePncMotherMasterByPncMasterIdAndMemberId(pncMasterId, memberId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AshaPncChildMaster retrievePncChildMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId) {
        return ashaPncChildMasterDao.retrievePncChildMasterByPncMasterIdAndMemberId(pncMasterId, memberId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storePncServiceVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {

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

        if (pregnancyRegDetId == null && ConstantUtil.DROP_TYPE.equals("P")) {
            if (motherEntity.getCurPregRegDetId() != null) {
                pregnancyRegDetId = motherEntity.getCurPregRegDetId();
            } else {
                throw new ImtechoUserException("Pregnancy Registration Details has not been generated yet.", 1);
            }
        }

        String phone = keyAndAnswerMap.get("13");
        String isAadharScanned = keyAndAnswerMap.get("15");
        String aadharMap = null;
        String aadharNumber = null;
        if (isAadharScanned != null) {
            if (isAadharScanned.equals("1")) {
                aadharMap = keyAndAnswerMap.get("16");
            } else {
                String answer = keyAndAnswerMap.get("17");
                if (answer != null && !answer.equals("T")) {
                    aadharNumber = answer.replace("F/", "");
                }
            }
        }
        mobileFhsService.updateMemberDetailsFromRchForms(phone, aadharMap, aadharNumber, null, null, motherEntity);

        AshaPncMaster pncMaster = new AshaPncMaster();
        pncMaster.setMemberId(memberId);
        pncMaster.setFamilyId(familyId);
        pncMaster.setLocationId(locationId);
        pncMaster.setPregnancyRegDetId(pregnancyRegDetId);
        pncMaster.setLatitude(keyAndAnswerMap.get("-2"));
        pncMaster.setLongitude(keyAndAnswerMap.get("-1"));
        pncMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        pncMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        pncMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));

        if (keyAndAnswerMap.get("10") != null) {
            pncMaster.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("10"))));
        }

        if (keyAndAnswerMap.get("12") != null) {
            pncMaster.setMemberStatus(keyAndAnswerMap.get("12"));
        }

        ashaPncMasterDao.create(pncMaster);

        if (pncMaster.getMemberStatus().equals(RchConstants.MEMBER_STATUS_DEATH)) {
            ashaReportedEventService.createNotificationForReportedEventByAsha(pncMaster.getMemberId(),
                    MobileConstantUtil.NOTIFICATION_FHW_DEATH_CONF,
                    pncMaster.getFamilyId(), pncMaster.getLocationId(), pncMaster.getId(),
                    MobileConstantUtil.ASHA_PNC_VISIT);
        }

        if (pncMaster.getMemberStatus().equals(RchConstants.MEMBER_STATUS_MIGRATED)) {
            ashaReportedEventService.createNotificationForReportedEventByAsha(pncMaster.getMemberId(),
                    MobileConstantUtil.NOTIFICATION_FHW_MEMBER_MIGRATION,
                    pncMaster.getFamilyId(), pncMaster.getLocationId(), pncMaster.getId(),
                    MobileConstantUtil.ASHA_PNC_VISIT);
            return pncMaster.getId();
        }

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

        AshaPncMotherMaster pncMotherMaster = new AshaPncMotherMaster();
        pncMotherMaster.setPncMasterId(pncMaster.getId());
        pncMotherMaster.setMotherId(memberId);

        for (Map.Entry<String, String> entrySet : motherKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToPncMotherMaster(key, answer, pncMotherMaster, keyAndAnswerMap);
        }

        ashaPncMotherMasterDao.create(pncMotherMaster);

        Map<String, AshaPncChildMaster> mapOfChildWithLoopIdAsKey = new HashMap<>();
        for (Map.Entry<String, String> entrySet : childKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            AshaPncChildMaster pncChildMaster;
            if (key.contains(".")) {
                String[] splitKey = key.split("\\.");
                pncChildMaster = mapOfChildWithLoopIdAsKey.get(splitKey[1]);
                if (pncChildMaster == null) {
                    pncChildMaster = new AshaPncChildMaster();
                    pncChildMaster.setPncMasterId(pncMaster.getId());
                    mapOfChildWithLoopIdAsKey.put(splitKey[1], pncChildMaster);
                }
                this.setAnswersToPncChildMaster(splitKey[0], answer, pncChildMaster, keyAndAnswerMap, splitKey[1]);
            } else {
                pncChildMaster = mapOfChildWithLoopIdAsKey.get("0");
                if (pncChildMaster == null) {
                    pncChildMaster = new AshaPncChildMaster();
                    pncChildMaster.setPncMasterId(pncMaster.getId());
                    mapOfChildWithLoopIdAsKey.put("0", pncChildMaster);
                }
                this.setAnswersToPncChildMaster(key, answer, pncChildMaster, keyAndAnswerMap, null);
            }
        }

        for (Map.Entry<String, AshaPncChildMaster> entrySet : mapOfChildWithLoopIdAsKey.entrySet()) {
            AshaPncChildMaster pncChildMaster = entrySet.getValue();
            ashaPncChildMasterDao.create(pncChildMaster);
        }

        ashaPncMasterDao.flush();
        ashaPncMotherMasterDao.flush();
        ashaPncChildMasterDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.ASHA_PNC, pncMaster.getId()));
        return pncMaster.getId();
    }

    /**
     * Retrieves keys for pnc mother master questions.
     * @return Returns keys for pnc mother master questions.
     */
    private List<String> getKeysForPncMotherMasterQuestions() {
        List<String> keys = new ArrayList<>();
        keys.add("-1");         //Longitude
        keys.add("-2");         //Latitude
        keys.add("-4");         //Member Id
        keys.add("-5");         //Family Id
        keys.add("-6");         //Location Id
        keys.add("-7");         //Pregnancy Reg Id
        keys.add("-8");         //Mobile Start Date
        keys.add("-9");         //Mobile End Date
        keys.add("1");          //Name of beneficiary
        keys.add("2");          //Health Id
        keys.add("3");          //Delivery Date
        keys.add("10");         //Service Date
        keys.add("11");         //Atleast one family member should be present at the time of home visit.
        keys.add("12");         //Member Status
        keys.add("120");        //Are you sure the mother is dead?
        keys.add("121");        //Death date
        keys.add("122");        //Place of death
        keys.add("123");        //Death reason
        keys.add("124");        //Hidden to check other death reason
        keys.add("125");        //Mention other death reason
        keys.add("13");         //Phone number
        keys.add("14");         //Hidden to check if Aadhar is available
        keys.add("15");         //Do you want to scan the aadhar card?
        keys.add("16");         //QRS
        keys.add("17");         //Aadhar Number
        keys.add("20");         //Mother related questions
        keys.add("21");         //Whether bleeding continues?
        keys.add("22");         //No. of pads changed in last 24 hours?
        keys.add("23");         //If there is any foul smell discharge?
        keys.add("24");         //Advice the mother to:
        keys.add("25");         //Abnormal talks or behaviour observed?
        keys.add("26");         //Has fever?
        keys.add("27");         //Has headache with visual disturbances?
        keys.add("28");         //Difficulty in breastfeeding?
        keys.add("29");         //Does mother have any of following problems? (Tick all that apply)
        keys.add("9997");       //Form entry completed
        keys.add("9999");       //PNC Visit form completed.

        return keys;
    }

    /**
     * Set answer to pnc mother master.
     * @param key Key.
     * @param answer Answer for member's anc visit details.
     * @param pncMotherMaster Pnc mother master details.
     * @param keyAndAnswerMap Contains key and answers.
     */
    private void setAnswersToPncMotherMaster(String key, String answer,
                                             AshaPncMotherMaster pncMotherMaster, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "12":
                if (answer.equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    pncMotherMaster.setIsAlive(Boolean.FALSE);
                } else {
                    pncMotherMaster.setIsAlive(Boolean.TRUE);
                }
                break;
            case "121":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    pncMotherMaster.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "122":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    pncMotherMaster.setDeathPlace(answer);
                }
                break;
            case "123":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_DEATH)) {
                    if (!answer.equals(RchConstants.DEATH_REASON_OTHER)) {
                        pncMotherMaster.setDeathReason(answer);
                    } else {
                        pncMotherMaster.setOtherDeathReason(keyAndAnswerMap.get("125"));
                    }
                }
                break;
            case "21":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setBleedingContinues(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "22":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setPadsChangedIn24Hours(Integer.valueOf(answer));
                }
                break;
            case "23":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setFoulSmellDischarge(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "25":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setAbnormalBehaviour(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "26":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setHaveFever(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "27":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setHaveVisualDisturbances(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "28":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    pncMotherMaster.setDifficultyInBreastfeeding(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "29":
                if (keyAndAnswerMap.get("12").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && !answer.contains("NOPROB")) {
                    String[] problemsArray = answer.split(",");
                    Set<String> problemsSet = new HashSet<>(Arrays.asList(problemsArray));
                    pncMotherMaster.setProblemsPresent(problemsSet);
                }
                break;
            default:
        }
    }

    /**
     * Set answer to pnc child master.
     * @param key Key.
     * @param answer Answer for member's anc visit details.
     * @param pncChildMaster Pnc child master details.
     * @param keyAndAnswerMap Contains key and answers.
     * @param childCount  Total number of children.
     */
    private void setAnswersToPncChildMaster(String key, String answer, AshaPncChildMaster pncChildMaster,
                                            Map<String, String> keyAndAnswerMap, String childCount) {
        switch (key) {
            case "31":
                MemberEntity childEntity = memberDao.getMemberByUniqueHealthIdAndFamilyId(answer, null);
                pncChildMaster.setChildId(childEntity.getId());
                break;
            case "40":
                pncChildMaster.setIsAlive(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "401":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("F")) {
                        pncChildMaster.setDeathDate(new Date(Long.parseLong(answer)));
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("F")) {
                        pncChildMaster.setDeathDate(new Date(Long.parseLong(answer)));
                    }
                }
                break;
            case "402":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("F")) {
                        pncChildMaster.setDeathPlace(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("F")) {
                        pncChildMaster.setDeathPlace(answer);
                    }
                }
                break;
            case "403":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("F")) {
                        if (!answer.equals(RchConstants.DEATH_REASON_OTHER)) {
                            pncChildMaster.setDeathReason(answer);
                        } else {
                            pncChildMaster.setOtherDeathReason(keyAndAnswerMap.get("405" + "." + childCount));
                        }
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("F")) {
                        if (!answer.equals(RchConstants.DEATH_REASON_OTHER)) {
                            pncChildMaster.setDeathReason(answer);
                        } else {
                            pncChildMaster.setOtherDeathReason(keyAndAnswerMap.get("405"));
                        }
                    }
                }
                break;
            case "41":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setCry(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setCry(answer);
                    }
                }
                break;
            case "42":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setFedLessThanUsual(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setFedLessThanUsual(answer);
                    }
                }
                break;
            case "43":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setSucking(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setSucking(answer);
                    }
                }
                break;
            case "44":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setThrowsMilk(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setThrowsMilk(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                }
                break;
            case "45":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setHandsFeetsCold(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setHandsFeetsCold(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                }
                break;
            case "51":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setSkin(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setSkin(answer);
                    }
                }
                break;
            case "52":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setSkinPustules(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setSkinPustules(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                }
                break;
            case "53":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setHaveChestIndrawing(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setHaveChestIndrawing(ImtechoUtil.returnTrueFalseFromInitials(answer));
                    }
                }
                break;
            case "54":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setUmbilicus(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setUmbilicus(answer);
                    }
                }
                break;
            case "55":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setAbdomen(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setAbdomen(answer);
                    }
                }
                break;
            case "56":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        String[] split = answer.split("-");
                        if (split.length == 3 && split[0].equals("F")) {
                            pncChildMaster.setTempreature(Float.valueOf(split[1] + "." + split[2]));
                        }
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        String[] split = answer.split("-");
                        if (split.length == 3 && split[0].equals("F")) {
                            pncChildMaster.setTempreature(Float.valueOf(split[1] + "." + split[2]));
                        }
                    }
                }
                break;
            case "60":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setLimbsNeck(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setLimbsNeck(answer);
                    }
                }
                break;
            case "61":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")) {
                        pncChildMaster.setEyes(answer);
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")) {
                        pncChildMaster.setEyes(answer);
                    }
                }
                break;
            case "63":
                if (childCount != null) {
                    if (keyAndAnswerMap.get("40" + "." + childCount).equals("T")
                            && !answer.equals("T")) {
                        pncChildMaster.setWeight(Float.valueOf(answer));
                    }
                } else {
                    if (keyAndAnswerMap.get("40").equals("T")
                            && !answer.equals("T")) {
                        pncChildMaster.setWeight(Float.valueOf(answer));
                    }
                }
                break;
            default:
        }
    }
}
