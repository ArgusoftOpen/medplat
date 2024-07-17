package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberOralDetail;

import java.util.Date;

/**
 *
 * <p>
 * Define methods for member oral details.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface MemberOralDetailDao extends GenericDao<MemberOralDetail, Integer> {

    /**
     * Retrieves oral details by referral id and screening date.
     * @param referralId Referral id
     * @param screeningDate Screening date.
     * @return Returns list of oral details by defined params.
     */
    MemberOralDetail retrieveForReferralId(Integer referralId, Date screeningDate);

    /**
     * Retrieves oral details by member id and screening date.
     * @param memberId Member id.
     * @param screeningDate Screening date.
     * @return Returns list of oral details by defined params.
     */
    MemberOralDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    /**
     * Retrieves last record of oral for particular member id.
     * @param memberId Member id.
     * @return Returns last record of oral.
     */
    MemberOralDetail retrieveLastRecordByMemberId(Integer memberId);
    MemberOralDetail retrieveFirstRecordByMemberId(Integer memberId);

}
