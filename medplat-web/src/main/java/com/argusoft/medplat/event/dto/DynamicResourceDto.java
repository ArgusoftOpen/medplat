package com.argusoft.medplat.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 *     Used for dynamic resource.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicResourceDto {

    private Integer resTypeId;
    //id of dynamically generated table.
    private Integer resourceId;
    //id of newly generated row in dynamically generated tables for update purpose.
    private Integer newResourceId;
    private String resourceName;
    private Map<String, List<String>> compositeUniqueKey;
    // Below map contains column name as key and data type as value.
    private Map<String, String> resourceFieldNameDataType;
    //Below map contains column name as key and its value to stor in database value.
    private Map<String, Object> insertionValues;
    private Integer createdBy;
    private Integer updatedBy;

    public DynamicResourceDto(String resourceDisplayName, Map<String, String> resourceFieldNameType) {
        this.resourceName = resourceDisplayName;
        this.resourceFieldNameDataType = resourceFieldNameType;
    }

    public DynamicResourceDto(String resourceDisplayName, Map<String, String> resourceFieldNameType, Map<String, Object> insertionValues, Integer newResourceId) {
        this.resourceName = resourceDisplayName;
        this.resourceFieldNameDataType = resourceFieldNameType;
        this.insertionValues = insertionValues;
        this.newResourceId = newResourceId;
    }

}
