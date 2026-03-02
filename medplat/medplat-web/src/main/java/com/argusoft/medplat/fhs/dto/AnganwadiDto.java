package com.argusoft.medplat.fhs.dto;

/**
 *
 * <p>
 *     Used for anganwadi.
 * </p>
 * @author shrey
 * @since 26/08/20 11:00 AM
 *
 */
public class AnganwadiDto {

    private Integer id;
    private String name;
    private Integer parent;
    private String type;
    private String state;
    private String emamtaId;
    private String aliasName;
    private String location;
    private String icdsCode;

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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmamtaId() {
        return emamtaId;
    }

    public void setEmamtaId(String emamtaId) {
        this.emamtaId = emamtaId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIcdsCode() {
        return icdsCode;
    }

    public void setIcdsCode(String icdsCode) {
        this.icdsCode = icdsCode;
    }
        
}
