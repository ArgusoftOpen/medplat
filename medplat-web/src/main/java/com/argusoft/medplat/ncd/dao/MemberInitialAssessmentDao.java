package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;

import java.util.Date;
import java.util.List;


public interface MemberInitialAssessmentDao extends GenericDao<MemberInitialAssessmentDetail, Integer> {

    MemberInitialAssessmentDetail retrieveForReferralId(Integer referralId, Date screeningDate);
    List<MemberInitialAssessmentDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId);
    MemberInitialAssessmentDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);
    MemberInitialAssessmentDetail retrieveLastRecordByMemberId(Integer memberId);
}
