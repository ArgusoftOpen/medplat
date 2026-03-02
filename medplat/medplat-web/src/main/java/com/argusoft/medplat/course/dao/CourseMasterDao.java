/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.dao;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.model.CourseMaster;
import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.CourseDataBean;

import java.util.List;

/**
 * @author akshar
 */
public interface CourseMasterDao extends GenericDao<CourseMaster, Integer> {

    public List<CourseMaster> getCoursesbyRoleIds(List<Integer> roleIds);

    public void toggleActive(Integer courseId, Boolean isActive);

    public List<RoleMasterDto> getRolesByCourse(Integer courseId);

    public List<RoleMasterDto> getAllRoles();

    public List<RoleMasterDto> getTrainerRolesByCourse(Integer courseId);

    public List<CourseMasterDto> getCoursesByModuleIds(List<Integer> moduleIds);

    /**
     * Returns list of Course DataBeans by roleId
     *
     * @param userId A id of user
     * @return A list of course dataBeans
     */
    public List<CourseDataBean> getCourseDataBeansByUserId(Integer userId);

}
