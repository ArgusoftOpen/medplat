/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.WpdChildDto;
import com.argusoft.medplat.rch.dto.WpdMasterDto;
import com.argusoft.medplat.rch.dto.WpdMotherDto;
import com.argusoft.medplat.rch.model.WpdChildMaster;
import com.argusoft.medplat.rch.model.WpdMotherMaster;

/**
 * <p>
 * Mapper for wpd in order to convert dto to model or model to dto.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class WpdMapper {
    private WpdMapper() {
    }

    /**
     * Convert wpd master details into entity.
     *
     * @param wpdMasterDto Wpd master details.
     * @return Returns entity of wpd master.
     */
    public static WpdMotherMaster convertWpdMasterDtoToWpdMotherMaster(WpdMasterDto wpdMasterDto) {
        WpdMotherMaster wpdMotherMaster = new WpdMotherMaster();
        wpdMotherMaster.setHasDeliveryHappened(wpdMasterDto.getIsDeliveryDone());
        wpdMotherMaster.setMemberId(wpdMasterDto.getMemberId());
        wpdMotherMaster.setFamilyId(wpdMasterDto.getFamilyId());
        wpdMotherMaster.setMobileStartDate(wpdMasterDto.getStartDate());
        wpdMotherMaster.setMobileEndDate(wpdMasterDto.getEndDate());
        wpdMotherMaster.setLocationId(wpdMasterDto.getLocationId());
        wpdMotherMaster.setDateOfDelivery(wpdMasterDto.getDeliveryDate());
        wpdMotherMaster.setMemberStatus(wpdMasterDto.getMemberStatus());
        wpdMotherMaster.setDeliveryPlace(wpdMasterDto.getDeliveryPlace());
        wpdMotherMaster.setTypeOfDelivery(wpdMasterDto.getTypeOfDelivery());
        wpdMotherMaster.setCorticoSteroidGiven(wpdMasterDto.getIsCorticoSteroidGiven());
        wpdMotherMaster.setMisoprostolGiven(wpdMasterDto.getWasMisoProstolGiven());
        wpdMotherMaster.setIsDischarged(wpdMasterDto.getIsDischarged());
        wpdMotherMaster.setTypeOfHospital(wpdMasterDto.getTypeOfHospital());
        wpdMotherMaster.setInstitutionalDeliveryPlace(wpdMasterDto.getDeliveryPlace());
        if (wpdMasterDto.getIsDischarged() != null && wpdMasterDto.getIsDischarged()) {
            wpdMotherMaster.setDischargeDate(wpdMasterDto.getDischargeDate());
            wpdMotherMaster.setFreeDropDelivery(wpdMasterDto.getFreeDropDelivery());
            wpdMotherMaster.setFreeMedicines(wpdMasterDto.getFreeMedicines());
            wpdMotherMaster.setFreeLabTest(wpdMasterDto.getFreeLabTest());
            wpdMotherMaster.setFreeDiet(wpdMasterDto.getFreeDiet());
            wpdMotherMaster.setFreeBloodTransfusion(wpdMasterDto.getFreeBloodTransfusion());
            wpdMotherMaster.setFreeDropTransport(wpdMasterDto.getFreeDropTransport());
        }
        wpdMotherMaster.setMtpDoneAt(wpdMasterDto.getMtpPlace());
        wpdMotherMaster.setMtpPerformedBy(wpdMasterDto.getMtpPerformedBy());
        if (wpdMasterDto.getDeliveryPlace().equals("HOSP") || wpdMasterDto.getDeliveryPlace().equals("THISHOSP")) {
            wpdMotherMaster.setDeliveryDoneBy(wpdMasterDto.getDeliveryDoneBy());
            wpdMotherMaster.setDeliveryPerson(wpdMasterDto.getDeliveryPerson());
            wpdMotherMaster.setDeliveryPersonName(wpdMasterDto.getDeliveryPersonName());
        }
        wpdMotherMaster.setMotherAlive(wpdMasterDto.getIsMotherAlive());
        if (Boolean.FALSE.equals(wpdMasterDto.getIsMotherAlive())) {
            wpdMotherMaster.setDeathDate(wpdMasterDto.getDeathDate());
            wpdMotherMaster.setDeathReason(wpdMasterDto.getDeathReason());
            wpdMotherMaster.setPlaceOfDeath(wpdMasterDto.getDeathPlace());
            wpdMotherMaster.setDeathInfrastructureId(wpdMasterDto.getDeathInfrastructureId());
            wpdMotherMaster.setFbmdsr(wpdMasterDto.getFbmdsr());
            wpdMotherMaster.setIsDischarged(Boolean.TRUE);
            wpdMotherMaster.setDischargeDate(wpdMasterDto.getDeathDate());
        } else {
            wpdMotherMaster.setOtherDangerSigns(wpdMasterDto.getMotherOtherDangerSign());
            wpdMotherMaster.setMotherDangerSigns(wpdMasterDto.getMotherDangerSigns());
            wpdMotherMaster.setHighRisks(wpdMasterDto.getHighRiskSymptomsDuringDelivery());
            wpdMotherMaster.setTreatments(wpdMasterDto.getTreatmentsDuringDelivery());
            wpdMotherMaster.setReferralPlace(wpdMasterDto.getMotherReferralPlace());
            wpdMotherMaster.setReferralInfraId(wpdMasterDto.getMotherReferralInfraId());
            wpdMotherMaster.setIsHighRiskCase(wpdMasterDto.getIsHighRiskCase());
        }
        if (wpdMasterDto.getMotherReferralInfraId() != null) {
            wpdMotherMaster.setIsDischarged(Boolean.TRUE);
            wpdMotherMaster.setDischargeDate(wpdMasterDto.getDeliveryDate());
        }
        wpdMotherMaster.setHealthInfrastructureId(wpdMasterDto.getHealthInfrastructureId());
        wpdMotherMaster.setPregnancyRegDetId(wpdMasterDto.getPregRegDetId());
        wpdMotherMaster.setIsFromWeb(wpdMasterDto.getIsFromWeb());
        wpdMotherMaster.setFamilyPlanningMethod(wpdMasterDto.getFamilyPlanningMethod());
        return wpdMotherMaster;
    }

    /**
     * Convert wpd child details into entity.
     *
     * @param wpdChildDto Wpd child details.
     * @return Returns entity of wpd child entity.
     */
    public static WpdChildMaster convertWpdChildDtoToWpdChildMaster(WpdChildDto wpdChildDto) {
        WpdChildMaster wpdChildMaster = new WpdChildMaster();
        wpdChildMaster.setPregnancyOutcome(wpdChildDto.getPregnancyOutcome());
        if (wpdChildDto.getPregnancyOutcome().equals("LBIRTH") || wpdChildDto.getPregnancyOutcome().equals("SBIRTH")) {
            wpdChildMaster.setGender(wpdChildDto.getChildGender());
            wpdChildMaster.setBirthWeight(wpdChildDto.getChildBirthWeight());
            wpdChildMaster.setDateOfDelivery(wpdChildDto.getDeliveryDate());
            wpdChildMaster.setBabyCriedAtBirth(wpdChildDto.getChildCry());
            wpdChildMaster.setMemberStatus(wpdChildDto.getMemberStatus());
            wpdChildMaster.setBreastFeedingInOneHour(wpdChildDto.getBreastFeeding());
            wpdChildMaster.setKangarooCare(wpdChildDto.getKangarooCare());
            wpdChildMaster.setBreastCrawl(wpdChildDto.getBreastCrawl());
            wpdChildMaster.setOtherCongentialDeformity(wpdChildDto.getCongenitalDeformityOther());
            wpdChildMaster.setReferralReason(wpdChildDto.getChildReferralReason());
            wpdChildMaster.setReferralTransport(wpdChildDto.getChildReferralTransport());
            wpdChildMaster.setReferralPlace(wpdChildDto.getChildReferralPlace());
            wpdChildMaster.setReferralInfraId(wpdChildDto.getChildReferralInfraId());
            wpdChildMaster.setCongentialDeformity(wpdChildDto.getCongenitalDeformityPresent());
            wpdChildMaster.setDangerSigns(wpdChildDto.getChildDangerSigns());
            wpdChildMaster.setOtherDangerSign(wpdChildDto.getChildOtherDangerSign());
            wpdChildMaster.setWasPremature(wpdChildDto.getWasPremature());
            wpdChildMaster.setName(wpdChildDto.getName());
            return wpdChildMaster;
        } else {
            return null;
        }

    }

    /**
     * Convert wpd mother master entity into dto.
     *
     * @param wpdMotherMaster Entity of wpd mother master.
     * @return Returns wpd mother master details.
     */
    public static WpdMotherDto convertWpdMotherMasterToWpdMotherMasterDto(WpdMotherMaster wpdMotherMaster) {
        WpdMotherDto wpdMotherDto = new WpdMotherDto();
        wpdMotherDto.setCreatedBy(wpdMotherMaster.getCreatedBy());
        wpdMotherDto.setCreatedOn(wpdMotherMaster.getCreatedOn());
        wpdMotherDto.setModifiedBy(wpdMotherMaster.getModifiedBy());
        wpdMotherDto.setModifiedOn(wpdMotherMaster.getModifiedOn());
        wpdMotherDto.setId(wpdMotherMaster.getId());
        wpdMotherDto.setFamilyId(wpdMotherMaster.getFamilyId());
        wpdMotherDto.setDateOfDelivery(wpdMotherMaster.getDateOfDelivery());
        wpdMotherDto.setMemberStatus(wpdMotherMaster.getMemberStatus());
        wpdMotherDto.setHasDeliveryHappened(wpdMotherMaster.getHasDeliveryHappened());
        wpdMotherDto.setCorticoSteroidGiven(wpdMotherMaster.getCorticoSteroidGiven());
        wpdMotherDto.setIsPretermBirth(wpdMotherMaster.getIsPretermBirth());
        wpdMotherDto.setDeliveryPlace(wpdMotherMaster.getDeliveryPlace());
        wpdMotherDto.setTypeOfHospital(wpdMotherMaster.getTypeOfHospital());
        wpdMotherDto.setDeliveryDoneBy(wpdMotherMaster.getDeliveryDoneBy());
        wpdMotherDto.setMotherAlive(wpdMotherMaster.getMotherAlive());
        wpdMotherDto.setHealthInfrastructureId(wpdMotherMaster.getHealthInfrastructureId());
        wpdMotherDto.setOtherDangerSigns(wpdMotherMaster.getOtherDangerSigns());
        wpdMotherDto.setReferralDone(wpdMotherMaster.getReferralDone());
        wpdMotherDto.setReferralPlace(wpdMotherMaster.getReferralPlace());
        wpdMotherDto.setMotherReferralInfraId(wpdMotherMaster.getReferralInfraId());
        wpdMotherDto.setIsDischarged(wpdMotherMaster.getIsDischarged());
        wpdMotherDto.setDischargeDate(wpdMotherMaster.getDischargeDate());
        wpdMotherDto.setBreastFeedingInOneHour(wpdMotherMaster.getBreastFeedingInOneHour());
        wpdMotherDto.setMtpDoneAt(wpdMotherMaster.getMtpDoneAt());
        wpdMotherDto.setMtpPerformedBy(wpdMotherMaster.getMtpPerformedBy());
        wpdMotherDto.setDeathDate(wpdMotherMaster.getDeathDate());
        wpdMotherDto.setPlaceOfDeath(wpdMotherMaster.getPlaceOfDeath());
        wpdMotherDto.setDeathReason(wpdMotherMaster.getDeathReason());
        wpdMotherDto.setIsHighRiskCase(wpdMotherMaster.getIsHighRiskCase());
        wpdMotherDto.setPregnancyRegDetId(wpdMotherMaster.getPregnancyRegDetId());
        wpdMotherDto.setPregnancyOutcome(wpdMotherMaster.getPregnancyOutcome());
        wpdMotherDto.setMisoprostolGiven(wpdMotherMaster.getMisoprostolGiven());
        wpdMotherDto.setFreeDropDelivery(wpdMotherMaster.getFreeDropDelivery());
        wpdMotherDto.setDeliveryPerson(wpdMotherMaster.getDeliveryPerson());
        wpdMotherDto.setDeliveryPersonName(wpdMotherMaster.getDeliveryPersonName());
        return wpdMotherDto;
    }

    /**
     * Convert wpd child master entity into dto.
     *
     * @param wpdChildMaster Entity of wpd child master.
     * @return Returns wpd child master details.
     */
    public static WpdChildDto convertWpdChildMasterToWpdChildMasterDto(WpdChildMaster wpdChildMaster) {
        WpdChildDto wpdChildDto = new WpdChildDto();
        wpdChildDto.setModifiedBy(wpdChildMaster.getModifiedBy());
        wpdChildDto.setModifiedOn(wpdChildMaster.getModifiedOn());
        wpdChildDto.setCreatedOn(wpdChildMaster.getCreatedOn());
        wpdChildDto.setCreatedBy(wpdChildMaster.getCreatedBy());
        wpdChildDto.setFamilyId(wpdChildMaster.getFamilyId());
        wpdChildDto.setStartDate(wpdChildMaster.getMobileStartDate());
        wpdChildDto.setEndDate(wpdChildMaster.getMobileEndDate());
        wpdChildDto.setNotificationId(wpdChildMaster.getNotificationId());
        wpdChildDto.setMemberId(wpdChildMaster.getMemberId());
        wpdChildDto.setWpdMotherId(wpdChildMaster.getWpdMotherId());
        wpdChildDto.setMotherId(wpdChildMaster.getMemberId());
        wpdChildDto.setPregnancyOutcome(wpdChildMaster.getPregnancyOutcome());
        wpdChildDto.setChildGender(wpdChildMaster.getGender());
        wpdChildDto.setChildBirthWeight(wpdChildMaster.getBirthWeight());
        wpdChildDto.setDeliveryDate(wpdChildMaster.getDateOfDelivery());
        wpdChildDto.setChildOtherDangerSign(wpdChildMaster.getOtherDangerSign());
        wpdChildDto.setChildCry(wpdChildMaster.getBabyCriedAtBirth());
        wpdChildDto.setBreastFeeding(wpdChildMaster.getBreastFeedingInOneHour());
        wpdChildDto.setMemberStatus(wpdChildMaster.getMemberStatus());
        wpdChildDto.setWasPremature(wpdChildMaster.getWasPremature());
        wpdChildDto.setChildReferralReason(wpdChildMaster.getReferralReason());
        wpdChildDto.setChildReferralTransport(wpdChildMaster.getReferralTransport());
        wpdChildDto.setChildReferralPlace(wpdChildMaster.getReferralPlace());
        wpdChildDto.setChildReferralInfraId(wpdChildMaster.getReferralInfraId());
        wpdChildDto.setName(wpdChildMaster.getName());
        wpdChildDto.setBreastCrawl(wpdChildMaster.getBreastCrawl());
        wpdChildDto.setKangarooCare(wpdChildMaster.getKangarooCare());
        wpdChildDto.setTypeOfDelivery(wpdChildMaster.getTypeOfDelivery());
        wpdChildDto.setDeathDate(wpdChildMaster.getDeathDate());
        wpdChildDto.setDeathReason(wpdChildMaster.getDeathReason());
        wpdChildDto.setIsHighRiskCase(wpdChildMaster.getIsHighRiskCase());
        wpdChildDto.setPlaceOfDeath(wpdChildMaster.getPlaceOfDeath());
        wpdChildDto.setCreatedOn(wpdChildDto.getCreatedOn());
        wpdChildDto.setModifiedOn(wpdChildDto.getModifiedOn());
        wpdChildDto.setCreatedBy(wpdChildMaster.getCreatedBy());
        wpdChildDto.setModifiedBy(wpdChildMaster.getModifiedBy());

        return wpdChildDto;
    }
}
