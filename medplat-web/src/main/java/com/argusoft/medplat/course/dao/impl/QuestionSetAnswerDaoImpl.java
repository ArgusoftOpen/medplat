package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.QuestionSetAnswerDao;
import com.argusoft.medplat.course.model.QuestionSetAnswerMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionSetAnswerDaoImpl extends GenericDaoImpl<QuestionSetAnswerMaster, Integer> implements QuestionSetAnswerDao {
}
