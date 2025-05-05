/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.service.CourseMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 *
 * @author akshar
 */
@RestController
@RequestMapping("/api/course")
@Tag(name = "Course Master Controller", description = "")
public class CourseMasterController {
    
    @Autowired
    CourseMasterService courseMasterService;
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createCourse(@RequestBody CourseMasterDto courseMasterDto) {
        courseMasterService.createCourse(courseMasterDto);
    }
            
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CourseMasterDto> retrieveAllCourse(@RequestParam(value = "isActive",required = false) Boolean isActive) {
        return courseMasterService.getAllCourses(isActive);
    }

    @RequestMapping(value = "/allRoles", method = RequestMethod.GET)
    public List<RoleMasterDto> getAllRoles(){
        return courseMasterService.getAllRoles();
    }

    @RequestMapping(value = "/completiondate", method = RequestMethod.GET)
    public Date getTrainingCompletionDate(@RequestParam("trainingStartDate") Long trainingStartDate,
            @RequestParam("courseId") List<Integer> courseIds) {
        return courseMasterService.getTrainingCompletionDate(trainingStartDate,courseIds);
    }
    
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public List<CourseMasterDto> getCoursesByRoles(@RequestParam("roles") List<Integer> rolesIds) {
        return courseMasterService.getCoursesbyRoleIds(rolesIds);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CourseMasterDto getCoursesById(@PathVariable("id") Integer id) {
        return courseMasterService.getCourseById(id);
    }
    
    @RequestMapping(value = "/toggleactive", method = RequestMethod.GET)
    public void toggleActive(@RequestParam("courseId") Integer courseId,
            @RequestParam("isActive") Boolean isActive) {
        courseMasterService.toggleActive(courseId, isActive);
    }
    
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void updateCourse(@RequestBody CourseMasterDto courseMasterDto) {
        courseMasterService.updateCourse(courseMasterDto);
    }
    
    @RequestMapping(value = "/rolesbycourse", method = RequestMethod.GET)
    public List<RoleMasterDto> getRolesByCourse(@RequestParam("courseId") Integer courseId) {
        return courseMasterService.getRolesByCourse(courseId);
    }
    
    @RequestMapping(value = "/rolesbycourse/trainer", method = RequestMethod.GET)
    public List<RoleMasterDto> getTrainerRolesByCourse(@RequestParam("courseId") Integer courseId) {
        return courseMasterService.getTrainerRolesByCourse(courseId);
    }
    
    @RequestMapping(value = "/module", method = RequestMethod.GET)
    public List<CourseMasterDto> getCoursesByModuleIds(@RequestParam("moduleIds") List<Integer> moduleIds) {
        return courseMasterService.getCoursesByModuleIds(moduleIds);
    }
}