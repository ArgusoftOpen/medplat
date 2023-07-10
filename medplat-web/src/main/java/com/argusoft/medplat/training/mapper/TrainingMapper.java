/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.mapper;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.service.RoleService;
import com.argusoft.medplat.web.users.service.UserService;
import com.argusoft.medplat.common.util.AESEncryption;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.service.CourseMasterService;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.web.location.dto.LocationDetailDto;
import com.argusoft.medplat.web.location.service.LocationService;
import com.argusoft.medplat.training.dto.TrainingDto;
import com.argusoft.medplat.training.dto.TrainingScheduleDto;
import com.argusoft.medplat.training.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 *
 * <p>
 *     Mapper for training in order to convert dto to model or model to dto.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Component
public class TrainingMapper {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    CourseMasterService courseMasterService;

    @Autowired
    LocationService locationService;

    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;

    @PostConstruct
    public void init() {
        AESEncryption instance = AESEncryption.getInstance();
        instance.init("argusadmin123456", "argusadmin123456");
    }

    /**
     * Convert training entity into details.
     * @param training Entity  of training.
     * @return Returns details of training.
     */
    public TrainingDto entityToTrainingDto(Training training) {

        TrainingDto trainingDto = new TrainingDto();

        trainingDto.setTrainingId(training.getTrainingId());
        trainingDto.setTrainingName(training.getTrainingName());
        trainingDto.setTrainingDescription(training.getTrainingDescription());
        trainingDto.setTrainingState(training.getTrainingState());
        trainingDto.setEffectiveDate(training.getTrainingEffectiveDate());
        trainingDto.setExpirationDate(training.getTrainingExpirationDate());
        trainingDto.setLocation(training.getTrainingLocationName());

            Map<Integer, String> trainingCourses = new HashMap<>();
            Set<Integer> trainingCourseIds = training.getTrainingCourseIds();
            List<CourseMasterDto> courseMasterDtos = courseMasterService.getCoursesByIds(trainingCourseIds);

            for (CourseMasterDto courseMasterDto : courseMasterDtos) {
                String courseName = courseMasterDto.getCourseName();
                trainingCourses.put(courseMasterDto.getCourseId(), courseName);
            }
            trainingDto.setCourses(trainingCourses);

            Map<Integer, String> primaryTrainers = new HashMap<>();
            Set<Integer> primaryTrainerIds = training.getTrainingPrimaryTrainerIds();
            if (!primaryTrainerIds.isEmpty()) {
                List<UserMasterDto> userMasterDtos = userService.getUsersByIds(primaryTrainerIds);

                for (UserMasterDto userMasterDto : userMasterDtos) {
                    String personName = userMasterDto.getFirstName() + " " + userMasterDto.getLastName();
                    primaryTrainers.put(userMasterDto.getId(), personName);
                }
            }
            trainingDto.setPrimaryTrainers(primaryTrainers);

            Map<Integer, String> optionalTrainers = new HashMap<>();
            Set<Integer> optionalTrainerIds = training.getTrainingOptionalTrainerIds();
            if (!optionalTrainerIds.isEmpty()) {
                List<UserMasterDto> userMasterDtos = userService.getUsersByIds(optionalTrainerIds);

                for (UserMasterDto userMasterDto : userMasterDtos) {
                    String personName = userMasterDto.getFirstName() + " " + userMasterDto.getLastName();
                    optionalTrainers.put(userMasterDto.getId(), personName);
                }
            }
            trainingDto.setOptionalTrainers(optionalTrainers);

            Map<Integer, String> organizationUnits = new HashMap<>();
            Set<Integer> organizationUnitIds = training.getTrainingOrganizationUnitIds();
            if (!organizationUnitIds.isEmpty()) {
                List<LocationDetailDto> locationMasterDtos = locationService.getLocationsByIds(organizationUnitIds);
                for (LocationDetailDto locationMasterDto : locationMasterDtos) {
                    String locationName = locationMasterDto.getName();
                    organizationUnits.put(locationMasterDto.getId(), locationName);
                }
            }
            trainingDto.setOrganizationUnits(organizationUnits);

            Map<Integer, String> attendees = new HashMap<>();
            Set<Integer> attendeesIds = training.getTrainingAttendeeIds();
            if (!attendeesIds.isEmpty()) {
                List<UserMasterDto> userMasterDtos = userService.getUsersByIds(attendeesIds);
                for (UserMasterDto userMasterDto : userMasterDtos) {
                    String userName = userMasterDto.getFirstName() + " " + userMasterDto.getLastName();
                    attendees.put(userMasterDto.getId(), userName);
                }
            }
            trainingDto.setAttendees(attendees);

            Map<Integer, String> additionalAttendees = new HashMap<>();
            Set<Integer> additionalAttendeesIds = training.getTrainingAdditionalAttendeeIds();
            if (!additionalAttendeesIds.isEmpty()) {
                List<UserMasterDto> userMasterDtos = userService.getUsersByIds(additionalAttendeesIds);
                for (UserMasterDto userMasterDto : userMasterDtos) {
                    String userName = userMasterDto.getFirstName() + " " + userMasterDto.getLastName();
                    additionalAttendees.put(userMasterDto.getId(), userName);
                }
            }
            trainingDto.setAdditionalAttendees(additionalAttendees);

            Map<Integer, String> trainerRoles = new HashMap<>();
            Set<Integer> trainerRoleIds = training.getTrainerRoleIds();
            if (!trainerRoleIds.isEmpty()) {
                List<RoleMasterDto> roleMasterDtos = roleService.getRolesByIds(trainerRoleIds);
                for (RoleMasterDto roleMasterDto : roleMasterDtos) {
                    String roleName = roleMasterDto.getName();
                    trainerRoles.put(roleMasterDto.getId(), roleName);
                }
            }
            trainingDto.setPrimaryTrainerRole(trainerRoles);


            Map<Integer, String> targetRoles = new HashMap<>();
            Set<Integer> targetRoleIds = training.getTargetRoleIds();
            if (!targetRoleIds.isEmpty()) {
                List<RoleMasterDto> roleMasterDtos = roleService.getRolesByIds(targetRoleIds);

                for (RoleMasterDto roleMasterDto : roleMasterDtos) {
                    String roleName = roleMasterDto.getName();
                    targetRoles.put(roleMasterDto.getId(), roleName);
                }
            }
            trainingDto.setPrimaryTargetRole(targetRoles);

        return trainingDto;
    }

    /**
     * Convert list of training entities into details.
     * @param trainingList List of training entities.
     * @return Returns list of training details.
     */
    public List<TrainingDto> entityToTrainingDtoList(List<Training> trainingList) {
        List<TrainingDto> trainingDtos = new LinkedList<>();

        for (Training training : trainingList) {
            trainingDtos.add(this.entityToTrainingDto(training));
        }

        return trainingDtos;
    }

    /**
     * Convert training schedule details into entity.
     * @param trainingScheduleDto Training schedule details.
     * @return Returns entity of training schedule.
     */
    public static Training dtoToEntityTrainingScheduleDto(TrainingScheduleDto trainingScheduleDto) {

        Training training = new Training();

        training.setTrainingId(trainingScheduleDto.getTrainingId());
        training.setTrainingName(trainingScheduleDto.getTrainingName());
        training.setTrainingDescription(trainingScheduleDto.getTrainingDescription());
        training.setTrainingState(trainingScheduleDto.getTrainingState());
        training.setTrainingEffectiveDate(new Date(trainingScheduleDto.getEffectiveDate()));
        if(!trainingScheduleDto.getCourseType().equals("ONLINE")){
            training.setTrainingExpirationDate(new Date(trainingScheduleDto.getExpirationDate()));
        }
        training.setTrainingLocationName(trainingScheduleDto.getLocation());
        training.setTrainingPrimaryTrainerIds(trainingScheduleDto.getPrimaryTrainers());
        training.setTrainingOptionalTrainerIds(trainingScheduleDto.getOptionalTrainers());
        training.setTrainingOrganizationUnitIds(trainingScheduleDto.getOrganizationUnits());
        training.setTrainingCourseIds(trainingScheduleDto.getCourses());
        training.setTrainingAttendeeIds(trainingScheduleDto.getAttendees());
        training.setTrainingAdditionalAttendeeIds(trainingScheduleDto.getAdditionalAttendees());
        training.setTargetRoleIds(trainingScheduleDto.getPrimaryTargetRole());
        training.setTrainerRoleIds(trainingScheduleDto.getPrimaryTrainerRole());
        return training;
    }

    /**
     * Convert training schedule entity into details.
     * @param training Entity of training schedule.
     * @return Returns details of training schedule.
     */
    public static TrainingScheduleDto entityToTrainingScheduleDto(Training training) {

        TrainingScheduleDto trainingDto = new TrainingScheduleDto();

        trainingDto.setTrainingId(training.getTrainingId());
        trainingDto.setTrainingName(training.getTrainingName());
        trainingDto.setTrainingDescription(training.getTrainingDescription());
        trainingDto.setTrainingState(training.getTrainingState());
        trainingDto.setEffectiveDate(training.getTrainingEffectiveDate().getTime());
        trainingDto.setExpirationDate(training.getTrainingExpirationDate().getTime());
        trainingDto.setLocation(training.getTrainingLocationName());

        trainingDto.setCourses(training.getTrainingCourseIds());
        trainingDto.setPrimaryTrainers(training.getTrainingPrimaryTrainerIds());
        trainingDto.setOptionalTrainers(training.getTrainingOptionalTrainerIds());
        trainingDto.setOrganizationUnits(training.getTrainingOptionalTrainerIds());
        trainingDto.setAttendees(training.getTrainingAttendeeIds());
        trainingDto.setAdditionalAttendees(training.getTrainingAdditionalAttendeeIds());
        trainingDto.setPrimaryTrainerRole(training.getTrainerRoleIds());
        trainingDto.setPrimaryTargetRole(training.getTargetRoleIds());
        return trainingDto;
    }

}
