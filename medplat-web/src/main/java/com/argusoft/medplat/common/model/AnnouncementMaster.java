/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *<p>Defines fields related to user</p>
 * @author smeet
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "announcement_info_master")
public class AnnouncementMaster extends EntityAuditInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "from_date", nullable = false)
    private Date fromDate;
    
    @Column(name="default_language")
    private String defaultLanguage;
    
    @Column(name="contains_multimedia")
    private Boolean containsMultimedia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Boolean getContainsMultimedia() {
        return containsMultimedia;
    }

    public void setContainsMultimedia(Boolean containsMultimedia) {
        this.containsMultimedia = containsMultimedia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AnnouncementMaster{" + "id=" + id + ", subject=" + subject + ", isActive=" + isActive + ", fromDate=" + fromDate + ", defaultLanguage=" + defaultLanguage + ", containsMultimedia=" + containsMultimedia + '}';
    }
    
}
