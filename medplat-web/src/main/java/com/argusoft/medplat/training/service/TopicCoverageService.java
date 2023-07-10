/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.service;

import com.argusoft.medplat.training.dto.TopicCoverageDto;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 *     Define services for topic coverage.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public interface TopicCoverageService {

    /**
     * Retrieves topic coverage details by training id, before and after date, on date.
     * @param trainingId Training id.
     * @param afterDate Training after date.
     * @param onDate Training on date.
     * @param beforeDate Training before date.
     * @return Returns list of topic coverage details by defined criteria.
     */
    List<TopicCoverageDto> searchForTopicCoverages(Integer trainingId, Long afterDate, Long onDate, Long beforeDate);

    /**
     * Add topic coverage details for training.
     * @param topicCoverageDto Topic coverage details.
     */
    void createTopicCoverage(TopicCoverageDto topicCoverageDto);

    /**
     * Retrieves topic coverage details by id.
     * @param topicCoverageId Topic coverage id.
     * @return Returns topic coverage details.
     */
    TopicCoverageDto getTopicCoverageByTopicCoverageId(Integer topicCoverageId);

    /**
     * Used to update topic coverage details.
     * @param topicCoverage Topic coverage details.
     */
    void updateTopicCoverage(TopicCoverageDto topicCoverage);

    /**
     * Delete topic coverage details by id.
     * @param topicCoverageId Topic coverage id.
     */
    void deleteTopicCoverage(Integer topicCoverageId);

    /**
     * Retrieves topic coverage details by training id.
     * @param trainingId Training id.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverageDto> getTopicCoverageByTrainingId(Integer trainingId);

    /**
     * Retrieves topic coverages by training id and after date.
     * @param trainingId Training id.
     * @param date After date.
     * @return Returns list of topic coverage details..
     */
    List<TopicCoverageDto> getTopicCoveragesByTrainingIdAndDate(Integer trainingId,
            Date date);

    /**
     * Retrieves topic coverages by training id and training on date.
     * @param trainingId Training id.
     * @param onDate Training on date.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverageDto> getTopicCoveragesByTrainingOnDate(Integer trainingId,
            Date onDate);

    /**
     * Retrieves topic coverage details by topic coverage id.
     * @param topicCoverageIds List of topic coverage ids.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverageDto> getTopicCoverageByTopicCoverageIds(List<Integer> topicCoverageIds);

    /**
     * Retrieves topic coverages by training id and before date.
     * @param trainingId Training id.
     * @param beforeDate Before date.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverageDto> getTopicCoveragesByTrainingAndBeforeDate(Integer trainingId,
            Date beforeDate);

    /**
     * Retrieves topic coverages by list of trainings ids.
     * @param trainingIds List of trainings ids.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverageDto> getTopicCoveragesByTrainingIds(List<Integer> trainingIds);

    /**
     * Reschedule topic coverage details.
     * @param topicCoverages Topic coverage details.
     * @param newDate New date for topic.
     * @return  Returns result.
     */
    Map<Date, List<TopicCoverageDto>> rescheduleTopicCoverages(List<TopicCoverageDto> topicCoverages, Date newDate);

    /**
     * Change state to submit for topic coverages.
     * @param topicCoverageDto Topic coverage details.
     */
    void changeStateToSubmit4TopicCoverages(TopicCoverageDto topicCoverageDto);

    /**
     * Retrieves unique dates of topics coverage by list of training ids.
     * @param trainingId List of training ids.
     * @return Returns list of on dates of topic coverage.
     */
    List<Date> getDistinctDatesOfTopicCoveragesByTrainingIds(List<Integer> trainingId);
    
}
