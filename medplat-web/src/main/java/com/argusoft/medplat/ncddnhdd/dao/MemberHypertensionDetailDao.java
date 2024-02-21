/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberHypertensionDetail;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for member hypertension details.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface MemberHypertensionDetailDao extends GenericDao<MemberHypertensionDetail, Integer> {

    /**
     * Retrieves hypertension details by referral id and screening date.
     * @param referralId Referral id
     * @param screeningDate Screening date.
     * @return Returns list of hypertension details by defined params.
     */
    MemberHypertensionDetail retrieveForReferralId(Integer referralId, Date screeningDate);

    List<MemberHypertensionDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId);

    /**
     * Retrieves hypertension details by member id and screening date.
     * @param memberId Member id.
     * @param screeningDate Screening date.
     * @return Returns list of hypertension details by defined params.
     */
    MemberHypertensionDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    /**
     * Retrieves last record of hypertension for particular member id.
     * @param memberId Member id.
     * @return Returns last record of hypertension.
     */
    List<MemberHypertensionDetail> retrieveLastRecordByMemberId(Integer memberId);

    MemberHypertensionDetail retrieveLastSingleRecordByMemberId(Integer memberId);

    List<MemberHypertensionDetail> retrieveLastNRecordsByMemberId(Integer memberId, String visitType, Integer numberOfRecords);

    void updateHypertensionDetailsInNcdMemberDetail(Integer memberId);

}
