package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dto.MemberCbcDataBean;

import java.util.List;
import java.util.Map;

public interface AnemiaSurveyService {

//    Integer storeAnemiaSurveyForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
//    Integer storeAnemiaProtocolDeviationForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
//    Integer storeAnemiaCrfForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user);
    MemberCbcDataBean getMemberCbcDetail(Integer patientId);
}
