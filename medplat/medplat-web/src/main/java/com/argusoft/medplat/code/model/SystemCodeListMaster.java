
package com.argusoft.medplat.code.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * Defines fields for system code list
 * </p>
 *
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */

@Entity
@Table(name = "system_code_master_list")
public class SystemCodeListMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @Column(name = "code_type")
    private String codeType;

    @Column(name = "code_category")
    private String codeCategory;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "code")
    private String code;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    @Column(name = "desc_type_id")
    private String descTypeId;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column(name = "other_details")
    private String otherDetails;

    @Column(name = "published_edition")
    private String publishedEdition;


    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * An util class for string constants of system code list
     */
    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String CODE_TYPE = "codeType";
        public static final String CODE_ID = "codeId";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String DESC_TYPE_ID = "descTypeId";
        public static final String PUBLISHED_EDITION = "publishedEdition";
        public static final String IS_ACTIVE = "isActive";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_ON = "modifiedOn";

    }

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


}
