package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by prateek on 16/1/19.
 */

public interface NcdService {

    List<FamilyDataBean> retrieveFamiliesForCbacEntry(String searchString, Integer areaId, long limit, long offset,LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveMembersListForCbacEntry(String familyId);
    List<MemberDataBean> retrieveMembersListForDnhddCbacAndNutritionEntry(String familyId);

    List<FamilyDataBean> retrieveFamiliesForDnhddNcdScreening(String searchString, List<Integer> areaId, long limit, long offset,LinkedHashMap<String, String> qrData);
    List<FamilyDataBean> retrieveFamiliesForDnhddCbacAndNutritionEntry(String searchString, Integer areaId, long limit, long offset,LinkedHashMap<String, String> qrData);
    List<FamilyDataBean> retrieveFamiliesForNcdScreening(String searchString, List<Integer> areaId, long limit, long offset,LinkedHashMap<String, String> qrData);

    List<MemberDataBean> retrieveMembersListForNcdScreening(String familyId);

    public List<MemberDataBean> retrieveMembersListForNcdConfirmation(String searchString, List<Integer> areaIds, long limit, long offset);

    List<MemberBean> retrieveMembersForNcdRegisterFhw(Integer selectedVillage, List<Integer> selectedAshaAreas, String selectedScreening);

    List<MemberDataBean> retrieveMembersForNcdRegisterAsha(List<Integer> areaIds, String search, long limit, long offset,
                                                           LinkedHashMap<String, String> qrData);

    MemberCbacDetailBean retrieveMemberCbacDetailBean(Long memberId);

    void markMemberAsCbacDone(Long actualId, Integer score);

    void markMemberAsPersonalHistoryDone(Long actualId);

    void markMemberAsDiabetesConfirmed(Long actualId);

    void markFamilyAsCbacDoneForAnyMember(Long actualId);

    void markMemberAsNCDScreeningDone(Long memberActualId, String formType, Date screeningDate);

    List<MemberDataBean> retrieveMembersListForWeeklyClinic(String searchString, boolean referenceDue, List<Integer> areaIds, long limit, long offset);

    MemberMoConfirmedDataBean retrieveMoConfirmedBeanByMemberId(Long memberId);

    DrugInventoryBean retrieveDrugByMedicineId(Integer medicineId);

    void markMemberEveningAvailability(Long memberId, String familyId, boolean evening);
}
