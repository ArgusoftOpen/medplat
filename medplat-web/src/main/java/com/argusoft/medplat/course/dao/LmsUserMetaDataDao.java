package com.argusoft.medplat.course.dao;

import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

public interface LmsUserMetaDataDao extends GenericDao<LmsUserMetaData, Integer> {

    List<LmsUserMetaData> retrieveByUserId(Integer userId);

    LmsUserMetaData retrieveByUserIdAndCourseId(Integer userId, Integer courseId);
}
