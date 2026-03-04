/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.TopicMasterDao;
import com.argusoft.medplat.course.model.TopicMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.TopicDataBean;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author akshar
 */
@Repository("topicMasterDao")
public class TopicMasterDaoImpl extends GenericDaoImpl<TopicMaster, Integer> implements TopicMasterDao {

    @Override
    public List<TopicMaster> getTopicByIds(List<Integer> topicIds) {

        var session = getCurrentSession();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<TopicMaster> cq = cb.createQuery(TopicMaster.class);
        Root<TopicMaster> root = cq.from(TopicMaster.class);
        cq.select(root).where(
                cb.in(root.get(TopicMaster.Fields.TOPIC_ID)).value(topicIds)
        );
        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    public List<TopicDataBean> getTopicDataBeanByCourseId(Integer courseId) {
        String query = "select ttm.topic_id as \"topicId\", tctr.course_id as \"courseId\", ttm.topic_name as \"topicName\",\n" +
                "\tttm.topic_description as \"topicDescription\",\n" +
                "\tttm.topic_order as \"topicOrder\", ttm.topic_state as \"topicState\" \n" +
                "from tr_topic_master ttm \n" +
                "inner join tr_course_topic_rel tctr on ttm.topic_id = tctr.topic_id\n" +
                "where tctr.course_id = :courseId and ttm.topic_state = 'ACTIVE';";

        NativeQuery<TopicDataBean> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("courseId", courseId)
                .addScalar("courseId", StandardBasicTypes.INTEGER)
                .addScalar("topicId", StandardBasicTypes.INTEGER)
                .addScalar("topicName", StandardBasicTypes.STRING)
                .addScalar("topicDescription", StandardBasicTypes.STRING)
                .addScalar("topicOrder", StandardBasicTypes.STRING)
                .addScalar("topicState", StandardBasicTypes.STRING);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(TopicDataBean.class)).list();

    }

}
