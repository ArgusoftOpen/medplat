package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.service.DocumentService;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.FamilyAdditionalInfo;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.mapper.MemberMapper;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.web.healthinfra.constants.HealthInfrastructureConstants;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
//import com.argusoft.medplat.web.location.dao.LocationLevelHierarchyDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dao.MemberEcgTokenDao;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.dto.RecordStatusBean;
import com.argusoft.medplat.mobile.dto.UploadFileDataBean;
import com.argusoft.medplat.ncd.dao.*;
import com.argusoft.medplat.ncd.dto.*;
import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.NcdService;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.verification.cfhc.dao.CFHCDao;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Define all services for ncd.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class NcdServiceImpl implements NcdService {

    @Autowired
    private MemberCbacDetailDao cbacDetailDao;
    @Autowired
    private MemberHypertensionDetailDao hypertensionDetailDao;
    @Autowired
    private MemberGeneralDetailDao memberGeneralDetailDao;
    @Autowired
    private MemberDiabetesDetailDao diabetesDetailDao;
    @Autowired
    private MemberMentalHealthDetailDao mentalHealthDetailDao;
    @Autowired
    private DrugInventoryDao drugInventoryDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private MemberOtherInfoDao otherInfoDao;
    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private ImtechoSecurityUser user;
    @Autowired
    private MemberComplaintsDao memberComplaintsDao;
    @Autowired
    private MedicineMasterDao medicineMasterDao;
    @Autowired
    private MemberDiseaseDiagnosisDao memberDiseaseDiagnosisDao;
    @Autowired
    private MemberDiseaseMedicineDao memberDiseaseMedicineDao;
    @Autowired
    private MemberDiseaseFollowupDao memberDiseaseFollowupDao;
    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;
    @Autowired
    private MemberHealthDetailDao healthDetailDao;
    @Autowired
    private NcdSpecicalistMasterDao ncdSpecicalistMasterDao;
    @Autowired
    private MemberPersonalHistoryDao personalHistoryDao;
    @Autowired
    private MemberClinicVisitDetailDao clinicVisitDetailDao;
    @Autowired
    private ImtechoSecurityUser imtechoSecurityUser;
    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private MemberHomeVisitDetailDao homeVisitDetailDao;
    @Autowired
    private CFHCDao cFHCDao;
    @Autowired
    private NcdMasterDao ncdMasterDao;
    @Autowired
    private MemberInvestigationDao memberInvestigationDao;
    @Autowired
    private NcdVisitHistoryDao ncdVisitHistoryDao;
    @Autowired
    private MemberFollowupDetailDao memberFollowupDetailDao;
//    @Autowired
//    private LocationLevelHierarchyDao locationLevelHierarchyDao;
    @Autowired
    private QueryMasterService queryMasterService;
    @Autowired
    private MemberInitialAssessmentDao memberInitialAssessmentDao;
    @Autowired
    private MemberGeneralScreeningDao generalScreeningDao;
    @Autowired
    private MemberUrineTestDetailDao urineTestDetailDao;
    @Autowired
    private MemberEcgDetailDao ecgDetailDao;
    @Autowired
    private MemberEcgTokenDao ecgTokenDao;
    @Autowired
    private MemberEcgGraphDetailDao ecgGraphDetailDao;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private NcdMemberDao ncdMemberDao;
    @Autowired
    private MemberCvcHomeVisitDetailDao cvcHomeVisitDetailDao;
    @Autowired
    private MemberCvcClinicVisitDetailDao cvcClinicVisitDetailDao;
    @Autowired
    private EventHandler eventHandler;
    @Autowired
    private MemberRetinopathyTestDetailDao retinopathyDetailDao;


    /**
     * Update additional info of family.
     *
     * @param familyEntity Additional info of family.
     */
    @Override
    public void updateFamilyAdditionalInfo(FamilyEntity familyEntity, Date screeningDate) {
        String financialYear = ImtechoUtil.getFinancialYearFromDate(screeningDate);
        if (checkLastNcdScreeningDone(familyEntity.getFamilyId(), financialYear)) {
            Gson gson = new Gson();
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

    /**
     * Check last ncd screening done or not.
     *
     * @param familyId Family id.
     * @return Returns last ncd screening done or not.
     */
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

        MemberAdditionalInfo additionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeCbacForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberCbacDetail memberCbacDetail = setBasicDataForCbac(memberId, familyId, familyEntity, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.ASHA)) {
            memberCbacDetail.setDoneBy(MemberCbacDetail.DoneBy.ASHA);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            memberCbacDetail.setDoneBy(MemberCbacDetail.DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            memberCbacDetail.setDoneBy(MemberCbacDetail.DoneBy.CHO);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberCbacDetail(key, answer, familyEntity, memberCbacDetail));

        cbacDetailDao.create(memberCbacDetail);

        familyEntity.setAnyMemberCbacDone(Boolean.TRUE);
        familyDao.update(familyEntity);

        updateMemberAdditionalInfoFromCbac(member, memberCbacDetail);
        memberDao.update(member);

        cbacDetailDao.flush();
        return memberCbacDetail.getId();
    }

    /**
     * Set basic details of CBAC.
     *
     * @param memberId        Member id.
     * @param familyId        Family id.
     * @param familyEntity    Family details.
     * @param keyAndAnswerMap Contains key and answers.
     * @return Returns basic details of CBAC.
     */
    private MemberCbacDetail setBasicDataForCbac(Integer memberId, Integer familyId, FamilyEntity familyEntity, Map<String, String> keyAndAnswerMap) {
        MemberCbacDetail memberCbacDetail = new MemberCbacDetail();
        memberCbacDetail.setMemberId(memberId);
        memberCbacDetail.setFamilyId(familyId);
        memberCbacDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberCbacDetail.setLongitude(keyAndAnswerMap.get("-1"));
        memberCbacDetail.setLatitude(keyAndAnswerMap.get("-2"));
        memberCbacDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        memberCbacDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        memberCbacDetail.setDoneOn(new Date(Long.parseLong(keyAndAnswerMap.get("1000"))));
        return memberCbacDetail;
    }

    /**
     * Update additional info of member for CBAC.
     *
     * @param memberEntity     Member details.
     * @param memberCbacDetail CBAC details of member.
     */
    private void updateMemberAdditionalInfoFromCbac(MemberEntity memberEntity, MemberCbacDetail memberCbacDetail) {
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;
        if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setCbacDate(memberCbacDetail.getDoneOn().getTime());
        memberAdditionalInfo.setCbacScore(memberCbacDetail.getScore());
        memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    /**
     * Set answer from CBAC details to family according to key.
     *
     * @param key              Key.
     * @param answer           Answer for family details.
     * @param familyEntity     Family details.
     * @param memberCbacDetail Member's CBAC details.
     */
    private void setAnswersToMemberCbacDetail(String key, String answer, FamilyEntity familyEntity, MemberCbacDetail memberCbacDetail) {
        switch (key) {
            //Family Details
            case "101":
                familyEntity.setTypeOfHouse(answer);
                break;

            case "102":
                familyEntity.setTypeOfToilet(answer);
                break;

            case "103":
                familyEntity.setElectricityAvailability(answer);
                break;

            case "104":
                familyEntity.setDrinkingWaterSource(answer);
                break;

            case "105":
                familyEntity.setFuelForCooking(answer);
                break;

            case "106":
                String[] vehiclesArray = answer.split(",");
                Set<String> vehiclesSet = new HashSet<>(Arrays.asList(vehiclesArray));
                familyEntity.setVehicleDetails(vehiclesSet);
                break;

            case "107":
                familyEntity.setHouseOwnershipStatus(answer);
                break;

            case "108":
                familyEntity.setRationCardNumber(answer);
                break;

            case "109":
                familyEntity.setAnnualIncome(answer);
                break;

            //Risk Assessment
            case "12":
                memberCbacDetail.setSmokeOrConsumeGutka(answer);
                break;

            case "13":
                if (answer.equals("1")) {
                    memberCbacDetail.setConsumeAlcoholDaily(Boolean.TRUE);
                } else {
                    memberCbacDetail.setConsumeAlcoholDaily(Boolean.FALSE);
                }
                break;

            case "15":
                memberCbacDetail.setWaist(answer);
                break;

            case "16":
                memberCbacDetail.setPhysicalActivity150Min(answer);
                break;

            case "17":
                if (answer.equals("1")) {
                    memberCbacDetail.setBpDiabetesHeartHistory(Boolean.TRUE);
                } else {
                    memberCbacDetail.setBpDiabetesHeartHistory(Boolean.FALSE);
                }
                break;

            case "50":
                memberCbacDetail.setScore(Integer.valueOf(answer));
                break;

            //Early Detection
            case "18":
                if (answer.equals("1")) {
                    memberCbacDetail.setShortnessOfBreath(Boolean.TRUE);
                } else {
                    memberCbacDetail.setShortnessOfBreath(Boolean.FALSE);
                }
                break;

            case "19":
                if (answer.equals("1")) {
                    memberCbacDetail.setFitsHistory(Boolean.TRUE);
                } else {
                    memberCbacDetail.setFitsHistory(Boolean.FALSE);
                }
                break;

            case "20":
                if (answer.equals("1")) {
                    memberCbacDetail.setTwoWeeksCoughing(Boolean.TRUE);
                } else {
                    memberCbacDetail.setTwoWeeksCoughing(Boolean.FALSE);
                }
                break;

            case "21":
                if (answer.equals("1")) {
                    memberCbacDetail.setMouthOpeningDifficulty(Boolean.TRUE);
                } else {
                    memberCbacDetail.setMouthOpeningDifficulty(Boolean.FALSE);
                }
                break;

            case "22":
                if (answer.equals("1")) {
                    memberCbacDetail.setBloodInSputum(Boolean.TRUE);
                } else {
                    memberCbacDetail.setBloodInSputum(Boolean.FALSE);
                }
                break;

            case "23":
                if (answer.equals("1")) {
                    memberCbacDetail.setTwoWeeksUlcersInMouth(Boolean.TRUE);
                } else {
                    memberCbacDetail.setTwoWeeksUlcersInMouth(Boolean.FALSE);
                }
                break;

            case "24":
                if (answer.equals("1")) {
                    memberCbacDetail.setTwoWeeksFever(Boolean.TRUE);
                } else {
                    memberCbacDetail.setTwoWeeksFever(Boolean.FALSE);
                }
                break;

            case "25":
                if (answer.equals("1")) {
                    memberCbacDetail.setChangeInToneOfVoice(Boolean.TRUE);
                } else {
                    memberCbacDetail.setChangeInToneOfVoice(Boolean.FALSE);
                }
                break;

            case "26":
                if (answer.equals("1")) {
                    memberCbacDetail.setLossOfWeight(Boolean.TRUE);
                } else {
                    memberCbacDetail.setLossOfWeight(Boolean.FALSE);
                }
                break;

            case "27":
                if (answer.equals("1")) {
                    memberCbacDetail.setPatchOnSkin(Boolean.TRUE);
                } else {
                    memberCbacDetail.setPatchOnSkin(Boolean.FALSE);
                }
                break;

            case "28":
                if (answer.equals("1")) {
                    memberCbacDetail.setNightSweats(Boolean.TRUE);
                } else {
                    memberCbacDetail.setNightSweats(Boolean.FALSE);
                }
                break;
            case "29":
                if (answer.equals("1")) {
                    memberCbacDetail.setDifficultyHoldingObjects(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDifficultyHoldingObjects(Boolean.FALSE);
                }
                break;

            case "30":
                if (answer.equals("1")) {
                    memberCbacDetail.setSensationLossPalm(Boolean.TRUE);
                } else {
                    memberCbacDetail.setSensationLossPalm(Boolean.FALSE);
                }
                break;

            case "31":
                if (answer.equals("1")) {
                    memberCbacDetail.setTakingAntiTbDrugs(Boolean.TRUE);
                } else {
                    memberCbacDetail.setTakingAntiTbDrugs(Boolean.FALSE);
                }
                break;

            case "32":
                if (answer.equals("1")) {
                    memberCbacDetail.setFamilyMemberSufferingFromTb(Boolean.TRUE);
                } else {
                    memberCbacDetail.setFamilyMemberSufferingFromTb(Boolean.FALSE);
                }
                break;
            case "33":
                if (answer.equals("1")) {
                    memberCbacDetail.setHistoryOfTb(Boolean.TRUE);
                } else {
                    memberCbacDetail.setHistoryOfTb(Boolean.FALSE);
                }
                break;

            case "35":
                if (answer.equals("1")) {
                    memberCbacDetail.setLumpInBreast(Boolean.TRUE);
                } else {
                    memberCbacDetail.setLumpInBreast(Boolean.FALSE);
                }
                break;

            case "36":
                if (answer.equals("1")) {
                    memberCbacDetail.setBleedingAfterMenopause(Boolean.TRUE);
                } else {
                    memberCbacDetail.setBleedingAfterMenopause(Boolean.FALSE);
                }
                break;

            case "37":
                if (answer.equals("1")) {
                    memberCbacDetail.setNippleBloodStainedDischarge(Boolean.TRUE);
                } else {
                    memberCbacDetail.setNippleBloodStainedDischarge(Boolean.FALSE);
                }
                break;
            case "38":
                if (answer.equals("1")) {
                    memberCbacDetail.setBleedingAfterIntercourse(Boolean.TRUE);
                } else {
                    memberCbacDetail.setBleedingAfterIntercourse(Boolean.FALSE);
                }
                break;

            case "39":
                if (answer.equals("1")) {
                    memberCbacDetail.setChangeInSizeOfBreast(Boolean.TRUE);
                } else {
                    memberCbacDetail.setChangeInSizeOfBreast(Boolean.FALSE);
                }
                break;

            case "40":
                if (answer.equals("1")) {
                    memberCbacDetail.setFoulVaginalDischarge(Boolean.TRUE);
                } else {
                    memberCbacDetail.setFoulVaginalDischarge(Boolean.FALSE);
                }
                break;

            case "41":
                if (answer.equals("1")) {
                    memberCbacDetail.setBleedingBetweenPeriods(Boolean.TRUE);
                } else {
                    memberCbacDetail.setBleedingBetweenPeriods(Boolean.FALSE);
                }
                break;

            case "42":
                memberCbacDetail.setOccupationalExposure(answer);
                break;

            //Menstrual History
            case "201":
                memberCbacDetail.setAgeAtMenarche(Integer.valueOf(answer));
                break;

            case "202":
                if (answer.equals("1")) {
                    memberCbacDetail.setMenopauseArrived(Boolean.TRUE);
                } else {
                    memberCbacDetail.setMenopauseArrived(Boolean.FALSE);
                }
                break;

            case "203":
                memberCbacDetail.setDurationOfMenoapuse(Integer.valueOf(answer));
                break;

            case "204":
                if (answer.equals("1")) {
                    memberCbacDetail.setPregnant(Boolean.TRUE);
                } else {
                    memberCbacDetail.setPregnant(Boolean.FALSE);
                }
                break;

            case "205":
                if (answer.equals("1")) {
                    memberCbacDetail.setLactating(Boolean.TRUE);
                } else {
                    memberCbacDetail.setLactating(Boolean.FALSE);
                }
                break;

            case "206":
                if (answer.equals("1")) {
                    memberCbacDetail.setRegularPeriods(Boolean.TRUE);
                } else {
                    memberCbacDetail.setRegularPeriods(Boolean.FALSE);
                }
                break;

            case "207":
                memberCbacDetail.setLmp(new Date(Long.parseLong(answer)));
                break;

            case "208":
                memberCbacDetail.setBleeding(answer);
                break;

            case "209":
                memberCbacDetail.setAssociatedWith(answer);
                break;

            case "210":
                memberCbacDetail.setRemarks(answer);
                break;

            case "301":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForHypertension(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForHypertension(Boolean.FALSE);
                }
                break;

            case "302":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForHypertension(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForHypertension(Boolean.FALSE);
                }
                break;

            case "303":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForDiabetes(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForDiabetes(Boolean.FALSE);
                }
                break;

            case "304":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForDiabetes(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForDiabetes(Boolean.FALSE);
                }
                break;

            case "305":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForHeartDiseases(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForHeartDiseases(Boolean.FALSE);
                }
                break;

            case "306":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForHeartDiseases(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForHeartDiseases(Boolean.FALSE);
                }
                break;

            case "307":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForStroke(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForStroke(Boolean.FALSE);
                }
                break;

            case "308":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForStroke(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForStroke(Boolean.FALSE);
                }
                break;

            case "309":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForKidneyFailure(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForKidneyFailure(Boolean.FALSE);
                }
                break;

            case "310":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForKidneyFailure(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForKidneyFailure(Boolean.FALSE);
                }
                break;

            case "311":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForNonHealingWound(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForNonHealingWound(Boolean.FALSE);
                }
                break;

            case "312":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForNonHealingWound(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForNonHealingWound(Boolean.FALSE);
                }
                break;

            case "313":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForCOPD(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForCOPD(Boolean.FALSE);
                }
                break;

            case "314":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForCOPD(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForCOPD(Boolean.FALSE);
                }
                break;

            case "315":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForAsthama(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForAsthama(Boolean.FALSE);
                }
                break;

            case "316":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForAsthama(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForAsthama(Boolean.FALSE);
                }
                break;

            case "317":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForOralCancer(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForOralCancer(Boolean.FALSE);
                }
                break;

            case "318":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForOralCancer(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForOralCancer(Boolean.FALSE);
                }
                break;

            case "319":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForBreastCancer(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForBreastCancer(Boolean.FALSE);
                }
                break;

            case "320":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForBreastCancer(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForBreastCancer(Boolean.FALSE);
                }
                break;

            case "321":
                if (answer.equals("1")) {
                    memberCbacDetail.setDiagnosedForCervicalCancer(Boolean.TRUE);
                } else {
                    memberCbacDetail.setDiagnosedForCervicalCancer(Boolean.FALSE);
                }
                break;

            case "322":
                if (answer.equals("1")) {
                    memberCbacDetail.setUnderTreatementForCervicalCancer(Boolean.TRUE);
                } else {
                    memberCbacDetail.setUnderTreatementForCervicalCancer(Boolean.FALSE);
                }
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
            case "1008":
                memberCbacDetail.setRecurrentUlceration(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1009":
                memberCbacDetail.setRecurrentTingling(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1010":
                memberCbacDetail.setCloudyVision(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1011":
                memberCbacDetail.setReadingDifficulty(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1012":
                memberCbacDetail.setEyePain(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1013":
                memberCbacDetail.setEyeRedness(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1014":
                memberCbacDetail.setHearingDifficulty(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "1015":
                memberCbacDetail.setChewingPain(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2055":
                memberCbacDetail.setMouthUlcers(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2056":
                memberCbacDetail.setMouthPatch(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2030":
                memberCbacDetail.setThickSkin(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2031":
                memberCbacDetail.setNodulesOnSkin(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2032":
                memberCbacDetail.setClawingOfFingers(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2033":
                memberCbacDetail.setTinglingInHand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2034":
                memberCbacDetail.setInabilityCloseEyelid(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2035":
                memberCbacDetail.setFeetWeakness(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            // Occupational Exposure
            case "2005":
                memberCbacDetail.setCropResidueBurning(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2006":
                memberCbacDetail.setGarbageBurning(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2007":
                memberCbacDetail.setWorkingIndustry(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "2008":
                memberCbacDetail.setInterestDoingThings(answer);
                break;

            case "2009":
                memberCbacDetail.setFeelingDown(answer);
                break;
            case "411":
                memberCbacDetail.setReferralId(Integer.valueOf(answer));
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
            case "408":
                memberCbacDetail.setForgetNames(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }

    /**
     * Set Referral Place for member referral details
     *
     * @param healthInfra    Health Infrastructure where member is referred
     * @param memberReferral Member Referral Details
     */
    private void setReferredToForMemberReferral(HealthInfrastructureDetails healthInfra, MemberReferral memberReferral) {
        if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_DISTRICT_HOSPITAL)) {
            memberReferral.setReferredTo(ReferralPlace.D);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_SUB_DISTRICT_HOSPITAL)) {
            memberReferral.setReferredTo(ReferralPlace.B);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_COMMUNITY_HEALTH_CENTER)) {
            memberReferral.setReferredTo(ReferralPlace.C);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_URBAN_COMMUNITY_HEALTH_CENTER)) {
            memberReferral.setReferredTo(ReferralPlace.C);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_PHC)) {
            memberReferral.setReferredTo(ReferralPlace.P);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_UPHC)) {
            memberReferral.setReferredTo(ReferralPlace.U);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_SC)) {
            memberReferral.setReferredTo(ReferralPlace.SC);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_TRUST_HOSPITAL)) {
            memberReferral.setReferredTo(ReferralPlace.T);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_PRIVATE_HOSPITAL)) {
            memberReferral.setReferredTo(ReferralPlace.PVT);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_MEDICAL_COLLEGE_HOSPITAL)) {
            memberReferral.setReferredTo(ReferralPlace.M);
        } else if (healthInfra.getType().equals(HealthInfrastructureConstants.INFRA_GRANT_IN_AID)) {
            memberReferral.setReferredTo(ReferralPlace.G);
        } else {
            memberReferral.setReferredTo(ReferralPlace.OTHERS);
        }
    }

    @Override
    public Map<String, String> storeHealthScreeningForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Map<String, String> returnMap = new LinkedHashMap<>();
//        returnMap.put("createdInstanceId", "1");
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        if (keyAndAnswerMap.get("3") != null && keyAndAnswerMap.get("3").equals("2")) {
            return returnMap;
        }

        MemberHealthDetails healthDetails = setBasicDataForHealthDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            healthDetails.setDoneBy(MemberHealthDetails.DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            healthDetails.setDoneBy(MemberHealthDetails.DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            healthDetails.setDoneBy(MemberHealthDetails.DoneBy.MPHW);
        }

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberHealthDetail(key, answer, healthDetails, keyAndAnswerMap));
        returnMap.put("createdInstanceId", healthDetails.getId().toString());

        healthDetailDao.create(healthDetails);

        updateMemberAdditionalInfoFromHealth(member, healthDetails);
        memberDao.update(member);

        healthDetailDao.flush();

        StringBuilder sb = new StringBuilder();
        sb.append(member.getUniqueHealthId());
        sb.append("-");
        sb.append(member.getFirstName());
        sb.append(" ");
        sb.append(member.getMiddleName());
        sb.append(" ");
        sb.append(member.getLastName());
        returnMap.put("memberId", sb.toString());

        sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n");
        sb.append("Family ID : ");
        sb.append(member.getFamilyId());
        sb.append("\n");
        sb.append("Member ID : ");
        sb.append(member.getUniqueHealthId());
        sb.append("\n");
        sb.append("Member Name : ");
        sb.append(member.getFirstName());
        sb.append(" ");
        sb.append(member.getMiddleName());
        sb.append(" ");
        sb.append(member.getLastName());
        returnMap.put("message", sb.toString());

        return returnMap;
    }

    private MemberHealthDetails setBasicDataForHealthDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberHealthDetails healthDetails = new MemberHealthDetails();
        healthDetails.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        healthDetails.setFamilyId(familyId);
        healthDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        healthDetails.setLongitude(keyAndAnswerMap.get("-1"));
        healthDetails.setLatitude(keyAndAnswerMap.get("-2"));
        healthDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        healthDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        healthDetails.setDoneOn(new Date());
        return healthDetails;
    }

    private void setAnswersToMemberHealthDetail(String key, String answer, MemberHealthDetails
            healthDetails, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "3":
                healthDetails.setProvidedConsent(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "4":
                String[] ansSplit = answer.split("/");
                if (ansSplit.length == 3) {
                    try {
                        healthDetails.setHeight(Integer.valueOf(ansSplit[0]));
                        healthDetails.setWeight(Float.valueOf(ansSplit[1]));
                        healthDetails.setBmi(Float.valueOf(ansSplit[2]));
                    } catch (NumberFormatException e) {
                        healthDetails.setWeight(Float.valueOf(ansSplit[0]));
                        healthDetails.setHeight(Integer.valueOf(ansSplit[1].split("\\.")[0]));
                        healthDetails.setBmi(Float.valueOf(ansSplit[2]));
                    }
                }
                break;
            case "15":
                healthDetails.setWaist(Integer.valueOf(answer));
                break;
            case "6":
                healthDetails.setDiseaseHistory(answer);
                break;
            case "8":
                healthDetails.setOtherDisease(answer);
                break;
            case "9":
                healthDetails.setRiskFactor(answer);
                break;
            default:
        }
    }

    private void updateMemberAdditionalInfoFromHealth(MemberEntity member, MemberHealthDetails healthDetails) {
        Gson gson = new Gson();
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = gson.fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }

        memberAdditionalInfo.setHealthYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getHealthYear(), ImtechoUtil.getFinancialYearFromDate(healthDetails.getDoneOn())));
        member.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
    }

    @Override
    public Integer storeMoConfirmedForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberGeneralScreeningDetail memberGeneralScreeningDetail = setBasicDataForGeneralScreening(memberId, familyId, familyEntity, keyAndAnswerMap);

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberGeneralScreening(key, answer, memberGeneralScreeningDetail));

        Integer id = generalScreeningDao.create(memberGeneralScreeningDetail);
        createOrUpdateMasterRecord(memberGeneralScreeningDetail.getMemberId(),id,"generalScreening");

        updateMemberAdditionalInfoFromGeneralScreening(member, memberGeneralScreeningDetail);
        memberDao.update(member);
        generalScreeningDao.flush();
        return memberGeneralScreeningDetail.getId();
    }

    private String getCommaSeparatedFromLongList(List<Long> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Long value : list) {
            sb.append(value);
            if (i != list.size()) {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }

    private void updateMemberAdditionalInfoFromGeneralScreening(MemberEntity member, MemberGeneralScreeningDetail generalScreeningDetail) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(generalScreeningDetail.getServiceDate().getTime());
//        memberAdditionalInfo.setGeneralScreeningDone(true);
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberGeneralScreeningDetail setBasicDataForGeneralScreening(Integer memberId, Integer familyId, FamilyEntity familyEntity, Map<String, String> keyAndAnswerMap) {
        MemberGeneralScreeningDetail generalScreeningDetail = new MemberGeneralScreeningDetail();
        generalScreeningDetail.setMemberId(memberId);
        generalScreeningDetail.setFamilyId(familyId);
        generalScreeningDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        generalScreeningDetail.setLongitude(keyAndAnswerMap.get("-1"));
        generalScreeningDetail.setLatitude(keyAndAnswerMap.get("-2"));
        return generalScreeningDetail;
    }

    private void setAnswersToMemberGeneralScreening(String key, String answer, MemberGeneralScreeningDetail generalScreeningDetail) {
        switch (key) {
            case "8":
                generalScreeningDetail.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "9":
                generalScreeningDetail.setStrokeHistory(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                generalScreeningDetail.setStrokeDurationYears(Integer.parseInt(answer));
                break;
            case "11":
                generalScreeningDetail.setStrokeDurationMonths(Integer.parseInt(answer));
                break;
            case "12":
                generalScreeningDetail.setStrokeSymptoms(answer);
                break;
            case "14":
                generalScreeningDetail.setFootProblemHistory(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "15":
                generalScreeningDetail.setFootProblemCause(answer);
                break;
            case "16":
                generalScreeningDetail.setAmputatedBodyPart(answer);
                break;
            case "101":
                generalScreeningDetail.setDurationOfHypertension(Integer.parseInt(answer));
                break;
            case "102":
                generalScreeningDetail.setDurationOfDiabetes(Integer.parseInt(answer));
                break;
            case "201":
                generalScreeningDetail.setHadCervicalCancerTest(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "202":
                generalScreeningDetail.setHadBreastCancerTest(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "203":
                generalScreeningDetail.setHadOralCancerTest(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "204":
                generalScreeningDetail.setImageUuid(answer);
                break;
            default:
        }
    }

    @Override
    public Integer storeUrineTestForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberUrineTestDetail urineTestDetail = new MemberUrineTestDetail();
        urineTestDetail.setMemberId(memberId);
        urineTestDetail.setFamilyId(familyId);
        urineTestDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        urineTestDetail.setLongitude(keyAndAnswerMap.get("-1"));
        urineTestDetail.setLatitude(keyAndAnswerMap.get("-2"));
        urineTestDetail.setServiceDate(new Date(Long.parseLong(keyAndAnswerMap.get("8"))));
        if (keyAndAnswerMap.containsKey("9")) {
            urineTestDetail.setUrineTestFlag(ImtechoUtil.returnTrueFalseFromInitials(keyAndAnswerMap.get("9")));
        }
        if (keyAndAnswerMap.containsKey("10")) {
            urineTestDetail.setAlbumin(keyAndAnswerMap.get("10"));
        }

        Integer id = urineTestDetailDao.create(urineTestDetail);
        createOrUpdateMasterRecord(urineTestDetail.getMemberId(),id,"urine");

        updateMemberAdditionalInfoFromUrineScreening(member, urineTestDetail);
        memberDao.update(member);
        urineTestDetailDao.flush();
        return urineTestDetail.getId();
    }

    private void updateMemberAdditionalInfoFromUrineScreening(MemberEntity member, MemberUrineTestDetail urineTestDetail) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(urineTestDetail.getServiceDate().getTime());
//        memberAdditionalInfo.setUrineTestDone(true);
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    @Override
    public Integer storeRetinopathyTestForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberRetinopathyTestDetail retinopathyDetail = new MemberRetinopathyTestDetail();
        retinopathyDetail.setMemberId(memberId);
        retinopathyDetail.setFamilyId(familyId);
        retinopathyDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        retinopathyDetail.setLongitude(keyAndAnswerMap.get("-1"));
        retinopathyDetail.setLatitude(keyAndAnswerMap.get("-2"));

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberRetinopathyScreening(key, answer, retinopathyDetail));

        Integer id =retinopathyDetailDao.create(retinopathyDetail);
        createOrUpdateMasterRecord(retinopathyDetail.getMemberId(),id,"retinopathy");

//        Integer notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NCD_RETINOPATHY_TEST).getId();
//        techoNotificationMasterDao.markNotificationAsCompletedByType(member.getId(), notificationTypeId);
        updateMemberAdditionalInfoFromRetinopathyScreening(member, retinopathyDetail);
        memberDao.update(member);
        retinopathyDetailDao.flush();
        return retinopathyDetail.getId();
    }

    private void updateMemberAdditionalInfoFromRetinopathyScreening(MemberEntity member, MemberRetinopathyTestDetail retinopathyTestDetail) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(retinopathyTestDetail.getServiceDate().getTime());
//        memberAdditionalInfo.setRetinopathyTestDone(true);
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private void setAnswersToMemberRetinopathyScreening(String key, String answer, MemberRetinopathyTestDetail retinopathyTestDetail) {
        switch (key) {
            case "8":
                retinopathyTestDetail.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "9":
                retinopathyTestDetail.setOnDiabetesTreatment(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "10":
                retinopathyTestDetail.setDiabetesTreatment(answer);
                break;
            case "12":
                retinopathyTestDetail.setPastEyeOperation(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "13":
                retinopathyTestDetail.setEyeOperationType(answer);
                break;
            case "14":
                retinopathyTestDetail.setVisionProblemFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "15":
                retinopathyTestDetail.setVisionProblem(answer);
                break;
            case "16":
                retinopathyTestDetail.setVisionProblemDuration(Integer.parseInt(answer));
                break;
            case "19":
                retinopathyTestDetail.setAbsentEyeball(answer);
                break;
            case "17":
                retinopathyTestDetail.setRetinopathyTestFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "18":
                retinopathyTestDetail.setRemidioId(answer);
                break;
            case "20":
                retinopathyTestDetail.setRightEyeRetinopathyDetected(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "21":
                retinopathyTestDetail.setLeftEyeRetinopathyDetected(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }

    @Override
    public Integer storeEcgDetails(MemberEcgDto ecgDto) {

        MemberEcgDetail memberEcgDetail = new MemberEcgDetail();
        memberEcgDetail.setMemberId(ecgDto.getMemberId());
        memberEcgDetail.setLocationId(ecgDto.getLocationId());
        MemberEntity member = memberDao.retrieveById(ecgDto.getMemberId());
        FamilyEntity family = familyDao.retrieveFamilyByFamilyId(member.getFamilyId());
        memberEcgDetail.setFamilyId(family.getId());
        memberEcgDetail.setServiceDate(new Date (ecgDto.getServiceDate()));
        memberEcgDetail.setSymptom(ecgDto.getSelectedSymptoms());
        memberEcgDetail.setOtherSymptom(ecgDto.getOtherSymptoms());
        memberEcgDetail.setDetection(ecgDto.getDetection());
        memberEcgDetail.setEcgType(ecgDto.getEcgType());
        memberEcgDetail.setRecommendation(ecgDto.getRecommendation());
        memberEcgDetail.setRisk(ecgDto.getRisk());
        memberEcgDetail.setAnomalies(ecgDto.getAnomalies());
        memberEcgDetail.setHeartRate(ecgDto.getHeartRate());
        memberEcgDetail.setPr(ecgDto.getPr());
        memberEcgDetail.setQrs(ecgDto.getQrs());
        memberEcgDetail.setQt(ecgDto.getQt());
        memberEcgDetail.setQtc(ecgDto.getQtc());
        memberEcgDetail.setReportPdfDocUuid(ecgDto.getReportPdfDocUuid());
        memberEcgDetail.setReportImageDocUuid(ecgDto.getReportImageDocUuid());

        // Graph Data set
        if(ecgDto.getEcgGraphData() != null) {
            MemberEcgGraphDetail memberEcgGraphDetail = getMemberEcgGraphDetail(ecgDto);
            Integer id = ecgGraphDetailDao.create(memberEcgGraphDetail);
            memberEcgDetail.setGraphDetailId(id);
        }
        Integer ecgId = ecgDetailDao.create(memberEcgDetail);
        createOrUpdateMasterRecord(memberEcgDetail.getMemberId(), ecgId, "ECG");
//        Integer notificationTypeId = notificationTypeMasterDao.retrieveByCode(MobileConstantUtil.NCD_ECG_TEST).getId();
//        techoNotificationMasterDao.markNotificationAsCompletedByType(member.getId(), notificationTypeId);
        updateMemberAdditionalInfoFromEcgScreening(member, memberEcgDetail);
        memberDao.update(member);
        ecgDetailDao.flush();
        ecgGraphDetailDao.flush();
        return memberEcgDetail.getId();
    }

    @NotNull
    private static MemberEcgGraphDetail getMemberEcgGraphDetail(MemberEcgDto ecgDto) {
        MemberEcgGraphDetail memberEcgGraphDetail = new MemberEcgGraphDetail();
        memberEcgGraphDetail.setAvfData(ecgDto.getEcgGraphData().getAvf_data());
        memberEcgGraphDetail.setAvlData(ecgDto.getEcgGraphData().getAvl_data());
        memberEcgGraphDetail.setAvrData(ecgDto.getEcgGraphData().getAvr_data());
        memberEcgGraphDetail.setV1Data(ecgDto.getEcgGraphData().getV1_data());
        memberEcgGraphDetail.setV2Data(ecgDto.getEcgGraphData().getV2_data());
        memberEcgGraphDetail.setV3Data(ecgDto.getEcgGraphData().getV3_data());
        memberEcgGraphDetail.setV4Data(ecgDto.getEcgGraphData().getV4_data());
        memberEcgGraphDetail.setV5Data(ecgDto.getEcgGraphData().getV5_data());
        memberEcgGraphDetail.setV6Data(ecgDto.getEcgGraphData().getV6_data());
        memberEcgGraphDetail.setLead1Data(ecgDto.getEcgGraphData().getLead1_data());
        memberEcgGraphDetail.setLead2Data(ecgDto.getEcgGraphData().getLead2_data());
        memberEcgGraphDetail.setLead3Data(ecgDto.getEcgGraphData().getLead3_data());
        return memberEcgGraphDetail;
    }

    @Override
    public Integer storeEcgToken(MemberEcgTokenDto memberEcgTokenDto, ParsedRecordBean parsedRecordBean, UserMaster user){
        MemberEcgToken memberEcgToken = new MemberEcgToken();
        memberEcgToken.setUserId(user.getId());
        memberEcgToken.setToken(memberEcgTokenDto.getToken());
        memberEcgToken.setTestFailurePoint(memberEcgTokenDto.getTestFailurePoint());
        memberEcgToken.setGeneratedDataPoints(memberEcgTokenDto.getGeneratedDataPoints());
        memberEcgToken.setEcgPosition(memberEcgTokenDto.getEcgPosition());
        memberEcgToken.setCreatedOn(new Date(Long.parseLong(parsedRecordBean.getMobileDate())));
        Integer id = ecgTokenDao.create(memberEcgToken);
        ecgTokenDao.flush();
        return id;
    }

    private void updateMemberAdditionalInfoFromEcgScreening(MemberEntity member, MemberEcgDetail memberEcgDetail) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(memberEcgDetail.getServiceDate().getTime());
//        memberAdditionalInfo.setEcgTestDone(true);
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    @Override
    public Integer storePersonalHistoryForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        MemberPersonalHistoryDetail memberPersonalHistoryDetail = setBasicDataForPersonalHistory(memberId, familyId, familyEntity, keyAndAnswerMap);

        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberPersonalHistory(key, answer, familyEntity, member, memberPersonalHistoryDetail, keyAndAnswerMap));

        personalHistoryDao.create(memberPersonalHistoryDetail);

        member.setPersonalHistoryDone(Boolean.TRUE);
        updateMemberAdditionalInfoFromPersonalHistory(member, memberPersonalHistoryDetail);
        memberDao.update(member);
        personalHistoryDao.flush();
        return memberPersonalHistoryDetail.getId();
    }

    private void updateMemberAdditionalInfoFromPersonalHistory(MemberEntity member, MemberPersonalHistoryDetail personalHistoryDetail) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(personalHistoryDetail.getServiceDate().getTime());
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberPersonalHistoryDetail setBasicDataForPersonalHistory(Integer memberId, Integer familyId, FamilyEntity familyEntity, Map<String, String> keyAndAnswerMap) {
        MemberPersonalHistoryDetail memberPersonalHistoryDetail = new MemberPersonalHistoryDetail();
        memberPersonalHistoryDetail.setMemberId(memberId);
        memberPersonalHistoryDetail.setFamilyId(familyId);
        memberPersonalHistoryDetail.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberPersonalHistoryDetail.setLongitude(keyAndAnswerMap.get("-1"));
        memberPersonalHistoryDetail.setLatitude(keyAndAnswerMap.get("-2"));
        return memberPersonalHistoryDetail;
    }

    private void setAnswersToMemberPersonalHistory(String key, String answer, FamilyEntity familyEntity, MemberEntity memberEntity, MemberPersonalHistoryDetail memberPersonalHistoryDetail, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            // Personal History
            case "301":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForHypertension(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForHypertension(Boolean.FALSE);
                }
                break;

            case "302":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForHypertension(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForHypertension(Boolean.FALSE);
                }
                break;

            case "303":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForDiabetes(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForDiabetes(Boolean.FALSE);
                }
                break;

            case "304":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForDiabetes(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForDiabetes(Boolean.FALSE);
                }
                break;

            case "305":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForHeartDiseases(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForHeartDiseases(Boolean.FALSE);
                }
                break;

            case "306":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForHeartDiseases(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForHeartDiseases(Boolean.FALSE);
                }
                break;

            case "307":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForStroke(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForStroke(Boolean.FALSE);
                }
                break;

            case "308":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForStroke(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForStroke(Boolean.FALSE);
                }
                break;

            case "309":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForKidneyFailure(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForKidneyFailure(Boolean.FALSE);
                }
                break;

            case "310":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForKidneyFailure(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForKidneyFailure(Boolean.FALSE);
                }
                break;

            case "311":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForNonHealingWound(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForNonHealingWound(Boolean.FALSE);
                }
                break;

            case "312":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForNonHealingWound(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForNonHealingWound(Boolean.FALSE);
                }
                break;

            case "313":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForCOPD(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForCOPD(Boolean.FALSE);
                }
                break;

            case "314":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForCOPD(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForCOPD(Boolean.FALSE);
                }
                break;

            case "315":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForAsthama(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForAsthama(Boolean.FALSE);
                }
                break;

            case "316":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForAsthama(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForAsthama(Boolean.FALSE);
                }
                break;

            case "317":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForOralCancer(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForOralCancer(Boolean.FALSE);
                }
                break;

            case "318":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForOralCancer(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForOralCancer(Boolean.FALSE);
                }
                break;

            case "319":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForBreastCancer(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForBreastCancer(Boolean.FALSE);
                }
                break;

            case "320":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForBreastCancer(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForBreastCancer(Boolean.FALSE);
                }
                break;

            case "321":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setDiagnosedForCervicalCancer(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setDiagnosedForCervicalCancer(Boolean.FALSE);
                }
                break;

            case "322":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setUnderTreatementForCervicalCancer(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setUnderTreatementForCervicalCancer(Boolean.FALSE);
                }
                break;

            case "323":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setAnyOtherExamination(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setAnyOtherExamination(Boolean.FALSE);
                }
                break;

            case "324":
                memberPersonalHistoryDetail.setSpecifyOtherExamination(answer);
                break;

            //Personal Examination
            case "1":
                memberPersonalHistoryDetail.setServiceDate(new Date(Long.parseLong(answer)));
                break;

            case "401":
                memberPersonalHistoryDetail.setHeight(Integer.valueOf(answer));
                break;

            case "402":
                memberPersonalHistoryDetail.setWeight(Float.valueOf(answer));
                break;

            case "403":
                String[] split = answer.split("/");
                if (split.length == 3) {
                    try {
                        memberPersonalHistoryDetail.setHeight(Integer.valueOf(split[0]));
                        memberPersonalHistoryDetail.setWeight(Float.valueOf(split[1]));
                        memberPersonalHistoryDetail.setBmi(Float.valueOf(split[2]));
                    } catch (NumberFormatException e) {
                        memberPersonalHistoryDetail.setWeight(Float.valueOf(split[0]));
                        memberPersonalHistoryDetail.setHeight(Integer.valueOf(split[1].split("\\.")[0]));
                        memberPersonalHistoryDetail.setBmi(Float.valueOf(split[2]));
                    }
                }
                break;

            //Menstrual History
            case "201":
                memberPersonalHistoryDetail.setAgeAtMenarche(Integer.valueOf(answer));
                break;

            case "202":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setMenopauseArrived(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setMenopauseArrived(Boolean.FALSE);
                }
                break;

//            case "4067":
//                if (answer != null && memberEntity.getDob() != null) {
//                    String yearsDuration = keyAndAnswerMap.get("4066");
//                    Calendar cal = Calendar.getInstance();
//                    cal.add(Calendar.YEAR, -(Integer.parseInt(yearsDuration)));
//                    cal.add(Calendar.MONTH, -(Integer.parseInt(answer)));
//                    Date currDate = cal.getTime();
//                    int[] diff = ImtechoUtil.calculateAgeYearMonthDayOnGivenDate(memberEntity.getDob().getTime(), currDate.getTime());
//                    memberPersonalHistoryDetail.setMenopauseDurationInYears(diff[0]);
//                    memberPersonalHistoryDetail.setMenopauseDurationInMonths(diff[1]);
//                    memberEntity.setMenopauseDurationInYears(diff[0]);
//                    memberEntity.setMenopauseDurationInMonths(diff[1]);
//                }
//                break;

            case "204":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setPregnant(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setPregnant(Boolean.FALSE);
                }
                break;

            case "205":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setLactating(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setLactating(Boolean.FALSE);
                }
                break;

            case "206":
                if (answer.equals("1")) {
                    memberPersonalHistoryDetail.setRegularPeriods(Boolean.TRUE);
                } else {
                    memberPersonalHistoryDetail.setRegularPeriods(Boolean.FALSE);
                }
                break;

            case "207":
                memberPersonalHistoryDetail.setLmp(new Date(Long.parseLong(answer)));
                memberEntity.setLmpDate(new Date(Long.parseLong(answer)));
                break;

            case "208":
                memberPersonalHistoryDetail.setBleeding(answer);
                break;

            case "209":
                memberPersonalHistoryDetail.setAssociatedWith(answer);
                break;

            case "210":
                memberPersonalHistoryDetail.setRemarks(answer);
                break;
            default:
        }
    }

    @Override
    public Integer storeWeeklyHomeForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_MIGRATED)) {
            return member.getId();
        }
        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
            Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(member.getId());
            if (Boolean.TRUE.equals(abBoolean)) {
                throw new ImtechoMobileException("Member with Health ID " + member.getUniqueHealthId() + " is already marked DEAD. "
                        + "You cannot mark a DEAD member DEAD again.", 1);
            } else {
                markMemberAsDeath(member, familyEntity, keyAndAnswerMap, user, MobileConstantUtil.NCD_FHW_WEEKLY_HOME);
                return member.getId();
            }
        }

        MemberHomeVisitDetails memberHomeVisitDetails = setBasicDataForHomeVisitDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.CC);
        }

        if (keyAndAnswerMap.get("405") != null && !keyAndAnswerMap.get("405").isEmpty()) {
            member.setMobileNumber(keyAndAnswerMap.get("405"));
            memberDao.update(member);
        }
        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberHomeVisitDetail(key, answer, memberHomeVisitDetails, keyAndAnswerMap));

        homeVisitDetailDao.create(memberHomeVisitDetails);

        //update status in ncd_master & create visit history
        Status status = null;
        if (memberHomeVisitDetails.getRequiredReference() != null && memberHomeVisitDetails.getRequiredReference()) {
            if (memberHomeVisitDetails.getReferralPlace().equalsIgnoreCase("MO")) {
                status = Status.REFERRED_MO;
            } else if (memberHomeVisitDetails.getReferralPlace().equalsIgnoreCase("SPECIALIST")) {
                status = Status.REFERRED_CONSULTANT;
            } else {
                status = null;
            }
        } else {
            if (memberHomeVisitDetails.getFollowupVisitType().equalsIgnoreCase("CLINIC_VISIT") || memberHomeVisitDetails.getFollowupVisitType().equalsIgnoreCase("HOME_VISIT")) {
                status = Status.FOLLOWUP;
            } else {
                status = null;
            }
        }

        //add entry in general & followup for web
        Integer generalId = null;
        if (memberHomeVisitDetails.getFollowupVisitType() != null || memberHomeVisitDetails.getReferralPlace() != null) {
            NcdMaster ncdMaster = new NcdMaster();
            //check if ncd master entry exists
            ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberHomeVisitDetails.getMemberId(), DiseaseCode.G.toString());
            Integer masterId = null;
            if (ncdMaster == null) {
                ncdMaster = new NcdMaster();
                ncdMaster.setMemberId(memberHomeVisitDetails.getMemberId());
                ncdMaster.setLocationId(memberHomeVisitDetails.getLocationId());
                ncdMaster.setDiseaseCode(DiseaseCode.G.toString());
                ncdMaster.setStatus(status.toString());
                ncdMaster.setActive(true);
                ncdMaster.setLastVisitDate(memberHomeVisitDetails.getClinicDate());
                masterId = ncdMasterDao.create(ncdMaster);
            } else {
                ncdMaster.setStatus(status.toString());
                ncdMaster.setLastVisitDate(memberHomeVisitDetails.getClinicDate());
                ncdMasterDao.update(ncdMaster);
                masterId = ncdMaster.getId();
            }

            MemberGeneralDetail memberGeneralDetail = new MemberGeneralDetail();
            memberGeneralDetail.setMemberId(memberHomeVisitDetails.getMemberId());
            memberGeneralDetail.setScreeningDate(memberHomeVisitDetails.getClinicDate());
            memberGeneralDetail.setDoneBy(memberHomeVisitDetails.getDoneBy());
            memberGeneralDetail.setLocationId(memberHomeVisitDetails.getLocationId());
            memberGeneralDetail.setFamilyId(memberHomeVisitDetails.getFamilyId());
            memberGeneralDetail.setDoesRequiredRef(memberHomeVisitDetails.getRequiredReference());
            if (memberHomeVisitDetails.getFollowupVisitType() != null) {
                String followupPlace = memberHomeVisitDetails.getFollowupVisitType().equalsIgnoreCase("DOCTOR_VISIT") ? "doctor" : memberHomeVisitDetails.getFollowupVisitType().equalsIgnoreCase("HOME_VISIT") ? "home" : memberHomeVisitDetails.getFollowupVisitType().equalsIgnoreCase("CLINIC_VISIT") ? "clinic" : null;
                memberGeneralDetail.setFollowupPlace(followupPlace);
            }
            if (memberHomeVisitDetails.getReferralPlace() != null) {
                String followupPlace = memberHomeVisitDetails.getReferralPlace().equalsIgnoreCase("MO") ? "MO" : memberHomeVisitDetails.getReferralPlace().equalsIgnoreCase("SPECIALIST") ? "Specialist" : null;
                memberGeneralDetail.setRefferalPlace(followupPlace);
            }
            memberGeneralDetail.setFollowUpDate(memberHomeVisitDetails.getFollowupDate());
            memberGeneralDetail.setMasterId(masterId);
            memberGeneralDetail.setMobileEndDate(new Date(-1L));
            memberGeneralDetail.setMobileStartDate(new Date(-1L));
            generalId = memberGeneralDetailDao.create(memberGeneralDetail);
        }

        MemberHypertensionDetail memberHypertensionDetail = null;
        if (memberHomeVisitDetails.getDiastolicBp() != null) {
            memberHypertensionDetail = new MemberHypertensionDetail();
            memberHypertensionDetail.setScreeningDate(memberHomeVisitDetails.getClinicDate());
            memberHypertensionDetail.setDiastolicBp(memberHomeVisitDetails.getDiastolicBp());
            memberHypertensionDetail.setSystolicBp(memberHomeVisitDetails.getSystolicBp());
            memberHypertensionDetail.setPulseRate(memberHomeVisitDetails.getPulseRate());
            memberHypertensionDetail.setStatus(memberHomeVisitDetails.getHypertensionResult());
            memberHypertensionDetail.setMemberId(memberHomeVisitDetails.getMemberId());
            memberHypertensionDetail.setFamilyId(memberHomeVisitDetails.getFamilyId());
            memberHypertensionDetail.setMobileStartDate(memberHomeVisitDetails.getMobileStartDate());
            memberHypertensionDetail.setMobileEndDate(memberHomeVisitDetails.getMobileEndDate());
            memberHypertensionDetail.setLocationId(memberHomeVisitDetails.getLocationId());
            memberHypertensionDetail.setLatitude(memberHomeVisitDetails.getLatitude());
            memberHypertensionDetail.setLongitude(memberHomeVisitDetails.getLongitude());
            memberHypertensionDetail.setVisitType(MobileConstantUtil.HOME_VISIT);
            Integer id = hypertensionDetailDao.create(memberHypertensionDetail);
            //update master status
            //Integer masterId = updateNcdMasterSubStatus(memberHomeVisitDetails.getMemberId(),DiseaseCode.HT.toString(),subStatus,null,memberHomeVisitDetails.getClinicDate());
            Integer masterId = updateNcdMasterStatus(memberHomeVisitDetails.getMemberId(), DiseaseCode.HT, status, null, memberHomeVisitDetails.getClinicDate());
            //create visit history
            String reading = memberHomeVisitDetails.getSystolicBp() + "/" + memberHomeVisitDetails.getDiastolicBp();
            createVisitHistory(masterId, memberHomeVisitDetails.getMemberId(), memberHomeVisitDetails.getClinicDate(), memberHomeVisitDetails.getDoneBy(), id, memberHomeVisitDetails.getHypertensionResult(), DiseaseCode.HT, memberHomeVisitDetails.getFlag(), reading);
            //create followup entry
            if (generalId != null) {
                MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                memberFollowupDetail.setMemberId(memberHomeVisitDetails.getMemberId());
                memberFollowupDetail.setDiseaseCode(DiseaseCode.HT.toString());
                memberFollowupDetail.setReferenceId(generalId);
                memberFollowupDetailDao.create(memberFollowupDetail);
            }
        }
        MemberMentalHealthDetails memberMentalHealthDetails = null;
        if (memberHomeVisitDetails.getTalk() != null) {
            memberMentalHealthDetails = new MemberMentalHealthDetails();
            memberMentalHealthDetails.setScreeningDate(memberHomeVisitDetails.getClinicDate());
            memberMentalHealthDetails.setTalk(memberHomeVisitDetails.getTalk());
            memberMentalHealthDetails.setOwnDailyWork(memberHomeVisitDetails.getOwnDailyWork());
            memberMentalHealthDetails.setUnderstanding(memberHomeVisitDetails.getUnderstanding());
            memberMentalHealthDetails.setSocialWork(memberHomeVisitDetails.getSocialWork());
            memberMentalHealthDetails.setStatus(memberHomeVisitDetails.getMentalHealthResult());
            memberMentalHealthDetails.setMemberId(memberHomeVisitDetails.getMemberId());
            memberMentalHealthDetails.setFamilyId(memberHomeVisitDetails.getFamilyId());
            memberMentalHealthDetails.setMobileStartDate(memberHomeVisitDetails.getMobileStartDate());
            memberMentalHealthDetails.setMobileEndDate(memberHomeVisitDetails.getMobileEndDate());
            memberMentalHealthDetails.setLocationId(memberHomeVisitDetails.getLocationId());
            memberMentalHealthDetails.setLatitude(memberHomeVisitDetails.getLatitude());
            memberMentalHealthDetails.setLongitude(memberHomeVisitDetails.getLongitude());
            memberMentalHealthDetails.setVisitType(MobileConstantUtil.HOME_VISIT);
            Integer id = mentalHealthDetailDao.create(memberMentalHealthDetails);
            //update master status
            //Integer masterId = updateNcdMasterSubStatus(memberHomeVisitDetails.getMemberId(),DiseaseCode.MH.toString(),subStatus,null,memberHomeVisitDetails.getClinicDate());
            Integer masterId = updateNcdMasterStatus(memberHomeVisitDetails.getMemberId(), DiseaseCode.MH, status, null, memberHomeVisitDetails.getClinicDate());
            //create visit history
            String reading = "Talk : " + memberHomeVisitDetails.getTalk() + ", Daily work : " + memberHomeVisitDetails.getOwnDailyWork() + ", Social work : " + memberHomeVisitDetails.getSocialWork() + ", Understanding : " + memberHomeVisitDetails.getUnderstanding();
            createVisitHistory(masterId, memberHomeVisitDetails.getMemberId(), memberHomeVisitDetails.getClinicDate(), memberHomeVisitDetails.getDoneBy(), id, memberHomeVisitDetails.getMentalHealthResult(), DiseaseCode.MH, memberHomeVisitDetails.getFlag(), reading);
            //create followup entry
            if (generalId != null) {
                MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                memberFollowupDetail.setMemberId(memberHomeVisitDetails.getMemberId());
                memberFollowupDetail.setDiseaseCode(DiseaseCode.MH.toString());
                memberFollowupDetail.setReferenceId(generalId);
                memberFollowupDetailDao.create(memberFollowupDetail);
            }
        }
        MemberDiabetesDetail memberDiabetesDetail = null;
        if (memberHomeVisitDetails.getBloodSugar() != null) {
            memberDiabetesDetail = new MemberDiabetesDetail();
            memberDiabetesDetail.setScreeningDate(memberHomeVisitDetails.getClinicDate());
            memberDiabetesDetail.setMeasurementType("RBS");
            memberDiabetesDetail.setBloodSugar(memberHomeVisitDetails.getBloodSugar());
            memberDiabetesDetail.setStatus(memberHomeVisitDetails.getDiabetesResult());
            memberDiabetesDetail.setMemberId(memberHomeVisitDetails.getMemberId());
            memberDiabetesDetail.setFamilyId(memberHomeVisitDetails.getFamilyId());
            memberDiabetesDetail.setMobileStartDate(memberHomeVisitDetails.getMobileStartDate());
            memberDiabetesDetail.setMobileEndDate(memberHomeVisitDetails.getMobileEndDate());
            memberDiabetesDetail.setLocationId(memberHomeVisitDetails.getLocationId());
            memberDiabetesDetail.setLatitude(memberHomeVisitDetails.getLatitude());
            memberDiabetesDetail.setLongitude(memberHomeVisitDetails.getLongitude());
            memberDiabetesDetail.setVisitType(MobileConstantUtil.HOME_VISIT);
            if (memberHomeVisitDetails.getHeight() != null) {
                memberDiabetesDetail.setHeight(memberHomeVisitDetails.getHeight());
                memberDiabetesDetail.setWeight(memberHomeVisitDetails.getWeight());
                memberDiabetesDetail.setBmi(memberHomeVisitDetails.getBmi());
            }
            Integer id = diabetesDetailDao.create(memberDiabetesDetail);
            //update master status
            //Integer masterId = updateNcdMasterSubStatus(memberHomeVisitDetails.getMemberId(),DiseaseCode.D.toString(),subStatus,null,memberHomeVisitDetails.getClinicDate());
            Integer masterId = updateNcdMasterStatus(memberHomeVisitDetails.getMemberId(), DiseaseCode.D, status, null, memberHomeVisitDetails.getClinicDate());
            //create visit history
            createVisitHistory(masterId, memberHomeVisitDetails.getMemberId(), memberHomeVisitDetails.getClinicDate(), memberHomeVisitDetails.getDoneBy(), id, memberHomeVisitDetails.getDiabetesResult(), DiseaseCode.D, memberHomeVisitDetails.getFlag(), memberHomeVisitDetails.getBloodSugar().toString());
            //create followup entry
            if (generalId != null) {
                MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                memberFollowupDetail.setMemberId(memberHomeVisitDetails.getMemberId());
                memberFollowupDetail.setDiseaseCode(DiseaseCode.D.toString());
                memberFollowupDetail.setReferenceId(generalId);
                memberFollowupDetailDao.create(memberFollowupDetail);
            }
        }

        NcdMemberEntity ncdMember = createOrUpdateNcdMemberDetails(member, familyEntity, null, memberHomeVisitDetails, null, null);

        ncdMemberDao.createOrUpdate(ncdMember);
        updateMemberAdditionalInfoFromHomeVisit(member, memberHomeVisitDetails);
        memberDao.update(member);
        homeVisitDetailDao.flush();
        ncdMemberDao.flush();
        memberDao.flush();
        mentalHealthDetailDao.flush();
        hypertensionDetailDao.flush();
        diabetesDetailDao.flush();
        memberDiseaseMedicineDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, MobileConstantUtil.NOTIFICATION_NCD_HOME_VISIT, memberHomeVisitDetails.getId()));
        return memberHomeVisitDetails.getId();
    }

    private void updateMemberAdditionalInfoFromHomeVisit(MemberEntity member, MemberHomeVisitDetails homeVisitDetails) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (homeVisitDetails.getHeight() != null) {
            memberAdditionalInfo.setHeight(homeVisitDetails.getHeight());
        }
        if (homeVisitDetails.getWeight() != null) {
            memberAdditionalInfo.setWeight(homeVisitDetails.getWeight());
            member.setWeight(homeVisitDetails.getWeight());
        }
        if (homeVisitDetails.getBmi() != null) {
            memberAdditionalInfo.setBmi(homeVisitDetails.getBmi());
        }
        memberAdditionalInfo.setLastServiceLongDate(homeVisitDetails.getClinicDate().getTime());
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberHomeVisitDetails setBasicDataForHomeVisitDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberHomeVisitDetails memberHomeVisitDetails = new MemberHomeVisitDetails();
        memberHomeVisitDetails.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        memberHomeVisitDetails.setFamilyId(familyId);
        memberHomeVisitDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberHomeVisitDetails.setLongitude(keyAndAnswerMap.get("-1"));
        memberHomeVisitDetails.setLatitude(keyAndAnswerMap.get("-2"));
        memberHomeVisitDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        memberHomeVisitDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        memberHomeVisitDetails.setDoneOn(new Date());
        return memberHomeVisitDetails;
    }

    private void setAnswersToMemberHomeVisitDetail(String key, String answer, MemberHomeVisitDetails
            memberHomeVisitDetails, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "9":
                memberHomeVisitDetails.setClinicDate(new Date(Long.parseLong(answer)));
                break;
            case "15":
                String[] arr1 = answer.split("-");
                if (arr1.length > 1) {
                    memberHomeVisitDetails.setSystolicBp(Integer.valueOf(arr1[1].split("\\.")[0]));
                    memberHomeVisitDetails.setDiastolicBp(Integer.valueOf(arr1[2].split("\\.")[0]));
                }
                break;
            case "16":
                answer = answer.replace(".", "");
                if (!answer.isEmpty()) {
                    memberHomeVisitDetails.setPulseRate(Integer.valueOf(answer));
                }
                break;
            case "17":
                if (answer.equalsIgnoreCase("controlled")) {
                    memberHomeVisitDetails.setHypertensionResult(Status.CONTROLLED.toString());
                } else if (answer.equalsIgnoreCase("uncontrolled")) {
                    memberHomeVisitDetails.setHypertensionResult(Status.UNCONTROLLED.toString());
                }
                break;
            case "14":
                memberHomeVisitDetails.setBloodSugar(Integer.valueOf(answer.replace(".", "")));
                break;
            case "45":
                if (answer.equalsIgnoreCase("controlled")) {
                    memberHomeVisitDetails.setDiabetesResult(Status.CONTROLLED.toString());
                } else if (answer.equalsIgnoreCase("uncontrolled")) {
                    memberHomeVisitDetails.setDiabetesResult(Status.UNCONTROLLED.toString());
                }
                break;
            case "48":
                if (answer.equalsIgnoreCase("controlled")) {
                    memberHomeVisitDetails.setMentalHealthResult(Status.CONTROLLED.toString());
                } else if (answer.equalsIgnoreCase("uncontrolled")) {
                    memberHomeVisitDetails.setMentalHealthResult(Status.UNCONTROLLED.toString());
                }
                break;
            case "20":
                memberHomeVisitDetails.setTalk(Integer.valueOf(answer));
                break;
            case "21":
                memberHomeVisitDetails.setOwnDailyWork(Integer.valueOf(answer));
                break;
            case "22":
                memberHomeVisitDetails.setSocialWork(Integer.valueOf(answer));
                break;
            case "23":
                memberHomeVisitDetails.setUnderstanding(Integer.valueOf(answer));
                break;
            case "24":
                memberHomeVisitDetails.setPatientTakingMedicine(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "201":
                memberHomeVisitDetails.setAnyAdverseEffect(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "202":
                memberHomeVisitDetails.setAdverseEffect(answer);
                break;
            case "26":
                memberHomeVisitDetails.setRequiredReference(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "203":
                memberHomeVisitDetails.setGivenConsent(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "204":
                memberHomeVisitDetails.setReferralPlace(answer);
                memberHomeVisitDetails.setReferStatus("REFER_MO");
                break;
            case "205":
                memberHomeVisitDetails.setOtherReferralPlace(answer);
                break;
            case "206":
                memberHomeVisitDetails.setReferralId(Integer.valueOf(answer));
                break;
            case "33":
                memberHomeVisitDetails.setRemarks(answer);
                break;
            case "27":
                memberHomeVisitDetails.setReferenceReason(answer);
                break;
            case "29":
                memberHomeVisitDetails.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "31":
                memberHomeVisitDetails.setFlagReason(answer);
                break;
            case "41":
                memberHomeVisitDetails.setOtherReason(answer);
                break;
            case "42":
                memberHomeVisitDetails.setFollowupVisitType(answer);
                break;
            case "43":
                memberHomeVisitDetails.setFollowupDate(new Date(Long.parseLong(answer)));
                break;
            case "35":
                String[] ansSplit = answer.split("/");
                if (ansSplit.length == 3) {
                    try {
                        memberHomeVisitDetails.setHeight(Integer.valueOf(ansSplit[0]));
                        memberHomeVisitDetails.setWeight(Float.valueOf(ansSplit[1]));
                        memberHomeVisitDetails.setBmi(Float.valueOf(ansSplit[2]));
                    } catch (NumberFormatException e) {
                        memberHomeVisitDetails.setWeight(Float.valueOf(ansSplit[0]));
                        memberHomeVisitDetails.setHeight(Integer.valueOf(ansSplit[1].split("\\.")[0]));
                        memberHomeVisitDetails.setBmi(Float.valueOf(ansSplit[2]));
                    }
                }
                break;
            default:
        }
    }

    @Override
    public Integer storeWeeklyClinicForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);


        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_MIGRATED)) {
            return member.getId();
        }

        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
            Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(member.getId());
            if (Boolean.TRUE.equals(abBoolean)) {
                throw new ImtechoMobileException("Member with Health ID " + member.getUniqueHealthId() + " is already marked DEAD. "
                        + "You cannot mark a DEAD member DEAD again.", 1);
            } else {
                markMemberAsDeath(member, familyEntity, keyAndAnswerMap, user, MobileConstantUtil.NCD_FHW_WEEKLY_CLINIC);
                return member.getId();
            }
        }

        MemberClinicVisitDetails memberClinicVisitDetails = setBasicDataForClinicVisitDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.CC);
        }

        if (keyAndAnswerMap.get("405") != null && !keyAndAnswerMap.get("405").isEmpty()) {
            member.setMobileNumber(keyAndAnswerMap.get("405"));
            memberDao.update(member);
        }
        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberClinicVisitDetail(key, answer, memberClinicVisitDetails, keyAndAnswerMap));

        clinicVisitDetailDao.create(memberClinicVisitDetails);

        //update status in ncd_master & create visit history
        Status status = null;
        if (memberClinicVisitDetails.getRequiredReference() != null && memberClinicVisitDetails.getRequiredReference()) {
            if (memberClinicVisitDetails.getReferralPlace().equalsIgnoreCase("MO")) {
                status = Status.REFERRED_MO;
            } else if (memberClinicVisitDetails.getReferralPlace().equalsIgnoreCase("SPECIALIST")) {
                status = Status.REFERRED_CONSULTANT;
            } else {
                status = null;
            }
        } else {
            if (memberClinicVisitDetails.getFollowupVisitType().equalsIgnoreCase("CLINIC_VISIT") || memberClinicVisitDetails.getFollowupVisitType().equalsIgnoreCase("HOME_VISIT")) {
                status = Status.FOLLOWUP;
            } else {
                status = null;
            }
        }

        //add entry in general & followup for web
        Integer generalId = null;
        if (memberClinicVisitDetails.getFollowupVisitType() != null || memberClinicVisitDetails.getReferralPlace() != null) {
            NcdMaster ncdMaster = new NcdMaster();
            //check if ncd master entry exists
            ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberClinicVisitDetails.getMemberId(), DiseaseCode.G.toString());
            Integer masterId = null;
            if (ncdMaster == null) {
                ncdMaster = new NcdMaster();
                ncdMaster.setMemberId(memberClinicVisitDetails.getMemberId());
                ncdMaster.setLocationId(memberClinicVisitDetails.getLocationId());
                ncdMaster.setDiseaseCode(DiseaseCode.G.toString());
                ncdMaster.setStatus(status.toString());
                ncdMaster.setActive(true);
                ncdMaster.setLastVisitDate(memberClinicVisitDetails.getClinicDate());
                masterId = ncdMasterDao.create(ncdMaster);
            } else {
                ncdMaster.setStatus(status.toString());
                ncdMaster.setLastVisitDate(memberClinicVisitDetails.getClinicDate());
                ncdMasterDao.update(ncdMaster);
                masterId = ncdMaster.getId();
            }

            MemberGeneralDetail memberGeneralDetail = new MemberGeneralDetail();
            memberGeneralDetail.setMemberId(memberClinicVisitDetails.getMemberId());
            memberGeneralDetail.setScreeningDate(memberClinicVisitDetails.getClinicDate());
            memberGeneralDetail.setDoneBy(memberClinicVisitDetails.getDoneBy());
            memberGeneralDetail.setLocationId(memberClinicVisitDetails.getLocationId());
            memberGeneralDetail.setFamilyId(memberClinicVisitDetails.getFamilyId());
            memberGeneralDetail.setDoesRequiredRef(memberClinicVisitDetails.getRequiredReference());
            if (memberClinicVisitDetails.getFollowupVisitType() != null) {
                String followupPlace = memberClinicVisitDetails.getFollowupVisitType().equalsIgnoreCase("DOCTOR_VISIT") ? "doctor" : memberClinicVisitDetails.getFollowupVisitType().equalsIgnoreCase("HOME_VISIT") ? "home" : memberClinicVisitDetails.getFollowupVisitType().equalsIgnoreCase("CLINIC_VISIT") ? "clinic" : null;
                memberGeneralDetail.setFollowupPlace(followupPlace);
            } else if (memberClinicVisitDetails.getReferralPlace() != null) {
                String followupPlace = memberClinicVisitDetails.getReferralPlace().equalsIgnoreCase("MO") ? "MO" : memberClinicVisitDetails.getReferralPlace().equalsIgnoreCase("SPECIALIST") ? "Specialist" : null;
                memberGeneralDetail.setRefferalPlace(followupPlace);
            }
            memberGeneralDetail.setFollowUpDate(memberClinicVisitDetails.getFollowupDate());
            memberGeneralDetail.setMasterId(masterId);
            memberGeneralDetail.setMobileEndDate(new Date(-1L));
            memberGeneralDetail.setMobileStartDate(new Date(-1L));
            generalId = memberGeneralDetailDao.create(memberGeneralDetail);
        }

        MemberHypertensionDetail memberHypertensionDetail = null;
        if (memberClinicVisitDetails.getDiastolicBp() != null) {
            memberHypertensionDetail = getMemberHypertensionDetail(user, memberClinicVisitDetails);
            Integer id = hypertensionDetailDao.create(memberHypertensionDetail);
            //update master status
            //Integer masterId = updateNcdMasterSubStatus(memberClinicVisitDetails.getMemberId(),DiseaseCode.HT.toString(),subStatus,null,memberClinicVisitDetails.getClinicDate());
            Integer masterId = updateNcdMasterStatus(memberClinicVisitDetails.getMemberId(), DiseaseCode.HT, status, null, memberClinicVisitDetails.getClinicDate());
            //create visit history
            String reading = memberClinicVisitDetails.getSystolicBp() + "/" + memberClinicVisitDetails.getDiastolicBp();
            createVisitHistory(masterId, memberClinicVisitDetails.getMemberId(), memberClinicVisitDetails.getClinicDate(), memberClinicVisitDetails.getDoneBy(), id, memberClinicVisitDetails.getHypertensionResult(), DiseaseCode.HT, memberClinicVisitDetails.getFlag(), reading);
            //create followup entry
            if (generalId != null) {
                MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                memberFollowupDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                memberFollowupDetail.setDiseaseCode(DiseaseCode.HT.toString());
                memberFollowupDetail.setReferenceId(generalId);
                memberFollowupDetailDao.create(memberFollowupDetail);
            }
        }

        MemberMentalHealthDetails memberMentalHealthDetails = null;
        if (memberClinicVisitDetails.getTalk() != null) {
            memberMentalHealthDetails = getMemberMentalHealthDetails(user, memberClinicVisitDetails);
            Integer id = mentalHealthDetailDao.create(memberMentalHealthDetails);
            //update master status
            //Integer masterId = updateNcdMasterSubStatus(memberClinicVisitDetails.getMemberId(),DiseaseCode.MH.toString(),subStatus,null,memberClinicVisitDetails.getClinicDate());
            Integer masterId = updateNcdMasterStatus(memberClinicVisitDetails.getMemberId(), DiseaseCode.MH, status, null, memberClinicVisitDetails.getClinicDate());
            //create visit history
            String reading = "Talk : " + memberClinicVisitDetails.getTalk() + ", Daily work : " + memberClinicVisitDetails.getOwnDailyWork() + ", Social work : " + memberClinicVisitDetails.getSocialWork() + ", Understanding : " + memberClinicVisitDetails.getUnderstanding();
            createVisitHistory(masterId, memberClinicVisitDetails.getMemberId(), memberClinicVisitDetails.getClinicDate(), memberClinicVisitDetails.getDoneBy(), id, memberClinicVisitDetails.getMentalHealthResult(), DiseaseCode.MH, memberClinicVisitDetails.getFlag(), reading);
            //create followup entry
            if (generalId != null) {
                MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                memberFollowupDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                memberFollowupDetail.setDiseaseCode(DiseaseCode.MH.toString());
                memberFollowupDetail.setReferenceId(generalId);
                memberFollowupDetailDao.create(memberFollowupDetail);
            }
        }

        MemberDiabetesDetail memberDiabetesDetail = null;
        if (memberClinicVisitDetails.getBloodSugar() != null) {
            memberDiabetesDetail = getMemberDiabetesDetail(user, memberClinicVisitDetails);
            Integer id = diabetesDetailDao.create(memberDiabetesDetail);
            //update master status
            //Integer masterId = updateNcdMasterSubStatus(memberClinicVisitDetails.getMemberId(),DiseaseCode.D.toString(),subStatus,null,memberClinicVisitDetails.getClinicDate());
            Integer masterId = updateNcdMasterStatus(memberClinicVisitDetails.getMemberId(), DiseaseCode.D, status, null, memberClinicVisitDetails.getClinicDate());
            //create visit history
            createVisitHistory(masterId, memberClinicVisitDetails.getMemberId(), memberClinicVisitDetails.getClinicDate(), memberClinicVisitDetails.getDoneBy(), id, memberClinicVisitDetails.getDiabetesResult(), DiseaseCode.D, memberClinicVisitDetails.getFlag(), memberClinicVisitDetails.getBloodSugar().toString());
            //create followup entry
            if (generalId != null) {
                MemberFollowupDetail memberFollowupDetail = new MemberFollowupDetail();
                memberFollowupDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                memberFollowupDetail.setDiseaseCode(DiseaseCode.D.toString());
                memberFollowupDetail.setReferenceId(generalId);
                memberFollowupDetailDao.create(memberFollowupDetail);
            }
        }
        NcdMemberEntity ncdMember = createOrUpdateNcdMemberDetails(member, familyEntity, memberClinicVisitDetails, null, null, null);

        if (keyAndAnswerMap.containsKey("301")) {
            String ans = keyAndAnswerMap.get("301");
            if (ans != null && !ans.isEmpty()) {
                List<MedicineListDataBean> medicines = new Gson().fromJson(ans, new TypeToken<List<MedicineListDataBean>>() {
                }.getType());

                if (!medicines.isEmpty()) {
                    for (MedicineListDataBean medicine : medicines) {
                        MemberDiseaseMedicine diseaseMedicine = memberDiseaseMedicineDao.retrieveDetailByMemberAndMedicine(memberClinicVisitDetails.getMemberId(), medicine.getMedicineId());
                        if (diseaseMedicine != null) {
                            if (medicine.getSpecialInstruction() != null) {
                                diseaseMedicine.setSpecialInstruction(medicine.getSpecialInstruction());
                            }
                            if (!diseaseMedicine.getDuration().equals(medicine.getDuration())) {
                                Integer newDurationValue = medicine.getDuration();

                                DrugInventoryDetail drugInventoryDetail = new DrugInventoryDetail();
                                drugInventoryDetail.setMedicineId(diseaseMedicine.getMedicineId());
                                drugInventoryDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
                                drugInventoryDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
                                drugInventoryDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                                drugInventoryDetail.setIssuedDate(memberClinicVisitDetails.getClinicDate());
                                drugInventoryDetail.setIsReturn(false);
                                DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicineByLocationId
                                        (diseaseMedicine.getMedicineId(), memberClinicVisitDetails.getLocationId());
                                if (diseaseMedicine.getDuration() < newDurationValue) {
                                    drugInventoryDetail.setIsIssued(true);
                                    drugInventoryDetail.setQuantityIssued(diseaseMedicine.getFrequency() * (newDurationValue - diseaseMedicine.getDuration()));
                                    drugInventoryDetail.setIsReceived(false);
                                    drugInventoryDetail.setQuantityReceived(0);
                                    drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                                    drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - drugInventoryDetail.getQuantityIssued());
                                } else if (diseaseMedicine.getDuration() > newDurationValue) {
                                    drugInventoryDetail.setIsIssued(false);
                                    drugInventoryDetail.setQuantityIssued(0);
                                    drugInventoryDetail.setIsReceived(true);
                                    drugInventoryDetail.setQuantityReceived(diseaseMedicine.getFrequency() * (diseaseMedicine.getDuration() - newDurationValue));
                                    drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                                    drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() + drugInventoryDetail.getQuantityIssued());
                                }
                                drugInventoryDao.create(drugInventoryDetail);

                                diseaseMedicine.setDuration(newDurationValue);
                                diseaseMedicine.setQuantity(newDurationValue * diseaseMedicine.getFrequency());
                                Calendar c = Calendar.getInstance();
                                c.setTime(diseaseMedicine.getDiagnosedOn());
                                c.add(Calendar.DATE, diseaseMedicine.getDuration());
                                diseaseMedicine.setExpiryDate(c.getTime());
                                memberDiseaseMedicineDao.saveOrUpdate(diseaseMedicine);
                            }
                        } else {
                            MemberDiseaseMedicine memberDiseaseMedicine = new MemberDiseaseMedicine();
                            memberDiseaseMedicine.setMemberId(member.getId());
                            memberDiseaseMedicine.setDiagnosedOn(memberClinicVisitDetails.getClinicDate());
                            memberDiseaseMedicine.setStartDate(memberClinicVisitDetails.getClinicDate());
                            memberDiseaseMedicine.setMedicineId(medicine.getMedicineId());
                            memberDiseaseMedicine.setFrequency(medicine.getFrequency());
                            memberDiseaseMedicine.setDuration(medicine.getDuration());
                            memberDiseaseMedicine.setQuantity(medicine.getQuantity());
                            if (medicine.getSpecialInstruction() != null) {
                                memberDiseaseMedicine.setSpecialInstruction(medicine.getSpecialInstruction());
                            }
                            Calendar c = Calendar.getInstance();
                            c.setTime(memberDiseaseMedicine.getDiagnosedOn());
                            c.add(Calendar.DATE, memberDiseaseMedicine.getDuration());
                            memberDiseaseMedicine.setExpiryDate(c.getTime());
                            memberDiseaseMedicine.setActive(true);
                            memberDiseaseMedicineDao.create(memberDiseaseMedicine);

                            //update quantity in drug inventory
                            DrugInventoryDetail drugInventoryDetail = new DrugInventoryDetail();
                            drugInventoryDetail.setMedicineId(memberDiseaseMedicine.getMedicineId());
                            drugInventoryDetail.setIssuedDate(memberClinicVisitDetails.getClinicDate());
                            drugInventoryDetail.setIsIssued(true);
                            drugInventoryDetail.setQuantityIssued(memberDiseaseMedicine.getQuantity());
                            drugInventoryDetail.setIsReceived(false);
                            drugInventoryDetail.setIsReturn(false);
                            drugInventoryDetail.setQuantityReceived(0);
                            drugInventoryDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                            drugInventoryDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
                            drugInventoryDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
                            //retrieve last record to calculate balance in hand
                            DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicineByLocationId
                                    (memberDiseaseMedicine.getMedicineId(), memberClinicVisitDetails.getLocationId());
                            drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                            drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - memberDiseaseMedicine.getQuantity());
                            drugInventoryDao.create(drugInventoryDetail);
                        }
                    }
                }
            }
        }

        if (keyAndAnswerMap.containsKey("302")) {
            String ans = keyAndAnswerMap.get("302");
            if (ans != null && !ans.isEmpty()) {
                List<MedicineListDataBean> medicines = new Gson().fromJson(ans, new TypeToken<List<MedicineListDataBean>>() {
                }.getType());

                if (!medicines.isEmpty()) {
                    for (MedicineListDataBean medicine : medicines) {
                        MemberDiseaseMedicine memberDiseaseMedicine = new MemberDiseaseMedicine();
                        memberDiseaseMedicine.setMemberId(member.getId());
                        memberDiseaseMedicine.setDiagnosedOn(memberClinicVisitDetails.getClinicDate());
                        memberDiseaseMedicine.setStartDate(memberClinicVisitDetails.getClinicDate());
                        memberDiseaseMedicine.setMedicineId(medicine.getMedicineId());
                        memberDiseaseMedicine.setFrequency(medicine.getFrequency());
                        memberDiseaseMedicine.setDuration(medicine.getDuration());
                        memberDiseaseMedicine.setQuantity(medicine.getQuantity());
                        if (medicine.getSpecialInstruction() != null) {
                            memberDiseaseMedicine.setSpecialInstruction(medicine.getSpecialInstruction());
                        }
                        Calendar c = Calendar.getInstance();
                        c.setTime(memberDiseaseMedicine.getDiagnosedOn());
                        c.add(Calendar.DATE, memberDiseaseMedicine.getDuration());
                        memberDiseaseMedicine.setExpiryDate(c.getTime());
                        memberDiseaseMedicine.setActive(true);
                        memberDiseaseMedicineDao.create(memberDiseaseMedicine);


                        //update quantity in drug inventory
                        DrugInventoryDetail drugInventoryDetail = new DrugInventoryDetail();
                        drugInventoryDetail.setMedicineId(memberDiseaseMedicine.getMedicineId());
                        drugInventoryDetail.setIssuedDate(memberClinicVisitDetails.getClinicDate());
                        drugInventoryDetail.setIsIssued(true);
                        drugInventoryDetail.setQuantityIssued(memberDiseaseMedicine.getQuantity());
                        drugInventoryDetail.setIsReceived(false);
                        drugInventoryDetail.setIsReturn(false);
                        drugInventoryDetail.setQuantityReceived(0);
                        drugInventoryDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                        drugInventoryDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
                        drugInventoryDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
                        //retrieve last record to calculate balance in hand
                        DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicineByLocationId
                                (memberDiseaseMedicine.getMedicineId(), memberClinicVisitDetails.getLocationId());
                        drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                        drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - memberDiseaseMedicine.getQuantity());
                        drugInventoryDao.create(drugInventoryDetail);
                    }
                }
            }
        }

        ncdMemberDao.createOrUpdate(ncdMember);
        updateMemberAdditionalInfoFromClinicVisit(member, memberClinicVisitDetails);
        memberDao.update(member);
        clinicVisitDetailDao.flush();
        memberDao.flush();
        ncdMemberDao.flush();
        mentalHealthDetailDao.flush();
        hypertensionDetailDao.flush();
        diabetesDetailDao.flush();
        memberDiseaseMedicineDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, MobileConstantUtil.NOTIFICATION_NCD_CLINIC_VISIT, memberClinicVisitDetails.getId()));
        return memberClinicVisitDetails.getId();
    }

    private void updateMemberAdditionalInfoFromClinicVisit(MemberEntity member, MemberClinicVisitDetails clinicVisitDetails) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (clinicVisitDetails.getHeight() != null) {
            memberAdditionalInfo.setHeight(clinicVisitDetails.getHeight());
        }
        if (clinicVisitDetails.getWeight() != null) {
            memberAdditionalInfo.setWeight(clinicVisitDetails.getWeight());
            member.setWeight(clinicVisitDetails.getWeight());
        }
        if (clinicVisitDetails.getBmi() != null) {
            memberAdditionalInfo.setBmi(clinicVisitDetails.getBmi());
        }
        memberAdditionalInfo.setLastServiceLongDate(clinicVisitDetails.getClinicDate().getTime());
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberClinicVisitDetails setBasicDataForClinicVisitDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberClinicVisitDetails memberClinicVisitDetails = new MemberClinicVisitDetails();
        memberClinicVisitDetails.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        memberClinicVisitDetails.setFamilyId(familyId);
        memberClinicVisitDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberClinicVisitDetails.setLongitude(keyAndAnswerMap.get("-1"));
        memberClinicVisitDetails.setLatitude(keyAndAnswerMap.get("-2"));
        memberClinicVisitDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        memberClinicVisitDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        memberClinicVisitDetails.setDoneOn(new Date());
        return memberClinicVisitDetails;
    }

    private void setAnswersToMemberClinicVisitDetail(String key, String answer, MemberClinicVisitDetails
            memberClinicVisitDetails, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "9":
                memberClinicVisitDetails.setClinicDate(new Date(Long.parseLong(answer)));
                break;
            case "10":
                memberClinicVisitDetails.setClinicType(answer);
                break;
            case "15":
                String[] arr1 = answer.split("-");
                if (arr1.length > 1) {
                    memberClinicVisitDetails.setSystolicBp(Integer.valueOf(arr1[1].split("\\.")[0]));
                    memberClinicVisitDetails.setDiastolicBp(Integer.valueOf(arr1[2].split("\\.")[0]));
                }
                break;
            case "16":
                answer = answer.replace(".", "");
                if (!answer.isEmpty()) {
                    memberClinicVisitDetails.setPulseRate(Integer.valueOf(answer));
                }
                break;
            case "17":
                if (answer.equalsIgnoreCase("controlled")) {
                    memberClinicVisitDetails.setHypertensionResult(Status.CONTROLLED.toString());
                } else if (answer.equalsIgnoreCase("uncontrolled")) {
                    memberClinicVisitDetails.setHypertensionResult(Status.UNCONTROLLED.toString());
                }
                break;
            case "14":
                memberClinicVisitDetails.setBloodSugar(Integer.valueOf(answer.replace(".", "")));
                break;
            case "45":
                if (answer.equalsIgnoreCase("controlled")) {
                    memberClinicVisitDetails.setDiabetesResult(Status.CONTROLLED.toString());
                } else if (answer.equalsIgnoreCase("uncontrolled")) {
                    memberClinicVisitDetails.setDiabetesResult(Status.UNCONTROLLED.toString());
                }
                break;
            case "48":
                if (answer.equalsIgnoreCase("controlled")) {
                    memberClinicVisitDetails.setMentalHealthResult(Status.CONTROLLED.toString());
                } else if (answer.equalsIgnoreCase("uncontrolled")) {
                    memberClinicVisitDetails.setMentalHealthResult(Status.UNCONTROLLED.toString());
                }
                break;
            case "20":
                memberClinicVisitDetails.setTalk(Integer.valueOf(answer));
                break;
            case "21":
                memberClinicVisitDetails.setOwnDailyWork(Integer.valueOf(answer));
                break;
            case "22":
                memberClinicVisitDetails.setSocialWork(Integer.valueOf(answer));
                break;
            case "23":
                memberClinicVisitDetails.setUnderstanding(Integer.valueOf(answer));
                break;
            case "24":
                memberClinicVisitDetails.setPatientTakingMedicine(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "26":
                memberClinicVisitDetails.setRequiredReference(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "27":
                memberClinicVisitDetails.setReferenceReason(answer);
                break;
            case "28":
                memberClinicVisitDetails.setReferralPlace(answer);
                memberClinicVisitDetails.setReferStatus("REFER_MO");
                break;
            case "29":
                memberClinicVisitDetails.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "31":
                memberClinicVisitDetails.setFlagReason(answer);
                break;
            case "41":
                memberClinicVisitDetails.setOtherReason(answer);
                break;
            case "42":
                memberClinicVisitDetails.setFollowupVisitType(answer);
                break;
            case "43":
                memberClinicVisitDetails.setFollowupDate(new Date(Long.parseLong(answer)));
                break;
            case "33":
                memberClinicVisitDetails.setRemarks(answer);
                break;
            case "35":
                String[] ansSplit = answer.split("/");
                if (ansSplit.length == 3) {
                    try {
                        memberClinicVisitDetails.setHeight(Integer.valueOf(ansSplit[0]));
                        memberClinicVisitDetails.setWeight(Float.valueOf(ansSplit[1]));
                        memberClinicVisitDetails.setBmi(Float.valueOf(ansSplit[2]));
                    } catch (NumberFormatException e) {
                        memberClinicVisitDetails.setWeight(Float.valueOf(ansSplit[0]));
                        memberClinicVisitDetails.setHeight(Integer.valueOf(ansSplit[1].split("\\.")[0]));
                        memberClinicVisitDetails.setBmi(Float.valueOf(ansSplit[2]));
                    }
                }
                break;
            default:
        }
    }

    @Override
    public Integer storeCvcHomeForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);

        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_MIGRATED)) {
            return member.getId();
        }
        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
            Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(member.getId());
            if (Boolean.TRUE.equals(abBoolean)) {
                throw new ImtechoMobileException("Member with Health ID " + member.getUniqueHealthId() + " is already marked DEAD. "
                        + "You cannot mark a DEAD member DEAD again.", 1);
            } else {
//                markMemberAsDeath(member, familyEntity, keyAndAnswerMap, user, MobileConstantUtil.NCD_CVC_HOME);
                return member.getId();
            }
        }

        MemberCvcHomeVisitDetails memberHomeVisitDetails = setBasicDataForCvcHomeVisitDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberHomeVisitDetails.setDoneBy(DoneBy.CC);
        }

        if (keyAndAnswerMap.get("405") != null && !keyAndAnswerMap.get("405").isEmpty()) {
            member.setMobileNumber(keyAndAnswerMap.get("405"));
            memberDao.update(member);
        }
        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberCvcHomeVisitDetail(key, answer, memberHomeVisitDetails, keyAndAnswerMap));

        cvcHomeVisitDetailDao.create(memberHomeVisitDetails);

        NcdMemberEntity ncdMember = createOrUpdateNcdMemberDetails(member, familyEntity, null, null, null, memberHomeVisitDetails);

        ncdMemberDao.createOrUpdate(ncdMember);
        updateMemberAdditionalInfoFromCvcHomeVisit(member, memberHomeVisitDetails);
        memberDao.update(member);
        cvcHomeVisitDetailDao.flush();
        ncdMemberDao.flush();
        memberDao.flush();
//        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, MobileConstantUtil.NCD_CVC_HOME_VISIT, memberHomeVisitDetails.getId()));
        return memberHomeVisitDetails.getId();
    }

    private void updateMemberAdditionalInfoFromCvcHomeVisit(MemberEntity member, MemberCvcHomeVisitDetails homeVisitDetails) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(homeVisitDetails.getClinicDate().getTime());
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberCvcHomeVisitDetails setBasicDataForCvcHomeVisitDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberCvcHomeVisitDetails memberHomeVisitDetails = new MemberCvcHomeVisitDetails();
        memberHomeVisitDetails.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        memberHomeVisitDetails.setFamilyId(familyId);
        memberHomeVisitDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberHomeVisitDetails.setLongitude(keyAndAnswerMap.get("-1"));
        memberHomeVisitDetails.setLatitude(keyAndAnswerMap.get("-2"));
        memberHomeVisitDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        memberHomeVisitDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        memberHomeVisitDetails.setDoneOn(new Date());
        return memberHomeVisitDetails;
    }

    private void setAnswersToMemberCvcHomeVisitDetail(String key, String answer, MemberCvcHomeVisitDetails
            memberHomeVisitDetails, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "9":
                memberHomeVisitDetails.setClinicDate(new Date(Long.parseLong(answer)));
                break;
            case "24":
                memberHomeVisitDetails.setPatientTakingMedicine(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "201":
                memberHomeVisitDetails.setAnyAdverseEffect(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "202":
                memberHomeVisitDetails.setAdverseEffect(answer);
                break;
            case "26":
                memberHomeVisitDetails.setRequiredReference(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "203":
                memberHomeVisitDetails.setGivenConsent(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "204":
                memberHomeVisitDetails.setReferralPlace(answer);
                memberHomeVisitDetails.setReferStatus("REFER_MO");
                break;
            case "205":
                memberHomeVisitDetails.setOtherReferralPlace(answer);
                break;
            case "206":
                memberHomeVisitDetails.setReferralId(Integer.valueOf(answer));
                break;
            case "33":
                memberHomeVisitDetails.setRemarks(answer);
                break;
            case "27":
                memberHomeVisitDetails.setReferenceReason(answer);
                break;
            case "29":
                memberHomeVisitDetails.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "31":
                memberHomeVisitDetails.setFlagReason(answer);
                break;
            case "41":
                memberHomeVisitDetails.setOtherReason(answer);
                break;
            case "42":
                memberHomeVisitDetails.setFollowupVisitType(answer);
                break;
            case "43":
                memberHomeVisitDetails.setFollowupDate(new Date(Long.parseLong(answer)));
                break;
            default:
        }
    }

    @Override
    public Integer storeCvcClinicForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        MemberEntity member = memberDao.retrieveById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveById(familyId);


        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_MIGRATED)) {
            return member.getId();
        }

        if (keyAndAnswerMap.containsKey("1012") && keyAndAnswerMap.get("1012").equalsIgnoreCase(RchConstants.MEMBER_STATUS_DEATH)) {
            Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(member.getId());
            if (Boolean.TRUE.equals(abBoolean)) {
                throw new ImtechoMobileException("Member with Health ID " + member.getUniqueHealthId() + " is already marked DEAD. "
                        + "You cannot mark a DEAD member DEAD again.", 1);
            } else {
//                markMemberAsDeath(member, familyEntity, keyAndAnswerMap, user, MobileConstantUtil.NCD_CVC_CLINIC);
                return member.getId();
            }
        }

        MemberCvcClinicVisitDetails memberClinicVisitDetails = setBasicDataForCvcClinicVisitDetails(familyEntity, familyId, keyAndAnswerMap);

        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.FHW)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.FHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CHO_HWC)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.CHO);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.MPHW)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.MPHW);
        } else if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberClinicVisitDetails.setDoneBy(DoneBy.CC);
        }

        if (keyAndAnswerMap.get("405") != null && !keyAndAnswerMap.get("405").isEmpty()) {
            member.setMobileNumber(keyAndAnswerMap.get("405"));
            memberDao.update(member);
        }
        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberCvcClinicVisitDetail(key, answer, memberClinicVisitDetails, keyAndAnswerMap));

        cvcClinicVisitDetailDao.create(memberClinicVisitDetails);

        NcdMemberEntity ncdMember = createOrUpdateNcdMemberDetails(member, familyEntity, null, null, memberClinicVisitDetails, null);

        if (keyAndAnswerMap.containsKey("301")) {
            String ans = keyAndAnswerMap.get("301");
            if (ans != null && !ans.isEmpty()) {
                List<MedicineListDataBean> medicines = new Gson().fromJson(ans, new TypeToken<List<MedicineListDataBean>>() {
                }.getType());

                if (!medicines.isEmpty()) {
                    for (MedicineListDataBean medicine : medicines) {
                        MemberDiseaseMedicine diseaseMedicine = memberDiseaseMedicineDao.retrieveDetailByMemberAndMedicine(memberClinicVisitDetails.getMemberId(), medicine.getMedicineId());
                        if (diseaseMedicine != null) {
                            if (medicine.getSpecialInstruction() != null) {
                                diseaseMedicine.setSpecialInstruction(medicine.getSpecialInstruction());
                            }
                            if (!diseaseMedicine.getDuration().equals(medicine.getDuration())) {
                                Integer newDurationValue = medicine.getDuration();

                                DrugInventoryDetail drugInventoryDetail = new DrugInventoryDetail();
                                drugInventoryDetail.setMedicineId(diseaseMedicine.getMedicineId());
                                drugInventoryDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
                                drugInventoryDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
                                drugInventoryDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                                drugInventoryDetail.setIssuedDate(memberClinicVisitDetails.getClinicDate());
                                drugInventoryDetail.setIsReturn(false);
                                DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicineByLocationId
                                        (diseaseMedicine.getMedicineId(), memberClinicVisitDetails.getLocationId());
                                if (diseaseMedicine.getDuration() < newDurationValue) {
                                    drugInventoryDetail.setIsIssued(true);
                                    drugInventoryDetail.setQuantityIssued(diseaseMedicine.getFrequency() * (newDurationValue - diseaseMedicine.getDuration()));
                                    drugInventoryDetail.setIsReceived(false);
                                    drugInventoryDetail.setQuantityReceived(0);
                                    drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                                    drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - drugInventoryDetail.getQuantityIssued());
                                } else if (diseaseMedicine.getDuration() > newDurationValue) {
                                    drugInventoryDetail.setIsIssued(false);
                                    drugInventoryDetail.setQuantityIssued(0);
                                    drugInventoryDetail.setIsReceived(true);
                                    drugInventoryDetail.setQuantityReceived(diseaseMedicine.getFrequency() * (diseaseMedicine.getDuration() - newDurationValue));
                                    drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                                    drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() + drugInventoryDetail.getQuantityIssued());
                                }
                                drugInventoryDao.create(drugInventoryDetail);

                                diseaseMedicine.setDuration(newDurationValue);
                                diseaseMedicine.setQuantity(newDurationValue * diseaseMedicine.getFrequency());
                                Calendar c = Calendar.getInstance();
                                c.setTime(diseaseMedicine.getDiagnosedOn());
                                c.add(Calendar.DATE, diseaseMedicine.getDuration());
                                diseaseMedicine.setExpiryDate(c.getTime());
                                memberDiseaseMedicineDao.saveOrUpdate(diseaseMedicine);
                            }
                        } else {
                            MemberDiseaseMedicine memberDiseaseMedicine = new MemberDiseaseMedicine();
                            memberDiseaseMedicine.setMemberId(member.getId());
                            memberDiseaseMedicine.setDiagnosedOn(memberClinicVisitDetails.getClinicDate());
                            memberDiseaseMedicine.setStartDate(memberClinicVisitDetails.getClinicDate());
                            memberDiseaseMedicine.setMedicineId(medicine.getMedicineId());
                            memberDiseaseMedicine.setFrequency(medicine.getFrequency());
                            memberDiseaseMedicine.setDuration(medicine.getDuration());
                            memberDiseaseMedicine.setQuantity(medicine.getQuantity());
                            if (medicine.getSpecialInstruction() != null) {
                                memberDiseaseMedicine.setSpecialInstruction(medicine.getSpecialInstruction());
                            }
                            Calendar c = Calendar.getInstance();
                            c.setTime(memberDiseaseMedicine.getDiagnosedOn());
                            c.add(Calendar.DATE, memberDiseaseMedicine.getDuration());
                            memberDiseaseMedicine.setExpiryDate(c.getTime());
                            memberDiseaseMedicine.setActive(true);
                            memberDiseaseMedicineDao.create(memberDiseaseMedicine);

                            //update quantity in drug inventory
                            DrugInventoryDetail drugInventoryDetail = new DrugInventoryDetail();
                            drugInventoryDetail.setMedicineId(memberDiseaseMedicine.getMedicineId());
                            drugInventoryDetail.setIssuedDate(memberClinicVisitDetails.getClinicDate());
                            drugInventoryDetail.setIsIssued(true);
                            drugInventoryDetail.setQuantityIssued(memberDiseaseMedicine.getQuantity());
                            drugInventoryDetail.setIsReceived(false);
                            drugInventoryDetail.setIsReturn(false);
                            drugInventoryDetail.setQuantityReceived(0);
                            drugInventoryDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                            drugInventoryDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
                            drugInventoryDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
                            //retrieve last record to calculate balance in hand
                            DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicineByLocationId
                                    (memberDiseaseMedicine.getMedicineId(), memberClinicVisitDetails.getLocationId());
                            drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                            drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - memberDiseaseMedicine.getQuantity());
                            drugInventoryDao.create(drugInventoryDetail);
                        }
                    }
                }
            }
        }

        if (keyAndAnswerMap.containsKey("302")) {
            String ans = keyAndAnswerMap.get("302");
            if (ans != null && !ans.isEmpty()) {
                List<MedicineListDataBean> medicines = new Gson().fromJson(ans, new TypeToken<List<MedicineListDataBean>>() {
                }.getType());

                if (!medicines.isEmpty()) {
                    for (MedicineListDataBean medicine : medicines) {
                        MemberDiseaseMedicine memberDiseaseMedicine = new MemberDiseaseMedicine();
                        memberDiseaseMedicine.setMemberId(member.getId());
                        memberDiseaseMedicine.setDiagnosedOn(memberClinicVisitDetails.getClinicDate());
                        memberDiseaseMedicine.setStartDate(memberClinicVisitDetails.getClinicDate());
                        memberDiseaseMedicine.setMedicineId(medicine.getMedicineId());
                        memberDiseaseMedicine.setFrequency(medicine.getFrequency());
                        memberDiseaseMedicine.setDuration(medicine.getDuration());
                        memberDiseaseMedicine.setQuantity(medicine.getQuantity());
                        if (medicine.getSpecialInstruction() != null) {
                            memberDiseaseMedicine.setSpecialInstruction(medicine.getSpecialInstruction());
                        }
                        Calendar c = Calendar.getInstance();
                        c.setTime(memberDiseaseMedicine.getDiagnosedOn());
                        c.add(Calendar.DATE, memberDiseaseMedicine.getDuration());
                        memberDiseaseMedicine.setExpiryDate(c.getTime());
                        memberDiseaseMedicine.setActive(true);
                        memberDiseaseMedicineDao.create(memberDiseaseMedicine);


                        //update quantity in drug inventory
                        DrugInventoryDetail drugInventoryDetail = new DrugInventoryDetail();
                        drugInventoryDetail.setMedicineId(memberDiseaseMedicine.getMedicineId());
                        drugInventoryDetail.setIssuedDate(memberClinicVisitDetails.getClinicDate());
                        drugInventoryDetail.setIsIssued(true);
                        drugInventoryDetail.setQuantityIssued(memberDiseaseMedicine.getQuantity());
                        drugInventoryDetail.setIsReceived(false);
                        drugInventoryDetail.setIsReturn(false);
                        drugInventoryDetail.setQuantityReceived(0);
                        drugInventoryDetail.setMemberId(memberClinicVisitDetails.getMemberId());
                        drugInventoryDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
                        drugInventoryDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
                        //retrieve last record to calculate balance in hand
                        DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicineByLocationId
                                (memberDiseaseMedicine.getMedicineId(), memberClinicVisitDetails.getLocationId());
                        drugInventoryDetail.setHealthInfraId(lastDrugInventoryDto.getHealthInfraId());
                        drugInventoryDetail.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - memberDiseaseMedicine.getQuantity());
                        drugInventoryDao.create(drugInventoryDetail);
                    }
                }
            }
        }

        ncdMemberDao.createOrUpdate(ncdMember);
        updateMemberAdditionalInfoFromCvcClinicVisit(member, memberClinicVisitDetails);
        memberDao.update(member);
        cvcClinicVisitDetailDao.flush();
        ncdMemberDao.flush();
        memberDao.flush();
        drugInventoryDao.flush();
        memberDiseaseMedicineDao.flush();
//        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, MobileConstantUtil.NCD_CVC_CLINIC_VISIT, memberClinicVisitDetails.getId()));
        return memberClinicVisitDetails.getId();
    }

    private void updateMemberAdditionalInfoFromCvcClinicVisit(MemberEntity member, MemberCvcClinicVisitDetails clinicVisitDetails) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        memberAdditionalInfo.setLastServiceLongDate(clinicVisitDetails.getClinicDate().getTime());
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private MemberCvcClinicVisitDetails setBasicDataForCvcClinicVisitDetails(FamilyEntity familyEntity, Integer familyId, Map<String, String> keyAndAnswerMap) {
        MemberCvcClinicVisitDetails memberClinicVisitDetails = new MemberCvcClinicVisitDetails();
        memberClinicVisitDetails.setMemberId(Integer.valueOf(keyAndAnswerMap.get("-4")));
        memberClinicVisitDetails.setFamilyId(familyId);
        memberClinicVisitDetails.setLocationId(familyEntity.getAreaId() == null ? familyEntity.getLocationId() : familyEntity.getAreaId());
        memberClinicVisitDetails.setLongitude(keyAndAnswerMap.get("-1"));
        memberClinicVisitDetails.setLatitude(keyAndAnswerMap.get("-2"));
        memberClinicVisitDetails.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        memberClinicVisitDetails.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        memberClinicVisitDetails.setDoneOn(new Date());
        return memberClinicVisitDetails;
    }

    private void setAnswersToMemberCvcClinicVisitDetail(String key, String answer, MemberCvcClinicVisitDetails
            memberClinicVisitDetails, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "9":
                memberClinicVisitDetails.setClinicDate(new Date(Long.parseLong(answer)));
                break;
            case "10":
                memberClinicVisitDetails.setClinicType(answer);
                break;
            case "24":
                memberClinicVisitDetails.setPatientTakingMedicine(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "26":
                memberClinicVisitDetails.setRequiredReference(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "27":
                memberClinicVisitDetails.setReferenceReason(answer);
                break;
            case "28":
                memberClinicVisitDetails.setReferralPlace(answer);
                memberClinicVisitDetails.setReferStatus("REFER_MO");
                break;
            case "29":
                memberClinicVisitDetails.setFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "31":
                memberClinicVisitDetails.setFlagReason(answer);
                break;
            case "41":
                memberClinicVisitDetails.setOtherReason(answer);
                break;
            case "42":
                memberClinicVisitDetails.setFollowupVisitType(answer);
                break;
            case "43":
                memberClinicVisitDetails.setFollowupDate(new Date(Long.parseLong(answer)));
                break;
            case "33":
                memberClinicVisitDetails.setRemarks(answer);
                break;
            default:
        }
    }

    private NcdMemberEntity createOrUpdateNcdMemberDetails(MemberEntity member, FamilyEntity family, MemberClinicVisitDetails clinicVisitDetails,
                                                           MemberHomeVisitDetails homeVisitDetails, MemberCvcClinicVisitDetails cvcClinicVisitDetails,
                                                           MemberCvcHomeVisitDetails cvcHomeVisitDetails) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(member.getId());
        if (ncdMember == null) {
            ncdMember = new NcdMemberEntity();
            ncdMember.setMemberId(member.getId());
            if (family.getAreaId() != null) {
                ncdMember.setLocationId(family.getAreaId());
            } else {
                ncdMember.setLocationId(family.getLocationId());
            }
        }
        if (cvcClinicVisitDetails != null) {
            ncdMember.setLastServiceDate(cvcClinicVisitDetails.getClinicDate());
            ncdMember.setLastMobileVisit(cvcClinicVisitDetails.getClinicDate());
            ncdMember.setLastRemark(cvcClinicVisitDetails.getRemarks());
            return ncdMember;
        }
        if (cvcHomeVisitDetails != null) {
            ncdMember.setLastServiceDate(cvcHomeVisitDetails.getClinicDate());
            ncdMember.setLastMobileVisit(cvcHomeVisitDetails.getClinicDate());
            ncdMember.setLastRemark(cvcHomeVisitDetails.getRemarks());
            return ncdMember;
        }
        if (homeVisitDetails != null) {
            ncdMember.setLastServiceDate(homeVisitDetails.getClinicDate());
            ncdMember.setLastMobileVisit(homeVisitDetails.getClinicDate());
            ncdMember.setLastRemark(homeVisitDetails.getRemarks());
            if (homeVisitDetails.getMentalHealthResult() != null) {
                ncdMember.setMentalHealthStatus(homeVisitDetails.getMentalHealthResult());
            }
            if (homeVisitDetails.getDiabetesResult() != null) {
                ncdMember.setDiabetesStatus(homeVisitDetails.getDiabetesResult());
            }
            if (homeVisitDetails.getHypertensionResult() != null) {
                ncdMember.setHypertensionStatus(homeVisitDetails.getHypertensionResult());
            }
        }
        if (clinicVisitDetails != null) {
            ncdMember.setLastServiceDate(clinicVisitDetails.getClinicDate());
            ncdMember.setLastMobileVisit(clinicVisitDetails.getClinicDate());
            ncdMember.setLastRemark(clinicVisitDetails.getRemarks());
            if (clinicVisitDetails.getMentalHealthResult() != null) {
                ncdMember.setMentalHealthStatus(clinicVisitDetails.getMentalHealthResult());
            }
            if (clinicVisitDetails.getDiabetesResult() != null) {
                ncdMember.setDiabetesStatus(clinicVisitDetails.getDiabetesResult());
            }
            if (clinicVisitDetails.getHypertensionResult() != null) {
                ncdMember.setHypertensionStatus(clinicVisitDetails.getHypertensionResult());
            }
        }
        return ncdMember;
    }

    private static MemberHypertensionDetail getMemberHypertensionDetail(UserMaster user, MemberClinicVisitDetails memberClinicVisitDetails) {
        MemberHypertensionDetail memberHypertensionDetail = new MemberHypertensionDetail();
        memberHypertensionDetail.setScreeningDate(memberClinicVisitDetails.getClinicDate());
        memberHypertensionDetail.setDiastolicBp(memberClinicVisitDetails.getDiastolicBp());
        memberHypertensionDetail.setSystolicBp(memberClinicVisitDetails.getSystolicBp());
        memberHypertensionDetail.setPulseRate(memberClinicVisitDetails.getPulseRate());
        memberHypertensionDetail.setStatus(memberClinicVisitDetails.getHypertensionResult());
        memberHypertensionDetail.setMemberId(memberClinicVisitDetails.getMemberId());
        memberHypertensionDetail.setFamilyId(memberClinicVisitDetails.getFamilyId());
        memberHypertensionDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
        memberHypertensionDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
        memberHypertensionDetail.setLocationId(memberClinicVisitDetails.getLocationId());
        memberHypertensionDetail.setLatitude(memberClinicVisitDetails.getLatitude());
        memberHypertensionDetail.setLongitude(memberClinicVisitDetails.getLongitude());
        memberHypertensionDetail.setVisitType(MobileConstantUtil.CLINIC_VISIT);
        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberHypertensionDetail.setDoneBy(DoneBy.CC);
        }
        return memberHypertensionDetail;
    }

    private static MemberMentalHealthDetails getMemberMentalHealthDetails(UserMaster user, MemberClinicVisitDetails memberClinicVisitDetails) {
        MemberMentalHealthDetails memberMentalHealthDetails = new MemberMentalHealthDetails();
        memberMentalHealthDetails.setScreeningDate(memberClinicVisitDetails.getClinicDate());
        memberMentalHealthDetails.setTalk(memberClinicVisitDetails.getTalk());
        memberMentalHealthDetails.setOwnDailyWork(memberClinicVisitDetails.getOwnDailyWork());
        memberMentalHealthDetails.setUnderstanding(memberClinicVisitDetails.getUnderstanding());
        memberMentalHealthDetails.setSocialWork(memberClinicVisitDetails.getSocialWork());
        memberMentalHealthDetails.setStatus(memberClinicVisitDetails.getMentalHealthResult());
        memberMentalHealthDetails.setMemberId(memberClinicVisitDetails.getMemberId());
        memberMentalHealthDetails.setFamilyId(memberClinicVisitDetails.getFamilyId());
        memberMentalHealthDetails.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
        memberMentalHealthDetails.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
        memberMentalHealthDetails.setLocationId(memberClinicVisitDetails.getLocationId());
        memberMentalHealthDetails.setLatitude(memberClinicVisitDetails.getLatitude());
        memberMentalHealthDetails.setLongitude(memberClinicVisitDetails.getLongitude());
        memberMentalHealthDetails.setVisitType(MobileConstantUtil.CLINIC_VISIT);
        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberMentalHealthDetails.setDoneBy(DoneBy.CC);
        }
        return memberMentalHealthDetails;
    }

    private static MemberDiabetesDetail getMemberDiabetesDetail(UserMaster user, MemberClinicVisitDetails memberClinicVisitDetails) {
        MemberDiabetesDetail memberDiabetesDetail = new MemberDiabetesDetail();
        memberDiabetesDetail.setScreeningDate(memberClinicVisitDetails.getClinicDate());
        memberDiabetesDetail.setMeasurementType("RBS");
        memberDiabetesDetail.setBloodSugar(memberClinicVisitDetails.getBloodSugar());
        memberDiabetesDetail.setStatus(memberClinicVisitDetails.getDiabetesResult());
        memberDiabetesDetail.setMemberId(memberClinicVisitDetails.getMemberId());
        memberDiabetesDetail.setFamilyId(memberClinicVisitDetails.getFamilyId());
        memberDiabetesDetail.setMobileStartDate(memberClinicVisitDetails.getMobileStartDate());
        memberDiabetesDetail.setMobileEndDate(memberClinicVisitDetails.getMobileEndDate());
        memberDiabetesDetail.setLocationId(memberClinicVisitDetails.getLocationId());
        memberDiabetesDetail.setLatitude(memberClinicVisitDetails.getLatitude());
        memberDiabetesDetail.setLongitude(memberClinicVisitDetails.getLongitude());
        memberDiabetesDetail.setVisitType(MobileConstantUtil.CLINIC_VISIT);
        if (user.getRole() != null && user.getRole().getName() != null && user.getRole().getName().equalsIgnoreCase(MobileConstantUtil.Roles.CC)) {
            memberDiabetesDetail.setDoneBy(DoneBy.CC);
        }
        if (memberClinicVisitDetails.getHeight() != null) {
            memberDiabetesDetail.setHeight(memberClinicVisitDetails.getHeight());
            memberDiabetesDetail.setWeight(memberClinicVisitDetails.getWeight());
            memberDiabetesDetail.setBmi(memberClinicVisitDetails.getBmi());
        }
        return memberDiabetesDetail;
    }

    private void markMemberAsDeath(MemberEntity memberEntity, FamilyEntity familyEntity, Map<String, String> memberKeyAndAnswerMap, UserMaster user, String formType) {
        String dateOfDeath;
        String placeOfDeath;
        String deathReason;
        String healthInfraId;

        if (memberKeyAndAnswerMap.containsKey("2602")) {
            dateOfDeath = memberKeyAndAnswerMap.get("2602");
            placeOfDeath = memberKeyAndAnswerMap.get("2603");
            deathReason = memberKeyAndAnswerMap.get("1013");
            healthInfraId = memberKeyAndAnswerMap.get("2607");
        } else {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        QueryDto queryDto = new QueryDto();
        queryDto.setCode("mark_member_as_death");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("member_id", memberEntity.getId());
        if (familyEntity.getAreaId() != null) {
            parameters.put("location_id", familyEntity.getAreaId());
//            parameters.put("location_hierarchy_id", locationLevelHierarchyDao.retrieveByLocationId(familyEntity.getAreaId()).getId());
        } else {
            parameters.put("location_id", familyEntity.getLocationId());
//            parameters.put("location_hierarchy_id", locationLevelHierarchyDao.retrieveByLocationId(familyEntity.getLocationId()).getId());
        }
        parameters.put("action_by", user.getId());
        parameters.put("family_id", familyEntity.getId());
        parameters.put("death_date", sdf.format(new Date(Long.parseLong(dateOfDeath))));
        parameters.put("place_of_death", placeOfDeath);
        parameters.put("death_reason", deathReason);
        parameters.put("other_death_reason", null);
        parameters.put("service_type", formType);
        parameters.put("reference_id", memberEntity.getId());
        parameters.put("health_infra_id", healthInfraId);
        parameters.put("member_death_mark_state", FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_DEAD);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        queryMasterService.executeQuery(queryDtos, true);
    }

    @Override
    public MemberMoConfirmedDetailDataBean retrieveMoConfirmedDetail(MemberMoConfirmedDetailDataBean moConfirmedDetailDataBean) {
        if (moConfirmedDetailDataBean != null) {
            Integer memberId = moConfirmedDetailDataBean.getMemberId();
            if (moConfirmedDetailDataBean.getConfirmedForDiabetes() != null && moConfirmedDetailDataBean.getConfirmedForDiabetes()) {
                List<NcdDiabetesDetailDataBean> ncdDiabetesDetailDataBeans = new ArrayList<>();
                List<MemberDiabetesDetail> diabetesScreenedDetails = diabetesDetailDao.retrieveLastNRecordsByMemberIdAndMeasurement(memberId, "RBS", MobileConstantUtil.SCREENED, 1);
                if (diabetesScreenedDetails != null && !diabetesScreenedDetails.isEmpty()) {
                    NcdDiabetesDetailDataBean ncdDiabetesDetail = new NcdDiabetesDetailDataBean();
                    ncdDiabetesDetail.setScreeningDate(diabetesScreenedDetails.get(0).getScreeningDate().getTime());
                    ncdDiabetesDetail.setBloodSugar(diabetesScreenedDetails.get(0).getBloodSugar());
                    ncdDiabetesDetailDataBeans.add(ncdDiabetesDetail);
                }
                List<MemberDiabetesDetail> diabetesDetails = diabetesDetailDao.retrieveLastNRecordsByMemberIdAndMeasurement(memberId, null, null, 1);
                if (diabetesDetails != null && !diabetesDetails.isEmpty()) {
                    moConfirmedDetailDataBean.setDiabetesTreatmentStatus(diabetesDetails.get(0).getTreatmentStatus());
                }
                List<MemberDiabetesDetail> memberDiabetesDetails = diabetesDetailDao.retrieveLastNRecordsByMemberIdAndMeasurement(memberId, "RBS", null, 2);
                if (memberDiabetesDetails != null && !memberDiabetesDetails.isEmpty()) {
                    for (MemberDiabetesDetail bean : memberDiabetesDetails) {
                        NcdDiabetesDetailDataBean ncdDiabetesDetail = new NcdDiabetesDetailDataBean();
                        ncdDiabetesDetail.setScreeningDate(bean.getScreeningDate().getTime());
                        ncdDiabetesDetail.setBloodSugar(bean.getBloodSugar());
                        ncdDiabetesDetailDataBeans.add(ncdDiabetesDetail);
                    }
                    moConfirmedDetailDataBean.setDiabetesDetails(ncdDiabetesDetailDataBeans);
                }
            }
            if (moConfirmedDetailDataBean.getConfirmedForHypertension() != null && moConfirmedDetailDataBean.getConfirmedForHypertension()) {
                List<NcdHypertensionDetailDataBean> ncdHypertensionDetailDataBeans = new ArrayList<>();
                List<MemberHypertensionDetail> memberScreenedDetails = hypertensionDetailDao.retrieveLastNRecordsByMemberId(memberId, MobileConstantUtil.SCREENED, 1);
                if (memberScreenedDetails != null && !memberScreenedDetails.isEmpty()) {
                    NcdHypertensionDetailDataBean ncdHypertensionDetail = new NcdHypertensionDetailDataBean();
                    ncdHypertensionDetail.setScreeningDate(memberScreenedDetails.get(0).getScreeningDate().getTime());
                    ncdHypertensionDetail.setSystolicBp(memberScreenedDetails.get(0).getSystolicBp());
                    ncdHypertensionDetail.setDiastolicBp(memberScreenedDetails.get(0).getDiastolicBp());
                    ncdHypertensionDetail.setPulseRate(memberScreenedDetails.get(0).getPulseRate());
                    ncdHypertensionDetailDataBeans.add(ncdHypertensionDetail);
                }
                List<MemberHypertensionDetail> memberHypertensionDetails = hypertensionDetailDao.retrieveLastNRecordsByMemberId(memberId, null, 2);
                if (memberHypertensionDetails != null && !memberHypertensionDetails.isEmpty()) {
                    moConfirmedDetailDataBean.setHypertensionTreatmentStatus(memberHypertensionDetails.get(0).getTreatmentStatus());
                }
                if (memberHypertensionDetails != null && !memberHypertensionDetails.isEmpty()) {
                    for (MemberHypertensionDetail bean : memberHypertensionDetails) {
                        NcdHypertensionDetailDataBean ncdHypertensionDetail = new NcdHypertensionDetailDataBean();
                        ncdHypertensionDetail.setScreeningDate(bean.getScreeningDate().getTime());
                        ncdHypertensionDetail.setSystolicBp(bean.getSystolicBp());
                        ncdHypertensionDetail.setDiastolicBp(bean.getDiastolicBp());
                        ncdHypertensionDetail.setPulseRate(bean.getPulseRate());
                        ncdHypertensionDetailDataBeans.add(ncdHypertensionDetail);
                    }
                    moConfirmedDetailDataBean.setHypertensionDetails(ncdHypertensionDetailDataBeans);
                }
            }
            if (moConfirmedDetailDataBean.getConfirmedForMentalHealth() != null && moConfirmedDetailDataBean.getConfirmedForMentalHealth()) {
                List<NcdMentalHealthDetailDataBean> ncdMentalHealthDetailDataBeans = new ArrayList<>();
                List<MemberMentalHealthDetails> mentalHealthScreened = mentalHealthDetailDao.retrieveLastNRecordsByMemberId(memberId, MobileConstantUtil.SCREENED, 1);
                if (mentalHealthScreened != null && !mentalHealthScreened.isEmpty()) {
                    NcdMentalHealthDetailDataBean ncdMentalHealthDetail = new NcdMentalHealthDetailDataBean();
                    ncdMentalHealthDetail.setScreeningDate(mentalHealthScreened.get(0).getScreeningDate().getTime());
                    ncdMentalHealthDetail.setTalk(mentalHealthScreened.get(0).getTalk());
                    ncdMentalHealthDetail.setUnderstanding(mentalHealthScreened.get(0).getUnderstanding());
                    ncdMentalHealthDetail.setSocialWork(mentalHealthScreened.get(0).getSocialWork());
                    ncdMentalHealthDetail.setOwnDailyWork(mentalHealthScreened.get(0).getOwnDailyWork());
                    ncdMentalHealthDetailDataBeans.add(ncdMentalHealthDetail);
                }
                List<MemberMentalHealthDetails> memberMentalHealthDetails = mentalHealthDetailDao.retrieveLastNRecordsByMemberId(memberId, null, 2);
                if (memberMentalHealthDetails != null && !memberMentalHealthDetails.isEmpty()) {
                    moConfirmedDetailDataBean.setMentalHealthTreatmentStatus(memberMentalHealthDetails.get(0).getTreatmentStatus());
                }
                if (memberMentalHealthDetails != null && !memberMentalHealthDetails.isEmpty()) {
                    for (MemberMentalHealthDetails bean : memberMentalHealthDetails) {
                        NcdMentalHealthDetailDataBean ncdMentalHealthDetail = new NcdMentalHealthDetailDataBean();
                        ncdMentalHealthDetail.setScreeningDate(bean.getScreeningDate().getTime());
                        ncdMentalHealthDetail.setTalk(bean.getTalk());
                        ncdMentalHealthDetail.setUnderstanding(bean.getUnderstanding());
                        ncdMentalHealthDetail.setSocialWork(bean.getSocialWork());
                        ncdMentalHealthDetail.setOwnDailyWork(bean.getOwnDailyWork());
                        ncdMentalHealthDetailDataBeans.add(ncdMentalHealthDetail);
                    }
                    moConfirmedDetailDataBean.setMentalHealthDetails(ncdMentalHealthDetailDataBeans);
                }
            }
            List<GeneralDetailMedicineDto> memberDiseaseMedicines = memberDiseaseMedicineDao.retrievePrescribedMedicineForMobile(memberId);
            if (memberDiseaseMedicines != null && !memberDiseaseMedicines.isEmpty()) {
                List<NcdMemberMedicineDataBean> ncdMemberMedicineDataBeans = new ArrayList<>();
                for (GeneralDetailMedicineDto bean : memberDiseaseMedicines) {
                    NcdMemberMedicineDataBean ncdMemberMedicineDetail = new NcdMemberMedicineDataBean();
                    ncdMemberMedicineDetail.setMedicineId(bean.getMedicineId());
                    ncdMemberMedicineDetail.setMedicineName(cFHCDao.getValueFromListValue(bean.getMedicineId()));
                    ncdMemberMedicineDetail.setFrequency(bean.getFrequency());
                    ncdMemberMedicineDetail.setDuration(bean.getDuration());
                    ncdMemberMedicineDetail.setQuantity(bean.getQuantity());
                    ncdMemberMedicineDetail.setSpecialInstruction(bean.getSpecialInstruction());
                    ncdMemberMedicineDetail.setExpiryDate(bean.getExpiryDate());
                    ncdMemberMedicineDataBeans.add(ncdMemberMedicineDetail);
                }
                moConfirmedDetailDataBean.setMedicineDetails(ncdMemberMedicineDataBeans);
            }
            MemberGeneralDetail generalDetail = memberGeneralDetailDao.retrieveLastRecordByMemberId(memberId);
            if (generalDetail != null && generalDetail.getComment() != null) {
                moConfirmedDetailDataBean.setMoComment(generalDetail.getComment());
            }
            return moConfirmedDetailDataBean;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDetailDto retrieveMemberDetail(Integer memberId) {
        MemberDetailDto memberDetailDto = new MemberDetailDto();
        MemberReferral hypertensionDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.HT);
        MemberReferral diabetesDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.D);
        MemberReferral oralDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.O);
        MemberReferral breastDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.B);
        MemberReferral cervicalDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.C);
        MemberReferral mentalHealthDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.MH);
        MemberReferral generalDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.G);
        MemberReferral initialAssessmemtDetail = memberReferralDao.retrievePendingFollowUpByMemberIdAndDiseaseType(memberId, DiseaseCode.IA);
        MemberEntity basicDetail = memberDao.retrieveMemberById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(basicDetail.getFamilyId());
        memberDetailDto.setFamilyId(familyEntity.getId());
        memberDetailDto.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberDetailDto.setLocationHierarchy(locationHierchyCloserDetailDao.getLocationHierarchyStringByLocationId(memberDetailDto.getLocationId()));
        memberDetailDto.setBasicDetails(MemberMapper.getMemberDto(basicDetail));
        memberDetailDto.setMemberHypertensionDto(hypertensionDetail);
        memberDetailDto.setMemberDiabetesDto(diabetesDetail);
        memberDetailDto.setMemberOralDto(oralDetail);
        memberDetailDto.setMemberBreastDto(breastDetail);
        memberDetailDto.setMemberCervicalDto(cervicalDetail);
        memberDetailDto.setMemberGeneralDto(generalDetail);
        memberDetailDto.setMemberInitialAssessmentDto(initialAssessmemtDetail);
        memberDetailDto.setMemberMentalHealth(mentalHealthDetail);
        memberDetailDto.setName(Stream.of(basicDetail.getFirstName(), " ", basicDetail.getMiddleName(), " ", basicDetail.getLastName()).filter(name -> name != null).collect(Collectors.joining()));
        return memberDetailDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMemberDetails(MemberDetailDto member) {
        // Not implemented for now
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeDellApiResponseDetails(Date requestStartDate, Date requestEndDate, String locationName, Integer
            locationId, String response, Integer enrolled) {
        otherInfoDao.storeDellApiResponseDetails(requestStartDate, requestEndDate, locationName, locationId, response, enrolled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDetailDto> retrieveMembers(Integer limit, Integer offset, String
            healthInfrastructureType, String searchBy, String searchString) {
        List<MemberDetailDto> memberDetailDtos = memberDao.retrieveNcdMembers(user.getId(), limit, offset, healthInfrastructureType, searchBy, searchString);
        String status = "";
        for (MemberDetailDto detailDto : memberDetailDtos) {
            List<String> st = memberDao.retrieveMembersOnStatus(detailDto.getId());
            if (!st.isEmpty() && st.get(0) != null) {
                for (String s : st) {
                    if (s != null) {
                        status = status + s.concat(",");
                    }
                }
                detailDto.setStatus(status);
            }
            detailDto.setSubCenter(memberDao.retrieveMembersSC(detailDto.getLocationId()).get(0));
        }
        return memberDetailDtos;
    }

    @Override
    public List<MemberDetailDto> retrieveMembersForCFS(Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString) {
        List<MemberDetailDto> memberDetailDtos = new ArrayList<>();
        List<MemberDetailDto> memberDetailDtoList = memberDao.retrieveNcdMembers(user.getId(), limit, offset, healthInfrastructureType, searchBy, searchString);
        for (MemberDetailDto detailDto : memberDetailDtoList) {
            if (!memberDao.retrieveGeneralMembers(detailDto.getId()).isEmpty()) {
                memberDetailDtos.add(detailDto);
            }
        }
        return memberDetailDtos;
    }

    @Override
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineForUser(Integer memberId) {
        return memberDiseaseMedicineDao.retrievePrescribedMedicineForUser(memberId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveComplaints(MemberComplaintsDto complaintsDto) {
        memberComplaintsDao.save(MemberDetailMapper.dtoToEntityForComplaints(complaintsDto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberComplaintsDto> retrieveComplaintsByMemberId(Integer memberId) {
        return memberComplaintsDao.retrieveByMemberId(memberId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MedicineMaster> retrieveAllMedicines() {
        return medicineMasterDao.retrieveAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDiagnosis(MemberDiagnosisDto diagnosisDto) {
        diagnosisDto.setDiagnosedOn(new Date());
        MemberDiseaseDiagnosis memberDiseaseDiagnosis = MemberDetailMapper.convertDtoToEntityForDiagnosis(diagnosisDto);
        MemberEntity memberEntity = memberDao.retrieveById(diagnosisDto.getMemberId());
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
        if (Objects.nonNull(familyEntity)) {
            if (familyEntity.getAreaId() != null) {
                memberDiseaseDiagnosis.setLocationId(familyEntity.getAreaId());
            } else {
                memberDiseaseDiagnosis.setLocationId(familyEntity.getLocationId());
            }
        }
        memberDiseaseDiagnosisDao.save(memberDiseaseDiagnosis);

        List<MemberDiseaseMedicine> memberDiseaseMedicines = MemberDetailMapper.convertDtoToEntityForMedicines(diagnosisDto);
        memberDiseaseMedicineDao.saveOrUpdateAll(memberDiseaseMedicines);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(Integer memberId, String diseaseCode) {
        return memberDiseaseMedicineDao.retrieveTreatmentHistory(memberId, diseaseCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveReferral(MemberReferralDto memberReferralDto) {
        memberReferralDto.setState(State.ACTIVE);
        if (memberReferralDto.getPreviousRefferalId() != null) {
            MemberReferral retrieveById = memberReferralDao.retrieveById(memberReferralDto.getPreviousRefferalId());
            if (retrieveById != null) {
                retrieveById.setState(State.INACTIVE);
                memberReferralDao.update(retrieveById);
            }
        }
        memberReferralDao.save(MemberDetailMapper.convertDtoToEntityForReferral(memberReferralDto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFollowUp(MemberDiseaseFollowupDto memberDiseaseFollowupDto) {
        memberDiseaseFollowupDao.save(MemberDetailMapper.dtoToEntityForFollowup(memberDiseaseFollowupDto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDetailDto> retrieveMembersForFollowup(Integer limit, Integer offset, String
            healthInfrastructureType, String[] status) {
        return memberDao.retrieveNcdMembersForFollowup(user.getId(), limit, offset, healthInfrastructureType, status);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberReferralDto> retrieveReffForToday(Integer memberId) {
        return memberReferralDao.retrieveReffForToday(memberId, user.getId());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDiseaseFollowupDto> retrieveNextFollowUp(Integer memberId) {
        return memberDiseaseFollowupDao.retrieveNextFollowUp(memberId, user.getId());

    }

    @Override
    public void createMedicineDetails(List<GeneralDetailMedicineDto> medicineDetail, Integer memberId, Date screeningDate, Integer generalId, MemberDiseaseDiagnosis.DiseaseCode g, Integer healthInfraId) {
        List<MemberDiseaseMedicine> memberMedicines = new ArrayList<>();
        if (medicineDetail != null) {
            if (!medicineDetail.isEmpty() && medicineDetail.size() > 0) {
                for (GeneralDetailMedicineDto medicine : medicineDetail) {
                    //check if medicine already exists
//                    MemberDiseaseMedicine previousMedicine = memberDiseaseMedicineDao.retrieveLastActiveRecordByMemberId(memberId, medicine.getMedicineId());
//                    if (previousMedicine != null) {
//                        //if medicine exists update duration,quantity & expiry date
//                        previousMedicine.setDuration(medicine.getDuration() + previousMedicine.getDuration());
//                        previousMedicine.setQuantity(medicine.getQuantity() + previousMedicine.getQuantity());
//                        Calendar c = Calendar.getInstance();
//                        c.setTime(previousMedicine.getExpiryDate());
//                        c.add(Calendar.DATE, medicine.getDuration());
//                        previousMedicine.setExpiryDate(c.getTime());
//                        memberDiseaseMedicineDao.update(previousMedicine);
//                    } else {
                        //Add in diagnosed medicine table
                        MemberDiseaseMedicine mdm = new MemberDiseaseMedicine();
                        mdm.setMedicineId(medicine.getMedicineId());
                        mdm.setMemberId(memberId);
                        mdm.setStartDate(medicine.getStartDate());
                        mdm.setDiseaseCode(g);
                        mdm.setDiagnosedOn(screeningDate);
                        mdm.setReferenceId(generalId);
                        mdm.setFrequency(medicine.getFrequency());
                        mdm.setDuration(medicine.getDuration());
                        mdm.setQuantity(medicine.getQuantity());
                        mdm.setSpecialInstruction(medicine.getSpecialInstruction());
                        mdm.setExpiryDate(medicine.getExpiryDate());
                        mdm.setActive(true);
                        memberMedicines.add(mdm);
//                    }
                    //update quantity in drug inventory
                    DrugInventoryDto drugInventoryDto = new DrugInventoryDto();
                    drugInventoryDto.setMedicineId(medicine.getMedicineId());
                    drugInventoryDto.setIssuedDate(screeningDate);
                    drugInventoryDto.setIsIssued(true);
                    drugInventoryDto.setHealthInfraId(healthInfraId);
                    drugInventoryDto.setQuantityIssued(medicine.getQuantity());
                    drugInventoryDto.setIsReceived(false);
                    drugInventoryDto.setIsReturn(false);
                    drugInventoryDto.setQuantityReceived(0);
                    //retrieve last record to calculate balance in hand
                    DrugInventoryDto lastDrugInventoryDto = drugInventoryDao.retrieveMedicine
                            (medicine.getMedicineId(), healthInfraId);
                    drugInventoryDto.setBalanceInHand(lastDrugInventoryDto.getBalanceInHand() - medicine.getQuantity());
                    DrugInventoryDetail drugInventoryDetail = MemberDetailMapper.dtoToEntityForDrugInventoryDetail(drugInventoryDto);
                    drugInventoryDao.create(drugInventoryDetail);
                }
                memberDiseaseMedicineDao.saveOrUpdateAll(memberMedicines);
            }
        }
    }

    @Override
    public Integer createMasterRecord(Integer memberId, Integer locationId, DiseaseCode diseaseCode, Boolean flag, Date visitDate, Boolean suspected) {
        //check if already member have master record with same disease
        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberId, diseaseCode.toString());
        if (ncdMaster != null) {
            if (diseaseCode.equals(DiseaseCode.D) && suspected) {
                ncdMaster.setStatus(Status.CONFIRMATION_PENDING.toString());
            } else if (!diseaseCode.equals(DiseaseCode.D) && suspected) {
                ncdMaster.setStatus(Status.SUSPECTED.toString());
            } else {
                ncdMaster.setStatus(Status.NORMAL.toString());
            }
            ncdMaster.setLastVisitDate(visitDate);
            if (flag != null) {
                ncdMaster.setFlag(flag);
            }
            updateStateInMemberNcdDetail(ncdMaster.getMemberId(), ncdMaster.getDiseaseCode().toString(), ncdMaster.getStatus().toString());
            ncdMasterDao.update(ncdMaster);
            return ncdMaster.getId();
        } else {
            //Member does not have any record, create master record with screened status
            NcdMaster newNcdMaster = new NcdMaster();
            newNcdMaster.setMemberId(memberId);
            newNcdMaster.setDiseaseCode(diseaseCode.toString());
            if (!(diseaseCode.equals(DiseaseCode.G) || diseaseCode.equals(DiseaseCode.IA))) {
                //newNcdMaster.setStatus(Status.SCREENED.toString());
                if (diseaseCode.equals(DiseaseCode.D) && suspected) {
                    newNcdMaster.setStatus(Status.CONFIRMATION_PENDING.toString());
                } else if (!diseaseCode.equals(DiseaseCode.D) && suspected) {
                    newNcdMaster.setStatus(Status.SUSPECTED.toString());
                } else {
                    newNcdMaster.setStatus(Status.NORMAL.toString());
                }
                newNcdMaster.setLocationId(locationId);
            } else {
                newNcdMaster.setHealthInfraId(locationId);
            }
            newNcdMaster.setActive(true);
            if (flag != null) {
                newNcdMaster.setFlag(flag);
            }
            newNcdMaster.setLastVisitDate(visitDate);
            if(newNcdMaster.getStatus()!=null){
                updateStateInMemberNcdDetail(newNcdMaster.getMemberId(), newNcdMaster.getDiseaseCode().toString(), newNcdMaster.getStatus().toString());
            }
            return ncdMasterDao.create(newNcdMaster);
        }
    }

    @Override
    public Integer updateNcdMasterStatus(Integer memberId, DiseaseCode diseaseCode, Status status, Integer healthInfraId, Date visitDate) {
        //check if already member have master record with same disease
        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberId, diseaseCode.toString());
        if (ncdMaster != null) {
            //Member already have a master record, update status
            if (status != null) {
                ncdMaster.setStatus(status.toString());
                updateStateInMemberNcdDetail(ncdMaster.getMemberId(), ncdMaster.getDiseaseCode().toString(), ncdMaster.getStatus().toString());
            }
            ncdMaster.setHealthInfraId(healthInfraId);
            ncdMaster.setLastVisitDate(visitDate);
            ncdMasterDao.update(ncdMaster);
            return ncdMaster.getId();
        } else {
            //Member does not have any record, create master record with status
            NcdMaster newNcdMaster = new NcdMaster();
            newNcdMaster.setMemberId(memberId);
            newNcdMaster.setHealthInfraId(healthInfraId);
            newNcdMaster.setDiseaseCode(diseaseCode.toString());
            if (status != null) {
                newNcdMaster.setStatus(status.toString());
                updateStateInMemberNcdDetail(newNcdMaster.getMemberId(), newNcdMaster.getDiseaseCode().toString(), newNcdMaster.getStatus().toString());
            }
            newNcdMaster.setLastVisitDate(visitDate);
            newNcdMaster.setActive(true);
            return ncdMasterDao.create(newNcdMaster);
        }
    }

    public void updateStateInMemberNcdDetail(Integer memberId, String diseaseCode, String status) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(memberId);
        if(ncdMember == null) {
            ncdMember = new NcdMemberEntity();
            ncdMember.setMemberId(memberId);
        }
        switch (diseaseCode){
            case "HT":
                ncdMember.setHypertensionState(status);
                break;
            case "D":
                ncdMember.setDiabetesState(status);
                break;
            case "MH":
                ncdMember.setMentalHealthState(status);
                break;
            default:
                break;
        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public Integer updateNcdMasterSubStatus(Integer memberId, String diseaseCode, SubStatus subStatus, Boolean resetFlag, Date visitDate) {
        //check if already member have master record with same disease
        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberId, diseaseCode);
        if (ncdMaster != null) {
            //If master record exists, and reset flag is true - set it to false
            if (resetFlag != null && resetFlag) {
                ncdMaster.setFlag(false);
            }
            //If master record exists, update substatus
            if (subStatus == null) {
                ncdMaster.setSubStatus(null);
            } else {
                ncdMaster.setSubStatus(subStatus.toString());
            }
            if (visitDate != null) {
                ncdMaster.setLastVisitDate(visitDate);
            }
            ncdMasterDao.update(ncdMaster);
            return ncdMaster.getId();
        }
        return 0;
    }

    @Override
    public List<MemberDetailDto> retrieveAllMembers(String type, Integer limit, Integer offset) {
        return ncdMasterDao.retrieveAllMembers(imtechoSecurityUser.getId(), type, limit, offset);
    }

    @Override
    public List<MemberDetailDto> retreiveSearchedMembers(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag, Boolean review) {
        return ncdMasterDao.retreiveSearchedMembers(limit, offset, searchBy, searchString, flag, imtechoSecurityUser.getId(), review);
    }

    @Override
    public MemberInvestigationDetail saveInvestigation(MemberInvestigationDto memberInvestigationDto) {
        Integer id = -1;
        for (MemberInvestigationDetail item : memberInvestigationDto.getReports()
        ) {
            MemberInvestigationDetail memberInvestigationDetail = MemberDetailMapper.dtoToEntityForInvestigation(memberInvestigationDto);
            memberInvestigationDetail.setReport(item.getReport());
            memberInvestigationDetail.setType(item.getType());
            id = memberInvestigationDao.create(memberInvestigationDetail);
        }
        return memberInvestigationDao.retrieveById(id);
    }

    @Override
    public void createVisitHistory(Integer masterId, Integer memberId, Date visitDate, DoneBy doneBy, Integer referenceId, String status, DiseaseCode diseaseCode, Boolean flag, String reading) {
        NcdVisitHistory ncdVisitHistory = new NcdVisitHistory();
        ncdVisitHistory.setMasterId(masterId);
        ncdVisitHistory.setMemberId(memberId);
        ncdVisitHistory.setVisitBy(doneBy);
        ncdVisitHistory.setVisitDate(visitDate);
        ncdVisitHistory.setDiseaseCode(diseaseCode);
        ncdVisitHistory.setStatus(status);
        ncdVisitHistory.setReferenceId(referenceId);
        ncdVisitHistory.setFlag(flag);
        ncdVisitHistory.setReading(reading);
        ncdVisitHistoryDao.create(ncdVisitHistory);
    }

    @Override
    public List<MemberInvestigationDetail> retreiveInvestigationDetailByMemberId(Integer memberId) {
        return memberInvestigationDao.findByMemberId(memberId);
    }

    @Override
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineHistoryForUser(Integer memberId) {
        return memberDiseaseMedicineDao.retrievePrescribedMedicineHistoryForUser(memberId);
    }

    @Override
    public MemberMasterDto retrieveMemberDetailMaster(Integer memberId) {
        MemberMasterDto memberMasterDto = new MemberMasterDto();
        MemberEntity basicDetail = memberDao.retrieveMemberById(memberId);
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(basicDetail.getFamilyId());
        NcdMemberEntity ncdMemberEntity = ncdMemberDao.retrieveNcdMemberByMemberId(memberId);
        if(ncdMemberEntity!=null){
            memberMasterDto.setNcdMemberEntity(ncdMemberEntity);
        }
        memberMasterDto.setBasicDetails(MemberMapper.getMemberDto(basicDetail));
        memberMasterDto.setFamilyId(familyEntity.getId());
        memberMasterDto.setName(Stream.of(basicDetail.getFirstName(), " ", basicDetail.getMiddleName(), " ", basicDetail.getLastName()).filter(name -> name != null).collect(Collectors.joining()));
        List<MemberHypertensionDetail> memberHypertensionDetail = hypertensionDetailDao.retrieveLastRecordByMemberId(memberId);
        MemberDiabetesDetail memberDiabetesDetail = diabetesDetailDao.retrieveLastRecordByMemberId(memberId);
        MemberMentalHealthDetails memberMentalHealthDetails = mentalHealthDetailDao.retrieveLastRecordByMemberId(memberId);
        MemberInitialAssessmentDetail memberInitialAssessmentDetail = memberInitialAssessmentDao.retrieveLastRecordByMemberId(memberId);
        memberMasterDto.setMemberHypertensionDetail(memberHypertensionDetail.isEmpty() ? null : memberHypertensionDetail.get(0));
        memberMasterDto.setMemberDiabetesDetail(memberDiabetesDetail);
        memberMasterDto.setMemberMentalHealthDetails(memberMentalHealthDetails);
        memberMasterDto.setMemberInitialAssessmentDetail(memberInitialAssessmentDetail);
        memberMasterDto.setBpl(familyEntity.getBplFlag());
        memberMasterDto.setVulnerable(familyEntity.getVulnerableFlag());
        if (basicDetail.getAdditionalInfo() != null && !basicDetail.getAdditionalInfo().isEmpty()) {
            MemberAdditionalInfo memberAdditionalInfo = new Gson().fromJson(basicDetail.getAdditionalInfo(), MemberAdditionalInfo.class);
            memberMasterDto.setMemberAdditionalInfo(memberAdditionalInfo);
        }
        return memberMasterDto;
    }

    public String retrieveMemberStatusForNotification(Integer memberId, String diseaseCode, Status status) {
        NcdMaster ncdMaster = ncdMasterDao.retriveByMemberIdAndDiseaseCode(memberId, diseaseCode);
        if (ncdMaster != null) {
            if (ncdMaster.getStatus() != null && (status.equals(Status.REFERRED_BACK_MO))
                    && (ncdMaster.getStatus().equalsIgnoreCase(Status.REFERRED_MO.toString())
                    || ncdMaster.getStatus().equalsIgnoreCase(Status.REFERRED_CONSULTANT.toString()))) {
                return SubStatus.FOLLOWUP.toString();
            }
        }
        return null;
    }

    @Override
    public void handleEditMedicineDetails(List<GeneralDetailMedicineDto> editedMedicineDetail, Integer memberId, Date screeningDate, Integer hypertensionId, MemberDiseaseDiagnosis.DiseaseCode ht, Integer healthInfraId) {
        //check if there is a medicine in edit
        if (editedMedicineDetail != null && !editedMedicineDetail.isEmpty() && editedMedicineDetail.size() > 0) {
            //fetch each record
            for (GeneralDetailMedicineDto medicine : editedMedicineDetail) {
                //check if medicine already exists
                MemberDiseaseMedicine previousMedicine = memberDiseaseMedicineDao.retrieveById(medicine.getId());
                //check if record exists
                if (previousMedicine != null) {
                    DrugInventoryDto lastParentInventoryDto = drugInventoryDao.retrieveMedicine
                            (medicine.getMedicineId(), healthInfraId);
                    if (lastParentInventoryDto != null) {
                        //update drug inventory
                        DrugInventoryDto drugInventoryDto = new DrugInventoryDto();
                        drugInventoryDto.setMedicineId(medicine.getMedicineId());
                        drugInventoryDto.setIssuedDate(screeningDate);
                        drugInventoryDto.setHealthInfraId(healthInfraId);
                        if (previousMedicine.getQuantity() > medicine.getQuantity() && medicine.getReturn()!=null && medicine.getReturn()) {
                            //add medicines in drug inventory
                            drugInventoryDto.setIsReceived(true);
                            drugInventoryDto.setIsIssued(false);
                            drugInventoryDto.setQuantityReceived(previousMedicine.getQuantity() - medicine.getQuantity());
                            drugInventoryDto.setQuantityIssued(0);
                            drugInventoryDto.setBalanceInHand(lastParentInventoryDto.getBalanceInHand() + drugInventoryDto.getQuantityReceived());
                        } else if (previousMedicine.getQuantity() < medicine.getQuantity()) {
                            //remove medicines from drug inventory
                            drugInventoryDto.setIsReceived(false);
                            drugInventoryDto.setIsIssued(true);
                            drugInventoryDto.setQuantityReceived(0);
                            drugInventoryDto.setQuantityIssued(medicine.getQuantity() - previousMedicine.getQuantity());
                            drugInventoryDto.setBalanceInHand(lastParentInventoryDto.getBalanceInHand() - drugInventoryDto.getQuantityIssued());
                        } else {
                            //do nothing
                        }
                        DrugInventoryDetail drugInventoryDetail = MemberDetailMapper.dtoToEntityForDrugInventoryDetail(drugInventoryDto);
                        drugInventoryDao.create(drugInventoryDetail);
                    } else {
                        if (previousMedicine.getQuantity() > medicine.getQuantity() && medicine.getReturn()!=null && medicine.getReturn()) {
                            //check if medicine is for return
                            // if so, add in drug inventory
                            DrugInventoryDto drugInventoryDto = new DrugInventoryDto();
                            drugInventoryDto.setMedicineId(medicine.getMedicineId());
                            drugInventoryDto.setIssuedDate(screeningDate);
                            drugInventoryDto.setHealthInfraId(healthInfraId);
                            drugInventoryDto.setIsReceived(true);
                            drugInventoryDto.setIsIssued(false);
                            drugInventoryDto.setQuantityReceived(previousMedicine.getQuantity() - medicine.getQuantity());
                            drugInventoryDto.setQuantityIssued(0);
                            drugInventoryDto.setBalanceInHand(drugInventoryDto.getQuantityReceived());
                            DrugInventoryDetail drugInventoryDetail = MemberDetailMapper.dtoToEntityForDrugInventoryDetail(drugInventoryDto);
                            drugInventoryDao.create(drugInventoryDetail);
                        } else if (previousMedicine.getQuantity() < medicine.getQuantity()) {
                            //check if it is to get medicine
                            //if so, throw error
                            throw new ImtechoUserException("Insufficient medicines ", 400);
                        } else {
                            //Do nothing
                        }
                    }
                    //update duration, quantity & expiry date
                    Calendar c = Calendar.getInstance();
                    c.setTime(previousMedicine.getExpiryDate());
                    c.add(Calendar.DATE, medicine.getDuration() - previousMedicine.getDuration());
                    previousMedicine.setExpiryDate(c.getTime());
                    previousMedicine.setDuration(medicine.getDuration());
                    previousMedicine.setFrequency(medicine.getFrequency());
                    previousMedicine.setQuantity(medicine.getQuantity());
                    memberDiseaseMedicineDao.update(previousMedicine);

                }
            }
        }
    }

    @Override
    public void handleDeleteMedicineDetails(List<GeneralDetailMedicineDto> deletedMedicineDetail, Integer memberId, Integer healthInfraId) {
        if (deletedMedicineDetail != null && !deletedMedicineDetail.isEmpty() && deletedMedicineDetail.size() > 0) {
            for (GeneralDetailMedicineDto medicine : deletedMedicineDetail
            ) {
                MemberDiseaseMedicine memberDiseaseMedicine = memberDiseaseMedicineDao.retrieveByIdAndMemberId(medicine.getId(), memberId);
                if (memberDiseaseMedicine != null) {
                    memberDiseaseMedicine.setDeleted(true);
                    memberDiseaseMedicine.setDeletedOn(new Date());
                    memberDiseaseMedicineDao.update(memberDiseaseMedicine);
                    if(medicine.getReturn()!= null && medicine.getReturn()) {
                        DrugInventoryDto drugInventoryDto = new DrugInventoryDto();
                        drugInventoryDto.setMedicineId(medicine.getMedicineId());
                        drugInventoryDto.setIssuedDate(new Date());
                        drugInventoryDto.setHealthInfraId(healthInfraId);
                        drugInventoryDto.setIsReceived(true);
                        drugInventoryDto.setIsIssued(false);
                        drugInventoryDto.setQuantityReceived(medicine.getQuantity());
                        drugInventoryDto.setQuantityIssued(0);
                        DrugInventoryDto lastParentInventoryDto = drugInventoryDao.retrieveMedicine
                                (medicine.getMedicineId(), healthInfraId);
                        if (lastParentInventoryDto != null) {
                            drugInventoryDto.setBalanceInHand(lastParentInventoryDto.getBalanceInHand() + drugInventoryDto.getQuantityReceived());
                        } else {
                            drugInventoryDto.setBalanceInHand(drugInventoryDto.getQuantityReceived());
                        }
                        DrugInventoryDetail drugInventoryDetail = MemberDetailMapper.dtoToEntityForDrugInventoryDetail(drugInventoryDto);
                        drugInventoryDao.create(drugInventoryDetail);
                    }
                } else {
                    throw new ImtechoUserException("Issue while deleting medicines", 400);
                }
            }
        }
    }

    private void createOrUpdateMasterRecord(Integer memberId, Integer id, String type) {
        NcdSpecialistMaster ncdSpecialistMaster = ncdSpecicalistMasterDao.retrieveByMemberId(memberId);
        if(ncdSpecialistMaster == null){
            ncdSpecialistMaster = new NcdSpecialistMaster();
            ncdSpecialistMaster.setMemberId(memberId);
        }
        switch (type){
            case "ECG":
                ncdSpecialistMaster.setLastEcgScreeningId(id);
                break;
            case "urine":
                ncdSpecialistMaster.setLastUrineScreeningId(id);
                break;
            case "generalScreening":
                ncdSpecialistMaster.setLastGenericScreeningId(id);
                break;
            case "retinopathy":
                ncdSpecialistMaster.setLastRetinopathyTestId(id);
                break;
            default:
                break;
        }
        ncdSpecicalistMasterDao.createOrUpdate(ncdSpecialistMaster);
    }

    @Override
    public RecordStatusBean storeMediaData(DocumentDto documentDto, UploadFileDataBean uploadFileDataBean) {
        RecordStatusBean recordStatusBean = new RecordStatusBean();
        if(uploadFileDataBean.getFormType().equals(MobileConstantUtil.NCD_ECG_REPORT)) {

            MemberEcgDetail memberEcgDetail = ecgDetailDao.retrieveByPdfUuId(uploadFileDataBean.getUniqueId());
            if (memberEcgDetail != null) {

                memberEcgDetail.setReportPdfDocId(documentDto.getId());
                memberEcgDetail.setReportPdfDocUuid(null);
                ecgDetailDao.update(memberEcgDetail);

                recordStatusBean.setChecksum(uploadFileDataBean.getCheckSum());
                recordStatusBean.setStatus(MobileConstantUtil.SUCCESS_VALUE);
            } else {
                memberEcgDetail = ecgDetailDao.retrieveByImageUuId(uploadFileDataBean.getUniqueId());
                if (memberEcgDetail != null) {

                    memberEcgDetail.setReportImageDocId(documentDto.getId());
                    memberEcgDetail.setReportImageDocUuid(null);
                    ecgDetailDao.update(memberEcgDetail);

                    recordStatusBean.setChecksum(uploadFileDataBean.getCheckSum());
                    recordStatusBean.setStatus(MobileConstantUtil.SUCCESS_VALUE);
                }
            }
        }
        else if(uploadFileDataBean.getFormType().equals(MobileConstantUtil.NCD_MO_CONFIRMED)) {
            MemberGeneralScreeningDetail memberGeneralScreeningDetail = generalScreeningDao.retrieveByImageUuId(uploadFileDataBean.getUniqueId());
            if (memberGeneralScreeningDetail != null) {
                List<String> imageUuids = new ArrayList<>();
                List<Long> imageIds = new ArrayList<>();
                if(memberGeneralScreeningDetail.getImageUuid() != null && !memberGeneralScreeningDetail.getImageUuid().isEmpty()){
                    String[] array = memberGeneralScreeningDetail.getImageUuid().split(",");
                    imageUuids = new ArrayList<>(Arrays.asList(array));
                }
                if(memberGeneralScreeningDetail.getImage() != null && !memberGeneralScreeningDetail.getImage().isEmpty()){
                  imageIds = Arrays.stream(memberGeneralScreeningDetail.getImage().split(","))
                          .map(Long::parseLong).collect(Collectors.toList());
                }
                imageIds.add(documentDto.getId());

                memberGeneralScreeningDetail.setImage(getCommaSeparatedFromLongList(imageIds));
                imageUuids.remove(uploadFileDataBean.getUniqueId());
                if(!imageUuids.isEmpty())
                    memberGeneralScreeningDetail.setImageUuid(String.join(",",imageUuids));
                else
                    memberGeneralScreeningDetail.setImageUuid(null);
                generalScreeningDao.update(memberGeneralScreeningDetail);

                recordStatusBean.setChecksum(uploadFileDataBean.getCheckSum());
                recordStatusBean.setStatus(MobileConstantUtil.SUCCESS_VALUE);
            }
        }
        return recordStatusBean;
    }
}
