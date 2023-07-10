
package com.argusoft.medplat.course.mapper;

import com.argusoft.medplat.course.dto.TopicMediaMasterDto;
import com.argusoft.medplat.course.model.TopicMediaMaster;
import com.argusoft.medplat.mobile.dto.QuestionSetBean;
import com.argusoft.medplat.mobile.dto.TopicMediaDataBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Mapper for expected target in order to convert dto to model or model to dto.
 * </p>
 *
 * @author sneha
 * @since 01/03/2021 18:38
 */
public class TopicMediaMasterMapper {

    private TopicMediaMasterMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert expected target entity to dto.
     *
     * @param topicMediaMaster An Entity of expected target.
     * @return Returns Object of expected target.
     */
    public static TopicMediaMasterDto entityToDtoTopicMaster(TopicMediaMaster topicMediaMaster) {
        TopicMediaMasterDto topicMediaMasterDto = new TopicMediaMasterDto();

        topicMediaMasterDto.setId(topicMediaMaster.getId());
        topicMediaMasterDto.setTopicId(topicMediaMaster.getTopicId());
        topicMediaMasterDto.setMediaId(topicMediaMaster.getMediaId());
        topicMediaMasterDto.setMediaFileName(topicMediaMaster.getMediaFileName());
        topicMediaMasterDto.setMediaType(topicMediaMaster.getMediaType());
        topicMediaMasterDto.setMediaOrder(topicMediaMaster.getMediaOrder());
        topicMediaMasterDto.setTranscriptFileId(topicMediaMaster.getTranscriptFileId());
        topicMediaMasterDto.setTranscriptFileName(topicMediaMaster.getTranscriptFileName());
        topicMediaMasterDto.setUrl(topicMediaMaster.getUrl());
        topicMediaMasterDto.setTitle(topicMediaMaster.getTitle());
        topicMediaMasterDto.setDescription(topicMediaMaster.getDescription());
        topicMediaMasterDto.setMediaState(topicMediaMaster.getMediaState());
        topicMediaMasterDto.setIsUserFeedbackRequired(topicMediaMaster.getIsUserFeedbackRequired());
        topicMediaMasterDto.setSize(topicMediaMaster.getSize());

        return topicMediaMasterDto;
    }

    /**
     * Convert expected list of target entity to list of dto.
     *
     * @param topicMediaMasters A list of entity of expected target.
     * @return Returns a list of dto of expected target.
     */
    public static List<TopicMediaMasterDto> entityToDtoTopicMasterList(List<TopicMediaMaster> topicMediaMasters) {
        List<TopicMediaMasterDto> topicMasterDtos = new LinkedList<>();
        for (TopicMediaMaster topicMaster : topicMediaMasters) {
            topicMasterDtos.add(TopicMediaMasterMapper.entityToDtoTopicMaster(topicMaster));
        }
        return topicMasterDtos;
    }

    /**
     * Convert expected target dto to entity.
     *
     * @param topicMediaMasterDto A dto of expected target.
     * @return Returns an entity of expected target.
     */
    public static TopicMediaMaster dtoToEntityTopicMaster(TopicMediaMasterDto topicMediaMasterDto) {
        TopicMediaMaster topicMediaMaster = new TopicMediaMaster();

        topicMediaMaster.setId(topicMediaMasterDto.getId());
        topicMediaMaster.setMediaId(topicMediaMasterDto.getMediaId());
        topicMediaMaster.setMediaFileName(topicMediaMasterDto.getMediaFileName());
        topicMediaMaster.setMediaType(topicMediaMasterDto.getMediaType());
        topicMediaMaster.setMediaOrder(topicMediaMasterDto.getMediaOrder());
        topicMediaMaster.setTranscriptFileId(topicMediaMasterDto.getTranscriptFileId());
        topicMediaMaster.setTranscriptFileName(topicMediaMasterDto.getTranscriptFileName());
        topicMediaMaster.setTopicId(topicMediaMasterDto.getTopicId());
        topicMediaMaster.setUrl(topicMediaMasterDto.getUrl());
        topicMediaMaster.setTitle(topicMediaMasterDto.getTitle());
        topicMediaMaster.setDescription(topicMediaMasterDto.getDescription());
        topicMediaMaster.setMediaState(topicMediaMasterDto.getMediaState());
        topicMediaMaster.setIsUserFeedbackRequired(topicMediaMasterDto.getIsUserFeedbackRequired());
        topicMediaMaster.setSize(topicMediaMasterDto.getSize());

        return topicMediaMaster;
    }


    /**
     * Convert expected list of target dto to list of entity.
     *
     * @param topicMediaMasterDtos A list of dto of expected target.
     * @return Returns a list of entity of expected target.
     */
    public static List<TopicMediaMaster> dtoToEntityTopicMasterList(List<TopicMediaMasterDto> topicMediaMasterDtos) {
        List<TopicMediaMaster> topicMediaMasters = new LinkedList<>();
        for (TopicMediaMasterDto topicMediaMasterDto : topicMediaMasterDtos) {
            topicMediaMasters.add(TopicMediaMasterMapper.dtoToEntityTopicMaster(topicMediaMasterDto));
        }
        return topicMediaMasters;
    }

    /**
     * Convert expected target entity to dataBean
     *
     * @param topicMediaMaster An topicVideoMaster of expected target
     * @return an DataBean of expected target
     */
    public static TopicMediaDataBean entityToDataBean(TopicMediaMaster topicMediaMaster, List<QuestionSetBean> questionSets) {
        TopicMediaDataBean dataBean = new TopicMediaDataBean();
        dataBean.setActualId(topicMediaMaster.getId());
        dataBean.setTopicId(topicMediaMaster.getTopicId());
        dataBean.setMediaId(topicMediaMaster.getMediaId());
        dataBean.setMediaFileName(topicMediaMaster.getMediaFileName());
        dataBean.setMediaType(topicMediaMaster.getMediaType().toString());
        dataBean.setMediaOrder(topicMediaMaster.getMediaOrder());
        dataBean.setTranscriptFileId(topicMediaMaster.getTranscriptFileId());
        dataBean.setTranscriptFileName(topicMediaMaster.getTranscriptFileName());
        dataBean.setUrl(topicMediaMaster.getUrl());
        dataBean.setTitle(topicMediaMaster.getTitle());
        dataBean.setDescription(topicMediaMaster.getDescription());
        dataBean.setMediaState(topicMediaMaster.getMediaState().toString());
        dataBean.setIsUserFeedbackRequired(topicMediaMaster.getIsUserFeedbackRequired());
        dataBean.setSize(topicMediaMaster.getSize());

        if (questionSets != null) {
            dataBean.setQuestionSet(questionSets);
        }
        return dataBean;
    }

    /**
     * Convert expected target entities to dataBeans
     *
     * @param topicMediaMasters An topicVideoMasters of expected target
     * @return an DataBeans of expected target
     */
    public static List<TopicMediaDataBean> entityToDataBeanList(List<TopicMediaMaster> topicMediaMasters) {
        List<TopicMediaDataBean> dataBeans = new ArrayList<>();
        for (TopicMediaMaster topicMediaMaster : topicMediaMasters) {
            dataBeans.add(entityToDataBean(topicMediaMaster, null));
        }
        return dataBeans;
    }

}
