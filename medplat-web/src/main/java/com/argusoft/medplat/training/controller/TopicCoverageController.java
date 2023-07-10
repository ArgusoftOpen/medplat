/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.controller;

import com.argusoft.medplat.training.dto.TopicCoverageDto;
import com.argusoft.medplat.training.service.TopicCoverageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * <p>
 * Define APIs for topic coverage.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/topiccoverage")
public class TopicCoverageController {

    @Autowired
    TopicCoverageService topicCoverageService;

    /**
     * Add topic coverage details for training.
     * @param topicCoverageDto Topic coverage details.
     */
    @PostMapping(value = "/create")
    public void createTopicCoverage(@RequestBody TopicCoverageDto topicCoverageDto) {
        topicCoverageService.createTopicCoverage(topicCoverageDto);
    }

    /**
     * Update list of topic coverage details for training.
     * @param topicCoverageDtos List of topic coverage details.
     */
    @PutMapping(value = "/update")
    public void updateTopicCoverage(@RequestBody List<TopicCoverageDto> topicCoverageDtos) {
        for (TopicCoverageDto topicCoverageDto1 : topicCoverageDtos) {
            topicCoverageService.updateTopicCoverage(topicCoverageDto1);
        }
        
    }

    /**
     * Delete topic coverage details by id.
     * @param topicCoverageId Topic coverage id.
     */
    @DeleteMapping(value = "/{topicCoverageId}")
    public void deleteTopicCoverage(@PathVariable("topicCoverageId") Integer topicCoverageId) {
        topicCoverageService.deleteTopicCoverage(topicCoverageId);
    }

    /**
     * Retrieves topic coverage details by id.
     * @param topicCoverageId Topic coverage id.
     * @return Returns topic coverage details.
     */
    @GetMapping(value = "/{topicCoverageId}")
    public TopicCoverageDto getTopicCoverage(@PathVariable("topicCoverageId") Integer topicCoverageId) {
        return topicCoverageService.getTopicCoverageByTopicCoverageId(topicCoverageId);
    }

    /**
     * Retrieves topic coverage details by training id.
     * @param trainingId Training id.
     * @return Returns list of topic coverage details.
     */
    @GetMapping(value = "/training")
    public List<TopicCoverageDto> getTopicCoveragesByTrainingId(@RequestParam("trainingId") Integer trainingId) {
        return topicCoverageService.getTopicCoverageByTrainingId(trainingId);
    }

    /**
     * Retrieves topic coverage details by topic coverage id.
     * @param topicCoverageIds List of topic coverage ids.
     * @return Returns list of topic coverage details.
     */
    @GetMapping(value = "/ids/{topicCoverageId}")
    public List<TopicCoverageDto> getTopicCoverageByTopicCoverageIds(@PathVariable(value = "topicCoverageId") List<Integer> topicCoverageIds) {
        return topicCoverageService.getTopicCoverageByTopicCoverageIds(topicCoverageIds);
    }

    /**
     * Retrieves topic coverage details by training id, before and after date, on date.
     * @param trainingId Training id.
     * @param afterDate Training after date.
     * @param onDate Training on date.
     * @param beforeDate Training before date.
     * @return Returns list of topic coverage details by defined criteria.
     */
    @GetMapping(value = "/retrieve")
    public List<TopicCoverageDto> searchForTopicCoverages(@RequestParam("trainingId") Integer trainingId,
            @RequestParam(value  = "afterDate", required = false) Long afterDate,
            @RequestParam(value = "onDate" , required = false) Long onDate,
            @RequestParam(value = "beforeDate", required = false) Long beforeDate) {
        return topicCoverageService.searchForTopicCoverages(trainingId, afterDate, onDate, beforeDate);
    }

    /**
     * Change state to submit for topic coverages.
     * @param topicCoverageDto Topic coverage details.
     */
    @PutMapping(value = "/state/change")
    public void changeStateToSubmit4TopicCoverages(@RequestBody TopicCoverageDto topicCoverageDto) {
         topicCoverageService.changeStateToSubmit4TopicCoverages(topicCoverageDto);
    }
    
}
