
package com.argusoft.medplat.course.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * <p>
 * Define tr_topic_video_master entity and its fields.
 * </p>
 *
 * @author sneha
 * @since 01/03/2021 18:38
 */
@Entity
@Table(name = "tr_topic_media_master")
@Data
public class TopicMediaMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "topic_id", nullable = false)
    private Integer topicId;
    @Column(name = "media_id")
    private Long mediaId;
    @Column(name = "media_file_name")
    private String mediaFileName;
    @Column(name = "media_order")
    private Integer mediaOrder;
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType mediaType;
    @Column(name = "transcript_file_id")
    private Long transcriptFileId;
    @Column(name = "transcript_file_name")
    private String transcriptFileName;
    @Column(name = "url")
    private String url;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "is_user_feedback_required")
    private Boolean isUserFeedbackRequired;
    @Enumerated(EnumType.STRING)
    @Column(name = "media_state")
    private State mediaState;

    @Column(name="size")
    private Long size;

    public enum MediaType {
        PDF,
        VIDEO,
        AUDIO,
        IMAGE
    }

    public enum State {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String TOPIC_ID = "topicId";
        public static final String MEDIA_STATE = "mediaState";
    }

}
