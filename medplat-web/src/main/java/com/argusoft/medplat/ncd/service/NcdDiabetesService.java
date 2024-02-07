package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberDiabetesDto;
import com.argusoft.medplat.ncd.dto.NcdDiabetesDetailDataBean;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberDiabetesDetail;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.Date;
import java.util.Map;

public interface NcdDiabetesService {

    MemberDiabetesDetail saveDiabetes(MemberDiabetesDto diabetesDto);
    Integer storeDiabetesForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);


    MemberDiabetesDetail retrieveDiabetesDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type);
    MemberDiabetesDto retrieveLastRecordForDiabetesByMemberId(Integer memberId);
    Integer storeDiabetesConfirmationForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
    NcdDiabetesDetailDataBean getDiabetesDetailDataBean(MemberDiabetesDetail diabetesDetail, NcdMemberEntity ncdMember);
}
