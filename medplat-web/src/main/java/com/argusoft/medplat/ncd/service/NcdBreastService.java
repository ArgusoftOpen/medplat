package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberBreastDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberBreastDetail;

import java.util.Date;
import java.util.Map;

public interface NcdBreastService {

    void saveBreast(MemberBreastDto breastDto);
    Integer storeBreastForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);

    MemberBreastDetail retrieveBreastDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type);
    MemberBreastDetail retrieveLastRecordForBreastByMemberId(Integer memberId);
}
