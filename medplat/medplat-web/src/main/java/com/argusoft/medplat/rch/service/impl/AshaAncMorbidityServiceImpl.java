package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dao.AshaAncMorbidityDao;
import com.argusoft.medplat.rch.dao.AshaAncMorbidityDetailsDao;
import com.argusoft.medplat.rch.dto.MorbidityDto;
import com.argusoft.medplat.rch.model.AshaAncMorbidityDetailsMaster;
import com.argusoft.medplat.rch.model.AshaAncMorbidityMaster;
import com.argusoft.medplat.rch.model.MorbidityDetailsPKey;
import com.argusoft.medplat.rch.service.AshaAncMorbidityService;
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
 * Define services for ASHA anc morbidity.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AshaAncMorbidityServiceImpl implements AshaAncMorbidityService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AshaAncMorbidityDao morbidityDao;

    @Autowired
    private AshaAncMorbidityDetailsDao morbidityDetailsDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeAshaAncMorbidity(ParsedRecordBean parsedRecordBean, Integer dependencyId, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));
        MemberEntity motherEntity = memberDao.retrieveById(memberId);

        AshaAncMorbidityMaster morbidityMaster = new AshaAncMorbidityMaster();
        morbidityMaster.setMemberId(memberId);
        morbidityMaster.setFamilyId(familyId);
        morbidityMaster.setLocationId(locationId);
        morbidityMaster.setAncId(dependencyId);
        for (Map.Entry<String, String> entrySet : keyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToAncMorbidity(key, answer, morbidityMaster);
        }
        List<MorbidityDto> morbidityDtos = extractMorbidityDetails(morbidityMaster, parsedRecordBean.getMorbidityFrame());
        morbidityDao.create(morbidityMaster);
        List<AshaAncMorbidityDetailsMaster> morbidityDetailsMasters = new ArrayList<>();
        for (MorbidityDto morbidityDto : morbidityDtos) {
            morbidityDetailsMasters.add(
                    new AshaAncMorbidityDetailsMaster(
                            new MorbidityDetailsPKey(morbidityMaster.getId(), morbidityDto.getCode()),
                            morbidityDto.getStatus(),
                            morbidityDto.getSymptoms()));
        }
        if (!morbidityDetailsMasters.isEmpty()) {
            morbidityDetailsDao.createOrUpdateAll(morbidityDetailsMasters);
        }
        updateMemberDetails(motherEntity, morbidityDtos);
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
    private List<MorbidityDto> extractMorbidityDetails(AshaAncMorbidityMaster morbidityMaster, String morbidityFrame) {
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
     * Set morbidity details in Morbidity List DTO.
     *
     * @param morbidityList   List of morbidity.
     * @param morbidityString Morbidity String.
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
    private void setAnswersToAncMorbidity(String key, String answer, AshaAncMorbidityMaster morbidityMaster) {
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
            case "103":
                morbidityMaster.setFamilyUnderstandsBigHospital(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "104":
                morbidityMaster.setReadyForReferral(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "106":
                morbidityMaster.setAbleToCall108(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "50":
                morbidityMaster.setCallLog(answer);
                break;
            case "107":
                morbidityMaster.setAccompanyWomen(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "108":
                morbidityMaster.setReferralSlipGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "109":
                morbidityMaster.setReferralPlace(answer);
                break;
            case "110":
                morbidityMaster.setReferralVehicle(answer);
                break;
            case "222":
                morbidityMaster.setUnderstandHospitalDelivery(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "307":
                morbidityMaster.setSevereAnemiaIfaUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "401":
            case "404":
                morbidityMaster.setSickleCellPcmGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "501":
                morbidityMaster.setBadObstetricDoctorVisit(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "502":
                morbidityMaster.setBadObstetricHospitalVisit(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "503":
                morbidityMaster.setBadObstetricHospitalDelivery(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "504":
                morbidityMaster.setUnintendedPregContinuePregnancy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "505":
                morbidityMaster.setUnintendedPregArrangeMarriage(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "508":
                morbidityMaster.setUnintendedPregTerminationUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "511":
                morbidityMaster.setUnintendedPregHelp(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "514":
                morbidityMaster.setMildHypertensionHospitalDelivery(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "518":
                morbidityMaster.setMalariaChloroquineGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "519":
                morbidityMaster.setMalariaPcmGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "520":
                morbidityMaster.setMalariaChloroquineUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "521":
                morbidityMaster.setMalariaFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "522":
                morbidityMaster.setFeverPcmGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "523":
                morbidityMaster.setFeverPhcVisit(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "524":
                morbidityMaster.setFeverFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "529":
                morbidityMaster.setUrinaryTractHospitalVisit(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "530":
                morbidityMaster.setUrinaryTractFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "532":
                morbidityMaster.setVaginitisHospitalVisit(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "533":
                morbidityMaster.setVaginitisFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "535":
                morbidityMaster.setVaginitisBatheDaily(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "537":
                morbidityMaster.setNightBlindnessVhndVisit(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "544":
                morbidityMaster.setProbableAnemiaIfaUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "546":
                morbidityMaster.setProbableAnemiaHospitalDelivery(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "605":
                morbidityMaster.setEmesisPregnancyFamilyUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "610":
                morbidityMaster.setRespiratoryTractDrinkWater(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "612":
                morbidityMaster.setModerateAnemiaIfaGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "620":
                morbidityMaster.setModerateAnemiaIfaUnderstand(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "621":
                morbidityMaster.setModerateAnemiaHospitalDelivery(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "625":
                morbidityMaster.setBreastProblemDemonstration(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "626":
                morbidityMaster.setBreastProblemSyringeGiven(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }
}
