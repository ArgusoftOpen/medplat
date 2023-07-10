/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.training.model.Training;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for training.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public interface TrainingDao extends GenericDao<Training, Integer>{

    /**
     * Retrieves list of effective trainings by training on date.
     * @param trainerId Trainer id.
     * @param onDate Training on date.
     * @return Returns list of effective training details.
     */
    List<Training> getEffectiveTrainingsByTrainerOnDate(Integer trainerId, Date onDate);

    /**
     * Retrieves trainings by training before date.
     * @param trainerId Trainer id.
     * @param beforeDate Training before date.
     * @return Returns list of trainings details.
     */
    List<Training> getTrainingsByTrainerBeforeDate(Integer trainerId, Date beforeDate);

    /**
     * Retrieves effective trainings by trainer after date.
     * @param trainerId Trainer id.
     * @param afterDate Trainer after date.
     * @return Returns list of training details.
     */
    List<Training> getEffectiveTrainingsByTrainerAfterDate(Integer trainerId, Date afterDate);

    /**
     * Retrieves trainings by trainer id.
     * @param trainerId Trainer id.
     * @param b Want to fetch optional trainer's training details.
     * @return Returns list of training details.
     */
    List<Training> getTrainingsByTrainer(Integer trainerId, boolean b);

    /**
     * Retrieves effective trainings by training after date.
     * @param afterDate Training after date.
     * @return Returns list of training details.
     */
    List<Training> getEffectiveTrainingsAfterDate(Date afterDate);

    /**
     * Retrieves trainings by training before date.
     * @param beforeDate Training before date.
     * @return Returns list of training details.
     */
    List<Training> getTrainingsBeforeDate(Date beforeDate);

    /**
     * Retrieves training ids by user id.
     * @param userId User id.
     * @return Returns list of trainings ids.
     */
    List<Integer> getTrainingIdsofUserChildLocationsAndUsersTraining(Integer userId);

    /**
     * Retrieves trainings by list of training ids and training date.
     * @param trainingIds List of training ids.
     * @param afterDate Training after date.
     * @param currentDate Current date.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @return Returns list of training details.
     */
    List<Training> getTrainingsByTrainingIdsAndDate(List<Integer> trainingIds,Date afterDate,Date currentDate,String limit, String offset);

    /**
     * Retrieves attendees details for upcoming trainings by role id and course id.
     * @param roleId Role id.
     * @param courseId Course id.
     * @return Returns list of ids of attendees.
     */
    List<Integer> getAttendeesForUpcomingTrainingsByRoleAndCourse(Integer roleId, Integer courseId);
}
