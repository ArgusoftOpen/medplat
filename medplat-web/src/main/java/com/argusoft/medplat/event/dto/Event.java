/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dto;

/**
 *
 * <p>
 *     Used for event.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
public class Event {

    protected EVENT_TYPE eType;
    private Integer eventConfigId;
    protected ResourceDto resourceDto;

    public Event(EVENT_TYPE eType, Integer eventConfigId, String resourseType, Integer resourseId) {
        this.eType = eType;
        this.eventConfigId = eventConfigId;
        this.resourceDto = new ResourceDto(resourseType, resourseId);
    }

    public EVENT_TYPE geteType() {
        return eType;
    }

    public void seteType(EVENT_TYPE eType) {
        this.eType = eType;
    }

    public ResourceDto getResourceDto() {
        return resourceDto;
    }

    public void setResourceDto(ResourceDto resourceDto) {
        this.resourceDto = resourceDto;
    }

    public Integer getEventConfigId() {
        return eventConfigId;
    }

    public void setEventConfigId(Integer eventConfigId) {
        this.eventConfigId = eventConfigId;
    }

    public enum EventGroup {

        FORM,
        TIMER,
        MANUAL

    }

    public enum EVENT_TYPE {

        FORM_SUBMITTED("FORM_SUBMITTED", EventGroup.FORM),
        TIMER_EVENT("TIMER_EVENT", EventGroup.TIMER),
        MANUAL("MANUAL", EventGroup.MANUAL);

        private String name;
        private EventGroup group;

        EVENT_TYPE(String name, EventGroup group) {
            this.group = group;
            this.name = name;

        }

        public String getName() {
            return name;
        }

        public EventGroup getGroup() {
            return group;
        }

    }

}
