package com.argusoft.medplat.course.dao;

import com.argusoft.medplat.course.model.QuestionSetConfiguration;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

public interface QuestionSetConfigurationDao extends GenericDao<QuestionSetConfiguration, Integer> {

    public List<QuestionSetConfiguration> getQuestionSetByReferenceIdAndType(Integer refId, String refType, Integer questionSetType);
}
