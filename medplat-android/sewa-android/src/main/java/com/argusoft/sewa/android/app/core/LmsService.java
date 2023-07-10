package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.BookmarkDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserLessonSessionMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LmsViewedMediaDataBean;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LmsService {

    List<LmsCourseDataBean> retrieveCourses();

    void makeCourseArchive(Integer courseId, boolean isArchive);

    void makeCourseDownloadable(Integer courseId, boolean isMediaDownloaded);

    LmsCourseDataBean retrieveCourseByCourseId(Integer courseId);

    void deleteBookmark(LmsBookMarkBean bookMarkBean);

    void updateBookmark(LmsBookMarkBean bookMarkBean);

    List<LmsBookMarkBean> retrieveBookmarks(Integer lessonId);

    void saveBookmark(Integer lessonId, BookmarkDataBean bookmarkDataBean);

    Map<Integer, Boolean> getAllowedMediaMap(Integer courseId);

    Boolean isQuizMinimumMarks(Integer courseId, Integer moduleId, Integer lessonId);

    List<Integer> getViewedLessonList();

    void storeLmsTestResult(LmsQuestionSetAnswerDataBean lmsQuestionSetAnswerDataBean, String testFor);

    Integer getLastPausedOn(Integer lessonId);

    LmsUserMetaData getLmsUserMetaDataByCourseId(Integer courseId);

    LmsViewedMediaBean getViewedLessonById(Integer lessonId);

    boolean isAnyUpdatedDataAvailable();

    LmsViewedMediaDataBean getViewedCourseById(Integer courseId);

    void markCourseViewed(Integer courseId);

    void markCourseDataViewed(Integer courseId, List<LmsTopicDataBean> topics, List<LmsQuestionSetDataBean> questionSet);

    void markLessonCompleted(Integer lessonId, Integer moduleId, Integer courseId);

    void updateLastPausedOnForMedia(Integer lastPausedOn, Integer lessonId, Integer moduleId, Integer courseId);

    void updateStartDateForMedia(Date startDate, Integer lessonId, Integer moduleId, Integer courseId);

    void updateEndDateForMedia(Date endDate, Integer lessonId, Integer moduleId, Integer courseId);

    void addSessionsForMedia(Date startDate, Date endDate, Integer lessonId, Integer moduleId, Integer courseId);

    void storeUserFeedbackOfMedia(Integer feedBack, Integer lessonId, Integer moduleId, Integer courseId);

    void createLmsUserMetaDataBean(LmsUserMetaDataBean quizMetadataBean);

    boolean isQuizMinimumMarks(Integer courseId, Integer moduleId, Integer lessonId, Integer questionTypeId);

    void createStoreEvent(String eventType, String eventData);

    void createStoreEventForLessonStartDate(Integer courseId, Integer moduleId, Integer lessonId, Date startDate);

    void createStoreEventForLessonEndDate(Integer courseId, Integer moduleId, Integer lessonId, Date endDate);

    void createStoreEventForLessonPausedOn(Integer courseId, Integer moduleId, Integer lessonId, Integer pausedOn);

    void createStoreEventForLessonCompleted(Integer courseId, Integer moduleId, Integer lessonId, Boolean completed);

    void createStoreEventForLessonSession(Integer courseId, Integer moduleId, Integer lessonId, LmsUserLessonSessionMetaData session);

    void createStoreEventForLessonFeedback(Integer courseId, Integer moduleId, Integer lessonId, Integer feedback);
}
