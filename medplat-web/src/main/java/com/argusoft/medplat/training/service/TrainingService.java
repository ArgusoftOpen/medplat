/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.service;

import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.training.dto.TrainingDto;
import com.argusoft.medplat.training.dto.TrainingScheduleDto;
import com.argusoft.medplat.training.dto.TrainingStatus;
import com.argusoft.medplat.training.validation.impl.ValidationResultInfo;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Define services for training.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public interface TrainingService {

    /**
     * Add new training.
     * @param trainingScheduleDto Training schedule details.
     */
    void createTraining(TrainingScheduleDto trainingScheduleDto);

    /**
     * Retrieves trainings by trainer id.
     * @param trainerId Trainer id.
     * @param fetchOptionalTrainerTrainings Want to fetch optional trainer's training details.
     * @return Returns list of training details.
     */
    List<TrainingDto> getTrainingsByTrainer(Integer trainerId, boolean fetchOptionalTrainerTrainings);

    /**
     * Validate training by validation type, training schedule details, old training date.
     * @param validationType Validation type.
     * @param trainingScheduleDto Training schedule details.
     * @param oldDate Old training date.
     * @return Returns list of validation result.
     */
    List<ValidationResultInfo> validateTraining(String validationType, TrainingScheduleDto trainingScheduleDto, Date oldDate);

    /**
     * Reschedule training.
     * @param trainingId Training id.
     * @param oldDate Old training date.
     * @param newDate New training date.
     * @param first is first time for reschedule.
     */
    void rescheduleTraining(Integer trainingId, Long oldDate, Long newDate, Boolean first);

    /**
     * Retrieves training schedule details by trainer id.
     * @param trainerId Trainer id.
     * @param onDate Training date.
     * @param afterDate Current date.
     * @param beforeDate Before training date.
     * @param fetchOptionalTrainerTrainings Want to fetch optional trainer's training details.
     * @return Returns list of training details based on defined params.
     */
    List<TrainingDto> searchForTraining(Integer trainerId, Long onDate, Long afterDate, Long beforeDate, boolean fetchOptionalTrainerTrainings);

    /**
     * Retrieves trainers by location.
     * @param locationId Location id.
     * @param roleId Role id.
     * @param courseId Course id.
     * @return Returns list of trainers details.
     */
    List<UserMasterDto> searchUsers(Integer locationId, Integer roleId, Integer courseId);

    /**
     * Retrieves training using user location.
     * @param afterDate After training date.
     * @param currentDate Current date.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @return Returns list of training.
     */
    List<TrainingDto> getTrainingsOfUserLocation(Long afterDate, Long currentDate, String limit, String offset);

    /**
     * Retrieves training status by trainer id and training date.
     * @param trainingId Training id.
     * @param trainerId Trainer id.
     * @param beforeDate Before training date.
     * @return Returns list of training status.
     */
    List<TrainingStatus> getTrainingStatusesByTrainerAndDate(Integer trainingId, Integer trainerId, Long beforeDate);

    /**
     * Update trainee status.
     * @param updateTraineeStatus Trainee status details.
     */
    void updateTraineeStatus(TrainingStatus updateTraineeStatus);

    /**
     * Retrieves today's training using trainer id.
     * @param trainerId Trainer id.
     * @return Returns list of trainings.
     */
    List<TrainingDto> getUserTrainingsOfToday(Integer trainerId, Date date);
}
