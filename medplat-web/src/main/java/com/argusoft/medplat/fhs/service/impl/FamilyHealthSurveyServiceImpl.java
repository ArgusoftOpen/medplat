/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.service.impl;

import com.argusoft.medplat.cfhc.dao.MemberCFHCDao;
import com.argusoft.medplat.cfhc.model.MemberCFHCEntity;
import com.argusoft.medplat.common.service.SequenceService;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.*;
import com.argusoft.medplat.fhs.mapper.MemberMapper;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.FamilyDataBean;
import com.argusoft.medplat.mobile.dto.MemberDataBean;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.mapper.FamilyDataBeanMapper;
import com.argusoft.medplat.mobile.mapper.MemberDataBeanMapper;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.rch.dao.ChildServiceDao;
import com.argusoft.medplat.rch.dto.ChildServiceMasterDto;
import com.argusoft.medplat.rch.dto.PncMotherDto;
import com.argusoft.medplat.rch.dto.WpdChildDto;
import com.argusoft.medplat.rch.dto.WpdMotherDto;
import com.argusoft.medplat.rch.mapper.ChildServiceMapper;
import com.argusoft.medplat.rch.model.ChildServiceMaster;
import com.argusoft.medplat.rch.dto.PncChildDto;
import com.argusoft.medplat.rch.service.PncService;
import com.argusoft.medplat.rch.service.WpdService;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author harsh
 */
@Service
@Transactional
public class FamilyHealthSurveyServiceImpl implements FamilyHealthSurveyService {
    public static final String STATE_VERIFIED = "VERIFIED";
    public static final String STATE_REVERIFICATION = "REVERIFICATION";
    public static final String CODE_FOR_DEATH = "mark_member_as_death";
    public static final String NOT_AVAILABLE = "Not available";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    public static final String MEMBER_ID = "member_id";
    public static final String HEALTH_INFRA_ID = "health_infra_id";
    public static final String LOCATION_ID = "location_id";
    public static final String ACTION_BY = "action_by";
    public static final String MEMBER_LIST = "MEMBERLIST";
    public static final String CHECK_WITH = "CHECKWITH";
    public static final String FAMILY_TO_BE_ASSIGNED = "Family to be assigned";
    public static final String FAMILY_ID = "family_id";
    public static final String DEATH_DATE = "death_date";
    public static final String PLACE_OF_DEATH = "place_of_death";
    public static final String DEATH_REASON = "death_reason";
    public static final String OTHER_DEATH_REASON = "other_death_reason";
    public static final String SERVICE_TYPE = "service_type";
    public static final String REFERENCE_ID = "reference_id";

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private MemberDao memberDao;

//    @Autowired
//    private FamilyStateDetailDao familyStateDetailDao;

//    @Autowired
//    private MemberStateDetailDao memberStateDetailDao;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private UserLocationDao userLocationDao;

    @Autowired
    private QueryMasterService queryMasterService;

    @Autowired
    @Lazy
    private WpdService wpdService;

    @Autowired
    @Lazy
    private PncService pncService;

    @Autowired
    private ChildServiceDao childServiceDao;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private ImtechoSecurityUser user;

//    @Autowired
//    MyTechoUserDao myTechoUserDao;

    @Autowired
    private MemberCFHCDao memberCFHCDao;

//    @Autowired
//    @Lazy
//    private HealthIdCommonUtilService healthIdCommonUtilService;

//    @Autowired
//    private AadhaarVaultCommonUtilService aadhaarVaultCommonUtilService;

    @Autowired
    private MobileUtilService mobileUtilService;


//    @Autowired
//    @Lazy
//    private OfflineAbhaNumberService offlineAbhaNumberService;

    @Autowired
    HttpServletRequest httpServletRequest;

//    @Override
//    public void updateFamily(FamilyDto familyDto, String fromState, String toState) {
//        if (toState != null) {
//            familyDto.setState(toState);
//        }
//        FamilyEntity familyEntity = null;
//        if (familyDto.getId() != null) {
//            familyEntity = familyDao.retrieveById(familyDto.getId());
//        }
//        familyDao.update(FamilyMapper.getFamilyEntity(familyDto, familyEntity));
//    }

    @Override
    public void updateFamily(FamilyEntity family, String fromState, String toState) {
        if (toState != null) {
            family.setState(toState);
        }
        familyDao.update(family);
    }

    @Override
    public void persistFamilyCFHC(String syncStatusId, FamilyEntity familyEntity, Set<MemberEntity> membersToAdd, Set<MemberEntity> membersToArchive, Set<MemberEntity> membersToUpdate, String principleId, Set<MemberEntity> membersToBeMarkedDead, Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey, String motherChildRelationString, String husbandWifeRelationshipString,
                                  Map<Integer, String> failedAbhaCreationMessageMap) {
        List<MemberEntity> persistedMembers = new ArrayList<>(membersToUpdate);
        Map<String, UUID> mapOfAadhaarReferenceKey = new HashMap<String, UUID>();

        if (familyEntity.getId() == null) {
            familyEntity.setCreatedOn(new Date());
            familyEntity.setState(FamilyHealthSurveyServiceConstants.CFHC_FAMILY_STATE_NEW);
            familyEntity.setIsVerifiedFlag(Boolean.TRUE);
            updateAdditionalInfo(familyEntity);
            familyEntity.setId(familyDao.create(familyEntity));
        } else {
            familyEntity.setModifiedBy(Integer.valueOf(principleId));
            familyEntity.setModifiedOn(new Date());
            familyEntity.setIsVerifiedFlag(Boolean.TRUE);

            switch (familyEntity.getState()) {
                case FamilyHealthSurveyServiceConstants.CFHC_FAMILY_STATE_IN_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.CFHC_FAMILY_STATE_MO_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.CFHC_FAMILY_STATE_GVK_REVERIFIED);
                    break;
                default:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.CFHC_FAMILY_STATE_VERIFIED);
                    break;
            }

            updateAdditionalInfo(familyEntity);
            familyDao.update(familyEntity);
        }
        familyDao.flush();

        for (MemberEntity memberEntity : membersToAdd) {
            memberEntity.setCreatedBy(Integer.valueOf(principleId));
            memberEntity.setCreatedOn(new Date());
            memberEntity.setState(FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_NEW);

//            if (memberEntity.getAadharNumber() != null) {
//                UUID referenceKey = aadhaarVaultCommonUtilService.saveAadhaarNumber(memberEntity.getAadharNumber());
//                memberEntity.setAadhaarReferenceKey(referenceKey);
//                memberEntity.setAadharNumberAvailable(true);
//                mapOfAadhaarReferenceKey.put(memberEntity.getAadharNumber(), referenceKey);
//                memberEntity.setAadharNumber(null);
//            }

            String offlineHealthIDData = null;
            //Remove json from offline health id
            if (memberEntity.getNdhmHealthId() != null && memberEntity.getNdhmHealthId().startsWith("{")) {
                offlineHealthIDData = memberEntity.getNdhmHealthId();
                memberEntity.setNdhmHealthId(null);
            }

            MemberEntity createdMember = memberDao.createMember(memberEntity);

            //Online created health id update into the health id User Detail with member id
//            if (memberEntity.getNdhmHealthId() != null && !memberEntity.getNdhmHealthId().isEmpty()) {
//                HealthIdResponseDto responseDto =  healthIdCommonUtilService.setMemberId(memberEntity.getNdhmHealthId(), memberEntity.getId());
//                if (responseDto.getStatus().equals(HealthIdResponseDto.Status.FAILED)) {
//                    failedAbhaCreationMessageMap.put(createdMember.getId(), "Failed to create ABHA number(" + responseDto.getMessage() + ")");
//                }
//            }

            //Offline health id create and save health id card into the health id User Detail
//            if (offlineHealthIDData != null) {
//                OfflineHealthIdEntity offlineHealthIdEntity = null;
//                try {
//                    offlineHealthIdEntity = new Gson().fromJson(offlineHealthIDData, OfflineHealthIdEntity.class);
//                    OfflineHealthIdResponseDto responseDto = offlineAbhaNumberService.offlineAbhaCreation(offlineHealthIdEntity, createdMember, Integer.parseInt(principleId), MobileConstantUtil.CFHC);
//                    if (responseDto.getStatus().equals(OfflineHealthIdResponseDto.Status.FAILED)) {
//                        failedAbhaCreationMessageMap.put(createdMember.getId(), "Failed to create ABHA number(" + responseDto.getMessage() + ")");
//                    }
//                } catch (ImtechoUserException e) {
//                    e.printStackTrace();
//                }
//            }

            persistedMembers.add(createdMember);
            insertDiseasesInformationForMember(memberEntity);
        }

        for (MemberEntity memberEntity : membersToUpdate) {
            memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
            memberEntity.setModifiedBy(Integer.valueOf(principleId));
            memberEntity.setModifiedOn(new Date());

            if (memberEntity.getAgreedToShareAadhar() != null && !memberEntity.getAgreedToShareAadhar()) {
                memberEntity.setAadharNumber(null);
                memberEntity.setAadharNumberAvailable(null);
            }

//            if (memberEntity.getAadharNumber() != null) {
//                UUID referenceKey = aadhaarVaultCommonUtilService.saveAadhaarNumber(memberEntity.getAadharNumber());
//                memberEntity.setAadhaarReferenceKey(referenceKey);
//                memberEntity.setAadharNumberAvailable(true);
//                mapOfAadhaarReferenceKey.put(memberEntity.getAadharNumber(), referenceKey);
//                memberEntity.setAadharNumber(null);
//            }

            this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_VERIFIED);
            insertDiseasesInformationForMember(memberEntity);
        }

        for (MemberEntity memberEntity : membersToBeMarkedDead) {
            memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
            memberEntity.setModifiedBy(Integer.valueOf(principleId));
            memberEntity.setModifiedOn(new Date());
            memberEntity.setFamilyHeadFlag(Boolean.FALSE);
            this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_DEAD);
        }

        if (motherChildRelationString != null) {
            Gson gson = new Gson();
            Map<String, String> motherChildRelationMap = gson.fromJson(motherChildRelationString, new TypeToken<HashMap<String, String>>() {
            }.getType());

            for (Map.Entry<String, String> entry : motherChildRelationMap.entrySet()) {
                String child = entry.getKey();
                String mother = entry.getValue();

                if (!mother.equals("-1")) {
                    MemberEntity childEntity = mapOfMemberWithLoopIdAsKey.get(child);
                    MemberEntity motherEntity = mapOfMemberWithLoopIdAsKey.get(mother);
                    if (childEntity != null && motherEntity != null) {
                        childEntity.setMotherId(motherEntity.getId());
                        memberDao.update(childEntity);
                    }
                }
            }
        }

        if (husbandWifeRelationshipString != null) {
            Gson gson = new Gson();
            Map<String, String> husbandWifeRelationMap = gson.fromJson(husbandWifeRelationshipString, new TypeToken<HashMap<String, String>>() {
            }.getType());

            for (Map.Entry<String, String> entry : husbandWifeRelationMap.entrySet()) {
                String wife = entry.getKey();
                String husband = entry.getValue();

                if (!husband.equals("-1")) {
                    MemberEntity wifeEntity = mapOfMemberWithLoopIdAsKey.get(wife);
                    MemberEntity husbandEntity = mapOfMemberWithLoopIdAsKey.get(husband);
                    if (wifeEntity != null && husbandEntity != null) {
                        wifeEntity.setHusbandId(husbandEntity.getId());
                        memberDao.update(wifeEntity);
                    }
                }
            }
        }
        if (mapOfAadhaarReferenceKey.size() > 0) {
            SyncStatus status = mobileUtilService.retrieveSyncStatusById(syncStatusId);
            for (Map.Entry<String, UUID> entry : mapOfAadhaarReferenceKey.entrySet()) {
                status.setRecordString(status.getRecordString().replace(entry.getKey(), entry.getValue().toString()));
            }
            mobileUtilService.updateSyncStatus(status);
        }

        Integer contactPersonId = getContactPersonIdForFamilyEntity(persistedMembers);
        Integer headOfTheFamilyId = getHOFIdForFamilyEntity(persistedMembers);
        this.updateSickleCellTestUpdate(persistedMembers);

        familyEntity.setContactPersonId(contactPersonId);
        familyEntity.setHeadOfFamily(headOfTheFamilyId);
        familyDao.updateFamily(familyEntity);
        familyDao.flush();
        memberDao.flush();

        for (MemberEntity memberEntity : membersToUpdate) {
            MemberCFHCEntity memberCFHCEntity = memberEntity.getMemberCFHCEntity();
            memberCFHCEntity.setFamilyId(familyEntity.getId());
            memberCFHCEntity.setMemberId(memberEntity.getId());
            memberCFHCDao.createOrUpdate(memberCFHCEntity);
        }

        for (MemberEntity memberEntity : membersToAdd) {
            MemberCFHCEntity memberCFHCEntity = memberEntity.getMemberCFHCEntity();
            memberCFHCEntity.setFamilyId(familyEntity.getId());
            memberCFHCEntity.setMemberId(memberEntity.getId());
            memberCFHCDao.createOrUpdate(memberCFHCEntity);
        }

        membersToAdd.forEach(memberEntity ->
                eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHS_NEW_MEMBER, memberEntity.getId()))
        );

    }

    private void insertDiseasesInformationForMember(MemberEntity memberEntity) {
        Set<Integer> diseaseIds = null;
        String diseaseType = null;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    diseaseIds = memberEntity.getChronicDiseaseDetails();
                    diseaseType = "CHRONIC";
                    break;
                case 1:
                    diseaseIds = memberEntity.getCongenitalAnomalyDetails();
                    diseaseType = "CONGENITAL";
                    break;
                case 2:
                    diseaseIds = memberEntity.getCurrentDiseaseDetails();
                    diseaseType = "CURRENT";
                    break;
                case 3:
                    diseaseIds = memberEntity.getEyeIssueDetails();
                    diseaseType = "EYE";
                    break;
                default:
            }
            if (!CollectionUtils.isEmpty(diseaseIds))
                memberDao.insertDiseaseRelationsOfMember(memberEntity.getId(), diseaseIds, diseaseType);
        }
    }

    @Override
    public void persistFamily(FamilyEntity familyEntity, Set<MemberEntity> membersToAdd, Set<MemberEntity> membersToArchive,
                              Set<MemberEntity> membersToUpdate, String principleId, Set<MemberEntity> membersToBeMarkedDead,
                              Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey, String motherChildRelationString,
                              String husbandWifeRelationshipString) {
        List<MemberEntity> persistedMembers = new ArrayList<>(membersToUpdate);

        if (familyEntity.getId() == null) {
            familyEntity.setCreatedOn(new Date());
            familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW);
            familyEntity.setIsVerifiedFlag(Boolean.TRUE);
            FamilyAdditionalInfo additionalInfo = new FamilyAdditionalInfo();
            additionalInfo.setLastFhsDate((new Date()).getTime());
            familyEntity.setAdditionalInfo((new Gson()).toJson(additionalInfo));
            familyEntity.setId(familyDao.create(familyEntity));
            familyDao.flush();
        }

        for (MemberEntity memberEntity : membersToAdd) {
            memberEntity.setCreatedBy(Integer.valueOf(principleId));
            memberEntity.setCreatedOn(new Date());
            memberEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW);

//            if (memberEntity.getAgreedToShareAadhar() != null && !memberEntity.getAgreedToShareAadhar() && memberEntity.getAadharNumber() != null) {
//                SaveAadhaarRequest request = new SaveAadhaarRequest(memberEntity.getAadharNumber());
//                ReferenceKeyResponse response = aadhaarVaultService.saveAadhaarNumber(request);
//                memberEntity.setAadhaarReferenceKey(response.getReferenceKey());
//                memberEntity.setAadharNumberAvailable(true);
//            }
//            memberEntity.setAadharNumber(null);
            MemberEntity createdMember = memberDao.createMember(memberEntity);
            persistedMembers.add(createdMember);
        }

        for (MemberEntity memberEntity : membersToUpdate) {
            memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
            memberEntity.setModifiedBy(Integer.valueOf(principleId));
            memberEntity.setModifiedOn(new Date());

            if (memberEntity.getAgreedToShareAadhar() != null && !memberEntity.getAgreedToShareAadhar()) {
                memberEntity.setAadharNumber(null);
                memberEntity.setAadharNumberAvailable(null);
            }

            switch (memberEntity.getState()) {
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ORPHAN:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_VERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_MO_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW_FHW_REVERIFIED);
                    break;
                default:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_VERIFIED);
                    break;
            }
        }

        for (MemberEntity memberEntity : membersToArchive) {
            memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
            memberEntity.setModifiedBy(Integer.valueOf(principleId));
            memberEntity.setModifiedOn(new Date());
            memberEntity.setFamilyHeadFlag(Boolean.FALSE);
            switch (memberEntity.getState()) {
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_VERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW_FHW_REVERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ORPHAN:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_MO_FHW_REVERIFIED);
                    break;
                default:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED);
                    break;
            }
        }

        for (MemberEntity memberEntity : membersToBeMarkedDead) {
            memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
            memberEntity.setModifiedBy(Integer.valueOf(principleId));
            memberEntity.setModifiedOn(new Date());
            memberEntity.setFamilyHeadFlag(Boolean.FALSE);
            switch (memberEntity.getState()) {
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_VERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW_FHW_REVERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ORPHAN:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION:
                case FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_MO_FHW_REVERIFIED);
                    break;
                default:
                    this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD);
                    break;
            }
        }

        if (motherChildRelationString != null) {
            Gson gson = new Gson();
            Map<String, String> motherChildRelationMap = gson.fromJson(motherChildRelationString, new TypeToken<HashMap<String, String>>() {
            }.getType());

            for (Map.Entry<String, String> entry : motherChildRelationMap.entrySet()) {
                String child = entry.getKey();
                String mother = entry.getValue();

                if (!mother.equals("-1")) {
                    MemberEntity childEntity = mapOfMemberWithLoopIdAsKey.get(child);
                    MemberEntity motherEntity = mapOfMemberWithLoopIdAsKey.get(mother);
                    if (childEntity != null && motherEntity != null) {
                        childEntity.setMotherId(motherEntity.getId());
                        memberDao.update(childEntity);
                    }
                }
            }
        }

        if (husbandWifeRelationshipString != null) {
            Gson gson = new Gson();
            Map<String, String> husbandWifeRelationMap = gson.fromJson(husbandWifeRelationshipString, new TypeToken<HashMap<String, String>>() {
            }.getType());

            for (Map.Entry<String, String> entry : husbandWifeRelationMap.entrySet()) {
                String wife = entry.getKey();
                String husband = entry.getValue();

                if (!husband.equals("-1")) {
                    MemberEntity wifeEntity = mapOfMemberWithLoopIdAsKey.get(wife);
                    MemberEntity husbandEntity = mapOfMemberWithLoopIdAsKey.get(husband);
                    if (wifeEntity != null && husbandEntity != null) {
                        wifeEntity.setHusbandId(husbandEntity.getId());
                        memberDao.update(wifeEntity);
                    }
                }
            }
        }

        Integer contactPersonId = getContactPersonIdForFamilyEntity(persistedMembers);
        Integer headOfTheFamilyId = getHOFIdForFamilyEntity(persistedMembers);
        this.updateSickleCellTestUpdate(persistedMembers);

        familyEntity.setContactPersonId(contactPersonId);
        familyEntity.setHeadOfFamily(headOfTheFamilyId);
        if (familyEntity.getId() != null) {
            familyEntity.setModifiedBy(Integer.valueOf(principleId));
            familyEntity.setModifiedOn(new Date());
            familyEntity.setState(createEntryForFamilyStateDetailAndGetStateAccordingToLastState(familyEntity));
            familyEntity.setIsVerifiedFlag(Boolean.TRUE);
            if (familyEntity.getAdditionalInfo() != null) {
                updateAdditionalInfo(familyEntity);
            }
            familyDao.update(familyEntity);
        }
        memberDao.flush();
        membersToAdd.forEach(memberEntity ->
                eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHS_NEW_MEMBER, memberEntity.getId()))
        );
    }

    private void updateAdditionalInfo(FamilyEntity familEntity) {
        Gson gson = new Gson();
        FamilyAdditionalInfo additionalInfo;
        if (familEntity.getAdditionalInfo() != null) {
            additionalInfo = gson.fromJson(familEntity.getAdditionalInfo(), FamilyAdditionalInfo.class);
        } else {
            additionalInfo = new FamilyAdditionalInfo();
        }
        additionalInfo.setLastFhsDate(new Date().getTime());
        familEntity.setAdditionalInfo(gson.toJson(additionalInfo));
    }

//    @Override
//    public void updateMember(MemberDto memberDto, String fromState, String toState) {
//        if (toState != null) {
//            memberDto.setState(toState);
//        }
//        MemberEntity memberEntityDefault = memberDao.retrieveById(memberDto.getId());
//        memberDao.update(MemberMapper.getMemberEntity(memberDto, memberEntityDefault));
//    }

    @Override
    public void updateMember(MemberEntity memberEntity, String fromState, String toState) {
        if (toState != null && !memberEntity.getState().equals(toState)) {
            memberEntity.setState(toState);
        }
        memberDao.update(memberEntity);
    }

//    @Override
//    public List<FamilyEntity> getOrphanedOrReverificationFamiliesAssignedToUser(List<Integer> locationIds) {
//        List<String> projections = new ArrayList<>();
//        List<String> familyIdsFromMemberQuery = new ArrayList<>();
//        Set<FamilyEntity> finalResult = new HashSet<>();
//        List<String> states = new ArrayList<>();
//        Map<String, FamilyEntity> mapOfFamilyWithFamilyIdAsKey = new HashMap<>();
//        Map<String, FamilyEntity> mapOfFamilyWithFamilyIdAsKeyForMembers = new HashMap<>();
//
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_MO_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED);
//        //Families with states orphan or reverification
//        List<FamilyEntity> familiesForReverification = familyDao.getFamilies(null, null, locationIds, states, null);
//
//        if (!CollectionUtils.isEmpty(familiesForReverification)) {
//            for (FamilyEntity familyDto : familiesForReverification) {
//                mapOfFamilyWithFamilyIdAsKey.put(familyDto.getFamilyId(), familyDto);
//            }
//            getCommentsFromStateChangeDetailsForFamilies(familiesForReverification, mapOfFamilyWithFamilyIdAsKey);
//            finalResult.addAll(mapOfFamilyWithFamilyIdAsKey.values());
//        }
//
//        //Families with members having states as reverification
//        projections.add("familyId");
////        projections.add("currentState");
//        projections.add("uniqueHealthId");
//
//        states.clear();
//        states.add(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION);
//        states.add(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION);
//        List<MemberEntity> members = memberDao.getMembers(states, locationIds, projections, null, null, null);
//
//        //Collecting familyId from the members
//        for (MemberEntity memberDto : members) {
//            familyIdsFromMemberQuery.add(memberDto.getFamilyId());
//        }
//
//        if (!CollectionUtils.isEmpty(familyIdsFromMemberQuery)) {
//            List<FamilyEntity> familiesByFamilyIds = familyDao.retrieveFamiliesByFamilyIds(familyIdsFromMemberQuery);
//            for (FamilyEntity familyDto : familiesByFamilyIds) {
//                mapOfFamilyWithFamilyIdAsKeyForMembers.put(familyDto.getFamilyId(), familyDto);
//            }
//            getCommentsFromStateChangeDetailsForMembers(members, mapOfFamilyWithFamilyIdAsKeyForMembers);
//            finalResult.addAll(mapOfFamilyWithFamilyIdAsKeyForMembers.values());
//        }
//
//        if (CollectionUtils.isEmpty(finalResult)) {
//            return new ArrayList<>();
//        }
//        return new ArrayList<>(finalResult);
//    }

//    private void getCommentsFromStateChangeDetailsForFamilies(List<FamilyEntity> familyDtosForReverification, Map<String, FamilyEntity> mapOfFamilyWithFamilyIdAsKey) {
//        for (FamilyEntity familyDto : familyDtosForReverification) {
//            if (familyDto.getCurrentState() != null) {
//                FamilyStateDetailEntity familyState = familyStateDetailDao.retrieveById(familyDto.getCurrentState());
//                if (familyState != null) {
//                    mapOfFamilyWithFamilyIdAsKey.get(familyDto.getFamilyId()).setComment(familyState.getComment());
//                }
//            }
//        }
//    }

//    private void getCommentsFromStateChangeDetailsForMembers(List<MemberEntity> memberDtosForReverification, Map<String, FamilyEntity> mapOfFamilyWithFamilyIdAsKey) {
//        for (MemberEntity memberDto : memberDtosForReverification) {
//            Integer currentStateId = memberStateDetailDao.retrieveMemberCurrentState(memberDto.getId());
//            if (currentStateId != null) {
//                MemberStateDetailEntity memberState = memberStateDetailDao.retrieveById(currentStateId);
//                String comment = mapOfFamilyWithFamilyIdAsKey.get(memberDto.getFamilyId()).getComment();
//                if (comment == null || comment.isEmpty()) {
//                    if (memberDto.getUniqueHealthId() != null && memberState != null) {
//                        comment = memberDto.getUniqueHealthId() + "-" + memberState.getComment();
//                    }
//                } else if (memberDto.getUniqueHealthId() != null && memberState != null) {
//                    comment = comment + "\n" + memberDto.getUniqueHealthId() + "-" + memberState.getComment();
//                }
//                mapOfFamilyWithFamilyIdAsKey.get(memberDto.getFamilyId()).setComment(comment);
//            }
//        }
//    }

    private String createEntryForFamilyStateDetailAndGetStateAccordingToLastState(FamilyEntity familyEntity) {

        if (familyEntity.getState() == null || familyEntity.getState().isEmpty()) {
            familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW);
        } else {
            switch (familyEntity.getState()) {
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED:
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_VERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_MO_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_MO_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_MO_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_ARCHIVED);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_DEAD);
                    break;
                case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED:
                    familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_VERIFIED);
                    break;
                default:
                    if (familyEntity.getState().contains(".new")) {
                        familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW);
                    } else {
                        familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_VERIFIED);
                    }
                    break;
            }
        }

        return familyEntity.getState();
    }

    @Override
    public List<MemberEntity> getMembers(List<String> states, List<String> familyIds, List<Integer> locationIds, List<String> basicStates) {
        return memberDao.getMembers(states, locationIds, null, familyIds, null, basicStates);
    }

    @Override
    public List<MemberEntity> getMembersForAsha(List<String> states, List<String> familyIds, List<Integer> locationIds) {
        return memberDao.getMembersForAsha(states, locationIds, null, familyIds, null);
    }

    private Integer getContactPersonIdForFamilyEntity(List<MemberEntity> members) {
        Integer contactPersonId = null;
        for (MemberEntity memberDto : members) {
            if (memberDto.getMobileNumber() != null) {
                if (Boolean.TRUE.equals(memberDto.getFamilyHeadFlag())) {
                    contactPersonId = memberDto.getId();
                    break;
                }
                contactPersonId = memberDto.getId();
            }
        }
        return contactPersonId;
    }

    private Integer getHOFIdForFamilyEntity(List<MemberEntity> members) {
        Integer hofId = null;
        for (MemberEntity memberEntity : members) {
            if (Boolean.TRUE.equals(memberEntity.getFamilyHeadFlag())) {
                hofId = memberEntity.getId();
            }
        }
        return hofId;
    }

    private void updateSickleCellTestUpdate(List<MemberEntity> members) {
        Gson gson = new Gson();
        for (MemberEntity aMember : members) {
            if (aMember.getChronicDiseaseDetails() != null && aMember.getChronicDiseaseDetails().contains(729)) {
                MemberAdditionalInfo memberAdditionalInfo;
                if (aMember.getAdditionalInfo() != null && !aMember.getAdditionalInfo().isEmpty()) {
                    memberAdditionalInfo = gson.fromJson(aMember.getAdditionalInfo(), MemberAdditionalInfo.class);
                } else {
                    memberAdditionalInfo = new MemberAdditionalInfo();
                }
                memberAdditionalInfo.setSickleCellTest("POSITIVE");
                aMember.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
            } else {
                MemberAdditionalInfo memberAdditionalInfo;
                if (aMember.getAdditionalInfo() != null && !aMember.getAdditionalInfo().isEmpty()) {
                    memberAdditionalInfo = gson.fromJson(aMember.getAdditionalInfo(), MemberAdditionalInfo.class);
                    memberAdditionalInfo.setSickleCellTest(null);
                    aMember.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
                }
            }
        }
    }

    @Override
    public Map<String, String> storeFamilyHealthSurveyForm(ParsedRecordBean parsedRecordBean, UserMaster user) {
        Map<String, String> returnMap = new LinkedHashMap<>();
        returnMap.put("createdInstanceId", "1");
        boolean isNewFamily = false;
        List<String> keysForFamilyQuestions = this.getKeysForFamilyQuestions();
        Map<String, String> keyAndAnswerMap = new HashMap<>();
        Map<String, String> familyKeyAndAnswerMap = new HashMap<>();
        Map<String, String> memberKeyAndAnswerMap = new HashMap<>();
        String familyId;
        String recordString = parsedRecordBean.getAnswerRecord();
        String[] keyAndAnswerSet = recordString.split(MobileConstantUtil.ANSWER_STRING_FIRST_SEPARATER);
        List<String> keyAndAnswerSetList = new ArrayList<>(Arrays.asList(keyAndAnswerSet));
        Set<MemberEntity> membersToBeArchived = new HashSet<>();
        Set<MemberEntity> membersToBeMarkedDead = new HashSet<>();
        for (String aKeyAndAnswer : keyAndAnswerSetList) {
            String[] keyAnswerSplit = aKeyAndAnswer.split(MobileConstantUtil.ANSWER_STRING_SECOND_SEPARATER);
            if (keyAnswerSplit.length != 2) {
                continue;
            }
            keyAndAnswerMap.put(keyAnswerSplit[0], keyAnswerSplit[1]);
        }

        familyId = keyAndAnswerMap.get("1");

        if (keyAndAnswerMap.get("101") != null) {
            switch (keyAndAnswerMap.get("101")) {
                //if option selected is Can't verify now (on the mobile)
                case "2":
                    return returnMap;
                //if option selected is archive (on the mobile)
                case "3":
                    if (familyId == null || familyId.isEmpty() || familyId.equals(NOT_AVAILABLE)) {
                        return returnMap;
                    }
                    this.markFamilyAsArchived(familyId);
                    return returnMap;
                //if option selected is Does not fall under my area (on the mobile)
                case "4":
                    if (familyId == null || familyId.isEmpty() || familyId.equals(NOT_AVAILABLE)) {
                        return returnMap;
                    }
                    this.markFamilyAsOrphan(familyId, user.getId(), keyAndAnswerMap.get("116"));
                    return returnMap;
                default:
            }
        }

        FamilyEntity familyEntity;
        if (familyId == null || familyId.equals(NOT_AVAILABLE)) {
            familyEntity = new FamilyEntity();
        } else {
            familyEntity = familyDao.retrieveFamilyByFamilyId(familyId);
        }

        List<MemberEntity> membersInTheFamily = memberDao.retrieveMemberEntitiesByFamilyId(familyEntity.getFamilyId());
        Map<String, MemberEntity> mapOfMemberWithHealthIdAsKey = new HashMap<>();
        Map<String, MemberEntity> mapOfNewMemberWithLoopIdAsKey = new HashMap<>();
        Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey = new HashMap<>();
        Map<String, MemberEntity> mapOfDeadMemberWithLoopIsAsKey = new HashMap<>();
        String motherChildRelationString = keyAndAnswerMap.get("4003");
        String husbandWifeRelationString = keyAndAnswerMap.get("4005");

        for (MemberEntity memberDto : membersInTheFamily) {
            mapOfMemberWithHealthIdAsKey.put(memberDto.getUniqueHealthId(), memberDto);
        }

        if (familyEntity.getFamilyId() == null) {
            familyEntity.setFamilyId(this.generateFamilyId());
            isNewFamily = true;
        }

        for (Map.Entry<String, String> keyAnswerSet : keyAndAnswerMap.entrySet()) {
            String key = keyAnswerSet.getKey();
            String answer = keyAnswerSet.getValue();
            if (keysForFamilyQuestions.contains(key)) {
                familyKeyAndAnswerMap.put(key, answer);
            } else {
                memberKeyAndAnswerMap.put(key, answer);
            }
        }

        for (Map.Entry<String, String> entrySet : familyKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            this.setAnswersToFamilyDto(key, answer, familyEntity);
        }

        String longitude = keyAndAnswerMap.get("-1");
        String latitude = keyAndAnswerMap.get("-2");
        familyEntity.setLongitude(longitude);
        familyEntity.setLatitude(latitude);
        for (Map.Entry<String, String> entrySet : memberKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            MemberEntity memberEntity;
            if (key.contains(".")) {
                String[] splitKey = key.split("\\.");
                memberEntity = mapOfMemberWithHealthIdAsKey.get(memberKeyAndAnswerMap.get("90" + "." + splitKey[1]));
                if (memberEntity == null) {
                    memberEntity = mapOfNewMemberWithLoopIdAsKey.get(splitKey[1]);
                    if (memberEntity == null) {
                        memberEntity = new MemberEntity();
                        memberEntity.setFamilyId(familyEntity.getFamilyId());
                        memberEntity.setUniqueHealthId(this.generateMemberUniqueHealthId());
                        mapOfNewMemberWithLoopIdAsKey.put(splitKey[1], memberEntity);
                    }
                }
                mapOfMemberWithLoopIdAsKey.putIfAbsent(splitKey[1], memberEntity);

                if (memberKeyAndAnswerMap.get("94" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("94" + "." + splitKey[1]).equals("2")) {
                    membersToBeMarkedDead.add(memberEntity);
                    mapOfDeadMemberWithLoopIsAsKey.put(splitKey[1], memberEntity);
                } else if (memberKeyAndAnswerMap.get("94" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("94" + "." + splitKey[1]).equals("3")) {
                    membersToBeArchived.add(memberEntity);
                } else {
                    this.setAnswersToMemberEntity(splitKey[0], answer, memberEntity, memberKeyAndAnswerMap, splitKey[1]);
                }
            } else {
                memberEntity = mapOfMemberWithHealthIdAsKey.get(memberKeyAndAnswerMap.get("90"));
                if (memberEntity == null) {
                    memberEntity = mapOfNewMemberWithLoopIdAsKey.get("0");
                    if (memberEntity == null) {
                        memberEntity = new MemberEntity();
                        memberEntity.setFamilyId(familyEntity.getFamilyId());
                        memberEntity.setUniqueHealthId(this.generateMemberUniqueHealthId());
                        mapOfNewMemberWithLoopIdAsKey.put("0", memberEntity);
                    }
                }
                mapOfMemberWithLoopIdAsKey.putIfAbsent("0", memberEntity);
                if ((memberKeyAndAnswerMap.get("94") != null && memberKeyAndAnswerMap.get("94").equals("2"))) {
                    membersToBeMarkedDead.add(memberEntity);
                    mapOfDeadMemberWithLoopIsAsKey.put("0", memberEntity);
                } else if ((memberKeyAndAnswerMap.get("94") != null && memberKeyAndAnswerMap.get("94").equals("3"))) {
                    memberEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED);
                    membersToBeArchived.add(memberEntity);
                } else {
                    this.setAnswersToMemberEntity(key, answer, memberEntity, memberKeyAndAnswerMap, null);
                }
            }
        }

        Set<MemberEntity> membersToBeAdded = new HashSet<>(mapOfNewMemberWithLoopIdAsKey.values());
        Set<MemberEntity> membersToBeUpdated = new HashSet<>(mapOfMemberWithLoopIdAsKey.values());
        membersToBeUpdated.removeAll(membersToBeArchived);
        membersToBeUpdated.removeAll(membersToBeMarkedDead);
        membersToBeUpdated.removeAll(membersToBeAdded);

        if (familyEntity.getId() != null) {
            familyEntity.setModifiedBy(user.getId());
            familyEntity.setModifiedOn(new Date());
        }

        //Adding DEAD Member to rch_member_death_detail table
        if (!mapOfDeadMemberWithLoopIsAsKey.isEmpty()) {
            for (Map.Entry<String, MemberEntity> entry : mapOfDeadMemberWithLoopIsAsKey.entrySet()) {
                String key = entry.getKey();
                MemberEntity memberEntity = entry.getValue();
                if (memberEntity.getState() != null
                        && !memberEntity.getState().equals(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED)) {
                    Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(memberEntity.getId());
                    if (Boolean.TRUE.equals(abBoolean)) {
                        throw new ImtechoMobileException("Member with Health ID " + memberEntity.getUniqueHealthId() + " is already marked DEAD. "
                                + "You cannot mark a DEAD member DEAD again.", 1);
                    } else {
                        if (key.equals("0")) {
                            if (memberKeyAndAnswerMap.containsKey("4900")) {
                                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                                Long dateOfDeath = Long.valueOf(memberKeyAndAnswerMap.get("4900"));
                                QueryDto queryDto = new QueryDto();
                                queryDto.setCode(CODE_FOR_DEATH);
                                LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                                parameters.put(MEMBER_ID, memberEntity.getId());
                                if (familyEntity.getAreaId() != null) {
                                    parameters.put(LOCATION_ID, familyEntity.getAreaId());
                                } else {
                                    parameters.put(LOCATION_ID, familyEntity.getLocationId());
                                }
                                parameters.put(ACTION_BY, user.getId());
                                parameters.put(FAMILY_ID, familyEntity.getId());
                                parameters.put(DEATH_DATE, sdf.format(new Date(dateOfDeath)));
                                parameters.put(PLACE_OF_DEATH, memberKeyAndAnswerMap.get("4901"));
                                parameters.put(DEATH_REASON, null);
                                parameters.put(OTHER_DEATH_REASON, null);
                                parameters.put(SERVICE_TYPE, "FHS");
                                parameters.put(REFERENCE_ID, memberEntity.getId());
                                parameters.put(HEALTH_INFRA_ID, memberKeyAndAnswerMap.get("4950"));
                                queryDto.setParameters(parameters);
                                List<QueryDto> queryDtos = new LinkedList<>();
                                queryDtos.add(queryDto);
                                queryMasterService.executeQuery(queryDtos, true);
                            }
                        } else {
                            if (memberKeyAndAnswerMap.containsKey("4900" + "." + key)) {
                                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                                long dateOfDeath = Long.parseLong(memberKeyAndAnswerMap.get("4900" + "." + key));
                                QueryDto queryDto = new QueryDto();
                                queryDto.setCode(CODE_FOR_DEATH);
                                LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                                parameters.put(MEMBER_ID, memberEntity.getId());
                                if (familyEntity.getAreaId() != null) {
                                    parameters.put(LOCATION_ID, familyEntity.getAreaId());
                                } else {
                                    parameters.put(LOCATION_ID, familyEntity.getLocationId());
                                }
                                parameters.put(ACTION_BY, user.getId());
                                parameters.put(FAMILY_ID, familyEntity.getId());
                                parameters.put(DEATH_DATE, sdf.format(new Date(dateOfDeath)));
                                parameters.put(PLACE_OF_DEATH, memberKeyAndAnswerMap.get("4901" + "." + key));
                                parameters.put(DEATH_REASON, null);
                                parameters.put(OTHER_DEATH_REASON, null);
                                parameters.put(SERVICE_TYPE, "FHS");
                                parameters.put(REFERENCE_ID, memberEntity.getId());
                                parameters.put(HEALTH_INFRA_ID, memberKeyAndAnswerMap.get("4950" + "." + key));
                                queryDto.setParameters(parameters);
                                List<QueryDto> queryDtos = new LinkedList<>();
                                queryDtos.add(queryDto);
                                queryMasterService.executeQuery(queryDtos, true);
                            }
                        }
                    }
                } else if (memberKeyAndAnswerMap.containsKey("4900" + "." + key)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                    long dateOfDeath = Long.parseLong(memberKeyAndAnswerMap.get("4900" + "." + key));
                    QueryDto queryDto = new QueryDto();
                    queryDto.setCode(CODE_FOR_DEATH);
                    LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                    parameters.put(MEMBER_ID, memberEntity.getId());
                    if (familyEntity.getAreaId() != null) {
                        parameters.put(LOCATION_ID, familyEntity.getAreaId());
                    } else {
                        parameters.put(LOCATION_ID, familyEntity.getLocationId());
                    }
                    parameters.put(ACTION_BY, user.getId());
                    parameters.put(FAMILY_ID, familyEntity.getId());
                    parameters.put(DEATH_DATE, sdf.format(new Date(dateOfDeath)));
                    parameters.put(PLACE_OF_DEATH, memberKeyAndAnswerMap.get("4901" + "." + key));
                    parameters.put(DEATH_REASON, null);
                    parameters.put(OTHER_DEATH_REASON, null);
                    parameters.put(SERVICE_TYPE, "FHS");
                    parameters.put(REFERENCE_ID, memberEntity.getId());
                    parameters.put(HEALTH_INFRA_ID, memberKeyAndAnswerMap.get("4950" + "." + key));
                    queryDto.setParameters(parameters);
                    List<QueryDto> queryDtos = new LinkedList<>();
                    queryDtos.add(queryDto);
                    queryMasterService.executeQuery(queryDtos, true);
                }
            }
        }

        this.persistFamily(familyEntity, membersToBeAdded, membersToBeArchived, membersToBeUpdated,
                String.valueOf(user.getId()), membersToBeMarkedDead, mapOfMemberWithLoopIdAsKey,
                motherChildRelationString, husbandWifeRelationString);

        if (isNewFamily) {
            StringBuilder sb = new StringBuilder();
            sb.append("Family ID : ");
            sb.append(familyEntity.getFamilyId());
            sb.append("\n");
            sb.append("\n");
            int count = 1;
            for (Map.Entry<String, MemberEntity> entry : mapOfMemberWithLoopIdAsKey.entrySet()) {
                MemberEntity member = entry.getValue();
                if (Boolean.TRUE.equals(member.getFamilyHeadFlag())) {
                    returnMap.put("familyId", familyEntity.getFamilyId() + " - " + member.getFirstName() + " " + member.getMiddleName() + " " + member.getLastName());
                }
                sb.append(member.getUniqueHealthId());
                sb.append(" - ");
                sb.append(member.getFirstName());
                sb.append(" ");
                sb.append(member.getMiddleName());
                sb.append(" ");
                sb.append(member.getLastName());
                if (count < mapOfMemberWithLoopIdAsKey.size()) {
                    sb.append("\n");
                }
                count++;
            }
            if (sb.length() > 0) {
                returnMap.put("message", sb.toString());
            }
        }
        return returnMap;
    }

    private void markFamilyAsArchived(String familyId) {
        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(familyId);
        switch (familyEntity.getState()) {
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED:
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN:
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED:
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW:
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD:
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED:
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED);
                break;
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_ARCHIVED_FHW_REVERIFIED);
                break;
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_MO_REVERIFICATION:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW_ARCHIVED_MO_FHW_REVERIFIED);
                break;
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_FHW_REVERIFIED);
                break;
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_MO_FHW_REVERIFIED);
                break;
            case FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_EMRI_REVERIFICATION:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED_EMRI_FHW_REVERIFIED);
                break;
            default:
                this.updateFamily(familyEntity, familyEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED);
                break;
        }
    }

    private void markFamilyAsOrphan(String familyId, Integer userId, String locationId) {
        FamilyEntity family = familyDao.retrieveFamilyByFamilyId(familyId);
        family.setModifiedBy(userId);
        family.setModifiedOn(new Date());
        if (locationId == null || locationId.equals("null")) {
            familyDao.update(family);
        } else {
            family.setLocationId(Integer.valueOf(locationId));
            family.setAreaId(null);
            if (FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_VERIFIED.contains(family.getState())
                    || FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_NEW.contains(family.getState())) {
                familyDao.update(family);
                return;
            }
            this.updateFamily(family, family.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN);
            List<MemberEntity> members = memberDao.retrieveMemberEntitiesByFamilyId(familyId);
            for (MemberEntity member : members) {
                member.setModifiedBy(userId);
                member.setModifiedOn(new Date());
                this.updateMember(member, member.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ORPHAN);
            }
        }
    }

    private List<String> getKeysForFamilyQuestions() {
        List<String> keys = new ArrayList<>();
        keys.add("1");
        keys.add("100");
        keys.add("101");
        keys.add("2");
        keys.add("3");
        keys.add("31");
        keys.add("30");
        keys.add("32");
        keys.add("4");
        keys.add("5");
        keys.add("51");
        keys.add("61");
        keys.add("6");
        keys.add("7");
        keys.add("21");
        keys.add("81");
        keys.add("8");
        keys.add("-3");
        keys.add("33");
        keys.add("4999");
        keys.add("9997");
        return keys;
    }


    private void setAnswersToMemberEntity(String key, String answer, MemberEntity memberEntity, Map<String, String> memberKeyAndAnswerMap, String memberCount) {
        switch (key) {
            case "16":
                switch (answer) {
                    case "1":
                        memberEntity.setFamilyHeadFlag(Boolean.TRUE);
                        break;
                    case "2":
                        memberEntity.setFamilyHeadFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "999":
                if (answer.trim().length() > 0) {
                    memberEntity.setEmamtaHealthId(answer.trim());
                }
                break;
            case "9":
                if (answer.trim().length() > 0) {
                    memberEntity.setFirstName(answer.trim());
                }
                break;
            case "10":
                if (answer.trim().length() > 0 && !answer.equals("1")) {
                    memberEntity.setMiddleName(answer.trim());
                }
                break;
            case "104":
                if (answer.trim().length() > 0) {
                    memberEntity.setGrandfatherName(answer.trim());
                }
                break;
            case "11":
                if (answer.trim().length() > 0) {
                    memberEntity.setLastName(answer.trim());
                }
                break;
            case "12":
                switch (answer) {
                    case "1":
                        memberEntity.setGender("M");
                        break;
                    case "2":
                        memberEntity.setGender("F");
                        break;
                    case "3":
                        memberEntity.setGender("T");
                        break;
                    default:
                }
                break;
            case "13":
                if (answer != null) {
                    memberEntity.setMaritalStatus(Integer.parseInt(answer));
                }
                break;
            case "1102":
                memberEntity.setEmamtaHealthId(answer);
                break;
            case "1302":
                switch (answer) {
                    case "1":
                        memberEntity.setIsNativeFlag(Boolean.FALSE);
                        break;
                    case "2":
                        memberEntity.setIsNativeFlag(Boolean.TRUE);
                        break;
                    default:
                }
                break;
            case "14":
                if (!answer.equals("T")) {
                    memberEntity.setAadharNumber(answer.replace("F/", ""));
                    memberEntity.setAadharNumberAvailable(Boolean.TRUE);
                } else {
                    memberEntity.setAadharNumberAvailable(Boolean.FALSE);
                }
                break;
            case "15":
                if (!answer.equals("T")) {
                    memberEntity.setMobileNumber(answer.replace("F/", ""));
                }
                break;
            case "17":
                memberEntity.setDob(new Date(Long.parseLong(answer)));
                break;
            case "19":
                if (answer.trim().length() > 0) {
                    memberEntity.setIfsc(answer.trim());
                }
                break;
            case "20":
                if (answer.trim().length() > 0) {
                    memberEntity.setAccountNumber(answer.trim());
                }
                break;
            case "23":
                switch (answer) {
                    case "1":
                        memberEntity.setIsPregnantFlag(Boolean.TRUE);
                        break;
                    case "2":
                        memberEntity.setIsPregnantFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "22":
                memberEntity.setLmpDate(new Date(Long.parseLong(answer)));
                break;
            case "24":
                memberEntity.setFamilyPlanningMethod(answer);
                break;
            case "25":
                float floatAnswer = Float.parseFloat(answer);
                memberEntity.setNormalCycleDays((short) floatAnswer);
                break;
            case "2504":
                switch (answer) {
                    case "1":
                        memberEntity.setIsNativeFlag(Boolean.TRUE);
                        break;
                    case "2":
                        memberEntity.setIsNativeFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            case "41":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("40" + "." + memberCount).equals("1")) {
                        Set<Integer> anomalyRelEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            anomalyRelEntitys.add(Integer.parseInt(id));
                        }
                        memberEntity.setCongenitalAnomalyDetails(anomalyRelEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("40").equals("1")) {
                    Set<Integer> anomalyRelEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        anomalyRelEntitys.add(Integer.parseInt(id));
                    }
                    memberEntity.setCongenitalAnomalyDetails(anomalyRelEntitys);
                }
                break;
            case "43":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("42" + "." + memberCount).equals("1")) {
                        Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                        }
                        memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("42").equals("1")) {
                    Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                    }
                    memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                }
                break;
            case "45":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("44" + "." + memberCount).equals("1")) {
                        Set<Integer> currentDiseaseEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            currentDiseaseEntitys.add(Integer.parseInt(id));
                        }
                        memberEntity.setCurrentDiseaseDetails(currentDiseaseEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("44").equals("1")) {
                    Set<Integer> currentDiseaseEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        currentDiseaseEntitys.add(Integer.parseInt(id));
                    }
                    memberEntity.setCurrentDiseaseDetails(currentDiseaseEntitys);
                }
                break;
            case "47":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("46" + "." + memberCount).equals("1")) {
                        Set<Integer> eyeRelEntities = new HashSet<>();
                        for (String id : answer.split(",")) {
                            eyeRelEntities.add(Integer.parseInt(id));
                        }
                        memberEntity.setEyeIssueDetails(eyeRelEntities);
                    }
                } else if (memberKeyAndAnswerMap.get("46").equals("1")) {
                    Set<Integer> eyeRelEntities = new HashSet<>();
                    for (String id : answer.split(",")) {
                        eyeRelEntities.add(Integer.parseInt(id));
                    }
                    memberEntity.setEyeIssueDetails(eyeRelEntities);
                }
                break;
            case "48":
                memberEntity.setEducationStatus(answer != null ? Integer.parseInt(answer) : null);
                break;
            case "96":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("95" + "." + memberCount).equals("1")) {
                        Map<String, String> aadharValuesMap = new HashMap<>();
                        String keyValueString = org.apache.commons.lang3.StringUtils.substringBetween(answer, "{", "}");
                        String[] keyValueSplit = keyValueString.split(",");
                        for (String keyValuePair : keyValueSplit) {
                            String[] split = keyValuePair.split("=");
                            if (split.length == 2) {
                                aadharValuesMap.put(split[0].trim(), split[1].trim());
                            }
                        }
                        memberEntity.setNameAsPerAadhar(aadharValuesMap.get("name"));
                    }
                } else if (memberKeyAndAnswerMap.get("95").equals("1")) {
                    Map<String, String> aadharValuesMap = new HashMap<>();
                    String keyValueString = org.apache.commons.lang3.StringUtils.substringBetween(answer, "{", "}");
                    String[] keyValueSplit = keyValueString.split(",");
                    for (String keyValuePair : keyValueSplit) {
                        String[] split = keyValuePair.split("=");
                        if (split.length == 2) {
                            aadharValuesMap.put(split[0].trim(), split[1].trim());
                        }
                    }
                    memberEntity.setNameAsPerAadhar(aadharValuesMap.get("name"));
                }
                break;
            case "5001":
                switch (answer) {
                    case "1":
                        memberEntity.setAgreedToShareAadhar(Boolean.TRUE);
                        break;
                    case "2":
                        memberEntity.setAgreedToShareAadhar(Boolean.FALSE);
                        break;
                    default:
                }
                break;
            default:
        }
    }

    private void setAnswersToFamilyDto(String key, String answer, FamilyEntity familyEntity) {
        switch (key) {
            case "2":
                if (answer.trim().length() > 0) {
                    familyEntity.setHouseNumber(answer.trim());
                }
                break;

            case "3":
                if (answer.trim().length() > 0) {
                    familyEntity.setAddress1(answer.trim());
                }
                break;

            case "31":
                if (answer.trim().length() > 0) {
                    familyEntity.setAddress2(answer.trim());
                }
                break;

            case "32":
                if (answer.trim().length() > 0) {
                    familyEntity.setAreaId(Integer.valueOf(answer.trim()));
                }
                break;

            case "4":
                if (answer.trim().length() > 0) {
                    familyEntity.setReligion(answer.trim());
                }
                break;

            case "5":
                if (answer.trim().length() > 0) {
                    familyEntity.setCaste(answer.trim());
                }
                break;

            case "51":
                switch (answer) {
                    case "1":
                        familyEntity.setMigratoryFlag(Boolean.TRUE);
                        break;

                    case "2":
                        familyEntity.setMigratoryFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;

            case "61":
                switch (answer) {
                    case "1":
                        familyEntity.setVulnerableFlag(Boolean.TRUE);
                        break;

                    case "2":
                        familyEntity.setVulnerableFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;

            case "6":
                switch (answer) {
                    case "1":
                        familyEntity.setBplFlag(Boolean.TRUE);
                        break;

                    case "2":
                        familyEntity.setBplFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;

            case "7":
                switch (answer) {
                    case "1":
                        familyEntity.setToiletAvailableFlag(Boolean.TRUE);
                        break;
                    case "2":
                        familyEntity.setToiletAvailableFlag(Boolean.FALSE);
                        break;
                    default:
                }
                break;

            case "21":
                if (answer.trim().length() > 0) {
                    familyEntity.setMaaVatsalyaNumber(answer.trim());
                }
                break;

            case "8":
                if (answer.equals("0")) {
                    familyEntity.setAnganwadiId(null);
                } else {
                    familyEntity.setAnganwadiId(Integer.valueOf(answer));
                }
                break;
            case "-3":
                familyEntity.setLocationId(Integer.valueOf(answer));
                break;
            case "33":
                if (answer.trim().length() > 0) {
                    familyEntity.setRsbyCardNumber(answer.replace("-", "").trim());
                }
                break;
            default:
        }
    }

    @Override
    public String generateFamilyId() {
        StringBuilder familyId = new StringBuilder("FM");
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        familyId.append("/");
        familyId.append(year);
        familyId.append("/");
        familyId.append(sequenceService.getNextValueBySequenceName("family_id_seq"));
        familyId.append("N");
        return familyId.toString();
    }

    @Override
    public String generateMemberUniqueHealthId() {
        StringBuilder memberUniqueHealthId = new StringBuilder("A");
        memberUniqueHealthId.append(sequenceService.getNextValueBySequenceName("member_unique_health_id_seq"));
        memberUniqueHealthId.append("N");
        return memberUniqueHealthId.toString();
    }

    @Override
    public List<Map<String, List<FamilyDataBean>>> getFamiliesToBeAssignedBySearchString(Integer userId, List<String> searchString, Boolean searchByFamilyId, Boolean searchByLocationId, Boolean isArchivedFamily, Boolean isVerifiedFamily) {

        if (Boolean.TRUE.equals(searchByFamilyId)) {
            return this.getFamilyToBeAssignedByFamilyId(userId, searchString, isArchivedFamily, isVerifiedFamily);
        } else if (Boolean.TRUE.equals(searchByLocationId)) {
            List<Map<String, List<FamilyDataBean>>> mapList = new ArrayList<>();
            Map<String, List<FamilyDataBean>> map = new HashMap<>();
            List<FamilyEntity> familyEntityList = familyDao.getFamilyListByLocationId(Integer.parseInt(searchString.get(0)), isArchivedFamily, isVerifiedFamily);
            List<FamilyDataBean> familyList = new ArrayList<>();

            for (FamilyEntity family : familyEntityList) {
                List<MemberEntity> memberEntitys = memberDao.retrieveMemberEntitiesByFamilyId(family.getFamilyId());
                List<MemberDataBean> memberDataBeans = new ArrayList<>();
                memberEntitys.forEach(memberEntity ->
                        memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity))
                );
                familyList.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(family, memberDataBeans));
            }
            String errormessage = "There are no ";
            if (Boolean.FALSE.equals(isVerifiedFamily) && Boolean.FALSE.equals(isArchivedFamily)) {
                errormessage += "unverified family on this location";
            } else if (Boolean.TRUE.equals(isVerifiedFamily)) {
                errormessage += "verified family on this location";
            } else {
                errormessage += "unverified and archied family on this location";
            }

            if (familyList.isEmpty()) {
                throw new ImtechoUserException(errormessage, 0);
            }
            map.put(FAMILY_TO_BE_ASSIGNED, familyList);
            mapList.add(map);
            return mapList;
        } else {
            List<String> familyId = memberDao.getFamilyIdsByMemberUniqueHealthIds(searchString);
            if (!familyId.isEmpty()) {
                return this.getFamilyToBeAssignedByFamilyId(userId, familyId, isArchivedFamily, isVerifiedFamily);
            } else {
                throw new ImtechoUserException("Please Enter valid  Id!", 0);
            }
        }
    }

    private List<Map<String, List<FamilyDataBean>>> getFamilyToBeAssignedByFamilyId(Integer userId, List<String> familyId, Boolean isArchivedFamily, Boolean isVerifiedFamily) {
        List<Map<String, List<FamilyDataBean>>> mapList = new ArrayList<>();
        List<FamilyEntity> familyEntityList = familyDao.retrieveFamiliesByFamilyIds(familyId);
        if (Boolean.FALSE.equals(isArchivedFamily) && Boolean.FALSE.equals(isVerifiedFamily)) {
            familyEntityList = familyEntityList.stream().filter(
                    f -> f.getState().equals(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED)
                            || f.getState().equals(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN)
            ).collect(Collectors.toList());
            List<FamilyDataBean> familyList = new ArrayList<>();

            for (FamilyEntity family : familyEntityList) {
                List<MemberEntity> memberEntitys = memberDao.retrieveMemberEntitiesByFamilyId(family.getFamilyId());
                List<MemberDataBean> memberDataBeans = new ArrayList<>();
                memberEntitys.forEach(memberEntity ->
                        memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity))
                );
                familyList.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(family, memberDataBeans));
            }
            Map<String, List<FamilyDataBean>> map = new HashMap<>();
            map.put(FAMILY_TO_BE_ASSIGNED, familyList);
            if (familyList.isEmpty()) {
                throw new ImtechoUserException("A family is belongs to verified or archived family ", 0);
            } else {
                return Collections.singletonList(map);
            }

        } else if (Boolean.TRUE.equals(isArchivedFamily)) {
            if (!CollectionUtils.isEmpty(familyEntityList)) {
                familyEntityList = familyEntityList.stream().filter(
                        f -> (f.getState().equals(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED)
                                || f.getState().equals(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN)
                                || FamilyHealthSurveyServiceConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES.contains(f.getState()))
                ).collect(Collectors.toList());
                for (FamilyEntity family : familyEntityList) {
                    Map<String, List<FamilyDataBean>> map = new HashMap<>();
                    if (family.getLocationId().equals(-1)) {
                        List<MemberEntity> memberEntitys = memberDao.retrieveMemberEntitiesByFamilyId(family.getFamilyId());
                        List<MemberDataBean> memberDataBeans = new ArrayList<>();
                        memberEntitys.forEach(memberEntity ->
                                memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity))
                        );
                        FamilyDataBean familyDataBean = FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(family, memberDataBeans);
                        List<FamilyDataBean> familyList = new ArrayList<>();
                        familyList.add(familyDataBean);
                        map.put(FAMILY_TO_BE_ASSIGNED, familyList);
                    } else if (family.getAssignedTo() != null) {
                        if (family.getAssignedTo().equals(userId)) {
                            map.put(family.getFamilyId() + " Already verified by you", null);
                        } else {
                            map.put(family.getFamilyId() + " Already verified by another user", null);
                        }
                    } else {
                        List<Integer> locationIds = userLocationDao.retrieveLocationIdsByUserId(userId);
                        if (locationIds.contains(family.getLocationId())) {
                            map.put(family.getFamilyId() + " Already verified by you", null);
                        } else {
                            map.put(family.getFamilyId() + " Already verified by another user", null);
                        }
                    }
                    mapList.add(map);
                }
                if (mapList.isEmpty()) {
                    throw new ImtechoUserException("This family is not archived Family", 0);
                }
                return mapList;
            } else {
                throw new ImtechoUserException("Please Enter valid  Id!", 0);
            }
        } else {
            //isVerified family
            if (!CollectionUtils.isEmpty(familyEntityList)) {
                familyEntityList = familyEntityList.stream().filter(
                        f -> (FamilyHealthSurveyServiceConstants.FHS_TOTAL_MEMBERS_CRITERIA_FAMILY_STATES.contains(f.getState()))
                ).collect(Collectors.toList());
                for (FamilyEntity family : familyEntityList) {
                    Map<String, List<FamilyDataBean>> map = new HashMap<>();
                    if (!family.getLocationId().equals(-1)) {
                        List<MemberEntity> memberEntitys = memberDao.retrieveMemberEntitiesByFamilyId(family.getFamilyId());
                        List<MemberDataBean> memberDataBeans = new ArrayList<>();
                        memberEntitys.forEach(memberEntity ->
                                memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity))
                        );
                        FamilyDataBean familyDataBean = FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(family, memberDataBeans);
                        List<FamilyDataBean> familyList = new ArrayList<>();
                        familyList.add(familyDataBean);
                        map.put(FAMILY_TO_BE_ASSIGNED, familyList);
                    } else {
                        map.put(family.getFamilyId() + " is not verified ", null);
                    }
                    mapList.add(map);
                }
                if (mapList.isEmpty()) {
                    throw new ImtechoUserException("This family is not verified", 0);
                }
                return mapList;
            }
        }
        return mapList;
    }

    @Override
    public MemberDto retrieveDetailsByMemberId(Integer memberId) {
        return memberDao.retrieveDetailsByMemberId(memberId);
    }

//    @Override
    public MemberDto getMemberDetailsByUniqueHealthId(String  byUniqueHealthId) {
        var memberEntity = memberDao.retrieveMemberByUniqueHealthId(byUniqueHealthId);
        if(Objects.nonNull(memberEntity)){
            return  MemberMapper.getMemberDto(memberEntity);
        } else {
            throw new ImtechoUserException("Member Unique Health Id Is Invalid.", 0);
        }
    }

//    @Override
//    public FamilyEntity createTemporaryFamily(Integer locationId, Integer ashaAreaId) {
//        FamilyEntity familyEntity = new FamilyEntity();
//        familyEntity.setFamilyId(this.generateFamilyId());
//        familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_TEMPORARY);
//        familyEntity.setLocationId(locationId);
//        if (ashaAreaId != null && !ashaAreaId.toString().equals("null")) {
//            familyEntity.setAreaId(ashaAreaId);
//        }
//        familyEntity.setAddress1("TEMPORARY");
//        familyEntity.setAddress2("TEMPORARY");
//        familyDao.createFamily(familyEntity);
//        return familyEntity;
//    }

    @Override
    public Map<String, String> storeMemberUpdateForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswersMap, UserMaster user) {
        Map<String, String> returnMap = new LinkedHashMap<>();
        returnMap.put("createdInstanceId", "1");
        String uniqueHealthId = keyAndAnswersMap.get("2");
        MemberEntity memberEntity;
        if (uniqueHealthId != null) {
            memberEntity = memberDao.getMemberByUniqueHealthIdAndFamilyId(uniqueHealthId, null);
            if (keyAndAnswersMap.get("5") != null && (keyAndAnswersMap.get("5").equals("DEAD") || keyAndAnswersMap.get("5").equals("DEATH"))) {
                Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(memberEntity.getId());
                if (abBoolean != null && abBoolean) {
                    throw new ImtechoMobileException("Member with Health ID " + memberEntity.getUniqueHealthId() + " is already marked DEAD. "
                            + "You cannot mark a DEAD member DEAD again.", 1);
                } else {
                    long dateOfDeath = Long.parseLong(keyAndAnswersMap.get("51"));
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                    String deathPlace = keyAndAnswersMap.get("52");
                    String deathReason = keyAndAnswersMap.get("3001");
                    String otherDeathreason = keyAndAnswersMap.get("3003");
                    FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
                    QueryDto queryDto = new QueryDto();
                    queryDto.setCode(CODE_FOR_DEATH);
                    LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                    parameters.put(MEMBER_ID, memberEntity.getId());
                    if (familyEntity.getAreaId() != null) {
                        parameters.put(LOCATION_ID, familyEntity.getAreaId());
                    } else {
                        parameters.put(LOCATION_ID, familyEntity.getLocationId());
                    }
                    parameters.put(ACTION_BY, user.getId());
                    parameters.put(FAMILY_ID, familyEntity.getId());
                    parameters.put(DEATH_DATE, sdf.format(new Date(dateOfDeath)));
                    parameters.put(PLACE_OF_DEATH, deathPlace);
                    parameters.put(DEATH_REASON, deathReason);
                    parameters.put(OTHER_DEATH_REASON, otherDeathreason);
                    parameters.put(SERVICE_TYPE, "MEMBER_UPDATE");
                    parameters.put(REFERENCE_ID, memberEntity.getId());
                    parameters.put(HEALTH_INFRA_ID, keyAndAnswersMap.get("54"));
                    queryDto.setParameters(parameters);
                    List<QueryDto> queryDtos = new LinkedList<>();
                    queryDtos.add(queryDto);
                    queryMasterService.executeQuery(queryDtos, true);
                }
            } else if (keyAndAnswersMap.get("5") != null && keyAndAnswersMap.get("5").equals("MIGRATED")) {
                return returnMap;
            } else if (keyAndAnswersMap.get("5") != null && keyAndAnswersMap.get("5").equals("ARCHIVE")) {
                memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
                this.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_ARCHIVED);
                return returnMap;
            } else {
                for (Map.Entry<String, String> entry : keyAndAnswersMap.entrySet()) {
                    String key = entry.getKey();
                    String answer = entry.getValue();
                    setAnswersToMemberEntity(key, answer, memberEntity, keyAndAnswersMap, null);
                }
                if (keyAndAnswersMap.get("9992") != null && !keyAndAnswersMap.get("9992").equals("0")) {
                    //Setting Mother ID for this Child
                    Calendar instance = Calendar.getInstance();
                    instance.add(Calendar.YEAR, -20);
                    if (memberEntity.getDob().after(instance.getTime())) {
                        memberEntity.setMotherId(Integer.valueOf(keyAndAnswersMap.get("9992")));
                    }
                }
                memberDao.update(memberEntity);
                if (keyAndAnswersMap.get("9996") != null) {
                    //Setting Childs for this Mother
                    String answer = keyAndAnswersMap.get("9996");
                    List<Integer> childIds = new ArrayList<>();
                    if (answer.contains(",")) {
                        String[] split = answer.split(",");
                        for (String s : split) {
                            childIds.add(Integer.valueOf(s));
                        }
                    } else {
                        childIds.add(Integer.valueOf(answer));
                    }
                    memberDao.updateMotherIdInChildren(memberEntity.getId(), childIds);
                }
            }
        } else {
            memberEntity = new MemberEntity();
            for (Map.Entry<String, String> entry : keyAndAnswersMap.entrySet()) {
                String key = entry.getKey();
                String answer = entry.getValue();
                setAnswersToMemberEntity(key, answer, memberEntity, keyAndAnswersMap, null);
            }
            memberEntity.setFamilyId(keyAndAnswersMap.get("4"));
            memberEntity.setUniqueHealthId(this.generateMemberUniqueHealthId());
            memberEntity.setState(FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_NEW);
            if (keyAndAnswersMap.get("9992") != null && !keyAndAnswersMap.get("9992").equals("0")) {
                //Setting Mother ID for this Child
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.YEAR, -20);
                if (memberEntity.getDob().after(instance.getTime())) {
                    memberEntity.setMotherId(Integer.valueOf(keyAndAnswersMap.get("9992")));
                }
            }
            memberDao.create(memberEntity);

            if (keyAndAnswersMap.get("9996") != null) {
                //Setting Childs for this Mother
                String answer = keyAndAnswersMap.get("9996");
                List<Integer> childIds = new ArrayList<>();
                if (answer.contains(",")) {
                    String[] split = answer.split(",");
                    for (String s : split) {
                        childIds.add(Integer.valueOf(s));
                    }
                } else {
                    childIds.add(Integer.valueOf(answer));
                }
                memberDao.updateMotherIdInChildren(memberEntity.getId(), childIds);
            }
        }

        if (uniqueHealthId == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(memberEntity.getUniqueHealthId());
            sb.append("-");
            sb.append(memberEntity.getFirstName());
            sb.append(" ");
            sb.append(memberEntity.getMiddleName());
            sb.append(" ");
            sb.append(memberEntity.getLastName());
            returnMap.put("memberId", sb.toString());

            sb = new StringBuilder();
            sb.append("New Member Added");
            sb.append("\n");
            sb.append("\n");
            sb.append("Family ID : ");
            sb.append(memberEntity.getFamilyId());
            sb.append("\n");
            sb.append("Member ID : ");
            sb.append(memberEntity.getUniqueHealthId());
            sb.append("\n");
            sb.append("Member Name : ");
            sb.append(memberEntity.getFirstName());
            sb.append(" ");
            sb.append(memberEntity.getMiddleName());
            sb.append(" ");
            sb.append(memberEntity.getLastName());
            returnMap.put("message", sb.toString());
        }

        return returnMap;
    }

    @Override
    public MemberInformationDto searchMembersByUniqueHealthId(String uniqueHealthId) {
        MemberInformationDto memberInformationDto = memberDao.getMemberInfoByUniqueHealthId(uniqueHealthId);
        if (memberInformationDto == null) {
            throw new ImtechoUserException("Enter valid Member Id", 0);
        }
        memberInformationDto.setMemberState(setMemberState(memberInformationDto.getMemberState()));
        memberInformationDto.setFamilyState(setFamilyState(memberInformationDto.getFamilyState()));

        Integer memberId = memberInformationDto.getMemberId();
        if (memberInformationDto.getGender().equals("F")) {

            // get pregnancyRegistrationDetail info
            List<PregnancyRegistrationDetailDto> pregnancyRegistrationDetailDtos = this.getPregnancyRegistrationDetailByMemberId(memberId);
            if (!pregnancyRegistrationDetailDtos.isEmpty()) {
                memberInformationDto.setPregnancyRegistrationDetailDtos(pregnancyRegistrationDetailDtos);
            }

            // get rch_Wpd_Mother_master info
            List<WpdMotherDto> rchWpdMotherMasterDtos = wpdService.getWpdMotherbyMemberid(memberId);
            if (rchWpdMotherMasterDtos != null) {
                memberInformationDto.setWpdMotherDtos(rchWpdMotherMasterDtos);
            }

            // RchPncMotherMaster
            List<PncMotherDto> rchPncMotherMasterDtos = pncService.getPncMotherbyMemberid(memberId);
            if (rchPncMotherMasterDtos != null) {
                memberInformationDto.setPncMotherDtos(rchPncMotherMasterDtos);
            }

        }

        // child information
        List<WpdChildDto> rchWpdChildMasterDtos = wpdService.getWpdChildbyMemberid(memberId);
        if (rchWpdChildMasterDtos != null) {
            memberInformationDto.setWpdChildDtos(rchWpdChildMasterDtos);
        }

        List<PncChildDto> rchPncChildMasterDtos = pncService.getPncChildbyMemberid(memberId);
        if (rchPncChildMasterDtos != null) {
            memberInformationDto.setPncChildDtos(rchPncChildMasterDtos);
        }

        List<ChildServiceMaster> childServiceMasterList = childServiceDao.retrieveByMemberId(memberId, 3);
        List<ChildServiceMasterDto> childServiceMasterDtos = new ArrayList<>();
        if (!childServiceMasterList.isEmpty()) {
            for (ChildServiceMaster childServiceMaster : childServiceMasterList) {
                childServiceMasterDtos.add(ChildServiceMapper.convertEntityToDto(childServiceMaster));
            }
            memberInformationDto.setChildServiceMasterDtos(childServiceMasterDtos);
        }

        return memberInformationDto;
    }

    private String setMemberState(String memberState) {
        String stateOfMember;
        if (FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.contains(memberState)) {
            stateOfMember = "ARCHIVED";
        } else if (FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_VERIFIED.contains(memberState)) {
            stateOfMember = STATE_VERIFIED;
        } else if (FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION.contains(memberState)) {
            stateOfMember = STATE_REVERIFICATION;
        } else if (FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_NEW.contains(memberState)) {
            stateOfMember = "NEW";
        } else if (FamilyHealthSurveyServiceConstants.FHS_MEMBER_VERIFICATION_STATE_DEAD.contains(memberState)) {
            stateOfMember = "DEAD";
        } else {
            stateOfMember = "UNVERIFIED";
        }
        return stateOfMember;
    }

    private String setFamilyState(String familyState) {
        String stateOfFamily;
        if (FamilyHealthSurveyServiceConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES.contains(familyState)) {
            stateOfFamily = "ARCHIVED";
        } else if (FamilyHealthSurveyServiceConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES.contains(familyState)) {
            stateOfFamily = STATE_VERIFIED;
        } else if (FamilyHealthSurveyServiceConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.contains(familyState)) {
            stateOfFamily = STATE_REVERIFICATION;
        } else if (FamilyHealthSurveyServiceConstants.FHS_NEW_CRITERIA_FAMILY_STATES.contains(familyState)) {
            stateOfFamily = "NEW";
        } else {
            stateOfFamily = "UNVERIFIED";
        }
        return stateOfFamily;
    }

    public List<PregnancyRegistrationDetailDto> getPregnancyRegistrationDetailByMemberId(Integer memberId) {
        return memberDao.getPregnancyRegistrationDetailByMemberId(memberId);
    }

    @Override
    public void updateVerifiedFamilyLocation(List<String> familyList, Integer selectedMoveLocationAshaAreaId, Integer selectedMoveLocationAnganwadiId) {
        familyDao.updateVerifiedFamilyLocation(user.getId(), familyList, selectedMoveLocationAshaAreaId, selectedMoveLocationAnganwadiId);
    }

    @Override
    public MemberEntity retrieveMemberEntityByID(Integer id) {
        if (id == null) {
            return null;
        }
        return memberDao.retrieveById(id);
    }

//    @Override
//    public List<MemberDto> searchMembers(String searchString, String searchBy, Integer limit, Integer offset) {
//        return memberDao.searchMembers(searchString, searchBy, limit, offset);
//    }

//    @Override
//    public Map<String, Object> retrieveMemberByPhoneNumber(String phoneNumber) {
//        List<MemberEntity> membersByPhoneNumber = memberDao.retrieveMembersByPhoneNumber(phoneNumber, null);
//        if (CollectionUtils.isEmpty(membersByPhoneNumber)) {
//            return null;
//        }
//        MemberEntity memberEntity = membersByPhoneNumber.get(0);
//        FamilyEntity memberFamily = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
//        Map<String, Object> mapToReturn = new HashMap<>();
//        if (memberFamily == null) {
//            return null;
//        }
//        // if mobile number is registered with only one user
//        if (!CollectionUtils.isEmpty(membersByPhoneNumber) && membersByPhoneNumber.size() == 1) {
//            checkUserIsAllowToRegisterInMyTecho(phoneNumber, memberEntity.getId(), memberEntity.getFamilyId(), false); // for checking is user allowed to register in mytecho
//            mapToReturn.put("UNIQUE_MEMBER", memberEntityToMemberDataBean(memberEntity, memberFamily));
//            return mapToReturn;
//        }
//        List<MemberDataBean> membersDataBean = new LinkedList<>();
//        HashMap<String, FamilyEntity> familiesFromMembers = new HashMap<>();
//        if (!CollectionUtils.isEmpty(membersByPhoneNumber)) {
//            membersByPhoneNumber.forEach(member -> {
//                FamilyEntity family = familyDao.retrieveFamilyByFamilyId(member.getFamilyId());
//                familiesFromMembers.put(member.getFamilyId(), family);
//                membersDataBean.add(memberEntityToMemberDataBean(member, family));
//            });
//        }
//        Boolean isDobUnique;
//        if (familiesFromMembers.size() == 1) {
//            isDobUnique = memberDao.checkIfDOBIsUnique(memberEntity.getFamilyId(), phoneNumber);
//            if (Boolean.FALSE.equals(isDobUnique)) {
//                mapToReturn.put(CHECK_WITH, null);
//                mapToReturn.put(MEMBER_LIST, membersDataBean);
//            } else {
//                mapToReturn.put(CHECK_WITH, "DOB");
//            }
//            mapToReturn.put("FAMILY_ID", memberEntity.getFamilyId());
//        } else if (familiesFromMembers.size() > 1) {
//            HashMap<String, MemberDataBean> familyWiseHofMember = new HashMap<>();
//            familiesFromMembers.forEach((key, value) ->
//                    familyWiseHofMember.put(key, memberEntityToMemberDataBean(memberDao.getFamilyHeadMemberDetail(key), value)));
//            mapToReturn.put("FAMILYLIST", familyWiseHofMember);
//        } else {
//            mapToReturn.put(MEMBER_LIST, membersDataBean);
//        }
//        return mapToReturn;
//    }

    @Override
    public List<MemberDataBean> retrieveMemberDataBeansByFamilyId(String familyId) {
        List<MemberDataBean> memberDataBeans = new LinkedList<>();
        List<String> familyIds = new LinkedList<>();
        familyIds.add(familyId);
        List<MemberEntity> members = this.getMembers(null, familyIds, null, FamilyHealthSurveyServiceConstants.VALID_MEMBERS_BASIC_STATES);

        if (CollectionUtils.isEmpty(members)) {
            return Collections.emptyList();
        } else {
            for (MemberEntity member : members) {
                memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(member));
            }
        }
        return memberDataBeans;
    }

    private MemberDataBean memberEntityToMemberDataBean(MemberEntity memberEntity, FamilyEntity familyEntity) {
        MemberDataBean memberDataBean = new MemberDataBean();
        memberDataBean.setId(memberEntity.getId());
        memberDataBean.setLocationId(familyEntity.getLocationId());
        memberDataBean.setUniqueHealthId(memberEntity.getUniqueHealthId());
        memberDataBean.setFirstName(memberEntity.getFirstName());
        memberDataBean.setMiddleName(memberEntity.getMiddleName());
        memberDataBean.setLastName(memberEntity.getLastName());
        memberDataBean.setFamilyId(memberEntity.getFamilyId());
        memberDataBean.setDateOfBirth(memberEntity.getDob());
        memberDataBean.setGender(memberEntity.getGender());
        memberDataBean.setMaritalStatus(memberEntity.getMaritalStatus());
        memberDataBean.setAddress(familyEntity.getAddress1());
        memberDataBean.setFamilyHeadFlag(memberEntity.getFamilyHeadFlag());
        memberDataBean.setMobileNumber(memberEntity.getMobileNumber());
        return memberDataBean;
    }

//    @Override
//    public MemberDataBean verifyMemberDetailByFamilyId(String familyId, Long dob, String aadharNumber) {
//        List<MemberEntity> memberEntity = memberDao.verifyMemberDetailByFamilyId(familyId, dob, aadharNumber);
//        if (memberEntity == null) {
//            return null;
//        }
//        if (memberEntity.size() == 1) {
//            FamilyEntity family = familyDao.retrieveFamilyByFamilyId(memberEntity.get(0).getFamilyId());
//            if (family == null) {
//                return null;
//            }
//            checkUserIsAllowToRegisterInMyTecho(memberEntity.get(0).getMobileNumber(), memberEntity.get(0).getId(), familyId, false); // for checking is user allowed to register in mytecho
//            return memberEntityToMemberDataBean(memberEntity.get(0), family);
//        } else {
//            MemberDataBean tempBean = new MemberDataBean();
//            tempBean.setId(null);
//            tempBean.setFamilyId(familyId);
//            tempBean.setDob(dob);
//            return tempBean;
//        }
//    }

//    @Override
//    public Map<String, Object> verifyFamilyById(String familyId, String mobileNumber) {
//        Map<String, Object> mapToReturn = new HashMap<>();
//        List<MemberEntity> membersByFamilyId = memberDao.retrieveMembersByPhoneNumber(mobileNumber, familyId);
//        if (CollectionUtils.isEmpty(membersByFamilyId)) {
//            return null;
//        }
//        FamilyEntity memberFamily = familyDao.retrieveFamilyByFamilyId(membersByFamilyId.get(0).getFamilyId());
//        if (!CollectionUtils.isEmpty(membersByFamilyId) && membersByFamilyId.size() == 1) {  // if mobile number is registered with only one user
//            checkUserIsAllowToRegisterInMyTecho(mobileNumber, membersByFamilyId.get(0).getId(), familyId, false); // for checking is user allowed to register in mytecho
//            mapToReturn.put("UNIQUE_MEMBER", memberEntityToMemberDataBean(membersByFamilyId.get(0), memberFamily));
//            return mapToReturn;
//        }
//        mapToReturn.put(CHECK_WITH, "DOB");
//        mapToReturn.put(MEMBER_LIST, retrieveMemberDataBeansByFamilyId(familyId));
//        return mapToReturn;
//    }

//    @Override
//    public Boolean checkUserIsAllowToRegisterInMyTecho(String mobilenNumber, Integer memberId, String familyId, Boolean isUserNotIdentified) {
//        if (isUserNotIdentified != null && isUserNotIdentified) {
//            return CollectionUtils.isEmpty(myTechoUserDao.retrieveByPhone(mobilenNumber, null, true));
//        } else if (!CollectionUtils.isEmpty(myTechoUserDao.retrieveByPhone(mobilenNumber, memberId, false))) {
//            throw new ImtechoMobileException("Not allowed to register,You have already registered In MyTecho+", 501);
//        } else {
//            if (CollectionUtils.isEmpty(myTechoUserDao.checkIfMobileNumberIsExistForAnotherFamily(mobilenNumber, familyId))) {
//                return true;
//            } else {
//                throw new ImtechoMobileException("This Mobile Number already registered with another family!", 501);
//            }
//        }
//    }

//    @Override
//    public void deleteMobileNumberInFamilyExceptVerifiedFamily(String verifiedFamilyId, String mobileNumber) {
//        memberDao.deleteMobileNumberInFamilyExceptVerifiedFamily(verifiedFamilyId, mobileNumber);
//    }

//    @Override
//    public MyTechoFamilyDataBean retrieveImTechoFamilyDataByMemberId(Integer memberId) {
//        MyTechoFamilyDataBean myTechoFamilyDataBean = new MyTechoFamilyDataBean();
//        MemberEntity memberEntity = memberDao.retrieveMemberById(memberId);
//        if (memberEntity == null) {
//            throw new ImtechoMobileException("Member not found", 501);
//        }
//        List<String> basicStates = new ArrayList<>();
//        List<String> familyIds = new ArrayList<>();
//        List<MemberDataBean> memberDataBeans = new ArrayList<>();
//
//        basicStates.add(STATE_VERIFIED);
//        basicStates.add("NEW");
//        basicStates.add(STATE_REVERIFICATION);
//        familyIds.add(memberEntity.getFamilyId());
//
//        List<MemberEntity> memberEntitys = memberDao.getMembers(null, null, null, familyIds, null, basicStates);
//        memberEntitys.forEach(memberModel ->
//                memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberModel))
//        );
//        myTechoFamilyDataBean.setFamilyDataBean(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId()), memberDataBeans));
//        return myTechoFamilyDataBean;
//    }

//    @Override
//    public Integer retrieveFamilyIdByFamilyId(String familyId) {
//        FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(familyId);
//        if (familyEntity == null) {
//            return null;
//        }
//        return familyEntity.getId();
//    }

//    public Map<String, Object> getEqualDobMembers(String familyId, String dob) {
//
//        List<MemberDataBean> memberDataBeans = new ArrayList<>();
//        List<MemberEntity> memberEntitys = memberDao.getEqualDobMembers(familyId, dob);
//        memberEntitys.forEach(memberEntity ->
//                memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity))
//        );
//        Map<String, Object> mapToReturn = new HashMap<>();
//        mapToReturn.put("MEMBERS", memberDataBeans);
//        return mapToReturn;
//    }

}
