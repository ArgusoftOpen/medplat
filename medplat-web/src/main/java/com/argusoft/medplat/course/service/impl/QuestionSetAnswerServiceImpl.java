package com.argusoft.medplat.course.service.impl;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.course.dao.*;
import com.argusoft.medplat.course.dto.LmsUserQuizMetaData;
import com.argusoft.medplat.course.model.CourseMaster;
import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.course.model.QuestionSetAnswerMaster;
import com.argusoft.medplat.course.model.QuestionSetConfiguration;
import com.argusoft.medplat.course.service.QuestionSetAnswerService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.LmsQuizConfigDataBean;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class QuestionSetAnswerServiceImpl implements QuestionSetAnswerService {

    @Autowired
    private QuestionSetAnswerDao questionSetAnswerDao;

    @Autowired
    private QuestionSetConfigurationDao questionSetConfigurationDao;

    @Autowired
    private LmsUserMetaDataDao lmsUserMetaDataDao;

    @Autowired
    private TopicMediaMasterDao topicMediaMasterDao;

    @Autowired
    private CourseMasterDao courseMasterDao;

    @Override
    public Integer storeQuestionSetAnswerForMobile(ParsedRecordBean parsedRecordBean, UserMaster user) {
        QuestionSetAnswerMaster answerMaster = new GsonBuilder()
                .registerTypeAdapter(Date.class, MobileConstantUtil.jsonDateDeserializerStringFormat)
                .create().fromJson(parsedRecordBean.getAnswerRecord(), QuestionSetAnswerMaster.class);
        QuestionSetConfiguration questionSetConfiguration = questionSetConfigurationDao.retrieveById(answerMaster.getQuestionSetId());
        LmsUserMetaData lmsUserMetaData = lmsUserMetaDataDao.retrieveByUserIdAndCourseId(user.getId(), questionSetConfiguration.getCourseId());
        CourseMaster courseMaster = courseMasterDao.retrieveById(questionSetConfiguration.getCourseId());
        LmsQuizConfigDataBean lmsQuizConfigDataBean = null;

        if (courseMaster != null && courseMaster.getTestConfigJson() != null) {
            Map<Integer, LmsQuizConfigDataBean> lmsQuizConfigDataBeanMap = new Gson().fromJson(courseMaster.getTestConfigJson(), new TypeToken<Map<Integer, LmsQuizConfigDataBean>>() {
            }.getType());
            lmsQuizConfigDataBean = lmsQuizConfigDataBeanMap.get(questionSetConfiguration.getQuestionSetType());
        }

        if (lmsUserMetaData == null) {
            createNewUserQuizMetaData(user, questionSetConfiguration, answerMaster, lmsQuizConfigDataBean);
        } else {
            updateUserQuizMetaData(lmsUserMetaData, questionSetConfiguration, answerMaster, lmsQuizConfigDataBean);
        }
        return questionSetAnswerDao.create(answerMaster);
    }

    private void createNewUserQuizMetaData(UserMaster user, QuestionSetConfiguration questionSetConfiguration, QuestionSetAnswerMaster answerMaster, LmsQuizConfigDataBean lmsQuizConfigDataBean) {
        LmsUserQuizMetaData quizMetaData = new LmsUserQuizMetaData();
        quizMetaData.setQuizAttempts(1);
        quizMetaData.setScoreInFirstAttempt(answerMaster.getMarksScored());
        quizMetaData.setQuizRefId(questionSetConfiguration.getRefId());
        quizMetaData.setQuizRefType(questionSetConfiguration.getRefType());
        quizMetaData.setQuizTypeId(questionSetConfiguration.getQuestionSetType());
        quizMetaData.setLatestScore(answerMaster.getMarksScored());
        quizMetaData.setLastQuizDate(answerMaster.getEndDate().getTime());
        if (quizMetaData.getQuizAttemptsToPass() == null && questionSetConfiguration.getMinimumMarks() != null
                && answerMaster.getMarksScored() != null && answerMaster.getMarksScored() >= questionSetConfiguration.getMinimumMarks()) {
            quizMetaData.setQuizAttemptsToPass(quizMetaData.getQuizAttempts());
            quizMetaData.setScoreWhenPassed(answerMaster.getMarksScored());
        }
        if (questionSetConfiguration.getRefType().equals("MODULE")) {
            quizMetaData.setModuleId(questionSetConfiguration.getRefId());
        } else if (questionSetConfiguration.getRefType().equals("LESSON")) {
            quizMetaData.setLessonId(questionSetConfiguration.getRefId());
            quizMetaData.setModuleId(topicMediaMasterDao.retrieveById(questionSetConfiguration.getRefId()).getTopicId());
        }
        if (lmsQuizConfigDataBean != null && Boolean.TRUE.equals(lmsQuizConfigDataBean.getIsCaseStudyQuestionSetType())) {
            quizMetaData.setQuestionSetId(questionSetConfiguration.getId());
        }
        if (Boolean.TRUE.equals(answerMaster.getLocked())) {
            quizMetaData.setLocked(true);
        }
        List<LmsUserQuizMetaData> quizMetaDataList = new ArrayList<>();
        quizMetaDataList.add(quizMetaData);

        LmsUserMetaData lmsUserMetaData = new LmsUserMetaData();
        lmsUserMetaData.setUserId(user.getId());
        lmsUserMetaData.setCourseId(questionSetConfiguration.getCourseId());
        lmsUserMetaData.setQuizMetaData(new Gson().toJson(quizMetaDataList));
        lmsUserMetaData.setLastAccessedQuizOn(answerMaster.getEndDate());
        lmsUserMetaDataDao.create(lmsUserMetaData);
    }

    private void updateUserQuizMetaData(LmsUserMetaData lmsUserMetaData, QuestionSetConfiguration questionSetConfiguration, QuestionSetAnswerMaster answerMaster, LmsQuizConfigDataBean lmsQuizConfigDataBean) {
        List<LmsUserQuizMetaData> quizMetaDataList = new ArrayList<>();
        if (lmsUserMetaData.getQuizMetaData() != null) {
            quizMetaDataList = new Gson().fromJson(lmsUserMetaData.getQuizMetaData(), new TypeToken<List<LmsUserQuizMetaData>>() {
            }.getType());
        }
        boolean isUpdated = false;
        for (LmsUserQuizMetaData quizMetaData : quizMetaDataList) {
            if (quizMetaData.getQuizRefId().equals(questionSetConfiguration.getRefId())
                    && quizMetaData.getQuizRefType().equals(questionSetConfiguration.getRefType())
                    && quizMetaData.getQuizTypeId().equals(questionSetConfiguration.getQuestionSetType())) {

                if (lmsQuizConfigDataBean != null && Boolean.TRUE.equals(lmsQuizConfigDataBean.getIsCaseStudyQuestionSetType())) {
                    if (quizMetaData.getQuestionSetId() != null && quizMetaData.getQuestionSetId().equals(questionSetConfiguration.getQuestionSetType())) {
                        quizMetaData.setQuestionSetId(questionSetConfiguration.getId());
                        quizMetaData.setQuizAttempts(quizMetaData.getQuizAttempts() != null ? quizMetaData.getQuizAttempts() + 1 : 1);
                        if (quizMetaData.getQuizAttemptsToPass() == null && questionSetConfiguration.getMinimumMarks() != null
                                && answerMaster.getMarksScored() != null && answerMaster.getMarksScored() >= questionSetConfiguration.getMinimumMarks()) {
                            quizMetaData.setQuizAttemptsToPass(quizMetaData.getQuizAttempts());
                            quizMetaData.setScoreWhenPassed(answerMaster.getMarksScored());
                        }
                        quizMetaData.setLatestScore(answerMaster.getMarksScored());
                        quizMetaData.setLastQuizDate(answerMaster.getEndDate().getTime());
                        if (Boolean.TRUE.equals(answerMaster.getLocked())) {
                            quizMetaData.setLocked(true);
                        }
                        isUpdated = true;
                    }
                } else {
                    quizMetaData.setQuizAttempts(quizMetaData.getQuizAttempts() != null ? quizMetaData.getQuizAttempts() + 1 : 1);
                    if (quizMetaData.getQuizAttemptsToPass() == null && questionSetConfiguration.getMinimumMarks() != null
                            && answerMaster.getMarksScored() != null && answerMaster.getMarksScored() >= questionSetConfiguration.getMinimumMarks()) {
                        quizMetaData.setQuizAttemptsToPass(quizMetaData.getQuizAttempts());
                        quizMetaData.setScoreWhenPassed(answerMaster.getMarksScored());
                    }
                    if (Boolean.TRUE.equals(answerMaster.getLocked())) {
                        quizMetaData.setLocked(true);
                    }
                    quizMetaData.setLatestScore(answerMaster.getMarksScored());
                    quizMetaData.setLastQuizDate(answerMaster.getEndDate().getTime());
                    isUpdated = true;
                }
                break;
            }
        }

        if (!isUpdated) {
            LmsUserQuizMetaData quizMetaData = new LmsUserQuizMetaData();
            quizMetaData.setQuizAttempts(1);
            quizMetaData.setScoreInFirstAttempt(answerMaster.getMarksScored());
            quizMetaData.setQuizRefId(questionSetConfiguration.getRefId());
            quizMetaData.setQuizRefType(questionSetConfiguration.getRefType());
            quizMetaData.setQuizTypeId(questionSetConfiguration.getQuestionSetType());
            quizMetaData.setLatestScore(answerMaster.getMarksScored());
            quizMetaData.setLastQuizDate(answerMaster.getEndDate().getTime());
            if (quizMetaData.getQuizAttemptsToPass() == null && questionSetConfiguration.getMinimumMarks() != null
                    && answerMaster.getMarksScored() != null && answerMaster.getMarksScored() >= questionSetConfiguration.getMinimumMarks()) {
                quizMetaData.setQuizAttemptsToPass(quizMetaData.getQuizAttempts());
                quizMetaData.setScoreWhenPassed(answerMaster.getMarksScored());
            }
            if (lmsQuizConfigDataBean != null && Boolean.TRUE.equals(lmsQuizConfigDataBean.getIsCaseStudyQuestionSetType())) {
                quizMetaData.setQuestionSetId(questionSetConfiguration.getId());
            }
            if (questionSetConfiguration.getRefType().equals("MODULE")) {
                quizMetaData.setModuleId(questionSetConfiguration.getRefId());
            } else if (questionSetConfiguration.getRefType().equals("LESSON")) {
                quizMetaData.setLessonId(questionSetConfiguration.getRefId());
                quizMetaData.setModuleId(topicMediaMasterDao.retrieveById(questionSetConfiguration.getRefId()).getTopicId());
            }
            if (Boolean.TRUE.equals(answerMaster.getLocked())) {
                quizMetaData.setLocked(true);
            }
            quizMetaDataList.add(quizMetaData);
        }
        lmsUserMetaData.setQuizMetaData(new Gson().toJson(quizMetaDataList));
        lmsUserMetaData.setLastAccessedQuizOn(answerMaster.getEndDate());
        lmsUserMetaDataDao.update(lmsUserMetaData);
    }
}
