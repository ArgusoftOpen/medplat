package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberMentalHealthDto;
import com.argusoft.medplat.ncd.dto.NcdMentalHealthDetailDataBean;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberMentalHealthDetails;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Date;
import java.util.Map;

public interface NcdMentalHealthService {

    MemberMentalHealthDetails saveMentalHealth(MemberMentalHealthDto memberMentalHealthDto);

    Integer storeMentalHealthForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    MemberMentalHealthDetails retrieveMentalHealthDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type);
    MemberMentalHealthDetails retrieveLastRecordForMentalHealthByMemberId(Integer memberId);
    NcdMentalHealthDetailDataBean getMentalHealthDetailDataBean(MemberMentalHealthDetails mentalHealthDetails, NcdMemberEntity ncdMember);
}
