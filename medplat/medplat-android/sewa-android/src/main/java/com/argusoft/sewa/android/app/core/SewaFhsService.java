/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MobileNumberUpdationBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.databean.RchVillageProfileDataBean;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberPregnancyStatusBean;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kunjan
 */
public interface SewaFhsService {

    List<FamilyDataBean> retrieveFamilyDataBeansForCFHCByVillage(String locationId, boolean isReverification, long limit, long offset);

    List<MemberDataBean> retrieveMemberDataBeansForDnhddNcdByFamily(String familyId);

    List<FamilyDataBean> searchFamilyDataBeansForCFHCByVillage(String search, String locationId, boolean isReverification, LinkedHashMap<String, String> qrData);

    List<FamilyDataBean> retrieveFamilyDataBeansForMergeFamily(String locationId, String search, long limit, long offset);

    List<FamilyDataBean> retrieveFamilyDataBeansForFHSByArea(Long areaId, long limit, long offset);

    List<FamilyDataBean> searchFamilyDataBeansForFHSByArea(Long areaId, String search);

    List<MemberDataBean> retrieveMemberDataBeansByFamily(String familyId);

    Map<String, MemberDataBean> retrieveHeadMemberDataBeansByFamilyId(List<String> familyIds);

    FamilyDataBean retrieveFamilyDataBeanByFamilyId(String familyId);

    void markFamilyAsVerified(String familyId);

    void markFamilyAsCFHCVerified(String familyId);

    void markFamilyAsMigrated(String familyId);

    void markFamilyAsArchived(String familyId);

    List<LocationBean> getDistinctLocationsAssignedToUser();

    List<FhwServiceDetailBean> retrieveFhwServiceDetailBeansByVillageId(Integer locationId);

    List<MemberDataBean> retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(String familyId);

    void mergeFamilies(FamilyDataBean familyToBeExpanded, FamilyDataBean familyToBeMerged);

    Map<Long, MemberBean> retrieveMemberBeansMapByActualIds(List<Long> actualIds);

    String assignFamilyToUser(String locationId, FamilyDataBean familyDataBean);

    List<FieldValueMobDataBean> retrieveAnganwadiListFromSubcentreId(Integer subcentreId);

    Integer retrieveSubcenterIdFromAnganwadiId(Integer anganwadiId);

    RchVillageProfileDataBean getRchVillageProfileDataBean(String locationId);

    List<LocationBean> retrieveAshaAreaAssignedToUser(Integer villageId);

    List<MemberDataBean> retrieveEligibleCouplesByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrievePregnantWomenByAshaArea(List<Integer> locationIds, boolean isHighRisk, List<Integer> villageIds, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveWPDWomenByAshaArea(List<Integer> locationIds, List<Integer> villageIds, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrievePncMothersByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveChildsBelow5YearsByAshaArea(List<Integer> locationIds, Boolean isHighRisk, List<Integer> villageIds, CharSequence s, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveChildsBelow6YearsByAshaArea(Integer locationId, CharSequence s, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveChildsBetween6MonthsTo3YearsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveMothersWithLastDeliveryLessThan6MonthsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveAdolescentGirlsByAshaArea(Integer locationId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveMembersByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<FamilyDataBean> retrieveFamilyDataBeansByAshaArea(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset);

    MemberBean retrieveMemberBeanByActualId(Long memberId);

    MemberBean retrieveMemberBeanByHealthId(String healthId);

    FamilyBean retrieveFamilyBeanByActualId(Long id);

    void markMemberAsMigrated(Long memberActualId);

    void markMemberAsDead(Long memberActualId);

    void updateMemberPregnantFlag(Long memberActualId, boolean pregnantFlag);

    void updateMemberLmpDate(Long memberActualId, Date lmpDate);

    void updateFamily(FamilyBean familyBean);

    void deleteNotificationByMemberIdAndNotificationType(Long memberActualId, List<String> notificationTypes);

    void updateVaccinationGivenForChild(Long memberActualId, String vaccinationGiven);

    void updateVaccinationGivenForChild(String uniqueHealthId, String vaccinationGiven);

    void markNotificationAsRead(NotificationMobDataBean notificationMobDataBean);

    String getChildrenCount(Long motherId, boolean total, boolean male, boolean female);

    MemberDataBean getLatestChildByMotherId(Long motherId);

    String getValueOfListValuesById(String id);

    Map<String, String> retrieveAshaInfoByAreaId(String areaId);

    List<MemberDataBean> retrieveMemberListForRchRegister(List<Integer> locationIds, Integer villageId, CharSequence searchString, long limit, long offset, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveMemberListForNPCB(List<Integer> areaIds, CharSequence searchString, LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveMembersForPhoneVerificationByFhsr(Long locationId, String searchString, long limit, long offset);

    void storeFhsrPhoneVerificationForm(Long memberId, String phoneNumber, String answerString);

    long getMobileNumberCount(String mobileNumber);

    MemberPregnancyStatusBean retrievePregnancyStatusBeanByMemberId(Long memberId);

    List<FamilyDataBean> retrieveAssignedFamilyDataBeansToUserForMobileNumberUpdationAndVerification(CharSequence searchString, String locationId, long limit, long offset);

    List<MemberDataBean> retrieveMemberDataBeanForMobileNumberUpdation(String familyId);

    void mobileNumberVerificationAndUpdationOfFamilyMember(MobileNumberUpdationBean mobileNumberUpdationBean, Long memberId);

    List<CovidTravellersInfoBean> retrieveTravellersList(List<Integer> areaIds, String search, long limit, long offset);

    List<String[]> retrieveGeriatricMembers(List<String> areaId, CharSequence s, LinkedHashMap<String, String> qrData);

    List<MemberBean> retrieveFamilyMembersContactListByMember(String familyId, String memberId);

    CovidTravellersInfoBean retrieveTravellersInfoBeanById(Integer actualId);

    List<FamilyDataBean> retrieveFamilyDataBeansForIDSPByVillage(String locationId, List<Integer> ashaAreas, long limit, long offset);

    List<FamilyDataBean> searchFamilyDataBeansForIDSPByVillage(String search, String locationId, List<Integer> ashaAreas);

    List<MemberDataBean> retrieveActiveMemberByFamilyIdForIdsp(String familyId);

    List<FamilyDataBean> retrieveFamilyDataBeansForAbhaNumberByVillage(String locationId, long limit, long offset);

    List<FamilyDataBean> searchFamilyDataBeansForAbhaNumberByVillage(String search, String locationId);
}
