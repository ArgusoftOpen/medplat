
package com.argusoft.medplat.code.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * <p>
 * Defines fields for system code
 * </p>
 *
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */

@Entity
@Table(name = "system_code_master")
public class SystemCodeMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "table_type")
    private String tableType;

    @Column(name = "code_type")
    private String codeType;

    @Column(name = "code")
    private String code;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "description")
    private String description;

    /**
     * An util class for string constants of system code
     */
    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String TABLE_ID = "tableId";
        public static final String TABLE_TYPE = "tableType";
        public static final String CODE_TYPE = "codeType";
        public static final String CODE = "code";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_ON = "modifiedOn";

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
