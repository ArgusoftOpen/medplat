/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;

/**
 *<p>Defines fields related to user</p>
 * @author smeet
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "announcement_info_detail")
public class AnnouncementInfoDetail {

    @EmbeddedId
    private AnnouncementInfoDetailPKey announcementInfoDetailPKey;

    @Column(name = "content")
    private byte[] content;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "media_path")
    private String mediaPath;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public AnnouncementInfoDetailPKey getAnnouncementInfoDetailPKey() {
        return announcementInfoDetailPKey;
    }

    public void setAnnouncementInfoDetailPKey(AnnouncementInfoDetailPKey announcementInfoDetailPKey) {
        this.announcementInfoDetailPKey = announcementInfoDetailPKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AnnouncementInfoDetail{" + "announcementInfoDetailPKey=" + announcementInfoDetailPKey + ", content=" + Arrays.toString(content) + ", fileExtension=" + fileExtension + ", mediaPath=" + mediaPath + '}';
    }

}
