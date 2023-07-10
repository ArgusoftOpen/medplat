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
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AshaChildServiceDao;
import com.argusoft.medplat.rch.model.AshaChildServiceMaster;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.service.AshaChildService;
import com.argusoft.medplat.rch.service.AshaReportedEventService;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Define services for ASHA child service.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AshaChildServiceImpl implements AshaChildService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private AshaChildServiceDao ashaChildServiceDao;

    @Autowired
    private ImmunisationService immunisationService;

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
    public Integer storeChildServiceVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity childEntity = memberDao.retrieveById(memberId);

        if (childEntity.getState() != null
                && FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_DEAD.contains(childEntity.getState())) {
            throw new ImtechoMobileException("Child is already marked as dead.", 1);
        }

        if (keyAndAnswerMap.get("2") != null) {
            childEntity.setFirstName(keyAndAnswerMap.get("2"));
        }

        String phone = keyAndAnswerMap.get("30");
        String isAadharScanned = keyAndAnswerMap.get("32");
        String aadharMap = null;
        String aadharNumber = null;
        if (isAadharScanned != null) {
            if (isAadharScanned.equals("1")) {
                aadharMap = keyAndAnswerMap.get("33");
            } else {
                String answer = keyAndAnswerMap.get("34");
                if (answer != null && !answer.equals("T")) {
                    aadharNumber = answer.replace("F/", "");
                }
            }
        }
        if (phone != null && childEntity.getMotherId() != null) {
            mobileFhsService.updateMemberDetailsFromRchForms(phone, null, null, null, null, memberDao.retrieveById(childEntity.getMotherId()));
        }
        mobileFhsService.updateMemberDetailsFromRchForms(null, aadharMap, aadharNumber, null, null, childEntity);

        AshaChildServiceMaster serviceMaster = new AshaChildServiceMaster();
        serviceMaster.setMemberId(memberId);
        serviceMaster.setFamilyId(familyId);
        serviceMaster.setLocationId(locationId);
        serviceMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));
        serviceMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        serviceMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToChildServiceMaster(key, answer, keyAndAnswerMap, serviceMaster);
        }

        ashaChildServiceDao.create(serviceMaster);
        updateMemberAdditionalInfo(memberDao.retrieveById(memberId), serviceMaster);

        if (serviceMaster.getMemberStatus().equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
            ashaReportedEventService.createNotificationForReportedEventByAsha(serviceMaster.getMemberId(),
                    MobileConstantUtil.NOTIFICATION_FHW_DEATH_CONF,
                    serviceMaster.getFamilyId(), serviceMaster.getLocationId(), serviceMaster.getId(),
                    MobileConstantUtil.ASHA_CS_VISIT);
            return serviceMaster.getId();
        }

        if (serviceMaster.getMemberStatus().equalsIgnoreCase(RchConstants.MEMBER_STATUS_MIGRATED)) {
            ashaReportedEventService.createNotificationForReportedEventByAsha(serviceMaster.getMemberId(),
                    MobileConstantUtil.NOTIFICATION_FHW_MEMBER_MIGRATION,
                    serviceMaster.getFamilyId(), serviceMaster.getLocationId(), serviceMaster.getId(),
                    MobileConstantUtil.ASHA_CS_VISIT);
            return serviceMaster.getId();
        }

        if (keyAndAnswerMap.containsKey("85")) {
            StringBuilder immunisationGiven = new StringBuilder();
            String answer = keyAndAnswerMap.get("85").replace(" ", "");
            String[] split = answer.split("-");
            for (String split1 : split) {
                String[] immunisation = split1.split("/");
                ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyId, childEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                        MobileConstantUtil.ASHA_CS_VISIT, serviceMaster.getId(), Integer.valueOf(parsedRecordBean.getNotificationId()),
                        immunisation[0].trim(), new Date(Long.parseLong(immunisation[2])), user.getId(), locationId, null);
                if (immunisationService.checkImmunisationEntry(immunisationMaster)) {
                    immunisationService.createImmunisationMaster(immunisationMaster);
                    immunisationGiven.append(immunisation[0].trim());
                    immunisationGiven.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                    immunisationGiven.append(sdf.format(new Date(Long.parseLong(immunisation[2]))));
                    immunisationGiven.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                }
            }

            if (immunisationGiven.length() > 1) {
                immunisationGiven.deleteCharAt(immunisationGiven.lastIndexOf(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR));
                if (childEntity.getImmunisationGiven() != null && childEntity.getImmunisationGiven().length() > 0) {
                    String sb = childEntity.getImmunisationGiven() + MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR + immunisationGiven;
                    childEntity.setImmunisationGiven(sb);
                } else {
                    childEntity.setImmunisationGiven(immunisationGiven.toString());
                }
                memberDao.update(childEntity);
            }
        }

        ashaChildServiceDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.ASHA_CS, serviceMaster.getId()));
        return serviceMaster.getId();
    }

    /**
     * Update additional info for child service.
     *
     * @param memberEntity           Member details.
     * @param ashaChildServiceMaster Child service visit details.
     */
    private void updateMemberAdditionalInfo(MemberEntity memberEntity, AshaChildServiceMaster ashaChildServiceMaster) {
        boolean isUpdate = false;
        String additionalInfo = memberEntity.getAdditionalInfo();
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;
        if (additionalInfo == null) {
            memberAdditionalInfo = new MemberAdditionalInfo();
        } else {
            memberAdditionalInfo = gson.fromJson(additionalInfo, MemberAdditionalInfo.class);
        }

        if (ashaChildServiceMaster.getWeight() != null) {
            if (memberAdditionalInfo.getWeightMap() != null && ashaChildServiceMaster.getWeight() != null) {
                memberAdditionalInfo.getWeightMap().put(ashaChildServiceMaster.getServiceDate().getTime(), ashaChildServiceMaster.getWeight());
            } else {
                LinkedHashMap<Long, Float> weightMap = new LinkedHashMap<>();
                weightMap.put(ashaChildServiceMaster.getServiceDate().getTime(), ashaChildServiceMaster.getWeight());
                memberAdditionalInfo.setWeightMap(weightMap);
            }
            isUpdate = true;
        }

        if (isUpdate) {
            memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            memberDao.update(memberEntity);
        }
    }

    /**
     * Set answer to child service  visit.
     *
     * @param key           Key.
     * @param answer        Answer for member's child visit details.
     * @param serviceMaster Child service visit details.
     */
    private void setAnswersToChildServiceMaster(String key, String answer, Map<String, String> keyAndAnswersMap, AshaChildServiceMaster serviceMaster) {
        switch (key) {
            case "-1":
                serviceMaster.setLongitude(answer);
                break;
            case "-2":
                serviceMaster.setLatitude(answer);
                break;
            case "20":
                serviceMaster.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "21":
                serviceMaster.setMemberStatus(answer);
                break;
            case "23":
                if (keyAndAnswersMap.get("21").equals("F")) {
                    serviceMaster.setDeathDate(new Date(Long.parseLong(answer)));
                }
                break;
            case "24":
                if (keyAndAnswersMap.get("21").equals("F")) {
                    serviceMaster.setDeathPlace(answer);
                }
                break;
            case "25":
                if (keyAndAnswersMap.get("21").equals("F")) {
                    if (!answer.equals("OTHER")) {
                        serviceMaster.setDeathReason(answer);
                    } else {
                        serviceMaster.setOtherDeathReason(keyAndAnswersMap.get("27"));
                    }
                }
                break;
            case "40":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setUnableToDrinkBreastfeed(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "41":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setVomitEverything(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "42":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHaveConvulsions(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "44":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHaveCoughOrFastBreathing(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "45":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("44").equals("T")) {
                    serviceMaster.setCoughDays(Integer.valueOf(answer));
                }
                break;
            case "46":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("44").equals("T")) {
                    serviceMaster.setBreathInOneMinute(Integer.valueOf(answer));
                }
                break;
            case "47":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("44").equals("T")) {
                    serviceMaster.setHaveChestIndrawing(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "48":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHaveMoreStoolsDiarrhea(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "49":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("48").equals("T")) {
                    serviceMaster.setDiarrheaDays(Integer.valueOf(answer));
                }
                break;
            case "50":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("48").equals("T")) {
                    serviceMaster.setBloodInStools(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "51":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("48").equals("T")) {
                    serviceMaster.setSunkenEyes(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "52":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setIrritableOrRestless(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "53":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setLethargicOrUnconsious(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "54":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setSkinBehaviourAfterPinching(answer);
                }
                break;
            case "55":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setDrinkingBehaviour(answer);
                }
                break;
            case "56":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHaveFever(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "57":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("56").equals("T")) {
                    serviceMaster.setFeverDays(Integer.valueOf(answer));
                }
                break;
            case "58":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("56").equals("T")
                        && Integer.parseInt(keyAndAnswersMap.get("57")) < 7) {
                    serviceMaster.setFeverPresentEachDay(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "59":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("56").equals("T")) {
                    serviceMaster.setHaveFeverOnService(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "60":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)
                        && keyAndAnswersMap.get("56").equals("T")) {
                    serviceMaster.setIsNeckStiff(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "61":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHavePalmerPoller(answer);
                }
                break;
            case "62":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHaveSevereWasting(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "63":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setHaveEdemaBothFeet(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "64":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    String[] split = answer.split("/");
                    serviceMaster.setWeight(Float.valueOf(split[0]));
                    serviceMaster.setDateOfWeight(new Date(Long.parseLong(split[1])));
                }
                break;
            case "67":
                if (keyAndAnswersMap.get("21").equals(RchConstants.MEMBER_STATUS_AVAILABLE)) {
                    serviceMaster.setMotherInformedAboutGrade(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            default:
        }
    }
}
