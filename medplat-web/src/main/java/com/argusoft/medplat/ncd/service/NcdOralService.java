package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberOralDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberOralDetail;

import java.util.Date;
import java.util.Map;

public interface NcdOralService {

    void saveOral(MemberOralDto oralDto);

    Integer storeOralForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    MemberOralDetail retrieveOralDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type);
    MemberOralDetail retrieveLastRecordForOralByMemberId(Integer memberId);
}
