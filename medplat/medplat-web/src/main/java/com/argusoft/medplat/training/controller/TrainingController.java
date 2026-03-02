
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.training.dto.TrainingDto;
import com.argusoft.medplat.training.dto.TrainingScheduleDto;
import com.argusoft.medplat.training.dto.TrainingStatus;
import com.argusoft.medplat.training.service.CertificateService;
import com.argusoft.medplat.training.service.TrainingService;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * <p>
 * Define APIs for training.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/training")
@Tag(name = "Training Controller", description = "")
public class TrainingController {

    @Autowired
    TrainingService trainingService;

    @Autowired
    CertificateService certificateService;


    /**
     * Add new training.
     * @param trainingScheduleDto Training schedule details.
     */
    @PostMapping(value = "/create")
    public void createTraining(@RequestBody List<TrainingScheduleDto> trainingList) {
        for (TrainingScheduleDto trainingScheduleDto: trainingList){
            trainingService.createTraining(trainingScheduleDto);
        }
    }

    /**
     * Retrieves training schedule details by trainer id.
     * @param trainerId Trainer id.
     * @param onDate Training date.
     * @param afterDate Current date.
     * @param beforeDate Before training date.
     * @param fetchOptionalTrainerTrainings Want to fetch optional trainer's training details.
     * @return Returns list of training details based on defined params.
     */
    @GetMapping(value = "/retrieve")
    public List<TrainingDto> searchForTraining(@RequestParam("trainerId") Integer trainerId,
            @RequestParam(value = "onDate", required = false) Long onDate,
            @RequestParam(value = "afterDate", required = false) Long afterDate,
            @RequestParam(value = "beforeDate", required = false) Long beforeDate,
            @RequestParam(value = "fetchOptionalTrainerTrainings", required = false) boolean fetchOptionalTrainerTrainings) {
        return trainingService.searchForTraining(trainerId, onDate, afterDate, beforeDate, fetchOptionalTrainerTrainings);
    }

    /**
     * Retrieves trainers by location.
     * @param locationId Location id.
     * @param roleId Role id.
     * @param courseId Course id.
     * @return Returns list of trainers details.
     */
    @GetMapping(value = "/users")
    public List<UserMasterDto> searchUsers(@RequestParam(value = "locationId", required = false) Integer locationId,
                                           @RequestParam(value = "roleId") Integer roleId,
                                           @RequestParam(value = "courseId", required = false) Integer courseId) {
        return trainingService.searchUsers(locationId, roleId, courseId);
    }

    /**
     * Reschedule training.
     * @param trainingId Training id.
     * @param oldDate Old training date.
     * @param newDate New training date.
     * @param isFirst is first time for reschedule.
     */
    @PutMapping(value = "/reschedule/{trainingId}")
    public void rescheduleTraining(@PathVariable("trainingId") Integer trainingId,
            @RequestParam("oldDate") Long oldDate,
            @RequestParam("newDate") Long newDate,
            @RequestParam("isFirst") Boolean isFirst) {
        trainingService.rescheduleTraining(trainingId,oldDate,newDate,isFirst);
    }

    /**
     * Retrieves training using user location.
     * @param afterDate After training date.
     * @param currentDate Current date.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @return Returns list of training.
     */
    @GetMapping(value = "/dashboard")
    public List<TrainingDto> getTrainingsOfUserLocation(@RequestParam(value = "afterDate" ,required = false) Long afterDate,
            @RequestParam(value = "currentDate" ,required = false) Long currentDate,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset) {
        return trainingService.getTrainingsOfUserLocation(afterDate, currentDate, limit, offset);
    }


    /**
     * Update trainee status.
     * @param updateTraineeStatus Trainee status details.
     */
    @PutMapping(value = "/status")
    public void updateTraineeStatus(@RequestBody TrainingStatus updateTraineeStatus) {
        trainingService.updateTraineeStatus(updateTraineeStatus);
    }

    /**
     * Retrieves training status by trainer id and training date.
     * @param trainingId Training id.
     * @param trainerId Trainer id.
     * @param beforeDate Before training date.
     * @return Returns list of training status.
     */
    @GetMapping(value = "/status")
    public List<TrainingStatus> getTrainingStatusesByTrainerAndDate(@RequestParam(value = "trainingId",required = false) Integer trainingId,
            @RequestParam(value ="trainerId",required = false) Integer trainerId,
            @RequestParam(value ="beforeDate",required = false) Long beforeDate) {
        return trainingService.getTrainingStatusesByTrainerAndDate(trainingId,trainerId,beforeDate);
    }

    /**
     * Retrieves today's training using trainer id.
     * @param trainerId Trainer id.
     * @return Returns list of trainings.
     */
    @GetMapping(value = "/today")
    public List<TrainingDto> getUserTrainingsByToday(@RequestParam("userId") Integer trainerId) {
        return trainingService.getUserTrainingsOfToday(trainerId, new Date());
    }
}