/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.training.model.TopicCoverage;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for topic coverage.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public interface TopicCoverageDao extends GenericDao<TopicCoverage, Integer> {

    /**
     * Retrieves topic coverage details by training id.
     * @param trainingId Training id.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverage> getTopicCoverageByTrainingId(Integer trainingId);

    /**
     * Retrieves topic coverages by training id and before date.
     * @param trainingId Training id.
     * @param beforeDate Before date.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverage> getTopicCoveragesByTrainingAndBeforeDate(Integer trainingId, Date beforeDate);

    /**
     * Retrieves topic coverages by training id and after date.
     * @param trainingId Training id.
     * @param afterDate After date.
     * @return Returns list of topic coverage details..
     */
    List<TopicCoverage> getTopicCoveragesByTrainingIdAndDate(Integer trainingId, Date afterDate);

    /**
     * Retrieves topic coverages by training id and training on date.
     * @param trainingId Training id.
     * @param onDate Training on date.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverage> getTopicCoveragesByTrainingOnDate(Integer trainingId, Date onDate);

    /**
     * Retrieves topic coverages by list of topic coverage ids.
     * @param topicCoverageIds List of topic coverages ids.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverage> getTopicCoverageByTopicCoverageIds(List<Integer> topicCoverageIds);

    /**
     * Retrieves topic coverages by list of trainings ids.
     * @param trainingIds List of trainings ids.
     * @return Returns list of topic coverage details.
     */
    List<TopicCoverage> getTopicCoveragesByTrainingIds(List<Integer> trainingIds);

    /**
     * Retrieves unique dates of topics coverage by list of training ids.
     * @param trainingId List of training ids.
     * @return Returns list of on dates of topic coverage.
     */
    List<Date> getDistinctDatesOfTopicCoveragesByTrainingIds(List<Integer> trainingId);
}
