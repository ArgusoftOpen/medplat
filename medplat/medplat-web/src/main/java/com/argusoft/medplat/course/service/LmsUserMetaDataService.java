package com.argusoft.medplat.course.service;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.course.dto.LmsMobileEventDto;

public interface LmsUserMetaDataService {

    Integer storeLmsLessonStartDate(UserMaster user, LmsMobileEventDto dto);

    Integer storeLmsLessonEndDate(UserMaster user, LmsMobileEventDto dto);

    Integer storeLmsLessonSession(UserMaster user, LmsMobileEventDto dto);

    Integer storeLmsLessonFeedback(UserMaster user, LmsMobileEventDto dto);

    Integer storeLmsLessonCompleted(UserMaster user, LmsMobileEventDto dto);

    Integer storeLmsLessonPausedOn(UserMaster user, LmsMobileEventDto dto);

}
