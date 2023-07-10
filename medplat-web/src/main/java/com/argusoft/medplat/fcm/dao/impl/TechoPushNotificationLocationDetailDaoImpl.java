/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fcm.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationLocationDetailDao;
import com.argusoft.medplat.fcm.model.TechoPushNotificationLocationDetail;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author nihar
 */
@Repository
@Transactional
public class TechoPushNotificationLocationDetailDaoImpl extends
        GenericDaoImpl<TechoPushNotificationLocationDetail, Integer>
        implements TechoPushNotificationLocationDetailDao {


    @Override
    public List<TechoPushNotificationLocationDetail> findByNotificationConfigId(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(TechoPushNotificationLocationDetail.class);
        criteria.add(Restrictions.eq(TechoPushNotificationLocationDetail.Fields.PUSH_CONFIG_ID, id));
        List<TechoPushNotificationLocationDetail> locationDetails = (List) criteria.list();
        return locationDetails;
    }

    @Override
    public void deleteByConfigId(Integer id) {
        String query = "delete from techo_push_notification_location_detail " +
                "where push_config_id = :id";
        Session session = sessionFactory.getCurrentSession();
        SQLQuery q = session.createSQLQuery(query);
        q.setParameter("id", id);
        q.executeUpdate();
    }
}
