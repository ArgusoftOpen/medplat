/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.mapper;

import com.argusoft.medplat.training.dto.TopicCoverageDto;
import com.argusoft.medplat.training.model.TopicCoverage;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * <p>
 *     Mapper for topic coverage in order to convert dto to model or model to dto.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class TopicCoverageMapper {
    private TopicCoverageMapper(){}

    /**
     * Retrieves topic coverage entity into details.
     * @param topicCoverage Entity of topic coverage.
     * @return Returns topic coverage details.
     */
    public static TopicCoverageDto entityToDtoTopicCoverage(TopicCoverage topicCoverage) {
        TopicCoverageDto topicCoverageDto = new TopicCoverageDto();

        if (topicCoverage != null) {
            if (topicCoverage.getTopicCoverageId() != null) {
                topicCoverageDto.setTopicCoverageId(topicCoverage.getTopicCoverageId());
            }
            topicCoverageDto.setTrainingId(topicCoverage.getTrainingId());
            topicCoverageDto.setCourseId(topicCoverage.getCourseId());
            topicCoverageDto.setTopicId(topicCoverage.getTopicId());
            topicCoverageDto.setTopicCoverageName(topicCoverage.getTopicCoverageName());
            topicCoverageDto.setTopicCoverageDescription(topicCoverage.getTopicCoverageDescription());
            topicCoverageDto.setTopicCoverageState(topicCoverage.getTopicCoverageState());
            topicCoverageDto.setCompletedOn(topicCoverage.getTopicCoverageCompletedOn());
            topicCoverageDto.setSubmittedOn(topicCoverage.getTopicCoverageSubmittedOn());
            topicCoverageDto.setReason(topicCoverage.getTopicCoverageReason());
            topicCoverageDto.setRemarks(topicCoverage.getTopicCoverageRemarks());
            topicCoverageDto.setEffectiveDate(topicCoverage.getTopicCoverageEffectiveDate());
            topicCoverageDto.setExpirationDate(topicCoverage.getTopicCoverageExpirationDate());

        }
        return topicCoverageDto;
    }

    /**
     * Retrieves list of topic coverage entities into details.
     * @param topicCoverages List of topic coverage entities.
     * @return Returns topic coverage details.
     */
    public static List<TopicCoverageDto> entityToDtoTopicCoverageList(List<TopicCoverage> topicCoverages) {
        List<TopicCoverageDto> topicCoverageDtos = new LinkedList<>();
        for (TopicCoverage topicCoverage : topicCoverages) {
            topicCoverageDtos.add(TopicCoverageMapper.entityToDtoTopicCoverage(topicCoverage));
        }
        return topicCoverageDtos;
    }

    /**
     * Retrieves details of topic coverage into entity.
     * @param topicCoverageDto Details of topic coverage.
     * @param topicCoverage Entity of topic coverage.
     * @return Returns entity of topic coverage.
     */
    public static TopicCoverage dtoToEntityTopicCoverage(TopicCoverageDto topicCoverageDto, TopicCoverage topicCoverage) {
        if (topicCoverage == null) {
            topicCoverage = new TopicCoverage();
        }
        if (topicCoverageDto != null) {
            if (topicCoverageDto.getTopicCoverageId() != null) {
                topicCoverage.setTopicCoverageId(topicCoverageDto.getTopicCoverageId());
            }
            topicCoverage.setTrainingId(topicCoverageDto.getTrainingId());
            topicCoverage.setCourseId(topicCoverageDto.getCourseId());
            topicCoverage.setTopicId(topicCoverageDto.getTopicId());
            topicCoverage.setTopicCoverageName(topicCoverageDto.getTopicCoverageName());
            topicCoverage.setTopicCoverageDescription(topicCoverageDto.getTopicCoverageDescription());
            topicCoverage.setTopicCoverageState(topicCoverageDto.getTopicCoverageState());
            topicCoverage.setTopicCoverageCompletedOn(topicCoverageDto.getCompletedOn());
            topicCoverage.setTopicCoverageSubmittedOn(topicCoverageDto.getSubmittedOn());
            topicCoverage.setTopicCoverageReason(topicCoverageDto.getReason());
            topicCoverage.setTopicCoverageRemarks(topicCoverageDto.getRemarks());
            topicCoverage.setTopicCoverageEffectiveDate(topicCoverageDto.getEffectiveDate());
            topicCoverage.setTopicCoverageExpirationDate(topicCoverageDto.getExpirationDate());
        }
        return topicCoverage;
    }
}
