package com.argusoft.medplat.course.service;

import com.argusoft.medplat.course.dto.LmsMobileEventDto;
import com.argusoft.medplat.mobile.dto.RecordStatusBean;

import java.util.List;

public interface LmsMobileEventSubmissionService {

    List<RecordStatusBean> storeLmsMobileEventToDB(String token, List<LmsMobileEventDto> mobileEventDtos);
}
