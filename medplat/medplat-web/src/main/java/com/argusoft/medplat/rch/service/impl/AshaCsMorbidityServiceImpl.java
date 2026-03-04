package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dao.AshaCsMorbidityDao;
import com.argusoft.medplat.rch.dao.AshaCsMorbidityDetailsDao;
import com.argusoft.medplat.rch.dto.MorbidityDto;
import com.argusoft.medplat.rch.model.AshaCsMorbidityDetailsMaster;
import com.argusoft.medplat.rch.model.AshaCsMorbidityMaster;
import com.argusoft.medplat.rch.model.MorbidityDetailsPKey;
import com.argusoft.medplat.rch.service.AshaCsMorbidityService;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Define services for ASHA child service morbidity.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AshaCsMorbidityServiceImpl implements AshaCsMorbidityService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AshaCsMorbidityDao morbidityDao;

    @Autowired
    private AshaCsMorbidityDetailsDao morbidityDetailsDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAshaCsMorbidity(ParsedRecordBean parsedRecordBean, Integer dependencyId, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity childEntity = memberDao.retrieveById(memberId);

        AshaCsMorbidityMaster morbidityMaster = new AshaCsMorbidityMaster();
        morbidityMaster.setMemberId(memberId);
        morbidityMaster.setFamilyId(familyId);
        morbidityMaster.setLocationId(locationId);
        morbidityMaster.setCsId(dependencyId);
        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToCSMorbidity(key, answer, morbidityMaster, keyAndAnswerMap);
        }
        List<MorbidityDto> morbidityDtos = extractMorbidityDetails(morbidityMaster, parsedRecordBean.getMorbidityFrame());
        morbidityDao.create(morbidityMaster);
        List<AshaCsMorbidityDetailsMaster> morbidityDetailsMasters = new ArrayList<>();
        for (MorbidityDto morbidityDto : morbidityDtos) {
            morbidityDetailsMasters.add(
                    new AshaCsMorbidityDetailsMaster(
                            new MorbidityDetailsPKey(morbidityMaster.getId(), morbidityDto.getCode()),
                            morbidityDto.getStatus(),
                            morbidityDto.getSymptoms()));
        }
        if (!morbidityDetailsMasters.isEmpty()) {
            morbidityDetailsDao.createOrUpdateAll(morbidityDetailsMasters);
        }
        updateMemberDetails(childEntity, morbidityDtos);
        return morbidityMaster.getId();
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
     * Extract morbidity details.
     *
     * @param morbidityMaster Entity of morbidity.
     * @param morbidityFrame  Morbidity frame.
     * @return Returns list of morbidity details.
     */
    private List<MorbidityDto> extractMorbidityDetails(AshaCsMorbidityMaster morbidityMaster, String morbidityFrame) {
        List<MorbidityDto> morbidityList = new ArrayList<>();
        String[] parseRecord = morbidityFrame.split(MobileConstantUtil.MORBIDITY_DASH_SEPERATOR);
        for (String s : parseRecord) {
            String[] beneficiaryString = s.split(MobileConstantUtil.MORBIDITY_TILD_SEPERATOR);
            for (String value : beneficiaryString) {
                String[] beneficiaryValue = value.split(MobileConstantUtil.MORBIDITY_EXCLM_SEPERATOR);
                int bindIndex = Integer.parseInt(beneficiaryValue[0]);
                String bindValue = beneficiaryValue[1];
                switch (bindIndex) {
                    case 1:
                        // Beneficiary Name
                        break;
                    case 2:
                        // Beneficiary Type
                        break;
                    case 3:
                        // Family Understand
                        morbidityMaster.setFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(bindValue));
                        break;
                    case 4:
                        // Morbidity found
                        morbidityMaster.setMorbidityFound(ImtechoUtil.returnTrueFalseFromInitials(bindValue));
                        break;
                    case 5:
                        // Morbidity List
                        setMorbidityListDetails(morbidityList, bindValue);
                        break;
                    default:
                }
            }
        }
        return morbidityList;
    }

    /**
     * Retrieves morbidity details.
     *
     * @param morbidityList   List of morbidity.
     * @param morbidityString Morbidity.
     */
    private void setMorbidityListDetails(List<MorbidityDto> morbidityList, String morbidityString) {
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
    }

    /**
     * Set answer to anc visit morbidity.
     *
     * @param key             Key.
     * @param answer          Answer for member's anc morbidity details.
     * @param morbidityMaster Entity of morbidity.
     */
    private void setAnswersToCSMorbidity(String key, String answer, AshaCsMorbidityMaster morbidityMaster, Map<String, String> keyAndAnswerMap) {
        switch (key) {
            case "-1":
                morbidityMaster.setLongitude(answer);
                break;

            case "-2":
                morbidityMaster.setLatitude(answer);
                break;

            case "-8":
                morbidityMaster.setMobileStartDate(new Date(Long.parseLong(answer)));
                break;

            case "-9":
                morbidityMaster.setMobileEndDate(new Date(Long.parseLong(answer)));
                break;

            case "53":
                morbidityMaster.setReadyForReferral(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "54":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setAbleToCall108(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "1000":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setCallLog(answer);
                }
                break;

            case "56":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setAccompanyChild(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "57":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "58":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setReferralPlace(answer);
                }
                break;

            case "59":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setReferralVehicle(answer);
                }
                break;

            case "300":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSeverePneumoniaOralFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "3001":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSeverePneumoniaOralFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "301":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSeverePneumoniaCotriGiven(answer);
                }
                break;

            case "3011":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSeverePneumoniaCotriGiven(answer);
                }
                break;

            case "302":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setChronicCoughFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "3021":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setChronicCoughFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "303":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setDiarrhoeaSevereDehydrationOralFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "3031":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setDiarrhoeaSevereDehydrationOralFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "304":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setDiarrhoeaSevereDehydrationOrsGiven(answer);
                }
                break;

            case "3041":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setDiarrhoeaSevereDehydrationOrsGiven(answer);
                }
                break;

            case "306":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setFebrileIllnessCotriGiven(answer);
                }
                break;

            case "3061":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setFebrileIllnessCotriGiven(answer);
                }
                break;

            case "307":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setFebrileIllnessSlidesTaken(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "3071":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setFebrileIllnessSlidesTaken(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "310":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setFebrileIllnessChloroquineGiven(answer);
                }
                break;

            case "3101":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setFebrileIllnessChloroquineGiven(answer);
                }
                break;

            case "312":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setFebrileIllnessPcmGiven(answer);
                }
                break;

            case "3121":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setFebrileIllnessPcmGiven(answer);
                }
                break;

            case "315":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSevereMalnutritionVitaminAGiven(answer);
                }
                break;

            case "3151":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSevereMalnutritionVitaminAGiven(answer);
                }
                break;

            case "319":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSevereMalnutritionBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "3191":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSevereMalnutritionBreastFeedingUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "322":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSevereAnemiaFolicGiven(answer);
                }
                break;

            case "3221":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSevereAnemiaFolicGiven(answer);
                }
                break;

            case "323":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSeverePersistentDiarrioeaOralFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "3231":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSeverePersistentDiarrioeaOralFood(ImtechoUtil.returnTrueFalseFromInitials(answer));
                }
                break;

            case "324":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("T")) {
                    morbidityMaster.setSeverePersistentDiarrioeaOrsGiven(answer);
                }
                break;

            case "3241":
                if (keyAndAnswerMap.get("53") != null && keyAndAnswerMap.get("53").equals("F")) {
                    morbidityMaster.setSeverePersistentDiarrioeaOrsGiven(answer);
                }
                break;

            case "999":
                morbidityMaster.setPneumoniaCotriGiven(answer);
                break;

            case "403":
                morbidityMaster.setPneumoniaCotrimoxazoleSyrup(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "404":
                morbidityMaster.setPneumoniaFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "409":
                morbidityMaster.setDiarrhoeaSomeDehydrationOrsGiven(answer);
                break;

            case "413":
                morbidityMaster.setDiarrhoeaSomeDehydrationOrsUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "416":
                morbidityMaster.setDiarrhoeaSomeDehydrationFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "418":
                morbidityMaster.setDiarrhoeaSomeDehydrationDehydrationUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "420":
                morbidityMaster.setDysentryCotriGiven(answer);
                break;

            case "422":
                morbidityMaster.setDysentryCotrimoxazoleSyrup(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "423":
                morbidityMaster.setDysentryReturnImmediately(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "425":
                morbidityMaster.setMalariaSlidesTaken(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "428":
                morbidityMaster.setMalariaChloroquineGiven(answer);
                break;

            case "430":
                morbidityMaster.setMalariaPcmGiven(answer);
                break;

            case "432":
                morbidityMaster.setMalariaFeverPersistFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "450":
                morbidityMaster.setAnemiaFolicGiven(answer);
                break;

            case "501":
                morbidityMaster.setColdCoughFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "506":
                morbidityMaster.setDiarrhoeaNoDehydrationOrsGiven(answer);
                break;

            case "510":
                morbidityMaster.setDiarrhoeaNoDehydrationOrsUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "511":
                morbidityMaster.setDiarrhoeaNoDehydrationFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;

            case "526":
                morbidityMaster.setNoAnemiaFolicGiven(answer);
                break;
            default:

        }
    }
}
