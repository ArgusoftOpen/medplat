
package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.TopicMediaMasterDao;
import com.argusoft.medplat.course.model.TopicMediaMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 * Define Database logic for topic video
 * </p>
 *
 * @author sneha
 * @since 01/03/2021 18:38
 */
@Repository
public class TopicMediaMasterDaoImpl extends GenericDaoImpl<TopicMediaMaster, Integer> implements TopicMediaMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicMediaMaster> getTopicMediaByTopicId(Integer topicId) {

        var session = getCurrentSession();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<TopicMediaMaster> cq = cb.createQuery(TopicMediaMaster.class);
        Root<TopicMediaMaster> root = cq.from(TopicMediaMaster.class);
        cq.select(root).where(
                cb.and(cb.equal(root.get(TopicMediaMaster.Fields.TOPIC_ID), topicId),
                        cb.equal(root.get(TopicMediaMaster.Fields.MEDIA_STATE), TopicMediaMaster.State.ACTIVE))
        );
        return session.createQuery(cq).getResultList();
    }
}
