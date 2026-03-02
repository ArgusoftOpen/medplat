/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.service.impl;

import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.training.dao.TopicCoverageDao;
import com.argusoft.medplat.training.dto.TopicCoverageDto;
import com.argusoft.medplat.training.mapper.TopicCoverageMapper;
import com.argusoft.medplat.training.model.TopicCoverage;
import com.argusoft.medplat.training.service.TopicCoverageService;
import com.argusoft.medplat.training.util.TrainingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <p>
 *     Define services for topic coverage.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Service("topicCoverageService")
@Transactional
public class TopicCoverageServiceImpl implements TopicCoverageService {

    @Autowired
    TopicCoverageDao topicCoverageDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Date, List<TopicCoverageDto>> rescheduleTopicCoverages(List<TopicCoverageDto> topicCoverages, Date newDate) {
        try {
            Map<Date, List<TopicCoverageDto>> dateAndTopicMap = new LinkedHashMap<>();
            for (TopicCoverageDto oldTopicCoverage : topicCoverages) {
                if (dateAndTopicMap.get(oldTopicCoverage.getEffectiveDate()) != null) {
                    dateAndTopicMap.get(oldTopicCoverage.getEffectiveDate()).add(oldTopicCoverage);
                } else {
                    ArrayList<TopicCoverageDto> topics = new ArrayList<>();
                    topics.add(oldTopicCoverage);
                    dateAndTopicMap.put(oldTopicCoverage.getEffectiveDate(), topics);
                }
            }

            Date newDateExcludingSunday = newDate;
            Set<Date> trainingDays = dateAndTopicMap.keySet();
            List<Date> daysList = new ArrayList<>(trainingDays);
            Collections.sort(daysList);
            for (Date effectiveDate : daysList) {
                for (TopicCoverageDto info : dateAndTopicMap.get(effectiveDate)) {
                    TopicCoverageDto updatedTopicCoverageDto = new TopicCoverageDto(info);
                    updatedTopicCoverageDto.setEffectiveDate(TrainingUtil.prepareDate(newDateExcludingSunday, 0, 0, 0, 0));
                    updatedTopicCoverageDto.setExpirationDate(TrainingUtil.prepareDate(newDateExcludingSunday, 23, 59, 59, 59));
                    TopicCoverage topicCoverage = topicCoverageDao.retrieveById(updatedTopicCoverageDto.getTopicCoverageId());
                    topicCoverageDao.createOrUpdate(TopicCoverageMapper.dtoToEntityTopicCoverage(updatedTopicCoverageDto, topicCoverage));
                }
                Date temp = newDateExcludingSunday;
                if (daysList.stream().filter(d -> d.compareTo(effectiveDate) > 0).noneMatch(d -> d.compareTo(temp) < 0)) {
                    break;
                }
                newDateExcludingSunday = TrainingUtil.calculateNewDateExcludingSunday(newDateExcludingSunday, 2);
            }

            return dateAndTopicMap;

        } catch (Exception ex) {
            throw new ImtechoUserException("exception in rescheduleTopicCoverages", 0, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> searchForTopicCoverages(Integer trainingId, Long afterDateInLong, Long onDateInLong, Long beforeDateInLong) {

        Date afterDate = null;
        Date onDate = null;
        Date beforeDate = null;

        if (afterDateInLong != null) {
            afterDate = new Date(afterDateInLong);
        }

        if (onDateInLong != null) {
            onDate = new Date(onDateInLong);
        }

        if (beforeDateInLong != null) {
            beforeDate = new Date(beforeDateInLong);
        }

        if (trainingId != null && onDate != null) {
            return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingOnDate(trainingId, onDate));
        }

        if (trainingId != null && afterDate != null) {
            return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingIdAndDate(trainingId, afterDate));
        }

        if (trainingId != null && beforeDate != null) {
            return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingAndBeforeDate(trainingId, beforeDate));
        }

        if (trainingId != null) {
            return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoverageByTrainingId(trainingId));
        }
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTopicCoverage(TopicCoverageDto topicCoverageDto) {
        topicCoverageDao.create(TopicCoverageMapper.dtoToEntityTopicCoverage(topicCoverageDto, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicCoverageDto getTopicCoverageByTopicCoverageId(Integer topicCoverageId) {
        return TopicCoverageMapper.entityToDtoTopicCoverage(topicCoverageDao.retrieveById(topicCoverageId));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTopicCoverage(TopicCoverageDto topicCoverageDto) {
        TopicCoverage topicCoverage = topicCoverageDao.retrieveById(topicCoverageDto.getTopicCoverageId());
        topicCoverageDao.createOrUpdate(TopicCoverageMapper.dtoToEntityTopicCoverage(topicCoverageDto, topicCoverage));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTopicCoverage(Integer topicCoverageId) {
        topicCoverageDao.deleteById(topicCoverageId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> getTopicCoverageByTrainingId(Integer trainingId) {
        return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoverageByTrainingId(trainingId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> getTopicCoveragesByTrainingIdAndDate(Integer trainingId, Date date) {
        return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingIdAndDate(trainingId, date));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> getTopicCoveragesByTrainingOnDate(Integer trainingId, Date onDate) {
        try {
            return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingOnDate(trainingId, onDate));
        } catch (Exception ex) {
            throw new ImtechoUserException("Exception in date", 0, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> getTopicCoverageByTopicCoverageIds(List<Integer> topicCoverageIds) {
        return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoverageByTopicCoverageIds(topicCoverageIds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> getTopicCoveragesByTrainingAndBeforeDate(Integer trainingId, Date beforeDate) {
        try {
            return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingAndBeforeDate(trainingId, beforeDate));
        } catch (Exception ex) {
            throw new ImtechoUserException(" exception in Date", 0, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverageDto> getTopicCoveragesByTrainingIds(List<Integer> trainingIds) {
        return TopicCoverageMapper.entityToDtoTopicCoverageList(topicCoverageDao.getTopicCoveragesByTrainingIds(trainingIds));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changeStateToSubmit4TopicCoverages(TopicCoverageDto topicCoverageDto)
    {
        List<Integer> topicCoverageIds = topicCoverageDto.getTopicCoverageIds();
        List<TopicCoverageDto> topicCoverages = this.getTopicCoverageByTopicCoverageIds(topicCoverageIds);
        for (TopicCoverageDto topicCoverage : topicCoverages) {
            try {
                topicCoverage.setTopicCoverageState(TopicCoverage.State.SUBMITTED);
                topicCoverage.setSubmittedOn(TrainingUtil.prepareDate(new Date(), 0, 0, 0, 0));
                this.updateTopicCoverage(topicCoverage);
            } catch (ImtechoUserException ex) {
                throw new ImtechoUserException("OperationFailedException in changeStateToSubmit4TopicCoverages", 0, ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Date> getDistinctDatesOfTopicCoveragesByTrainingIds(List<Integer> trainingId) {
        return topicCoverageDao.getDistinctDatesOfTopicCoveragesByTrainingIds(trainingId);
    }
}
