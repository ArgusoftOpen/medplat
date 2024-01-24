package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberHyperTensionDto;
import com.argusoft.medplat.ncd.dto.NcdHypertensionDetailDataBean;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NcdHypertensionService {

    MemberHypertensionDetail saveHypertension(MemberHyperTensionDto hyperTensionDto);
    Integer storeHypertensionForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
    MemberHypertensionDetail retrieveHypertensionDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type);
    List<MemberHyperTensionDto> retrieveLastRecordForHypertensionByMemberId(Integer memberId);
    NcdHypertensionDetailDataBean getHypertensionDetailDataBean(MemberHypertensionDetail hypertensionDetail, NcdMemberEntity ncdMember);

}
