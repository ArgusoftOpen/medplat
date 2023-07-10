package com.argusoft.medplat.course.service.impl;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.course.dao.CourseMasterDao;
import com.argusoft.medplat.course.dao.LmsUserMetaDataDao;
import com.argusoft.medplat.course.dao.TopicMediaMasterDao;
import com.argusoft.medplat.course.dto.LmsMobileEventDto;
import com.argusoft.medplat.course.dto.LmsUserLessonMetaData;
import com.argusoft.medplat.course.dto.LmsUserLessonSessionMetaData;
import com.argusoft.medplat.course.model.CourseMaster;
import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.course.model.TopicMediaMaster;
import com.argusoft.medplat.course.service.LmsUserMetaDataService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LmsUserMetaDataServiceImpl implements LmsUserMetaDataService {

    @Autowired
    private LmsUserMetaDataDao metaDataDao;

    @Autowired
    private CourseMasterDao courseMasterDao;

    @Autowired
    private TopicMediaMasterDao topicMediaMasterDao;

    private final Gson gson = new Gson();
    private static final String COURSE_ID = "courseId";
    private static final String MODULE_ID = "moduleId";
    private static final String LESSON_ID = "lessonId";

    public Integer storeLmsLessonStartDate(UserMaster user, LmsMobileEventDto dto) {
        JSONObject jsonObject = new JSONObject(dto.getEventData());
        LmsUserMetaData userMetaData = metaDataDao.retrieveByUserIdAndCourseId(user.getId(), jsonObject.getInt(COURSE_ID));
        List<LmsUserLessonMetaData> lessonMetaDataList = new ArrayList<>();
        boolean isUpdated = false;
        if (userMetaData != null) {
            if (userMetaData.getLessonMetaData() != null) {
                lessonMetaDataList = gson.fromJson(userMetaData.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
                }.getType());
                for (LmsUserLessonMetaData metaData : lessonMetaDataList) {
                    if (metaData.getLessonId().equals(jsonObject.get(LESSON_ID))) {
                        if (metaData.getStartDate() == null) {
                            metaData.setStartDate(jsonObject.getLong("startDate"));
                        }
                        isUpdated = true;
                        break;
                    }
                }
            }
        } else {
            userMetaData = new LmsUserMetaData();
            userMetaData.setUserId(user.getId());
            userMetaData.setCourseId(Integer.parseInt(jsonObject.get(COURSE_ID).toString()));
        }

        if (!isUpdated) {
            LmsUserLessonMetaData lessonMetaData = new LmsUserLessonMetaData();
            lessonMetaData.setLessonId(Integer.parseInt(jsonObject.get(LESSON_ID).toString()));
            lessonMetaData.setModuleId(Integer.parseInt(jsonObject.get(MODULE_ID).toString()));
            lessonMetaData.setStartDate(jsonObject.getLong("startDate"));
            lessonMetaDataList.add(lessonMetaData);
        }

        userMetaData.setLessonMetaData(gson.toJson(lessonMetaDataList));
        userMetaData.setLastAccessedLessonOn(dto.getMobileDate());
        userMetaData.setCourseCompleted(checkIfCourseIsCompleted(userMetaData.getCourseId(), lessonMetaDataList));
        metaDataDao.createOrUpdate(userMetaData);
        metaDataDao.flush();
        return userMetaData.getId();
    }

    public Integer storeLmsLessonEndDate(UserMaster user, LmsMobileEventDto dto) {
        JSONObject jsonObject = new JSONObject(dto.getEventData());
        LmsUserMetaData userMetaData = metaDataDao.retrieveByUserIdAndCourseId(user.getId(), jsonObject.getInt(COURSE_ID));
        List<LmsUserLessonMetaData> lessonMetaDataList = new ArrayList<>();
        boolean isUpdated = false;
        if (userMetaData != null) {
            if (userMetaData.getLessonMetaData() != null) {
                lessonMetaDataList = gson.fromJson(userMetaData.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
                }.getType());
                for (LmsUserLessonMetaData metaData : lessonMetaDataList) {
                    if (metaData.getLessonId().equals(jsonObject.get(LESSON_ID))) {
                        if (metaData.getEndDate() == null) {
                            metaData.setEndDate(jsonObject.getLong("endDate"));
                        }
                        isUpdated = true;
                        break;
                    }
                }
            }
        } else {
            userMetaData = new LmsUserMetaData();
            userMetaData.setUserId(user.getId());
            userMetaData.setCourseId(Integer.parseInt(jsonObject.get(COURSE_ID).toString()));
        }

        if (!isUpdated) {
            LmsUserLessonMetaData lessonMetaData = new LmsUserLessonMetaData();
            lessonMetaData.setLessonId(Integer.parseInt(jsonObject.get(LESSON_ID).toString()));
            lessonMetaData.setModuleId(Integer.parseInt(jsonObject.get(MODULE_ID).toString()));
            lessonMetaData.setEndDate(jsonObject.getLong("endDate"));
            lessonMetaDataList.add(lessonMetaData);
        }

        userMetaData.setLessonMetaData(gson.toJson(lessonMetaDataList));
        userMetaData.setLastAccessedLessonOn(dto.getMobileDate());
        userMetaData.setCourseCompleted(checkIfCourseIsCompleted(userMetaData.getCourseId(), lessonMetaDataList));
        metaDataDao.createOrUpdate(userMetaData);
        metaDataDao.flush();
        return userMetaData.getId();
    }

    public Integer storeLmsLessonSession(UserMaster user, LmsMobileEventDto dto) {
        JSONObject jsonObject = new JSONObject(dto.getEventData());
        LmsUserLessonSessionMetaData sessionMetaData = gson.fromJson(jsonObject.getString("session"), LmsUserLessonSessionMetaData.class);
        LmsUserMetaData userMetaData = metaDataDao.retrieveByUserIdAndCourseId(user.getId(), jsonObject.getInt(COURSE_ID));
        List<LmsUserLessonMetaData> lessonMetaDataList = new ArrayList<>();
        boolean isUpdated = false;
        if (userMetaData != null) {
            if (userMetaData.getLessonMetaData() != null) {
                lessonMetaDataList = gson.fromJson(userMetaData.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
                }.getType());
                for (LmsUserLessonMetaData metaData : lessonMetaDataList) {
                    if (metaData.getLessonId().equals(jsonObject.get(LESSON_ID))) {
                        if (metaData.getSessions() != null && !metaData.getSessions().isEmpty()) {
                            metaData.getSessions().add(sessionMetaData);
                        } else {
                            List<LmsUserLessonSessionMetaData> sessionMetaDataList = new ArrayList<>();
                            sessionMetaDataList.add(sessionMetaData);
                            metaData.setSessions(sessionMetaDataList);
                        }
                        isUpdated = true;
                        break;
                    }
                }
            }
        } else {
            userMetaData = new LmsUserMetaData();
            userMetaData.setUserId(user.getId());
            userMetaData.setCourseId(Integer.parseInt(jsonObject.get(COURSE_ID).toString()));
        }

        if (!isUpdated) {
            LmsUserLessonMetaData lessonMetaData = new LmsUserLessonMetaData();
            lessonMetaData.setLessonId(Integer.parseInt(jsonObject.get(LESSON_ID).toString()));
            lessonMetaData.setModuleId(Integer.parseInt(jsonObject.get(MODULE_ID).toString()));
            List<LmsUserLessonSessionMetaData> sessionMetaDataList = new ArrayList<>();
            sessionMetaDataList.add(sessionMetaData);
            lessonMetaData.setSessions(sessionMetaDataList);
            lessonMetaDataList.add(lessonMetaData);
        }

        userMetaData.setLessonMetaData(gson.toJson(lessonMetaDataList));
        userMetaData.setLastAccessedLessonOn(dto.getMobileDate());
        userMetaData.setCourseCompleted(checkIfCourseIsCompleted(userMetaData.getCourseId(), lessonMetaDataList));
        metaDataDao.createOrUpdate(userMetaData);
        metaDataDao.flush();
        return userMetaData.getId();
    }

    public Integer storeLmsLessonFeedback(UserMaster user, LmsMobileEventDto dto) {
        JSONObject jsonObject = new JSONObject(dto.getEventData());
        LmsUserMetaData userMetaData = metaDataDao.retrieveByUserIdAndCourseId(user.getId(), jsonObject.getInt(COURSE_ID));
        List<LmsUserLessonMetaData> lessonMetaDataList = new ArrayList<>();
        boolean isUpdated = false;
        if (userMetaData != null) {
            if (userMetaData.getLessonMetaData() != null) {
                lessonMetaDataList = gson.fromJson(userMetaData.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
                }.getType());
                for (LmsUserLessonMetaData metaData : lessonMetaDataList) {
                    if (metaData.getLessonId().equals(jsonObject.get(LESSON_ID))) {
                        if (metaData.getUserFeedback() == null) {
                            metaData.setUserFeedback(jsonObject.getInt("feedback"));
                        }
                        isUpdated = true;
                        break;
                    }
                }
            }
        } else {
            userMetaData = new LmsUserMetaData();
            userMetaData.setUserId(user.getId());
            userMetaData.setCourseId(Integer.parseInt(jsonObject.get(COURSE_ID).toString()));
        }

        if (!isUpdated) {
            LmsUserLessonMetaData lessonMetaData = new LmsUserLessonMetaData();
            lessonMetaData.setLessonId(Integer.parseInt(jsonObject.get(LESSON_ID).toString()));
            lessonMetaData.setModuleId(Integer.parseInt(jsonObject.get(MODULE_ID).toString()));
            lessonMetaData.setUserFeedback(Integer.parseInt(jsonObject.get("feedback").toString()));
            lessonMetaDataList.add(lessonMetaData);
        }

        userMetaData.setLessonMetaData(gson.toJson(lessonMetaDataList));
        userMetaData.setLastAccessedLessonOn(dto.getMobileDate());
        userMetaData.setCourseCompleted(checkIfCourseIsCompleted(userMetaData.getCourseId(), lessonMetaDataList));
        metaDataDao.createOrUpdate(userMetaData);
        metaDataDao.flush();
        return userMetaData.getId();
    }

    public Integer storeLmsLessonCompleted(UserMaster user, LmsMobileEventDto dto) {
        JSONObject jsonObject = new JSONObject(dto.getEventData());
        LmsUserMetaData userMetaData = metaDataDao.retrieveByUserIdAndCourseId(user.getId(), jsonObject.getInt(COURSE_ID));
        List<LmsUserLessonMetaData> lessonMetaDataList = new ArrayList<>();
        boolean isUpdated = false;
        if (userMetaData != null) {
            if (userMetaData.getLessonMetaData() != null) {
                lessonMetaDataList = gson.fromJson(userMetaData.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
                }.getType());
                for (LmsUserLessonMetaData metaData : lessonMetaDataList) {
                    if (metaData.getLessonId().equals(jsonObject.get(LESSON_ID))) {
                        if (metaData.getCompleted() == null) {
                            metaData.setCompleted(jsonObject.getBoolean("completed"));
                        }
                        isUpdated = true;
                        break;
                    }
                }
            }
        } else {
            userMetaData = new LmsUserMetaData();
            userMetaData.setUserId(user.getId());
            userMetaData.setCourseId(Integer.parseInt(jsonObject.get(COURSE_ID).toString()));
        }

        if (!isUpdated) {
            LmsUserLessonMetaData lessonMetaData = new LmsUserLessonMetaData();
            lessonMetaData.setLessonId(Integer.parseInt(jsonObject.get(LESSON_ID).toString()));
            lessonMetaData.setModuleId(Integer.parseInt(jsonObject.get(MODULE_ID).toString()));
            lessonMetaData.setCompleted(jsonObject.getBoolean("completed"));
            lessonMetaDataList.add(lessonMetaData);
        }

        userMetaData.setLessonMetaData(gson.toJson(lessonMetaDataList));
        userMetaData.setLastAccessedLessonOn(dto.getMobileDate());
        userMetaData.setCourseCompleted(checkIfCourseIsCompleted(userMetaData.getCourseId(), lessonMetaDataList));
        metaDataDao.createOrUpdate(userMetaData);
        metaDataDao.flush();
        return userMetaData.getId();
    }

    public Integer storeLmsLessonPausedOn(UserMaster user, LmsMobileEventDto dto) {
        JSONObject jsonObject = new JSONObject(dto.getEventData());
        LmsUserMetaData userMetaData = metaDataDao.retrieveByUserIdAndCourseId(user.getId(), jsonObject.getInt(COURSE_ID));
        List<LmsUserLessonMetaData> lessonMetaDataList = new ArrayList<>();
        boolean isUpdated = false;
        if (userMetaData != null) {
            if (userMetaData.getLessonMetaData() != null) {
                lessonMetaDataList = gson.fromJson(userMetaData.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
                }.getType());
                for (LmsUserLessonMetaData metaData : lessonMetaDataList) {
                    if (metaData.getLessonId().equals(jsonObject.get(LESSON_ID))) {
                        if (jsonObject.has("pausedOn")) {
                            metaData.setLastPausedOn(jsonObject.getInt("pausedOn"));
                        } else {
                            metaData.setLastPausedOn(null);
                        }
                        isUpdated = true;
                        break;
                    }
                }
            }
        } else {
            userMetaData = new LmsUserMetaData();
            userMetaData.setUserId(user.getId());
            userMetaData.setCourseId(Integer.parseInt(jsonObject.get(COURSE_ID).toString()));
        }

        if (!isUpdated) {
            LmsUserLessonMetaData lessonMetaData = new LmsUserLessonMetaData();
            lessonMetaData.setLessonId(Integer.parseInt(jsonObject.get(LESSON_ID).toString()));
            lessonMetaData.setModuleId(Integer.parseInt(jsonObject.get(MODULE_ID).toString()));
            lessonMetaData.setLastPausedOn(jsonObject.getInt("pausedOn"));
            lessonMetaDataList.add(lessonMetaData);
        }

        userMetaData.setLessonMetaData(gson.toJson(lessonMetaDataList));
        userMetaData.setLastAccessedLessonOn(dto.getMobileDate());
        userMetaData.setCourseCompleted(checkIfCourseIsCompleted(userMetaData.getCourseId(), lessonMetaDataList));
        metaDataDao.createOrUpdate(userMetaData);
        metaDataDao.flush();
        return userMetaData.getId();
    }

    private boolean checkIfCourseIsCompleted(Integer courseId, List<LmsUserLessonMetaData> lessonMetaDataList) {
        List<Integer> completedLessonIds = new ArrayList<>();
        for (LmsUserLessonMetaData lessonMetaData : lessonMetaDataList) {
            if (Boolean.TRUE.equals(lessonMetaData.getCompleted())) {
                completedLessonIds.add(lessonMetaData.getLessonId());
            } else {
                return false;
            }
        }

        CourseMaster courseMaster = courseMasterDao.retrieveById(courseId);
        Set<Integer> topicIds = courseMaster.getTopicIds();
        if (topicIds != null && !topicIds.isEmpty()) {
            for (Integer topicId : topicIds) {
                List<TopicMediaMaster> topicMedias = topicMediaMasterDao.getTopicMediaByTopicId(topicId);
                if (topicMedias != null && !topicMedias.isEmpty()) {
                    for (TopicMediaMaster mediaMaster : topicMedias) {
                        if (!completedLessonIds.contains(mediaMaster.getId())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
