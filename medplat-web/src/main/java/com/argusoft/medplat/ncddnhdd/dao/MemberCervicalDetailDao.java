package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberCervicalDetail;

import java.util.Date;

/**
 *
 * <p>
 * Define methods for member cervical details.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface MemberCervicalDetailDao extends GenericDao<MemberCervicalDetail, Integer> {

    /**
     * Retrieves cervical details  by referral id and screening date.
     * @param referralId Referral id
     * @param screeningDate Screening date.
     * @return Returns list of cervical details by defined params.
     */
    MemberCervicalDetail retrieveForReferralId(Integer referralId, Date screeningDate);

    /**
     * Retrieves cervical details by member id and screening date.
     * @param memberId Member id.
     * @param screeningDate Screening date.
     * @return Returns list of cervical details by defined params.
     */
    MemberCervicalDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    /**
     * Retrieves last record of cervical for particular member id.
     * @param memberId Member id.
     * @return Returns last record of cervical.
     */
    MemberCervicalDetail retrieveLastRecordByMemberId(Integer memberId);
    MemberCervicalDetail retrieveFirstRecordByMemberId(Integer memberId);

}
