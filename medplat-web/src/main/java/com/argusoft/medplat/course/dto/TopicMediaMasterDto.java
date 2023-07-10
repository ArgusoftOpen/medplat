
package com.argusoft.medplat.course.dto;

import com.argusoft.medplat.course.model.TopicMediaMaster;
import lombok.Data;

/**
 * <p>
 * A TopicMasterDto object used as Data transfer Object
 * </p>
 *
 * @author sneha
 * @since 01/03/2021 18:38
 */
@Data
public class TopicMediaMasterDto {

    private Integer id;
    private Integer topicId;
    private Long mediaId;
    private String mediaFileName;
    private TopicMediaMaster.MediaType mediaType;
    private Boolean isUserFeedbackRequired;
    private Integer mediaOrder;
    private Long transcriptFileId;
    private String transcriptFileName;
    private String url;
    private String title;
    private String description;
    private TopicMediaMaster.State mediaState;
    private Long size;

}
