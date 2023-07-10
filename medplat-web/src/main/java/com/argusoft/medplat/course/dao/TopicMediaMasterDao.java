
package com.argusoft.medplat.course.dao;

import com.argusoft.medplat.course.model.TopicMediaMaster;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;


/**
 * <p>
 * Define topic video dao
 * </p>
 *
 * @author sneha
 * @since 01/03/2021 18:38
 */
public interface TopicMediaMasterDao extends GenericDao<TopicMediaMaster, Integer> {

    /**
     * Returns topic video list by given topic Id
     *
     * @param topicId A topic Id
     * @return A list of topic video of given topic ids
     */
    List<TopicMediaMaster> getTopicMediaByTopicId(Integer topicId);
}
