package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberCervicalDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberCervicalDetail;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Date;
import java.util.Map;

public interface NcdCervicalService {

    void saveCervical(MemberCervicalDto cervicalDto);
    Integer storeCervicalForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
    MemberCervicalDetail retrieveCervicalDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type);
    MemberCervicalDetail retrieveLastRecordForCervicalByMemberId(Integer memberId);

}
