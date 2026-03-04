package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberCervicalDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberMentalHealthDetails;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Add description here
 * </p>
 *
 * @author namrata
 * @since 28/04/2022 10:09 AM
 */
public interface MemberMentalHealthDetailDao extends GenericDao<MemberMentalHealthDetails, Integer> {

    MemberMentalHealthDetails retrieveForReferralId(Integer referralId, Date screeningDate);

    List<MemberMentalHealthDetails> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId);

    MemberMentalHealthDetails retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    MemberMentalHealthDetails retrieveLastRecordByMemberId(Integer memberId);

    List<MemberMentalHealthDetails> retrieveLastNRecordsByMemberId(Integer memberId, String visitType, Integer numberOfRecords);

    void updateMentalHealthDetailsInNcdMemberDetail(Integer memberId);
}
