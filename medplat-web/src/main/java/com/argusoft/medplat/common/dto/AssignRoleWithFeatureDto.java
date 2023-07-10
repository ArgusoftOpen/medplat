package com.argusoft.medplat.common.dto;

/**
 *
 * <p>
 * Assign feature to role dto
 * </p>
 *
 * @author monika
 * @since 26/11/20 5:13 PM
 */
public class AssignRoleWithFeatureDto {
    private String featureName;
    private Integer userMenuItemId;
    private String description;
    private Integer featureId;

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Integer getUserMenuItemId() {
        return userMenuItemId;
    }

    public void setUserMenuItemId(Integer userMenuItemId) {
        this.userMenuItemId = userMenuItemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }
}
