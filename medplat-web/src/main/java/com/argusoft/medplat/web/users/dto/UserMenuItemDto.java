package com.argusoft.medplat.web.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Defines fields related to user menu item</p>
 * @author charmi
 * @since 26/08/2020 5:30
 */

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMenuItemDto {
    
    private Integer id;
    private Integer userId;
    private Integer designationId;
    private Integer groupId;
    private Integer menuConfigId;
    private String featureJson;
    private String fullName;
    private String roleName;
    
}
