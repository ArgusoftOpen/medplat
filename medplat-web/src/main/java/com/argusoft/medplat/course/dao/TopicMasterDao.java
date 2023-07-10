/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.dao;

import com.argusoft.medplat.course.model.TopicMaster;
import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.TopicDataBean;

import java.util.List;

/**
 * @author akshar
 */
public interface TopicMasterDao extends GenericDao<TopicMaster, Integer> {

    /**
     * @param topicIds
     * @return
     */
    public List<TopicMaster> getTopicByIds(List<Integer> topicIds);

    /**
     * Returns topic dataBeans by courseId
     *
     * @param courseId An instance of courseId
     * @return A list of topic dataBeans
     */
    List<TopicDataBean> getTopicDataBeanByCourseId(Integer courseId);

}
