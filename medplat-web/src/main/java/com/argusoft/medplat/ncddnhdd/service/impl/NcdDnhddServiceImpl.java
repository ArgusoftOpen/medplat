package com.argusoft.medplat.ncddnhdd.service.impl;

import com.argusoft.medplat.web.users.dao.RoleDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.FamilyAdditionalInfo;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.mapper.MemberMapper;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.web.healthinfra.constants.HealthInfrastructureConstants;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncddnhdd.dao.*;
import com.argusoft.medplat.ncddnhdd.dto.*;
import com.argusoft.medplat.ncddnhdd.enums.*;
import com.argusoft.medplat.ncddnhdd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncddnhdd.model.*;
import com.argusoft.medplat.ncddnhdd.dao.CancerScreeningDao;
import com.argusoft.medplat.ncddnhdd.dao.HypertensionDiabetesAndMentalHealthDao;
import com.argusoft.medplat.ncddnhdd.dao.MemberCbacAndNutritionMasterDao;
import com.argusoft.medplat.ncddnhdd.dto.MemberNcdDetailDto;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDnhddDto;
import com.argusoft.medplat.ncddnhdd.dto.MemberRegistrationDto;
import com.argusoft.medplat.ncddnhdd.model.CancerScreeningMaster;
import com.argusoft.medplat.ncddnhdd.model.HypertensionDiabetesAndMentalHealthMaster;
import com.argusoft.medplat.ncddnhdd.model.MemberCbacAndNutritionMaster;
import com.argusoft.medplat.ncddnhdd.service.NcdDnhddService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.nutrition.dao.ChildNutritionSamScreeningDao;
import com.argusoft.medplat.nutrition.model.ChildNutritionSamScreening;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kunjan
 */
@Service
@Transactional
public class NcdDnhddServiceImpl implements NcdDnhddService {
    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberCbacDetailDao cbacDetailDao;
    @Autowired
    private MemberOralDetailDao oralDetailDao;
    @Autowired
    private MemberMentalHealthDetailDao mentalHealthDetailDao;
    @Autowired
    private MemberDiabetesDetailDao diabetesDetailDao;
    @Autowired
    private MemberBreastDetailDao breastDetailDao;
    @Autowired
    private MemberCervicalDetailDao cervicalDetailDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private MemberHypertensionDetailDao hypertensionDetailDao;
    @Autowired
    private ImtechoSecurityUser user;
    @Autowired
    private MedicineMasterDao medicineMasterDao;
    @Autowired
    private MemberDiseaseDiagnosisDao memberDiseaseDiagnosisDao;
    @Autowired
    private MemberDiseaseMedicineDao memberDiseaseMedicineDao;
    @Autowired
    private MemberDiseaseFollowupDao memberDiseaseFollowupDao;
    @Autowired
    private HealthInfrastructureDetailsDao healthInfrastructureDetailsDao;
    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;
    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;
    @Autowired
    private NcdMasterDao ncdMasterDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ChildNutritionSamScreeningDao childNutritionSamScreeningDao;
    @Autowired
    private MemberCbacAndNutritionMasterDao memberCbacAndNutritionMasterDao;
    @Autowired
    private CancerScreeningDao cancerScreeningDao;
    @Autowired
    private HypertensionDiabetesAndMentalHealthDao hypertensionDiabetesAndMentalHealthDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private EventHandler eventHandler;

    private static final String FIELD_KEY_FOR_CHRONIC_DISEASE = "1007";
    private static final Gson gson = new Gson();

    private void updateFamilyAdditionalInfo(FamilyEntity familyEntity, Date screeningDate) {
        String financialYear = ImtechoUtil.getFinancialYearFromDate(screeningDate);
        if (checkLastNcdScreeningDone(familyEntity.getFamilyId(), financialYear)) {
            FamilyAdditionalInfo additionalInfo;
            if (familyEntity.getAdditionalInfo() != null) {
                additionalInfo = gson.fromJson(familyEntity.getAdditionalInfo(), FamilyAdditionalInfo.class);
            } else {
                additionalInfo = new FamilyAdditionalInfo();
            }
            additionalInfo.setLastMemberNcdScreeningDate(screeningDate.getTime());
            familyEntity.setAdditionalInfo(gson.toJson(additionalInfo));
        }
    }

    private boolean checkLastNcdScreeningDone(String familyId, String financialYear) {
        List<MemberEntity> members = memberDao.retrieveMemberEntitiesByFamilyId(familyId);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -30);
        for (MemberEntity member : members) {
            if (member.getDob() != null && member.getDob().before(instance.getTime())
                    && member.getState() != null && !FamilyHealthSurveyServiceConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES.contains(member.getState())
                    && !getAllScreeningDoneForMember(financialYear, member)) {
                return false;
            }
        }
        return true;
    }

    private boolean getAllScreeningDoneForMember(String financialYear, MemberEntity member) {
        if (member.getAdditionalInfo() == null || member.getAdditionalInfo().isEmpty()) {
            return false;
        }

        MemberAdditionalInfo additionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        if (member.getGender().equals("M") || member.getGender().equals("T")) {
            return additionalInfo.getHypYear() != null && additionalInfo.getHypYear().contains(financialYear)
                    && additionalInfo.getDiabetesYear() != null && additionalInfo.getDiabetesYear().contains(financialYear)
                    && additionalInfo.getOralYear() != null && additionalInfo.getOralYear().contains(financialYear);
        } else if (member.getGender().equals("F")) {
            return additionalInfo.getHypYear() != null && additionalInfo.getHypYear().contains(financialYear)
                    && additionalInfo.getDiabetesYear() != null && additionalInfo.getDiabetesYear().contains(financialYear)
                    && additionalInfo.getOralYear() != null && additionalInfo.getOralYear().contains(financialYear)
                    && additionalInfo.getBreastYear() != null && additionalInfo.getBreastYear().contains(financialYear)
                    && additionalInfo.getCervicalYear() != null && additionalInfo.getCervicalYear().contains(financialYear);
        }
        return false;
    }

    @Override
    public Integer storeCbacAndNutritionForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(member.getFamilyId());

        Map<Integer, String> memberAbhaConsentMap = new LinkedHashMap<>();

        MemberCbacAndNutritionMaster memberCbacAndNutritionMaster = new MemberCbacAndNutritionMaster();
        memberCbacAndNutritionMaster.setMemberId(memberId);
        memberCbacAndNutritionMaster.setFamilyId(familyEntity.getId());
        memberCbacAndNutritionMaster.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberCbacAndNutritionMaster.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("10"))));
        Integer masterId = memberCbacAndNutritionMasterDao.create(memberCbacAndNutritionMaster);

        MemberCbacDetail memberCbacDetail = new MemberCbacDetail();
        memberCbacDetail.setMemberId(memberId);
        memberCbacDetail.setFamilyId(familyEntity.getId());
        memberCbacDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberCbacDetail.setLongitude(keyAndAnswerMap.get("-1"));
        memberCbacDetail.setLatitude(keyAndAnswerMap.get("-2"));
        memberCbacDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        memberCbacDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        memberCbacDetail.setDoneOn(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("10")));
        memberCbacDetail.setCbacAndNutritionMasterId(masterId);
        memberCbacDetail.setDoneBy(getDoneByForCbacFor(user));

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            setAnswersToMemberCbacAndNutritionDetail(key, answer, memberCbacDetail, memberAbhaConsentMap, member);
        }

        Integer cbacTableId = cbacDetailDao.create(memberCbacDetail);
        memberCbacAndNutritionMaster.setCbacTableId(cbacTableId.toString());
        cbacDetailDao.flush();

        ChildNutritionSamScreening samScreening = null;
        if (keyAndAnswerMap.containsKey("232")
                || keyAndAnswerMap.containsKey("233")
                || keyAndAnswerMap.containsKey("234")
                || keyAndAnswerMap.containsKey("236")) {
            samScreening = new ChildNutritionSamScreening();
            samScreening.setMemberId(memberId);
            samScreening.setFamilyId(familyEntity.getId());
            samScreening.setDoneBy(user.getId());
            samScreening.setRoleId(roleDao.retrieveByCode("ASHA").getId());
            samScreening.setDoneFrom(MobileConstantUtil.DNHDD_NCD_CBAC_AND_NUTRITION);
            samScreening.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("10"))));
            samScreening.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
            samScreening.setCbacAndNutritionMasterId(masterId);
            if (keyAndAnswerMap.containsKey("232")) {
                samScreening.setHeight(Integer.valueOf(keyAndAnswerMap.get("232")));
            }
            samScreening.setWeight(Float.valueOf(keyAndAnswerMap.get("233")));
            if (keyAndAnswerMap.containsKey("234")) {
                samScreening.setMuac(Float.valueOf(keyAndAnswerMap.get("234")));
            }
            samScreening.setSdScore(keyAndAnswerMap.get("236"));
            Integer nutritionTableId = childNutritionSamScreeningDao.create(samScreening);
            memberCbacAndNutritionMaster.setChildNutritionTableId(nutritionTableId.toString());
            childNutritionSamScreeningDao.flush();
            eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.DNHDD_NCD_CBAC_AND_NUTRITION, samScreening.getId()));
        }

        memberCbacAndNutritionMasterDao.update(memberCbacAndNutritionMaster);
        updateMemberAdditionalInfoFromCbacAndNutrition(member, memberCbacDetail, samScreening);
        memberDao.update(member);
//        saveMemberAbhaConsent(memberAbhaConsentMap);

        return memberCbacAndNutritionMaster.getId();
    }

    public MemberCbacDetail.DoneBy getDoneByForCbacFor(UserMaster user) {
        switch (user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()) {
            case MobileConstantUtil.Roles.ASHA:
            case "ASHA":
                return MemberCbacDetail.DoneBy.ASHA;
            case MobileConstantUtil.Roles.FHW:
            case MobileConstantUtil.Roles.ANM:
                return MemberCbacDetail.DoneBy.FHW;
            case MobileConstantUtil.Roles.CHO_HWC:
            case MobileConstantUtil.Roles.CHO:
                return MemberCbacDetail.DoneBy.CHO;
            case "MPW":
                return MemberCbacDetail.DoneBy.MPW;
            case "RBSK":
                return MemberCbacDetail.DoneBy.RBSK;
            default:
                return null;
        }
    }

    @Override
    public MemberBreastDetail retrieveLastRecordForBreastByMemberId(Integer memberId) {
        return breastDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    private void updateMemberAdditionalInfoFromCbacAndNutrition(
            MemberEntity memberEntity, MemberCbacDetail memberCbacDetail, ChildNutritionSamScreening samScreening) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setCbacDate(memberCbacDetail.getDoneOn().getTime());
        memberAdditionalInfo.setHmisId(memberCbacDetail.getHmisId());
        if (memberCbacDetail.getScore() != null) {
            memberAdditionalInfo.setCbacScore(memberCbacDetail.getScore());
        }
        if (memberCbacDetail.getBmi() != null) {
            memberAdditionalInfo.setBmi(memberCbacDetail.getBmi());
        }
        if (samScreening != null) {
            memberAdditionalInfo.setLastSamScreeningDate(samScreening.getServiceDate().getTime());
            memberAdditionalInfo.setSdScore(samScreening.getSdScore());
            String sdScore = samScreening.getSdScore();
            Float muac = samScreening.getMuac();
            if (muac != null && muac < 11.5f || (sdScore != null && (sdScore.equals("SD4") || sdScore.equals("SD3")))) {
                memberAdditionalInfo.setNutritionStatus(SystemConstantUtil.SUSPECTED_SAM_FROM_ASHA);
            } else if (muac != null && (muac >= 11.5f && muac <= 12.5f) || (sdScore != null && sdScore.equals("SD2"))) {
                memberAdditionalInfo.setNutritionStatus(SystemConstantUtil.SUSPECTED_MAM_FROM_ASHA);
            } else {
                memberAdditionalInfo.setNutritionStatus(SystemConstantUtil.NORMAL);
            }
        }
        memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void setAnswersToMemberCbacAndNutritionDetail(String key, String answer,
                                                          MemberCbacDetail memberCbacDetail,
                                                          Map<Integer, String> memberAbhaConsentMap,
                                                          MemberEntity memberEntity) {
        switch (key) {
            case "-1":
                memberCbacDetail.setLongitude(answer);
                break;
            case "-2":
                memberCbacDetail.setLatitude(answer);
                break;

            //store abha consents
            case "150":
                if (answer != null && answer.contains("ABHACONSENT")) {
                    memberAbhaConsentMap.put(memberEntity.getId(), answer);
                }
                break;

            //update member info in member table
//            case "2141":
//                memberEntity.setOtherDisability(answer);
//                break;
            case "666":
                Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                for (String id : answer.split(",")) {
                    if (!"NONE".equals(id) && !"OTHER".equals(id) && !id.equals("null")) {
                        chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                    }
                }
                memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                break;
            case "667":
                Set<Integer> eyeRelEntities = new HashSet<>();
                for (String id : answer.split(",")) {
                    if (!"NONE".equals(id) && !"OTHER".equals(id) && !id.equals("null")) {
                        eyeRelEntities.add(Integer.parseInt(id));
                    }
                }
                memberEntity.setEyeIssueDetails(eyeRelEntities);
//                memberEntity.setOtherEyeIssue(null);
                break;
            case "668":
                memberEntity.setSickleCellStatus(answer);
                break;
            case "669":
                memberEntity.setCataractSurgery(answer);
                break;

            //Risk Assessment
            case "214":
                memberCbacDetail.setKnownDisabilities(answer);
                memberEntity.setPhysicalDisability(answer);
                break;
            case "12":
                memberCbacDetail.setSmoke(answer);
                break;
            case "112":
                memberCbacDetail.setConsumeGutka(answer);
                break;
            case "13":
                memberCbacDetail.setConsumeAlcohol(answer);
                break;
            case "14":
                try {
                    memberCbacDetail.setHmisId(Long.parseLong(answer));
                } catch (NumberFormatException e) {
                    Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage());
                }
                break;
            case "15":
                memberCbacDetail.setWaist(answer);
                break;
            case "16":
                memberCbacDetail.setPhysicalActivity30min(answer);
                break;
            case "17":
                memberCbacDetail.setBpDiabetesHeartHistory(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "50":
                memberCbacDetail.setScore(Integer.valueOf(answer));
                break;

            //Early Detection
            case "18":
                memberCbacDetail.setShortnessOfBreath(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "19":
                memberCbacDetail.setFitsHistory(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "20":
                memberCbacDetail.setTwoWeeksCoughing(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "21":
                memberCbacDetail.setMouthOpeningDifficulty(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "22":
                memberCbacDetail.setBloodInSputum(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "24":
                memberCbacDetail.setTwoWeeksFever(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "25":
                memberCbacDetail.setChangeInToneOfVoice(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "26":
                memberCbacDetail.setLossOfWeight(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "27":
                memberCbacDetail.setPatchOnSkin(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "28":
                memberCbacDetail.setNightSweats(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "29":
                memberCbacDetail.setDifficultyHoldingObjects(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "30":
                memberCbacDetail.setSensationLossPalm(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "1008":
                memberCbacDetail.setTakingAntiTbDrugs(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "32":
                memberCbacDetail.setFamilyMemberSufferingFromTb(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "35":
                memberCbacDetail.setLumpInBreast(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "36":
                memberCbacDetail.setBleedingAfterMenopause(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "37":
                memberCbacDetail.setNippleBloodStainedDischarge(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "38":
                memberCbacDetail.setBleedingAfterIntercourse(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "39":
                memberCbacDetail.setChangeInSizeOfBreast(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "40":
                memberCbacDetail.setFoulVaginalDischarge(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "41":
                memberCbacDetail.setBleedingBetweenPeriods(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "42":
                memberCbacDetail.setOccupationalExposure(answer);
                break;
            case "313":
                memberCbacDetail.setDiagnosedForCOPD(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            //Personal Examination
            case "401":
                memberCbacDetail.setHeight(Integer.valueOf(answer));
                break;
            case "402":
                memberCbacDetail.setWeight(Float.valueOf(answer));
                break;
            case "403":
                String[] split = answer.split("/");
                if (split.length == 3) {
                    try {
                        memberCbacDetail.setHeight(Integer.valueOf(split[0]));
                        memberCbacDetail.setWeight(Float.valueOf(split[1]));
                        memberCbacDetail.setBmi(Float.valueOf(split[2]));
                    } catch (NumberFormatException e) {
                        memberCbacDetail.setWeight(Float.valueOf(split[0]));
                        memberCbacDetail.setHeight(Integer.valueOf(split[1].split("\\.")[0]));
                        memberCbacDetail.setBmi(Float.valueOf(split[2]));
                    }
                }
                break;
            // as per CBAC revised form
            case "2008":
                memberCbacDetail.setInterestDoingThings(answer);
                break;
            case "2009":
                memberCbacDetail.setFeelingDown(answer);
                break;
            case "405":
                memberCbacDetail.setFeelingUnsteady(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "406":
                memberCbacDetail.setPhysicalDisability(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "407":
                memberCbacDetail.setNeedHelpFromOthers(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "230":
                memberCbacDetail.setMouthUlcers(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "2055":
                memberCbacDetail.setGrowthInMouth(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "2056":
                memberCbacDetail.setMouthPatch(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "1015":
                memberCbacDetail.setChewingPain(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "3001":
                memberCbacDetail.setCloudyVision(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "3002":
                memberCbacDetail.setBlurredVisionEye(answer);
                break;
            case "3003":
                memberCbacDetail.setHearingDifficulty(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "2201":
                memberCbacDetail.setTreatmentForLeprosy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
        }
    }

    @Override
    public Integer storeHypertensionDiabetesAndMentalHealthForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        MemberEntity member = memberDao.retrieveById(Integer.valueOf(keyAndAnswerMap.get("-4")));
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(member.getFamilyId());

        HypertensionDiabetesAndMentalHealthMaster memberMaster = new HypertensionDiabetesAndMentalHealthMaster();
        memberMaster.setMemberId(member.getId());
        memberMaster.setFamilyId(familyEntity.getId());
        memberMaster.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberMaster.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("7"))));
        hypertensionDiabetesAndMentalHealthDao.create(memberMaster);

        MemberHypertensionDetail hypertensionDetail = new MemberHypertensionDetail();
        hypertensionDetail.setMemberId(member.getId());
        hypertensionDetail.setFamilyId(familyEntity.getId());
        hypertensionDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        hypertensionDetail.setLongitude(keyAndAnswerMap.get("-1"));
        hypertensionDetail.setLatitude(keyAndAnswerMap.get("-2"));
        hypertensionDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        hypertensionDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        hypertensionDetail.setDoneOn(new Date());
        hypertensionDetail.setHyperDiaMentalMasterId(memberMaster.getId());

        MemberDiabetesDetail diabetesDetail = new MemberDiabetesDetail();
        diabetesDetail.setMemberId(member.getId());
        diabetesDetail.setFamilyId(familyEntity.getId());
        diabetesDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        diabetesDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        diabetesDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        diabetesDetail.setDoneOn(new Date());
        diabetesDetail.setLongitude(keyAndAnswerMap.get("-1"));
        diabetesDetail.setLatitude(keyAndAnswerMap.get("-2"));
        diabetesDetail.setHyperDiaMentalMasterId(memberMaster.getId());

        MemberMentalHealthDetails mentalHealthDetails = new MemberMentalHealthDetails();
        mentalHealthDetails.setMemberId(member.getId());
        mentalHealthDetails.setFamilyId(familyEntity.getId());
        mentalHealthDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        mentalHealthDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        mentalHealthDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        mentalHealthDetails.setDoneOn(new Date());
        mentalHealthDetails.setLongitude(keyAndAnswerMap.get("-1"));
        mentalHealthDetails.setLatitude(keyAndAnswerMap.get("-2"));
        mentalHealthDetails.setHyperDiaMentalMasterId(memberMaster.getId());

        //Done by
        hypertensionDetail.setDoneBy(DoneBy.valueOf(user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()));
        diabetesDetail.setDoneBy(DoneBy.valueOf(user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()));
        mentalHealthDetails.setDoneBy(DoneBy.valueOf(user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()));

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            setAnswersToMemberHypertensionDiabetesAndMentalHealthDetail(key, answer, hypertensionDetail, diabetesDetail, mentalHealthDetails, keyAndAnswerMap, member);
        }

        hypertensionDetail.setReferralId(storeMemberReferralFromForms(
                hypertensionDetail.getMemberId(), hypertensionDetail.getLocationId(),
                DiseaseCode.HT, user, new Date(), hypertensionDetail.getHealthInfraId()));
        diabetesDetail.setReferralId(storeMemberReferralFromForms(
                diabetesDetail.getMemberId(), diabetesDetail.getLocationId(),
                DiseaseCode.D, user, new Date(), diabetesDetail.getHealthInfraId()));
        mentalHealthDetails.setReferralId(storeMemberReferralFromForms(
                mentalHealthDetails.getMemberId(), mentalHealthDetails.getLocationId(),
                DiseaseCode.MH, user, new Date(), mentalHealthDetails.getHealthInfraId()));

        hypertensionDetailDao.create(hypertensionDetail);
        diabetesDetailDao.create(diabetesDetail);
        mentalHealthDetailDao.create(mentalHealthDetails);

        memberMaster.setHypertensionId(hypertensionDetail.getId());
        memberMaster.setDiabetesId(diabetesDetail.getId());
        memberMaster.setMentalHealthId(mentalHealthDetails.getId());

        updateMemberAdditionalInfoFromHypertension(member, hypertensionDetail);
        updateMemberAdditionalInfoFromDiabetes(member, diabetesDetail, false);
        updateMemberAdditionalInfoFromMentalHealth(member, mentalHealthDetails);

        updateFamilyAdditionalInfo(familyEntity, hypertensionDetail.getScreeningDate());

        memberDao.update(member);
        familyDao.update(familyEntity);

        hypertensionDetailDao.flush();
        return memberMaster.getId();
    }

    private Integer storeMemberReferralFromForms(Integer memberId, Integer locationId,
                                                 DiseaseCode diseaseCode, UserMaster user,
                                                 Date referredOn, Integer infraId) {
        if (infraId == null) {
            return null;
        }

        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setMemberId(memberId);
        memberReferral.setMemberLocation(locationId);
        memberReferral.setDiseaseCode(diseaseCode);
        memberReferral.setReferredBy(user.getId());
        memberReferral.setReferredOn(referredOn);
        memberReferral.setReferredFromHealthInfrastructureId(infraId);
        memberReferral.setState(State.PENDING);
        memberReferral.setReferredFrom(getReferredFromForMemberReferral(user));

        if (infraId == -1) {
            memberReferral.setReferredTo(ReferralPlace.PVT);
        } else {
            HealthInfrastructureDetails healthInfra = healthInfrastructureDetailsDao.retrieveById(infraId);
            memberReferral.setLocationId(healthInfra.getLocationId());
            memberReferral.setReferredTo(getReferralPlaceFromInfraTypeForMemberReferral(healthInfra.getType()));
        }
        memberReferralDao.create(memberReferral);
        return memberReferral.getId();
    }

    public static ReferralPlace getReferredFromForMemberReferral(UserMaster user) {
        switch (user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()) {
            case MobileConstantUtil.Roles.ASHA:
            case "ASHA":
                return ReferralPlace.ASHA;
            case MobileConstantUtil.Roles.FHW:
            case MobileConstantUtil.Roles.ANM:
                return ReferralPlace.FHW;
            case MobileConstantUtil.Roles.CHO_HWC:
            case MobileConstantUtil.Roles.CHO:
                return ReferralPlace.CHO;
            case MobileConstantUtil.Roles.MPHW:
                return ReferralPlace.MPHW;
            case "MPW":
                return ReferralPlace.MPW;
            case "RBSK":
                return ReferralPlace.RBSK;
            default:
                return ReferralPlace.OTHERS;
        }
    }

    public static ReferralPlace getReferralPlaceFromInfraTypeForMemberReferral(Integer infraType) {
        if (HealthInfrastructureConstants.INFRA_DISTRICT_HOSPITAL.equals(infraType)) {
            return ReferralPlace.D;
        } else if (HealthInfrastructureConstants.INFRA_SUB_DISTRICT_HOSPITAL.equals(infraType)) {
            return ReferralPlace.B;
        } else if (HealthInfrastructureConstants.INFRA_COMMUNITY_HEALTH_CENTER.equals(infraType)) {
            return ReferralPlace.C;
        } else if (HealthInfrastructureConstants.INFRA_URBAN_COMMUNITY_HEALTH_CENTER.equals(infraType)) {
            return ReferralPlace.C;
        } else if (HealthInfrastructureConstants.INFRA_PHC.equals(infraType)) {
            return ReferralPlace.P;
        } else if (HealthInfrastructureConstants.INFRA_UPHC.equals(infraType)) {
            return ReferralPlace.U;
        } else if (HealthInfrastructureConstants.INFRA_SC.equals(infraType)) {
            return ReferralPlace.SC;
        } else if (HealthInfrastructureConstants.INFRA_TRUST_HOSPITAL.equals(infraType)) {
            return ReferralPlace.T;
        } else if (HealthInfrastructureConstants.INFRA_PRIVATE_HOSPITAL.equals(infraType)) {
            return ReferralPlace.PVT;
        } else if (HealthInfrastructureConstants.INFRA_MEDICAL_COLLEGE_HOSPITAL.equals(infraType)) {
            return ReferralPlace.M;
        } else if (HealthInfrastructureConstants.INFRA_GRANT_IN_AID.equals(infraType)) {
            return ReferralPlace.G;
        } else {
            return ReferralPlace.OTHERS;
        }
    }

    private void updateMemberAdditionalInfoFromHypertension(MemberEntity member, MemberHypertensionDetail hypertensionDetail) {
        MemberAdditionalInfo memberAdditionalInfo = new MemberAdditionalInfo();
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        }

        memberAdditionalInfo.setLastServiceLongDate(hypertensionDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setHypDiaMentalServiceDate(hypertensionDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setHypYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getHypYear(), ImtechoUtil.getFinancialYearFromDate(hypertensionDetail.getScreeningDate())));
        if (hypertensionDetail.getSystolicBp() != null) {
            memberAdditionalInfo.setSystolicBp(hypertensionDetail.getSystolicBp());
        }
        if (hypertensionDetail.getDiastolicBp() != null) {
            memberAdditionalInfo.setDiastolicBp(hypertensionDetail.getDiastolicBp());
        }
        if (Objects.nonNull(hypertensionDetail.getSuspected()) && hypertensionDetail.getSuspected()) {
            memberAdditionalInfo.setSuspectedForHypertension(true);
        }
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromDiabetes(MemberEntity member, MemberDiabetesDetail diabetesDetail, boolean isDiabetes) {
        MemberAdditionalInfo memberAdditionalInfo = new MemberAdditionalInfo();
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        }

        memberAdditionalInfo.setLastServiceLongDate(diabetesDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setDiabetesYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getDiabetesYear(), ImtechoUtil.getFinancialYearFromDate(diabetesDetail.getScreeningDate())));
        if (diabetesDetail.getBloodSugar() != null) {
            memberAdditionalInfo.setBloodSugar(diabetesDetail.getBloodSugar());
        }
        if (Boolean.TRUE.equals(diabetesDetail.getSuspected())) {
            memberAdditionalInfo.setSuspectedForDiabetes(true);
        }
        if (isDiabetes) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.D.toString()));
        }
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromMentalHealth(MemberEntity member, MemberMentalHealthDetails mentalHealthDetails) {
        MemberAdditionalInfo memberAdditionalInfo = new MemberAdditionalInfo();
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        }

        memberAdditionalInfo.setLastServiceLongDate(mentalHealthDetails.getScreeningDate().getTime());
        memberAdditionalInfo.setMentalHealthYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getMentalHealthYear(), ImtechoUtil.getFinancialYearFromDate(mentalHealthDetails.getScreeningDate())));
        if ((mentalHealthDetails.getObservation() != null && !mentalHealthDetails.getObservation().contains("NONE"))
                || mentalHealthDetails.getOtherProblems() != null && !mentalHealthDetails.getOtherProblems().contains("NONE")) {
            mentalHealthDetails.setIsSuspected(true);
            memberAdditionalInfo.setSuspectedForMentalHealth(true);
        }
        if (mentalHealthDetails.getDoesSuffering() != null) {
            memberAdditionalInfo.setSufferingMentalHealth(mentalHealthDetails.getDoesSuffering());
        }
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void setAnswersToMemberHypertensionDiabetesAndMentalHealthDetail(String key, String answer, MemberHypertensionDetail hypertensionDetail,
                                                                             MemberDiabetesDetail diabetesDetail,
                                                                             MemberMentalHealthDetails mentalHealthDetails,
                                                                             Map<String, String> keyAndAnswerMap,
                                                                             MemberEntity memberEntity) {
        switch (key) {
            //update member info in member table
            case "666":
                Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                for (String id : answer.split(",")) {
                    if (!"NONE".equals(id) && !"OTHER".equals(id) && !id.equals("null")) {
                        chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                    }
                }
                memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                break;
            case "7":
                hypertensionDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                diabetesDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                mentalHealthDetails.setScreeningDate(new Date(Long.parseLong(answer)));
                break;
            //Hypertension related entries
            case "8":
                hypertensionDetail.setDiagnosedEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                if (keyAndAnswerMap.get("8").equals("1")) {
                    hypertensionDetail.setCurrentlyUnderTreatement(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "11":
                if (keyAndAnswerMap.get("10").equals("1")) {
                    hypertensionDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "12":
                if (keyAndAnswerMap.get("10").equals("1") && keyAndAnswerMap.get("11").equals("GOVERNMENT")) {
                    hypertensionDetail.setGovtFacilityId(Integer.valueOf(answer));
                }
                break;
            case "120":
                if (keyAndAnswerMap.get("10").equals("1") && keyAndAnswerMap.get("11").equals("PRIVATE")) {
                    hypertensionDetail.setPrivateFacility(answer);
                }
                break;
            case "121":
                if (keyAndAnswerMap.get("10").equals("1") && keyAndAnswerMap.get("11").equals("OUTOFTERRITORY")) {
                    hypertensionDetail.setOutOfTerritoryFacility(answer);
                }
                break;
            case "18":
                String[] arr = answer.split("-");
                if (arr.length > 1) {
                    hypertensionDetail.setSystolicBp(Integer.valueOf(arr[1].split("\\.")[0]));
                    hypertensionDetail.setDiastolicBp(Integer.valueOf(arr[2].split("\\.")[0]));
                }
                break;
            case "19":
                hypertensionDetail.setSuspected(answer.equals("suspected") || answer.equals("uncontrolled"));
                break;
            case "20":
                String[] arr1 = answer.split("-");
                if (arr1.length > 1) {
                    hypertensionDetail.setSystolicBp2(Integer.valueOf(arr1[1].split("\\.")[0]));
                    hypertensionDetail.setDiastolicBp2(Integer.valueOf(arr1[2].split("\\.")[0]));
                }
                break;
            case "49":
                answer = answer.replace(".", "");
                if (!answer.isEmpty()) {
                    hypertensionDetail.setPulseRate(Integer.valueOf(answer));
                }
                break;
            case "21":
                answer = answer.replace(".", "");
                if (!answer.isEmpty()) {
                    hypertensionDetail.setPulseRate2(Integer.valueOf(answer));
                }
                break;
            //Diabetes related entries
            case "9":
                diabetesDetail.setEarlierDiabetesDiagnosis(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "13":
                if (keyAndAnswerMap.get("9").equals("1")) {
                    diabetesDetail.setCurrentlyUnderTreatment(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "14":
                if (keyAndAnswerMap.get("13").equals("1")) {
                    diabetesDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "15":
                diabetesDetail.setSuspected(answer.equals("suspected") || answer.equals("uncontrolled"));
                break;
            case "45":
                if (keyAndAnswerMap.get("13").equals("1") && keyAndAnswerMap.get("14").equals("GOVERNMENT")) {
                    diabetesDetail.setGovtFacilityId(Integer.valueOf(answer));
                }
                break;
            case "450":
                if (keyAndAnswerMap.get("13").equals("1") && keyAndAnswerMap.get("14").equals("PRIVATE")) {
                    diabetesDetail.setPrivateFacility(answer);
                }
                break;
            case "451":
                if (keyAndAnswerMap.get("13").equals("1") && keyAndAnswerMap.get("14").equals("OUTOFTERRITORY")) {
                    diabetesDetail.setOutOfTerritoryFacility(answer);
                }
                break;
            case "16":
                diabetesDetail.setMeasurementType(answer);
                break;
            case "17":
                diabetesDetail.setBloodSugar(Integer.valueOf(answer.replace(".", "")));
                break;
            //Mental Health related entries
            case "24":
                mentalHealthDetails.setSufferingEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "25":
                if (keyAndAnswerMap.get("24").equals("1")) {
                    mentalHealthDetails.setDiagnosis(answer);
                }
                break;
            case "26":
                if (keyAndAnswerMap.get("24").equals("1")) {
                    mentalHealthDetails.setCurrentlyUnderTreatement(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "27":
                if (keyAndAnswerMap.get("26").equals("1")) {
                    mentalHealthDetails.setCurrentTreatmentPlace(answer);
                }
                break;
            case "28":
                if (keyAndAnswerMap.get("26").equals("1") && keyAndAnswerMap.get("27").equals("GOVERNMENT")) {
                    mentalHealthDetails.setGovtFacilityId(Integer.valueOf(answer));
                }
                break;
            case "280":
                if (keyAndAnswerMap.get("26").equals("1") && keyAndAnswerMap.get("27").equals("PRIVATE")) {
                    mentalHealthDetails.setPrivateFacility(answer);
                }
                break;
            case "281":
                if (keyAndAnswerMap.get("26").equals("1") && keyAndAnswerMap.get("27").equals("OUTOFTERRITORY")) {
                    mentalHealthDetails.setOutOfTerritoryFacility(answer);
                }
                break;
            case "29":
                mentalHealthDetails.setObservation(answer);
                break;
            case "4551":
                mentalHealthDetails.setOtherProblems(answer);
                break;
            case "31":
                hypertensionDetail.setRefferaldone(Boolean.TRUE);
                hypertensionDetail.setHealthInfraId(Integer.valueOf(answer));
                diabetesDetail.setRefferalDone(Boolean.TRUE);
                diabetesDetail.setHealthInfraId(Integer.valueOf(answer));
                mentalHealthDetails.setReferralDone(Boolean.TRUE);
                mentalHealthDetails.setHealthInfraId(Integer.valueOf(answer));
                break;
            default:
        }
    }

    @Override
    public Integer storeCancerForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        MemberEntity member = memberDao.retrieveById(Integer.valueOf(keyAndAnswerMap.get("-4")));
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(member.getFamilyId());

        CancerScreeningMaster cancerScreeningMaster = new CancerScreeningMaster();
        cancerScreeningMaster.setMemberId(member.getId());
        cancerScreeningMaster.setFamilyId(familyEntity.getId());
        cancerScreeningMaster.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        cancerScreeningMaster.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("6"))));
        cancerScreeningDao.create(cancerScreeningMaster);

        MemberOralDetail oralDetail = new MemberOralDetail();
        oralDetail.setMemberId(member.getId());
        oralDetail.setFamilyId(familyEntity.getId());
        oralDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        oralDetail.setLongitude(keyAndAnswerMap.get("-1"));
        oralDetail.setLatitude(keyAndAnswerMap.get("-2"));
        oralDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        oralDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        oralDetail.setDoneOn(new Date());
        oralDetail.setCancerScreeningMasterId(cancerScreeningMaster.getId());
        oralDetail.setDoneBy(DoneBy.valueOf(user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()));

        MemberBreastDetail breastDetail = null;
        MemberCervicalDetail cervicalDetail = null;

        if (member.getGender().equals("F")) {
            breastDetail = new MemberBreastDetail();
            breastDetail.setMemberId(member.getId());
            breastDetail.setFamilyId(familyEntity.getId());
            breastDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
            breastDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
            breastDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
            breastDetail.setDoneOn(new Date());
            breastDetail.setLongitude(keyAndAnswerMap.get("-1"));
            breastDetail.setLatitude(keyAndAnswerMap.get("-2"));
            breastDetail.setCancerScreeningMasterId(cancerScreeningMaster.getId());
            breastDetail.setDoneBy(DoneBy.valueOf(user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()));

            cervicalDetail = new MemberCervicalDetail();
            cervicalDetail.setMemberId(member.getId());
            cervicalDetail.setFamilyId(familyEntity.getId());
            cervicalDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
            cervicalDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
            cervicalDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
            cervicalDetail.setDoneOn(new Date());
            cervicalDetail.setLongitude(keyAndAnswerMap.get("-1"));
            cervicalDetail.setLatitude(keyAndAnswerMap.get("-2"));
            cervicalDetail.setCancerScreeningMasterId(cancerScreeningMaster.getId());
            cervicalDetail.setDoneBy(DoneBy.valueOf(user.getRole().getCode() != null ? user.getRole().getCode() : user.getRole().getName()));
        }

        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            setAnswersToMemberOralBreastAndCervicalDetail(key, answer, oralDetail, breastDetail, cervicalDetail, keyAndAnswerMap, member);
        }

        oralDetail.setReferralId(storeMemberReferralFromForms(
                oralDetail.getMemberId(), oralDetail.getLocationId(), DiseaseCode.O,
                user, new Date(), oralDetail.getHealthInfraId()));
        oralDetailDao.create(oralDetail);
        cancerScreeningMaster.setOralId(oralDetail.getId());
        updateMemberAdditionalInfoFromOral(member, oralDetail, false);

        if (breastDetail != null) {
            breastDetail.setReferralId(storeMemberReferralFromForms(
            breastDetail.getMemberId(), breastDetail.getLocationId(),
            DiseaseCode.B, user, new Date(), breastDetail.getHealthInfraId()));
            breastDetailDao.create(breastDetail);
            cancerScreeningMaster.setBreastId(breastDetail.getId());
            updateMemberAdditionalInfoFromBreast(member, breastDetail, false);
        }
        if (cervicalDetail != null) {
            cervicalDetail.setReferralId(storeMemberReferralFromForms(
            cervicalDetail.getMemberId(), cervicalDetail.getLocationId(), DiseaseCode.C,
            user, new Date(), cervicalDetail.getHealthInfraId()));
            cervicalDetailDao.create(cervicalDetail);
            cancerScreeningMaster.setCervicalId(cervicalDetail.getId());
            updateMemberAdditionalInfoFromCervical(member, cervicalDetail, false);
        }

        updateFamilyAdditionalInfo(familyEntity, oralDetail.getScreeningDate());
        cancerScreeningDao.update(cancerScreeningMaster);
        memberDao.update(member);
        familyDao.update(familyEntity);
        cancerScreeningDao.flush();
        return cancerScreeningMaster.getId();
    }

    private void updateMemberAdditionalInfoFromOral(MemberEntity member, MemberOralDetail oralDetail, boolean isOral) {
        boolean isSuspected = Boolean.TRUE.equals(oralDetail.getWhiteRedPatchOralCavity())
                || Boolean.TRUE.equals(oralDetail.getDifficultyInSpicyFood())
                || Boolean.TRUE.equals(oralDetail.getVoiceChange())
                || Boolean.TRUE.equals(oralDetail.getDifficultyInOpeningMouth())
                || Boolean.TRUE.equals(oralDetail.getThreeWeeksMouthUlcer())
                || oralDetail.getGrowthOfRecentOrigins() != null
                || oralDetail.getNonHealingUlcers() != null
                || oralDetail.getWhitePatches() != null
                || oralDetail.getRedPatches() != null;

        MemberAdditionalInfo memberAdditionalInfo = new MemberAdditionalInfo();
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        }
        if (isSuspected) {
            oralDetail.setSuspected(true);
            memberAdditionalInfo.setSuspectedForOralCancer(true);
        }
        if (isOral) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.O.toString()));
        }
        memberAdditionalInfo.setCancerServiceDate(oralDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setLastServiceLongDate(oralDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setOralYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getOralYear(), ImtechoUtil.getFinancialYearFromDate(oralDetail.getScreeningDate())));
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromBreast(MemberEntity member, MemberBreastDetail breastDetail, boolean isBreast) {
        boolean isSuspected = Boolean.TRUE.equals(breastDetail.getLumpInBreast())
                || Boolean.TRUE.equals(breastDetail.getSizeChange())
                || Boolean.TRUE.equals(breastDetail.getNippleShapeAndPositionChange())
                || Boolean.TRUE.equals(breastDetail.getAnyRetractionOfNipple())
                || Boolean.TRUE.equals(breastDetail.getDischargeFromNipple())
                || Boolean.TRUE.equals(breastDetail.getRednessOfSkinOverNipple())
                || Boolean.TRUE.equals(breastDetail.getErosionsOfNipple())
                || "NONE".equals(breastDetail.getVisualSwellingInArmpit())
                || breastDetail.getVisualLumpInBreast() != null
                || breastDetail.getVisualNippleRetractionDistortion() != null
                || breastDetail.getVisualUlceration() != null
                || breastDetail.getVisualSkinDimplingRetraction() != null;

        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (isSuspected) {
            breastDetail.setSuspected(true);
            memberAdditionalInfo.setSuspectedForBreastCancer(true);
        }
        if (isBreast) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.B.toString()));
        }

        memberAdditionalInfo.setBreastYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getBreastYear(), ImtechoUtil.getFinancialYearFromDate(breastDetail.getScreeningDate())));
        memberAdditionalInfo.setCancerServiceDate(breastDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setLastServiceLongDate(breastDetail.getScreeningDate().getTime());
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromCervical(MemberEntity member, MemberCervicalDetail cervicalDetail, boolean isCervical) {
        boolean isSuspected = Boolean.TRUE.equals(cervicalDetail.getExcessiveBleedingDuringPeriods())
                || Boolean.TRUE.equals(cervicalDetail.getBleedingBetweenPeriods())
                || Boolean.TRUE.equals(cervicalDetail.getBleedingAfterIntercourse())
                || Boolean.TRUE.equals(cervicalDetail.getExcessiveSmellingVaginalDischarge())
                || Boolean.TRUE.equals(cervicalDetail.getPostmenopausalBleeding());

        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (isSuspected) {
            memberAdditionalInfo.setSuspectedForCervicalCancer(true);
        }
        if (isCervical) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.C.toString()));
        }

        memberAdditionalInfo.setCervicalYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getCervicalYear(), ImtechoUtil.getFinancialYearFromDate(cervicalDetail.getScreeningDate())));
        memberAdditionalInfo.setCancerServiceDate(cervicalDetail.getScreeningDate().getTime());
        memberAdditionalInfo.setLastServiceLongDate(cervicalDetail.getScreeningDate().getTime());
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    private void setAnswersToMemberOralBreastAndCervicalDetail(String key, String answer, MemberOralDetail oralDetail,
                                                               MemberBreastDetail breastDetail, MemberCervicalDetail cervicalDetail,
                                                               Map<String, String> keyAndAnswerMap,
                                                               MemberEntity memberEntity) {
        switch (key) {
            // update member info in member entity
            case "666":
                Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                for (String id : answer.split(",")) {
                    if (!"NONE".equals(id) && !"OTHER".equals(id) && !id.equals("null")) {
                        chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                    }
                }
                memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                break;

            case "6":
                oralDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                if (breastDetail != null)
                    breastDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                if (cervicalDetail != null)
                    cervicalDetail.setScreeningDate(new Date(Long.parseLong(answer)));
                break;

            //Oral related entries
            case "7":
                oralDetail.setDiagnosedEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "8":
                if (keyAndAnswerMap.get("7").equals("1")) {
                    oralDetail.setCurrentlyUnderTreatment(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "9":
                if (keyAndAnswerMap.get("8").equals("1")) {
                    oralDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "10":
                if (keyAndAnswerMap.get("8").equals("1") && keyAndAnswerMap.get("9").equals("GOVERNMENT")) {
                    oralDetail.setGovtFacilityId(Integer.valueOf(answer));
                }
                break;
            case "100":
                if (keyAndAnswerMap.get("8").equals("1") && keyAndAnswerMap.get("9").equals("PRIVATE")) {
                    oralDetail.setPrivateFacility(answer);
                }
                break;
            case "1001":
                if (keyAndAnswerMap.get("8").equals("1") && keyAndAnswerMap.get("9").equals("OUTOFTERRITORY")) {
                    oralDetail.setOutOfTerritoryFacility(answer);
                }
                break;
            case "12":
                oralDetail.setWhiteRedPatchOralCavity(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "20":
                oralDetail.setDifficultyInSpicyFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "19":
                oralDetail.setVoiceChange(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "21":
                oralDetail.setDifficultyInOpeningMouth(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "15":
                oralDetail.setThreeWeeksMouthUlcer(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "18":
                oralDetail.setGrowthOfRecentOrigins(answer);
                break;
            case "16":
                oralDetail.setNonHealingUlcers(answer);
                break;
            case "14":
                oralDetail.setRedPatches(answer);
                break;
            case "13":
                oralDetail.setWhitePatches(answer);
                break;
            case "17":
                oralDetail.setGrowthOfRecentOriginFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            //Breast related entries
            case "40":
                breastDetail.setDiagnosedEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "41":
                if (keyAndAnswerMap.get("40").equals("1")) {
                    breastDetail.setCurrentlyUnderTreatment(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "42":
                if (keyAndAnswerMap.get("41").equals("1")) {
                    breastDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "43":
                if (keyAndAnswerMap.get("41").equals("1") && keyAndAnswerMap.get("42").equals("GOVERNMENT")) {
                    breastDetail.setGovtFacilityId(Integer.valueOf(answer));
                }
                break;
            case "430":
                if (keyAndAnswerMap.get("41").equals("1") && keyAndAnswerMap.get("42").equals("PRIVATE")) {
                    breastDetail.setPrivateFacility(answer);
                }
                break;
            case "4310":
                if (keyAndAnswerMap.get("41").equals("1") && keyAndAnswerMap.get("42").equals("OUTOFTERRITORY")) {
                    breastDetail.setOutOfTerritoryFacility(answer);
                }
                break;
            case "45":
                breastDetail.setLumpInBreast(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "46":
                breastDetail.setSizeChange(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "47":
                breastDetail.setNippleShapeAndPositionChange(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "48":
                breastDetail.setAnyRetractionOfNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "49":
                breastDetail.setDischargeFromNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "50":
                breastDetail.setRednessOfSkinOverNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "51":
                breastDetail.setErosionsOfNipple(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "53":
                breastDetail.setAgreedForSelfBreastExam(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "54":
                if (keyAndAnswerMap.get("50").equals("1")) {
                    breastDetail.setVisualSwellingInArmpit(answer);
                }
                break;
            case "52":
                if (keyAndAnswerMap.get("53").equals("1")) {
                    breastDetail.setVisualDischargeFromNipple(answer);
                }
                break;
            case "57":
                if (keyAndAnswerMap.get("53").equals("1")) {
                    breastDetail.setVisualLumpInBreast(answer);
                }
                break;
            case "58":
                if (keyAndAnswerMap.get("53").equals("1")) {
                    breastDetail.setVisualNippleRetractionDistortion(answer);
                }
                break;
            case "59":
                if (keyAndAnswerMap.get("53").equals("1")) {
                    breastDetail.setVisualUlceration(answer);
                }
                break;
            case "60":
                if (keyAndAnswerMap.get("53").equals("1")) {
                    breastDetail.setVisualSkinDimplingRetraction(answer);
                }
                break;
            //Cervical related entries
            case "80":
                cervicalDetail.setDiagnosedEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "81":
                if (keyAndAnswerMap.get("80").equals("1")) {
                    cervicalDetail.setCurrentlyUnderTreatment(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;
            case "82":
                if (keyAndAnswerMap.get("81").equals("1")) {
                    cervicalDetail.setCurrentTreatmentPlace(answer);
                }
                break;
            case "83":
                if (keyAndAnswerMap.get("81").equals("1") && keyAndAnswerMap.get("82").equals("GOVERNMENT")) {
                    cervicalDetail.setGovtFacilityId(Integer.valueOf(answer));
                }
                break;
            case "830":
                if (keyAndAnswerMap.get("81").equals("1") && keyAndAnswerMap.get("82").equals("PRIVATE")) {
                    cervicalDetail.setPrivateFacility(answer);
                }
                break;
            case "8310":
                if (keyAndAnswerMap.get("81").equals("1") && keyAndAnswerMap.get("82").equals("OUTOFTERRITORY")) {
                    cervicalDetail.setOutOfTerritoryFacility(answer);
                }
                break;
            case "85":
                cervicalDetail.setFutureScreeningDate(new Date(Long.parseLong(answer)));
                break;
            case "86":
                cervicalDetail.setRefferalDone(Boolean.TRUE);
                cervicalDetail.setHealthInfraId(Integer.valueOf(answer));
                break;
            case "92":
                oralDetail.setRefferalDone(Boolean.TRUE);
                oralDetail.setHealthInfraId(Integer.valueOf(answer));
                if (breastDetail != null) {
                    breastDetail.setRefferalDone(Boolean.TRUE);
                    breastDetail.setHealthInfraId(Integer.valueOf(answer));
                }
                break;
            default:
        }
    }

    private GeneralDetailMedicineDto findMedicineById(List<GeneralDetailMedicineDto> medicines, int id) {
        for (GeneralDetailMedicineDto medicine : medicines) {
            if (medicine.getMedicineId() == id) {
                return medicine;
            }
        }
        return null; // Return null if no matching Medicine is found
    }

    @Override
    public void saveHypertension(MemberHyperTensionDto hyperTensionDto) {
        MemberEntity memberEntity = memberDao.retrieveById(hyperTensionDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        boolean currentlyUnderTreatment = false;
        if (hyperTensionDto.getReferralId() != null) {
            MemberReferral previousMemberReferral = memberReferralDao.retrieveById(hyperTensionDto.getReferralId());
            previousMemberReferral.setState(State.COMPLETED);
            memberReferralDao.update(previousMemberReferral);
        }

        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setReferredFrom(ReferralPlace.MO);
        memberReferral.setMemberId(hyperTensionDto.getMemberId());
        if (hyperTensionDto.getStatus().equals(Status.NO_ABNORMALITY)) {
            memberReferral.setState(State.COMPLETED);
        } else {
            memberReferral.setState(State.PENDING);
        }
        memberReferral.setStatus(hyperTensionDto.getStatus());
        memberReferral.setFollowUpDate(hyperTensionDto.getFollowUpDate());
        memberReferral.setDiseaseCode(DiseaseCode.HT);
        memberReferral.setReferredOn(hyperTensionDto.getScreeningDate());
        memberReferral.setMemberLocation(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberReferral.setReason(hyperTensionDto.getReason());
        memberReferral.setHealthInfraId(hyperTensionDto.getHealthInfraId());
        memberReferral.setPvtHealthInfraName(hyperTensionDto.getPvtHealthInfraName());
        memberReferral.setReferredFromHealthInfrastructureId(hyperTensionDto.getReferredFromHealthInfrastructureId());
        HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(hyperTensionDto.getReferredFromHealthInfrastructureId());
        if (healthInfrastructureDetails != null) {
            memberReferral.setLocationId(healthInfrastructureDetails.getLocationId());
        }
        Integer memberReferralId = memberReferralDao.create(memberReferral);

        MemberDiseaseDiagnosis memberDiseaseDiagnosis = memberDiseaseDiagnosisDao.retrieveByMemberIdAndDiseaseType(hyperTensionDto.getMemberId(), DiseaseCode.HT);
        if (memberDiseaseDiagnosis != null) {
            memberDiseaseDiagnosis.setStatus(hyperTensionDto.getStatus());
            memberDiseaseDiagnosis.setSubType(hyperTensionDto.getSubType());
            memberDiseaseDiagnosis.setReadings(hyperTensionDto.getReadings());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.update(memberDiseaseDiagnosis);
        } else {
            memberDiseaseDiagnosis = new MemberDiseaseDiagnosis();
            memberDiseaseDiagnosis.setMemberId(hyperTensionDto.getMemberId());
            memberDiseaseDiagnosis.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.HT);
            memberDiseaseDiagnosis.setStatus(hyperTensionDto.getStatus());
            memberDiseaseDiagnosis.setSubType(hyperTensionDto.getSubType());
            memberDiseaseDiagnosis.setReadings(hyperTensionDto.getReadings());
            memberDiseaseDiagnosis.setDiagnosedOn(hyperTensionDto.getScreeningDate());
            memberDiseaseDiagnosis.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.create(memberDiseaseDiagnosis);
        }


        MemberHypertensionDetail previousMemberHypertensionDetail = hypertensionDetailDao.retrieveLastSingleRecordByMemberId(hyperTensionDto.getMemberId());
        if ((previousMemberHypertensionDetail != null && previousMemberHypertensionDetail.getCurrentlyUnderTreatement() != null && previousMemberHypertensionDetail.getCurrentlyUnderTreatement()) || hyperTensionDto.getStatus().equals(Status.TREATMENT_STARTED)) {
            currentlyUnderTreatment = true;
        }
        hyperTensionDto.setDoneBy(DoneBy.MO);
        MemberHypertensionDetail memberHypertensionDetail = MemberDetailMapper.dtoToEntityForHyperTension(hyperTensionDto);
        memberHypertensionDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberHypertensionDetail.setFamilyId(familyEntity.getId());
        memberHypertensionDetail.setReferralId(memberReferralId);
        memberHypertensionDetail.setCurrentlyUnderTreatement(currentlyUnderTreatment);
        Integer hypertensionId = hypertensionDetailDao.create(memberHypertensionDetail);

        if (Objects.nonNull(hyperTensionDto.getHmisId())) {
            MemberCbacDetail cbacDetail = cbacDetailDao.retrieveById(hyperTensionDto.getCbacId());
            cbacDetail.setHmisId(hyperTensionDto.getHmisId());
            cbacDetailDao.update(cbacDetail);
        }

        List<MemberDiseaseMedicine> memberMedicines = new ArrayList<>();
        if (hyperTensionDto.getMedicines() != null) {
            List<GeneralDetailMedicineDto> medicines = hyperTensionDto.getMedicineDetail(); // Assuming you have this list

            for (Integer id : hyperTensionDto.getMedicines()) {
                MemberDiseaseMedicine mdm = new MemberDiseaseMedicine();
                mdm.setMedicineId(id);
                mdm.setMemberId(hyperTensionDto.getMemberId());
                mdm.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.HT);
                mdm.setDiagnosedOn(hyperTensionDto.getScreeningDate());
                mdm.setReferralId(memberReferralId);
//                mdm.setReferenceId(hypertensionId);

                // Find the Medicine object with the matching medicineId
                GeneralDetailMedicineDto matchingMedicine = findMedicineById(medicines, id);

                if (matchingMedicine != null) {
                    // Set other properties from the matching Medicine object
                    mdm.setStartDate(matchingMedicine.getStartDate());
                    mdm.setFrequency(matchingMedicine.getFrequency());
                    mdm.setDuration(matchingMedicine.getDuration());
                    mdm.setQuantity(matchingMedicine.getQuantity());
                    mdm.setSpecialInstruction(matchingMedicine.getSpecialInstruction());
                }

                memberMedicines.add(mdm);
            }
            memberDiseaseMedicineDao.saveOrUpdateAll(memberMedicines);
        }

        updateMemberAdditionalInfoFromHypertension(memberEntity, memberHypertensionDetail);
        updateFamilyAdditionalInfo(familyEntity, memberHypertensionDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public void saveDiabetes(MemberDiabetesDto diabetesDto) {
        MemberEntity memberEntity = memberDao.retrieveById(diabetesDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        if (diabetesDto.getReferralId() != null) {
            MemberReferral previousMemberReferral = memberReferralDao.retrieveById(diabetesDto.getReferralId());
            previousMemberReferral.setState(State.COMPLETED);
            memberReferralDao.update(previousMemberReferral);
        }

        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setReferredFrom(ReferralPlace.MO);
        memberReferral.setMemberId(diabetesDto.getMemberId());
        if (diabetesDto.getStatus().equals(Status.NO_ABNORMALITY)) {
            memberReferral.setState(State.COMPLETED);
        } else {
            memberReferral.setState(State.PENDING);
        }
        memberReferral.setStatus(diabetesDto.getStatus());
        memberReferral.setFollowUpDate(diabetesDto.getFollowUpDate());
        memberReferral.setDiseaseCode(DiseaseCode.D);
        memberReferral.setReferredOn(diabetesDto.getScreeningDate());
        memberReferral.setMemberLocation(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberReferral.setReason(diabetesDto.getReason());
        memberReferral.setHealthInfraId(diabetesDto.getHealthInfraId());
        memberReferral.setPvtHealthInfraName(diabetesDto.getPvtHealthInfraName());
        memberReferral.setReferredFromHealthInfrastructureId(diabetesDto.getReferredFromHealthInfrastructureId());
        HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(diabetesDto.getReferredFromHealthInfrastructureId());
        if (healthInfrastructureDetails != null) {
            memberReferral.setLocationId(healthInfrastructureDetails.getLocationId());
        }
        Integer memberReferralId = memberReferralDao.create(memberReferral);

        MemberDiseaseDiagnosis memberDiseaseDiagnosis = memberDiseaseDiagnosisDao.retrieveByMemberIdAndDiseaseType(diabetesDto.getMemberId(), DiseaseCode.D);
        if (memberDiseaseDiagnosis != null) {
            memberDiseaseDiagnosis.setStatus(diabetesDto.getStatus());
            memberDiseaseDiagnosis.setSubType(diabetesDto.getSubType());
            memberDiseaseDiagnosis.setReadings(diabetesDto.getReadings());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.update(memberDiseaseDiagnosis);
        } else {
            memberDiseaseDiagnosis = new MemberDiseaseDiagnosis();
            memberDiseaseDiagnosis.setMemberId(diabetesDto.getMemberId());
            memberDiseaseDiagnosis.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.D);
            memberDiseaseDiagnosis.setStatus(diabetesDto.getStatus());
            memberDiseaseDiagnosis.setSubType(diabetesDto.getSubType());
            memberDiseaseDiagnosis.setReadings(diabetesDto.getReadings());
            memberDiseaseDiagnosis.setDiagnosedOn(diabetesDto.getScreeningDate());
            memberDiseaseDiagnosis.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.create(memberDiseaseDiagnosis);
        }

        boolean currentlyUnderTreatment = false;
        MemberDiabetesDetail previousMemberDiabetesDetail = diabetesDetailDao.retrieveLastRecordByMemberId(diabetesDto.getMemberId());
        if ((previousMemberDiabetesDetail != null && previousMemberDiabetesDetail.getCurrentlyUnderTreatment() != null && previousMemberDiabetesDetail.getCurrentlyUnderTreatment()) || diabetesDto.getStatus().equals(Status.TREATMENT_STARTED)) {
            currentlyUnderTreatment = true;
        }

        diabetesDto.setDoneBy(DoneBy.MO);
        MemberDiabetesDetail memberDiabetesDetail = MemberDetailMapper.dtoToEntityForDiabetes(diabetesDto);
        memberDiabetesDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberDiabetesDetail.setFamilyId(familyEntity.getId());
        memberDiabetesDetail.setReferralId(memberReferralId);
        memberDiabetesDetail.setCurrentlyUnderTreatment(currentlyUnderTreatment);
        Integer diabetesId = diabetesDetailDao.create(memberDiabetesDetail);

        List<MemberDiseaseMedicine> memberMedicines = new ArrayList<>();
        if (diabetesDto.getMedicines() != null) {
            for (Integer id : diabetesDto.getMedicines()) {
                MemberDiseaseMedicine mdm = new MemberDiseaseMedicine();
                mdm.setMedicineId(id);
                mdm.setMemberId(diabetesDto.getMemberId());
                mdm.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.D);
                mdm.setDiagnosedOn(diabetesDto.getScreeningDate());
                mdm.setReferralId(memberReferralId);
                mdm.setReferenceId(diabetesId);
                memberMedicines.add(mdm);
            }
            memberDiseaseMedicineDao.saveOrUpdateAll(memberMedicines);
        }

        updateMemberAdditionalInfoFromDiabetes(memberEntity, memberDiabetesDetail,
                diabetesDto.getStatus().equals(Status.CONFIRMED) || diabetesDto.getStatus().equals(Status.TREATMENT_STARTED) || diabetesDto.getStatus().equals(Status.REFERRED));
        updateFamilyAdditionalInfo(familyEntity, memberDiabetesDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public void saveCervical(MemberCervicalDto cervicalDto) {
        MemberEntity memberEntity = memberDao.retrieveById(cervicalDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        if (cervicalDto.getReferralId() != null) {
            MemberReferral previousMemberReferral = memberReferralDao.retrieveById(cervicalDto.getReferralId());
            previousMemberReferral.setState(State.COMPLETED);
            memberReferralDao.update(previousMemberReferral);
        }

        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setReferredFrom(ReferralPlace.MO);
        memberReferral.setMemberId(cervicalDto.getMemberId());
        if (cervicalDto.getStatus().equals(Status.NO_ABNORMALITY)) {
            memberReferral.setState(State.COMPLETED);
        } else {
            memberReferral.setState(State.PENDING);
        }
        memberReferral.setStatus(cervicalDto.getStatus());
        memberReferral.setFollowUpDate(cervicalDto.getFollowUpDate());
        memberReferral.setDiseaseCode(DiseaseCode.C);
        memberReferral.setReferredOn(cervicalDto.getScreeningDate());
        memberReferral.setMemberLocation(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberReferral.setReason(cervicalDto.getReason());
        memberReferral.setHealthInfraId(cervicalDto.getHealthInfraId());
        memberReferral.setPvtHealthInfraName(cervicalDto.getPvtHealthInfraName());
        memberReferral.setReferredFromHealthInfrastructureId(cervicalDto.getReferredFromHealthInfrastructureId());
        HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(cervicalDto.getReferredFromHealthInfrastructureId());
        if (healthInfrastructureDetails != null) {
            memberReferral.setLocationId(healthInfrastructureDetails.getLocationId());
        }
        Integer memberReferralId = memberReferralDao.create(memberReferral);

        MemberDiseaseDiagnosis memberDiseaseDiagnosis = memberDiseaseDiagnosisDao.retrieveByMemberIdAndDiseaseType(cervicalDto.getMemberId(), DiseaseCode.C);
        if (memberDiseaseDiagnosis != null) {
            memberDiseaseDiagnosis.setStatus(cervicalDto.getStatus());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.update(memberDiseaseDiagnosis);
        } else {
            memberDiseaseDiagnosis = new MemberDiseaseDiagnosis();
            memberDiseaseDiagnosis.setMemberId(cervicalDto.getMemberId());
            memberDiseaseDiagnosis.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.C);
            memberDiseaseDiagnosis.setStatus(cervicalDto.getStatus());
            memberDiseaseDiagnosis.setDiagnosedOn(cervicalDto.getScreeningDate());
            memberDiseaseDiagnosis.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.create(memberDiseaseDiagnosis);
        }

        boolean currentlyUnderTreatment;
        MemberCervicalDetail previousMemberCervicalDetail = cervicalDetailDao.retrieveLastRecordByMemberId(cervicalDto.getMemberId());
        currentlyUnderTreatment = (previousMemberCervicalDetail != null && previousMemberCervicalDetail.getCurrentlyUnderTreatment() != null && previousMemberCervicalDetail.getCurrentlyUnderTreatment()) || cervicalDto.getStatus().equals(Status.SUSPECTED) || cervicalDto.getStatus().equals(Status.CONFIRMATION_PENDING);

        cervicalDto.setDoneBy(DoneBy.MO);

        MemberCervicalDetail memberCervicalDetail = MemberDetailMapper.dtoToEntityForCervical(cervicalDto);
        memberCervicalDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberCervicalDetail.setFamilyId(familyEntity.getId());
        memberCervicalDetail.setReferralId(memberReferralId);
        memberCervicalDetail.setCurrentlyUnderTreatment(currentlyUnderTreatment);
        cervicalDetailDao.create(memberCervicalDetail);

        if (Objects.nonNull(cervicalDto.getHmisId())) {
            MemberCbacDetail cbacDetail = cbacDetailDao.retrieveById(cervicalDto.getCbacId());
            cbacDetail.setHmisId(cervicalDto.getHmisId());
            cbacDetailDao.update(cbacDetail);
        }

        updateMemberAdditionalInfoFromCervical(memberEntity, memberCervicalDetail,
                cervicalDto.getStatus().equals(Status.CONFIRMED) || cervicalDto.getStatus().equals(Status.REFERRED));
        updateFamilyAdditionalInfo(familyEntity, memberCervicalDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public void saveOral(MemberOralDto oralDto) {
        MemberEntity memberEntity = memberDao.retrieveById(oralDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        if (oralDto.getReferralId() != null) {
            MemberReferral previousMemberReferral = memberReferralDao.retrieveById(oralDto.getReferralId());
            previousMemberReferral.setState(State.COMPLETED);
            memberReferralDao.update(previousMemberReferral);
        }

        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setReferredFrom(ReferralPlace.MO);
        memberReferral.setMemberId(oralDto.getMemberId());
        if (oralDto.getStatus().equals(Status.NO_ABNORMALITY)) {
            memberReferral.setState(State.COMPLETED);
        } else {
            memberReferral.setState(State.PENDING);
        }
        memberReferral.setStatus(oralDto.getStatus());
        memberReferral.setFollowUpDate(oralDto.getFollowUpDate());
        memberReferral.setDiseaseCode(DiseaseCode.O);
        memberReferral.setReferredOn(oralDto.getScreeningDate());
        memberReferral.setMemberLocation(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberReferral.setReason(oralDto.getReason());
        memberReferral.setHealthInfraId(oralDto.getHealthInfraId());
        memberReferral.setPvtHealthInfraName(oralDto.getPvtHealthInfraName());
        memberReferral.setReferredFromHealthInfrastructureId(oralDto.getReferredFromHealthInfrastructureId());
        HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(oralDto.getReferredFromHealthInfrastructureId());
        if (healthInfrastructureDetails != null) {
            memberReferral.setLocationId(healthInfrastructureDetails.getLocationId());
        }
        Integer memberReferralId = memberReferralDao.create(memberReferral);

        MemberDiseaseDiagnosis memberDiseaseDiagnosis = memberDiseaseDiagnosisDao.retrieveByMemberIdAndDiseaseType(oralDto.getMemberId(), DiseaseCode.O);
        if (memberDiseaseDiagnosis != null) {
            memberDiseaseDiagnosis.setStatus(oralDto.getStatus());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.update(memberDiseaseDiagnosis);
        } else {
            memberDiseaseDiagnosis = new MemberDiseaseDiagnosis();
            memberDiseaseDiagnosis.setMemberId(oralDto.getMemberId());
            memberDiseaseDiagnosis.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.O);
            memberDiseaseDiagnosis.setStatus(oralDto.getStatus());
            memberDiseaseDiagnosis.setDiagnosedOn(oralDto.getScreeningDate());
            memberDiseaseDiagnosis.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.create(memberDiseaseDiagnosis);
        }

        boolean currentlyUnderTreatment;
        MemberOralDetail previousMemberOralDetail = oralDetailDao.retrieveLastRecordByMemberId(oralDto.getMemberId());
        currentlyUnderTreatment = (previousMemberOralDetail != null && previousMemberOralDetail.getCurrentlyUnderTreatment() != null && previousMemberOralDetail.getCurrentlyUnderTreatment()) || oralDto.getStatus().equals(Status.SUSPECTED) || oralDto.getStatus().equals(Status.CONFIRMATION_PENDING);

        oralDto.setDoneBy(DoneBy.MO);
        MemberOralDetail memberOralDetail = MemberDetailMapper.dtoToEntityForOral(oralDto);
        memberOralDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberOralDetail.setFamilyId(familyEntity.getId());
        memberOralDetail.setReferralId(memberReferralId);
        memberOralDetail.setCurrentlyUnderTreatment(currentlyUnderTreatment);
        oralDetailDao.create(memberOralDetail);

        if (Objects.nonNull(oralDto.getHmisId())) {
            MemberCbacDetail cbacDetail = cbacDetailDao.retrieveById(oralDto.getCbacId());
            cbacDetail.setHmisId(oralDto.getHmisId());
            cbacDetailDao.update(cbacDetail);
        }

        updateMemberAdditionalInfoFromOral(memberEntity, memberOralDetail,
                oralDto.getStatus().equals(Status.CONFIRMED) || oralDto.getStatus().equals(Status.REFERRED));
        updateFamilyAdditionalInfo(familyEntity, memberOralDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public void saveBreast(MemberBreastDto breastDto) {
        MemberEntity memberEntity = memberDao.retrieveById(breastDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        if (breastDto.getReferralId() != null) {
            MemberReferral previousMemberReferral = memberReferralDao.retrieveById(breastDto.getReferralId());
            previousMemberReferral.setState(State.COMPLETED);
            memberReferralDao.update(previousMemberReferral);
        }

        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setReferredFrom(ReferralPlace.MO);
        memberReferral.setMemberId(breastDto.getMemberId());
        if (breastDto.getStatus().equals(Status.NO_ABNORMALITY)) {
            memberReferral.setState(State.COMPLETED);
        } else {
            memberReferral.setState(State.PENDING);
        }
        memberReferral.setStatus(breastDto.getStatus());
        memberReferral.setFollowUpDate(breastDto.getFollowUpDate());
        memberReferral.setDiseaseCode(DiseaseCode.B);
        memberReferral.setReferredOn(breastDto.getScreeningDate());
        memberReferral.setMemberLocation(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberReferral.setReason(breastDto.getReason());
        memberReferral.setHealthInfraId(breastDto.getHealthInfraId());
        memberReferral.setPvtHealthInfraName(breastDto.getPvtHealthInfraName());
        memberReferral.setReferredFromHealthInfrastructureId(breastDto.getReferredFromHealthInfrastructureId());
        HealthInfrastructureDetails healthInfrastructureDetails = healthInfrastructureDetailsDao.retrieveById(breastDto.getReferredFromHealthInfrastructureId());
        if (healthInfrastructureDetails != null) {
            memberReferral.setLocationId(healthInfrastructureDetails.getLocationId());
        }
        Integer memberReferralId = memberReferralDao.create(memberReferral);

        MemberDiseaseDiagnosis memberDiseaseDiagnosis = memberDiseaseDiagnosisDao.retrieveByMemberIdAndDiseaseType(breastDto.getMemberId(), DiseaseCode.B);
        if (memberDiseaseDiagnosis != null) {
            memberDiseaseDiagnosis.setStatus(breastDto.getStatus());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.update(memberDiseaseDiagnosis);
        } else {
            memberDiseaseDiagnosis = new MemberDiseaseDiagnosis();
            memberDiseaseDiagnosis.setMemberId(breastDto.getMemberId());
            memberDiseaseDiagnosis.setDiseaseCode(MemberDiseaseDiagnosis.DiseaseCode.B);
            memberDiseaseDiagnosis.setStatus(breastDto.getStatus());
            memberDiseaseDiagnosis.setDiagnosedOn(breastDto.getScreeningDate());
            memberDiseaseDiagnosis.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
            if (memberDiseaseDiagnosis.getStatus().equals(Status.NO_ABNORMALITY)) {
                memberDiseaseDiagnosis.setCaseCompleted(Boolean.TRUE);
            }
            memberDiseaseDiagnosisDao.create(memberDiseaseDiagnosis);
        }

        boolean currentlyUnderTreatment;
        MemberBreastDetail previousMemberBreastDetail = breastDetailDao.retrieveLastRecordByMemberId(breastDto.getMemberId());
        currentlyUnderTreatment = (previousMemberBreastDetail != null && previousMemberBreastDetail.getCurrentlyUnderTreatment() != null && previousMemberBreastDetail.getCurrentlyUnderTreatment()) || breastDto.getStatus().equals(Status.SUSPECTED) || breastDto.getStatus().equals(Status.CONFIRMATION_PENDING);

        breastDto.setDoneBy(DoneBy.MO);
        MemberBreastDetail memberBreastDetail = MemberDetailMapper.dtoToEntityForBreast(breastDto);
        memberBreastDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberBreastDetail.setFamilyId(familyEntity.getId());
        memberBreastDetail.setReferralId(memberReferralId);
        memberBreastDetail.setCurrentlyUnderTreatment(currentlyUnderTreatment);
        breastDetailDao.create(memberBreastDetail);

        if (Objects.nonNull(breastDto.getHmisId())) {
            MemberCbacDetail cbacDetail = cbacDetailDao.retrieveById(breastDto.getCbacId());
            cbacDetail.setHmisId(breastDto.getHmisId());
            cbacDetailDao.update(cbacDetail);
        }

        updateMemberAdditionalInfoFromBreast(memberEntity, memberBreastDetail,
                breastDto.getStatus().equals(Status.CONFIRMED) || breastDto.getStatus().equals(Status.REFERRED));
        updateFamilyAdditionalInfo(familyEntity, memberBreastDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
    }

    @Override
    public List<MedicineMaster> retrieveAllMedicines() {
        return medicineMasterDao.retrieveAll();
    }

    @Override
    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(Integer memberId, String diseaseCode) {
        return memberDiseaseMedicineDao.retrieveTreatmentHistoryDnhdd(memberId, diseaseCode);
    }

    @Override
    public void saveFollowUp(MemberDiseaseFollowupDto memberDiseaseFollowupDto) {
        memberDiseaseFollowupDao.save(MemberDetailMapper.dtoToEntityForFollowup(memberDiseaseFollowupDto));
    }

    @Override
    public List<MemberDetailDto> retrieveMembersForFollowup(Integer limit, Integer offset, String
            healthInfrastructureType, String[] status) {
        return memberDao.retrieveNcdMembersForFollowup(user.getId(), limit, offset, healthInfrastructureType, status);
    }

    @Override
    public List<MemberReferralDto> retrieveReffForToday(Integer memberId) {
        return memberReferralDao.retrieveReffForToday(memberId, user.getId());
    }

    @Override
    public List<MemberDiseaseFollowupDto> retrieveNextFollowUp(Integer memberId) {
        return memberDiseaseFollowupDao.retrieveNextFollowUp(memberId, user.getId());
    }

    @Override
    public MemberHypertensionDetail retrieveHypertensionDetailsByMemberAndDate(Integer memberId, Date screeningDate) {
        return hypertensionDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, DoneBy.MO);
    }

    @Override
    public MemberDiabetesDetail retrieveDiabetesDetailsByMemberAndDate(Integer memberId, Date screeningDate) {
        return diabetesDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, DoneBy.MO);
    }

    @Override
    public MemberOralDetail retrieveOralDetailsByMemberAndDate(Integer memberId, Date screeningDate) {
        return oralDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, DoneBy.MO);
    }

    @Override
    public MemberBreastDetail retrieveBreastDetailsByMemberAndDate(Integer memberId, Date screeningDate) {
        return breastDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, DoneBy.MO);
    }

    @Override
    public MemberCervicalDetail retrieveCervicalDetailsByMemberAndDate(Integer memberId, Date screeningDate) {
        return cervicalDetailDao.retrieveByMemberIdAndScreeningDate(memberId, screeningDate, DoneBy.MO);
    }

    @Override
    public MemberCervicalDetail retrieveLastRecordForCervicalByMemberId(Integer memberId) {
        return cervicalDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    @Override
    public List<MemberReferralDnhddDto> retrieveMembers(Integer limit, Integer offset, String
            healthInfrastructureType, String searchBy, String searchString, Boolean isSus) {
        return memberReferralDao.retrieveMembersNew(user.getId(), limit, offset, healthInfrastructureType, searchBy, searchString, isSus);
    }

    @Override
    public MemberNcdDetailDto retrieveMemberDetail(Integer memberId) {

        MemberNcdDetailDto memberDetailDto = new MemberNcdDetailDto();
        MemberReferral hypertensionDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.HT);
        MemberReferral diabetesDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.D);
        MemberReferral oralDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.O);
        MemberReferral breastDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.B);
        MemberReferral cervicalDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.C);
        MemberEntity basicDetail = memberDao.retrieveMemberById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(basicDetail.getFamilyId());
        memberDetailDto.setFamilyId(familyEntity.getId());
        memberDetailDto.setServerDate(new Date());
        memberDetailDto.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberDetailDto.setLocationHierarchy(locationHierchyCloserDetailDao.getLocationHierarchyStringByLocationId(memberDetailDto.getLocationId()));
        memberDetailDto.setBasicDetails(MemberMapper.getMemberDto(basicDetail));
        memberDetailDto.setMemberHypertensionDto(hypertensionDetail);
        memberDetailDto.setMemberDiabetesDto(diabetesDetail);
        memberDetailDto.setMemberOralDto(oralDetail);
        memberDetailDto.setMemberBreastDto(breastDetail);
        memberDetailDto.setMemberCervicalDto(cervicalDetail);
        memberDetailDto.setName(Stream.of(basicDetail.getFirstName(), basicDetail.getMiddleName(), basicDetail.getLastName()).filter(Objects::nonNull).collect(Collectors.joining(" ")));
        memberDetailDto.setAdditionalInfo(basicDetail.getAdditionalInfo());
        MemberBreastDetail memberBreastDetail = breastDetailDao.retrieveFirstRecordByMemberId(memberId);
        if (memberBreastDetail != null && memberBreastDetail.getDiagnosedEarlier() != null && memberBreastDetail.getDiagnosedEarlier()) {
            memberDetailDto.setCaseOfBreastCancer(true);
        }
        MemberCervicalDetail memberCervicalDetail = cervicalDetailDao.retrieveFirstRecordByMemberId(memberId);
        if (memberCervicalDetail != null && memberCervicalDetail.getDiagnosedEarlier() != null && memberCervicalDetail.getDiagnosedEarlier()) {
            memberDetailDto.setCaseOfCervicalCancer(true);
        }
        MemberOralDetail memberOralDetail = oralDetailDao.retrieveFirstRecordByMemberId(memberId);
        if (memberOralDetail != null && memberOralDetail.getDiagnosedEarlier() != null && memberOralDetail.getDiagnosedEarlier()) {
            memberDetailDto.setCaseOfOralCancer(true);
        }
        MemberHypertensionDetail memberHypertensionDetail = hypertensionDetailDao.retrieveFirstRecordByMemberId(memberId);
        if (memberHypertensionDetail != null && memberHypertensionDetail.getDiagnosedEarlier() != null && memberHypertensionDetail.getDiagnosedEarlier()) {
            memberDetailDto.setCaseOfHypertension(true);
        }
        MemberDiabetesDetail memberDiabetesDetail = diabetesDetailDao.retrieveFirstRecordByMemberId(memberId);
        if (memberDiabetesDetail != null && memberDiabetesDetail.getEarlierDiabetesDiagnosis() != null && memberDiabetesDetail.getEarlierDiabetesDiagnosis()) {
            memberDetailDto.setCaseOfDiabetes(true);
        }
        return memberDetailDto;
    }

    @Override
    public MemberHyperTensionDto retrieveLastRecordForHypertensionByMemberId(Integer memberId) {
        MemberHypertensionDetail memberHypertensionDetail = hypertensionDetailDao.retrieveLastSingleRecordByMemberId(memberId);
        if (memberHypertensionDetail != null) {
            MemberHyperTensionDto memberHyperTensionDto = MemberDetailMapper.entityToDtoForHyperTension(memberHypertensionDetail);
            List<MedicineMaster> medicines = new ArrayList<>();
            List<MemberDiseaseMedicine> memberDiseaseMedicines = memberDiseaseMedicineDao.retrieveMedicinesByReferenceId(memberHypertensionDetail.getId());
            for (MemberDiseaseMedicine memberDiseaseMedicine : memberDiseaseMedicines) {
                medicines.add(medicineMasterDao.retrieveById(memberDiseaseMedicine.getMedicineId()));
            }
            memberHyperTensionDto.setMedicineMasters(medicines);
            return memberHyperTensionDto;
        } else {
            return null;
        }
    }

    @Override
    public MemberDiabetesDto retrieveLastRecordForDiabetesByMemberId(Integer memberId) {
        MemberDiabetesDetail memberDiabetesDetail = diabetesDetailDao.retrieveLastRecordByMemberId(memberId);
        if (memberDiabetesDetail != null) {
            MemberDiabetesDto memberDiabetesDto = MemberDetailMapper.entityToDtoForDiabetes(memberDiabetesDetail);
            List<MedicineMaster> medicines = new ArrayList<>();
            List<MemberDiseaseMedicine> memberDiseaseMedicines = memberDiseaseMedicineDao.retrieveMedicinesByReferenceId(memberDiabetesDetail.getId());
            for (MemberDiseaseMedicine memberDiseaseMedicine : memberDiseaseMedicines) {
                medicines.add(medicineMasterDao.retrieveById(memberDiseaseMedicine.getMedicineId()));
            }
            memberDiabetesDto.setMedicineMasters(medicines);
            return memberDiabetesDto;
        } else {
            return null;
        }
    }

    @Override
    public MemberOralDetail retrieveLastRecordForOralByMemberId(Integer memberId) {
        return oralDetailDao.retrieveLastRecordByMemberId(memberId);
    }

    @Override
    public Map<String, String> registerNewMember(MemberRegistrationDto ncdMember) {
        String hofMobileNumber = "-1";
        Integer hofId = null;
        Map<String, String> result = new HashMap<>();
        MemberEntity hofMember = null;
        MemberEntity newMember = MemberMapper.getMemberEntity(ncdMember.getMember(), null);
        if (Boolean.FALSE.equals(ncdMember.getIsHof())) {
            hofMember = MemberMapper.getMemberEntity(ncdMember.getHof(), null);
            hofMobileNumber = hofMember.getMobileNumber();
            newMember.setRelationWithHof(ncdMember.getHof().getRelationWithHof());
        }

        String foundFamilyId = memberDao.getFamilyIdByPhoneNumber(hofMobileNumber, newMember.getMobileNumber());
        FamilyEntity family;
        if (Objects.nonNull(foundFamilyId)) {
            family = familyDao.retrieveFamilyByFamilyId(foundFamilyId);
        } else {
            family = new FamilyEntity();
            family.setLocationId(ncdMember.getLocationId());
            family.setAreaId(ncdMember.getAreaId());
            family.setState(FamilyHealthSurveyServiceConstants.NCD_FAMILY_STATE_NEW);
            family.setFamilyId(familyHealthSurveyService.generateFamilyId());
            family.setAddress1(ncdMember.getAddress());


            if (Objects.nonNull(hofMember)) {
                hofMember.setFamilyId(family.getFamilyId());
                hofMember.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                hofMember.setState(FamilyHealthSurveyServiceConstants.NCD_MEMBER_STATE_NEW);
                hofId = memberDao.create(hofMember);
            }

            family.setId(familyDao.create(family));
        }

        newMember.setFamilyId(family.getFamilyId());
        newMember.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
        newMember.setState(FamilyHealthSurveyServiceConstants.NCD_MEMBER_STATE_NEW);
        Integer imtMemberId = memberDao.create(newMember);
        if (Objects.isNull(foundFamilyId)) {
            family.setHeadOfFamily(Objects.nonNull(hofId) ? hofId : imtMemberId);
            family.setContactPersonId(imtMemberId);
            familyDao.update(family);
        }

        NcdMaster ncdMasterMember = new NcdMaster();
        ncdMasterMember.setMemberId(imtMemberId);
        ncdMasterMember.setLocationId(family.getAreaId());
        ncdMasterMember.setHealthInfraId(ncdMember.getReferredFromInfraId());
        ncdMasterDao.create(ncdMasterMember);

        MemberReferral ncdMemberReferral = new MemberReferral();
        ncdMemberReferral.setMemberId(imtMemberId);
        ncdMemberReferral.setReferredOn(new Date());
        ncdMemberReferral.setDiseaseCode(DiseaseCode.HT);
        ncdMemberReferral.setReferredBy(user.getId());
        ncdMemberReferral.setLocationId(healthInfrastructureDetailsDao.retrieveById(ncdMember.getReferredFromInfraId()).getLocationId());
        ncdMemberReferral.setReferredFrom(ReferralPlace.MO);
        ncdMemberReferral.setState(State.PENDING);
        ncdMemberReferral.setMemberLocation(family.getAreaId());
        ncdMemberReferral.setReferredFromHealthInfrastructureId(ncdMember.getReferredFromInfraId());
        memberReferralDao.create(ncdMemberReferral);

        result.put("id", imtMemberId.toString());
        return result;
    }

//    private void saveMemberAbhaConsent(Map<Integer, String> memberAbhaConsentMap) {
//        for (Map.Entry<Integer, String> entrySet : memberAbhaConsentMap.entrySet()) {
//            Integer memberId = entrySet.getKey();
//            String consentDetails = entrySet.getValue();
//
//            AbhaConsentCheckboxModel abhaConsentCheckboxModel = new AbhaConsentCheckboxModel();
//            abhaConsentCheckboxModel.setMemberId(memberId);
//            abhaConsentCheckboxModel.setIsAadhaarSharingConsentGiven(consentDetails.contains("ABHACONSENT_1"));
//            abhaConsentCheckboxModel.setIsDocOtherThanAadhaarConsentGiven(consentDetails.contains("ABHACONSENT_2"));
//            abhaConsentCheckboxModel.setIsAbhaUsageConsentGiven(consentDetails.contains("ABHACONSENT_3"));
//            abhaConsentCheckboxModel.setIsSharingHealthRecordsConsentGiven(consentDetails.contains("ABHACONSENT_4"));
//            abhaConsentCheckboxModel.setIsAnonymizationConsentGiven(consentDetails.contains("ABHACONSENT_5"));
//            abhaConsentCheckboxModel.setIsHealthWorkerConsentGiven(consentDetails.contains("ABHACONSENT_6"));
//            abhaConsentCheckboxModel.setIsBeneficiaryConsentGiven(consentDetails.contains("ABHACONSENT_7"));
//            Boolean isConsentAvailable = memberDao.isAbhaConsentAvailableByMemberId(memberId);
//            if (Boolean.TRUE.equals(isConsentAvailable)) {
//                memberDao.updateMemberAbhaConsent(abhaConsentCheckboxModel);
//            } else {
//                memberDao.saveMemberAbhaConsent(abhaConsentCheckboxModel);
//            }
//        }
//
//    }
}
