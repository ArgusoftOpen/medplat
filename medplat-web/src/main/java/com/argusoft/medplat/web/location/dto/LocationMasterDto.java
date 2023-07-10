/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dto;

/**
 *
 * <p>
 *     Used for location master details.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
public class LocationMasterDto {

    private Integer id;
    private String name;
    private String type;
    private Integer level;
    private Integer parent;
    private Long locationCode;
    private String locationHierarchy;
    private String locationFlag;
    private Boolean containsCmtcCenter;
    private Boolean containsNrcCenter;
    private Boolean cerebralPalsyModule;
    private Boolean geoFencing;
    private String englishName;
    private String lgdCode;
    private String mddsCode;
    private Boolean isTaaho;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Long getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Long locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationHierarchy() {
        return locationHierarchy;
    }

    public void setLocationHierarchy(String locationHierarchy) {
        this.locationHierarchy = locationHierarchy;
    }

    public String getLocationFlag() {
        return locationFlag;
    }

    public void setLocationFlag(String locationFlag) {
        this.locationFlag = locationFlag;
    }

    public Boolean getContainsCmtcCenter() {
        return containsCmtcCenter;
    }

    public void setContainsCmtcCenter(Boolean containsCmtcCenter) {
        this.containsCmtcCenter = containsCmtcCenter;
    }

    public Boolean getContainsNrcCenter() {
        return containsNrcCenter;
    }

    public void setContainsNrcCenter(Boolean containsNrcCenter) {
        this.containsNrcCenter = containsNrcCenter;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Boolean getCerebralPalsyModule() {
        return cerebralPalsyModule;
    }

    public void setCerebralPalsyModule(Boolean cerebralPalsyModule) {
        this.cerebralPalsyModule = cerebralPalsyModule;
    }

    public String getLgdCode() {
        return lgdCode;
    }

    public void setLgdCode(String lgdCode) {
        this.lgdCode = lgdCode;
    }

    public String getMddsCode() {
        return mddsCode;
    }

    public void setMddsCode(String mddsCode) {
        this.mddsCode = mddsCode;
    }

    public Boolean getGeoFencing() {
        return geoFencing;
    }

    public void setGeoFencing(Boolean geoFencing) {
        this.geoFencing = geoFencing;
    }

    public Boolean getIsTaaho() {
        return isTaaho;
    }

    public void setIsTaaho(Boolean isTaaho) {
        this.isTaaho = isTaaho;
    }
}
