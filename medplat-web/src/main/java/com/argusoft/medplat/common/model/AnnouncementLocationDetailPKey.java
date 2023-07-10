/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * <p>Defines fields related to user</p>
 *
 * @author smeet
 * @since 26/08/2020 5:30
 */
@Embeddable
public class AnnouncementLocationDetailPKey implements Serializable {

    @Basic(optional = false)
    @Column(name = "announcement")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "location")
    private Integer location;

    @Basic(optional = false)
    @Column(name = "announcement_for")
    private String announcementFor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getAnnouncementFor() {
        return announcementFor;
    }

    public void setAnnouncementFor(String announcementFor) {
        this.announcementFor = announcementFor;
    }

    @Override
    public String toString() {
        return "AnnouncementLocationDetailPKey{" +
                "id=" + id +
                ", location=" + location +
                ", announcementFor='" + announcementFor + '\'' +
                '}';
    }
}
