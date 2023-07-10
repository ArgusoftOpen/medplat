package com.argusoft.medplat.training.view;

/**
 *
 * <p>
 *     Define view for pending status display.
 * </p>
 * @author shrey
 * @since 26/08/20 11:00 AM
 *
 */
public class PendingStatusDisplayObject {

    private String courseName;
    private String role;
    private Integer total;
    private Integer practicing;
    private Integer scheduled;
    private Integer production;
    private Integer roleId;
    private Integer id;
    private Integer pending;

    public PendingStatusDisplayObject() {
        // Do nothing
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPracticing() {
        return practicing;
    }

    public void setPracticing(Integer practicing) {
        this.practicing = practicing;
    }

    public Integer getScheduled() {
        return scheduled;
    }

    public void setScheduled(Integer scheduled) {
        this.scheduled = scheduled;
    }

    public Integer getProduction() {
        return production;
    }

    public void setProduction(Integer  production) {
        this.production = production;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }
}
