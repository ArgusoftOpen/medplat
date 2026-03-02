package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.model.SchoolBean;

import java.util.List;

public interface SchoolService {

    List<SchoolBean> retrieveSchoolsListBySearch(String search, Integer standard);

    List<SchoolBean> retrieveSchoolsListByLocationId(Long locationId, Integer standard);
}
