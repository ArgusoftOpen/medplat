package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.MemberGeneralDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberGeneralDetail;

import java.util.Date;

public interface NcdGeneralService {

    MemberGeneralDetail saveGeneral(MemberGeneralDto memberGeneralDto);
    MemberGeneralDto retrieveGeneralDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type);
    MemberGeneralDetail retrieveLastRecordForGeneralByMemberId(Integer memberId);

    MemberGeneralDto retrieveLastCommentForGeneralByMemberIdAndType(Integer memberId, DoneBy type);
}
