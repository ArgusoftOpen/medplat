/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.service;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.dto.TopicMasterDto;
import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.mobile.dto.CourseDataBean;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author akshar
 */
public interface CourseMasterService {

    /**
     * This method is used to create course
     *
     * @param courseMasterDto
     */
    public void createCourse(CourseMasterDto courseMasterDto);

    /**
     * This method is used to get all courses
     *
     * @return
     *
     */
    public List<CourseMasterDto> getAllCourses(Boolean isActive);

    /**
     * This method is used to create topic
     *
     * @param topicMasterDto
     * @return
     */
    public Integer createTopic(TopicMasterDto topicMasterDto);

    /**
     * This method is used to get topic of given topicId
     *
     * @param topicId
     * @return
     */
    public TopicMasterDto getTopicById(Integer topicId);
    
    /**
     * This method is used to get course of given courseId
     *
     * @param courseIds
     * @return
     */
    public List<CourseMasterDto> getCoursesByIds(Set<Integer> courseIds);

    /**
     * This method is used to get list of topics of given courseId
     *
     * @param courseId
     * @return
     */
    public List<TopicMasterDto> getTopicsByCourse(Integer courseId);

    /**
     *
     * @param trainingStartDate
     * @param courseIds
     * @return
     */
    public Date getTrainingCompletionDate(Long trainingStartDate, List<Integer> courseIds);

    /**
     * 
     * @param courseId
     * @return 
     */
    public CourseMasterDto getCourseById(Integer courseId);
    
    /**
     * 
     * @param topicIds
     * @return 
     */
    public List<TopicMasterDto> getTopicByIds(List<Integer> topicIds);
    
    /**
     * 
     * @param roleIds
     * @return 
     */
    public List<CourseMasterDto> getCoursesbyRoleIds(List<Integer> roleIds);
    
    /**
     * Used to change state of course
     * @param courseId
     * @param isActive 
     */
    public void toggleActive(Integer courseId,Boolean isActive);
    
     /**
     * Used to update course
     * @param courseMasterDto
     */
    public void updateCourse(CourseMasterDto courseMasterDto);

    public List<RoleMasterDto> getRolesByCourse(Integer courseId);

    public List<RoleMasterDto> getAllRoles();

    public List<RoleMasterDto> getTrainerRolesByCourse(Integer courseId);

    public List<CourseMasterDto> getCoursesByModuleIds(List<Integer> moduleIds);

    /**
     * Returns list of course dataBeans by roleId
     * @param userId An instance of userId
     * @return A list of Course dataBeans
     */
    List<CourseDataBean> getCourseDataBeanByUserId(Integer userId);

    /**
     * Returns metaData for user
     * @param userId An instance of userId
     * @return quiz metaData for user
     */
    List<LmsUserMetaData> getLmsUserMetaData(Integer userId);

    String updateMediaSize(Integer courseId);
}
