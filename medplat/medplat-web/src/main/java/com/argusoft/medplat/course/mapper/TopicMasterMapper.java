/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.mapper;

import com.argusoft.medplat.course.dto.TopicMasterDto;
import com.argusoft.medplat.course.model.TopicMaster;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author akshar
 */
public class TopicMasterMapper {
    public static TopicMasterDto entityToDtoTopicMaster(TopicMaster topicMaster){
        TopicMasterDto topicMasterDto = new TopicMasterDto();
       
        topicMasterDto.setTopicDescription(topicMaster.getTopicDescription());
        topicMasterDto.setTopicId(topicMaster.getTopicId());
        topicMasterDto.setTopicName(topicMaster.getTopicName());
        topicMasterDto.setTopicOrder(topicMaster.getTopicOrder());
        topicMasterDto.setTopicState(topicMaster.getTopicState());
        topicMasterDto.setTopicDay(topicMaster.getDay());
        topicMasterDto.setTopicState(topicMaster.getTopicState());
        
        return topicMasterDto;
    }
    
    public static List<TopicMasterDto> entityToDtoTopicMasterList(List<TopicMaster> topicMasters){
        List<TopicMasterDto> topicMasterDtos = new LinkedList<>();
        for(TopicMaster topicMaster:topicMasters){
            topicMasterDtos.add(TopicMasterMapper.entityToDtoTopicMaster(topicMaster));
        }
        return topicMasterDtos;
    }
    
    public static TopicMaster dtoToEntityTopicMaster(TopicMasterDto topicMasterdto){
        TopicMaster topicMaster = new TopicMaster();
        
        topicMaster.setTopicDescription(topicMasterdto.getTopicDescription());
        topicMaster.setTopicId(topicMasterdto.getTopicId());
        topicMaster.setTopicName(topicMasterdto.getTopicName());
        topicMaster.setTopicState(topicMasterdto.getTopicState());
        topicMaster.setDay(topicMasterdto.getTopicDay());
        topicMaster.setTopicOrder(topicMasterdto.getTopicOrder());
        topicMaster.setTopicState(topicMasterdto.getTopicState());
        return topicMaster;
    }
    
    public static List<TopicMaster> dtoToEntityTopicMasterList(List<TopicMasterDto> topicMastersDtos){
        List<TopicMaster> topicMasters = new LinkedList<>();
        for(TopicMasterDto topicMasterDto:topicMastersDtos){
            topicMasters.add(TopicMasterMapper.dtoToEntityTopicMaster(topicMasterDto));
        }
        return topicMasters;
    }
}
