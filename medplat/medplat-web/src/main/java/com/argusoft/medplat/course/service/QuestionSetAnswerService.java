package com.argusoft.medplat.course.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;

public interface QuestionSetAnswerService {

    Integer storeQuestionSetAnswerForMobile(ParsedRecordBean parsedRecordBean, UserMaster user);
}
