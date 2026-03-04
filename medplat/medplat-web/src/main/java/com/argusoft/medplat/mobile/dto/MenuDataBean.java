package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 * <p>
 *     MenuDataBean dto
 * </p>
 *
 * @author rahul
 * @since 13/01/21 5:38 PM
 */
public class MenuDataBean {

    private Integer actualId;

    private int order;

    private String constant;

    private String displayName;

    private Date modifiedOn;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
