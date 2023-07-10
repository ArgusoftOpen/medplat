package com.argusoft.medplat.event.dto;

import lombok.Data;

/**
 *
 * <p>
 *     Used for resource.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Data
public class ResourceDto {

    protected String type;
    protected Integer id;
    protected Integer resourceDetailId;
    private DynamicResourceDto dynamicResourceDto;

    public ResourceDto() {
    }

    protected ResourceDto(String type, Integer resourceDetailId) {
        this.type = type;
        this.resourceDetailId = resourceDetailId;
    }

    protected ResourceDto(String type, int id, Integer resourceDetailId) {
        this.type = type;
        this.id = id;
        this.resourceDetailId = resourceDetailId;
    }

}
