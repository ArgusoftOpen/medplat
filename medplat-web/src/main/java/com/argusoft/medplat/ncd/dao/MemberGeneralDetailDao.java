package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.MedicineDto;
import com.argusoft.medplat.ncd.dto.MemberGeneralDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;

import java.util.Date;
import java.util.List;

public interface MemberGeneralDetailDao extends GenericDao<MemberGeneralDetail, Integer> {

    MemberGeneralDetail retrieveForReferralId(Integer referralId, Date screeningDate);

    MemberGeneralDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type);

    List<MemberGeneralDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId);

    MemberGeneralDetail retrieveLastRecordByMemberId(Integer memberId);

    List<MedicineDto> retrieveGeneralDrugs();

    MemberGeneralDto retrieveLastCommentForGeneralByMemberIdAndType(Integer memberId, DoneBy type);
}
