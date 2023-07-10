package com.argusoft.medplat.course.dao;

import com.argusoft.medplat.course.model.QuestionBankConfiguration;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

public interface QuestionBankConfigurationDao extends GenericDao<QuestionBankConfiguration, Integer> {

    public List<QuestionBankConfiguration> getQuestionBanksByQuestionSetId(Integer questionSetId);
}
