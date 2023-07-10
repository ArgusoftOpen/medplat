/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.service;

import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.dto.MemberInformationDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.dto.FamilyDataBean;
import com.argusoft.medplat.mobile.dto.MemberDataBean;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Define all services for family health survey.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 11:00 AM
 */
public interface FamilyHealthSurveyService {

    /**
     * Update family details.
     *
     * @param family    Family details.
     * @param fromState Old state.
     * @param toState   New State.
     */
//    void updateFamily(FamilyDto family,
//                      String fromState,
//                      String toState);

    /**
     * Update family entity.
     *
     * @param family    Family entity.
     * @param fromState Old state.
     * @param toState   New State.
     */
    void updateFamily(FamilyEntity family,
                      String fromState,
                      String toState);


    /**
     * Create new family.
     *
     * @param family                        Family details.
     * @param membersToAdd                  Member to be added.
     * @param membersToArchive              Member to be archived.
     * @param membersToUpdate               Member to be updated.
     * @param principleId                   Principle id.
     * @param membersToBeMarkedDead         Member to be marked as dead.
     * @param mapOfMemberWithLoopIdAsKey    Mapper of member with loop id as key.
     * @param motherChildRelationString     Relation of mother and child.
     * @param husbandWifeRelationshipString Relation of husband and wife.
     */
    void persistFamily(FamilyEntity family,
                       Set<MemberEntity> membersToAdd,
                       Set<MemberEntity> membersToArchive,
                       Set<MemberEntity> membersToUpdate,
                       String principleId,
                       Set<MemberEntity> membersToBeMarkedDead,
                       Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey,
                       String motherChildRelationString,
                       String husbandWifeRelationshipString);

    /**
     * Create new family.
     *
     * @param syncStatusId                  Sync status id.
     * @param family                        Family details.
     * @param membersToAdd                  Member to be added.
     * @param membersToArchive              Member to be archived.
     * @param membersToUpdate               Member to be updated.
     * @param principleId                   Principle id.
     * @param membersToBeMarkedDead         Member to be marked as dead.
     * @param mapOfMemberWithLoopIdAsKey    Mapper of member with loop id as key.
     * @param motherChildRelationString     Relation of mother and child.
     * @param husbandWifeRelationshipString Relation of husband and wife.
     */
    void persistFamilyCFHC(String syncStatusId, FamilyEntity family,
                           Set<MemberEntity> membersToAdd,
                           Set<MemberEntity> membersToArchive,
                           Set<MemberEntity> membersToUpdate,
                           String principleId,
                           Set<MemberEntity> membersToBeMarkedDead,
                           Map<String, MemberEntity> mapOfMemberWithLoopIdAsKey,
                           String motherChildRelationString,
                           String husbandWifeRelationshipString,
                           Map<Integer, String> failedAbhaCreationMessageMap);

    /**
     * Update member.
     *
     * @param member    Member details.
     * @param fromState Old state.
     * @param toState   New state.
     */
//    void updateMember(MemberDto member, String fromState, String toState);

    /**
     * Update member entity.
     *
     * @param member    Entity of member.
     * @param fromState Old state.
     * @param toState   New State.
     */
    void updateMember(MemberEntity member, String fromState, String toState);

    /**
     * Retrieves family details for re verification for particular location.
     *
     * @param locationIds List of location.
     * @return Returns list of families.
     */
//    List<FamilyEntity> getOrphanedOrReverificationFamiliesAssignedToUser(List<Integer> locationIds);

    /**
     * Retrieves members details by states, list of family ids and list of location ids.
     *
     * @param states      List of states like archived, verified, unverified etc.
     * @param familyIds   List of family ids.
     * @param locationIds List of location ids.
     * @param basicStates Define basic states.
     * @return Returns list of members details.
     */
    List<MemberEntity> getMembers(List<String> states, List<String> familyIds, List<Integer> locationIds, List<String> basicStates);

    /**
     * Retrieves member details for ASHA.
     *
     * @param states      List of states.
     * @param familyIds   List of family ids.
     * @param locationIds List of location ids.
     * @return Returns list of member details based on defined criteria.
     */
    List<MemberEntity> getMembersForAsha(List<String> states, List<String> familyIds, List<Integer> locationIds);

    /**
     * Store family health survey form details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param user             User details.
     * @return Returns result details.
     */
    Map<String, String> storeFamilyHealthSurveyForm(ParsedRecordBean parsedRecordBean, UserMaster user);

    /**
     * Generate family id.
     *
     * @return Returns id of family.
     */
    String generateFamilyId();

    /**
     * Generate member's unique health id.
     *
     * @return Returns unique health id of member.
     */
    String generateMemberUniqueHealthId();

    /**
     * Retrieves assigned families by search text.
     *
     * @param userId             Id of user.
     * @param searchString       Search text.
     * @param searchByFamilyId   Search by family id.
     * @param searchByLocationId Search by location.
     * @param isArchivedFamily   Is family archived or not.
     * @param isVerifiedFamily   Is family verified or not.
     * @return Returns list of families.
     */
    List<Map<String, List<FamilyDataBean>>> getFamiliesToBeAssignedBySearchString(Integer userId, List<String> searchString, Boolean searchByFamilyId, Boolean searchByLocationId, Boolean isArchivedFamily, Boolean isVerifiedFamily);

    /**
     * Create temporary family.
     *
     * @param locationId Id of location.
     * @param ashaAreaId Id of asha area.
     * @return Return newly created temporary family.
     */
//    FamilyEntity createTemporaryFamily(Integer locationId, Integer ashaAreaId);

    /**
     * Retrieve Member Details by memberId
     *
     * @param memberId ID of imt_member
     * @return Returns Member Details
     */
    MemberDto retrieveDetailsByMemberId(Integer memberId);

    MemberDto  getMemberDetailsByUniqueHealthId(String  byUniqueHealthId);

    /**
     * Store member update form details.
     *
     * @param parsedRecordBean Contains details like form fill up time, relative id, village id etc.
     * @param keyAndAnswersMap Contains key and answers.
     * @param user             User details.
     * @return Returns result.
     */
    Map<String, String> storeMemberUpdateForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswersMap, UserMaster user);

    /**
     * Search member by unique health id.
     *
     * @param byUniqueHealthId Unique health id of member.
     * @return Returns member details.
     */
    MemberInformationDto searchMembersByUniqueHealthId(String byUniqueHealthId);

    /**
     * Update verified family location details.
     *
     * @param familyList                  List of families.
     * @param selectedMoveAnganwadiAreaId New anganwadi area id.
     * @param selectedMoveAshaAreaId      New ASHA area id.
     */
    void updateVerifiedFamilyLocation(List<String> familyList, Integer selectedMoveAnganwadiAreaId, Integer selectedMoveAshaAreaId);

    /**
     * Retrieves member entity by id.
     *
     * @param id Id of member.
     * @return Returns member entity.
     */
    MemberEntity retrieveMemberEntityByID(Integer id);

    /**
     * Retrieves member by search text, search by like member id, family id, name etc.
     *
     * @param searchString Search text.
     * @param searchBy     Define search by like member id, family id, mobile number, name etc.
     * @param limit        The number of data need to fetch.
     * @param offset       The number of data to skip before starting to fetch details.
     * @return Returns list of members based on define params.
     */
//    List<MemberDto> searchMembers(String searchString, String searchBy, Integer limit, Integer offset);

    /**
     * Retrieves member by phone number.
     *
     * @param phoneNumber Phone number of member.
     * @return Returns member details.
     */
//    Map<String, Object> retrieveMemberByPhoneNumber(String phoneNumber);

    /**
     * Retrieves member details by family id.
     *
     * @param familyId Family id.
     * @return Returns list of member details.
     */
    List<MemberDataBean> retrieveMemberDataBeansByFamilyId(String familyId);

    /**
     * Verify member details by family id.
     *
     * @param familyId     Family id.
     * @param dob          Date of birth.
     * @param aadharNumber Aadhar number.
     * @return Returns member details.
     */
//    MemberDataBean verifyMemberDetailByFamilyId(String familyId, Long dob, String aadharNumber);

    /**
     * Verify family details by family id.
     *
     * @param familyId     Family id.
     * @param mobileNumber Phone number.
     * @return Returns family details.
     */
//    Map<String, Object> verifyFamilyById(String familyId, String mobileNumber);

    /**
     * Delete mobile number for all family except verified family.
     *
     * @param familyId     Verified family id.
     * @param mobileNumber Mobile number.
     */
//    void deleteMobileNumberInFamilyExceptVerifiedFamily(String familyId, String mobileNumber);

    /**
     * Retrieves MyTecho family details by member id.
     *
     * @param memberId Member id.
     * @return Returns MyTecho family details.
     */
//    MyTechoFamilyDataBean retrieveImTechoFamilyDataByMemberId(Integer memberId);

    /**
     * Retrieves family id.
     *
     * @param familyId Family id.
     * @return Returns family id.
     */
//    Integer retrieveFamilyIdByFamilyId(String familyId);

    /**
     * Check user is allow to register in MyTecho or not.
     *
     * @param mobilenNumber       Mobile number.
     * @param memberId            Member id.
     * @param familyId            Family id.
     * @param isUserNotIdentified Is user identified or not.
     * @return Returns user allow to register in MyTecho or not.
     */
//    Boolean checkUserIsAllowToRegisterInMyTecho(String mobilenNumber, Integer memberId, String familyId, Boolean isUserNotIdentified);

    /**
     * Get members whom birth date is equal.
     *
     * @param familyId Family id.
     * @param dob      Date of birth.
     * @return Returns list of members.
     */
//    Map<String, Object> getEqualDobMembers(String familyId, String dob);
}
