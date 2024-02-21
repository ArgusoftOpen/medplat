package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberBreastDetail;

import java.util.Date;

/**
 *
 * <p>
 * Define methods for member breast details.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface MemberBreastDetailDao extends GenericDao<MemberBreastDetail, Integer> {

    /**
     * Retrieves breast details by referral id and screening date.
     * @param referralId Referral id
     * @param screeningDate Screening date.
     * @return Returns list of hypertension details by defined params.
     */
    MemberBreastDetail retrieveForReferralId(Integer referralId, Date screeningDate);

    /**
     * Retrieves breast details by member id and screening date.
     * @param memberId Member id.
     * @param screeningDate Screening date.
     * @return Returns list of breast details by defined params.
     */
    MemberBreastDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    /**
     * Retrieves last record of breast for particular member id.
     * @param memberId Member id.
     * @return Returns last record of breast.
     */
    MemberBreastDetail retrieveLastRecordByMemberId(Integer memberId);
}
