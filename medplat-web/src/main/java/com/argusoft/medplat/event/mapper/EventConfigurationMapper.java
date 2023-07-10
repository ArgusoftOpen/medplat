/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.mapper;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.event.dto.MobileEventConfigDto;
import com.argusoft.medplat.event.dto.PushNotificationConfigDto;
import com.argusoft.medplat.event.model.EventConfiguration;
import com.argusoft.medplat.event.model.EventConfigurationType;
import com.argusoft.medplat.event.model.MobileEventConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 *     Mapper for event configuration in order to convert dto to model or model to dto.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class EventConfigurationMapper {

    private EventConfigurationMapper() {

    }

    /**
     * Convert event configuration master entity into dto.
     * @param configuration Event configuration entity.
     * @return Returns event configuration details.
     * @throws IOException If an I/O error occurs when reading or writing.
     */
    public static EventConfigurationDto convertMasterToDto(EventConfiguration configuration) throws IOException {
        EventConfigurationDto notificationConfigDto = new EventConfigurationDto();
        notificationConfigDto.setDay(configuration.getDay());
        notificationConfigDto.setHour(configuration.getHour());
        notificationConfigDto.setMinute(configuration.getMinute());
        notificationConfigDto.setDescription(configuration.getDescription());
        notificationConfigDto.setEventType(configuration.getEventType());
        notificationConfigDto.setEventTypeDetailId(configuration.getEventTypeDetailId());
        notificationConfigDto.setFormTypeId(configuration.getFormTypeId());
        notificationConfigDto.setId(configuration.getId());
        notificationConfigDto.setName(configuration.getName());
        notificationConfigDto.setTrigerWhen(configuration.getTriggerWhen());
        notificationConfigDto.setNotificationConfigDetails(ImtechoUtil.convertJsonToNotificationConfigList(configuration.getNotificationConfigurationDetailJson()));
        notificationConfigDto.setCreatedBy(configuration.getCreatedBy());
        notificationConfigDto.setCreatedOn(configuration.getCreatedOn());
        notificationConfigDto.setState(configuration.getState());
        notificationConfigDto.setEventTypeDetailCode(configuration.getEventTypeDetailCode());
        notificationConfigDto.setUuid(configuration.getUuid());
        return notificationConfigDto;
    }

    /**
     * Convert event configuration details into entity.
     * @param configurationDto Event configuration details.
     * @param eventConfiguration Entity of event configuration.
     * @param isMetodCallBySyncFunction Is method called by sync function.
     * @return Returns entity of event configuration.
     */
    public static EventConfiguration convertDtoToMaster(EventConfigurationDto configurationDto,EventConfiguration eventConfiguration, boolean isMetodCallBySyncFunction) {
        if(eventConfiguration == null){
            eventConfiguration = new EventConfiguration();
        }

        if(!isMetodCallBySyncFunction) {
            eventConfiguration.setId(configurationDto.getId());
        }

        eventConfiguration.setDay(configurationDto.getDay());
        eventConfiguration.setHour(configurationDto.getHour());
        eventConfiguration.setMinute(configurationDto.getMinute());
        eventConfiguration.setDescription(configurationDto.getDescription());
        eventConfiguration.setEventType(configurationDto.getEventType());
        eventConfiguration.setEventTypeDetailId(configurationDto.getEventTypeDetailId());
        eventConfiguration.setFormTypeId(configurationDto.getFormTypeId());
        eventConfiguration.setName(configurationDto.getName());
        eventConfiguration.setTriggerWhen(configurationDto.getTrigerWhen());
        eventConfiguration.setState(eventConfiguration.getState() == null ? EventConfiguration.State.ACTIVE : eventConfiguration.getState());
        eventConfiguration.setCreatedBy(configurationDto.getCreatedBy());
        eventConfiguration.setCreatedOn(configurationDto.getCreatedOn());
        eventConfiguration.setEventTypeDetailCode(configurationDto.getEventTypeDetailCode());
        eventConfiguration.setUuid(configurationDto.getUuid());

        return eventConfiguration;
    }

    /**
     * Convert list of event configuration entities into details.
     * @param notificationConfigs List of event configuration details.
     * @return Returns list of event configuration entities.
     * @throws IOException Signals that an I/O exception of has occurred.
     */
    public static List<EventConfigurationDto> convertMasterListToDtoList(List<EventConfiguration> notificationConfigs) throws IOException {
        List<EventConfigurationDto> notificationConfigurationDtos = new LinkedList<>();
        if (!CollectionUtils.isEmpty(notificationConfigs)) {
            for (EventConfiguration notificationConfiguration : notificationConfigs) {
                notificationConfigurationDtos.add(convertMasterToDto(notificationConfiguration));

            }
        }
        return notificationConfigurationDtos;
    }

    /**
     * Convert event configuration type details into entity.
     * @param configTypeDto Event configuration details.
     * @return Returns entity of event configuration.
     * @throws JsonProcessingException Exception occurred while json processing.
     */
    public static EventConfigurationType convertToConfigurationTypeMaster(EventConfigTypeDto configTypeDto) throws JsonProcessingException {
        EventConfigurationType configurationType = new EventConfigurationType();
        configurationType.setDay(configTypeDto.getDay());
        configurationType.setBaseDateFieldName(configTypeDto.getBaseDateFieldName());
        configurationType.setUserFieldName(configTypeDto.getUserFieldName());
        configurationType.setMemberFieldName(configTypeDto.getMemberFieldName());
        configurationType.setFamilyFieldName(configTypeDto.getFamilyFieldName());
        configurationType.setBaseDateFieldName(configTypeDto.getBaseDateFieldName());
        configurationType.setBaseDateFieldName(configTypeDto.getBaseDateFieldName());
        configurationType.setEmailSubject(configTypeDto.getEmailSubject());
        configurationType.setEmailSubjectParameter(configTypeDto.getEmailSubjectParameter());
        configurationType.setHour(configTypeDto.getHour());
        configurationType.setMinute(configTypeDto.getMiniute());
        configurationType.setMobileNotificationType(configTypeDto.getMobileNotificationType());
        configurationType.setTemplateParameter(configTypeDto.getTemplateParameter());
        configurationType.setId(configTypeDto.getId() == null ? UUID.randomUUID().toString() : configTypeDto.getId());
        configurationType.setTemplate(configTypeDto.getTemplate());
        configurationType.setTrigerWhen(configTypeDto.getTrigerWhen());
        configurationType.setType(configTypeDto.getType());
        configurationType.setConfigId(configTypeDto.getConfigId());
        configurationType.setQueryCode(configTypeDto.getQueryCode());
        configurationType.setQueryMasterParamJson(ImtechoUtil.convertObjectToJson(configTypeDto.getQueryMasterParamJson()));
        configurationType.setSmsConfigJson(ImtechoUtil.convertObjectToJson(configTypeDto.getSmsConfigJson()));
        configurationType.setPushNotificationConfigJson(ImtechoUtil.convertObjectToJson(configTypeDto.getPushNotificationConfigJson()));
        return configurationType;
    }

    /**
     * Convert mobile event configuration details into entity.
     * @param mobileNotificationConfigDto Event configuration details.
     * @return Returns entity of event configuration.
     */
    public static MobileEventConfiguration convertToMobileNotificationConfigMaster(MobileEventConfigDto mobileNotificationConfigDto) {
        MobileEventConfiguration mobileNotificationConfiguration = new MobileEventConfiguration();
        mobileNotificationConfiguration.setId(mobileNotificationConfigDto.getId() == null ? UUID.randomUUID().toString() : mobileNotificationConfigDto.getId());
        mobileNotificationConfiguration.setNotificationCode(mobileNotificationConfigDto.getNotificationCode());
        mobileNotificationConfiguration.setNumberOfDaysAddedForDueDate(mobileNotificationConfigDto.getNumberOfDaysAddedForDueDate());
        mobileNotificationConfiguration.setNumberOfDaysAddedForExpiryDate(mobileNotificationConfigDto.getNumberOfDaysAddedForExpiryDate());
        mobileNotificationConfiguration.setNumberOfDaysAddedForOnDate(mobileNotificationConfigDto.getNumberOfDaysAddedForOnDate());
        mobileNotificationConfiguration.setNotificationTypeConfigId(mobileNotificationConfigDto.getNotificationTypeConfigId());
        return mobileNotificationConfiguration;
    }

    /**
     * Convert event config type entity into dto.
     * @param notificationConfigurationType Entity of event config type.
     * @return Returns event config details.
     */
    public static EventConfigTypeDto convertToConfigurationTypeDto(EventConfigurationType notificationConfigurationType) {
        EventConfigTypeDto notificationConfigTypeDto = new EventConfigTypeDto();
        notificationConfigTypeDto.setDay(notificationConfigurationType.getDay());
        notificationConfigTypeDto.setBaseDateFieldName(notificationConfigurationType.getBaseDateFieldName());
        notificationConfigTypeDto.setUserFieldName(notificationConfigurationType.getUserFieldName());
        notificationConfigTypeDto.setFamilyFieldName(notificationConfigurationType.getFamilyFieldName());
        notificationConfigTypeDto.setMemberFieldName(notificationConfigurationType.getMemberFieldName());
        notificationConfigTypeDto.setEmailSubject(notificationConfigurationType.getEmailSubject());
        notificationConfigTypeDto.setEmailSubjectParameter(notificationConfigurationType.getEmailSubjectParameter());
        notificationConfigTypeDto.setHour(notificationConfigurationType.getHour());
        notificationConfigTypeDto.setMiniute(notificationConfigurationType.getMinute());
        notificationConfigTypeDto.setMobileNotificationType(notificationConfigurationType.getMobileNotificationType());
        notificationConfigTypeDto.setTemplateParameter(notificationConfigurationType.getTemplateParameter());
        notificationConfigTypeDto.setId(notificationConfigurationType.getId() == null ? UUID.randomUUID().toString() : notificationConfigurationType.getId());
        notificationConfigTypeDto.setTemplate(notificationConfigurationType.getTemplate());
        notificationConfigTypeDto.setTrigerWhen(notificationConfigurationType.getTrigerWhen());
        notificationConfigTypeDto.setType(notificationConfigurationType.getType());
        notificationConfigTypeDto.setConfigId(notificationConfigurationType.getConfigId());

        ObjectMapper mapper = new ObjectMapper();
        PushNotificationConfigDto pushNotificationConfigDto = null;
        if (notificationConfigurationType.getPushNotificationConfigJson() != null) {
            try {
                pushNotificationConfigDto = mapper.readValue(notificationConfigurationType.getPushNotificationConfigJson(), new TypeReference<PushNotificationConfigDto>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        notificationConfigTypeDto.setPushNotificationConfigJson(pushNotificationConfigDto);
        notificationConfigTypeDto.setQueryCode(notificationConfigurationType.getQueryCode());
        notificationConfigTypeDto.setQueryMasterParamJson(ImtechoUtil.convertJsonToObject(notificationConfigurationType.getQueryMasterParamJson()));
        notificationConfigTypeDto.setSmsConfigJson(ImtechoUtil.convertJsonToObject(notificationConfigurationType.getSmsConfigJson()));
        return notificationConfigTypeDto;
    }

}
