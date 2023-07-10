package com.argusoft.medplat.code.dto;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *     Defines fields for system code list
 * </p>
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */
public class SystemCodeListDto {

    private UUID id;
    private String codeType;
    private String codeCategory;
    private String codeId;
    private String code;
    private String parentCode;
    private String name;
    private String description;
    private String descTypeId;
    private Date effectiveDate;
    private String otherDetails;
    private String publishedEdition;
    private String languageCode;
    private Boolean isActive;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeCategory() {
        return codeCategory;
    }

    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescTypeId() {
        return descTypeId;
    }

    public void setDescTypeId(String descTypeId) {
        this.descTypeId = descTypeId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getPublishedEdition() {
        return publishedEdition;
    }

    public void setPublishedEdition(String publishedEdition) {
        this.publishedEdition = publishedEdition;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    
    
    

}
