/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.cfhc.service.impl;

import com.argusoft.medplat.cfhc.dao.MemberCFHCDao;
import com.argusoft.medplat.cfhc.model.MemberCFHCEntity;
import com.argusoft.medplat.cfhc.service.CFHCService;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Implements methods of CFHCService</p>
 *
 * @author rahul
 * @since 25/08/20 4:30 PM
 */
@Service
@Transactional
public class CFHCServiceImpl implements CFHCService {

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;

    @Autowired
    private LocationMasterDao locationMasterDao;

    @Autowired
    private QueryMasterService queryMasterService;

    @Autowired
    private MemberCFHCDao memberCFHCDao;

    /**
     * Returns keys for family questions
     *
     * @return list of string
     */
    private List<String> getKeysForFamilyQuestions() {
        List<String> keys = new ArrayList<>();
        keys.add("10001");      //Comments for reverification
        keys.add("10000");      //Family Number
        keys.add("1");          //Family Number
        keys.add("2");          //Head of the family
        keys.add("3");          //Members Info
        keys.add("4");          //Is the family available?
        keys.add("5");          //Select Subcenter/UHC where the family belongs
        keys.add("6");          //Select village/sector where the family belongs
        keys.add("10004");      //You are about to register a new family
        keys.add("7");          //House Number
        keys.add("8");          //Address Line 1
        keys.add("9");          //Address Line 2
        keys.add("100");        //Hidden Question to check if the village has sub areas
        keys.add("11");         //Select area of village
        keys.add("12");         //Family Religion
        keys.add("13");         //Family Caste
        keys.add("14");         //Is it a seasonal migrant family?
        keys.add("15");         //Is it a vulnerable family?
        keys.add("16");         //Is the family below poverty level?
        keys.add("17");         //Is toilet available in the house?
        keys.add("114");        //Hidden Question to check bpl flag
        keys.add("1005");       //Enter Below poverty level (BPL) number for family
        keys.add("18");         //MAA / MAA Vatsalya Card Number
        keys.add("19");         //PMJAY Card Number
        keys.add("20");         //Ration Card Number
        keys.add("101");        //Hidden Question to check if the village has anganwadis
        keys.add("102");        //Hidden Question to add all option in SubCentre List
        keys.add("21");         //Select Sub-Centre For Anganwadi
        keys.add("22");         //Anganwadi ID
        keys.add("1000");       //Now begins the details about every member of the family
        keys.add("109");        //Hidden to check if any Member less than 20 exists
        keys.add("59");         //Please select mother for these members
        keys.add("110");        //Hidden to check if any female married members exists
        keys.add("60");         //Please select husband for these members
        keys.add("9997");       //Do you want to submit or review the data?
        keys.add("9999");       //CFHC form is complete.
        keys.add("-3");         //Location Id
        keys.add("-8");         //Mobile Start Date
        keys.add("-9");         //Mobile End Date
        keys.add("-1");         //Longitude
        keys.add("-2");         //Latitude
        keys.add("405");        //Are family providing consent to participate in the comprehensive primary care research?
        keys.add("406");        //Vulnerability Criteria
        keys.add("411");        //Is this family eligible for the PMJAY?
        keys.add("6022");       // Vehicle Details
        keys.add("6025");       // Other type of house
        keys.add("6004");       // Type of house
        keys.add("6007");       // Availability of toilet
        keys.add("6001");       // Source of drinking water
        keys.add("6002");       // Type of fuel
        keys.add("6003");       // Electricity availability
        return keys;
    }

    private List<String> getKeysForDeadMembersQuestions() {
        List<String> keys = new ArrayList<>();
        keys.add("8053");
        keys.add("8054");
        keys.add("8055");
        keys.add("8056");
        keys.add("8057");
        keys.add("8058");
        keys.add("8059");
        return keys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> storeComprehensiveFamilyHealthCensusForm(ParsedRecordBean parsedRecordBean, UserMaster user) {
        Map<String, String> returnMap = new LinkedHashMap<>();
        boolean isNewFamily = false;
        List<String> keysForFamilyQuestions = this.getKeysForFamilyQuestions();
        List<String> keysForDeadMembersQuestions = this.getKeysForDeadMembersQuestions();
        Map<String, String> keyAndAnswerMap = new LinkedHashMap<>();
        Map<String, String> familyKeyAndAnswerMap = new LinkedHashMap<>();
        Map<String, String> memberKeyAndAnswerMap = new LinkedHashMap<>();
        Map<String, String> deadMemberKeyAndAnswerMap = new LinkedHashMap<>();
        String familyId;
        String recordString = parsedRecordBean.getAnswerRecord();
        String[] keyAndAnswerSet = recordString.split(MobileConstantUtil.ANSWER_STRING_FIRST_SEPARATER);
        List<String> keyAndAnswerSetList = new ArrayList<>(Arrays.asList(keyAndAnswerSet));
        Set<MemberEntity> membersToBeArchived = new HashSet<>();
        Set<MemberEntity> membersToBeMarkedDead = new HashSet<>();
        Set<MemberEntity> membersDeadLastYear = new HashSet<>();
        MemberEntity maleHofMember = null;
        MemberEntity femaleHofMember = null;
        MemberEntity hofHusbandOrWifeMember = null;
        for (String aKeyAndAnswer : keyAndAnswerSetList) {
            String[] keyAnswerSplit = aKeyAndAnswer.split(MobileConstantUtil.ANSWER_STRING_SECOND_SEPARATER);
            if (keyAnswerSplit.length != 2) {
                continue;
            }
            keyAndAnswerMap.put(keyAnswerSplit[0], keyAnswerSplit[1]);
        }

        if (keyAndAnswerMap.get("405") != null && keyAndAnswerMap.get("405").equals("2")) {
            return returnMap;
        }

        familyId = keyAndAnswerMap.get("1");

        FamilyEntity familyEntity;
        if (familyId == null || familyId.equals("Not available")) {
            familyEntity = new FamilyEntity();
        } else {
            familyEntity = familyDao.retrieveFamilyByFamilyId(familyId);
        }

        if ("3".equals(keyAndAnswerMap.get("4")) && familyEntity.getId() != null) {
            //Archive this family
            familyEntity.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ARCHIVED);
            familyDao.updateFamily(familyEntity);
            returnMap.put("createdInstanceId", familyEntity.getId().toString());
            return returnMap;
        }

        List<MemberEntity> membersInTheFamily = memberDao.retrieveMemberEntitiesByFamilyId(familyEntity.getFamilyId());
        List<MemberCFHCEntity> memberCFHCEntities = memberCFHCDao.retrieveMemberCFHCEntitiesByFamilyId(familyEntity.getId());
        Map<String, MemberEntity> mapOfMemberWithHealthIdAsKey = new LinkedHashMap<>();
        Map<String, MemberEntity> mapOfNewMemberWithLoopIdAsKey = new LinkedHashMap<>();
        Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey = new LinkedHashMap<>();
        Map<String, MemberEntity> mapOfDeadMemberWithLoopIsAsKey = new LinkedHashMap<>();
        Map<String, MemberEntity> mapOfDeadLastYearWithLoopIsAsKey = new LinkedHashMap<>();

        Map<Integer, MemberCFHCEntity> mapOfMemberCFHCWithMemberIdAsKey = new LinkedHashMap<>();
        Map<String, MemberCFHCEntity> mapOfNewMemberCFHCWithLoopIdAsKey = new LinkedHashMap<>();

        String motherChildRelationString = keyAndAnswerMap.get("59");
        String husbandWifeRelationString = keyAndAnswerMap.get("60");


        for (MemberEntity memberEntity : membersInTheFamily) {
            mapOfMemberWithHealthIdAsKey.put(memberEntity.getUniqueHealthId(), memberEntity);
        }

        for (MemberCFHCEntity memberCFHCEntity : memberCFHCEntities) {
            mapOfMemberCFHCWithMemberIdAsKey.put(memberCFHCEntity.getMemberId(), memberCFHCEntity);
        }

        if (familyEntity.getFamilyId() == null) {
            familyEntity.setFamilyId(familyHealthSurveyService.generateFamilyId());
            isNewFamily = true;
        }

        for (Map.Entry<String, String> keyAnswerSet : keyAndAnswerMap.entrySet()) {
            String key = keyAnswerSet.getKey();
            String answer = keyAnswerSet.getValue();
            if (keysForFamilyQuestions.contains(key)) {
                familyKeyAndAnswerMap.put(key, answer);
            } else if (keysForDeadMembersQuestions.contains(key) ||
                    (key.contains(".") && keysForDeadMembersQuestions.contains(key.split("\\.")[0]))) {
                deadMemberKeyAndAnswerMap.put(key, answer);
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

        if (familyEntity.getLocationId() == null) {
            if (familyEntity.getAreaId() != null) {
                familyEntity.setLocationId(locationMasterDao.retrieveById(familyEntity.getAreaId()).getParent());
            } else {
                throw new ImtechoMobileException("Please refresh and try again", 1);
            }
        }

        for (Map.Entry<String, String> entrySet : memberKeyAndAnswerMap.entrySet()) {
            String key = entrySet.getKey();
            String answer = entrySet.getValue();
            MemberEntity memberEntity;
            MemberCFHCEntity memberCFHCEntity;

            if (key.contains(".")) {
                String[] splitKey = key.split("\\.");
                String uniqueHealthId = memberKeyAndAnswerMap.get("23" + "." + splitKey[1]);
                memberEntity = mapOfMemberWithHealthIdAsKey.get(uniqueHealthId);

                if (uniqueHealthId != null && memberEntity == null) {
                    continue;
                }

                if (memberEntity == null) {
                    memberEntity = mapOfNewMemberWithLoopIdAsKey.get(splitKey[1]);
                    memberCFHCEntity = mapOfNewMemberCFHCWithLoopIdAsKey.get(splitKey[1]);
                    if (memberEntity == null) {
                        memberEntity = new MemberEntity();
                        memberCFHCEntity = new MemberCFHCEntity();
                        memberEntity.setFamilyId(familyEntity.getFamilyId());
                        memberEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                        mapOfNewMemberWithLoopIdAsKey.put(splitKey[1], memberEntity);
                        memberCFHCEntity.setMemberId(memberEntity.getId());
                        mapOfNewMemberCFHCWithLoopIdAsKey.put(splitKey[1], memberCFHCEntity);
                        memberEntity.setMemberCFHCEntity(memberCFHCEntity);
                    }
                } else {
                    memberCFHCEntity = mapOfMemberCFHCWithMemberIdAsKey.get(memberEntity.getId());
                    memberEntity.setMemberCFHCEntity(memberCFHCEntity);
                    if (memberCFHCEntity == null) {
                        memberCFHCEntity = new MemberCFHCEntity();
                        memberCFHCEntity.setMemberId(memberEntity.getId());
                        mapOfMemberCFHCWithMemberIdAsKey.put(memberEntity.getId(), memberCFHCEntity);
                        memberEntity.setMemberCFHCEntity(memberCFHCEntity);
                    }
                }

                mapOfMemberWithLoopIdAsKey.putIfAbsent(splitKey[1], memberEntity);

                if (memberKeyAndAnswerMap.get("25" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("25" + "." + splitKey[1]).equals("2")) {
                    membersToBeMarkedDead.add(memberEntity);
                    mapOfDeadMemberWithLoopIsAsKey.put(splitKey[1], memberEntity);
                } else if (memberKeyAndAnswerMap.get("25" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("25" + "." + splitKey[1]).equals("3")) {
                    membersToBeArchived.add(memberEntity);
                } else {
                    this.setAnswersToMemberEntity(splitKey[0], answer, memberEntity, memberCFHCEntity, memberKeyAndAnswerMap, splitKey[1]);
                }

                if (memberKeyAndAnswerMap.get("31" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("31" + "." + splitKey[1]).equals("1")) {
                    if (memberKeyAndAnswerMap.get("36" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("36" + "." + splitKey[1]).equals("1")) {
                        maleHofMember = memberEntity;
                    } else if (memberKeyAndAnswerMap.get("36" + "." + splitKey[1]) != null && memberKeyAndAnswerMap.get("36" + "." + splitKey[1]).equals("2")) {
                        femaleHofMember = memberEntity;
                    }
                }
                if (memberKeyAndAnswerMap.get("311" + "." + splitKey[1]) != null &&
                        (memberKeyAndAnswerMap.get("311" + "." + splitKey[1]).equals("WIFE")
                                || memberKeyAndAnswerMap.get("311" + "." + splitKey[1]).equals("HUSBAND"))) {
                    hofHusbandOrWifeMember = memberEntity;
                }
            } else {
                String uniqueHealthId = memberKeyAndAnswerMap.get("23");
                memberEntity = mapOfMemberWithHealthIdAsKey.get(uniqueHealthId);

                if (uniqueHealthId != null && memberEntity == null) {
                    continue;
                }

                if (memberEntity == null) {
                    memberEntity = mapOfNewMemberWithLoopIdAsKey.get("0");
                    memberCFHCEntity = mapOfNewMemberCFHCWithLoopIdAsKey.get("0");
                    if (memberEntity == null) {
                        memberEntity = new MemberEntity();
                        memberCFHCEntity = new MemberCFHCEntity();
                        memberEntity.setFamilyId(familyEntity.getFamilyId());
                        memberEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                        mapOfNewMemberWithLoopIdAsKey.put("0", memberEntity);
                        memberCFHCEntity.setMemberId(memberEntity.getId());
                        mapOfNewMemberCFHCWithLoopIdAsKey.put("0", memberCFHCEntity);
                        memberEntity.setMemberCFHCEntity(memberCFHCEntity);
                    }
                } else {
                    memberCFHCEntity = mapOfMemberCFHCWithMemberIdAsKey.get(memberEntity.getId());
                    memberEntity.setMemberCFHCEntity(memberCFHCEntity);
                    if (memberCFHCEntity == null) {
                        memberCFHCEntity = new MemberCFHCEntity();
                        memberCFHCEntity.setMemberId(memberEntity.getId());
                        mapOfMemberCFHCWithMemberIdAsKey.put(memberEntity.getId(), memberCFHCEntity);
                        memberEntity.setMemberCFHCEntity(memberCFHCEntity);
                    }
                }

                mapOfMemberWithLoopIdAsKey.putIfAbsent("0", memberEntity);

                if ((memberKeyAndAnswerMap.get("25") != null && memberKeyAndAnswerMap.get("25").equals("2"))) {
                    membersToBeMarkedDead.add(memberEntity);
                    mapOfDeadMemberWithLoopIsAsKey.put("0", memberEntity);
                } else if (memberKeyAndAnswerMap.get("25") != null && memberKeyAndAnswerMap.get("25").equals("3")) {
                    membersToBeArchived.add(memberEntity);
                } else {
                    this.setAnswersToMemberEntity(key, answer, memberEntity, memberCFHCEntity, memberKeyAndAnswerMap, null);
                }

                if (memberKeyAndAnswerMap.get("31") != null && memberKeyAndAnswerMap.get("31").equals("1")) {
                    if (memberKeyAndAnswerMap.get("36") != null && memberKeyAndAnswerMap.get("36").equals("1")) {
                        maleHofMember = memberEntity;
                    } else if (memberKeyAndAnswerMap.get("36") != null && memberKeyAndAnswerMap.get("36").equals("2")) {
                        femaleHofMember = memberEntity;
                    }
                }
                if (memberKeyAndAnswerMap.get("311") != null &&
                        (memberKeyAndAnswerMap.get("311").equals("WIFE")
                                || memberKeyAndAnswerMap.get("311").equals("HUSBAND"))) {
                    hofHusbandOrWifeMember = memberEntity;
                }
            }
        }

        if (!deadMemberKeyAndAnswerMap.isEmpty()) {
            for (Map.Entry<String, String> entrySet : deadMemberKeyAndAnswerMap.entrySet()) {
                String key = entrySet.getKey();
                String answer = entrySet.getValue();
                MemberEntity memberEntity;

                if (key.contains(".")) {
                    String[] splitKey = key.split("\\.");
                    memberEntity = mapOfDeadLastYearWithLoopIsAsKey.get(splitKey[1]);

                    if (memberEntity == null) {
                        memberEntity = new MemberEntity();
                        memberEntity.setFamilyId(familyEntity.getFamilyId());
                        memberEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                        membersDeadLastYear.add(memberEntity);
                        mapOfDeadLastYearWithLoopIsAsKey.put(splitKey[1], memberEntity);
                    }
                    this.setAnswersToDeadMember(splitKey[0], answer, memberEntity, deadMemberKeyAndAnswerMap, splitKey[1]);
                } else {
                    memberEntity = mapOfDeadLastYearWithLoopIsAsKey.get("0");
                    if (memberEntity == null) {
                        memberEntity = new MemberEntity();
                        memberEntity.setFamilyId(familyEntity.getFamilyId());
                        memberEntity.setUniqueHealthId(familyHealthSurveyService.generateMemberUniqueHealthId());
                        membersDeadLastYear.add(memberEntity);
                        mapOfDeadLastYearWithLoopIsAsKey.put("0", memberEntity);
                    }
                    this.setAnswersToDeadMember(key, answer, memberEntity, deadMemberKeyAndAnswerMap, null);
                }
            }
        }

        Set<MemberEntity> membersToBeAdded = new HashSet<>(mapOfNewMemberWithLoopIdAsKey.values());
        Set<MemberEntity> membersToBeUpdated = new HashSet<>(mapOfMemberWithLoopIdAsKey.values());
        membersToBeUpdated.removeAll(membersToBeMarkedDead);
        membersToBeUpdated.removeAll(membersToBeAdded);
        membersToBeUpdated.removeAll(membersToBeArchived);

        if (familyEntity.getId() != null) {
            familyEntity.setModifiedBy(user.getId());
            familyEntity.setModifiedOn(new Date());
        }

        Map<Integer, String> failedOfflineAbhaMessageMap = new HashMap<>();

        familyHealthSurveyService.persistFamilyCFHC(parsedRecordBean.getChecksum(), familyEntity, membersToBeAdded, membersToBeArchived, membersToBeUpdated,
                String.valueOf(user.getId()), membersToBeMarkedDead, mapOfMemberWithLoopIdAsKey,
                motherChildRelationString, husbandWifeRelationString, failedOfflineAbhaMessageMap);

        returnMap.put("createdInstanceId", familyEntity.getId().toString());

        if (!mapOfDeadMemberWithLoopIsAsKey.isEmpty()) {
            for (Map.Entry<String, MemberEntity> entry : mapOfDeadMemberWithLoopIsAsKey.entrySet()) {
                String key = entry.getKey();
                MemberEntity memberEntity = entry.getValue();
                Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(memberEntity.getId());
                if (Boolean.TRUE.equals(abBoolean)) {
                    throw new ImtechoMobileException("Member with Health ID " + memberEntity.getUniqueHealthId() + " is already marked DEAD. "
                            + "You cannot mark a DEAD member DEAD again.", 1);
                } else {
                    memberDao.deleteDiseaseRelationsOfMember(memberEntity.getId());
                    memberEntity.setModifiedBy(user.getId());
                    memberEntity.setModifiedOn(new Date());
                    memberEntity.setFamilyHeadFlag(Boolean.FALSE);
                    familyHealthSurveyService.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_DEAD);
                    memberDao.flush();
                    markMemberAsDeath(memberEntity, familyEntity, memberKeyAndAnswerMap, key, user);
                }
            }
        }

        for (MemberEntity memberEntity : membersDeadLastYear) {
            memberEntity.setCreatedBy(user.getId());
            memberEntity.setCreatedOn(new Date());
            memberEntity.setState(FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_DEAD);
            memberDao.createMember(memberEntity);
        }
        if (!mapOfDeadLastYearWithLoopIsAsKey.isEmpty()) {
            for (Map.Entry<String, MemberEntity> entry : mapOfDeadLastYearWithLoopIsAsKey.entrySet()) {
                String key = entry.getKey();
                MemberEntity memberEntity = entry.getValue();
                Boolean abBoolean = memberDao.checkIfMemberAlreadyMarkedDead(memberEntity.getId());
                if (Boolean.TRUE.equals(abBoolean)) {
                    throw new ImtechoMobileException("Member with Health ID " + memberEntity.getUniqueHealthId() + " is already marked DEAD. "
                            + "You cannot mark a DEAD member DEAD again.", 1);
                } else {
                    markMemberAsDeath(memberEntity, familyEntity, deadMemberKeyAndAnswerMap, key, user);
                }
            }
        }

        if (femaleHofMember != null && hofHusbandOrWifeMember != null) {
            femaleHofMember.setHusbandId(hofHusbandOrWifeMember.getId());
            memberDao.update(femaleHofMember);
        }

        if (maleHofMember != null && hofHusbandOrWifeMember != null) {
            hofHusbandOrWifeMember.setHusbandId(maleHofMember.getId());
            memberDao.update(maleHofMember);
        }
        memberDao.flush();

        if (isNewFamily) {
            newFamilyMsg(familyEntity, mapOfMemberWithLoopIdAsKey, returnMap, failedOfflineAbhaMessageMap, parsedRecordBean);
        }
        returnMap.put("createdInstanceId", familyEntity.getId().toString());
        return returnMap;
    }

    /**
     * Executes query mark_member_as_death for given member
     *
     * @param memberEntity          Instance of MemberEntity
     * @param familyEntity          Instance of familyEntity
     * @param memberKeyAndAnswerMap Map of family question and answer
     * @param key                   String of family question
     * @param user                  Instance of UserMaster
     */
    private void markMemberAsDeath(MemberEntity memberEntity, FamilyEntity familyEntity, Map<String, String> memberKeyAndAnswerMap, String key, UserMaster user) {
        String dateOfDeath;
        String placeOfDeath;
        String deathReason;
        String otherDeathReason;
        String healthInfraId;

        if (key.equals("0") && memberKeyAndAnswerMap.containsKey("27")) {
            dateOfDeath = memberKeyAndAnswerMap.get("27");
            placeOfDeath = memberKeyAndAnswerMap.get("28");
            deathReason = memberKeyAndAnswerMap.get("3001");
            otherDeathReason = memberKeyAndAnswerMap.get("3003");
            healthInfraId = memberKeyAndAnswerMap.get("29");
        } else if (!key.equals("0") && memberKeyAndAnswerMap.containsKey("27." + key)) {
            dateOfDeath = memberKeyAndAnswerMap.get("27" + "." + key);
            placeOfDeath = memberKeyAndAnswerMap.get("28" + "." + key);
            deathReason = memberKeyAndAnswerMap.get("3001" + "." + key);
            otherDeathReason = memberKeyAndAnswerMap.get("3003" + "." + key);
            healthInfraId = memberKeyAndAnswerMap.get("29" + "." + key);
        } else if (key.equals("0") && memberKeyAndAnswerMap.containsKey("8053")) {
            dateOfDeath = memberKeyAndAnswerMap.containsKey("8058") ? memberKeyAndAnswerMap.get("8058") : null;
            placeOfDeath = null;
            deathReason = null;
            otherDeathReason = null;
            healthInfraId = null;
        } else if (!key.equals("0") && memberKeyAndAnswerMap.containsKey("8053." + key)) {
            dateOfDeath = memberKeyAndAnswerMap.containsKey("8058." + key) ? memberKeyAndAnswerMap.get("8058." + key) : null;
            placeOfDeath = null;
            deathReason = null;
            otherDeathReason = null;
            healthInfraId = null;
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
        } else {
            parameters.put("location_id", familyEntity.getLocationId());
        }
        parameters.put("action_by", user.getId());
        parameters.put("family_id", familyEntity.getId());
        if (dateOfDeath != null) {
            parameters.put("death_date", sdf.format(new Date(Long.parseLong(dateOfDeath))));
        } else {
            parameters.put("death_date", null);
        }
        parameters.put("place_of_death", placeOfDeath);
        parameters.put("death_reason", deathReason);
        parameters.put("other_death_reason", otherDeathReason);
        parameters.put("service_type", "CFHC");
        parameters.put("reference_id", memberEntity.getId());
        parameters.put("health_infra_id", healthInfraId);
        parameters.put("member_death_mark_state", FamilyHealthSurveyServiceConstants.CFHC_MEMBER_STATE_DEAD);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        queryMasterService.executeQuery(queryDtos, true);
    }

    /**
     * Generates string message for new family
     *
     * @param familyEntity               Instance of familyEntity
     * @param mapOfMemberWithLoopIdAsKey Map of member with loop as key
     * @param returnMap                  Map of string
     */
    private void newFamilyMsg(FamilyEntity familyEntity, Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey, Map<String, String> returnMap, Map<Integer, String> failedOfflineAbhaMessageMap, ParsedRecordBean parsedRecordBean) {
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
            if (!failedOfflineAbhaMessageMap.isEmpty()) {
                parsedRecordBean.setAbhaFailed(Boolean.TRUE);
                String message = failedOfflineAbhaMessageMap.get(member.getId());
                if (message != null && !message.isBlank()) {
                    sb.append("\n");
                    sb.append(message);
                    sb.append("\n");
                }
            }
            if (count < mapOfMemberWithLoopIdAsKey.size()) {
                sb.append("\n");
            }
            count++;
        }
        if (sb.length() > 0) {
            returnMap.put("message", sb.toString());
        }
    }

    /**
     * Set value in familyEntity
     *
     * @param key          Question string
     * @param answer       Answer string
     * @param familyEntity Instance of FamilyEntity
     */
    private void setAnswersToFamilyDto(String key, String answer, FamilyEntity familyEntity) {
        switch (key) {
            case "7":
                if (answer.trim().length() > 0) {
                    familyEntity.setHouseNumber(answer.trim());
                }
                break;
            case "8":
                if (answer.trim().length() > 0) {
                    familyEntity.setAddress1(answer.trim());
                }
                break;
            case "9":
                if (answer.trim().length() > 0) {
                    familyEntity.setAddress2(answer.trim());
                }
                break;
            case "11":
                if (answer.trim().length() > 0) {
                    familyEntity.setAreaId(Integer.valueOf(answer.trim()));
                }
                break;
            case "12":
                if (answer.trim().length() > 0) {
                    familyEntity.setReligion(answer.trim());
                }
                break;
            case "13":
                if (answer.trim().length() > 0) {
                    familyEntity.setCaste(answer.trim());
                }
                break;
            case "14":
                familyEntity.setMigratoryFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "15":
                familyEntity.setVulnerableFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "16":
                familyEntity.setBplFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "17":
                familyEntity.setToiletAvailableFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "18":
                if (answer.trim().length() > 0) {
                    familyEntity.setMaaVatsalyaNumber(answer.trim());
                }
                break;
            case "19":
                if (answer.trim().length() > 0) {
                    familyEntity.setPmjayCardNumber(answer.trim());
                }
                break;
            case "20":
                if (answer.trim().length() > 0) {
                    familyEntity.setRationCardNumber(answer.trim());
                }
                break;
            case "22":
                if (answer.equals("0")) {
                    familyEntity.setAnganwadiId(null);
                } else {
                    familyEntity.setAnganwadiId(Integer.valueOf(answer));
                }
                break;
            case "-3":
                familyEntity.setAreaId(Integer.valueOf(answer));
                break;
            case "1005":
                familyEntity.setBplCardNumber(answer);
                break;
            case "405":
                familyEntity.setProvidingConsent(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "406":
                familyEntity.setVulnerabilityCriteria(answer);
                break;
            case "411":
                familyEntity.setEligibleForPmjay(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "6022":
                String[] vehiclesArray = answer.split(",");
                Set<String> vehiclesSet = new HashSet<>(Arrays.asList(vehiclesArray));
                familyEntity.setVehicleDetails(vehiclesSet);
                break;
            case "6007":
                familyEntity.setTypeOfToilet(answer);
                break;
            case "6001":
                familyEntity.setDrinkingWaterSource(answer);
                break;
            case "6002":
                familyEntity.setFuelForCooking(answer);
                break;
            case "6003":
                familyEntity.setElectricityAvailability(answer);
                break;
            case "6004":
                familyEntity.setTypeOfHouse(answer);
                break;
            case "6025":
                familyEntity.setOtherTypeOfHouse(answer);
                break;
            default:
        }
    }

    /**
     * Set value to memberEntity
     *
     * @param key                   Question string
     * @param answer                Answer string
     * @param memberEntity          Instance of MemberEntity
     * @param memberCFHCEntity      Instance of MemberCFHCEntity
     * @param memberKeyAndAnswerMap Map of question and answer
     * @param memberCount           Counts of member
     */
    private void setAnswersToMemberEntity(String key, String answer, MemberEntity memberEntity, MemberCFHCEntity memberCFHCEntity, Map<String, String> memberKeyAndAnswerMap, String memberCount) {
        switch (key) {
            case "31":
                memberEntity.setFamilyHeadFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "32":
                if (answer.trim().length() > 0) {
                    memberEntity.setFirstName(answer.trim());
                }
                break;
            case "33":
                if (answer.trim().length() > 0 && !answer.equals("1")) {
                    memberEntity.setMiddleName(answer.trim());
                }
                break;
            case "35":
                if (answer.trim().length() > 0) {
                    memberEntity.setGrandfatherName(answer.trim());
                }
                break;
            case "34":
                if (answer.trim().length() > 0) {
                    memberEntity.setLastName(answer.trim());
                }
                break;
            case "36":
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
                        memberEntity.setGender(answer);
                        break;
                }
                break;
            case "37":
                if (answer != null) {
                    memberEntity.setMaritalStatus(Integer.parseInt(answer));
                }
                break;
            case "310":
                memberEntity.setEmamtaHealthId(answer);
                break;
            case "40":
                if (!answer.equals("T")) {
                    memberEntity.setMobileNumber(answer.replace("F/", ""));
                }
                break;
            case "38":
            case "3092":
                memberEntity.setDob(new Date(Long.parseLong(answer)));
                break;
            case "41":
                if (answer.trim().length() > 0) {
                    memberEntity.setIfsc(answer.trim());
                }
                break;
            case "42":
                if (answer.trim().length() > 0) {
                    memberEntity.setAccountNumber(answer.trim());
                }
                break;
            case "43":
                memberEntity.setEducationStatus(answer != null ? Integer.parseInt(answer) : null);
                break;
            case "45":
                memberCFHCEntity.setReadyForMoreChild(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "4804":
                memberEntity.setMenopauseArrived(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "4601":
                memberCFHCEntity.setCurrentlyUsingFpMethod(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "4800":
                memberEntity.setLastMethodOfContraception(answer);
                break;
            case "4801":
                memberEntity.setFpInsertOperateDate(new Date(Long.parseLong(answer)));
                break;
            case "47":
                memberCFHCEntity.setChangeFpMethod(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "4701":
                memberCFHCEntity.setFpMethod(answer);
                break;
            case "50":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("49" + "." + memberCount).equals("1")) {
                        Set<Integer> anomalyRelEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            anomalyRelEntitys.add(Integer.parseInt(id));
                        }
                        memberEntity.setCongenitalAnomalyDetails(anomalyRelEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("49").equals("1")) {
                    Set<Integer> anomalyRelEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        anomalyRelEntitys.add(Integer.parseInt(id));
                    }
                    memberEntity.setCongenitalAnomalyDetails(anomalyRelEntitys);
                }
                break;
            case "52":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("51" + "." + memberCount).equals("1")) {
                        Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                        }
                        memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("51").equals("1")) {
                    Set<Integer> chronicDiseaseRelEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        chronicDiseaseRelEntitys.add(Integer.parseInt(id));
                    }
                    memberEntity.setChronicDiseaseDetails(chronicDiseaseRelEntitys);
                }
                break;
            case "54":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("53" + "." + memberCount).equals("1")) {
                        Set<Integer> currentDiseaseEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            currentDiseaseEntitys.add(Integer.parseInt(id));
                        }
                        memberEntity.setCurrentDiseaseDetails(currentDiseaseEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("53").equals("1")) {
                    Set<Integer> currentDiseaseEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        currentDiseaseEntitys.add(Integer.parseInt(id));
                    }
                    memberEntity.setCurrentDiseaseDetails(currentDiseaseEntitys);
                }
                break;
            case "56":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("55" + "." + memberCount).equals("1")) {
                        Set<Integer> eyeRelEntities = new HashSet<>();
                        for (String id : answer.split(",")) {
                            eyeRelEntities.add(Integer.parseInt(id));
                        }
                        memberEntity.setEyeIssueDetails(eyeRelEntities);
                    }
                } else if (memberKeyAndAnswerMap.get("55").equals("1")) {
                    Set<Integer> eyeRelEntities = new HashSet<>();
                    for (String id : answer.split(",")) {
                        eyeRelEntities.add(Integer.parseInt(id));
                    }
                    memberEntity.setEyeIssueDetails(eyeRelEntities);
                }
                break;
            case "61":
                memberCFHCEntity.setIsFeverWithCSForDADays(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "62":
                memberCFHCEntity.setIsFeverWithHEPMPSR(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "63":
                memberCFHCEntity.setIsFeverWithHJP(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "65":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("64" + "." + memberCount).equals("1")) {
                        Set<Integer> currentDiseaseEntitys = new HashSet<>();
                        for (String id : answer.split(",")) {
                            currentDiseaseEntitys.add(Integer.parseInt(id));
                        }
                        memberCFHCEntity.setSickleCellAnemiaDetails(currentDiseaseEntitys);
                    }
                } else if (memberKeyAndAnswerMap.get("64").equals("1")) {
                    Set<Integer> currentDiseaseEntitys = new HashSet<>();
                    for (String id : answer.split(",")) {
                        currentDiseaseEntitys.add(Integer.parseInt(id));
                    }
                    memberCFHCEntity.setSickleCellAnemiaDetails(currentDiseaseEntitys);
                }
                break;
            case "66":
                // Hypo pigmented or reddish colors skin patches with definite loss of sensation
                memberCFHCEntity.setIsSkinPatches(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "67":
                memberCFHCEntity.setBloodPressure(answer);
                break;
            case "68":
                memberCFHCEntity.setIsCoughForMTOneWeek(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "69":
                memberCFHCEntity.setIsFeverAtEveningTime(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "70":
                memberCFHCEntity.setIsFeelingAnyWeakness(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "311":
                memberEntity.setRelationWithHof(answer);
                break;
            case "71":
                memberCFHCEntity.setIsChildGoingSchool(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "72":
                memberCFHCEntity.setCurrentStudyingStandard(answer);
                break;
            case "44":
                memberCFHCEntity.setCurrentSchool(Integer.parseInt(answer));
                break;
            case "4100":
                if (answer.startsWith("T")) {
                    memberEntity.setIsMobileNumberVerified(Boolean.TRUE);
                    if (answer.endsWith("T")) {
                        MemberAdditionalInfo memberAdditionalInfo;
                        Gson gson = new Gson();
                        if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
                            memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
                        } else {
                            memberAdditionalInfo = new MemberAdditionalInfo();
                        }
                        memberAdditionalInfo.setWhatsappConsentOn(new Date().getTime());
                        memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
                    }
                } else {
                    memberEntity.setIsMobileNumberVerified(Boolean.FALSE);
                }
                break;
            case "204":
                memberCFHCEntity.setPmjayNumber(answer);
                break;
            case "5000":
                memberEntity.setNdhmHealthId(answer);
                break;
            case "3901":
            case "3902":
            case "3904":
            case "3905":
                MemberAdditionalInfo memberAdditionalInfo;
                Gson gson = new Gson();
                if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
                    memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
                } else {
                    memberAdditionalInfo = new MemberAdditionalInfo();
                }
                if (key.equals("3901")) {
                    String[] ansSplit = answer.split("/");
                    if (ansSplit.length == 3) {
                        try {
                            memberAdditionalInfo.setHeight(Integer.valueOf(ansSplit[0]));
                            memberAdditionalInfo.setWeight(Float.valueOf(ansSplit[1]));
                            memberAdditionalInfo.setBmi(Float.valueOf(ansSplit[2]));
                        } catch (NumberFormatException e) {
                            memberEntity.setWeight(Float.valueOf(ansSplit[0]));
                            memberAdditionalInfo.setHeight(Integer.valueOf(ansSplit[1].split("\\.")[0]));
                            memberAdditionalInfo.setBmi(Float.valueOf(ansSplit[2]));
                        }
                    }
                } else if (key.equals("3902")) {
                    memberAdditionalInfo.setDiseaseHistory(answer);
                } else if (key.equals("3904")) {
                    memberAdditionalInfo.setOtherDiseaseHistory(answer);
                } else {
                    memberAdditionalInfo.setRiskFactor(answer);
                }
                memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
                break;
            case "452":
                memberEntity.setVulnerableFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "96":
                if (memberCount != null) {
                    if (memberKeyAndAnswerMap.get("95" + "." + memberCount).equals("1")) {
                        Map<String, String> aadharValuesMap = new HashMap<>();
                        String keyValueString = org.apache.commons.lang3.StringUtils.substringBetween(answer, "{", "}");
                        String[] keyValueSplit = keyValueString.split(",");
                        for (String keyValuePair : keyValueSplit) {
                            String[] split = keyValuePair.split(":");
                            if (split.length == 2) {
                                aadharValuesMap.put(split[0].trim().replace("\"", ""), split[1].trim().replace("\"", ""));
                            }
                        }
                        memberEntity.setNameAsPerAadhar(aadharValuesMap.get("name"));
                        if (aadharValuesMap.get("uid").length() > 4) {
                            memberEntity.setAadharNumber(aadharValuesMap.get("uid"));
                        }
                    }
                } else if (memberKeyAndAnswerMap.get("95").equals("1")) {
                    Map<String, String> aadharValuesMap = new HashMap<>();
                    String keyValueString = org.apache.commons.lang3.StringUtils.substringBetween(answer, "{", "}");
                    String[] keyValueSplit = keyValueString.split(",");
                    for (String keyValuePair : keyValueSplit) {
                        String[] split = keyValuePair.split(":");
                        if (split.length == 2) {
                            aadharValuesMap.put(split[0].trim().replace("\"", ""), split[1].trim().replace("\"", ""));
                        }
                    }
                    memberEntity.setNameAsPerAadhar(aadharValuesMap.get("name"));
                    if (aadharValuesMap.get("uid").length() > 4) {
                        memberEntity.setAadharNumber(aadharValuesMap.get("uid"));
                    }
                }
                break;
            case "97":
                String aadhaarNumber = answer.replace("F/", "");
                if (memberKeyAndAnswerMap.get("94").equals("1")) {
                    memberEntity.setAadharNumber(aadhaarNumber);
                }
                break;
            case "94":
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
            case "6043":
                memberEntity.setHealthInsurance(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "6044":
                memberEntity.setSchemeDetail(answer);
                break;
            case "7052":
                memberEntity.setDateOfWedding(new Date(Long.parseLong(answer)));
                break;
            case "453":
                memberEntity.setPmjayAvailability(answer);
                break;
            case "5013":
                memberEntity.setAlcoholAddiction(answer);
                break;
            case "5014":
                memberEntity.setSmokingAddiction(answer);
                break;
            case "5015":
                memberEntity.setTobaccoAddiction(answer);
                break;
            case "4501":
                memberEntity.setIsPregnantFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "4502":
                memberEntity.setLmpDate(new Date(Long.parseLong(answer)));
                break;
            case "4504":
                memberEntity.setRchId(answer);
                break;
            case "455":
                memberEntity.setHospitalizedEarlier(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }

    private void setAnswersToDeadMember(String key, String answer, MemberEntity memberEntity, Map<String, String> keyAnswerMap, String count) {
        switch (key) {
            case "8053":
                if (answer.trim().length() > 0) {
                    memberEntity.setFirstName(answer.trim());
                }
                break;
            case "8054":
                if (answer.trim().length() > 0 && !answer.equals("1")) {
                    memberEntity.setMiddleName(answer.trim());
                }
                break;
            case "8055":
                if (answer.trim().length() > 0) {
                    memberEntity.setLastName(answer.trim());
                }
                break;
            case "8056":
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
                        memberEntity.setGender(answer);
                        break;
                }
                break;
            case "8057":
                if (answer.trim().length() > 0) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, -(Integer.parseInt(answer)));
                    if (count != null && keyAnswerMap.containsKey("8060" + "." + count)) {
                        cal.add(Calendar.MONTH, -(Integer.parseInt(keyAnswerMap.get("8060" + "." + count))));
                    } else if (keyAnswerMap.containsKey("8060")) {
                        cal.add(Calendar.MONTH, -(Integer.parseInt(keyAnswerMap.get("8060"))));
                    }
                    Date dobDate = cal.getTime();
                    memberEntity.setDob(dobDate);
                }
                break;
            default:
        }
    }
}
