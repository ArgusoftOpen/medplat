package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.ncd.dto.MemberInitialAssessmentDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;

import java.util.Date;

public interface NcdInitialAssessmentService {

    MemberInitialAssessmentDetail saveInitialAssessment(MemberInitialAssessmentDto memberInitialAssessmentDto);
    MemberInitialAssessmentDetail retrieveInitialAssessmentDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type);
    MemberInitialAssessmentDetail retrieveLastRecordForInitialAssessmentByMemberId(Integer memberId);
    void updateMemberAdditionalInfoFromInitialAssessment(MemberEntity member, MemberInitialAssessmentDetail memberInitialAssessmentDetail, boolean isInitialAssessment);
}
