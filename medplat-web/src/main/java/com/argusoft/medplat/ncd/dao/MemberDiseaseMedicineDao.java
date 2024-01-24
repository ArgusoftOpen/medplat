/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MemberTreatmentHistoryDto;
import com.argusoft.medplat.ncd.model.MemberDiseaseMedicine;

import java.util.List;

/**
 *
 * <p>
 * Define methods for member disease medicine.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface MemberDiseaseMedicineDao extends GenericDao<MemberDiseaseMedicine, Integer> {
    MemberDiseaseMedicine retrieveLastActiveRecordByMemberId(Integer memberId, Integer medicineId);

    /**
     * Retrieves treatment history by member id.
     * @param memberId Member id.
     * @param diseaseCode Disease code like HT(hypertension),D(diabetes) etc.
     * @return Returns list of member treatment history.
     */
    List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(Integer memberId, String diseaseCode);

    /**
     * Retrieves medicines by reference id.
     * @param referenceId Reference id.
     * @return Returns list of medicines.
     */
    List<MemberDiseaseMedicine> retrieveMedicinesByReferenceId(Integer referenceId);
    List<GeneralDetailMedicineDto> retrievePrescribedMedicineForUser(Integer memberId);

    MemberDiseaseMedicine retrieveDetailByMemberAndMedicine(Integer memberId, Integer medicineId);

    List<GeneralDetailMedicineDto> retrievePrescribedMedicineHistoryForUser(Integer memberId);

    List<GeneralDetailMedicineDto> retrievePrescribedMedicineForMobile(Integer memberId);

    MemberDiseaseMedicine retrieveByIdAndMemberId(Integer id, Integer memberId);

    /**
     * Retrieves treatment history by member id.
     * @param memberId Member id.
     * @param diseaseCode Disease code like HT(hypertension),D(diabetes) etc.
     * @return Returns list of member treatment history.
     */
    List<MemberTreatmentHistoryDto> retrieveTreatmentHistoryDnhdd(Integer memberId, String diseaseCode);
}
