package com.argusoft.sewa.android.app.core.impl;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LmsEventConstants;
import com.argusoft.sewa.android.app.core.LmsService;
import com.argusoft.sewa.android.app.databean.BookmarkDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuizConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserLessonSessionMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserQuizMetaData;
import com.argusoft.sewa.android.app.databean.LmsViewedMediaDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.model.LmsEventBean;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EBean(scope = Singleton)
public class LmsServiceImpl implements LmsService {

    @Bean
    SewaServiceImpl sewaService;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsCourseBean, Integer> courseBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsBookMarkBean, Integer> bookMarkBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsViewedMediaBean, Integer> viewedMediaBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsUserMetaDataBean, Integer> quizMetadataBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsEventBean, Integer> eventBeanDao;

    @Override
    public List<LmsCourseDataBean> retrieveCourses() {
        List<LmsCourseDataBean> lmsCourseDataBeans = new ArrayList<>();
        int total;
        int completed;
        int percent;

        try {
            List<LmsCourseBean> lmsCourseBeans = courseBeanDao.queryBuilder().where().le(FieldNameConstants.SCHEDULE_DATE, new Date()).query();
            for (LmsCourseBean bean : lmsCourseBeans) {
                total = 0;
                completed = 0;

                LmsCourseDataBean dataBean = new LmsCourseDataBean(bean);
                if (dataBean.getTopics() != null && !dataBean.getTopics().isEmpty()) {
                    for (LmsTopicDataBean topic : dataBean.getTopics()) {
                        if (topic.getTopicMedias() != null && !topic.getTopicMedias().isEmpty()) {
                            for (LmsLessonDataBean lesson : topic.getTopicMedias()) {
                                LmsViewedMediaBean viewedMediaBean = getViewedLessonById(lesson.getActualId());
                                if (viewedMediaBean != null && (viewedMediaBean.getCompleted() != null && viewedMediaBean.getCompleted())) {
                                    if (lesson.getUserFeedbackRequired() != null && lesson.getUserFeedbackRequired() && viewedMediaBean.getUserFeedback() != null) {
                                        completed++;
                                    } else if (lesson.getUserFeedbackRequired() != null && !lesson.getUserFeedbackRequired()) {
                                        completed++;
                                    }
                                }
                                total++;
                            }
                        }
                    }
                }

                if (dataBean.getTopics() != null && !dataBean.getTopics().isEmpty()) {
                    if (dataBean.getQuestionSet() != null && !dataBean.getQuestionSet().isEmpty()) {
                        for (LmsQuestionSetDataBean lmsQuestionSetDataBean : dataBean.getQuestionSet()) {
                            total++;
                            if (isQuizMinimumMarks(dataBean.getCourseId(), null, null, lmsQuestionSetDataBean.getQuestionSetType())) {
                                completed++;
                            }
                        }

                    }

                    for (LmsTopicDataBean lmsTopicDataBean : dataBean.getTopics()) {
                        if (lmsTopicDataBean.getQuestionSet() != null && !lmsTopicDataBean.getQuestionSet().isEmpty()) {
                            for (LmsQuestionSetDataBean lmsQuestionSetDataBean : lmsTopicDataBean.getQuestionSet()) {
                                total++;
                                if (isQuizMinimumMarks(dataBean.getCourseId(), lmsTopicDataBean.getTopicId(), null, lmsQuestionSetDataBean.getQuestionSetType())) {
                                    completed++;
                                }
                            }

                        }

                        for (LmsLessonDataBean lmsLessonDataBean : lmsTopicDataBean.getTopicMedias()) {
                            if (lmsLessonDataBean.getQuestionSet() != null && !lmsLessonDataBean.getQuestionSet().isEmpty()) {
                                for (LmsQuestionSetDataBean lmsQuestionSetDataBean : lmsLessonDataBean.getQuestionSet()) {
                                    total++;
                                    if (isQuizMinimumMarks(dataBean.getCourseId(), lmsTopicDataBean.getTopicId(), lmsLessonDataBean.getActualId(), lmsQuestionSetDataBean.getQuestionSetType())) {
                                        completed++;
                                    }
                                }

                            }
                        }
                    }
                }

                if (total == 0) {
                    percent = 0;
                } else {
                    percent = completed * 100 / total;
                }
                dataBean.setCompletionStatus(percent);
                lmsCourseDataBeans.add(dataBean);
            }
            Collections.sort(lmsCourseDataBeans, MyComparatorUtil.LMS_COURSE_COMPLETION_COMPARATOR);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return lmsCourseDataBeans;
    }

    @Override
    public void makeCourseArchive(Integer courseId, boolean isArchive) {
        if (courseId == null) {
            return;
        }
        try {
            LmsCourseBean lmsCourseBean = courseBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
            lmsCourseBean.setArchived(isArchive);
            courseBeanDao.update(lmsCourseBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void makeCourseDownloadable(Integer courseId, boolean isMediaDownloaded) {
        if (courseId == null) {
            return;
        }
        try {
            LmsCourseBean lmsCourseBean = courseBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
            lmsCourseBean.setMediaDownloaded(isMediaDownloaded);
            courseBeanDao.update(lmsCourseBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public LmsCourseDataBean retrieveCourseByCourseId(Integer courseId) {
        if (courseId == null) {
            return null;
        }

        try {
            LmsCourseBean lmsCourseBean = courseBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
            return new LmsCourseDataBean(lmsCourseBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return null;
    }

    @Override
    public void deleteBookmark(LmsBookMarkBean bookmarkDataBean) {
        try {
            bookMarkBeanDao.delete(bookmarkDataBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void updateBookmark(LmsBookMarkBean bookmarkDataBean) {
        try {
            bookMarkBeanDao.update(bookmarkDataBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public List<LmsBookMarkBean> retrieveBookmarks(Integer lessonId) {
        List<LmsBookMarkBean> bookmarkBeanLms = new LinkedList<>();
        try {
            bookmarkBeanLms = bookMarkBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).query();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return bookmarkBeanLms;
    }

    @Override
    public void saveBookmark(Integer lessonId, BookmarkDataBean bookmarkDataBean) {
        try {
            LmsBookMarkBean lmsBookMarkBean = new LmsBookMarkBean();
            lmsBookMarkBean.setPosition(bookmarkDataBean.getPosition());
            lmsBookMarkBean.setBookmarkText(bookmarkDataBean.getBookmarkText());
            lmsBookMarkBean.setBookmarkNote(bookmarkDataBean.getBookmarkNote());
            lmsBookMarkBean.setFileName(bookmarkDataBean.getFileName());
            lmsBookMarkBean.setLessonId(lessonId);
            bookMarkBeanDao.create(lmsBookMarkBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public Map<Integer, Boolean> getAllowedMediaMap(Integer courseId) {
        Map<Integer, Boolean> allowedMediaMap = new HashMap<>();
        List<Integer> viewedLessons = getViewedLessonList();
        LmsCourseDataBean course = retrieveCourseByCourseId(courseId);
        Integer previousLessonId = null;

        List<LmsTopicDataBean> topics = course.getTopics();
        Collections.sort(topics, MyComparatorUtil.LMS_TOPIC_ORDER_COMPARATOR);
        for (LmsTopicDataBean topic : topics) {
            List<LmsLessonDataBean> medias = topic.getTopicMedias();
            Collections.sort(medias, MyComparatorUtil.LMS_TOPIC_MEDIA_ORDER_COMPARATOR);
            for (LmsLessonDataBean media : medias) {
                allowedMediaMap.put(media.getActualId(), (topic.getTopicOrder().equals("1") && media.getMediaOrder() == 1) || viewedLessons.contains(previousLessonId));
                previousLessonId = media.getActualId();
            }
        }
        return allowedMediaMap;
    }

    @Override
    public Boolean isQuizMinimumMarks(Integer courseId, Integer moduleId, Integer lessonId) {
        LmsCourseDataBean course = retrieveCourseByCourseId(courseId);
        boolean isPassedMarks = true;
        for (LmsTopicDataBean lmsTopicDataBean : course.getTopics()) {
            for (LmsLessonDataBean lmsLessonDataBean : lmsTopicDataBean.getTopicMedias()) {
                if (Objects.equals(lmsTopicDataBean.getTopicId(), moduleId) && Objects.equals(lmsLessonDataBean.getActualId(), lessonId)) {
                    return isPassedMarks;
                }
                if (lmsLessonDataBean.getQuestionSet() != null && !lmsLessonDataBean.getQuestionSet().isEmpty()) {
                    for (LmsQuestionSetDataBean lmsQuestionSetDataBean : lmsLessonDataBean.getQuestionSet()) {
                        if (lmsQuestionSetDataBean.getMinimumMarks() != null) {
                            if (!isPassedMarks) break;
                            isPassedMarks =
                                    getQuizScore(courseId, lmsTopicDataBean.getTopicId(), lmsLessonDataBean.getActualId(), lmsQuestionSetDataBean.getQuestionSetType()) >= lmsQuestionSetDataBean.getMinimumMarks();
                        } else isPassedMarks = true;
                    }
                }
            }
            if (lmsTopicDataBean.getQuestionSet() != null && !lmsTopicDataBean.getQuestionSet().isEmpty()) {
                for (LmsQuestionSetDataBean lmsQuestionSetDataBean : lmsTopicDataBean.getQuestionSet()) {
                    if (lmsQuestionSetDataBean.getMinimumMarks() != null) {
                        if (!isPassedMarks) break;
                        isPassedMarks =
                                getQuizScore(courseId, lmsTopicDataBean.getTopicId(), null, lmsQuestionSetDataBean.getQuestionSetType()) >= lmsQuestionSetDataBean.getMinimumMarks();
                    } else isPassedMarks = true;
                }
            }
        }
        return true;
    }

    private Integer getQuizScore(Integer courseId, Integer moduleId, Integer lessonId, Integer questionTypeId) {
        if (courseId == null) {
            return 0;
        }
        LmsUserMetaDataBean lmsUserMetaDataBean = null;
        try {
            lmsUserMetaDataBean = quizMetadataBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (lmsUserMetaDataBean == null) {
            return 0;
        }

        LmsUserMetaData lmsUserMetaData = new LmsUserMetaData(lmsUserMetaDataBean);
        List<LmsUserQuizMetaData> quizMetaData = lmsUserMetaData.getQuizMetaData();
        if (quizMetaData != null) {
            Integer refId;
            if (lessonId == null) {
                refId = moduleId;
            } else {
                refId = lessonId;
            }
            for (LmsUserQuizMetaData quiz : quizMetaData) {
                if (Objects.equals(quiz.getQuizRefId(), refId) && quiz.getQuizTypeId().equals(questionTypeId)) {
                    return quiz.getLatestScore();
                }
            }
        }
        return 0;
    }

    @Override
    public List<Integer> getViewedLessonList() {
        try {
            List<Integer> viewedMedias = new ArrayList<>();
            List<LmsViewedMediaBean> mediaBeanList = viewedMediaBeanDao.queryBuilder().where().eq("isCompleted", true).query();
            for (LmsViewedMediaBean lmsViewedMediaBean : mediaBeanList) {
                viewedMedias.add(lmsViewedMediaBean.getLessonId());
            }
            return viewedMedias;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void storeLmsTestResult(LmsQuestionSetAnswerDataBean questionSetAnswer, String testFor) {
        Log.i(getClass().getSimpleName(), "Storing LMS Test Result : " + new Gson().toJson(questionSetAnswer));

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setAnswerEntity(FormConstants.LMS_TEST);
        storeAnswerBean.setAnswer(new Gson().toJson(questionSetAnswer));
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(-1L);
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        if (SewaTransformer.loginBean != null) {
            storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
            storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        }
        sewaService.createStoreAnswerBean(storeAnswerBean);

        LoggerBean loggerBean = new LoggerBean();
        loggerBean.setBeneficiaryName(testFor);
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setFormType(FormConstants.LMS_TEST);
        loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.LMS_TEST));
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    public Integer getLastPausedOn(Integer lessonId) {
        try {
            LmsViewedMediaBean lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
            if (lmsViewedMediaBean != null && lmsViewedMediaBean.getLastPausedOn() != null) {
                return lmsViewedMediaBean.getLastPausedOn();
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return 0;
    }

    @Override
    public LmsUserMetaData getLmsUserMetaDataByCourseId(Integer courseId) {
        try {
            return new LmsUserMetaData(quizMetadataBeanDao.queryBuilder().where().eq("courseId", courseId).queryForFirst());
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return null;
    }

    @Override
    public LmsViewedMediaBean getViewedLessonById(Integer lessonId) {
        try {
            return viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return null;
    }

    @Override
    public boolean isAnyUpdatedDataAvailable() {
        boolean isUpdateAvailable = false;
        try {
            List<LmsCourseDataBean> lmsCourseDataBeans = retrieveCourses();

            for (LmsCourseDataBean lmsCourseBean : lmsCourseDataBeans) {
                if(lmsCourseBean.getCompletionStatus() < 100){
                    LmsViewedMediaDataBean lmsViewedMediaDataBean = getViewedCourseById(lmsCourseBean.getCourseId());
                    if(lmsViewedMediaDataBean == null){
                        isUpdateAvailable = true;
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return isUpdateAvailable;
    }

    @Override
    public LmsViewedMediaDataBean getViewedCourseById(Integer courseId) {
        try {
            LmsViewedMediaBean lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().
                    eq(FieldNameConstants.COURSE_ID, courseId).and().
                    eq("isViewed",true).and().
                    eq("isCompleted", false)
                    .queryForFirst();
            if(lmsViewedMediaBean != null)
                return new LmsViewedMediaDataBean(lmsViewedMediaBean);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return null;
    }

    @Override
    public void markCourseViewed(Integer courseId) {
        LmsViewedMediaBean lmsViewedMediaBean = null;
        try {
            lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().
                    eq(FieldNameConstants.COURSE_ID, courseId).and().eq("isCompleted", false).queryForFirst();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (lmsViewedMediaBean != null) {
            if (!Boolean.TRUE.equals(lmsViewedMediaBean.getViewed())) {
                lmsViewedMediaBean.setViewed(true);
            }
            try {
                viewedMediaBeanDao.update(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            lmsViewedMediaBean = new LmsViewedMediaBean();
            lmsViewedMediaBean.setLessonId(null);
            lmsViewedMediaBean.setModuleId(null);
            lmsViewedMediaBean.setCourseId(courseId);
            lmsViewedMediaBean.setViewed(true);
            lmsViewedMediaBean.setCompleted(false);
            lmsViewedMediaBean.setLastPausedOn(null);
            try {
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    @Override
    public void markCourseDataViewed(Integer courseId, List<LmsTopicDataBean> topics, List<LmsQuestionSetDataBean> questionSet) {
        LmsViewedMediaBean lmsViewedMediaBean = null;
        try {
            lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().
                    eq(FieldNameConstants.COURSE_ID, courseId).and().eq("isCompleted", false).queryForFirst();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (lmsViewedMediaBean != null) {
            if (!Boolean.TRUE.equals(lmsViewedMediaBean.getViewed())) {
                lmsViewedMediaBean.setViewed(true);
            }
            lmsViewedMediaBean.setTopics(new Gson().toJson(topics));
            lmsViewedMediaBean.setQuestionSet(new Gson().toJson(questionSet));
            try {
                viewedMediaBeanDao.update(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            lmsViewedMediaBean = new LmsViewedMediaBean();
            lmsViewedMediaBean.setLessonId(null);
            lmsViewedMediaBean.setModuleId(null);
            lmsViewedMediaBean.setCourseId(courseId);
            lmsViewedMediaBean.setViewed(true);
            lmsViewedMediaBean.setCompleted(false);
            lmsViewedMediaBean.setLastPausedOn(null);
            lmsViewedMediaBean.setTopics(new Gson().toJson(topics));
            lmsViewedMediaBean.setQuestionSet(new Gson().toJson(questionSet));
            try {
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void markLessonCompleted(Integer lessonId, Integer moduleId, Integer courseId) {
        LmsViewedMediaBean lmsViewedMediaBean = null;
        try {
            lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (lmsViewedMediaBean != null) {
            if (!Boolean.TRUE.equals(lmsViewedMediaBean.getCompleted())) {
                lmsViewedMediaBean.setCompleted(true);
                createStoreEventForLessonCompleted(courseId, moduleId, lessonId, true);
            }
            lmsViewedMediaBean.setLastPausedOn(null);
            try {
                viewedMediaBeanDao.update(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            lmsViewedMediaBean = new LmsViewedMediaBean();
            lmsViewedMediaBean.setLessonId(lessonId);
            lmsViewedMediaBean.setModuleId(moduleId);
            lmsViewedMediaBean.setCourseId(courseId);
            lmsViewedMediaBean.setCompleted(true);
            lmsViewedMediaBean.setLastPausedOn(null);
            try {
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            createStoreEventForLessonCompleted(courseId, moduleId, lessonId, true);
        }
    }

    @Override
    public void updateLastPausedOnForMedia(Integer lastPausedOn, Integer lessonId, Integer moduleId, Integer courseId) {
        LmsViewedMediaBean lmsViewedMediaBean = null;
        try {
            lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (lmsViewedMediaBean != null) {
            lmsViewedMediaBean.setLastPausedOn(lastPausedOn);
            try {
                viewedMediaBeanDao.update(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            lmsViewedMediaBean = new LmsViewedMediaBean();
            lmsViewedMediaBean.setLessonId(lessonId);
            lmsViewedMediaBean.setModuleId(moduleId);
            lmsViewedMediaBean.setCourseId(courseId);
            lmsViewedMediaBean.setLastPausedOn(lastPausedOn);
            try {
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        createStoreEventForLessonPausedOn(courseId, moduleId, lessonId, lastPausedOn);
    }

    @Override
    public void updateStartDateForMedia(Date startDate, Integer lessonId, Integer moduleId, Integer courseId) {
        LmsViewedMediaBean lmsViewedMediaBean = null;
        try {
            lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (lmsViewedMediaBean != null) {
            if (lmsViewedMediaBean.getStartDate() == null) {
                lmsViewedMediaBean.setStartDate(startDate);
                try {
                    viewedMediaBeanDao.update(lmsViewedMediaBean);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                createStoreEventForLessonStartDate(courseId, moduleId, lessonId, startDate);
            }
        } else {
            lmsViewedMediaBean = new LmsViewedMediaBean();
            lmsViewedMediaBean.setLessonId(lessonId);
            lmsViewedMediaBean.setModuleId(moduleId);
            lmsViewedMediaBean.setCourseId(courseId);
            lmsViewedMediaBean.setStartDate(startDate);
            try {
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            createStoreEventForLessonStartDate(courseId, moduleId, lessonId, startDate);
        }
    }

    @Override
    public void updateEndDateForMedia(Date endDate, Integer lessonId, Integer moduleId, Integer courseId) {
        LmsViewedMediaBean lmsViewedMediaBean = null;
        try {
            lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (lmsViewedMediaBean != null) {
            if (lmsViewedMediaBean.getEndDate() == null) {
                lmsViewedMediaBean.setEndDate(endDate);
                try {
                    viewedMediaBeanDao.update(lmsViewedMediaBean);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                createStoreEventForLessonEndDate(courseId, moduleId, lessonId, endDate);
            }
        } else {
            lmsViewedMediaBean = new LmsViewedMediaBean();
            lmsViewedMediaBean.setLessonId(lessonId);
            lmsViewedMediaBean.setModuleId(moduleId);
            lmsViewedMediaBean.setCourseId(courseId);
            lmsViewedMediaBean.setEndDate(endDate);
            try {
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            createStoreEventForLessonEndDate(courseId, moduleId, lessonId, endDate);
        }
    }

    @Override
    public void addSessionsForMedia(Date startDate, Date endDate, Integer lessonId, Integer moduleId, Integer courseId) {
        if (startDate == null || endDate == null) {
            return;
        }
        LmsUserLessonSessionMetaData sessionMetaData = new LmsUserLessonSessionMetaData();
        sessionMetaData.setStartDate(startDate.getTime());
        sessionMetaData.setEndDate(endDate.getTime());
        try {
            List<LmsUserLessonSessionMetaData> sessionMetaDataList = new ArrayList<>();
            LmsViewedMediaBean lmsViewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
            if (lmsViewedMediaBean != null) {
                if (lmsViewedMediaBean.getSessions() != null && !lmsViewedMediaBean.getSessions().isEmpty()) {
                    sessionMetaDataList = new Gson().fromJson(lmsViewedMediaBean.getSessions(), new TypeToken<List<LmsUserLessonSessionMetaData>>() {
                    }.getType());
                }
                sessionMetaDataList.add(sessionMetaData);
                lmsViewedMediaBean.setSessions(new Gson().toJson(sessionMetaDataList));
                viewedMediaBeanDao.update(lmsViewedMediaBean);
            } else {
                sessionMetaDataList.add(sessionMetaData);
                lmsViewedMediaBean = new LmsViewedMediaBean();
                lmsViewedMediaBean.setLessonId(lessonId);
                lmsViewedMediaBean.setModuleId(moduleId);
                lmsViewedMediaBean.setCourseId(courseId);
                lmsViewedMediaBean.setSessions(new Gson().toJson(sessionMetaDataList));
                viewedMediaBeanDao.create(lmsViewedMediaBean);
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        createStoreEventForLessonSession(courseId, moduleId, lessonId, sessionMetaData);
    }

    @Override
    public void storeUserFeedbackOfMedia(Integer feedBack, Integer lessonId, Integer moduleId, Integer courseId) {
        LmsViewedMediaBean viewedMediaBean = null;
        try {
            viewedMediaBean = viewedMediaBeanDao.queryBuilder().where().eq(FieldNameConstants.LESSON_ID, lessonId).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (viewedMediaBean != null) {
            if (viewedMediaBean.getUserFeedback() == null) {
                viewedMediaBean.setUserFeedback(feedBack);
                try {
                    viewedMediaBeanDao.update(viewedMediaBean);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                createStoreEventForLessonFeedback(courseId, moduleId, lessonId, feedBack);
            }
        } else {
            viewedMediaBean = new LmsViewedMediaBean();
            viewedMediaBean.setLessonId(lessonId);
            viewedMediaBean.setModuleId(moduleId);
            viewedMediaBean.setCourseId(courseId);
            viewedMediaBean.setUserFeedback(feedBack);
            try {
                viewedMediaBeanDao.create(viewedMediaBean);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void createLmsUserMetaDataBean(LmsUserMetaDataBean quizMetadataBean) {
        try {
            TableUtils.clearTable(quizMetadataBeanDao.getConnectionSource(), LmsUserMetaDataBean.class);
            quizMetadataBeanDao.create(quizMetadataBean);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean isQuizMinimumMarks(Integer courseId, Integer moduleId, Integer lessonId, Integer questionTypeId) {
        if (courseId == null) {
            return false;
        }
        LmsCourseDataBean lmsCourseDataBean = null;
        LmsUserMetaDataBean lmsUserMetaDataBean = null;
        try {
            lmsUserMetaDataBean = quizMetadataBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
            lmsCourseDataBean = retrieveCourseByCourseId(courseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (lmsUserMetaDataBean == null) {
            return false;
        }

        LmsUserMetaData lmsUserMetaData = new LmsUserMetaData(lmsUserMetaDataBean);
        List<LmsUserQuizMetaData> quizMetaData = lmsUserMetaData.getQuizMetaData();
        LmsQuizConfigDataBean lmsQuizConfigDataBean = lmsCourseDataBean.getTestConfigJson().get(questionTypeId);
        if (quizMetaData == null || quizMetaData.isEmpty()) {
            return false;
        }

        if (moduleId == null && lessonId == null) {
            for (LmsUserQuizMetaData quiz : quizMetaData) {
                if (quiz.getLessonId() == null && quiz.getModuleId() == null &&
                        quiz.getQuizTypeId().equals(questionTypeId) &&
                        ((lmsQuizConfigDataBean != null && Boolean.TRUE.equals(lmsQuizConfigDataBean.getDoYouWantAQuizToBeMarked())
                                && quiz.getQuizAttemptsToPass() != null) || (quiz.getQuizAttempts() != null))) {
                    return true;
                }
            }
            return false;
        }

        if (lessonId == null) {
            for (LmsUserQuizMetaData quiz : quizMetaData) {
                if (Objects.equals(quiz.getModuleId(), moduleId) && quiz.getLessonId() == null &&
                        quiz.getQuizTypeId().equals(questionTypeId) &&
                        ((lmsQuizConfigDataBean != null && Boolean.TRUE.equals(lmsQuizConfigDataBean.getDoYouWantAQuizToBeMarked())
                                && quiz.getQuizAttemptsToPass() != null) || (quiz.getQuizAttempts() != null))) {
                    return true;
                }
            }
            return false;
        }

        for (LmsUserQuizMetaData quiz : quizMetaData) {
            if (Objects.equals(quiz.getModuleId(), moduleId) && Objects.equals(quiz.getLessonId(), lessonId) &&
                    quiz.getQuizTypeId().equals(questionTypeId) &&
                    ((lmsQuizConfigDataBean != null && Boolean.TRUE.equals(lmsQuizConfigDataBean.getDoYouWantAQuizToBeMarked())
                            && quiz.getQuizAttemptsToPass() != null) || (quiz.getQuizAttempts() != null))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createStoreEvent(String eventType, String eventData) {
        LmsEventBean lmsEventBean = new LmsEventBean();
        lmsEventBean.setChecksum(SewaTransformer.loginBean.getUsername() + Calendar.getInstance().getTimeInMillis());
        lmsEventBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        lmsEventBean.setToken(SewaTransformer.loginBean.getUserToken());
        lmsEventBean.setUserId(SewaTransformer.loginBean.getUserID().intValue());
        lmsEventBean.setMobileDate(new Date());
        lmsEventBean.setEventType(eventType);
        lmsEventBean.setEventData(eventData);
        try {
            eventBeanDao.create(lmsEventBean);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("#### LMS Event Created for Mobile : " + eventType + " : " + eventData);
    }

    private JsonObject getObjectForStoreEventData(Integer courseId, Integer moduleId, Integer lessonId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseId", courseId);
        jsonObject.addProperty("moduleId", moduleId);
        jsonObject.addProperty("lessonId", lessonId);
        return jsonObject;
    }

    @Override
    public void createStoreEventForLessonStartDate(Integer courseId, Integer moduleId, Integer lessonId, Date startDate) {
        JsonObject jsonObject = getObjectForStoreEventData(courseId, moduleId, lessonId);
        jsonObject.addProperty("startDate", startDate.getTime());
        createStoreEvent(LmsEventConstants.LESSON_START_DATE, new Gson().toJson(jsonObject));
    }

    @Override
    public void createStoreEventForLessonEndDate(Integer courseId, Integer moduleId, Integer lessonId, Date endDate) {
        JsonObject jsonObject = getObjectForStoreEventData(courseId, moduleId, lessonId);
        jsonObject.addProperty("endDate", endDate.getTime());
        createStoreEvent(LmsEventConstants.LESSON_END_DATE, new Gson().toJson(jsonObject));
    }

    @Override
    public void createStoreEventForLessonPausedOn(Integer courseId, Integer moduleId, Integer lessonId, Integer pausedOn) {
        JsonObject jsonObject = getObjectForStoreEventData(courseId, moduleId, lessonId);
        jsonObject.addProperty("pausedOn", pausedOn);
        createStoreEvent(LmsEventConstants.LESSON_PAUSED_ON, new Gson().toJson(jsonObject));
    }

    @Override
    public void createStoreEventForLessonCompleted(Integer courseId, Integer moduleId, Integer lessonId, Boolean completed) {
        JsonObject jsonObject = getObjectForStoreEventData(courseId, moduleId, lessonId);
        jsonObject.addProperty("completed", completed);
        createStoreEvent(LmsEventConstants.LESSON_COMPLETED, new Gson().toJson(jsonObject));
    }

    @Override
    public void createStoreEventForLessonSession(Integer courseId, Integer moduleId, Integer lessonId, LmsUserLessonSessionMetaData session) {
        JsonObject jsonObject = getObjectForStoreEventData(courseId, moduleId, lessonId);
        jsonObject.addProperty("session", new Gson().toJson(session));
        createStoreEvent(LmsEventConstants.LESSON_SESSION, new Gson().toJson(jsonObject));
    }

    @Override
    public void createStoreEventForLessonFeedback(Integer courseId, Integer moduleId, Integer lessonId, Integer feedback) {
        JsonObject jsonObject = getObjectForStoreEventData(courseId, moduleId, lessonId);
        jsonObject.addProperty("feedback", feedback);
        createStoreEvent(LmsEventConstants.LESSON_FEEDBACK, new Gson().toJson(jsonObject));
    }
}
