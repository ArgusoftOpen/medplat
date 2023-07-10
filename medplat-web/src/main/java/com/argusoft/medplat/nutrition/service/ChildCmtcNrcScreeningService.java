
package com.argusoft.medplat.nutrition.service;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.nutrition.dto.*;
import com.argusoft.medplat.rch.dto.ChildServiceMasterDto;
import com.argusoft.medplat.web.location.dto.LocationMasterDto;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *     Defines methods of screening for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC)
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcScreeningService {

    /**
     * Creates child cmtc nrc admission
     * @param childCmtcNrcAdmission An instance of ChildCmtcNrcAdmissionDto
     */
    void createChildCmtcNrcAdmission(ChildCmtcNrcAdmissionDto childCmtcNrcAdmission);

    /**
     * Returns a list of child cmtc nrc screening details based on given limit offset parameter
     * @param user An instance of ImtechoSecurityUser
     * @param limit A limit value
     * @param offset An offset value
     * @return A list of ChildCmtcNrcScreeningDto
     */
    List<ChildCmtcNrcScreeningDto> retrieveAllScreenedChildren(ImtechoSecurityUser user, Integer limit, Integer offset);

    /**
     * Returns a list of admitted child details based on given limit offset parameter
     * @param user An instance of ImtechoSecurityUser
     * @param limit A limit value
     * @param offset An offset value
     * @return A list of ChildCmtcNrcScreeningDto
     */
    List<ChildCmtcNrcScreeningDto> retrieveAllAdmittedChildren(ImtechoSecurityUser user, Integer limit, Integer offset);

    /**
     * Returns a list of default child details based on given limit offset parameter
     * @param user An instance of ImtechoSecurityUser
     * @param limit A limit value
     * @param offset An offset value
     * @return A list of ChildCmtcNrcScreeningDto
     */
    List<ChildCmtcNrcScreeningDto> retrieveAllDefaulterChildren(ImtechoSecurityUser user, Integer limit, Integer offset);

    /**
     * Returns a list of discharged child details based on given limit offset parameter
     * @param user An instance of ImtechoSecurityUser
     * @param limit A limit value
     * @param offset An offset value
     * @return A list of ChildCmtcNrcScreeningDto
     */
    List<ChildCmtcNrcScreeningDto> retrieveAllDischargedChildren(ImtechoSecurityUser user, Integer limit, Integer offset);

    /**
     * Updates a weight of given list of child
     * @param childCmtcNrcWeight A list of ChildCmtcNrcWeightDto
     */
    void createChildCmtcNrcWeightEntry(List<ChildCmtcNrcWeightDto> childCmtcNrcWeight);

    /**
     * Returns a list of child weight info based on given admission id
     * @param admissionId An id of admission
     * @return A list of ChildCmtcNrcWeightDto
     */
    List<ChildCmtcNrcWeightDto> getCmtcNrcWeighDtosFromAdmissionId(Integer admissionId);

    /**
     * Returns child cmtc nrc details based on given admission id
     * @param id An id of admission
     * @return An instance of ChildCmtcNrcScreeningDto
     */
    ChildCmtcNrcAdmissionDto retrieveAdmissionDetailById(Integer id);

    /**
     * Returns a details of child sd score based on given gender, height, weight
     * @param gender A gender of child
     * @param height A height of child
     * @param weight A weight of child
     * @return An instance of ChildCmtcNrcSdScoreDto
     */
    ChildCmtcNrcSdScoreDto getSdScore(String gender, Integer height, Float weight);

    /**
     * Saves death details of given child
     * @param admissionDto An instance of ChildCmtcNrcAdmissionDto
     * @param user An instance of ImtechoSecurityUser
     */
    void saveDeathDetails(ChildCmtcNrcAdmissionDto admissionDto, ImtechoSecurityUser user);

    /**
     * Saves discharge details of given child
     * @param dischargeDto An instance of ChildCmtcNrcDischargeDto
     */
    void saveDischargeDetails(ChildCmtcNrcDischargeDto dischargeDto);

    /**
     * Saves default details of given child
     * @param admissionDto An instance of ChildCmtcNrcAdmissionDto
     */
    void saveDefaulterDetails(ChildCmtcNrcAdmissionDto admissionDto);

    /**
     * Saves laboratory test details of given child
     * @param childCmtcNrcLaboratoryDto An instance of ChildCmtcNrcLaboratoryDto
     */
    void saveLaboratoryTests(ChildCmtcNrcLaboratoryDto childCmtcNrcLaboratoryDto);

    /**
     * Saves medicines details of given child
     * @param childCmtcNrcWeightDto An instance of ChildCmtcNrcWeightDto
     */
    void saveMedicines(ChildCmtcNrcWeightDto childCmtcNrcWeightDto);

    /**
     * Saves follow up details of given child
     * @param childCmtcNrcFollowUpDto An instance of ChildCmtcNrcFollowUpDto
     */
    void saveFollowUp(ChildCmtcNrcFollowUpDto childCmtcNrcFollowUpDto);

    /**
     * Returns a last follow up visit details of given child
     * @param childId An id of child
     * @param admissionId An id of admission
     * @return An instance of ChildCmtcNrcFollowUpDto
     */
    ChildCmtcNrcFollowUpDto getLastFollowUpVisit(Integer childId, Integer admissionId);

    /**
     * Returns a discharge details of given discharge id
     * @param dischargeId A discharge id
     * @return An instance of ChildCmtcNrcDischargeDto
     */
    ChildCmtcNrcDischargeDto getDischargeDetails(Integer dischargeId);

    /**
     * Returns a rch child service details of given child
     * @param childId An id of child
     * @return An instance of ChildServiceMasterDto
     */
    ChildServiceMasterDto retrieveRchChildServiceDetailsByMemberId(Integer childId);

    /**
     * Returns a list of location assigned to given user
     * @param userId An id of user
     * @return A list of LocationMasterDto
     */
    List<LocationMasterDto> getBlocksAssignedForMoVerification(Integer userId);

    /**
     * Creates child screening details
     * @param childCmtcNrcScreeningDto An instance of ChildCmtcNrcScreeningDto
     */
    void createChildScreening(ChildCmtcNrcScreeningDto childCmtcNrcScreeningDto);

    /**
     * Returns a map of admission type for given child id
     * @param childId An id of child
     * @param admissionDate An admission date
     * @return A map of admission type
     * @throws ParseException Signals that an error has been occurred during parsing
     */
    String checkAdmissionIndicator(Integer childId, String admissionDate) throws ParseException;

    /**
     * Returns a list of phone number of given asha member
     * @param memberId An id of member
     * @return A list of phone number
     */
    List<String> retrieveAshaPhoneNumber(Integer memberId);

    /**
     * Returns a last child laboratory test details based on given admission id
     * @param admissionId An id of admission
     * @return An instance of ChildCmtcNrcLaboratoryDto
     */
    ChildCmtcNrcLaboratoryDto retrieveLastLaboratoryTest(Integer admissionId);

    /**
     * Returns a list of child laboratory info based on given admission id
     * @param admissionId An id of admission
     * @return A list of ChildCmtcNrcLaboratoryDto
     */
    List<ChildCmtcNrcLaboratoryDto> getCmtcNrcLaboratoryDtosFromAdmissionId(Integer admissionId);

    /**
     * Creates mo verification
     * @param childCmtcNrcMoVerificationDto An instance of ChildCmtcNrcMoVerificationDto
     */
    void createMoVerification(ChildCmtcNrcMoVerificationDto childCmtcNrcMoVerificationDto);

    /**
     * Returns cmtc nrc screening details of given child id
     * @param childId An id of child
     * @return An instance of ChildCmtcNrcScreeningDto
     */
    ChildCmtcNrcScreeningDto retrieveScreeningDetails(Integer childId);

    /**
     * Deletes child screening details of given child id
     * @param childId An id of child
     */
    void deleteChildScreeningByChildId(Integer childId);

    /**
     * Checks admission status of given child
     * @param childId An id of child
     */
    void checkAdmissionValidity(Integer childId);

    /**
     * Returns a list of referred child details based on given limit offset parameter
     * @param securityUser An instance of ImtechoSecurityUser
     * @param limit A limit value
     * @param offset An offset value
     * @return A list of ChildCmtcNrcScreeningDto
     */
    List<ChildCmtcNrcScreeningDto> retrieveAllReferredChildren(ImtechoSecurityUser securityUser, Integer limit, Integer offset);

    /**
     * Returns a list of discharged child details based on given limit offset parameter
     * @param securityUser An instance of ImtechoSecurityUser
     * @param limit A limit value
     * @param offset An offset value
     * @return A list of ChildCmtcNrcScreeningDto
     */
    List<ChildCmtcNrcScreeningDto> retrieveTreatmentCompletedChildren(ImtechoSecurityUser securityUser, Integer limit, Integer offset);
}
