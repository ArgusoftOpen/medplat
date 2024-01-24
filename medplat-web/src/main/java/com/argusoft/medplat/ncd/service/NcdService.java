/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.mobile.dto.UploadFileDataBean;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.dto.RecordStatusBean;
import com.argusoft.medplat.ncd.dto.*;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.enums.SubStatus;
import com.argusoft.medplat.ncd.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 *     Define all services for ncd.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
public interface NcdService {

    void updateFamilyAdditionalInfo(FamilyEntity familyEntity, Date screeningDate);

    /**
     * Store CBAC form details.
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap Contains key and answers.
     * @param user User details.
     * @return Returns id of store details.
     */
    Integer storeCbacForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
    /**
     * Store mental health form details.
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswerMap Contains key and answers.
     * @param user User details.
     * @return Returns id of store details.
     */
    Map<String, String> storeHealthScreeningForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeMoConfirmedForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeUrineTestForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeRetinopathyTestForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeEcgDetails(MemberEcgDto ecgDto);

    Integer storeEcgToken(MemberEcgTokenDto memberEcgTokenDto, ParsedRecordBean parsedRecordBean, UserMaster user);

    Integer storePersonalHistoryForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeWeeklyHomeForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeWeeklyClinicForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeCvcHomeForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    Integer storeCvcClinicForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    MemberMoConfirmedDetailDataBean retrieveMoConfirmedDetail(MemberMoConfirmedDetailDataBean moConfirmedDetailDataBean);

    /**
     * Retrieves ncd member details by member id.
     * @param memberId Member id.
     * @return Returns ncd member details.
     */
    MemberDetailDto retrieveMemberDetail(Integer memberId);

    /**
     * Save ncd member details.
     * @param member Ncd member details.
     */
    void saveMemberDetails(MemberDetailDto member);

    /**
     * Store dell API response details.
     * @param requestStartDate Start date of request.
     * @param requestEndDate End date of request.
     * @param locationName Location name.
     * @param locationId Location id.
     * @param response Response details.
     * @param enrolled Enrolled.
     */
    void storeDellApiResponseDetails(Date requestStartDate, Date requestEndDate, String locationName, Integer locationId, String response, Integer enrolled);

    /**
     * Retrieves all members of ncd.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @param healthInfrastructureType Type of health infrastructure.
     * @param searchString Search text.
     * @param searchBy Search by like member id, family id, Mobile number etc.
     * @return Returns list of members details by defined params.
     */
    List<MemberDetailDto> retrieveMembers(Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString);

    List<MemberDetailDto> retrieveMembersForCFS(Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString);
    /**
     * Save complaint details.
     * @param complaintsDto Complaint details.
     */
    void saveComplaints(MemberComplaintsDto complaintsDto);

    /**
     * Retrieves complaint by member id.
     * @param memberId Member id.
     * @return Returns list of complaint details by member id.
     */
    List<MemberComplaintsDto> retrieveComplaintsByMemberId(Integer memberId);

    /**
     * Retrieves all medicine details.
     * @return Returns list of medicine details.
     */
    List<MedicineMaster> retrieveAllMedicines();

    /**
     * Save diagnosis details.
     * @param diagnosisDto Member diagnosis details.
     */
    void saveDiagnosis(MemberDiagnosisDto diagnosisDto);

    /**
     * Retrieves treatment history by member id.
     * @param memberId Member id.
     * @param diseaseCode Disease code like HT(hypertension),D(diabetes) etc.
     * @return Returns list of member treatment history.
     */
    List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(Integer memberId, String diseaseCode);

    /**
     * Save referral details.
     * @param memberReferralDto Referral details of member.
     */
    void saveReferral(MemberReferralDto memberReferralDto);

    /**
     * Save followup details.
     * @param memberDiseaseFollowupDto Follow up details of member.
     */
    void saveFollowUp(MemberDiseaseFollowupDto memberDiseaseFollowupDto);

    /**
     * Retrieves all follow up details of member.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @param healthInfrastructureType Type of health infrastructure.
     * @param status Define status like pending, completed.
     * @return Returns list of members based on define params.
     */
    List<MemberDetailDto> retrieveMembersForFollowup(Integer limit, Integer offset, String healthInfrastructureType, String[] status);

    /**
     * Retrieves referral for today for particular member.
     * @param memberId Member id.
     * @return Returns list of member referral.
     */
    List<MemberReferralDto> retrieveReffForToday(Integer memberId);

    /**
     * Retrieves next follow up details for particular member.
     * @param memberId Member id.
     * @return Returns list of next follow up details.
     */
    List<MemberDiseaseFollowupDto> retrieveNextFollowUp(Integer memberId);

    List<GeneralDetailMedicineDto> retrievePrescribedMedicineForUser(Integer memberId);

    void createMedicineDetails(List<GeneralDetailMedicineDto> medicineDetail, Integer memberId, Date screeningDate, Integer generalId, MemberDiseaseDiagnosis.DiseaseCode g, Integer healthInfraId);

    Integer createMasterRecord(Integer memberId, Integer locationId, DiseaseCode diseaseCode, Boolean flag, Date visitDate, Boolean suspected);

    Integer updateNcdMasterStatus(Integer memberId, DiseaseCode diseaseCode, Status status, Integer healthInfraId, Date visitDate);

    Integer updateNcdMasterSubStatus(Integer memberId, String diseaseCode, SubStatus subStatus, Boolean resetFlag, Date visitDate);

    List<MemberDetailDto> retrieveAllMembers(String type, Integer limit, Integer offset);

    List<MemberDetailDto> retreiveSearchedMembers(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag, Boolean review);

    MemberInvestigationDetail saveInvestigation(MemberInvestigationDto memberInvestigationDto);

    void createVisitHistory(Integer masterId, Integer memberId, Date visitDate, DoneBy doneBy, Integer referenceId, String status, DiseaseCode diseaseCode, Boolean flag, String reading);

    List<MemberInvestigationDetail> retreiveInvestigationDetailByMemberId(Integer memberId);

    List<GeneralDetailMedicineDto> retrievePrescribedMedicineHistoryForUser(Integer memberId);

    MemberMasterDto retrieveMemberDetailMaster(Integer memberId);
    
    String retrieveMemberStatusForNotification(Integer memberId, String diseaseCode, Status status);

    void handleEditMedicineDetails(List<GeneralDetailMedicineDto> medicineDetail, Integer memberId, Date screeningDate, Integer hypertensionId, MemberDiseaseDiagnosis.DiseaseCode ht, Integer healthInfraId);

    void handleDeleteMedicineDetails(List<GeneralDetailMedicineDto> deletedMedicineDetail, Integer memberId, Integer healthInfraId);

    RecordStatusBean storeMediaData(DocumentDto documentDto, UploadFileDataBean uploadFileDataBean);

//    public RecordStatusBean storeMediaData(DocumentDto documentDto, UploadFileDataBean uploadFileDataBean);

}
