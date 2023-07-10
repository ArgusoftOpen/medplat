/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author harshit
 */
@Entity
@Table(name = "user_input_duration_detail")
@NamedQueries({
        @NamedQuery(name = "UserInputDuration.findAll", query = "SELECT u FROM UserInputDuration u")})
public class UserInputDuration implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "duration")
    private long duration;
    @Basic(optional = false)
    @Column(name = "on_date")
    @Temporal(TemporalType.DATE)
    private Date onDate;
    @Basic(optional = false)
    @Column(name = "by_user")
    private int byUser;
    @Basic(optional = false)
    @Column(name = "form_type", nullable = false, length = 15)
    private String formType;
    @Basic(optional = false)
    @Column(name = "related_id")
    private int relatedId;
    @Basic(optional = false)
    @Column(name = "is_active")
    private boolean isActive;
    //for mobile date form entry time
    @Column(name = "mobile_created_on_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileCreatedOnDate; // form end date
    @Column(name = "beneficiaryid")
    private Integer beneficiaryId;
    @Column(name = "is_child")
    private Boolean isChild;
    //for form entry
    @Column(name = "form_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date formStartDate;

    public Date getFormStartDate() {
        return formStartDate;
    }

    public void setFormStartDate(Date formStartDate) {
        this.formStartDate = formStartDate;
    }

    public UserInputDuration() {
    }

    public UserInputDuration(Integer id) {
        this.id = id;
    }

    public UserInputDuration(Integer id, int duration, Date onDate, int byUser, String formType, int relatedId, boolean isActive) {
        this.id = id;
        this.duration = duration;
        this.onDate = onDate;
        this.byUser = byUser;
        this.formType = formType;
        this.relatedId = relatedId;
        this.isActive = isActive;
    }

    public Date getMobileCreatedOnDate() {
        return mobileCreatedOnDate;
    }

    public void setMobileCreatedOnDate(Date mobileCreatedOnDate) {
        this.mobileCreatedOnDate = mobileCreatedOnDate;
    }

    public Integer getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Integer beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Boolean getIsChild() {
        return isChild;
    }

    public void setIsChild(Boolean isChild) {
        this.isChild = isChild;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public int getByUser() {
        return byUser;
    }

    public void setByUser(int byUser) {
        this.byUser = byUser;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserInputDuration)) {
            return false;
        }
        UserInputDuration other = (UserInputDuration) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.argusoft.sewa.model.UserInputDurationDetail[ id=" + id + " ]";
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String ID = "id";
        public static final String DURATION = "duration";
        public static final String ON_DATE = "onDate";
        public static final String BY_USER = "byUser";
        public static final String FORM_TYPE = "formType";
        public static final String RELATED_ID = "relatedId";
        public static final String IS_ACTIVE = "isActive";
        public static final String MOBILE_CREATED_ON_DATE = "mobileCreatedOnDate";
        public static final String BENEFICIARY_ID = "beneficiaryId";
        public static final String IS_CHILD = "isChild";
        public static final String FORM_START_DATE = "formStartDate";
    }
}
