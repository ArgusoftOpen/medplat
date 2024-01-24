package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberDiabetesDetail;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for member diabetes details.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface MemberDiabetesDetailDao extends GenericDao<MemberDiabetesDetail, Integer> {

    /**
     * Retrieves diabetes details by referral id and screening date.
     * @param referralId Referral id
     * @param screeningDate Screening date.
     * @return Returns list of diabetes details by defined params.
     */
    MemberDiabetesDetail retrieveForReferralId(Integer referralId, Date screeningDate);

    List<MemberDiabetesDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId);

    /**
     * Retrieves diabetes details by member id and screening date.
     * @param memberId Member id.
     * @param screeningDate Screening date.
     * @return Returns list of diabetes details by defined params.
     */
    MemberDiabetesDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    /**
     * Retrieves last record of diabetes for particular member id.
     * @param memberId Member id.
     * @return Returns last record of diabetes.
     */
    MemberDiabetesDetail retrieveLastRecordByMemberId(Integer memberId);

    List<MemberDiabetesDetail> retrieveLastNRecordsByMemberIdAndMeasurement(Integer memberId, String measurementType, String visitType, Integer numberOfRecords);

    void updateDiabetesDetailsInNcdMemberDetail(Integer memberId);

}
