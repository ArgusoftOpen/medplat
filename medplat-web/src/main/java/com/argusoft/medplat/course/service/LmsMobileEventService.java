package com.argusoft.medplat.course.service;

import com.argusoft.medplat.course.model.LmsMobileEventMaster;

public interface LmsMobileEventService {

    LmsMobileEventMaster retrieveMobileEventMaster(String checksum);

    void createOrUpdateLmsMobileEventMaster(LmsMobileEventMaster lmsMobileEventMaster);
}
