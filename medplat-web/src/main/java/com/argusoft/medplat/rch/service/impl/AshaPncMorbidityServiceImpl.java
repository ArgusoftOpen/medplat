package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dao.AshaPncChildMorbidityDao;
import com.argusoft.medplat.rch.dao.AshaPncChildMorbidityDetailsDao;
import com.argusoft.medplat.rch.dao.AshaPncMotherMorbidityDao;
import com.argusoft.medplat.rch.dao.AshaPncMotherMorbidityDetailsDao;
import com.argusoft.medplat.rch.dto.MorbidityDto;
import com.argusoft.medplat.rch.model.*;
import com.argusoft.medplat.rch.service.AshaPncMorbidityService;
import com.argusoft.medplat.rch.service.AshaPncService;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * Define services for ASHA pnc morbidity.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AshaPncMorbidityServiceImpl implements AshaPncMorbidityService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AshaPncMotherMorbidityDao motherMorbidityDao;

    @Autowired
    private AshaPncMotherMorbidityDetailsDao motherMorbidityDetailsDao;

    @Autowired
    private AshaPncChildMorbidityDao childMorbidityDao;

    @Autowired
    private AshaPncChildMorbidityDetailsDao childMorbidityDetailsDao;

    @Autowired
    private AshaPncService ashaPncService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAshaPncMorbidity(ParsedRecordBean parsedRecordBean, Integer dependencyId, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer createdInstanceId = null;
        Map<Integer, AshaPncMotherMorbidityMaster> motherMap = new HashMap<>();
        Map<Integer, AshaPncChildMorbidityMaster> childMap = new HashMap<>();
        Map<Integer, List<MorbidityDto>> morbidityMap = new HashMap<>();
        Map<Integer, MemberEntity> memberMap = new HashMap<>();

        extractMorbidityDetails(parsedRecordBean.getMorbidityFrame(), motherMap, childMap, morbidityMap, memberMap);

        if (!motherMap.isEmpty()) {
            for (Map.Entry<Integer, AshaPncMotherMorbidityMaster> entry : motherMap.entrySet()) {
                int counter = entry.getKey();
                AshaPncMotherMorbidityMaster morbidityMaster = entry.getValue();
                morbidityMaster.setLongitude(keyAndAnswerMap.get("-1"));
                morbidityMaster.setLatitude(keyAndAnswerMap.get("-2"));
                morbidityMaster.setFamilyId(Integer.valueOf(keyAndAnswerMap.get("-5")));
                morbidityMaster.setLocationId(Integer.valueOf(keyAndAnswerMap.get("-6")));
                morbidityMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
                morbidityMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));

                morbidityMaster.setPncId(
                        ashaPncService.retrievePncMotherMasterByPncMasterIdAndMemberId(
                                dependencyId, morbidityMaster.getMemberId()
                        ).getId()
                );

                setCommonAnswersToMotherMorbidity(keyAndAnswerMap, morbidityMaster);
                for (Map.Entry<String, String> entry1 : keyAndAnswerMap.entrySet()) {
                    setAnswersToPncMotherMorbidity(entry1.getKey(), entry1.getValue(), morbidityMaster);
                }
                motherMorbidityDao.create(morbidityMaster);
                createdInstanceId = morbidityMaster.getId();
                List<MorbidityDto> morbidityDtos = morbidityMap.get(counter);
                List<AshaPncMotherMorbidityDetailsMaster> morbidityDetailsMasters = new ArrayList<>();
                for (MorbidityDto morbidityDto : morbidityDtos) {
                    morbidityDetailsMasters.add(
                            new AshaPncMotherMorbidityDetailsMaster(
                                    new MorbidityDetailsPKey(morbidityMaster.getId(), morbidityDto.getCode()),
                                    morbidityDto.getStatus(),
                                    morbidityDto.getSymptoms()));
                }
                if (!morbidityDetailsMasters.isEmpty()) {
                    motherMorbidityDetailsDao.createOrUpdateAll(morbidityDetailsMasters);
                }
                updateMemberDetails(memberMap.get(counter), morbidityDtos);
            }
        }

        if (!childMap.isEmpty()) {
            for (Map.Entry<Integer, AshaPncChildMorbidityMaster> entry : childMap.entrySet()) {
                int counter = entry.getKey();
                AshaPncChildMorbidityMaster morbidityMaster = entry.getValue();
                morbidityMaster.setLongitude(keyAndAnswerMap.get("-1"));
                morbidityMaster.setLatitude(keyAndAnswerMap.get("-2"));
                morbidityMaster.setFamilyId(Integer.valueOf(keyAndAnswerMap.get("-5")));
                morbidityMaster.setLocationId(Integer.valueOf(keyAndAnswerMap.get("-6")));
                morbidityMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
                morbidityMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));

                morbidityMaster.setPncId(
                        ashaPncService.retrievePncChildMasterByPncMasterIdAndMemberId(
                                dependencyId, morbidityMaster.getMemberId()
                        ).getId()
                );

                setCommonAnswersToChildMorbidity(keyAndAnswerMap, morbidityMaster);
                for (Map.Entry<String, String> entry1 : keyAndAnswerMap.entrySet()) {
                    String key = entry1.getKey();
                    String value = entry1.getValue();
                    if (counter == 0) {
                        setAnswersToPncChildMorbidity(key, value, morbidityMaster);
                    } else {
                        if (key.contains(".")) {
                            String[] split = key.split("\\.");
                            if (Integer.parseInt(split[1]) == counter) {
                                setAnswersToPncChildMorbidity(split[0], value, morbidityMaster);
                            }
                        }
                    }
                }
                childMorbidityDao.create(morbidityMaster);
                if (createdInstanceId == null) {
                    createdInstanceId = morbidityMaster.getId();
                }
                List<MorbidityDto> morbidityDtos = morbidityMap.get(counter);
                List<AshaPncChildMorbidityDetailsMaster> morbidityDetailsMasters = new ArrayList<>();
                for (MorbidityDto morbidityDto : morbidityDtos) {
                    morbidityDetailsMasters.add(
                            new AshaPncChildMorbidityDetailsMaster(
                                    new MorbidityDetailsPKey(morbidityMaster.getId(), morbidityDto.getCode()),
                                    morbidityDto.getStatus(),
                                    morbidityDto.getSymptoms()));
                }
                if (!morbidityDetailsMasters.isEmpty()) {
                    childMorbidityDetailsDao.createOrUpdateAll(morbidityDetailsMasters);
                }
                updateMemberDetails(memberMap.get(counter), morbidityDtos);
            }
        }
        return createdInstanceId;
    }

    /**
     * Extract morbidity details.
     *
     * @param motherMap      Entity of morbidity.
     * @param morbidityFrame Morbidity frame.
     * @param childMap       Map for child.
     * @param morbidityMap   Map for morbidity.
     * @param memberMap      map for member.
     * @return Returns list of morbidity details.
     */
    private void extractMorbidityDetails(String morbidityFrame,
                                         Map<Integer, AshaPncMotherMorbidityMaster> motherMap, Map<Integer, AshaPncChildMorbidityMaster> childMap,
                                         Map<Integer, List<MorbidityDto>> morbidityMap, Map<Integer, MemberEntity> memberMap) {

        String[] parseRecord = morbidityFrame.split(MobileConstantUtil.MORBIDITY_DASH_SEPERATOR);
        for (int i = 0; i < parseRecord.length; i++) {
            String[] beneficiaryString = parseRecord[i].split(MobileConstantUtil.MORBIDITY_TILD_SEPERATOR);
            String beneficiaryType = getBeneficiaryType(beneficiaryString);

            if (beneficiaryType != null && beneficiaryType.equals("M")) {
                motherMap.put(i, new AshaPncMotherMorbidityMaster());
            } else if (beneficiaryType != null && beneficiaryType.equals("C")) {
                childMap.put(i, new AshaPncChildMorbidityMaster());
            }

            for (String s : beneficiaryString) {
                String[] beneficiaryValue = s.split(MobileConstantUtil.MORBIDITY_EXCLM_SEPERATOR);
                int bindIndex = Integer.parseInt(beneficiaryValue[0]);
                String bindValue = beneficiaryValue[1];
                switch (bindIndex) {
                    case 0:
                        // Beneficiary Health Id
                        MemberEntity memberEntity = memberDao.getMemberByUniqueHealthIdAndFamilyId(bindValue, null);
                        memberMap.put(i, memberEntity);
                        if (beneficiaryType != null && beneficiaryType.equals("M")) {
                            motherMap.get(i).setMemberId(memberEntity.getId());
                        } else if (beneficiaryType != null && beneficiaryType.equals("C")) {
                            childMap.get(i).setMemberId(memberEntity.getId());
                        }
                        break;
                    case 1:
                        // Beneficiary Name
                        break;
                    case 2:
                        // Beneficiary Type
                        break;
                    case 3:
                        // Family Understand
                        if (beneficiaryType != null && beneficiaryType.equals("M")) {
                            motherMap.get(i).setFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(bindValue));
                        } else if (beneficiaryType != null && beneficiaryType.equals("C")) {
                            childMap.get(i).setFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(bindValue));
                        }
                        break;
                    case 4:
                        // Morbidity found
                        if (beneficiaryType != null && beneficiaryType.equals("M")) {
                            motherMap.get(i).setMorbidityFound(ImtechoUtil.returnTrueFalseFromInitials(bindValue));
                        } else if (beneficiaryType != null && beneficiaryType.equals("C")) {
                            childMap.get(i).setMorbidityFound(ImtechoUtil.returnTrueFalseFromInitials(bindValue));
                        }
                        break;
                    case 5:
                        // Morbidity List
                        morbidityMap.put(i, getMorbidityList(bindValue));
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Retrieves beneficiary details by type.
     *
     * @param beneficiaryString Beneficiary type.
     * @return Returns beneficiary details.
     */
    private String getBeneficiaryType(String[] beneficiaryString) {
        for (String s : beneficiaryString) {
            String[] beneficiaryValue = s.split(MobileConstantUtil.MORBIDITY_EXCLM_SEPERATOR);
            if (beneficiaryValue[0].equals("2")) {
                return beneficiaryValue[1];
            }
        }
        return null;
    }

    /**
     * Retrieves morbidity details.
     *
     * @param morbidityString Morbidity.
     * @return Returns list of morbidity details.
     */
    private List<MorbidityDto> getMorbidityList(String morbidityString) {
        List<MorbidityDto> morbidityList = new ArrayList<>();
        String[] morbidityArray = morbidityString.split(MobileConstantUtil.MORBIDITY_HASH_SEPERATOR);
        for (String s : morbidityArray) {
            MorbidityDto morbidity = new MorbidityDto();
            String[] morbidityValue = s.split(MobileConstantUtil.MORBIDITY_AT_SEPERATOR);
            if (morbidityValue.length > 0) {
                morbidity.setCode(morbidityValue[0]);
                if (morbidityValue.length > 1) {
                    morbidity.setStatus(morbidityValue[1]);
                    if (morbidityValue.length > 2) {
                        morbidity.setSymptoms(morbidityValue[2]);
                    }
                }
            }
            morbidityList.add(morbidity);
        }
        return morbidityList;
    }

    /**
     * Update member details.
     *
     * @param member        Entity of member.
     * @param morbidityDtos Morbidity details.
     */
    private void updateMemberDetails(MemberEntity member, List<MorbidityDto> morbidityDtos) {
        if (morbidityDtos != null && !morbidityDtos.isEmpty()) {
            MemberAdditionalInfo memberAdditionalInfo;
            String additionalInfo = member.getAdditionalInfo();
            if (additionalInfo == null) {
                memberAdditionalInfo = new MemberAdditionalInfo();
            } else {
                memberAdditionalInfo = new Gson().fromJson(additionalInfo, MemberAdditionalInfo.class);
            }
            memberAdditionalInfo.setAncAshaMorbidity(new Gson().toJson(morbidityDtos));
            member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
            memberDao.update(member);
        }
    }

    /**
     * Set answers to mother's morbidity details.
     *
     * @param answerMap       Map for answer.
     * @param morbidityMaster Mother morbidity master.
     */
    private void setCommonAnswersToMotherMorbidity(Map<String, String> answerMap,
                                                   AshaPncMotherMorbidityMaster morbidityMaster) {
        if (answerMap.get("53") != null) {
            morbidityMaster.setReadyForReferral(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("53")));
        }
        if (answerMap.get("54") != null) {
            morbidityMaster.setAbleToCall108(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("54")));
        }
        if (answerMap.get("1000") != null) {
            morbidityMaster.setCallLog(answerMap.get("1000"));
        }
        if (answerMap.get("56") != null) {
            morbidityMaster.setAccompanyWomen(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("56")));
        }
        if (answerMap.get("60") != null) {
            morbidityMaster.setAccompanyWomen(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("60")));
        }
        if (answerMap.get("64") != null) {
            morbidityMaster.setAccompanyWomen(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("64")));
        }
        if (answerMap.get("57") != null) {
            morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("57")));
        }
        if (answerMap.get("61") != null) {
            morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("61")));
        }
        if (answerMap.get("65") != null) {
            morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("65")));
        }
        if (answerMap.get("58") != null) {
            morbidityMaster.setReferralPlace(answerMap.get("58"));
        }
        if (answerMap.get("62") != null) {
            morbidityMaster.setReferralPlace(answerMap.get("62"));
        }
        if (answerMap.get("66") != null) {
            morbidityMaster.setReferralPlace(answerMap.get("66"));
        }
        if (answerMap.get("59") != null) {
            morbidityMaster.setReferralVehicle(answerMap.get("59"));
        }
        if (answerMap.get("63") != null) {
            morbidityMaster.setReferralVehicle(answerMap.get("63"));
        }
        if (answerMap.get("67") != null) {
            morbidityMaster.setReferralVehicle(answerMap.get("67"));
        }
    }

    /**
     * Set answers to child's morbidity details.
     *
     * @param answerMap       Map for answer.
     * @param morbidityMaster Child morbidity master.
     */
    private void setCommonAnswersToChildMorbidity(Map<String, String> answerMap,
                                                  AshaPncChildMorbidityMaster morbidityMaster) {
        if (answerMap.get("53") != null) {
            morbidityMaster.setReadyForReferral(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("53")));
        }
        if (answerMap.get("54") != null) {
            morbidityMaster.setAbleToCall108(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("54")));
        }
        if (answerMap.get("1000") != null) {
            morbidityMaster.setCallLog(answerMap.get("1000"));
        }
        if (answerMap.get("56") != null) {
            morbidityMaster.setAccompanyChild(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("56")));
        }
        if (answerMap.get("60") != null) {
            morbidityMaster.setAccompanyChild(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("60")));
        }
        if (answerMap.get("64") != null) {
            morbidityMaster.setAccompanyChild(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("64")));
        }
        if (answerMap.get("57") != null) {
            morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("57")));
        }
        if (answerMap.get("61") != null) {
            morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("61")));
        }
        if (answerMap.get("65") != null) {
            morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answerMap.get("65")));
        }
        if (answerMap.get("58") != null) {
            morbidityMaster.setReferralPlace(answerMap.get("58"));
        }
        if (answerMap.get("62") != null) {
            morbidityMaster.setReferralPlace(answerMap.get("62"));
        }
        if (answerMap.get("66") != null) {
            morbidityMaster.setReferralPlace(answerMap.get("66"));
        }
        if (answerMap.get("59") != null) {
            morbidityMaster.setReferralVehicle(answerMap.get("59"));
        }
        if (answerMap.get("63") != null) {
            morbidityMaster.setReferralVehicle(answerMap.get("63"));
        }
        if (answerMap.get("67") != null) {
            morbidityMaster.setReferralVehicle(answerMap.get("67"));
        }
    }

    /**
     * Set answers to pnc mother morbidity.
     *
     * @param key             Key.
     * @param answer          Answer for pnc mother morbidity.
     * @param morbidityMaster Mother morbidity master.
     */
    private void setAnswersToPncMotherMorbidity(String key, String answer, AshaPncMotherMorbidityMaster morbidityMaster) {
        switch (key) {
            case "394":
            case "398":
                morbidityMaster.setMastitisPcmGiven(answer);
                break;
            case "396":
            case "400":
                morbidityMaster.setMastitisBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "401":
                morbidityMaster.setMastitisWarmWaterUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "535":
                morbidityMaster.setFeedingProblemBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "540":
                morbidityMaster.setFeedingProblemEngorgedBreastUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "544":
                morbidityMaster.setFeedingProblemCrakdNippleUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "547":
                morbidityMaster.setFeedingProblemRetrctdNippleUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "548":
                morbidityMaster.setFeedingProblemFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }

    /**
     * Set answers to pnc child morbidity.
     *
     * @param key             Key.
     * @param answer          Answer for pnc child morbidity.
     * @param morbidityMaster Child morbidity master.
     */
    private void setAnswersToPncChildMorbidity(String key, String answer, AshaPncChildMorbidityMaster morbidityMaster) {
        switch (key) {
            case "309":
                morbidityMaster.setSepsisCotriUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "300":
            case "310":
                morbidityMaster.setSepsisCotriGiven(answer);
                break;
            case "308":
            case "314":
                morbidityMaster.setSepsisWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "305":
            case "316":
                morbidityMaster.setSepsisBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "303":
            case "320":
                morbidityMaster.setSepsisSyrupPcmGiven(answer);
                break;
            case "323":
                morbidityMaster.setSepsisGvLotion(answer);
                break;
            case "328":
            case "339":
            case "639":
                morbidityMaster.setVlbwBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "331":
            case "351":
                morbidityMaster.setVlbwWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "640":
                morbidityMaster.setVlbwKmcUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "352":
                morbidityMaster.setVlbwCallUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "357":
                morbidityMaster.setBleedingWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "359":
                morbidityMaster.setBleedingBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "361":
            case "363":
                morbidityMaster.setJaundiceBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "366":
                morbidityMaster.setJaundiceWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "381":
            case "372":
                morbidityMaster.setDiarrhoeaOrsUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "384":
            case "375":
                morbidityMaster.setDiarrhoeaBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "385":
                morbidityMaster.setDiarrhoeaCallUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "502":
                morbidityMaster.setPneumoniaCotriUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "503":
                morbidityMaster.setPneumoniaCotriGiven(answer);
                break;
            case "504":
                morbidityMaster.setPneumoniaCallUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "509":
                morbidityMaster.setLocalInfectionGvLotionUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "510":
                morbidityMaster.setLocalInfectionGvLotionGiven(answer);
                break;
            case "511":
                morbidityMaster.setLocalInfectionCallUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "702":
                morbidityMaster.setHypothermiaRiskUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "709":
                morbidityMaster.setHypothermiaWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "712":
                morbidityMaster.setHypothermiaKmcUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "713":
                morbidityMaster.setHypothermiaKmcHelp(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "527":
                morbidityMaster.setHighRiskLbwBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "530":
                morbidityMaster.setHighRiskLbwWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "5271":
                morbidityMaster.setHighRiskLbwKmcUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "531":
                morbidityMaster.setHighRiskLbwKmcHelp(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "532":
                morbidityMaster.setHighRiskLbwCallUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "561":
                morbidityMaster.setLbwBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "562":
                morbidityMaster.setLbwKmcUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "565":
                morbidityMaster.setLbwWarmUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "566":
                morbidityMaster.setLbwKmcHelp(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "567":
                morbidityMaster.setLbwCallUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }
}
