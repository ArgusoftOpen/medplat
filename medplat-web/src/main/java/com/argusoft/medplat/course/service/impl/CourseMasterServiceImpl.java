/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.service.impl;

import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.common.dto.FieldValueMasterDto;
import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.service.FieldMasterService;
import com.argusoft.medplat.common.service.FieldValueService;
import com.argusoft.medplat.web.users.service.RoleService;
import com.argusoft.medplat.course.dao.*;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.dto.TopicMasterDto;
import com.argusoft.medplat.course.dto.TopicMediaMasterDto;
import com.argusoft.medplat.course.mapper.CourseMasterMapper;
import com.argusoft.medplat.course.mapper.QuestionSetMapper;
import com.argusoft.medplat.course.mapper.TopicMasterMapper;
import com.argusoft.medplat.course.mapper.TopicMediaMasterMapper;
import com.argusoft.medplat.course.model.*;
import com.argusoft.medplat.course.service.CourseMasterService;
import com.argusoft.medplat.document.service.DocumentService;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.training.util.TrainingUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

/**
 * @author akshar
 */
@Service("courseMasterService")
@Transactional
public class CourseMasterServiceImpl implements CourseMasterService {

    @Autowired
    private CourseMasterDao courseMasterDao;

    @Autowired
    private TopicMasterDao topicMasterDao;

    @Autowired
    private TopicMediaMasterDao topicMediaMasterDao;

    @Autowired
    private FieldValueService fieldValueService;

    @Autowired
    private FieldMasterService fieldMasterService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private QuestionSetConfigurationDao questionSetConfigurationDao;

    @Autowired
    private QuestionBankConfigurationDao questionBankConfigurationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LmsUserMetaDataDao lmsUserMetaDataDao;

    @Autowired
    DocumentService documentService;

    private final Random random = new Random();

    @Override
    public void createCourse(CourseMasterDto courseMasterDto) {
        Set<TopicMasterDto> topicMasterDtos = courseMasterDto.getTopicMasterDtos();
        if (courseMasterDto.getCourseState() == null)
            courseMasterDto.setCourseState(CourseMaster.State.ACTIVE);
//
        Set<Integer> topicIds = new HashSet<>();
        for (TopicMasterDto topicMasterDto : topicMasterDtos) {
            topicIds.add(this.createTopic(topicMasterDto));
        }
        courseMasterDao.create(CourseMasterMapper.dtoToEntityCourseMaster(courseMasterDto, null, topicIds));
    }

    @Override
    public List<CourseMasterDto> getAllCourses(Boolean isActive) {

        List<CourseMaster> courseMasters = courseMasterDao.retrieveAll();
        if (isActive != null && isActive) {
            courseMasters = courseMasters.stream()
                    .filter(c -> c.getCourseState().equals(CourseMaster.State.ACTIVE)).collect(Collectors.toList());
        }

        List<CourseMasterDto> courseMasterDtos = new LinkedList<>();

        for (CourseMaster courseMaster : courseMasters) {
            CourseMasterDto course = new CourseMasterDto();
            course.setCourseDescription(courseMaster.getCourseDescription());
            course.setCourseName(courseMaster.getCourseName());
            course.setCourseId(courseMaster.getCourseId());
            course.setCourseType(courseMaster.getCourseType());
            course.setCourseState(courseMaster.getCourseState());
            course.setTargetRole(this.getRolesByRoleIds(courseMaster.getRoleIds()));
            course.setTrainerRole(courseMaster.getTrainerRoleIds().size() != 0 ? this.getRolesByRoleIds(courseMaster.getTrainerRoleIds()) : null);
            course.setCourseModuleId(courseMaster.getCourseModuleId());
            course.setModuleName(courseMaster.getCourseModuleId() != null ? fieldValueService.getFieldNameById(courseMaster.getCourseModuleId()) : null);
            course.setDuration(this.calculateDuration(courseMaster.getCourseId()));
            course.setTestConfigJson(courseMaster.getTestConfigJson());
            UserMaster userMaster = userDao.retrieveById(courseMaster.getCreatedBy());
            if(userMaster != null) {
                course.setCreatedByUserName(userMaster.getFirstName().concat(" ").concat(userMaster.getLastName()));
            }
            courseMasterDtos.add(course);
        }
        return courseMasterDtos;
    }

    private void createOrUpdateMediaVideo(List<TopicMediaMasterDto> topicMediaMasterDtos, Integer topicId) {
        List<TopicMediaMaster> topicMediaMasters = topicMediaMasterDao.getTopicMediaByTopicId(topicId);
        topicMediaMasters.forEach(topicMediaMaster -> {
            topicMediaMaster.setMediaState(TopicMediaMaster.State.INACTIVE);
            topicMediaMasterDao.merge(topicMediaMaster);
        });
        topicMediaMasterDtos.forEach(topicMediaMasterDto -> {
            topicMediaMasterDto.setTopicId(topicId);
            topicMediaMasterDto.setMediaState(TopicMediaMaster.State.ACTIVE);
            TopicMediaMaster topicMediaMaster = TopicMediaMasterMapper.dtoToEntityTopicMaster(topicMediaMasterDto);
            if (topicMediaMaster.getId() != null) {
                TopicMediaMaster exitingTopicMediaMaster = topicMediaMasterDao.retrieveById(topicMediaMaster.getId());
                topicMediaMaster.setCreatedBy(exitingTopicMediaMaster.getCreatedBy());
                topicMediaMaster.setCreatedOn(exitingTopicMediaMaster.getCreatedOn());
                topicMediaMasterDao.merge(topicMediaMaster);
            } else {
                topicMediaMasterDao.create(topicMediaMaster);
            }
        });
    }

    @Override
    public Integer createTopic(TopicMasterDto topicMasterDto) {
        topicMasterDto.setTopicState(TopicMaster.State.ACTIVE);
        TopicMaster topicMaster = TopicMasterMapper.dtoToEntityTopicMaster(topicMasterDto);
        if (topicMaster.getTopicId() == null) {
            topicMasterDao.create(topicMaster);
        }
        List<TopicMediaMasterDto> topicMediaMasterDtos = topicMasterDto.getTopicMediaList();
        if (Objects.nonNull(topicMediaMasterDtos)) {
            createOrUpdateMediaVideo(topicMediaMasterDtos, topicMaster.getTopicId());
        }
        return topicMaster.getTopicId();
    }

    @Override
    public TopicMasterDto getTopicById(Integer topicId) {
        TopicMasterDto topicMasterDto = TopicMasterMapper.entityToDtoTopicMaster(topicMasterDao.retrieveById(topicId));
        List<TopicMediaMaster> topicMediaMasters = topicMediaMasterDao.getTopicMediaByTopicId(topicId);
        topicMasterDto.setTopicMediaList(TopicMediaMasterMapper.entityToDtoTopicMasterList(topicMediaMasters));
        return topicMasterDto;
    }

    @Override
    public List<TopicMasterDto> getTopicsByCourse(Integer courseId) {

        CourseMaster courseMaster = courseMasterDao.retrieveById(courseId);
        Set<Integer> topicIds = courseMaster.getTopicIds();
        List<TopicMasterDto> topicMasterDtos = new LinkedList<>();

        for (Integer topicId : topicIds) {
            topicMasterDtos.add(TopicMasterMapper.entityToDtoTopicMaster(topicMasterDao.retrieveById(topicId)));
        }
        return topicMasterDtos;
    }


    public CourseMasterDto getCourseById(Integer courseId) {
        CourseMaster courseMaster = courseMasterDao.retrieveById(courseId);
        Set<TopicMasterDto> topicMasterDtos = new HashSet<>();

        for (Integer topicId : courseMaster.getTopicIds()) {
            topicMasterDtos.add(this.getTopicById(topicId));
        }
        List<String> keys = new ArrayList<>();
        keys.add("COURSE_MODULE_NAME");
        List<Integer> fieldIds = fieldMasterService.getIdsByNameForFieldConstants(keys);
        Map<String, List<FieldValueMasterDto>> dropDown = fieldValueService.getFieldValuesByList(fieldIds);
        CourseMasterDto result = CourseMasterMapper.entityToDtoCourseMaster(courseMasterDao.retrieveById(courseId), topicMasterDtos);
        result.setDropDown(dropDown);
        return result;
    }


    @Override
    public Date getTrainingCompletionDate(Long trainingStartDateInLong, List<Integer> courseIds) throws ImtechoUserException {
        Date trainingStartDate = new Date(trainingStartDateInLong);

        try {
            List<TopicMasterDto> allTopics = new ArrayList<>();
            for (Integer courseId : courseIds) {
                List<TopicMasterDto> topics = this.getTopicsByCourse(courseId);
                allTopics.addAll(topics);
            }

            return TrainingUtil.calculateNewDateExcludingSunday(trainingStartDate, allTopics.stream().max(Comparator.comparingInt(TopicMasterDto::getTopicDay)).get().getTopicDay());
        } catch (Exception ex) {
            throw new ImtechoSystemException("error in getTrainingCompletionDate", ex);
        }
    }

    @Override
    public List<CourseMasterDto> getCoursesByIds(Set<Integer> courseIds) {
        List<Integer> courseIdList = new LinkedList<>(courseIds);

        List<CourseMaster> courseMasters = courseMasterDao.retriveByIds("courseId", courseIdList);

        List<CourseMasterDto> courseMasterDtos = new LinkedList<>();

        for (CourseMaster courseMaster : courseMasters) {
            Set<TopicMasterDto> topicMasterDtos = new HashSet<>();
            Integer courseId = courseMaster.getCourseId();
            for (Integer topicId : courseMaster.getTopicIds()) {
                topicMasterDtos.add(this.getTopicById(topicId));
            }
            courseMasterDtos.add(CourseMasterMapper.entityToDtoCourseMaster(courseMasterDao.retrieveById(courseId), topicMasterDtos));
        }

        return courseMasterDtos;
    }

    @Override
    public List<TopicMasterDto> getTopicByIds(List<Integer> topicIds) {
        return TopicMasterMapper.entityToDtoTopicMasterList(topicMasterDao.getTopicByIds(topicIds));
    }

    @Override
    public List<CourseMasterDto> getCoursesbyRoleIds(List<Integer> roleIds) {
        List<CourseMasterDto> list = new ArrayList<>();
        for (CourseMaster course : courseMasterDao.getCoursesbyRoleIds(roleIds)) {
            list.add(CourseMasterMapper.entityToDtoCourseMaster(course, null));
        }
        return list;
    }

    @Override
    public void toggleActive(Integer courseId, Boolean isActive) {
        courseMasterDao.toggleActive(courseId, isActive);
    }

    @Override
    public void updateCourse(CourseMasterDto courseMasterDto) {
        CourseMaster courseMaster = courseMasterDao.retrieveById(courseMasterDto.getCourseId());
        Set<Integer> topicIds = new HashSet<>();
        for (TopicMasterDto topicMasterDto : courseMasterDto.getTopicMasterDtos()) {
            if (topicMasterDto.getTopicId() != null) {
                topicIds.add(topicMasterDto.getTopicId());
                TopicMaster topicMaster = topicMasterDao.retrieveById(topicMasterDto.getTopicId());
                topicMaster.setTopicName(topicMasterDto.getTopicName());
                topicMaster.setTopicDescription(topicMasterDto.getTopicDescription());
                topicMaster.setDay(topicMasterDto.getTopicDay());
                topicMaster.setTopicOrder(topicMasterDto.getTopicOrder());
                topicMasterDao.update(topicMaster);
                createOrUpdateMediaVideo(topicMasterDto.getTopicMediaList(), topicMasterDto.getTopicId());
            } else {
                topicIds.add(createTopic(topicMasterDto));
                courseMaster.setTopicIds(topicIds);
            }
        }

        courseMasterDao.update(CourseMasterMapper.dtoToEntityCourseMaster(courseMasterDto, courseMaster, topicIds));
    }

    @Override
    public List<RoleMasterDto> getRolesByCourse(Integer courseId) {
        return courseMasterDao.getRolesByCourse(courseId);
    }

    @Override
    public List<RoleMasterDto> getAllRoles(){
        return courseMasterDao.getAllRoles();
    }

    @Override
    public List<RoleMasterDto> getTrainerRolesByCourse(Integer courseId) {
        return courseMasterDao.getTrainerRolesByCourse(courseId);
    }

    @Override
    public List<CourseMasterDto> getCoursesByModuleIds(List<Integer> moduleIds) {
        return courseMasterDao.getCoursesByModuleIds(moduleIds);
    }

    private String getRolesByRoleIds(Set<Integer> roleIds) {
        return roleService.getRolesByIds(roleIds)
                .stream().map(r -> r.getName()).collect(Collectors.joining(","));
    }

    private int calculateDuration(Integer courseId) {
        return this.getTopicsByCourse(courseId).stream().max(Comparator.comparingInt(TopicMasterDto::getTopicDay)).get().getTopicDay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDataBean> getCourseDataBeanByUserId(Integer userId) {
        List<CourseDataBean> courseDataBeanList = courseMasterDao.getCourseDataBeansByUserId(userId);

        for (CourseDataBean course : courseDataBeanList) {
            //Adding test Json
            if (course.getTestConfigJsonString() != null && !course.getTestConfigJsonString().isEmpty()) {
                course.setTestConfigJson(new Gson().fromJson(course.getTestConfigJsonString(), new TypeToken<Map<Integer, LmsQuizConfigDataBean>>() {
                }.getType()));
            }
            //Adding course image Json
            if (course.getCourseImageJsonString() != null && !course.getCourseImageJsonString().isEmpty()) {
                course.setCourseImage(new Gson().fromJson(course.getCourseImageJsonString(), CourseImageDataBean.class));
            }
            //Adding modules
            List<TopicDataBean> topicDataBeanList = topicMasterDao.getTopicDataBeanByCourseId(course.getCourseId());
            for (TopicDataBean topic : topicDataBeanList) {
                //Adding lessons
                List<TopicMediaMaster> topicMediaMasters = topicMediaMasterDao.getTopicMediaByTopicId(topic.getTopicId());
                List<TopicMediaDataBean> mediaDataBeans = new ArrayList<>();
                for (TopicMediaMaster mediaMaster : topicMediaMasters) {
                    mediaDataBeans.add(TopicMediaMasterMapper.entityToDataBean(mediaMaster,
                            getQuestionSetBeansByReferenceIdAndType(mediaMaster.getId(), "LESSON",
                                    course.getTestConfigJson() != null ? course.getTestConfigJson().keySet() : new HashSet<>())));
                }
                topic.setTopicMedias(mediaDataBeans);
                topic.setQuestionSet(getQuestionSetBeansByReferenceIdAndType(topic.getTopicId(), "MODULE",
                        course.getTestConfigJson() != null ? course.getTestConfigJson().keySet() : new HashSet<>()));
            }
            course.setTopics(topicDataBeanList);
            course.setQuestionSet(getQuestionSetBeansByReferenceIdAndType(course.getCourseId(), "COURSE",
                    course.getTestConfigJson() != null ? course.getTestConfigJson().keySet() : new HashSet<>()));
        }

        return courseDataBeanList;
    }

    public List<QuestionSetBean> getQuestionSetBeansByReferenceIdAndType(Integer referenceId, String referenceType, Set<Integer> quizTypes) {
        List<QuestionSetBean> questionSetBeans = new ArrayList<>();
        for (Integer quizType : quizTypes) {
            List<QuestionSetConfiguration> questionSets = questionSetConfigurationDao.getQuestionSetByReferenceIdAndType(referenceId, referenceType, quizType);
            if (questionSets != null && !questionSets.isEmpty()) {
                for (QuestionSetConfiguration questionSetConfiguration : questionSets) {
                    List<QuestionBankConfiguration> questionBanks = questionBankConfigurationDao.getQuestionBanksByQuestionSetId(questionSetConfiguration.getId());
                    questionSetBeans.add(QuestionSetMapper.questionSetEntityToDataBean(questionSetConfiguration, questionBanks));
                }
            }
        }
        return questionSetBeans;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LmsUserMetaData> getLmsUserMetaData(Integer userId) {
        return lmsUserMetaDataDao.retrieveByUserId(userId);
    }

    @Override
    public String updateMediaSize(Integer courseId) {
        CourseMasterDto courseMasterDto = getCourseById(courseId);
        if(courseMasterDto.getCourseId() != null) {
            for (TopicMasterDto topicMasterDto : courseMasterDto.getTopicMasterDtos()) {
                List<TopicMediaMasterDto> topicMediaMasterDtos = topicMasterDto.getTopicMediaList();
                topicMediaMasterDtos.forEach(topicMediaMasterDto -> {
                    try {
                        File file = documentService.getFile(topicMediaMasterDto.getMediaId());
                        if(file.exists()) {
                            if(topicMediaMasterDto.getMediaType().toString().equals("VIDEO") && topicMediaMasterDto.getTranscriptFileId() != null) {
                                File transcriptFile = documentService.getFile(topicMediaMasterDto.getTranscriptFileId());
                                topicMediaMasterDto.setSize(file.length() + transcriptFile.length());
                            } else {
                                topicMediaMasterDto.setSize(file.length());
                            }
                            TopicMediaMaster topicMediaMaster = TopicMediaMasterMapper.dtoToEntityTopicMaster(topicMediaMasterDto);
                            if(topicMediaMaster.getId() != null) {
                                TopicMediaMaster exitingTopicMediaMaster = topicMediaMasterDao.retrieveById(topicMediaMaster.getId());
                                topicMediaMaster.setCreatedBy(exitingTopicMediaMaster.getCreatedBy());
                                topicMediaMaster.setCreatedOn(exitingTopicMediaMaster.getCreatedOn());
                                topicMediaMasterDao.merge(topicMediaMaster);
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                });
            }
        }
        return "PROCESS COMPLETED";
    }

}
