package com.argusoft.medplat.nutrition.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.nutrition.dao.*;
import com.argusoft.medplat.nutrition.dto.*;
import com.argusoft.medplat.nutrition.enums.Identification;
import com.argusoft.medplat.nutrition.mapper.*;
import com.argusoft.medplat.nutrition.model.*;
import com.argusoft.medplat.nutrition.service.ChildCmtcNrcScreeningService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.rch.dao.ChildServiceDao;
import com.argusoft.medplat.rch.dto.ChildServiceMasterDto;
import com.argusoft.medplat.rch.mapper.ChildServiceMapper;
import com.argusoft.medplat.rch.model.ChildServiceMaster;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.location.dto.LocationMasterDto;
import com.argusoft.medplat.web.location.mapper.LocationMasterMapper;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Implements methods of ChildCmtcNrcScreeningService
 * </p>
 *
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Service
@Transactional
public class ChildCmtcNrcScreeningServiceImpl implements ChildCmtcNrcScreeningService {

    private static final String NO_SCREENING_MESSAGE = "No screening details exist for given child admission";

    @Autowired
    QueryMasterService queryMasterService;

    @Autowired
    private ChildCmtcNrcAdmissionDao childCmtcNrcAdmissionDao;

    @Autowired
    private ChildCmtcNrcWeightDao childCmtcNrcWeightDao;

    @Autowired
    private ChildCmtcNrcScreeningDao childCmtcNrcScreeningDao;

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private ChildCmtcNrcDischargeDao childCmtcNrcDischargeDao;

    @Autowired
    private ChildCmtcNrcFollowUpDao childCmtcNrcFollowUpDao;

    @Autowired
    private ChildCmtcNrcLaboratoryDao childCmtcNrcLaboratoryDao;

    @Autowired
    private ChildCmtcNrcMoVerificationDao childCmtcNrcMoVerificationDao;

    @Autowired
    private ChildServiceDao childServiceDao;

    @Autowired
    private UserLocationDao userLocationDao;

    @Autowired
    private LocationMasterDao locationMasterDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private ChildNutritionCmamMasterDao childNutritionCmamMasterDao;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private ChildCmtcNrcReferralDao childCmtcNrcReferralDao;

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private ImmunisationService immunisationService;


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChildCmtcNrcAdmission(ChildCmtcNrcAdmissionDto childCmtcNrcAdmission) {
        int updatedRows;
        if (childCmtcNrcAdmission.getIsDirectAdmission() != null && childCmtcNrcAdmission.getIsDirectAdmission()) {
            ChildCmtcNrcScreening childCmtcNrcScreening = new ChildCmtcNrcScreening();
            childCmtcNrcScreening.setIdentifiedFrom(Identification.DIRECT);
            childCmtcNrcScreening.setChildId(childCmtcNrcAdmission.getChildId());
            childCmtcNrcScreening.setScreenedOn(childCmtcNrcAdmission.getAdmissionDate());
            childCmtcNrcScreening.setState(childCmtcNrcAdmission.getState());
            childCmtcNrcScreening.setLocationId(Integer.valueOf(childCmtcNrcAdmission.getLocationId()));
            childCmtcNrcScreening.setIsDirectAdmission(childCmtcNrcAdmission.getIsDirectAdmission());
            childCmtcNrcScreening.setScreeningCenter(childCmtcNrcAdmission.getScreeningCenter());
            childCmtcNrcScreeningDao.create(childCmtcNrcScreening);
            childCmtcNrcAdmission.setCaseId(childCmtcNrcScreening.getId());
            Integer admissionId = childCmtcNrcAdmissionDao.create(ChildCmtcNrcAdmissionMapper.convertDtoToEntity(childCmtcNrcAdmission));
            updatedRows = childCmtcNrcScreeningDao.updateAdmissionIdInScreeningInfo(childCmtcNrcScreening.getId(), admissionId);
        } else {
            ChildCmtcNrcScreening childCmtcNrcScreening = childCmtcNrcScreeningDao.retrieveById(childCmtcNrcAdmission.getScreeningId());
            if (childCmtcNrcScreening != null) {
                childCmtcNrcScreeningDao.updateScreeningCenter(childCmtcNrcAdmission.getScreeningId(), childCmtcNrcAdmission.getScreeningCenter());
            }
            Integer admissionId = childCmtcNrcAdmissionDao.create(ChildCmtcNrcAdmissionMapper.convertDtoToEntity(childCmtcNrcAdmission));
            updatedRows = childCmtcNrcScreeningDao.updateAdmissionIdInScreeningInfo(childCmtcNrcAdmission.getScreeningId(), admissionId);
        }
        MemberEntity memberEntity = memberDao.retrieveMemberById(childCmtcNrcAdmission.getChildId());
        memberEntity.setWeight(childCmtcNrcAdmission.getWeightAtAdmission());
        memberEntity.setChildNrcCmtcStatus(ConstantUtil.CMTC_ACTIVE_STATE);
        updateMemberAdditionalInfoFromAdmission(memberEntity, childCmtcNrcAdmission);
        memberDao.update(memberEntity);
        if (updatedRows == 0) {
            throw new ImtechoUserException(NO_SCREENING_MESSAGE, 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcScreeningDto retrieveScreeningDetails(Integer childId) {
        ChildCmtcNrcScreeningDto screeningDto = new ChildCmtcNrcScreeningDto();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_screening_details");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("childId", childId);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (resultQueryDto.get(0).getResult().size() > 1) {
            throw new ImtechoUserException("Multiple Active Case Found For the child", 1);
        } else if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setServerDate(new Date());
                cmtcNrcScreeningDto.setId(Integer.valueOf(aResult.get("screeningId").toString()));
                cmtcNrcScreeningDto.setChildId(Integer.valueOf(aResult.get("childId").toString()));
                cmtcNrcScreeningDto.setScreenedOn((Date) aResult.get("screenedOn"));
                cmtcNrcScreeningDto.setState(aResult.get("state").toString());
                aResult.remove("screenedOn");
                aResult.remove("state");
                aResult.remove("childId");

                if (aResult.get("admissionId") != null) {
                    cmtcNrcScreeningDto.setAdmissionId(Integer.valueOf(aResult.get("admissionId").toString()));
                    aResult.remove("admissionId");
                }

                if (aResult.get("dischargeId") != null) {
                    cmtcNrcScreeningDto.setDischargeId(Integer.valueOf(aResult.get("dischargeId").toString()));
                    aResult.remove("dischargeId");
                }

                cmtcNrcScreeningDto.setResult(aResult);
                screeningDto = cmtcNrcScreeningDto;
            }
        }
        return screeningDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcScreeningDto> retrieveAllScreenedChildren(ImtechoSecurityUser user, Integer limit, Integer offset) {
        List<ChildCmtcNrcScreeningDto> screeningDtos = new LinkedList<>();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_screened_list");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(ChildCmtcNrcScreening.Fields.USER_ID, user.getId());
        if (limit != null && offset != null) {
            parameters.put(ChildCmtcNrcScreening.Fields.LIMIT, limit);
            parameters.put(ChildCmtcNrcScreening.Fields.OFFSET, offset);
        }
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setResult(aResult);
                screeningDtos.add(cmtcNrcScreeningDto);
            }
        }
        return screeningDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcScreeningDto> retrieveAllReferredChildren(ImtechoSecurityUser user, Integer limit, Integer offset) {
        List<ChildCmtcNrcScreeningDto> screeningDtos = new LinkedList<>();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_referred_list");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(ChildCmtcNrcScreening.Fields.USER_ID, user.getId());
        if (limit != null && offset != null) {
            parameters.put(ChildCmtcNrcScreening.Fields.LIMIT, limit);
            parameters.put(ChildCmtcNrcScreening.Fields.OFFSET, offset);
        }
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setResult(aResult);
                screeningDtos.add(cmtcNrcScreeningDto);
            }
        }
        return screeningDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcScreeningDto> retrieveTreatmentCompletedChildren(ImtechoSecurityUser user, Integer limit, Integer offset) {
        List<ChildCmtcNrcScreeningDto> screeningDtos = new LinkedList<>();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_treatment_completed_list");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(ChildCmtcNrcScreening.Fields.USER_ID, user.getId());
        parameters.put(ChildCmtcNrcScreening.Fields.LIMIT, limit);
        parameters.put(ChildCmtcNrcScreening.Fields.OFFSET, offset);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setResult(aResult);
                screeningDtos.add(cmtcNrcScreeningDto);
            }
        }
        return screeningDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcScreeningDto> retrieveAllAdmittedChildren(ImtechoSecurityUser user, Integer limit, Integer offset) {
        List<ChildCmtcNrcScreeningDto> screeningDtos = new LinkedList<>();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_admission_list");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(ChildCmtcNrcScreening.Fields.USER_ID, user.getId());
        parameters.put(ChildCmtcNrcScreening.Fields.LIMIT, limit);
        parameters.put(ChildCmtcNrcScreening.Fields.OFFSET, offset);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setResult(aResult);
                screeningDtos.add(cmtcNrcScreeningDto);
            }
        }
        return screeningDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcScreeningDto> retrieveAllDefaulterChildren(ImtechoSecurityUser user, Integer limit, Integer offset) {
        List<ChildCmtcNrcScreeningDto> screeningDtos = new LinkedList<>();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_defaulter_list");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(ChildCmtcNrcScreening.Fields.USER_ID, user.getId());
        parameters.put(ChildCmtcNrcScreening.Fields.LIMIT, limit);
        parameters.put(ChildCmtcNrcScreening.Fields.OFFSET, offset);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setResult(aResult);
                screeningDtos.add(cmtcNrcScreeningDto);
            }
        }
        return screeningDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcScreeningDto> retrieveAllDischargedChildren(ImtechoSecurityUser user, Integer limit, Integer offset) {
        List<ChildCmtcNrcScreeningDto> screeningDtos = new LinkedList<>();
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("child_cmtc_nrc_discharge_list");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(ChildCmtcNrcScreening.Fields.USER_ID, user.getId());
        parameters.put(ChildCmtcNrcScreening.Fields.LIMIT, limit);
        parameters.put(ChildCmtcNrcScreening.Fields.OFFSET, offset);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            List<LinkedHashMap<String, Object>> result = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> aResult : result) {
                ChildCmtcNrcScreeningDto cmtcNrcScreeningDto = new ChildCmtcNrcScreeningDto();
                cmtcNrcScreeningDto.setResult(aResult);
                screeningDtos.add(cmtcNrcScreeningDto);
            }
        }
        return screeningDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createChildCmtcNrcWeightEntry(List<ChildCmtcNrcWeightDto> childCmtcNrcWeight) {
        for (ChildCmtcNrcWeightDto childCmtcNrcWeightDto : childCmtcNrcWeight) {
            if (childCmtcNrcWeightDao.checkIfEntryForWeightExists(childCmtcNrcWeightDto.getAdmissionId(), childCmtcNrcWeightDto.getWeightDate())) {
                childCmtcNrcWeightDao.create(ChildCmtcNrcWeightMapper.convertDtoToEntity(childCmtcNrcWeightDto));
            } else {
                throw new ImtechoUserException("Weight is already recorded for the given date", 101);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcWeightDto> getCmtcNrcWeighDtosFromAdmissionId(Integer admissionId) {
        List<ChildCmtcNrcWeightDto> weightDtos = new ArrayList<>();
        List<ChildCmtcNrcWeight> childCmtcNrcWeights = childCmtcNrcWeightDao.getCmtcNrcWeightEntitiesFromAdmissionId(admissionId);
        for (ChildCmtcNrcWeight cmtcNrcWeight : childCmtcNrcWeights) {
            weightDtos.add(ChildCmtcNrcWeightMapper.convertEntityToDto(cmtcNrcWeight));
        }
        return weightDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcAdmissionDto retrieveAdmissionDetailById(Integer id) {
        ChildCmtcNrcAdmission childCmtcNrcAdmission = childCmtcNrcAdmissionDao.retrieveById(id);
        return ChildCmtcNrcAdmissionMapper.convertEntityToDto(childCmtcNrcAdmission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcSdScoreDto getSdScore(String gender, Integer height, Float weight) {
        ChildCmtcNrcSdScoreDto childCmtcNrcSdScoreDto = new ChildCmtcNrcSdScoreDto();
        childCmtcNrcSdScoreDto.setSdScore(childCmtcNrcScreeningDao.getSdScore(gender, height, weight));
        return childCmtcNrcSdScoreDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDeathDetails(ChildCmtcNrcAdmissionDto admissionDto, ImtechoSecurityUser user) {
        if (admissionDto.getId() != null) {
            ChildCmtcNrcAdmission childCmtcNrcAdmission = childCmtcNrcAdmissionDao.retrieveById(admissionDto.getId());
            childCmtcNrcAdmission.setDeathDate(admissionDto.getDeathDate());
            childCmtcNrcAdmission.setDeathReason(admissionDto.getDeathReason());
            childCmtcNrcAdmission.setDeathPlace(admissionDto.getDeathPlace());
            childCmtcNrcAdmission.setOtherDeathPlace(admissionDto.getOtherDeathPlace());
            childCmtcNrcAdmission.setOtherDeathReason(admissionDto.getOtherDeathReason());
            childCmtcNrcAdmissionDao.update(childCmtcNrcAdmission);
            int updatedRows = childCmtcNrcScreeningDao.updateDeathStatusInScreeningInfo(admissionDto.getScreeningId());
            MemberEntity memberEntity = memberDao.retrieveMemberById(childCmtcNrcAdmission.getChildId());
            memberEntity.setChildNrcCmtcStatus(ConstantUtil.CMTC_DEATH_STATE);
            memberDao.update(memberEntity);
            memberDao.flush();
            Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(memberEntity.getId());
            if (Boolean.TRUE.equals(abBoolean)) {
                throw new ImtechoUserException("Member with Health ID " + memberEntity.getUniqueHealthId() + " is already marked DEAD. "
                        + "You cannot mark a DEAD member DEAD again.", 1);
            } else {
                long dateOfDeath = admissionDto.getDeathDate().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                String deathPlace = admissionDto.getDeathPlace();
                FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
                QueryDto queryDto = new QueryDto();
                queryDto.setCode("mark_member_as_death");
                LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                parameters.put("member_id", memberEntity.getId());
                if (familyEntity.getAreaId() != null) {
                    parameters.put("location_id", familyEntity.getAreaId());
                } else {
                    parameters.put("location_id", familyEntity.getLocationId());
                }
                parameters.put("action_by", user.getId());
                parameters.put("family_id", familyEntity.getId());
                parameters.put("death_date", sdf.format(new Date(dateOfDeath)));
                parameters.put("place_of_death", deathPlace);
                if (!admissionDto.getDeathReason().equals("OTHER")) {
                    parameters.put("death_reason", admissionDto.getDeathReason());
                    parameters.put("other_death_reason", null);
                } else {
                    parameters.put("death_reason", null);
                    parameters.put("other_death_reason", admissionDto.getOtherDeathReason());
                }
                parameters.put("service_type", "CMTC");
                parameters.put("reference_id", childCmtcNrcAdmission.getId());
                queryDto.setParameters(parameters);
                List<QueryDto> queryDtos = new LinkedList<>();
                queryDtos.add(queryDto);
                queryMasterService.executeQuery(queryDtos, true);
            }
            if (updatedRows == 0) {
                throw new ImtechoUserException(NO_SCREENING_MESSAGE, 101);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDischargeDetails(ChildCmtcNrcDischargeDto dischargeDto) {
        int updatedRows = 0;
        ChildCmtcNrcDischarge childCmtcNrcDischarge = ChildCmtcNrcDischargeMapper.convertDtoToEntity(dischargeDto);
        childCmtcNrcDischargeDao.create(childCmtcNrcDischarge);
        if (dischargeDto.getHigherFacilityReferralPlace() != null) {
            ChildCmtcNrcScreening childCmtcNrcScreening = childCmtcNrcScreeningDao.retrieveByAdmissionId(dischargeDto.getAdmissionId());
            ChildCmtcNrcAdmission childCmtcNrcAdmission = childCmtcNrcAdmissionDao.retrieveById(dischargeDto.getAdmissionId());
            if (childCmtcNrcAdmission.getScreeningCenter() != null) {
                ChildCmtcNrcReferralDetail childCmtcNrcReferralDetail = new ChildCmtcNrcReferralDetail();
                childCmtcNrcReferralDetail.setChildId(childCmtcNrcAdmission.getChildId());
                childCmtcNrcReferralDetail.setAdmissionId(childCmtcNrcAdmission.getId());
                childCmtcNrcReferralDetail.setReferredFrom(childCmtcNrcAdmission.getScreeningCenter());
                childCmtcNrcReferralDetail.setReferredTo(dischargeDto.getHigherFacilityReferralPlace());
                childCmtcNrcReferralDetail.setReferredDate(new Date());
                childCmtcNrcReferralDao.create(childCmtcNrcReferralDetail);
                updatedRows = childCmtcNrcScreeningDao.updateHigherFacilityReferralDetails(childCmtcNrcScreening.getId(), childCmtcNrcAdmission.getScreeningCenter(), dischargeDto.getHigherFacilityReferralPlace());
            }
        } else {
            updatedRows = childCmtcNrcScreeningDao.updateDischargeIdInScreeningInfo(dischargeDto.getScreeningId(), childCmtcNrcDischarge.getId());
            MemberEntity memberEntity = memberDao.retrieveMemberById(dischargeDto.getChildId());
            memberEntity.setWeight(dischargeDto.getWeight());
            memberEntity.setChildNrcCmtcStatus(ConstantUtil.CMTC_DISCHARGE_STATE);
            updateMemberAdditionalInfoFromDischarge(memberEntity, dischargeDto);
            memberDao.update(memberEntity);
            eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FSAM_TO_CMAM, dischargeDto.getScreeningId()));
        }
        if (updatedRows == 0) {
            throw new ImtechoUserException(NO_SCREENING_MESSAGE, 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDefaulterDetails(ChildCmtcNrcAdmissionDto admissionDto) {
        if (admissionDto.getId() != null) {
            ChildCmtcNrcAdmission childCmtcNrcAdmission = childCmtcNrcAdmissionDao.retrieveById(admissionDto.getId());
            childCmtcNrcAdmission.setDefaulterDate(admissionDto.getDefaulterDate());
            childCmtcNrcAdmission.setState(ConstantUtil.CMTC_DEFAULTER_STATE);
            childCmtcNrcAdmissionDao.update(childCmtcNrcAdmission);
            int updatedRows = childCmtcNrcScreeningDao.updateDefualterStateInScreeningInfo(admissionDto.getScreeningId());
            if (updatedRows == 0) {
                throw new ImtechoUserException(NO_SCREENING_MESSAGE, 101);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLaboratoryTests(ChildCmtcNrcLaboratoryDto childCmtcNrcLaboratoryDto) {
        if (childCmtcNrcLaboratoryDto.getId() != null) {
            ChildCmtcNrcLaboratory childCmtcNrcLaboratory = childCmtcNrcLaboratoryDao.retrieveById(childCmtcNrcLaboratoryDto.getId());
            childCmtcNrcLaboratory.setHemoglobin(childCmtcNrcLaboratoryDto.getHemoglobin());
            childCmtcNrcLaboratory.setPsForMp(childCmtcNrcLaboratoryDto.getPsForMp());
            childCmtcNrcLaboratory.setPsForMpValue(childCmtcNrcLaboratoryDto.getPsForMpValue());
            childCmtcNrcLaboratory.setMonotouxTest(childCmtcNrcLaboratoryDto.getMonotouxTest());
            childCmtcNrcLaboratory.setXrayChest(childCmtcNrcLaboratoryDto.getXrayChest());
            childCmtcNrcLaboratory.setUrinePusCells(childCmtcNrcLaboratoryDto.getUrinePusCells());
            childCmtcNrcLaboratory.setUrineAlbumin(childCmtcNrcLaboratoryDto.getUrineAlbumin());
            if (childCmtcNrcLaboratoryDto.getHiv() != null) {
                childCmtcNrcLaboratory.setHiv(childCmtcNrcLaboratoryDto.getHiv());
            }
            if (childCmtcNrcLaboratoryDto.getSickle() != null) {
                childCmtcNrcLaboratory.setSickle(childCmtcNrcLaboratoryDto.getSickle());
            }
            if (childCmtcNrcLaboratoryDto.getBloodGroup() != null) {
                childCmtcNrcLaboratory.setBloodGroup(childCmtcNrcLaboratoryDto.getBloodGroup());
            }
            childCmtcNrcLaboratory.setTestOutputState(ConstantUtil.CMTC_TEST_OUTPUT_COMPLETED_STATE);
            childCmtcNrcLaboratoryDao.update(childCmtcNrcLaboratory);
        } else {
            ChildCmtcNrcLaboratory childCmtcNrcLaboratory = ChildCmtcNrcLaboratoryMapper.convertDtoToEntity(childCmtcNrcLaboratoryDto);
            childCmtcNrcLaboratory.setTestOutputState(ConstantUtil.CMTC_TEST_OUTPUT_PENDING_STATE);
            childCmtcNrcLaboratoryDao.create(childCmtcNrcLaboratory);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMedicines(ChildCmtcNrcWeightDto childCmtcNrcWeightDto) {
        if (childCmtcNrcWeightDao.checkIfEntryForWeightExists(childCmtcNrcWeightDto.getAdmissionId(), childCmtcNrcWeightDto.getWeightDate())) {
            ChildCmtcNrcAdmission childCmtcNrcAdmission = childCmtcNrcAdmissionDao.retrieveById(childCmtcNrcWeightDto.getAdmissionId());
            ChildCmtcNrcScreening childCmtcNrcScreening = childCmtcNrcScreeningDao.retrieveByAdmissionId(childCmtcNrcWeightDto.getAdmissionId());
            List<ChildCmtcNrcWeight> childCmtcNrcWeights = childCmtcNrcWeightDao.getCmtcNrcWeightEntitiesFromAdmissionId(childCmtcNrcWeightDto.getAdmissionId());
            setWeightGainForChild(childCmtcNrcWeightDto, childCmtcNrcWeights, childCmtcNrcAdmission);
            childCmtcNrcWeightDto.setChildId(childCmtcNrcAdmission.getChildId());
            ChildCmtcNrcWeight childCmtcNrcWeight = ChildCmtcNrcWeightMapper.convertDtoToEntity(childCmtcNrcWeightDto);
            childCmtcNrcWeightDao.create(childCmtcNrcWeight);
            if (childCmtcNrcWeightDto.getIsAmoxicillin()) {
                if (childCmtcNrcAdmission.getNoOfTimesAmoxicillinGiven() != null) {
                    childCmtcNrcAdmission.setNoOfTimesAmoxicillinGiven(childCmtcNrcAdmission.getNoOfTimesAmoxicillinGiven() + 1);
                } else {
                    childCmtcNrcAdmission.setNoOfTimesAmoxicillinGiven(1);
                }
            }
            setHigherFacilityReferralPlaceForScreening(childCmtcNrcWeightDto, childCmtcNrcAdmission);
            childCmtcNrcAdmissionDao.update(childCmtcNrcAdmission);
            MemberEntity memberEntity = memberDao.retrieveMemberById(childCmtcNrcAdmission.getChildId());
            FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
            if (childCmtcNrcWeightDto.getIsVitaminA()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String immunisation = "";
                ImmunisationMaster immunisationMaster = new ImmunisationMaster(familyEntity.getId(),
                        memberEntity.getId(), MobileConstantUtil.CHILD_BENEFICIARY,
                        "NUTRITION", childCmtcNrcWeight.getId(), null,
                        "VITAMIN_A", childCmtcNrcWeightDto.getWeightDate(),
                        user.getId(), childCmtcNrcScreening.getLocationId(),
                        null);
                immunisationService.createImmunisationMaster(immunisationMaster);
                if (memberEntity.getImmunisationGiven() != null && memberEntity.getImmunisationGiven().length() > 0) {
                    StringBuilder sb = new StringBuilder(memberEntity.getImmunisationGiven());
                    sb.append(MobileConstantUtil.IMMUNISATION_NAME_SEPARATOR);
                    sb.append("VITAMIN_A");
                    sb.append(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR);
                    sb.append(sdf.format(childCmtcNrcWeightDto.getWeightDate()));
                    immunisation = sb.toString().replace(" ", "");
                } else {
                    immunisation = "VITAMIN_A".concat(MobileConstantUtil.IMMUNISATION_DATE_SEPARATOR).concat(sdf.format(childCmtcNrcWeightDto.getWeightDate()));
                }
                memberEntity.setImmunisationGiven(immunisation);
            }
            memberEntity.setWeight(childCmtcNrcWeightDto.getWeight());
            updateMemberAdditionalInfoFromDailyWeight(memberEntity, childCmtcNrcWeightDto);
            memberDao.update(memberEntity);
        } else {
            throw new ImtechoUserException("Weight is already recorded for the given date", 101);
        }
    }

    /**
     * Set Weight Gain for child
     *
     * @param childCmtcNrcWeightDto Child Weight Object from WEB
     * @param childCmtcNrcWeights   Child Weight Entity
     * @param childCmtcNrcAdmission Child Admission Entity
     */
    private void setWeightGainForChild(ChildCmtcNrcWeightDto childCmtcNrcWeightDto, List<ChildCmtcNrcWeight> childCmtcNrcWeights, ChildCmtcNrcAdmission childCmtcNrcAdmission) {
        if (childCmtcNrcWeights.size() > 0) {
            if (childCmtcNrcWeightDto.getWeight() >= (childCmtcNrcWeights.get(0).getWeight() + (childCmtcNrcWeights.get(0).getWeight() * 0.005))) {
                childCmtcNrcWeightDto.setWeightGain5Gm1Day(Boolean.TRUE);
            } else {
                childCmtcNrcWeightDto.setWeightGain5Gm1Day(Boolean.FALSE);
            }
            if (childCmtcNrcWeights.get(0).getWeightGain5Gm1Day() != null) {
                childCmtcNrcWeightDto.setWeightGain5Gm2Day(childCmtcNrcWeights.get(0).getWeightGain5Gm1Day());
            }
            if (childCmtcNrcWeights.get(0).getWeightGain5Gm2Day() != null) {
                childCmtcNrcWeightDto.setWeightGain5Gm3Day(childCmtcNrcWeights.get(0).getWeightGain5Gm2Day());
            }
            if (childCmtcNrcWeightDto.getWeightGain5Gm1Day() != null && childCmtcNrcWeightDto.getWeightGain5Gm1Day()
                    && childCmtcNrcWeightDto.getWeightGain5Gm2Day() != null && childCmtcNrcWeightDto.getWeightGain5Gm2Day()
                    && childCmtcNrcWeightDto.getWeightGain5Gm3Day() != null && childCmtcNrcWeightDto.getWeightGain5Gm3Day()) {
                childCmtcNrcAdmission.setConsecutive3DaysWeightGain(Boolean.TRUE);
            } else {
                childCmtcNrcAdmission.setConsecutive3DaysWeightGain(Boolean.FALSE);
            }
        } else {
            childCmtcNrcAdmission.setConsecutive3DaysWeightGain(Boolean.FALSE);
        }
    }

    /**
     * Set higher facility referral place for child screening.
     *
     * @param childCmtcNrcWeightDto An instance of ChildCmtcNrcWeightDto.
     * @param childCmtcNrcAdmission An instance of ChildCmtcNrcAdmission.
     */
    private void setHigherFacilityReferralPlaceForScreening(ChildCmtcNrcWeightDto childCmtcNrcWeightDto, ChildCmtcNrcAdmission childCmtcNrcAdmission) {
        if (childCmtcNrcWeightDto.getHigherFacilityReferralPlace() != null) {
            ChildCmtcNrcScreening childCmtcNrcScreening = childCmtcNrcScreeningDao.retrieveByAdmissionId(childCmtcNrcWeightDto.getAdmissionId());
            if (childCmtcNrcAdmission.getScreeningCenter() != null) {
                ChildCmtcNrcReferralDetail childCmtcNrcReferralDetail = new ChildCmtcNrcReferralDetail();
                childCmtcNrcReferralDetail.setChildId(childCmtcNrcAdmission.getChildId());
                childCmtcNrcReferralDetail.setAdmissionId(childCmtcNrcAdmission.getId());
                childCmtcNrcReferralDetail.setReferredFrom(childCmtcNrcAdmission.getScreeningCenter());
                childCmtcNrcReferralDetail.setReferredTo(childCmtcNrcWeightDto.getHigherFacilityReferralPlace());
                childCmtcNrcReferralDetail.setReferredDate(new Date());
                childCmtcNrcReferralDao.create(childCmtcNrcReferralDetail);
                int updatedRows = childCmtcNrcScreeningDao.updateHigherFacilityReferralDetails(childCmtcNrcScreening.getId(), childCmtcNrcAdmission.getScreeningCenter(), childCmtcNrcWeightDto.getHigherFacilityReferralPlace());
                if (updatedRows == 0) {
                    throw new ImtechoUserException(NO_SCREENING_MESSAGE, 101);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFollowUp(ChildCmtcNrcFollowUpDto childCmtcNrcFollowUpDto) {
        ChildCmtcNrcFollowUp childCmtcNrcFollowUp = ChildCmtcNrcFollowUpMapper.convertDtoToEntity(childCmtcNrcFollowUpDto);
        if (childCmtcNrcFollowUp.getFollowUpVisit() != null && childCmtcNrcFollowUp.getFollowUpVisit() == 3) {
            childCmtcNrcScreeningDao.updateCompletedProgramInScreening(childCmtcNrcFollowUpDto.getCaseId());
        }
        childCmtcNrcFollowUpDao.create(childCmtcNrcFollowUp);
        MemberEntity memberEntity = memberDao.retrieveMemberById(childCmtcNrcFollowUpDto.getChildId());
        memberEntity.setWeight(childCmtcNrcFollowUpDto.getWeight());
        updateMemberAdditionalInfoFromFollowUp(memberEntity, childCmtcNrcFollowUpDto);
        memberDao.update(memberEntity);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcFollowUpDto getLastFollowUpVisit(Integer childId, Integer admissionId) {
        if (admissionId == null) {
            admissionId = childCmtcNrcScreeningDao.retrieveAdmisionIdByChildId(childId);
        }
        if (admissionId != null) {
            ChildCmtcNrcFollowUp childCmtcNrcFollowUp = childCmtcNrcFollowUpDao.getLastFollowUpVisit(childId, admissionId);
            if (childCmtcNrcFollowUp != null) {
                return ChildCmtcNrcFollowUpMapper.convertEntityToDto(childCmtcNrcFollowUp);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcDischargeDto getDischargeDetails(Integer dischargeId) {
        ChildCmtcNrcDischarge childCmtcNrcDischarge = childCmtcNrcDischargeDao.retrieveById(dischargeId);
        return ChildCmtcNrcDischargeMapper.convertEntityToDto(childCmtcNrcDischarge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildServiceMasterDto retrieveRchChildServiceDetailsByMemberId(Integer childId) {
        List<ChildServiceMaster> childServiceMaster = childServiceDao.retrieveByMemberId(childId, 1);
        if (!childServiceMaster.isEmpty()) {
            return ChildServiceMapper.convertEntityToDto(childServiceMaster.get(0));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMasterDto> getBlocksAssignedForMoVerification(Integer userId) {
        List<LocationMaster> locationMasters = locationMasterDao.retrieveBlockLocationsByUserId(userId);
        return LocationMasterMapper.getLocationMasterDtoList(locationMasters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createChildScreening(ChildCmtcNrcScreeningDto childCmtcNrcScreeningDto) {
        ChildCmtcNrcScreening childCmtcNrcScreening = ChildCmtcNrcScreeningMapper.convertEntityToDto(childCmtcNrcScreeningDto);
        ChildCmtcNrcScreening childById = childCmtcNrcScreeningDao.retrieveByChildId(childCmtcNrcScreeningDto.getChildId());
        if (childById == null || (childById.getIsCaseCompleted() != null && childById.getIsCaseCompleted())) {
            childCmtcNrcScreeningDao.create(childCmtcNrcScreening);
        } else {
            throw new ImtechoUserException("Child already exists", 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String checkAdmissionIndicator(Integer childId, String admissionDate) throws ParseException {
        ChildCmtcNrcScreening childCmtcNrcScreening = childCmtcNrcScreeningDao.retrieveByChildId(childId);
        if (childCmtcNrcScreening != null && childCmtcNrcScreening.getAdmissionId() != null) {
            ChildCmtcNrcAdmission childCmtcNrcAdmission = childCmtcNrcAdmissionDao.retrieveById(childCmtcNrcScreening.getAdmissionId());
            if (childCmtcNrcScreening.getState().equals(ConstantUtil.CMTC_DEFAULTER_STATE)) {
                return retrieveStateForCMTCDefault(childCmtcNrcAdmission, admissionDate);
            } else if (childCmtcNrcScreening.getState().equals(ConstantUtil.CMTC_DISCHARGE_STATE)) {
                return retrieveStateForCMTCDischarge(childCmtcNrcAdmission, admissionDate);
            } else {
                return ConstantUtil.CMTC_WEB_NEW_ADMISSION;
            }
        } else {
            return ConstantUtil.CMTC_WEB_NEW_ADMISSION;
        }
    }

    /**
     * Retrieves state for CMTC defaulter.
     *
     * @param childCmtcNrcAdmission An instance of childCmtcNrcAdmission.
     * @param admissionDate         Admission date.
     * @return Returns state for CMTC defaulter.
     * @throws ParseException Define date parse exception.
     */
    private String retrieveStateForCMTCDefault(ChildCmtcNrcAdmission childCmtcNrcAdmission, String admissionDate) throws ParseException {
        if (getDifferenceInDays(childCmtcNrcAdmission.getAdmissionDate(), new SimpleDateFormat("dd/MM/yyyy").parse(admissionDate)) <= 60) {
            return ConstantUtil.CMTC_WEB_RE_ADMISSION;
        } else {
            return ConstantUtil.CMTC_WEB_NEW_ADMISSION;
        }
    }

    /**
     * Retrieves state for CMTC discharge.
     *
     * @param childCmtcNrcAdmission An instance of childCmtcNrcAdmission.
     * @param admissionDate         Admission date.
     * @return Returns state for CMTC discharge.
     * @throws ParseException Define date parse exception.
     */
    private String retrieveStateForCMTCDischarge(ChildCmtcNrcAdmission childCmtcNrcAdmission, String admissionDate) throws ParseException {
        if (getDifferenceInDays(childCmtcNrcAdmission.getAdmissionDate(), new SimpleDateFormat("dd/MM/yyyy").parse(admissionDate)) <= 60) {
            return ConstantUtil.CMTC_WEB_RELAPSE;
        } else {
            return ConstantUtil.CMTC_WEB_NEW_ADMISSION;
        }
    }

    private Long getDifferenceInDays(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> retrieveAshaPhoneNumber(Integer memberId) {
        return memberDao.retrieveAshaPhoneNumberByMemberId(memberId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcLaboratoryDto retrieveLastLaboratoryTest(Integer admissionId) {
        ChildCmtcNrcLaboratory childCmtcNrcLaboratory = childCmtcNrcLaboratoryDao.retrieveLastLaboratoryTest(admissionId);
        if (childCmtcNrcLaboratory != null) {
            return ChildCmtcNrcLaboratoryMapper.convertEntityToDto(childCmtcNrcLaboratory);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildCmtcNrcLaboratoryDto> getCmtcNrcLaboratoryDtosFromAdmissionId(Integer admissionId) {
        List<ChildCmtcNrcLaboratoryDto> laboratoryDtos = new LinkedList<>();
        List<ChildCmtcNrcLaboratory> childCmtcNrcLaboratorys = childCmtcNrcLaboratoryDao.getCmtcNrcLaboratoryEntitiesFromAdmissionId(admissionId);

        for (ChildCmtcNrcLaboratory childCmtcNrcLaboratory : childCmtcNrcLaboratorys) {
            laboratoryDtos.add(ChildCmtcNrcLaboratoryMapper.convertEntityToDto(childCmtcNrcLaboratory));
        }
        return laboratoryDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMoVerification(ChildCmtcNrcMoVerificationDto childCmtcNrcMoVerificationDto) {
        childCmtcNrcMoVerificationDao.create(ChildCmtcNrcMoVerificationMapper.convertDtoToEntity(childCmtcNrcMoVerificationDto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteChildScreeningByChildId(Integer childId) {
        childCmtcNrcScreeningDao.deleteChildScreeningByChildId(childId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAdmissionValidity(Integer childId) {
        ChildCmtcNrcScreening childCmtcNrcScreening = childCmtcNrcScreeningDao.checkAdmissionValidity(childId);
        if (childCmtcNrcScreening != null) {
            if (childCmtcNrcScreening.getState().equals(ConstantUtil.CMTC_DEATH_STATE)) {
                throw new ImtechoUserException("Child is marked as Dead", 1);
            } else {
                throw new ImtechoUserException("Child already admitted", 1);
            }
        }
        ChildNutritionCmamMaster childNutritionCmamMaster = childNutritionCmamMasterDao.retrieveActiveChildByChildId(childId);
        if (childNutritionCmamMaster != null) {
            throw new ImtechoUserException("Child is already under CMAM program", 1);
        }
    }

    private void updateMemberAdditionalInfoFromAdmission(MemberEntity member, ChildCmtcNrcAdmissionDto admissionDto) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (admissionDto.getHeight() != null) {
            memberAdditionalInfo.setHeight(admissionDto.getHeight());
        }
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromDailyWeight(MemberEntity member, ChildCmtcNrcWeightDto weightDto) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (weightDto.getHeight() != null) {
            memberAdditionalInfo.setHeight(weightDto.getHeight());
        }
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromDischarge(MemberEntity member, ChildCmtcNrcDischargeDto dischargeDto) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (dischargeDto.getHeight() != null) {
            memberAdditionalInfo.setHeight(dischargeDto.getHeight());
        }
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    private void updateMemberAdditionalInfoFromFollowUp(MemberEntity member, ChildCmtcNrcFollowUpDto followUpDto) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (followUpDto.getHeight() != null) {
            memberAdditionalInfo.setHeight(followUpDto.getHeight());
        }
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

}
